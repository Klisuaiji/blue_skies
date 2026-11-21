package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.WhistleshellCrabModel;
import com.legacy.blue_skies.entities.hostile.WhistleshellCrabEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WhistleshellCrabRenderer<T extends WhistleshellCrabEntity> extends MobRenderer<T, WhistleshellCrabModel<T>> {
   private static final ResourceLocation TEXTURE_GREEN = BlueSkies.locate("textures/entity/whistleshell_crab/whistleshell_crab_green.png");
   private static final ResourceLocation TEXTURE_RED = BlueSkies.locate("textures/entity/whistleshell_crab/whistleshell_crab_red.png");

   public WhistleshellCrabRenderer(Context context) {
      super(context, new WhistleshellCrabModel(context.bakeLayer(SkiesRenderRefs.WHISTLESHELL_CRAB)), 0.7F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.15F, 1.15F, 1.15F);
      matrixStackIn.translate(0.0F, 0.0F, -0.15F);
   }

   // 1.21.1 appended a float scale argument to LivingEntityRenderer#setupRotations.
   protected void setupRotations(T entityIn, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
      super.setupRotations(entityIn, matrixStackIn, ageInTicks, rotationYaw, partialTicks, scale);
      float f = ageInTicks - entityIn.tickCount;
      float animScale = entityIn.getSidewaysAnimationScale(f);
      animScale *= animScale;
      matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F * animScale));
   }

   public ResourceLocation getTextureLocation(T entity) {
      return entity.getCrabType() == 1 ? TEXTURE_GREEN : TEXTURE_RED;
   }
}
