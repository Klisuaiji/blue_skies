package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ChilledLilyFeature extends Feature<NoneFeatureConfiguration> {
   public ChilledLilyFeature(Codec<NoneFeatureConfiguration> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      RandomSource rand = level.getRandom();
      BlockPos blockpos = pos;

      while (blockpos.getY() > 0) {
         BlockPos blockpos1 = blockpos.below();
         if (!level.isEmptyBlock(blockpos1)) {
            break;
         }

         blockpos = blockpos1;
      }

      for (int i = 0; i < 10; i++) {
         BlockPos blockpos2 = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
         BlockState blockstate = SkiesBlocks.chilled_lily_pad.defaultBlockState();
         if (level.isEmptyBlock(blockpos2) && blockstate.canSurvive(level, blockpos2)) {
            level.setBlock(blockpos2, blockstate, 2);
         }
      }

      return true;
   }
}
