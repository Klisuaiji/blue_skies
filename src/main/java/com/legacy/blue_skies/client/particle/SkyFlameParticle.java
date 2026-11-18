package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkyFlameParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   private SkyFlameParticle(ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(worldIn, x, y, z, dx, dy, dz);
      this.spriteSet = spriteSet;
      this.xd = this.xd * 0.01F + dx;
      this.yd = this.yd * 0.01F + dy;
      this.zd = this.zd * 0.01F + dz;
      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
      this.setSpriteFromAge(spriteSet);
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public void move(double x, double y, double z) {
      this.setBoundingBox(this.getBoundingBox().move(x, y, z));
      this.setLocationFromBoundingbox();
   }

   public float getQuadSize(float partialTicks) {
      float f = (this.age + partialTicks) / this.lifetime;
      return this.quadSize * (1.0F - f * f * 0.5F);
   }

   public int getLightColor(float partialTick) {
      float f = (this.lifetime - (this.age + partialTick)) / this.lifetime;
      f = Mth.clamp(f, 0.65F, 1.0F);
      int i = super.getLightColor(partialTick);
      int j = i & 0xFF;
      int k = i >> 16 & 0xFF;
      j += (int)(f * 15.0F * 16.0F);
      if (j > 240) {
         j = 240;
      }

      return j | k << 16;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         this.setSpriteFromAge(this.spriteSet);
         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.96F;
         this.yd *= 0.96F;
         this.zd *= 0.96F;
         if (this.onGround) {
            this.xd *= 0.7F;
            this.zd *= 0.7F;
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz) {
         return new SkyFlameParticle(worldIn, x, y, z, dx, dy, dz, this.spriteSet);
      }
   }
}
