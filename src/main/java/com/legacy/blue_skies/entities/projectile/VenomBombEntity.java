package com.legacy.blue_skies.entities.projectile;

import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;

public class VenomBombEntity extends ThrowableProjectile {
   public MobEffectInstance effect;
   public boolean lingering = false;

   public VenomBombEntity(EntityType<? extends VenomBombEntity> typeIn, Level worldIn) {
      super(typeIn, worldIn);
   }

   public VenomBombEntity(Level worldIn, LivingEntity livingEntityIn) {
      super(SkiesEntityTypes.VENOM_BOMB, livingEntityIn, worldIn);
   }

   public VenomBombEntity(Level worldIn, double x, double y, double z) {
      super(SkiesEntityTypes.VENOM_BOMB, x, y, z, worldIn);
   }

   protected void defineSynchedData() {
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         for (int i = 0; i < 2; i++) {
            float dx = (this.random.nextFloat() - 0.5F) / 7.0F;
            float dy = this.random.nextFloat() / 10.0F;
            float dz = (this.random.nextFloat() - 0.5F) / 7.0F;
            this.level()
               .addParticle(
                  new PoisonData(false), this.blockPosition().getX() + 0.5, this.blockPosition().getY() + 0.5F, this.blockPosition().getZ() + 0.5, dx, dy, dz
               );
         }
      }
   }

   protected float getGravity() {
      return 0.05F;
   }

   protected void onHit(HitResult result) {
      BlockPos pos = this.blockPosition();
      RandomSource random = this.random;
      if (result.getType() == Type.BLOCK) {
         if (!this.level().isClientSide) {
            this.applyEffectToNearbyMobs(this.effect, result.getType() == Type.ENTITY ? ((EntityHitResult)result).getEntity() : null);
            if (this.lingering && random.nextInt(3) == 0) {
               this.makeAreaOfEffectCloud(this.effect);
            }

            this.discard();
         } else {
            double d11 = pos.getX() + 0.5;
            double d14 = pos.getY() + 0.1;
            double d19 = pos.getZ() + 0.5;

            for (int j2 = 0; j2 < 8; j2++) {
               this.level()
                  .addParticle(new PoisonData(false), d11, d14, d19, random.nextGaussian() * 0.15, random.nextDouble() * 0.2, random.nextGaussian() * 0.15);
            }
         }
      }
   }

   private void applyEffectToNearbyMobs(MobEffectInstance effect, @Nullable Entity entityIn) {
      if (effect != null) {
         AABB axisalignedbb = this.getBoundingBox().inflate(5.0, 2.0, 5.0);
         List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);
         if (!list.isEmpty()) {
            for (LivingEntity livingentity : list) {
               if (livingentity.isAffectedByPotions()) {
                  double d0 = this.distanceToSqr(livingentity);
                  if (d0 < 16.0) {
                     double d1 = 1.0 - Math.sqrt(d0) / 4.0;
                     if (livingentity == entityIn) {
                        d1 = 1.0;
                     }

                     int i = (int)(d1 * effect.getDuration() + 0.5);
                     if (i > 20) {
                        livingentity.addEffect(new MobEffectInstance(effect.getEffect(), i, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
                     }
                  }
               }
            }
         }
      }
   }

   private void makeAreaOfEffectCloud(MobEffectInstance effect) {
      AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
      areaeffectcloudentity.setRadius(3.0F);
      areaeffectcloudentity.setRadiusOnUse(this.level().getDifficulty() == Difficulty.EASY ? -5.0F : -3.0F);
      areaeffectcloudentity.setWaitTime(20);
      areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / areaeffectcloudentity.getDuration());
      if (this.getOwner() instanceof LivingEntity) {
         areaeffectcloudentity.setOwner((LivingEntity)this.getOwner());
      }

      areaeffectcloudentity.addEffect(effect);
      this.level().addFreshEntity(areaeffectcloudentity);
   }
}
