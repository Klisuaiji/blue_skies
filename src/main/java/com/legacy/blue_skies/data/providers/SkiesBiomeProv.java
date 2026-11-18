package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.registries.SkiesConfiguredCarvers;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesFeaturePlacements;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.MobSpawnSettings.Builder;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;

public class SkiesBiomeProv {
   public static class Makers {
      protected static final int BRIGHT_FOLIAGE_COLOR = 7059637;
      protected static final int DAWN_FOLIAGE_COLOR = 12485859;

      public static Biome calmingSkies(BootstapContext<?> bootstrap) {
         int grassColor = 16777215;
         int foliageColor = 7059637;
         int waterColor = 2903689;
         int waterFogColor = 2903689;
         int skyColor = 9684223;
         float temp = 0.3F;
         float downfall = 0.0F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.STARDUST_RAM, 8, 2, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.AZULFO, 6, 4, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 2, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.DIOPHYDE_PROWLER, 1, 1, 2));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CALMING_SKIES_BLUEBRIGHT_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CALMING_SKIES_STARLIT_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_NOISE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRITTLEBUSH_3);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.POLAR_POSY_3);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CALMING_SKIES_CAVE_DRIPSTONE);
         SkiesFeaturePlacements.addBrightLakes(builder);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, true, true);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome brightlands(BootstapContext<?> bootstrap) {
         int grassColor = 14016767;
         int foliageColor = 7059637;
         int waterColor = 2903689;
         int waterFogColor = 2903689;
         int skyColor = 9684223;
         float temp = 0.0F;
         float downfall = 0.8F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.STARDUST_RAM, 8, 2, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.REINDEER, 4, 2, 3));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.AZULFO, 6, 4, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.CRYNOCEROUS, 1, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 2, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.DIOPHYDE_PROWLER, 1, 1, 2));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRIGHTLANDS_FALLEN_LOG);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRIGHTLANDS_STARLIT_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_3);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CHILLWEED_3);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOWBLOOM_2);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.BRIGHTLANDS_CAVE_GRASS);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.BRIGHTLANDS_GIANT_CAVE_ICICLE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.BRIGHTLANDS_CAVE_WALL);
         SkiesFeaturePlacements.addBrightLakes(builder);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, true, true);
         SkiesFeaturePlacements.addEmeraldOre(builder, true);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome slushlands(BootstapContext<?> bootstrap) {
         int grassColor = 12119807;
         int foliageColor = 7059637;
         int waterColor = 2903689;
         int waterFogColor = 2903689;
         int skyColor = 7252691;
         float temp = 0.5F;
         float downfall = 0.0F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.STARDUST_RAM, 8, 2, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.AZULFO, 6, 4, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 2, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.SHRUMPTY, 2, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SLUSHLANDS_LARGE_SNOWCAP_MUSHROOM);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SLUSHLANDS_SNOWCAP_MUSHROOM);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SLUSHLANDS_CHILLED_LILY);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.PATCHY_SNOW);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRITTLEBUSH_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOWCAP_PINHEAD_8);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.SLUSHLANDS_CAVE_WALL);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SLUSHLANDS_CAVE_BLUEBRIGHT_VINE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SLUSHLANDS_CAVE_PINHEAD);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, false, false);
         SkiesFeaturePlacements.addBrightDoubleGrass(builder);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome frostbittenForest(BootstapContext<?> bootstrap, boolean isClearing) {
         int grassColor = 13288447;
         int foliageColor = 7059637;
         int waterColor = 6583020;
         int waterFogColor = 6583020;
         int skyColor = 6654717;
         float temp = 0.0F;
         float downfall = 0.0F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SNOW_OWL, 6, 2, 2));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 2, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.DIOPHYDE_PROWLER, 1, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_BUSH);
         if (isClearing) {
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_SMALL_FROSTBRIGHT_TREE);
         } else {
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_FROSTBRIGHT_TREE);
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_BLUEBRIGHT_TREE);
         }

         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRITTLEBUSH_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CHILLWEED_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOWBLOOM_2);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_CAVE_FROSTBRIGHT_VINE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_CAVE_GRASS);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.FROSTBITTEN_FOREST_CAVE_WALL);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, true, false);
         SkiesFeaturePlacements.addBrightDoubleGrass(builder);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome peekingOcean(BootstapContext<?> bootstrap, boolean isDeep, boolean isBrumbleForest) {
         int grassColor = 16777215;
         int foliageColor = 7059637;
         int waterColor = isDeep ? 4672964 : 6583020;
         int waterFogColor = isDeep ? 4672964 : 6583020;
         int skyColor = 9684223;
         float temp = 0.2F;
         float downfall = 0.4F;
         Builder spawns = new Builder();
         double fishCost = 0.05;
         double fishBudget = 1.0;
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.COD, 15, 3, 6));
         spawns.addMobCharge(EntityType.COD, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.JELLY_DRIFTER, 5, 1, 3));
         spawns.addMobCharge(SkiesEntityTypes.JELLY_DRIFTER, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.GRITTLE_FLATFISH, 5, 2, 3));
         spawns.addMobCharge(SkiesEntityTypes.GRITTLE_FLATFISH, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.MUNICIPAL_MONKFISH, 15, 3, 6));
         spawns.addMobCharge(SkiesEntityTypes.MUNICIPAL_MONKFISH, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 25, 2, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.WHISTLESHELL_CRAB, 20, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.SECLAM, 10, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.PEEKING_OCEAN_BRUMBLE);
         if (isBrumbleForest) {
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRUMBLE_FOREST_BRUMBLE_VINE);
         } else {
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.PEEKING_OCEAN_SEA_MOSS);
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.PEEKING_OCEAN_BRUMBLE_VINE);
            builder.addFeature(Decoration.SURFACE_STRUCTURES, SkiesFeaturePlacements.PEEKING_OCEAN_SPIKE);
         }

         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.PEEKING_OCEAN_CAVE_MOSS_CARPET);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.PEEKING_OCEAN_CAVE_DENSE_MOSS_WALL);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.PEEKING_OCEAN_CAVE_MOSS_WALL);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_5);
         SkiesConfiguredCarvers.addOceanCarvers(builder);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, true, false);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome middayShore(BootstapContext<?> bootstrap) {
         int grassColor = 16777215;
         int foliageColor = 7059637;
         int waterColor = 6583020;
         int waterFogColor = 6583020;
         int skyColor = 10994416;
         float temp = 0.3F;
         float downfall = 0.4F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.WHISTLESHELL_CRAB, 20, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.MIDDAY_BAYHOP_6);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, false, false);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome snowCoveredPines(BootstapContext<?> bootstrap) {
         int grassColor = 14016767;
         int foliageColor = 7059637;
         int waterColor = 2903689;
         int waterFogColor = 2903689;
         int skyColor = 9684223;
         float temp = 0.0F;
         float downfall = 0.8F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.STARDUST_RAM, 8, 2, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.REINDEER, 4, 2, 3));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.AZULFO, 6, 4, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.CRYNOCEROUS, 1, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 2, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.DIOPHYDE_PROWLER, 1, 1, 2));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOW_COVERED_PINES_STARLIT_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_2);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRITTLEBUSH_3);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CHILLWEED_3);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOWBLOOM_2);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOW_COVERED_PINES_CAVE_STARLIT_VINE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SNOW_COVERED_PINES_WALL_MOONSTONE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.SNOW_COVERED_PINES_CAVE_WALL);
         SkiesFeaturePlacements.addBrightLakes(builder);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, true, true);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome briskMeadow(BootstapContext<?> bootstrap) {
         int grassColor = 16777215;
         int foliageColor = 7059637;
         int waterColor = 2903689;
         int waterFogColor = 2903689;
         int skyColor = 9684223;
         float temp = 0.3F;
         float downfall = 0.0F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.STARDUST_RAM, 5, 2, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.AZULFO, 8, 4, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 2, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.DIOPHYDE_PROWLER, 1, 1, 2));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRISK_MEADOW_BLUEBRIGHT_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRISK_MEADOW_BUSH);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRISK_MEADOW_FLOWER);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_8);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BREWBERRY_3);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.BRISK_MEADOW_CAVE_WALL_STONE_RIMESTONE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.BRISK_MEADOW_CAVE_WALL_RIMESTONE_STONE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BRISK_MEADOW_CAVE_FROSE);
         SkiesFeaturePlacements.addBrightLakes(builder);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, false, false);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(
               createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor)
                  .ambientParticle(new AmbientParticleSettings(SkiesParticles.BRISK_PETAL, 0.012F))
                  .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome polarHighland(BootstapContext<?> bootstrap) {
         int grassColor = 11657944;
         int foliageColor = 7059637;
         int waterColor = 2903689;
         int waterFogColor = 2903689;
         int skyColor = 7252691;
         float temp = 0.3F;
         float downfall = 0.0F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.STARDUST_RAM, 5, 2, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.AZULFO, 8, 4, 4));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 3, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.DIOPHYDE_PROWLER, 2, 1, 2));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.POLARGEIST, 1, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.LOCAL_MODIFICATIONS, SkiesFeaturePlacements.POLAR_HIGHLAND_ROCK);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.POLAR_HIGHLAND_VEGETATION);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.TURQUOISE_GRASS_NOISE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.POLAR_POSY_6);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.POLAR_HIGHLAND_CAVE_WALL_TARATITE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.POLAR_HIGHLAND_CAVE_GRASS);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.POLAR_HIGHLAND_CAVE_POSY);
         SkiesFeaturePlacements.addBrightLakes(builder);
         SkiesFeaturePlacements.addGeneralBrightFeatures(builder, true, true);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome unorthodoxValley(BootstapContext<?> bootstrap) {
         int grassColor = 14843647;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 9471171;
         float temp = 1.3F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.COSMIC_FOX, 6, 1, 2));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 8, 2, 3));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.EMBERBACK, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.UNORTHODOX_VALLEY_DUSK_LUNAR_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.UNORTHODOX_VALLEY_CRESCENT_FRUIT_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.UNORTHODOX_VALLEY_BUSH);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.RISING_CREEK_MOONLIT_WATER_LILY);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.NIGHTCRESS_2);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.UNORTHODOX_VALLEY_CAVE_DRIPSTONE);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, true, false);
         SkiesFeaturePlacements.addEmeraldOre(builder, false);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome shadedWoodlands(BootstapContext<?> bootstrap) {
         int grassColor = 13151691;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 0;
         float temp = 1.1F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.AMBIENT, new SpawnerData(SkiesEntityTypes.FIREFLY, 10, 3, 5));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.COSMIC_FOX, 5, 4, 4));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 8, 2, 3));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.NYCTOFLY, 5, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SHADED_WOODLANDS_LARGE_DUSK_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_MUCKWEED_20);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_MUCKWEED_NOISE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SHADED_WOODLANDS_FLOWER);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.BANEFUL_MUSHROOM_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SHADED_WOODLANDS_CAVE_DUSK_VINE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.SHADED_WOODLANDS_CAVE_WALL);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, false, false);
         SkiesFeaturePlacements.addDawnDoubleGrass(builder);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(
               createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor)
                  .ambientLoopSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SkiesSounds.AMBIENT_SHADED_WOODLANDS_LOOP))
                  .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome crystalDunes(BootstapContext<?> bootstrap, boolean hasSpikes) {
         int grassColor = 16753663;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 15784896;
         float temp = 1.7F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.CRYSTAL_CAMEL, 5, 2, 3));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 2, 2, 3));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.EMBERBACK, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.SURFACE_STRUCTURES, SkiesFeaturePlacements.CRYSTAL_DUNES_MOONSTONE_SPIKE);
         if (hasSpikes) {
            builder.addFeature(Decoration.SURFACE_STRUCTURES, SkiesFeaturePlacements.CRYSTAL_DUNES_LARGE_MOONSTONE_SPIKE);
         }

         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LIGHT_CRYSTALLIZED_GRASS_2);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CRYSTAL_FLOWER_2);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRYSTAL_DUNES_CAVE_MOONSTONE_CRYSTAL);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRYSTAL_DUNES_CAVE_GRASS);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CRYSTAL_DUNES_CAVE_WALL_MOONSTONE);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, false, true);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome risingCreek(BootstapContext<?> bootstrap) {
         int grassColor = 15844607;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 9471171;
         float temp = 1.3F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         double fishCost = 0.05;
         double fishBudget = 1.0;
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.SALMON, 5, 1, 5));
         spawns.addMobCharge(EntityType.SALMON, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.CHARSCALE_MOKI, 5, 3, 5));
         spawns.addMobCharge(SkiesEntityTypes.CHARSCALE_MOKI, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.HORIZOFIN_TUNID, 5, 3, 5));
         spawns.addMobCharge(SkiesEntityTypes.HORIZOFIN_TUNID, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 5, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.RISING_CREEK_MOONLIT_WATER_LILY);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_MUCKWEED_20);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, true, false);
         SkiesFeaturePlacements.addDawnDoubleGrass(builder);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome moonlitReservoir(BootstapContext<?> bootstrap) {
         int grassColor = 9269656;
         int foliageColor = 12485859;
         int waterColor = 4354427;
         int waterFogColor = 4354427;
         int skyColor = 8687555;
         float temp = 1.3F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.CROGRE, 8, 1, 2));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 4, 1, 2));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.NYCTOFLY, 5, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         double fishCost = 0.05;
         double fishBudget = 1.0;
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.SALMON, 5, 1, 5));
         spawns.addMobCharge(EntityType.SALMON, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.CHARSCALE_MOKI, 5, 3, 5));
         spawns.addMobCharge(SkiesEntityTypes.CHARSCALE_MOKI, fishCost, fishBudget);
         spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.HORIZOFIN_TUNID, 5, 3, 5));
         spawns.addMobCharge(SkiesEntityTypes.HORIZOFIN_TUNID, fishCost, fishBudget);
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.MOONLIT_RESERVOIR_DUSK_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.MOONLIT_RESERVOIR_GLIMMER_REEDS);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.MOONLIT_RESERVOIR_MOONLIT_WATER_LILY);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_MUCKWEED_NOISE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUCENTROOT_2);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.MOONLIT_RESERVOIR_CAVE_GRASS);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.MOONLIT_RESERVOIR_CAVE_WALL);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, true, false);
         SkiesFeaturePlacements.addDawnDoubleGrass(builder);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(
               createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor)
                  .ambientLoopSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SkiesSounds.AMBIENT_MOONLIT_RESERVOIR_LOOP))
                  .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome crystalRoughs(BootstapContext<?> bootstrap) {
         int grassColor = 13155070;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 14350335;
         float temp = 1.7F;
         float downfall = 0.0F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.CRYSTAL_CAMEL, 2, 2, 3));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 8, 1, 2));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.EMBERBACK, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.SURFACE_STRUCTURES, SkiesFeaturePlacements.CRYSTAL_ROUGHS_LUNAR_TREE);
         builder.addFeature(Decoration.SURFACE_STRUCTURES, SkiesFeaturePlacements.CRYSTAL_ROUGHS_CRYSTALIZED_TREE);
         builder.addFeature(Decoration.SURFACE_STRUCTURES, SkiesFeaturePlacements.CRYSTAL_ROUGHS_SHORT_MOONSTONE_SPIKE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CRYSTALLIZED_GRASS_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CRYSTAL_FLOWER_2);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRYSTAL_ROUGHS_CAVE_GRASS);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, false, true);
         return new BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome sunsetMapleForest(BootstapContext<?> bootstrap) {
         int grassColor = 15829434;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 15770516;
         float temp = 1.5F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.COSMIC_FOX, 5, 1, 2));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 8, 2, 3));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SLIV, 2, 2, 3));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.EMBERBACK, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SUNSET_MAPLE_FOREST_MAPLE_TREE);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_7);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FLARE_FLORET_BLAZE_BUD_LUCENTROOT_4);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SUNSET_MAPLE_FOREST_CAVE_WALL_SUNSTONE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.SUNSET_MAPLE_FOREST_CAVE_MAGMA);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SUNSET_MAPLE_FOREST_CAVE_MAPLE_VINE);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, false, true);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome searingGrassland(BootstapContext<?> bootstrap) {
         int grassColor = 15829396;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 15766676;
         float temp = 1.6F;
         float downfall = 0.1F;
         Builder spawns = new Builder();
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.COSMIC_FOX, 5, 1, 2));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 8, 2, 3));
         spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SLIV, 2, 2, 3));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.EMBERBACK, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SEARING_GRASSLAND_BUSH);
         builder.addFeature(Decoration.LOCAL_MODIFICATIONS, SkiesFeaturePlacements.SEARING_GRASSLAND_ROCK);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_7);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.FLARE_FLORET_BLAZE_BUD_LUCENTROOT_5);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.SEARING_GRASSLAND_CAVE_WALL_SUNSTONE);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.SEARING_GRASSLAND_CAVE_MAGMA);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.SEARING_GRASSLAND_CAVE_WALL);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, false, true);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(
               createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor)
                  .ambientParticle(new AmbientParticleSettings(SkiesParticles.SEARING_SMOKE, 0.01F))
                  .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      public static Biome crescentOrchard(BootstapContext<?> bootstrap, boolean isLake) {
         int grassColor = 12025574;
         int foliageColor = 12485859;
         int waterColor = 4351611;
         int waterFogColor = 4351611;
         int skyColor = 15774612;
         float temp = 1.3F;
         float downfall = 0.3F;
         Builder spawns = new Builder();
         if (isLake) {
            double fishCost = 0.05;
            double fishBudget = 1.0;
            spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.CHARSCALE_MOKI, 5, 3, 5));
            spawns.addMobCharge(SkiesEntityTypes.CHARSCALE_MOKI, fishCost, fishBudget);
            spawns.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(SkiesEntityTypes.HORIZOFIN_TUNID, 3, 3, 5));
            spawns.addMobCharge(SkiesEntityTypes.HORIZOFIN_TUNID, fishCost, fishBudget);
         } else {
            spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.COSMIC_FOX, 5, 1, 2));
            spawns.addSpawn(MobCategory.CREATURE, new SpawnerData(SkiesEntityTypes.SHADE_MONITOR, 8, 2, 3));
         }

         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 7, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new SpawnerData(SkiesEntityTypes.INFESTED_SWARMER, 7, 1, 1));
         net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
            bootstrap.lookup(Registries.PLACED_FEATURE), bootstrap.lookup(Registries.CONFIGURED_CARVER)
         );
         if (!isLake) {
            builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.CRESCENT_ORCHARD_CRESCENT_FRUIT_TREE);
         }

         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.LUNAR_GRASS_4);
         builder.addFeature(Decoration.VEGETAL_DECORATION, SkiesFeaturePlacements.NIGHTCRESS_2);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRESCENT_ORCHARD_CAVE_GRASS);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRESCENT_ORCHARD_CAVE_DIRT);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRESCENT_ORCHARD_CAVE_BLOOM);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRESCENT_ORCHARD_CAVE_NIGHTCRESS);
         builder.addFeature(Decoration.UNDERGROUND_DECORATION, SkiesFeaturePlacements.CRESCENT_ORCHARD_CAVE_LUCENTROOT);
         SkiesFeaturePlacements.addDawnLakes(builder);
         SkiesFeaturePlacements.addGeneralDawnFeatures(builder, true, false);
         return new BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(temp)
            .downfall(downfall)
            .specialEffects(createAmbienceBuilder(grassColor, foliageColor, waterColor, waterFogColor, skyColor).build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(builder.build())
            .build();
      }

      protected static net.minecraft.world.level.biome.BiomeSpecialEffects.Builder createAmbienceBuilder(
         int grassColor, int foliageColor, int waterColor, int waterFogColor, int skyColor
      ) {
         return new net.minecraft.world.level.biome.BiomeSpecialEffects.Builder()
            .fogColor(12638463)
            .skyColor(calcDefaultSkyColor(0.3F))
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .grassColorOverride(grassColor)
            .foliageColorOverride(foliageColor)
            .waterColor(waterColor)
            .waterFogColor(waterFogColor)
            .skyColor(skyColor);
      }

      private static int calcDefaultSkyColor(float chance) {
         float thirdChance = chance / 3.0F;
         thirdChance = Mth.clamp(thirdChance, -1.0F, 1.0F);
         return Mth.hsvToRgb(0.62222224F - thirdChance * 0.05F, 0.5F + thirdChance * 0.1F, 1.0F);
      }
   }
}
