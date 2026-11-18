package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.structure_gel.api.util.VoxelShapeUtil;
import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TroughBlock extends HorizontalDirectionalBlock implements WorldlyContainerHolder {
   public static final MapCodec<TroughBlock> CODEC = simpleCodec(TroughBlock::new);
   public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 4);
   private static final Map<Direction, List<VoxelShape>> SHAPES = new HashMap<>();

   public TroughBlock(Properties builder) {
      super(builder);
      this.registerDefaultState((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, Direction.NORTH)).setValue(LEVEL, 0));
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return SHAPES.get(state.getValue(FACING)).get((Integer)state.getValue(LEVEL));
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return SHAPES.get(state.getValue(FACING)).get(0);
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      int level = (Integer)state.getValue(LEVEL);
      ItemStack heldStack = player.getItemInHand(hand);
      if (level < 4 && canUseItem(heldStack)) {
         if (!world.isClientSide) {
            attemptFill(state, world, pos, heldStack);
            if (!player.getAbilities().instabuild) {
               heldStack.shrink(1);
            }
         }

         return InteractionResult.sidedSuccess(world.isClientSide);
      } else {
         return InteractionResult.PASS;
      }
   }

   public static boolean attemptFill(BlockState state, LevelAccessor world, BlockPos pos, ItemStack fillWithStack) {
      if (canUseItem(fillWithStack) && world.getRandom().nextFloat() < 0.4F) {
         BlockState newState = (BlockState)state.setValue(LEVEL, Math.min(4, (Integer)state.getValue(LEVEL) + 1));
         world.setBlock(pos, newState, 3);
         world.levelEvent(1500, pos, 1);
         return true;
      } else {
         world.levelEvent(1500, pos, 0);
         return false;
      }
   }

   public static boolean canConsume(BlockState state, LevelAccessor world) {
      return (Integer)state.getValue(LEVEL) > 0;
   }

   public static boolean attemptConsume(BlockState state, LevelAccessor world, BlockPos pos) {
      if (canConsume(state, world)) {
         BlockState newState = (BlockState)state.setValue(LEVEL, Math.max(0, (Integer)state.getValue(LEVEL) - 1));
         world.setBlock(pos, newState, 3);
         return true;
      } else {
         return false;
      }
   }

   public static boolean canUseItem(ItemStack item) {
      return item.is(SkiesItemTags.TROUGH_FOODS);
   }

   public boolean hasAnalogOutputSignal(BlockState state) {
      return true;
   }

   public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
      return (Integer)state.getValue(LEVEL);
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING, LEVEL});
   }

   public WorldlyContainer getContainer(BlockState state, LevelAccessor world, BlockPos pos) {
      return new TroughBlock.TroughInventory(state, world, pos);
   }

   protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
      return CODEC;
   }

   static {
      VoxelShape outside = Block.box(0.0, 0.0, 2.0, 16.0, 10.0, 14.0);

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         SHAPES.put(dir, new ArrayList<>());

         for (int i = 0; i <= 4; i++) {
            SHAPES.get(dir).add(VoxelShapeUtil.rotate(Shapes.join(outside, Block.box(1.0, i * 2 + 1, 3.0, 15.0, 10.0, 13.0), BooleanOp.ONLY_FIRST), dir));
         }
      }
   }

   static class TroughInventory extends SimpleContainer implements WorldlyContainer {
      private final BlockState state;
      private final int level;
      private final LevelAccessor world;
      private final BlockPos pos;
      private boolean hasInserted = false;

      protected TroughInventory(BlockState state, LevelAccessor world, BlockPos pos) {
         super(1);
         this.state = state;
         this.level = (Integer)state.getValue(TroughBlock.LEVEL);
         this.world = world;
         this.pos = pos;
      }

      public int[] getSlotsForFace(Direction dir) {
         return dir == Direction.UP ? new int[]{0} : new int[0];
      }

      public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction dir) {
         return !this.hasInserted && this.level < 4 && dir == Direction.UP && TroughBlock.canUseItem(stack);
      }

      public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction dir) {
         return false;
      }

      public void setChanged() {
         ItemStack stackInThis = this.getItem(0);
         if (!stackInThis.isEmpty()) {
            this.hasInserted = true;
            TroughBlock.attemptFill(this.state, this.world, this.pos, stackInThis);
            this.removeItemNoUpdate(0);
         }
      }
   }
}
