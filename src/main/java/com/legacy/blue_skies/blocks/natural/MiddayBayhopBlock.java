package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MiddayBayhopBlock extends FlowerBlock {
   private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);

   public MiddayBayhopBlock(MobEffect effect, int effectDuration, Properties properties) {
      super(effect, effectDuration, properties);
   }

   public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      Vec3 offset = state.getOffset(world, pos);
      return SHAPE.move(offset.x, offset.y, offset.z);
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
      Block block = state.getBlock();
      return state.is(BlockTags.SAND) || block == SkiesBlocks.midnight_sand || block == SkiesBlocks.crystal_sand;
   }
}
