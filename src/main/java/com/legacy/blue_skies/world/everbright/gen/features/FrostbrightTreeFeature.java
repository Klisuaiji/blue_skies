package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class FrostbrightTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.frostbright_log.defaultBlockState());
   private static final Lazy<BlockState> LOG_X = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.X));
   private static final Lazy<BlockState> LOG_Z = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.Z));
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.frostbright_leaves.defaultBlockState());
   private static final Lazy<BlockState> SNOW = Lazy.of(() -> Blocks.SNOW.defaultBlockState());

   public FrostbrightTreeFeature(Codec<TreeConfiguration> config, boolean natural) {
      super(config, natural);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(7) + Math.random() < 0.1 ? 30 : 20;
      if (!this.isAreaOk(world, pos, height, 2, height - 2, 11)) {
         return false;
      }

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
               if (y < height - 1 || x == 0 && z == 0) {
                  this.setBlockIfOk(changedLogs, world, pos.offset(x, y, z), (BlockState)LOG.get());
               }
            }
         }
      }

      for (int x = -6; x <= 6; x++) {
         for (int z = -6; z <= 6; z++) {
            if (x == 0) {
               x = 1;
            }

            if (z == 0) {
               z = 1;
            }

            BlockPos leafPos = pos.offset(x > 0 ? x - 1 : x, height - 2, z > 0 ? z - 1 : z).offset(1, 0, 1);
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            if (z1 < 3 || z1 < 5 && x1 < 6 || z1 == 5 && x1 < 5 || z1 == 6 && x1 < 3) {
               this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());
               if (this.natural) {
                  this.setBlockIfOk(null, world, leafPos.above(), (BlockState)SNOW.get());
               }
            }
         }
      }

      for (int x = -5; x <= 5; x++) {
         for (int z = -5; z <= 5; z++) {
            if (x == 0) {
               x = 1;
            }

            if (z == 0) {
               z = 1;
            }

            BlockPos leafPos = pos.offset(x > 0 ? x - 1 : x, height - 1, z > 0 ? z - 1 : z).offset(1, 0, 1);
            int x1 = Math.abs(x);
            int z1 = Math.abs(z);
            if (z1 < 3 || z1 < 5 && x1 < 5 || z1 == 5 && x1 < 3) {
               this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());
               if (this.natural) {
                  this.setBlockIfOk(null, world, leafPos.above(), (BlockState)SNOW.get());
               }
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
            if (z1 + x1 <= 4) {
               this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());
               if (this.natural && world.isEmptyBlock(leafPos.above())) {
                  this.setBlockIfOk(null, world, leafPos.above(), (BlockState)SNOW.get());
               }
            }
         }
      }

      this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(rand.nextInt(2), rand.nextInt(height - 9) + 3, -1));
      this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(2, rand.nextInt(height - 9) + 3, rand.nextInt(2)));
      this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(rand.nextInt(2), rand.nextInt(height - 9) + 3, 2));
      this.setLowerBranch(changedLogs, changedLeaves, world, pos.offset(-1, rand.nextInt(height - 9) + 3, rand.nextInt(2)));

      for (int branch = 0; branch < 4; branch++) {
         int y = height - 5 - rand.nextInt(2);
         int xz = rand.nextInt(2);
         int length = rand.nextInt(2) + 1;

         for (int l = y; l < height - 2; l++) {
            if (branch == 0) {
               this.setBlockIfOk(changedLogs, world, pos.offset(2 + length, l + 1, xz), (BlockState)LOG.get());
            }

            if (branch == 1) {
               this.setBlockIfOk(changedLogs, world, pos.offset(xz, l + 1, 2 + length), (BlockState)LOG.get());
            }

            if (branch == 2) {
               this.setBlockIfOk(changedLogs, world, pos.offset(-1 - length, l + 1, xz), (BlockState)LOG.get());
            }

            if (branch == 3) {
               this.setBlockIfOk(changedLogs, world, pos.offset(xz, l + 1, -1 - length), (BlockState)LOG.get());
            }
         }

         for (int l = 0; l < length + 1; l++) {
            if (branch == 0) {
               this.setBlockIfOk(changedLogs, world, pos.offset(2 + l, y, xz), (BlockState)LOG_X.get());
            }

            if (branch == 1) {
               this.setBlockIfOk(changedLogs, world, pos.offset(xz, y, 2 + l), (BlockState)LOG_Z.get());
            }

            if (branch == 2) {
               this.setBlockIfOk(changedLogs, world, pos.offset(-1 - l, y, xz), (BlockState)LOG_X.get());
            }

            if (branch == 3) {
               this.setBlockIfOk(changedLogs, world, pos.offset(xz, y, -1 - l), (BlockState)LOG_Z.get());
            }
         }
      }

      for (int x = -1; x <= 2; x++) {
         for (int z = -1; z <= 2; z++) {
            if ((x == -1 || x == 2) ^ (z == -1 || z == 2)) {
               this.setBlockIfOk(changedLogs, world, pos.offset(x, 0, z), (BlockState)LOG.get());
               this.setBlockIfOk(changedLogs, world, pos.offset(x, 1, z), (BlockState)LOG.get());
               if (rand.nextBoolean()) {
                  this.setBlockIfOk(changedLogs, world, pos.offset(x, 2, z), (BlockState)LOG.get());
               }
            }
         }
      }

      if (this.natural) {
         this.placeSnowCircle(changedLogs, world, rand, pos);
      }

      return true;
   }

   protected void setLowerBranch(
      BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, BlockPos pos
   ) {
      this.setBlockIfOk(changedLogs, world, pos, (BlockState)LOG.get());

      for (int x = -1; x < 2; x++) {
         for (int z = -1; z < 2; z++) {
            for (int y = 0; y < 2; y++) {
               if (y != 1 || Math.abs(x) != 1 || Math.abs(z) != 1) {
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, y, z), (BlockState)LEAF.get());
               }
            }
         }
      }
   }

   protected void placeSnowCircle(BiConsumer<BlockPos, BlockState> changedBlocks, WorldGenLevel world, RandomSource rand, BlockPos centerPos) {
      for (int x = -5; x <= 6; x++) {
         for (int z = -5; z <= 6; z++) {
            if (rand.nextBoolean()) {
               for (int y = 2; y >= -3; y--) {
                  BlockPos offsetPos = centerPos.offset(x, y, z);
                  BlockState snow = (BlockState)((BlockState)SNOW.get()).setValue(SnowLayerBlock.LAYERS, rand.nextFloat() > 0.8 ? 2 : 1);
                  if (this.setBlockIfOk(null, world, offsetPos, snow) || !world.isEmptyBlock(offsetPos) && y < 0) {
                     break;
                  }
               }
            }
         }
      }
   }
}
