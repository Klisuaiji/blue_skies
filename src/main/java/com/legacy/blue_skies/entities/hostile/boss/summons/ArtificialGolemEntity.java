package com.legacy.blue_skies.entities.hostile.boss.summons;

import com.legacy.blue_skies.entities.hostile.boss.SummonerEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBoss;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBossMob;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesDamageTypes;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import java.util.EnumSet;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ArtificialGolemEntity extends SkiesMonsterEntity implements ISkyBossMob, RangedAttackMob {
   public static final EntityDataAccessor<Boolean> ENRAGED = SynchedEntityData.defineId(ArtificialGolemEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<Boolean> RANGED = SynchedEntityData.defineId(ArtificialGolemEntity.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(ArtificialGolemEntity.class, EntityDataSerializers.BOOLEAN);
   private int attackTimer;
   private LivingEntity caster;
   private UUID casterUuid;

   public ArtificialGolemEntity(EntityType<? extends ArtificialGolemEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 0;
   }

   public ArtificialGolemEntity(Level worldIn, LivingEntity casterIn) {
      this(SkiesEntityTypes.ARTIFICIAL_GOLEM, worldIn);
      this.setCaster(casterIn);
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficulty, reason, spawnDataIn);
      this.populateDefaultEquipmentEnchantments(worldIn.getRandom(), difficulty);
      if (reason == MobSpawnType.SPAWN_EGG) {
         float chance = this.level().random.nextFloat();
         if (chance < 0.3F) {
            this.setRanged(true);
         } else if (chance < 0.6F) {
            this.setEnraged(true);
         }
      }

      return spawnDataIn;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new ArtificialGolemEntity.MeleeGolemAttackGoal(this, 1.0, true));
      this.goalSelector.addGoal(1, new ArtificialGolemEntity.RangedGolemAttackGoal<>(this, 1.0, 20, 15.0F));
      this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, entity -> !this.playerSpawned()));
      this.targetSelector
         .addGoal(
            3,
            new NearestAttackableTargetGoal(
               this, Mob.class, 5, false, false, entity -> this.playerSpawned() && entity instanceof Enemy && !(entity instanceof Creeper)
            )
         );
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(ENRAGED, this.phaseTwoQualified());
      builder.define(RANGED, false);
      builder.define(ATTACKING, false);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.22F);
   }

   protected int decreaseAirSupply(int air) {
      return air;
   }

   public void aiStep() {
      super.aiStep();
      if (this.attackTimer > 0) {
         this.attackTimer--;
      }

      if (!this.isNoAi()) {
         int lifeTicks = this.playerSpawned() ? 1200 : 100;
         if (this.getCaster() != null) {
            if (this.getTarget() != null) {
               if (!this.getCaster().isAlive() || this.getCaster().getHealth() == 0.0F) {
                  this.spawnAnim();
                  this.discard();
               }
            } else if (!this.level().isClientSide && (this.playerSpawned() && !this.getCaster().isAlive() || this.tickCount > lifeTicks)) {
               this.spawnAnim();
               this.discard();
            }
         }

         if (this.getTarget() != null) {
            float speed = 1.0F;
            if (!this.getSensing().hasLineOfSight(this.getTarget())) {
               this.getNavigation().moveTo(this.getTarget(), speed);
            } else if (this.distanceTo(this.getTarget()) > 1.5F) {
               this.getMoveControl().setWantedPosition(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), speed);
            }
         } else if (this.playerSpawned()) {
            if (!this.isRanged() && this.getCaster().getHealth() < this.getCaster().getMaxHealth() != this.isEnraged()) {
               this.playSound(this.isEnraged() ? SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_DEACTIVATE : SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_ACTIVATE, 1.0F, 1.0F);
               this.setEnraged(this.getCaster().getHealth() < this.getCaster().getMaxHealth());
            }

            if (this.distanceTo(this.getCaster()) > 15.0F) {
               this.teleportTo(this.getCaster().getX(), this.getCaster().getY(), this.getCaster().getZ());
            } else if (this.distanceTo(this.getCaster()) > 7.0F) {
               this.getNavigation().moveTo(this.getCaster(), 1.0);
               this.getLookControl().setLookAt(this.getCaster(), 40.0F, 40.0F);
            }
         }
      }
   }

   public void load(CompoundTag tagCompound) {
      super.load(tagCompound);
      if (tagCompound.contains("PhaseTwo")) {
         this.setEnraged(tagCompound.getBoolean("PhaseTwo"));
      } else {
         this.setEnraged(tagCompound.getBoolean("IsEnraged"));
      }

      if (tagCompound.contains("IsArcher")) {
         this.setRanged(tagCompound.getBoolean("IsArcher"));
      } else {
         this.setRanged(tagCompound.getBoolean("IsRanged"));
      }

      if (tagCompound.contains("OwnerUUID")) {
         this.casterUuid = tagCompound.getUUID("OwnerUUID");
      }

      if (tagCompound.contains("GolemAttacking")) {
         this.setAttacking(tagCompound.getBoolean("GolemAttacking"));
      }
   }

   public void addAdditionalSaveData(CompoundTag tagCompound) {
      super.addAdditionalSaveData(tagCompound);
      if (this.casterUuid != null) {
         tagCompound.putUUID("OwnerUUID", this.casterUuid);
      }

      tagCompound.putBoolean("IsEnraged", this.isEnraged());
      tagCompound.putBoolean("IsRanged", this.isRanged());
      tagCompound.putBoolean("GolemAttacking", this.getAttacking());
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (entityIn != null) {
         this.level().broadcastEntityEvent(this, (byte)4);
         this.attackTimer = 10;
         float damage = (this.playerSpawned() ? 2.0F : 0.0F) + (this.isEnraged() ? 3.5F : 2.5F);
         boolean flag = entityIn.hurt(
            this.getCaster() != null ? SkiesDamageSources.get(entityIn.level()).summon(this, this.getCaster()) : this.damageSources().mobAttack(this), damage
         );
         if (flag) {
            this.doEnchantDamageEffects(this, entityIn);
         }

         this.playSound(SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_ATTACK, 1.0F, 1.0F);
         return flag;
      } else {
         return super.doHurtTarget(entityIn);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      if (id == 4) {
         this.attackTimer = 10;
         this.playSound(SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_ATTACK, 1.0F, 1.0F);
      } else {
         super.handleEntityEvent(id);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   public boolean isAlliedTo(Entity entityIn) {
      if (entityIn == null) {
         return false;
      } else if (this.getCaster() != null && entityIn.equals(this.getCaster())) {
         return true;
      } else if (entityIn instanceof Vex) {
         return this.isAlliedTo(((Vex)entityIn).getOwner());
      } else {
         return entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getType().is(net.minecraft.tags.EntityTypeTags.ILLAGER)
            ? this.getTeam() == null && entityIn.getTeam() == null
            : super.isAlliedTo(entityIn) && entityIn != this;
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      return source.isIndirect() && source.getEntity() instanceof ArtificialGolemEntity
         ? false
         : !source.is(SkiesDamageTypes.STRANGE_LIGHTNING.getKey()) && super.hurt(source, amount);
   }

   public void setTarget(LivingEntity entitylivingbaseIn) {
      super.setTarget(entitylivingbaseIn);
      if (entitylivingbaseIn != null) {
         this.setAttacking(true);
      } else {
         this.setAttacking(false);
      }
   }


   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_ARTIFICIAL_GOLEM_DEATH;
   }

   public float getVoicePitch() {
      return EntityUtil.getSoundPitchWithStart(this.random, 0.9F, 0.15F);
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
   }

   public void setRanged(boolean archer) {
      this.entityData.set(RANGED, archer);
      if (archer) {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
         this.setHealth(this.getMaxHealth());
      }
   }

   public boolean isRanged() {
      return (Boolean)this.entityData.get(RANGED);
   }

   public void setAttacking(boolean att) {
      this.entityData.set(ATTACKING, att);
   }

   public boolean getAttacking() {
      return (Boolean)this.entityData.get(ATTACKING);
   }

   public void setCaster(LivingEntity entityIn) {
      this.caster = entityIn;
      this.casterUuid = entityIn == null ? null : entityIn.getUUID();
   }

   public LivingEntity getCaster() {
      if (this.caster == null && this.casterUuid != null && this.level() instanceof ServerLevel) {
         Entity entity = ((ServerLevel)this.level()).getEntity(this.casterUuid);
         if (entity instanceof LivingEntity) {
            this.caster = (LivingEntity)entity;
         }
      }

      return this.caster;
   }

   public void setEnraged(boolean phaseTwo) {
      if (!this.isRanged()) {
         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(phaseTwo ? (this.shouldBuff() ? 0.27F : 0.25F) : (this.shouldBuff() ? 0.25F : 0.22F));
         this.entityData.set(ENRAGED, phaseTwo);
      }
   }

   public boolean isEnraged() {
      return (Boolean)this.entityData.get(ENRAGED);
   }

   public boolean phaseTwoQualified() {
      return this.getCaster() != null && this.getCaster().getHealth() == this.getCaster().getHealth() / this.getCaster().getMaxHealth();
   }

   public boolean playerSpawned() {
      return this.getCaster() != null && this.getCaster() instanceof Player;
   }

   private boolean shouldBuff() {
      if (this.getCaster() != null && this.getCaster() instanceof SummonerEntity) {
         SummonerEntity summoner = (SummonerEntity)this.getCaster();
         if (summoner.getDifficultyID() >= ISkyBoss.BossLevel.GOLD.getId()) {
            return true;
         }
      }

      return false;
   }

   public void performRangedAttack(LivingEntity target, float distanceFactor) {
      ItemStack stack = new ItemStack(Items.ARROW);
      AbstractArrow abstractarrowentity = ProjectileUtil.getMobArrow(this, stack, distanceFactor);
      abstractarrowentity = ((BowItem)new ItemStack(Items.BOW).getItem()).customArrow(abstractarrowentity, stack);
      double d0 = target.getX() - this.getX();
      double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - abstractarrowentity.getY();
      double d2 = target.getZ() - this.getZ();
      double d3 = Mth.sqrt((float)(d0 * d0 + d2 * d2));
      abstractarrowentity.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, 14 - this.level().getDifficulty().getId() * 4);
      this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
      this.level().addFreshEntity(abstractarrowentity);
   }

   public boolean canChangeDimensions() {
      return false;
   }

   protected boolean canRide(Entity pEntity) {
      return false;
   }

   class MeleeGolemAttackGoal extends MeleeAttackGoal {
      private int failedPathFindingPenalty = 0;
      private double targetX;
      private double targetY;
      private double targetZ;
      private boolean longMemory;
      private boolean canPenalize = false;

      public MeleeGolemAttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
         super(creature, speedIn, useLongMemory);
         this.longMemory = useLongMemory;
      }

      public boolean canUse() {
         ArtificialGolemEntity golem = (ArtificialGolemEntity)this.mob;
         return super.canUse() && !golem.isRanged();
      }

      public boolean canContinueToUse() {
         ArtificialGolemEntity golem = (ArtificialGolemEntity)this.mob;
         return super.canContinueToUse() && !golem.isRanged();
      }

      public void tick() {
         if ((this.mob == null || this.mob.getTarget() != null) && this.mob != null) {
            LivingEntity livingentity = this.mob.getTarget();
            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.longMemory || this.mob.getSensing().hasLineOfSight(livingentity))
               && this.ticksUntilNextPathRecalculation <= 0
               && (
                  this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
                     || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0
                     || this.mob.getRandom().nextFloat() < 0.05F
               )) {
               this.targetX = livingentity.getX();
               this.targetY = livingentity.getY();
               this.targetZ = livingentity.getZ();
               this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
               if (this.canPenalize) {
                  this.ticksUntilNextPathRecalculation = this.ticksUntilNextPathRecalculation + this.failedPathFindingPenalty;
                  if (this.mob.getNavigation().getPath() != null) {
                     Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
                     if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1.0) {
                        this.failedPathFindingPenalty = 0;
                     } else {
                        this.failedPathFindingPenalty += 10;
                     }
                  } else {
                     this.failedPathFindingPenalty += 10;
                  }
               }

               if (d0 > 1024.0) {
                  this.ticksUntilNextPathRecalculation += 10;
               } else if (d0 > 256.0) {
                  this.ticksUntilNextPathRecalculation += 5;
               }
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingentity);
         }
      }
   }

   class RangedGolemAttackGoal<T extends ArtificialGolemEntity & RangedAttackMob> extends Goal {
      private final T entity;
      private final double moveSpeedAmp;
      private int attackCooldown;
      private final float maxAttackDistance;
      private int seeTime;
      private boolean strafingClockwise;
      private boolean strafingBackwards;
      private int strafingTime = -1;

      public RangedGolemAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
         this.entity = mob;
         this.moveSpeedAmp = moveSpeedAmpIn;
         this.attackCooldown = attackCooldownIn;
         this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      public void setAttackCooldown(int attackCooldownIn) {
         this.attackCooldown = attackCooldownIn;
      }

      public boolean canUse() {
         return this.entity.getTarget() == null ? false : this.entity.isRanged();
      }

      public boolean canContinueToUse() {
         return this.entity.isRanged() && (this.canUse() || !this.entity.getNavigation().isDone());
      }

      public void start() {
         super.start();
         this.entity.setAggressive(true);
      }

      public void stop() {
         super.stop();
         this.entity.setAggressive(false);
         this.seeTime = 0;
         this.entity.stopUsingItem();
      }

      public void tick() {
         LivingEntity livingentity = this.entity.getTarget();
         if (livingentity != null) {
            double d0 = this.entity.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ());
            boolean flag = this.entity.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
               this.seeTime = 0;
            }

            if (flag) {
               this.seeTime++;
            } else {
               this.seeTime--;
            }

            if (!(d0 > this.maxAttackDistance) && this.seeTime >= 20) {
               this.entity.getNavigation().stop();
               this.strafingTime++;
            } else {
               this.entity.getNavigation().moveTo(livingentity, this.moveSpeedAmp);
               this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
               if (this.entity.getRandom().nextFloat() < 0.3) {
                  this.strafingClockwise = !this.strafingClockwise;
               }

               if (this.entity.getRandom().nextFloat() < 0.3) {
                  this.strafingBackwards = !this.strafingBackwards;
               }

               this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
               if (d0 > this.maxAttackDistance * 0.75F) {
                  this.strafingBackwards = false;
               } else if (d0 < this.maxAttackDistance * 0.25F) {
                  this.strafingBackwards = true;
               }

               this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
               this.entity.lookAt(livingentity, 30.0F, 30.0F);
            } else {
               this.entity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (!flag && this.seeTime < -60) {
               this.entity.stopUsingItem();
            } else if (flag && this.entity.tickCount % 40 == 0) {
               this.entity.stopUsingItem();
               this.entity.performRangedAttack(livingentity, BowItem.getPowerForTime(20 + ArtificialGolemEntity.this.random.nextInt(5)));
            }
         }
      }
   }
}
