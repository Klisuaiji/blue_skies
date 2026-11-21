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

public class FluctuantSphereBeamParticle extends TextureSheetParticle {
   private final float rotationSpeed;

   public FluctuantSphereBeamParticle(ClientLevel worldIn, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet spriteSet) {
      super(worldIn, x, y, z, motionX, motionY, motionZ);
      this.xd = motionX;
      this.yd = motionY;
      this.zd = motionZ;
      this.x = x + Math.random() / 100.0;
      this.y = y;
      this.z = z + Math.random() / 100.0;
      this.lifetime = 20;
      this.hasPhysics = false;
      this.setAlpha(1.0F);
      this.pickSprite(spriteSet);
      this.quadSize = (float)(Math.random() / 10.0) + 0.2F;
      this.roll = (float)(Math.random() * Math.PI * 2.0);
      this.rotationSpeed = (float)(Math.random() / 10.0);
   }

   public float getQuadSize(float partialTicks) {
      return Math.max(0.0F, this.quadSize - this.quadSize * (this.age + partialTicks) / this.lifetime);
   }

   public int getLightColor(float partialTicks) {
      int i = 13;
      return i << 20 | i << 4;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age >= this.lifetime) {
         this.remove();
      }

      this.age++;
      this.oRoll = this.roll;
      this.roll = this.roll - (float)(Math.PI * this.rotationSpeed);
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
         return new FluctuantSphereBeamParticle(worldIn, x, y, z, dx, dy, dz, this.spriteSet);
      }
   }
}
