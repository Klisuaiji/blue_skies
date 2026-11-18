package com.legacy.blue_skies.client.models.entities.hostile;

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

public class NyctoflyModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart tail;
   protected final ModelPart frontRightLeg;
   protected final ModelPart head;
   protected final ModelPart frontLeftWing;
   protected final ModelPart backLeftLeg;
   protected final ModelPart backRightWing;
   protected final ModelPart body;
   protected final ModelPart butt;
   protected final ModelPart backRightLeg;
   protected final ModelPart middleRightLeg;
   protected final ModelPart frontLeftLeg;
   protected final ModelPart middleLeftLeg;
   protected final ModelPart frontRightWing;
   protected final ModelPart backLeftWing;
   protected final ModelPart rightEye;
   protected final ModelPart leftEye;

   public NyctoflyModel(ModelPart model) {
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
         "head", CubeListBuilder.create().texOffs(32, 4).addBox(-4.0F, -3.75F, -8.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 17.0F, -3.0F)
      );
      head.addOrReplaceChild(
         "right_eye", CubeListBuilder.create().texOffs(26, 0).addBox(-1.45F, -1.6F, -1.4F, 3.0F, 3.0F, 3.0F), PartPose.offset(-3.25F, -0.25F, -8.0F)
      );
      head.addOrReplaceChild(
         "left_eye", CubeListBuilder.create().texOffs(26, 0).addBox(-1.55F, -1.6F, -1.4F, 3.0F, 3.0F, 3.0F), PartPose.offset(3.25F, -0.25F, -8.0F)
      );
      root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 22).addBox(-3.0F, -0.75F, -3.0F, 9.0F, 9.0F, 9.0F), PartPose.offset(-1.5F, 13.5F, 0.0F)
      );
      root.addOrReplaceChild(
         "front_right_wing",
         CubeListBuilder.create().texOffs(0, 57).addBox(-13.5874F, 0.6463F, -1.5972F, 16.0F, 1.0F, 5.0F),
         PartPose.offset(-4.0F, 15.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "front_left_wing",
         CubeListBuilder.create().texOffs(0, 57).mirror().addBox(-2.4126F, 0.6463F, -1.5972F, 16.0F, 1.0F, 5.0F),
         PartPose.offset(4.0F, 15.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "back_right_wing",
         CubeListBuilder.create().texOffs(0, 57).addBox(-13.5874F, 0.6463F, -0.4028F, 16.0F, 1.0F, 5.0F),
         PartPose.offset(-4.0F, 15.0F, 1.0F)
      );
      root.addOrReplaceChild(
         "back_left_wing",
         CubeListBuilder.create().texOffs(0, 57).mirror().addBox(-2.4126F, 0.6463F, -0.4028F, 16.0F, 1.0F, 5.0F),
         PartPose.offset(4.0F, 15.0F, 1.0F)
      );
      PartDefinition butt = root.addOrReplaceChild(
         "butt",
         CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.4418F, 0.1597F, 8.0F, 7.0F, 7.0F),
         PartPose.offsetAndRotation(0.0F, 17.25F, 5.6F, 0.1F, 0.0F, 0.0F)
      );
      butt.addOrReplaceChild(
         "tail",
         CubeListBuilder.create().texOffs(22, 27).addBox(-2.5F, -2.75F, -0.25F, 5.0F, 5.0F, 16.0F),
         PartPose.offsetAndRotation(0.0F, 0.25F, 6.9F, 0.17F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "front_right_leg", CubeListBuilder.create().texOffs(0, 50).addBox(-7.0F, -0.7F, -1.0F, 7.0F, 2.0F, 2.0F), PartPose.offset(-4.0F, 19.2F, -1.5F)
      );
      root.addOrReplaceChild(
         "middle_right_leg", CubeListBuilder.create().texOffs(0, 50).addBox(-7.0F, -0.7F, -1.0F, 7.0F, 2.0F, 2.0F), PartPose.offset(-4.0F, 19.2F, 1.5F)
      );
      root.addOrReplaceChild(
         "back_right_leg", CubeListBuilder.create().texOffs(0, 50).addBox(-7.0F, -0.7F, -1.0F, 7.0F, 2.0F, 2.0F), PartPose.offset(-4.0F, 19.2F, 4.5F)
      );
      root.addOrReplaceChild(
         "front_left_leg", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-1.0F, -0.7F, -1.0F, 7.0F, 2.0F, 2.0F), PartPose.offset(4.0F, 19.2F, -1.5F)
      );
      root.addOrReplaceChild(
         "middle_left_leg", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-1.0F, -0.7F, -1.0F, 7.0F, 2.0F, 2.0F), PartPose.offset(4.0F, 19.2F, 1.5F)
      );
      root.addOrReplaceChild(
         "back_left_leg", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-1.0F, -0.7F, -1.0F, 7.0F, 2.0F, 2.0F), PartPose.offset(4.0F, 19.2F, 4.5F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
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
      this.setRotationAngle(this.frontLeftLeg, 0.0F, 0.7F, 0.7F);
      this.setRotationAngle(this.middleLeftLeg, 0.0F, 0.0F, 0.7F);
      this.setRotationAngle(this.backLeftLeg, 0.0F, -0.7F, 0.7F);
      this.setRotationAngle(this.frontRightLeg, 0.0F, -0.7F, -0.7F);
      this.setRotationAngle(this.middleRightLeg, 0.0F, 0.0F, -0.7F);
      this.setRotationAngle(this.backRightLeg, 0.0F, 0.7F, -0.7F);
      this.butt.xRot = Mth.cos(ageInTicks * 0.5F) * 0.05F + 0.1F;
      this.tail.xRot = Mth.cos(ageInTicks * 3.0F) * 0.1F + 0.2F;
      this.head.xRot = headPitch / (float) (180.0 / Math.PI);
      this.head.yRot = netHeadYaw / (float) (180.0 / Math.PI);
      float wingRot = Mth.cos(ageInTicks * 1.5F) * 1.0F;
      this.frontLeftWing.zRot = wingRot;
      this.backLeftWing.zRot = -wingRot;
      this.frontRightWing.zRot = -wingRot;
      this.backRightWing.zRot = wingRot;
      this.frontLeftLeg.zRot = Mth.cos(ageInTicks * 0.5F) * 0.2F + 0.5F;
      this.middleLeftLeg.zRot = Mth.cos(ageInTicks * 0.5F + 2.0F) * 0.2F + 0.5F;
      this.backLeftLeg.zRot = Mth.cos(ageInTicks * 0.5F + 4.0F) * 0.2F + 0.5F;
      this.frontRightLeg.zRot = -(Mth.cos(ageInTicks * 0.5F) * 0.2F) - 0.5F;
      this.middleRightLeg.zRot = -(Mth.cos(ageInTicks * 0.5F + 2.0F) * 0.2F) - 0.5F;
      this.backRightLeg.zRot = -(Mth.cos(ageInTicks * 0.5F + 4.0F) * 0.2F) - 0.5F;
   }
}
