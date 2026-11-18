package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.registries.SkiesBlocks;
import net.minecraft.world.level.block.state.BlockState;

public class IronBarsBlockHooks {
   public static boolean shouldAttach(BlockState state) {
      return state.getBlock() == SkiesBlocks.nature_keystone;
   }
}
