package com.legacy.blue_skies.client.models.entities.hostile;

import com.legacy.blue_skies.entities.hostile.SeclamEntity;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SeclamModel<T extends SeclamEntity> extends ListModel<T> {
   private final ModelPart bottomShell;
   private final ModelPart pearl;
   private final ModelPart topShell;
   private final ModelPart hinge;

   public SeclamModel(ModelPart model) {
      this.bottomShell = model.getChild("bottom_shell");
      this.pearl = this.bottomShell.getChild("pearl");
      this.topShell = model.getChild("top_shell");
      this.hinge = model.getChild("hinge");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition bottomShell = root.addOrReplaceChild(
         "bottom_shell", CubeListBuilder.create().texOffs(0, 23).addBox(-11.5F, 0.0F, -17.0F, 23.0F, 5.0F, 17.0F, size), PartPose.offset(0.0F, 19.0F, 7.0F)
      );
      bottomShell.addOrReplaceChild(
         "pearl", CubeListBuilder.create().texOffs(0, 52).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 5.0F, 5.0F, size), PartPose.offset(0.0F, 0.0F, -9.5F)
      );
      root.addOrReplaceChild(
         "top_shell",
         CubeListBuilder.create().texOffs(1, 0).mirror().addBox(-12.5F, -4.0F, -18.0F, 25.0F, 5.0F, 18.0F, size),
         PartPose.offset(0.0F, 19.0F, 7.0F)
      );
      root.addOrReplaceChild(
         "hinge", CubeListBuilder.create().texOffs(0, 45).addBox(-9.5F, -2.0F, 0.0F, 19.0F, 4.0F, 3.0F, size), PartPose.offset(0.0F, 19.0F, 7.0F)
      );
      return LayerDefinition.create(mesh, 96, 96);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.bottomShell, this.topShell, this.hinge);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.pearl.visible = entity.hasPearl();
      float f = ageInTicks - entity.tickCount;
      float openScale = entity.getOpenAnimationScale(f);
      openScale *= openScale;
      this.topShell.xRot = 0.0F - 2.0F * openScale;
      this.hinge.xRot = this.topShell.xRot * 0.5F;
      this.pearl.setPos(0.0F, 0.01F, -9.5F);
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
