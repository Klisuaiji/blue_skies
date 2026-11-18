package com.legacy.blue_skies.blocks.natural;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LargeLilyPadBlock extends BushBlock {
   public static final MapCodec<LargeLilyPadBlock> CODEC = simpleCodec(LargeLilyPadBlock::new);
   public static final EnumProperty<LargeLilyPadBlock.Corner> CORNER = EnumProperty.create("corner", LargeLilyPadBlock.Corner.class);
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
   private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

   public LargeLilyPadBlock(Properties builder) {
      super(builder);
      this.registerDefaultState(
         (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, Direction.NORTH)).setValue(CORNER, LargeLilyPadBlock.Corner.BOTTOM_L)
      );
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   protected boolean mayPlaceOn(BlockState groundState, BlockGetter world, BlockPos groundPos) {
      return (groundState.getFluidState().getType() == Fluids.WATER || groundState.is(BlockTags.ICE))
         && world.getFluidState(groundPos.above()).getType() == Fluids.EMPTY;
   }

   public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
      if (state.is(this) && super.canSurvive(state, world, pos)) {
         LargeLilyPadBlock.Corner corner = (LargeLilyPadBlock.Corner)state.getValue(CORNER);
         Direction facing = (Direction)state.getValue(FACING);
         switch (corner) {
            case BOTTOM_L:
               for (LargeLilyPadBlock.Corner c : new LargeLilyPadBlock.Corner[]{
                  LargeLilyPadBlock.Corner.TOP_L, LargeLilyPadBlock.Corner.TOP_R, LargeLilyPadBlock.Corner.BOTTOM_R
               }) {
                  BlockPos offsetPos = c.getOffset(pos, facing);
                  BlockState offsetState = world.getBlockState(offsetPos);
                  if (offsetState.getBlock() != this || !super.canSurvive(offsetState, world, offsetPos)) {
                     return false;
                  }
               }

               return true;
            default:
               BlockPos offsetPos = switch (corner) {
                  case BOTTOM_R -> pos.relative(facing.getClockWise().getClockWise().getClockWise());
                  case TOP_R -> pos.relative(facing.getOpposite()).relative(facing.getOpposite().getClockWise());
                  case TOP_L -> pos.relative(facing.getOpposite());
                  default -> pos;
               };
               return this.canSurvive(world.getBlockState(offsetPos), world, offsetPos);
         }
      } else {
         return false;
      }
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      Direction facing = context.getHorizontalDirection();

      for (LargeLilyPadBlock.Corner corner : new LargeLilyPadBlock.Corner[]{
         LargeLilyPadBlock.Corner.TOP_L, LargeLilyPadBlock.Corner.TOP_R, LargeLilyPadBlock.Corner.BOTTOM_R
      }) {
         LevelAccessor world = context.getLevel();
         BlockPos offsetPos = corner.getOffset(context.getClickedPos(), facing);
         if (!world.getBlockState(offsetPos).canBeReplaced(context) || !this.mayPlaceOn(world.getBlockState(offsetPos.below()), world, offsetPos.below())) {
            return null;
         }
      }

      return (BlockState)this.defaultBlockState().setValue(FACING, facing);
   }

   public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      Direction facing = (Direction)state.getValue(FACING);

      for (LargeLilyPadBlock.Corner corner : new LargeLilyPadBlock.Corner[]{
         LargeLilyPadBlock.Corner.TOP_L, LargeLilyPadBlock.Corner.TOP_R, LargeLilyPadBlock.Corner.BOTTOM_R
      }) {
         world.setBlock(corner.getOffset(pos, facing), (BlockState)state.setValue(CORNER, corner), 27);
      }
   }

   public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation) {
      return this.rotate(state, rotation);
   }

   public BlockState rotate(BlockState state, Rotation rotation) {
      return (BlockState)((BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))))
         .setValue(CORNER, ((LargeLilyPadBlock.Corner)state.getValue(CORNER)).rotate(rotation));
   }

   public BlockState mirror(BlockState state, Mirror mirror) {
      return this.rotate(state, mirror.getRotation((Direction)state.getValue(FACING)));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, CORNER});
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.DESTROY;
   }

   protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
   }

   public static enum Corner implements StringRepresentable {
      BOTTOM_L("bottom_left"),
      BOTTOM_R("bottom_right"),
      TOP_L("top_left"),
      TOP_R("top_right");

      private final String name;

      private Corner(String name) {
         this.name = name;
      }

      public BlockPos getOffset(BlockPos pos, Direction facing) {
         switch (this) {
            case BOTTOM_R:
               return pos.relative(facing.getClockWise());
            case TOP_R:
               return pos.relative(facing).relative(facing.getClockWise());
            case TOP_L:
               return pos.relative(facing);
            default:
               return pos;
         }
      }

      public LargeLilyPadBlock.Corner rotate(Rotation rotation) {
         LargeLilyPadBlock.Corner corner = this;

         for (int i = 0; i < rotation.ordinal(); i++) {
            corner = corner.rotate90();
         }

         return corner;
      }

      public LargeLilyPadBlock.Corner rotate90() {
         switch (this) {
            case BOTTOM_R:
               return TOP_R;
            case TOP_R:
            default:
               return TOP_L;
            case TOP_L:
               return BOTTOM_L;
            case BOTTOM_L:
               return BOTTOM_R;
         }
      }

      public LargeLilyPadBlock.Corner mirror(Mirror mirror, Direction stateFacing) {
         switch (this) {
            case BOTTOM_R:
               return BOTTOM_L;
            case TOP_R:
            default:
               return TOP_L;
            case TOP_L:
               return TOP_R;
            case BOTTOM_L:
               return BOTTOM_R;
         }
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}
