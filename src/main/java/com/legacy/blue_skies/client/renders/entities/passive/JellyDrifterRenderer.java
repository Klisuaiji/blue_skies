package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.JellyDrifterModel;
import com.legacy.blue_skies.client.renders.entities.util.TranslucentMobRenderer;
import com.legacy.blue_skies.entities.passive.fish.JellyDrifterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class JellyDrifterRenderer<T extends JellyDrifterEntity> extends TranslucentMobRenderer<T, JellyDrifterModel<T>> {
   public JellyDrifterRenderer(Context context) {
      super(context, new JellyDrifterModel(context.bakeLayer(SkiesRenderRefs.JELLY_DRIFTER)), 0.5F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      if (entityIn.isInWater()) {
         matrixStackIn.scale(1.0F, Mth.cos((entityIn.tickCount + partialTicks) * 0.07F) * 0.05F + 0.9F, 1.0F);
      }

      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
      if (entityIn.isInWater()) {
         matrixStackIn.scale(1.0F, Mth.cos(partialTicks * 0.07F) * 0.05F + 0.9F, 1.0F);
      }
   }

   protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      if (entityLiving.deathTime > 0) {
         float f = (entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
         f = Mth.sqrt(f);
         if (f > 1.0F) {
            f = 1.0F;
         }

         matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(entityLiving)));
      }

      float f = Mth.lerp(partialTicks, entityLiving.prevJellyPitch, entityLiving.jellyPitch);
      float f1 = Mth.lerp(partialTicks, entityLiving.prevJellyYaw, entityLiving.jellyYaw);
      matrixStackIn.translate(0.0, 0.5, 0.0);
      matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
      matrixStackIn.mulPose(Axis.XP.rotationDegrees(f));
      matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
      if (entityLiving.isInWaterOrBubble()) {
         matrixStackIn.translate(0.0, -1.0, 0.0);
      } else {
         matrixStackIn.translate(0.0, -0.5, 0.0);
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return JellyDrifterEntity.Colors.byId(entity.getColor()).getTexture();
   }

   @Override
   protected boolean shouldCull() {
      return false;
   }
}
