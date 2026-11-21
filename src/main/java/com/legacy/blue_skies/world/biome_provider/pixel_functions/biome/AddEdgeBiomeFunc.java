package com.legacy.blue_skies.world.biome_provider.pixel_functions.biome;

import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.AreaBiomeFunction;
import java.util.Random;

public abstract class AddEdgeBiomeFunc implements AreaBiomeFunction {
   public record Inner(int biomeId, int edgeId) implements AreaBiomeFunction {
      @Override
      public int transformPixel(int center, int left, int right, int down, int up, Random rand) {
         return center != this.biomeId || left == this.biomeId && right == this.biomeId && down == this.biomeId && up == this.biomeId ? center : this.edgeId;
      }
   }

   public record Outer(int biomeId, int edgeId) implements AreaBiomeFunction {
      @Override
      public int transformPixel(int center, int left, int right, int down, int up, Random rand) {
         return center == this.biomeId
               || left != this.biomeId
                  && right != this.biomeId
                  && down != this.biomeId
                  && up != this.biomeId
                  && left != this.edgeId
                  && right != this.edgeId
                  && down != this.edgeId
                  && up != this.edgeId
            ? center
            : this.edgeId;
      }
   }
}
