package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.EmberbackModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.EmberbackEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EmberbackRenderer<T extends EmberbackEntity> extends MobRenderer<T, EmberbackModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/emberback/emberback.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/emberback/emberback_glow.png");

   public EmberbackRenderer(Context context) {
      super(context, new EmberbackModel(context.bakeLayer(SkiesRenderRefs.EMBERBACK)), 0.7F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.15F, 1.15F, 1.15F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
