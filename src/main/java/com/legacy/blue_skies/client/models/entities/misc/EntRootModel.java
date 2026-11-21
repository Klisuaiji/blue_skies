package com.legacy.blue_skies.client.models.entities.misc;

import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import java.util.List;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class EntRootModel<T extends EntRootEntity> extends ListModel<T> {
   protected final ModelPart rootBottom;
   protected final ModelPart rootBottom2;
   protected final ModelPart rootTop;
   protected final ModelPart rootTop2;
   protected final ModelPart largeRootBottom;
   protected final ModelPart largeRootBottom2;
   protected final ModelPart largeRootMiddle;
   protected final ModelPart largeRootMiddle2;
   protected final ModelPart largeRootTop;
   protected final ModelPart largeRootTop2;

   public EntRootModel(ModelPart model) {
      this.rootBottom = model.getChild("root_bottom");
      this.rootBottom2 = this.rootBottom.getChild("root_bottom_2");
      this.rootTop = this.rootBottom.getChild("root_top");
      this.rootTop2 = this.rootTop.getChild("root_top_2");
      this.largeRootBottom = model.getChild("large_root_bottom");
      this.largeRootBottom2 = this.largeRootBottom.getChild("large_root_bottom_2");
      this.largeRootMiddle = this.largeRootBottom.getChild("large_root_middle");
      this.largeRootMiddle2 = this.largeRootMiddle.getChild("large_root_middle_2");
      this.largeRootTop = this.largeRootMiddle.getChild("large_root_top");
      this.largeRootTop2 = this.largeRootTop.getChild("large_root_top_2");
   }

   public static LayerDefinition createRootLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      CubeListBuilder bottom = CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -7.0F, -3.5F, 0.0F, 7.0F, 7.0F);
      PartDefinition rootBottom = root.addOrReplaceChild("root_bottom", bottom, PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
      rootBottom.addOrReplaceChild("root_bottom_2", bottom, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
      CubeListBuilder top = CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -7.0F, -3.5F, 0.0F, 7.0F, 7.0F);
      PartDefinition rootTop = rootBottom.addOrReplaceChild("root_top", top, PartPose.offset(0.0F, -7.0F, 0.0F));
      rootTop.addOrReplaceChild("root_top_2", top, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
      CubeListBuilder largeBottom = CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -7.0F, -4.5F, 0.0F, 7.0F, 9.0F);
      PartDefinition largeRootBottom = root.addOrReplaceChild(
         "large_root_bottom", largeBottom, PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F)
      );
      largeRootBottom.addOrReplaceChild("large_root_bottom_2", largeBottom, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
      CubeListBuilder largeMiddle = CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, -7.0F, -4.5F, 0.0F, 7.0F, 9.0F);
      PartDefinition largeRootMiddle = largeRootBottom.addOrReplaceChild("large_root_middle", largeMiddle, PartPose.offset(0.0F, -7.0F, 0.0F));
      largeRootMiddle.addOrReplaceChild("large_root_middle_2", largeMiddle, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
      CubeListBuilder largeTop = CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -7.0F, -4.5F, 0.0F, 7.0F, 9.0F);
      PartDefinition largeRootTop = largeRootMiddle.addOrReplaceChild("large_root_top", largeTop, PartPose.offset(0.0F, -7.0F, 0.0F));
      largeRootTop.addOrReplaceChild("large_root_top_2", largeTop, PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
      return LayerDefinition.create(mesh, 32, 32);
   }

   public Iterable<ModelPart> parts() {
      return List.of(this.rootBottom, this.largeRootBottom);
   }

   public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.rootBottom.visible = !entityIn.isLarge();
      this.largeRootBottom.visible = entityIn.isLarge();
      if (entityIn.isLarge()) {
         float anim = Mth.cos((ageInTicks + entityIn.tickCount) * 0.2F) * 0.15F;
         this.largeRootBottom.xRot = anim;
         this.largeRootMiddle.xRot = anim;
         this.largeRootTop.xRot = anim;
         EntRootEntity root = entityIn;
         if (root.lifeTicks <= 3) {
            this.largeRootBottom.xRot *= 0.2F;
            this.largeRootMiddle.xRot *= 0.2F;
            this.largeRootTop.xRot *= 0.2F;
         }

         this.largeRootBottom.yRot = 0.0F;
         this.largeRootBottom.zRot = 0.0F;
      } else {
         float anim = Mth.cos((ageInTicks + entityIn.tickCount) * 0.5F) * 0.2F;
         this.rootBottom.xRot = anim;
         this.rootTop.xRot = anim;
         EntRootEntity root = entityIn;
         if (root.lifeTicks <= 3) {
            this.rootBottom.xRot *= 0.2F;
            this.rootTop.xRot *= 0.2F;
         }

         this.rootBottom.yRot = 0.0F;
         this.rootBottom.zRot = 0.0F;
      }
   }

   public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
      modelRenderer.xRot = x;
      modelRenderer.yRot = y;
      modelRenderer.zRot = z;
   }
}
