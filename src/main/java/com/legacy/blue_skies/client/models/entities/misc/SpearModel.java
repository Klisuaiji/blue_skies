package com.legacy.blue_skies.client.models.entities.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class SpearModel extends Model {
   protected final ModelPart stick;
   protected final ModelPart cross1;
   protected final ModelPart cross2;

   public SpearModel(ModelPart model) {
      super(RenderType::entitySolid);
      this.stick = model.getChild("stick");
      this.cross1 = this.stick.getChild("cross_1");
      this.cross2 = this.stick.getChild("cross_2");
   }

   public static LayerDefinition createSpearLayer(CubeDeformation size, boolean isSoulbound) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition stick = root.addOrReplaceChild(
         "stick", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 2.0F, -0.5F, 1.0F, 28.0F, 1.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      stick.addOrReplaceChild(
         "cross_1",
         CubeListBuilder.create().texOffs(4, 0).addBox(-3.5F, -4.0F, 0.0F, 7.0F, isSoulbound ? 9.0F : 7.0F, 0.0F, size),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F)
      );
      stick.addOrReplaceChild(
         "cross_2",
         CubeListBuilder.create().texOffs(4, 0).addBox(-3.5F, -4.0F, 0.0F, 7.0F, isSoulbound ? 9.0F : 7.0F, 0.0F, size),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   // 1.21.1 Model#renderToBuffer takes a packed ARGB int instead of four color floats.
   public void renderToBuffer(
      PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color
   ) {
      this.stick.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, color);
   }
}
