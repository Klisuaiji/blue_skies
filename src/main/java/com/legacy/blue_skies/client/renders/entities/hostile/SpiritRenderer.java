package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.FrostSpiritModel;
import com.legacy.blue_skies.entities.hostile.FrostSpiritEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpiritRenderer<T extends FrostSpiritEntity> extends MobRenderer<T, FrostSpiritModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/frost_spirit/frost_spirit_wings.png");

   public SpiritRenderer(Context context) {
      super(context, new FrostSpiritModel(context.bakeLayer(SkiesRenderRefs.FROST_SPIRIT)), 0.25F);
   }

   protected float getBob(T entityIn, float partialTicks) {
      float f1 = entityIn.prevWingRotation + (entityIn.wingRotation - entityIn.prevWingRotation) * partialTicks;
      float f2 = entityIn.prevDestPos + (entityIn.destPos - entityIn.prevDestPos) * partialTicks;
      return (Mth.sin(f1) + 0.0F) * f2;
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.translate(0.0, 1.5, 0.0);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
