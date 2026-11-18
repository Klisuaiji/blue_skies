package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SeaMossBlock extends Block implements BonemealableBlock {
   public SeaMossBlock(Properties prop) {
      super(prop);
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return level.getFluidState(pos.above()).is(FluidTags.WATER);
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      BlockState moss = this.defaultBlockState();

      for (int x = -1; x <= 1; x++) {
         for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
               this.tryPlaceMoss(level, pos.offset(x, y, z), rand, moss);
            }
         }
      }

      int width = rand.nextInt(2) + 2;
      int depth = rand.nextInt(2) + 2;
      int height = rand.nextInt(3) + 2;

      for (int x = -width; x <= width; x++) {
         for (int z = -depth; z <= depth; z++) {
            for (int y = -height; y <= height; y++) {
               BlockPos offsetPos = pos.offset(x, y, z);
               if (Math.abs(x) != width && Math.abs(z) != depth && Math.abs(y) != height) {
                  this.tryPlaceMoss(level, offsetPos, rand, moss);
               } else if (rand.nextFloat() < 0.5F) {
                  this.tryPlaceMoss(level, offsetPos, rand, moss);
               }
            }
         }
      }
   }

   protected boolean tryPlaceMoss(ServerLevel level, BlockPos pos, RandomSource rand, BlockState moss) {
      BlockPos abovePos = pos.above();
      if (level.getFluidState(abovePos).is(FluidTags.WATER)) {
         BlockState replaceState = level.getBlockState(pos);
         if (replaceState.is(BlockTags.MOSS_REPLACEABLE)) {
            level.setBlock(pos, moss, 3);
            if (level.getBlockState(abovePos).is(Blocks.WATER) && rand.nextFloat() < 0.7) {
               BlockState topState = (rand.nextBoolean() ? SkiesBlocks.sea_moss_carpet : SkiesBlocks.sea_moss).defaultBlockState();
               if (topState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                  topState = (BlockState)topState.setValue(BlockStateProperties.WATERLOGGED, true);
               }

               level.setBlock(abovePos, topState, 3);
            }

            return true;
         }
      }

      return false;
   }
}
