package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.FireflyModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.passive.FireflyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class FireflyRenderer<T extends FireflyEntity> extends MobRenderer<T, FireflyModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/firefly/firefly.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/firefly/firefly_glow.png");

   public FireflyRenderer(Context context) {
      super(context, new FireflyModel(context.bakeLayer(SkiesRenderRefs.FIREFLY)), 0.25F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
