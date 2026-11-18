package com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome;

import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public interface NoiseBiomeFunction extends BiomePixelFunction {
   @Override
   default int transformPixel(BiomeRegion region, int x, int z, Random rand) {
      return this.transformPixel(region, region.getDirect(x, z), region.getGlobalX(x), region.getGlobalZ(z), rand, region.biomeProvider.noiseA);
   }

   int transformPixel(BiomeRegion var1, int var2, int var3, int var4, Random var5, PerlinSimplexNoise var6);
}
