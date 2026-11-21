package com.legacy.blue_skies.client.models.entities.passive;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class JellyDrifterModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart cap;
   protected final ModelPart capTop;
   protected final ModelPart tentacleLeft;
   protected final ModelPart tentacleRight;
   protected final ModelPart tentacleBack;
   protected final ModelPart tentacleFront;

   public JellyDrifterModel(ModelPart model) {
      super(RenderType::entityTranslucent);
      this.cap = model.getChild("cap");
      this.capTop = model.getChild("cap_top");
      this.tentacleLeft = model.getChild("tentacle_left");
      this.tentacleRight = model.getChild("tentacle_right");
      this.tentacleBack = model.getChild("tentacle_back");
      this.tentacleFront = model.getChild("tentacle_front");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "cap", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 6.0F, 8.0F, size), PartPose.offset(0.0F, 7.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "cap_top", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F, size), PartPose.offset(0.0F, 7.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "tentacle_left", CubeListBuilder.create().texOffs(20, 8).addBox(0.0F, 0.0F, -3.0F, 0.0F, 11.0F, 6.0F, size), PartPose.offset(3.0F, 13.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "tentacle_right", CubeListBuilder.create().texOffs(20, 8).addBox(0.0F, 0.0F, -3.0F, 0.0F, 11.0F, 6.0F, size), PartPose.offset(-3.0F, 13.0F, 0.0F)
      );
      root.addOrReplaceChild(
         "tentacle_back", CubeListBuilder.create().texOffs(20, 14).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 11.0F, 0.0F, size), PartPose.offset(0.0F, 13.0F, 3.0F)
      );
      root.addOrReplaceChild(
         "tentacle_front", CubeListBuilder.create().texOffs(20, 14).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 11.0F, 0.0F, size), PartPose.offset(0.0F, 13.0F, -3.0F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   public Iterable<ModelPart> parts() {
      return ImmutableList.of(this.tentacleFront, this.tentacleRight, this.capTop, this.tentacleLeft, this.cap, this.tentacleBack);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float tentacleRotPoint = !entityIn.isInWater() ? 23.0F : 13.0F;
      float capRotPoint = !entityIn.isInWater() ? 17.0F : 7.0F;
      if (entityIn.isInWater()) {
         float moveAmount = limbSwingAmount * 0.8F;
         float animAmount = Mth.cos(ageInTicks * 0.07F) * 0.1F;
         this.tentacleLeft.zRot = animAmount + -moveAmount;
         this.tentacleRight.zRot = -animAmount + moveAmount;
         this.tentacleFront.xRot = animAmount + -moveAmount;
         this.tentacleBack.xRot = -animAmount + moveAmount;
      } else {
         float squishAmount = -1.5F;
         this.tentacleLeft.zRot = squishAmount;
         this.tentacleRight.zRot = -squishAmount;
         this.tentacleFront.xRot = squishAmount;
         this.tentacleBack.xRot = -squishAmount;
      }

      this.tentacleLeft.y = tentacleRotPoint;
      this.tentacleRight.y = tentacleRotPoint;
      this.tentacleFront.y = tentacleRotPoint;
      this.tentacleBack.y = tentacleRotPoint;
      this.cap.y = capRotPoint;
      this.capTop.y = capRotPoint;
   }
}
