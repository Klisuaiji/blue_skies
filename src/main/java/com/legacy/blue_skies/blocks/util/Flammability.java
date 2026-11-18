package com.legacy.blue_skies.blocks.util;

import com.legacy.blue_skies.registries.SkiesBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class Flammability {
   public static void init() {
      setList(
         5,
         20,
         SkiesBlocks.bluebright_planks,
         SkiesBlocks.starlit_planks,
         SkiesBlocks.frostbright_planks,
         SkiesBlocks.lunar_planks,
         SkiesBlocks.dusk_planks,
         SkiesBlocks.maple_planks,
         SkiesBlocks.comet_planks
      );
      setList(
         5,
         20,
         SkiesBlocks.bluebright_slab,
         SkiesBlocks.starlit_slab,
         SkiesBlocks.frostbright_slab,
         SkiesBlocks.lunar_slab,
         SkiesBlocks.dusk_slab,
         SkiesBlocks.maple_slab,
         SkiesBlocks.comet_slab
      );
      setList(
         5,
         20,
         SkiesBlocks.bluebright_stairs,
         SkiesBlocks.starlit_stairs,
         SkiesBlocks.frostbright_stairs,
         SkiesBlocks.lunar_stairs,
         SkiesBlocks.dusk_stairs,
         SkiesBlocks.maple_stairs,
         SkiesBlocks.comet_stairs
      );
      setList(
         5,
         20,
         SkiesBlocks.bluebright_fence,
         SkiesBlocks.starlit_fence,
         SkiesBlocks.frostbright_fence,
         SkiesBlocks.lunar_fence,
         SkiesBlocks.dusk_fence,
         SkiesBlocks.maple_fence,
         SkiesBlocks.comet_fence
      );
      setList(
         5,
         20,
         SkiesBlocks.bluebright_fence_gate,
         SkiesBlocks.starlit_fence_gate,
         SkiesBlocks.frostbright_fence_gate,
         SkiesBlocks.lunar_fence_gate,
         SkiesBlocks.dusk_fence_gate,
         SkiesBlocks.maple_fence_gate,
         SkiesBlocks.comet_fence_gate
      );
      setList(
         5,
         5,
         SkiesBlocks.bluebright_log,
         SkiesBlocks.starlit_log,
         SkiesBlocks.frostbright_log,
         SkiesBlocks.lunar_log,
         SkiesBlocks.dusk_log,
         SkiesBlocks.maple_log,
         SkiesBlocks.comet_log
      );
      setList(
         5,
         5,
         SkiesBlocks.bluebright_wood,
         SkiesBlocks.starlit_wood,
         SkiesBlocks.frostbright_wood,
         SkiesBlocks.lunar_wood,
         SkiesBlocks.dusk_wood,
         SkiesBlocks.maple_wood,
         SkiesBlocks.comet_wood
      );
      setList(
         5,
         5,
         SkiesBlocks.stripped_bluebright_log,
         SkiesBlocks.stripped_starlit_log,
         SkiesBlocks.stripped_frostbright_log,
         SkiesBlocks.stripped_lunar_log,
         SkiesBlocks.stripped_dusk_log,
         SkiesBlocks.stripped_maple_log,
         SkiesBlocks.stripped_comet_log
      );
      setList(
         5,
         5,
         SkiesBlocks.stripped_bluebright_wood,
         SkiesBlocks.stripped_starlit_wood,
         SkiesBlocks.stripped_frostbright_wood,
         SkiesBlocks.stripped_lunar_wood,
         SkiesBlocks.stripped_dusk_wood,
         SkiesBlocks.stripped_maple_wood,
         SkiesBlocks.stripped_comet_wood
      );
      setList(
         30,
         60,
         SkiesBlocks.bluebright_leaves,
         SkiesBlocks.starlit_leaves,
         SkiesBlocks.frostbright_leaves,
         SkiesBlocks.lunar_leaves,
         SkiesBlocks.dusk_leaves,
         SkiesBlocks.maple_leaves,
         SkiesBlocks.comet_leaves,
         SkiesBlocks.crescent_fruit_leaves
      );
      setList(
         15,
         100,
         SkiesBlocks.bluebright_vine,
         SkiesBlocks.starlit_vine,
         SkiesBlocks.frostbright_vine,
         SkiesBlocks.lunar_vine,
         SkiesBlocks.dusk_vine,
         SkiesBlocks.maple_vine
      );
      setList(
         60,
         100,
         SkiesBlocks.brewberry_bush,
         SkiesBlocks.short_turquoise_grass,
         SkiesBlocks.short_lunar_grass,
         SkiesBlocks.snowbloom,
         SkiesBlocks.camellia,
         SkiesBlocks.moonlit_bloom,
         SkiesBlocks.crystal_flower,
         SkiesBlocks.blaze_bud
      );
   }

   private static void setList(int encouragement, int flammability, Block... blocks) {
      FireBlock fire = (FireBlock)Blocks.FIRE;

      for (Block b : blocks) {
         fire.setFlammable(b, encouragement, flammability);
      }
   }
}
