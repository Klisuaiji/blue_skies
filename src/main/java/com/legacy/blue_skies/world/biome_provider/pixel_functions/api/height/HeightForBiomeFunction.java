package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height;

import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;
import java.util.Random;

public interface HeightForBiomeFunction extends HeightPixelFunction {
   @Override
   default int transformPixel(HeightRegion region, int x, int z, Random rand) {
      return this.transformPixel(region, region.getDirect(x, z), region.biomeRegion.getDirect(x, z), region.getGlobalX(x), region.getGlobalZ(z));
   }

   int transformPixel(HeightRegion var1, int var2, int var3, int var4, int var5);
}
