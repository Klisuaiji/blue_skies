package com.legacy.blue_skies.client.renders.entities.layers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.models.entities.passive.StardustRamModel;
import com.legacy.blue_skies.client.models.entities.passive.StardustWoolModel;
import com.legacy.blue_skies.entities.passive.StardustRamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StardustWoolLayer<T extends StardustRamEntity> extends RenderLayer<T, StardustRamModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/stardust_ram/stardust_wool.png");
   private final StardustWoolModel<T> sheepModel;

   public StardustWoolLayer(RenderLayerParent<T, StardustRamModel<T>> parent, StardustWoolModel<T> model) {
      super(parent);
      this.sheepModel = model;
   }

   public void render(
      PoseStack matrixStackIn,
      MultiBufferSource bufferIn,
      int packedLightIn,
      T entitylivingbaseIn,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      if (!entitylivingbaseIn.isSheared() && !entitylivingbaseIn.isInvisible()) {
         float f;
         float f1;
         float f2;
         if (!entitylivingbaseIn.hasCustomName()
            || !"jeb_".equals(entitylivingbaseIn.getName().getContents()) && !"Jesterguy".equals(entitylivingbaseIn.getName().getContents())) {
            float[] afloat = StardustRamEntity.getColorArray(entitylivingbaseIn.getColor());
            f = afloat[0];
            f1 = afloat[1];
            f2 = afloat[2];
         } else {
            int i = entitylivingbaseIn.tickCount / 25 + entitylivingbaseIn.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f3 = (entitylivingbaseIn.tickCount % 25 + partialTicks) / 25.0F;
            float[] afloat1 = StardustRamEntity.getColorArray(DyeColor.byId(k));
            float[] afloat2 = StardustRamEntity.getColorArray(DyeColor.byId(l));
            f = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
            f1 = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
            f2 = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
         }

         coloredCutoutModelCopyLayerRender(
            this.getParentModel(),
            this.sheepModel,
            TEXTURE,
            matrixStackIn,
            bufferIn,
            packedLightIn,
            entitylivingbaseIn,
            limbSwing,
            limbSwingAmount,
            ageInTicks,
            netHeadYaw,
            headPitch,
            partialTicks,
            f,
            f1,
            f2
         );
      }
   }
}
