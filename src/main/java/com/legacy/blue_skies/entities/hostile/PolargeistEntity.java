package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PolargeistEntity extends SkiesMonsterEntity {
   private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(PolargeistEntity.class, EntityDataSerializers.BOOLEAN);
   private float standTicks;
   private float prevStandTicks;

   public PolargeistEntity(EntityType<? extends PolargeistEntity> type, Level world) {
      super(type, world);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.7F, true) {
         protected void checkAndPerformAttack(LivingEntity enemy) {
            double d0 = (enemy.getBbWidth() + 3.0F) * (enemy.getBbWidth() + 3.0F);
            if (this.mob.distanceToSqr(enemy) <= d0 && this.isTimeToAttack()) {
               this.resetAttackCooldown();
               this.mob.doHurtTarget(enemy);
               PolargeistEntity.this.setStanding(false);
            } else if (this.mob.distanceToSqr(enemy) <= d0 * 2.0) {
               if (this.isTimeToAttack()) {
                  PolargeistEntity.this.setStanding(false);
                  this.resetAttackCooldown();
               }

               if (this.getTicksUntilNextAttack() <= 10) {
                  PolargeistEntity.this.setStanding(true);
               }
            } else {
               this.resetAttackCooldown();
               PolargeistEntity.this.setStanding(false);
            }
         }
      });
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 26.0).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 4.5);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(STANDING, false);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
   }

   public void load(CompoundTag compound) {
      super.load(compound);
   }

   public void tick() {
      super.tick();
      if ((!this.isAlive() || this.getTarget() == null) && this.isStanding() && !this.level().isClientSide) {
         this.setStanding(false);
      }

      if (this.level().isClientSide()) {
         if (this.standTicks != this.prevStandTicks) {
            this.refreshDimensions();
         }

         this.prevStandTicks = this.standTicks;
         if (this.isStanding()) {
            this.standTicks = Mth.clamp(this.standTicks + 1.0F, 0.0F, 6.0F);
         } else {
            this.standTicks = Mth.clamp(this.standTicks - 1.0F, 0.0F, 6.0F);
         }
      }
   }

   public EntityDimensions getDimensions(Pose poseIn) {
      if (this.standTicks > 0.0F) {
         float f = this.standTicks / 6.0F;
         float f1 = 1.0F + f;
         return super.getDimensions(poseIn).scale(1.0F, f1);
      } else {
         return super.getDimensions(poseIn);
      }
   }

   public boolean isStanding() {
      return (Boolean)this.entityData.get(STANDING);
   }

   public void setStanding(boolean standing) {
      this.entityData.set(STANDING, standing);
   }

   @OnlyIn(Dist.CLIENT)
   public float getStandingAnimationScale(float p_189795_1_) {
      return Mth.lerp(p_189795_1_, this.prevStandTicks, this.standTicks) / 6.0F;
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_POLARGEIST_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_POLARGEIST_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_POLARGEIST_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return 1.2F;
   }

   public boolean doHurtTarget(Entity entityIn) {
      this.playSound(SkiesSounds.ENTITY_POLARGEIST_ATTACK, this.getSoundVolume(), this.getVoicePitch());
      return super.doHurtTarget(entityIn);
   }

   public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
      return randomBoolean && super.checkSpawnRules(worldIn, spawnReasonIn);
   }

   @Override
   public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
      return 0.0F;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   public MobType getMobType() {
      return MobType.UNDEAD;
   }
}
