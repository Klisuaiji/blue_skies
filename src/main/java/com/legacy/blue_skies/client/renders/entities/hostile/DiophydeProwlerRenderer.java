package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.DiophydeProwlerModel;
import com.legacy.blue_skies.entities.hostile.DiophydeProwlerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DiophydeProwlerRenderer<T extends DiophydeProwlerEntity> extends MobRenderer<T, DiophydeProwlerModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/diophyde_prowler/diophyde_prowler.png");

   public DiophydeProwlerRenderer(Context context) {
      super(context, new DiophydeProwlerModel(context.bakeLayer(SkiesRenderRefs.DIOPHYDE_PROWLER)), 1.0F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.translate(0.0F, 0.0F, 0.15F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
