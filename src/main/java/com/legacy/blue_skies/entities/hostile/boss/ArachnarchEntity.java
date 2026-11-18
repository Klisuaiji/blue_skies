package com.legacy.blue_skies.entities.hostile.boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.blocks.dungeon.SpiderNestBlock;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.entities.hostile.boss.summons.NestedSpiderEntity;
import com.legacy.blue_skies.entities.projectile.VenomBombEntity;
import com.legacy.blue_skies.entities.projectile.VenomSpitEntity;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.entities.util.StunnableLookController;
import com.legacy.blue_skies.entities.util.StunnedEntitySound;
import com.legacy.blue_skies.entities.util.base.SkiesBossEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.entities.util.interfaces.IStunnableMob;
import com.legacy.blue_skies.items.LootBagItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEffects;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.structure_gel.api.util.Positions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiManager.Occupancy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.ToolActions;

public class ArachnarchEntity extends SkiesBossEntity implements RangedAttackMob, IStunnableMob {
   protected static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.BYTE);
   protected static final EntityDataAccessor<Boolean> CEILING_LOCKED = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Boolean> STUNNED = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Integer> TIME_ON_CEILING = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Integer> TIME_SINCE_LOCKED = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Integer> TIME_STUNNED = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Integer> TICKS_UNTIL_NEXT_SCREECH = SynchedEntityData.defineId(ArachnarchEntity.class, EntityDataSerializers.INT);
   private static final Set<MobEffect> IMMUNE_EFFECTS = ImmutableSet.of(
      MobEffects.MOVEMENT_SLOWDOWN, MobEffects.POISON, SkiesEffects.DEADLY_VENOM, MobEffects.SLOW_FALLING, MobEffects.LEVITATION
   );
   public static final Set<Item> SHIELDS = ImmutableSet.of(SkiesItems.moonstone_shield, SkiesItems.spike_shield);
   private Map<UUID, Vec3> oldPosRef = new HashMap<>();
   public int deathTicks;
   public int holdingTime = 1;
   public int clientScreechTime = 0;
   public float damageSinceGrabbed = 0.0F;
   @OnlyIn(Dist.CLIENT)
   private SoundInstance stunSound;

   public ArachnarchEntity(EntityType<? extends ArachnarchEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 40;
      this.lookControl = new StunnableLookController(this);
   }

   @Override
   public void move(MoverType type, Vec3 vec) {
      if (this.getInvulTime() <= 0 && !this.isStunned()) {
         super.move(type, vec);
      } else {
         super.move(type, new Vec3(0.0, vec.y, 0.0));
      }
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector2.addSkyGoal(1, new FloatGoal(this));
      this.goalSelector2
         .addSkyGoal(
            5,
            new WaterAvoidingRandomStrollGoal(this, 0.8) {
               public boolean canContinueToUse() {
                  return !((ArachnarchEntity)this.mob).isCeilingLocked() && this.mob.getTarget() == null
                     ? !((ArachnarchEntity)this.mob).isDungeonSpawned() && super.canContinueToUse()
                     : false;
               }

               public boolean canUse() {
                  return !((ArachnarchEntity)this.mob).isCeilingLocked() && this.mob.getTarget() == null
                     ? !((ArachnarchEntity)this.mob).isDungeonSpawned() && super.canUse()
                     : false;
               }
            }
         );
      this.goalSelector2.addSkyGoal(6, new LookAtPlayerGoal(this, Player.class, 15.0F));
      this.goalSelector2.addSkyGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector2.addSkyGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector2.addSkyGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
      this.goalSelector2.addSkyGoal(3, new ArachnarchEntity.VenomAttackGoal(this, 1.25, 20, 40.0F));
      this.goalSelector2.addSkyGoal(3, new ArachnarchEntity.VenomWalkingAttackGoal(this, 1.25, 60, 40.0F));
      this.goalSelector2.addSkyGoal(3, new ArachnarchEntity.LungeAtTargetGoal());
      this.goalSelector2.addSkyGoal(3, new ArachnarchEntity.WalkAndDropGoal());
      this.goalSelector2.addSkyGoal(3, new ArachnarchEntity.ScreechAttackGoal());
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag
   ) {
      SpawnGroupData flag = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      if (!this.level().isClientSide()) {
         if (this.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId()) {
            if (this.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId()) {
               this.getAttribute(Attributes.ARMOR).setBaseValue(7.5);
            }

            this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(this.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId() ? 6.0 : 4.0);
         }

         this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(14.0 + Math.min(8.0, this.getExtraPlayers() * 1.5));
      }

      return flag;
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 500.0)
         .add(Attributes.FOLLOW_RANGE, 40.0)
         .add(Attributes.MOVEMENT_SPEED, 0.36F)
         .add(Attributes.ATTACK_DAMAGE, 14.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
         .add(Attributes.ARMOR, 6.0)
         .add(Attributes.ATTACK_KNOCKBACK, 4.0)
         .add((Attribute)NeoForgeMod.STEP_HEIGHT.value(), 2.0);
   }

   @Override
   public double getBossHealth() {
      return 500.0 + this.getExtraPlayers() * 200.0;
   }

   @Override
   public float getHealAmount() {
      return 50.0F;
   }

   @Override
   public LootBagItem getLootBag() {
      return SkiesItems.loot_bag_arachnarch;
   }

   @Override
   public TrophyBlock getTrophy() {
      return SkiesBlocks.arachnarch_trophy;
   }

   @Override
   public float getMaxReach() {
      return 5.08F;
   }

   @Override
   public int getDamageCap() {
      return 15;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(CLIMBING, (byte)0);
      this.entityData.define(CEILING_LOCKED, false);
      this.entityData.define(CHARGING, false);
      this.entityData.define(STUNNED, false);
      this.entityData.define(TIME_ON_CEILING, 0);
      this.entityData.define(TIME_SINCE_LOCKED, 0);
      this.entityData.define(TIME_STUNNED, 0);
      this.entityData.define(TICKS_UNTIL_NEXT_SCREECH, 0);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.deathTicks <= 0 && !this.isNoAi()) {
         if (this.clientScreechTime > 0) {
            this.clientScreechTime--;
         }

         if (!this.level().isClientSide()) {
            if (this.getPlayersInDungeonArea().isEmpty() && !this.getSpidersInDungeonArea(20.0).isEmpty()) {
               this.getSpidersInDungeonArea(20.0).forEach(spider -> {
                  spider.discard();
                  spider.spawnAnim();
               });
            }

            this.setBesideClimbableBlock(this.horizontalCollision);
            if (this.isStunned()) {
               if (!this.getPassengers().isEmpty()) {
                  ((Entity)this.getPassengers().get(0)).stopRiding();
               }

               if (this.getStunTime() > 0) {
                  this.setStunTime(this.getStunTime() - 1);
               } else {
                  this.setStunned(false);
               }
            } else {
               if (this.getSpidersInDungeonArea(15.0).isEmpty() && this.getTicksUntilNextScreech() > 0) {
                  this.setTicksUntilNextScreech(this.getTicksUntilNextScreech() - 1);
               }

               if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity) {
                  if (this.holdingTime > 0) {
                     this.holdingTime--;
                  }

                  if (this.holdingTime <= 70 && !this.isCeilingLocked() && this.getDifficultyID() >= ISkyBoss.BossLevel.PLATINUM.getId()) {
                     if (this.distanceToSqr(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ()) > 60.0) {
                        this.getMoveControl().setWantedPosition(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ(), 1.0);
                     } else {
                        this.slingToCeiling();
                     }
                  }

                  if (this.isCharging()) {
                     this.setCharging(false);
                  }

                  LivingEntity rider = (LivingEntity)this.getPassengers().get(0);
                  rider.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
                  this.setYRot(this.yHeadRot);
                  double yLook = this.isCeilingLocked() ? this.getY() + this.getEyeHeight() : this.getHome().getY();
                  if (this.distanceToSqr(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ()) > 20.0) {
                     this.lookAt(Anchor.EYES, new Vec3(this.getHome().getX(), yLook, this.getHome().getZ()));
                  }
               }

               if (!this.isCeilingLocked()) {
                  BlockPos pos = this.blockPosition();
                  if (!this.level().isEmptyBlock(pos.above(2)) && this.isBesideClimbableBlock() && this.getTarget() == null) {
                     this.setCeilingLocked(true);
                  }

                  if (this.getTarget() != null) {
                     boolean canSling = !this.level().canSeeSky(this.blockPosition())
                        && this.level().getBrightness(LightLayer.SKY, this.blockPosition()) < 5
                        && this.getInvulTime() <= 0;
                     if (this.getTimeSinceLocked() < 260 && canSling && this.getPassengers().isEmpty()) {
                        this.setTimeSinceLocked(this.getTimeSinceLocked() + 1);
                     }

                     if (this.getTimeSinceLocked() >= 240 && canSling) {
                        if (this.distanceToSqr(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ()) > 60.0) {
                           this.getMoveControl().setWantedPosition(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ(), 1.0);
                        } else {
                           this.slingToCeiling();
                        }
                     }
                  }

                  if (this.isCharging() && this.distanceToSqr(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ()) > 60.0) {
                     this.getMoveControl().setWantedPosition(this.getHome().getX(), this.getHome().getY(), this.getHome().getZ(), 1.0);
                  }
               } else if (this.getTimeOnCeiling() <= 0) {
                  this.setCeilingLocked(false);
               } else {
                  boolean canScreech = this.getTicksUntilNextScreech() <= 0 && this.getTimeOnCeiling() > 600;
                  if (!this.isSecondPhase()
                     || this.isSecondPhase() && this.getSpidersInDungeonArea(15.0).isEmpty() && !canScreech && this.getPassengers().isEmpty()) {
                     this.setTimeOnCeiling(this.getTimeOnCeiling() - 1);
                  }

                  Player closestPlayer = this.level().getNearestPlayer(this, 30.0);
                  if (!this.getSpidersInDungeonArea(15.0).isEmpty() && closestPlayer != null) {
                     this.getLookControl().setLookAt(closestPlayer, 30.0F, 30.0F);
                  }

                  if (this.tickCount % 8 == 0 && this.zza != 0.0F) {
                     this.playStepSound(this.blockPosition(), SkiesBlocks.poison_stonebrick.defaultBlockState());
                  }

                  if (!this.isNoGravity()) {
                     this.setNoGravity(true);
                  }

                  if (this.distanceToSqr(this.getHome().getX(), this.getY(), this.getHome().getZ()) >= 2.0 && this.getPassengers().isEmpty()) {
                     this.getMoveControl().setWantedPosition(this.getHome().getX(), this.getY(), this.getHome().getZ(), 2.0);
                  }
               }
            }

            if ((this.getTarget() == null || this.isCeilingLocked()) && this.isCharging()) {
               this.setCharging(false);
            }

            if (this.getTimeOnCeiling() == 320 && this.random.nextBoolean()) {
               this.setTimeOnCeiling(0);
            }

            if (this.isCeilingLocked()) {
               this.oldPosRef = new HashMap<>();

               for (Player players : this.getPlayersInDungeonArea(25.0, false)) {
                  this.oldPosRef.put(players.getUUID(), players.position());
               }
            } else {
               this.oldPosRef = null;
            }
         } else {
            if (this.isCharging()) {
               for (int t = 0; t < 4; t++) {
                  this.spawnParticles(ParticleTypes.LARGE_SMOKE);
                  int i = Mth.floor(this.getX());
                  int j = Mth.floor(this.getY() - 0.2F);
                  int k = Mth.floor(this.getZ());
                  BlockPos blockPos = Positions.blockPos(i, j, k);
                  BlockState blockstate = this.level().getBlockState(blockPos);
                  if (!this.level().isEmptyBlock(blockPos)) {
                     this.level()
                        .addParticle(
                           new BlockParticleOption(ParticleTypes.BLOCK, blockstate).setPos(Positions.blockPos(i, j, k)),
                           this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth(),
                           this.getBoundingBox().minY + 0.1,
                           this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth(),
                           4.0 * (this.random.nextFloat() - 0.5),
                           0.5,
                           (this.random.nextFloat() - 0.5) * 4.0
                        );
                  }
               }
            }

            if (this.isStunned()) {
               for (int tx = 0; tx < 4; tx++) {
                  this.level()
                     .addParticle(
                        ParticleTypes.AMBIENT_ENTITY_EFFECT,
                        this.getX() + this.level().random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
                        this.getY() + this.level().random.nextFloat() * this.getBbHeight(),
                        this.getZ() + this.level().random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
                        0.19215687F,
                        0.5411765F,
                        0.015686275F
                     );
               }
            }
         }
      }
   }

   @Override
   public void invulnerableTick() {
      super.invulnerableTick();
      if (this.level().isClientSide()) {
         for (int t = 0; t < 4; t++) {
            this.spawnParticles(new PoisonData(false));
            this.spawnParticles(ParticleTypes.AMBIENT_ENTITY_EFFECT);
         }
      }
   }

   @Override
   public void peacefulTick() {
      this.setTimeOnCeiling(0);
      this.setCeilingLocked(false);
      this.setTimeSinceLocked(0);
      this.setStunned(false);
      this.ejectPassengers();
   }

   @Override
   public void load(CompoundTag compound) {
      super.load(compound);
      this.setCeilingLocked(compound.getBoolean("CelingLocked"));
      this.setCharging(compound.getBoolean("Charging"));
      this.setStunned(compound.getBoolean("Stunned"));
      this.setTimeOnCeiling(compound.getInt("TimeOnCeiling"));
      this.setTimeSinceLocked(compound.getInt("TimeSinceCeilingLocked"));
      this.setStunTime(compound.getInt("TimeStunned"));
      this.setTicksUntilNextScreech(compound.getInt("TicksUntilNextScreech"));
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("CelingLocked", this.isCeilingLocked());
      compound.putBoolean("Charging", this.isCharging());
      compound.putBoolean("Stunned", this.isStunned());
      compound.putInt("TimeOnCeiling", this.getTimeOnCeiling());
      compound.putInt("TimeSinceCeilingLocked", this.getTimeSinceLocked());
      compound.putInt("TimeStunned", this.getStunTime());
      compound.putInt("TicksUntilNextScreech", this.getTicksUntilNextScreech());
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (this.isAlliedTo(entityIn)) {
         return false;
      } else {
         if (entityIn instanceof Player player) {
            ItemStack playerItem = player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY;
            if (!playerItem.isEmpty() && !this.level().isClientSide) {
               if (SHIELDS.contains(playerItem.getItem())) {
                  this.level().broadcastEntityEvent(this, (byte)4);
                  this.setStunned(true);
                  this.level().broadcastEntityEvent(player, (byte)29);
                  this.level().broadcastEntityEvent(player, (byte)30);
                  player.disableShield(true);
                  SHIELDS.forEach(item -> player.getCooldowns().addCooldown(item, 300));
                  if (player.getUsedItemHand() != null) {
                     playerItem.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
                  }

                  return false;
               }

               if (playerItem.canPerformAction(ToolActions.SHIELD_BLOCK)) {
                  player.displayClientMessage(Component.translatable("gui.blue_skies.tooltip.arachnarch_incorrect_shield"), true);
                  player.disableShield(true);
                  this.level().broadcastEntityEvent(player, (byte)30);
                  player.getCooldowns().addCooldown(playerItem.getItem(), 800);
                  return super.doHurtTarget(entityIn);
               }
            }
         }

         return super.doHurtTarget(entityIn);
      }
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (!this.getPassengers().isEmpty()) {
         this.damageSinceGrabbed += amount;
      }

      if (source.isCreativePlayer()) {
         return super.hurt(source, amount);
      } else if (this.isCeilingLocked() && this.getPassengers().isEmpty()) {
         return super.hurt(source, Math.min(1.0F, amount));
      } else {
         return !this.isStunned() ? super.hurt(source, amount * 0.8F) : super.hurt(source, amount);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.setStunned(true);
            break;
         case 5:
            this.spawnParticles(ParticleTypes.POOF);
            break;
         case 6:
            this.clientScreechTime = 50;
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   public void positionRider(Entity passenger, MoveFunction callback) {
      if (this.hasPassenger(passenger)) {
         Vec3 vec3d = this.getViewVector(1.0F);
         callback.accept(
            passenger,
            this.getX() + vec3d.x * 3.0,
            this.getY()
               + passenger.getMyRidingOffset(this)
               + (this.isCeilingLocked() ? -Mth.cos(this.tickCount * 0.1F) : Mth.cos(this.tickCount * 0.1F)) * 0.05
               + (this.isCeilingLocked() ? -1.2F : 1.4),
            this.getZ() + vec3d.z * 3.0
         );
         if (this.isStunned() || this.holdingTime <= 0 || !this.level().isClientSide && this.damageSinceGrabbed > 15.0F || !this.isAlive()) {
            this.damageSinceGrabbed = 0.0F;
            passenger.stopRiding();
            this.playSound(SkiesSounds.ENTITY_ARACHNARCH_THROW, this.getSoundVolume(), 2.0F);
            passenger.hurtMarked = true;
            passenger.setDeltaMovement(vec3d.x() * 1.0, 0.5, vec3d.z() * 1.0);
         }

         if (this.damageSinceGrabbed > 15.0F) {
            passenger.setDeltaMovement(vec3d.x() * 1.0, 0.5, vec3d.z() * 1.0);
            if (passenger instanceof Player) {
               ((Player)passenger).hurtMarked = true;
            }
         }
      }
   }

   public void knockback(double strength, double x, double z) {
   }

   public void push(double x, double y, double z) {
   }

   public boolean canBeAffected(MobEffectInstance potioneffectIn) {
      return IMMUNE_EFFECTS.contains(potioneffectIn.getEffect()) ? false : super.canBeAffected(potioneffectIn);
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.8F;
   }

   public boolean canRiderInteract() {
      return true;
   }

   public boolean shouldRiderSit() {
      return false;
   }

   @Override
   public BossBarColor getBossBarColor() {
      return BossBarColor.PINK;
   }

   @Override
   public SkiesDungeonType getBossType() {
      return SkiesDungeonType.POISON;
   }

   @Override
   public int getProgression() {
      return 2;
   }

   @Override
   public boolean isEverbrightBoss() {
      return false;
   }

   public boolean isCeilingLocked() {
      return (Boolean)this.entityData.get(CEILING_LOCKED);
   }

   public void setCeilingLocked(boolean locked) {
      this.getNavigation().stop();
      if (locked) {
         this.setTimeOnCeiling(this.getTicksUntilNextScreech() == 0 && this.isSecondPhase() ? 610 : (this.random.nextInt(5) == 0 ? 300 : 600));
      } else {
         if (this.isCeilingLocked()) {
            this.playSound(SkiesSounds.ENTITY_ARACHNARCH_FLIP, this.getSoundVolume(), this.getVoicePitch());
         }

         this.setNoGravity(false);
         this.setTimeSinceLocked(0);
      }

      this.entityData.set(CEILING_LOCKED, locked);
   }

   public int getTimeOnCeiling() {
      return (Integer)this.entityData.get(TIME_ON_CEILING);
   }

   public void setTimeOnCeiling(int time) {
      this.entityData.set(TIME_ON_CEILING, time);
   }

   public int getTimeSinceLocked() {
      return (Integer)this.entityData.get(TIME_SINCE_LOCKED);
   }

   public void setTimeSinceLocked(int time) {
      this.entityData.set(TIME_SINCE_LOCKED, time);
   }

   public int getStunTime() {
      return (Integer)this.entityData.get(TIME_STUNNED);
   }

   public void setStunTime(int time) {
      this.entityData.set(TIME_STUNNED, time);
   }

   public int getTicksUntilNextScreech() {
      return (Integer)this.entityData.get(TICKS_UNTIL_NEXT_SCREECH);
   }

   public void setTicksUntilNextScreech(int time) {
      this.entityData.set(TICKS_UNTIL_NEXT_SCREECH, time);
   }

   @Override
   public boolean isStunned() {
      return (Boolean)this.entityData.get(STUNNED);
   }

   public void setStunned(boolean stunned) {
      this.entityData.set(STUNNED, stunned);
      if (stunned) {
         if (this.level().isClientSide) {
            this.playStunSound();
            return;
         }

         this.setStunTime(Math.max(20, 60 - this.getExtraPlayers() * 10));
      } else {
         this.setStunTime(0);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void playStunSound() {
      if (!this.isSilent() && this.level().isClientSide) {
         SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
         if (this.stunSound == null || this.stunSound != null && !soundHandler.isActive(this.stunSound)) {
            soundHandler.play(this.stunSound = new StunnedEntitySound(this, SkiesSounds.ENTITY_ARACHNARCH_STUNNED, this.getSoundVolume()));
         }
      }
   }

   public boolean isCharging() {
      return (Boolean)this.entityData.get(CHARGING);
   }

   public void setCharging(boolean charge) {
      this.entityData.set(CHARGING, charge);
   }

   public void setHolding(LivingEntity entity) {
      this.holdingTime = 100;
      if (!this.level().isClientSide) {
         entity.startRiding(this);
         this.damageSinceGrabbed = 0.0F;
      }
   }

   public MobType getMobType() {
      return MobType.ARTHROPOD;
   }

   protected SoundEvent getAmbientSound() {
      return this.isStunned() ? null : SkiesSounds.ENTITY_ARACHNARCH_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_ARACHNARCH_HURT;
   }

   protected SoundEvent getDeathSound() {
      this.playSound(SkiesSounds.ENTITY_ARACHNARCH_DEATH, this.getSoundVolume(), 1.0F);
      return super.getDeathSound();
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 0.5F);
   }

   public float getVoicePitch() {
      return super.getVoicePitch();
   }

   @Override
   protected float getSoundVolume() {
      return 2.0F;
   }

   public boolean onClimbable() {
      return this.isBesideClimbableBlock();
   }

   public boolean isBesideClimbableBlock() {
      return ((Byte)this.entityData.get(CLIMBING) & 1) != 0;
   }

   public void setBesideClimbableBlock(boolean climbing) {
      byte b0 = (Byte)this.entityData.get(CLIMBING);
      if (climbing) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 = (byte)(b0 & -2);
      }

      this.entityData.set(CLIMBING, b0);
   }

   public void makeStuckInBlock(BlockState state, Vec3 motionVec) {
      if (state.getBlock() != Blocks.COBWEB && state.getBlock() != SkiesBlocks.spider_webbing) {
         super.makeStuckInBlock(state, motionVec);
      }
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public void performRangedAttack(LivingEntity target, float distanceFactor) {
      if (!(target instanceof Player)) {
         this.spit(target);
      }

      for (Player players : this.getPlayersInDungeonArea()) {
         this.spit(players);
      }
   }

   private void spit(LivingEntity target) {
      this.setYRot(this.yHeadRot);
      if (this.getDifficultyID() == ISkyBoss.BossLevel.PLATINUM.getId()
         && this.isSecondPhase()
         && this.distanceToSqr(this.getHome().getX(), this.getY(), this.getHome().getZ()) > 2.0) {
         VenomSpitEntity venomSpit = new VenomSpitEntity(this.level(), this);
         double d0 = target.getX() - this.getX();
         double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - venomSpit.getY();
         double d2 = target.getZ() - this.getZ();
         float f = Mth.sqrt((float)(d0 * d0 + d2 * d2)) * 0.2F;
         venomSpit.shoot(d0, d1 + f, d2, 1.5F, 0.0F);
         this.level().addFreshEntity(venomSpit);
         this.playSound(SkiesSounds.ENTITY_ARACHNARCH_SPIT, this.getSoundVolume(), 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
      } else if (this.distanceTo(target) > 8.0F) {
         Vec3 predictedVec = target.position();
         if (target instanceof Player player
            && this.getDifficultyID() > ISkyBoss.BossLevel.BRONZE.getId()
            && this.oldPosRef != null
            && this.oldPosRef.containsKey(player.getUUID())) {
            predictedVec = target.position().add(target.position().subtract(this.oldPosRef.get(player.getUUID())).scale(8.0));
         }

         VenomSpitEntity venomSpit = new VenomSpitEntity(this.level(), this);
         double x = predictedVec.x - venomSpit.getX();
         double y = target.getBoundingBox().minY + target.getEyeHeight() / 3.0F - venomSpit.getY();
         double z = predictedVec.z - venomSpit.getZ();
         float f = Mth.sqrt((float)(x * x + z * z)) * (this.distanceTo(target) < 9.0 ? 0.0F : this.distanceTo(target) * 0.01F);
         venomSpit.shoot(x, y + f, z, 1.5F, 0.0F);
         venomSpit.setDeadly(true);
         this.level().addFreshEntity(venomSpit);
         this.playSound(SkiesSounds.ENTITY_ARACHNARCH_SPIT, this.getSoundVolume(), 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
      } else {
         VenomBombEntity venomBomb = new VenomBombEntity(this.level(), this);
         venomBomb.effect = new MobEffectInstance(SkiesEffects.DEADLY_VENOM, 100, this.getDifficultyID());
         venomBomb.setXRot(venomBomb.getXRot() + 20.0F);
         venomBomb.setPos(this.getX(), this.getY() - 1.0, this.getZ());
         Vec3 look = this.getViewVector(1.0F);
         venomBomb.shoot(look.x() * 0.1F, 0.0, look.z() * 0.1F, 0.15F, 10.0F);
         this.level().addFreshEntity(venomBomb);
         this.playSound(SkiesSounds.ENTITY_ARACHNARCH_VENOM_DROP, this.getSoundVolume(), 1.0F);
      }
   }

   public List<NestedSpiderEntity> getSpidersInDungeonArea(double distance) {
      return (List<NestedSpiderEntity>)(this.getHome() != null && this.getHome() != BlockPos.ZERO
         ? this.level()
            .getEntitiesOfClass(
               NestedSpiderEntity.class,
               new AABB(
                  this.getHome().getX() - distance,
                  this.getHome().getY() - 1.0,
                  this.getHome().getZ() - distance,
                  this.getHome().getX() + distance,
                  this.getHome().getY() + 10.0,
                  this.getHome().getZ() + distance
               )
            )
         : ImmutableList.of());
   }

   public void slingToCeiling() {
      this.setCeilingLocked(true);
      this.setDeltaMovement(0.0, 3.0, 0.0);
      this.playSound(SkiesSounds.ENTITY_ARACHNARCH_SLING, this.getSoundVolume(), this.getVoicePitch());
   }

   protected void tickDeath() {
      boolean flag = this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
      if (this.isCeilingLocked()) {
         this.setCeilingLocked(false);
      }

      this.deathTicks++;
      if (this.deathTicks < 20) {
         super.tickDeath();
      }

      float money = 40.0F;
      if (this.deathTicks > 30 && this.deathTicks % 5 == 0 && flag) {
         this.dropExperience(Mth.floor(money * 0.08F));
      }

      if (this.deathTicks >= 100 && !this.level().isClientSide && !this.level().isClientSide) {
         if (this.deathTicks >= 120) {
            this.discard();
         }

         if (this.deathTicks % 2 == 0) {
            for (int i = 0; i < 20; i++) {
               this.level().broadcastEntityEvent(this, (byte)5);
            }
         }
      }

      if (this.isCharging()) {
         this.setCharging(false);
      }
   }

   private void dropExperience(int amount) {
      while (amount > 0) {
         int i = ExperienceOrb.getExperienceValue(amount);
         amount -= i;
         this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY(), this.getZ(), i));
      }
   }

   public void kill() {
      this.discard();
   }

   public boolean isAlliedTo(Entity entityIn) {
      return entityIn.getType().is(SkiesEntityTags.POISON_ALLIES)
         ? this.getTeam() == null && entityIn.getTeam() == null
         : super.isAlliedTo(entityIn) && entityIn != null && entityIn != this;
   }

   class LungeAtTargetGoal extends SkiesBossEntity.AbstractBossGoal {
      private final ArachnarchEntity boss = ArachnarchEntity.this;
      private int chargeTime;

      public LungeAtTargetGoal() {
      }

      public boolean canUse() {
         return this.boss.getTarget() == null
            ? false
            : !this.boss.isCeilingLocked()
               && !this.boss.isStunned()
               && this.boss.getInvulTime() <= 0
               && this.boss.getPassengers().isEmpty()
               && this.boss.getTimeSinceLocked() <= this.getTimeNeededForLunge();
      }

      public boolean canContinueToUse() {
         return !this.boss.isCeilingLocked()
            && this.boss.getTarget() != null
            && this.chargeTime > 0
            && !this.boss.isStunned()
            && this.boss.getPassengers().isEmpty();
      }

      public void start() {
         List<Player> playersInDungeon = this.boss.getPlayersInDungeonArea();

         for (Player players : playersInDungeon) {
            if (players != this.boss.getTarget() && this.boss.random.nextBoolean() && playersInDungeon.size() > 1) {
               this.boss.setTarget(players);
            }
         }

         this.chargeTime = 80;
         this.boss.setCharging(true);
         this.boss.playSound(SkiesSounds.ENTITY_ARACHNARCH_CHARGE, this.boss.getSoundVolume(), this.boss.getVoicePitch());
      }

      public void tick() {
         LivingEntity target = this.boss.getTarget();
         if (target != null) {
            Level world = this.boss.level();
            this.boss.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.chargeTime > 0) {
               this.chargeTime--;
            }

            if (this.chargeTime == 20) {
               this.boss.setCharging(false);
               Vec3 vec3d = this.boss.getDeltaMovement();
               Vec3 vec3d1 = new Vec3(target.getX() - this.boss.getX(), 0.0, target.getZ() - this.boss.getZ());
               if (vec3d1.lengthSqr() > 1.0E-7) {
                  vec3d1 = vec3d1.normalize().scale(3.0).add(vec3d.scale(3.0));
               }

               this.boss.setDeltaMovement(vec3d1.x, 0.4, vec3d1.z);
               this.boss.playSound(SkiesSounds.ENTITY_ARACHNARCH_LUNGE, this.boss.getSoundVolume(), this.boss.getVoicePitch());
            }

            for (Entity entity : world.getEntities(this.boss, this.boss.getBoundingBox().inflate(this.chargeTime <= 5 ? 0.5 : 0.0))) {
               if (entity instanceof LivingEntity && ((LivingEntity)entity).hurtTime <= 0) {
                  if (this.boss.isStunned()) {
                     return;
                  }

                  LivingEntity living = (LivingEntity)entity;
                  boolean attack = this.boss.doHurtTarget(living);
                  if (attack && this.boss.getPassengers().isEmpty() && (this.boss.isCharging() || this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F)) {
                     this.boss.playSound(SkiesSounds.ENTITY_ARACHNARCH_ATTACK, this.boss.getSoundVolume(), this.boss.getVoicePitch());
                     if (living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue() < 1.0) {
                        this.boss.setHolding(living);
                     }

                     return;
                  }
               }
            }
         }
      }

      private int getTimeNeededForLunge() {
         return 180;
      }
   }

   class ScreechAttackGoal extends SkiesBossEntity.AbstractBossGoal {
      private final ArachnarchEntity boss = ArachnarchEntity.this;
      private int screechTime;

      public ScreechAttackGoal() {
      }

      public boolean canUse() {
         boolean closeToHome = this.boss.distanceToSqr(this.boss.getHome().getX(), this.boss.getY(), this.boss.getHome().getZ()) <= 2.0;
         return this.boss.isCeilingLocked()
            && this.boss.getTarget() != null
            && !this.boss.isStunned()
            && this.boss.getInvulTime() == 0
            && this.boss.getPassengers().isEmpty()
            && this.boss.isSecondPhase()
            && this.boss.getTicksUntilNextScreech() <= 0
            && this.boss.getTimeOnCeiling() > 600
            && closeToHome;
      }

      public boolean canContinueToUse() {
         return this.boss.isCeilingLocked()
            && this.boss.getTarget() != null
            && this.screechTime > 0
            && !this.boss.isStunned()
            && this.boss.getPassengers().isEmpty();
      }

      public void start() {
         this.screechTime = 80;
      }

      public void tick() {
         LivingEntity target = this.boss.getTarget();
         if (target != null) {
            this.boss.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.screechTime > 0) {
               this.screechTime--;
            }

            if (this.screechTime == 75) {
               this.boss.level().broadcastEntityEvent(this.boss, (byte)6);
               this.boss.playSound(SkiesSounds.ENTITY_ARACHNARCH_SCREECH, this.boss.getSoundVolume(), this.boss.getVoicePitch());
            }

            if (this.screechTime == 79 && !this.boss.level().isClientSide) {
               for (Player players : this.boss.getPlayersInDungeonArea()) {
                  players.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, true, false));
               }
            }

            if (this.screechTime == 20) {
               BlockPos homePos = this.boss.getHome();
               byte radius = 15;
               if (this.boss.level() instanceof ServerLevel) {
                  ServerLevel world = (ServerLevel)this.boss.level();
                  PoiManager pointofinterestmanager = world.getPoiManager();
                  pointofinterestmanager.ensureLoadedAndValid(this.boss.level(), homePos, radius);
                  Stream<PoiRecord> optional = world.getPoiManager()
                     .getInSquare(poiType -> poiType.is(SkiesPoiTypes.SPIDER_NEST.getKey()), homePos, radius, Occupancy.ANY);
                  optional.forEach(poi -> {
                     BlockPos pos = poi.getPos();
                     BlockState funny = this.boss.level().getBlockState(pos);
                     if (funny.getBlock() instanceof SpiderNestBlock && this.boss.level().random.nextFloat() < 0.7F) {
                        SpiderNestBlock.spawnSpiders(funny, this.boss.level(), pos, SkiesEntityTypes.NESTED_SPIDER);
                     }
                  });
               }
            }

            if (this.screechTime <= 1) {
               this.boss.setTicksUntilNextScreech(1200);
            }
         }
      }
   }

   class VenomAttackGoal extends RangedAttackGoal {
      private final ArachnarchEntity boss = ArachnarchEntity.this;
      private boolean nestedSpidersDead = this.boss.getSpidersInDungeonArea(15.0).isEmpty();

      public VenomAttackGoal(RangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
         super(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
      }

      public boolean canUse() {
         return this.nestedSpidersDead && super.canUse()
            ? this.boss.isCeilingLocked()
               && this.boss.distanceToSqr(this.boss.getHome().getX(), this.boss.getY(), this.boss.getHome().getZ()) <= 2.0
               && this.boss.getTimeOnCeiling() > 300
               && this.boss.getTimeOnCeiling() <= 600
            : false;
      }

      public boolean canContinueToUse() {
         return this.nestedSpidersDead && super.canContinueToUse()
            ? this.boss.isCeilingLocked()
               && this.boss.distanceToSqr(this.boss.getHome().getX(), this.boss.getY(), this.boss.getHome().getZ()) <= 2.0
               && this.boss.getTimeOnCeiling() > 300
               && this.boss.getTimeOnCeiling() <= 600
            : false;
      }
   }

   class VenomWalkingAttackGoal extends RangedAttackGoal {
      private final ArachnarchEntity boss = ArachnarchEntity.this;
      private boolean nestedSpidersDead = this.boss.getSpidersInDungeonArea(15.0).isEmpty();

      public VenomWalkingAttackGoal(RangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
         super(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
      }

      public boolean canUse() {
         return this.nestedSpidersDead && super.canUse() && this.boss.getPassengers().isEmpty()
            ? this.boss.getDifficultyID() == ISkyBoss.BossLevel.PLATINUM.getId()
               && this.boss.isCeilingLocked()
               && this.boss.getTimeOnCeiling() <= 300
               && this.boss.isSecondPhase()
            : false;
      }

      public boolean canContinueToUse() {
         return this.nestedSpidersDead && super.canContinueToUse() && this.boss.getPassengers().isEmpty()
            ? this.boss.getDifficultyID() == ISkyBoss.BossLevel.PLATINUM.getId()
               && this.boss.isCeilingLocked()
               && this.boss.getTimeOnCeiling() <= 300
               && this.boss.isSecondPhase()
            : false;
      }
   }

   class WalkAndDropGoal extends SkiesBossEntity.AbstractBossGoal {
      private final ArachnarchEntity boss = ArachnarchEntity.this;
      private int walkTime;

      public WalkAndDropGoal() {
      }

      public boolean canUse() {
         return !this.boss.getPassengers().isEmpty()
            ? false
            : this.boss.isCeilingLocked() && this.boss.getTarget() != null && this.boss.getTimeOnCeiling() < 300;
      }

      public boolean canContinueToUse() {
         return !this.boss.getPassengers().isEmpty() ? false : this.boss.isCeilingLocked() && this.boss.getTarget() != null && this.walkTime > 0;
      }

      public void start() {
         this.walkTime = 300;
      }

      public void tick() {
         LivingEntity target = this.boss.getTarget();
         if (target != null) {
            Level world = this.boss.level();
            this.boss.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.walkTime > 0) {
               this.walkTime--;
               if (this.boss.distanceTo(target) >= 2.0F) {
                  this.boss.getMoveControl().setWantedPosition(target.getX(), this.boss.getY(), target.getZ(), 15.0);
               }

               if (this.boss.tickCount % 10 == 0) {
                  Vec3 look = this.boss.getViewVector(1.0F);
                  VenomBombEntity venomBomb = new VenomBombEntity(world, this.boss);
                  venomBomb.effect = new MobEffectInstance(SkiesEffects.DEADLY_VENOM, 50, this.boss.getDifficultyID());
                  venomBomb.setXRot(venomBomb.getXRot() + 20.0F);
                  venomBomb.setPos(this.boss.getX() - look.x() * 1.0, this.boss.getY() - 1.0, this.boss.getZ() - look.z() * 1.0);
                  venomBomb.shoot(look.x() * 0.1F, 0.0, look.z() * 0.1F, 0.15F, 10.0F);
                  venomBomb.lingering = true;
                  world.addFreshEntity(venomBomb);
                  this.boss.playSound(SkiesSounds.ENTITY_ARACHNARCH_VENOM_DROP, 1.0F, 1.0F);
               }
            }
         }
      }
   }
}
