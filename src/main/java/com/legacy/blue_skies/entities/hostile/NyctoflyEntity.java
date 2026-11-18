package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.ai.NonCreatureAttackGoal;
import com.legacy.blue_skies.entities.util.MovingEntitySound;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.structure_gel.api.util.Positions;
import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl.Operation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class NyctoflyEntity extends FlyingMob implements Enemy {
   public float wingRotation;
   public float destPos;
   public float prevDestPos;
   public float prevWingRotation;
   protected int idleTime;
   @OnlyIn(Dist.CLIENT)
   private SoundInstance flapSound;

   public NyctoflyEntity(EntityType<? extends NyctoflyEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 5;
      this.setNoGravity(true);
      this.moveControl = new NyctoflyEntity.AIMoveControl(this);
      this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(3, new NonCreatureAttackGoal(this, 1.7F, true));
      this.goalSelector.addGoal(5, new NyctoflyEntity.NyctoRandomFlyGoal(this));
      this.goalSelector.addGoal(7, new NyctoflyEntity.NyctoLookAroundGoal(this));
      this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0);
   }

   public void move(MoverType typeIn, Vec3 pos) {
      if (this.isNoAi()) {
         super.move(typeIn, Vec3.ZERO);
      } else {
         super.move(typeIn, pos);
      }
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide()) {
         this.playFlightSound();
      }

      if (!this.level().isClientSide()) {
         if (this.isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value())) {
            this.getMoveControl().setWantedPosition(this.getX(), this.getY() + 2.0, this.getZ(), 0.3);
            this.hurt(this.damageSources().drown(), 1.0F);
         }

         if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
         }
      }
   }

   public void aiStep() {
      super.aiStep();
      if (this.onGround()) {
         this.setOnGround(false);
      }

      if (this.getTarget() != null) {
         if (!this.getTarget().isAlive() || !EntityUtil.CAN_ATTACK.test(this.getTarget())) {
            this.setTarget(null);
         }

         if (this.getTarget() != null) {
            Vec3 targetEyes = this.getTarget().getEyePosition(1.0F);
            this.getMoveControl().setWantedPosition(targetEyes.x, targetEyes.y, targetEyes.z, 1.0);
            this.getLookControl().setLookAt(this.getTarget(), 40.0F, 40.0F);
         }
      }

      if (!this.level().isClientSide) {
         if (this.getDeltaMovement().x <= 1.0 && this.getDeltaMovement().z <= 1.0 && this.idleTime <= 100) {
            this.idleTime++;
         } else {
            this.idleTime = 0;
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void playFlightSound() {
      if (!this.isSilent() && this.level().isClientSide) {
         SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
         if (this.flapSound == null && !soundHandler.isActive(this.flapSound) || this.flapSound != null && !soundHandler.isActive(this.flapSound)) {
            soundHandler.play(this.flapSound = new MovingEntitySound(this, SkiesSounds.ENTITY_NYCTOFLY_FLYING));
         }
      }
   }

   public boolean doHurtTarget(Entity entityIn) {
      this.playSound(SkiesSounds.ENTITY_NYCTOFLY_BITE, 0.5F, 1.0F);
      return entityIn.hurt(SkiesDamageSources.get(entityIn.level()).bite(this), (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
   }

   public SoundSource getSoundSource() {
      return SoundSource.HOSTILE;
   }

   protected SoundEvent getAmbientSound() {
      return null;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_NYCTOFLY_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_NYCTOFLY_DEATH;
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
      return randomBoolean && super.checkSpawnRules(worldIn, spawnReasonIn);
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.5F;
   }

   public boolean isNotColliding(double x, double y, double z, double radius) {
      double d0 = (x - this.getX()) / radius;
      double d1 = (y - this.getY()) / radius;
      double d2 = (z - this.getZ()) / radius;
      AABB axisalignedbb = this.getBoundingBox();

      for (int i = 1; i < radius; i++) {
         axisalignedbb = axisalignedbb.move(d0, d1, d2);
         if (!this.level().noCollision(axisalignedbb)) {
            return false;
         }
      }

      return true;
   }

   public MobType getMobType() {
      return MobType.ARTHROPOD;
   }

   class AIMoveControl extends MoveControl {
      private NyctoflyEntity nycto = NyctoflyEntity.this;

      public AIMoveControl(NyctoflyEntity bug) {
         super(bug);
      }

      public void tick() {
         if (this.operation == Operation.MOVE_TO) {
            Vec3 difference = new Vec3(this.wantedX - this.nycto.getX(), this.wantedY - this.nycto.getY(), this.wantedZ - this.nycto.getZ());
            double d0 = difference.length();
            if (d0 < this.nycto.getBoundingBox().getSize()) {
               this.operation = Operation.WAIT;
               this.nycto.setDeltaMovement(this.nycto.getDeltaMovement().scale(0.5));
            } else {
               if (this.nycto.getTarget() != null && this.nycto.getTarget().isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value())) {
                  return;
               }

               this.nycto.setDeltaMovement(this.nycto.getDeltaMovement().add(difference.scale(this.getSpeedModifier() * 0.05 / d0)));
               if (this.nycto.getTarget() == null) {
                  this.nycto.setYRot(-((float)Mth.atan2(this.nycto.getDeltaMovement().x, this.nycto.getDeltaMovement().z)) * (180.0F / (float)Math.PI));
                  this.nycto.yBodyRot = this.nycto.getYRot();
                  if (this.nycto.verticalCollision && this.nycto.tickCount > 100) {
                     this.setWantedPosition(difference.x, difference.x, difference.z, 1.0);
                  }

                  if (this.nycto.idleTime >= 300 || !this.nycto.isNotColliding(this.wantedX, this.wantedY, this.wantedZ, d0)) {
                     this.operation = Operation.WAIT;
                  }
               } else {
                  double d4 = this.nycto.getTarget().getX() - this.nycto.getX();
                  double d5 = this.nycto.getTarget().getZ() - this.nycto.getZ();
                  this.nycto.setYRot(-((float)Mth.atan2(d4, d5)) * (180.0F / (float)Math.PI));
                  this.nycto.yBodyRot = this.nycto.getYRot();
               }
            }
         }
      }

      public double getSpeedModifier() {
         return this.nycto.getTarget() != null && this.nycto.distanceTo(this.nycto.getTarget()) < 7.0 ? 0.6 : 0.8;
      }
   }

   static class NyctoLookAroundGoal extends Goal {
      private final NyctoflyEntity parentEntity;

      public NyctoLookAroundGoal(NyctoflyEntity bug) {
         this.parentEntity = bug;
         this.setFlags(EnumSet.of(Flag.LOOK));
      }

      public boolean canUse() {
         return true;
      }

      public void tick() {
         if (this.parentEntity.getTarget() == null) {
            Vec3 Vector3d = this.parentEntity.getDeltaMovement();
            this.parentEntity.setYRot(-((float)Mth.atan2(Vector3d.x, Vector3d.z)) * (180.0F / (float)Math.PI));
            this.parentEntity.yBodyRot = this.parentEntity.getYRot();
         } else {
            LivingEntity entitylivingbase = this.parentEntity.getTarget();
            if (entitylivingbase.distanceTo(this.parentEntity) < 4096.0) {
               double d1 = entitylivingbase.getX() - this.parentEntity.getX();
               double d2 = entitylivingbase.getZ() - this.parentEntity.getZ();
               this.parentEntity.setYRot(-((float)Mth.atan2(d1, d2)) * (180.0F / (float)Math.PI));
               this.parentEntity.yBodyRot = this.parentEntity.getYRot();
            }
         }
      }
   }

   static class NyctoRandomFlyGoal extends Goal {
      private final NyctoflyEntity nycto;

      public NyctoRandomFlyGoal(NyctoflyEntity bug) {
         this.nycto = bug;
         this.setFlags(EnumSet.of(Flag.MOVE));
      }

      public boolean canUse() {
         if (this.nycto.getTarget() != null) {
            return false;
         } else {
            MoveControl movementController = this.nycto.getMoveControl();
            if (!movementController.hasWanted() && this.nycto.getTarget() == null) {
               return true;
            } else {
               double d0 = movementController.getWantedX() - this.nycto.getX();
               double d1 = movementController.getWantedY() - this.nycto.getY();
               double d2 = movementController.getWantedZ() - this.nycto.getZ();
               double d3 = d0 * d0 + d1 * d1 + d2 * d2;
               return d3 < 1.0 || d3 > 3600.0;
            }
         }
      }

      public boolean canContinueToUse() {
         return false;
      }

      public void start() {
         RandomSource random = this.nycto.getRandom();
         double d0 = this.nycto.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
         double d1 = this.nycto.getY() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
         double d2 = this.nycto.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
         if (this.nycto.level().getBlockState(Positions.blockPos(d0, d1, d2)).getFluidState().getType() != Fluids.WATER) {
            if (d1 >= 100.0) {
               d1 = this.nycto.getY() - (this.nycto.getY() - 100.0 - (random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            }

            if (random.nextFloat() < 0.02F || !this.nycto.level().noCollision(this.nycto.getBoundingBox().inflate(2.0))) {
               this.nycto.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
         }
      }
   }
}
