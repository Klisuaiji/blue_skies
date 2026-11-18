package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.ReindeerModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.client.renders.entities.layers.TextureRenderLayer;
import com.legacy.blue_skies.entities.passive.ReindeerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReindeerRenderer<T extends ReindeerEntity> extends MobRenderer<T, ReindeerModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/reindeer/reindeer.png");
   private static final ResourceLocation TEXTURE_BABY = BlueSkies.locate("textures/entity/reindeer/reindeer_baby.png");
   private static final ResourceLocation TEXTURE_SADDLE = BlueSkies.locate("textures/entity/reindeer/saddle.png");
   private static final ResourceLocation TEXTURE_XMAS = BlueSkies.locate("textures/entity/reindeer/reindeer_xmas.png");
   private static final ResourceLocation TEXTURE_XMAS_ANTLER_GLOW = BlueSkies.locate("textures/entity/reindeer/reindeer_xmas_antler_glow.png");
   private static final ResourceLocation TEXTURE_XMAS_ANTLER_OVERLAY = BlueSkies.locate("textures/entity/reindeer/reindeer_xmas_antler_overlay.png");

   public ReindeerRenderer(Context context) {
      super(context, new ReindeerModel(context.bakeLayer(SkiesRenderRefs.REINDEER)), 0.7F);
      this.addLayer(new TextureRenderLayer<T, ReindeerModel<T>>(this, new ReindeerModel(context.bakeLayer(SkiesRenderRefs.REINDEER_SADDLE)), TEXTURE_SADDLE) {
         protected boolean shouldRenderOverlay(T entity) {
            return entity.isSaddled();
         }
      });
      this.addLayer(new TextureRenderLayer<T, ReindeerModel<T>>(this, TEXTURE_XMAS_ANTLER_OVERLAY) {
         protected boolean shouldRenderOverlay(T entity) {
            int festiveStatus = entity.getFestiveStatus();
            return (festiveStatus >= 2 || BlueSkiesConfig.COMMON.isChristmas() && festiveStatus >= 1) && !entity.isBaby();
         }
      });
      this.addLayer(
         new EmissiveRenderLayer<T, ReindeerModel<T>>(this, new ReindeerModel(context.bakeLayer(SkiesRenderRefs.REINDEER_XMAS_GLOW)), TEXTURE_XMAS_ANTLER_GLOW) {
            protected boolean shouldRenderOverlay(T entity) {
               int festiveStatus = entity.getFestiveStatus();
               return (festiveStatus >= 2 || BlueSkiesConfig.COMMON.isChristmas() && festiveStatus >= 1) && !entity.isBaby();
            }
         }
      );
   }

   public ResourceLocation getTextureLocation(T entity) {
      if (entity.getFestiveStatus() >= 2) {
         return TEXTURE_XMAS;
      } else {
         return entity.isBaby() ? TEXTURE_BABY : TEXTURE;
      }
   }
}
