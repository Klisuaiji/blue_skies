package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.StoneletModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.StoneletEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StoneletRenderer<T extends StoneletEntity> extends MobRenderer<T, StoneletModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/stonelet/stonelet.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/stonelet/stonelet_glow.png");

   public StoneletRenderer(Context context) {
      super(context, new StoneletModel(context.bakeLayer(SkiesRenderRefs.STONELET)), 0.7F);
      this.addLayer(new EmissiveRenderLayer(this, new StoneletModel(context.bakeLayer(SkiesRenderRefs.STONELET), true), GLOW));
      this.addLayer(
         new ItemInHandLayer<T, StoneletModel<T>>(this, context.getItemInHandRenderer()) {
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
               matrixStackIn.pushPose();
               matrixStackIn.translate(0.0F, 0.35F, 0.0F);
               super.render(
                  matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch
               );
               matrixStackIn.popPose();
            }
         }
      );
   }

   protected void scale(T entityIn, PoseStack pose, float partialTicks) {
      pose.scale(1.2F, 1.2F, 1.2F);
      pose.translate(0.0, 0.5, 0.0);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
