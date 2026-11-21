package com.legacy.blue_skies.client.renders.entities.hostile.boss.summons;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.init.SkiesRenderRefs;
import com.legacy.blue_skies.client.models.entities.hostile.ArtificialGolemModel;
import com.legacy.blue_skies.client.renders.entities.layers.EmissiveRenderLayer;
import com.legacy.blue_skies.client.renders.entities.util.IMultiGlowRenderer;
import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArtificialGolemRenderer<T extends ArtificialGolemEntity> extends MobRenderer<T, ArtificialGolemModel<T>> implements IMultiGlowRenderer<T> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/entity/artificial_golem/artificial_golem.png");
   private static final ResourceLocation TEXTURE_RED = BlueSkies.locate("textures/entity/artificial_golem/artificial_golem_red.png");
   private static final ResourceLocation TEXTURE_RANGED = BlueSkies.locate("textures/entity/artificial_golem/artificial_golem_ranged.png");
   private static final ResourceLocation GLOW = BlueSkies.locate("textures/entity/artificial_golem/artificial_glow.png");
   private static final ResourceLocation GLOW_RED = BlueSkies.locate("textures/entity/artificial_golem/artificial_glow_red.png");
   private static final ResourceLocation GLOW_RANGED = BlueSkies.locate("textures/entity/artificial_golem/artificial_glow_ranged.png");

   public ArtificialGolemRenderer(Context context) {
      super(context, new ArtificialGolemModel(context.bakeLayer(SkiesRenderRefs.ARTIFICIAL_GOLEM)), 0.5F);
      this.addLayer(new EmissiveRenderLayer(this));
   }

   protected boolean isShaking(T entity) {
      return entity.hurtTime > 0;
   }

   public ResourceLocation getTextureLocation(T entity) {
      return entity.isEnraged() && !entity.isRanged() ? TEXTURE_RED : (entity.isRanged() ? TEXTURE_RANGED : TEXTURE);
   }

   public ResourceLocation getGlowTextures(T entity) {
      return entity.isEnraged() && !entity.isRanged() ? GLOW_RED : (entity.isRanged() ? GLOW_RANGED : GLOW);
   }
}
