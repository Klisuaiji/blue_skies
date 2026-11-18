package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class StarGlimmerParticle extends TextureSheetParticle {
   private StarGlimmerParticle(ClientLevel worldIn, double x, double y, double z, SpriteSet spriteSet) {
      super(worldIn, x, y, z, 0.0, 0.0, 0.0);
      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.lifetime = (int)(8.0 / (Math.random() * 0.3 + 0.2)) + 14;
      this.quadSize = 0.12F;
      this.setSpriteFromAge(spriteSet);
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public void move(double x, double y, double z) {
      this.setBoundingBox(this.getBoundingBox().move(x, y, z));
      this.setLocationFromBoundingbox();
   }

   protected int getLightColor(float partialTick) {
      return 15728880;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.lifetime-- <= 0) {
         this.remove();
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public StarGlimmerParticle createParticle(
         SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
      ) {
         return new StarGlimmerParticle(worldIn, x, y, z, this.spriteSet);
      }
   }
}
