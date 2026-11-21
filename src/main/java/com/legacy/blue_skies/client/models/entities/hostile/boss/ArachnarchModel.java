package com.legacy.blue_skies.client.models.entities.hostile.boss;

import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class ArachnarchModel<T extends ArachnarchEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart head;
   protected final ModelPart rightPinser;
   protected final ModelPart leftPinserEnd;
   protected final ModelPart rightPinserEnd;
   protected final ModelPart leftPinser;
   protected final ModelPart butt;
   protected final ModelPart leftLeg1;
   protected final ModelPart leftLegEnd1;
   protected final ModelPart leftLeg2;
   protected final ModelPart leftLegEnd2;
   protected final ModelPart leftLeg3;
   protected final ModelPart leftLegEnd3;
   protected final ModelPart leftLeg4;
   protected final ModelPart leftLegEnd4;
   protected final ModelPart rightLeg1;
   protected final ModelPart rightLegEnd1;
   protected final ModelPart rightLeg2;
   protected final ModelPart rightLegEnd2;
   protected final ModelPart rightLeg3;
   protected final ModelPart rightLegEnd3;
   protected final ModelPart rightLeg4;
   protected final ModelPart rightLegEnd4;

   public ArachnarchModel(ModelPart model) {
      this.head = model.getChild("head");
      this.rightPinser = this.head.getChild("right_pinser");
      this.rightPinserEnd = this.head.getChild("right_pinser_end");
      this.leftPinser = this.head.getChild("left_pinser");
      this.leftPinserEnd = this.head.getChild("left_pinser_end");
      this.body = model.getChild("body");
      this.butt = model.getChild("butt");
      this.rightLeg1 = model.getChild("right_leg_1");
      this.rightLeg2 = model.getChild("right_leg_2");
      this.rightLeg3 = model.getChild("right_leg_3");
      this.rightLeg4 = model.getChild("right_leg_4");
      this.rightLegEnd1 = this.rightLeg1.getChild("right_leg_end_1");
      this.rightLegEnd2 = this.rightLeg2.getChild("right_leg_end_2");
      this.rightLegEnd3 = this.rightLeg3.getChild("right_leg_end_3");
      this.rightLegEnd4 = this.rightLeg4.getChild("right_leg_end_4");
      this.leftLeg1 = model.getChild("left_leg_1");
      this.leftLeg2 = model.getChild("left_leg_2");
      this.leftLeg3 = model.getChild("left_leg_3");
      this.leftLeg4 = model.getChild("left_leg_4");
      this.leftLegEnd1 = this.leftLeg1.getChild("left_leg_end_1");
      this.leftLegEnd2 = this.leftLeg2.getChild("left_leg_end_2");
      this.leftLegEnd3 = this.leftLeg3.getChild("left_leg_end_3");
      this.leftLegEnd4 = this.leftLeg4.getChild("left_leg_end_4");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -7.0F, -9.0F, 9.0F, 9.0F, 9.0F), PartPose.offset(0.0F, 15.0F, -8.0F)
      );
      head.addOrReplaceChild(
         "right_pinser", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-2.8F, -0.5F, -17.0F, 1.0F, 1.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 5.0F)
      );
      head.addOrReplaceChild(
         "right_pinser_end", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-1.8F, -0.5F, -17.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 5.0F)
      );
      head.addOrReplaceChild(
         "left_pinser", CubeListBuilder.create().texOffs(0, 23).addBox(1.8F, -0.5F, -17.0F, 1.0F, 1.0F, 3.0F), PartPose.offset(0.0F, 0.0F, 5.0F)
      );
      head.addOrReplaceChild(
         "left_pinser_end", CubeListBuilder.create().texOffs(0, 28).addBox(0.8F, -0.5F, -17.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 5.0F)
      );
      root.addOrReplaceChild(
         "body",
         CubeListBuilder.create()
            .texOffs(76, 16)
            .addBox(-6.0F, -9.0F, -8.0F, 12.0F, 12.0F, 14.0F)
            .texOffs(86, 43)
            .addBox(-7.0F, -10.0F, -9.0F, 14.0F, 14.0F, 7.0F),
         PartPose.offset(0.0F, 15.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "butt", CubeListBuilder.create().texOffs(0, 28).addBox(-9.0F, -8.0F, 3.0F, 18.0F, 16.0F, 20.0F), PartPose.offset(0.0F, 12.0F, 3.0F)
      );
      PartDefinition r1 = root.addOrReplaceChild(
         "right_leg_1",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -1.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(-3.0F, 10.0F, -4.0F, 0.3491F, 2.7576F, 0.7156F)
      );
      r1.addOrReplaceChild(
         "right_leg_end_1", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, -1.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition r2 = root.addOrReplaceChild(
         "right_leg_2",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -2.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(-3.0F, 10.0F, -2.0F, 0.0873F, 2.9845F, 0.6458F)
      );
      r2.addOrReplaceChild(
         "right_leg_end_2", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, -2.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition r3 = root.addOrReplaceChild(
         "right_leg_3",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -2.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(-3.0F, 10.0F, 0.0F, -0.0873F, -3.0194F, 0.6458F)
      );
      r3.addOrReplaceChild(
         "right_leg_end_3", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, 0.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, -2.0F)
      );
      PartDefinition r4 = root.addOrReplaceChild(
         "right_leg_4",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -3.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(-3.0F, 10.0F, 2.0F, -0.3491F, -2.7751F, 0.7156F)
      );
      r4.addOrReplaceChild(
         "right_leg_end_4", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, 1.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, -4.0F)
      );
      PartDefinition l1 = root.addOrReplaceChild(
         "left_leg_1",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -3.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(3.0F, 10.0F, -4.0F, -0.3491F, 0.384F, -0.7156F)
      );
      l1.addOrReplaceChild(
         "left_leg_end_1", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, -3.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition l2 = root.addOrReplaceChild(
         "left_leg_2",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -2.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(3.0F, 10.0F, -2.0F, -0.0873F, 0.1396F, -0.6458F)
      );
      l2.addOrReplaceChild(
         "left_leg_end_2", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, -2.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition l3 = root.addOrReplaceChild(
         "left_leg_3",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -2.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(3.0F, 10.0F, 0.0F, 0.0873F, -0.1396F, -0.6458F)
      );
      l3.addOrReplaceChild(
         "left_leg_end_3", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, -2.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition l4 = root.addOrReplaceChild(
         "left_leg_4",
         CubeListBuilder.create().texOffs(82, 0).addBox(2.0F, 0.0F, -1.0F, 19.0F, 3.0F, 4.0F),
         PartPose.offsetAndRotation(3.0F, 10.0F, 2.0F, 0.3491F, -0.384F, -0.7156F)
      );
      l4.addOrReplaceChild(
         "left_leg_end_4", CubeListBuilder.create().texOffs(58, 0).addBox(21.0F, 0.0F, -1.0F, 3.0F, 32.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(
         this.body,
         this.head,
         this.butt,
         this.leftLeg1,
         this.leftLeg2,
         this.leftLeg3,
         this.leftLeg4,
         this.rightLeg1,
         this.rightLeg2,
         this.rightLeg3,
         this.rightLeg4
      );
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      BlockPos posUp = entityIn.blockPosition().above(2);
      boolean onCeiling = !entityIn.level().getBlockState(posUp).isAir() && (entityIn.isBesideClimbableBlock() || entityIn.isCeilingLocked());
      boolean holding = !entityIn.getPassengers().isEmpty();
      float finalHeadPitch = headPitch * (float) (Math.PI / 180.0);
      float finalHeadYaw = netHeadYaw * (float) (Math.PI / 180.0);
      this.head.xRot = onCeiling ? -finalHeadPitch : finalHeadPitch;
      this.head.yRot = onCeiling ? -finalHeadYaw : finalHeadYaw;
      this.setRotationAngle(this.rightLeg4, -0.3491F, -2.7751F, 0.7156F);
      this.setRotationAngle(this.rightLeg3, -0.0873F, -3.0194F, 0.6458F);
      this.setRotationAngle(this.rightLeg2, 0.0873F, 2.9845F, 0.6458F);
      this.setRotationAngle(this.rightLeg1, 0.3491F, 2.7576F, 0.7156F);
      this.setRotationAngle(this.leftLeg4, 0.3491F, -0.384F, -0.7156F);
      this.setRotationAngle(this.leftLeg3, 0.0873F, -0.1396F, -0.6458F);
      this.setRotationAngle(this.leftLeg2, -0.0873F, 0.1396F, -0.6458F);
      this.setRotationAngle(this.leftLeg1, -0.3491F, 0.384F, -0.7156F);
      float f3 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
      float f4 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * limbSwingAmount;
      float f5 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount;
      float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
      float f7 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
      float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.4F) * limbSwingAmount;
      float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI / 2)) * 0.4F) * limbSwingAmount;
      float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
      this.rightLeg4.yRot += f3;
      this.leftLeg4.yRot += -f3;
      this.rightLeg3.yRot += f4;
      this.leftLeg3.yRot += -f4;
      this.rightLeg2.yRot += f5;
      this.leftLeg2.yRot += -f5;
      this.rightLeg1.yRot += holding ? 0.0F : f6;
      this.leftLeg1.yRot += holding ? 0.0F : -f6;
      this.rightLeg4.zRot += f7;
      this.leftLeg4.zRot += -f7;
      this.rightLeg3.zRot += f8;
      this.leftLeg3.zRot += -f8;
      this.rightLeg2.zRot += f9;
      this.leftLeg2.zRot += -f9;
      this.rightLeg1.zRot += holding ? 0.0F : f10;
      this.leftLeg1.zRot += holding ? 0.0F : -f10;
      if (entityIn.deathTime > 0) {
         float animStartPos = 1.0F;
         float animSpeed = 0.7F;
         float animRange = 0.4F;
         this.head.xRot = this.head.xRot + (0.7F + Mth.cos(ageInTicks * 0.6F) * 0.1F);
         this.butt.xRot = -0.6F + Mth.cos(ageInTicks * 0.6F) * 0.1F;
         this.leftLeg1.zRot = this.leftLeg1.zRot + (animStartPos + Mth.cos(0.0F + ageInTicks * animSpeed) * animRange);
         this.leftLeg2.zRot = this.leftLeg2.zRot + (animStartPos + Mth.cos(3.0F + ageInTicks * animSpeed) * animRange);
         this.leftLeg3.zRot = this.leftLeg3.zRot + (animStartPos + Mth.cos(1.0F + ageInTicks * animSpeed) * animRange);
         this.leftLeg4.zRot = this.leftLeg4.zRot + (animStartPos + Mth.cos(2.0F + ageInTicks * animSpeed) * animRange);
         this.rightLeg1.zRot = this.rightLeg1.zRot + -(animStartPos + Mth.cos(2.0F + ageInTicks * animSpeed) * animRange);
         this.rightLeg2.zRot = this.rightLeg2.zRot + -(animStartPos + Mth.cos(1.0F + ageInTicks * animSpeed) * animRange);
         this.rightLeg3.zRot = this.rightLeg3.zRot + -(animStartPos + Mth.cos(3.0F + ageInTicks * animSpeed) * animRange);
         this.rightLeg4.zRot = this.rightLeg4.zRot + -(animStartPos + Mth.cos(0.0F + ageInTicks * animSpeed) * animRange);
      } else {
         if (entityIn.isStunned()) {
            this.head.zRot = 0.3F * Mth.sin(0.4F * ageInTicks);
            this.head.xRot = 0.4F;
            this.leftLeg1.xRot += -0.5F;
            this.leftLeg1.zRot = -1.0F + Mth.cos(ageInTicks * 0.2F) * 0.25F;
            this.rightLeg1.xRot += 0.5F;
            this.rightLeg1.zRot = 1.0F + -Mth.cos(ageInTicks * 0.2F) * 0.25F;
         } else if (holding) {
            this.head.xRot = -0.5F;
            this.head.zRot = -0.0F;
            float animAmount = Mth.cos(ageInTicks * 0.1F);
            float rotX = 1.5F + animAmount * 0.05F;
            float rotZ = 0.7F - animAmount * 0.05F;
            this.leftLeg1.xRot += -rotX;
            this.leftLeg1.zRot = -rotZ;
            this.rightLeg1.xRot += rotX;
            this.rightLeg1.zRot = rotZ;
         } else {
            this.head.zRot = 0.0F;
         }

         this.butt.xRot = Mth.cos(ageInTicks * 0.2F) * 0.12F;
         if (entityIn.clientScreechTime > 0) {
            this.head.xRot = this.head.xRot + -(Mth.cos(ageInTicks * 0.2F) * 0.12F);
            this.butt.xRot = Mth.cos(ageInTicks * 0.6F) * 0.15F;
         }

         float animAmount = Mth.cos(ageInTicks * (0.1F + (!entityIn.getPassengers().isEmpty() ? 0.5F : 0.0F))) * 0.02F;
         this.rightPinser.yRot = animAmount;
         this.rightPinserEnd.yRot = animAmount;
         this.leftPinser.yRot = -animAmount;
         this.leftPinserEnd.yRot = -animAmount;
      }
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
