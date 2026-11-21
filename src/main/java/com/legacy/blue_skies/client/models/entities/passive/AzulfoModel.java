package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.entities.passive.AzulfoEntity;
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

public class AzulfoModel<T extends AzulfoEntity> extends QuadrupedModel<T> {
   protected final ModelPart body;
   protected final ModelPart leftHindLeg;
   protected final ModelPart leftFrontLeg;
   protected final ModelPart rightHindLeg;
   protected final ModelPart rightFrontLeg;
   protected final ModelPart bodyBack;
   protected final ModelPart rightShoulder;
   protected final ModelPart leftShoulder;
   protected final ModelPart tail;
   protected final ModelPart head;
   protected final ModelPart hornTopRight;
   protected final ModelPart hornTopLeft;
   protected final ModelPart hornBaseLeft;
   protected final ModelPart snootTop;
   protected final ModelPart hornBaseRight;
   protected final ModelPart snootBottom;
   protected final ModelPart helmet;

   public AzulfoModel(ModelPart model) {
      super(model, false, 12.0F, 8.0F, 2.0F, 2.0F, 24);
      this.head = model.getChild("head");
      this.snootTop = this.head.getChild("snoot_top");
      this.snootBottom = this.head.getChild("snoot_bottom");
      this.hornTopRight = this.head.getChild("horn_top_right");
      this.hornBaseRight = this.head.getChild("horn_base_right");
      this.hornTopLeft = this.head.getChild("horn_top_left");
      this.hornBaseLeft = this.head.getChild("horn_base_left");
      this.body = model.getChild("body");
      this.bodyBack = model.getChild("body_back");
      this.tail = model.getChild("tail");
      this.rightFrontLeg = model.getChild("right_front_leg");
      this.rightShoulder = this.rightFrontLeg.getChild("right_shoulder");
      this.leftFrontLeg = model.getChild("left_front_leg");
      this.leftShoulder = this.leftFrontLeg.getChild("left_shoulder");
      this.rightHindLeg = model.getChild("right_hind_leg");
      this.leftHindLeg = model.getChild("left_hind_leg");
      this.helmet = model.getChild("helmet");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "helmet", CubeListBuilder.create().texOffs(0, 102).addBox(-5.0F, -7.0F, -12.0F, 10.0F, 14.0F, 12.0F, size), PartPose.offset(0.0F, 0.0F, -17.0F)
      );
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 79).addBox(-4.5F, -6.0F, -8.0F, 9.0F, 12.0F, 9.0F, size), PartPose.offset(0.0F, 0.0F, -17.0F)
      );
      head.addOrReplaceChild(
         "snoot_top", CubeListBuilder.create().texOffs(37, 79).addBox(-3.0F, 1.0F, -18.0F, 6.0F, 3.0F, 3.0F, size), PartPose.offset(0.0F, 0.0F, 7.0F)
      );
      head.addOrReplaceChild(
         "snoot_bottom", CubeListBuilder.create().texOffs(37, 85).addBox(-2.5F, 3.0F, -17.0F, 5.0F, 2.0F, 2.0F, size), PartPose.offset(0.0F, 0.0F, 7.0F)
      );
      head.addOrReplaceChild(
         "horn_top_right",
         CubeListBuilder.create().texOffs(36, 89).mirror().addBox(-9.5F, -10.0F, -12.0F, 2.0F, 5.0F, 2.0F, size),
         PartPose.offset(0.0F, 0.0F, 6.0F)
      );
      head.addOrReplaceChild(
         "horn_base_right",
         CubeListBuilder.create().texOffs(36, 96).mirror().addBox(-9.5F, -5.0F, -12.0F, 5.0F, 2.0F, 2.0F, size),
         PartPose.offset(0.0F, 0.0F, 6.0F)
      );
      head.addOrReplaceChild(
         "horn_top_left", CubeListBuilder.create().texOffs(36, 89).addBox(7.5F, -10.0F, -12.0F, 2.0F, 5.0F, 2.0F, size), PartPose.offset(0.0F, 0.0F, 6.0F)
      );
      head.addOrReplaceChild(
         "horn_base_left", CubeListBuilder.create().texOffs(36, 96).addBox(4.5F, -5.0F, -12.0F, 5.0F, 2.0F, 2.0F, size), PartPose.offset(0.0F, 0.0F, 6.0F)
      );
      root.addOrReplaceChild(
         "body",
         CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -19.0F, -7.0F, 18.0F, 23.0F, 19.0F, size),
         PartPose.offsetAndRotation(-3.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "body_back",
         CubeListBuilder.create().texOffs(0, 42).addBox(0.0F, -8.9421F, -1.0188F, 15.0F, 20.0F, 17.0F, size),
         PartPose.offsetAndRotation(-8.0F, 11.0F, 7.2F, 1.4573F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "tail",
         CubeListBuilder.create().texOffs(0, 100).addBox(-1.5F, -0.2071F, -0.9783F, 3.0F, 12.0F, 2.0F, size),
         PartPose.offsetAndRotation(-0.5F, 0.0F, 19.3F, 0.2086F, 0.0F, 0.0F)
      );
      PartDefinition rightLeg = root.addOrReplaceChild(
         "right_front_leg",
         CubeListBuilder.create().texOffs(75, 0).mirror().addBox(-2.0F, 2.0F, -2.4F, 5.0F, 14.0F, 5.0F, size),
         PartPose.offset(-8.0F, 8.0F, -12.1F)
      );
      rightLeg.addOrReplaceChild(
         "right_shoulder",
         CubeListBuilder.create().texOffs(55, 3).mirror().addBox(0.0F, -4.0F, -13.0F, 2.0F, 8.0F, 8.0F, size),
         PartPose.offset(-3.0F, 0.0F, 9.0F)
      );
      PartDefinition leftLeg = root.addOrReplaceChild(
         "left_front_leg", CubeListBuilder.create().texOffs(75, 0).addBox(-3.0F, 2.0F, -2.4F, 5.0F, 14.0F, 5.0F, size), PartPose.offset(8.0F, 8.0F, -12.1F)
      );
      leftLeg.addOrReplaceChild(
         "left_shoulder", CubeListBuilder.create().texOffs(55, 3).addBox(-2.0F, -4.0F, -13.0F, 2.0F, 8.0F, 8.0F, size), PartPose.offset(3.0F, 0.0F, 9.0F)
      );
      root.addOrReplaceChild(
         "right_hind_leg",
         CubeListBuilder.create().texOffs(47, 42).mirror().addBox(-2.7F, 0.0F, -2.0F, 5.0F, 12.0F, 5.0F, size),
         PartPose.offset(-5.0F, 12.0F, 13.0F)
      );
      root.addOrReplaceChild(
         "left_hind_leg", CubeListBuilder.create().texOffs(47, 42).addBox(-2.3F, 0.0F, -3.0F, 5.0F, 12.0F, 5.0F, size), PartPose.offset(4.0F, 12.0F, 14.0F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of(this.body, this.bodyBack, this.leftFrontLeg, this.rightFrontLeg, this.leftHindLeg, this.rightHindLeg, this.tail);
   }

   public Iterable<ModelPart> headParts() {
      return List.of(this.head, this.helmet);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.helmet.visible = entityIn.hasCustomName() && "alf".equals(entityIn.getName().getContents());
      this.leftFrontLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.rightHindLeg.xRot = Mth.cos((float)(limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;
      if (entityIn.attackTimer >= 11) {
         this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      }

      this.head.yRot = SkiesClientUtil.lockNum(netHeadYaw * (float) (Math.PI / 180.0), 1.0F);
      this.helmet.xRot = this.head.xRot;
      this.helmet.yRot = this.head.yRot;
      this.tail.zRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
   }

   public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTicks) {
      if (entityIn.attackTimer < 11) {
         float i = entityIn.attackTimer + partialTicks;
         this.head.xRot = (float)(
            entityIn.attackTimer > 8 ? Math.min(entityIn.getXRot() * (float) (Math.PI / 180.0), Math.sin(i * 0.58F)) : Math.sin(i * 0.58F)
         );
      }
   }
}
