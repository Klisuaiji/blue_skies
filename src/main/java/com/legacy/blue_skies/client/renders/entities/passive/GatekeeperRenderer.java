package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.GatekeeperModel;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
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
public class GatekeeperRenderer<T extends GatekeeperEntity> extends MobRenderer<T, GatekeeperModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/gatekeeper/gatekeeper.png");

   public GatekeeperRenderer(Context context) {
      super(context, new GatekeeperModel(context.bakeLayer(SkiesRenderRefs.GATEKEEPER)), 0.5F);
      this.addLayer(
         new ItemInHandLayer<T, GatekeeperModel<T>>(this, context.getItemInHandRenderer()) {
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
               if (entitylivingbaseIn.isAggressive()) {
                  super.render(
                     matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, -1);
               }
            }
         }
      );
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
