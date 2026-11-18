package com.legacy.blue_skies.client.models.entities.passive;

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
import net.minecraft.world.entity.Entity;

public class StardustWoolModel<T extends Entity> extends QuadrupedModel<T> {
   protected final ModelPart body;
   protected final ModelPart rightHindLeg;
   protected final ModelPart leftHindLeg;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;

   public StardustWoolModel(ModelPart model) {
      super(model, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
      this.body = model.getChild("body");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
   }

   public static LayerDefinition createWoolLayer() {
      MeshDefinition mesh = QuadrupedModel.createBodyMesh(0, CubeDeformation.NONE);
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "body",
         CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, 0.0F, 0.0F, 10.0F, 10.0F, 21.0F, new CubeDeformation(0.01F)),
         PartPose.offset(-5.0F, 4.5F, -10.5F)
      );
      root.addOrReplaceChild(
         "right_front_leg", CubeListBuilder.create().texOffs(23, 0).addBox(-0.5F, -0.5F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(-4.5F, 14.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(23, 0).mirror().addBox(-0.5F, -0.5F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(1.5F, 14.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "right_hind_leg", CubeListBuilder.create().texOffs(23, 0).addBox(-0.5F, -0.5F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(-4.5F, 14.0F, 7.5F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(23, 0).mirror().addBox(-0.5F, -0.5F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(1.5F, 14.0F, 7.5F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg);
   }

   public Iterable<ModelPart> headParts() {
      return List.of();
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.body.xRot = 0.0F;
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
   }
}
