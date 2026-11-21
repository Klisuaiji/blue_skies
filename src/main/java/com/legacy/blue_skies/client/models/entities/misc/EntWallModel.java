package com.legacy.blue_skies.client.models.entities.misc;

import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class EntWallModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart wall;
   protected final ModelPart wallDecor;

   public EntWallModel(ModelPart model) {
      this.wall = model.getChild("wall");
      this.wallDecor = this.wall.getChild("wall_decor");
   }

   public static LayerDefinition createWallLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition wall = root.addOrReplaceChild(
         "wall", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -48.0F, -4.0F, 48.0F, 48.0F, 8.0F), PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      wall.addOrReplaceChild(
         "wall_decor",
         CubeListBuilder.create().texOffs(0, 56).addBox(-19.0F, -48.0F, -4.0F, 48.0F, 48.0F, 8.0F, new CubeDeformation(0.5F)),
         PartPose.offset(-5.0F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 128, 128);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.wall);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }
}
