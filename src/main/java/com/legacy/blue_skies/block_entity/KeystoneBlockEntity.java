package com.legacy.blue_skies.block_entity;

import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class KeystoneBlockEntity extends SkiesBlockEntity {
   public static BlockPos posUp = new BlockPos(0, 1, 0);
   protected EntityType<?> containedMob;
   protected Item key;
   protected Item arc;
   protected boolean spawnsBoss = true;
   protected boolean keysRequired = true;
   protected BlockPos spawnOffset;
   protected BlockPos teleportOffset;

   public KeystoneBlockEntity(
      BlockEntityType<?> type,
      BlockPos tilePos,
      BlockState tileState,
      EntityType<?> containedMob,
      Item key,
      Item arc,
      BlockPos spawnOffset,
      BlockPos teleportOffset
   ) {
      super(type, tilePos, tileState);
      this.containedMob = containedMob;
      this.key = key;
      this.arc = arc;
      this.spawnOffset = spawnOffset;
      this.teleportOffset = teleportOffset;
   }

   public void spawnMob(boolean spawnedFromArc) {
      if (this.level != null && !this.level.isClientSide && this.spawnsBoss && this.level instanceof ServerLevelAccessor) {
         BlockPos position = this.worldPosition.offset(this.spawnOffset);
         Entity mob = this.containedMob.create(this.level);
         if (mob instanceof ISkyBoss) {
            ((ISkyBoss)mob).setInvulTime(100);
            ((ISkyBoss)mob).setDungeonSpawned(true);
            ((ISkyBoss)mob).setHome(position);
            ((ISkyBoss)mob).setBossRespawned(spawnedFromArc);
            position = position.offset(((ISkyBoss)mob).bossSpawnOffset());
         }

         mob.setPos(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
         if (mob instanceof Mob) {
            ((Mob)mob)
               .finalizeSpawn(
                  (ServerLevelAccessor)this.level, this.level.getCurrentDifficultyAt(position), MobSpawnType.TRIGGERED, (SpawnGroupData)null, (CompoundTag)null
               );
         }

         this.level.addFreshEntity(mob);
      }
   }

   public void teleportClosePlayers(Level worldIn) {
      List<ServerPlayer> players = worldIn.getEntitiesOfClass(ServerPlayer.class, new AABB(this.worldPosition).inflate(7.0));
      players.forEach(player -> this.teleportPlayer(player));
   }

   public void teleportPlayer(Player player) {
      if (this.level != null && !this.level.isClientSide) {
         this.level
            .playSound(
               null,
               this.worldPosition.getX() + 0.5F,
               this.worldPosition.getY() + 0.5F,
               this.worldPosition.getZ() + 0.5F,
               SkiesSounds.BLOCK_KEYSTONE_TELEPORT,
               SoundSource.BLOCKS,
               0.5F,
               1.0F
            );
         BlockPos position = this.worldPosition.offset(this.teleportOffset);
         player.teleportTo(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
      }
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.containedMob = (EntityType<?>)BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(compound.getString("ContainedMob")));
      this.key = (Item)BuiltInRegistries.ITEM.get(new ResourceLocation(compound.getString("Key")));
      this.arc = (Item)BuiltInRegistries.ITEM.get(new ResourceLocation(compound.getString("Arc")));
      this.teleportOffset = compound.contains("TeleportOffset") ? NbtUtils.readBlockPos(compound.getCompound("TeleportOffset")) : BlockPos.ZERO;
      this.spawnOffset = compound.contains("SpawnOffset") ? NbtUtils.readBlockPos(compound.getCompound("SpawnOffset")) : BlockPos.ZERO;
      this.keysRequired = compound.getBoolean("KeysRequired");
      this.spawnsBoss = compound.getBoolean("SpawnsBoss");
   }

   public void saveAdditional(CompoundTag compound) {
      super.saveAdditional(compound);
      ResourceLocation mobKey;
      if ((mobKey = BuiltInRegistries.ENTITY_TYPE.getKey(this.containedMob)) != null) {
         compound.putString("ContainedMob", mobKey.toString());
      }

      compound.putString("Arc", BuiltInRegistries.ITEM.getKey(this.arc).toString());
      compound.putString("Key", BuiltInRegistries.ITEM.getKey(this.key).toString());
      compound.put("TeleportOffset", NbtUtils.writeBlockPos(this.teleportOffset));
      compound.put("SpawnOffset", NbtUtils.writeBlockPos(this.spawnOffset));
      compound.putBoolean("KeysRequired", this.keysRequired);
      compound.putBoolean("SpawnsBoss", this.spawnsBoss);
   }

   public void setContainedMob(EntityType<?> containedMob) {
      this.containedMob = containedMob;
      this.setChanged();
   }

   public BlockPos getTeleportOffset() {
      return this.teleportOffset;
   }

   public void setTeleportOffset(BlockPos teleportOffset) {
      this.teleportOffset = teleportOffset;
      this.setChanged();
   }

   public BlockPos getSpawnOffset() {
      return this.spawnOffset;
   }

   public void setSpawnOffset(BlockPos spawnOffset) {
      this.spawnOffset = spawnOffset;
      this.setChanged();
   }

   public void setKey(Item key) {
      this.key = key;
      this.setChanged();
   }

   public void setArc(Item arc) {
      this.arc = arc;
      this.setChanged();
   }

   public boolean getSpawnsBoss() {
      return this.spawnsBoss;
   }

   public void setSpawnsBoss(boolean spawnsBoss) {
      this.spawnsBoss = spawnsBoss;
      this.setChanged();
   }

   public boolean getKeysRequired() {
      return this.keysRequired;
   }

   public void setKeysRequired(boolean keysRequired) {
      this.keysRequired = keysRequired;
      this.setChanged();
   }

   public boolean isCorrectArc(Item item) {
      return item == this.arc;
   }

   public boolean isCorrectKey(ItemStack stack) {
      return stack.getItem() == this.key && stack.getCount() >= 4;
   }

   public int getBossRoomSize() {
      return 15;
   }

   public int getBossRoomCeilingSize() {
      return 0;
   }

   public boolean isEverbrightBased() {
      return true;
   }

   public int getRequiredProgressionId() {
      return 0;
   }

   public static class BrightBlindingKeystoneBlockEntity extends KeystoneBlockEntity {
      public BrightBlindingKeystoneBlockEntity(BlockPos pos, BlockState state) {
         super(
            SkiesBlockEntityTypes.BRIGHT_BLINDING_KEYSTONE,
            pos,
            state,
            SkiesEntityTypes.SUMMONER,
            SkiesItems.blinding_key,
            SkiesItems.ethereal_arc,
            posUp,
            posUp
         );
      }

      @Override
      public int getBossRoomSize() {
         return 20;
      }
   }

   public static class DawnBlindingKeystoneBlockEntity extends KeystoneBlockEntity {
      public DawnBlindingKeystoneBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.DAWN_BLINDING_KEYSTONE, pos, state, SkiesEntityTypes.ALCHEMIST, SkiesItems.blinding_key, SkiesItems.dusk_arc, posUp, posUp);
      }

      @Override
      public int getBossRoomSize() {
         return 20;
      }

      @Override
      public boolean isEverbrightBased() {
         return false;
      }
   }

   public static class NatureKeystoneBlockEntity extends KeystoneBlockEntity {
      public NatureKeystoneBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.NATURE_KEYSTONE, pos, state, SkiesEntityTypes.STARLIT_CRUSHER, SkiesItems.nature_key, SkiesItems.nature_arc, posUp, posUp);
      }

      public void teleportPlayer(Player player, Direction clickFace, Direction blockDirection) {
         if (this.level != null && !this.level.isClientSide) {
            boolean leaving = clickFace != blockDirection;
            if (leaving && SkiesPlayerEvents.inBossFight(player)) {
               this.level
                  .playSound(
                     null,
                     this.worldPosition.getX() + 0.5F,
                     this.worldPosition.getY() + 0.5F,
                     this.worldPosition.getZ() + 0.5F,
                     SkiesSounds.BLOCK_KEYSTONE_LOCKED,
                     SoundSource.BLOCKS,
                     1.0F,
                     1.0F
                  );
               player.displayClientMessage(Component.translatable("tile.keystone.leaving_in_fight"), true);
               return;
            }

            this.level
               .playSound(
                  null,
                  this.worldPosition.getX() + 0.5F,
                  this.worldPosition.getY() + 0.5F,
                  this.worldPosition.getZ() + 0.5F,
                  SkiesSounds.BLOCK_KEYSTONE_TELEPORT,
                  SoundSource.BLOCKS,
                  0.5F,
                  1.0F
               );
            BlockPos position = this.worldPosition.offset(this.teleportOffset);
            if (leaving) {
               position = this.worldPosition.offset(new BlockPos(-this.teleportOffset.getX(), this.teleportOffset.getY(), -this.teleportOffset.getZ()));
            }

            player.teleportTo(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
         }
      }

      @Override
      public int getRequiredProgressionId() {
         return 1;
      }
   }

   public static class PoisonKeystoneBlockEntity extends KeystoneBlockEntity {
      public PoisonKeystoneBlockEntity(BlockPos pos, BlockState state) {
         super(SkiesBlockEntityTypes.POISON_KEYSTONE, pos, state, SkiesEntityTypes.ARACHNARCH, SkiesItems.poison_key, SkiesItems.poison_arc, posUp, posUp);
      }

      public void teleportPlayer(Player player, Direction clickFace, Direction blockDirection) {
         if (this.level != null && !this.level.isClientSide) {
            boolean leaving = clickFace != blockDirection;
            if (leaving && SkiesPlayerEvents.inBossFight(player)) {
               this.level
                  .playSound(
                     null,
                     this.worldPosition.getX() + 0.5F,
                     this.worldPosition.getY() + 0.5F,
                     this.worldPosition.getZ() + 0.5F,
                     SkiesSounds.BLOCK_KEYSTONE_LOCKED,
                     SoundSource.BLOCKS,
                     1.0F,
                     1.0F
                  );
               player.displayClientMessage(Component.translatable("tile.keystone.leaving_in_fight"), true);
               return;
            }

            this.level
               .playSound(
                  null,
                  this.worldPosition.getX() + 0.5F,
                  this.worldPosition.getY() + 0.5F,
                  this.worldPosition.getZ() + 0.5F,
                  SkiesSounds.BLOCK_KEYSTONE_TELEPORT,
                  SoundSource.BLOCKS,
                  0.5F,
                  1.0F
               );
            BlockPos position = this.worldPosition.offset(this.teleportOffset);
            if (leaving) {
               position = this.worldPosition.offset(new BlockPos(-this.teleportOffset.getX(), this.teleportOffset.getY(), -this.teleportOffset.getZ()));
            }

            player.teleportTo(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
         }
      }

      @Override
      public int getBossRoomCeilingSize() {
         return 8;
      }

      @Override
      public boolean isEverbrightBased() {
         return false;
      }

      @Override
      public int getRequiredProgressionId() {
         return 1;
      }
   }
}
