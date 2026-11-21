package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ShrumptyEntity extends SkiesMonsterEntity {
   public boolean shouldSpreadLegs = false;
   public boolean safeDistance = true;
   public boolean landedHit = false;

   public ShrumptyEntity(EntityType<? extends ShrumptyEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(2, new AvoidEntityGoal(this, Player.class, 20.0F, 1.2, 1.2) {
         public boolean canUse() {
            return !ShrumptyEntity.this.safeDistance && super.canUse();
         }

         public boolean canContinueToUse() {
            return !ShrumptyEntity.this.safeDistance && super.canContinueToUse();
         }
      });
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2F, true) {
         public boolean canUse() {
            return !ShrumptyEntity.this.landedHit && super.canUse();
         }

         public boolean canContinueToUse() {
            return !ShrumptyEntity.this.landedHit && super.canContinueToUse();
         }
      });
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0, 1.0E-5F));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
   }

   public void tick() {
      super.tick();
      if (!this.safeDistance && this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 15.0, true) == null) {
         this.safeDistance = true;
         this.landedHit = false;
      }

      if (!this.level().isClientSide) {
         this.setShrumptyNoticed(!this.getNavigation().isDone() || this.zza != 0.0F || this.getTarget() != null);
      }
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_SHRUMPTY_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_SHRUMPTY_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_SHRUMPTY_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      super.playStepSound(pos, blockIn);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public boolean hurt(DamageSource source, float amount) {
      return super.hurt(source, amount);
   }

   public boolean doHurtTarget(Entity entityIn) {
      this.safeDistance = false;
      this.landedHit = true;
      this.playSound(SkiesSounds.ENTITY_SHRUMPTY_ATTACK, this.getSoundVolume(), this.getVoicePitch());
      if (this.onGround()) {
         Vec3 vec3d = this.getDeltaMovement();
         Vec3 vec3d1 = new Vec3(entityIn.getX() - this.getX(), 0.0, entityIn.getZ() - this.getZ());
         if (vec3d1.lengthSqr() > 1.0E-7) {
            vec3d1 = vec3d1.normalize().scale(0.4).add(vec3d.scale(0.2));
         }

         this.setDeltaMovement(vec3d1.x, 0.3F, vec3d1.z);
      }

      if (entityIn instanceof LivingEntity) {
         ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
      }

      return super.doHurtTarget(entityIn);
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return super.getStandingEyeHeight(poseIn, sizeIn);
   }

   public void setShrumptyNoticed(boolean noticedIn) {
      if (noticedIn) {
         this.shouldSpreadLegs = true;
         this.level().broadcastEntityEvent(this, (byte)4);
      } else {
         this.shouldSpreadLegs = false;
         this.level().broadcastEntityEvent(this, (byte)5);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.shouldSpreadLegs = true;
            break;
         case 5:
            this.shouldSpreadLegs = false;
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }
}
