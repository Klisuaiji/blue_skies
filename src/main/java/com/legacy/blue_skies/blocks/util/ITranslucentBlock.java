package com.legacy.blue_skies.blocks.util;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ITranslucentBlock {
   @OnlyIn(Dist.CLIENT)
   default boolean isSideInvisible(BlockGetter world, BlockState state, BlockState adjacentState, Direction side, BlockPos pos) {
      return this.getCullingBlocks().contains(adjacentState.getBlock())
         ? !Shapes.joinIsNotEmpty(
            adjacentState.getBlockSupportShape(world, pos.relative(side)).getFaceShape(side.getOpposite()),
            state.getBlockSupportShape(world, pos).getFaceShape(side),
            BooleanOp.ONLY_SECOND
         )
         : false;
   }

   Set<Block> getCullingBlocks();
}
