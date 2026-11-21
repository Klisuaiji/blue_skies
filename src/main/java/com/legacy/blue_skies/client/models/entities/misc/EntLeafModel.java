package com.legacy.blue_skies.client.models.entities.misc;

import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.entity.Entity;

public class EntLeafModel<T extends Entity> extends ListModel<T> {
   protected final ModelPart leaf;

   public EntLeafModel(ModelPart model) {
      this.leaf = model.getChild("leaf");
   }

   public static LayerDefinition createLeafLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      mesh.getRoot()
         .addOrReplaceChild(
            "leaf", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 0.0F, 6.0F, size), PartPose.offset(0.0F, 24.0F, 0.0F)
         );
      return LayerDefinition.create(mesh, 32, 16);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.leaf);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
