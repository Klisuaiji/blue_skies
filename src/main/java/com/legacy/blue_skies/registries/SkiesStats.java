package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class SkiesStats {
   public static final ResourceLocation BOSSES_DEFEATED = registerCustom("bosses_defeated", StatFormatter.DEFAULT);
   public static final ResourceLocation DUNGEONS_CONQUERED = registerCustom("dungeons_conquered", StatFormatter.DEFAULT);
   public static final ResourceLocation TOMES_CHANGED = registerCustom("tomes_changed", StatFormatter.DEFAULT);
   public static final ResourceLocation VILLAGERS_PICKPOCKETED = registerCustom("villagers_pickpocketed", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_SNOWCAP_OVEN = registerCustom("interact_with_snowcap_oven", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_HORIZONITE_FORGE = registerCustom("interact_with_horizonite_forge", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_SUMMONING_TABLE = registerCustom("interact_with_summoning_table", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_ALCHEMY_TABLE = registerCustom("interact_with_alchemy_table", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_TOOL_BOX = registerCustom("interact_with_tool_box", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_KEYSTONE = registerCustom("interact_with_keystone", StatFormatter.DEFAULT);
   public static final ResourceLocation INTERACT_WITH_FOOD_PREP_TABLE = registerCustom("interact_with_food_prep_table", StatFormatter.DEFAULT);
   public static final ResourceLocation OPEN_BAG_OF_SPOILS = registerCustom("open_bag_of_spoils", StatFormatter.DEFAULT);

   public static void init() {
   }

   private static ResourceLocation registerCustom(String key, StatFormatter formatter) {
      ResourceLocation resourcelocation = BlueSkies.locate(key);
      Registry.register(BuiltInRegistries.CUSTOM_STAT, key, resourcelocation);
      Stats.CUSTOM.get(resourcelocation, formatter);
      return resourcelocation;
   }
}
