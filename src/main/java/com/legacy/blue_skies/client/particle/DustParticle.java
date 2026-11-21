package com.legacy.blue_skies.client.particle;

import com.legacy.blue_skies.registries.SkiesParticles;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class DustParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   protected DustParticle(ClientLevel world, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(world, x, y, z, dx, dy, dz);
      this.spriteSet = spriteSet;
      this.xd = this.xd * this.getBaseVelocityMultiplier();
      this.yd *= 0.1;
      this.zd = this.zd * this.getBaseVelocityMultiplier();
      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05;
      this.quadSize = (this.random.nextFloat() + 0.5F) * 0.5F;
      this.pickSprite(spriteSet);
   }

   public float getBaseVelocityMultiplier() {
      return 15.0F;
   }

   public float getTickVelocityMultiplier() {
      return 0.7F;
   }

   public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
      if (this.alpha >= 0.0F) {
         super.render(buffer, renderInfo, partialTicks);
      }
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.alpha <= 0.0F) {
         this.remove();
      } else {
         this.setSpriteFromAge(this.spriteSet);
         this.move(this.xd, this.yd, this.zd);
         float multiplier = this.getTickVelocityMultiplier();
         this.xd *= multiplier;
         this.zd *= multiplier;
         this.yd *= 0.95F;
         if (this.alpha >= 0.0F) {
            this.alpha -= 0.01F;
            this.alpha *= 0.96F;
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public DustParticle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
         return BuiltInRegistries.PARTICLE_TYPE.getKey(type).equals(BuiltInRegistries.PARTICLE_TYPE.getKey(SkiesParticles.CRUSHER_DUST))
            ? new CrusherDustParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet)
            : new DustParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
