package com.legacy.blue_skies.entities.passive;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import com.legacy.blue_skies.entities.util.base.SkiesAnimalEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;

public class SnowOwlEntity extends SkiesAnimalEntity implements FlyingAnimal {
   protected static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(SnowOwlEntity.class, EntityDataSerializers.BOOLEAN);
   public float flap;
   public float flapSpeed;
   public float oFlapSpeed;
   public float oFlap;
   public float flapping = 1.0F;
   public float nextFlap = 1.0F;
   public BlockPos exitPos = null;

   public SnowOwlEntity(EntityType<? extends SnowOwlEntity> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new FlyingMoveControl(this, 10, false);
   }

   @Nullable
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 6.0)
         .add(Attributes.FLYING_SPEED, 0.4F)
         .add(Attributes.MOVEMENT_SPEED, 0.2F)
         .add(Attributes.ATTACK_DAMAGE)
         .add(Attributes.ATTACK_KNOCKBACK);
   }

   protected PathNavigation createNavigation(Level worldIn) {
      FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn);
      flyingpathnavigator.setCanOpenDoors(false);
      flyingpathnavigator.setCanFloat(true);
      flyingpathnavigator.setCanOpenDoors(true);
      return flyingpathnavigator;
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(HOSTILE, false);
   }

   public void load(CompoundTag compound) {
      super.load(compound);
      this.setOwlHostile(compound.getBoolean("OwlHostile"));
   }

   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("OwlHostile", this.isOwlHostile());
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return sizeIn.height * 0.6F;
   }

   public void aiStep() {
      super.aiStep();
      this.calculateFlapping();
      float speed = this.isFlying() ? 3.0F : 1.0F;
      if (this.isOwlHostile() && this.exitPos != null && this.exitPos != BlockPos.ZERO) {
         this.getNavigation().moveTo(this.exitPos.getX(), this.exitPos.getY(), this.exitPos.getZ(), speed);
         if (EntityUtil.getDistanceToPos(this.blockPosition(), this.exitPos) < 2.0F) {
            this.exitPos = null;
         }
      } else if (this.getTarget() != null) {
         if (!this.getSensing().hasLineOfSight(this.getTarget())) {
            this.getNavigation().moveTo(this.getTarget(), speed);
         } else {
            this.getMoveControl()
               .setWantedPosition(this.getTarget().getX(), this.getTarget().getY() + this.getTarget().getEyeHeight(), this.getTarget().getZ(), speed);
         }
      }
   }

   private void calculateFlapping() {
      this.oFlap = this.flap;
      this.oFlapSpeed = this.flapSpeed;
      this.flapSpeed = (float)(this.flapSpeed + (!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3);
      this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
      if (!this.onGround() && this.flapping < 1.0F) {
         this.flapping = 1.0F;
      }

      this.flapping = (float)(this.flapping * 0.9);
      Vec3 vec3d = this.getDeltaMovement();
      if (!this.onGround() && vec3d.y < 0.0) {
         this.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0));
      }

      this.flap = this.flap + this.flapping * 2.0F;
   }

   protected boolean isFlapping() {
      return this.flyDist > this.nextFlap;
   }

   protected void onFlap() {
      this.playSound(SkiesSounds.ENTITY_SNOW_OWL_FLAP, 0.15F, 1.0F);
      this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return false;
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   @Override
   public AgeableMob createChild(AgeableMob ageable) {
      return null;
   }

   public boolean doHurtTarget(Entity entityIn) {
      return super.doHurtTarget(entityIn);
   }

   public ResourceKey<LootTable> getDefaultLootTable() {
      return this.isOwlHostile() ? BuiltInLootTables.EMPTY : this.getType().getDefaultLootTable();
   }

   public SoundEvent getAmbientSound() {
      if (this.isOwlHostile()) {
         return null;
      } else {
         return this.random.nextFloat() < 0.03F && BlueSkiesConfig.COMMON.isChristmas() ? SkiesSounds.ENTITY_SNOW_OWL_SING : SkiesSounds.ENTITY_SNOW_OWL_IDLE;
      }
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_SNOW_OWL_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_SNOW_OWL_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
   }

   public float getVoicePitch() {
      return EntityUtil.getSoundPitchWithStart(this.random, 1.0F);
   }

   protected float getSoundVolume() {
      return this.isOwlHostile() ? 1.0F : 1.7F;
   }

   public SoundSource getSoundSource() {
      return SoundSource.NEUTRAL;
   }

   public boolean isPushable() {
      return true;
   }

   protected void doPush(Entity entityIn) {
      if (!(entityIn instanceof Player) && !(entityIn instanceof EntWallEntity)) {
         super.doPush(entityIn);
      }
   }

   public boolean isFlying() {
      return !this.onGround();
   }

   public boolean isOwlHostile() {
      return (Boolean)this.entityData.get(HOSTILE);
   }

   public void setOwlHostile(boolean hostileIn) {
      if (hostileIn) {
         this.xpReward = 0;
         this.goalSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
         this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
         this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 2.0, true));
         this.goalSelector.addGoal(2, new AvoidEntityGoal(this, StarlitCrusherEntity.class, 5.0F, 0.6, 1.0));
      } else {
         this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
      }

      this.entityData.set(HOSTILE, hostileIn);
   }

   public boolean isAlliedTo(Entity entityIn) {
      if (entityIn instanceof EntRootEntity) {
         return this.isAlliedTo(((EntRootEntity)entityIn).getCaster());
      } else {
         return entityIn.getType().is(SkiesEntityTags.NATURE_ALLIES)
            ? this.getTeam() == null && entityIn.getTeam() == null
            : super.isAlliedTo(entityIn) && entityIn != null && entityIn != this;
      }
   }

   public static boolean spawnConditions(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
      BlockState block = world.getBlockState(pos.below());
      return (block.is(BlockTags.LEAVES) || block.is(BlockTags.LOGS)) && world.getRawBrightness(pos, 0) > 8;
   }
}
