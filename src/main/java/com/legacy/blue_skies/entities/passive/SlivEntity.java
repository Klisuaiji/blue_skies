package com.legacy.blue_skies.entities.passive;

import com.legacy.blue_skies.entities.util.base.SkiesAnimalEntity;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SlivEntity extends SkiesAnimalEntity {
   private float prevToungeAnim;
   private float toungeAnim;
   private boolean playToungeAnim;

   public SlivEntity(EntityType<? extends SlivEntity> type, Level levelIn) {
      super(type, levelIn);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         this.prevToungeAnim = this.toungeAnim;
         if (this.playToungeAnim) {
            this.toungeAnim = Mth.clamp(this.toungeAnim + 6.0F, 0.0F, 6.0F);
         } else {
            this.toungeAnim = Mth.clamp(this.toungeAnim - 6.0F, 0.0F, 6.0F);
         }
      }

      if (!this.level().isClientSide) {
         if (this.tickCount % 60 == 0 || this.tickCount % 60 == 4) {
            this.playToungeAnim = true;
            this.level().broadcastEntityEvent(this, (byte)4);
         }

         if (this.tickCount % 60 == 2 || this.tickCount % 60 == 6) {
            this.playToungeAnim = false;
            this.level().broadcastEntityEvent(this, (byte)5);
         }
      }
   }

   protected void playStepSound(BlockPos pos, BlockState blockIn) {
   }

   @OnlyIn(Dist.CLIENT)
   public float getToungeAnimationScale(float pctIn) {
      return Mth.lerp(pctIn, this.prevToungeAnim, this.toungeAnim) / 6.0F;
   }

   @OnlyIn(Dist.CLIENT)
   public void handleEntityEvent(byte id) {
      switch (id) {
         case 4:
            this.playToungeAnim = true;
            break;
         case 5:
            this.playToungeAnim = false;
            break;
         default:
            super.handleEntityEvent(id);
      }
   }

   @Override
   public AgeableMob createChild(AgeableMob ageable) {
      return null;
   }

   protected SoundEvent getAmbientSound() {
      return SkiesSounds.ENTITY_SLIV_IDLE;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SkiesSounds.ENTITY_SLIV_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_SLIV_DEATH;
   }
}
