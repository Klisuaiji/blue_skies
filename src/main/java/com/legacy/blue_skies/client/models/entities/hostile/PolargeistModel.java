package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.entities.hostile.PolargeistEntity;
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

public class PolargeistModel<T extends PolargeistEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart bodyMid;
   protected final ModelPart bodyIceBottom;
   protected final ModelPart head;
   protected final ModelPart snout;
   protected final ModelPart rightEar;
   protected final ModelPart leftEar;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart leftHindLeg;
   protected final ModelPart rightHindLeg;

   public PolargeistModel(ModelPart model) {
      super(RenderType::entityTranslucentCull);
      this.head = model.getChild("head");
      this.rightEar = this.head.getChild("right_ear");
      this.leftEar = this.head.getChild("left_ear");
      this.snout = this.head.getChild("snout");
      this.body = model.getChild("body");
      this.bodyMid = this.body.getChild("body_middle");
      this.bodyIceBottom = this.body.getChild("body_ice_bottom");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -8.0F, 10.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 9.0F, -14.0F)
      );
      head.addOrReplaceChild(
         "snout",
         CubeListBuilder.create()
            .texOffs(15, 16)
            .addBox(-3.0F, -15.0F, -25.0F, 6.0F, 4.0F, 3.0F)
            .texOffs(82, 57)
            .addBox(-3.0F, -11.0F, -25.0F, 6.0F, 4.0F, 3.0F),
         PartPose.offset(0.0F, 15.0F, 14.0F)
      );
      head.addOrReplaceChild(
         "right_ear", CubeListBuilder.create().texOffs(28, 3).mirror().addBox(-6.0F, -20.0F, -20.0F, 3.0F, 3.0F, 2.0F), PartPose.offset(0.0F, 15.0F, 14.0F)
      );
      head.addOrReplaceChild(
         "left_ear", CubeListBuilder.create().texOffs(28, 3).addBox(3.0F, -20.0F, -20.0F, 3.0F, 3.0F, 2.0F), PartPose.offset(0.0F, 15.0F, 14.0F)
      );
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 24).addBox(-7.5F, -23.0F, -14.0F, 15.0F, 14.0F, 26.0F), PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "body_middle", CubeListBuilder.create().texOffs(56, 22).addBox(-8.5F, -24.0F, -5.0F, 17.0F, 15.0F, 13.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "body_ice_bottom", CubeListBuilder.create().texOffs(40, 0).addBox(-8.5F, -9.0F, -5.0F, 17.0F, 5.0F, 13.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-2.0F, -1.0F, -2.5F, 5.0F, 11.0F, 5.0F),
         PartPose.offset(-5.0F, 14.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -1.0F, -2.5F, 5.0F, 11.0F, 5.0F), PartPose.offset(5.0F, 14.0F, -7.5F)
      );
      root.addOrReplaceChild(
         "right_hind_leg", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-2.5F, 0.0F, -4.0F, 5.0F, 9.0F, 7.0F), PartPose.offset(-5.0F, 15.0F, 9.0F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 9.0F, 7.0F), PartPose.offset(5.0F, 15.0F, 9.0F)
      );
      return LayerDefinition.create(mesh, 128, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.head, this.body, this.rightFrontLeg, this.leftFrontLeg, this.leftHindLeg, this.rightHindLeg);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      this.head.yRot = SkiesClientUtil.lockNum(netHeadYaw * (float) (Math.PI / 180.0), 0.9F);
      this.head.zRot = 0.0F;
      this.body.setPos(0.0F, 24.0F, 0.0F);
      this.setRotationAngle(this.body, 0.0F, 0.0F, 0.0F);
      this.head.setPos(0.0F, 9.0F, -14.0F);
      float f = ageInTicks - entityIn.tickCount;
      float f1 = entityIn.getStandingAnimationScale(f);
      f1 *= f1;
      float f2 = 1.0F - f1;
      this.rightFrontLeg.xRot = entityIn.isStanding() ? 0.0F : Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftFrontLeg.xRot = entityIn.isStanding() ? 0.0F : Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.body.xRot = -0.004203677F - f1;
      float lowerAmount = 13.0F;
      this.body.y = 24.0F * f2 + lowerAmount * f1;
      this.body.z = 0.0F * -f2 + 8.0F * -f1;
      this.rightFrontLeg.y = 14.0F * f2 - 2.0F * f1;
      this.rightFrontLeg.z = -10.0F * f2 - 4.0F * f1;
      this.rightFrontLeg.xRot = this.rightFrontLeg.xRot - (f1 * (float) Math.PI * 0.45F + (entityIn.isStanding() ? Mth.cos(ageInTicks * 0.6F) * 0.2F : 0.0F));
      this.leftFrontLeg.y = this.rightFrontLeg.y;
      this.leftFrontLeg.z = this.rightFrontLeg.z;
      this.leftFrontLeg.xRot = this.leftFrontLeg.xRot
         - (f1 * (float) Math.PI * 0.45F + (entityIn.isStanding() ? Mth.cos(3.0F + ageInTicks * 0.6F) * 0.2F : 0.0F));
      this.head.y = 9.0F * f2 - 10.0F * f1;
      this.head.z = -14.0F * f2 - 3.0F * f1;
      this.head.xRot += f1 * (float) Math.PI * 0.15F;
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
