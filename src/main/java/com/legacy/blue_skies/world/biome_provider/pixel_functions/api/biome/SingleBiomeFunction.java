package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome;

import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;

public interface SingleBiomeFunction extends BiomePixelFunction {
   @Override
   default int transformPixel(BiomeRegion region, int x, int z, Random rand) {
      return this.transformPixel(region.getDirect(x, z), rand);
   }

   int transformPixel(int var1, Random var2);
}
