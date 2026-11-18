package com.legacy.blue_skies.data.objects.tags;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SkiesItemTags {
   public static final TagKey<Item> BOOKSHELVES = tag("bookshelves");
   public static final TagKey<Item> CRAFTING_TABLES = tag("crafting_tables");
   public static final TagKey<Item> LADDERS = tag("ladders");
   public static final TagKey<Item> VINES = tag("vines");
   public static final TagKey<Item> BLUEBRIGHT_LOGS = tag("logs/bluebright");
   public static final TagKey<Item> STARLIT_LOGS = tag("logs/starlit");
   public static final TagKey<Item> FROSTBRIGHT_LOGS = tag("logs/frostbright");
   public static final TagKey<Item> LUNAR_LOGS = tag("logs/lunar");
   public static final TagKey<Item> DUSK_LOGS = tag("logs/dusk");
   public static final TagKey<Item> MAPLE_LOGS = tag("logs/maple");
   public static final TagKey<Item> COMET_LOGS = tag("logs/comet");
   public static final TagKey<Item> CRYSTALLIZED_LOGS = tag("logs/crystallized");
   public static final TagKey<Item> COBBLESTONES = tag("cobblestone");
   public static final TagKey<Item> STONES = tag("stone");
   public static final TagKey<Item> BLINDING_STONES = tag("dungeon/blinding");
   public static final TagKey<Item> POISON_STONES = tag("dungeon/poison");
   public static final TagKey<Item> NATURE_STONES = tag("dungeon/nature");
   public static final TagKey<Item> AQUATIC_STONES = tag("dungeon/aquatic");
   public static final TagKey<Item> LIFE_STONES = tag("dungeon/life");
   public static final TagKey<Item> ILLAGER_TELEPORTABLE = tag("dungeon/illager_teleportable");
   public static final TagKey<Item> MOONSTONE_ORES = tag("ores/moonstone");
   public static final TagKey<Item> PYROPE_ORES = tag("ores/pyrope");
   public static final TagKey<Item> DIOPSIDE_ORES = tag("ores/diopside");
   public static final TagKey<Item> AQUITE_ORES = tag("ores/aquite");
   public static final TagKey<Item> CHAROITE_ORES = tag("ores/charoite");
   public static final TagKey<Item> HORIZONITE_ORES = tag("ores/horizonite");
   public static final TagKey<Item> FALSITE_ORES = tag("ores/falsite");
   public static final TagKey<Item> VENTIUM_ORES = tag("ores/ventium");
   public static final TagKey<Item> MOONSTONE_BLOCKS = tag("storage_blocks/moonstone");
   public static final TagKey<Item> PYROPE_BLOCKS = tag("storage_blocks/pyrope");
   public static final TagKey<Item> DIOPSIDE_BLOCKS = tag("storage_blocks/diopside");
   public static final TagKey<Item> AQUITE_BLOCKS = tag("storage_blocks/aquite");
   public static final TagKey<Item> CHAROITE_BLOCKS = tag("storage_blocks/charoite");
   public static final TagKey<Item> HORIZONITE_BLOCKS = tag("storage_blocks/horizonite");
   public static final TagKey<Item> FALSITE_BLOCKS = tag("storage_blocks/falsite");
   public static final TagKey<Item> VENTIUM_BLOCKS = tag("storage_blocks/ventium");
   public static final TagKey<Item> RAW_AQUITE_BLOCKS = tag("storage_blocks/raw_aquite");
   public static final TagKey<Item> RAW_CHAROITE_BLOCKS = tag("storage_blocks/raw_charoite");
   public static final TagKey<Item> RAW_HORIZONITE_BLOCKS = tag("storage_blocks/raw_horizonite");
   public static final TagKey<Item> RAW_FALSITE_BLOCKS = tag("storage_blocks/raw_falsite");
   public static final TagKey<Item> RAW_VENTIUM_BLOCKS = tag("storage_blocks/raw_ventium");
   public static final TagKey<Item> WOODEN_PICKAXES = tag("tools/wooden_pickaxes");
   public static final TagKey<Item> WOODEN_SHOVELS = tag("tools/wooden_shovels");
   public static final TagKey<Item> WOODEN_AXES = tag("tools/wooden_axes");
   public static final TagKey<Item> WOODEN_HOES = tag("tools/wooden_hoes");
   public static final TagKey<Item> WOODEN_SWORDS = tag("tools/wooden_swords");
   public static final TagKey<Item> WOODEN_TOOLS = tag("wooden_tools");
   public static final TagKey<Item> HORIZONITE_TOOLS = tag("horizonite_tools");
   public static final TagKey<Item> TOOLS = tag("tools");
   public static final TagKey<Item> PICKAXES = tag("tools/pickaxes");
   public static final TagKey<Item> AXES = tag("tools/axes");
   public static final TagKey<Item> SHOVELS = tag("tools/shovels");
   public static final TagKey<Item> HOES = tag("tools/hoes");
   public static final TagKey<Item> SWORDS = tag("tools/swords");
   public static final TagKey<Item> MOONSTONE = tag("gems/moonstone");
   public static final TagKey<Item> MOONSTONE_SHARD = tag("gems/moonstone_shard");
   public static final TagKey<Item> PYROPE = tag("gems/pyrope");
   public static final TagKey<Item> AQUITE = tag("gems/aquite");
   public static final TagKey<Item> DIOPSIDE = tag("gems/diopside");
   public static final TagKey<Item> CHAROITE = tag("gems/charoite");
   public static final TagKey<Item> HORIZONITE = tag("ingots/horizonite");
   public static final TagKey<Item> FALSITE = tag("ingots/falsite");
   public static final TagKey<Item> VENTIUM = tag("ingots/ventium");
   public static final TagKey<Item> HORIZONITE_NUGGETS = tag("nuggets/horizonite");
   public static final TagKey<Item> FALSITE_NUGGETS = tag("nuggets/falsite");
   public static final TagKey<Item> VENTIUM_NUGGETS = tag("nuggets/ventium");
   public static final TagKey<Item> RAW_MATERIALS_AQUITE = tag("raw_materials/aquite");
   public static final TagKey<Item> RAW_MATERIALS_CHAROITE = tag("raw_materials/charoite");
   public static final TagKey<Item> RAW_MATERIALS_HORIZONITE = tag("raw_materials/horizonite");
   public static final TagKey<Item> RAW_MATERIALS_FALSITE = tag("raw_materials/falsite");
   public static final TagKey<Item> RAW_MATERIALS_VENTIUM = tag("raw_materials/ventium");
   public static final TagKey<Item> DUNGEON_KEYS = tag("dungeon_keys");
   public static final TagKey<Item> ARCS = tag("arcs");
   public static final TagKey<Item> COMET_GRASS_DROPS = tag("drops/comet_grass");
   public static final TagKey<Item> TURQUOISE_GRASS_DROPS = tag("drops/turquoise_grass");
   public static final TagKey<Item> LUNAR_GRASS_DROPS = tag("drops/lunar_grass");
   public static final TagKey<Item> TROUGH_FOODS = tag("trough_foods");

   public static void init() {
   }

   private static TagKey<Item> tag(String key) {
      return ItemTags.create(BlueSkies.locate(key));
   }
}
