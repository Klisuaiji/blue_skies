package com.legacy.blue_skies.client.renders.entities.passive;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.passive.StardustRamModel;
import com.legacy.blue_skies.client.models.entities.passive.StardustWoolModel;
import com.legacy.blue_skies.client.renders.entities.layers.StardustWoolLayer;
import com.legacy.blue_skies.entities.passive.StardustRamEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StardustRamRenderer<T extends StardustRamEntity> extends MobRenderer<T, StardustRamModel<T>> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/stardust_ram/stardust_ram.png");

   public StardustRamRenderer(Context context) {
      super(context, new StardustRamModel(context.bakeLayer(SkiesRenderRefs.STARDUST_RAM)), 1.0F);
      this.addLayer(new StardustWoolLayer(this, new StardustWoolModel(context.bakeLayer(SkiesRenderRefs.STARDUST_RAM_FUR))));
   }

   protected void scale(T entitylivingbaseIn, PoseStack matrixStackIn, float partialTicks) {
      matrixStackIn.scale(1.2F, 1.2F, 1.2F);
   }

   public ResourceLocation getTextureLocation(T entity) {
      return TEXTURE;
   }
}
