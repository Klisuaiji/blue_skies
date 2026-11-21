package com.legacy.blue_skies.world.biome_provider.region;

import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.BiomeFuncs;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.biome.BiomePixelFunction;
import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;

public class BiomeRegion extends AbstractRegion<BiomePixelFunction> {
   private int[][] biomes = new int[this.scale][this.scale];

   public BiomeRegion(AbstractBiomeProvider biomeProvider, int regionX, int regionZ, int scale) {
      super(biomeProvider, regionX, regionZ, scale * 2);

      for (int x = 0; x < this.scale; x++) {
         for (int z = 0; z < this.scale; z++) {
            this.biomes[x][z] = BiomeIds.PLAINS;
         }
      }
   }

   @Override
   public int[][] getData() {
      return this.biomes;
   }

   @Override
   protected void bakeData() {
      int newScale = this.scale / 2;
      int offset = newScale / 2;
      int[][] croppedBiomes = new int[newScale][newScale];

      for (int x = 0; x < newScale; x++) {
         for (int z = 0; z < newScale; z++) {
            croppedBiomes[x][z] = BiomeIds.getData(this.biomes[x + offset][z + offset]).alias();
         }
      }

      this.setScale(newScale);
      this.biomes = croppedBiomes;
   }

   public void transform(BiomePixelFunction func, long seedModifier) {
      int[][] transformed = new int[this.scale][this.scale];
      int negativeEdge = (this.scale - Math.min(this.scale, this.scale / 2 + 16)) / 2;
      int positiveEdge = this.scale - negativeEdge;
      int negativeBorder = Math.max(negativeEdge - 1, 0);
      int positiveBorder = Math.min(positiveEdge + 1, this.scale - 1);

      for (int x = negativeBorder; x <= positiveBorder; x++) {
         int[] transformedX = transformed[x];
         int[] rawX = this.biomes[x];

         for (int z = negativeBorder; z <= positiveBorder; z++) {
            if (x >= negativeEdge && x <= positiveEdge && z >= negativeEdge && z <= positiveEdge) {
               transformedX[z] = func.transformPixel(this, x, z, this.getRand(seedModifier, x, z));
            } else {
               transformedX[z] = rawX[z];
            }
         }
      }

      this.biomes = transformed;
   }

   public void upscale() {
      int newScale = this.scale * 2;
      int[][] upscaled = new int[newScale][newScale];

      for (int x = 0; x < newScale; x++) {
         for (int z = 0; z < newScale; z++) {
            upscaled[x][z] = this.biomes[x / 2][z / 2];
         }
      }

      this.setScale(newScale);
      this.biomes = upscaled;
   }

   public void upscaleAndEdges(long seedModifier) {
      this.upscale();
      this.transform(BiomeFuncs.FUZZ_EDGES, seedModifier);
      this.transform(BiomeFuncs.SMOOTH_EDGES, seedModifier);
      this.transform(BiomeFuncs.REMOVE_LONELY, seedModifier);
   }
}
