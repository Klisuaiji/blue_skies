package com.legacy.blue_skies.client.models.entities.misc.pet;

import com.legacy.blue_skies.client.SkiesClientUtil;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class PetArtificialGolemModel<T extends LivingEntity> extends ListModel<T> {
   protected final ModelPart rightLeg;
   protected final ModelPart leftLeg;
   protected final ModelPart body;
   protected final ModelPart head;
   protected final ModelPart nose;
   protected final ModelPart rightHornBottom;
   protected final ModelPart rightHornTop;
   protected final ModelPart leftHornBottom;
   protected final ModelPart leftHornTop;
   protected final ModelPart rightArm;
   protected final ModelPart leftArm;

   public PetArtificialGolemModel(ModelPart model) {
      this.body = model.getChild("body");
      this.head = model.getChild("head");
      this.nose = this.head.getChild("nose");
      this.rightHornBottom = this.head.getChild("right_horn_bottom");
      this.rightHornTop = this.rightHornBottom.getChild("right_horn_top");
      this.leftHornBottom = this.head.getChild("left_horn_bottom");
      this.leftHornTop = this.leftHornBottom.getChild("left_horn_top");
      this.rightArm = model.getChild("right_arm");
      this.leftArm = model.getChild("left_arm");
      this.rightLeg = model.getChild("right_leg");
      this.leftLeg = model.getChild("left_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -3.0F, -2.5F, 6.0F, 5.0F, 4.0F), PartPose.offset(0.0F, 17.0F, 0.0F));
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.75F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(0.0F, 13.75F, -0.5F)
      );
      head.addOrReplaceChild(
         "nose", CubeListBuilder.create().texOffs(0, 1).mirror().addBox(-0.5F, -0.25F, -1.0F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -1.25F, -2.0F)
      );
      PartDefinition rightHornBottom = head.addOrReplaceChild(
         "right_horn_bottom", CubeListBuilder.create().texOffs(16, 7).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(2.0F, -2.75F, 0.0F)
      );
      rightHornBottom.addOrReplaceChild(
         "right_horn_top", CubeListBuilder.create().texOffs(16, 3).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(1.5F, -0.5F, 0.0F)
      );
      PartDefinition leftHornBottom = head.addOrReplaceChild(
         "left_horn_bottom", CubeListBuilder.create().texOffs(16, 7).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F), PartPose.offset(-2.0F, -2.75F, 0.0F)
      );
      leftHornBottom.addOrReplaceChild(
         "left_horn_top", CubeListBuilder.create().texOffs(16, 3).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(-1.5F, -0.5F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_arm", CubeListBuilder.create().texOffs(9, 18).mirror().addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F), PartPose.offset(-3.0F, 15.0F, -0.5F)
      );
      root.addOrReplaceChild(
         "left_arm", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, -1.0F, -1.5F, 2.0F, 6.0F, 2.0F), PartPose.offset(3.0F, 15.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_leg", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-1.0F, 0.75F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(-1.75F, 18.25F, -0.5F)
      );
      root.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -0.25F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(1.75F, 19.25F, -0.5F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      SkiesClientUtil.addHumanoidAnimation(
         this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.head, ageInTicks, netHeadYaw, headPitch, limbSwing, limbSwingAmount
      );
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
   }
}
