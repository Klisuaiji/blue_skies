package com.legacy.blue_skies.client.renders.entities.misc;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.misc.pet.PetArtificialGolemModel;
import com.legacy.blue_skies.client.models.entities.misc.pet.PetNyctoflyModel;
import com.legacy.blue_skies.client.models.entities.misc.pet.PetStoneletModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.client.renders.entities.util.IMultiGlowRenderer;
import com.legacy.blue_skies.client.renders.entities.util.IMultiModelRenderer;
import com.legacy.blue_skies.entities.SupporterPetEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SupporterPetRenderer<T extends SupporterPetEntity> extends MobRenderer<T, EntityModel<T>> implements IMultiGlowRenderer<T>, IMultiModelRenderer<T> {
   private static final ResourceLocation BABY_NYCTOFLY_TEXTURE = BlueSkies.locate("textures/entity/pets/baby_nyctofly.png");
   private static final ResourceLocation PEBBLET_TEXTURE = BlueSkies.locate("textures/entity/pets/pebblet.png");
   private static final ResourceLocation BABY_ARTIFICIAL_GOLEM_TEXTURE = BlueSkies.locate("textures/entity/pets/baby_artificial_golem.png");
   private static final ResourceLocation BABY_NYCTOFLY_GLOW = BlueSkies.locate("textures/entity/pets/baby_nyctofly_glow.png");
   private static final ResourceLocation PEBBLET_GLOW = BlueSkies.locate("textures/entity/pets/pebblet_glow.png");
   private static final ResourceLocation BABY_ARTIFICIAL_GOLEM_GLOW = BlueSkies.locate("textures/entity/pets/baby_artificial_golem_glow.png");
   private final EntityModel<T> nyctoflyModel;
   private final EntityModel<T> stoneletModel;
   private final EntityModel<T> artificialGolemModel;

   public SupporterPetRenderer(Context context) {
      super(context, new PetNyctoflyModel(context.bakeLayer(SkiesRenderRefs.BABY_NYCTOFLY_SUPPORTER_PET)), 0.3F);
      this.nyctoflyModel = new PetNyctoflyModel(context.bakeLayer(SkiesRenderRefs.BABY_NYCTOFLY_SUPPORTER_PET));
      this.stoneletModel = new PetStoneletModel(context.bakeLayer(SkiesRenderRefs.PEBBLET_SUPPORTER_PET));
      this.artificialGolemModel = new PetArtificialGolemModel(context.bakeLayer(SkiesRenderRefs.BABY_ARTIFICIAL_GOLEM_SUPPORTER_PET));
      this.addLayer(new EmissiveRenderLayer(this));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      if (entityIn.getTypeID() < 1) {
         matrixStackIn.translate(0.0, -0.2, 0.0);
      }

      this.model = this.getModelForType(entityIn.getTypeID());
   }

   public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
      this.model = this.getModelForType(entityIn.getTypeID());
   }

   public EntityModel<T> getModel() {
      return this.model;
   }

   private EntityModel<T> getModelForType(byte typeIn) {
      switch (typeIn) {
         case 1:
            return this.stoneletModel;
         case 2:
            return this.artificialGolemModel;
         default:
            return this.nyctoflyModel;
      }
   }

   public ResourceLocation getTextureLocation(T entityIn) {
      switch (entityIn.getTypeID()) {
         case 1:
            return PEBBLET_TEXTURE;
         case 2:
            return BABY_ARTIFICIAL_GOLEM_TEXTURE;
         default:
            return BABY_NYCTOFLY_TEXTURE;
      }
   }

   public ResourceLocation getGlowTextures(T entityIn) {
      switch (entityIn.getTypeID()) {
         case 1:
            return PEBBLET_GLOW;
         case 2:
            return BABY_ARTIFICIAL_GOLEM_GLOW;
         default:
            return BABY_NYCTOFLY_GLOW;
      }
   }

   public EntityModel<T> getGlowModels(T entityIn) {
      return this.getModelForType(entityIn.getTypeID());
   }
}
