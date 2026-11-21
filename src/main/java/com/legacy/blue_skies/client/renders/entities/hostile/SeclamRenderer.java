package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.SeclamModel;
import com.legacy.blue_skies.entities.hostile.SeclamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SeclamRenderer<T extends SeclamEntity> extends MobRenderer<T, SeclamModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/seclam/seclam.png");

   public SeclamRenderer(Context context) {
      super(context, new SeclamModel(context.bakeLayer(SkiesRenderRefs.SECLAM)), 0.7F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
