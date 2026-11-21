package com.legacy.blue_skies.client.models.entities.passive;

import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class SnowOwlModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart head;
   protected final ModelPart rightBrow;
   protected final ModelPart leftBrow;
   protected final ModelPart body;
   protected final ModelPart tailFeather;
   protected final ModelPart rightWing;
   protected final ModelPart leftWing;

   public SnowOwlModel(ModelPart model) {
      this.head = model.getChild("head");
      this.rightBrow = this.head.getChild("right_brow");
      this.leftBrow = this.head.getChild("left_brow");
      this.body = model.getChild("body");
      this.tailFeather = this.body.getChild("tail_feather");
      this.rightWing = model.getChild("right_wing");
      this.leftWing = model.getChild("left_wing");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F), PartPose.offset(0.0F, 19.0F, 0.0F)
      );
      head.addOrReplaceChild(
         "right_brow", CubeListBuilder.create().texOffs(13, 0).mirror().addBox(-2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F), PartPose.offset(-1.0F, -4.0F, -2.0F)
      );
      head.addOrReplaceChild(
         "left_brow", CubeListBuilder.create().texOffs(13, 0).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F), PartPose.offset(1.0F, -4.0F, -2.0F)
      );
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F), PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "tail_feather",
         CubeListBuilder.create().texOffs(0, 26).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 1.0F, 4.0F),
         PartPose.offsetAndRotation(0.0F, -1.0F, 2.0F, -0.1745F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "right_wing",
         CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -1.0F, -2.0F, 1.0F, 5.0F, 4.0F),
         PartPose.offsetAndRotation(-2.0F, 20.0F, 0.0F, 0.1745F, 0.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "left_wing",
         CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, -1.0F, -2.0F, 1.0F, 5.0F, 4.0F),
         PartPose.offsetAndRotation(2.0F, 20.0F, 0.0F, 0.1745F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.head, this.body, this.rightWing, this.leftWing);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.head.xRot = headPitch * (float) (Math.PI / 180.0);
      this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
      this.rightWing.zRot = ageInTicks;
      this.leftWing.zRot = -ageInTicks;
      this.tailFeather.xRot = !entityIn.onGround() ? -1.0472F : -0.1745F;
   }
}
