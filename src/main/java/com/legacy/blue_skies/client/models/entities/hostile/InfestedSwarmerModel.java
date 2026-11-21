package com.legacy.blue_skies.client.models.entities.hostile;

import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class InfestedSwarmerModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart head;
   protected final ModelPart rightAntenna;
   protected final ModelPart leftAntenna;
   protected final ModelPart rightPinser;
   protected final ModelPart leftPinser;
   protected final ModelPart body;
   protected final ModelPart bodyBack;
   protected final ModelPart butt;
   protected final ModelPart topSpikeFront;
   protected final ModelPart topSpikeRight;
   protected final ModelPart topSpikeLeft;
   protected final ModelPart topSpikeMiddle;
   protected final ModelPart topSpikeMiddle2;
   protected final ModelPart topSpikeBack;
   protected final ModelPart backRightLeg;
   protected final ModelPart backLeftLeg;
   protected final ModelPart middleRightLeg;
   protected final ModelPart middleLeftLeg;
   protected final ModelPart frontRightLeg;
   protected final ModelPart frontLeftLeg;

   public InfestedSwarmerModel(ModelPart model) {
      this.head = model.getChild("head");
      this.rightPinser = this.head.getChild("right_pinser");
      this.leftPinser = this.head.getChild("left_pinser");
      this.rightAntenna = this.head.getChild("right_antenna");
      this.leftAntenna = this.head.getChild("left_antenna");
      this.body = model.getChild("body");
      this.bodyBack = this.body.getChild("body_back");
      this.butt = this.bodyBack.getChild("butt");
      this.topSpikeFront = this.butt.getChild("top_spike_front");
      this.topSpikeBack = this.butt.getChild("top_spike_back");
      this.topSpikeRight = this.butt.getChild("top_spike_right");
      this.topSpikeLeft = this.butt.getChild("top_spike_left");
      this.topSpikeMiddle = this.butt.getChild("top_spike_middle");
      this.topSpikeMiddle2 = this.topSpikeMiddle.getChild("top_spike_middle_2");
      this.frontRightLeg = model.getChild("front_right_leg");
      this.middleRightLeg = model.getChild("middle_right_leg");
      this.backRightLeg = model.getChild("back_right_leg");
      this.frontLeftLeg = model.getChild("front_left_leg");
      this.middleLeftLeg = model.getChild("middle_left_leg");
      this.backLeftLeg = model.getChild("back_left_leg");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -3.5F, -8.5F, 8.0F, 7.0F, 10.0F), PartPose.offset(0.0F, 17.0F, -4.5F)
      );
      head.addOrReplaceChild(
         "right_pinser", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 4.0F), PartPose.offset(-1.5F, 1.5F, -8.5F)
      );
      head.addOrReplaceChild(
         "left_pinser", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 4.0F), PartPose.offset(1.5F, 1.5F, -8.5F)
      );
      head.addOrReplaceChild(
         "right_antenna",
         CubeListBuilder.create().texOffs(24, 70).mirror().addBox(-0.05F, -9.0F, -13.5F, 0.0F, 9.0F, 17.0F, size),
         PartPose.offset(-2.5F, -3.5F, -5.0F)
      );
      head.addOrReplaceChild(
         "left_antenna", CubeListBuilder.create().texOffs(24, 70).addBox(0.05F, -9.0F, -13.5F, 0.0F, 9.0F, 17.0F, size), PartPose.offset(2.5F, -3.5F, -5.0F)
      );
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 17).addBox(-3.5F, -2.0F, -4.25F, 7.0F, 7.0F, 8.0F), PartPose.offset(0.0F, 15.5F, 0.25F)
      );
      PartDefinition bodyBack = body.addOrReplaceChild(
         "body_back", CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, -2.75F, 0.0F, 5.0F, 5.0F, 5.0F), PartPose.offset(0.0F, 1.75F, 3.75F)
      );
      PartDefinition butt = bodyBack.addOrReplaceChild(
         "butt", CubeListBuilder.create().texOffs(0, 42).addBox(-5.5F, -4.0F, 0.0F, 11.0F, 8.0F, 13.0F), PartPose.offset(0.0F, -0.25F, 4.0F)
      );
      butt.addOrReplaceChild(
         "top_spike_front", CubeListBuilder.create().texOffs(0, 64).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 6.0F, 0.0F, size), PartPose.offset(0.0F, -4.0F, 2.5F)
      );
      butt.addOrReplaceChild(
         "top_spike_back", CubeListBuilder.create().texOffs(0, 78).addBox(-4.5F, -6.0F, 0.0F, 9.0F, 6.0F, 0.0F, size), PartPose.offset(0.0F, -4.0F, 10.5F)
      );
      butt.addOrReplaceChild(
         "top_spike_right", CubeListBuilder.create().texOffs(0, 60).addBox(0.0F, -6.0F, -5.5F, 0.0F, 6.0F, 11.0F, size), PartPose.offset(-3.0F, -4.0F, 6.5F)
      );
      butt.addOrReplaceChild(
         "top_spike_left", CubeListBuilder.create().texOffs(0, 74).addBox(0.0F, -6.0F, -5.5F, 0.0F, 6.0F, 11.0F, size), PartPose.offset(3.0F, -4.0F, 6.5008F)
      );
      PartDefinition middleSpike = butt.addOrReplaceChild(
         "top_spike_middle", CubeListBuilder.create().texOffs(23, 69).addBox(0.0F, -5.5F, -7.0F, 0.0F, 9.0F, 5.0F, size), PartPose.offset(0.0F, -7.5F, 11.0F)
      );
      middleSpike.addOrReplaceChild(
         "top_spike_middle_2", CubeListBuilder.create().texOffs(23, 64).addBox(-2.5F, -5.5F, -4.5F, 5.0F, 9.0F, 0.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "front_right_leg",
         CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-11.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F),
         PartPose.offset(-3.5F, 17.0F, -1.0F)
      );
      root.addOrReplaceChild(
         "middle_right_leg",
         CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-11.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F),
         PartPose.offset(-3.5F, 17.0F, 1.0F)
      );
      root.addOrReplaceChild(
         "back_right_leg",
         CubeListBuilder.create().texOffs(26, 0).mirror().addBox(-11.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F),
         PartPose.offset(-3.5F, 17.0F, 3.0F)
      );
      root.addOrReplaceChild(
         "front_left_leg", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F), PartPose.offset(3.5F, 17.0F, -1.0F)
      );
      root.addOrReplaceChild(
         "middle_left_leg", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F), PartPose.offset(3.5F, 17.0F, 1.0F)
      );
      root.addOrReplaceChild(
         "back_left_leg", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F), PartPose.offset(3.5F, 17.0F, 3.0F)
      );
      return LayerDefinition.create(mesh, 80, 96);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.middleLeftLeg, this.backRightLeg, this.body, this.middleRightLeg, this.frontRightLeg, this.head, this.backLeftLeg, this.frontLeftLeg);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.setRotationAngle(this.head, 0.2618F, 0.0F, 0.0F);
      this.setRotationAngle(this.leftAntenna, 0.0F, -0.4363F, 0.0F);
      this.setRotationAngle(this.rightAntenna, 0.0F, 0.4363F, 0.0F);
      this.head.xRot += headPitch / (float) (180.0 / Math.PI);
      this.head.yRot += netHeadYaw / (float) (180.0 / Math.PI);
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
      float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 2.41661F) * 0.4F) * limbSwingAmount + 0.8F;
      float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount - 0.1F;
      float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount - 0.1F;
      float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + 4.08407F) * 0.4F) * limbSwingAmount + 0.2F;
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
      float antennaAnim = Mth.cos(ageInTicks * 0.2F) * 0.05F;
      this.leftAntenna.zRot = antennaAnim;
      this.rightAntenna.zRot = -antennaAnim;
      this.leftAntenna.yRot += antennaAnim;
      this.rightAntenna.yRot += -antennaAnim;
      this.leftPinser.yRot = Mth.cos(ageInTicks * 0.1F) * 0.1F;
      this.rightPinser.yRot = -(Mth.cos(ageInTicks * 0.1F) * 0.1F);
      float buttAnim = Mth.cos(ageInTicks * 0.1F) * 0.12F * 0.5F;
      this.bodyBack.xRot = buttAnim * 0.9F;
      this.butt.xRot = buttAnim;
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
