package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.NoiseBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public record SubBiomesBiomeFunc(
   Map<Integer, Function<Double, Integer>> largeSubBiomes,
   double largeSubBiomeScale,
   Map<Integer, Function<Double, Integer>> smallSubBiomes,
   double smallSubBiomeScale
) implements NoiseBiomeFunction {
   @Override
   public int transformPixel(BiomeRegion region, int center, int globalX, int globalZ, Random rand, PerlinSimplexNoise noise) {
      Function<Double, Integer> largeFunc = this.largeSubBiomes.get(center);
      if (largeFunc != null) {
         center = largeFunc.apply(noise.getValue(globalX * this.largeSubBiomeScale, globalZ * this.largeSubBiomeScale, false));
      }

      Function<Double, Integer> smallFunc = this.smallSubBiomes.get(center);
      if (smallFunc != null) {
         center = smallFunc.apply(noise.getValue(globalX * this.smallSubBiomeScale, globalZ * this.smallSubBiomeScale, false));
      }

      return center;
   }
}
