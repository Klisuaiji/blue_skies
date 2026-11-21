package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.world.everbright.gen.features.FallenLogFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.MoonstoneSpikeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.cave.CaveWallCrystalFeature;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.legacy.blue_skies.world.general_features.BushFeature;
import com.legacy.blue_skies.world.general_features.cave.CaveGrassFeature;
import com.legacy.blue_skies.world.general_features.cave.CavePlantFeature;
import com.legacy.blue_skies.world.general_features.cave.CaveVineFeature;
import com.legacy.blue_skies.world.general_features.cave.CaveWallFeature;
import com.legacy.blue_skies.world.general_features.cave.SkyDripstoneFeature;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

public class SkiesConfiguredFeatures {
   public static final RegistrarHandler<ConfiguredFeature<?, ?>> HANDLER = RegistrarHandler.getOrCreate(Registries.CONFIGURED_FEATURE, "blue_skies")
      .bootstrap(c -> bootstrap(c));
   private static final Map<ResourceLocation, SkiesConfiguredFeatures.ConfiguredValues<? extends FeatureConfiguration>> CONFIGURED_VALUES = new HashMap<>();
   public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_BLUEBRIGHT_TREE = register(
      "small_bluebright_tree", SkiesFeatures.SMALL_BLUEBRIGHT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BLUEBRIGHT_TREE = register(
      SkiesRefs.BLUEBRIGHT_TREE, SkiesFeatures.BLUEBRIGHT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_STARLIT_TREE = register(
      "small_starlit_tree", SkiesFeatures.SMALL_STARLIT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> STARLIT_TREE = register(
      SkiesRefs.STARLIT_TREE, SkiesFeatures.STARLIT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_FROSTBRIGHT_TREE = register(
      SkiesRefs.SMALL_FROSTBRIGHT_TREE, SkiesFeatures.SMALL_FROSTBRIGHT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWY_FROSTBRIGHT_TREE = register(
      "snowy_frostbright_tree", SkiesFeatures.SNOWY_FROSTBRIGHT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FROSTBRIGHT_TREE = register(
      SkiesRefs.FROSTBRIGHT_TREE, SkiesFeatures.FROSTBRIGHT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_SNOWCAP_MUSHROOM = register(
      "giant_snowcap_mushroom", SkiesFeatures.SNOWCAP_MUSHROOM, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> LUNAR_TREE = register(
      SkiesRefs.LUNAR_TREE, SkiesFeatures.LUNAR_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> DUSK_TREE = register(
      SkiesRefs.DUSK_TREE, SkiesFeatures.DUSK_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_DUSK_TREE = register(
      SkiesRefs.LARGE_DUSK_TREE, SkiesFeatures.LARGE_DUSK_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> VINED_LARGE_DUSK_TREE = register(
      "vined_large_dusk_tree", SkiesFeatures.VINED_LARGE_DUSK_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_TREE = register(
      SkiesRefs.MAPLE_TREE, SkiesFeatures.MAPLE_TREE_UNNATURAL, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_FRUIT_TREE = register(
      SkiesRefs.CRESCENT_FRUIT_TREE, SkiesFeatures.CRESCENT_FRUIT_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTALIZED_TREE = register(
      "crystalized_tree", SkiesFeatures.CRYSTALIZED_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> RESERVOIR_DUSK_TREE = register(
      "reservoir_dusk_tree", SkiesFeatures.RESERVOIR_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> GROWN_CRYSTALIZED_TREE = register(
      "grown_crystalized_tree", SkiesFeatures.CRYSTALIZED_TREE_UNNATURAL, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> COMET_TREE = register("comet_tree", SkiesFeatures.COMET_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG);
   public static final ResourceKey<ConfiguredFeature<?, ?>> BLUEBRIGHT_BUSH = register(
      "bluebright_bush",
      SkiesFeatures.BUSH,
      new BushFeature.Config(() -> SkiesBlocks.bluebright_log.defaultBlockState(), () -> SkiesBlocks.bluebright_leaves.defaultBlockState(), false)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FROSTBRIGHT_BUSH = register(
      "frostbright_bush",
      SkiesFeatures.BUSH,
      new BushFeature.Config(() -> SkiesBlocks.frostbright_log.defaultBlockState(), () -> SkiesBlocks.frostbright_leaves.defaultBlockState(), false)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> DUSK_BUSH = register(
      "dusk_bush",
      SkiesFeatures.BUSH,
      new BushFeature.Config(() -> SkiesBlocks.dusk_log.defaultBlockState(), () -> SkiesBlocks.dusk_leaves.defaultBlockState(), false)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MAPLE_BUSH = register(
      "maple_bush",
      SkiesFeatures.BUSH,
      new BushFeature.Config(() -> SkiesBlocks.maple_log.defaultBlockState(), () -> SkiesBlocks.maple_leaves.defaultBlockState(), false)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> NATURE_DUNGEON_COMET_TREE = register(
      "nature_dungeon_comet_tree", SkiesFeatures.NATURE_COMET_TREE, AbstractSkyTreeFeature.DUMMY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> NATURE_DUNGEON_STARLIT_BUSH = register(
      "nature_dungeon_starlit_bush",
      SkiesFeatures.BUSH,
      new BushFeature.Config(() -> SkiesBlocks.starlit_log.defaultBlockState(), () -> SkiesBlocks.starlit_leaves.defaultBlockState(), true)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_TURQUOISE_DRIPSTONE = register(
      "cave_turquoise_dripstone", SkiesFeatures.DRIPSTONE, new SkyDripstoneFeature.Config(SkiesBlocks.turquoise_dripstone.defaultBlockState(), 5)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> EVERBRIGHT_CAVE_SPAWNER_CONFIGURED = register(
      "everbright_cave_spawner", SkiesFeatures.EVERBRIGHT_CAVE_SPAWNER, NoneFeatureConfiguration.INSTANCE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_STARLIT_LOG = register(
      "fallen_starlit_log", SkiesFeatures.FALLEN_LOG, new FallenLogFeature.Config(SkiesBlocks.starlit_log.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CAVE_ICICLE = register(
      "giant_cave_icicle", SkiesFeatures.GIANT_CAVE_ICICLE, NoneFeatureConfiguration.INSTANCE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> TURQUOISE_COBBLESTONE_ROCK = register(
      "turquoise_cobblestone_rock", SkiesFeatures.LARGE_ROCK, new BlockStateConfiguration(SkiesBlocks.turquoise_cobblestone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_LUNAR_LAVA_SPIRE = register(
      "cave_lunar_lava_spire", SkiesFeatures.LUNAR_LAVA_SPIRE, NoneFeatureConfiguration.INSTANCE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_LUNAR_DRIPSTONE = register(
      "cave_lunar_dripstone", SkiesFeatures.DRIPSTONE, new SkyDripstoneFeature.Config(SkiesBlocks.lunar_dripstone.defaultBlockState(), 5)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> EVERDAWN_CAVE_SPAWNER_CONFIGURED = register(
      "everdawn_cave_spawner", SkiesFeatures.EVERDAWN_CAVE_SPAWNER, NoneFeatureConfiguration.INSTANCE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_MAGMA = register("cave_magma", SkiesFeatures.CAVE_MAGMA, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> SHORT_MOONSTONE_SPIKE = register(
      "short_moonstone_spike", SkiesFeatures.SHORT_MOONSTONE_SPIKE, FeatureConfiguration.NONE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> LUNAR_COBBLESTONE_ROCK = register(
      "lunar_cobblestone_rock", SkiesFeatures.LARGE_ROCK, new BlockStateConfiguration(SkiesBlocks.lunar_cobblestone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOONSTONE = register(
      "ore_everbright_moonstone",
      SkiesFeatures.ORE,
      new OreConfiguration(
         SkiesFeatures.Configs.oreConfig(SkiesBlocks.everbright_moonstone_ore.defaultBlockState(), SkiesBlocks.everdawn_moonstone_ore.defaultBlockState()), 14
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_PYROPE = register(
      "ore_everbright_pyrope",
      SkiesFeatures.ORE,
      new OreConfiguration(
         SkiesFeatures.Configs.oreConfig(SkiesBlocks.everbright_pyrope_ore.defaultBlockState(), SkiesBlocks.everdawn_pyrope_ore.defaultBlockState()), 7
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_AQUITE = register(
      "ore_aquite",
      SkiesFeatures.ORE,
      new OreConfiguration(
         SkiesFeatures.Configs.oreConfig(SkiesBlocks.everbright_aquite_ore.defaultBlockState(), SkiesBlocks.everdawn_aquite_ore.defaultBlockState()), 5
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIOPSIDE = register(
      "ore_diopside",
      SkiesFeatures.ORE,
      new OreConfiguration(
         SkiesFeatures.Configs.oreConfig(SkiesBlocks.everbright_diopside_ore.defaultBlockState(), SkiesBlocks.everdawn_diopside_ore.defaultBlockState()), 6
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CHAROITE = register(
      "ore_charoite",
      SkiesFeatures.ORE,
      new OreConfiguration(
         SkiesFeatures.Configs.oreConfig(SkiesBlocks.everbright_charoite_ore.defaultBlockState(), SkiesBlocks.everdawn_charoite_ore.defaultBlockState()), 6
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EVERBRIGHT_FALSITE = register(
      "ore_everbright_falsite",
      SkiesFeatures.ORE,
      new OreConfiguration(SkiesFeatures.Configs.TURQUOISE_RULE_TEST, SkiesBlocks.falsite_ore.defaultBlockState(), 6)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EVERBRIGHT_VENTIUM = register(
      "ore_everbright_ventium",
      SkiesFeatures.ORE,
      new OreConfiguration(SkiesFeatures.Configs.TURQUOISE_RULE_TEST, SkiesBlocks.ventium_ore.defaultBlockState(), 6)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EVERBRIGHT_EMERALD = register(
      "ore_everbright_emerald",
      Feature.REPLACE_SINGLE_BLOCK,
      new ReplaceBlockConfiguration(SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.everbright_emerald_ore.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EVERDAWN_HORIZONITE = register(
      "ore_everdawn_horizonite",
      SkiesFeatures.ORE,
      new OreConfiguration(SkiesFeatures.Configs.LUNAR_RULE_TEST, SkiesBlocks.horizonite_ore.defaultBlockState(), 4)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EVERDAWN_EMERALD = register(
      "ore_everdawn_emerald",
      Feature.REPLACE_SINGLE_BLOCK,
      new ReplaceBlockConfiguration(SkiesBlocks.lunar_stone.defaultBlockState(), SkiesBlocks.everdawn_emerald_ore.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> COARSE_TURQUOISE_DIRT_BLOB = register(
      "coarse_turquoise_dirt_blob",
      SkiesFeatures.ORE,
      new OreConfiguration(SkiesFeatures.Configs.TURQUOISE_RULE_TEST, SkiesBlocks.coarse_turquoise_dirt.defaultBlockState(), 33)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> RIMESTONE_BLOB = register(
      "rimestone_blob", SkiesFeatures.ORE, new OreConfiguration(SkiesFeatures.Configs.TURQUOISE_RULE_TEST, SkiesBlocks.rimestone.defaultBlockState(), 64)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> TARATITE_BLOB = register(
      "taratite_blob", SkiesFeatures.ORE, new OreConfiguration(SkiesFeatures.Configs.TURQUOISE_RULE_TEST, SkiesBlocks.taratite.defaultBlockState(), 64)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> COARSE_LUNAR_DIRT_BLOB = register(
      "coarse_lunar_dirt_blob",
      SkiesFeatures.ORE,
      new OreConfiguration(SkiesFeatures.Configs.LUNAR_RULE_TEST, SkiesBlocks.coarse_lunar_dirt.defaultBlockState(), 33)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> UMBER_BLOB = register(
      "umber_blob", SkiesFeatures.ORE, new OreConfiguration(SkiesFeatures.Configs.LUNAR_RULE_TEST, SkiesBlocks.umber.defaultBlockState(), 64)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CINDERSTONE_BLOB = register(
      "cinderstone_blob", SkiesFeatures.ORE, new OreConfiguration(SkiesFeatures.Configs.LUNAR_RULE_TEST, SkiesBlocks.cinderstone.defaultBlockState(), 64)
   );
   private static final HolderSet<Block> SPRING_REPLACEABLES = HolderSet.direct(
      Block::builtInRegistryHolder,
      new Block[]{
         SkiesBlocks.turquoise_stone, SkiesBlocks.lunar_stone, SkiesBlocks.cinderstone, SkiesBlocks.taratite, SkiesBlocks.rimestone, SkiesBlocks.umber
      }
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> EVERBRIGHT_LAKE_CONFIGURED = register(
      "everbright_lake", SkiesFeatures.EVERBRIGHT_LAKE, new BlockStateConfiguration(Blocks.WATER.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> EVERDAWN_LAKE_CONFIGURED = register(
      "everdawn_lake", SkiesFeatures.EVERDAWN_LAKE, new BlockStateConfiguration(Blocks.WATER.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_WATER = register(
      "spring_water", Feature.SPRING, new SpringConfiguration(Fluids.WATER.defaultFluidState(), true, 4, 1, SPRING_REPLACEABLES)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SPRING_LAVA = register(
      "spring_lava", Feature.SPRING, new SpringConfiguration(Fluids.LAVA.defaultFluidState(), true, 4, 1, SPRING_REPLACEABLES)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> PATCHY_SNOW = register("patchy_snow", SkiesFeatures.PATCHY_SNOW, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_AND_SNOW_NO_PATH_CONFIGURED = register(
      "ice_and_snow_no_path", SkiesFeatures.ICE_AND_SNOW_NO_PATH, FeatureConfiguration.NONE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CAMELLIA_PATCH = register("camellia_patch", Feature.FLOWER, SkiesFeatures.Configs.CAMELLIA_CONFIG);
   public static final ResourceKey<ConfiguredFeature<?, ?>> POLAR_POSY_PATCH = register(
      "polar_posy_patch", Feature.FLOWER, SkiesFeatures.Configs.POLAR_POSY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWBLOOM_PATCH = register(
      "snowbloom_patch", Feature.FLOWER, SkiesFeatures.Configs.SNOWBLOOM_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MOONLIT_BLOOM_PATCH = register(
      "moonlit_bloom_patch", Feature.FLOWER, SkiesFeatures.Configs.MOONLIT_BLOOM_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> LUCENTROOT_PATCH = register(
      "lucentroot_patch", Feature.FLOWER, SkiesFeatures.Configs.LUCENTROOT_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> NIGHTCRESS_PATCH = register(
      "nightcress_patch", Feature.FLOWER, SkiesFeatures.Configs.NIGHTCRESS_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_FLOWER_PATCH = register(
      "crystal_flower_patch", Feature.FLOWER, SkiesFeatures.Configs.CRYSTAL_FLOWER_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MIDDAY_BAYHOP_PATCH = register(
      "midday_bayhop_patch", Feature.FLOWER, SkiesFeatures.Configs.MIDDAY_BAYHOP_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FLARE_FLORET_BLAZE_BUD_LUCENTROOT_PATCH = register(
      "flare_floret_blaze_bud_lucentroot_patch", Feature.FLOWER, SkiesFeatures.Configs.FlARE_BLAZE_LUCENTROOT_MIX_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWCAP_PINHEAD_PATCH = register(
      "snowcap_pinhead_patch", Feature.FLOWER, SkiesFeatures.Configs.SNOWCAP_PINHEAD_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOWCAP_MUSHROOM_PATCH = register(
      "snowcap_mushroom_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.SNOWCAP_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BREWBERRY_PATCH = register(
      "brewberry_patch", Feature.FLOWER, SkiesFeatures.Configs.BREWBERRY_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CHILLED_LILY = register("chilled_lily", SkiesFeatures.CHILLED_LILY, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> BANEFUL_MUSHROOM_PATCH = register(
      "baneful_mushroom_patch", Feature.FLOWER, SkiesFeatures.Configs.BANEFUL_MUSHROOM_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> GLIMMER_REEDS = register("glimmer_reeds", SkiesFeatures.GLIMMER_REEDS, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> MOONLIT_WATER_LILY = register(
      "moonlit_water_lily", SkiesFeatures.MOONLIT_WATER_LILIES, FeatureConfiguration.NONE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> TURQUOISE_GRASS_PATCH = register(
      "turquoise_grass_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.TURQUOISE_GRASS
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_TURQUOISE_GRASS_PATCH = register(
      "tall_turquoise_grass_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.TALL_TURQUOISE_GRASS_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRITTLEBUSH_PATCH = register(
      "brittlebush_patch", Feature.FLOWER, SkiesFeatures.Configs.BRITTLEBUSH_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CHILLWEED_PATCH = register(
      "chillweed_patch", Feature.FLOWER, SkiesFeatures.Configs.CHILLWEED_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> LUNAR_GRASS_PATCH = register(
      "lunar_grass_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.LUNAR_GRASS
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_LUNAR_GRASS_PATCH = register(
      "tall_lunar_grass_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.TALL_LUNAR_GRASS_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MUCKWEED_PATCH = register("muckweed_patch", Feature.FLOWER, SkiesFeatures.Configs.MUCKWEED_CONFIG);
   public static final ResourceKey<ConfiguredFeature<?, ?>> LUNAR_GRASS_MUCKWEED_PATCH = register(
      "lunar_grass_muckweed_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.LUNAR_GRASS_MUCKWEED_MIX_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTALLIZED_GRASS_PATCH = register(
      "crystallized_grass_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.CRYSTALLIZED_GRASS
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> LIGHT_CRYSTALLIZED_GRASS_PATCH = register(
      "light_crystallized_grass_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.LIGHT_CRYSTALLIZED_GRASS
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_DENSE_TURQUOISE_DRIPSTONE = register(
      "cave_dense_turquoise_dripstone", SkiesFeatures.DRIPSTONE, new SkyDripstoneFeature.Config(SkiesBlocks.turquoise_dripstone.defaultBlockState(), 10)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRIGHTLANDS_CAVE_GRASS = register(
      "brightlands_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(Blocks.PACKED_ICE.defaultBlockState(), SkiesBlocks.turquoise_stone.defaultBlockState(), Blocks.AIR.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRIGHTLANDS_CAVE_WALL = register(
      "brightlands_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.rimestone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SLUSHLANDS_CAVE_WALL = register(
      "slushlands_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.turquoise_cobblestone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SLUSHLANDS_CAVE_BLUEBRIGHT_VINE = register(
      "bluebright_cave_vine", SkiesFeatures.CAVE_VINE, new CaveVineFeature.Config(SkiesBlocks.bluebright_vine)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SLUSHLANDS_CAVE_PINHEAD = register(
      "slushlands_cave_pinhead_patch", SkiesFeatures.CAVE_PLANT, new CavePlantFeature.Config(SkiesBlocks.snowcap_pinhead.defaultBlockState(), 30, 10)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FROSTBITTEN_FOREST_CAVE_FROSTBRIGHT_VINE = register(
      "frostbright_cave_vine", SkiesFeatures.CAVE_VINE, new CaveVineFeature.Config(SkiesBlocks.frostbright_vine)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FROSTBITTEN_FOREST_CAVE_GRASS = register(
      "frostbitten_forest_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.coarse_turquoise_dirt.defaultBlockState(),
         SkiesBlocks.turquoise_dirt.defaultBlockState(),
         SkiesBlocks.short_turquoise_grass.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> FROSTBITTEN_FOREST_CAVE_WALL = register(
      "frostbitten_forest_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.turquoise_cobblestone.defaultBlockState(), SkiesBlocks.turquoise_stone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_SEA_MOSS = register("sea_moss", SkiesFeatures.SEA_MOSS, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_BRUMBLE = register("brumble_patch", SkiesFeatures.BRUMBLE, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_BRUMBLE_VINE = register(
      "brumble_vine_patch", SkiesFeatures.BRUMBLE_VINE, FeatureConfiguration.NONE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_SPIKE = register("ocean_spike", SkiesFeatures.OCEAN_SPIKE, FeatureConfiguration.NONE);
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_CAVE_MOSS_CARPET = register(
      "peeking_ocean_cave_moss_carpet_patch",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.sea_moss_carpet.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_CAVE_DENSE_MOSS_WALL = register(
      "turquoise_stone_sea_moss_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.sea_moss_block.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> PEEKING_OCEAN_CAVE_MOSS_WALL = register(
      "peeking_ocean_cave_moss_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.sea_moss_block.defaultBlockState(), SkiesBlocks.turquoise_stone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_COVERED_PINES_VEGETATION = register("snow_covered_pines_vegetation");
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_COVERED_PINES_CAVE_STARLIT_VINE = register(
      "starlit_cave_vine", SkiesFeatures.CAVE_VINE, new CaveVineFeature.Config(SkiesBlocks.starlit_vine)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_COVERED_PINES_WALL_MOONSTONE = register(
      "snow_covered_pines_cave_wall_moonstone",
      SkiesFeatures.CAVE_WALL_CRYSTAL,
      new CaveWallCrystalFeature.Config(SkiesBlocks.moonstone_crystal.defaultBlockState(), 60, 4)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_COVERED_PINES_CAVE_WALL = register(
      "snow_covered_pines_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.turquoise_stone.defaultBlockState(), Blocks.PACKED_ICE.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRISK_MEADOW_FLOWER = register(
      "briskbloom_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.BRISK_MEADOW_FLOWER_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRISK_MEADOW_CAVE_WALL_STONE_RIMESTONE = register(
      "brisk_meadow_cave_wall_stone_rimestone",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.turquoise_stone.defaultBlockState(), SkiesBlocks.rimestone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRISK_MEADOW_CAVE_WALL_RIMESTONE_STONE = register(
      "brisk_meadow_cave_wall_rimestone_stone",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.rimestone.defaultBlockState(), SkiesBlocks.turquoise_stone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> BRISK_MEADOW_CAVE_FROSE = register(
      "brisk_meadow_cave_frose_patch", SkiesFeatures.CAVE_PLANT, new CavePlantFeature.Config(SkiesBlocks.frose.defaultBlockState(), 15, 6)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> POLAR_HIGHLAND_VEGETATION = register("polar_highland_vegetation");
   public static final ResourceKey<ConfiguredFeature<?, ?>> POLAR_HIGHLAND_CAVE_WALL_TARATITE = register(
      "taratite_cave_wall", SkiesFeatures.CAVE_WALL, new CaveWallFeature.Config(SkiesBlocks.taratite.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> POLAR_HIGHLAND_CAVE_GRASS = register(
      "turquoise_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.turquoise_grass_block.defaultBlockState(),
         SkiesBlocks.turquoise_dirt.defaultBlockState(),
         SkiesBlocks.short_turquoise_grass.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> POLAR_HIGHLAND_CAVE_POSY = register(
      "polar_highland_cave_posy_patch", SkiesFeatures.CAVE_PLANT, new CavePlantFeature.Config(SkiesBlocks.polar_posy.defaultBlockState(), 25, 10)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> UNORTHODOX_VALLEY_VEGETATION = register("unorthodox_valley_vegetation");
   public static final ResourceKey<ConfiguredFeature<?, ?>> UNORTHODOX_VALLEY_CAVE_DRIPSTONE = register(
      "cave_dense_lunar_dripstone", SkiesFeatures.DRIPSTONE, new SkyDripstoneFeature.Config(SkiesBlocks.lunar_dripstone.defaultBlockState(), 10)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SHADED_WOODLANDS_FLOWER_PATCH = register(
      "shaded_woodlands_flower_patch", Feature.RANDOM_PATCH, SkiesFeatures.Configs.SHADED_WOODLANDS_FLOWER_CONFIG
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SHADED_WOODLANDS_CAVE_DUSK_VINE = register(
      "dusk_cave_vine", SkiesFeatures.CAVE_VINE, new CaveVineFeature.Config(SkiesBlocks.dusk_vine)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SHADED_WOODLANDS_CAVE_WALL = register(
      "lunar_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.lunar_grass_block.defaultBlockState(), SkiesBlocks.lunar_dirt.defaultBlockState(), SkiesBlocks.short_lunar_grass.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_DUNES_MOONSTONE_SPIKE = register(
      "crystal_dunes_moonstone_spike", SkiesFeatures.MOONSTONE_SPIKE, new MoonstoneSpikeFeature.Config(7, 10, 1, 1)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_DUNES_LARGE_MOONSTONE_SPIKE = register(
      "crystal_dunes_large_moonstone_spike", SkiesFeatures.MOONSTONE_SPIKE, new MoonstoneSpikeFeature.Config(11, 20, 2, 3)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_DUNES_CAVE_MOONSTONE_CRYSTAL = register(
      "crystal_dunes_cave_moonstone_crystal", SkiesFeatures.MOONSTONE_CAVE_CRYSTAL, FeatureConfiguration.NONE
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_DUNES_CAVE_GRASS = register(
      "crystal_sand_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.crystal_sand.defaultBlockState(), SkiesBlocks.crystal_sandstone.defaultBlockState(), Blocks.AIR.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_DUNES_CAVE_WALL_MOONSTONE = register(
      "crystal_dunes_cave_wall_moonstone",
      SkiesFeatures.CAVE_WALL_CRYSTAL,
      new CaveWallCrystalFeature.Config(SkiesBlocks.moonstone_crystal.defaultBlockState(), 60, 4)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MOONLIT_RESERVOIR_CAVE_GRASS = register(
      "moonlit_reservoir_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.coarse_lunar_dirt.defaultBlockState(), SkiesBlocks.lunar_dirt.defaultBlockState(), SkiesBlocks.muckweed.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> MOONLIT_RESERVOIR_CAVE_WALL = register(
      "mossy_lunar_cobblestone_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.lunar_cobblestone.defaultBlockState(), SkiesBlocks.mossy_lunar_cobblestone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRYSTAL_ROUGHS_CAVE_GRASS = register(
      "crystal_roughs_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.crystal_sand.defaultBlockState(), SkiesBlocks.crystal_sandstone.defaultBlockState(), SkiesBlocks.crystallized_grass.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SUNSET_MAPLE_FOREST_CAVE_WALL_SUNSTONE = register(
      "sunset_maple_forest_cave_wall_sunstone",
      SkiesFeatures.CAVE_WALL_CRYSTAL,
      new CaveWallCrystalFeature.Config(SkiesBlocks.sunstone_crystal.defaultBlockState(), 60, 4)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SUNSET_MAPLE_FOREST_CAVE_MAPLE_VINE = register(
      "sunset_maple_forest_cave_maple_vine", SkiesFeatures.CAVE_VINE, new CaveVineFeature.Config(SkiesBlocks.maple_vine)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SEARING_GRASSLAND_CAVE_WALL_SUNSTONE = register(
      "searing_grassland_cave_wall_sunstone",
      SkiesFeatures.CAVE_WALL_CRYSTAL,
      new CaveWallCrystalFeature.Config(SkiesBlocks.sunstone_crystal.defaultBlockState(), 60, 4)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> SEARING_GRASSLAND_CAVE_WALL = register(
      "searing_grassland_cave_wall",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.lunar_stone.defaultBlockState(), SkiesBlocks.cinderstone.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_ORCHARD_CAVE_WALL_UMBER = register(
      "crescent_orchard_cave_wall_umber",
      SkiesFeatures.CAVE_WALL,
      new CaveWallFeature.Config(SkiesBlocks.lunar_stone.defaultBlockState(), SkiesBlocks.umber.defaultBlockState())
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_ORCHARD_CAVE_GRASS = register(
      "crescent_orchard_cave_grass",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.lunar_grass_block.defaultBlockState(), SkiesBlocks.lunar_dirt.defaultBlockState(), SkiesBlocks.short_lunar_grass.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_ORCHARD_CAVE_DIRT = register(
      "crescent_orchard_cave_dirt",
      SkiesFeatures.CAVE_GRASS,
      new CaveGrassFeature.Config(
         SkiesBlocks.coarse_lunar_dirt.defaultBlockState(), SkiesBlocks.coarse_lunar_dirt.defaultBlockState(), SkiesBlocks.muckweed.defaultBlockState()
      )
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_ORCHARD_CAVE_BLOOM = register(
      "crescent_orchard_cave_bloom", SkiesFeatures.CAVE_PLANT, new CavePlantFeature.Config(SkiesBlocks.moonlit_bloom.defaultBlockState(), 25, 10)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_ORCHARD_CAVE_NIGHTCRESS = register(
      "crescent_orchard_cave_nightcress_patch", SkiesFeatures.CAVE_PLANT, new CavePlantFeature.Config(SkiesBlocks.nightcress.defaultBlockState(), 25, 10)
   );
   public static final ResourceKey<ConfiguredFeature<?, ?>> CRESCENT_ORCHARD_CAVE_LUCENTROOT = register(
      "crescent_orchard_cave_lucentroot_patch", SkiesFeatures.CAVE_PLANT, new CavePlantFeature.Config(SkiesBlocks.lucentroot.defaultBlockState(), 25, 10)
   );

   private static ResourceKey<ConfiguredFeature<?, ?>> register(String name) {
      return FeatureUtils.createKey(BlueSkies.find(name));
   }

   private static <FC extends FeatureConfiguration, F extends Feature<FC>> ResourceKey<ConfiguredFeature<?, ?>> register(String name, F feature, FC config) {
      return register(FeatureUtils.createKey(BlueSkies.find(name)), feature, config);
   }

   private static <FC extends FeatureConfiguration, F extends Feature<FC>> ResourceKey<ConfiguredFeature<?, ?>> register(
      ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config
   ) {
      if (DatagenModLoader.isRunningDataGen()) {
         CONFIGURED_VALUES.put(key.location(), new SkiesConfiguredFeatures.ConfiguredValues<>(key, feature, config));
      }

      return key;
   }

   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> bootstrap) {
      CONFIGURED_VALUES.forEach((key, value) -> FeatureUtils.register(bootstrap, value.key(), value.feature(), value.config()));
      HolderGetter<ConfiguredFeature<?, ?>> featureGetter = bootstrap.lookup(Registries.CONFIGURED_FEATURE);
      FeatureUtils.register(
         bootstrap,
         SNOW_COVERED_PINES_VEGETATION,
         Feature.RANDOM_SELECTOR,
         new RandomFeatureConfiguration(
            List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(featureGetter.getOrThrow(SMALL_STARLIT_TREE), new PlacementModifier[0]), 0.2F)),
            PlacementUtils.inlinePlaced(featureGetter.getOrThrow(STARLIT_TREE), new PlacementModifier[0])
         )
      );
      FeatureUtils.register(
         bootstrap,
         POLAR_HIGHLAND_VEGETATION,
         Feature.RANDOM_SELECTOR,
         new RandomFeatureConfiguration(
            List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(featureGetter.getOrThrow(BLUEBRIGHT_BUSH), new PlacementModifier[0]), 0.2F)),
            PlacementUtils.inlinePlaced(featureGetter.getOrThrow(SMALL_BLUEBRIGHT_TREE), new PlacementModifier[0])
         )
      );
      FeatureUtils.register(
         bootstrap,
         UNORTHODOX_VALLEY_VEGETATION,
         Feature.RANDOM_SELECTOR,
         new RandomFeatureConfiguration(
            List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced(featureGetter.getOrThrow(DUSK_TREE), new PlacementModifier[0]), 0.3F)),
            PlacementUtils.inlinePlaced(featureGetter.getOrThrow(LUNAR_TREE), new PlacementModifier[0])
         )
      );
   }

   public static void init() {
   }

   private record ConfiguredValues<FC extends FeatureConfiguration>(ResourceKey<ConfiguredFeature<?, ?>> key, Feature feature, FC config) {
   }
}
