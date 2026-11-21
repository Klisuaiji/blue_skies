package com.legacy.blue_skies.client.models.entities.hostile.boss;

import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
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

public class StarlitCrusherModel<T extends StarlitCrusherEntity> extends ListModel<T> {
   protected final ModelPart leftHindLeg;
   protected final ModelPart rightHindLeg;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart dummyPoint;
   protected final ModelPart body;
   protected final ModelPart leftBranch;
   protected final ModelPart rightBranch;
   protected final ModelPart backBranch;
   protected final ModelPart leaves;
   protected final ModelPart leafCross1;
   protected final ModelPart leafCross2;
   protected final ModelPart rightArm;
   protected final ModelPart rightArmLayer;
   protected final ModelPart hammer;
   protected final ModelPart hammerLayer;
   protected final ModelPart head;
   protected final ModelPart headLayer;
   private float smashWave = 0.0F;

   public StarlitCrusherModel(ModelPart model) {
      this.dummyPoint = model.getChild("dummy");
      this.body = this.dummyPoint.getChild("body");
      this.leaves = this.body.getChild("leaves");
      this.leafCross1 = this.leaves.getChild("leaf_cross_1");
      this.leafCross2 = this.leaves.getChild("leaf_cross_2");
      this.head = this.body.getChild("head");
      this.headLayer = this.head.getChild("head_layer");
      this.rightBranch = this.body.getChild("right_branch");
      this.leftBranch = this.body.getChild("left_branch");
      this.backBranch = this.body.getChild("back_branch");
      this.rightArm = this.body.getChild("right_arm");
      this.rightArmLayer = this.rightArm.getChild("right_arm_layer");
      this.hammer = this.rightArm.getChild("hammer");
      this.hammerLayer = this.hammer.getChild("hammer_layer");
      this.rightFrontLeg = this.dummyPoint.getChild("right_front_leg");
      this.leftFrontLeg = this.dummyPoint.getChild("left_front_leg");
      this.rightHindLeg = this.dummyPoint.getChild("right_hind_leg");
      this.leftHindLeg = this.dummyPoint.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      CubeDeformation size = new CubeDeformation(0.5F);
      PartDefinition dummy = root.addOrReplaceChild(
         "dummy", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -5.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -2.0F, 0.0F)
      );
      PartDefinition body = dummy.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 53).addBox(-9.0F, -28.0F, -9.0F, 18.0F, 29.0F, 18.0F), PartPose.offset(0.0F, 17.0F, 0.0F)
      );
      PartDefinition leaves = body.addOrReplaceChild(
         "leaves", CubeListBuilder.create().texOffs(0, 0).addBox(-19.0F, -17.0F, -19.0F, 38.0F, 16.0F, 37.0F), PartPose.offset(0.0F, -25.0F, 0.0F)
      );
      leaves.addOrReplaceChild(
         "leaf_cross_1",
         CubeListBuilder.create().texOffs(72, 73).addBox(-22.0F, -11.0F, 0.0F, 44.0F, 11.0F, 0.0F),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.7854F, 0.0F)
      );
      leaves.addOrReplaceChild(
         "leaf_cross_2",
         CubeListBuilder.create().texOffs(72, 73).addBox(-22.0F, -11.0F, 0.0F, 44.0F, 11.0F, 0.0F),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.7854F, 0.0F)
      );
      PartDefinition head = body.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -7.0F, 8.0F, 10.0F, 8.0F), PartPose.offset(0.0F, -16.0F, -9.0F)
      );
      head.addOrReplaceChild(
         "head_layer", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -5.0F, -7.0F, 8.0F, 10.0F, 8.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "right_branch",
         CubeListBuilder.create().texOffs(74, 53).addBox(-2.5F, -12.0F, -3.0F, 4.0F, 14.0F, 6.0F),
         PartPose.offsetAndRotation(8.5F, -20.0F, 0.0F, 0.0F, 0.0F, 0.6981F)
      );
      body.addOrReplaceChild(
         "left_branch",
         CubeListBuilder.create().texOffs(74, 53).mirror().addBox(-1.5F, -12.0F, -3.0F, 4.0F, 14.0F, 6.0F),
         PartPose.offsetAndRotation(-8.5F, -20.0F, 0.0F, 0.0F, 0.0F, -0.6981F)
      );
      body.addOrReplaceChild(
         "back_branch",
         CubeListBuilder.create().texOffs(54, 53).addBox(-2.5F, -12.0F, -3.0F, 6.0F, 14.0F, 4.0F),
         PartPose.offsetAndRotation(-0.5F, -20.0F, 9.0F, -0.6981F, 0.0F, 0.0F)
      );
      PartDefinition rightArm = body.addOrReplaceChild(
         "right_arm", CubeListBuilder.create().texOffs(110, 118).addBox(-16.0F, -2.0F, -2.75F, 18.0F, 5.0F, 5.0F), PartPose.offset(-9.0F, -11.0F, 0.25F)
      );
      rightArm.addOrReplaceChild(
         "right_arm_layer",
         CubeListBuilder.create().texOffs(109, 108).addBox(-16.0F, -2.0F, -2.75F, 18.0F, 5.0F, 5.0F, size),
         PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition hammer = rightArm.addOrReplaceChild(
         "hammer", CubeListBuilder.create().texOffs(0, 100).addBox(-10.0F, -4.5F, -9.0F, 10.0F, 9.0F, 17.0F), PartPose.offset(-16.0F, 0.5F, 0.0F)
      );
      hammer.addOrReplaceChild(
         "hammer_layer", CubeListBuilder.create().texOffs(55, 100).addBox(-10.0F, -4.5F, -9.0F, 10.0F, 9.0F, 17.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      dummy.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(136, 87).mirror().addBox(-3.0F, -1.0F, -3.0F, 6.0F, 13.0F, 6.0F),
         PartPose.offset(-9.0F, 14.0F, -9.0F)
      );
      dummy.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(136, 87).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 13.0F, 6.0F), PartPose.offset(9.0F, 14.0F, -9.0F)
      );
      dummy.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(136, 87).mirror().addBox(-3.0F, -1.0F, -3.0F, 6.0F, 13.0F, 6.0F),
         PartPose.offset(-9.0F, 14.0F, 9.0F)
      );
      dummy.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(136, 87).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 13.0F, 6.0F), PartPose.offset(9.0F, 14.0F, 9.0F)
      );
      return LayerDefinition.create(mesh, 160, 128);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.dummyPoint);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch) {
      StarlitCrusherModel.EntAnimations anims = StarlitCrusherModel.EntAnimations.INSTANCE;
      boolean dying = entityIn.getDeathTicks() > 0;
      boolean rooted = entityIn.isRooted();
      boolean stunned = entityIn.isStunned();
      StarlitCrusherEntity.EntLegPosition legAnim = StarlitCrusherEntity.EntLegPosition.getEntPosition(entityIn);
      switch (legAnim) {
         case NONE:
            anims.defaultLegAnimations(this, limbSwing, limbSwingAmount);
            this.leftHindLeg.yRot = 0.0F;
            this.rightHindLeg.yRot = 0.0F;
            this.rightFrontLeg.yRot = 0.0F;
            this.leftFrontLeg.yRot = 0.0F;
            this.leftHindLeg.zRot = 0.0F;
            this.rightHindLeg.zRot = 0.0F;
            this.rightFrontLeg.zRot = 0.0F;
            this.leftFrontLeg.zRot = 0.0F;
            break;
         case DYING: {
            float x = -0.5F;
            float z = -0.5F;
            if (this.leftHindLeg.xRot > x) {
               this.leftHindLeg.xRot -= 0.01F;
            }

            if (this.rightHindLeg.xRot > x) {
               this.rightHindLeg.xRot -= 0.01F;
            }

            if (this.rightFrontLeg.xRot < -x) {
               this.rightFrontLeg.xRot += 0.01F;
            }

            if (this.leftFrontLeg.xRot < -x) {
               this.leftFrontLeg.xRot += 0.01F;
            }

            this.leftHindLeg.yRot = 0.0F;
            this.rightHindLeg.yRot = 0.0F;
            this.rightFrontLeg.yRot = 0.0F;
            this.leftFrontLeg.yRot = 0.0F;
            if (this.leftHindLeg.zRot < -z) {
               this.leftHindLeg.zRot += 0.01F;
            }

            if (this.rightHindLeg.zRot > z) {
               this.rightHindLeg.zRot -= 0.01F;
            }

            if (this.rightFrontLeg.zRot > z) {
               this.rightFrontLeg.zRot -= 0.01F;
            }

            if (this.leftFrontLeg.zRot < -z) {
               this.leftFrontLeg.zRot += 0.01F;
            }
            break;
         }
         case SPINNING: {
            float x = -0.5F;
            float y = 0.0F;
            float z = -0.5F;
            this.leftHindLeg.xRot = x;
            this.rightHindLeg.xRot = x;
            this.rightFrontLeg.xRot = -x;
            this.leftFrontLeg.xRot = -x;
            this.leftHindLeg.yRot = -y;
            this.rightHindLeg.yRot = y;
            this.rightFrontLeg.yRot = -y;
            this.leftFrontLeg.yRot = y;
            this.leftHindLeg.zRot = -z;
            this.rightHindLeg.zRot = z;
            this.rightFrontLeg.zRot = z;
            this.leftFrontLeg.zRot = -z;
            break;
         }
         case ROOTED:
            this.leftHindLeg.xRot = 1.0F;
            this.rightHindLeg.xRot = 1.0F;
            this.rightFrontLeg.xRot = -1.0F;
            this.leftFrontLeg.xRot = -1.0F;
            this.leftHindLeg.yRot = -0.7F;
            this.rightHindLeg.yRot = 0.7F;
            this.rightFrontLeg.yRot = -0.7F;
            this.leftFrontLeg.yRot = 0.7F;
            this.leftHindLeg.zRot = -1.2F;
            this.rightHindLeg.zRot = 1.2F;
            this.rightFrontLeg.zRot = 1.2F;
            this.leftFrontLeg.zRot = -1.2F;
            break;
         case STUNNED:
            this.leftHindLeg.xRot = 1.0F;
            this.rightHindLeg.xRot = 1.0F;
            this.rightFrontLeg.xRot = -1.0F;
            this.leftFrontLeg.xRot = -1.0F;
            this.leftHindLeg.yRot = -0.1F;
            this.rightHindLeg.yRot = 0.1F;
            this.rightFrontLeg.yRot = -0.1F;
            this.leftFrontLeg.yRot = 0.1F;
            this.leftHindLeg.zRot = -1.5F;
            this.rightHindLeg.zRot = 1.5F;
            this.rightFrontLeg.zRot = 1.5F;
            this.leftFrontLeg.zRot = -1.5F;
      }

      if (dying) {
         this.leaves.yRot = Mth.cos(partialTicks * 3.0F) * 0.1F;
         this.leaves.xRot = -0.12F + Mth.cos(partialTicks * 1.5F) * 0.1F;
         if (this.head.xRot > -1.0F) {
            this.head.xRot -= 0.005F;
         }

         if (this.head.xRot > -1.0F) {
            this.head.xRot = 0.0F - entityIn.getDeathTicks() * 0.01F;
         } else {
            this.head.xRot = -1.0F;
         }

         this.head.yRot = Mth.cos(partialTicks * 1.0F) * 0.1F;
         this.rightArm.xRot = 0.0F;
         this.rightArm.yRot = 0.0F;
         if (this.rightArm.zRot < 1.0F) {
            this.rightArm.zRot = -0.65F + entityIn.getDeathTicks() * 0.023F;
         } else {
            this.rightArm.zRot = 1.0F;
         }
      } else {
         if (rooted) {
            anims.defaultHeadAnimations(this, headPitch, netHeadYaw, entityIn, partialTicks);
            this.dummyPoint.y = 6.0F;
            this.body.xRot = 0.0F;
            anims.hammerAnimations(this, entityIn, limbSwingAmount, partialTicks);
            this.rightArm.zRot = -0.3F;
            if (entityIn.canShake) {
               this.leaves.yRot = Mth.cos(partialTicks * 2.0F) * 0.1F;
            }
         } else if (stunned) {
            this.head.xRot = 0.5F + Mth.cos(partialTicks * 0.1F) * 0.2F + limbSwingAmount * 0.5F;
            this.head.yRot = 0.0F;
            this.headLayer.xRot = Mth.sin(partialTicks * 0.1F) * 3.0F * (float) (Math.PI / 180.0);
            this.headLayer.yRot = 0.0F;
            this.headLayer.zRot = Mth.cos(partialTicks * 0.1F) * 2.5F * (float) (Math.PI / 180.0);
            this.dummyPoint.y = 6.0F;
            this.body.xRot = 0.0F;
            this.rightArm.xRot = 0.0F;
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = -0.3F;
         } else {
            if (entityIn.isSpinning()) {
               this.body.xRot = 0.0F;
            }

            this.dummyPoint.y = entityIn.isSpinning() ? -1.5F : -2.0F;
            this.rightArm.visible = true;
            anims.hammerAnimations(this, entityIn, limbSwingAmount, partialTicks);
            anims.defaultHeadAnimations(this, headPitch, netHeadYaw, entityIn, partialTicks);
         }

         if (!entityIn.canShake) {
            this.leaves.yRot = 0.0F;
         }

         this.leaves.xRot = -0.12F + Mth.cos(partialTicks * 0.2F) * 0.1F;
         this.backBranch.xRot = -0.6981F + Mth.cos(partialTicks * 0.2F) * 0.1F;
         this.leftBranch.xRot = Mth.cos(partialTicks * 0.2F) * 0.1F;
         this.rightBranch.xRot = Mth.cos(partialTicks * 0.2F) * 0.1F;
      }
   }

   public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
      if (entityIn.attackTimer > 0) {
         this.smashWave = 0.0F;
         float h = -0.5F + 1.5F * this.triangleWave(entityIn.attackTimer - partialTick, 10.0F);
         this.rightArm.yRot = Math.min(0.0F, h);
      } else if (entityIn.smashTimer > 0) {
         float f = this.triangleWave(entityIn.smashTimer - partialTick, 20.0F);
         float h = -0.5F + 1.5F * f;
         float finalX = Math.min(0.1F, f * 0.4F);
         this.rightArm.yRot = -1.0F + Math.min(0.0F, h);
         this.body.xRot = 0.0F;
         this.body.xRot += finalX;
         this.smashWave = -finalX;
      } else {
         this.body.xRot = 0.0F;
         this.smashWave = 0.0F;
      }
   }

   private float triangleWave(float min, float max) {
      return (Math.abs(min % max - max * 0.5F) - max * 0.25F) / (max * 0.25F);
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }

   static class EntAnimations {
      public static final StarlitCrusherModel.EntAnimations INSTANCE = new StarlitCrusherModel.EntAnimations();

      public void defaultHeadAnimations(StarlitCrusherModel<?> model, float headPitch, float netHeadYaw, StarlitCrusherEntity entityIn, float partialTicks) {
         model.head.zRot = 0.0F;
         model.head.xRot = headPitch * (float) (Math.PI / 180.0) + model.smashWave;
         model.head.yRot = SkiesClientUtil.lockNum(netHeadYaw * (float) (Math.PI / 180.0), 0.75F);
         if (model.head.yRot == 4.0F) {
            model.head.yRot = 0.0F;
         }

         model.headLayer.xRot = Mth.sin(partialTicks * 0.1F) * 3.0F * (float) (Math.PI / 180.0);
         model.headLayer.yRot = 0.0F;
         model.headLayer.zRot = Mth.cos(partialTicks * 0.1F) * 2.5F * (float) (Math.PI / 180.0);
      }

      public void defaultLegAnimations(StarlitCrusherModel<?> model, float limbSwing, float limbSwingAmount) {
         model.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
         model.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
         model.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
         model.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      }

      public void hammerAnimations(StarlitCrusherModel<?> model, StarlitCrusherEntity entityIn, float limbSwingAmount, float ageInTicks) {
         if (entityIn.isSpinning()) {
            model.rightArm.yRot = 0.0F;
            model.rightArm.xRot = 0.0F;
            model.rightArm.zRot = 0.0F;
         } else {
            float moveResist = limbSwingAmount * 1.0F;
            boolean attacking = entityIn.attackTimer > 0.0F;
            boolean smashing = entityIn.smashTimer > 0.0F;
            if (attacking) {
               model.rightArm.xRot = 0.0F;
               model.rightArm.zRot = -0.65F;
            } else if (smashing) {
               model.rightArm.xRot = 0.0F;
               model.rightArm.zRot = -1.6F;
            } else {
               model.rightArm.xRot = -Mth.cos(ageInTicks * 0.1F) * 0.1F - moveResist;
               model.rightArm.yRot = Mth.cos(ageInTicks * 0.1F) * 0.1F + moveResist;
               model.rightArm.zRot = -0.65F;
            }
         }
      }
   }
}
