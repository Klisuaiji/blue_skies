package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.fish.GrittleFlatfishModel;
import com.legacy.blue_skies.entities.passive.fish.GrittleFlatfishEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrittleFlatfishRenderer<T extends GrittleFlatfishEntity> extends MobRenderer<T, GrittleFlatfishModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/fish/grittle_flatfish.png");

   public GrittleFlatfishRenderer(Context context) {
      super(context, new GrittleFlatfishModel(context.bakeLayer(SkiesRenderRefs.GRITTLE_FLATFISH)), 0.7F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
