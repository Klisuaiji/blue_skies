package com.legacy.blue_skies.client.models.entities.passive;

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
import net.minecraft.world.entity.Entity;

public class FireflyModel<T extends Entity> extends ListModel<T> {
   private final ModelPart body;
   private final ModelPart rightLegs;
   private final ModelPart leftLegs;
   private final ModelPart rightAntena;
   private final ModelPart leftAntena;
   private final ModelPart rightShell;
   private final ModelPart leftShell;
   private final ModelPart rightWing;
   private final ModelPart leftWing;

   public FireflyModel(ModelPart model) {
      this.body = model.getChild("body");
      this.rightLegs = this.body.getChild("right_legs");
      this.leftLegs = this.body.getChild("left_legs");
      this.rightAntena = this.body.getChild("right_antena");
      this.leftAntena = this.body.getChild("left_antena");
      this.rightShell = this.body.getChild("right_shell");
      this.leftShell = this.body.getChild("left_shell");
      this.rightWing = this.body.getChild("right_wing");
      this.leftWing = this.body.getChild("left_wing");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 6.0F, 4.0F, 10.0F), PartPose.offset(-2.0F, 19.0F, -4.0F)
      );
      body.addOrReplaceChild(
         "right_legs", CubeListBuilder.create().texOffs(0, 7).mirror().addBox(0.0F, 0.0F, -4.0F, 0.0F, 2.0F, 8.0F), PartPose.offset(0.0F, 3.0F, 5.0F)
      );
      body.addOrReplaceChild("left_legs", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -4.0F, 0.0F, 2.0F, 8.0F), PartPose.offset(4.0F, 3.0F, 5.0F));
      body.addOrReplaceChild(
         "right_antena",
         CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -4.0F, 1.0F, 0.0F, 4.0F),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.6981F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "left_antena",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -4.0F, 1.0F, 0.0F, 4.0F),
         PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -0.6981F, 0.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "right_shell",
         CubeListBuilder.create().texOffs(0, 18).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.025F)),
         PartPose.offsetAndRotation(2.0F, -1.0F, 3.0F, 0.6109F, -0.6981F, 0.0F)
      );
      body.addOrReplaceChild(
         "left_shell",
         CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.025F)),
         PartPose.offsetAndRotation(2.0F, -1.0F, 3.0F, 0.6109F, 0.6981F, 0.0F)
      );
      body.addOrReplaceChild(
         "right_wing",
         CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-4.0F, 0.0F, -3.0F, 4.0F, 0.0F, 6.0F),
         PartPose.offsetAndRotation(1.0F, -1.0F, 7.0F, 0.0F, 0.0F, 0.3491F)
      );
      body.addOrReplaceChild(
         "left_wing",
         CubeListBuilder.create().texOffs(0, 28).addBox(0.0F, 0.0F, -3.0F, 4.0F, 0.0F, 6.0F),
         PartPose.offsetAndRotation(3.0F, -1.0F, 7.0F, 0.0F, 0.0F, -0.3491F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.body);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float rot = Mth.cos(ageInTicks * 0.18F);
      float wingRot = 0.2F + Mth.cos(ageInTicks * 2.1F) * (float) Math.PI * 0.15F;
      this.rightWing.zRot = wingRot;
      this.leftWing.zRot = -wingRot;
      this.body.y = 19.0F - rot * 1.0F;
      this.rightAntena.xRot = -0.6981F + Mth.cos(2.0F + ageInTicks * 0.18F) * (float) Math.PI * 0.05F;
      this.leftAntena.xRot = -0.6981F + Mth.cos(ageInTicks * 0.18F) * (float) Math.PI * 0.05F;
      this.rightLegs.zRot = -Mth.cos(ageInTicks * 0.18F) * (float) Math.PI * 0.05F;
      this.leftLegs.zRot = Mth.cos(ageInTicks * 0.18F) * (float) Math.PI * 0.05F;
      this.setRotationAngle(this.rightShell, 0.6109F, -0.6981F, 0.0F);
      this.setRotationAngle(this.leftShell, 0.6109F, 0.6981F, 0.0F);
      this.rightShell.xRot += rot * 0.05F;
      this.leftShell.xRot += rot * 0.05F;
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
