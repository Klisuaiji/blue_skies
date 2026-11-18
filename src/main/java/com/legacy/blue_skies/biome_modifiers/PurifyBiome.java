package com.legacy.blue_skies.biome_modifiers;

import com.legacy.blue_skies.BlueSkiesConfig;
import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifier.Phase;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public class PurifyBiome implements BiomeModifier {
   public static final PurifyBiome INSTANCE = new PurifyBiome();
   public static final Codec<PurifyBiome> CODEC = Codec.unit(() -> INSTANCE);

   private PurifyBiome() {
   }

   public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
      if (phase == Phase.REMOVE && biome.unwrapKey().map(biomeKey -> "blue_skies".equals(biomeKey.location().getNamespace())).orElse(false)) {
         for (Decoration stage : Decoration.values()) {
            builder.getGenerationSettings()
               .getFeatures(stage)
               .removeIf(
                  feature -> {
                     Optional<ResourceKey<PlacedFeature>> placedFeatureKey = feature.unwrapKey();
                     return placedFeatureKey.isPresent()
                        ? !BlueSkiesConfig.COMMON.isModAllowedForFeatureGen(placedFeatureKey.get().location().getNamespace())
                        : false;
                  }
               );
         }

         for (MobCategory category : MobCategory.values()) {
            builder.getMobSpawnSettings().getSpawner(category).removeIf(spawnerData -> !BlueSkiesConfig.COMMON.isEntityAllowedToSpawn(spawnerData.type));
         }
      }
   }

   public Codec<? extends BiomeModifier> codec() {
      return CODEC;
   }
}
