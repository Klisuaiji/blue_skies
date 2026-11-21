package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.PolargeistModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.client.renders.entities.util.TranslucentMobRenderer;
import com.legacy.blue_skies.entities.hostile.PolargeistEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PolargeistRenderer<T extends PolargeistEntity> extends TranslucentMobRenderer<T, PolargeistModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/polargeist/polargeist.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/polargeist/polargeist_layer.png");

   public PolargeistRenderer(Context context) {
      super(context, new PolargeistModel<>(context.bakeLayer(SkiesRenderRefs.POLARGEIST)), 0.0F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
