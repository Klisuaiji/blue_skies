package com.legacy.blue_skies.client.renders.entities.hostile.boss;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.boss.SummonerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SummonerRenderer<T extends SummonerEntity> extends IllagerRenderer<T> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/summoner/summoner.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/summoner/summoner_glow.png");
   private static final ResourceLocation TOME = BlueSkies.locate("textures/entity/summoner/summoner_tome.png");
   private final BookModel modelBook;

   public SummonerRenderer(Context context) {
      super(context, new IllagerModel(context.bakeLayer(SkiesRenderRefs.SUMMONER)), 0.5F);
      this.addLayer(new EmissiveRenderLayer(this, GLOW));
      ((IllagerModel)this.model).getHat().visible = true;
      this.modelBook = new BookModel(context.bakeLayer(ModelLayers.BOOK));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.0F, 1.0F, 1.0F);
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
      if (entityIn.getInvulTime() <= 0 && entityIn.isAlive()) {
         float forwards = 0.9F;
         float up = 1.0F;
         float left = 0.5F;
         Vec2 vector2f = new Vec2(0.0F, Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot));
         Vec3 vector3d = Vec3.ZERO;
         float f = Mth.cos((vector2f.y + 90.0F) * (float) (Math.PI / 180.0));
         float f1 = Mth.sin((vector2f.y + 90.0F) * (float) (Math.PI / 180.0));
         float f2 = Mth.cos(-vector2f.x * (float) (Math.PI / 180.0));
         float f3 = Mth.sin(-vector2f.x * (float) (Math.PI / 180.0));
         float f4 = Mth.cos((-vector2f.x + 90.0F) * (float) (Math.PI / 180.0));
         float f5 = Mth.sin((-vector2f.x + 90.0F) * (float) (Math.PI / 180.0));
         Vec3 vector3d1 = new Vec3(f * f2, f3, f1 * f2);
         Vec3 vector3d2 = new Vec3(f * f4, f5, f1 * f4);
         Vec3 vector3d3 = vector3d1.cross(vector3d2).scale(-1.0);
         double d0 = vector3d1.x * forwards + vector3d2.x * up + vector3d3.x * left;
         double d1 = vector3d1.y * forwards + vector3d2.y * up + vector3d3.y * left;
         double d2 = vector3d1.z * forwards + vector3d2.z * up + vector3d3.z * left;
         Vec3 finalVec = new Vec3(vector3d.x + d0, vector3d.y + d1, vector3d.z + d2);
         matrixStackIn.pushPose();
         matrixStackIn.translate(0.0, 1.3, 0.0);
         float arg1 = entityIn.tickCount + partialTicks;
         matrixStackIn.translate(finalVec.x(), 0.1F + Mth.sin(arg1 * 0.2F) * 0.07F, finalVec.z());
         float arg2 = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
         matrixStackIn.mulPose(Axis.YP.rotationDegrees(-arg2 + 140.0F));
         matrixStackIn.mulPose(Axis.ZP.rotationDegrees(40.0F));
         float arg3 = 1.0F;
         float arg4 = Mth.frac(arg3 + 0.25F) * 1.6F - 0.3F;
         float arg5 = Mth.frac(arg3 + 0.75F) * 1.6F - 0.3F;
         this.modelBook.setupAnim(arg1, Mth.clamp(arg4, 0.0F, 1.0F), Mth.clamp(arg5, 0.0F, 1.0F), entityIn.isCastingSpell() ? 1.0F : 0.0F);
         VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutout(TOME));
         this.modelBook
            .render(
               matrixStackIn,
               ivertexbuilder,
               packedLightIn,
               getOverlayCoords(entityIn, this.getWhiteOverlayProgress(entityIn, partialTicks)), -1);
         matrixStackIn.popPose();
      }
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
