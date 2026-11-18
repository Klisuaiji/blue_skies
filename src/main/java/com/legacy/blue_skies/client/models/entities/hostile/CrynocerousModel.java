package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.entities.hostile.CrynocerousEntity;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class CrynocerousModel<T extends CrynocerousEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart tail;
   protected final ModelPart neck;
   protected final ModelPart head;
   protected final ModelPart snout;
   protected final ModelPart backHorn;
   protected final ModelPart frontHorn;
   protected final ModelPart leftEar;
   protected final ModelPart rightEar;
   protected final ModelPart spirit;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart leftShoulder;
   protected final ModelPart rightHindLeg;
   protected final ModelPart rightHindShoulder;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart rightShoulder;
   protected final ModelPart leftHindLeg;
   protected final ModelPart leftHindShoulder;

   public CrynocerousModel(ModelPart model) {
      super(RenderType::entityTranslucentCull);
      this.neck = model.getChild("neck");
      this.head = this.neck.getChild("head");
      this.rightEar = this.head.getChild("right_ear");
      this.leftEar = this.head.getChild("left_ear");
      this.spirit = this.head.getChild("spirit");
      this.snout = this.head.getChild("snout");
      this.frontHorn = this.snout.getChild("front_horn");
      this.backHorn = this.snout.getChild("back_horn");
      this.body = model.getChild("body");
      this.tail = this.body.getChild("tail");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.rightShoulder = this.rightFrontLeg.getChild("right_shoulder");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.leftShoulder = this.leftFrontLeg.getChild("left_shoulder");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.rightHindShoulder = this.rightHindLeg.getChild("right_hind_shoulder");
      this.leftHindLeg = model.getChild("left_hind_leg");
      this.leftHindShoulder = this.leftHindLeg.getChild("left_hind_shoulder");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition neck = root.addOrReplaceChild(
         "neck", CubeListBuilder.create().texOffs(98, 18).addBox(-5.5F, -6.0F, -4.25F, 11.0F, 12.0F, 4.0F), PartPose.offset(0.0F, 2.1F, -15.0F)
      );
      PartDefinition head = neck.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(94, 0).addBox(-5.5F, -4.0F, -17.25F, 11.0F, 10.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 7.0F)
      );
      head.addOrReplaceChild(
         "right_ear", CubeListBuilder.create().texOffs(90, 52).mirror().addBox(-5.0F, -6.0F, -14.25F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "left_ear", CubeListBuilder.create().texOffs(90, 52).addBox(3.0F, -6.0F, -14.25F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "spirit", CubeListBuilder.create().texOffs(110, 55).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 0.5F, -12.35F)
      );
      PartDefinition snout = head.addOrReplaceChild(
         "snout", CubeListBuilder.create().texOffs(96, 36).addBox(-4.5F, -1.0F, -24.25F, 9.0F, 7.0F, 7.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      snout.addOrReplaceChild(
         "front_horn", CubeListBuilder.create().texOffs(100, 55).addBox(-1.0F, -5.0F, -23.55F, 2.0F, 4.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      snout.addOrReplaceChild(
         "back_horn", CubeListBuilder.create().texOffs(100, 55).addBox(-1.0F, -7.0F, -20.25F, 2.0F, 6.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition body = root.addOrReplaceChild(
         "body",
         CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -17.25F, -7.0F, 17.0F, 29.0F, 18.0F),
         PartPose.offsetAndRotation(-2.5F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "tail",
         CubeListBuilder.create().texOffs(75, 50).addBox(-1.5F, -0.25F, 0.0F, 3.0F, 12.0F, 2.0F),
         PartPose.offsetAndRotation(2.5F, 11.0F, 7.5F, -1.3614F, 0.0F, 0.0F)
      );
      PartDefinition rightLeg = root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(75, 25).mirror().addBox(-2.5F, -1.0F, -2.5F, 5.0F, 16.0F, 5.0F),
         PartPose.offset(-6.0F, 9.0F, -10.25F)
      );
      rightLeg.addOrReplaceChild(
         "right_shoulder",
         CubeListBuilder.create().texOffs(75, 0).mirror().addBox(-3.5F, -2.0F, -10.25F, 2.0F, 6.0F, 6.0F),
         PartPose.offset(-0.5F, -1.0F, 7.25F)
      );
      PartDefinition leftLeg = root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(75, 25).addBox(-3.0F, 2.0F, -2.4F, 5.0F, 14.0F, 5.0F), PartPose.offset(6.0F, 9.0F, -10.25F)
      );
      leftLeg.addOrReplaceChild(
         "left_shoulder", CubeListBuilder.create().texOffs(75, 0).addBox(2.5F, -2.0F, -10.25F, 2.0F, 6.0F, 6.0F), PartPose.offset(-0.5F, -1.0F, 7.25F)
      );
      PartDefinition rightHindLeg = root.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(75, 25).mirror().addBox(-2.5F, -1.0F, -2.5F, 5.0F, 16.0F, 5.0F),
         PartPose.offset(-6.0F, 9.0F, 10.25F)
      );
      rightHindLeg.addOrReplaceChild(
         "right_hind_shoulder",
         CubeListBuilder.create().texOffs(75, 0).mirror().addBox(-3.5F, -2.0F, -10.25F, 2.0F, 6.0F, 6.0F),
         PartPose.offset(-0.5F, -1.0F, 7.25F)
      );
      PartDefinition leftHindLeg = root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(75, 25).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 16.0F, 5.0F), PartPose.offset(6.0F, 9.0F, 10.25F)
      );
      leftHindLeg.addOrReplaceChild(
         "left_hind_shoulder", CubeListBuilder.create().texOffs(75, 0).addBox(2.5F, -2.0F, -10.25F, 2.0F, 6.0F, 6.0F), PartPose.offset(-0.5F, -1.0F, 7.25F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.neck, this.body, this.rightFrontLeg, this.leftFrontLeg, this.rightHindLeg, this.leftHindLeg);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.rightFrontLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      if (entityIn.attackTimer >= 11) {
         this.neck.xRot = headPitch / (float) (180.0 / Math.PI);
      }

      this.neck.yRot = netHeadYaw / (float) (180.0 / Math.PI);
      this.tail.xRot = -1.3614F;
      this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
   }

   public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTicks) {
      if (entityIn.attackTimer < 11) {
         float i = entityIn.attackTimer + partialTicks;
         this.neck.xRot = (float)(
            entityIn.attackTimer > 8 ? Math.min((double)(entityIn.getXRot() / (float) (180.0 / Math.PI)), Math.sin(i * 0.58F)) : Math.sin(i * 0.58F)
         );
      }
   }
}
