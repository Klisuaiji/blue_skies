package com.legacy.blue_skies.client;

import com.legacy.blue_skies.BlueSkies;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SkiesClientUtil {
   private static final ResourceLocation NOTIFICATION_SPRITE = BlueSkies.locate("notification");

   public static void bind(ResourceLocation texture) {
      bind(texture, true);
   }

   public static void bind(ResourceLocation texture, boolean setShader) {
      if (setShader) {
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
      }

      RenderSystem.setShaderTexture(0, texture);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static float lockNum(float base, float max) {
      if (base == (float) (Math.PI * 2)) {
         return 0.0F;
      }

      if (base > max) {
         base = max;
      }

      if (base < -max) {
         base = -max;
      }

      return base;
   }

   public static void addHumanoidAnimation(
      ModelPart rightArm,
      ModelPart leftArm,
      ModelPart rightLeg,
      ModelPart leftLeg,
      ModelPart head,
      float ageInTicks,
      float netHeadYaw,
      float headPitch,
      float limbSwing,
      float limbSwingAmount
   ) {
      rightArm.setRotation(0.0F, 0.0F, 0.0F);
      rightLeg.setRotation(0.0F, 0.0F, 0.0F);
      leftArm.setRotation(0.0F, 0.0F, 0.0F);
      leftLeg.setRotation(0.0F, 0.0F, 0.0F);
      head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      head.xRot = headPitch * (float) (Math.PI / 180.0);
      rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
      leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
      rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      AnimationUtils.bobArms(rightArm, leftArm, ageInTicks);
   }

   public static void renderExclamation(GuiGraphics graphics, int x, int y) {
      PoseStack stack = graphics.pose();
      stack.pushPose();
      stack.translate(0.0F, 0.0F, 300.0F);
      float ticks = x - y - (float)Util.getMillis() * 0.03F;
      float f = (int)(Math.max(0.0F, Math.max(Mth.sin((10.0F + ticks) * 0.57F), Mth.cos(ticks * 0.35F))) * -6.0F);
      stack.translate(0.0F, f, 0.0F);
      graphics.blitSprite(NOTIFICATION_SPRITE, x, y, 8, 8);
      stack.popPose();
   }
}
