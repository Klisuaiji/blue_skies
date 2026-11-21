package com.legacy.blue_skies.compat;
import net.minecraft.world.food.FoodProperties;
/** 1.20.4 saturationMod(float) -> 1.21.1 saturationModifier(float). */
public final class LegacyFoods {
   private LegacyFoods() {}
   public static FoodProperties.Builder saturationMod(FoodProperties.Builder builder, float value) {
      return builder.saturationModifier(value);
   }
}
