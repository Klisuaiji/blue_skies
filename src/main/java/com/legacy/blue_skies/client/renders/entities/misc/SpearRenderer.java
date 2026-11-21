package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.misc.SpearModel;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import com.legacy.blue_skies.registries.SkiesItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpearRenderer<T extends SpearEntity> extends EntityRenderer<T> {
   public static final ResourceLocation BLUEBRIGHT_SPEAR = BlueSkies.locate("textures/entity/spear/bluebright_spear.png");
   public static final ResourceLocation STARLIT_SPEAR = BlueSkies.locate("textures/entity/spear/starlit_spear.png");
   public static final ResourceLocation FROSTBRIGHT_SPEAR = BlueSkies.locate("textures/entity/spear/frostbright_spear.png");
   public static final ResourceLocation LUNAR_SPEAR = BlueSkies.locate("textures/entity/spear/lunar_spear.png");
   public static final ResourceLocation DUSK_SPEAR = BlueSkies.locate("textures/entity/spear/dusk_spear.png");
   public static final ResourceLocation MAPLE_SPEAR = BlueSkies.locate("textures/entity/spear/maple_spear.png");
   public static final ResourceLocation COMET_SPEAR = BlueSkies.locate("textures/entity/spear/comet_spear.png");
   public static final ResourceLocation SOULBOUND_SPEAR = BlueSkies.locate("textures/entity/spear/soulbound_spear.png");
   public static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/spear/spear_glow.png");
   public static final ResourceLocation SOULBOUND_GLOW = BlueSkies.locate("textures/entity/spear/soulbound_spear_glow.png");
   private final SpearModel spearModel;
   private final SpearModel soulboundModel;

   public SpearRenderer(Context context) {
      super(context);
      this.spearModel = new SpearModel(context.bakeLayer(SkiesRenderRefs.SPEAR));
      this.soulboundModel = new SpearModel(context.bakeLayer(SkiesRenderRefs.SOULBOUND_SPEAR));
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      if (entityIn.tickCount >= 1) {
         matrixStackIn.pushPose();
         matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
         matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) + 90.0F));
         VertexConsumer builder = ItemRenderer.getFoilBufferDirect(
            bufferIn, RenderType.entityCutout(this.getTextureLocation(entityIn)), false, entityIn.hasEffect()
         );
         this.getModel(entityIn).renderToBuffer(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
         VertexConsumer glowBuilder = ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.eyes(this.getGlowTexture(entityIn)), false, entityIn.hasEffect());
         this.getModel(entityIn).renderToBuffer(matrixStackIn, glowBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, -1);
         matrixStackIn.popPose();
         super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return getSpearTexture(entity.getPickupItem().getItem());
   }

   public static ResourceLocation getSpearTexture(Item item) {
      if (item.equals(SkiesItems.starlit_spear)) {
         return STARLIT_SPEAR;
      } else if (item.equals(SkiesItems.frostbright_spear)) {
         return FROSTBRIGHT_SPEAR;
      } else if (item.equals(SkiesItems.lunar_spear)) {
         return LUNAR_SPEAR;
      } else if (item.equals(SkiesItems.dusk_spear)) {
         return DUSK_SPEAR;
      } else if (item.equals(SkiesItems.maple_spear)) {
         return MAPLE_SPEAR;
      } else if (item.equals(SkiesItems.comet_spear)) {
         return COMET_SPEAR;
      } else {
         return item.equals(SkiesItems.soulbound_spear) ? SOULBOUND_SPEAR : BLUEBRIGHT_SPEAR;
      }
   }

   public ResourceLocation getGlowTexture(T entity) {
      return entity.getPickupItem().getItem() == SkiesItems.soulbound_spear ? SOULBOUND_GLOW : GLOW;
   }

   public SpearModel getModel(T entityIn) {
      return entityIn.getPickupItem().getItem() == SkiesItems.soulbound_spear ? this.soulboundModel : this.spearModel;
   }
}
