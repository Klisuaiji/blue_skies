package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.entities.passive.CosmicFoxEntity;
import java.util.List;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class CosmicFoxModel<T extends CosmicFoxEntity> extends AgeableListModel<T> {
   public ModelPart head;
   public ModelPart nose;
   public ModelPart rightHindLeg;
   public ModelPart leftEar;
   public ModelPart leftFrontLeg;
   public ModelPart rightEar;
   public ModelPart leftHindLeg;
   public ModelPart tail;
   public ModelPart rightFrontLeg;
   public ModelPart body;

   public CosmicFoxModel(ModelPart model) {
      super(true, 8.0F, 3.35F);
      this.head = model.getChild("head");
      this.rightEar = model.getChild("right_ear");
      this.leftEar = model.getChild("left_ear");
      this.nose = model.getChild("nose");
      this.body = model.getChild("body");
      this.tail = model.getChild("tail");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -6.0F, 8.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 16.5F, -5.0F));
      root.addOrReplaceChild(
         "right_ear", CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-3.5F, -5.0F, -3.0F, 2.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 16.5F, -5.0F)
      );
      root.addOrReplaceChild(
         "left_ear", CubeListBuilder.create().texOffs(29, 0).addBox(1.5F, -5.0F, -3.0F, 2.0F, 3.0F, 1.0F), PartPose.offset(0.0F, 16.5F, -5.0F)
      );
      root.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(30, 6).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 16.5F, -5.0F));
      root.addOrReplaceChild(
         "body",
         CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -7.0F, -3.0F, 6.0F, 11.0F, 5.0F),
         PartPose.offsetAndRotation(1.0F, 16.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
      );
      root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(23, 16).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 3.0F, 9.0F), PartPose.offset(0.0F, 17.0F, 5.0F));
      root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(23, 13).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F),
         PartPose.offset(-2.0F, 19.0F, -4.0F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(23, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(2.0F, 19.0F, -4.0F)
      );
      root.addOrReplaceChild(
         "right_hind_leg", CubeListBuilder.create().texOffs(23, 13).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(-2.0F, 19.0F, 4.5F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(23, 13).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F), PartPose.offset(2.0F, 19.0F, 4.5F)
      );
      return LayerDefinition.create(mesh, 64, 32);
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of(this.tail, this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg);
   }

   public Iterable<ModelPart> headParts() {
      return List.of(this.head, this.nose, this.rightEar, this.leftEar);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      boolean sitting = entityIn.isInSittingPose();
      this.head.y = sitting ? 16.0F : 16.5F;
      this.nose.y = this.head.y;
      this.rightEar.y = this.head.y;
      this.leftEar.y = this.head.y;
      this.head.xRot = headPitch / (float) (180.0 / Math.PI);
      this.head.yRot = netHeadYaw / (float) (180.0 / Math.PI);
      this.nose.xRot = this.head.xRot;
      this.nose.yRot = this.head.yRot;
      this.rightEar.xRot = this.head.xRot;
      this.rightEar.yRot = this.head.yRot;
      this.leftEar.xRot = this.head.xRot;
      this.leftEar.yRot = this.head.yRot;
      this.rightHindLeg.xRot = sitting ? -1.57F : Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = sitting ? -1.57F : Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.rightFrontLeg.xRot = sitting ? -1.57F : Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = sitting ? -1.57F : Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.rightHindLeg.yRot = sitting ? 0.5F : 0.0F;
      this.leftHindLeg.yRot = sitting ? -0.5F : 0.0F;
      this.rightFrontLeg.yRot = sitting ? 0.7F : 0.0F;
      this.leftFrontLeg.yRot = sitting ? -0.7F : 0.0F;
      this.tail.xRot = sitting ? -0.25F : -0.47F;
   }

   public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
