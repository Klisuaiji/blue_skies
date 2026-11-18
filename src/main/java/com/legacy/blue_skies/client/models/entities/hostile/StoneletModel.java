package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.client.SkiesClientUtil;
import java.util.List;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class StoneletModel<T extends LivingEntity> extends HumanoidModel<T> {
   private final ModelPart body;
   private final ModelPart lowerBody;
   private final ModelPart rightLeg;
   private final ModelPart rightLegLayer;
   private final ModelPart leftLeg;
   private final ModelPart leftLegLayer;
   private final ModelPart lowerBodyLayer;
   private final ModelPart head;
   private final ModelPart headLayer;
   private final ModelPart rightArm;
   private final ModelPart rightArmLayer;
   private final ModelPart leftArm;
   private final ModelPart leftArmLayer;
   private final ModelPart topDecal;
   private final ModelPart bodyLayer;
   protected boolean hideLayers = false;

   public StoneletModel(ModelPart model) {
      this(model, false);
   }

   public StoneletModel(ModelPart model, boolean hideLayersIn) {
      super(model);
      this.hideLayers = hideLayersIn;
      this.body = model.getChild("body");
      this.bodyLayer = this.body.getChild("body_layer");
      this.head = this.body.getChild("head");
      this.headLayer = this.head.getChild("head_layer");
      this.rightArm = this.body.getChild("right_arm");
      this.rightArmLayer = this.rightArm.getChild("right_arm_layer");
      this.leftArm = this.body.getChild("left_arm");
      this.leftArmLayer = this.leftArm.getChild("left_arm_layer");
      this.lowerBody = this.body.getChild("lower_body");
      this.lowerBodyLayer = this.lowerBody.getChild("lower_body_layer");
      this.rightLeg = this.lowerBody.getChild("right_leg");
      this.rightLegLayer = this.rightLeg.getChild("right_leg_layer");
      this.leftLeg = this.lowerBody.getChild("left_leg");
      this.leftLegLayer = this.leftLeg.getChild("left_leg_layer");
      this.topDecal = this.body.getChild("top_decal");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      PartDefinition root = mesh.getRoot();
      CubeDeformation size = new CubeDeformation(0.5F);
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 41).addBox(-8.0F, -10.0F, -7.0F, 16.0F, 10.0F, 13.0F), PartPose.offset(0.25F, 8.0F, 0.0F)
      );
      body.addOrReplaceChild("body_layer", CubeListBuilder.create().texOffs(0, 18).addBox(-8.0F, -10.0F, -7.0F, 16.0F, 10.0F, 13.0F, size), PartPose.ZERO);
      PartDefinition head = body.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(59, 42).addBox(-4.25F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F), PartPose.offset(2.75F, -5.0F, -7.0F)
      );
      head.addOrReplaceChild("head_layer", CubeListBuilder.create().texOffs(59, 28).addBox(-4.25F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, size), PartPose.ZERO);
      PartDefinition lowerBody = body.addOrReplaceChild(
         "lower_body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -4.5F, 12.0F, 7.0F, 9.0F), PartPose.offset(0.0F, 0.0F, -0.5F)
      );
      lowerBody.addOrReplaceChild(
         "lower_body_layer", CubeListBuilder.create().texOffs(43, 0).addBox(-6.0F, 0.0F, -4.5F, 12.0F, 7.0F, 9.0F, size), PartPose.ZERO
      );
      PartDefinition rightArm = body.addOrReplaceChild(
         "right_arm", CubeListBuilder.create().texOffs(108, 14).addBox(-5.0F, -2.0F, -2.5F, 5.0F, 9.0F, 5.0F), PartPose.offset(-7.5F, -6.0F, -0.5F)
      );
      rightArm.addOrReplaceChild("right_arm_layer", CubeListBuilder.create().texOffs(108, 0).addBox(-5.0F, -2.0F, -2.5F, 5.0F, 9.0F, 5.0F, size), PartPose.ZERO);
      PartDefinition leftArm = body.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(92, 16).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F), PartPose.offset(7.5F, -7.0F, 0.0F)
      );
      leftArm.addOrReplaceChild("left_arm_layer", CubeListBuilder.create().texOffs(92, 0).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, size), PartPose.ZERO);
      PartDefinition rightLeg = lowerBody.addOrReplaceChild(
         "right_leg", CubeListBuilder.create().texOffs(108, 46).addBox(-2.0F, 0.0F, -2.5F, 5.0F, 13.0F, 5.0F), PartPose.offset(-5.5F, 3.0F, 0.0F)
      );
      rightLeg.addOrReplaceChild(
         "right_leg_layer", CubeListBuilder.create().texOffs(108, 28).addBox(-2.0F, 0.0F, -2.5F, 5.0F, 13.0F, 5.0F, size), PartPose.ZERO
      );
      PartDefinition leftLeg = lowerBody.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(88, 48).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F), PartPose.offset(4.5F, 5.0F, 0.0F)
      );
      leftLeg.addOrReplaceChild("left_leg_layer", CubeListBuilder.create().texOffs(88, 32).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 11.0F, 5.0F, size), PartPose.ZERO);
      body.addOrReplaceChild(
         "top_decal", CubeListBuilder.create().texOffs(58, 57).addBox(-8.0F, -7.0F, 0.0F, 15.0F, 7.0F, 0.0F), PartPose.offset(0.5F, -10.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 64);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      if (this.hideLayers) {
         this.bodyLayer.visible = false;
         this.lowerBodyLayer.visible = false;
         this.headLayer.visible = false;
         this.leftLegLayer.visible = false;
         this.rightLegLayer.visible = false;
         this.leftArmLayer.visible = false;
         this.rightArmLayer.visible = false;
         this.topDecal.visible = false;
      }

      super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      this.copyOnlyAngles(this.body, super.body);
      this.copyOnlyAngles(this.head, super.head);
      this.copyOnlyAngles(this.leftLeg, super.leftLeg);
      this.copyOnlyAngles(this.rightLeg, super.rightLeg);
      this.copyOnlyAngles(this.leftArm, super.leftArm);
      this.copyOnlyAngles(this.rightArm, super.rightArm);
      this.head.yRot = SkiesClientUtil.lockNum(netHeadYaw * (float) (Math.PI / 180.0), 0.8F);
      this.lowerBody.xRot = -this.body.xRot;
      this.lowerBody.yRot = -this.body.yRot;
      this.lowerBody.zRot = -this.body.zRot;
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of(this.body);
   }

   public Iterable<ModelPart> headParts() {
      return List.of();
   }

   public void copyOnlyAngles(ModelPart model, ModelPart copyModel) {
      model.xRot = copyModel.xRot;
      model.yRot = copyModel.yRot;
      model.zRot = copyModel.zRot;
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
