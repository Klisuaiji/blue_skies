package com.legacy.blue_skies.client.renders.entities.layers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.models.entities.passive.ReindeerModel;
import com.legacy.blue_skies.entities.passive.ReindeerEntity;
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
public class DeerSaddleLayer<T extends ReindeerEntity> extends RenderLayer<T, ReindeerModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/reindeer/saddle.png");
   private final ReindeerModel<T> deerModel;

   public DeerSaddleLayer(RenderLayerParent<T, ReindeerModel<T>> parent, ReindeerModel<T> model) {
      super(parent);
      this.deerModel = model;
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
         ((ReindeerModel)this.getParentModel()).copyPropertiesTo(this.deerModel);
         this.deerModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.deerModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
         this.deerModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
      }
   }
}
