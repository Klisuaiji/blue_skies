package com.legacy.blue_skies.entities.passive.fish;

import com.legacy.blue_skies.entities.util.base.SkyFishEntity;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GrittleFlatfishEntity extends SkyFishEntity {
   protected boolean safeDistance = true;
   protected boolean landedHit = false;

   public GrittleFlatfishEntity(EntityType<? extends GrittleFlatfishEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(2, new AvoidEntityGoal(this, Player.class, 20.0F, 1.8, 1.8) {
         public boolean canUse() {
            return !GrittleFlatfishEntity.this.safeDistance && super.canUse();
         }

         public boolean canContinueToUse() {
            return !GrittleFlatfishEntity.this.safeDistance && super.canContinueToUse();
         }
      });
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.7F, true) {
         public boolean canUse() {
            return !GrittleFlatfishEntity.this.landedHit && super.canUse();
         }

         public boolean canContinueToUse() {
            return !GrittleFlatfishEntity.this.landedHit && super.canContinueToUse();
         }
      });
      this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 40));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE).add(Attributes.MAX_HEALTH, 8.0).add(Attributes.FOLLOW_RANGE, 5.0);
   }

   public void tick() {
      super.tick();
      Player player = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 15.0, true);
      if (!this.safeDistance && player == null) {
         this.safeDistance = true;
         this.landedHit = false;
      } else if (this.landedHit) {
         this.moveRelative(0.1F, new Vec3(0.0, 0.0, 0.3));
      }
   }

   public boolean doHurtTarget(Entity entityIn) {
      this.safeDistance = false;
      this.landedHit = true;
      return super.doHurtTarget(entityIn);
   }

   @Override
   public ItemStack getVentiumBucket() {
      return new ItemStack(SkiesItems.ventium_grittle_flatfish_bucket);
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.COD_AMBIENT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.COD_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.COD_HURT;
   }

   protected SoundEvent getFlopSound() {
      return SoundEvents.COD_FLOP;
   }
}
