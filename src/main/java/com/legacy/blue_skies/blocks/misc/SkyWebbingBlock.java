package com.legacy.blue_skies.blocks.misc;

import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkyWebbingBlock extends Block {
   public static final BooleanProperty NORTH = BooleanProperty.create("north");
   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
   public static final BooleanProperty EAST = BooleanProperty.create("east");
   public static final BooleanProperty WEST = BooleanProperty.create("west");
   public static final BooleanProperty UP = BooleanProperty.create("up");
   public static final BooleanProperty DOWN = BooleanProperty.create("down");
   public static final BooleanProperty HAS_COLLISION = BooleanProperty.create("has_collision");
   public static final VoxelShape X = Block.box(7.5, 0.0, 0.0, 8.5, 16.0, 16.0);
   public static final VoxelShape Y = Block.box(0.0, 7.5, 0.0, 16.0, 8.5, 16.0);
   public static final VoxelShape Z = Block.box(0.0, 0.0, 7.5, 16.0, 16.0, 8.5);

   public SkyWebbingBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(
         (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(NORTH, false))
                           .setValue(SOUTH, false))
                        .setValue(EAST, false))
                     .setValue(WEST, false))
                  .setValue(UP, false))
               .setValue(DOWN, false))
            .setValue(HAS_COLLISION, false)
      );
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      boolean xFlag = false;
      boolean yFlag = false;
      boolean zFlag = false;
      if (this.connectedAtCorner(state, NORTH, UP, SOUTH, DOWN)) {
         xFlag = true;
      }

      if (this.connectedAtCorner(state, NORTH, EAST, SOUTH, WEST)) {
         yFlag = true;
      }

      if (this.connectedAtCorner(state, EAST, UP, WEST, DOWN)) {
         zFlag = true;
      }

      if (xFlag && yFlag && zFlag) {
         return Shapes.block();
      } else {
         VoxelShape shape = Shapes.empty();
         if (xFlag) {
            shape = Shapes.or(shape, X);
         }

         if (yFlag) {
            shape = Shapes.or(shape, Y);
         }

         if (zFlag) {
            shape = Shapes.or(shape, Z);
         }

         return shape;
      }
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return state.getValue(HAS_COLLISION) ? this.getShape(state, worldIn, pos, context) : Shapes.empty();
   }

   public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
      if (!(Boolean)state.getValue(HAS_COLLISION)) {
         VoxelShape shape = state.getShape(worldIn, pos);
         if (!shape.isEmpty() && shape.bounds().move(pos).intersects(entityIn.getBoundingBox())) {
            entityIn.makeStuckInBlock(state, new Vec3(0.75, 0.55, 0.75));
         }
      }
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
      BlockState updatedState = this.getStateAtLocation(state, level, currentPos);
      return !this.canSurvive(updatedState, level, currentPos) ? Blocks.AIR.defaultBlockState() : updatedState;
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return this.getStateAtLocation(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
   }

   public BlockState getStateAtLocation(BlockState state, LevelReader level, BlockPos pos) {
      for (Direction facing : Direction.values()) {
         BlockPos facingPos = pos.relative(facing);
         BlockState facingState = level.getBlockState(facingPos);
         state = (BlockState)state.setValue(
            this.getProperty(facing), this.canAttachTo(facingState, facingState.isFaceSturdy(level, facingPos, facing.getOpposite()))
         );
      }

      return state;
   }

   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      return this.connectedAtCorner(state, NORTH, EAST, SOUTH, WEST)
         || this.connectedAtCorner(state, NORTH, UP, SOUTH, DOWN)
         || this.connectedAtCorner(state, EAST, UP, WEST, DOWN);
   }

   public boolean connectedAtCorner(BlockState state, BooleanProperty a, BooleanProperty b, BooleanProperty c, BooleanProperty d) {
      return (Boolean)state.getValue(a) && (Boolean)state.getValue(b)
         || (Boolean)state.getValue(b) && (Boolean)state.getValue(c)
         || (Boolean)state.getValue(c) && (Boolean)state.getValue(d)
         || (Boolean)state.getValue(d) && (Boolean)state.getValue(a);
   }

   public final boolean canAttachTo(BlockState attachState, boolean isSolidSide) {
      return isSolidSide && !isExceptionForConnection(attachState) || attachState.is(SkiesBlockTags.WEBBING_ATTACHES_TO);
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return true;
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.DESTROY;
   }

   public BlockState rotate(BlockState state, Rotation rotation) {
      switch (rotation) {
         case CLOCKWISE_180:
            return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(SOUTH)))
                     .setValue(EAST, (Boolean)state.getValue(WEST)))
                  .setValue(SOUTH, (Boolean)state.getValue(NORTH)))
               .setValue(WEST, (Boolean)state.getValue(EAST));
         case COUNTERCLOCKWISE_90:
            return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(EAST)))
                     .setValue(EAST, (Boolean)state.getValue(SOUTH)))
                  .setValue(SOUTH, (Boolean)state.getValue(WEST)))
               .setValue(WEST, (Boolean)state.getValue(NORTH));
         case CLOCKWISE_90:
            return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(WEST)))
                     .setValue(EAST, (Boolean)state.getValue(NORTH)))
                  .setValue(SOUTH, (Boolean)state.getValue(EAST)))
               .setValue(WEST, (Boolean)state.getValue(SOUTH));
         default:
            return state;
      }
   }

   public BlockState mirror(BlockState state, Mirror mirrorIn) {
      switch (mirrorIn) {
         case LEFT_RIGHT:
            return (BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(SOUTH))).setValue(SOUTH, (Boolean)state.getValue(NORTH));
         case FRONT_BACK:
            return (BlockState)((BlockState)state.setValue(EAST, (Boolean)state.getValue(WEST))).setValue(WEST, (Boolean)state.getValue(EAST));
         default:
            return state;
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{NORTH, SOUTH, EAST, WEST, UP, DOWN, HAS_COLLISION});
   }

   public BooleanProperty getProperty(Direction facing) {
      switch (facing) {
         case NORTH:
            return NORTH;
         case SOUTH:
            return SOUTH;
         case EAST:
            return EAST;
         case WEST:
            return WEST;
         case UP:
            return UP;
         default:
            return DOWN;
      }
   }
}
