package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.misc.EntWallModel;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EntWallRenderer<T extends EntWallEntity> extends LivingEntityRenderer<T, EntWallModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/starlit_crusher/wall.png");

   public EntWallRenderer(Context context) {
      super(context, new EntWallModel(context.bakeLayer(SkiesRenderRefs.ENT_WALL)), 0.0F);
   }

   protected void scale(T entityliving, PoseStack matrixStackIn, float partialTicks) {
      if (!entityliving.isFacingZ()) {
         matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
      }

      float translateAmount = (entityliving.tickCount + partialTicks) * 0.1F;
      if (entityliving.tickCount < 45) {
         matrixStackIn.translate(0.0F, 3.0F + -Math.min(3.0F, translateAmount), 0.0F);
      }
   }

   protected boolean shouldShowName(T entity) {
      return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
   }

   protected float getFlipDegrees(T entityLivingBaseIn) {
      return 0.0F;
   }

   // 1.21.1 appended a float scale argument to LivingEntityRenderer#setupRotations.
   protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
   }

   public static String getVariant(EntityRenderer<EntWallEntity> entityRenderer, Entity entity) {
      if (entityRenderer instanceof EntWallRenderer) {
         EntWallEntity wall = (EntWallEntity)entity;
         return TEXTURE.toString() + "%" + wall.isFacingZ();
      } else {
         return TEXTURE.toString();
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
