package com.legacy.blue_skies.client.renders.entities.util;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.Mob;

public abstract class TranslucentMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {
   public TranslucentMobRenderer(Context context, M entityModelIn, float shadowSizeIn) {
      super(context, entityModelIn, shadowSizeIn);
   }

   protected boolean shouldCull() {
      return true;
   }
}
