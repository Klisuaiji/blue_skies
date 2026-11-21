package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import com.legacy.blue_skies.entities.projectile.SeedBombEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBossMob;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSounds;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SpewterEntity extends SkiesMonsterEntity implements ISkyBossMob, RangedAttackMob {
   protected static final EntityDataAccessor<Boolean> GROWN = SynchedEntityData.defineId(SpewterEntity.class, EntityDataSerializers.BOOLEAN);
   public int prevCounter;
   public int counter;
   public int spawnCounter;

   public SpewterEntity(EntityType<? extends SpewterEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 0;
   }

   protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(GROWN, false);
   }

   public static Builder registerAttributes() {
      return ArmoredFrostSpiritEntity.registerAttributes()
         .add(Attributes.MAX_HEALTH, 28.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.FOLLOW_RANGE, 7.5);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25, 20, 7.0F));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public void move(MoverType typeIn, Vec3 pos) {
      super.move(typeIn, new Vec3(0.0, pos.y, 0.0));
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      if (!this.level().isClientSide) {
         double maxHealth = difficultyIn.getDifficulty() == Difficulty.HARD ? 32.0 : (difficultyIn.getDifficulty() == Difficulty.NORMAL ? 28.0 : 26.0);
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
         this.setHealth((float)maxHealth);
      }

      return spawnDataIn;
   }

   public void tick() {
      super.tick();
      this.prevCounter = this.counter;
      if (this.spawnCounter > 0) {
         this.spawnCounter--;
      }

      if (this.spawnCounter < 0) {
         this.spawnCounter++;
      }

      if (this.deathTime > 0 && this.counter < 20) {
         this.counter += 2;
      } else if (this.deathTime <= 0 && this.counter > 0) {
         this.counter--;
      }

      this.setXRot(0.0F);
      this.setYRot(0.0F);
      this.yHeadRot = this.xRotO = this.yRotO = this.yHeadRot = 0.0F;
   }

   protected SoundEvent getAmbientSound() {
      return null;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_SPEWTER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_SPEWTER_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public boolean hurt(DamageSource source, float amount) {
      boolean flag = super.hurt(source, amount);
      if (flag && amount < this.getHealth() && !this.level().isClientSide) {
         this.level().broadcastEntityEvent(this, (byte)5);
         this.counter = 17;
      }

      return flag;
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return 1.0F;
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   public boolean isGrown() {
      return (Boolean)this.entityData.get(GROWN);
   }

   public void setGrown(boolean grown) {
      if (grown) {
         this.spawnCounter = 15;
         this.level().broadcastEntityEvent(this, (byte)6);
      }

      this.entityData.set(GROWN, grown);
   }

   protected void doPush(Entity entityIn) {
      if (!(entityIn instanceof StarlitCrusherEntity)) {
         super.doPush(entityIn);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.counter = 15;
            break;
         case 5:
            this.counter = 17;
            break;
         case 6:
            this.spawnCounter = 30;
            break;
         default:
            super.handleEntityEvent(id);
      }
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

   public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      return super.checkSpawnRules(worldIn, spawnReasonIn)
         && worldIn.canSeeSky(this.blockPosition())
         && worldIn.getBlockState(this.blockPosition().below()).getBlock() == SkiesBlocks.turquoise_comet_grass_block;
   }

   public void performRangedAttack(LivingEntity target, float distanceFactor) {
      if (!(this.distanceTo(target) >= 8.0)) {
         SeedBombEntity seedBomb = new SeedBombEntity(this.level(), this);
         double d0 = target.getX() - this.getX();
         double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - seedBomb.getY();
         double d2 = target.getZ() - this.getZ();
         float f = Mth.sqrt((float)(d0 * d0 + d2 * d2)) * 1.5F;
         seedBomb.shoot(d0, d1 + f, d2, 0.5F, 0.0F);
         this.playSound(SkiesSounds.ENTITY_SPEWTER_SPIT, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
         this.level().addFreshEntity(seedBomb);
         if (this.hurtTime <= 0) {
            this.counter = 15;
            this.level().broadcastEntityEvent(this, (byte)4);
         }
      }
   }
}
