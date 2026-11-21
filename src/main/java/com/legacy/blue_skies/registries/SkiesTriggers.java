package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.triggers.SpecialArachnarchTrigger;
import com.legacy.blue_skies.triggers.SpecialCrusherTrigger;
import com.legacy.blue_skies.triggers.UsedAlchemyTableTrigger;
import com.legacy.blue_skies.triggers.UsedFoodPrepTrigger;
import com.legacy.blue_skies.triggers.UsedToolboxTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

public class SkiesTriggers {
   public static final UsedAlchemyTableTrigger USED_ALCHEMY_TABLE = register("used_alchemy_table", new UsedAlchemyTableTrigger());
   public static final UsedToolboxTrigger USED_TOOL_BOX = register("used_tool_box", new UsedToolboxTrigger());
   public static final UsedFoodPrepTrigger USED_FOOD_PREP = register("used_food_prep", new UsedFoodPrepTrigger());
   public static final SpecialCrusherTrigger SPECIAL_CRUSHER_KILL = register("special_crusher_kill", new SpecialCrusherTrigger());
   public static final SpecialArachnarchTrigger SPECIAL_ARACHNARCH_KILL = register("special_arachnarch_kill", new SpecialArachnarchTrigger());

   public static void init() {
   }

   public static <T extends CriterionTrigger<?>> T register(String name, T criterion) {
      return (T)CriteriaTriggers.register(BlueSkies.find(name), criterion);
   }
}
