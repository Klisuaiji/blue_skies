package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.blocks.util.ITranslucentBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHooks {
   public static boolean isTranslucentBlock(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
      BlockState adjacentState = world.getBlockState(pos.relative(dir));
      return state.getBlock() instanceof ITranslucentBlock translucentBlock
         && adjacentState.getBlock() instanceof ITranslucentBlock
         && translucentBlock.isSideInvisible(world, state, adjacentState, dir, pos);
   }
}
