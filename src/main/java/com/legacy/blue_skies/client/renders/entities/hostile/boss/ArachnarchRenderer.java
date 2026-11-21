package com.legacy.blue_skies.client.renders.entities.hostile.boss;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.boss.ArachnarchModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArachnarchRenderer<T extends ArachnarchEntity> extends MobRenderer<T, ArachnarchModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/arachnarch/arachnarch.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/arachnarch/arachnarch_glow.png");
   private static final ResourceLocation TEXTURE_HALLOWEEN = BlueSkies.locate("textures/entity/arachnarch/arachnarch_halloween.png");
   private static final ResourceLocation GLOW_HALLOWEEN = BlueSkies.locate("textures/entity/arachnarch/arachnarch_glow_halloween.png");

   public ArachnarchRenderer(Context context) {
      super(context, new ArachnarchModel(context.bakeLayer(SkiesRenderRefs.ARACHNARCH)), 1.5F);
      this.addLayer(new EmissiveRenderLayer(this, BlueSkiesConfig.COMMON.isHalloween() ? GLOW_HALLOWEEN : GLOW));
   }

   protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
      Level world = entity.level();
      BlockPos pos = entity.blockPosition();
      boolean climbing = entity.isBesideClimbableBlock();
      if ((entity.onGround() || !climbing) && !entity.isCeilingLocked()) {
         matrixStackIn.translate(0.0F, 0.1F, 0.0F);
      } else if (!world.isEmptyBlock(pos.above(2)) && entity.isCeilingLocked()) {
         matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
         matrixStackIn.translate(0.0F, 1.85F, 0.0F);
      }

      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
      if (entity.deathTime > 0) {
         matrixStackIn.translate(0.0F, 1.0F, 0.0F);
      }
   }

   // 1.21.1 appended a float scale argument to LivingEntityRenderer#setupRotations.
   protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
      if (entityLiving.deathTicks > 0) {
         rotationYaw += (float)(Math.cos(entityLiving.tickCount * 3.25) * Math.PI * 0.55);
      }

      if (entityLiving.clientScreechTime > 0) {
         rotationYaw += (float)(Math.sin(entityLiving.tickCount * 3.25) * Math.PI * 0.55);
      }

      super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, scale);
   }

   protected float getFlipDegrees(T entityLivingBaseIn) {
      return 180.0F;
   }

   public ResourceLocation getTextureLocation(T entity) {
      return BlueSkiesConfig.COMMON.isHalloween() ? TEXTURE_HALLOWEEN : TEXTURE;
   }
}
