package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.SlivModel;
import com.legacy.blue_skies.entities.passive.SlivEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SlivRenderer<T extends SlivEntity> extends MobRenderer<T, SlivModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/sliv/sliv.png");

   public SlivRenderer(Context context) {
      super(context, new SlivModel(context.bakeLayer(SkiesRenderRefs.SLIV)), 0.4F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
