package com.legacy.blue_skies.blocks.dungeon;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkyDecayingSpikeBlock extends Block {
   private static final VoxelShape DOWNWARD_SHAPE = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 11.0, 14.0), Block.box(3.0, 0.0, 3.0, 13.0, 13.0, 13.0));
   private static final VoxelShape UPWARD_SHAPE = Shapes.or(Block.box(2.0, 5.0, 2.0, 14.0, 16.0, 14.0), Block.box(3.0, 3.0, 3.0, 13.0, 13.0, 13.0));
   public static final BooleanProperty DOWNWARD = BooleanProperty.create("downward");
   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

   public SkyDecayingSpikeBlock() {
      super(Properties.ofFullCopy(Blocks.COBBLESTONE).randomTicks());
      this.registerDefaultState((BlockState)((BlockState)super.defaultBlockState().setValue(DOWNWARD, true)).setValue(WATERLOGGED, false));
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return state.getValue(DOWNWARD) ? DOWNWARD_SHAPE : UPWARD_SHAPE;
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{DOWNWARD, WATERLOGGED});
   }

   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      level.destroyBlock(pos, false);
   }

   public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
      if ((Boolean)stateIn.getValue(WATERLOGGED)) {
         level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }

      int i = level.getRandom().nextInt(100) + 60;
      level.scheduleTick(pos, this, i);
      return stateIn;
   }

   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
      if (oldState.getBlock() != state.getBlock()) {
         int i = level.random.nextInt(100) + 60;
         level.scheduleTick(pos, this, i);
      }
   }

   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
   }
}
