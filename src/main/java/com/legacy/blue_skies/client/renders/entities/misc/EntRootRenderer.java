package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.misc.EntRootModel;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EntRootRenderer<T extends EntRootEntity> extends EntityRenderer<T> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/starlit_crusher/small_root.png");
   private static final ResourceLocation LARGE_TEXTURE = BlueSkies.locate("textures/entity/starlit_crusher/large_root.png");
   private final EntityModel<T> rootModel;

   public EntRootRenderer(Context context) {
      super(context);
      this.rootModel = new EntRootModel<T>(context.bakeLayer(SkiesRenderRefs.ENT_ROOT));
   }

   public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      float minMax = entity.isLarge() ? 1.7F : 1.0F;
      matrixStackIn.pushPose();
      matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
      float translateAmount = (entity.tickCount + partialTicks) * 0.1F;
      float downwardTranslateAmount = (entity.downwardTranslateAmount - partialTicks) * 0.1F;
      matrixStackIn.translate(0.0F, -1.5F, 0.0F);
      if (entity.tickCount < 45) {
         matrixStackIn.translate(0.0F, minMax + -Math.min(minMax, translateAmount), 0.0F);
      } else {
         matrixStackIn.translate(0.0F, 0.0F + -Math.max(-minMax, downwardTranslateAmount), 0.0F);
      }

      VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.rootModel.renderType(this.getTextureLocation(entity)));
      this.rootModel.setupAnim(entity, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F);
      this.rootModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
      matrixStackIn.popPose();
      super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return entity.isLarge() ? LARGE_TEXTURE : TEXTURE;
   }
}
