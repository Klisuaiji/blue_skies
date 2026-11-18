package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.ShrumptyModel;
import com.legacy.blue_skies.entities.hostile.ShrumptyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShrumptyRenderer<T extends ShrumptyEntity> extends MobRenderer<T, ShrumptyModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/shrumpty/shrumpty.png");

   public ShrumptyRenderer(Context context) {
      super(context, new ShrumptyModel(context.bakeLayer(SkiesRenderRefs.SHRUMPTY)), 0.5F);
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
