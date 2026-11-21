package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome;

import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;

public interface BiomePixelFunction {
   int transformPixel(BiomeRegion var1, int var2, int var3, Random var4);
}
