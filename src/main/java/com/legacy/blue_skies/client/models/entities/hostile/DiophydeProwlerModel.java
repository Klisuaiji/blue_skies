package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.entities.hostile.DiophydeProwlerEntity;
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

public class DiophydeProwlerModel<T extends DiophydeProwlerEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart scales;
   protected final ModelPart tail;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart rightHindLeg;
   protected final ModelPart leftHindLeg;
   protected final ModelPart head;
   protected final ModelPart snout;
   protected final ModelPart rightFang;
   protected final ModelPart leftFang;
   protected final ModelPart rightEar;
   protected final ModelPart leftEar;
   protected final ModelPart rightFur;
   protected final ModelPart leftFur;
   protected final ModelPart topFur;

   public DiophydeProwlerModel(ModelPart model) {
      this.head = model.getChild("head");
      this.rightFur = this.head.getChild("right_fur");
      this.leftFur = this.head.getChild("left_fur");
      this.topFur = this.head.getChild("top_fur");
      this.rightEar = this.head.getChild("right_ear");
      this.leftEar = this.head.getChild("left_ear");
      this.snout = this.head.getChild("snout");
      this.rightFang = this.snout.getChild("right_fang");
      this.leftFang = this.snout.getChild("left_fang");
      this.body = model.getChild("body");
      this.scales = this.body.getChild("scales");
      this.tail = this.body.getChild("tail");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -4.0F, -7.5F, 11.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 4.0F, -15.5F)
      );
      head.addOrReplaceChild(
         "right_fur",
         CubeListBuilder.create().texOffs(39, 6).mirror().addBox(-4.0F, -4.0F, 0.0F, 4.0F, 8.0F, 0.0F),
         PartPose.offsetAndRotation(-5.5F, 0.0F, -7.5F, 0.0F, 0.5236F, 0.0F)
      );
      head.addOrReplaceChild(
         "left_fur",
         CubeListBuilder.create().texOffs(39, 6).addBox(0.0F, -4.0F, 0.0F, 4.0F, 8.0F, 0.0F),
         PartPose.offsetAndRotation(5.5F, 0.0F, -7.5F, 0.0F, -0.5236F, 0.0F)
      );
      head.addOrReplaceChild(
         "top_fur",
         CubeListBuilder.create().texOffs(39, 1).addBox(-5.5F, -4.0F, 0.0F, 11.0F, 4.0F, 0.0F),
         PartPose.offsetAndRotation(0.0F, -4.0F, -7.5F, -0.5236F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "right_ear", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-6.0F, -6.5F, -5.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(-2.5F, 2.5F, 0.5F)
      );
      head.addOrReplaceChild(
         "left_ear", CubeListBuilder.create().texOffs(0, 32).addBox(2.0F, -6.5F, -5.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(3.5F, 2.5F, 0.5F)
      );
      PartDefinition snout = head.addOrReplaceChild(
         "snout", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -14.0F, -18.6667F, 6.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 14.0F, 7.1667F)
      );
      snout.addOrReplaceChild(
         "right_fang",
         CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-1.0F, -4.5F, -11.5F, 0.0F, 6.0F, 7.0F),
         PartPose.offset(-2.0F, -5.5F, -10.1667F)
      );
      snout.addOrReplaceChild(
         "left_fang", CubeListBuilder.create().texOffs(0, 18).addBox(1.0F, -4.5F, -11.5F, 0.0F, 6.0F, 7.0F), PartPose.offset(2.0F, -5.5F, -10.1667F)
      );
      PartDefinition body = root.addOrReplaceChild(
         "body",
         CubeListBuilder.create().texOffs(0, 16).addBox(-7.5F, -13.0F, -13.5F, 15.0F, 13.0F, 27.0F, new CubeDeformation(0.01F)),
         PartPose.offset(0.0F, 13.0F, -2.0F)
      );
      body.addOrReplaceChild(
         "scales", CubeListBuilder.create().texOffs(25, 30).addBox(0.0F, -12.8F, -14.0F, 0.0F, 6.0F, 27.0F), PartPose.offset(0.0F, -6.0F, 0.5F)
      );
      body.addOrReplaceChild(
         "tail", CubeListBuilder.create().texOffs(26, 64).addBox(-3.0F, -1.0F, -1.5F, 6.0F, 4.0F, 13.0F), PartPose.offset(0.0F, -10.0F, 13.5F)
      );
      root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(0, 57).mirror().addBox(-3.0F, -1.0F, -3.0F, 6.0F, 12.0F, 6.0F),
         PartPose.offset(-3.9F, 13.0F, -11.0F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 12.0F, 6.0F), PartPose.offset(4.0F, 13.0F, -11.0F)
      );
      root.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(0, 57).mirror().addBox(-3.0F, -1.0F, -3.0F, 6.0F, 12.0F, 6.0F),
         PartPose.offset(-3.9F, 13.0F, 6.95F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 12.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 6.95F)
      );
      return LayerDefinition.create(mesh, 128, 96);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.body, this.leftFrontLeg, this.rightFrontLeg, this.leftHindLeg, this.rightHindLeg, this.head);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.rightFrontLeg.setPos(-3.9F, 13.0F, -11.0F);
      this.leftFrontLeg.setPos(4.0F, 13.0F, -11.0F);
      this.head.setPos(0.0F, 4.0F, -15.5F);
      this.body.setPos(0.0F, 13.0F, -2.0F);
      float f = ageInTicks - entityIn.tickCount;
      float animScale = entityIn.getChargingAnimationScale(f);
      animScale *= animScale;
      this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.tail.xRot = -0.8F + limbSwingAmount * 0.8F + 2.0F * animScale;
      this.tail.yRot = 0.0F;
      this.tail.zRot = 0.0F;
      this.body.xRot = 0.0F;
      if (animScale > 0.0F) {
         this.rightFrontLeg.y += 3.0F * animScale;
         this.leftFrontLeg.y += 3.0F * animScale;
         this.rightFrontLeg.xRot += -(0.8F * animScale);
         this.leftFrontLeg.xRot += -(0.8F * animScale);
         this.body.xRot = 0.2F * animScale;
         this.head.y += 6.0F * animScale;
         this.body.y += 3.0F * animScale;
         this.tail.yRot = Mth.cos(ageInTicks * 1.6F) * 0.1F;
         this.tail.zRot = Mth.cos(ageInTicks * 1.6F) * 0.2F;
      }
   }
}
