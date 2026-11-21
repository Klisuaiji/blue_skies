package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.misc.EntLeafModel;
import com.legacy.blue_skies.entities.projectile.EntLeafEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntLeafRenderer<T extends EntLeafEntity> extends EntityRenderer<T> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/starlit_crusher/leaf.png");
   private final EntLeafModel<T> model;

   public EntLeafRenderer(Context context) {
      super(context);
      this.model = new EntLeafModel(context.bakeLayer(SkiesRenderRefs.ENT_LEAF));
   }

   public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      matrixStackIn.pushPose();
      matrixStackIn.mulPose(Axis.YP.rotationDegrees((entity.tickCount + partialTicks) * -60.0F));
      matrixStackIn.translate(0.0, -1.45, 0.0);
      VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
      this.model.setupAnim(entity, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F);
      this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
      matrixStackIn.popPose();
      super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
