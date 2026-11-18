package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.fish.CharscaleMokiModel;
import com.legacy.blue_skies.entities.passive.fish.CharscaleMokiEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CharscaleMokiRenderer<T extends CharscaleMokiEntity> extends MobRenderer<T, CharscaleMokiModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/fish/charscale_moki.png");

   public CharscaleMokiRenderer(Context context) {
      super(context, new CharscaleMokiModel(context.bakeLayer(SkiesRenderRefs.CHARSCALE_MOKI)), 0.5F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }

   protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      float f = 4.3F * Mth.sin(0.6F * ageInTicks);
      matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
      if (!entityLiving.isInWater()) {
         matrixStackIn.translate(0.1F, 0.1F, -0.1F);
         matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
      }
   }
}
