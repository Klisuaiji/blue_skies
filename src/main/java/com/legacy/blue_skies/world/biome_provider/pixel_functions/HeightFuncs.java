package com.legacy.blue_skies.world.biome_provider.pixel_functions;

import com.legacy.blue_skies.util.LogicUtil;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeData;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height.HeightForBiomeFunction;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height.HeightPixelFunction;
import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public class HeightFuncs {
   public static final HeightForBiomeFunction BASE_NOISE = HeightFuncs::applyBaseNoise;
   public static final HeightForBiomeFunction ADDITIONAL_NOISE = HeightFuncs::additionalNoise;
   public static final HeightPixelFunction LARGE_AVERAGE = HeightFuncs::largeAverage;
   public static final HeightPixelFunction SMALL_AVERAGE = HeightFuncs::smallAverage;
   private static final double MAIN_SCALE_A = 0.003;
   private static final double MAIN_SCALE_B = 0.03;
   private static final double VALLEY_SCALE = 0.006;
   private static final double UNORTHODOX_SCALE_A = 0.004;
   private static final double UNORTHODOX_SCALE_B = 0.02;
   private static final double UNORTHODOX_VALLEY_SCALE = 0.007;

   private static int largeAverage(HeightRegion region, int x, int z, Random rand) {
      int total = 0;
      int count = 0;
      int range = 7;
      int internalRange = 6;

      for (int dx = -6; dx <= 6; dx++) {
         for (int dz = -6; dz <= 6; dz++) {
            total += region.get(x + dx, z + dz);
            count++;
         }
      }

      for (int d = -2; d <= 2; d++) {
         total += region.get(x + d, z + 7);
         total += region.get(x + d, z - 7);
         total += region.get(x + 7, z + d);
         total += region.get(x - 7, z + d);
         count += 4;
      }

      int outerPieces = 8;
      total += region.get(x, z + 8);
      total += region.get(x, z - 8);
      total += region.get(x + 8, z);
      total += region.get(x - 8, z);
      count += 4;
      return total / count;
   }

   private static int smallAverage(HeightRegion region, int x, int z, Random rand) {
      int total = region.getDirect(x, z);
      int count = 1;
      int r = 2;

      for (int dx = -r; dx <= r; dx++) {
         for (int dz = -r; dz <= r; dz++) {
            if (Math.abs(dx) + Math.abs(dz) <= r) {
               total += region.get(x + dx, z + dz);
               count++;
            }
         }
      }

      return Math.round((float)total / count);
   }

   private static int applyBaseNoise(HeightRegion region, int height, int biome, int globalX, int globalZ) {
      BiomeData data = BiomeIds.getData(biome);
      return data.mainHeightFunc().transformPixel(region, height, data, globalX, globalZ);
   }

   public static int mainHeightFunc(HeightRegion region, int height, BiomeData data, int globalX, int globalZ) {
      int variance = data.variance();
      if (variance > 0) {
         double[] noises = getDualNoise(region, globalX, globalZ, region.biomeProvider.noiseA, 0.003, region.biomeProvider.noiseB, 0.03);
         double mergedNoise = noises[0] * 0.93 + noises[1] * 0.07;
         height += (int)(mergedNoise * variance);
      }

      int valley = data.valleyDepth();
      if (valley > 0) {
         double val = Math.abs(region.biomeProvider.noiseC.getValue(globalX * 0.006, globalZ * 0.006, false)) * 2.0 - 1.0;
         height = (int)(height + val * valley);
      }

      return height;
   }

   public static int unorthodoxHeightFunc(HeightRegion region, int height, BiomeData data, int globalX, int globalZ) {
      double[] noises = getDualNoise(region, globalX, globalZ, region.biomeProvider.noiseA, 0.004, region.biomeProvider.noiseB, 0.02);
      double mergedNoise = 1.0 - Math.abs(noises[0] * 0.93 + noises[1] * 0.07);
      mergedNoise = mergedNoise * 2.0 - 1.0;
      mergedNoise = Mth.clamp(mergedNoise * 1.3, 0.0, 1.0);
      height += (int)(mergedNoise * data.variance());
      double val = Math.abs(region.biomeProvider.noiseC.getValue(globalX * 0.007, globalZ * 0.007, false)) * 2.0 - 1.0;
      if (height < data.height() + 15) {
         val *= 0.2;
      }

      return (int)(height + val * data.valleyDepth());
   }

   private static int additionalNoise(HeightRegion region, int height, int biome, int globalX, int globalZ) {
      BiomeData data = BiomeIds.getData(biome);
      int pointiness = data.pointiness();
      if (pointiness > 0) {
         double pointScale = 0.018;
         double pointNoiseVal = 1.0 - Math.abs(region.biomeProvider.noiseB.getValue(globalX * pointScale, globalZ * pointScale, false)) * 2.0;
         if (pointNoiseVal > 0.0) {
            double maskScale = 0.01;
            double maskVal = region.biomeProvider.noiseA.getValue(globalX * maskScale, globalZ * maskScale, false);
            if (maskVal > 0.0) {
               height = (int)(height + pointNoiseVal * maskVal * pointiness);
            }
         }
      }

      double stepSeparation = data.stepSeparation();
      if (stepSeparation > 0.0) {
         double stepScale = 0.011;
         double stepNoiseThreshold = data.stepNoiseThreshold();
         double noiseVal = region.biomeProvider.noiseC.getValue(globalX * stepScale, globalZ * stepScale, false);
         double h = Math.floor(height / stepSeparation) * stepSeparation;
         double r = Math.floorMod(height, (int)stepSeparation);
         double n = Mth.lerp(noiseVal, -1.0, 1.0) * stepSeparation / 2.0;
         double newHeight = h + n + r * data.stepMaxHeight() / stepSeparation;
         if (noiseVal > stepNoiseThreshold) {
            height = (int)newHeight;
         } else if (noiseVal > stepNoiseThreshold - 0.23) {
            double p = LogicUtil.convertToNewRange(noiseVal, stepNoiseThreshold - 0.23, stepNoiseThreshold, 0.0, 1.0);
            height = (int)(newHeight * p + height * (1.0 - p));
         }
      }

      int roughness = data.roughness();
      if (roughness > 0) {
         double scale = 0.01;
         double r = region.biomeProvider.noiseC.getValue(globalX * scale, globalZ * scale, false);
         if (r * r > 0.65) {
            scale *= 0.8;
            height = (int)(height + roughness * Math.abs(region.biomeProvider.noiseC.getValue(globalX * scale, globalZ * scale, true)));
         }
      }

      if (biome == BiomeIds.UNORTHODOX_VALLEY) {
         if (height > 185) {
            height = height / 10 * 10 + (int)(height % 10 * 0.2);
         } else if (height > 155) {
            height = height / 6 * 6;
         }
      }

      return height;
   }

   private static double[] getDualNoise(
      HeightRegion region, int globalX, int globalZ, PerlinSimplexNoise noiseA, double scaleA, PerlinSimplexNoise noiseB, double scaleB
   ) {
      return new double[]{noiseA.getValue(globalX * scaleA, globalZ * scaleA, false), noiseB.getValue(globalX * scaleB, globalZ * scaleB, true)};
   }
}
