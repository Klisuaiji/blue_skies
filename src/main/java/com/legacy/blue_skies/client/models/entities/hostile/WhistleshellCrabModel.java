package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.entities.hostile.WhistleshellCrabEntity;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class WhistleshellCrabModel<T extends WhistleshellCrabEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart rightEye;
   protected final ModelPart leftEye;
   protected final ModelPart rightLeg1;
   protected final ModelPart rightLeg2;
   protected final ModelPart rightLeg3;
   protected final ModelPart rightLeg4;
   protected final ModelPart leftLeg1;
   protected final ModelPart leftLeg2;
   protected final ModelPart leftLeg3;
   protected final ModelPart leftLeg4;
   protected final ModelPart leftArm;
   protected final ModelPart leftClaw;
   protected final ModelPart rightArm;
   protected final ModelPart rightClawTop;
   protected final ModelPart rightClawBottom;

   public WhistleshellCrabModel(ModelPart model) {
      this.body = model.getChild("body");
      this.rightEye = this.body.getChild("right_eye");
      this.leftEye = this.body.getChild("left_eye");
      this.rightArm = model.getChild("right_arm");
      this.rightClawTop = this.rightArm.getChild("right_claw_top");
      this.rightClawBottom = this.rightArm.getChild("right_claw_bottom");
      this.leftArm = model.getChild("left_arm");
      this.leftClaw = this.leftArm.getChild("left_claw");
      this.rightLeg1 = model.getChild("right_leg_1");
      this.rightLeg2 = model.getChild("right_leg_2");
      this.rightLeg3 = model.getChild("right_leg_3");
      this.rightLeg4 = model.getChild("right_leg_4");
      this.leftLeg1 = model.getChild("left_leg_1");
      this.leftLeg2 = model.getChild("left_leg_2");
      this.leftLeg3 = model.getChild("left_leg_3");
      this.leftLeg4 = model.getChild("left_leg_4");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 0).addBox(-12.4333F, -7.8F, -7.6667F, 25.0F, 8.0F, 18.0F), PartPose.offset(0.0333F, 21.0F, 0.6667F)
      );
      body.addOrReplaceChild(
         "right_eye",
         CubeListBuilder.create().texOffs(13, 15).mirror().addBox(-0.7F, -0.95F, -0.25F, 1.0F, 1.0F, 1.0F),
         PartPose.offset(-4.7333F, -7.75F, -4.4167F)
      );
      body.addOrReplaceChild(
         "left_eye", CubeListBuilder.create().texOffs(13, 15).addBox(-0.5F, -0.95F, -0.25F, 1.0F, 1.0F, 1.0F), PartPose.offset(5.0667F, -7.75F, -4.4167F)
      );
      PartDefinition rightArm = root.addOrReplaceChild(
         "right_arm", CubeListBuilder.create().texOffs(40, 68).addBox(-1.7F, -1.5F, -8.4F, 3.0F, 3.0F, 11.0F), PartPose.offset(-12.25F, 15.5F, -7.0F)
      );
      rightArm.addOrReplaceChild(
         "right_claw_top", CubeListBuilder.create().texOffs(0, 57).addBox(-3.35F, 0.0F, -13.0F, 7.0F, 6.0F, 13.0F), PartPose.offset(-0.25F, 0.0F, -8.0F)
      );
      rightArm.addOrReplaceChild(
         "right_claw_bottom", CubeListBuilder.create().texOffs(0, 37).addBox(-3.35F, -7.0F, -13.0F, 7.0F, 7.0F, 13.0F), PartPose.offset(-0.25F, 0.0F, -8.0F)
      );
      PartDefinition leftArm = root.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(41, 45).addBox(-1.5F, -1.5F, -4.9F, 3.0F, 3.0F, 7.0F), PartPose.offset(12.5F, 15.5F, -7.0F)
      );
      leftArm.addOrReplaceChild(
         "left_claw", CubeListBuilder.create().texOffs(41, 55).addBox(-1.8F, -3.1F, -6.9F, 4.0F, 6.0F, 7.0F), PartPose.offset(0.0F, 0.0F, -5.0F)
      );
      root.addOrReplaceChild(
         "right_leg_1",
         CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-11.0F, -1.0F, -1.25F, 14.0F, 8.0F, 3.0F),
         PartPose.offset(-12.0F, 17.0F, -0.75F)
      );
      root.addOrReplaceChild(
         "right_leg_2", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-13.0F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F), PartPose.offset(-12.0F, 17.0F, 3.0F)
      );
      root.addOrReplaceChild(
         "right_leg_3", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-13.0F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F), PartPose.offset(-12.0F, 17.0F, 6.5F)
      );
      root.addOrReplaceChild(
         "right_leg_4",
         CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-12.75F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F),
         PartPose.offset(-10.25F, 17.0F, 10.5F)
      );
      root.addOrReplaceChild(
         "left_leg_1", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F), PartPose.offset(11.0F, 17.0F, -0.5F)
      );
      root.addOrReplaceChild(
         "left_leg_2", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F), PartPose.offset(12.0F, 17.0F, 3.0F)
      );
      root.addOrReplaceChild(
         "left_leg_3", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F), PartPose.offset(12.0F, 17.0F, 6.5F)
      );
      root.addOrReplaceChild(
         "left_leg_4", CubeListBuilder.create().texOffs(0, 26).addBox(-1.25F, -1.0F, -1.5F, 14.0F, 8.0F, 3.0F), PartPose.offset(10.25F, 17.0F, 10.5F)
      );
      return LayerDefinition.create(mesh, 96, 96);
   }

   public Iterable<ModelPart> parts() {
      return List.of(
         this.body,
         this.rightLeg1,
         this.rightLeg2,
         this.rightLeg3,
         this.rightLeg4,
         this.leftLeg1,
         this.leftLeg2,
         this.leftLeg3,
         this.leftLeg4,
         this.leftArm,
         this.rightArm
      );
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.setRotationAngle(this.rightLeg1, -0.1F, -0.5236F, 0.0F);
      this.setRotationAngle(this.rightLeg2, 0.0F, 0.0F, 0.0F);
      this.setRotationAngle(this.rightLeg3, 0.0F, 0.3491F, 0.0F);
      this.setRotationAngle(this.rightLeg4, 0.1F, 0.6981F, 0.0F);
      this.setRotationAngle(this.leftLeg1, -0.1F, 0.5236F, 0.0F);
      this.setRotationAngle(this.leftLeg2, 0.0F, 0.0F, 0.0F);
      this.setRotationAngle(this.leftLeg3, 0.0F, -0.3491F, 0.0F);
      this.setRotationAngle(this.leftLeg4, 0.1F, -0.6981F, 0.0F);
      float animAmount = Mth.cos(ageInTicks * 0.2F) * 0.1F;
      this.leftArm.xRot = -0.2F - Mth.cos(ageInTicks * 0.05F) * 0.1F;
      this.leftArm.yRot = -0.6F;
      this.rightArm.xRot = -0.2F + Mth.cos(ageInTicks * 0.05F) * 0.1F;
      this.rightArm.yRot = 0.6F;
      float extra = 0.5F;
      float speed = 0.7662F;
      float f7 = Math.abs(Mth.sin(limbSwing * speed + 0.0F) * (0.4F + extra)) * limbSwingAmount;
      float f8 = Math.abs(Mth.sin(limbSwing * speed + (float) Math.PI) * (0.4F + extra)) * limbSwingAmount;
      float f9 = Math.abs(Mth.sin(limbSwing * speed + (float) (Math.PI / 2)) * (0.4F + extra)) * limbSwingAmount;
      float f10 = Math.abs(Mth.sin(limbSwing * speed + (float) (Math.PI * 3.0 / 2.0)) * (0.4F + extra)) * limbSwingAmount;
      this.rightLeg4.zRot += f7;
      this.leftLeg4.zRot += -f7;
      this.rightLeg2.zRot += f8;
      this.leftLeg2.zRot += -f8;
      this.rightLeg3.zRot += f9;
      this.leftLeg3.zRot += -f9;
      this.rightLeg1.zRot += f10;
      this.leftLeg1.zRot += -f10;
      float f = ageInTicks - entityIn.tickCount;
      float attackAnimScale = entityIn.getAttackAnimationScale(f);
      attackAnimScale *= attackAnimScale;
      if (attackAnimScale > 0.0F) {
         this.rightArm.yRot -= 1.3F * attackAnimScale;
         float attackAnim = attackAnimScale * 0.5F - 0.5F;
         this.rightClawTop.xRot = -attackAnim;
         this.rightClawBottom.xRot = attackAnim;
      } else {
         this.rightClawTop.xRot = Math.max(0.0F, animAmount + 0.1F);
         this.rightClawBottom.xRot = -Math.max(0.0F, animAmount + 0.1F);
      }
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
