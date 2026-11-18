package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class CrystalizedTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.crystallized_log.defaultBlockState());
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.crystallized_leaves.defaultBlockState());

   public CrystalizedTreeFeature(Codec<TreeConfiguration> config, boolean natural) {
      super(config, natural);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(3) + 6;
      if (!this.isAreaOk(world, pos, height, 1, 3, 7)) {
         return false;
      } else {
         for (int y = 0; y < height; y++) {
            this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)LOG.get());
         }

         Direction noBranch = DirectionUtil.randHorizontal(rand);

         for (Direction dir : DirectionUtil.HORIZONTAL) {
            if (dir != noBranch) {
               BlockPos branchPos = pos.above(3 + rand.nextInt(3));
               int maxBranchLength = rand.nextInt(2) + 4;

               for (int branchLength = 0; branchLength < maxBranchLength; branchLength++) {
                  branchPos = branchPos.above();
                  if (branchLength < maxBranchLength / 2) {
                     branchPos = branchPos.relative(dir);
                  }

                  this.setBlockIfOk(changedLogs, world, branchPos, (BlockState)LOG.get());
                  if (branchLength == maxBranchLength - 1) {
                     this.setBlockIfOk(changedLeaves, world, branchPos.above(), (BlockState)LEAF.get());
                  }
               }
            }
         }

         int xzSize = 4;

         for (int x = -xzSize; x <= xzSize; x++) {
            for (int z = -xzSize; z <= xzSize; z++) {
               for (int y = 5; y < height + 6; y++) {
                  BlockPos logPos = pos.offset(x, y, z);
                  if (world.getBlockState(logPos).getBlock() == ((BlockState)LOG.get()).getBlock()) {
                     for (Direction dirx : Direction.values()) {
                        if (rand.nextBoolean()) {
                           BlockPos leafPos = logPos.relative(dirx);
                           this.setBlockIfOk(changedLeaves, world, leafPos, (BlockState)LEAF.get());

                           for (Direction leafDir : Direction.values()) {
                              this.setBlockIfOk(changedLeaves, world, leafPos.relative(leafDir), (BlockState)LEAF.get());
                              if (rand.nextBoolean()) {
                                 this.setBlockIfOk(changedLeaves, world, leafPos.relative(leafDir).relative(leafDir), (BlockState)LEAF.get());
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         if (this.natural) {
            for (int attempts = 0; attempts < 8; attempts++) {
               BlockPos flowerPos = pos.offset(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(4) - rand.nextInt(4));
               this.setBlockIfOk(null, world, flowerPos, SkiesBlocks.crystal_flower.defaultBlockState());
            }
         }

         return true;
      }
   }

   @Override
   public boolean isValidGround(WorldGenLevel world, BlockState groundState, BlockPos pos) {
      return groundState.is(BlockTags.SAND);
   }
}
