package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.CosmicFoxModel;
import com.legacy.blue_skies.entities.passive.CosmicFoxEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class CosmicFoxRenderer<T extends CosmicFoxEntity> extends MobRenderer<T, CosmicFoxModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/cosmic_fox/cosmic_fox.png");

   public CosmicFoxRenderer(Context context) {
      super(context, new CosmicFoxModel(context.bakeLayer(SkiesRenderRefs.COSMIC_FOX)), 0.5F);
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
      if (entitylivingbaseIn.isInSittingPose()) {
         matrixStackIn.translate(0.0, 0.25, 0.0);
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
