package com.legacy.blue_skies.blocks.construction;

import com.legacy.structure_gel.api.util.VoxelShapeUtil;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallMoonstoneBlock extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock {
   public static final MapCodec<WallMoonstoneBlock> CODEC = simpleCodec(WallMoonstoneBlock::new);
   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape FLOOR = Block.box(4.0, 0.0, 4.0, 12.0, 2.0, 12.0);
   private static final VoxelShape CEILING = VoxelShapeUtil.mirror(FLOOR, Axis.Y);
   private static final VoxelShape NORTH = Block.box(4.0, 4.0, 14.0, 12.0, 12.0, 16.0);
   private static final VoxelShape SOUTH = VoxelShapeUtil.rotate(NORTH, Direction.SOUTH);
   private static final VoxelShape EAST = VoxelShapeUtil.rotate(NORTH, Direction.EAST);
   private static final VoxelShape WEST = VoxelShapeUtil.rotate(NORTH, Direction.WEST);

   public WallMoonstoneBlock(Properties builder) {
      super(builder);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(FACE, AttachFace.WALL))
            .setValue(WATERLOGGED, false)
      );
   }

   public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
      return isValidFace(worldIn, pos, getConnectedDirection(state).getOpposite());
   }

   public static boolean isValidFace(LevelReader worldIn, BlockPos pos, Direction direction) {
      BlockPos blockPos = pos.relative(direction);
      return direction.getAxis() != Axis.Y || !worldIn.getBlockState(blockPos).is(BlockTags.FENCES) && !worldIn.getBlockState(blockPos).is(BlockTags.WALLS)
         ? worldIn.getBlockState(blockPos).isFaceSturdy(worldIn, blockPos, direction.getOpposite())
         : true;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACE, FACING, WATERLOGGED});
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      switch ((AttachFace)state.getValue(FACE)) {
         case WALL:
            switch ((Direction)state.getValue(FACING)) {
               case NORTH:
                  return NORTH;
               case SOUTH:
                  return SOUTH;
               case EAST:
                  return EAST;
               default:
                  return WEST;
            }
         case CEILING:
            return CEILING;
         default:
            return FLOOR;
      }
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
   }

   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)stateIn.getValue(WATERLOGGED)) {
         worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
      }

      return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      for (Direction direction : context.getNearestLookingDirections()) {
         BlockState blockstate;
         if (direction.getAxis() == Axis.Y) {
            blockstate = (BlockState)((BlockState)this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR))
               .setValue(FACING, context.getHorizontalDirection());
         } else {
            blockstate = (BlockState)((BlockState)this.defaultBlockState().setValue(FACE, AttachFace.WALL)).setValue(FACING, direction.getOpposite());
         }

         if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
            return (BlockState)blockstate.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
         }
      }

      return null;
   }

   protected MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
      return CODEC;
   }
}
