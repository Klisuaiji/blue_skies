package com.legacy.blue_skies.client.renders.entities.layers;

import com.legacy.blue_skies.client.renders.entities.util.IMultiGlowRenderer;
import com.legacy.blue_skies.client.renders.entities.util.IMultiModelRenderer;
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
public class EmissiveRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   protected final RenderLayerParent<T, M> parentRenderer;
   protected static final Minecraft mc = Minecraft.getInstance();
   protected RenderType glow;
   protected EntityModel<T> glowModel = super.getParentModel();

   public EmissiveRenderLayer(RenderLayerParent<T, M> rendererIn, EntityModel<T> customModelIn, ResourceLocation glowLocationIn) {
      this(rendererIn, glowLocationIn);
      this.glowModel = customModelIn;
   }

   public EmissiveRenderLayer(RenderLayerParent<T, M> rendererIn, EntityModel<T> customModelIn) {
      this(rendererIn);
      this.glowModel = customModelIn;
   }

   public EmissiveRenderLayer(RenderLayerParent<T, M> rendererIn, ResourceLocation glowLocationIn) {
      this(rendererIn);
      this.glow = RenderType.eyes(glowLocationIn);
   }

   public EmissiveRenderLayer(RenderLayerParent<T, M> rendererIn) {
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
         if (this.parentRenderer != null && this.parentRenderer instanceof IMultiGlowRenderer) {
            this.glow = RenderType.eyes(((IMultiGlowRenderer)this.parentRenderer).getGlowTextures(entityIn));
         }

         VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.glow);
         this.getParentModel().copyPropertiesTo(this.getGlowModel(this.parentRenderer, entityIn));
         this.getGlowModel(this.parentRenderer, entityIn).setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         this.getGlowModel(this.parentRenderer, entityIn).prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTicks);
         this.getGlowModel(this.parentRenderer, entityIn)
            .renderToBuffer(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   protected EntityModel<T> getGlowModel(RenderLayerParent<T, M> rendererIn, T entityIn) {
      return this.parentRenderer != null && this.parentRenderer instanceof IMultiModelRenderer
         ? ((IMultiModelRenderer)rendererIn).getGlowModels(entityIn)
         : this.glowModel;
   }

   protected boolean shouldRenderOverlay(T entity) {
      return true;
   }
}
