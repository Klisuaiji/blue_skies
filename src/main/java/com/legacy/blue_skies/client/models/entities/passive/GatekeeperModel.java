package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GatekeeperModel<T extends GatekeeperEntity> extends VillagerModel<T> implements ArmedModel {
   protected final ModelPart gatekeeperRoot;
   protected final ModelPart rightArm;
   protected final ModelPart leftArm;
   protected final ModelPart arms;

   public GatekeeperModel(ModelPart model) {
      super(model);
      this.gatekeeperRoot = model;
      this.rightArm = model.getChild("right_arm");
      this.leftArm = model.getChild("left_arm");
      this.arms = model.getChild("arms");
      this.getHead().getChild("hat").visible = false;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = createBodyModel();
      PartDefinition root = mesh.getRoot();
      root.getChild("body")
         .addOrReplaceChild(
            "jacket", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.ZERO
         );
      root.addOrReplaceChild(
         "right_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(-5.0F, 2.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(5.0F, 2.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public ModelPart root() {
      return this.gatekeeperRoot;
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      GatekeeperEntity.ArmPose armPose = entityIn.getArmPose();
      this.arms.visible = armPose == GatekeeperEntity.ArmPose.CROSSED;
      this.rightArm.visible = armPose != GatekeeperEntity.ArmPose.CROSSED;
      this.leftArm.visible = armPose != GatekeeperEntity.ArmPose.CROSSED;
      if (armPose == GatekeeperEntity.ArmPose.SPELLCASTING) {
         this.rightArm.z = 0.0F;
         this.rightArm.x = -5.0F;
         this.leftArm.z = 0.0F;
         this.leftArm.x = 5.0F;
         this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
         this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
         this.rightArm.zRot = (float) (Math.PI * 3.0 / 4.0);
         this.leftArm.zRot = (float) (-Math.PI * 3.0 / 4.0);
         this.rightArm.yRot = 0.0F;
         this.leftArm.yRot = 0.0F;
      } else if (armPose == GatekeeperEntity.ArmPose.ATTACKING) {
         float f = Mth.sin(this.attackTime * (float) Math.PI);
         float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
         this.rightArm.zRot = 0.0F;
         this.leftArm.zRot = 0.0F;
         this.rightArm.yRot = (float) (Math.PI / 20);
         this.leftArm.yRot = (float) (-Math.PI / 20);
         if (entityIn.getMainArm() == HumanoidArm.RIGHT) {
            this.rightArm.xRot = -1.8849558F + Mth.cos(ageInTicks * 0.09F) * 0.15F;
            this.leftArm.xRot = -0.0F + Mth.cos(ageInTicks * 0.19F) * 0.5F;
            this.rightArm.xRot += f * 2.2F - f1 * 0.4F;
            this.leftArm.xRot += f * 1.2F - f1 * 0.4F;
         } else {
            this.rightArm.xRot = -0.0F + Mth.cos(ageInTicks * 0.19F) * 0.5F;
            this.leftArm.xRot = -1.8849558F + Mth.cos(ageInTicks * 0.09F) * 0.15F;
            this.rightArm.xRot += f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot += f * 2.2F - f1 * 0.4F;
         }

         this.rightArm.zRot = this.rightArm.zRot + (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
         this.leftArm.zRot = this.leftArm.zRot - (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
         this.rightArm.xRot = this.rightArm.xRot + Mth.sin(ageInTicks * 0.067F) * 0.05F;
         this.leftArm.xRot = this.leftArm.xRot - Mth.sin(ageInTicks * 0.067F) * 0.05F;
      } else if (armPose == GatekeeperEntity.ArmPose.BOW_AND_ARROW) {
         this.rightArm.yRot = -0.1F + this.getHead().yRot;
         this.rightArm.xRot = (float) (-Math.PI / 2) + this.getHead().xRot;
         this.rightArm.zRot = 0.3F;
         this.leftArm.xRot = -0.9424779F + this.getHead().xRot;
         this.leftArm.yRot = this.getHead().yRot - 0.2F;
         this.leftArm.zRot = (float) (Math.PI / 2);
      }
   }

   private ModelPart getArm(HumanoidArm primaryHand) {
      return primaryHand == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
   }

   public void translateToHand(HumanoidArm sideIn, PoseStack matrixStackIn) {
      this.getArm(sideIn).translateAndRotate(matrixStackIn);
   }
}
