package com.legacy.blue_skies.entities.hostile.boss.summons;

import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.blue_skies.entities.util.interfaces.ISkyBossMob;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEffects;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NestedSpiderEntity extends Spider implements ISkyBossMob {
   public NestedSpiderEntity(EntityType<? extends NestedSpiderEntity> type, Level worldIn) {
      super(type, worldIn);
      this.xpReward = 0;
   }

   public static Builder registerAttributes() {
      return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 26.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
   }

   public boolean doHurtTarget(Entity entityIn) {
      return super.doHurtTarget(entityIn);
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag
   ) {
      return spawnDataIn;
   }

   protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
      return 0.45F;
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_NESTED_SPIDER_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_NESTED_SPIDER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_NESTED_SPIDER_DEATH;
   }

   public float getVoicePitch() {
      return EntityUtil.getSoundPitchWithStart(this.random, 1.5F, 0.2F);
   }

   public boolean canBeAffected(MobEffectInstance potioneffectIn) {
      return potioneffectIn.getEffect() == SkiesEffects.DEADLY_VENOM ? false : super.canBeAffected(potioneffectIn);
   }

   public boolean isClimbing() {
      return false;
   }

   public void makeStuckInBlock(BlockState state, Vec3 motionVec) {
      if (state.getBlock() != Blocks.COBWEB && state.getBlock() != SkiesBlocks.spider_webbing) {
         super.makeStuckInBlock(state, motionVec);
      }
   }

   public boolean isAlliedTo(Entity entityIn) {
      return entityIn instanceof ArachnarchEntity
         ? this.getTeam() == null && entityIn.getTeam() == null
         : super.isAlliedTo(entityIn) && entityIn != null && entityIn != this;
   }

   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }
}
