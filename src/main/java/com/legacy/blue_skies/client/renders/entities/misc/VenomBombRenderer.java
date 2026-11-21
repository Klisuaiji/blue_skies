package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.entities.projectile.VenomBombEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VenomBombRenderer<T extends VenomBombEntity> extends EntityRenderer<T> {
   private static final ResourceLocation LLAMA_SPIT_TEXTURE = ResourceLocation.parse("textures/entity/llama/spit.png");
   private final LlamaSpitModel<VenomBombEntity> model;

   public VenomBombRenderer(Context context) {
      super(context);
      this.model = new LlamaSpitModel(context.bakeLayer(ModelLayers.LLAMA_SPIT));
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      matrixStackIn.pushPose();
      matrixStackIn.translate(0.0, 0.15F, 0.0);
      matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
      matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
      this.model.setupAnim(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
      VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(LLAMA_SPIT_TEXTURE));
      this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
      matrixStackIn.popPose();
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return LLAMA_SPIT_TEXTURE;
   }
}
