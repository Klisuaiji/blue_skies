package com.legacy.blue_skies.blocks.dungeon;

import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PoisonKeystoneBlock extends KeystoneBlock.DirectionalKeystoneBlock {
   public static final MapCodec<PoisonKeystoneBlock> CODEC = MapCodec.unit(PoisonKeystoneBlock::new);
   public static final VoxelShape SHAPE_Z = Shapes.or(Block.box(0.0, 0.0, 7.5, 16.0, 16.0, 8.5), Block.box(4.0, 4.0, 7.0, 12.0, 12.0, 9.0));
   public static final VoxelShape SHAPE_X = Shapes.or(Block.box(7.5, 0.0, 0.0, 8.5, 16.0, 16.0), Block.box(7.0, 4.0, 4.0, 9.0, 12.0, 12.0));

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      switch ((Direction)state.getValue(FACING)) {
         case NORTH:
            return SHAPE_Z;
         case SOUTH:
            return SHAPE_Z;
         case EAST:
            return SHAPE_X;
         default:
            return SHAPE_X;
      }
   }

   public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return this.getShape(state, worldIn, pos, context);
   }

   @Override
   public boolean teleportPlayer(KeystoneBlockEntity tile, BlockState state, Level worldIn, BlockPos pos, Player player, Direction clickDirection) {
      if (tile instanceof KeystoneBlockEntity.PoisonKeystoneBlockEntity) {
         ((KeystoneBlockEntity.PoisonKeystoneBlockEntity)tile).teleportPlayer(player, clickDirection, (Direction)state.getValue(FACING));
         return true;
      } else {
         return super.teleportPlayer(tile, state, worldIn, pos, player, clickDirection);
      }
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new KeystoneBlockEntity.PoisonKeystoneBlockEntity(pos, state);
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return true;
   }

   public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
      return true;
   }

   protected MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }
}
