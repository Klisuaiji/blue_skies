package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.client.SkiesClientUtil;
import java.util.List;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ReindeerModel<T extends LivingEntity> extends QuadrupedModel<T> {
   protected final ModelPart leftEar;
   protected final ModelPart rightEar;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart body;
   protected final ModelPart leftThigh;
   protected final ModelPart head;
   protected final ModelPart tail;
   protected final ModelPart rightThigh;
   protected final ModelPart leftHindLeg;
   protected final ModelPart mouthTop;
   protected final ModelPart mouthBottom;
   protected final ModelPart rightHindLeg;
   protected final ModelPart rightAntler;
   protected final ModelPart leftAntler;

   public ReindeerModel(ModelPart model) {
      super(model, true, 16.0F, 4.0F, 2.0F, 2.0F, 24);
      this.head = model.getChild("head");
      this.rightAntler = this.head.getChild("right_antler");
      this.leftAntler = this.head.getChild("left_antler");
      this.rightEar = this.head.getChild("right_ear");
      this.leftEar = this.head.getChild("left_ear");
      this.mouthTop = this.head.getChild("mouth_top");
      this.mouthBottom = this.head.getChild("mouth_bottom");
      this.body = model.getChild("body");
      this.tail = model.getChild("tail");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightThigh = model.getChild("right_thigh");
      this.leftThigh = model.getChild("left_thigh");
      this.rightHindLeg = this.rightThigh.getChild("right_hind_leg");
      this.leftHindLeg = this.leftThigh.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head",
         CubeListBuilder.create().texOffs(46, 32).addBox(-2.0F, -9.8F, -2.0F, 4.0F, 12.0F, 5.0F),
         PartPose.offsetAndRotation(0.0F, 6.0F, -8.5F, (float) (Math.PI / 6), 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "mouth_top", CubeListBuilder.create().texOffs(30, 32).addBox(-1.5F, -9.6F, -6.5F, 3.0F, 3.0F, 5.0F), PartPose.offset(0.0F, 0.0F, 0.02F)
      );
      head.addOrReplaceChild(
         "mouth_bottom", CubeListBuilder.create().texOffs(32, 40).addBox(-1.5F, -6.6F, -6.0F, 3.0F, 2.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "right_ear", CubeListBuilder.create().texOffs(43, 33).mirror().addBox(-4.0F, -11.0F, 2.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "left_ear", CubeListBuilder.create().texOffs(43, 33).addBox(2.0F, -11.0F, 2.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "right_antler",
         CubeListBuilder.create().texOffs(40, 37).mirror().addBox(-0.3188F, -14.9632F, -6.9268F, 0.0F, 15.0F, 12.0F, size),
         PartPose.offsetAndRotation(-0.5F, -9.2224F, 0.3494F, -0.1745F, 0.4363F, -0.3491F)
      );
      head.addOrReplaceChild(
         "left_antler",
         CubeListBuilder.create().texOffs(40, 37).addBox(0.3188F, -14.9632F, -6.9268F, 0.0F, 15.0F, 12.0F, size),
         PartPose.offsetAndRotation(0.5F, -9.2224F, 0.3494F, -0.1745F, -0.4363F, 0.3491F)
      );
      root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -8.0F, -19.0F, 9.0F, 10.0F, 22.0F, size), PartPose.offset(0.0F, 11.0F, 9.0F)
      );
      root.addOrReplaceChild(
         "tail",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 3.0F, 7.0F),
         PartPose.offsetAndRotation(0.0F, 3.0F, 12.0F, 0.813F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(13, 45).mirror().addBox(-1.0F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F),
         PartPose.offset(-3.5F, 13.0F, -8.0F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(13, 45).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 11.0F, 3.0F), PartPose.offset(3.5F, 13.0F, -8.0F)
      );
      PartDefinition rightThigh = root.addOrReplaceChild(
         "right_thigh", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-1.5F, -2.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offset(-3.5F, 10.0F, 9.0F)
      );
      PartDefinition leftThigh = root.addOrReplaceChild(
         "left_thigh", CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, -2.0F, -2.5F, 4.0F, 8.0F, 5.0F), PartPose.offset(3.5F, 10.0F, 9.0F)
      );
      rightThigh.addOrReplaceChild(
         "right_hind_leg", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-1.0F, 6.0F, -1.5F, 3.0F, 8.0F, 3.0F), PartPose.ZERO
      );
      leftThigh.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, 6.0F, -1.5F, 3.0F, 8.0F, 3.0F), PartPose.ZERO);
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of(this.body, this.leftFrontLeg, this.rightFrontLeg, this.rightThigh, this.leftThigh, this.tail);
   }

   public Iterable<ModelPart> headParts() {
      return List.of(this.head);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.leftAntler.visible = !entity.isBaby();
      this.rightAntler.visible = !entity.isBaby();
      this.head.yRot = SkiesClientUtil.lockNum(netHeadYaw * (float) (Math.PI / 180.0), 0.5F);
      this.head.xRot = SkiesClientUtil.lockNum(headPitch * (float) (Math.PI / 180.0), 20.0F) + 0.5F;
      if (limbSwingAmount > 0.2F) {
         this.head.xRot = this.head.xRot + Mth.cos(limbSwing * 0.6F) * 0.15F * limbSwingAmount;
      }

      this.head.y = 4.0F;
      this.head.z = -10.0F;
      this.leftThigh.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.rightThigh.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.tail.xRot = 0.813F + Mth.sin(ageInTicks * 0.1F) * 4.5F * (float) (Math.PI / 180.0) - Math.min(limbSwingAmount, 0.8F);
      this.tail.zRot = Mth.cos(ageInTicks * 0.1F) * 2.5F * (float) (Math.PI / 180.0);
   }

   public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
   }
}
