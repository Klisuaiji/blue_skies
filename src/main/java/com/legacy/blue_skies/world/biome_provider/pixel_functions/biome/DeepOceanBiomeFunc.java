package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeData;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.AreaBiomeFunction;
import java.util.Random;

public abstract class DeepOceanBiomeFunc implements AreaBiomeFunction {
   public static class Add extends DeepOceanBiomeFunc {
      @Override
      public int transformPixel(int center, int left, int right, int down, int up, Random rand) {
         BiomeData centerData = BiomeIds.getData(center);
         return centerData.isShallowOcean()
               && BiomeIds.getData(left).isOcean()
               && BiomeIds.getData(right).isOcean()
               && BiomeIds.getData(down).isOcean()
               && BiomeIds.getData(up).isOcean()
            ? centerData.deepOceanVariant().get()
            : center;
      }
   }

   public static class RemoveEdges extends DeepOceanBiomeFunc {
      @Override
      public int transformPixel(int center, int left, int right, int down, int up, Random rand) {
         BiomeData centerData = BiomeIds.getData(center);
         return !centerData.isDeepOcean()
               || !BiomeIds.getData(left).isShallowOcean()
                  && !BiomeIds.getData(right).isShallowOcean()
                  && !BiomeIds.getData(down).isShallowOcean()
                  && !BiomeIds.getData(up).isShallowOcean()
            ? center
            : centerData.shallowOceanVariant().get();
      }
   }
}
