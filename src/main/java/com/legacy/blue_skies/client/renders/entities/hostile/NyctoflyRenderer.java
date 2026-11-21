package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.NyctoflyModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.NyctoflyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NyctoflyRenderer<T extends NyctoflyEntity> extends MobRenderer<T, NyctoflyModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/nyctofly/nyctofly.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/nyctofly/nycto_glow.png");

   public NyctoflyRenderer(Context context) {
      super(context, new NyctoflyModel(context.bakeLayer(SkiesRenderRefs.NYCTOFLY)), 0.7F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTicks) {
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
