package com.legacy.blue_skies.registries;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.blocks.natural.BrewberryBushBlock;
import com.legacy.blue_skies.world.everbright.gen.features.BluebrightTreeFeature;
import com.legacy.blue_skies.world.everbright.gen.features.BrumbleFeature;
import com.legacy.blue_skies.world.everbright.gen.features.BrumbleVineFeature;
import com.legacy.blue_skies.world.everbright.gen.features.ChilledLilyFeature;
import com.legacy.blue_skies.world.everbright.gen.features.FallenLogFeature;
import com.legacy.blue_skies.world.everbright.gen.features.FrostbrightTreeFeature;
import com.legacy.blue_skies.world.everbright.gen.features.OceanSpikeFeature;
import com.legacy.blue_skies.world.everbright.gen.features.PatchySnowFeature;
import com.legacy.blue_skies.world.everbright.gen.features.SeaMossFeature;
import com.legacy.blue_skies.world.everbright.gen.features.SkyIceAndSnowFeature;
import com.legacy.blue_skies.world.everbright.gen.features.SmallPineTreeFeature;
import com.legacy.blue_skies.world.everbright.gen.features.SnowcapMushroomFeature;
import com.legacy.blue_skies.world.everbright.gen.features.StarlitTreeFeature;
import com.legacy.blue_skies.world.everbright.gen.features.cave.EverbrightCaveSpawnerFeature;
import com.legacy.blue_skies.world.everbright.gen.features.cave.GiantCaveIcicleFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.CrescentFruitTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.CrystalizedTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.DuskTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.GlimmerReedFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.LargeDuskTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.LunarTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.MapleTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.MoonlitWaterLilyFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.MoonstoneSpikeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.ReservoirTreeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.ShortMoonstoneSpikeFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.cave.CaveCrystalFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.cave.CaveMagmaFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.cave.CaveWallCrystalFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.cave.EverdawnCaveSpawnerFeature;
import com.legacy.blue_skies.world.everdawn.gen.features.cave.LunarLavaSpireFeature;
import com.legacy.blue_skies.world.general_features.BushFeature;
import com.legacy.blue_skies.world.general_features.CometTreeFeature;
import com.legacy.blue_skies.world.general_features.LargeRockFeature;
import com.legacy.blue_skies.world.general_features.SkyLakesFeature;
import com.legacy.blue_skies.world.general_features.SkyOreFeature;
import com.legacy.blue_skies.world.general_features.carver.SkyCanyonWorldCarver;
import com.legacy.blue_skies.world.general_features.carver.SkyCaveWorldCarver;
import com.legacy.blue_skies.world.general_features.carver.SkyNoiseCarver;
import com.legacy.blue_skies.world.general_features.cave.CaveGrassFeature;
import com.legacy.blue_skies.world.general_features.cave.CavePlantFeature;
import com.legacy.blue_skies.world.general_features.cave.CaveVineFeature;
import com.legacy.blue_skies.world.general_features.cave.CaveWallFeature;
import com.legacy.blue_skies.world.general_features.cave.SkyDripstoneFeature;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.SimpleWeightedRandomList.Builder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;

public class SkiesFeatures {
   public static final TreeFeature BLUEBRIGHT_TREE = new BluebrightTreeFeature(TreeConfiguration.CODEC, 5);
   public static final TreeFeature STARLIT_TREE = new StarlitTreeFeature(TreeConfiguration.CODEC);
   public static final TreeFeature SNOWY_FROSTBRIGHT_TREE = new FrostbrightTreeFeature(TreeConfiguration.CODEC, true);
   public static final TreeFeature FROSTBRIGHT_TREE = new FrostbrightTreeFeature(TreeConfiguration.CODEC, false);
   public static final TreeFeature LUNAR_TREE = new LunarTreeFeature(TreeConfiguration.CODEC, 5);
   public static final TreeFeature DUSK_TREE = new DuskTreeFeature(TreeConfiguration.CODEC, 5);
   public static final TreeFeature VINED_LARGE_DUSK_TREE = new LargeDuskTreeFeature(TreeConfiguration.CODEC, true);
   public static final TreeFeature LARGE_DUSK_TREE = new LargeDuskTreeFeature(TreeConfiguration.CODEC, false);
   public static final TreeFeature MAPLE_TREE = new MapleTreeFeature(TreeConfiguration.CODEC, true);
   public static final TreeFeature MAPLE_TREE_UNNATURAL = new MapleTreeFeature(TreeConfiguration.CODEC, false);
   public static final TreeFeature CRESCENT_FRUIT_TREE = new CrescentFruitTreeFeature(TreeConfiguration.CODEC);
   public static final TreeFeature COMET_TREE = new CometTreeFeature(TreeConfiguration.CODEC, false);
   public static final TreeFeature NATURE_COMET_TREE = new CometTreeFeature(TreeConfiguration.CODEC, true);
   public static final TreeFeature SMALL_BLUEBRIGHT_TREE = new SmallPineTreeFeature(
      TreeConfiguration.CODEC, 3, () -> SkiesBlocks.bluebright_leaves.defaultBlockState(), () -> SkiesBlocks.bluebright_log.defaultBlockState()
   );
   public static final TreeFeature SMALL_STARLIT_TREE = new SmallPineTreeFeature(
      TreeConfiguration.CODEC, 5, () -> SkiesBlocks.starlit_leaves.defaultBlockState(), () -> SkiesBlocks.starlit_log.defaultBlockState()
   );
   public static final TreeFeature SMALL_FROSTBRIGHT_TREE = new SmallPineTreeFeature(
      TreeConfiguration.CODEC, 6, () -> SkiesBlocks.frostbright_leaves.defaultBlockState(), () -> SkiesBlocks.frostbright_log.defaultBlockState()
   );
   public static final TreeFeature RESERVOIR_TREE = new ReservoirTreeFeature(TreeConfiguration.CODEC, 7);
   public static final TreeFeature CRYSTALIZED_TREE = new CrystalizedTreeFeature(TreeConfiguration.CODEC, true);
   public static final TreeFeature CRYSTALIZED_TREE_UNNATURAL = new CrystalizedTreeFeature(TreeConfiguration.CODEC, false);
   public static final TreeFeature SNOWCAP_MUSHROOM = new SnowcapMushroomFeature(TreeConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> CHILLED_LILY = new ChilledLilyFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<MoonstoneSpikeFeature.Config> MOONSTONE_SPIKE = new MoonstoneSpikeFeature(MoonstoneSpikeFeature.Config.CODEC);
   public static final Feature<BlockStateConfiguration> EVERBRIGHT_LAKE = new SkyLakesFeature(BlockStateConfiguration.CODEC, false);
   public static final Feature<BlockStateConfiguration> EVERDAWN_LAKE = new SkyLakesFeature(BlockStateConfiguration.CODEC, true);
   public static final Feature<OreConfiguration> ORE = new SkyOreFeature(OreConfiguration.CODEC);
   public static final Feature<BushFeature.Config> BUSH = new BushFeature(BushFeature.Config.CODEC);
   public static final Feature<FallenLogFeature.Config> FALLEN_LOG = new FallenLogFeature(FallenLogFeature.Config.CODEC);
   public static final Feature<BlockStateConfiguration> LARGE_ROCK = new LargeRockFeature(BlockStateConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> SEA_MOSS = new SeaMossFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> GLIMMER_REEDS = new GlimmerReedFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> MOONLIT_WATER_LILIES = new MoonlitWaterLilyFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> OCEAN_SPIKE = new OceanSpikeFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> BRUMBLE = new BrumbleFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> BRUMBLE_VINE = new BrumbleVineFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> SHORT_MOONSTONE_SPIKE = new ShortMoonstoneSpikeFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> PATCHY_SNOW = new PatchySnowFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> ICE_AND_SNOW_NO_PATH = new SkyIceAndSnowFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> LUNAR_LAVA_SPIRE = new LunarLavaSpireFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<CaveVineFeature.Config> CAVE_VINE = new CaveVineFeature(CaveVineFeature.Config.CODEC);
   public static final Feature<CaveGrassFeature.Config> CAVE_GRASS = new CaveGrassFeature(CaveGrassFeature.Config.CODEC);
   public static final Feature<CaveWallFeature.Config> CAVE_WALL = new CaveWallFeature(CaveWallFeature.Config.CODEC);
   public static final Feature<SkyDripstoneFeature.Config> DRIPSTONE = new SkyDripstoneFeature(SkyDripstoneFeature.Config.CODEC);
   public static final Feature<NoneFeatureConfiguration> MOONSTONE_CAVE_CRYSTAL = new CaveCrystalFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<CavePlantFeature.Config> CAVE_PLANT = new CavePlantFeature(CavePlantFeature.Config.CODEC);
   public static final Feature<CaveWallCrystalFeature.Config> CAVE_WALL_CRYSTAL = new CaveWallCrystalFeature(CaveWallCrystalFeature.Config.CODEC);
   public static final Feature<NoneFeatureConfiguration> CAVE_MAGMA = new CaveMagmaFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> GIANT_CAVE_ICICLE = new GiantCaveIcicleFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> EVERBRIGHT_CAVE_SPAWNER = new EverbrightCaveSpawnerFeature(NoneFeatureConfiguration.CODEC);
   public static final Feature<NoneFeatureConfiguration> EVERDAWN_CAVE_SPAWNER = new EverdawnCaveSpawnerFeature(NoneFeatureConfiguration.CODEC);
   public static RegisterEvent registerEvent;

   public static void init(RegisterEvent event) {
      registerEvent = event;
      register("bluebright_tree", BLUEBRIGHT_TREE);
      register("starlit_tree", STARLIT_TREE);
      register("snowy_frostbright_tree", SNOWY_FROSTBRIGHT_TREE);
      register("frostbright_tree", FROSTBRIGHT_TREE);
      register("lunar_tree", LUNAR_TREE);
      register("dusk_tree", DUSK_TREE);
      register("vined_large_dusk_tree", VINED_LARGE_DUSK_TREE);
      register("large_dusk_tree", LARGE_DUSK_TREE);
      register("maple_tree", MAPLE_TREE);
      register("maple_tree_unnatural", MAPLE_TREE_UNNATURAL);
      register("crescent_fruit_tree", CRESCENT_FRUIT_TREE);
      register("comet_tree", COMET_TREE);
      register("nature_comet_tree", NATURE_COMET_TREE);
      register("small_bluebright_tree", SMALL_BLUEBRIGHT_TREE);
      register("small_starlit_tree", SMALL_STARLIT_TREE);
      register("small_frostbright_tree", SMALL_FROSTBRIGHT_TREE);
      register("reservoir_tree", RESERVOIR_TREE);
      register("crystalized_tree", CRYSTALIZED_TREE);
      register("crystalized_tree_unnatural", CRYSTALIZED_TREE_UNNATURAL);
      register("snowcap_mushroom", SNOWCAP_MUSHROOM);
      register("chilled_lily", CHILLED_LILY);
      register("moonstone_spike", MOONSTONE_SPIKE);
      register("short_moonstone_spike", SHORT_MOONSTONE_SPIKE);
      register("everbright_lake", EVERBRIGHT_LAKE);
      register("everdawn_lake", EVERDAWN_LAKE);
      register("ore", ORE);
      register("bush", BUSH);
      register("fallen_log", FALLEN_LOG);
      register("large_rock", LARGE_ROCK);
      register("glimmer_reeds", GLIMMER_REEDS);
      register("moonlit_water_lilies", MOONLIT_WATER_LILIES);
      register("sea_moss", SEA_MOSS);
      register("ocean_spike", OCEAN_SPIKE);
      register("brumble", BRUMBLE);
      register("brumble_vine", BRUMBLE_VINE);
      register("patchy_snow", PATCHY_SNOW);
      register("ice_and_snow_no_path", ICE_AND_SNOW_NO_PATH);
      register("lunar_lava_spire", LUNAR_LAVA_SPIRE);
      register("cave_vine", CAVE_VINE);
      register("cave_grass", CAVE_GRASS);
      register("cave_wall", CAVE_WALL);
      register("dripstone", DRIPSTONE);
      register("moonstone_cave_crystal", MOONSTONE_CAVE_CRYSTAL);
      register("cave_plant", CAVE_PLANT);
      register("cave_wall_crystal", CAVE_WALL_CRYSTAL);
      register("cave_magma", CAVE_MAGMA);
      register("giant_cave_icicle", GIANT_CAVE_ICICLE);
      register("everbright_cave_spawner", EVERBRIGHT_CAVE_SPAWNER);
      register("everdawn_cave_spawner", EVERDAWN_CAVE_SPAWNER);
   }

   private static void register(String key, Feature<?> feature) {
      registerEvent.register(Registries.FEATURE, BlueSkies.locate(key), () -> feature);
   }

   public static class Carvers {
      public static final Set<Block> CARVABLE_BLOCKS = Set.of(
         SkiesBlocks.turquoise_stone,
         SkiesBlocks.lunar_stone,
         SkiesBlocks.turquoise_dirt,
         SkiesBlocks.lunar_dirt,
         SkiesBlocks.coarse_turquoise_dirt,
         SkiesBlocks.coarse_lunar_dirt,
         SkiesBlocks.turquoise_grass_block,
         SkiesBlocks.lunar_grass_block,
         SkiesBlocks.crystal_sand,
         SkiesBlocks.crystal_sandstone,
         SkiesBlocks.midnight_sand,
         SkiesBlocks.midnight_sandstone,
         Blocks.GRAVEL,
         Blocks.SNOW,
         Blocks.STONE,
         Blocks.PACKED_ICE,
         Blocks.ICE
      );
      public static final Set<Block> CARVABLE_SEA_BLOCKS = Streams.concat(
            new Stream[]{CARVABLE_BLOCKS.stream(), ImmutableSet.of(Blocks.WATER, Blocks.LAVA, Blocks.OBSIDIAN, Blocks.AIR, Blocks.CAVE_AIR).stream()}
         )
         .collect(ImmutableSet.toImmutableSet());
      public static final WorldCarver<CarverConfiguration> SKY_NOISE_CARVER = new SkyNoiseCarver(CarverConfiguration.CODEC.codec(), 90);
      public static final WorldCarver<CarverConfiguration> EVERBRIGHT_NOISE_CARVER = new SkyNoiseCarver(CarverConfiguration.CODEC.codec(), 90) {
         @Override
         protected BlockState getStateForPlace(RandomSource rand, int y) {
            if (y > 10) {
               return CAVE_AIR;
            } else {
               return y == 10 && rand.nextFloat() < 0.4F ? Blocks.PACKED_ICE.defaultBlockState() : Blocks.ICE.defaultBlockState();
            }
         }
      };
      public static final WorldCarver<CaveCarverConfiguration> EVERBRIGHT_CAVE_CARVER = new SkyCaveWorldCarver(CaveCarverConfiguration.CODEC, true);
      public static final WorldCarver<CanyonCarverConfiguration> EVERBRIGHT_CANYON_CARVER = new SkyCanyonWorldCarver(CanyonCarverConfiguration.CODEC, true);
      public static final WorldCarver<CaveCarverConfiguration> SKY_CAVE_CARVER = new SkyCaveWorldCarver(CaveCarverConfiguration.CODEC, false);
      public static final WorldCarver<CanyonCarverConfiguration> SKY_CANYON_CARVER = new SkyCanyonWorldCarver(CanyonCarverConfiguration.CODEC, false);

      public static void init(RegisterEvent event) {
         register(event, "everbright_noise_carver", EVERBRIGHT_NOISE_CARVER);
         register(event, "sky_noise_carver", SKY_NOISE_CARVER);
         register(event, "everbright_cave_carver", EVERBRIGHT_CAVE_CARVER);
         register(event, "everbright_canyon_carver", EVERBRIGHT_CANYON_CARVER);
         register(event, "sky_cave_carver", SKY_CAVE_CARVER);
         register(event, "sky_canyon_carver", SKY_CANYON_CARVER);
      }

      private static void register(RegisterEvent event, String name, WorldCarver<?> entry) {
         event.register(Registries.CARVER, BlueSkies.locate(name), () -> entry);
      }
   }

   public static class Configs {
      public static final List<BlockState> EVERBRIGHT_COMET_GRASS_STATES = new ArrayList<>(SkiesPoiTypes.getAllStates(SkiesBlocks.turquoise_comet_grass_block));
      public static final RandomPatchConfiguration TALL_TURQUOISE_GRASS_CONFIG = filteredPatch(
         "tall_turquoise_grass", SkiesBlocks.tall_turquoise_grass.defaultBlockState(), 16, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration SNOWCAP_CONFIG = filteredPatch(
         "snowcap", SkiesBlocks.snowcap_mushroom.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration CAMELLIA_CONFIG = filteredPatch(
         "camellia", SkiesBlocks.camellia.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration SNOWBLOOM_CONFIG = filteredPatch(
         "snowbloom", SkiesBlocks.snowbloom.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration BREWBERRY_CONFIG = filteredPatch(
         "brewberry",
         (BlockState)SkiesBlocks.brewberry_bush.defaultBlockState().setValue(BrewberryBushBlock.MATURE, true),
         64,
         SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration BRITTLEBUSH_CONFIG = filteredPatch(
         "brittlebush", SkiesBlocks.brittlebush.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration POLAR_POSY_CONFIG = filteredPatch(
         "polar_posy", SkiesBlocks.polar_posy.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration CHILLWEED_CONFIG = filteredPatch(
         "chillweed", SkiesBlocks.chillweed.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration SEA_MOSS_CONFIG = simplePatch("sea_moss", SkiesBlocks.sea_moss.defaultBlockState(), 64);
      public static final RandomPatchConfiguration SNOWCAP_PINHEAD_CONFIG = filteredPatch(
         "snowcap_pinhead", SkiesBlocks.snowcap_pinhead.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration MIDDAY_BAYHOP_CONFIG = filteredPatch(
         "midday_bayhop", SkiesBlocks.midday_bayhop.defaultBlockState(), 64, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration TALL_LUNAR_GRASS_CONFIG = simplePatch(
         "tall_lunar_grass", SkiesBlocks.tall_lunar_grass.defaultBlockState(), 16
      );
      public static final RandomPatchConfiguration MOONLIT_BLOOM_CONFIG = simplePatch("moonlit_bloom", SkiesBlocks.moonlit_bloom.defaultBlockState(), 64);
      public static final RandomPatchConfiguration BANEFUL_MUSHROOM_CONFIG = simplePatch(
         "baneful_mushroom", SkiesBlocks.baneful_mushroom.defaultBlockState(), 16
      );
      public static final RandomPatchConfiguration BLAZE_BUD_CONFIG = simplePatch("blaze_bud", SkiesBlocks.blaze_bud.defaultBlockState(), 64);
      public static final RandomPatchConfiguration CRYSTAL_FLOWER_CONFIG = simplePatch("crystal_flower", SkiesBlocks.crystal_flower.defaultBlockState(), 64);
      public static final RandomPatchConfiguration FLARE_FLORET_CONFIG = simplePatch("flare_floret", SkiesBlocks.flare_floret.defaultBlockState(), 64);
      public static final RandomPatchConfiguration MUCKWEED_CONFIG = simplePatch("muckweed", SkiesBlocks.muckweed.defaultBlockState(), 64);
      public static final RandomPatchConfiguration LUCENTROOT_CONFIG = simplePatch("lucentroot", SkiesBlocks.lucentroot.defaultBlockState(), 16);
      public static final RandomPatchConfiguration NIGHTCRESS_CONFIG = simplePatch("nightcress", SkiesBlocks.nightcress.defaultBlockState(), 64);
      public static final RandomPatchConfiguration LUNAR_GRASS_MUCKWEED_MIX_CONFIG = weightedPatch(
         "lunar_grass_muckweed_mix", weighted().add(SkiesBlocks.short_lunar_grass.defaultBlockState(), 3).add(SkiesBlocks.muckweed.defaultBlockState(), 1), 32
      );
      public static final RandomPatchConfiguration FlARE_BLAZE_LUCENTROOT_MIX_CONFIG = weightedPatch(
         "flare_blaze_lucentroot_mix",
         weighted()
            .add(SkiesBlocks.flare_floret.defaultBlockState(), 2)
            .add(SkiesBlocks.blaze_bud.defaultBlockState(), 2)
            .add(SkiesBlocks.lucentroot.defaultBlockState(), 1),
         64
      );
      public static final RandomPatchConfiguration MOONLIT_BLOOM_NIGHTCRESS_LUCENTROOT_MIX_CONFIG = weightedPatch(
         "moonlit_bloom_nightcress_lucentroot_mix",
         weighted()
            .add(SkiesBlocks.moonlit_bloom.defaultBlockState(), 2)
            .add(SkiesBlocks.nightcress.defaultBlockState(), 2)
            .add(SkiesBlocks.lucentroot.defaultBlockState(), 1),
         64
      );
      public static final RandomPatchConfiguration BRISK_MEADOW_FLOWER_CONFIG = weightedPatch(
         "brisk_meadow_flower",
         weighted()
            .add(SkiesBlocks.briskbloom.defaultBlockState(), 5)
            .add(SkiesBlocks.chillweed.defaultBlockState(), 1)
            .add(SkiesBlocks.brittlebush.defaultBlockState(), 1),
         80,
         c -> PlacementUtils.filtered(
            (Feature)c.getLeft(),
            (FeatureConfiguration)c.getRight(),
            BlockPredicate.allOf(
               BlockPredicate.matchesBlocks(new Block[]{Blocks.AIR}),
               BlockPredicate.not(BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), new Block[]{SkiesBlocks.turquoise_comet_grass_block}))
            )
         )
      );
      public static final RandomPatchConfiguration SHADED_WOODLANDS_FLOWER_CONFIG = weightedPatch(
         "shaded_woodlands_flower",
         weighted()
            .add(SkiesBlocks.moonlit_bloom.defaultBlockState(), 2)
            .add(SkiesBlocks.nightcress.defaultBlockState(), 2)
            .add(SkiesBlocks.lucentroot.defaultBlockState(), 1),
         6
      );
      public static final RandomPatchConfiguration TURQUOISE_GRASS = filteredPatch(
         "turquoise_grass", SkiesBlocks.short_turquoise_grass.defaultBlockState(), 32, SkiesBlocks.turquoise_comet_grass_block
      );
      public static final RandomPatchConfiguration LUNAR_GRASS = simplePatch("lunar_grass", SkiesBlocks.short_lunar_grass.defaultBlockState(), 32);
      public static final RandomPatchConfiguration CRYSTALLIZED_GRASS = simplePatch(
         "crystallized_grass", SkiesBlocks.crystallized_grass.defaultBlockState(), 32
      );
      public static final RandomPatchConfiguration LIGHT_CRYSTALLIZED_GRASS = simplePatch(
         "light_crystallized_grass", SkiesBlocks.crystallized_grass.defaultBlockState(), 16
      );
      public static final RuleTest TURQUOISE_RULE_TEST = new BlockMatchTest(SkiesBlocks.turquoise_stone);
      public static final RuleTest LUNAR_RULE_TEST = new BlockMatchTest(SkiesBlocks.lunar_stone);

      public static RandomPatchConfiguration simplePatch(String name, BlockState state, int tries) {
         return simplePatch(name, state, tries, p -> PlacementUtils.onlyWhenEmpty((Feature)p.getLeft(), (FeatureConfiguration)p.getRight()));
      }

      public static RandomPatchConfiguration filteredPatch(String name, BlockState state, int tries, Block notBlock) {
         return simplePatch(
            name,
            state,
            tries,
            p -> PlacementUtils.filtered(
               (Feature)p.getLeft(),
               (FeatureConfiguration)p.getRight(),
               BlockPredicate.allOf(
                  BlockPredicate.matchesBlocks(new Block[]{Blocks.AIR}),
                  BlockPredicate.not(BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), new Block[]{notBlock}))
               )
            )
         );
      }

      public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatch(
         String name, BlockState state, int tries, Function<Pair<F, FC>, Holder<PlacedFeature>> cons
      ) {
         return randomPatchConfig(name, tries, cons.apply(Pair.of(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(state)))));
      }

      public static RandomPatchConfiguration weightedPatch(String name, Builder<BlockState> builder, int tries) {
         return weightedPatch(name, builder, tries, p -> PlacementUtils.onlyWhenEmpty((Feature)p.getLeft(), (FeatureConfiguration)p.getRight()));
      }

      public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration weightedPatch(
         String name, Builder<BlockState> builder, int tries, Function<Pair<F, FC>, Holder<PlacedFeature>> cons
      ) {
         return randomPatchConfig(name, tries, cons.apply(Pair.of(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(builder)))));
      }

      public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration randomPatchConfig(
         String name, int tries, Holder<PlacedFeature> placedHolder
      ) {
         return FeatureUtils.simpleRandomPatchConfiguration(tries, placedHolder);
      }

      public static Builder<BlockState> weighted() {
         return SimpleWeightedRandomList.builder();
      }

      public static List<TargetBlockState> oreConfig(BlockState bright, BlockState dawn) {
         return List.of(OreConfiguration.target(TURQUOISE_RULE_TEST, bright), OreConfiguration.target(LUNAR_RULE_TEST, dawn));
      }
   }
}
