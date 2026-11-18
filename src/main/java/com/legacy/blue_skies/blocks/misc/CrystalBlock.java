package com.legacy.blue_skies.blocks.misc;

import com.legacy.blue_skies.blocks.natural.WallCrystalBlock;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CrystalBlock extends Block {
   private final Supplier<WallCrystalBlock> crystal;

   public CrystalBlock(Properties properties, Supplier<WallCrystalBlock> crystal) {
      super(properties);
      this.crystal = crystal;
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (rand.nextFloat() < 0.07) {
         Direction dir = (Direction)Util.getRandom(Direction.values(), rand);
         BlockPos offsetPos = pos.relative(dir);
         if (level.getBlockState(offsetPos).getBlock() == Blocks.WATER) {
            level.setBlockAndUpdate(
               offsetPos,
               (BlockState)((BlockState)this.crystal.get().defaultBlockState().setValue(WallCrystalBlock.FACING, dir))
                  .setValue(WallCrystalBlock.WATERLOGGED, true)
            );
         }
      }
   }
}
