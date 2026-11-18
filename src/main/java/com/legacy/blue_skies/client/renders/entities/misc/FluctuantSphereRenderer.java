package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.projectile.FluctuantSphereEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FluctuantSphereRenderer<T extends FluctuantSphereEntity> extends EntityRenderer<T> {
   private static final ResourceLocation SPHERE_TEXTURE = BlueSkies.locate("textures/entity/summoner/fluctuant_sphere.png");

   public FluctuantSphereRenderer(Context context) {
      super(context);
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
   }

   public ResourceLocation getTextureLocation(FluctuantSphereEntity entity) {
      return SPHERE_TEXTURE;
   }
}
