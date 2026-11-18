package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.passive.CrogreEntity;
import com.legacy.blue_skies.entities.util.MovingEntitySound;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl.Operation;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EmberbackEntity extends SkiesMonsterEntity {
   private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EmberbackEntity.class, EntityDataSerializers.BOOLEAN);
   public float wingRotation;
   public float destPos;
   public float prevDestPos;
   public float prevWingRotation;
   public int attackTimer;
   @OnlyIn(Dist.CLIENT)
   private SoundInstance flapSound;

   public EmberbackEntity(EntityType<? extends EmberbackEntity> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new EmberbackEntity.AIMoveControl(this);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(2, new AvoidEntityGoal(this, CrogreEntity.class, 8.0F, 0.6, 0.6));
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 0.4F, true));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public void move(MoverType typeIn, Vec3 pos) {
      super.move(typeIn, this.isNoAi() ? Vec3.ZERO : pos);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.getEntityData().define(FLYING, false);
   }

   public void setFlying(boolean type) {
      this.setNoGravity(type);
      this.entityData.set(FLYING, type);
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public boolean isFlying() {
      return (Boolean)this.entityData.get(FLYING);
   }

   public void tick() {
      super.tick();
      if (this.attackTimer > 0) {
         this.attackTimer--;
      }

      if (this.level().isClientSide) {
         this.updateWingRotation();
         if (this.isFlying() || this.attackTimer > 0) {
            for (int i = 0; i < 2; i++) {
               this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
         }
      }

      if (!this.isNoAi() && this.isAlive() && !this.wasEyeInWater) {
         if (this.getTarget() != null) {
            LivingEntity entitylivingbase = this.getTarget();
            if (this.distanceTo(entitylivingbase) <= 4.0F) {
               this.setFlying(false);
            } else if (this.isFlying()) {
               if (this.onGround() || !this.level().isEmptyBlock(this.blockPosition().below())) {
                  this.setDeltaMovement(this.getDeltaMovement().x(), 0.5, this.getDeltaMovement().z());
               }

               this.getNavigation().stop();
               Vec3 vec3d = entitylivingbase.getEyePosition(1.0F);
               this.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0);
               this.getLookControl().setLookAt(vec3d);
            } else if (!this.isFlying()) {
               this.setFlying(true);
            }
         } else if (!this.level().isClientSide && this.getTarget() == null) {
            this.setFlying(false);
         }
      }
   }

   public float getDistance(BlockPos posIn) {
      return EntityUtil.getDistanceToPos(this.blockPosition(), posIn);
   }

   public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
      super.setTarget(entitylivingbaseIn);
      if (entitylivingbaseIn == null) {
         this.setFlying(false);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void updateWingRotation() {
      if (!this.isSilent() && this.level().isClientSide) {
         SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
         if (!soundHandler.isActive(this.flapSound) && this.isFlying()) {
            soundHandler.play(this.flapSound = new MovingEntitySound(this, SkiesSounds.ENTITY_EMBERBACK_FLYING));
         } else if (!this.isFlying()) {
            soundHandler.stop(this.flapSound);
         }
      }
   }

   public static float minMax(float min, float max, float value) {
      return Math.min(max, Math.max(min, value));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.ARMOR, 10.0);
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("IsFlying", this.isFlying());
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setFlying(compound.getBoolean("IsFlying"));
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_EMBERBACK_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_EMBERBACK_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_EMBERBACK_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.3F);
   }

   protected float getSoundVolume() {
      return 1.0F;
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

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.5F;
   }

   public MobType getMobType() {
      return MobType.ARTHROPOD;
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (!(entityIn instanceof LivingEntity)
         || !this.wasEyeInWater && !entityIn.fireImmune() && !((LivingEntity)entityIn).hasEffect(MobEffects.FIRE_RESISTANCE)) {
         for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0), EntityUtil.CAN_ATTACK)) {
            if (living != this && !living.fireImmune() && living.hurt(SkiesDamageSources.get(living.level()).ember(this), 3.0F)) {
               living.setSecondsOnFire(1 + this.level().random.nextInt(5));
            }
         }

         this.level().broadcastEntityEvent(this, (byte)4);
         this.attackTimer = 10;
         this.playSound(SkiesSounds.ENTITY_EMBERBACK_SPRAY, this.getSoundVolume(), this.getVoicePitch());
         return true;
      } else {
         LivingEntity target = this.getTarget();
         if (this.onGround()) {
            Vec3 motion = this.getDeltaMovement();
            Vec3 targetDirection = new Vec3(target.getX() - this.getX(), 0.0, target.getZ() - this.getZ());
            if (targetDirection.lengthSqr() > 1.0E-7) {
               targetDirection = targetDirection.normalize().scale(0.4).add(motion.scale(0.2));
            }

            this.setDeltaMovement(targetDirection.x, 0.3F, targetDirection.z);
         }

         return entityIn.hurt(SkiesDamageSources.get(entityIn.level()).bite(this), 3.0F);
      }
   }

   public void travel(Vec3 vec) {
      if (this.isFlying()) {
         if (this.isInWater()) {
            this.moveRelative(0.02F, vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
         } else if (this.isInLava()) {
            this.moveRelative(0.02F, vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
         } else {
            BlockPos ground = Positions.blockPos(this.getX(), this.getBoundingBox().minY - 1.0, this.getZ());
            float f = 0.91F;
            if (this.onGround()) {
               f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.onGround()) {
               f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
            }

            this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(f));
         }
      } else {
         super.travel(vec);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            for (int i = 0; i < 30; i++) {
               double xSpeed = this.random.nextGaussian() * 0.2;
               double ySpeed = this.random.nextGaussian() * 0.2;
               double zSpeed = this.random.nextGaussian() * 0.2;
               this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), xSpeed, ySpeed, zSpeed);
            }

            this.attackTimer = 10;
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   class AIMoveControl extends MoveControl {
      public AIMoveControl(EmberbackEntity bug) {
         super(bug);
      }

      public void tick() {
         if (EmberbackEntity.this.getTarget() != null && EmberbackEntity.this.isFlying()) {
            if (this.operation != Operation.MOVE_TO) {
               this.operation = Operation.MOVE_TO;
            }

            Vec3 vec3d = new Vec3(
               this.wantedX - EmberbackEntity.this.getX(), this.wantedY - EmberbackEntity.this.getY(), this.wantedZ - EmberbackEntity.this.getZ()
            );
            double d0 = vec3d.length();
            EmberbackEntity.this.setDeltaMovement(EmberbackEntity.this.getDeltaMovement().add(vec3d.scale(this.getSpeed() * 0.05 / d0)));
            double d4 = EmberbackEntity.this.getTarget().getX() - EmberbackEntity.this.getX();
            double d5 = EmberbackEntity.this.getTarget().getZ() - EmberbackEntity.this.getZ();
            EmberbackEntity.this.setYRot(-((float)Mth.atan2(d4, d5)) * (180.0F / (float)Math.PI));
            EmberbackEntity.this.yBodyRot = EmberbackEntity.this.yHeadRot = EmberbackEntity.this.getYRot();
         } else {
            super.tick();
         }
      }

      public double getSpeed() {
         return 0.45F;
      }
   }
}
