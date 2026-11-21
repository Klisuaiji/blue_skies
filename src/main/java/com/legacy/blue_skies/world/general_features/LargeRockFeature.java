package com.legacy.blue_skies.world.general_features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class LargeRockFeature extends Feature<BlockStateConfiguration> implements IStructureRestricted {
   public LargeRockFeature(Codec<BlockStateConfiguration> p_i231931_1_) {
      super(p_i231931_1_);
   }

   public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      if (!this.shouldGenerate(level, pos)) {
         return false;
      }

      BlockStateConfiguration config = (BlockStateConfiguration)context.config();
      RandomSource rand = level.getRandom();

      while (pos.getY() > 3) {
         if (!level.isEmptyBlock(pos.below())) {
            BlockState block = level.getBlockState(pos.below());
            if (isDirt(block) || isStone(block)) {
               break;
            }
         }

         pos = pos.below();
      }

      if (pos.getY() <= 3) {
         return false;
      }

      for (int l = 0; l < 3; l++) {
         int i = 2;
         int j = rand.nextInt(4);
         int k = 2;
         float f = (i + j + k) * 0.333F + 0.5F;

         for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-i, -j, -k), pos.offset(i, j, k))) {
            if (blockpos.distSqr(pos) <= f * f) {
               level.setBlock(blockpos, config.state, 4);
            }
         }

         pos = pos.offset(-1 + rand.nextInt(2), -rand.nextInt(2), -1 + rand.nextInt(2));
      }

      return true;
   }
}
