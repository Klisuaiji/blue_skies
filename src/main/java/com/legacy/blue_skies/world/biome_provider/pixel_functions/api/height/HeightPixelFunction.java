package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height;

import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;
import java.util.Random;

public interface HeightPixelFunction {
   int transformPixel(HeightRegion var1, int var2, int var3, Random var4);
}
