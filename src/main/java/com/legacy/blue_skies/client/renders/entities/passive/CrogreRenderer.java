package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.CrogreModel;
import com.legacy.blue_skies.client.renders.entities.layers.TextureRenderLayer;
import com.legacy.blue_skies.entities.passive.CrogreEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrogreRenderer<T extends CrogreEntity> extends MobRenderer<T, CrogreModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/crogre/crogre.png");
   private static final ResourceLocation SADDLE_TEXTURE = BlueSkies.locate("textures/entity/crogre/saddle.png");

   public CrogreRenderer(Context context) {
      super(context, new CrogreModel(context.bakeLayer(SkiesRenderRefs.CROGRE)), 1.1F);
      this.addLayer(new TextureRenderLayer<T, CrogreModel<T>>(this, new CrogreModel(context.bakeLayer(SkiesRenderRefs.CROGRE_SADDLE)), SADDLE_TEXTURE) {
         protected boolean shouldRenderOverlay(T entity) {
            return entity.isSaddled();
         }
      });
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
      matrixStackIn.translate(0.0F, 0.0F, 0.05F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
