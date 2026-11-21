package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.entities.util.base.SkiesAnimalEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FireflyEntity extends SkiesAnimalEntity implements FlyingAnimal {
   public FireflyEntity(EntityType<? extends FireflyEntity> type, Level world) {
      super(type, world);
      this.moveControl = new FlyingMoveControl(this, 20, true);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(2, new FireflyEntity.WanderGoal(this, 1.0));
   }

   protected PathNavigation createNavigation(Level worldIn) {
      FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn) {
         public boolean isStableDestination(BlockPos pos) {
            return super.isStableDestination(pos);
         }
      };
      flyingpathnavigator.setCanOpenDoors(false);
      flyingpathnavigator.setCanFloat(false);
      flyingpathnavigator.setCanPassDoors(true);
      return flyingpathnavigator;
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return null;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_FIREFLY_DEATH;
   }

   public boolean isPushable() {
      return false;
   }

   public boolean isFlying() {
      return !this.onGround();
   }

   protected void doPush(Entity entityIn) {
   }

   protected void pushEntities() {
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0).add(Attributes.FLYING_SPEED, 0.4F).add(Attributes.MOVEMENT_SPEED, 0.2F);
   }

   protected MovementEmission getMovementEmission() {
      return MovementEmission.EVENTS;
   }

   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, DamageSource source) {
      return false;
   }

   protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
   }

   public boolean isIgnoringBlockTriggers() {
      return true;
   }

   public float getEyeHeight(Pose pose) {
      return this.getBbHeight() / 2.0F;
   }

   public void tick() {
      super.tick();
   }


   @Override
   public AgeableMob createChild(AgeableMob ageableIn) {
      return null;
   }

   public static boolean spawnConditions(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
      BlockState block = world.getBlockState(pos.below());
      return (block.is(SkiesBlocks.turquoise_grass_block) || block.is(SkiesBlocks.lunar_grass_block) || block.is(SkiesBlocks.crystal_sand)) && pos.getY() >= 60;
   }

   public static class WanderGoal extends WaterAvoidingRandomStrollGoal {
      public WanderGoal(PathfinderMob creature, double speed) {
         super(creature, speed, Float.MAX_VALUE);
      }

      protected Vec3 getPosition() {
         Vec3 view = this.mob.getViewVector(0.0F);
         return HoverRandomPos.getPos(this.mob, 8, 7, view.x, view.z, (float) (Math.PI / 2), 2, 1);
      }
   }
}
