package com.legacy.blue_skies.client.models.entities.passive;

import com.google.common.collect.ImmutableList;
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

public class ShadeMonitorModel<T extends LivingEntity> extends QuadrupedModel<T> {
   protected final ModelPart body;
   protected final ModelPart bodyBack;
   protected final ModelPart tail;
   protected final ModelPart tailEnd;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart rightHindLeg;
   protected final ModelPart leftHindLeg;
   protected final ModelPart head;

   public ShadeMonitorModel(ModelPart model) {
      super(model, true, 6.0F, 5.0F, 1.7F, 2.0F, 24);
      this.head = model.getChild("head");
      this.body = model.getChild("body");
      this.bodyBack = this.body.getChild("body_back");
      this.tail = this.bodyBack.getChild("tail");
      this.tailEnd = this.tail.getChild("tail_end");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.5F, -6.0F, 6.0F, 5.0F, 6.0F, size), PartPose.offset(0.0F, 18.0F, -11.0F)
      );
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(6, 0).addBox(-4.0F, -3.0F, -22.0F, 8.0F, 6.0F, 21.0F, size), PartPose.offset(0.0F, 18.0F, 11.0F)
      );
      PartDefinition bodyBack = body.addOrReplaceChild(
         "body_back",
         CubeListBuilder.create().texOffs(0, 11).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 5.0F, 5.0F, size),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F)
      );
      PartDefinition tail = bodyBack.addOrReplaceChild(
         "tail",
         CubeListBuilder.create().texOffs(0, 27).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 3.0F, 12.0F, size),
         PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, -0.0873F, 0.0F, 0.0F)
      );
      tail.addOrReplaceChild(
         "tail_end",
         CubeListBuilder.create().texOffs(0, 42).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 7.0F, size),
         PartPose.offsetAndRotation(0.0F, 0.5F, 11.0F, -0.0873F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(43, 11).mirror().addBox(-2.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, size),
         PartPose.offset(-4.0F, 18.0F, -6.5F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(43, 11).addBox(-1.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, size), PartPose.offset(4.0F, 18.0F, -6.5F)
      );
      root.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(43, 11).mirror().addBox(-2.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, size),
         PartPose.offset(-4.0F, 18.0F, 6.5F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(43, 11).addBox(-1.0F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, size), PartPose.offset(4.0F, 18.0F, 6.5F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.rightFrontLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      float moveAmount = Mth.cos(ageInTicks * 0.2F) * 0.52F + Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * limbSwingAmount;
      this.bodyBack.yRot = moveAmount / 4.0F;
      this.tail.yRot = moveAmount / 2.0F;
      this.tailEnd.yRot = moveAmount;
   }

   public Iterable<ModelPart> bodyParts() {
      return ImmutableList.of(this.body, this.rightFrontLeg, this.leftFrontLeg, this.rightHindLeg, this.leftHindLeg);
   }

   public Iterable<ModelPart> headParts() {
      return ImmutableList.of(this.head);
   }
}
