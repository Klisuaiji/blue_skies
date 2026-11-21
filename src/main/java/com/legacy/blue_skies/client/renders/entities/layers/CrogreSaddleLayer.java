package com.legacy.blue_skies.client.renders.entities.layers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.models.entities.passive.CrogreModel;
import com.legacy.blue_skies.entities.passive.CrogreEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrogreSaddleLayer<T extends CrogreEntity> extends RenderLayer<T, CrogreModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/crogre/saddle.png");
   private final CrogreModel<T> entityModel;

   public CrogreSaddleLayer(RenderLayerParent<T, CrogreModel<T>> parent, CrogreModel<T> crogreModel) {
      super(parent);
      this.entityModel = crogreModel;
   }

   public void render(
      PoseStack matrixStackIn,
      MultiBufferSource bufferIn,
      int packedLightIn,
      T entitylivingbaseIn,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      if (entitylivingbaseIn.isSaddled()) {
         ((CrogreModel)this.getParentModel()).copyPropertiesTo(this.entityModel);
         this.entityModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.entityModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
         this.entityModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
      }
   }
}
