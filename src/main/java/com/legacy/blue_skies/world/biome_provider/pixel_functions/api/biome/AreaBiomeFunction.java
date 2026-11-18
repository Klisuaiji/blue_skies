package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome;

import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;

public interface AreaBiomeFunction extends BiomePixelFunction {
   @Override
   default int transformPixel(BiomeRegion region, int x, int z, Random rand) {
      return this.transformPixel(region.getDirect(x, z), region.getLeft(x, z), region.getRight(x, z), region.getDown(x, z), region.getUp(x, z), rand);
   }

   int transformPixel(int var1, int var2, int var3, int var4, int var5, Random var6);
}
