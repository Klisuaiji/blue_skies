package com.legacy.blue_skies.client.renders.world;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class DawnWeatherRenderer {
   private static final ResourceLocation RAIN_TEXTURES = BlueSkies.locate("textures/environment/everdawn_rain.png");
   private static final ResourceLocation LIGHT_RAIN_TEXTURES = BlueSkies.locate("textures/environment/everdawn_drizzle.png");
   private static final ResourceLocation SAND_TEXTURES = BlueSkies.locate("textures/environment/sand.png");
   private final float[] rainXCoords = new float[1024];
   private final float[] rainYCoords = new float[1024];
   public static final DawnWeatherRenderer INSTANCE = new DawnWeatherRenderer();

   public DawnWeatherRenderer() {
      for (int i = 0; i < 32; i++) {
         for (int j = 0; j < 32; j++) {
            float f = j - 16;
            float f1 = i - 16;
            float f2 = Mth.sqrt(f * f + f1 * f1);
            this.rainXCoords[i << 5 | j] = -f1 / f2;
            this.rainYCoords[i << 5 | j] = f / f2;
         }
      }
   }

   public void renderRainSnow(int ticks, float partialTicks, ClientLevel level, LightTexture lightmapIn, double xIn, double yIn, double zIn) {
      float rainStrength = level.getRainLevel(partialTicks);
      float thunderStrength = level.getThunderLevel(partialTicks);
      if (!(rainStrength <= 0.0F)) {
         lightmapIn.turnOnLightLayer();
         int i = Mth.floor(xIn);
         int j = Mth.floor(yIn);
         int k = Mth.floor(zIn);
         Tesselator tessellator = Tesselator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuilder();
         RenderSystem.disableCull();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableDepthTest();
         int l = 5;
         if (Minecraft.useFancyGraphics()) {
            l = 10;
         }

         RenderSystem.depthMask(Minecraft.useShaderTransparency());
         int i1 = -1;
         float f1 = ticks + partialTicks;
         RenderSystem.setShader(GameRenderer::getParticleShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         MutableBlockPos blockpos$mutable = new MutableBlockPos();

         for (int j1 = k - l; j1 <= k + l; j1++) {
            for (int k1 = i - l; k1 <= i + l; k1++) {
               int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
               double d0 = this.rainXCoords[l1] * 0.5;
               double d1 = this.rainYCoords[l1] * 0.5;
               blockpos$mutable.set(k1, 0, j1);
               Holder<Biome> biome = level.getBiome(blockpos$mutable);
               boolean hasSandstorm = false;
               if (((Biome)biome.value()).getPrecipitationAt(blockpos$mutable) != Precipitation.NONE
                  || (hasSandstorm = level.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(SkiesBiomeTags.HAS_SANDSTORMS).contains(biome))) {
                  int i2 = level.getHeightmapPos(Types.MOTION_BLOCKING, blockpos$mutable).getY();
                  int j2 = j - l;
                  int k2 = j + l;
                  if (j2 < i2) {
                     j2 = i2;
                  }

                  if (k2 < i2) {
                     k2 = i2;
                  }

                  int l2 = i2;
                  if (i2 < j) {
                     l2 = j;
                  }

                  if (j2 != k2) {
                     Random random = new Random(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761);
                     blockpos$mutable.set(k1, j2, j1);
                     if (!hasSandstorm) {
                        if (i1 != 0) {
                           if (i1 >= 0) {
                              tessellator.end();
                           }

                           i1 = 0;
                           RenderSystem.setShaderTexture(0, thunderStrength >= 0.5F ? RAIN_TEXTURES : LIGHT_RAIN_TEXTURES);
                           bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
                        }

                        int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                        float f3 = -(i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
                        double d2 = k1 + 0.5F - xIn;
                        double d4 = j1 + 0.5F - zIn;
                        float f4 = Mth.sqrt((float)(d2 * d2 + d4 * d4)) / l;
                        float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * rainStrength;
                        blockpos$mutable.set(k1, l2, j1);
                        int j3 = LevelRenderer.getLightColor(level, blockpos$mutable);
                        bufferbuilder.vertex(k1 - xIn - d0 + 0.5, k2 - yIn, j1 - zIn - d1 + 0.5)
                           .uv(0.0F, j2 * 0.25F + f3)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .uv2(j3)
                           .endVertex();
                        bufferbuilder.vertex(k1 - xIn + d0 + 0.5, k2 - yIn, j1 - zIn + d1 + 0.5)
                           .uv(1.0F, j2 * 0.25F + f3)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .uv2(j3)
                           .endVertex();
                        bufferbuilder.vertex(k1 - xIn + d0 + 0.5, j2 - yIn, j1 - zIn + d1 + 0.5)
                           .uv(1.0F, k2 * 0.25F + f3)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .uv2(j3)
                           .endVertex();
                        bufferbuilder.vertex(k1 - xIn - d0 + 0.5, j2 - yIn, j1 - zIn - d1 + 0.5)
                           .uv(0.0F, k2 * 0.25F + f3)
                           .color(1.0F, 1.0F, 1.0F, f5)
                           .uv2(j3)
                           .endVertex();
                     } else {
                        if (i1 != 1) {
                           if (i1 >= 0) {
                              tessellator.end();
                           }

                           i1 = 1;
                           RenderSystem.setShaderTexture(0, SAND_TEXTURES);
                           bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.PARTICLE);
                        }

                        float f6 = -((ticks & 511) + partialTicks) / 56.0F;
                        float f7 = (float)(random.nextDouble() + f1 * 0.2 * (float)random.nextGaussian());
                        float f8 = (float)(random.nextDouble() + f1 * (float)random.nextGaussian() * 0.001);
                        double d3 = k1 + 0.5F - xIn;
                        double d5 = j1 + 0.5F - zIn;
                        float f9 = Mth.sqrt((float)(d3 * d3 + d5 * d5)) / l;
                        float f10 = ((1.0F - f9 * f9) * 0.3F + 0.5F) * rainStrength;
                        blockpos$mutable.set(k1, l2, j1);
                        int k3 = LevelRenderer.getLightColor(level, blockpos$mutable);
                        int l3 = k3 >> 16 & 65535;
                        int i4 = k3 & 65535;
                        int j4 = (l3 * 3 + 240) / 4;
                        int k4 = (i4 * 3 + 240) / 4;
                        bufferbuilder.vertex(k1 - xIn - d0 + 0.5, k2 - yIn, j1 - zIn - d1 + 0.5)
                           .uv(0.0F + f7, j2 * 0.25F + f6 + f8)
                           .color(1.0F, 1.0F, 1.0F, f10)
                           .uv2(k4, j4)
                           .endVertex();
                        bufferbuilder.vertex(k1 - xIn + d0 + 0.5, k2 - yIn, j1 - zIn + d1 + 0.5)
                           .uv(1.0F + f7, j2 * 0.25F + f6 + f8)
                           .color(1.0F, 1.0F, 1.0F, f10)
                           .uv2(k4, j4)
                           .endVertex();
                        bufferbuilder.vertex(k1 - xIn + d0 + 0.5, j2 - yIn, j1 - zIn + d1 + 0.5)
                           .uv(1.0F + f7, k2 * 0.25F + f6 + f8)
                           .color(1.0F, 1.0F, 1.0F, f10)
                           .uv2(k4, j4)
                           .endVertex();
                        bufferbuilder.vertex(k1 - xIn - d0 + 0.5, j2 - yIn, j1 - zIn - d1 + 0.5)
                           .uv(0.0F + f7, k2 * 0.25F + f6 + f8)
                           .color(1.0F, 1.0F, 1.0F, f10)
                           .uv2(k4, j4)
                           .endVertex();
                     }
                  }
               }
            }
         }

         if (i1 >= 0) {
            tessellator.end();
         }

         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         lightmapIn.turnOffLightLayer();
      }
   }
}
