package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.CrystalCamelModel;
import com.legacy.blue_skies.entities.passive.CrystalCamelEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class CrystalCamelRenderer<T extends CrystalCamelEntity> extends MobRenderer<T, CrystalCamelModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/crystal_camel/crystal_camel.png");

   public CrystalCamelRenderer(Context context) {
      super(context, new CrystalCamelModel(context.bakeLayer(SkiesRenderRefs.CRYSTAL_CAMEL)), 1.0F);
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      matrixStackIn.pushPose();
      ((CrystalCamelModel)this.model).saddled = entityIn.isSaddled();
      ((CrystalCamelModel)this.model).chested = entityIn.hasChest();
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
      matrixStackIn.popPose();
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
