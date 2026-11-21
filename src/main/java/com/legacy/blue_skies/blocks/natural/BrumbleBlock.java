package com.legacy.blue_skies.blocks.natural;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrumbleBlock extends SeagrassBlock {
   public BrumbleBlock(Properties properties) {
      super(properties);
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      Vec3 offset = state.getOffset(level, pos);
      return super.getShape(state, level, pos, context).move(offset.x(), offset.y(), offset.z());
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return true;
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      for (int attempts = rand.nextInt(3) + 7; attempts > 0; attempts--) {
         BlockPos offset = pos.offset(rand.nextInt(7) - 3, 0, rand.nextInt(7) - 3);

         for (int y = -1; y <= 1; y++) {
            BlockPos upOffset = offset.above(y);
            BlockState placementState = this.defaultBlockState();
            if (level.getBlockState(upOffset).getBlock() == Blocks.WATER && this.canSurvive(placementState, level, upOffset)) {
               level.setBlockAndUpdate(upOffset, placementState);
               break;
            }
         }
      }
   }
}
