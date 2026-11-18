package com.legacy.blue_skies.client.renders.entities.hostile.boss.summons;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.VenomSpiderModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.boss.summons.NestedSpiderEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NestedSpiderRenderer<T extends NestedSpiderEntity> extends MobRenderer<T, VenomSpiderModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/arachnarch/nested_spider.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/arachnarch/nested_spider_eyes.png");

   public NestedSpiderRenderer(Context context) {
      super(context, new VenomSpiderModel(context.bakeLayer(SkiesRenderRefs.NESTED_SPIDER)), 0.7F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(0.7F, 0.7F, 0.7F);
   }

   public ResourceLocation getTextureLocation(NestedSpiderEntity entity) {
      return TEXTURE;
   }
}
