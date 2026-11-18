package com.legacy.blue_skies.entities.hostile;

import com.legacy.blue_skies.client.particle.StarFlareParticle;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.LogicUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FrostSpiritEntity extends AmbientCreature {
   private BlockPos spawnPosition;
   public float wingRotation;
   public float destPos;
   public float prevDestPos;
   public float prevWingRotation;
   protected int ticksUntilFlap;

   public FrostSpiritEntity(EntityType<? extends FrostSpiritEntity> type, Level world) {
      super(type, world);
   }

   public static Builder registerAttributes() {
      return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0);
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected SoundEvent getHurtSound(DamageSource source) {
      return SkiesSounds.ENTITY_FROST_SPIRIT_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SkiesSounds.ENTITY_FROST_SPIRIT_DEATH;
   }

   public boolean isPushable() {
      return false;
   }

   protected void doPush(Entity entityIn) {
   }

   protected void pushEntities() {
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

   protected void customServerAiStep() {
      super.customServerAiStep();
      if (this.spawnPosition != null && (!this.level().isEmptyBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
         this.spawnPosition = null;
      }

      if (this.spawnPosition == null || this.random.nextInt(30) == 0 || this.spawnPosition.closerToCenterThan(this.position(), 2.0)) {
         this.spawnPosition = new BlockPos(
            (int)this.getX() + this.random.nextInt(7) - this.random.nextInt(7),
            (int)this.getY() + this.random.nextInt(6) - 2,
            (int)this.getZ() + this.random.nextInt(7) - this.random.nextInt(7)
         );
      }

      double d0 = this.spawnPosition.getX() + 0.5 - this.getX();
      double d1 = this.spawnPosition.getY() + 0.1 - this.getY();
      double d2 = this.spawnPosition.getZ() + 0.5 - this.getZ();
      Vec3 vec3d = this.getDeltaMovement();
      Vec3 vec3d1 = vec3d.add((Math.signum(d0) * 0.5 - vec3d.x) * 0.1F, (Math.signum(d1) * 0.7F - vec3d.y) * 0.1F, (Math.signum(d2) * 0.5 - vec3d.z) * 0.1F);
      this.setDeltaMovement(vec3d1);
      float f = (float)(Mth.atan2(vec3d1.z, vec3d1.x) * 180.0F / (float)Math.PI) - 90.0F;
      float f1 = Mth.wrapDegrees(f - this.getYRot());
      this.zza = 1.0F;
      this.setYRot(this.getYRot() + f1);
   }

   public void updateWingRotation() {
      if (!this.onGround()) {
         if (this.ticksUntilFlap == 0) {
            this.ticksUntilFlap = 25;
         } else {
            this.ticksUntilFlap--;
         }
      }

      this.prevWingRotation = this.wingRotation;
      this.prevDestPos = this.destPos;
      this.destPos = (float)(this.destPos + 0.2);
      this.destPos = minMax(0.01F, 1.0F, this.destPos);
      this.wingRotation++;
   }

   public static float minMax(float min, float max, float value) {
      return Math.min(max, Math.max(min, value));
   }

   public void tick() {
      super.tick();
      this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
      if (this.level().isClientSide && this.random.nextFloat() < 0.5F) {
         this.spawnParticles();
      }

      this.updateWingRotation();
   }

   @OnlyIn(Dist.CLIENT)
   private void spawnParticles() {
      double offset = 0.2;
      double x = LogicUtil.plusOrMinus(this.getX(), offset, this.random);
      double y = LogicUtil.plusOrMinus(this.getY(), offset, this.random);
      double z = LogicUtil.plusOrMinus(this.getZ(), offset, this.random);
      double motion = 0.025;
      double dx = LogicUtil.plusOrMinus(motion, this.random);
      double dy = LogicUtil.plusOrMinus(motion, this.random);
      double dz = LogicUtil.plusOrMinus(motion, this.random);
      this.level().addParticle(StarFlareParticle.dyeToRGB(DyeColor.WHITE), x, y, z, dx, dy, dz);
   }

   public boolean hurt(DamageSource source, float amount) {
      return this.tickCount < 7 ? false : super.hurt(source, amount);
   }
}
