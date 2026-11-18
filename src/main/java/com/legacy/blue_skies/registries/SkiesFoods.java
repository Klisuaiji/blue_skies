package com.legacy.blue_skies.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodProperties.Builder;

public class SkiesFoods {
   public static final FoodProperties COMET_BERRY = new Builder().nutrition(2).saturationMod(0.1F).fast().build();
   public static final FoodProperties BREWBERRY = new Builder().nutrition(2).saturationMod(0.1F).fast().build();
   public static final FoodProperties PINK_BREWBERRY = new Builder()
      .nutrition(2)
      .saturationMod(0.1F)
      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 40, 2), 100.0F)
      .alwaysEat()
      .fast()
      .build();
   public static final FoodProperties BLACK_BREWBERRY = new Builder().nutrition(2).saturationMod(0.1F).alwaysEat().fast().build();
   public static final FoodProperties WINTER_LEAVES = new Builder().nutrition(4).saturationMod(0.2F).build();
   public static final FoodProperties FIERY_BEAN = new Builder().nutrition(4).saturationMod(0.2F).build();
   public static final FoodProperties SCALEFRUIT = new Builder().nutrition(4).saturationMod(0.2F).build();
   public static final FoodProperties PINE_FRUIT = new Builder().nutrition(4).saturationMod(0.2F).build();
   public static final FoodProperties SOLNUT = new Builder().nutrition(2).saturationMod(0.6F).fast().build();
   public static final FoodProperties CRYO_ROOT = new Builder()
      .nutrition(3)
      .saturationMod(0.3F)
      .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0), 0.05F)
      .build();
   public static final FoodProperties CRESCENT_FRUIT = new Builder().nutrition(5).saturationMod(0.3F).build();
   public static final FoodProperties CARABEEF = new Builder().nutrition(3).saturationMod(0.3F).meat().build();
   public static final FoodProperties COOKED_CARABEEF = new Builder().nutrition(8).saturationMod(0.8F).meat().build();
   public static final FoodProperties VENISON = new Builder().nutrition(3).saturationMod(0.3F).meat().build();
   public static final FoodProperties COOKED_VENISON = new Builder().nutrition(8).saturationMod(0.8F).meat().build();
   public static final FoodProperties MONTIOR_TAIL = new Builder().nutrition(3).saturationMod(0.3F).meat().build();
   public static final FoodProperties COOKED_MONTIOR_TAIL = new Builder().nutrition(8).saturationMod(0.8F).meat().build();
   public static final FoodProperties GRITTLE_FLATFISH = new Builder().nutrition(4).saturationMod(0.1F).build();
   public static final FoodProperties COOKED_GRITTLE_FLATFISH = new Builder().nutrition(7).saturationMod(0.6F).build();
   public static final FoodProperties FISH = new Builder().nutrition(2).saturationMod(0.1F).build();
   public static final FoodProperties COOKED_FISH = new Builder().nutrition(5).saturationMod(0.6F).build();
}
