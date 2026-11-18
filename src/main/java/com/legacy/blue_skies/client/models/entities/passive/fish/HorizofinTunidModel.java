package com.legacy.blue_skies.client.models.entities.passive.fish;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class HorizofinTunidModel<T extends LivingEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart backFin;
   protected final ModelPart topFin;
   protected final ModelPart bottomFin;
   protected final ModelPart rightFin;
   protected final ModelPart leftFin;

   public HorizofinTunidModel(ModelPart model) {
      this.body = model.getChild("body");
      this.backFin = this.body.getChild("back_fin");
      this.leftFin = this.body.getChild("left_fin");
      this.rightFin = this.body.getChild("right_fin");
      this.topFin = this.body.getChild("top_fin");
      this.bottomFin = this.body.getChild("bottom_fin");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -8.0F, 2.0F, 4.0F, 14.0F), PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "back_fin", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F), PartPose.offset(-0.5F, -2.0F, 6.0F)
      );
      body.addOrReplaceChild(
         "left_fin", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 3.0F), PartPose.offset(0.5F, -1.5F, -5.0F)
      );
      body.addOrReplaceChild(
         "right_fin", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 3.0F), PartPose.offset(-1.5F, -1.5F, -5.0F)
      );
      body.addOrReplaceChild(
         "top_fin", CubeListBuilder.create().texOffs(19, 0).addBox(0.0F, -2.0F, -1.5F, 0.0F, 2.0F, 3.0F), PartPose.offset(-0.5F, -4.0F, 4.5F)
      );
      body.addOrReplaceChild(
         "bottom_fin", CubeListBuilder.create().texOffs(19, 3).addBox(0.0F, 0.0F, -1.5F, 0.0F, 2.0F, 3.0F), PartPose.offset(-0.5F, 0.0F, 4.5F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   public Iterable<ModelPart> parts() {
      return ImmutableList.of(this.body);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = 1.0F;
      if (!entity.isInWater()) {
         f = 1.5F;
      }

      float backFinRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
      float finRot = -f * 0.15F * Mth.sin(0.2F * ageInTicks);
      this.backFin.yRot = backFinRot;
      this.leftFin.yRot = 0.3F + finRot;
      this.rightFin.yRot = -0.3F + -finRot;
   }
}
