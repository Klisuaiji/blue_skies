package com.legacy.blue_skies.client.models.entities.passive.fish;

import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class GrittleFlatfishModel<T extends LivingEntity> extends ListModel<T> {
   private final ModelPart body;
   private final ModelPart backFin;
   private final ModelPart leftFin;
   private final ModelPart rightFin;
   private final ModelPart topLeftFin;
   private final ModelPart topRightFin;

   public GrittleFlatfishModel(ModelPart model) {
      this.body = model.getChild("body");
      this.backFin = this.body.getChild("back_fin");
      this.leftFin = this.body.getChild("left_fin");
      this.rightFin = this.body.getChild("right_fin");
      this.topLeftFin = model.getChild("top_left_fin");
      this.topRightFin = model.getChild("top_right_fin");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.5F, -7.875F, 10.0F, 4.0F, 15.0F), PartPose.offset(0.0F, 23.5F, -0.125F)
      );
      body.addOrReplaceChild(
         "back_fin", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 0.0F, 7.0F), PartPose.offset(0.0F, -1.5F, 7.125F)
      );
      body.addOrReplaceChild(
         "left_fin", CubeListBuilder.create().texOffs(0, 23).addBox(0.0F, 0.0F, -4.0F, 4.0F, 0.0F, 8.0F), PartPose.offset(5.0F, -1.5F, -2.375F)
      );
      body.addOrReplaceChild(
         "right_fin", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-4.0F, 0.0F, -4.0F, 4.0F, 0.0F, 8.0F), PartPose.offset(-5.0F, -1.5F, -2.375F)
      );
      root.addOrReplaceChild(
         "top_left_fin", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, -2.0F, -4.0F, 0.0F, 2.0F, 8.0F), PartPose.offset(3.0F, 20.0F, 2.0F)
      );
      root.addOrReplaceChild(
         "top_right_fin", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(0.0F, -2.0F, -4.0F, 0.0F, 2.0F, 8.0F), PartPose.offset(-3.0F, 20.0F, 2.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.body, this.topLeftFin, this.topRightFin);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = 1.0F;
      if (!entityIn.isInWater()) {
         f = 1.5F;
      }

      this.backFin.yRot = 0.0F;
      float finRot = -f * 0.25F * Mth.sin(0.4F * ageInTicks);
      float topFinRot = -f * 0.15F * Mth.sin(0.2F * ageInTicks);
      this.backFin.xRot = finRot;
      this.leftFin.zRot = finRot;
      this.rightFin.zRot = -finRot;
      this.topLeftFin.zRot = -topFinRot;
      this.topRightFin.zRot = topFinRot;
   }
}
