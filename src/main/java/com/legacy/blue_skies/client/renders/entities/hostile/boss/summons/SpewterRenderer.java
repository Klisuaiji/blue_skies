package com.legacy.blue_skies.client.renders.entities.hostile.boss.summons;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.SpewterModel;
import com.legacy.blue_skies.entities.hostile.SpewterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpewterRenderer<T extends SpewterEntity> extends MobRenderer<T, SpewterModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/spewter/spewter.png");

   public SpewterRenderer(Context context) {
      super(context, new SpewterModel(context.bakeLayer(SkiesRenderRefs.SPEWTER)), 1.0F);
   }

   protected void scale(T spewter, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.4F, 1.4F, 1.4F);
      if (spewter.isGrown() && spewter.tickCount < 29) {
         matrixStackIn.translate(0.0F, 1.0F - (spewter.tickCount + partialTicks) * 0.035F, 0.0F);
      }

      float f1 = (spewter.prevCounter + (spewter.counter - spewter.prevCounter) * partialTicks) / 20.0F;
      if (f1 < 0.0F) {
         f1 = 0.0F;
      }

      f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
      float amount = 1.0F - (spewter.deathTime > 0 ? (spewter.deathTime + partialTicks) * 0.07F : 0.0F);
      float f2 = (amount + f1) / 2.0F;
      float f3 = (amount + 1.0F / f1) / 2.0F;
      if (spewter.hurtTime <= 0 && spewter.deathTime <= 0) {
         matrixStackIn.scale(f2, f3, f2);
      } else {
         matrixStackIn.scale(f3, f2, f3);
      }
   }

   protected float getFlipDegrees(SpewterEntity entityLivingBaseIn) {
      return 0.0F;
   }

   public ResourceLocation getTextureLocation(SpewterEntity entity) {
      return TEXTURE;
   }
}
