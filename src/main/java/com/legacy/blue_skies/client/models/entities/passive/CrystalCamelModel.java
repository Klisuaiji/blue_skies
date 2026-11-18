package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.entities.passive.CrystalCamelEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class CrystalCamelModel<T extends CrystalCamelEntity> extends EntityModel<T> {
   protected final ModelPart body;
   protected final ModelPart leftHindLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart rightHindLeg;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart snoot;
   protected final ModelPart neckBottom;
   protected final ModelPart leftEar;
   protected final ModelPart head;
   protected final ModelPart rightEar;
   protected final ModelPart neckTop;
   protected final ModelPart backHump;
   protected final ModelPart frontHump;
   protected final ModelPart saddleTop;
   protected final ModelPart saddleBottom;
   protected final ModelPart chest;
   public boolean saddled = false;
   public boolean chested = false;

   public CrystalCamelModel(ModelPart model) {
      this.neckBottom = model.getChild("neck_bottom");
      this.neckTop = this.neckBottom.getChild("neck_top");
      this.head = this.neckTop.getChild("head");
      this.rightEar = this.head.getChild("right_ear");
      this.leftEar = this.head.getChild("left_ear");
      this.snoot = this.head.getChild("snoot");
      this.body = model.getChild("body");
      this.frontHump = model.getChild("front_hump");
      this.backHump = model.getChild("back_hump");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
      this.saddleTop = model.getChild("saddle_top");
      this.saddleBottom = this.saddleTop.getChild("saddle_bottom");
      this.chest = model.getChild("chest");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition neckBottom = root.addOrReplaceChild(
         "neck_bottom", CubeListBuilder.create().texOffs(0, 37).addBox(-3.0F, -3.5F, -9.5F, 6.0F, 5.0F, 8.0F), PartPose.offset(0.0F, 7.0F, -6.0F)
      );
      PartDefinition neckTop = neckBottom.addOrReplaceChild(
         "neck_top", CubeListBuilder.create().texOffs(0, 22).addBox(-3.0F, -6.5F, -2.5F, 6.0F, 9.0F, 5.0F), PartPose.offset(0.0F, -2.0F, -8.0F)
      );
      PartDefinition head = neckTop.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -5.0F, -3.5F, 6.0F, 5.0F, 7.0F), PartPose.offset(0.0F, -6.5F, 0.0F)
      );
      head.addOrReplaceChild(
         "right_ear", CubeListBuilder.create().texOffs(17, 0).mirror().addBox(-1.0F, -1.5F, -1.25F, 2.0F, 2.0F, 2.0F), PartPose.offset(-3.0F, -4.5F, 1.75F)
      );
      head.addOrReplaceChild(
         "left_ear", CubeListBuilder.create().texOffs(17, 0).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(3.0F, -4.5F, 1.5F)
      );
      head.addOrReplaceChild("snoot", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.5F, -4.0F, 4.0F, 3.0F, 4.0F), PartPose.offset(0.0F, -1.5F, -3.5F));
      root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(29, 0).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F), PartPose.offset(0.0F, 5.0F, 2.0F)
      );
      root.addOrReplaceChild(
         "front_hump",
         CubeListBuilder.create().texOffs(74, 0).addBox(-3.5F, -9.0F, 3.0F, 7.0F, 7.0F, 3.0F),
         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "back_hump",
         CubeListBuilder.create().texOffs(74, 0).addBox(-3.5F, 0.0F, 3.0F, 7.0F, 7.0F, 3.0F),
         PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(29, 29).mirror().addBox(-2.09F, 0.0F, -2.09F, 4.0F, 14.0F, 4.0F),
         PartPose.offset(-3.9F, 10.0F, -5.9F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(29, 29).addBox(-1.91F, 0.0F, -2.09F, 4.0F, 14.0F, 4.0F), PartPose.offset(3.9F, 10.0F, -5.9F)
      );
      root.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(29, 29).mirror().addBox(-2.09F, 0.0F, -1.91F, 4.0F, 14.0F, 4.0F),
         PartPose.offset(-3.9F, 10.0F, 7.9F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(29, 29).addBox(-1.91F, 0.0F, -1.91F, 4.0F, 14.0F, 4.0F), PartPose.offset(3.9F, 10.0F, 7.9F)
      );
      PartDefinition saddleTop = root.addOrReplaceChild(
         "saddle_top", CubeListBuilder.create().texOffs(80, 41).addBox(-5.5F, -2.0F, -5.5F, 10.0F, 3.0F, 13.0F), PartPose.offset(0.5F, 0.0F, 0.0F)
      );
      saddleTop.addOrReplaceChild(
         "saddle_bottom", CubeListBuilder.create().texOffs(73, 18).addBox(-7.5F, 1.0F, -5.5F, 14.0F, 7.0F, 13.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "chest", CubeListBuilder.create().texOffs(33, 49).addBox(-7.75F, -5.0F, -2.25F, 16.0F, 8.0F, 7.0F), PartPose.offset(0.0F, 1.0F, 7.75F)
      );
      return LayerDefinition.create(mesh, 128, 64);
   }

   public Iterable<ModelPart> getCamelLegParts() {
      return List.of(this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg);
   }

   public Iterable<ModelPart> getCamelBodyParts() {
      return List.of(this.body, this.frontHump, this.backHump);
   }

   public Iterable<ModelPart> getCamelHeadParts() {
      return List.of(this.neckBottom);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      boolean local = entityIn.isControlledByLocalInstance();
      this.neckBottom.xRot = headPitch * (float) (Math.PI / 180.0) + Mth.cos(limbSwing * 0.6662F) * (local ? 0.05F : 0.08F) * limbSwingAmount;
      this.neckBottom.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.body.xRot = (float) (Math.PI / 2);
      float legSwing = 1.0F;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * legSwing * limbSwingAmount;
      this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * legSwing * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * legSwing * limbSwingAmount;
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * legSwing * limbSwingAmount;
   }

   public void renderToBuffer(
      PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha
   ) {
      if (this.young) {
         matrixStackIn.pushPose();
         matrixStackIn.scale(0.71428573F, 0.64935064F, 0.7936508F);
         matrixStackIn.translate(0.0, 1.3125, 0.22F);
         this.getCamelHeadParts().forEach(model -> model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
         matrixStackIn.popPose();
         matrixStackIn.pushPose();
         matrixStackIn.scale(0.625F, 0.45454544F, 0.45454544F);
         matrixStackIn.translate(0.0, 2.0625, 0.0);
         this.getCamelBodyParts().forEach(model -> model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
         matrixStackIn.popPose();
         matrixStackIn.pushPose();
         matrixStackIn.scale(0.45454544F, 0.41322312F, 0.45454544F);
         matrixStackIn.translate(0.0, 2.0625, 0.0);
         this.getCamelLegParts().forEach(model -> model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
         matrixStackIn.popPose();
      } else {
         this.getCamelLegParts().forEach(model -> model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
         this.getCamelBodyParts().forEach(model -> model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
         this.getCamelHeadParts().forEach(model -> model.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
         if (this.saddled) {
            this.saddleTop.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
         }

         if (this.chested) {
            this.chest.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
         }
      }
   }
}
