package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesItems;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FruitBlock extends Block {
   private static final VoxelShape SHAPE = Block.box(4.0, 7.0, 4.0, 12.0, 16.0, 12.0);
   private Supplier<LeavesBlock> leaves;

   public FruitBlock(Supplier<LeavesBlock> leaves) {
      super(Properties.of().ignitedByLava().noCollission().strength(0.0F).sound(SoundType.WOOD).offsetType(OffsetType.XZ));
      this.leaves = leaves;
   }

   public LeavesBlock getLeaves() {
      return this.leaves.get();
   }

   public boolean isOvercrowded(ServerLevel level, BlockPos pos) {
      int range = 7;
      int fruitsNear = 0;

      for (int x = -range; x <= range; x++) {
         for (int z = -range; z <= range; z++) {
            for (int y = -range; y <= range; y++) {
               if (level.getBlockState(pos.offset(x, y, z)).getBlock() == this) {
                  fruitsNear++;
               }
            }
         }
      }

      return fruitsNear > 2;
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      Vec3 offset = state.getOffset(level, pos);
      return SHAPE.move(offset.x, offset.y, offset.z);
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.DESTROY;
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
      return true;
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
      return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : state;
   }

   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      return level.getBlockState(pos.above()).getBlock() == this.getLeaves();
   }

   public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
      Item item = (Item)BuiltInRegistries.ITEM.get(BuiltInRegistries.BLOCK.getKey(this));
      return new ItemStack(item != null ? item : SkiesItems.crescent_fruit);
   }
}
