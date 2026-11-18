package com.legacy.blue_skies.client.renders.entities.util;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;

public interface IMultiModelRenderer<T extends LivingEntity> {
   EntityModel<T> getGlowModels(T var1);
}
