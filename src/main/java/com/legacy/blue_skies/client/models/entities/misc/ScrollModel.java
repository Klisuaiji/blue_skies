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

public class ScrollModel extends Model {
   protected final ModelPart topHandle;
   protected final ModelPart bottomHandle;
   protected final ModelPart page;
   protected final ModelPart foldedTopHandle;
   protected final ModelPart foldedBottomHandle;
   protected final ModelPart foldedPage;
   private final boolean folded;

   public ScrollModel(ModelPart model, boolean folded) {
      super(RenderType::entityCutout);
      this.folded = folded;
      this.topHandle = model.getChild("top_handle");
      this.bottomHandle = model.getChild("bottom_handle");
      this.page = model.getChild("page_handle");
      this.foldedTopHandle = model.getChild("folded_top_handle");
      this.foldedBottomHandle = model.getChild("folded_bottom_handle");
      this.foldedPage = model.getChild("folded_page");
   }

   public static LayerDefinition createScrollLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "top_handle", CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, -0.5F, -0.51F, 9.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, -5.5F, 0.01F)
      );
      root.addOrReplaceChild(
         "bottom_handle", CubeListBuilder.create().texOffs(0, 12).addBox(-4.5F, -1.0F, -0.5F, 9.0F, 1.0F, 1.0F), PartPose.offset(-0.5F, 5.5F, 0.0F)
      );
      root.addOrReplaceChild(
         "page_handle",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -5.0F, 0.0F, 7.0F, 10.0F, 0.0F, new CubeDeformation(1.0E-4F)),
         PartPose.offset(-0.5F, 0.0F, -0.005F)
      );
      root.addOrReplaceChild(
         "folded_top_handle", CubeListBuilder.create().texOffs(3, 15).addBox(-1.5F, -0.5F, -0.51F, 1.0F, 9.0F, 1.0F), PartPose.offset(-0.5F, -5.5F, 0.01F)
      );
      root.addOrReplaceChild(
         "folded_bottom_handle", CubeListBuilder.create().texOffs(7, 15).addBox(0.5F, -11.5F, -0.49F, 1.0F, 9.0F, 1.0F), PartPose.offset(-0.5F, 5.5F, -0.01F)
      );
      root.addOrReplaceChild(
         "folded_page", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, -5.0F, 0.0F, 1.0F, 7.0F, 0.0F), PartPose.offset(-0.5F, 0.0F, -0.005F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   // 1.21.1 Model#renderToBuffer takes a packed ARGB int instead of four color floats.
   public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
      if (this.folded) {
         this.foldedTopHandle.render(matrixStack, buffer, packedLight, packedOverlay, color);
         this.foldedBottomHandle.render(matrixStack, buffer, packedLight, packedOverlay, color);
         this.foldedPage.render(matrixStack, buffer, packedLight, packedOverlay, color);
      } else {
         this.topHandle.render(matrixStack, buffer, packedLight, packedOverlay, color);
         this.bottomHandle.render(matrixStack, buffer, packedLight, packedOverlay, color);
         this.page.render(matrixStack, buffer, packedLight, packedOverlay, color);
      }
   }
}
