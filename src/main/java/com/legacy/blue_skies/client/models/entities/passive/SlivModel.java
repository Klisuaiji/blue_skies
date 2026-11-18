package com.legacy.blue_skies.client.models.entities.passive;

import com.legacy.blue_skies.entities.passive.SlivEntity;
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

public class SlivModel<T extends SlivEntity> extends ListModel<T> {
   protected final ModelPart head;
   protected final ModelPart tounge;
   protected final ModelPart bodyFront;
   protected final ModelPart bodyMid;
   protected final ModelPart bodyBack;

   public SlivModel(ModelPart model) {
      this.head = model.getChild("head");
      this.tounge = this.head.getChild("tounge");
      this.bodyFront = model.getChild("body_front");
      this.bodyMid = this.bodyFront.getChild("body_mid");
      this.bodyBack = this.bodyMid.getChild("body_back");
   }

   public static LayerDefinition createBodyLayer(CubeDeformation size) {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild(
         "head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -4.0F, 4.0F, 2.0F, 4.0F, size), PartPose.offset(-0.25F, 23.0F, -7.5F)
      );
      head.addOrReplaceChild(
         "tounge", CubeListBuilder.create().texOffs(8, 0).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 0.0F, 4.0F, size), PartPose.offset(0.0F, 0.0F, -4.0F)
      );
      PartDefinition bodyFront = root.addOrReplaceChild(
         "body_front", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 7.0F, size), PartPose.offset(-0.25F, 23.0F, -7.5F)
      );
      PartDefinition bodyMid = bodyFront.addOrReplaceChild(
         "body_mid", CubeListBuilder.create().texOffs(0, 15).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 7.0F, size), PartPose.offset(0.0F, 0.0F, 7.0F)
      );
      bodyMid.addOrReplaceChild(
         "body_back", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, size), PartPose.offset(0.0F, 0.0F, 7.0F)
      );
      return LayerDefinition.create(mesh, 32, 32);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = ageInTicks - entityIn.tickCount;
      float toungeScale = entityIn.getToungeAnimationScale(f);
      toungeScale *= toungeScale;
      this.tounge.visible = true;
      this.head.xRot = headPitch * (float) (Math.PI / 180.0) / 4.0F;
      this.head.yRot = netHeadYaw / 90.0F;
      this.head.setPos(-0.25F - Mth.cos((float)(limbSwing * 0.8F + Math.PI)) * -0.3F, 23.0F, -7.5F);
      this.bodyFront.setPos(-0.25F - Mth.cos((float)(limbSwing * 0.8F + Math.PI)), 23.0F, -7.5F);
      this.bodyMid.setPos(0.0F + Mth.cos((float)(limbSwing * 0.8F + Math.PI)) * 2.0F, 0.0F, 7.0F);
      this.bodyBack.setPos(0.0F - Mth.cos((float)(limbSwing * 0.8F + Math.PI)) * 2.5F, 0.0F, 7.0F);
      this.tounge.setPos(0.0F, 0.0F, -0.0F);
      this.tounge.z -= 4.0F * toungeScale;
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.head, this.bodyFront);
   }
}
