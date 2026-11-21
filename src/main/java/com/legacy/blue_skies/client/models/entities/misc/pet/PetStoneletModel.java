package com.legacy.blue_skies.client.models.entities.misc.pet;

import com.legacy.blue_skies.client.SkiesClientUtil;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class PetStoneletModel<T extends LivingEntity> extends ListModel<T> {
   protected final ModelPart rightLeg;
   protected final ModelPart rightLegLayer;
   protected final ModelPart leftLeg;
   protected final ModelPart leftLegLayer;
   protected final ModelPart lowerBody;
   protected final ModelPart lowerBodyLayer;
   protected final ModelPart body;
   protected final ModelPart bodyLayer;
   protected final ModelPart topDecal;
   protected final ModelPart head;
   protected final ModelPart headLayer;
   protected final ModelPart rightArm;
   protected final ModelPart rightArmLayer;
   protected final ModelPart leftArm;
   protected final ModelPart leftArmLayer;

   public PetStoneletModel(ModelPart model) {
      this.body = model.getChild("body");
      this.bodyLayer = this.body.getChild("body_layer");
      this.head = model.getChild("head");
      this.headLayer = this.head.getChild("head_layer");
      this.rightArm = model.getChild("right_arm");
      this.rightArmLayer = this.rightArm.getChild("right_arm_layer");
      this.leftArm = model.getChild("left_arm");
      this.leftArmLayer = this.leftArm.getChild("left_arm_layer");
      this.lowerBody = model.getChild("lower_body");
      this.lowerBodyLayer = this.lowerBody.getChild("lower_body_layer");
      this.rightLeg = model.getChild("right_leg");
      this.rightLegLayer = this.rightLeg.getChild("right_leg_layer");
      this.leftLeg = model.getChild("left_leg");
      this.leftLegLayer = this.leftLeg.getChild("left_leg_layer");
      this.topDecal = this.body.getChild("top_decal");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      CubeDeformation size = new CubeDeformation(0.2F);
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(32, 7).addBox(-3.25F, -4.0F, -3.0F, 7.0F, 4.0F, 6.0F), PartPose.offset(0.0F, 17.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "body_layer", CubeListBuilder.create().texOffs(0, 7).addBox(-4.0F, -2.0F, -3.0F, 7.0F, 4.0F, 6.0F, size), PartPose.offset(0.75F, -2.0F, 0.0F)
      );
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.25F, -2.75F, 4.0F, 4.0F, 3.0F), PartPose.offset(1.25F, 15.0F, -2.75F)
      );
      head.addOrReplaceChild(
         "head_layer", CubeListBuilder.create().texOffs(32, 0).addBox(-0.75F, -2.25F, -2.75F, 4.0F, 4.0F, 3.0F, size), PartPose.offset(-1.25F, 0.0F, 0.0F)
      );
      PartDefinition lowerBody = root.addOrReplaceChild(
         "lower_body", CubeListBuilder.create().texOffs(0, 17).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 3.0F, 4.0F), PartPose.offset(0.0F, 17.0F, 0.0F)
      );
      lowerBody.addOrReplaceChild(
         "lower_body_layer", CubeListBuilder.create().texOffs(32, 17).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 3.0F, 4.0F, size), PartPose.ZERO
      );
      PartDefinition rightArm = root.addOrReplaceChild(
         "right_arm", CubeListBuilder.create().texOffs(16, 26).addBox(-1.5F, -0.75F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offset(-3.5F, 14.5F, 0.0F)
      );
      rightArm.addOrReplaceChild(
         "right_arm_layer", CubeListBuilder.create().texOffs(48, 26).addBox(-1.5F, -0.75F, -1.0F, 2.0F, 4.0F, 2.0F, size), PartPose.ZERO
      );
      PartDefinition leftArm = root.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(24, 25).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(4.0F, 14.5F, 0.0F)
      );
      leftArm.addOrReplaceChild("left_arm_layer", CubeListBuilder.create().texOffs(56, 25).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, size), PartPose.ZERO);
      PartDefinition rightLeg = root.addOrReplaceChild(
         "right_leg", CubeListBuilder.create().texOffs(0, 24).addBox(-1.25F, -0.25F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(-2.0F, 18.25F, 0.0F)
      );
      rightLeg.addOrReplaceChild(
         "right_leg_layer", CubeListBuilder.create().texOffs(32, 24).addBox(-1.25F, -0.25F, -1.0F, 2.0F, 6.0F, 2.0F, size), PartPose.ZERO
      );
      PartDefinition leftLeg = root.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(8, 25).addBox(-0.75F, -0.25F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(2.0F, 19.25F, 0.0F)
      );
      leftLeg.addOrReplaceChild("left_leg_layer", CubeListBuilder.create().texOffs(40, 25).addBox(-0.75F, -0.25F, -1.0F, 2.0F, 5.0F, 2.0F, size), PartPose.ZERO);
      body.addOrReplaceChild(
         "top_decal", CubeListBuilder.create().texOffs(52, 22).addBox(-3.0F, 8.0F, 0.0F, 6.0F, 2.0F, 0.0F), PartPose.offset(0.0F, -14.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 32);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      SkiesClientUtil.addHumanoidAnimation(
         this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.head, ageInTicks, netHeadYaw, headPitch, limbSwing, limbSwingAmount
      );
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.head, this.body, this.lowerBody, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
   }
}
