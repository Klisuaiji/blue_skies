package com.legacy.blue_skies.world.biome_provider.region;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height.HeightPixelFunction;
import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;
import net.minecraft.util.Mth;

public class HeightRegion extends AbstractRegion<HeightPixelFunction> {
   public final BiomeRegion biomeRegion;
   private int[][] heights;

   public HeightRegion(AbstractBiomeProvider biomeProvider, int regionX, int regionZ, int scale, BiomeRegion biomeRegion) {
      super(biomeProvider, regionX, regionZ, scale);
      this.biomeRegion = biomeRegion;
      this.heights = new int[scale][scale];

      for (int x = 0; x < scale; x++) {
         for (int z = 0; z < scale; z++) {
            this.heights[x][z] = BiomeIds.getData(biomeRegion.getData()[x][z]).height();
         }
      }
   }

   @Override
   protected int toGlobalValue(int val, int regionVal) {
      int scale = this.scale / 2;
      int coordScale = 1024 / scale;
      return regionVal * scale * coordScale + val * coordScale;
   }

   @Override
   public int[][] getData() {
      return this.heights;
   }

   @Override
   protected void bakeData() {
      int newScale = this.scale / 2;
      int offset = newScale / 2;
      int[][] croppedHeights = new int[newScale][newScale];

      for (int x = 0; x < newScale; x++) {
         for (int z = 0; z < newScale; z++) {
            croppedHeights[x][z] = Mth.clamp(this.heights[x + offset][z + offset], this.biomeProvider.minY, this.biomeProvider.maxY);
         }
      }

      this.setScale(newScale);
      this.heights = croppedHeights;
   }

   public void transform(HeightPixelFunction func, long seedModifier) {
      int[][] transformed = new int[this.scale][this.scale];
      int negativeEdge = (this.scale - Math.min(this.scale, this.scale / 2 + 16)) / 2;
      int positiveEdge = this.scale - negativeEdge;
      int negativeBorder = Math.max(negativeEdge - 1, 0);
      int positiveBorder = Math.min(positiveEdge + 1, this.scale - 1);

      for (int x = negativeBorder; x <= positiveBorder; x++) {
         int[] transformedX = transformed[x];
         int[] rawX = this.heights[x];

         for (int z = negativeBorder; z <= positiveBorder; z++) {
            if (x >= negativeEdge && x <= positiveEdge && z >= negativeEdge && z <= positiveEdge) {
               transformedX[z] = func.transformPixel(this, x, z, this.getRand(seedModifier, x, z));
            } else {
               transformedX[z] = rawX[z];
            }
         }
      }

      this.heights = transformed;
   }
}
