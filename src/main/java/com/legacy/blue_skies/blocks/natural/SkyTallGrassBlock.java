package com.legacy.blue_skies.blocks.natural;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyTallGrassBlock extends TallGrassBlock {
   private final Supplier<DoublePlantBlock> doublePlant;

   public SkyTallGrassBlock(Supplier<DoublePlantBlock> doublePlant) {
      super(Properties.ofFullCopy(Blocks.SHORT_GRASS));
      this.doublePlant = doublePlant;
   }

   public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState state) {
      return this.doublePlant.get() != null;
   }

   public boolean isBonemealSuccess(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
      return this.doublePlant.get() != null;
   }

   public void performBonemeal(ServerLevel worldIn, RandomSource rand, BlockPos pos, BlockState state) {
      DoublePlantBlock doublePlant = this.doublePlant.get();
      if (doublePlant != null && doublePlant.defaultBlockState().canSurvive(worldIn, pos) && worldIn.isEmptyBlock(pos.above())) {
         DoublePlantBlock.placeAt(worldIn, doublePlant.defaultBlockState(), pos, 2);
      }
   }
}
