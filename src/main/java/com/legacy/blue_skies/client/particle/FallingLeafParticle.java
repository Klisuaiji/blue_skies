package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FallingLeafParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;
   private final float rotationSpeed;

   private FallingLeafParticle(ClientLevel worldIn, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(worldIn, x, y, z, dx, dy, dz);
      this.spriteSet = spriteSet;
      this.xd = this.xd * 0.01 + dx;
      this.yd = this.yd * 0.01 + dy;
      this.zd = this.zd * 0.01 + dz;
      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.lifetime = (int)(8.0 / (Math.random() * 0.3 + 0.2)) + 14;
      this.roll = (float)(Math.random() * Math.PI * 2.0);
      this.rotationSpeed = (float)(Math.random() / 20.0);
      this.quadSize = 0.12F;
      this.pickSprite(spriteSet);
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public void move(double x, double y, double z) {
      this.setBoundingBox(this.getBoundingBox().move(x, y, z));
      this.setLocationFromBoundingbox();
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

         if (this.age < this.lifetime / 2) {
            this.yd -= 0.01F;
         }

         this.oRoll = this.roll;
         this.roll = this.roll - (float)(Math.PI * this.rotationSpeed);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public FallingLeafParticle createParticle(
         SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
      ) {
         return new FallingLeafParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
