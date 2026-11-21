package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class MapleTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.maple_log.defaultBlockState());
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.maple_leaves.defaultBlockState());

   public MapleTreeFeature(Codec<TreeConfiguration> config, boolean natural) {
      super(config, natural);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(4) + (rand.nextFloat() < 0.2 ? 13 : 9);
      if (!this.isAreaOk(world, pos, height, 1, 5, 5)) {
         return false;
      }

      boolean branchType = rand.nextBoolean();

      for (int y = 0; y <= height; y++) {
         this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)LOG.get());
      }

      if (branchType) {
         for (int y = -2; y < 1; y++) {
            for (int x = -1; x <= 1; x++) {
               for (int z = -1; z <= 1; z++) {
                  if (y < 0) {
                     this.setBlockIfOk(changedLogs, world, pos.offset(x, y + height, z), (BlockState)LEAF.get());
                  } else if (Math.abs(x) != 1 || Math.abs(z) != 1) {
                     this.setBlockIfOk(changedLogs, world, pos.offset(x, y + height, z), (BlockState)LEAF.get());
                  }
               }
            }
         }

         this.setBlockIfOk(changedLeaves, world, pos.above(height + 1), (BlockState)LEAF.get());
         this.setBlockIfOk(changedLeaves, world, pos.above(height + 2), (BlockState)LEAF.get());
      } else {
         for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
               if (Math.abs(x) + Math.abs(z) < 3) {
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height - 5, z), (BlockState)LEAF.get());
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height - 1, z), (BlockState)LEAF.get());
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height, z), (BlockState)LEAF.get());
               }
            }
         }

         for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
               if (Math.abs(x) + Math.abs(z) < 4) {
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height - 4, z), (BlockState)LEAF.get());
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height - 2, z), (BlockState)LEAF.get());
               }
            }
         }

         for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
               if (Math.abs(x) + Math.abs(z) < 4 || Math.abs(x) == 2 && Math.abs(z) == 2) {
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height - 3, z), (BlockState)LEAF.get());
               }
            }
         }

         for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
               if (Math.abs(x) + Math.abs(z) < 2) {
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, height + 1, z), (BlockState)LEAF.get());
               }
            }
         }
      }

      if (branchType) {
         this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(0, height - rand.nextInt(3) - 3, -1), Direction.NORTH);
         this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(1, height - rand.nextInt(3) - 3, 0), Direction.EAST);
         this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(0, height - rand.nextInt(3) - 3, 1), Direction.SOUTH);
         this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(-1, height - rand.nextInt(3) - 3, 0), Direction.WEST);
      }

      return true;
   }

   protected void setLowerBranch(
      BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, BlockPos pos, Direction direction
   ) {
      BlockState axisLog = (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, direction.getAxis());
      this.setBlockIfOk(changedLogs, world, pos, axisLog);
      this.setBlockIfOk(changedLogs, world, pos.relative(direction, 1), axisLog);

      for (int x = -1; x < 2; x++) {
         for (int z = -1; z < 2; z++) {
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            if (z1 != 1 || x1 != 1) {
               this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 1).offset(x, 0, z), (BlockState)LEAF.get());
               this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 1).offset(x, -1, z), (BlockState)LEAF.get());
            }
         }
      }

      this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 1).above(), (BlockState)LEAF.get());
      this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 1).below(2), (BlockState)LEAF.get());
   }

   @Override
   public boolean isValidGround(WorldGenLevel world, BlockState state, BlockPos pos) {
      return !this.natural
         ? super.isValidGround(world, state, pos)
         : super.isValidGround(world, state, pos) && state.getBlock() != SkiesBlocks.coarse_lunar_dirt;
   }
}
