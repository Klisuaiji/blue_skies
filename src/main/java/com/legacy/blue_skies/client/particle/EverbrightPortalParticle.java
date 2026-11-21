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

public class EverbrightPortalParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   private EverbrightPortalParticle(ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(worldIn, x, y, z);
      this.spriteSet = spriteSet;
      this.xd = dx;
      this.yd = dy;
      this.zd = dz;
      this.x = x;
      this.y = y;
      this.z = z;
      this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
      float f = this.random.nextFloat() + 0.8F;
      this.rCol = this.rCol * Mth.clamp(f, 0.8F, 1.0F);
      this.gCol = this.gCol * Mth.clamp(f, 0.9F, 1.0F);
      this.bCol = this.bCol * Mth.clamp(f, 0.9F, 1.0F);
      this.lifetime = (int)(Math.random() * 10.0) + 60;
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
      f = 1.0F - f;
      f *= f;
      f = 1.0F - f;
      return this.quadSize * f;
   }

   public int getLightColor(float partialTick) {
      int i = super.getLightColor(partialTick);
      float f = (float)this.age / this.lifetime;
      f *= f;
      f *= f;
      int j = i & 0xFF;
      int k = i >> 16 & 0xFF;
      k += (int)(f * 15.0F * 16.0F);
      if (k > 240) {
         k = 240;
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
         this.xd *= 0.98;
         this.zd *= 0.98;
         this.yd *= 1.115;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz) {
         return new EverbrightPortalParticle(worldIn, x, y, z, dx, dy, dz, this.spriteSet);
      }
   }
}
