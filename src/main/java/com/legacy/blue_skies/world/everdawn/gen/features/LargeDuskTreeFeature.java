package com.legacy.blue_skies.world.everdawn.gen.features;

import com.google.common.collect.ImmutableSet;
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

public class LargeDuskTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.dusk_log.defaultBlockState());
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.dusk_leaves.defaultBlockState());

   public LargeDuskTreeFeature(Codec<TreeConfiguration> config, boolean natural) {
      super(config, natural);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(5) + 16;
      if (!this.isAreaOk(world, pos, height, 2, height - 2, 3)) {
         return false;
      }

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
               this.setBlockIfOk(changedLogs, world, pos.offset(x, y, z), (BlockState)LOG.get());
            }
         }
      }

      for (int x = -4; x <= 4; x++) {
         for (int z = -4; z <= 4; z++) {
            if (x == 0) {
               x = 1;
            }

            if (z == 0) {
               z = 1;
            }

            BlockPos leafPos = pos.offset(x > 0 ? x - 1 : x, height - 2, z > 0 ? z - 1 : z).offset(1, 0, 1);
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            if (z1 < 4 || z1 != 4 || x1 != 4) {
               this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());
               if (rand.nextFloat() < 0.3) {
                  this.setBlockIfOk(changedLeaves, world, leafPos.below(), (BlockState)LEAF.get());
               }
            }
         }
      }

      for (int x = -4; x <= 4; x++) {
         for (int z = -4; z <= 4; z++) {
            if (x == 0) {
               x = 1;
            }

            if (z == 0) {
               z = 1;
            }

            BlockPos leafPos = pos.offset(x > 0 ? x - 1 : x, height - 1, z > 0 ? z - 1 : z).offset(1, 0, 1);
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            if (z1 + x1 < 7) {
               this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());
            }
         }
      }

      for (int x = -3; x <= 3; x++) {
         for (int z = -3; z <= 3; z++) {
            if (x == 0) {
               x = 1;
            }

            if (z == 0) {
               z = 1;
            }

            BlockPos leafPos = pos.offset(x > 0 ? x - 1 : x, height, z > 0 ? z - 1 : z).offset(1, 0, 1);
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            if (z1 != 3 || x1 != 3) {
               this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());
            }
         }
      }

      this.setLowerBranch(changedLogs, changedLeaves, rand, world, pos.offset(rand.nextInt(2), rand.nextInt(5) + 6, -1), Direction.NORTH);
      this.setLowerBranch(changedLogs, changedLeaves, rand, world, pos.offset(2, rand.nextInt(5) + 6, rand.nextInt(2)), Direction.EAST);
      this.setLowerBranch(changedLogs, changedLeaves, rand, world, pos.offset(rand.nextInt(2), rand.nextInt(5) + 6, 2), Direction.SOUTH);
      this.setLowerBranch(changedLogs, changedLeaves, rand, world, pos.offset(-1, rand.nextInt(5) + 6, rand.nextInt(2)), Direction.WEST);

      for (int x = -1; x <= 2; x++) {
         for (int z = -1; z <= 2; z++) {
            if (rand.nextBoolean() && (x == -1 || x == 2) ^ (z == -1 || z == 2)) {
               this.setBlockIfOk(changedLogs, world, pos.offset(x, 0, z), (BlockState)LOG.get());
               if (rand.nextBoolean()) {
                  this.setBlockIfOk(changedLogs, world, pos.offset(x, 1, z), (BlockState)LOG.get());
                  this.setBlockIfOk(changedLogs, world, pos.offset(x, 2, z), (BlockState)LOG.get());
                  if (rand.nextBoolean()) {
                     this.setBlockIfOk(changedLogs, world, pos.offset(x, 3, z), (BlockState)LOG.get());
                  }
               }
            }
         }
      }

      if (this.natural) {
         AbstractSkyTreeFeature.placeVines(
            world, pos, rand, 13, height, 4, 6, 0.05F, SkiesBlocks.dusk_vine.defaultBlockState(), ImmutableSet.of(SkiesBlocks.dusk_leaves)
         );
      }

      return true;
   }

   protected void setLowerBranch(
      BiConsumer<BlockPos, BlockState> changedLogs,
      BiConsumer<BlockPos, BlockState> changedLeaves,
      RandomSource rand,
      WorldGenLevel world,
      BlockPos pos,
      Direction direction
   ) {
      BlockState axisLog = (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, direction.getAxis());
      this.setBlockIfOk(changedLogs, world, pos, axisLog);
      this.setBlockIfOk(changedLogs, world, pos.relative(direction, 1), axisLog);
      this.setBlockIfOk(changedLogs, world, pos.relative(direction, 2).above(), axisLog);
      this.setBlockIfOk(changedLogs, world, pos.relative(direction, 3).above(), axisLog);
      this.setBlockIfOk(changedLogs, world, pos.relative(direction, 4).above(2), (BlockState)LOG.get());

      for (int x = -2; x < 3; x++) {
         for (int z = -2; z < 3; z++) {
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 4).offset(x, 2, z), (BlockState)LEAF.get());
            if (z1 != 2 || x1 != 2) {
               this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 4).offset(x, 3, z), (BlockState)LEAF.get());
            }

            if (rand.nextFloat() < 0.3) {
               this.setBlockIfOk(changedLeaves, world, pos.relative(direction, 4).offset(x, 1, z), (BlockState)LEAF.get());
            }
         }
      }
   }

   @Override
   public boolean isValidGround(WorldGenLevel world, BlockState state, BlockPos pos) {
      return !this.natural
         ? super.isValidGround(world, state, pos)
         : super.isValidGround(world, state, pos) && state.getBlock() != SkiesBlocks.coarse_lunar_dirt;
   }
}
