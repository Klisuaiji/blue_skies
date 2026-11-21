package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;

public class DiophydeProwlerEntity extends SkiesMonsterEntity {
   private float prevChargeAnim;
   private float chargeAnim;
   private boolean playChargeAnim;

   public DiophydeProwlerEntity(EntityType<? extends DiophydeProwlerEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 26.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3)
         .add(Attributes.STEP_HEIGHT, 1.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(3, new DiophydeProwlerEntity.LungeAtTargetGoal());
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0) {
         public boolean canUse() {
            return DiophydeProwlerEntity.this.getTarget() == null && super.canUse();
         }

         public boolean canContinueToUse() {
            return DiophydeProwlerEntity.this.getTarget() == null && super.canContinueToUse();
         }
      });
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         this.prevChargeAnim = this.chargeAnim;
         if (this.playChargeAnim) {
            this.chargeAnim = Mth.clamp(this.chargeAnim + 2.0F, 0.0F, 6.0F);
         } else {
            this.chargeAnim = Mth.clamp(this.chargeAnim - 2.0F, 0.0F, 6.0F);
         }
      } else if (this.getTarget() == null && this.playChargeAnim) {
         this.setCharging(false);
      }
   }

   public void setCharging(boolean chargingIn) {
      if (chargingIn) {
         this.playChargeAnim = true;
         this.level().broadcastEntityEvent(this, (byte)4);
      } else {
         this.playChargeAnim = false;
         this.level().broadcastEntityEvent(this, (byte)5);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public float getChargingAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevChargeAnim, this.chargeAnim) / 6.0F;
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.playChargeAnim = true;
            break;
         case 5:
            this.playChargeAnim = false;
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_DIOPHYDE_PROWLER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_DIOPHYDE_PROWLER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_DIOPHYDE_PROWLER_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.WOLF_STEP, 0.35F, 1.0F);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   class LungeAtTargetGoal extends Goal {
      private final DiophydeProwlerEntity attacker = DiophydeProwlerEntity.this;
      private int chargeTime;

      public LungeAtTargetGoal() {
      }

      public boolean canUse() {
         return this.attacker.getTarget() != null;
      }

      public boolean canContinueToUse() {
         return this.attacker.getTarget() != null && this.chargeTime > 0;
      }

      public void start() {
         this.chargeTime = 60 + this.attacker.random.nextInt(40);
      }

      public void tick() {
         if (this.attacker.getTarget() != null) {
            Level world = this.attacker.level();
            this.attacker.getLookControl().setLookAt(this.attacker.getTarget(), 30.0F, 30.0F);
            if (this.attacker.distanceTo(this.attacker.getTarget()) > 7.0F && !this.attacker.playChargeAnim) {
               this.attacker.setCharging(false);
               LivingEntity target = this.attacker.getTarget();
               this.attacker.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.3F);
            } else {
               if (this.chargeTime > 0) {
                  this.chargeTime--;
               }

               if (this.chargeTime > 30 && this.attacker.onGround()) {
                  this.attacker.moveRelative(0.3F, new Vec3(-0.1, 0.0, 0.5));
               } else if (this.chargeTime > 15 && this.chargeTime <= 30) {
                  this.attacker.setCharging(true);
               } else {
                  this.attacker.setCharging(false);
               }

               if (this.chargeTime == 20) {
                  Vec3 vec3d = this.attacker.getDeltaMovement();
                  Vec3 vec3d1 = new Vec3(this.attacker.getTarget().getX() - this.attacker.getX(), 0.0, this.attacker.getTarget().getZ() - this.attacker.getZ());
                  if (vec3d1.lengthSqr() > 1.0E-7) {
                     vec3d1 = vec3d1.normalize().scale(1.5).add(vec3d.scale(1.5));
                  }

                  this.attacker.setDeltaMovement(vec3d1.x, 0.4, vec3d1.z);
                  this.attacker.playSound(SkiesSounds.ENTITY_DIOPHYDE_PROWLER_LUNGE, this.attacker.getSoundVolume(), this.attacker.getVoicePitch());
               }

               int rotAmount = this.chargeTime > 20 ? 90 : 0;
               double d1 = this.attacker.getTarget().getX() - this.attacker.getX();
               double d2 = this.attacker.getTarget().getZ() - this.attacker.getZ();
               this.attacker.setYRot(-((float)Mth.atan2(d1, d2)) * (180.0F / (float)Math.PI) - rotAmount);
               if (rotAmount > 0) {
                  this.attacker.yBodyRot = this.attacker.getYRot() + 60.0F;
                  this.attacker.yHeadRot = this.attacker.getYRot() + 60.0F;
               }

               for (Entity entity : world.getEntities(this.attacker, this.attacker.getBoundingBox())) {
                  if (entity instanceof LivingEntity && ((LivingEntity)entity).invulnerableTime <= 0) {
                     LivingEntity living = (LivingEntity)entity;
                     boolean attack = this.chargeTime <= 20 && this.attacker.doHurtTarget(living);
                     if (attack) {
                        this.attacker.setDeltaMovement(Vec3.ZERO);
                        return;
                     }
                  }
               }
            }
         }
      }

      public boolean requiresUpdateEveryTick() {
         return true;
      }
   }
}
