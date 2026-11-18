package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.util.EntityUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class StoneletEntity extends SkiesMonsterEntity {
   public StoneletEntity(EntityType<? extends StoneletEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 1;
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 30.0)
         .add(Attributes.MOVEMENT_SPEED, 0.2)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
         .add(Attributes.ARMOR, 6.0)
         .add(Attributes.ATTACK_DAMAGE, 5.0);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.4F, true));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag
   ) {
      spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
      this.setLeftHanded(this.random.nextFloat() >= 0.05F);
      return spawnDataIn;
   }

   public boolean checkSpawnRules(LevelAccessor pLevel, MobSpawnType reason) {
      BlockPos pos = this.blockPosition();
      if ((reason == MobSpawnType.NATURAL || reason == MobSpawnType.SPAWNER)
         && EntityUtil.isPosInStructure(this.level(), pos, SkiesStructures.NATURE_DUNGEON.getStructure())) {
         if (!EntityUtil.canSpawnWithoutFlooding(this)) {
            return false;
         }

         if (pos.getY() > 145 && (!this.level().canSeeSky(pos) || !this.level().getBlockState(pos.below()).is(SkiesBlocks.turquoise_comet_grass_block))) {
            return false;
         }
      }

      return super.checkSpawnRules(pLevel, reason);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_STONELET_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_STONELET_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_STONELET_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public boolean hurt(DamageSource source, float amount) {
      if (source.getDirectEntity() instanceof Player && this.hurtTime <= 0) {
         Player player = (Player)source.getDirectEntity();
         ItemStack stack = player.getInventory().getSelected();
         if (stack.getItem() instanceof PickaxeItem) {
            this.spawnBlockParticles(SkiesBlocks.nature_stone.defaultBlockState(), 30);
            return super.hurt(source, amount * 2.2F);
         }
      }

      return super.hurt(source, amount);
   }

   protected AABB getAttackBoundingBox() {
      return super.getAttackBoundingBox().inflate(0.4F);
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return 1.6F;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
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
}
