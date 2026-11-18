package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FroseSnowParticle extends TextureSheetParticle {
   protected FroseSnowParticle(ClientLevel world, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(world, x, y, z, dx, dy, dz);
      this.xd *= 0.1F;
      this.yd = 0.2;
      this.zd *= 0.1F;
      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.quadSize = this.random.nextFloat() * 0.04F;
      this.pickSprite(spriteSet);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.quadSize < 0.009) {
         this.remove();
      } else {
         this.move(this.xd, this.yd, this.zd);
         double slowDown = 0.93;
         double speedUp = 1.08;
         this.xd = this.xd * (this.yd < 0.0 ? slowDown : speedUp);
         this.yd = Math.max(this.yd - 0.006, -0.05);
         this.zd = this.zd * (this.yd < 0.0 ? slowDown : speedUp);
         this.quadSize *= 0.99F;
      }
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public FroseSnowParticle createParticle(
         SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
      ) {
         return new FroseSnowParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
