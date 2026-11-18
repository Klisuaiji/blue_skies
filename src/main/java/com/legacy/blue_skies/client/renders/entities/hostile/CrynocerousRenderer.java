package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.CrynocerousModel;
import com.legacy.blue_skies.client.renders.entities.util.TranslucentMobRenderer;
import com.legacy.blue_skies.entities.hostile.CrynocerousEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class CrynocerousRenderer<T extends CrynocerousEntity> extends TranslucentMobRenderer<T, CrynocerousModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/frost_spirit/cryno_ice.png");
   private static final ResourceLocation TEXTURE_PACKED = BlueSkies.locate("textures/entity/frost_spirit/cryno_packed_ice.png");

   public CrynocerousRenderer(Context context) {
      super(context, new CrynocerousModel<>(context.bakeLayer(SkiesRenderRefs.CRYNOCEROUS)), 1.5F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      if (entityIn.deathTime > 0.0F) {
         matrixStackIn.scale(0.0F, 0.0F, 0.0F);
      } else {
         matrixStackIn.scale(1.2F, 1.2F, 1.2F);
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return entity.getIceType() == 1 ? TEXTURE_PACKED : TEXTURE;
   }
}
