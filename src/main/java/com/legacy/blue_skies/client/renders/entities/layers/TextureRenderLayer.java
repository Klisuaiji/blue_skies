package com.legacy.blue_skies.client.renders.entities.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TextureRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   protected final RenderLayerParent<T, M> parentRenderer;
   protected static final Minecraft mc = Minecraft.getInstance();
   protected RenderType type;
   protected EntityModel<T> layerModel = super.getParentModel();

   public TextureRenderLayer(RenderLayerParent<T, M> rendererIn, EntityModel<T> customModelIn, ResourceLocation glowLocationIn) {
      this(rendererIn, glowLocationIn);
      this.layerModel = customModelIn;
   }

   public TextureRenderLayer(RenderLayerParent<T, M> rendererIn, EntityModel<T> customModelIn) {
      this(rendererIn);
      this.layerModel = customModelIn;
   }

   public TextureRenderLayer(RenderLayerParent<T, M> rendererIn, ResourceLocation glowLocationIn) {
      this(rendererIn);
      this.type = RenderType.entityCutoutNoCull(glowLocationIn);
   }

   public TextureRenderLayer(RenderLayerParent<T, M> rendererIn) {
      super(rendererIn);
      this.parentRenderer = rendererIn;
   }

   public void render(
      PoseStack matrixStackIn,
      MultiBufferSource bufferIn,
      int packedLightIn,
      T entityIn,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      if (this.shouldRenderOverlay(entityIn)) {
         this.getParentModel().copyPropertiesTo(this.layerModel);
         this.layerModel.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTicks);
         this.layerModel.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.type);
         this.layerModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   protected abstract boolean shouldRenderOverlay(T var1);
}
