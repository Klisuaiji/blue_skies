package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SearingSmokeParticle extends BaseAshSmokeParticle {
   protected SearingSmokeParticle(
      ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale, SpriteSet spriteWithAge
   ) {
      super(world, x, y, z, 0.1F, 0.1F, 0.1F, motionX, motionY, motionZ, scale, spriteWithAge, 1.0F, 15, 0.01F, true);
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
   }

   public int getLightColor(float partialTick) {
      float f = (this.lifetime - (this.age + partialTick)) / this.lifetime;
      f = Mth.clamp(f, 0.0F, 1.0F);
      int i = super.getLightColor(partialTick);
      int j = i & 0xFF;
      int k = i >> 16 & 0xFF;
      j += (int)(f * 15.0F * 16.0F);
      if (j > 240) {
         j = 240;
      }

      return j | k << 16;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
         return new SearingSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 0.5F, this.spriteSet);
      }
   }
}
