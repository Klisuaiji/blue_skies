package com.legacy.blue_skies.client.renders.world;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer.Usage;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class StarrySkyRenderer {
   private static final ResourceLocation SUN_TEXTURE = new ResourceLocation("textures/environment/sun.png");
   private static final ResourceLocation MOON_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
   private static final ResourceLocation HALLOWEEN_MOON_TEXTURE = BlueSkies.locate("textures/environment/halloween_moon.png");
   private VertexBuffer starBuffer;
   private VertexBuffer skyBuffer;
   private VertexBuffer darkBuffer;
   public static final StarrySkyRenderer BRIGHT_INSTANCE = new StarrySkyRenderer(false);
   public static final StarrySkyRenderer DAWN_INSTANCE = new StarrySkyRenderer(true);
   private boolean pinkSunrise = false;
   private float rotation = 0.0F;
   private long startTime = Util.getMillis();
   private final float[] colorsSunriseSunset = new float[4];

   public StarrySkyRenderer(boolean pinkSunriseIn) {
      this.pinkSunrise = pinkSunriseIn;
      this.generateStars();
      this.generateSky();
      this.generateDarkSky();
      this.startTime = Util.getMillis();
   }

   public void render(int ticks, float partialTicks, PoseStack pose, ClientLevel level, Camera camera, Matrix4f matrix, Runnable fog) {
      fog.run();
      if (this.rotation >= 360.0F) {
         this.startTime = Util.getMillis();
         this.rotation = 0.0F;
      } else {
         this.rotation = (float)(Util.getMillis() - this.startTime) * 3.0E-4F;
      }

      Vec3 vec3d = level.getSkyColor(camera.getPosition(), partialTicks);
      float f = (float)vec3d.x;
      float f1 = (float)vec3d.y;
      float f2 = (float)vec3d.z;
      FogRenderer.levelFogColor();
      BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
      RenderSystem.depthMask(false);
      RenderSystem.setShaderColor(f, f1, f2, 1.0F);
      ShaderInstance shaderinstance = RenderSystem.getShader();
      this.skyBuffer.bind();
      this.skyBuffer.drawWithShader(pose.last().pose(), matrix, shaderinstance);
      VertexBuffer.unbind();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      float[] afloat = this.pinkSunrise
         ? this.calcDawnSunriseSunsetColors(level.getTimeOfDay(partialTicks), partialTicks)
         : level.effects().getSunriseColor(level.getTimeOfDay(partialTicks), partialTicks);
      if (afloat != null) {
         RenderSystem.setShader(GameRenderer::getPositionColorShader);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         pose.pushPose();
         pose.mulPose(Axis.XP.rotationDegrees(90.0F));
         float f3 = Mth.sin(level.getSunAngle(partialTicks)) < 0.0F ? 180.0F : 0.0F;
         pose.mulPose(Axis.ZP.rotationDegrees(f3));
         pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
         float f4 = afloat[0];
         float f5 = afloat[1];
         float f6 = afloat[2];
         Matrix4f matrix4f = pose.last().pose();
         bufferbuilder.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
         bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();

         for (int j = 0; j <= 16; j++) {
            float f7 = j * (float) (Math.PI * 2) / 16.0F;
            float f8 = Mth.sin(f7);
            float f9 = Mth.cos(f7);
            bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
         }

         BufferUploader.drawWithShader(bufferbuilder.end());
         pose.popPose();
      }

      RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
      pose.pushPose();
      float f11 = 1.0F - level.getRainLevel(partialTicks);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
      pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
      pose.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
      Matrix4f matrix4f1 = pose.last().pose();
      float f12 = 30.0F;
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, SUN_TEXTURE);
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
      bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
      bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
      bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
      BufferUploader.drawWithShader(bufferbuilder.end());
      if (this.pinkSunrise) {
         f12 = 20.0F;
         if (BlueSkiesConfig.COMMON.isHalloween()) {
            RenderSystem.setShaderTexture(0, HALLOWEEN_MOON_TEXTURE);
            bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
         } else {
            RenderSystem.setShaderTexture(0, MOON_TEXTURES);
            int k = 8;
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f13 = (l + 0) / 4.0F;
            float f14 = (i1 + 0) / 2.0F;
            float f15 = (l + 1) / 4.0F;
            float f16 = (i1 + 1) / 2.0F;
            bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(f15, f16).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(f13, f16).endVertex();
            bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(f13, f14).endVertex();
            bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
         }
      }

      pose.mulPose(Axis.YP.rotationDegrees(-this.rotation));
      pose.mulPose(Axis.XP.rotationDegrees(this.rotation));
      float f10 = 1.0F * f11;
      if (f10 > 0.0F) {
         RenderSystem.setShaderColor(f10, f10, f10, f10);
         FogRenderer.setupNoFog();
         this.starBuffer.bind();
         this.starBuffer.drawWithShader(pose.last().pose(), matrix, GameRenderer.getPositionShader());
         VertexBuffer.unbind();
         fog.run();
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      pose.popPose();
      RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
      double d0 = camera.getEntity().getEyePosition(partialTicks).y - level.getLevelData().getHorizonHeight(level);
      if (d0 < 0.0) {
         pose.pushPose();
         pose.translate(0.0, 12.0, 0.0);
         this.darkBuffer.bind();
         this.darkBuffer.drawWithShader(pose.last().pose(), matrix, shaderinstance);
         VertexBuffer.unbind();
         pose.popPose();
      }

      if (level.effects().hasGround()) {
         RenderSystem.setShaderColor(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F, 1.0F);
      } else {
         RenderSystem.setShaderColor(f, f1, f2, 1.0F);
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.depthMask(true);
   }

   private void generateStars() {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tesselator.getBuilder();
      RenderSystem.setShader(GameRenderer::getPositionShader);
      if (this.starBuffer != null) {
         this.starBuffer.close();
      }

      this.starBuffer = new VertexBuffer(Usage.STATIC);
      RenderedBuffer box = this.renderStars(bufferbuilder);
      this.starBuffer.bind();
      this.starBuffer.upload(box);
      VertexBuffer.unbind();
   }

   private RenderedBuffer renderStars(BufferBuilder bufferBuilderIn) {
      Random random = new Random(1337L);
      bufferBuilderIn.begin(Mode.QUADS, DefaultVertexFormat.POSITION);

      for (int i = 0; i < 1500; i++) {
         double d0 = random.nextFloat() * 2.0F - 1.0F;
         double d1 = random.nextFloat() * 2.0F - 1.0F;
         double d2 = random.nextFloat() * 2.0F - 1.0F;
         double d3 = 0.15F + random.nextFloat() * 0.1F;
         double d4 = d0 * d0 + d1 * d1 + d2 * d2;
         if (d4 < 1.0 && d4 > 0.01) {
            d4 = 1.0 / Math.sqrt(d4);
            d0 *= d4;
            d1 *= d4;
            d2 *= d4;
            double d5 = d0 * 100.0;
            double d6 = d1 * 100.0;
            double d7 = d2 * 100.0;
            double d8 = Math.atan2(d0, d2);
            double d9 = Math.sin(d8);
            double d10 = Math.cos(d8);
            double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
            double d12 = Math.sin(d11);
            double d13 = Math.cos(d11);
            double d14 = random.nextDouble() * Math.PI * 2.0;
            double d15 = Math.sin(d14);
            double d16 = Math.cos(d14);

            for (int j = 0; j < 4; j++) {
               double d18 = ((j & 2) - 1) * d3;
               double d19 = ((j + 1 & 2) - 1) * d3;
               double d21 = d18 * d16 - d19 * d15;
               double d22 = d19 * d16 + d18 * d15;
               double d23 = d21 * d12 + 0.0 * d13;
               double d24 = 0.0 * d12 - d21 * d13;
               double d25 = d24 * d9 - d22 * d10;
               double d26 = d22 * d9 + d24 * d10;
               bufferBuilderIn.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
            }
         }
      }

      return bufferBuilderIn.end();
   }

   private void generateDarkSky() {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tesselator.getBuilder();
      if (this.darkBuffer != null) {
         this.darkBuffer.close();
      }

      this.darkBuffer = new VertexBuffer(Usage.STATIC);
      RenderedBuffer box = this.buildSkybox(bufferbuilder, -16.0F);
      this.darkBuffer.bind();
      this.darkBuffer.upload(box);
      VertexBuffer.unbind();
   }

   private void generateSky() {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tesselator.getBuilder();
      if (this.skyBuffer != null) {
         this.skyBuffer.close();
      }

      this.skyBuffer = new VertexBuffer(Usage.STATIC);
      RenderedBuffer box = this.buildSkybox(bufferbuilder, 16.0F);
      this.skyBuffer.bind();
      this.skyBuffer.upload(box);
      VertexBuffer.unbind();
   }

   private RenderedBuffer buildSkybox(BufferBuilder bufferBuilderIn, float posY) {
      float f = Math.signum(posY) * 512.0F;
      RenderSystem.setShader(GameRenderer::getPositionShader);
      bufferBuilderIn.begin(Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
      bufferBuilderIn.vertex(0.0, posY, 0.0).endVertex();

      for (int i = -180; i <= 180; i += 45) {
         bufferBuilderIn.vertex(f * Mth.cos(i * (float) (Math.PI / 180.0)), posY, 512.0F * Mth.sin(i * (float) (Math.PI / 180.0))).endVertex();
      }

      return bufferBuilderIn.end();
   }

   public float[] calcDawnSunriseSunsetColors(float celestialAngle, float partialTicks) {
      float f1 = Mth.cos(celestialAngle * (float) (Math.PI * 2)) - 0.0F;
      if (f1 >= -0.4F && f1 <= 0.4F) {
         float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
         float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float) Math.PI)) * 0.99F;
         f4 *= f4;
         this.colorsSunriseSunset[0] = f3 * 0.5F + 0.7F;
         this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
         this.colorsSunriseSunset[2] = f3 * f3 * 0.7F + 0.2F;
         this.colorsSunriseSunset[3] = f4;
         return this.colorsSunriseSunset;
      } else {
         return null;
      }
   }

   public static StarrySkyRenderer getSkyForDim(ResourceKey<Level> dim) {
      if (dim.equals(SkiesDimensions.everbrightKey())) {
         return BRIGHT_INSTANCE;
      } else {
         return dim.equals(SkiesDimensions.everdawnKey()) ? DAWN_INSTANCE : null;
      }
   }
}
