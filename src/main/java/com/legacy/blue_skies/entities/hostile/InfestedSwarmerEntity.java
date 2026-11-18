package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.entities.passive.CrogreEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMonsterEntity;
import com.legacy.blue_skies.registries.SkiesBiomes;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.BiomeUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class InfestedSwarmerEntity extends SkiesMonsterEntity {
   public InfestedSwarmerEntity(EntityType<? extends InfestedSwarmerEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.3);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
      this.goalSelector.addGoal(2, new AvoidEntityGoal(this, CrogreEntity.class, 8.0F, 1.2, 1.2));
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2F, true));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   @Override
   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag
   ) {
      return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
   }

   public static boolean swarmerSpawnConditions(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
      return SkiesEntityTypes.SpawnConditions.undergroundDawnMonsterSpawnConditions(type, world, reason, pos, rand)
         || BiomeUtil.getKeyFromBiome(world, (Biome)world.getBiome(pos).value()) == SkiesBiomes.CRYSTAL_ROUGHS.getKey()
            && SkiesEntityTypes.SpawnConditions.dawnMonsterSpawnConditions(type, world, reason, pos, rand);
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return 0.6F;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   public MobType getMobType() {
      return MobType.ARTHROPOD;
   }

   public boolean doHurtTarget(Entity entityIn) {
      this.playSound(SkiesSounds.ENTITY_INFESTED_SWARMER_BITE, this.getSoundVolume(), this.getVoicePitch());
      return super.doHurtTarget(entityIn);
   }

   protected AABB getAttackBoundingBox() {
      return super.getAttackBoundingBox().inflate(0.5);
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_INFESTED_SWARMER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_INFESTED_SWARMER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_INFESTED_SWARMER_DEATH;
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.1F);
   }
}
