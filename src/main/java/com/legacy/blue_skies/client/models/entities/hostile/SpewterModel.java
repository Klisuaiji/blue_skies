package com.legacy.blue_skies.client.models.entities.hostile;

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

public class SpewterModel<T extends LivingEntity> extends ListModel<T> {
   protected final ModelPart body;
   protected final ModelPart top;
   protected final ModelPart eastLeaf;
   protected final ModelPart northLeaf;
   protected final ModelPart westLeaf;
   protected final ModelPart southLeaf;
   protected final ModelPart bottomLeaves;

   public SpewterModel(ModelPart model) {
      this.body = model.getChild("body");
      this.bottomLeaves = this.body.getChild("bottom_leaves");
      this.top = this.body.getChild("top");
      this.northLeaf = this.top.getChild("north_leaf");
      this.eastLeaf = this.top.getChild("east_leaf");
      this.southLeaf = this.top.getChild("south_leaf");
      this.westLeaf = this.top.getChild("west_leaf");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild(
         "body", CubeListBuilder.create().texOffs(0, 18).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 14.0F), PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      body.addOrReplaceChild(
         "bottom_leaves", CubeListBuilder.create().texOffs(0, 72).addBox(-10.0F, -0.1F, -10.0F, 20.0F, 0.0F, 20.0F), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      PartDefinition top = body.addOrReplaceChild(
         "top", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 5.0F, 12.0F), PartPose.offset(0.0F, -10.0F, 0.0F)
      );
      top.addOrReplaceChild(
         "north_leaf",
         CubeListBuilder.create().texOffs(42, 0).addBox(-7.0F, 0.0F, -13.0F, 14.0F, 0.0F, 13.0F),
         PartPose.offsetAndRotation(0.0F, -4.0F, -6.0F, 0.8727F, 0.0F, 0.0F)
      );
      top.addOrReplaceChild(
         "east_leaf",
         CubeListBuilder.create().texOffs(0, 57).addBox(0.0F, 0.0F, -7.0F, 13.0F, 0.0F, 14.0F),
         PartPose.offsetAndRotation(6.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.8727F)
      );
      top.addOrReplaceChild(
         "south_leaf",
         CubeListBuilder.create().texOffs(0, 43).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 0.0F, 13.0F),
         PartPose.offsetAndRotation(0.0F, -4.0F, 6.0F, -0.8727F, 0.0F, 0.0F)
      );
      top.addOrReplaceChild(
         "west_leaf",
         CubeListBuilder.create().texOffs(42, 14).addBox(-13.0F, 0.0F, -7.0F, 13.0F, 0.0F, 14.0F),
         PartPose.offsetAndRotation(-6.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.8727F)
      );
      return LayerDefinition.create(mesh, 96, 96);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.body);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float speed = entityIn.hurtTime > 0 ? 1.0F : 0.0F;
      float extraLength = entityIn.hurtTime > 0 ? 1.0F : 0.0F;
      float extraLengthPedals = entityIn.hurtTime > 0 ? 0.3F : 0.0F;
      float animAmount = Mth.cos(ageInTicks * (0.1F + speed)) * (0.1F + extraLengthPedals);
      this.top.setPos(0.0F, -9.5F + Mth.cos(ageInTicks * (0.1F + speed)) * (0.4F + extraLength), 0.0F);
      this.northLeaf.xRot = 0.8727F - animAmount;
      this.southLeaf.xRot = -0.8727F + animAmount;
      this.eastLeaf.zRot = 0.8727F - animAmount;
      this.westLeaf.zRot = -0.8727F + animAmount;
   }
}
