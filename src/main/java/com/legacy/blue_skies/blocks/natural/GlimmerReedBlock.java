package com.legacy.blue_skies.blocks.natural;

import com.mojang.serialization.MapCodec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class GlimmerReedBlock extends BushBlock implements SimpleWaterloggedBlock {
   public static final MapCodec<GlimmerReedBlock> CODEC = simpleCodec(GlimmerReedBlock::new);
   public static final BooleanProperty TOP = BooleanProperty.create("top");
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public GlimmerReedBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(TOP, true)).setValue(WATERLOGGED, false));
   }

   public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
      return super.playerWillDestroy(worldIn, pos, state, player);
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
      return super.mayPlaceOn(state, world, pos) || state.getBlock() == this;
   }

   public boolean isRandomlyTicking(BlockState state) {
      return (Boolean)state.getValue(TOP);
   }

   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
      if (world.isLoaded(pos)) {
         int maxHeight = new Random(new BlockPos(pos.getX(), 0, pos.getZ()).asLong()).nextInt(2) + 2;
         int height = 0;
         height = 0;

         while (height <= maxHeight && world.getBlockState(pos.below(height)).getBlock() == this && world.getFluidState(pos.below(height)).isEmpty()) {
            height++;
         }

         BlockPos abovePos = pos.above();
         BlockState aboveState = world.getBlockState(abovePos);
         if (height < maxHeight && (aboveState.isAir() || aboveState.getBlock() == Blocks.WATER)) {
            world.setBlockAndUpdate(abovePos, (BlockState)this.defaultBlockState().setValue(WATERLOGGED, aboveState.getFluidState().is(FluidTags.WATER)));
         }
      }
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }

      if (facing == Direction.UP) {
         world.setBlock(currentPos, (BlockState)state.setValue(TOP, facingState.getBlock() != this), 3);
      }

      return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{TOP, WATERLOGGED});
   }

   protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
   }
}
