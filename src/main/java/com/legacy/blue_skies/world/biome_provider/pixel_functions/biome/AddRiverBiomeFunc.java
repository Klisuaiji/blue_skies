package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.NoiseBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import java.util.Random;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public record AddRiverBiomeFunc(int riverId, double riverWidth) implements NoiseBiomeFunction {
   @Override
   public int transformPixel(BiomeRegion region, int center, int globalX, int globalZ, Random rand, PerlinSimplexNoise noise) {
      if (BiomeIds.getData(center).canBecomeRiver()) {
         double riverScale = 0.0025;
         double na = noise.getValue(globalX * riverScale, globalZ * riverScale, false);
         riverScale *= 3.5;
         double nb = noise.getValue(globalX * riverScale, globalZ * riverScale, true);
         riverScale *= 3.0;
         double nc = noise.getValue(globalX * riverScale, globalZ * riverScale, true);
         double n = (na * 0.65 + nb * 0.35) * 0.975 + nc * 0.025;
         if (n > -this.riverWidth && n < this.riverWidth) {
            return this.riverId;
         }
      }

      return center;
   }
}
