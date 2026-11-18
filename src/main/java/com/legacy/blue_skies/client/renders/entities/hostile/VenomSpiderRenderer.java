package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.VenomSpiderModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.VenomSpiderEntity;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VenomSpiderRenderer<T extends VenomSpiderEntity> extends MobRenderer<T, VenomSpiderModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/venom_spider/venom_spider.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/venom_spider/venom_spider_eyes.png");

   public VenomSpiderRenderer(Context context) {
      super(context, new VenomSpiderModel(context.bakeLayer(SkiesRenderRefs.VENOM_SPIDER)), 1.0F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
   }

   protected float getFlipDegrees(T entityLivingBaseIn) {
      return 180.0F;
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
