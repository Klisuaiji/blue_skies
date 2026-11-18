package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.SnowOwlModel;
import com.legacy.blue_skies.entities.passive.SnowOwlEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnowOwlRenderer<T extends SnowOwlEntity> extends MobRenderer<T, SnowOwlModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/snow_owl/snow_owl.png");
   private static final ResourceLocation TEXTURE_HOSTILE = BlueSkies.locate("textures/entity/snow_owl/snow_owl_hostile.png");

   public SnowOwlRenderer(Context context) {
      super(context, new SnowOwlModel(context.bakeLayer(SkiesRenderRefs.SNOW_OWL)), 0.3F);
   }

   public float getBob(T livingBase, float partialTicks) {
      float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
      float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
      return (Mth.sin(f) + 1.0F) * f1;
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
      matrixStackIn.scale(1.3F, 1.3F, 1.3F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return entity.isOwlHostile() ? TEXTURE_HOSTILE : TEXTURE;
   }
}
