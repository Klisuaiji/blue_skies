package com.legacy.blue_skies.capability;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.blocks.dungeon.KeystoneBlock;
import com.legacy.blue_skies.entities.SupporterPetEntity;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.items.arcs.IArcItem;
import com.legacy.blue_skies.items.tools.weapons.AlchemyScrollItem;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.DisplayToastPacket;
import com.legacy.blue_skies.network.s_to_c.DungeonMusicPacket;
import com.legacy.blue_skies.network.s_to_c.SyncPlayerPacket;
import com.legacy.blue_skies.network.s_to_c.UpdateBlueLorePacket;
import com.legacy.blue_skies.network.s_to_c.UpdateNatureHealthPacket;
import com.legacy.blue_skies.registries.SkiesAttachmentTypes;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.util.EntityUtil;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class SkiesPlayer {
   private static final Marker MARKER = MarkerManager.getMarker("Player Capability");
   private Player player;
   private SkiesDungeonType activeBossType;
   private ArcInventory arcInventory = new ArcInventory();
   public SkiesDungeonType dungeonMusicType;
   private boolean wasInStructure = false;
   private boolean supporterPetEnabled = false;
   private boolean supporterPetAudible = false;
   private boolean usedBlueLore = false;
   private boolean openedBlueLore = false;
   private boolean fullDuskInvis = false;
   private byte brightProgression = 0;
   private byte dawnProgression = 0;
   private byte supporterPetId = 0;
   private byte supporterStyleId = 0;
   private float natureHealth;
   private Vec3 preservedMotionVec;
   private LivingEntity supporterPet;
   private String supporterPetName = "";
   private BlockPos prevKeystonePos = null;

   public SkiesPlayer(Player player) {
      this.player = player;
      this.arcInventory.refreshArcs(player);
   }

   @Nullable
   public static SkiesPlayer get(Player player) {
      return player != null && !(player instanceof FakePlayer) ? (SkiesPlayer)player.getData(SkiesAttachmentTypes.PLAYER_ATTACHMENT) : null;
   }

   public static <E extends Player> void ifPresent(E entity, Consumer<SkiesPlayer> action) {
      SkiesPlayer cap = get(entity);
      if (cap != null) {
         action.accept(cap);
      }
   }

   public static <E extends Player> void ifPresent(E entity, Consumer<SkiesPlayer> action, Runnable elseAction) {
      SkiesPlayer cap = get(entity);
      if (cap != null) {
         action.accept(cap);
      } else {
         elseAction.run();
      }
   }

   @Nullable
   public static <E extends Player, R> R getIfPresent(E entity, Function<SkiesPlayer, R> action, Supplier<R> elseAction) {
      SkiesPlayer cap = get(entity);
      return cap != null ? action.apply(cap) : elseAction.get();
   }

   public void serverTick() {
      if (this.player != null && this.player instanceof ServerPlayer && !this.player.getCommandSenderWorld().isClientSide()) {
         this.getArcInventory().getStacks().forEach(stack -> {
            if (stack.getItem() instanceof IArcItem) {
               ((IArcItem)stack.getItem()).serverTick(stack, (ServerPlayer)this.player);
            }
         });
         this.updateDungeonMusic();
         if (!this.player.isCreative()
            && !this.player.isSpectator()
            && this.player.tickCount % 40 == 0
            && (
               this.getBrightProgression() < 1
                     && EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.NATURE_DUNGEON.getStructure())
                  || this.getDawnProgression() < 1
                     && this.player.blockPosition().getY() < 63
                     && EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.POISON_DUNGEON.getStructure())
            )) {
            this.player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.not_progressed"), true);
            this.player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80));
            this.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
            this.player.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 1));
         }

         if (BlueSkies.ML_SUPPORTER.getRank(this.player).hasPerks()) {
            if (this.isSupporterPetEnabled()) {
               this.checkAndSpawnPet();
               if (this.getSupporterPet() != null && !this.getSupporterPet().level().dimension().location().equals(this.player.level().dimension().location())) {
                  this.getSupporterPet().discard();
                  this.setSupporterPet(null);
               }
            } else if (this.getSupporterPet() != null) {
               this.getSupporterPet().discard();
               this.setSupporterPet(null);
            }
         }

         if (this.player.getUseItem().getItem() instanceof AlchemyScrollItem && this.player.tickCount % 5 == 0) {
            AlchemyScrollItem.beginSpikeSpawning(this.player, false, 8);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void clientTick() {
      if (this.player != null && this.player instanceof LocalPlayer && this.player.getCommandSenderWorld().isClientSide()) {
         this.getArcInventory().getStacks().forEach(stack -> {
            if (stack.getItem() instanceof IArcItem) {
               ((IArcItem)stack.getItem()).clientTick(stack, (LocalPlayer)this.player);
            }
         });
      }
   }

   public void syncDataToClient() {
      if (this.getPlayer() instanceof ServerPlayer) {
         PacketHandler.sendToClient(new SyncPlayerPacket(this), (ServerPlayer)this.getPlayer());
      }
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setBossType(SkiesDungeonType bossType) {
      this.activeBossType = bossType;
   }

   public SkiesDungeonType getBossType() {
      return this.activeBossType;
   }

   public void setDungeonMusicId(SkiesDungeonType musicType) {
      this.dungeonMusicType = musicType;
   }

   public SkiesDungeonType getDungeonMusicId() {
      return this.dungeonMusicType;
   }

   public ArcInventory getArcInventory() {
      return this.arcInventory;
   }

   public void onDeath() {
      this.player
         .level()
         .getEntitiesOfClass(Mob.class, this.player.getBoundingBox().inflate(30.0), ISkyBoss.IS_BOSS)
         .forEach(
            boss -> {
               List<Player> players = this.player
                  .level()
                  .getEntitiesOfClass(
                     Player.class,
                     this.player.getBoundingBox().inflate(30.0),
                     entity -> entity.isAlive() && entity.getHealth() > 0.0F && !entity.isCreative() && !entity.isSpectator()
                  );
               boolean playersAlive = players.size() > 0;
               if (playersAlive) {
                  boss.heal(((ISkyBoss)boss).getHealAmount());
               } else if (this.player.level().getDifficulty() == Difficulty.EASY) {
                  boss.heal(((ISkyBoss)boss).getHealAmount() * 1.5F);
               } else {
                  boss.heal(boss.getMaxHealth());
               }
            }
         );
   }

   public void dropItems(Collection<ItemEntity> drops) {
      if (!this.player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
         for (int i = 0; i < this.arcInventory.getContainerSize(); i++) {
            ItemStack stack = this.arcInventory.getItem(i);
            if (!stack.isEmpty()) {
               if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, stack) == 0) {
                  drops.add(this.player.drop(stack, true, false));
               }

               this.arcInventory.removeItemNoUpdate(i);
            }
         }
      }

      this.syncDataToClient();
   }

   private void updateDungeonMusic() {
      boolean inDungeon = EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.NATURE_DUNGEON.getStructure())
         || EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.POISON_DUNGEON.getStructure())
         || EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getStructure())
         || EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getStructure());
      if (this.wasInStructure != inDungeon) {
         SkiesDungeonType dungeonType;
         if (EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getStructure())
            || EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getStructure())) {
            dungeonType = SkiesDungeonType.BLINDING;
         } else if (EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.NATURE_DUNGEON.getStructure())) {
            dungeonType = SkiesDungeonType.NATURE;
         } else if (EntityUtil.isPosInStructure(this.player.level(), this.player.blockPosition(), SkiesStructures.POISON_DUNGEON.getStructure())) {
            dungeonType = SkiesDungeonType.POISON;
         } else {
            dungeonType = SkiesDungeonType.NONE;
         }

         PacketHandler.sendToClient(new DungeonMusicPacket((byte)dungeonType.getId()), (ServerPlayer)this.player);
         this.wasInStructure = inDungeon;
      }
   }

   public void teleportToNearestKeystone() {
      if (this.player.level() instanceof ServerLevel) {
         BlockPos playerPos = this.player.blockPosition();
         byte distance = 30;
         ServerLevel world = (ServerLevel)this.player.level();
         PoiManager pointofinterestmanager = world.getPoiManager();
         pointofinterestmanager.ensureLoadedAndValid(this.player.level(), playerPos, distance);
         Optional<PoiRecord> optional = world.getPoiManager()
            .getInSquare(poiType -> poiType.is(SkiesPoiTypes.KEYSTONE.getKey()), playerPos, distance, Occupancy.ANY)
            .findFirst();
         if (optional.isPresent()) {
            BlockState keystoneState = world.getBlockState(optional.get().getPos());
            BlockPos spawnPos = keystoneState.hasProperty(KeystoneBlock.DirectionalKeystoneBlock.FACING)
               ? optional.get().getPos().relative((Direction)keystoneState.getValue(KeystoneBlock.DirectionalKeystoneBlock.FACING), 2)
               : optional.get().getPos().relative(Direction.NORTH, 3);
            this.player.setPos(spawnPos.getX() + 0.5F, spawnPos.getY(), spawnPos.getZ() + 0.5F);
            if (this.player instanceof ServerPlayer) {
               PacketHandler.sendToClient(new DisplayToastPacket((byte)1), (ServerPlayer)this.player);
            }
         }
      }
   }

   public void checkAndSpawnPet() {
      if (this.isSupporterPetEnabled() && this.getSupporterPet() == null && BlueSkies.ML_SUPPORTER.getRank(this.player).hasPerks()) {
         try {
            SupporterPetEntity pet = createPet(this.player.level());
            pet.setTame(true, true);
            pet.setOwnerUUID(this.player.getUUID());
            this.setSupporterPet(pet);
            pet.setTypeID((byte)Math.max(0, this.getSupporterPetID()));
            pet.setPos(this.player.getX(), this.player.getY(), this.player.getZ());
            pet.setSilent(!this.isSupporterPetAudible());
            if (this.getSupporterPetName() != null && !pet.hasCustomName() && this.getSupporterPetName().length() > 0) {
               pet.setCustomNameVisible(true);
               pet.setCustomName(Component.literal(this.getSupporterPetName()).withStyle(ChatFormatting.values()[Math.max(0, this.supporterStyleId)]));
            }

            this.player.level().addFreshEntity(pet);
         } catch (NullPointerException e) {
            BlueSkies.LOGGER.error(MARKER, "Something went wrong trying to spawn a supporter pet for {}!", this.player.getName().getString(), e);
         }
      }
   }

   private static SupporterPetEntity createPet(Level level) {
      return Objects.requireNonNull((SupporterPetEntity)SkiesEntityTypes.SUPPORTER_PET.create(level));
   }

   public byte getBrightProgression() {
      return this.brightProgression;
   }

   public byte getDawnProgression() {
      return this.dawnProgression;
   }

   public byte getSupporterPetID() {
      return this.supporterPetId;
   }

   public Vec3 getSavedClientMotion() {
      return this.preservedMotionVec;
   }

   public LivingEntity getSupporterPet() {
      return this.supporterPet;
   }

   public boolean isSupporterPetEnabled() {
      return this.supporterPetEnabled;
   }

   public boolean isSupporterPetAudible() {
      return this.supporterPetAudible;
   }

   public String getSupporterPetName() {
      return this.supporterPetName;
   }

   public boolean hasUsedBlueLore() {
      return this.usedBlueLore;
   }

   public boolean hasOpenedBlueLore() {
      return this.openedBlueLore;
   }

   public void setBrightProgression(byte bossNumber) {
      this.brightProgression = bossNumber;
   }

   public void setDawnProgression(byte bossNumber) {
      this.dawnProgression = bossNumber;
   }

   public void setSavedClientMotion(double motionX, double motionY, double motionZ) {
      this.preservedMotionVec = new Vec3(motionX, 0.0, motionZ);
   }

   public void setSupporterPetID(byte id) {
      this.supporterPetId = id;
   }

   public void setSupporterPetEnabled(boolean enabled) {
      this.supporterPetEnabled = enabled;
   }

   public void setSupporterPetAudible(boolean audible) {
      this.supporterPetAudible = audible;
   }

   public void setSupporterPetName(String name) {
      this.supporterPetName = name;
   }

   public void setSupporterPet(LivingEntity entity) {
      this.supporterPet = entity;
   }

   public byte getSupporterStyleID() {
      return this.supporterStyleId;
   }

   public void setSupporterStyleID(byte id) {
      this.supporterStyleId = id;
   }

   public void setUsedBlueLore(boolean used) {
      this.usedBlueLore = used;
      if (this.getPlayer() != null && this.getPlayer() instanceof ServerPlayer sp) {
         PacketHandler.sendToClient(new UpdateBlueLorePacket(used), sp);
      }
   }

   public void setOpenedBlueLore(boolean opened) {
      this.openedBlueLore = opened;
   }

   public boolean hasFullDuskInvis() {
      return this.fullDuskInvis;
   }

   public void setFullDuskInvis(boolean isInvisible) {
      this.fullDuskInvis = isInvisible;
   }

   public float getNatureHealth() {
      return this.natureHealth;
   }

   public void setNatureHealth(float value) {
      if (Float.isNaN(value)) {
         value = 0.0F;
      }

      this.natureHealth = Mth.clamp(value, 0.0F, 20.0F);
      if (this.getPlayer() != null && this.getPlayer() instanceof ServerPlayer) {
         PacketHandler.sendToClient(new UpdateNatureHealthPacket(value), (ServerPlayer)this.getPlayer());
      }
   }

   public void copyFrom(SkiesPlayer player) {
      this.arcInventory.copyFrom(player.getArcInventory());
      this.setBrightProgression(player.brightProgression);
      this.setDawnProgression(player.dawnProgression);
      this.setSupporterPetID(player.supporterPetId);
      this.setSupporterPetEnabled(player.supporterPetEnabled);
      this.setSupporterPetAudible(player.supporterPetAudible);
      this.setSupporterStyleID(player.supporterStyleId);
      this.usedBlueLore = player.usedBlueLore;
      this.setOpenedBlueLore(player.openedBlueLore);
      this.setFullDuskInvis(player.fullDuskInvis);
      this.natureHealth = player.natureHealth;
      this.prevKeystonePos = player.prevKeystonePos;
      this.setSupporterPetName(player.supporterPetName);
   }

   public static class Serializer implements IAttachmentSerializer<CompoundTag, SkiesPlayer> {
      public static final SkiesPlayer.Serializer INSTANCE = new SkiesPlayer.Serializer();

      public CompoundTag write(SkiesPlayer attachment, net.minecraft.core.HolderLookup.Provider provider) {
         CompoundTag compound = new CompoundTag();
         compound.put("ArcInventory", attachment.arcInventory.write(provider));
         compound.putByte("EverbrightProgression", attachment.getBrightProgression());
         compound.putByte("EverdawnProgression", attachment.getDawnProgression());
         compound.putByte("SupporterPetID", attachment.getSupporterPetID());
         compound.putBoolean("SupporterPetEnabled", attachment.isSupporterPetEnabled());
         compound.putBoolean("SupporterPetAudible", attachment.isSupporterPetAudible());
         compound.putByte("SupporterStyleID", attachment.getSupporterStyleID());
         compound.putBoolean("UsedBlueLore", attachment.hasUsedBlueLore());
         compound.putBoolean("OpenedBlueLore", attachment.hasOpenedBlueLore());
         compound.putBoolean("FullDuskInvis", attachment.hasFullDuskInvis());
         compound.putFloat("NatureHealth", attachment.getNatureHealth());
         if (attachment.prevKeystonePos != null) {
            compound.put("KeystoneTeleportPos", NbtUtils.writeBlockPos(attachment.prevKeystonePos));
         }

         if (attachment.getSupporterPetName().length() > 1) {
            compound.putString("SupporterPetName", attachment.getSupporterPetName());
         }

         return compound;
      }

      public SkiesPlayer read(IAttachmentHolder holder, CompoundTag compound, net.minecraft.core.HolderLookup.Provider provider) {
         SkiesPlayer attachment = new SkiesPlayer((Player)holder);
         attachment.arcInventory.read(provider, compound.getList("ArcInventory", 10));
         attachment.setBrightProgression(compound.getByte("EverbrightProgression"));
         attachment.setDawnProgression(compound.getByte("EverdawnProgression"));
         attachment.setSupporterPetID(compound.getByte("SupporterPetID"));
         attachment.setSupporterPetEnabled(compound.getBoolean("SupporterPetEnabled"));
         attachment.setSupporterPetAudible(compound.getBoolean("SupporterPetAudible"));
         attachment.setSupporterStyleID(compound.getByte("SupporterStyleID"));
         attachment.usedBlueLore = compound.getBoolean("UsedBlueLore");
         attachment.setOpenedBlueLore(compound.getBoolean("OpenedBlueLore"));
         attachment.setFullDuskInvis(compound.getBoolean("FullDuskInvis"));
         attachment.natureHealth = compound.getFloat("NatureHealth");
         attachment.prevKeystonePos = NbtUtils.readBlockPos(compound, "KeystoneTeleportPos").orElse(BlockPos.ZERO);
         if (compound.getString("SupporterPetName").length() > 1) {
            attachment.setSupporterPetName(compound.getString("SupporterPetName"));
         }

         return attachment;
      }
   }
}
