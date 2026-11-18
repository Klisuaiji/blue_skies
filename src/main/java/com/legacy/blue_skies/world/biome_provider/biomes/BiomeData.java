package com.legacy.blue_skies.world.biome_provider.biomes;

import com.legacy.blue_skies.world.biome_provider.pixel_functions.HeightFuncs;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.api.height.HeightForBiomeDataPixelFunction;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public record BiomeData(
   int alias,
   BiomeBasedChunkGen biomeBasedChunkGen,
   BiomeIds.Temperature[] temperatures,
   int height,
   int variance,
   HeightForBiomeDataPixelFunction mainHeightFunc,
   int valleyDepth,
   int pointiness,
   int stepSeparation,
   int stepMaxHeight,
   double stepNoiseThreshold,
   int roughness,
   boolean isShallowOcean,
   boolean isDeepOcean,
   Supplier<Integer> shallowOceanVariant,
   Supplier<Integer> deepOceanVariant,
   boolean hasBeaches,
   boolean canBecomeRiver
) {
   public boolean isOcean() {
      return this.isShallowOcean || this.isDeepOcean;
   }

   public boolean hasBeaches() {
      return !this.isOcean() && this.hasBeaches;
   }

   public boolean createsBeaches() {
      return this.isOcean();
   }

   public boolean canBecomeRiver() {
      return !this.isOcean() && this.canBecomeRiver;
   }

   public static BiomeData.Builder builder(int height, int variance) {
      return new BiomeData.Builder(height, variance);
   }

   public static class Builder {
      private int alias = -1;
      private BiomeBasedChunkGen biomeBasedChunkGen = BiomeBasedChunkGen.DEFAULT;
      private BiomeIds.Temperature[] temperatures = new BiomeIds.Temperature[]{BiomeIds.Temperature.NEUTRAL};
      private final int height;
      private final int variance;
      private HeightForBiomeDataPixelFunction mainHeightFunc = HeightFuncs::mainHeightFunc;
      private int valleyDepth = 0;
      private int pointiness = 0;
      private int stepSeparation = 0;
      private int stepMaxHeight = 0;
      private double stepNoiseThreshold = 1.0;
      private int roughness = 0;
      private boolean isShallowOcean = false;
      private boolean isDeepOcean = false;
      private Supplier<Integer> shallowOceanVariant = () -> BiomeIds.OCEAN;
      private Supplier<Integer> deepOceanVariant = () -> BiomeIds.DEEP_OCEAN;
      private boolean hasBeaches = true;
      private boolean canBecomeRiver = true;

      private Builder(int height, int variance) {
         this.height = height;
         this.variance = variance;
         this.valleyDepth = variance / 2;
      }

      public BiomeData.Builder biomeBasedChunkGen(BiomeBasedChunkGen biomeBasedChunkGen) {
         this.biomeBasedChunkGen = biomeBasedChunkGen;
         return this;
      }

      public BiomeData.Builder temperatures(BiomeIds.Temperature... temperatures) {
         this.temperatures = temperatures;
         return this;
      }

      public BiomeData.Builder mainHeightFunc(HeightForBiomeDataPixelFunction mainHeightFunc) {
         this.mainHeightFunc = mainHeightFunc;
         return this;
      }

      public BiomeData.Builder valleyDepth(int valleyDepth) {
         this.valleyDepth = valleyDepth;
         return this;
      }

      public BiomeData.Builder pointiness(int pointiness) {
         this.pointiness = pointiness;
         return this;
      }

      public BiomeData.Builder steps(int separation, int maxHeight, double noiseThreshold) {
         this.stepSeparation = separation;
         this.stepMaxHeight = maxHeight;
         this.stepNoiseThreshold = noiseThreshold;
         return this;
      }

      public BiomeData.Builder roughness(int roughness) {
         this.roughness = roughness;
         return this;
      }

      public BiomeData.Builder isShallowOcean(Supplier<Integer> deepOceanVariant) {
         this.isShallowOcean = true;
         this.deepOceanVariant = deepOceanVariant;
         return this;
      }

      public BiomeData.Builder isDeepOcean(Supplier<Integer> shallowOceanVariant) {
         this.isDeepOcean = true;
         this.shallowOceanVariant = shallowOceanVariant;
         return this;
      }

      public BiomeData.Builder noBeaches() {
         this.hasBeaches = false;
         return this;
      }

      public BiomeData.Builder cantBecomeRiver() {
         this.canBecomeRiver = false;
         return this;
      }

      public BiomeData.Builder alias(int aliasId) {
         this.alias = aliasId;
         return this;
      }

      public BiomeData build(int id, ResourceLocation name) {
         if (this.alias == -1 && id != this.alias) {
            this.alias = id;
         }

         if (id < 0 && this.alias < 0) {
            throw new IllegalStateException("The id or alias of " + name + " is a negative value. Must be 0 or greater. id=" + id + ", alias=" + this.alias);
         } else {
            return new BiomeData(
               this.alias,
               this.biomeBasedChunkGen,
               this.temperatures,
               this.height,
               this.variance,
               this.mainHeightFunc,
               this.valleyDepth,
               this.pointiness,
               this.stepSeparation,
               this.stepMaxHeight,
               this.stepNoiseThreshold,
               this.roughness,
               this.isShallowOcean,
               this.isDeepOcean,
               this.shallowOceanVariant,
               this.deepOceanVariant,
               this.hasBeaches,
               this.canBecomeRiver
            );
         }
      }
   }
}
