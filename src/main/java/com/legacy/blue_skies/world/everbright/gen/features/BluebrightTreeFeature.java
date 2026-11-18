package com.legacy.blue_skies.world.everbright.gen.features;

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

public class BluebrightTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.bluebright_log.defaultBlockState());
   private static final Lazy<BlockState> LOG_X = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.X));
   private static final Lazy<BlockState> LOG_Z = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.Z));
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.bluebright_leaves.defaultBlockState());
   private int minTreeHeight;

   public BluebrightTreeFeature(Codec<TreeConfiguration> config, int minTreeHeight) {
      super(config, true);
      this.minTreeHeight = minTreeHeight;
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(3) + this.minTreeHeight;
      if (!this.isAreaOk(world, pos, height, 1, 3, 7)) {
         return false;
      } else {
         for (int y = 0; y < height; y++) {
            this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)LOG.get());
         }

         this.setLeafBall(changedLeaves, world, rand, pos.offset(0, height - 2, 0));
         int iPosX = height - 4 - rand.nextInt(3);
         int iNegX = height - 4 - rand.nextInt(3);
         int iPosZ = height - 4 - rand.nextInt(3);
         int iNegZ = height - 4 - rand.nextInt(3);

         for (int i = 1; i <= 3; i++) {
            this.setBlockIfOk(changedLogs, world, pos.offset(i, iPosX + i, 0), (BlockState)LOG_X.get());
            this.setBlockIfOk(changedLogs, world, pos.offset(0, iPosZ + i, i), (BlockState)LOG_Z.get());
            if (i == 3) {
               this.setLeafBall(changedLeaves, world, rand, pos.offset(i, iPosX + 2, 0));
               this.setLeafBall(changedLeaves, world, rand, pos.offset(0, iPosZ + 2, i));
            }
         }

         for (int ix = -1; ix >= -3; ix--) {
            this.setBlockIfOk(changedLogs, world, pos.offset(ix, iNegX - ix, 0), (BlockState)LOG_X.get());
            this.setBlockIfOk(changedLogs, world, pos.offset(0, iNegZ - ix, ix), (BlockState)LOG_Z.get());
            if (ix == -3) {
               this.setLeafBall(changedLeaves, world, rand, pos.offset(ix, iNegX + 2, 0));
               this.setLeafBall(changedLeaves, world, rand, pos.offset(0, iNegZ + 2, ix));
            }
         }

         return true;
      }
   }

   protected void setLeafBall(BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, RandomSource rand, BlockPos pos) {
      for (int y = 0; y <= 2; y++) {
         this.setBlockIfOk(changedLeaves, world, pos.offset(0, y, 0), (BlockState)LEAF.get());

         for (int i = -1; i <= 1; i += 2) {
            this.setBlockIfOk(changedLeaves, world, pos.offset(i, y, 0), (BlockState)LEAF.get());
            this.setBlockIfOk(changedLeaves, world, pos.offset(0, y, i), (BlockState)LEAF.get());
         }
      }

      for (int i = -1; i <= 1; i += 2) {
         this.setBlockIfOk(changedLeaves, world, pos.offset(i, 1, i), (BlockState)LEAF.get());
         this.setBlockIfOk(changedLeaves, world, pos.offset(i, 1, -i), (BlockState)LEAF.get());
      }

      this.setBlockIfOk(changedLeaves, world, pos.offset(0, -1, 0), (BlockState)LEAF.get());
      this.setBlockIfOk(changedLeaves, world, pos.offset(0, 3, 0), (BlockState)LEAF.get());
   }
}
