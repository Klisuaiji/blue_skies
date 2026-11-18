package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class DuskParticle extends TextureSheetParticle {
   public DuskParticle(ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(worldIn, x, y, z, dx, dy, dz);
      this.xd = dx;
      this.yd = dy;
      this.zd = dz;
      this.x = x;
      this.y = y;
      this.z = z;
      this.lifetime = (int)(7.0 / (Math.random() * 0.8 + 0.2)) + 4;
      this.hasPhysics = false;
      this.setAlpha(0.35F);
      this.pickSprite(spriteSet);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.alpha <= 0.0F && this.age++ >= this.lifetime) {
         this.remove();
      } else {
         if (!(this.alpha - 0.01F < 0.0F)) {
            this.setAlpha(this.alpha - 0.01F);
         }

         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.9F;
         this.yd *= 0.9F;
         this.zd *= 0.9F;
      }
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz) {
         return new DuskParticle(worldIn, x, y, z, dx, dy, dz, this.spriteSet);
      }
   }
}
