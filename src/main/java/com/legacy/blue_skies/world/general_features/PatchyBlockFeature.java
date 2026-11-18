package com.legacy.blue_skies.world.general_features;

import com.legacy.blue_skies.util.MLSimplexNoise;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class PatchyBlockFeature<C extends FeatureConfiguration> extends Feature<C> {
   private static MLSimplexNoise noise;

   public PatchyBlockFeature(Codec<C> codec) {
      super(codec);
   }

   public boolean place(FeaturePlaceContext<C> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      RandomSource rand = level.getRandom();
      if (noise == null || noise.getSeed() != level.getSeed()) {
         noise = new MLSimplexNoise(level.getSeed());
      }

      for (int x = 0; x < 16; x++) {
         for (int z = 0; z < 16; z++) {
            double noiseVal = (noise.getValue((pos.getX() + x) / 50.0, (pos.getZ() + z) / 50.0) + 1.0) / 2.0;
            double threshold = 0.35;
            if (noiseVal > threshold && rand.nextFloat() + threshold < noiseVal || rand.nextFloat() < 0.01) {
               this.placeBlock(level, context.chunkGenerator(), rand, pos.offset(x, 0, z), (C)context.config());
            }
         }
      }

      return true;
   }

   public abstract void placeBlock(WorldGenLevel var1, ChunkGenerator var2, RandomSource var3, BlockPos var4, C var5);
}
