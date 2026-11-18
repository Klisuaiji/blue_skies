package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.entities.hostile.ShrumptyEntity;
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

public class ShrumptyModel<T extends ShrumptyEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart cap;
   protected final ModelPart tail;
   protected final ModelPart rightLeg;
   protected final ModelPart leftLeg;

   public ShrumptyModel(ModelPart model) {
      this.body = model.getChild("body");
      this.cap = this.body.getChild("cap");
      this.tail = this.body.getChild("tail");
      this.rightLeg = model.getChild("right_leg");
      this.leftLeg = model.getChild("left_leg");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 20).addBox(-6.0F, -4.75F, -6.0F, 12.0F, 5.0F, 12.0F, size), PartPose.offset(0.0F, 9.75F, 0.0F)
      );
      body.addOrReplaceChild(
         "cap", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F, size), PartPose.offset(0.0F, -4.75F, 0.0F)
      );
      body.addOrReplaceChild(
         "tail", CubeListBuilder.create().texOffs(17, 31).addBox(0.0F, 0.0F, -2.0F, 0.0F, 12.0F, 7.0F, size), PartPose.offset(0.0F, -0.75F, 6.0F)
      );
      root.addOrReplaceChild(
         "right_leg", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, size), PartPose.offset(-4.0F, 10.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, size), PartPose.offset(4.0F, 10.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 96, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.body, this.rightLeg, this.leftLeg);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entityIn.shouldSpreadLegs) {
         this.rightLeg.setPos(-2.0F, 10.0F, 0.0F);
         this.leftLeg.setPos(2.0F, 10.0F, 0.0F);
      } else {
         this.leftLeg.setPos(4.0F, 10.0F, 0.0F);
         this.rightLeg.setPos(-4.0F, 10.0F, 0.0F);
      }

      this.body.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.body.xRot = SkiesClientUtil.lockNum(headPitch * (float) (Math.PI / 180.0), 0.15F);
      this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
      this.tail.xRot = 0.1F + limbSwingAmount * 0.8F;
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
