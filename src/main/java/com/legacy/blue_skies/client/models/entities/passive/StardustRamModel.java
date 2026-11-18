package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.entities.passive.StardustRamEntity;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StardustRamModel<T extends StardustRamEntity> extends QuadrupedModel<T> {
   protected final ModelPart body;
   protected final ModelPart rightHindLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart head;
   protected final ModelPart rightHorn;
   protected final ModelPart leftHorn;
   protected final ModelPart leftHindLeg;
   protected final ModelPart rightFrontLeg;

   public StardustRamModel(ModelPart model) {
      super(model, true, 10.0F, 4.0F, 1.5F, 2.0F, 24);
      this.head = model.getChild("head");
      this.rightHorn = this.head.getChild("right_horn");
      this.leftHorn = this.head.getChild("left_horn");
      this.body = model.getChild("body");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.5F, -10.0F, 6.0F, 6.0F, 10.0F), PartPose.offset(0.0F, 3.0F, -6.0F)
      );
      head.addOrReplaceChild(
         "right_horn",
         CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, -3.0F, -2.0F, 2.0F, 8.0F, 8.0F),
         PartPose.offsetAndRotation(3.0F, -2.0F, -1.0F, 0.0F, 0.5236F, -0.2618F)
      );
      head.addOrReplaceChild(
         "left_horn",
         CubeListBuilder.create().texOffs(0, 29).mirror().addBox(-2.0F, -3.0F, -2.0F, 2.0F, 8.0F, 8.0F),
         PartPose.offsetAndRotation(-3.0F, -2.0F, -1.0F, 0.0F, -0.5236F, 0.2618F)
      );
      root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 35).addBox(0.0F, 0.0F, 0.0F, 9.0F, 9.0F, 20.0F), PartPose.offset(-4.5F, 5.0F, -10.0F));
      root.addOrReplaceChild(
         "right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(-4.5F, 14.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(1.5F, 14.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(-4.5F, 14.0F, 7.5F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(1.5F, 14.0F, 7.5F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg);
   }

   public Iterable<ModelPart> headParts() {
      return List.of(this.head);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      if (entityIn.getSheepTimer() <= 0) {
         this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      }

      this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.body.xRot = 0.0F;
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
   }

   public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
      this.head.y = 3.0F + entityIn.getHeadEatPositionScale(partialTick) * 9.0F;
      if (entityIn.getSheepTimer() > 0) {
         this.head.xRot = entityIn.getHeadEatAngleScale(partialTick);
      }
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
