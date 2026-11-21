package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.AreaBiomeFunction;
import java.util.Random;

public record AddBeachBiomeFunc(int beachId, double chance) implements AreaBiomeFunction {
   public AddBeachBiomeFunc(int beachId) {
      this(beachId, 1.0);
   }

   @Override
   public int transformPixel(int center, int left, int right, int down, int up, Random rand) {
      return !BiomeIds.getData(center).createsBeaches()
            || !BiomeIds.getData(left).hasBeaches()
               && !BiomeIds.getData(right).hasBeaches()
               && !BiomeIds.getData(down).hasBeaches()
               && !BiomeIds.getData(up).hasBeaches()
            || !(this.chance >= 1.0) && !(rand.nextFloat() < this.chance)
         ? center
         : this.beachId;
   }
}
