package com.legacy.blue_skies.blocks.natural;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public class SkyDoubleCropBlock extends SkyCropBlock {
   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

   public SkyDoubleCropBlock(Supplier<? extends Item> seed, double height) {
      super(seed, height);
      this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(SkyCropBlock.AGE, 0)).setValue(HALF, DoubleBlockHalf.LOWER));
   }

   @Override
   public VoxelShape[] generateShapes(double height) {
      int ages = this.getAgeProperty().getPossibleValues().size();
      VoxelShape[] shapes = new VoxelShape[ages];

      for (int i = 0; i < ages; i++) {
         shapes[i] = Block.box(4.0, 0.0, 4.0, 12.0, (double)(i + 1) / ages * height, 12.0);
      }

      return shapes;
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return !this.isMaxAge(state) || this.isMaxAge(state) && this.isLowerHalf(state);
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if (level.isLoaded(pos)) {
         if (level.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
               float f = getGrowthSpeed(this, level, pos);
               if (CommonHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                  level.setBlock(pos, this.withState(i + 1, (DoubleBlockHalf)state.getValue(HALF)), 2);
                  CommonHooks.onCropsGrowPost(level, pos, state);
               }
            }

            if (this.isMaxAge(state) && this.isLowerHalf(state) && level.isEmptyBlock(pos.above())) {
               float f = getGrowthSpeed(this, level, pos);
               if (CommonHooks.onCropsGrowPre(level, pos.above(), state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                  level.setBlock(pos.above(), this.withState(0, DoubleBlockHalf.UPPER), 2);
                  CommonHooks.onCropsGrowPost(level, pos.above(), state);
               }
            }
         }
      }
   }

   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
      DoubleBlockHalf doubleblockhalf = (DoubleBlockHalf)state.getValue(HALF);
      BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
      BlockState blockstate = level.getBlockState(blockpos);
      if (blockstate.getBlock() == this && blockstate.getValue(HALF) != doubleblockhalf) {
         level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
         level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
         if (!level.isClientSide && !player.isCreative()) {
            dropResources(state, level, pos, (BlockEntity)null, player, player.getMainHandItem());
            dropResources(blockstate, level, blockpos, (BlockEntity)null, player, player.getMainHandItem());
         }
      }

      return super.playerWillDestroy(level, pos, state, player);
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return state.isFertile(level, pos) || state.getBlock() instanceof FarmBlock;
   }

   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      if (this.isLowerHalf(state)) {
         return super.canSurvive(state, level, pos);
      }

      if (this.isLowerHalf(state)) {
         return false;
      }

      BlockState downState = level.getBlockState(pos.below());
      return downState.getBlock() == this
         && this.isLowerHalf(downState)
         && this.isMaxAge(downState)
         && (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos));
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      BlockState stateAbove = level.getBlockState(pos.above());
      return !this.isMaxAge(state)
         || this.isMaxAge(state) && this.isLowerHalf(state) && (stateAbove.getBlock() == this && !this.isMaxAge(stateAbove) || stateAbove.isAir());
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      int i = this.getAge(state) + this.getBonemealAgeIncrease(level);
      int j = this.getMaxAge();
      if (i > j) {
         i = j;
      }

      level.setBlock(pos, this.withState(i, (DoubleBlockHalf)state.getValue(HALF)), 2);
      if (this.isLowerHalf(state) && this.isMaxAge(state)) {
         BlockState stateAbove = level.getBlockState(pos.above());
         if (level.isEmptyBlock(pos.above())) {
            level.setBlockAndUpdate(pos.above(), this.withState(0, DoubleBlockHalf.UPPER));
         } else if (stateAbove.getBlock() == this && this.getAge(stateAbove) < this.getMaxAge()) {
            i = this.getAge(stateAbove) + this.getBonemealAgeIncrease(level);
            if (i > j) {
               i = j;
            }

            level.setBlockAndUpdate(pos.above(), this.withState(i, DoubleBlockHalf.UPPER));
         }
      }
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{HALF});
   }

   private BlockState withState(int age, DoubleBlockHalf half) {
      return (BlockState)((BlockState)this.defaultBlockState().setValue(SkyCropBlock.AGE, age)).setValue(HALF, half);
   }

   private boolean isLowerHalf(BlockState state) {
      return state.getBlock() == this && ((DoubleBlockHalf)state.getValue(HALF)).equals(DoubleBlockHalf.LOWER);
   }
}
