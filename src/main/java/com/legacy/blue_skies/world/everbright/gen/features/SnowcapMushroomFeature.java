package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.blue_skies.world.general_features.BaseLargeMushroomFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class SnowcapMushroomFeature extends BaseLargeMushroomFeature {
   private static final Lazy<BlockState> STEM = Lazy.of(() -> SkiesBlocks.snowcap_mushroom_stem.defaultBlockState());
   private static final Lazy<BlockState> CAP = Lazy.of(() -> SkiesBlocks.snowcap_mushroom_block.defaultBlockState());

   public SnowcapMushroomFeature(Codec<TreeConfiguration> config) {
      super(config);
   }

   @Override
   public boolean place(WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedMushrooms) {
      int capWidth = rand.nextBoolean() ? 3 : 4;
      int height = 10 + rand.nextInt(4);
      if (!this.isAreaOk(world, pos, height, 1, height, capWidth * 2 + 1)) {
         return false;
      }

      int bottomOffset = 0;
      if (world.getBlockState(pos.below()).getFluidState().is(FluidTags.WATER)) {
         while (bottomOffset < 2 && world.getBlockState(pos.below(bottomOffset + 1)).getFluidState().is(FluidTags.WATER)) {
            bottomOffset++;
         }
      }

      for (int y = -bottomOffset; y < height; y++) {
         this.setBlockIfOk(changedMushrooms, world, pos.above(y), (BlockState)STEM.get());
      }

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         BlockPos offsetPos = pos.relative(dir).below(bottomOffset);
         this.setBlockIfOk(changedMushrooms, world, offsetPos, (BlockState)STEM.get());
         if (rand.nextBoolean()) {
            this.setBlockIfOk(changedMushrooms, world, offsetPos.above(), (BlockState)STEM.get());
         }

         BlockState lowerState = world.getBlockState(offsetPos.below());
         if (lowerState.is(BlockTags.REPLACEABLE_BY_TREES) || lowerState.getBlock() instanceof BushBlock) {
            this.setBlock(changedMushrooms, world, offsetPos.below(), (BlockState)STEM.get());
         }
      }

      for (int x = -capWidth; x <= capWidth; x++) {
         for (int z = -capWidth; z <= capWidth; z++) {
            if (Math.abs(x) != capWidth || Math.abs(x) != Math.abs(z)) {
               this.setBlockIfOk(changedMushrooms, world, pos.offset(x, height, z), (BlockState)((BlockState)CAP.get()).setValue(HugeMushroomBlock.DOWN, false));
            }
         }
      }

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         for (int i = -capWidth; i <= capWidth; i++) {
            BlockState state = (BlockState)((BlockState)((BlockState)CAP.get()).setValue(HugeMushroomBlock.DOWN, false))
               .setValue((Property)PipeBlock.PROPERTY_BY_DIRECTION.get(dir.getOpposite().getClockWise()), false);
            if (Math.abs(i) == capWidth - 1) {
               state = (BlockState)((BlockState)state.setValue((Property)PipeBlock.PROPERTY_BY_DIRECTION.get(dir), false))
                  .setValue((Property)PipeBlock.PROPERTY_BY_DIRECTION.get(dir.getOpposite()), false);
            }

            this.setBlockIfOk(
               changedMushrooms,
               world,
               pos.relative(dir, i).relative(dir.getClockWise(), i > -capWidth + 1 && i < capWidth - 1 ? capWidth + 1 : capWidth).above(height - 1),
               state
            );
         }
      }

      AxisDirection lowerSide = rand.nextBoolean() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
      int baseBranchHeight = height - 5 - rand.nextInt(2);

      for (Direction dir : rand.nextBoolean() ? new Direction[]{Direction.NORTH, Direction.SOUTH} : new Direction[]{Direction.EAST, Direction.WEST}) {
         int y = baseBranchHeight;
         if (dir.getAxisDirection() == lowerSide) {
            y = Math.max(y - rand.nextInt(2) - 2, 2);
         }

         int branchLength = 2;

         for (int i = 1; i <= branchLength; i++) {
            this.setBlockIfOk(changedMushrooms, world, pos.relative(dir, i).above(y), (BlockState)STEM.get());
         }

         BlockPos branchCapPos = pos.relative(dir, rand.nextBoolean() ? branchLength : branchLength + 1);
         this.setBlockIfOk(changedMushrooms, world, branchCapPos.above(y + 1), (BlockState)STEM.get());
         int branchCapWidth = 1;

         for (int x = -branchCapWidth; x <= branchCapWidth; x++) {
            for (int z = -branchCapWidth; z <= branchCapWidth; z++) {
               this.setBlockIfOk(
                  changedMushrooms, world, branchCapPos.offset(x, y + 2, z), (BlockState)((BlockState)CAP.get()).setValue(HugeMushroomBlock.DOWN, false)
               );
            }
         }
      }

      return true;
   }

   @Override
   public boolean isValidGround(WorldGenLevel world, BlockState state, BlockPos pos) {
      if (super.isValidGround(world, state, pos)) {
         return true;
      }

      if (world.getBlockState(pos).getFluidState().is(FluidTags.WATER)) {
         int depth = 0;

         while (depth <= 2 && world.getBlockState(pos.below(depth)).getFluidState().is(FluidTags.WATER)) {
            depth++;
         }

         if (depth <= 2) {
            return true;
         }
      }

      return false;
   }
}
