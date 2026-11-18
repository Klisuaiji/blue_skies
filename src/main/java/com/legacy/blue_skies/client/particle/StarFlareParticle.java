package com.legacy.blue_skies.client.particle;

import com.legacy.blue_skies.client.particle.data.StarFlareData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class StarFlareParticle extends TextureSheetParticle {
   private final SpriteSet spriteSet;

   public StarFlareParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet, StarFlareData data) {
      super(level, x, y, z);
      this.spriteSet = spriteSet;
      this.xd = dx;
      this.yd = dy;
      this.zd = dz;
      this.x = x;
      this.y = y;
      this.z = z;
      this.lifetime = (int)(14.0 / (Math.random() * 0.8 + 0.2)) + 4;
      this.setColor(data.getRed(), data.getGreen(), data.getBlue());
      this.pickSprite(spriteSet);
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public float getQuadSize(float partialTicks) {
      return this.quadSize * 1.5F;
   }

   protected int getLightColor(float partialTick) {
      return 15728880;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         if (this.age % 3 == 0) {
            this.pickSprite(this.spriteSet);
         }

         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.9F;
         this.yd *= 0.9F;
         this.zd *= 0.9F;
      }
   }

   public static StarFlareData dyeToRGB(DyeColor color) {
      float[] rgb = color.getTextureDiffuseColors();
      float s = (color == DyeColor.BLACK ? 0.9F : 1.15F) + (float)(Math.random() * 0.1);
      return new StarFlareData(Math.min(rgb[0] * s, 1.0F), Math.min(rgb[1] * s, 1.0F), Math.min(rgb[2] * s, 1.0F));
   }

   @OnlyIn(Dist.CLIENT)
   public static class Factory implements ParticleProvider<StarFlareData> {
      private final SpriteSet spriteSet;

      public Factory(SpriteSet spriteSet) {
         this.spriteSet = spriteSet;
      }

      public Particle createParticle(StarFlareData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
         return new StarFlareParticle(level, x, y, z, dx, dy, dz, this.spriteSet, data);
      }
   }
}
