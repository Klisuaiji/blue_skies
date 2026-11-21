package com.legacy.blue_skies.client.models.entities.hostile;

import com.google.common.collect.ImmutableList;
import com.legacy.blue_skies.entities.hostile.ArmoredFrostSpiritEntity;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class ArmoredFrostSpiritModel<T extends ArmoredFrostSpiritEntity> extends HumanoidModel<T> {
   protected final ModelPart spirit;

   public ArmoredFrostSpiritModel(ModelPart model) {
      super(model, RenderType::entityTranslucentCull);
      this.spirit = model.getChild("spirit");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild(
         "spirit", CubeListBuilder.create().texOffs(33, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, size), PartPose.offset(0.0F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(mesh, 64, 64);
   }

   public Iterable<ModelPart> headParts() {
      return ImmutableList.of(this.spirit, this.head);
   }

   public Iterable<ModelPart> bodyParts() {
      return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
   }

   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      if (entity instanceof ArmoredFrostSpiritEntity) {
         this.spirit.copyFrom(this.head);
      }

      AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, entity.isAggressive(), this.attackTime, ageInTicks);
   }
}
