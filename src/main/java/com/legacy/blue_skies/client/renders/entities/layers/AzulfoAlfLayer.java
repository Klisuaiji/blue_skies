package com.legacy.blue_skies.client.renders.entities.layers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.models.entities.passive.AzulfoModel;
import com.legacy.blue_skies.entities.passive.AzulfoEntity;
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
public class AzulfoAlfLayer<T extends AzulfoEntity> extends RenderLayer<T, AzulfoModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/azulfo/alf.png");
   private final AzulfoModel<T> azulfoModel;

   public AzulfoAlfLayer(RenderLayerParent<T, AzulfoModel<T>> model, AzulfoModel<T> azulfoModel) {
      super(model);
      this.azulfoModel = azulfoModel;
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
      if (entitylivingbaseIn.hasCustomName() && "alf".equals(entitylivingbaseIn.getName().getContents())) {
         ((AzulfoModel)this.getParentModel()).copyPropertiesTo(this.azulfoModel);
         this.azulfoModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.azulfoModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
         this.azulfoModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
      }
   }
}
