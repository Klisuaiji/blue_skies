package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.passive.CrogreEntity;
import com.legacy.blue_skies.entities.projectile.VenomSpitEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEffects;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Spider.SpiderEffectsGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class VenomSpiderEntity extends SkiesMonsterEntity implements RangedAttackMob {
   private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(VenomSpiderEntity.class, EntityDataSerializers.BYTE);

   public VenomSpiderEntity(EntityType<? extends VenomSpiderEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
      this.goalSelector.addGoal(4, new VenomSpiderEntity.SpiderMeleeAttackGoal(this));
      this.goalSelector.addGoal(4, new RangedAttackGoal(this, 0.0, 40, 15.0F) {
         public boolean canUse() {
            VenomSpiderEntity spider = VenomSpiderEntity.this;
            LivingEntity target = spider.getTarget();
            return target == null ? false : target.distanceTo(spider) >= 5.0F && super.canUse();
         }

         public boolean canContinueToUse() {
            VenomSpiderEntity spider = VenomSpiderEntity.this;
            LivingEntity target = spider.getTarget();
            return target == null ? false : target.distanceTo(spider) >= 5.0F && super.canContinueToUse();
         }
      });
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.targetSelector.addGoal(2, new VenomSpiderEntity.SpiderTargetGoal(this, Player.class));
      this.targetSelector.addGoal(3, new VenomSpiderEntity.SpiderTargetGoal(this, IronGolem.class));
      this.goalSelector.addGoal(2, new AvoidEntityGoal(this, CrogreEntity.class, 8.0F, 1.0, 1.0));
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 22.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.FOLLOW_RANGE, 20.0);
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(CLIMBING, (byte)0);
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn
   ) {
      if (spawnDataIn == null) {
         spawnDataIn = new SpiderEffectsGroupData();
         if (worldIn.getDifficulty() == Difficulty.HARD && worldIn.getRandom().nextFloat() < 0.1F * difficultyIn.getSpecialMultiplier()) {
            ((SpiderEffectsGroupData)spawnDataIn).setRandomEffect(worldIn.getRandom());
         }
      }

      if (spawnDataIn instanceof SpiderEffectsGroupData) {
         MobEffect effect = ((SpiderEffectsGroupData)spawnDataIn).effect;
         if (effect != null) {
            this.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE));
         }
      }

      return spawnDataIn;
   }

   public static boolean spawnConditions(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource rand) {
      return reason == MobSpawnType.NATURAL && level.getLevel() != null && EntityUtil.isInDungeon(level.getLevel(), pos) && pos.getY() < 15
         ? false
         : SkiesEntityTypes.SpawnConditions.dawnMonsterSpawnConditions(type, level, reason, pos, rand);
   }

   protected PathNavigation createNavigation(Level worldIn) {
      return new WallClimberNavigation(this, worldIn);
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide()) {
         this.setBesideClimbableBlock(this.horizontalCollision);
      }
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
   }

   public void load(CompoundTag compound) {
      super.load(compound);
   }

   public double getMountedYOffset() {
      return this.getBbHeight() * 0.5F;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   @Override
   public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
      return 0.0F;
   }

   public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      boolean randomBoolean = this.level().isRaining() ? true : this.random.nextBoolean();
      return randomBoolean && super.checkSpawnRules(worldIn, spawnReasonIn);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_VENOM_SPIDER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_VENOM_SPIDER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_VENOM_SPIDER_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
   }

   public boolean onClimbable() {
      return this.isBesideClimbableBlock();
   }


   public boolean canBeAffected(MobEffectInstance potioneffectIn) {
      return potioneffectIn.getEffect() == MobEffects.POISON
         ? false
         : (potioneffectIn.getEffect() == SkiesEffects.DEADLY_VENOM ? false : super.canBeAffected(potioneffectIn));
   }

   public void makeStuckInBlock(BlockState stateIn, Vec3 motionVec) {
      if (stateIn.getBlock() != Blocks.COBWEB && stateIn.getBlock() != SkiesBlocks.spider_webbing) {
         super.makeStuckInBlock(stateIn, motionVec);
      }
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

   public void performRangedAttack(LivingEntity target, float distanceFactor) {
      VenomSpitEntity venom = new VenomSpitEntity(this.level(), this);
      double d0 = target.getX() - this.getX();
      double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - venom.getY();
      double d2 = target.getZ() - this.getZ();
      float f = Mth.sqrt((float)(d0 * d0 + d2 * d2)) * 0.2F;
      venom.shoot(d0, d1 + f, d2, 1.5F, 5.0F);
      this.playSound(SkiesSounds.ENTITY_VENOM_SPIDER_SPIT, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
      this.level().addFreshEntity(venom);
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (super.doHurtTarget(entityIn)) {
         if (entityIn instanceof LivingEntity) {
            int durration = 20 * (Math.min(2, this.level().getDifficulty().getId()) + 3);
            int amplifier = this.level().getDifficulty().getId() / 2;
            ((LivingEntity)entityIn).addEffect(new MobEffectInstance(MobEffects.POISON, durration, amplifier));
         }

         return true;
      } else {
         return false;
      }
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.65F;
   }

   static class SpiderMeleeAttackGoal extends MeleeAttackGoal {
      public SpiderMeleeAttackGoal(VenomSpiderEntity spider) {
         super(spider, 1.0, true);
      }

      public boolean canUse() {
         LivingEntity target = this.mob.getTarget();
         return target == null ? false : target.distanceTo(this.mob) < 5.0F && super.canUse();
      }

      public boolean canContinueToUse() {
         LivingEntity target = this.mob.getTarget();
         return target == null ? false : target.distanceTo(this.mob) < 5.0F && super.canContinueToUse();
      }

      public void tick() {
         super.tick();
         LivingEntity target = this.mob.getTarget();
         if (target != null && target.distanceTo(this.mob) >= 5.0F) {
            this.stop();
         }
      }
   }

   static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
      public SpiderTargetGoal(VenomSpiderEntity spider, Class<T> classTarget) {
         super(spider, classTarget, true);
      }

      public boolean canUse() {
         return super.canUse();
      }

      public void tick() {
         super.tick();
      }
   }
}
