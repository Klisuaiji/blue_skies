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
import net.minecraft.util.FastColor;
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
         int color;
         if (!entitylivingbaseIn.hasCustomName()
            || !"jeb_".equals(entitylivingbaseIn.getName().getContents()) && !"Jesterguy".equals(entitylivingbaseIn.getName().getContents())) {
            // 1.21.1 removed Sheep#getColorArray(DyeColor); Sheep#getColor(DyeColor) now returns the packed ARGB wool color.
            color = StardustRamEntity.getColor(entitylivingbaseIn.getColor());
         } else {
            int i = entitylivingbaseIn.tickCount / 25 + entitylivingbaseIn.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f3 = (entitylivingbaseIn.tickCount % 25 + partialTicks) / 25.0F;
            int color1 = StardustRamEntity.getColor(DyeColor.byId(k));
            int color2 = StardustRamEntity.getColor(DyeColor.byId(l));
            color = FastColor.ARGB32.lerp(f3, color1, color2);
         }

         // 1.21.1 signature: the last three float RGB components were folded into one packed ARGB int.
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
            color
         );
      }
   }
}
