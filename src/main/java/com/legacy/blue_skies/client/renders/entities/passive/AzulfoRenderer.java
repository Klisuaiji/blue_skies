package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.AzulfoModel;
import com.legacy.blue_skies.client.renders.entities.layers.AzulfoAlfLayer;
import com.legacy.blue_skies.entities.passive.AzulfoEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AzulfoRenderer<T extends AzulfoEntity> extends MobRenderer<T, AzulfoModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/azulfo/azulfo.png");

   public AzulfoRenderer(Context context) {
      super(context, new AzulfoModel(context.bakeLayer(SkiesRenderRefs.AZULFO)), 1.5F);
      this.addLayer(new AzulfoAlfLayer(this, new AzulfoModel<>(context.bakeLayer(SkiesRenderRefs.AZULFO))));
   }

   protected void scale(T entityIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
