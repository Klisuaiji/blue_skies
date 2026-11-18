package com.legacy.blue_skies.blocks.natural;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkySandBlock extends ColoredFallingBlock implements BonemealableBlock {
   private final Function<RandomSource, Block> flowerProvider;

   public SkySandBlock(int dustColorIn, Properties properties, Function<RandomSource, Block> flowerProvider) {
      super(new ColorRGBA(dustColorIn), properties);
      this.flowerProvider = flowerProvider;
   }

   public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
      return world.getBlockState(pos.above()).isAir();
   }

   public boolean isBonemealSuccess(Level world, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel world, RandomSource rand, BlockPos pos, BlockState state) {
      for (int attempts = rand.nextInt(3) + 7; attempts > 0; attempts--) {
         BlockPos offset = pos.offset(rand.nextInt(7) - 3, 0, rand.nextInt(7) - 3);

         for (int y = -1; y <= 1; y++) {
            BlockPos upOffset = offset.above(y);
            BlockState placementState = this.flowerProvider.apply(rand).defaultBlockState();
            if (world.isEmptyBlock(upOffset) && placementState.canSurvive(world, upOffset)) {
               world.setBlockAndUpdate(upOffset, placementState);
               break;
            }
         }
      }
   }
}
