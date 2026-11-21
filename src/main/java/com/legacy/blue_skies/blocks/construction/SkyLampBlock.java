package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

public class SkyLampBlock extends Block implements SimpleWaterloggedBlock {
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public SkyLampBlock() {
      super(
         Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.5F)
            .sound(SkiesSounds.MOONSTONE_LANTERN)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .lightLevel(value -> 15)
      );
      this.registerDefaultState((BlockState)super.defaultBlockState().setValue(WATERLOGGED, false));
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return true;
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
   }

   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)stateIn.getValue(WATERLOGGED)) {
         worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
      }

      return stateIn;
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockPos blockpos = context.getClickedPos();
      BlockState blockstate = context.getLevel().getBlockState(blockpos);
      if (blockstate.getBlock() == this) {
         return (BlockState)blockstate.setValue(WATERLOGGED, false);
      }

      FluidState ifluidstate = context.getLevel().getFluidState(blockpos);
      return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{WATERLOGGED});
   }
}
