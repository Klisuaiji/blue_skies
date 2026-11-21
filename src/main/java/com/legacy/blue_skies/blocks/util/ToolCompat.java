package com.legacy.blue_skies.blocks.util;

import com.legacy.blue_skies.registries.SkiesBlocks;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.Block;

public class ToolCompat {
   public static final Map<Block, Block> AXE_STRIPPING = new HashMap<>();
   public static final Map<Block, Block> HOE_TILLING = new HashMap<>();

   public static void init() {
   }

   static void axeStripping(Block log, Block stripped) {
      AXE_STRIPPING.put(log, stripped);
   }

   static void hoeTilling(Block dirt, Block farmland) {
      HOE_TILLING.put(dirt, farmland);
   }

   static {
      axeStripping(SkiesBlocks.bluebright_log, SkiesBlocks.stripped_bluebright_log);
      axeStripping(SkiesBlocks.bluebright_wood, SkiesBlocks.stripped_bluebright_wood);
      axeStripping(SkiesBlocks.starlit_log, SkiesBlocks.stripped_starlit_log);
      axeStripping(SkiesBlocks.starlit_wood, SkiesBlocks.stripped_starlit_wood);
      axeStripping(SkiesBlocks.frostbright_log, SkiesBlocks.stripped_frostbright_log);
      axeStripping(SkiesBlocks.frostbright_wood, SkiesBlocks.stripped_frostbright_wood);
      axeStripping(SkiesBlocks.lunar_log, SkiesBlocks.stripped_lunar_log);
      axeStripping(SkiesBlocks.lunar_wood, SkiesBlocks.stripped_lunar_wood);
      axeStripping(SkiesBlocks.dusk_log, SkiesBlocks.stripped_dusk_log);
      axeStripping(SkiesBlocks.dusk_wood, SkiesBlocks.stripped_dusk_wood);
      axeStripping(SkiesBlocks.maple_log, SkiesBlocks.stripped_maple_log);
      axeStripping(SkiesBlocks.maple_wood, SkiesBlocks.stripped_maple_wood);
      axeStripping(SkiesBlocks.comet_log, SkiesBlocks.stripped_comet_log);
      axeStripping(SkiesBlocks.comet_wood, SkiesBlocks.stripped_comet_wood);
      hoeTilling(SkiesBlocks.turquoise_grass_block, (Block)SkiesBlocks.turquoise_farmland.get());
      hoeTilling(SkiesBlocks.turquoise_dirt, (Block)SkiesBlocks.turquoise_farmland.get());
      hoeTilling(SkiesBlocks.coarse_turquoise_dirt, SkiesBlocks.turquoise_dirt);
      hoeTilling(SkiesBlocks.lunar_grass_block, (Block)SkiesBlocks.lunar_farmland.get());
      hoeTilling(SkiesBlocks.lunar_dirt, (Block)SkiesBlocks.lunar_farmland.get());
      hoeTilling(SkiesBlocks.coarse_lunar_dirt, SkiesBlocks.lunar_dirt);
      hoeTilling(SkiesBlocks.turquoise_comet_grass_block, (Block)SkiesBlocks.turquoise_farmland.get());
      hoeTilling(SkiesBlocks.lunar_comet_grass_block, (Block)SkiesBlocks.lunar_farmland.get());
   }
}
