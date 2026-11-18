package com.legacy.blue_skies.client.particle;

import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.legacy.blue_skies.util.LogicUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PoisonParticle extends TextureSheetParticle {
   private final PoisonData data;

   private PoisonParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet, PoisonData data) {
      super(level, x, y, z, dx, dy, dz);
      this.data = data;
      this.xd = this.xd * 0.01F + dx;
      this.yd = this.yd * 0.01F + dy;
      this.zd = this.zd * 0.01F + dz;
      this.x = this.x + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.y = this.y + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.z = this.z + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F;
      this.lifetime = (int)(7.0 / (Math.random() * 0.8 + 0.2)) + 4;
      if (data.isSmall()) {
         this.lifetime /= 2;
      }

      this.setSprite(data.isSmall() ? spriteSet.get(1, 1) : spriteSet.get(0, 1));
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public float getQuadSize(float partialTicks) {
      return !this.data.isSmall()
         ? this.quadSize * Mth.clamp((this.age + partialTicks) / this.lifetime * 32.0F, 0.0F, 1.0F)
         : this.quadSize * Mth.clamp(1.0F / (this.age + 1) * 3.0F, 0.0F, 0.8F);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }

      if (this.age > this.lifetime && !this.data.isSmall()) {
         float range = 0.05F;

         for (int i = 0; i < 4; i++) {
            this.level
               .addParticle(
                  new PoisonData(true),
                  this.x,
                  this.y,
                  this.z,
                  LogicUtil.plusOrMinus(range, this.random),
                  LogicUtil.plusOrMinus(range, this.random),
                  LogicUtil.plusOrMinus(range, this.random)
               );
         }
      } else {
         this.yd += 0.004;
         this.move(this.xd, this.yd, this.zd);
         if (this.y == this.yo) {
            this.xd *= 1.1;
            this.zd *= 1.1;
         }

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
   public static class Factory implements ParticleProvider<PoisonData> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet animatedSprite) {
         this.spriteSet = animatedSprite;
      }

      public Particle createParticle(PoisonData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
         return new PoisonParticle(level, x, y, z, dx, dy, dz, this.spriteSet, data);
      }
   }
}
