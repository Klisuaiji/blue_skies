package com.legacy.blue_skies.blocks.util;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class Composting {
   public static void init() {
      register(
         0.3F,
         SkiesBlocks.bluebright_leaves,
         SkiesBlocks.starlit_leaves,
         SkiesBlocks.frostbright_leaves,
         SkiesBlocks.lunar_leaves,
         SkiesBlocks.dusk_leaves,
         SkiesBlocks.maple_leaves,
         SkiesBlocks.comet_leaves,
         SkiesBlocks.crescent_fruit_leaves,
         SkiesBlocks.bluebright_sapling,
         SkiesBlocks.starlit_sapling,
         SkiesBlocks.frostbright_sapling,
         SkiesBlocks.lunar_sapling,
         SkiesBlocks.dusk_sapling,
         SkiesBlocks.maple_sapling,
         SkiesBlocks.comet_sapling,
         SkiesBlocks.crescent_fruit_sapling,
         SkiesBlocks.brewberry_bush,
         SkiesBlocks.short_turquoise_grass,
         SkiesBlocks.short_lunar_grass,
         SkiesBlocks.short_comet_grass,
         SkiesBlocks.brittlebush,
         SkiesItems.brewberry,
         SkiesItems.black_brewberry,
         SkiesItems.pink_brewberry,
         SkiesItems.pine_fruit_seeds,
         SkiesItems.scalefruit_seeds,
         SkiesItems.winter_leaf_seeds,
         SkiesItems.fiery_bean_seeds,
         SkiesBlocks.sea_moss_carpet,
         SkiesBlocks.sea_moss,
         SkiesBlocks.brumble,
         SkiesBlocks.brumble_vine_top
      );
      register(
         0.5F,
         SkiesBlocks.bluebright_vine,
         SkiesBlocks.starlit_vine,
         SkiesBlocks.frostbright_vine,
         SkiesBlocks.lunar_vine,
         SkiesBlocks.dusk_vine,
         SkiesBlocks.maple_vine,
         SkiesBlocks.tall_lunar_grass,
         SkiesBlocks.tall_turquoise_grass,
         SkiesBlocks.chillweed,
         SkiesBlocks.muckweed,
         SkiesBlocks.snowcap_pinhead,
         SkiesBlocks.snowcap_mushroom,
         SkiesBlocks.snowcap_mushroom_stem,
         SkiesBlocks.brumble_block
      );
      register(
         0.65F,
         SkiesBlocks.chilled_lily_pad,
         SkiesBlocks.moonlit_water_lily,
         SkiesBlocks.snowbloom,
         SkiesBlocks.camellia,
         SkiesBlocks.blush_blossom,
         SkiesBlocks.briskbloom,
         SkiesBlocks.polar_posy,
         SkiesBlocks.midday_bayhop,
         SkiesBlocks.frose,
         SkiesBlocks.moonlit_bloom,
         SkiesBlocks.crystal_flower,
         SkiesBlocks.blaze_bud,
         SkiesBlocks.flare_floret,
         SkiesBlocks.nightcress,
         SkiesBlocks.lucentroot,
         SkiesBlocks.glimmer_reed,
         SkiesBlocks.snowcap_mushroom_block,
         SkiesBlocks.baneful_mushroom,
         SkiesItems.pine_fruit,
         SkiesItems.scalefruit,
         SkiesItems.winter_leaves,
         SkiesItems.fiery_beans,
         SkiesItems.solnut,
         SkiesItems.cryo_root,
         SkiesItems.crescent_fruit,
         SkiesBlocks.sea_moss_block
      );
   }

   private static void register(float chance, ItemLike... items) {
      for (ItemLike item : items) {
         register(chance, item);
      }
   }

   private static void register(float chance, ItemLike item) {
      ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
   }
}
