package com.legacy.blue_skies.client.renders.entities.hostile;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.InfestedSwarmerModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.entities.hostile.InfestedSwarmerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfestedSwarmerRenderer<T extends InfestedSwarmerEntity> extends MobRenderer<T, InfestedSwarmerModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/infested_swarmer/infested_swarmer.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/infested_swarmer/infested_swarmer_glow.png");

   public InfestedSwarmerRenderer(Context context) {
      super(context, new InfestedSwarmerModel(context.bakeLayer(SkiesRenderRefs.INFESTED_SWARMER)), 0.8F);
      this.addLayer(new EmissiveRenderLayer(this, new InfestedSwarmerModel(context.bakeLayer(SkiesRenderRefs.INFESTED_SWARMER_GLOW)), GLOW));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.4F, 1.4F, 1.4F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
