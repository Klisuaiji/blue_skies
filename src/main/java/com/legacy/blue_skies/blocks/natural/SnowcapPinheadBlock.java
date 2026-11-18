package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.common.Tags.Blocks;

public class SnowcapPinheadBlock extends SkyMushroomBlock implements BonemealableBlock {
   public SnowcapPinheadBlock(Properties properties) {
      super(properties);
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return super.mayPlaceOn(state, level, pos) || state.is(Blocks.STONE);
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return true;
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      if (rand.nextFloat() < 0.75F && level.isEmptyBlock(pos.above())) {
         level.setBlock(pos, (BlockState)SkiesBlocks.snowcap_mushroom.defaultBlockState().setValue(SnowcapMushroomBlock.HALF, DoubleBlockHalf.LOWER), 19);
         level.setBlock(
            pos.above(), (BlockState)SkiesBlocks.snowcap_mushroom.defaultBlockState().setValue(SnowcapMushroomBlock.HALF, DoubleBlockHalf.UPPER), 19
         );
      }
   }
}
