package com.legacy.blue_skies.world.biome_provider.provider;

import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.BiomeFuncs;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.HeightFuncs;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;

public class EverdawnBiomeProvider extends AbstractBiomeProvider {
   public EverdawnBiomeProvider(int minY, int height) {
      super(SkiesDimensions.everdawnKey(), minY, height);
   }

   @Override
   public int[] getLandBiomes() {
      return new int[]{
         BiomeIds.UNORTHODOX_VALLEY,
         BiomeIds.SHADED_WOODLANDS,
         BiomeIds.CRYSTAL_DUNES,
         BiomeIds.SUNSET_MAPLE_FOREST,
         BiomeIds.MOONLIT_RESERVOIR,
         BiomeIds.CRYSTAL_ROUGHS,
         BiomeIds.SEARING_GRASSLAND,
         BiomeIds.CRESCENT_ORCHARD
      };
   }

   @Override
   public int[] getOceanBiomes() {
      return new int[]{BiomeIds.RISING_CREEK};
   }

   @Override
   public void transformBiomes(BiomeRegion region) {
      region.transform(BiomeFuncs.ASSIGN_BIOMES);
      region.upscale();
      region.transform(BiomeFuncs.FUZZ_EDGES);
      region.transform(BiomeFuncs.SMOOTH_EDGES);
      region.upscaleAndEdges(1L);
      region.upscaleAndEdges(2L);
      region.upscaleAndEdges(3L);
      region.transform(BiomeFuncs.ASSIGN_SUB_BIOMES);
      region.upscaleAndEdges(4L);
      region.upscaleAndEdges(5L);
      region.transform(BiomeFuncs.REMOVE_LONELY);
      region.transform(BiomeFuncs.ADD_DAWN_RIVER);

      for (int i = 0; i < 2; i++) {
         region.transform(BiomeFuncs.ADD_DAWN_RIVER_EDGE);
      }

      region.upscale();
   }

   @Override
   public void transformHeights(HeightRegion region) {
      region.transform(HeightFuncs.BASE_NOISE);
      region.transform(HeightFuncs.ADDITIONAL_NOISE);
   }
}
