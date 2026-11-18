package com.legacy.blue_skies.client.renders.entities.hostile.boss;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.boss.StarlitCrusherModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StarlitCrusherRenderer<T extends StarlitCrusherEntity> extends MobRenderer<T, StarlitCrusherModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/starlit_crusher/starlit_crusher.png");
   private static final ResourceLocation TEXTURE_XMAS = BlueSkies.locate("textures/entity/starlit_crusher/starlit_crusher_xmas.png");
   private static final ResourceLocation TEXTURE_XMAS_GLOW = BlueSkies.locate("textures/entity/starlit_crusher/starlit_crusher_xmas_glow.png");

   public StarlitCrusherRenderer(Context context) {
      super(context, new StarlitCrusherModel(context.bakeLayer(SkiesRenderRefs.STARLIT_CRUSHER)), 1.6F);
      this.addLayer(new EmissiveRenderLayer<T, StarlitCrusherModel<T>>(this, TEXTURE_XMAS_GLOW) {
         protected boolean shouldRenderOverlay(T entity) {
            return BlueSkiesConfig.COMMON.isChristmas();
         }
      });
   }

   public Vec3 getRenderOffset(T entityIn, float partialTicks) {
      return entityIn.getDeathTicks() > 0
         ? new Vec3(entityIn.level().random.nextGaussian() * 0.02, -(entityIn.getDeathTicks() * 0.04F), entityIn.level().random.nextGaussian() * 0.02)
         : super.getRenderOffset(entityIn, partialTicks);
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.4F, 1.4F, 1.4F);
   }

   protected float getFlipDegrees(T entityLivingBaseIn) {
      return 0.0F;
   }

   protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
      if (entityLiving.isSpinning()) {
         matrixStackIn.mulPose(Axis.YP.rotationDegrees((entityLiving.tickCount + partialTicks) * -60.0F));
      }
   }

   public ResourceLocation getTextureLocation(StarlitCrusherEntity entity) {
      return BlueSkiesConfig.COMMON.isChristmas() ? TEXTURE_XMAS : TEXTURE;
   }
}
