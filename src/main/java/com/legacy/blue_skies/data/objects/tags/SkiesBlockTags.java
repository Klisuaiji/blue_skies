package com.legacy.blue_skies.data.objects.tags;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SkiesBlockTags {
   public static final TagKey<Block> BOOKSHELVES = tag("bookshelves");
   public static final TagKey<Block> CRAFTING_TABLES = tag("crafting_tables");
   public static final TagKey<Block> LADDERS = tag("ladders");
   public static final TagKey<Block> VINES = tag("vines");
   public static final TagKey<Block> BLUEBRIGHT_LOGS = tag("logs/bluebright");
   public static final TagKey<Block> STARLIT_LOGS = tag("logs/starlit");
   public static final TagKey<Block> FROSTBRIGHT_LOGS = tag("logs/frostbright");
   public static final TagKey<Block> LUNAR_LOGS = tag("logs/lunar");
   public static final TagKey<Block> DUSK_LOGS = tag("logs/dusk");
   public static final TagKey<Block> MAPLE_LOGS = tag("logs/maple");
   public static final TagKey<Block> COMET_LOGS = tag("logs/comet");
   public static final TagKey<Block> CRYSTALLIZED_LOGS = tag("logs/crystallized");
   public static final TagKey<Block> COBBLESTONES = tag("cobblestone");
   public static final TagKey<Block> STONES = tag("stone");
   public static final TagKey<Block> BLINDING_STONES = tag("dungeon/blinding");
   public static final TagKey<Block> POISON_STONES = tag("dungeon/poison");
   public static final TagKey<Block> NATURE_STONES = tag("dungeon/nature");
   public static final TagKey<Block> AQUATIC_STONES = tag("dungeon/aquatic");
   public static final TagKey<Block> LIFE_STONES = tag("dungeon/life");
   public static final TagKey<Block> KEYSTONES = tag("dungeon/keystone");
   public static final TagKey<Block> ILLAGER_TELEPORTABLE = tag("dungeon/illager_teleportable");
   public static final TagKey<Block> MOONSTONE_ORES = tag("ores/moonstone");
   public static final TagKey<Block> PYROPE_ORES = tag("ores/pyrope");
   public static final TagKey<Block> DIOPSIDE_ORES = tag("ores/diopside");
   public static final TagKey<Block> AQUITE_ORES = tag("ores/aquite");
   public static final TagKey<Block> CHAROITE_ORES = tag("ores/charoite");
   public static final TagKey<Block> HORIZONITE_ORES = tag("ores/horizonite");
   public static final TagKey<Block> FALSITE_ORES = tag("ores/falsite");
   public static final TagKey<Block> VENTIUM_ORES = tag("ores/ventium");
   public static final TagKey<Block> MOONSTONE_BLOCKS = tag("storage_blocks/moonstone");
   public static final TagKey<Block> PYROPE_BLOCKS = tag("storage_blocks/pyrope");
   public static final TagKey<Block> DIOPSIDE_BLOCKS = tag("storage_blocks/diopside");
   public static final TagKey<Block> AQUITE_BLOCKS = tag("storage_blocks/aquite");
   public static final TagKey<Block> CHAROITE_BLOCKS = tag("storage_blocks/charoite");
   public static final TagKey<Block> HORIZONITE_BLOCKS = tag("storage_blocks/horizonite");
   public static final TagKey<Block> FALSITE_BLOCKS = tag("storage_blocks/falsite");
   public static final TagKey<Block> VENTIUM_BLOCKS = tag("storage_blocks/ventium");
   public static final TagKey<Block> RAW_AQUITE_BLOCKS = tag("storage_blocks/raw_aquite");
   public static final TagKey<Block> RAW_CHAROITE_BLOCKS = tag("storage_blocks/raw_charoite");
   public static final TagKey<Block> RAW_HORIZONITE_BLOCKS = tag("storage_blocks/raw_horizonite");
   public static final TagKey<Block> RAW_FALSITE_BLOCKS = tag("storage_blocks/raw_falsite");
   public static final TagKey<Block> RAW_VENTIUM_BLOCKS = tag("storage_blocks/raw_ventium");
   public static final TagKey<Block> CARVER_REPLACEABLES = tag("cave_replaceables");
   public static final TagKey<Block> BLUE_FIRE_BASE_BLOCKS = tag("blue_fire_base_blocks");
   public static final TagKey<Block> BLACK_FIRE_BASE_BLOCKS = tag("black_fire_base_blocks");
   public static final TagKey<Block> WEBBING_ATTACHES_TO = tag("webbing_attaches_to");

   public static void init() {
   }

   private static TagKey<Block> tag(String key) {
      return BlockTags.create(BlueSkies.locate(key));
   }
}
