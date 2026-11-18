package com.legacy.blue_skies.blocks.dungeon;

import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.structure_gel.api.util.VoxelShapeUtil;
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

public class NatureKeystoneBlock extends KeystoneBlock.DirectionalKeystoneBlock {
   public static final MapCodec<NatureKeystoneBlock> CODEC = MapCodec.unit(NatureKeystoneBlock::new);
   private static final VoxelShape SHAPE = Shapes.or(Block.box(0.0, 0.0, 5.0, 16.0, 16.0, 11.0), Block.box(3.0, 2.0, 4.0, 13.0, 14.0, 12.0));

   @Override
   public boolean teleportPlayer(KeystoneBlockEntity tile, BlockState state, Level worldIn, BlockPos pos, Player player, Direction clickDirection) {
      if (tile instanceof KeystoneBlockEntity.NatureKeystoneBlockEntity) {
         ((KeystoneBlockEntity.NatureKeystoneBlockEntity)tile).teleportPlayer(player, clickDirection, (Direction)state.getValue(FACING));
         return true;
      } else {
         return super.teleportPlayer(tile, state, worldIn, pos, player, clickDirection);
      }
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return VoxelShapeUtil.rotate(SHAPE, (Direction)state.getValue(KeystoneBlock.DirectionalKeystoneBlock.FACING));
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new KeystoneBlockEntity.NatureKeystoneBlockEntity(pos, state);
   }

   protected MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }
}
