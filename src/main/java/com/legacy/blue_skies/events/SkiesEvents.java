package com.legacy.blue_skies.events;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.blocks.construction.TroughBlock;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.entities.SupporterPetEntity;
import com.legacy.blue_skies.entities.ai.NightwatcherRobGoal;
import com.legacy.blue_skies.entities.ai.ShovelSnowGoal;
import com.legacy.blue_skies.entities.ai.VillagerWanderAtWorkGoal;
import com.legacy.blue_skies.entities.hostile.ArmoredFrostSpiritEntity;
import com.legacy.blue_skies.entities.hostile.boss.AlchemistEntity;
import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.hostile.boss.SummonerEntity;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import com.legacy.blue_skies.entities.util.SkiesEntityHooks;
import com.legacy.blue_skies.entities.util.base.SkiesIllagerBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.entities.villager.SkiesVillagerTrades;
import com.legacy.blue_skies.items.util.FurnaceFuels;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesEffects;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.blue_skies.registries.SkiesTriggers;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;
import com.legacy.structure_gel.api.util.Positions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event.Result;

import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.FinalizeSpawn;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent.Detonate;
import net.neoforged.neoforge.event.level.ExplosionEvent.Start;
import net.neoforged.neoforge.event.level.LevelEvent.Load;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

public class SkiesEvents {
   @SubscribeEvent
   public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
      if (event.getEntity() instanceof Villager villager) {
         villager.goalSelector.addGoal(1, new ShovelSnowGoal(villager, 0.5));
         villager.goalSelector.addGoal(1, new NightwatcherRobGoal(villager, 0.4));
         villager.goalSelector.addGoal(5, new VillagerWanderAtWorkGoal(villager, 0.6));
         villager.goalSelector.addGoal(1, new AvoidEntityGoal(villager, SummonerEntity.class, 8.0F, 0.6, 0.6));
         villager.goalSelector.addGoal(1, new AvoidEntityGoal(villager, AlchemistEntity.class, 8.0F, 0.6, 0.6));
         villager.goalSelector.addGoal(1, new AvoidEntityGoal(villager, ArmoredFrostSpiritEntity.class, 8.0F, 0.6, 0.6));
      }
   }

   @SubscribeEvent
   public static void onLevelLoad(Load event) {
      if (event.getLevel() instanceof Level level) {
         SkiesDamageSources.create(level);
      }
   }

   @SubscribeEvent
   public static void onEntityUpdate(LivingTickEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity instanceof Player p) {
         SkiesPlayer.ifPresent(p, skyPlayer -> skyPlayer.serverTick());
      }

      if (entity.getEffect(SkiesEffects.DEADLY_VENOM) != null) {
         MobEffectInstance effect = entity.getEffect(SkiesEffects.DEADLY_VENOM);
         int difficultyTimePerHit = effect.getAmplifier() == 0 ? 12 : (effect.getAmplifier() == 1 ? 11 : 2);
         boolean canDeadlyVenomHurt = entity.tickCount % difficultyTimePerHit == 0;
         if (canDeadlyVenomHurt) {
            entity.hurt(SkiesDamageSources.get(entity.level()).venomSpit(), 2.0F);
         }
      }

      if (entity.level() instanceof ServerLevel
         && entity instanceof Animal animal
         && animal.isBaby()
         && animal.level().random.nextFloat() < 0.3F
         && animal.tickCount % (200 + animal.getRandom().nextInt(50)) == 0) {
         int radius = 7;
         Stream<PoiRecord> poi = EntityUtil.getPoisInCircle(animal, SkiesPoiTypes.TROUGH.getKey(), radius);
         Optional<PoiRecord> optional = poi.findAny();
         if (optional.isPresent()) {
            BlockState trough = entity.level().getBlockState(optional.get().getPos());
            if (TroughBlock.canConsume(trough, entity.level())) {
               animal.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
               if (animal.getRandom().nextFloat() < 0.3F) {
                  TroughBlock.attemptConsume(trough, entity.level(), optional.get().getPos());
                  animal.ageUp(30 + animal.getRandom().nextInt(10), true);
               } else {
                  animal.ageUp(10 + animal.getRandom().nextInt(10), true);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityDeath(LivingDeathEvent event) {
      if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ISkyBoss boss) {
         AABB radiusCheck = new AABB(event.getEntity().blockPosition()).inflate(25.0, 15.0, 25.0);

         for (Player player : event.getEntity().level().getEntitiesOfClass(Player.class, radiusCheck)) {
            SkiesPlayer.ifPresent(player, skiesPlayer -> {
               if (boss.isDungeonSpawned()) {
                  if (boss.isEverbrightBoss() && skiesPlayer.getBrightProgression() == boss.getProgression() - 1) {
                     EntityUtil.sendJournalToast(player);
                     skiesPlayer.setBrightProgression((byte)boss.getProgression());
                  } else if (!boss.isEverbrightBoss() && skiesPlayer.getDawnProgression() == boss.getProgression() - 1) {
                     EntityUtil.sendJournalToast(player);
                     skiesPlayer.setDawnProgression((byte)boss.getProgression());
                  }
               }

               skiesPlayer.syncDataToClient();
            });
         }
      }
   }

   @SubscribeEvent
   public static void onEntityCheckSpawn(FinalizeSpawn event) {
      LivingEntity entity = event.getEntity();
      if (SkiesDimensions.inSkyDimension(entity)
         && (event.getSpawnType() == MobSpawnType.NATURAL || event.getSpawnType() == MobSpawnType.CHUNK_GENERATION)
         && !BlueSkiesConfig.COMMON.isEntityAllowedToSpawn(entity.getType())) {
         event.setSpawnCancelled(true);
      }

      if (entity.getType().getCategory() == MobCategory.MONSTER
         && (event.getSpawnType() == MobSpawnType.NATURAL || event.getSpawnType() == MobSpawnType.SPAWNER)
         && entity.level() instanceof ServerLevel
         && EntityUtil.getPoisInCircle(entity, SkiesPoiTypes.WARDING_PEARL.getKey(), 25).findAny().isPresent()) {
         event.setSpawnCancelled(true);
      }
   }

   @SubscribeEvent
   public static void onVillagerTradesAssigned(VillagerTradesEvent event) {
      try {
         if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            ((List)event.getTrades().get(2)).add(SkiesVillagerTrades.BLINDING_DUNGEON_OFFER);
            ((List)event.getTrades().get(3)).add(SkiesVillagerTrades.POPULATION_DUNGEON_OFFER);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @SubscribeEvent
   public static void onLivingSetTarget(LivingChangeTargetEvent event) {
      if (event.getNewTarget() instanceof SupporterPetEntity && event.getEntity() instanceof Mob mob) {
         mob.setTarget(event.getOriginalTarget());
      }
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void nerfReachAttacks(LivingDamageEvent event) {
      if (event.getEntity() instanceof ISkyBoss boss && boss.getMaxReach() >= 0.0F && !event.getSource().isCreativePlayer()) {
         DamageSource source = event.getSource();
         if (source.getDirectEntity() instanceof Player) {
            double dist = source.getDirectEntity().distanceTo(event.getEntity());
            float maxDist = boss.getMaxReach();
            if (dist > maxDist) {
               event.setAmount(0.0F);
               event.setCanceled(true);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLivingHurt(LivingDamageEvent event) {
      DamageSource source = event.getSource();
      if (source.getDirectEntity() != null) {
         if (event.getEntity() == source.getEntity()) {
            return;
         }

         if (SkiesDimensions.inSkyDimension(source.getDirectEntity())) {
            if (source.getDirectEntity() instanceof Player) {
               event.setAmount(SkiesEntityHooks.nerfDamage(source, event.getAmount()));
            } else if ((source.is(DamageTypeTags.IS_PROJECTILE) || source.isIndirect()) && !source.is(DamageTypeTags.IS_EXPLOSION)) {
               event.setAmount(source.getEntity() == null ? Math.min(5.0F, event.getAmount()) : SkiesEntityHooks.nerfIndirectDamage(source, event.getAmount()));
            }
         }
      }
   }

   @SubscribeEvent(priority = EventPriority.LOWEST)
   public static void capBossDamage(LivingDamageEvent event) {
      DamageSource source = event.getSource();
      if (event.getEntity() instanceof ISkyBoss boss
         && event.getAmount() > boss.getDamageCap()
         && !source.isCreativePlayer()
         && event.getSource() != event.getEntity().level().damageSources().fellOutOfWorld()) {
         event.setAmount(boss.getDamageCap());
      }

      if (source.getDirectEntity() != null
         && source.getEntity() != null
         && source.getEntity() instanceof ServerPlayer player
         && event.getAmount() >= event.getEntity().getHealth()) {
         if (event.getEntity() instanceof StarlitCrusherEntity
            && source.getDirectEntity() == player
            && !player.isUsingItem()
            && player.getMainHandItem().isEmpty()) {
            SkiesTriggers.SPECIAL_CRUSHER_KILL.trigger(player);
         } else if (event.getEntity() instanceof ArachnarchEntity arach && arach.isCeilingLocked()) {
            SkiesTriggers.SPECIAL_ARACHNARCH_KILL.trigger(player);
         }
      }
   }

   @SubscribeEvent
   public static void onLivingKnockBack(LivingKnockBackEvent event) {
      if (event.getEntity() instanceof ISkyBoss && !(event.getEntity() instanceof SkiesIllagerBossEntity)) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onExplosionStart(Start event) {
      if (EntityUtil.isInDungeon(event.getLevel(), Positions.blockPos(event.getExplosion().center()))) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onExplosionDetonate(Detonate event) {
      List<BlockPos> blocks = new ArrayList<>(event.getAffectedBlocks());
      blocks.forEach(positions -> {
         if (EntityUtil.isInDungeon(event.getLevel(), positions)) {
            event.getAffectedBlocks().remove(positions);
         }
      });
   }

   @SubscribeEvent
   public static void onMobGrief(EntityMobGriefingEvent event) {
      if (event.getEntity() != null
         && SkiesDimensions.inSkyDimension(event.getEntity())
         && EntityUtil.isInDungeon(event.getEntity().level(), event.getEntity().blockPosition())) {
         event.setResult(Result.DENY);
      }
   }

   @SubscribeEvent
   public static void onProjectileImpact(ProjectileImpactEvent event) {
      if (event.getRayTraceResult().getType() == Type.ENTITY
         && ((EntityHitResult)event.getRayTraceResult()).getEntity() instanceof AlchemistEntity alch
         && !alch.level().isClientSide
         && event.getProjectile() instanceof AbstractArrow arrowOld
         && !(event.getProjectile() instanceof ThrownTrident)
         && !(event.getProjectile() instanceof SpearEntity)) {
         event.setCanceled(true);
         LivingEntity target = arrowOld.getOwner() != null && arrowOld.getOwner() instanceof LivingEntity living ? living : alch.getTarget();
         if (target != null) {
            Arrow arrow = new Arrow(alch.level(), alch, Items.ARROW.getDefaultInstance());
            arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 2));
            arrow.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200));
            arrow.setKnockback(1);
            arrow.setCritArrow(true);
            double d0 = target.getX() - alch.getX();
            double d1 = target.getY(0.3333333333333333) - alch.getY();
            double d2 = target.getZ() - alch.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            arrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.5F, Math.max(0, 16 - alch.getDifficultyID() * 4));
            arrow.setBaseDamage(12.0);
            alch.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE, 1.0F, 1.0F);
            alch.spawnAnim();
            alch.level().addFreshEntity(arrow);
         }

         arrowOld.discard();
      }
   }

   @SubscribeEvent
   public static void getBurnTime(FurnaceFuelBurnTimeEvent event) {
      Item item = event.getItemStack().getItem();
      if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("blue_skies")) {
         event.setBurnTime(FurnaceFuels.getBurnTime(item));
      }
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent.Post event) {
      if (event.hasTime()) {
         AbstractBiomeProvider.updateCacheSize(event.getServer());
      }
   }
}
