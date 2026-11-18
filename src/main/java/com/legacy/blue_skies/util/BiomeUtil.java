package com.legacy.blue_skies.util;

import com.legacy.blue_skies.BlueSkies;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

public class BiomeUtil {
   public static Optional<ResourceKey<Biome>> getOptionalKeyFromBiome(LevelAccessor world, Biome biomeIn) {
      return world.registryAccess().registryOrThrow(Registries.BIOME).getResourceKey(biomeIn);
   }

   public static ResourceKey<Biome> getKeyFromBiome(LevelAccessor world, Biome biomeIn) {
      Optional<ResourceKey<Biome>> biome = getOptionalKeyFromBiome(world, biomeIn);
      if (biome.isPresent()) {
         return biome.get();
      } else {
         BlueSkies.LOGGER.error("Failed to get the registry key from " + biomeIn + ". This is not good!");
         return null;
      }
   }
}
