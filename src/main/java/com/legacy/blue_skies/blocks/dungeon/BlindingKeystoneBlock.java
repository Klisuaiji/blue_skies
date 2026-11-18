package com.legacy.blue_skies.blocks.dungeon;

import com.legacy.blue_skies.block_entity.KeystoneBlockEntity;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlindingKeystoneBlock extends KeystoneBlock {
   public static final MapCodec<BlindingKeystoneBlock> CODEC = MapCodec.unit(BlindingKeystoneBlock::new);
   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
      new VoxelShape[]{
         Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
         Block.box(5.0, 4.0, 5.0, 11.0, 12.0, 11.0),
         Block.box(1.0, 2.0, 1.0, 3.0, 14.0, 3.0),
         Block.box(13.0, 2.0, 13.0, 15.0, 14.0, 15.0),
         Block.box(1.0, 2.0, 13.0, 3.0, 14.0, 15.0),
         Block.box(13.0, 2.0, 1.0, 15.0, 14.0, 3.0)
      }
   );

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return (BlockEntity)(this == SkiesBlocks.bright_blinding_keystone
         ? new KeystoneBlockEntity.BrightBlindingKeystoneBlockEntity(pos, state)
         : new KeystoneBlockEntity.DawnBlindingKeystoneBlockEntity(pos, state));
   }

   protected MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }
}
