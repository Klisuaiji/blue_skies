package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.entities.passive.CrogreEntity;
import java.util.List;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class CrogreModel<T extends CrogreEntity> extends AgeableListModel<T> {
   protected final ModelPart head;
   protected final ModelPart rightEye;
   protected final ModelPart leftEye;
   protected final ModelPart body;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart rightHindLeg;
   protected final ModelPart rightFoot;
   protected final ModelPart leftHindLeg;
   protected final ModelPart leftFoot;

   public CrogreModel(ModelPart model) {
      super(true, 10.0F, 3.35F);
      this.head = model.getChild("head");
      this.rightEye = this.head.getChild("right_eye");
      this.leftEye = this.head.getChild("left_eye");
      this.body = model.getChild("body");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.rightFoot = this.rightHindLeg.getChild("right_foot");
      this.leftHindLeg = model.getChild("left_hind_leg");
      this.leftFoot = this.leftHindLeg.getChild("left_foot");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -4.0F, -6.95F, 12.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 19.0F, -10.25F)
      );
      head.addOrReplaceChild(
         "right_eye", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-4.0F, -6.0F, -7.45F, 3.0F, 2.0F, 3.0F), PartPose.offset(-2.0F, 0.0F, 4.5F)
      );
      head.addOrReplaceChild(
         "left_eye", CubeListBuilder.create().texOffs(0, 36).addBox(1.0F, -5.96F, -7.45F, 3.0F, 2.0F, 3.0F), PartPose.offset(2.0F, 0.0F, 4.5F)
      );
      root.addOrReplaceChild(
         "body",
         CubeListBuilder.create().texOffs(0, 43).addBox(-9.0F, -8.0F, -14.0F, 18.0F, 8.0F, 20.0F, size),
         PartPose.offsetAndRotation(0.0F, 24.0F, 3.0F, -0.0873F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_front_leg", CubeListBuilder.create().texOffs(0, 50).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 8.0F, 5.0F), PartPose.offset(9.5F, 18.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 8.0F, 5.0F), PartPose.offset(-9.5F, 18.0F, -7.5F)
      );
      PartDefinition rightHindLeg = root.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(0, 14).mirror().addBox(-3.5F, -4.4957F, -4.8497F, 5.0F, 7.0F, 8.0F),
         PartPose.offsetAndRotation(-8.5F, 19.0F, 7.0F, -0.1745F, 0.0F, 0.0F)
      );
      rightHindLeg.addOrReplaceChild(
         "right_foot",
         CubeListBuilder.create().texOffs(0, 29).mirror().addBox(-3.0F, -0.2651F, -11.8006F, 5.0F, 2.0F, 12.0F),
         PartPose.offsetAndRotation(-0.5F, 2.8F, 3.0F, 0.1745F, 0.0F, 0.0F)
      );
      PartDefinition leftHindLeg = root.addOrReplaceChild(
         "left_hind_leg",
         CubeListBuilder.create().texOffs(0, 14).addBox(-1.5F, -4.4957F, -4.8497F, 5.0F, 7.0F, 8.0F),
         PartPose.offsetAndRotation(8.5F, 19.0F, 7.0F, -0.1745F, 0.0F, 0.0F)
      );
      leftHindLeg.addOrReplaceChild(
         "left_foot",
         CubeListBuilder.create().texOffs(0, 29).addBox(-2.0F, -0.2651F, -11.8006F, 5.0F, 2.0F, 12.0F),
         PartPose.offsetAndRotation(0.5F, 2.8F, 3.0F, 0.1745F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 96, 96);
   }

   protected Iterable<ModelPart> headParts() {
      return List.of(this.head);
   }

   protected Iterable<ModelPart> bodyParts() {
      return List.of(this.body, this.rightFrontLeg, this.leftFrontLeg, this.leftHindLeg, this.rightHindLeg);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = ageInTicks - entityIn.tickCount;
      float jumpScale = entityIn.getJumpingAnimationScale(f);
      jumpScale *= jumpScale;
      float swimScale = entityIn.getSwimmingAnimationScale(f);
      swimScale *= swimScale;
      this.setRotationAngle(this.body, -0.0873F, 0.0F, 0.0F);
      this.setRotationAngle(this.rightHindLeg, -0.1745F, 0.0F, 0.0F);
      this.setRotationAngle(this.rightFoot, 0.1745F, 0.0F, 0.0F);
      this.setRotationAngle(this.leftHindLeg, -0.1745F, 0.0F, 0.0F);
      this.setRotationAngle(this.leftFoot, 0.1745F, 0.0F, 0.0F);
      this.setRotationAngle(this.leftFrontLeg, 0.0F, 0.0F, 0.0F);
      this.setRotationAngle(this.rightFrontLeg, 0.0F, 0.0F, 0.0F);
      this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      this.head.yRot = SkiesClientUtil.lockNum(netHeadYaw * (float) (Math.PI / 180.0), 0.45F);
      if (entityIn.isInWaterOrBubble()) {
         this.leftFrontLeg.zRot = 1.6F;
         this.rightFrontLeg.zRot = -1.6F;
         this.leftFrontLeg.xRot = -0.9F + 0.9F * swimScale;
         this.rightFrontLeg.xRot = -0.9F + 0.9F * swimScale;
         this.rightHindLeg.xRot = 1.0F + 0.9F * swimScale;
         this.leftHindLeg.xRot = 1.0F + 0.9F * swimScale;
      } else {
         this.rightHindLeg.xRot += 0.8F * jumpScale;
         this.leftHindLeg.xRot += 0.8F * jumpScale;
         this.leftFrontLeg.xRot += -(0.8F * jumpScale);
         this.rightFrontLeg.xRot += -(0.8F * jumpScale);
      }
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
