package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeData;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;
import java.util.Random;

public interface HeightForBiomeDataPixelFunction extends HeightPixelFunction {
   @Override
   default int transformPixel(HeightRegion region, int x, int z, Random rand) {
      return this.transformPixel(
         region, region.getDirect(x, z), BiomeIds.getData(region.biomeRegion.getDirect(x, z)), region.getGlobalX(x), region.getGlobalZ(z)
      );
   }

   int transformPixel(HeightRegion var1, int var2, BiomeData var3, int var4, int var5);
}
