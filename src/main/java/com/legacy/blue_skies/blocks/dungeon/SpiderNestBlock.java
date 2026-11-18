package com.legacy.blue_skies.blocks.dungeon;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;

public class SpiderNestBlock extends DirectionalBlock {
   public static final MapCodec<SpiderNestBlock> CODEC = simpleCodec(SpiderNestBlock::new);

   public SpiderNestBlock(Properties builder) {
      super(builder);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(FACING, Direction.NORTH));
   }

   public static <T extends Entity> void spawnSpiders(BlockState state, Level worldIn, BlockPos pos, EntityType<T> entityType) {
      BlockPos spawnPos = pos.relative((Direction)state.getValue(FACING), 2);
      T entity = (T)entityType.create(worldIn);
      entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
      entity.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 0.5F);
      worldIn.addFreshEntity(entity);
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public BlockState rotate(BlockState state, Rotation rotation) {
      return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
   }

   public BlockState mirror(BlockState state, Mirror mirrorIn) {
      return state.rotate(mirrorIn.getRotation((Direction)state.getValue(FACING)));
   }

   protected MapCodec<? extends DirectionalBlock> codec() {
      return CODEC;
   }
}
