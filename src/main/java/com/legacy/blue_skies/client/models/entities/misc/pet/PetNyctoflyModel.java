package com.legacy.blue_skies.client.models.entities.misc.pet;

import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PetNyctoflyModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart head;
   protected final ModelPart rightEye;
   protected final ModelPart leftEye;
   protected final ModelPart body;
   protected final ModelPart butt;
   protected final ModelPart tail;
   protected final ModelPart backRightLeg;
   protected final ModelPart middleRightLeg;
   protected final ModelPart frontRightLeg;
   protected final ModelPart backLeftLeg;
   protected final ModelPart middleLeftLeg;
   protected final ModelPart frontLeftLeg;
   protected final ModelPart frontRightWing;
   protected final ModelPart backRightWing;
   protected final ModelPart frontLeftWing;
   protected final ModelPart backLeftWing;

   public PetNyctoflyModel(ModelPart model) {
      this.head = model.getChild("head");
      this.rightEye = this.head.getChild("right_eye");
      this.leftEye = this.head.getChild("left_eye");
      this.body = model.getChild("body");
      this.frontRightWing = model.getChild("front_right_wing");
      this.frontLeftWing = model.getChild("front_left_wing");
      this.backRightWing = model.getChild("back_right_wing");
      this.backLeftWing = model.getChild("back_left_wing");
      this.butt = model.getChild("butt");
      this.tail = this.butt.getChild("tail");
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
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -3.7F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 21.0F, -3.3F)
      );
      head.addOrReplaceChild(
         "right_eye", CubeListBuilder.create().texOffs(5, 28).mirror().addBox(-2.4F, -1.2F, -4.2F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "left_eye", CubeListBuilder.create().texOffs(5, 28).addBox(0.6F, -1.2F, -4.2F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, -5.5F, -3.0F, 5.0F, 5.0F, 5.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
      root.addOrReplaceChild(
         "front_right_wing",
         CubeListBuilder.create().texOffs(16, 21).mirror().addBox(-6.0F, -0.5F, -0.8F, 6.0F, 1.0F, 2.0F),
         PartPose.offset(-2.0F, 20.5F, -1.5F)
      );
      root.addOrReplaceChild(
         "front_left_wing", CubeListBuilder.create().texOffs(16, 21).addBox(0.0F, -0.5F, -0.8F, 6.0F, 1.0F, 2.0F), PartPose.offset(2.0F, 20.5F, -1.5F)
      );
      root.addOrReplaceChild(
         "back_right_wing",
         CubeListBuilder.create().texOffs(16, 21).mirror().addBox(-6.0F, -0.5F, -1.2F, 6.0F, 1.0F, 2.0F),
         PartPose.offset(-2.0F, 20.5F, 0.5F)
      );
      root.addOrReplaceChild(
         "back_left_wing", CubeListBuilder.create().texOffs(16, 21).addBox(0.0F, -0.5F, -1.2F, 6.0F, 1.0F, 2.0F), PartPose.offset(2.0F, 20.5F, 0.5F)
      );
      PartDefinition butt = root.addOrReplaceChild(
         "butt",
         CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 4.0F),
         PartPose.offsetAndRotation(0.0F, 21.0F, 2.0F, 0.0873F, 0.0F, 0.0F)
      );
      butt.addOrReplaceChild(
         "tail",
         CubeListBuilder.create().texOffs(16, 24).addBox(-1.0F, -1.0852F, 0.0189F, 2.0F, 2.0F, 6.0F),
         PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0873F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "front_right_leg", CubeListBuilder.create().texOffs(0, 27).mirror().addBox(0.0F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offset(-2.0F, 22.0F, -2.0F)
      );
      root.addOrReplaceChild(
         "middle_right_leg",
         CubeListBuilder.create().texOffs(0, 27).mirror().addBox(0.0F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F),
         PartPose.offset(-2.0F, 22.0F, -0.5F)
      );
      root.addOrReplaceChild(
         "back_right_leg", CubeListBuilder.create().texOffs(0, 27).mirror().addBox(0.0F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offset(-2.0F, 22.0F, 1.0F)
      );
      root.addOrReplaceChild(
         "front_left_leg", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offset(2.0F, 22.0F, -2.0F)
      );
      root.addOrReplaceChild(
         "middle_left_leg", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offset(2.0F, 22.0F, -0.5F)
      );
      root.addOrReplaceChild(
         "back_left_leg", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -0.3F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offset(2.0F, 22.0F, 1.0F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }

   public Iterable<ModelPart> parts() {
      return List.of(
         this.frontRightWing,
         this.middleLeftLeg,
         this.backRightLeg,
         this.body,
         this.frontLeftWing,
         this.middleRightLeg,
         this.frontRightLeg,
         this.backLeftWing,
         this.backRightWing,
         this.butt,
         this.head,
         this.backLeftLeg,
         this.frontLeftLeg
      );
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.setRotationAngle(this.frontLeftWing, 0.0F, 0.4F, -0.75F);
      this.setRotationAngle(this.backLeftWing, 0.0F, -0.4F, -0.75F);
      this.setRotationAngle(this.frontRightWing, 0.0F, -0.4F, 0.75F);
      this.setRotationAngle(this.backRightWing, 0.0F, 0.4F, 0.75F);
      this.setRotationAngle(this.frontLeftLeg, -0.5F, 0.7F, 0.7F);
      this.setRotationAngle(this.middleLeftLeg, -0.0F, 0.0F, 0.7F);
      this.setRotationAngle(this.backLeftLeg, 0.5F, -0.7F, 0.7F);
      this.setRotationAngle(this.frontRightLeg, -0.5F, -0.7F, -0.7F);
      this.setRotationAngle(this.middleRightLeg, 0.0F, 0.0F, -0.7F);
      this.setRotationAngle(this.backRightLeg, 0.5F, 0.7F, -0.7F);
      float newAge = ageInTicks % 10000.0F;
      this.butt.xRot = Mth.cos(newAge * 0.5F) * 0.05F + 0.1F;
      this.tail.xRot = Mth.cos(newAge * 3.0F) * 0.1F + 0.2F;
      this.head.xRot = headPitch / (float) (180.0 / Math.PI);
      this.head.yRot = netHeadYaw / (float) (180.0 / Math.PI);
      float wingRot = Mth.cos(newAge * 2.0F) * 1.0F;
      this.frontLeftWing.zRot = wingRot;
      this.backLeftWing.zRot = -wingRot;
      this.frontRightWing.zRot = -wingRot;
      this.backRightWing.zRot = wingRot;
      this.frontLeftLeg.zRot = Mth.cos(newAge * 0.5F) * 0.2F - 0.7F;
      this.middleLeftLeg.zRot = Mth.cos(newAge * 0.5F + 2.0F) * 0.2F - 0.7F;
      this.backLeftLeg.zRot = Mth.cos(newAge * 0.5F + 4.0F) * 0.2F - 0.7F;
      this.frontRightLeg.zRot = -(Mth.cos(newAge * 0.5F) * 0.2F) + 0.7F;
      this.middleRightLeg.zRot = -(Mth.cos(newAge * 0.5F + 2.0F) * 0.2F) + 0.7F;
      this.backRightLeg.zRot = -(Mth.cos(newAge * 0.5F + 4.0F) * 0.2F) + 0.7F;
   }
}
