package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.ShadeMonitorModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.passive.ShadeMonitorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShadeMonitorRenderer<T extends ShadeMonitorEntity> extends MobRenderer<T, ShadeMonitorModel<T>> {
   private static final ResourceLocation TEXTURE_HALLOWEEN = BlueSkies.locate("textures/entity/shade_monitor/halloween.png");
   private static final ResourceLocation TEXTURE_HALLOWEEN_EYES = BlueSkies.locate("textures/entity/shade_monitor/halloween_eyes.png");

   public ShadeMonitorRenderer(Context context) {
      super(context, new ShadeMonitorModel(context.bakeLayer(SkiesRenderRefs.SHADE_MONITOR)), 1.0F);
      this.addLayer(new EmissiveRenderLayer<T, ShadeMonitorModel<T>>(this, TEXTURE_HALLOWEEN_EYES) {
         protected boolean shouldRenderOverlay(T entity) {
            return entity.getFestiveStatus() >= 2;
         }
      });
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
      matrixStackIn.scale(1.4F, 1.4F, 1.4F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      int festiveStatus = entity.getFestiveStatus();
      return festiveStatus < 2 && (!BlueSkiesConfig.COMMON.isHalloween() || festiveStatus < 1)
         ? ShadeMonitorEntity.Biomes.byId(entity.getBiome()).getTexture()
         : TEXTURE_HALLOWEEN;
   }
}
