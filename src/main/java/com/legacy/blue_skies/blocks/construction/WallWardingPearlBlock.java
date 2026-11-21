package com.legacy.blue_skies.blocks.construction;

import com.legacy.structure_gel.api.util.VoxelShapeUtil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallWardingPearlBlock extends Block implements SimpleWaterloggedBlock {
   public static final DirectionProperty HORIZONTAL_FACING = HorizontalDirectionalBlock.FACING;
   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
   private static final VoxelShape NORTH = Shapes.or(Block.box(5.0, 2.0, 8.0, 11.0, 9.0, 14.0), Block.box(6.0, 9.0, 9.0, 10.0, 10.0, 13.0));
   private static final VoxelShape SOUTH = VoxelShapeUtil.rotate(NORTH, Direction.SOUTH);
   private static final VoxelShape EAST = VoxelShapeUtil.rotate(NORTH, Direction.EAST);
   private static final VoxelShape WEST = VoxelShapeUtil.rotate(NORTH, Direction.WEST);

   public WallWardingPearlBlock(Properties properties) {
      super(properties);
      this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH)).setValue(WATERLOGGED, false));
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      switch ((Direction)state.getValue(HORIZONTAL_FACING)) {
         case NORTH:
            return NORTH;
         case SOUTH:
            return SOUTH;
         case EAST:
            return EAST;
         default:
            return WEST;
      }
   }

   public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      Direction direction = (Direction)state.getValue(HORIZONTAL_FACING);
      BlockPos attachedPos = pos.relative(direction.getOpposite());
      BlockState attachedState = world.getBlockState(attachedPos);
      return attachedState.isFaceSturdy(world, attachedPos, direction);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      BlockState state = this.defaultBlockState();
      Level world = context.getLevel();
      BlockPos pos = context.getClickedPos();

      for (Direction dir : context.getNearestLookingDirections()) {
         if (dir.getAxis().isHorizontal()) {
            Direction oppositeDir = dir.getOpposite();
            state = (BlockState)state.setValue(HORIZONTAL_FACING, oppositeDir);
            if (state.canSurvive(world, pos)) {
               return (BlockState)state.setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
            }
         }
      }

      return null;
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      if ((Boolean)state.getValue(WATERLOGGED)) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
      }

      return facing.getOpposite() == state.getValue(HORIZONTAL_FACING) && !state.canSurvive(world, currentPos) ? Blocks.AIR.defaultBlockState() : state;
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
   }

   public BlockState rotate(BlockState state, Rotation rot) {
      return (BlockState)state.setValue(HORIZONTAL_FACING, rot.rotate((Direction)state.getValue(HORIZONTAL_FACING)));
   }

   public BlockState mirror(BlockState state, Mirror mirrorIn) {
      return state.rotate(mirrorIn.getRotation((Direction)state.getValue(HORIZONTAL_FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{HORIZONTAL_FACING, WATERLOGGED});
   }
}
