package com.legacy.blue_skies.client.models.entities.hostile;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class VenomSpiderModel<T extends Entity> extends HierarchicalModel<T> {
   private final ModelPart root;
   protected final ModelPart head;
   protected final ModelPart rightPinser;
   protected final ModelPart leftPinser;
   protected final ModelPart leftPinserEnd;
   protected final ModelPart rightPinserEnd;
   protected final ModelPart body;
   protected final ModelPart butt;
   protected final ModelPart rightHindLeg;
   protected final ModelPart leftHindLeg;
   protected final ModelPart rightMiddleHindLeg;
   protected final ModelPart leftMiddleHindLeg;
   protected final ModelPart rightMiddleFrontLeg;
   protected final ModelPart leftMiddleFrontLeg;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;

   public VenomSpiderModel(ModelPart model) {
      this.root = model;
      this.head = model.getChild("head");
      this.rightPinser = this.head.getChild("right_pinser");
      this.rightPinserEnd = this.head.getChild("right_pinser_end");
      this.leftPinser = this.head.getChild("left_pinser");
      this.leftPinserEnd = this.head.getChild("left_pinser_end");
      this.body = model.getChild("body");
      this.butt = model.getChild("butt");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
      this.rightMiddleHindLeg = model.getChild("right_middle_hind_leg");
      this.leftMiddleHindLeg = model.getChild("left_middle_hind_leg");
      this.rightMiddleFrontLeg = model.getChild("right_middle_front_leg");
      this.leftMiddleFrontLeg = model.getChild("left_middle_front_leg");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(32, 4).addBox(-3.5F, -3.5F, -7.0F, 7.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 15.0F, -3.0F)
      );
      head.addOrReplaceChild("right_pinser", CubeListBuilder.create().texOffs(0, 13).mirror().addBox(-2.8F, 2.0F, -10.0F, 1.0F, 1.0F, 3.0F), PartPose.ZERO);
      head.addOrReplaceChild("right_pinser_end", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-1.8F, 2.0F, -10.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
      head.addOrReplaceChild("left_pinser", CubeListBuilder.create().texOffs(0, 13).addBox(1.8F, 2.0F, -10.0F, 1.0F, 1.0F, 3.0F), PartPose.ZERO);
      head.addOrReplaceChild("left_pinser_end", CubeListBuilder.create().texOffs(0, 18).addBox(0.8F, 2.0F, -10.0F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
      root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 15.0F, 0.0F));
      root.addOrReplaceChild(
         "butt", CubeListBuilder.create().texOffs(0, 19).addBox(-6.0F, -5.0F, -1.0F, 12.0F, 10.0F, 14.0F), PartPose.offset(0.0F, 15.0F, 4.0F)
      );
      CubeListBuilder rightLeg = CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F);
      CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F);
      root.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-4.0F, 15.0F, 2.0F));
      root.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(4.0F, 15.0F, 2.0F));
      root.addOrReplaceChild("right_middle_hind_leg", rightLeg, PartPose.offset(-4.0F, 15.0F, 1.0F));
      root.addOrReplaceChild("left_middle_hind_leg", leftLeg, PartPose.offset(4.0F, 15.0F, 1.0F));
      root.addOrReplaceChild("right_middle_front_leg", rightLeg, PartPose.offset(-4.0F, 15.0F, 0.0F));
      root.addOrReplaceChild("left_middle_front_leg", leftLeg, PartPose.offset(4.0F, 15.0F, 0.0F));
      root.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-4.0F, 15.0F, -1.0F));
      root.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(4.0F, 15.0F, -1.0F));
      return LayerDefinition.create(mesh, 64, 64);
   }

   public ModelPart root() {
      return this.root;
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      float f = (float) (Math.PI / 4);
      this.rightHindLeg.zRot = -f;
      this.leftHindLeg.zRot = f;
      this.rightMiddleHindLeg.zRot = -0.58119464F;
      this.leftMiddleHindLeg.zRot = 0.58119464F;
      this.rightMiddleFrontLeg.zRot = -0.58119464F;
      this.leftMiddleFrontLeg.zRot = 0.58119464F;
      this.rightFrontLeg.zRot = -f;
      this.leftFrontLeg.zRot = f;
      float f2 = (float) (Math.PI / 8);
      this.rightHindLeg.yRot = f;
      this.leftHindLeg.yRot = -f;
      this.rightMiddleHindLeg.yRot = f2;
      this.leftMiddleHindLeg.yRot = -f2;
      this.rightMiddleFrontLeg.yRot = -f2;
      this.leftMiddleFrontLeg.yRot = f2;
      this.rightFrontLeg.yRot = -f;
      this.leftFrontLeg.yRot = f;
      float f3 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
      float f4 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
      float f5 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount;
      float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
      float f7 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
      float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
      float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount;
      float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
      this.rightHindLeg.yRot += f3;
      this.leftHindLeg.yRot += -f3;
      this.rightMiddleHindLeg.yRot += f4;
      this.leftMiddleHindLeg.yRot += -f4;
      this.rightMiddleFrontLeg.yRot += f5;
      this.leftMiddleFrontLeg.yRot += -f5;
      this.rightFrontLeg.yRot += f6;
      this.leftFrontLeg.yRot += -f6;
      this.rightHindLeg.zRot += f7;
      this.leftHindLeg.zRot += -f7;
      this.rightMiddleHindLeg.zRot += f8;
      this.leftMiddleHindLeg.zRot += -f8;
      this.rightMiddleFrontLeg.zRot += f9;
      this.leftMiddleFrontLeg.zRot += -f9;
      this.rightFrontLeg.zRot += f10;
      this.leftFrontLeg.zRot += -f10;
      this.butt.xRot = Mth.cos(ageInTicks * 0.2F) * 0.12F;
      float animAmount = Mth.cos(ageInTicks * 0.1F) * 0.02F;
      this.rightPinser.yRot = animAmount;
      this.rightPinserEnd.yRot = animAmount;
      this.leftPinser.yRot = -animAmount;
      this.leftPinserEnd.yRot = -animAmount;
   }
}
