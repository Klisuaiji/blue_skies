package com.legacy.blue_skies.world.biome_provider.provider;

import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.BiomeFuncs;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.HeightFuncs;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;

public class EverbrightBiomeProvider extends AbstractBiomeProvider {
   public EverbrightBiomeProvider(int minY, int height) {
      super(SkiesDimensions.everbrightKey(), minY, height);
   }

   @Override
   public int[] getLandBiomes() {
      return new int[]{
         BiomeIds.CALMING_SKIES,
         BiomeIds.BRIGHTLANDS,
         BiomeIds.SLUSHLANDS,
         BiomeIds.FROSTBITTEN_FOREST,
         BiomeIds.SNOW_COVERED_PINES,
         BiomeIds.BRISK_MEADOW,
         BiomeIds.POLAR_HIGHLAND
      };
   }

   @Override
   public int[] getOceanBiomes() {
      return new int[]{BiomeIds.PEEKING_OCEAN};
   }

   @Override
   public void transformBiomes(BiomeRegion region) {
      region.transform(BiomeFuncs.ADD_OCEAN);
      region.transform(BiomeFuncs.ASSIGN_BIOMES);
      region.upscale();
      region.transform(BiomeFuncs.FUZZ_EDGES);
      region.transform(BiomeFuncs.SMOOTH_EDGES);
      region.upscaleAndEdges(1L);
      region.upscaleAndEdges(2L);
      region.transform(BiomeFuncs.ADD_DEEP_OCEAN);

      for (int i = 0; i < 3; i++) {
         region.transform(BiomeFuncs.REMOVE_DEEP_OCEAN_EDGES);
      }

      region.upscaleAndEdges(3L);

      for (int i = 0; i < 4; i++) {
         region.transform(BiomeFuncs.ADD_SHALLOW_BRIGHTLANDS);
      }

      region.transform(BiomeFuncs.ASSIGN_SUB_BIOMES);
      region.transform(BiomeFuncs.ADD_BRIGHT_BEACH);
      region.upscaleAndEdges(4L);
      region.upscaleAndEdges(5L);
      region.transform(BiomeFuncs.REMOVE_LONELY);

      for (int i = 0; i < 5; i++) {
         region.transform(BiomeFuncs.ADD_BRIGHT_BEACH);
      }

      region.upscale();
   }

   @Override
   public void transformHeights(HeightRegion region) {
      region.transform(HeightFuncs.BASE_NOISE);
      region.transform(HeightFuncs.ADDITIONAL_NOISE);
   }
}
