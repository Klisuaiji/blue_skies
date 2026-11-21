package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.entities.hostile.EmberbackEntity;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class EmberbackModel<T extends EmberbackEntity> extends ListModel<T> {
   public ModelPart head;
   public ModelPart body;
   public ModelPart leftShell;
   public ModelPart rightShell;
   public ModelPart frontLeftLeg;
   public ModelPart middleLeftLeg;
   public ModelPart backLeftLeg;
   public ModelPart frontRightLeg;
   public ModelPart middleRightLeg;
   public ModelPart backRightLeg;
   public ModelPart leftWing;
   public ModelPart rightWing;
   public ModelPart rightEye;
   public ModelPart leftEye;
   public ModelPart rightAntennae;
   public ModelPart leftAntennae;

   public EmberbackModel(ModelPart model) {
      this.head = model.getChild("head");
      this.rightEye = this.head.getChild("right_eye");
      this.leftEye = this.head.getChild("left_eye");
      this.rightAntennae = this.head.getChild("right_antennae");
      this.leftAntennae = this.head.getChild("left_antennae");
      this.body = model.getChild("body");
      this.rightWing = model.getChild("right_wing");
      this.leftWing = model.getChild("left_wing");
      this.rightShell = model.getChild("right_shell");
      this.leftShell = model.getChild("left_shell");
      this.frontRightLeg = model.getChild("front_right_leg");
      this.middleRightLeg = model.getChild("middle_right_leg");
      this.backRightLeg = model.getChild("back_right_leg");
      this.frontLeftLeg = model.getChild("front_left_leg");
      this.middleLeftLeg = model.getChild("middle_left_leg");
      this.backLeftLeg = model.getChild("back_left_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 18.0F, -8.0F)
      );
      head.addOrReplaceChild(
         "right_eye", CubeListBuilder.create().texOffs(0, 13).addBox(1.0F, -2.0F, -4.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "left_eye", CubeListBuilder.create().texOffs(0, 13).mirror().addBox(-4.0F, -2.0F, -4.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "right_antennae", CubeListBuilder.create().texOffs(0, 19).addBox(2.0F, -7.0F, -4.0F, 0.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "left_antennae", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-2.0F, -7.0F, -4.0F, 0.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 41).addBox(-6.0F, -4.0F, 0.0F, 12.0F, 8.0F, 15.0F), PartPose.offset(0.0F, 18.0F, -7.0F)
      );
      root.addOrReplaceChild(
         "right_wing", CubeListBuilder.create().texOffs(12, 0).mirror().addBox(-16.0F, 0.0F, -5.0F, 16.0F, 0.0F, 12.0F), PartPose.offset(-6.0F, 14.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_wing", CubeListBuilder.create().texOffs(12, 0).addBox(0.0F, 0.0F, -6.0F, 16.0F, 0.0F, 12.0F), PartPose.offset(6.0F, 14.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_shell", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-7.0F, -5.0F, 0.0F, 7.0F, 6.0F, 17.0F), PartPose.offset(0.0F, 18.0F, -8.0F)
      );
      root.addOrReplaceChild(
         "left_shell", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -5.0F, 0.0F, 7.0F, 6.0F, 17.0F), PartPose.offset(0.0F, 18.0F, -8.0F)
      );
      root.addOrReplaceChild(
         "front_right_leg",
         CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-5.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F),
         PartPose.offset(-5.0F, 21.0F, -4.0F)
      );
      root.addOrReplaceChild(
         "middle_right_leg",
         CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-5.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F),
         PartPose.offset(-5.0F, 21.0F, 0.5F)
      );
      root.addOrReplaceChild(
         "back_right_leg", CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-5.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F), PartPose.offset(-5.0F, 21.0F, 5.0F)
      );
      root.addOrReplaceChild(
         "front_left_leg", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F), PartPose.offset(5.0F, 21.0F, -4.0F)
      );
      root.addOrReplaceChild(
         "middle_left_leg", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F), PartPose.offset(5.0F, 21.0F, 0.5F)
      );
      root.addOrReplaceChild(
         "back_left_leg", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F), PartPose.offset(5.0F, 21.0F, 5.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(
         this.head,
         this.body,
         this.middleLeftLeg,
         this.middleRightLeg,
         this.backLeftLeg,
         this.frontLeftLeg,
         this.backRightLeg,
         this.frontRightLeg,
         this.rightShell,
         this.leftShell,
         this.rightWing,
         this.leftWing
      );
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.head.xRot = headPitch / (float) (180.0 / Math.PI);
      this.head.yRot = netHeadYaw / (float) (180.0 / Math.PI);
      this.leftWing.zRot = 0.0F;
      this.rightWing.zRot = 0.0F;
      this.rightWing.visible = entityIn.isFlying();
      this.leftWing.visible = entityIn.isFlying();
      this.setRotateAngle(this.leftAntennae, 0.0F, 0.0F, -0.12F);
      this.setRotateAngle(this.rightAntennae, 0.0F, 0.0F, 0.12F);
      this.leftAntennae.zRot = this.leftAntennae.zRot + Mth.cos(ageInTicks * 0.2F) * 0.05F;
      this.rightAntennae.zRot = this.rightAntennae.zRot + -(Mth.cos(ageInTicks * 0.2F) * 0.05F);
      if (!entityIn.isFlying()) {
         this.middleRightLeg.zRot = (float) (-Math.PI / 4);
         this.middleRightLeg.yRot = (float) (-Math.PI / 4);
         this.middleLeftLeg.zRot = (float) (Math.PI / 4);
         this.middleLeftLeg.yRot = (float) (Math.PI / 4);
         this.backRightLeg.zRot = -0.58119464F;
         this.backLeftLeg.zRot = 0.58119464F;
         this.frontRightLeg.zRot = -0.58119464F;
         this.frontLeftLeg.zRot = 0.58119464F;
         this.backRightLeg.yRot = (float) (Math.PI / 8);
         this.backLeftLeg.yRot = (float) (-Math.PI / 8);
         this.frontRightLeg.yRot = (float) (-Math.PI / 8);
         this.frontLeftLeg.yRot = (float) (Math.PI / 8);
         float f4 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
         float f5 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount;
         float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI * 2.0 / 3.0)) * 0.4F) * limbSwingAmount + 0.8F;
         float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
         float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount;
         float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount + 0.2F;
         this.backRightLeg.yRot += f4;
         this.backRightLeg.zRot += f8;
         this.frontRightLeg.yRot += f5;
         this.frontRightLeg.zRot += f9;
         this.middleRightLeg.yRot += f6;
         this.middleRightLeg.zRot += f10;
         this.backLeftLeg.yRot += -f4;
         this.backLeftLeg.zRot += -f8;
         this.frontLeftLeg.yRot += -f5;
         this.frontLeftLeg.zRot += -f9;
         this.middleLeftLeg.yRot += -f6;
         this.middleLeftLeg.zRot += -f10;
         if (entityIn.attackTimer > 0) {
            this.setRotateAngle(this.leftShell, 0.3F, 0.5F, -0.1F);
            this.setRotateAngle(this.rightShell, 0.3F, -0.5F, 0.1F);
            this.leftShell.setPos(0.0F, 18.0F, -5.0F);
            this.rightShell.setPos(0.0F, 18.0F, -5.0F);
         } else {
            this.setRotateAngle(this.leftShell, 0.0F, 0.0F, 0.0F);
            this.setRotateAngle(this.rightShell, 0.0F, 0.0F, 0.0F);
            this.leftShell.setPos(0.0F, 18.0F, -8.0F);
            this.rightShell.setPos(0.0F, 18.0F, -8.0F);
         }
      } else {
         this.setRotateAngle(this.middleLeftLeg, -0.0F, 0.0F, 0.6F);
         this.setRotateAngle(this.backRightLeg, 0.0F, 0.4F, -0.6F);
         this.setRotateAngle(this.backLeftLeg, 0.0F, -0.4F, 0.6F);
         this.setRotateAngle(this.middleRightLeg, -0.0F, 0.0F, -0.6F);
         this.setRotateAngle(this.frontRightLeg, 0.0F, -0.4F, -0.6F);
         this.setRotateAngle(this.frontLeftLeg, 0.0F, 0.4F, 0.6F);
         this.setRotateAngle(this.leftShell, 0.3F, 0.5F, -0.1F);
         this.setRotateAngle(this.rightShell, 0.3F, -0.5F, 0.1F);
         this.leftShell.setPos(0.0F, 18.0F, -5.0F);
         this.rightShell.setPos(0.0F, 18.0F, -5.0F);
         float zShellRot = Mth.cos(ageInTicks * 0.1F) * 0.05F;
         float xShellRot = Mth.cos(ageInTicks * 0.1F) * 0.01F;
         this.leftShell.zRot += zShellRot;
         this.rightShell.zRot += -zShellRot;
         this.leftShell.xRot += -xShellRot;
         this.rightShell.xRot += -xShellRot;
         float wingRot = Mth.cos(ageInTicks * 2.0F) * 0.8F + zShellRot * 2.5F;
         this.leftWing.zRot = wingRot;
         this.rightWing.zRot = -wingRot;
         this.frontLeftLeg.zRot = Mth.cos(ageInTicks * 0.5F) * 0.2F + 0.6F;
         this.middleLeftLeg.zRot = Mth.cos(ageInTicks * 0.5F + 2.0F) * 0.2F + 0.6F;
         this.backLeftLeg.zRot = Mth.cos(ageInTicks * 0.5F + 4.0F) * 0.2F + 0.6F;
         this.frontRightLeg.zRot = -(Mth.cos(ageInTicks * 0.5F) * 0.2F) - 0.6F;
         this.middleRightLeg.zRot = -(Mth.cos(ageInTicks * 0.5F + 2.0F) * 0.2F) - 0.6F;
         this.backRightLeg.zRot = -(Mth.cos(ageInTicks * 0.5F + 4.0F) * 0.2F) - 0.6F;
      }
   }

   public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
      ModelRenderer.xRot = x;
      ModelRenderer.yRot = y;
      ModelRenderer.zRot = z;
   }
}
