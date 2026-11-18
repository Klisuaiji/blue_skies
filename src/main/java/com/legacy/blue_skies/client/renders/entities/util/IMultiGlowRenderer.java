package com.legacy.blue_skies.client.renders.entities.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface IMultiGlowRenderer<T extends LivingEntity> {
   ResourceLocation getGlowTextures(T var1);
}
