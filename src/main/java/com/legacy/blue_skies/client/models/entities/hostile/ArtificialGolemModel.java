package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class ArtificialGolemModel<T extends ArtificialGolemEntity> extends ListModel<T> implements ArmedModel {
   protected final ModelPart body;
   protected final ModelPart leftArm;
   protected final ModelPart rightArm;
   protected final ModelPart cannonConnection;
   protected final ModelPart head;
   protected final ModelPart nose;
   protected final ModelPart rightHornTop;
   protected final ModelPart rightHornBottom;
   protected final ModelPart leftHornTop;
   protected final ModelPart leftHornBottom;
   protected final ModelPart leftLeg;
   protected final ModelPart rightLeg;

   public ArtificialGolemModel(ModelPart model) {
      this.body = model.getChild("body");
      this.head = model.getChild("head");
      this.nose = this.head.getChild("nose");
      this.rightHornBottom = this.head.getChild("right_horn_bottom");
      this.rightHornTop = this.head.getChild("right_horn_top");
      this.leftHornBottom = this.head.getChild("left_horn_bottom");
      this.leftHornTop = this.head.getChild("left_horn_top");
      this.rightArm = model.getChild("right_arm");
      this.cannonConnection = this.rightArm.getChild("cannon_connection");
      this.leftArm = model.getChild("left_arm");
      this.rightLeg = model.getChild("right_leg");
      this.leftLeg = model.getChild("left_leg");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 39).addBox(-6.5F, -1.8F, -3.0F, 13.0F, 12.0F, 6.0F, size), PartPose.offset(0.0F, 1.8F, 0.0F)
      );
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "nose", CubeListBuilder.create().texOffs(24, 2).mirror().addBox(-1.0F, -5.1F, -4.0F, 2.0F, 4.0F, 2.0F, size), PartPose.offset(0.0F, 2.1F, -2.0F)
      );
      head.addOrReplaceChild(
         "right_horn_bottom", CubeListBuilder.create().texOffs(33, 9).addBox(-8.0F, -10.1F, 1.0F, 4.0F, 2.0F, 2.0F, size), PartPose.offset(0.0F, 2.1F, -2.0F)
      );
      head.addOrReplaceChild(
         "right_horn_top", CubeListBuilder.create().texOffs(33, 3).addBox(-8.0F, -13.1F, 1.0F, 2.0F, 3.0F, 2.0F, size), PartPose.offset(0.0F, 2.1F, -2.0F)
      );
      head.addOrReplaceChild(
         "left_horn_bottom", CubeListBuilder.create().texOffs(33, 9).addBox(4.0F, -10.1F, 1.0F, 4.0F, 2.0F, 2.0F, size), PartPose.offset(0.0F, 2.1F, -2.0F)
      );
      head.addOrReplaceChild(
         "left_horn_top", CubeListBuilder.create().texOffs(33, 3).addBox(6.0F, -13.1F, 1.0F, 2.0F, 3.0F, 2.0F, size), PartPose.offset(0.0F, 2.1F, -2.0F)
      );
      PartDefinition rightArm = root.addOrReplaceChild(
         "right_arm",
         CubeListBuilder.create().texOffs(38, 19).mirror().addBox(-4.5F, -1.5F, -1.0F, 4.0F, 15.0F, 4.0F, size),
         PartPose.offset(-6.0F, 1.5F, -1.0F)
      );
      rightArm.addOrReplaceChild(
         "cannon_connection",
         CubeListBuilder.create().texOffs(17, 23).addBox(-11.0F, -19.0F, -2.5F, 5.0F, 9.0F, 5.0F, size),
         PartPose.offset(6.0F, 22.5F, 1.0F)
      );
      root.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(0, 19).addBox(0.5F, -1.5F, -1.0F, 4.0F, 15.0F, 4.0F, size), PartPose.offset(6.0F, 1.5F, -1.0F)
      );
      root.addOrReplaceChild(
         "right_leg",
         CubeListBuilder.create().texOffs(39, 41).mirror().addBox(-3.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, size),
         PartPose.offset(-2.5F, 12.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(39, 41).addBox(-3.5F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, size), PartPose.offset(5.0F, 12.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.body, this.leftArm, this.rightArm, this.head, this.leftLeg, this.rightLeg);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      int i = entityIn.getAttackTimer();
      this.head.xRot = headPitch / (float) (180.0 / Math.PI);
      this.head.yRot = netHeadYaw / (float) (180.0 / Math.PI);
      this.leftLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      if (entityIn.isRanged() && entityIn.getAttacking()) {
         this.rightArm.xRot = this.head.xRot + -1.5F;
         this.rightArm.yRot = this.head.yRot;
         this.rightArm.zRot = 0.0F;
         this.leftArm.zRot = 0.0F;
         this.leftArm.yRot = 0.0F;
         this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
         this.leftArm.zRot = this.leftArm.zRot - (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
         this.leftArm.xRot = this.leftArm.xRot - Mth.sin(ageInTicks * 0.067F) * 0.05F;
      } else if (i <= 0) {
         this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 2.0F * limbSwingAmount * 0.5F;
         this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
         this.rightArm.zRot = 0.0F;
         this.leftArm.zRot = 0.0F;
         this.rightArm.yRot = 0.0F;
         this.leftArm.yRot = 0.0F;
         this.rightArm.zRot = this.rightArm.zRot + (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
         this.leftArm.zRot = this.leftArm.zRot - (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
         this.rightArm.xRot = this.rightArm.xRot + Mth.sin(ageInTicks * 0.067F) * 0.05F;
         this.leftArm.xRot = this.leftArm.xRot - Mth.sin(ageInTicks * 0.067F) * 0.05F;
      }
   }

   public void prepareMobModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
      if (!entitylivingbaseIn.isRanged()) {
         int i = entitylivingbaseIn.getAttackTimer();
         if (i > 0) {
            this.rightArm.xRot = -2.0F + 1.5F * this.triangleWave(i - partialTickTime, 10.0F);
            this.leftArm.xRot = -2.0F + 1.5F * this.triangleWave(i - partialTickTime, 10.0F);
         }
      }
   }

   private float triangleWave(float p_78172_1_, float p_78172_2_) {
      return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
   }

   public void translateToHand(HumanoidArm side, PoseStack matrix) {
      this.getArmForSide(side).translateAndRotate(matrix);
   }

   protected ModelPart getArmForSide(HumanoidArm side) {
      return side == HumanoidArm.LEFT ? this.leftArm : this.leftArm;
   }
}
