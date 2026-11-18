package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BrumbleFeature extends Feature<NoneFeatureConfiguration> {
   public BrumbleFeature(Codec<NoneFeatureConfiguration> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      RandomSource rand = level.getRandom();
      if (rand.nextFloat() < 0.85) {
         int xOffset = rand.nextInt(8) - rand.nextInt(8);
         int zOffset = rand.nextInt(8) - rand.nextInt(8);
         BlockPos offsetPos = level.getHeightmapPos(Types.OCEAN_FLOOR, pos.offset(xOffset, 0, zOffset));
         if (level.getBlockState(offsetPos).getBlock() == Blocks.WATER) {
            BlockState state = SkiesBlocks.brumble.defaultBlockState();
            if (state.canSurvive(level, offsetPos)) {
               level.setBlock(offsetPos, state, 2);
               return true;
            }
         }
      }

      return false;
   }
}
