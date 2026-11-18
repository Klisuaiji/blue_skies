package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class DuskTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.dusk_log.defaultBlockState());
   private static final Lazy<BlockState> LOG_X = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.X));
   private static final Lazy<BlockState> LOG_Z = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.Z));
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.dusk_leaves.defaultBlockState());
   protected int minTreeHeight;

   public DuskTreeFeature(Codec<TreeConfiguration> config, int minTreeHeightIn) {
      super(config, true);
      this.minTreeHeight = minTreeHeightIn;
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(3) + this.minTreeHeight;
      if (!this.isAreaOk(world, pos, height, 1, height - 3, 7)) {
         return false;
      } else {
         this.setLeafCanopy(changedLeaves, world, rand, pos.offset(0, height - 2, 0));

         for (int y = 0; y < height; y++) {
            this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)LOG.get());
         }

         this.setBranches(changedLogs, world, rand, pos, height);
         return true;
      }
   }

   protected void setBranches(BiConsumer<BlockPos, BlockState> changedLogs, WorldGenLevel world, RandomSource rand, BlockPos pos, int height) {
      int y = height - 3;
      if (rand.nextBoolean()) {
         this.setBlockIfOk(changedLogs, world, pos.offset(1, y, 0), (BlockState)LOG_X.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(-1, y, 0), (BlockState)LOG_X.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(2, y + 1, 0), (BlockState)LOG.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(-2, y + 1, 0), (BlockState)LOG.get());
      } else {
         this.setBlockIfOk(changedLogs, world, pos.offset(0, y, 1), (BlockState)LOG_Z.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, y, -1), (BlockState)LOG_Z.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, y + 1, 2), (BlockState)LOG.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, y + 1, -2), (BlockState)LOG.get());
      }
   }

   protected void setLeafCanopy(BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, RandomSource rand, BlockPos pos) {
      for (int x = -3; x <= 3; x++) {
         for (int z = -3; z <= 3; z++) {
            if ((Math.abs(x) != 3 || Math.abs(z) != 3) && rand.nextInt(3) == 0) {
               this.setBlockIfOk(changedLeaves, world, pos.offset(x, 0, z), (BlockState)LEAF.get());
            }
         }
      }

      pos = pos.above();

      for (int x = -3; x <= 3; x++) {
         for (int zx = -3; zx <= 3; zx++) {
            if (Math.abs(x) != 3 || Math.abs(zx) != 3) {
               this.setBlockIfOk(changedLeaves, world, pos.offset(x, 0, zx), (BlockState)LEAF.get());
            }
         }
      }

      for (int longSide = -4; longSide <= 4; longSide++) {
         for (int shortSide = -1; shortSide <= 1; shortSide++) {
            if (Math.abs(longSide) == 4) {
               this.setBlockIfOk(changedLeaves, world, pos.offset(longSide, 0, shortSide), (BlockState)LEAF.get());
               this.setBlockIfOk(changedLeaves, world, pos.offset(shortSide, 0, longSide), (BlockState)LEAF.get());
            }
         }
      }

      pos = pos.above();

      for (int x = -2; x <= 2; x++) {
         for (int zxx = -2; zxx <= 2; zxx++) {
            if (Math.abs(x) != 2 || Math.abs(zxx) != 2) {
               this.setBlockIfOk(changedLeaves, world, pos.offset(x, 0, zxx), (BlockState)LEAF.get());
            }
         }
      }
   }
}
