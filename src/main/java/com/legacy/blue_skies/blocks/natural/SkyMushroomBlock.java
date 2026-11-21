package com.legacy.blue_skies.blocks.natural;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkyMushroomBlock extends BushBlock {
   public static final MapCodec<SkyMushroomBlock> CODEC = simpleCodec(SkyMushroomBlock::new);
   protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);

   public SkyMushroomBlock(Properties properties) {
      super(properties);
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
      return super.mayPlaceOn(state, worldIn, pos) || state.is(BlockTags.MUSHROOM_GROW_BLOCK);
   }

   protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
   }
}
