package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.NoiseBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public record AddOceanBiomeFunc(int oceanId, double noiseThreshold, double scale) implements NoiseBiomeFunction {
   @Override
   public int transformPixel(BiomeRegion region, int center, int globalX, int globalZ, Random rand, PerlinSimplexNoise noise) {
      return noise.getValue(globalX * this.scale, globalZ * this.scale, true) > this.noiseThreshold ? this.oceanId : center;
   }
}
