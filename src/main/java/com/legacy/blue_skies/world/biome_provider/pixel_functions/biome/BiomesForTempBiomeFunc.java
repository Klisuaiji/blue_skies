package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.NoiseBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public record BiomesForTempBiomeFunc(Function<Double, BiomeIds.Temperature> temperatureNoise, double scale) implements NoiseBiomeFunction {
   public static final Function<Double, BiomeIds.Temperature> FULL_RANGE = BiomesForTempBiomeFunc::fullRange;
   public static final Function<Double, BiomeIds.Temperature> EXTREMES = BiomesForTempBiomeFunc::extremes;
   public static final Function<Double, BiomeIds.Temperature> ONLY_NEUTRAL = t -> BiomeIds.Temperature.NEUTRAL;

   @Override
   public int transformPixel(BiomeRegion region, int center, int globalX, int globalZ, Random rand, PerlinSimplexNoise noise) {
      return region.biomeProvider
         .getBiomeForTemp(
            this.temperatureNoise.apply(noise.getValue(globalX * this.scale, globalZ * this.scale, false)), BiomeIds.getData(center).isOcean(), rand
         );
   }

   private static BiomeIds.Temperature fullRange(double tempNoise) {
      if (tempNoise > 0.55) {
         return BiomeIds.Temperature.HOT;
      } else if (tempNoise > 0.25) {
         return BiomeIds.Temperature.WARM;
      } else if (tempNoise > -0.25) {
         return BiomeIds.Temperature.NEUTRAL;
      } else {
         return tempNoise > -0.55 ? BiomeIds.Temperature.COLD : BiomeIds.Temperature.FROZEN;
      }
   }

   private static BiomeIds.Temperature extremes(double tempNoise) {
      if (tempNoise > 0.38) {
         return BiomeIds.Temperature.HOT;
      } else {
         return tempNoise > -0.38 ? BiomeIds.Temperature.NEUTRAL : BiomeIds.Temperature.FROZEN;
      }
   }
}
