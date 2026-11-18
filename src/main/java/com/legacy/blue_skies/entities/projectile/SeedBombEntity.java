package com.legacy.blue_skies.entities.projectile;

import com.legacy.blue_skies.registries.SkiesEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SeedBombEntity extends LlamaSpit {
   public SeedBombEntity(EntityType<? extends SeedBombEntity> type, Level world) {
      super(type, world);
   }

   public SeedBombEntity(Level worldIn, Monster shooter) {
      this(SkiesEntityTypes.SEED_BOMB, worldIn);
      super.setOwner(shooter);
      this.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight() - 0.1F, shooter.getZ());
   }

   @OnlyIn(Dist.CLIENT)
   public SeedBombEntity(Level worldIn, double x, double y, double z, double xDir, double yDir, double zDir) {
      this(SkiesEntityTypes.SEED_BOMB, worldIn);
      this.setPos(x, y, z);

      for (int i = 0; i < 7; i++) {
         double d0 = 0.4 + 0.1 * i;
         worldIn.addParticle(ParticleTypes.SPIT, x, y, z, xDir * d0, yDir, zDir * d0);
      }

      this.setDeltaMovement(xDir, yDir, zDir);
   }

   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         this.spawnParticles();
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void spawnParticles() {
      float dx = (this.random.nextFloat() - 0.5F) / 7.0F;
      float dy = this.random.nextFloat() / 10.0F;
      float dz = (this.random.nextFloat() - 0.5F) / 7.0F;
      this.level()
         .addParticle(
            ParticleTypes.SNEEZE, this.blockPosition().getX() + 0.5, this.blockPosition().getY() + 0.5F, this.blockPosition().getZ() + 0.5, dx, dy, dz
         );
   }

   public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
      Vec3 vec3d = new Vec3(x, y, z)
         .normalize()
         .add(
            this.random.nextGaussian() * 0.0075F * inaccuracy,
            this.random.nextGaussian() * 0.0075F * inaccuracy,
            this.random.nextGaussian() * 0.0075F * inaccuracy
         )
         .scale(velocity);
      this.setDeltaMovement(vec3d);
      double f = vec3d.horizontalDistance();
      this.setYRot((float)(Mth.atan2(vec3d.x, z) * 180.0F / (float)Math.PI));
      this.setXRot((float)(Mth.atan2(vec3d.y, f) * 180.0F / (float)Math.PI));
      this.yRotO = this.getYRot();
      this.xRotO = this.getXRot();
   }

   protected void onHitEntity(EntityHitResult result) {
      Entity entity = this.getOwner();
      if (entity != null && result.getEntity() != null && !entity.isAlliedTo(result.getEntity())) {
         this.causeSeedExplosion(this.blockPosition());
         super.onHitEntity(result);
         if (!this.level().isClientSide) {
            this.discard();
         }
      }
   }

   protected void onHitBlock(BlockHitResult result) {
      super.onHitBlock(result);
      this.causeSeedExplosion(result.getBlockPos().above(1));
   }

   public void causeSeedExplosion(BlockPos pos) {
      this.playSound(SoundEvents.WET_GRASS_BREAK, 0.7F, 1.0F);
      this.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 0.7F);
      double d0 = pos.getX() + 0.5;
      double d13 = pos.getY() + 0.5;
      double d18 = pos.getZ() + 0.5;

      for (double d24 = 0.0; d24 < Math.PI * 2; d24 += 0.1) {
         double motion = 0.1F;
         this.level()
            .addParticle(
               ParticleTypes.SMOKE, d0 + Math.cos(d24) * motion, d13 - 0.4, d18 + Math.sin(d24) * motion, Math.cos(d24) * -motion, 0.0, Math.sin(d24) * -motion
            );
      }
   }
}
