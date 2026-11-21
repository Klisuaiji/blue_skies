package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.entities.hostile.FrostSpiritEntity;
import java.util.List;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class FrostSpiritModel<T extends FrostSpiritEntity> extends HumanoidModel<T> {
   protected final ModelPart spirit;
   protected final ModelPart rightWing;
   protected final ModelPart leftWing;

   public FrostSpiritModel(ModelPart model) {
      super(model);
      this.spirit = model.getChild("spirit");
      this.rightWing = model.getChild("right_wing");
      this.leftWing = model.getChild("left_wing");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "spirit", CubeListBuilder.create().texOffs(33, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_wing",
         CubeListBuilder.create().texOffs(33, 19).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F, size),
         PartPose.offsetAndRotation(-1.0F, -3.9F, 1.3F, 0.4272566F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_wing",
         CubeListBuilder.create().texOffs(33, 19).mirror().addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F, size),
         PartPose.offsetAndRotation(1.0F, -3.9F, 1.3F, 0.4272566F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> headParts() {
      return List.of(this.rightWing, this.leftWing, this.spirit);
   }

   public Iterable<ModelPart> bodyParts() {
      return List.of();
   }

   public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.rightWing.yRot = ageInTicks;
      this.leftWing.yRot = -ageInTicks;
   }
}
