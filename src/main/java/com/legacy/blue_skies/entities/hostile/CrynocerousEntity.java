package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForgeMod;

public class CrynocerousEntity extends ArmoredFrostSpiritEntity {
   public int attackTimer = 11;

   public CrynocerousEntity(EntityType<? extends CrynocerousEntity> type, Level world) {
      super(type, world);
   }

   public static Builder registerAttributes() {
      return ArmoredFrostSpiritEntity.registerAttributes()
         .add(Attributes.MAX_HEALTH, 25.0)
         .add(Attributes.MOVEMENT_SPEED, 0.2F)
         .add(Attributes.ATTACK_DAMAGE, 6.0)
         .add((Attribute)NeoForgeMod.STEP_HEIGHT.value(), 1.0);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new CrynocerousEntity.CrynoMeleeGoal(this, 1.5, false));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_CRYNOCEROUS_IDLE;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_CRYNOCEROUS_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_CRYNOCEROUS_DEATH;
   }

   @Override
   public float getVoicePitch() {
      return EntityUtil.getSoundPitchWithStart(this.random, 1.0F);
   }

   public boolean doHurtTarget(Entity entityIn) {
      if (this.attackTimer >= 11) {
         this.attackTimer = 0;
         this.level().broadcastEntityEvent(this, (byte)4);
      }

      return super.doHurtTarget(entityIn);
   }

   public void aiStep() {
      if (this.attackTimer < 11) {
         this.attackTimer++;
      }

      super.aiStep();
   }

   public void handleEntityEvent(byte id) {
      if (id == 4) {
         this.attackTimer = 0;
      } else {
         super.handleEntityEvent(id);
      }
   }

   @Override
   protected void populateDefaultEquipmentSlots(RandomSource rand, DifficultyInstance difficulty) {
   }

   protected AABB getAttackBoundingBox() {
      return super.getAttackBoundingBox().inflate(0.6F);
   }

   class CrynoMeleeGoal extends MeleeAttackGoal {
      public CrynoMeleeGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
         super(creature, speedIn, useLongMemory);
      }
   }
}
