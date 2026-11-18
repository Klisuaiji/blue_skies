package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings.Builder;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.VeryBiasedToBottomHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseBasedCountPlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

public class SkiesFeaturePlacements {
   public static final RegistrarHandler<PlacedFeature> HANDLER = RegistrarHandler.getOrCreate(Registries.PLACED_FEATURE, "blue_skies")
      .bootstrap(SkiesFeaturePlacements::bootstrap);
   private static final Map<ResourceLocation, SkiesFeaturePlacements.PlacedValues> PLACED_VALUES = new HashMap<>();
   public static final ResourceKey<PlacedFeature> NATURE_DUNGEON_COMET_TREE = register(
      "nature_comet_tree",
      SkiesConfiguredFeatures.NATURE_DUNGEON_COMET_TREE,
      VegetationPlacements.treePlacement(CountPlacement.of(1), SkiesBlocks.comet_sapling)
   );
   public static final ResourceKey<PlacedFeature> NATURE_DUNGEON_STARLIT_BUSH = register(
      "nature_dungeon_starlit_bush",
      SkiesConfiguredFeatures.NATURE_DUNGEON_STARLIT_BUSH,
      VegetationPlacements.treePlacement(CountPlacement.of(3), SkiesBlocks.starlit_sapling)
   );
   public static final ResourceKey<PlacedFeature> CAVE_TURQUOISE_DRIPSTONE = register(
      "cave_turquoise_dripstone", SkiesConfiguredFeatures.CAVE_TURQUOISE_DRIPSTONE, countRange(69, 25)
   );
   public static final ResourceKey<PlacedFeature> EVERBRIGHT_CAVE_SPAWNER_CONFIGURED = register(
      "everbright_cave_spawner", SkiesConfiguredFeatures.EVERBRIGHT_CAVE_SPAWNER_CONFIGURED, countRangeRare(49, 1, RarityFilter.onAverageOnceEvery(4))
   );
   public static final ResourceKey<PlacedFeature> CAVE_LUNAR_LAVA_SPIRE = register(
      "cave_lunar_lava_spire", SkiesConfiguredFeatures.CAVE_LUNAR_LAVA_SPIRE, countRange(29, 30)
   );
   public static final ResourceKey<PlacedFeature> CAVE_LUNAR_DRIPSTONE = register(
      "cave_lunar_dripstone", SkiesConfiguredFeatures.CAVE_LUNAR_DRIPSTONE, countRange(69, 25)
   );
   public static final ResourceKey<PlacedFeature> EVERDAWN_CAVE_SPAWNER_CONFIGURED = register(
      "everdawn_cave_spawner", SkiesConfiguredFeatures.EVERDAWN_CAVE_SPAWNER_CONFIGURED, countRangeRare(49, 1, RarityFilter.onAverageOnceEvery(4))
   );
   private static final List<PlacementModifier> OLD_EMERALD_PLACEMENT = List.of(
      CountPlacement.of(UniformInt.of(3, 8)),
      InSquarePlacement.spread(),
      HeightRangePlacement.uniform(VerticalAnchor.absolute(4), VerticalAnchor.absolute(31)),
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> ORE_MOONSTONE = register("ore_moonstone", SkiesConfiguredFeatures.ORE_MOONSTONE, countRange(127, 20));
   public static final ResourceKey<PlacedFeature> ORE_PYROPE = register("ore_pyrope", SkiesConfiguredFeatures.ORE_PYROPE, countRange(83, 17));
   public static final ResourceKey<PlacedFeature> ORE_AQUITE = register("ore_aquite", SkiesConfiguredFeatures.ORE_AQUITE, countRange(54, 15));
   public static final ResourceKey<PlacedFeature> ORE_DIOPSIDE = register("ore_diopside", SkiesConfiguredFeatures.ORE_DIOPSIDE, countRange(19, 1));
   public static final ResourceKey<PlacedFeature> ORE_CHAROITE = register("ore_charoite", SkiesConfiguredFeatures.ORE_CHAROITE, countRange(13, 1));
   public static final ResourceKey<PlacedFeature> ORE_EVERBRIGHT_FALSITE = register(
      "ore_everbright_falsite", SkiesConfiguredFeatures.ORE_EVERBRIGHT_FALSITE, countRange(63, 20)
   );
   public static final ResourceKey<PlacedFeature> ORE_EVERBRIGHT_VENTIUM = register(
      "ore_everbright_ventium", SkiesConfiguredFeatures.ORE_EVERBRIGHT_VENTIUM, countRange(63, 20)
   );
   public static final ResourceKey<PlacedFeature> ORE_EVERBRIGHT_EMERALD = register(
      "ore_everbright_emerald", SkiesConfiguredFeatures.ORE_EVERBRIGHT_EMERALD, OLD_EMERALD_PLACEMENT
   );
   public static final ResourceKey<PlacedFeature> ORE_EVERDAWN_HORIZONITE = register(
      "ore_everdawn_horizonite", SkiesConfiguredFeatures.ORE_EVERDAWN_HORIZONITE, countRange(24, 10)
   );
   public static final ResourceKey<PlacedFeature> ORE_EVERDAWN_EMERALD = register(
      "ore_everdawn_emerald", SkiesConfiguredFeatures.ORE_EVERDAWN_EMERALD, OLD_EMERALD_PLACEMENT
   );
   public static final ResourceKey<PlacedFeature> COARSE_TURQUOISE_DIRT_BLOB = register(
      "coarse_turquoise_dirt_blob", SkiesConfiguredFeatures.COARSE_TURQUOISE_DIRT_BLOB, countRange(255, 10)
   );
   public static final ResourceKey<PlacedFeature> RIMESTONE_BLOB = register(
      "rimestone_blob", SkiesConfiguredFeatures.RIMESTONE_BLOB, RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), range(31), BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> TARATITE_BLOB = register(
      "taratite_blob", SkiesConfiguredFeatures.TARATITE_BLOB, RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), range(89), BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> COARSE_LUNAR_DIRT_BLOB = register(
      "coarse_lunar_dirt_blob", SkiesConfiguredFeatures.COARSE_LUNAR_DIRT_BLOB, countRange(255, 10)
   );
   public static final ResourceKey<PlacedFeature> UMBER_BLOB = register(
      "umber_blob", SkiesConfiguredFeatures.UMBER_BLOB, RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), range(79), BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> CINDERSTONE_BLOB = register(
      "cinderstone_blob",
      SkiesConfiguredFeatures.CINDERSTONE_BLOB,
      RarityFilter.onAverageOnceEvery(4),
      InSquarePlacement.spread(),
      range(31),
      BiomeFilter.biome()
   );
   private static final List<PlacementModifier> LAKE_PLACEMENT = List.of(
      RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> EVERBRIGHT_LAKE_CONFIGURED = register(
      "everbright_lake", SkiesConfiguredFeatures.EVERBRIGHT_LAKE_CONFIGURED, LAKE_PLACEMENT
   );
   public static final ResourceKey<PlacedFeature> EVERDAWN_LAKE_CONFIGURED = register(
      "everdawn_lake", SkiesConfiguredFeatures.EVERDAWN_LAKE_CONFIGURED, LAKE_PLACEMENT
   );
   public static final ResourceKey<PlacedFeature> SPRING_WATER = register(
      "spring_water",
      SkiesConfiguredFeatures.SPRING_WATER,
      CountPlacement.of(50),
      InSquarePlacement.spread(),
      HeightRangePlacement.of(BiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)),
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> SPRING_LAVA = register(
      "spring_lava",
      SkiesConfiguredFeatures.SPRING_LAVA,
      CountPlacement.of(20),
      InSquarePlacement.spread(),
      HeightRangePlacement.of(VeryBiasedToBottomHeight.of(VerticalAnchor.bottom(), VerticalAnchor.belowTop(8), 8)),
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> PATCHY_SNOW = register("patchy_snow", SkiesConfiguredFeatures.PATCHY_SNOW, BiomeFilter.biome());
   public static final ResourceKey<PlacedFeature> ICE_AND_SNOW_NO_PATH_CONFIGURED = register(
      "ice_and_snow_no_path", SkiesConfiguredFeatures.ICE_AND_SNOW_NO_PATH_CONFIGURED, BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> CAMELLIA_2 = register("camellia_2", SkiesConfiguredFeatures.CAMELLIA_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> CAMELLIA_3 = register("camellia_3", SkiesConfiguredFeatures.CAMELLIA_PATCH, flower(3));
   public static final ResourceKey<PlacedFeature> CAMELLIA_4 = register("camellia_4", SkiesConfiguredFeatures.CAMELLIA_PATCH, flower(4));
   public static final ResourceKey<PlacedFeature> POLAR_POSY_3 = register("polar_posy_3", SkiesConfiguredFeatures.POLAR_POSY_PATCH, flower(3));
   public static final ResourceKey<PlacedFeature> POLAR_POSY_6 = register("polar_posy_6", SkiesConfiguredFeatures.POLAR_POSY_PATCH, flower(6));
   public static final ResourceKey<PlacedFeature> SNOWBLOOM_2 = register("snowbloom_2", SkiesConfiguredFeatures.SNOWBLOOM_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> MOONLIT_BLOOM_2 = register("moonlit_bloom_2", SkiesConfiguredFeatures.MOONLIT_BLOOM_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> LUCENTROOT_2 = register("lucentroot_2", SkiesConfiguredFeatures.LUCENTROOT_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> NIGHTCRESS_2 = register("nightcress_2", SkiesConfiguredFeatures.NIGHTCRESS_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> CRYSTAL_FLOWER_2 = register("crystal_flower_2", SkiesConfiguredFeatures.CRYSTAL_FLOWER_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> MIDDAY_BAYHOP_6 = register("midday_bayhop_6", SkiesConfiguredFeatures.MIDDAY_BAYHOP_PATCH, flower(6));
   public static final ResourceKey<PlacedFeature> FLARE_FLORET_BLAZE_BUD_LUCENTROOT_4 = register(
      "flare_floret_blaze_bud_lucentroot_4", SkiesConfiguredFeatures.FLARE_FLORET_BLAZE_BUD_LUCENTROOT_PATCH, flower(4)
   );
   public static final ResourceKey<PlacedFeature> FLARE_FLORET_BLAZE_BUD_LUCENTROOT_5 = register(
      "flare_floret_blaze_bud_lucentroot_5", SkiesConfiguredFeatures.FLARE_FLORET_BLAZE_BUD_LUCENTROOT_PATCH, flower(5)
   );
   public static final ResourceKey<PlacedFeature> SNOWCAP_PINHEAD_8 = register("snowcap_pinhead_8", SkiesConfiguredFeatures.SNOWCAP_PINHEAD_PATCH, flower(8));
   public static final ResourceKey<PlacedFeature> BREWBERRY_2 = register("brewberry_2", SkiesConfiguredFeatures.BREWBERRY_PATCH, flower(2));
   public static final ResourceKey<PlacedFeature> BREWBERRY_3 = register("brewberry_3", SkiesConfiguredFeatures.BREWBERRY_PATCH, flower(3));
   public static final ResourceKey<PlacedFeature> BANEFUL_MUSHROOM_5 = register("baneful_mushroom_5", SkiesConfiguredFeatures.BANEFUL_MUSHROOM_PATCH, flower(5));
   private static final List<PlacementModifier> DEFAULT_NOISE = countNoise(-0.8, 5, 10);
   public static final ResourceKey<PlacedFeature> TURQUOISE_GRASS_2 = register(
      "turquoise_grass_2", SkiesConfiguredFeatures.TURQUOISE_GRASS_PATCH, flower(2, false)
   );
   public static final ResourceKey<PlacedFeature> TURQUOISE_GRASS_3 = register(
      "turquoise_grass_3", SkiesConfiguredFeatures.TURQUOISE_GRASS_PATCH, flower(3, false)
   );
   public static final ResourceKey<PlacedFeature> TURQUOISE_GRASS_5 = register(
      "turquoise_grass_5", SkiesConfiguredFeatures.TURQUOISE_GRASS_PATCH, flower(5, false)
   );
   public static final ResourceKey<PlacedFeature> TURQUOISE_GRASS_8 = register(
      "turquoise_grass_8", SkiesConfiguredFeatures.TURQUOISE_GRASS_PATCH, flower(8, false)
   );
   public static final ResourceKey<PlacedFeature> TURQUOISE_GRASS_NOISE = register(
      "turquoise_grass_noise", SkiesConfiguredFeatures.TURQUOISE_GRASS_PATCH, DEFAULT_NOISE
   );
   public static final ResourceKey<PlacedFeature> TALL_TURQUOISE_GRASS_NOISE = register(
      "tall_turquoise_grass_noise", SkiesConfiguredFeatures.TALL_TURQUOISE_GRASS_PATCH, DEFAULT_NOISE
   );
   public static final ResourceKey<PlacedFeature> BRITTLEBUSH_3 = register("brittlebush_3", SkiesConfiguredFeatures.BRITTLEBUSH_PATCH, flower(3));
   public static final ResourceKey<PlacedFeature> BRITTLEBUSH_5 = register("brittlebush_5", SkiesConfiguredFeatures.BRITTLEBUSH_PATCH, flower(5));
   public static final ResourceKey<PlacedFeature> CHILLWEED_3 = register("chillweed_3", SkiesConfiguredFeatures.CHILLWEED_PATCH, flower(3));
   public static final ResourceKey<PlacedFeature> CHILLWEED_5 = register("chillweed_5", SkiesConfiguredFeatures.CHILLWEED_PATCH, flower(5));
   public static final ResourceKey<PlacedFeature> LUNAR_GRASS_4 = register("lunar_grass_4", SkiesConfiguredFeatures.LUNAR_GRASS_PATCH, flower(4, false));
   public static final ResourceKey<PlacedFeature> LUNAR_GRASS_5 = register("lunar_grass_5", SkiesConfiguredFeatures.LUNAR_GRASS_PATCH, flower(5, false));
   public static final ResourceKey<PlacedFeature> LUNAR_GRASS_7 = register("lunar_grass_7", SkiesConfiguredFeatures.LUNAR_GRASS_PATCH, flower(7, false));
   public static final ResourceKey<PlacedFeature> TALL_LUNAR_GRASS_NOISE = register(
      "tall_lunar_grass_noise", SkiesConfiguredFeatures.TALL_LUNAR_GRASS_PATCH, DEFAULT_NOISE
   );
   public static final ResourceKey<PlacedFeature> MUCKWEED_5 = register("muckweed_5", SkiesConfiguredFeatures.MUCKWEED_PATCH, flower(5));
   public static final ResourceKey<PlacedFeature> LUNAR_GRASS_MUCKWEED_20 = register(
      "lunar_grass_muckweed_20", SkiesConfiguredFeatures.LUNAR_GRASS_MUCKWEED_PATCH, flower(20)
   );
   public static final ResourceKey<PlacedFeature> LUNAR_GRASS_MUCKWEED_NOISE = register(
      "lunar_grass_muckweed_noise", SkiesConfiguredFeatures.LUNAR_GRASS_MUCKWEED_PATCH, DEFAULT_NOISE
   );
   public static final ResourceKey<PlacedFeature> CRYSTALLIZED_GRASS_5 = register(
      "crystallized_grass_5", SkiesConfiguredFeatures.CRYSTALLIZED_GRASS_PATCH, flower(5, false)
   );
   public static final ResourceKey<PlacedFeature> LIGHT_CRYSTALLIZED_GRASS_2 = register(
      "light_crystallized_grass_2", SkiesConfiguredFeatures.LIGHT_CRYSTALLIZED_GRASS_PATCH, flower(2, false)
   );
   public static final ResourceKey<PlacedFeature> CALMING_SKIES_BLUEBRIGHT_TREE = register(
      "calming_skies_bluebright_tree",
      SkiesConfiguredFeatures.BLUEBRIGHT_TREE,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(2), SkiesBlocks.bluebright_sapling)
   );
   public static final ResourceKey<PlacedFeature> CALMING_SKIES_STARLIT_TREE = register(
      "calming_skies_starlit_tree",
      SkiesConfiguredFeatures.STARLIT_TREE,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3), SkiesBlocks.starlit_sapling)
   );
   public static final ResourceKey<PlacedFeature> CALMING_SKIES_CAVE_DRIPSTONE = register(
      "calming_skies_cave_dripstone", SkiesConfiguredFeatures.CAVE_DENSE_TURQUOISE_DRIPSTONE, countRange(69, 25)
   );
   public static final ResourceKey<PlacedFeature> BRIGHTLANDS_FALLEN_LOG = register(
      "brightlands_fallen_log", SkiesConfiguredFeatures.FALLEN_STARLIT_LOG, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(5))
   );
   public static final ResourceKey<PlacedFeature> BRIGHTLANDS_STARLIT_TREE = register(
      "brightlands_starlit_tree",
      SkiesConfiguredFeatures.STARLIT_TREE,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(5), SkiesBlocks.starlit_sapling)
   );
   public static final ResourceKey<PlacedFeature> BRIGHTLANDS_CAVE_GRASS = register(
      "brightlands_cave_grass", SkiesConfiguredFeatures.BRIGHTLANDS_CAVE_GRASS, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> BRIGHTLANDS_GIANT_CAVE_ICICLE = register(
      "brightlands_giant_icicle", SkiesConfiguredFeatures.GIANT_CAVE_ICICLE, countRange(59, 40)
   );
   public static final ResourceKey<PlacedFeature> BRIGHTLANDS_CAVE_WALL = register(
      "brightlands_cave_wall", SkiesConfiguredFeatures.BRIGHTLANDS_CAVE_WALL, countRange(19, 30)
   );
   public static final ResourceKey<PlacedFeature> SLUSHLANDS_LARGE_SNOWCAP_MUSHROOM = register(
      "slushlands_large_snowcap_mushroom",
      SkiesConfiguredFeatures.GIANT_SNOWCAP_MUSHROOM,
      CountPlacement.of(2),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
      SurfaceWaterDepthFilter.forMaxDepth(1),
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> SLUSHLANDS_SNOWCAP_MUSHROOM = register(
      "slushlands_snowcap_mushroom", SkiesConfiguredFeatures.SNOWCAP_MUSHROOM_PATCH, flower(6)
   );
   public static final ResourceKey<PlacedFeature> SLUSHLANDS_CHILLED_LILY = register(
      "slushlands_chilled_lily", SkiesConfiguredFeatures.CHILLED_LILY, flower(23)
   );
   public static final ResourceKey<PlacedFeature> SLUSHLANDS_CAVE_WALL = register(
      "slushlands_cave_wall", SkiesConfiguredFeatures.SLUSHLANDS_CAVE_WALL, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SLUSHLANDS_CAVE_BLUEBRIGHT_VINE = register(
      "slushlands_cave_bluebright_vine", SkiesConfiguredFeatures.SLUSHLANDS_CAVE_BLUEBRIGHT_VINE, countRange(59, 30)
   );
   public static final ResourceKey<PlacedFeature> SLUSHLANDS_CAVE_PINHEAD = register(
      "slushlands_cave_pinhead", SkiesConfiguredFeatures.SLUSHLANDS_CAVE_PINHEAD, countRange(59, 17)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_BUSH = register(
      "frostbitten_forest_bush",
      SkiesConfiguredFeatures.FROSTBRIGHT_BUSH,
      VegetationPlacements.treePlacement(CountPlacement.of(4), SkiesBlocks.frostbright_sapling)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_FROSTBRIGHT_TREE = register(
      "frostbitten_forest_frostbright_tree",
      SkiesConfiguredFeatures.SNOWY_FROSTBRIGHT_TREE,
      VegetationPlacements.treePlacement(CountPlacement.of(8), SkiesBlocks.frostbright_sapling)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_SMALL_FROSTBRIGHT_TREE = register(
      "frostbitten_forest_small_frostbright_tree",
      SkiesConfiguredFeatures.SMALL_FROSTBRIGHT_TREE,
      VegetationPlacements.treePlacement(CountPlacement.of(UniformInt.of(5, 6)), SkiesBlocks.frostbright_sapling)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_BLUEBRIGHT_TREE = register(
      "frostbitten_forest_bluebright_tree",
      SkiesConfiguredFeatures.BLUEBRIGHT_TREE,
      VegetationPlacements.treePlacement(CountPlacement.of(UniformInt.of(5, 6)), SkiesBlocks.bluebright_sapling)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_CAVE_FROSTBRIGHT_VINE = register(
      "frostbitten_forest_cave_frostbright_vine", SkiesConfiguredFeatures.FROSTBITTEN_FOREST_CAVE_FROSTBRIGHT_VINE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_CAVE_GRASS = register(
      "frostbitten_forest_cave_grass", SkiesConfiguredFeatures.FROSTBITTEN_FOREST_CAVE_GRASS, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> FROSTBITTEN_FOREST_CAVE_WALL = register(
      "frostbitten_forest_cave_wall", SkiesConfiguredFeatures.FROSTBITTEN_FOREST_CAVE_WALL, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_SEA_MOSS = register(
      "peeking_ocean_sea_moss", SkiesConfiguredFeatures.PEEKING_OCEAN_SEA_MOSS, BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_BRUMBLE = register(
      "peeking_ocean_brumble", SkiesConfiguredFeatures.PEEKING_OCEAN_BRUMBLE, CountPlacement.of(48), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_BRUMBLE_VINE = register(
      "peeking_ocean_brumble_vine",
      SkiesConfiguredFeatures.PEEKING_OCEAN_BRUMBLE_VINE,
      RarityFilter.onAverageOnceEvery(2),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_SPIKE = register(
      "peeking_ocean_spike",
      SkiesConfiguredFeatures.PEEKING_OCEAN_SPIKE,
      CountPlacement.of(3),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_CAVE_MOSS_CARPET = register(
      "peeking_ocean_cave_moss_carpet", SkiesConfiguredFeatures.PEEKING_OCEAN_CAVE_MOSS_CARPET, countRange(59, 25)
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_CAVE_DENSE_MOSS_WALL = register(
      "peeking_ocean_cave_dense_moss_wall", SkiesConfiguredFeatures.PEEKING_OCEAN_CAVE_DENSE_MOSS_WALL, countRange(59, 25)
   );
   public static final ResourceKey<PlacedFeature> PEEKING_OCEAN_CAVE_MOSS_WALL = register(
      "peeking_ocean_cave_moss_wall", SkiesConfiguredFeatures.PEEKING_OCEAN_CAVE_MOSS_WALL, countRange(59, 15)
   );
   public static final ResourceKey<PlacedFeature> BRUMBLE_FOREST_BRUMBLE_VINE = register(
      "brumble_forest_brumble_vine",
      SkiesConfiguredFeatures.PEEKING_OCEAN_BRUMBLE_VINE,
      CountPlacement.of(UniformInt.of(8, 10)),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> SNOW_COVERED_PINES_STARLIT_TREE = register(
      "snow_covered_pines_starlit_tree",
      SkiesConfiguredFeatures.SNOW_COVERED_PINES_VEGETATION,
      VegetationPlacements.treePlacement(CountPlacement.of(3), SkiesBlocks.starlit_sapling)
   );
   public static final ResourceKey<PlacedFeature> SNOW_COVERED_PINES_CAVE_STARLIT_VINE = register(
      "snow_covered_pines_cave_starlit_vine", SkiesConfiguredFeatures.SNOW_COVERED_PINES_CAVE_STARLIT_VINE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SNOW_COVERED_PINES_WALL_MOONSTONE = register(
      "snow_covered_pines_wall_moonstone", SkiesConfiguredFeatures.SNOW_COVERED_PINES_WALL_MOONSTONE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SNOW_COVERED_PINES_CAVE_WALL = register(
      "snow_covered_pines_cave_wall", SkiesConfiguredFeatures.SNOW_COVERED_PINES_CAVE_WALL, countRange(59, 10)
   );
   public static final ResourceKey<PlacedFeature> BRISK_MEADOW_BLUEBRIGHT_TREE = register(
      "brisk_meadow_bluebright_tree",
      SkiesConfiguredFeatures.BLUEBRIGHT_TREE,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1F, 1), SkiesBlocks.bluebright_sapling)
   );
   public static final ResourceKey<PlacedFeature> BRISK_MEADOW_BUSH = register(
      "brisk_meadow_bush",
      SkiesConfiguredFeatures.BLUEBRIGHT_BUSH,
      VegetationPlacements.treePlacement(CountPlacement.of(UniformInt.of(1, 2)), SkiesBlocks.starlit_sapling)
   );
   public static final ResourceKey<PlacedFeature> BRISK_MEADOW_FLOWER = register(
      "brisk_meadow_flower", SkiesConfiguredFeatures.BRISK_MEADOW_FLOWER, DEFAULT_NOISE
   );
   public static final ResourceKey<PlacedFeature> BRISK_MEADOW_CAVE_WALL_STONE_RIMESTONE = register(
      "brisk_meadow_cave_wall_stone_rimestone", SkiesConfiguredFeatures.BRISK_MEADOW_CAVE_WALL_STONE_RIMESTONE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> BRISK_MEADOW_CAVE_WALL_RIMESTONE_STONE = register(
      "brisk_meadow_cave_wall_rimestone_stone", SkiesConfiguredFeatures.BRISK_MEADOW_CAVE_WALL_RIMESTONE_STONE, countRange(59, 10)
   );
   public static final ResourceKey<PlacedFeature> BRISK_MEADOW_CAVE_FROSE = register(
      "brisk_meadow_cave_frose", SkiesConfiguredFeatures.BRISK_MEADOW_CAVE_FROSE, countRange(59, 17)
   );
   public static final ResourceKey<PlacedFeature> POLAR_HIGHLAND_ROCK = register(
      "polar_highland_rock",
      SkiesConfiguredFeatures.TURQUOISE_COBBLESTONE_ROCK,
      CountPlacement.of(UniformInt.of(0, 2)),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> POLAR_HIGHLAND_VEGETATION = register(
      "polar_highland_vegetation",
      SkiesConfiguredFeatures.POLAR_HIGHLAND_VEGETATION,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(8, 0.1F, 1), SkiesBlocks.bluebright_sapling)
   );
   public static final ResourceKey<PlacedFeature> POLAR_HIGHLAND_CAVE_WALL_TARATITE = register(
      "polar_highland_cave_wall_taratite", SkiesConfiguredFeatures.POLAR_HIGHLAND_CAVE_WALL_TARATITE, countRange(59, 30)
   );
   public static final ResourceKey<PlacedFeature> POLAR_HIGHLAND_CAVE_GRASS = register(
      "polar_highland_cave_grass", SkiesConfiguredFeatures.POLAR_HIGHLAND_CAVE_GRASS, countRange(59, 25)
   );
   public static final ResourceKey<PlacedFeature> POLAR_HIGHLAND_CAVE_POSY = register(
      "polar_highland_cave_posy", SkiesConfiguredFeatures.POLAR_HIGHLAND_CAVE_POSY, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> UNORTHODOX_VALLEY_BUSH = register(
      "unorthodox_valley_bush",
      SkiesConfiguredFeatures.DUSK_BUSH,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.1F, 2), SkiesBlocks.dusk_sapling)
   );
   public static final ResourceKey<PlacedFeature> UNORTHODOX_VALLEY_DUSK_LUNAR_TREE = register(
      "unorthodox_valley_dusk_lunar_tree",
      SkiesConfiguredFeatures.UNORTHODOX_VALLEY_VEGETATION,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(2), SkiesBlocks.lunar_sapling)
   );
   public static final ResourceKey<PlacedFeature> UNORTHODOX_VALLEY_CRESCENT_FRUIT_TREE = register(
      "unorthodox_valley_crescent_fruit_tree",
      SkiesConfiguredFeatures.CRESCENT_FRUIT_TREE,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1F, 1), SkiesBlocks.crescent_fruit_sapling)
   );
   public static final ResourceKey<PlacedFeature> UNORTHODOX_VALLEY_CAVE_DRIPSTONE = register(
      "unorthodox_valley_cave_dripstone", SkiesConfiguredFeatures.CAVE_LUNAR_DRIPSTONE, countRange(69, 25)
   );
   public static final ResourceKey<PlacedFeature> SHADED_WOODLANDS_LARGE_DUSK_TREE = register(
      "shaded_woodlands_large_dusk_tree",
      SkiesConfiguredFeatures.VINED_LARGE_DUSK_TREE,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(30, 0.1F, 1), SkiesBlocks.dusk_sapling)
   );
   public static final ResourceKey<PlacedFeature> SHADED_WOODLANDS_FLOWER = register(
      "shaded_woodlands_flower", SkiesConfiguredFeatures.SHADED_WOODLANDS_FLOWER_PATCH, DEFAULT_NOISE
   );
   public static final ResourceKey<PlacedFeature> SHADED_WOODLANDS_CAVE_DUSK_VINE = register(
      "shaded_woodlands_cave_dusk_vine", SkiesConfiguredFeatures.SHADED_WOODLANDS_CAVE_DUSK_VINE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SHADED_WOODLANDS_CAVE_WALL = register(
      "shaded_woodlands_cave_wall", SkiesConfiguredFeatures.SHADED_WOODLANDS_CAVE_WALL, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_DUNES_MOONSTONE_SPIKE = register(
      "crystal_dunes_moonstone_spike",
      SkiesConfiguredFeatures.CRYSTAL_DUNES_MOONSTONE_SPIKE,
      CountPlacement.of(1),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_DUNES_LARGE_MOONSTONE_SPIKE = register(
      "crystal_dunes_large_moonstone_spike",
      SkiesConfiguredFeatures.CRYSTAL_DUNES_LARGE_MOONSTONE_SPIKE,
      CountPlacement.of(UniformInt.of(2, 3)),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_DUNES_CAVE_MOONSTONE_CRYSTAL = register(
      "crystal_dunes_cave_moonstone_crystal", SkiesConfiguredFeatures.CRYSTAL_DUNES_CAVE_MOONSTONE_CRYSTAL, countRange(59, 60)
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_DUNES_CAVE_GRASS = register(
      "crystal_dunes_cave_grass", SkiesConfiguredFeatures.CRYSTAL_DUNES_CAVE_GRASS, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_DUNES_CAVE_WALL_MOONSTONE = register(
      "crystal_dunes_cave_wall_moonstone", SkiesConfiguredFeatures.CRYSTAL_DUNES_CAVE_WALL_MOONSTONE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> RISING_CREEK_MOONLIT_WATER_LILY = register(
      "rising_creek_moonlit_water_lily",
      SkiesConfiguredFeatures.MOONLIT_WATER_LILY,
      CountPlacement.of(UniformInt.of(1, 2)),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> MOONLIT_RESERVOIR_DUSK_TREE = register(
      "moonlit_reservoir_dusk_tree",
      SkiesConfiguredFeatures.RESERVOIR_DUSK_TREE,
      VegetationPlacements.treePlacement(CountPlacement.of(1), SkiesBlocks.dusk_sapling)
   );
   public static final ResourceKey<PlacedFeature> MOONLIT_RESERVOIR_GLIMMER_REEDS = register(
      "moonlit_reservoir_glimmer_reeds",
      SkiesConfiguredFeatures.GLIMMER_REEDS,
      NoiseBasedCountPlacement.of(80, 80.0, 0.0),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_TOP_SOLID,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> MOONLIT_RESERVOIR_MOONLIT_WATER_LILY = register(
      "moonlit_reservoir_moonlit_water_lily",
      SkiesConfiguredFeatures.MOONLIT_WATER_LILY,
      CountPlacement.of(5),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> MOONLIT_RESERVOIR_CAVE_GRASS = register(
      "moonlit_reservoir_cave_grass", SkiesConfiguredFeatures.MOONLIT_RESERVOIR_CAVE_GRASS, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> MOONLIT_RESERVOIR_CAVE_WALL = register(
      "moonlit_reservoir_cave_wall", SkiesConfiguredFeatures.MOONLIT_RESERVOIR_CAVE_WALL, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_ROUGHS_LUNAR_TREE = register(
      "crystal_roughs_lunar_tree",
      SkiesConfiguredFeatures.LUNAR_TREE,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(2), SkiesBlocks.lunar_sapling)
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_ROUGHS_CRYSTALIZED_TREE = register(
      "crystal_roughs_crystalized_tree",
      SkiesConfiguredFeatures.CRYSTALIZED_TREE,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(2), SkiesBlocks.crystallized_grass)
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_ROUGHS_SHORT_MOONSTONE_SPIKE = register(
      "crystal_roughs_short_moonstone_spike",
      SkiesConfiguredFeatures.SHORT_MOONSTONE_SPIKE,
      VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(2))
   );
   public static final ResourceKey<PlacedFeature> CRYSTAL_ROUGHS_CAVE_GRASS = register(
      "crystal_roughs_cave_grass", SkiesConfiguredFeatures.CRYSTAL_ROUGHS_CAVE_GRASS, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SUNSET_MAPLE_FOREST_MAPLE_TREE = register(
      "sunset_maple_forest_maple_tree",
      SkiesConfiguredFeatures.MAPLE_TREE,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(12, 0.1F, 1), SkiesBlocks.maple_sapling)
   );
   public static final ResourceKey<PlacedFeature> SUNSET_MAPLE_FOREST_CAVE_WALL_SUNSTONE = register(
      "sunset_maple_forest_cave_wall_sunstone", SkiesConfiguredFeatures.SUNSET_MAPLE_FOREST_CAVE_WALL_SUNSTONE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SUNSET_MAPLE_FOREST_CAVE_MAGMA = register(
      "sunset_maple_forest_cave_magma", SkiesConfiguredFeatures.CAVE_MAGMA, countRange(9, 6)
   );
   public static final ResourceKey<PlacedFeature> SUNSET_MAPLE_FOREST_CAVE_MAPLE_VINE = register(
      "sunset_maple_forest_cave_maple_vine", SkiesConfiguredFeatures.SUNSET_MAPLE_FOREST_CAVE_MAPLE_VINE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SEARING_GRASSLAND_BUSH = register(
      "searing_grassland_bush", SkiesConfiguredFeatures.MAPLE_BUSH, VegetationPlacements.treePlacement(CountPlacement.of(1), SkiesBlocks.lunar_sapling)
   );
   public static final ResourceKey<PlacedFeature> SEARING_GRASSLAND_ROCK = register(
      "searing_grassland_rock",
      SkiesConfiguredFeatures.LUNAR_COBBLESTONE_ROCK,
      CountPlacement.of(UniformInt.of(0, 1)),
      InSquarePlacement.spread(),
      PlacementUtils.HEIGHTMAP,
      BiomeFilter.biome()
   );
   public static final ResourceKey<PlacedFeature> SEARING_GRASSLAND_CAVE_WALL_SUNSTONE = register(
      "searing_grassland_cave_wall_sunstone", SkiesConfiguredFeatures.SEARING_GRASSLAND_CAVE_WALL_SUNSTONE, countRange(59, 20)
   );
   public static final ResourceKey<PlacedFeature> SEARING_GRASSLAND_CAVE_MAGMA = register(
      "searing_grassland_cave_magma", SkiesConfiguredFeatures.CAVE_MAGMA, countRange(9, 6)
   );
   public static final ResourceKey<PlacedFeature> SEARING_GRASSLAND_CAVE_WALL = register(
      "searing_grassland_cave_wall", SkiesConfiguredFeatures.SEARING_GRASSLAND_CAVE_WALL, countRange(29, 7)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CRESCENT_FRUIT_TREE = register(
      "crescent_orchard_crescent_fruit_tree",
      SkiesConfiguredFeatures.CRESCENT_FRUIT_TREE,
      VegetationPlacements.treePlacement(PlacementUtils.countExtra(7, 0.1F, 1), SkiesBlocks.crescent_fruit_sapling)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CAVE_WALL_UMBER = register(
      "crescent_orchard_cave_wall_umber", SkiesConfiguredFeatures.CRESCENT_ORCHARD_CAVE_WALL_UMBER, countRange(59, 35)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CAVE_GRASS = register(
      "crescent_orchard_cave_grass", SkiesConfiguredFeatures.CRESCENT_ORCHARD_CAVE_GRASS, countRange(59, 15)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CAVE_DIRT = register(
      "crescent_orchard_cave_dirt", SkiesConfiguredFeatures.CRESCENT_ORCHARD_CAVE_DIRT, countRange(59, 10)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CAVE_BLOOM = register(
      "crescent_orchard_cave_bloom", SkiesConfiguredFeatures.CRESCENT_ORCHARD_CAVE_BLOOM, countRange(59, 24)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CAVE_NIGHTCRESS = register(
      "crescent_orchard_cave_nightcress", SkiesConfiguredFeatures.CRESCENT_ORCHARD_CAVE_NIGHTCRESS, countRange(59, 22)
   );
   public static final ResourceKey<PlacedFeature> CRESCENT_ORCHARD_CAVE_LUCENTROOT = register(
      "crescent_orchard_cave_lucentroot", SkiesConfiguredFeatures.CRESCENT_ORCHARD_CAVE_LUCENTROOT, countRange(59, 18)
   );

   private static ResourceKey<PlacedFeature> register(String name, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey, PlacementModifier... mods) {
      return register(name, configuredFeatureKey, List.of(mods));
   }

   private static ResourceKey<PlacedFeature> register(String name, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey, List<PlacementModifier> modifiers) {
      ResourceKey<PlacedFeature> key = PlacementUtils.createKey(BlueSkies.find(name));
      if (DatagenModLoader.isRunningDataGen()) {
         PLACED_VALUES.put(BlueSkies.locate(name), new SkiesFeaturePlacements.PlacedValues(key, configuredFeatureKey, modifiers));
      }

      return key;
   }

   public static void bootstrap(BootstapContext<PlacedFeature> bootstrap) {
      PLACED_VALUES.forEach((key, value) -> {
         Reference<ConfiguredFeature<?, ?>> configured = bootstrap.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(value.configuredFeatureKey());
         PlacementUtils.register(bootstrap, value.key(), configured, value.modifiers());
      });
   }

   public static void addGeneralBrightFeatures(Builder builder, boolean flowers, boolean mountainous) {
      builder.addFeature(Decoration.TOP_LAYER_MODIFICATION, ICE_AND_SNOW_NO_PATH_CONFIGURED);
      builder.addFeature(Decoration.VEGETAL_DECORATION, NATURE_DUNGEON_COMET_TREE);
      builder.addFeature(Decoration.VEGETAL_DECORATION, NATURE_DUNGEON_STARLIT_BUSH);
      if (flowers) {
         addBrightFlowers(builder);
      }

      addDefaultBrightOres(builder, !mountainous);
      addSprings(builder, false);
      builder.addFeature(Decoration.VEGETAL_DECORATION, CAVE_TURQUOISE_DRIPSTONE);
      builder.addFeature(Decoration.UNDERGROUND_STRUCTURES, EVERBRIGHT_CAVE_SPAWNER_CONFIGURED);
      SkiesConfiguredCarvers.addBrightCarvers(builder);
   }

   public static void addGeneralDawnFeatures(Builder builder, boolean flowers, boolean superHot) {
      builder.addFeature(Decoration.VEGETAL_DECORATION, CAVE_LUNAR_DRIPSTONE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, CAVE_LUNAR_LAVA_SPIRE);
      if (flowers) {
         addDawnFlowers(builder);
      }

      addDefaultDawnOres(builder, superHot);
      addSprings(builder, true);
      builder.addFeature(Decoration.UNDERGROUND_STRUCTURES, EVERDAWN_CAVE_SPAWNER_CONFIGURED);
      SkiesConfiguredCarvers.addDawnCarvers(builder);
   }

   public static void addSprings(Builder builder, boolean allowLava) {
      builder.addFeature(Decoration.VEGETAL_DECORATION, SPRING_WATER);
      if (allowLava) {
         builder.addFeature(Decoration.VEGETAL_DECORATION, SPRING_LAVA);
      }
   }

   public static void addDefaultBrightOres(Builder builder, boolean nonMountainous) {
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_MOONSTONE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_PYROPE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_AQUITE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_DIOPSIDE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_CHAROITE);
      if (nonMountainous) {
         builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_EVERBRIGHT_FALSITE);
         builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_EVERBRIGHT_VENTIUM);
      }

      builder.addFeature(Decoration.UNDERGROUND_ORES, COARSE_TURQUOISE_DIRT_BLOB);
      builder.addFeature(Decoration.UNDERGROUND_ORES, RIMESTONE_BLOB);
      builder.addFeature(Decoration.UNDERGROUND_ORES, TARATITE_BLOB);
   }

   public static void addDefaultDawnOres(Builder builder, boolean superHot) {
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_MOONSTONE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_PYROPE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_AQUITE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_DIOPSIDE);
      builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_CHAROITE);
      if (superHot) {
         builder.addFeature(Decoration.UNDERGROUND_ORES, ORE_EVERDAWN_HORIZONITE);
      }

      builder.addFeature(Decoration.UNDERGROUND_ORES, COARSE_LUNAR_DIRT_BLOB);
      builder.addFeature(Decoration.UNDERGROUND_ORES, UMBER_BLOB);
      builder.addFeature(Decoration.UNDERGROUND_ORES, CINDERSTONE_BLOB);
   }

   public static void addEmeraldOre(Builder builder, boolean everbright) {
      builder.addFeature(Decoration.UNDERGROUND_ORES, everbright ? ORE_EVERBRIGHT_EMERALD : ORE_EVERDAWN_EMERALD);
   }

   public static void addBrightLakes(Builder builder) {
      builder.addFeature(Decoration.LOCAL_MODIFICATIONS, EVERBRIGHT_LAKE_CONFIGURED);
   }

   public static void addDawnLakes(Builder builder) {
      builder.addFeature(Decoration.LOCAL_MODIFICATIONS, EVERDAWN_LAKE_CONFIGURED);
   }

   public static void addBrightFlowers(Builder builder) {
      builder.addFeature(Decoration.VEGETAL_DECORATION, CAMELLIA_4);
      builder.addFeature(Decoration.VEGETAL_DECORATION, BREWBERRY_2);
   }

   public static void addDawnFlowers(Builder builder) {
      builder.addFeature(Decoration.VEGETAL_DECORATION, MOONLIT_BLOOM_2);
   }

   public static void addBrightDoubleGrass(Builder builder) {
      builder.addFeature(Decoration.VEGETAL_DECORATION, TALL_TURQUOISE_GRASS_NOISE);
   }

   public static void addDawnDoubleGrass(Builder builder) {
      builder.addFeature(Decoration.VEGETAL_DECORATION, TALL_LUNAR_GRASS_NOISE);
   }

   private static List<PlacementModifier> countRangeRare(int height, int count, PlacementModifier extraModifier) {
      return List.of(
         CountPlacement.of(count),
         extraModifier,
         InSquarePlacement.spread(),
         HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(height)),
         BiomeFilter.biome()
      );
   }

   private static List<PlacementModifier> countRange(int height, int count) {
      return List.of(
         CountPlacement.of(count),
         InSquarePlacement.spread(),
         HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(height)),
         BiomeFilter.biome()
      );
   }

   private static PlacementModifier range(int height) {
      return HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(height));
   }

   private static List<PlacementModifier> flower(int count) {
      return flower(count, true);
   }

   private static List<PlacementModifier> flower(int count, boolean legacyFlower) {
      PlacementModifier modifier = (PlacementModifier)(legacyFlower ? RarityFilter.onAverageOnceEvery(Math.max(2, 32 / count)) : CountPlacement.of(count));
      return List.of(modifier, InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
   }

   private static List<PlacementModifier> countNoise(double noise, int x, int y) {
      return countNoise(noise, x, y, PlacementUtils.HEIGHTMAP);
   }

   private static List<PlacementModifier> countNoise(double noise, int x, int y, PlacementModifier heightmap) {
      return List.of(NoiseThresholdCountPlacement.of(noise, x, y), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
   }

   public static void init() {
   }

   private record PlacedValues(ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey, List<PlacementModifier> modifiers) {
   }
}
