package com.legacy.blue_skies.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.asm_hooks.PlayerHooks;
import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.blocks.misc.SkyWebbingBlock;
import com.legacy.blue_skies.blocks.util.ToolCompat;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.entities.util.WallExtensionEntity;
import com.legacy.blue_skies.entities.util.base.SkiesIllagerBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.items.arcs.IArcItem;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.SpawnParticlePacket;
import com.legacy.blue_skies.network.s_to_c.SyncItemInSlotPacket;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.blue_skies.registries.SkiesStats;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.registries.SkiesVillagers;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.util.GeometryHelper;
import com.legacy.structure_gel.api.util.Positions;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event.Result;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent.ChorusFruit;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent.EnderPearl;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.FillBucketEvent;
import net.neoforged.neoforge.event.entity.player.SleepingTimeCheckEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent.AdvancementEarnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.neoforged.neoforge.event.level.BlockEvent.BlockToolModificationEvent;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;

public class SkiesPlayerEvents {
   public static int spawnDimensionID = 0;

   @SubscribeEvent
   public static void onDatapackSync(OnDatapackSyncEvent event) {
      if (event.getPlayer() != null) {
         BlueSkiesData.syncManagersToClient(event.getPlayer());
      } else {
         event.getPlayerList().getPlayers().forEach(player -> BlueSkiesData.syncManagersToClient(player));
      }
   }

   @SubscribeEvent
   public static void onAdvancementGranted(AdvancementEarnEvent event) {
      if (event.getAdvancement().id().equals(BlueSkies.locate("everbright/enter")) || event.getAdvancement().id().equals(BlueSkies.locate("everdawn/enter"))) {
         EntityUtil.sendJournalToast(event.getEntity());
      }
   }

   @SubscribeEvent
   public static void onPlayerHurt(LivingDamageEvent.Pre event) {
      if (event.getEntity() instanceof Player player && !event.getEntity().level().isClientSide) {
         SkiesPlayer.ifPresent(player, skiesPlayer -> {
            skiesPlayer.getArcInventory().getStacks().forEach(stack -> {
               if (stack.getItem() instanceof IArcItem) {
                  ((IArcItem)stack.getItem()).onHit(stack, player, event);
               }
            });
            float newHealth = skiesPlayer.getNatureHealth() - event.getNewDamage();
            skiesPlayer.setNatureHealth(Math.max(0.0F, newHealth));
            event.setNewDamage(newHealth < 0.0F ? newHealth * -1.0F : 0.0F);
         });
      }
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void teleportToKeystone(LivingDeathEvent event) {
      if (event.getEntity() instanceof Player player) {
         SkiesPlayer.ifPresent(player, skiesPlayer -> {
            if (!skiesPlayer.getPlayer().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && inBossFight(skiesPlayer.getPlayer())) {
               skiesPlayer.teleportToNearestKeystone();
            }
         });
      }
   }

   @SubscribeEvent
   public static void onEntityDeath(LivingDeathEvent event) {
      if (event.getEntity() instanceof Player player) {
         SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.onDeath());
      }
   }

   @SubscribeEvent
   public static void onDeathItemDrops(LivingDropsEvent event) {
      if (event.getEntity() instanceof Player player) {
         SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.dropItems(event.getDrops()));
      }
   }

   @SubscribeEvent
   public static void onPlayerCloned(Clone event) {
   }

   @SubscribeEvent
   public static void onPlayerLogin(PlayerLoggedInEvent event) {
      SkiesPlayer.ifPresent(event.getEntity(), skiesPlayer -> skiesPlayer.syncDataToClient());
      if (spawnDimensionID > 0 && spawnDimensionID <= 2) {
         MinecraftServer server = event.getEntity().getServer();
         if (server == null || !(event.getEntity() instanceof ServerPlayer sp)) {
            return;
         }

         ServerLevel transferWorld = server.getLevel(spawnDimensionID == 1 ? SkiesDimensions.everbrightKey() : SkiesDimensions.everdawnKey());
         teleportSafely(sp, transferWorld, sp.blockPosition());
         spawnDimensionID = 0;
      }
   }

   @SubscribeEvent
   public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
      Entity entity = event.getEntity();
      if (entity != null && entity instanceof Player) {
         SkiesPlayer.ifPresent((Player)entity, skiesPlayer -> {
            skiesPlayer.getArcInventory().refreshArcs((Player)entity);
            skiesPlayer.syncDataToClient();
         });
      }
   }

   @SubscribeEvent
   public static void onPlayerChangeDim(PlayerChangedDimensionEvent event) {
      SkiesPlayer.ifPresent(event.getEntity(), skiesPlayer -> {
         if (skiesPlayer.getSupporterPet() != null) {
            skiesPlayer.setSupporterPet(null);
         }

         skiesPlayer.checkAndSpawnPet();
         skiesPlayer.syncDataToClient();
      });
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      SkiesPlayer.ifPresent(event.getEntity(), skiesPlayer -> skiesPlayer.syncDataToClient());
   }

   @SubscribeEvent
   public static void onPlayerInteract(EntityInteractSpecific event) {
      Entity entity = event.getTarget();
      Player player = event.getEntity();
      InteractionHand hand = event.getHand();
      ItemStack heldItem = player.getItemInHand(hand);
      if (entity instanceof Cow cow && heldItem.getItem() == SkiesItems.ventium_bucket && !player.getAbilities().instabuild && !cow.isBaby()) {
         player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
         heldItem.shrink(1);
         if (heldItem.isEmpty()) {
            player.setItemInHand(hand, new ItemStack(SkiesItems.ventium_milk_bucket));
         } else if (!player.getInventory().add(new ItemStack(SkiesItems.ventium_milk_bucket))) {
            player.drop(new ItemStack(SkiesItems.ventium_milk_bucket), false);
         }
      }

      if (entity instanceof Villager villager
         && player.isShiftKeyDown()
         && heldItem.getItem() == SkiesBlocks.bag_of_spoils.asItem()
         && !player.getCooldowns().isOnCooldown(heldItem.getItem())) {
         if (player.level().isClientSide()) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
         }

         if (villager.getVillagerData().getProfession() != SkiesVillagers.NIGHTWATCHER.get() && !EntityUtil.canEntitySeePos(villager, player.blockPosition())) {
            try {
               CompoundTag compoundnbt = heldItem.getTagElement("BlockEntityTag");
               NonNullList<ItemStack> originalItems = NonNullList.withSize(5, ItemStack.EMPTY);
               List<ItemStack> trueItems = Lists.newArrayList();
               if (compoundnbt == null) {
                  compoundnbt = new CompoundTag();
                  ContainerHelper.saveAllItems(compoundnbt, originalItems, false);
                  heldItem.addTagElement("BlockEntityTag", compoundnbt);
               }

               if (compoundnbt.contains("Items", 9)) {
                  ContainerHelper.loadAllItems(compoundnbt, originalItems);

                  for (ItemStack itemstack : originalItems) {
                     if (!itemstack.isEmpty()) {
                        trueItems.add(itemstack);
                     }
                  }
               }

               if (trueItems.size() <= 5 && player.level() instanceof ServerLevel) {
                  List<ItemStack> gifts = getVillagerGifts(villager);
                  ItemStack gift = gifts.get(player.getRandom().nextInt(gifts.size()));
                  boolean hasItem = false;

                  for (ItemStack stack : trueItems) {
                     if (stack != ItemStack.EMPTY && stack.getItem() == gift.getItem() && stack.getCount() < stack.getMaxStackSize()) {
                        stack.setCount(stack.getCount() + 1);
                        hasItem = true;
                        break;
                     }
                  }

                  if (!hasItem && trueItems.size() < 5) {
                     originalItems.set(trueItems.size(), gift);
                  }

                  ContainerHelper.saveAllItems(compoundnbt, originalItems, false);
                  heldItem.addTagElement("BlockEntityTag", compoundnbt);
                  if (!hasItem && trueItems.size() < 5 || hasItem) {
                     entity.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 0.8F);
                     player.swing(hand, true);
                     player.awardStat(SkiesStats.VILLAGERS_PICKPOCKETED);
                     if (!player.isCreative()) {
                        player.getCooldowns().addCooldown(heldItem.getItem(), 120);
                     }
                  }
               }
            } catch (Throwable e) {
               e.printStackTrace();
            }
         } else {
            if (entity.level() instanceof ServerLevel) {
               ((ServerLevel)entity.level()).onReputationEvent(ReputationEventType.VILLAGER_HURT, player, villager);
               entity.level().broadcastEntityEvent(villager, (byte)13);
            }

            entity.playSound(SoundEvents.VILLAGER_NO, 1.0F, EntityUtil.getSoundPitchWithStart(entity.level().getRandom(), 1.0F));
            player.swing(hand, true);
         }

         event.setCancellationResult(InteractionResult.SUCCESS);
      }
   }

   @SubscribeEvent
   public static void onToolUse(BlockToolModificationEvent event) {
      ToolAction toolAction = event.getToolAction();
      if (toolAction == ItemAbilities.AXE_STRIP) {
         BlockState finalState = event.getFinalState();
         Block result = ToolCompat.AXE_STRIPPING.get(finalState.getBlock());
         if (result != null) {
            event.setFinalState(result.withPropertiesOf(finalState));
         }
      } else if (toolAction == ItemAbilities.HOE_TILL) {
         Block result = ToolCompat.HOE_TILLING.get(event.getFinalState().getBlock());
         if (result != null) {
            event.setFinalState(result.defaultBlockState());
         }
      }
   }

   private static List<ItemStack> getVillagerGifts(Villager villager) {
      if (villager.isBaby()) {
         return ImmutableList.of(new ItemStack(Items.POPPY));
      } else {
         VillagerProfession villagerprofession = villager.getVillagerData().getProfession();
         if (GiveGiftToHero.GIFTS.containsKey(villagerprofession)) {
            LootTable loottable = villager.level().getServer().getLootData().getLootTable((ResourceLocation)GiveGiftToHero.GIFTS.get(villagerprofession));
            Builder lootcontext$builder = new Builder((ServerLevel)villager.level())
               .withParameter(LootContextParams.ORIGIN, villager.position())
               .withParameter(LootContextParams.THIS_ENTITY, villager);
            return loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.GIFT));
         } else {
            return Lists.newArrayList(
               new ItemStack[]{new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.STRING), new ItemStack(Items.COBWEB), new ItemStack(Items.DIRT)}
            );
         }
      }
   }

   private static ItemStack getSmeltedResult(ItemStack stack, Level level) {
      Container inventory = new SimpleContainer(new ItemStack[]{stack});
      ItemStack output = level.getRecipeManager()
         .getRecipeFor(RecipeType.SMELTING, inventory, level)
         .map(h -> (SmeltingRecipe)h.value())
         .map(furnaceRecipe -> furnaceRecipe.assemble(inventory, level.registryAccess()))
         .orElse(stack);
      output.setCount(stack.getCount());
      return output;
   }

   @SubscribeEvent
   public static void attackEntity(AttackEntityEvent event) {
      Player player = event.getEntity();
      if (player.getMainHandItem().is(SkiesItemTags.HORIZONITE_TOOLS)) {
         event.getTarget().setRemainingFireTicks(3);
      }

      if (event.getTarget() instanceof WallExtensionEntity ex) {
         player.attack(ex.getParent());
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void entityAttackedEvent(LivingAttackEvent event) {
      if (event.getSource().getDirectEntity() instanceof Player player) {
         Item playerItem = player.getMainHandItem().getItem();
         if (playerItem == SkiesItems.debug_sword && !BlueSkies.ML_SUPPORTER.getRank(player).isDeveloper() && !BlueSkies.IDE) {
            BlueSkies.LOGGER.debug(player.getName().getContents() + " tried to use the debug sword.");
            event.setCanceled(true);
            return;
         }
      }
   }

   @SubscribeEvent
   public static void onSleepTimeCheck(SleepingTimeCheckEvent event) {
      if (SkiesDimensions.inSkyDimension(event.getEntity())) {
         event.setResult(Result.ALLOW);
      }
   }

   @SubscribeEvent
   public static void onPlayerPlaceBlock(EntityPlaceEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         BlockPos var4 = event.getPos();
         BlockState state = event.getPlacedBlock();
         if (!player.isCreative()
            && (inBossFight(player) || !SkiesPlayerEvents.DungeonBalancingChecks.playerCanPlaceInDungeon(player, event.getLevel(), var4, state.getBlock()))) {
            player.displayClientMessage(
               Component.translatable(
                  SkiesPlayerEvents.DungeonBalancingChecks.isPosInBossRoom(player.level(), var4)
                     ? "gui.blue_skies.tooltip.boss_room_denied_interaction"
                     : "gui.blue_skies.tooltip.progression_denied_interaction"
               ),
               true
            );
            event.setCanceled(true);
            PacketHandler.sendToClient(new SyncItemInSlotPacket(player.getMainHandItem(), player.getOffhandItem(), player.getInventory().selected), player);
         }
      }
   }

   @SubscribeEvent
   public static void harvestCheck(HarvestCheck event) {
      Item item = event.getEntity().getMainHandItem().getItem();
      if (item != null && PlayerHooks.webHarvestCheck(event.getTargetBlock().getBlock(), item)) {
         event.setCanHarvest(true);
      }

      if (PlayerHooks.isBreakingNerfed(item, event.getEntity(), event.getTargetBlock())) {
         event.setCanHarvest(false);
      }
   }

   @SubscribeEvent
   public static void destroyBlock(BreakEvent event) {
      if (event.getLevel() instanceof ServerLevel level) {
         Player var7 = event.getPlayer();
         BlockState state = event.getState();
         BlockPos pos = event.getPos();
         Item item = var7.getMainHandItem().getItem();
         if (state.getBlock() instanceof SpawnerBlock && EntityUtil.isInDungeon(level, pos)) {
            event.setExpToDrop(3);
         }

         if (!var7.isCreative()) {
            if (!(state.getBlock() instanceof BaseFireBlock)
               && (inBossFight(var7) || !SkiesPlayerEvents.DungeonBalancingChecks.playerCanBreakDungeon(var7, level, pos))) {
               var7.displayClientMessage(
                  Component.translatable(
                     SkiesPlayerEvents.DungeonBalancingChecks.isPosInBossRoom(var7.level(), pos)
                        ? "gui.blue_skies.tooltip.boss_room_denied_interaction"
                        : "gui.blue_skies.tooltip.progression_denied_interaction"
                  ),
                  true
               );
               event.setCanceled(true);
               return;
            }

            if (var7.hasCorrectToolForDrops(state)) {
               if (var7.getMainHandItem().is(SkiesItemTags.HORIZONITE_TOOLS)) {
                  List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos), var7, var7.getMainHandItem());
                  drops.forEach(itemStack -> {
                     ItemStack smeltedStack = getSmeltedResult(itemStack, level);
                     Block.popResource(level, pos, smeltedStack);
                     if (smeltedStack != itemStack) {
                        PacketHandler.sendToClients(new SpawnParticlePacket((byte)0, pos), level);
                     }
                  });
                  level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                  return;
               }

               if (item == SkiesItems.ventium_shears) {
                  List<ItemStack> drops = Block.getDrops(state, level, pos, level.getBlockEntity(pos), var7, new ItemStack(Items.SHEARS));
                  drops.forEach(itemStack -> Block.popResource(level, pos, itemStack));
                  level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                  return;
               }
            }

            if (PlayerHooks.isBreakingNerfed(item, var7, state)) {
               event.setCanceled(true);
               level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
               return;
            }
         }
      }
   }

   @SubscribeEvent
   public static void onBucketFill(FillBucketEvent event) {
      Player player = event.getEntity();
      if (!player.isCreative() && !SkiesPlayerEvents.DungeonBalancingChecks.isProgressedForPos(player, Positions.blockPos(event.getTarget().getLocation()))) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onPearlTeleport(EnderPearl event) {
      ServerPlayer player = event.getPlayer();
      if (!player.isCreative()
         && SkiesDimensions.inSkyDimension(player)
         && (
            !SkiesPlayerEvents.DungeonBalancingChecks.isProgressedForPos(player, Positions.blockPos(event.getTarget()).below())
               || !SkiesPlayerEvents.DungeonBalancingChecks.isProgressedForPos(player, Positions.blockPos(event.getTarget()))
         )) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onChorusTeleport(ChorusFruit event) {
      if (event.getEntityLiving() instanceof Player player
         && !player.isCreative()
         && SkiesDimensions.inSkyDimension(player)
         && (
            !SkiesPlayerEvents.DungeonBalancingChecks.isProgressedForPos(player, Positions.blockPos(event.getTarget()).below())
               || !SkiesPlayerEvents.DungeonBalancingChecks.isProgressedForPos(player, Positions.blockPos(event.getTarget()))
         )) {
         event.setCanceled(true);
      }
   }

   public static boolean inBossFight(Player player) {
      return !SkiesDimensions.inSkyDimension(player)
         ? false
         : !player.level()
            .getEntitiesOfClass(
               Monster.class,
               new AABB(player.blockPosition()).inflate(25.0, 15.0, 25.0),
               e -> e instanceof SkiesIllagerBossEntity && !(player.getY() >= e.getY() - 5.0) ? false : e instanceof ISkyBoss
            )
            .isEmpty();
   }

   private static ServerPlayer teleportSafely(ServerPlayer player, ServerLevel transferWorld, BlockPos pos) {
      player.teleportTo(transferWorld, pos.getX(), 100.0, pos.getZ(), player.getYRot(), player.getXRot());
      int maxY = transferWorld.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ());
      player.teleportTo(transferWorld, pos.getX(), maxY, pos.getZ(), player.getYRot(), player.getXRot());
      return player;
   }

   public static class DungeonBalancingChecks {
      private static final Set<Block> GENERIC_PLACEABLES = Set.of(
         Blocks.TORCH, Blocks.WALL_TORCH, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH, SkiesBlocks.moonstone
      );
      private static final Set<Block> GENERIC_BREAKABLES = Set.of(
         Blocks.SPAWNER, Blocks.COBWEB, Blocks.TORCH, Blocks.WALL_TORCH, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH, SkiesBlocks.moonstone
      );
      private static final Set<Block> NATURE_BREAKABLES = Set.of(SkiesBlocks.starlit_chest, SkiesBlocks.comet_chest);
      private static final Set<Block> POISON_BREAKABLES = Set.of(SkiesBlocks.maple_chest);
      private static final Set<UUID> KNOWN_MISSING = new HashSet<>();

      private static boolean isOurs(String mod) {
         return "blue_skies".equals(mod) || "minecraft".equals(mod);
      }

      public static boolean playerCanPlaceInDungeon(Player player, LevelAccessor levelAccessor, BlockPos pos, Block placingBlock) {
         Level level = (Level)(levelAccessor instanceof ServerLevel sl ? sl : player.level());
         if (player instanceof FakePlayer) {
            return !EntityUtil.isInDungeon(level, pos);
         } else {
            return isPosInBossRoom(level, pos)
               ? false
               : SkiesPlayer.getIfPresent(
                  player,
                  skyPlayer -> {
                     if ((
                           skyPlayer.getBrightProgression() >= 1
                              || !EntityUtil.isPosInStructure(level, pos, SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getStructure())
                        )
                        && (
                           skyPlayer.getDawnProgression() >= 1
                              || !EntityUtil.isPosInStructure(level, pos, SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getStructure())
                        )) {
                        return (
                                 skyPlayer.getBrightProgression() >= 2
                                    || !EntityUtil.isPosInStructure(level, pos, SkiesStructures.NATURE_DUNGEON.getStructure())
                              )
                              && (
                                 pos.getY() >= 64
                                    || skyPlayer.getDawnProgression() >= 2
                                    || !EntityUtil.isPosInStructure(level, pos, SkiesStructures.POISON_DUNGEON.getStructure())
                              )
                           ? true
                           : genericBlockPlaceCheck(placingBlock);
                     } else {
                        return genericBlockPlaceCheck(placingBlock);
                     }
                  },
                  () -> {
                     capabilityError(player, "place");
                     return !EntityUtil.isInDungeon(level, pos);
                  }
               );
         }
      }

      private static boolean genericBlockPlaceCheck(Block block) {
         return GENERIC_PLACEABLES.contains(block) || block instanceof BedBlock;
      }

      public static boolean playerCanBreakDungeon(Player player, ServerLevel level, BlockPos pos) {
         if (player instanceof FakePlayer) {
            return !EntityUtil.isInDungeon(level, pos);
         } else {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(block);
            if (registryName == null) {
               return true;
            } else {
               return isPosInBossRoom(level, pos)
                  ? !isOurs(registryName.getNamespace())
                  : SkiesPlayer.getIfPresent(
                     player,
                     skyPlayer -> {
                        if ((
                              skyPlayer.getBrightProgression() >= 1
                                 || !EntityUtil.isPosInStructure(level, pos, SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getStructure())
                           )
                           && (
                              skyPlayer.getDawnProgression() >= 1
                                 || !EntityUtil.isPosInStructure(level, pos, SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getStructure())
                           )) {
                           if (skyPlayer.getBrightProgression() < 2 && EntityUtil.isPosInStructure(level, pos, SkiesStructures.NATURE_DUNGEON.getStructure())) {
                              return NATURE_BREAKABLES.contains(block) || genericBlockBreakCheck(block, registryName);
                           } else {
                              return pos.getY() < 64
                                    && skyPlayer.getDawnProgression() < 2
                                    && EntityUtil.isPosInStructure(level, pos, SkiesStructures.POISON_DUNGEON.getStructure())
                                 ? POISON_BREAKABLES.contains(block)
                                    || block instanceof SkyWebbingBlock && !(Boolean)state.getValue(SkyWebbingBlock.HAS_COLLISION)
                                    || genericBlockBreakCheck(block, registryName)
                                 : true;
                           }
                        } else {
                           return genericBlockBreakCheck(block, registryName);
                        }
                     },
                     () -> {
                        capabilityError(player, "break");
                        return !EntityUtil.isInDungeon(level, pos);
                     }
                  );
            }
         }
      }

      private static boolean genericBlockBreakCheck(Block block, @Nonnull ResourceLocation registryName) {
         return GENERIC_BREAKABLES.contains(block) || block instanceof BedBlock || !isOurs(registryName.getNamespace());
      }

      public static boolean isProgressedForPos(Player player, BlockPos pos) {
         if (player instanceof FakePlayer) {
            return !EntityUtil.isInDungeon(player.level(), pos);
         } else {
            return isPosInBossRoom(player.level(), pos)
               ? false
               : SkiesPlayer.getIfPresent(
                  player,
                  skyPlayer -> {
                     if ((
                           skyPlayer.getBrightProgression() >= 1
                              || !EntityUtil.isPosInStructure(player.level(), pos, SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getStructure())
                        )
                        && (
                           skyPlayer.getDawnProgression() >= 1
                              || !EntityUtil.isPosInStructure(player.level(), pos, SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getStructure())
                        )) {
                        if (skyPlayer.getBrightProgression() < 2
                           && EntityUtil.isPosInStructure(player.level(), pos, SkiesStructures.NATURE_DUNGEON.getStructure())) {
                           return false;
                        } else {
                           return pos.getY() < 64
                                 && skyPlayer.getDawnProgression() < 2
                                 && EntityUtil.isPosInStructure(player.level(), pos, SkiesStructures.POISON_DUNGEON.getStructure())
                              ? false
                              : true;
                        }
                     } else {
                        return false;
                     }
                  },
                  () -> {
                     capabilityError(player, "position");
                     return false;
                  }
               );
         }
      }

      public static boolean isPosInBossRoom(Level level, BlockPos pos) {
         if (level instanceof ServerLevel serverLevel && EntityUtil.isInDungeon(level, pos)) {
            byte distance = 45;
            PoiManager poiManager = serverLevel.getPoiManager();
            poiManager.ensureLoadedAndValid(serverLevel, pos, distance);
            Optional<PoiRecord> optional = serverLevel.getPoiManager()
               .getInSquare(poiType -> poiType.is(SkiesPoiTypes.KEYSTONE.getKey()), pos, distance, Occupancy.ANY)
               .findFirst();
            if (optional.isPresent() && serverLevel.getBlockEntity(optional.get().getPos()) instanceof KeystoneBlockEntity keystoneTile) {
               BlockPos bossRoomCenter = keystoneTile.getBlockPos().offset(keystoneTile.getSpawnOffset());
               boolean isInCeilingRange = keystoneTile.getBossRoomCeilingSize() == 0
                  || pos.getY() <= bossRoomCenter.getY() + keystoneTile.getBossRoomCeilingSize();
               if (isInCeilingRange
                  && pos.getY() >= bossRoomCenter.getY() - 1
                  && GeometryHelper.isPointInsideRec(
                     pos,
                     Positions.blockPos(
                        bossRoomCenter.getX() - keystoneTile.getBossRoomSize(), pos.getY(), bossRoomCenter.getZ() - keystoneTile.getBossRoomSize()
                     ),
                     Positions.blockPos(
                        bossRoomCenter.getX() + keystoneTile.getBossRoomSize(), pos.getY(), bossRoomCenter.getZ() + keystoneTile.getBossRoomSize()
                     )
                  )) {
                  return true;
               }
            }
         }

         return false;
      }

      private static void capabilityError(Player player, String type) throws IllegalStateException {
         if (BlueSkiesConfig.COMMON.crashOnMissingCapabilityForDungeon()) {
            throw new IllegalStateException(
               "Blue Skies' player capability wasn't present for " + player.toString() + " when doing a progression check for \"" + type + "\""
            );
         }

         if (KNOWN_MISSING.add(player.getUUID())) {
            BlueSkies.LOGGER
               .error(
                  "Blue Skies' player capability wasn't present for {} when doing a progression check for \"{}\". This should be reported if happening consistently!",
                  player.toString(),
                  type
               );
         }
      }
   }
}
