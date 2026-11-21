package com.legacy.blue_skies.world.biome_provider.pixel_functions;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.AreaBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.NoiseBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.AddBeachBiomeFunc;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.AddEdgeBiomeFunc;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.AddOceanBiomeFunc;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.AddRiverBiomeFunc;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.BiomesForTempBiomeFunc;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.DeepOceanBiomeFunc;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.biome.SubBiomesBiomeFunc;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.Util;

public class BiomeFuncs {
   private static final Map<Integer, Function<Double, Integer>> LARGE_SUB_BIOMES = (Map<Integer, Function<Double, Integer>>)Util.make(new HashMap(), map -> {
      map.put(BiomeIds.DEEP_PEEKING_OCEAN, noiseVal -> !(noiseVal < -0.835) && !(noiseVal > 0.835) ? BiomeIds.DEEP_PEEKING_OCEAN : BiomeIds.BRUMBLE_FOREST);
      map.put(BiomeIds.CRYSTAL_DUNES, noiseVal -> noiseVal > 0.5 ? BiomeIds.CRYSTAL_DUNES_SPIKES : BiomeIds.CRYSTAL_DUNES);
   });
   private static final Map<Integer, Function<Double, Integer>> SMALL_SUB_BIOMES = (Map<Integer, Function<Double, Integer>>)Util.make(new HashMap(), map -> {
      map.put(BiomeIds.FROSTBITTEN_FOREST, noiseVal -> noiseVal > 0.5 ? BiomeIds.FROSTBITTEN_FOREST_CLEARING : BiomeIds.FROSTBITTEN_FOREST);
      map.put(BiomeIds.DEEP_PEEKING_OCEAN, noiseVal -> noiseVal > 0.55 ? BiomeIds.BRUMBLE_FOREST : BiomeIds.DEEP_PEEKING_OCEAN);
      map.put(BiomeIds.CRESCENT_ORCHARD, noiseVal -> noiseVal > 0.56 ? BiomeIds.CRESCENT_ORCHARD_LAKE : BiomeIds.CRESCENT_ORCHARD);
   });
   public static final AreaBiomeFunction FUZZ_EDGES = BiomeFuncs::fuzzEdges;
   public static final AreaBiomeFunction SMOOTH_EDGES = BiomeFuncs::smoothEdges;
   public static final AreaBiomeFunction REMOVE_LONELY = BiomeFuncs::removeLonely;
   public static final NoiseBiomeFunction ASSIGN_BIOMES = new BiomesForTempBiomeFunc(BiomesForTempBiomeFunc.EXTREMES, 0.07);
   public static final NoiseBiomeFunction ASSIGN_SUB_BIOMES = new SubBiomesBiomeFunc(LARGE_SUB_BIOMES, 0.018, SMALL_SUB_BIOMES, 0.24);
   public static final NoiseBiomeFunction ADD_OCEAN = new AddOceanBiomeFunc(BiomeIds.PEEKING_OCEAN, 0.3, 0.1);
   public static final AreaBiomeFunction ADD_DEEP_OCEAN = new DeepOceanBiomeFunc.Add();
   public static final AreaBiomeFunction REMOVE_DEEP_OCEAN_EDGES = new DeepOceanBiomeFunc.RemoveEdges();
   public static final AreaBiomeFunction ADD_BRIGHT_BEACH = new AddBeachBiomeFunc(BiomeIds.MIDDAY_SHORE);
   public static final AreaBiomeFunction ADD_SHALLOW_BRIGHTLANDS = new AddEdgeBiomeFunc.Inner(BiomeIds.BRIGHTLANDS, BiomeIds.SHALLOW_BRIGHTLANDS);
   public static final NoiseBiomeFunction ADD_DAWN_RIVER = new AddRiverBiomeFunc(BiomeIds.RISING_CREEK, 0.017);
   public static final AreaBiomeFunction ADD_DAWN_RIVER_EDGE = new AddEdgeBiomeFunc.Outer(BiomeIds.RISING_CREEK, BiomeIds.RISING_CREEK_EDGE);

   private static int randomPixel(int left, int right, int down, int up, Random rand) {
      return switch (rand.nextInt(4)) {
         case 0 -> left;
         case 1 -> right;
         case 2 -> down;
         default -> up;
      };
   }

   private static int fuzzEdges(int center, int left, int right, int down, int up, Random rand) {
      return center == left && center == right && center == down && center == up ? center : randomPixel(left, right, down, up, rand);
   }

   private static int smoothEdges(int center, int left, int right, int down, int up, Random rand) {
      if (right == down && down == up) {
         return right;
      } else if (left == right && left == down) {
         return left;
      } else if (left == right && left == up) {
         return left;
      } else if (left == down && left == up) {
         return left;
      } else if (left == right && down != up) {
         return left;
      } else if (left == down && right != up) {
         return left;
      } else if (left == up && right != down) {
         return left;
      } else if (right == down && left != up) {
         return right;
      } else if (right == up && left != down) {
         return right;
      } else {
         return down == up && left != right ? down : randomPixel(left, right, down, up, rand);
      }
   }

   private static int removeLonely(int center, int left, int right, int down, int up, Random rand) {
      if (center == left || center == right || center == down || center == up) {
         return center;
      } else if (left == right && up == down && left == up && left != center) {
         return left;
      } else if (left == right && left == down) {
         return left;
      } else if (left == right && left == up) {
         return left;
      } else if (down == up && down == left) {
         return down;
      } else if (down == up && down == right) {
         return down;
      } else if (left == right) {
         return left;
      } else if (down == up) {
         return down;
      } else if (left == up || left == down) {
         return left;
      } else {
         return right != up && right != down ? randomPixel(left, right, down, up, rand) : right;
      }
   }
}
