package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.ArmoredFrostSpiritModel;
import com.legacy.blue_skies.client.renders.entities.util.TranslucentMobRenderer;
import com.legacy.blue_skies.entities.hostile.ArmoredFrostSpiritEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ArmoredFrostSpiritRenderer<T extends ArmoredFrostSpiritEntity> extends TranslucentMobRenderer<T, ArmoredFrostSpiritModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/frost_spirit/frost_spirit_ice.png");
   private static final ResourceLocation TEXTURE_PACKED = BlueSkies.locate("textures/entity/frost_spirit/frost_spirit_packed_ice.png");

   public ArmoredFrostSpiritRenderer(Context context) {
      super(context, new ArmoredFrostSpiritModel<>(context.bakeLayer(SkiesRenderRefs.ARMORED_FROST_SPIRIT)), 0.5F);
      this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      if (entityIn.deathTime > 0.0F) {
         matrixStackIn.scale(0.0F, 0.0F, 0.0F);
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return entity.getIceType() == 1 ? TEXTURE_PACKED : TEXTURE;
   }
}
