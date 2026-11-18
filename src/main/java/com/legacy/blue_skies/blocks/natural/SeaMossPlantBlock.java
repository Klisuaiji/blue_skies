package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.structure_gel.api.util.GelCollectors;
import com.legacy.structure_gel.api.util.VoxelShapeUtil;
import com.mojang.serialization.MapCodec;
import java.util.Map;
import javax.annotation.Nullable;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.IShearable;

public class SeaMossPlantBlock extends BushBlock implements LiquidBlockContainer, BonemealableBlock, IShearable {
   public static final MapCodec<SeaMossPlantBlock> CODEC = simpleCodec(SeaMossPlantBlock::new);
   private static final VoxelShape MAIN_SHAPE = Block.box(0.0, 4.0, 0.0, 16.0, 7.0, 16.0);
   private static final VoxelShape NORTH_SHAPE = Block.box(0.0, 7.0, 0.0, 16.0, 16.0, 2.0);
   private static final VoxelShape SOUTH_SHAPE = VoxelShapeUtil.rotate(NORTH_SHAPE, Direction.SOUTH);
   private static final VoxelShape EAST_SHAPE = VoxelShapeUtil.rotate(NORTH_SHAPE, Direction.EAST);
   private static final VoxelShape WEST_SHAPE = VoxelShapeUtil.rotate(NORTH_SHAPE, Direction.WEST);
   public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
   public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
   public static final BooleanProperty EAST = BlockStateProperties.EAST;
   public static final BooleanProperty WEST = BlockStateProperties.WEST;
   private static final Map<Direction, BooleanProperty> DIRECTION_TO_PROP = GelCollectors.mapOf(
      Direction.NORTH, NORTH, new Object[]{Direction.SOUTH, SOUTH, Direction.EAST, EAST, Direction.WEST, WEST}
   );
   private static final Map<Direction, VoxelShape> DIRECTION_TO_SHAPE = GelCollectors.mapOf(
      Direction.NORTH, NORTH_SHAPE, new Object[]{Direction.SOUTH, SOUTH_SHAPE, Direction.EAST, EAST_SHAPE, Direction.WEST, WEST_SHAPE}
   );

   public SeaMossPlantBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(NORTH, false)).setValue(SOUTH, false)).setValue(EAST, false))
            .setValue(WEST, false)
      );
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      VoxelShape shape = MAIN_SHAPE;

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         if ((Boolean)state.getValue((Property)DIRECTION_TO_PROP.get(dir))) {
            shape = Shapes.or(shape, DIRECTION_TO_SHAPE.get(dir));
         }
      }

      return shape;
   }

   public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
      return Shapes.empty();
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
      return state.isFaceSturdy(world, pos, Direction.UP) && !state.is(Blocks.MAGMA_BLOCK);
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8
         ? this.getStateForPlacement(this.defaultBlockState(), context.getLevel(), context.getClickedPos())
         : null;
   }

   public BlockState getStateForPlacement(BlockState state, LevelAccessor world, BlockPos pos) {
      for (Direction dir : DirectionUtil.HORIZONTAL) {
         BlockPos offsetPos = pos.relative(dir);
         state = (BlockState)state.setValue((Property)DIRECTION_TO_PROP.get(dir), world.getBlockState(offsetPos).isFaceSturdy(world, offsetPos, dir));
      }

      return state;
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
      BlockState newState = super.updateShape(state, facing, facingState, world, currentPos, facingPos);
      if (newState.getBlock() == this) {
         world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
         return this.getStateForPlacement(newState, world, currentPos);
      } else {
         return newState;
      }
   }

   public FluidState getFluidState(BlockState state) {
      return Fluids.WATER.getSource(false);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{NORTH, SOUTH, EAST, WEST});
   }

   public boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
      return false;
   }

   public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
      return false;
   }

   public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
      return true;
   }

   public boolean isBonemealSuccess(Level world, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel world, RandomSource rand, BlockPos pos, BlockState state) {
      for (int attempts = rand.nextInt(3) + 7; attempts > 0; attempts--) {
         BlockPos offset = pos.offset(rand.nextInt(7) - 3, 0, rand.nextInt(7) - 3);

         for (int y = -1; y <= 1; y++) {
            BlockPos upOffset = offset.above(y);
            BlockState placementState = this.getStateForPlacement(state, world, upOffset);
            if (world.getBlockState(upOffset).getBlock() == Blocks.WATER && this.canSurvive(placementState, world, upOffset)) {
               world.setBlockAndUpdate(upOffset, placementState);
               break;
            }
         }
      }
   }

   protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
   }
}
