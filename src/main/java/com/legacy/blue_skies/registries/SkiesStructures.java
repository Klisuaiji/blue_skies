package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.blue_skies.world.everbright.gen.structures.blinding_dungeon.EverbrightBlindingDungeonPieces;
import com.legacy.blue_skies.world.everbright.gen.structures.blinding_dungeon.EverbrightBlindingDungeonStructure;
import com.legacy.blue_skies.world.everbright.gen.structures.bunker.FrozenBunkerPools;
import com.legacy.blue_skies.world.everbright.gen.structures.bunker.FrozenBunkerStructure;
import com.legacy.blue_skies.world.everbright.gen.structures.nature_dungeon.NatureDungeonPieces;
import com.legacy.blue_skies.world.everbright.gen.structures.nature_dungeon.NatureDungeonStructure;
import com.legacy.blue_skies.world.everdawn.gen.structures.blinding_dungeon.EverdawnBlindingDungeonPieces;
import com.legacy.blue_skies.world.everdawn.gen.structures.blinding_dungeon.EverdawnBlindingDungeonStructure;
import com.legacy.blue_skies.world.everdawn.gen.structures.poison_dungeon.PoisonDungeonPieces;
import com.legacy.blue_skies.world.everdawn.gen.structures.poison_dungeon.PoisonDungeonStructure;
import com.legacy.blue_skies.world.general_features.structures.GatekeeperHousePools;
import com.legacy.blue_skies.world.general_features.structures.GatekeeperHouseStructure;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillageBiomes;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillagePools;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillageStructure;
import com.legacy.blue_skies.world.util.structure_processors.RemoveSnowStructureProcessor;
import com.legacy.structure_gel.api.data.tags.FilterHolderSet;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Pointer;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure;
import com.legacy.structure_gel.api.structure.GridStructurePlacement;
import com.legacy.structure_gel.api.structure.GridStructurePlacement.TaggedExclusionZone;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride.BoundingBoxType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

@RegistrarHolder
public class SkiesStructures {
   public static final RegistrarHandler<StructureTemplatePool> HANDLER = RegistrarHandler.getOrCreate(Registries.TEMPLATE_POOL, "blue_skies")
      .bootstrap(SkiesStructures::bootstrap);
   public static final StructureRegistrar<ExtendedJigsawStructure> FROZEN_BUNKER = StructureRegistrar.jigsawBuilder(BlueSkies.locate("frozen_bunker"))
      .addPiece(() -> FrozenBunkerStructure.Piece::new)
      .pushStructure(
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(FrozenBunkerPools.ROOT))
            .heightmap(Types.WORLD_SURFACE_WG)
            .capability(FrozenBunkerStructure.Capability.INSTANCE)
            .build()
      )
      .biomes(new ResourceKey[]{SkiesBiomes.SLUSHLANDS.getKey()})
      .popStructure()
      .placement(() -> GridStructurePlacement.builder(25, 0.7F).build(BlueSkies.locate("frozen_bunker")))
      .build();
   public static final StructureRegistrar<EverbrightBlindingDungeonStructure> EVERBRIGHT_BLINDING_DUNGEON = StructureRegistrar.builder(
         BlueSkies.locate("everbright_blinding_dungeon"), () -> () -> EverbrightBlindingDungeonStructure.CODEC
      )
      .addPiece(() -> EverbrightBlindingDungeonPieces.Piece::new)
      .pushStructure(EverbrightBlindingDungeonStructure::new)
      .biomes(
         new ResourceKey[]{
            SkiesBiomes.CALMING_SKIES.getKey(), SkiesBiomes.BRISK_MEADOW.getKey(), SkiesBiomes.BRIGHTLANDS.getKey(), SkiesBiomes.SNOW_COVERED_PINES.getKey()
         }
      )
      .spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, List::of)
      .popStructure()
      .placement(
         c -> GridStructurePlacement.builder(45, 31, 0.75F)
            .exclusionZone(new TaggedExclusionZone(c.lookup(Registries.STRUCTURE).getOrThrow(SkiesStructureTags.ON_NATURE_MAPS), 10))
            .build(BlueSkies.locate("everbright_blinding_dungeon"))
      )
      .build();
   public static final StructureRegistrar<EverdawnBlindingDungeonStructure> EVERDAWN_BLINDING_DUNGEON = StructureRegistrar.builder(
         BlueSkies.locate("everdawn_blinding_dungeon"), () -> () -> EverdawnBlindingDungeonStructure.CODEC
      )
      .addPiece(() -> EverdawnBlindingDungeonPieces.Piece::new)
      .pushStructure(EverdawnBlindingDungeonStructure::new)
      .biomes(
         new ResourceKey[]{
            SkiesBiomes.SHADED_WOODLANDS.getKey(),
            SkiesBiomes.CRYSTAL_DUNES.getKey(),
            SkiesBiomes.SUNSET_MAPLE_FOREST.getKey(),
            SkiesBiomes.CRESCENT_ORCHARD.getKey(),
            SkiesBiomes.CRYSTAL_ROUGHS.getKey(),
            SkiesBiomes.SEARING_GRASSLAND.getKey()
         }
      )
      .spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, List::of)
      .popStructure()
      .placement(
         c -> GridStructurePlacement.builder(45, 31, 0.75F)
            .exclusionZone(new TaggedExclusionZone(c.lookup(Registries.STRUCTURE).getOrThrow(SkiesStructureTags.ON_POISON_MAPS), 10))
            .build(BlueSkies.locate("everdawn_blinding_dungeon"))
      )
      .build();
   public static final StructureRegistrar<NatureDungeonStructure> NATURE_DUNGEON = StructureRegistrar.builder(
         BlueSkies.locate("nature_dungeon"), () -> () -> NatureDungeonStructure.CODEC
      )
      .addPiece("main", () -> NatureDungeonPieces.Piece::new)
      .addPiece("garden", () -> NatureDungeonPieces.GardenPiece::new)
      .addPiece("stone_fill", () -> NatureDungeonPieces.StoneFillPiece::new)
      .addPiece("air_bubble", () -> NatureDungeonPieces.AirBubblePiece::new)
      .pushStructure(NatureDungeonStructure::new)
      .biomes(new ResourceKey[]{SkiesBiomes.SNOW_COVERED_PINES.getKey()})
      .spawns(
         MobCategory.MONSTER,
         () -> new StructureSpawnOverride(
            BoundingBoxType.PIECE,
            WeightedRandomList.create(
               new SpawnerData[]{new SpawnerData(SkiesEntityTypes.STONELET, 2, 1, 1), new SpawnerData(SkiesEntityTypes.SPEWTER, 2, 1, 1)}
            )
         )
      )
      .popStructure()
      .placement(() -> GridStructurePlacement.builder(63, 33, 0.8F).build(BlueSkies.locate("nature_dungeon")))
      .build();
   public static final StructureRegistrar<PoisonDungeonStructure> POISON_DUNGEON = StructureRegistrar.builder(
         BlueSkies.locate("poison_dungeon"), () -> () -> PoisonDungeonStructure.CODEC
      )
      .addPiece(() -> PoisonDungeonPieces.Piece::new)
      .pushStructure(PoisonDungeonStructure::new)
      .biomes(new ResourceKey[]{SkiesBiomes.SUNSET_MAPLE_FOREST.getKey()})
      .spawns(
         MobCategory.MONSTER,
         () -> new StructureSpawnOverride(
            BoundingBoxType.PIECE,
            WeightedRandomList.create(
               new SpawnerData[]{new SpawnerData(EntityType.CAVE_SPIDER, 1, 1, 1), new SpawnerData(SkiesEntityTypes.VENOM_SPIDER, 3, 1, 1)}
            )
         )
      )
      .popStructure()
      .placement(() -> GridStructurePlacement.builder(63, 33, 0.8F).build(BlueSkies.locate("poison_dungeon")))
      .build();
   public static final StructureRegistrar<ExtendedJigsawStructure> GATEKEEPER_HOUSE = StructureRegistrar.jigsawBuilder(BlueSkies.locate("gatekeeper_house"))
      .addPiece(() -> GatekeeperHouseStructure.Piece::new)
      .pushStructure(
         "plains",
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(GatekeeperHousePools.PLAINS))
            .heightmap(Types.WORLD_SURFACE_WG)
            .capability(GatekeeperHouseStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(
         c -> FilterHolderSet.builder(Registries.BIOME)
            .tags(true, new TagKey[]{BiomeTags.HAS_VILLAGE_PLAINS, BiomeTags.IS_SAVANNA})
            .build(c.lookup(Registries.BIOME))
      )
      .dimensions(new ResourceKey[]{Level.OVERWORLD})
      .spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, List::of)
      .popStructure()
      .pushStructure(
         "snowy",
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(GatekeeperHousePools.SNOWY))
            .heightmap(Types.WORLD_SURFACE_WG)
            .capability(GatekeeperHouseStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(
         c -> FilterHolderSet.builder(Registries.BIOME)
            .tags(true, new TagKey[]{BiomeTags.HAS_VILLAGE_SNOWY, BiomeTags.HAS_VILLAGE_TAIGA})
            .build(c.lookup(Registries.BIOME))
      )
      .dimensions(new ResourceKey[]{Level.OVERWORLD})
      .spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, List::of)
      .popStructure()
      .pushStructure(
         "mountain",
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(GatekeeperHousePools.MOUNTAIN))
            .heightmap(Types.WORLD_SURFACE_WG)
            .capability(GatekeeperHouseStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(c -> FilterHolderSet.builder(Registries.BIOME).tags(true, new TagKey[]{BiomeTags.IS_MOUNTAIN}).build(c.lookup(Registries.BIOME)))
      .dimensions(new ResourceKey[]{Level.OVERWORLD})
      .spawns(MobCategory.MONSTER, BoundingBoxType.PIECE, List::of)
      .popStructure()
      .placement(() -> GridStructurePlacement.builder().spacing(33).offset(17).probability(0.75F).build(BlueSkies.locate("gatekeeper_house")))
      .build();
   public static final StructureRegistrar<ExtendedJigsawStructure> BLUE_SKIES_VILLAGE = StructureRegistrar.jigsawBuilder(BlueSkies.locate("village"))
      .addPiece(() -> SkiesVillageStructure.Piece::new)
      .pushStructure(
         SkiesVillageBiomes.CALMING_SKIES.toString(),
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(SkiesVillagePools.CALMING_SKIES))
            .heightmap(Types.WORLD_SURFACE_WG)
            .useExpansionHack()
            .maxDepth(6)
            .capability(SkiesVillageStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(new ResourceKey[]{SkiesBiomes.CALMING_SKIES.getKey()})
      .popStructure()
      .pushStructure(
         SkiesVillageBiomes.BRIGHTLANDS.toString(),
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(SkiesVillagePools.BRIGHTLANDS))
            .heightmap(Types.WORLD_SURFACE_WG)
            .useExpansionHack()
            .maxDepth(6)
            .capability(SkiesVillageStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(new ResourceKey[]{SkiesBiomes.SNOW_COVERED_PINES.getKey()})
      .popStructure()
      .pushStructure(
         SkiesVillageBiomes.SLUSHLANDS.toString(),
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(SkiesVillagePools.SLUSHLANDS))
            .heightmap(Types.WORLD_SURFACE_WG)
            .useExpansionHack()
            .maxDepth(6)
            .capability(SkiesVillageStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(new ResourceKey[]{SkiesBiomes.SLUSHLANDS.getKey()})
      .popStructure()
      .pushStructure(
         SkiesVillageBiomes.SHADED_WOODLANDS.toString(),
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(SkiesVillagePools.SHADED_WOODLANDS))
            .heightmap(Types.WORLD_SURFACE_WG)
            .useExpansionHack()
            .maxDepth(6)
            .capability(SkiesVillageStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(new ResourceKey[]{SkiesBiomes.SHADED_WOODLANDS.getKey()})
      .popStructure()
      .pushStructure(
         SkiesVillageBiomes.SUNSET_MAPLE_FOREST.toString(),
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(SkiesVillagePools.SUNSET_MAPLE_FOREST))
            .heightmap(Types.WORLD_SURFACE_WG)
            .useExpansionHack()
            .maxDepth(6)
            .capability(SkiesVillageStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(new ResourceKey[]{SkiesBiomes.SUNSET_MAPLE_FOREST.getKey()})
      .popStructure()
      .pushStructure(
         SkiesVillageBiomes.CRYSTAL_DUNES.toString(),
         (c, s) -> ExtendedJigsawStructure.builder(s, c.lookup(Registries.TEMPLATE_POOL).getOrThrow(SkiesVillagePools.CRYSTAL_DUNES))
            .heightmap(Types.WORLD_SURFACE_WG)
            .useExpansionHack()
            .maxDepth(6)
            .capability(SkiesVillageStructure.Capability.INSTANCE)
            .build()
      )
      .terrainAdjustment(TerrainAdjustment.BEARD_THIN)
      .biomes(new ResourceKey[]{SkiesBiomes.CRYSTAL_DUNES.getKey()})
      .popStructure()
      .placement(
         c -> GridStructurePlacement.builder(28, 0.75F)
            .exclusionZone(new TaggedExclusionZone(c.lookup(Registries.STRUCTURE).getOrThrow(SkiesStructureTags.DUNGEONS), 7))
            .build(BLUE_SKIES_VILLAGE)
      )
      .build();

   public static void bootstrap(BootstrapContext<StructureTemplatePool> bootstrap) {
      FrozenBunkerPools.bootstrap(bootstrap);
      GatekeeperHousePools.bootstrap(bootstrap);
      SkiesVillagePools.bootstrap(bootstrap);
   }

   @RegistrarHolder
   public static class ProcessorLists {
      public static final RegistrarHandler<StructureProcessorList> HANDLER = RegistrarHandler.getOrCreate(Registries.PROCESSOR_LIST, "blue_skies");
      public static final Pointer<StructureProcessorList> DECAY_TURQUOISE_STONEBRICK = register(
         "decay_turquoise_stonebrick",
         () -> new StructureProcessorList(
            List.of(
               new RandomBlockSwapProcessor(SkiesBlocks.turquoise_stonebrick, 0.1F, SkiesBlocks.mossy_turquoise_stonebrick),
               new RandomBlockSwapProcessor(SkiesBlocks.turquoise_stonebrick, 0.2F, SkiesBlocks.cracked_turquoise_stonebrick)
            )
         )
      );
      public static final Pointer<StructureProcessorList> REMOVE_SNOW = registerOne("remove_snow", () -> RemoveSnowStructureProcessor.INSTANCE);
      public static final Pointer<StructureProcessorList> SWAP_PORTAL = registerOne(
         "swap_portal", () -> new RandomBlockSwapProcessor(SkiesBlocks.lunar_stonebrick, SkiesBlocks.turquoise_stonebrick)
      );
      public static final Pointer<StructureProcessorList> MODIFY_GRASS = registerOne(
         "modify_grass",
         () -> new RuleProcessor(
            List.of(
               new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.STONE), Blocks.STONE.defaultBlockState()),
               new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.GRAVEL), Blocks.STONE.defaultBlockState()),
               new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.AIR), Blocks.STONE.defaultBlockState())
            )
         )
      );
      public static final Pointer<StructureProcessorList> MODIFY_PATH = registerOne(
         "modify_path",
         () -> new RuleProcessor(
            List.of(
               new ProcessorRule(new BlockMatchTest(Blocks.DIRT_PATH), new BlockMatchTest(Blocks.WATER), Blocks.OAK_PLANKS.defaultBlockState()),
               new ProcessorRule(new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK.defaultBlockState()),
               new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState()),
               new ProcessorRule(new BlockMatchTest(Blocks.DIRT), new BlockMatchTest(Blocks.WATER), Blocks.OAK_PLANKS.defaultBlockState())
            )
         )
      );
      public static final Pointer<StructureProcessorList> SWAP_PORTAL_MODIFY_GRASS = register(
         "swap_portal_modify_grass",
         () -> new StructureProcessorList(
            List.of(
               new RandomBlockSwapProcessor(SkiesBlocks.lunar_stonebrick, SkiesBlocks.turquoise_stonebrick),
               new RuleProcessor(
                  List.of(
                     new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.STONE), Blocks.STONE.defaultBlockState()),
                     new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.GRAVEL), Blocks.STONE.defaultBlockState()),
                     new ProcessorRule(new BlockMatchTest(Blocks.GRASS_BLOCK), new BlockMatchTest(Blocks.AIR), Blocks.STONE.defaultBlockState())
                  )
               )
            )
         )
      );
      public static final Pointer<StructureProcessorList> SWAP_PORTAL_REMOVE_SNOW = register(
         "swap_portal_remove_snow",
         c -> new StructureProcessorList(
            List.of(RemoveSnowStructureProcessor.INSTANCE, new RandomBlockSwapProcessor(SkiesBlocks.lunar_stonebrick, SkiesBlocks.turquoise_stonebrick))
         )
      );
      public static final Pointer<StructureProcessorList> CALMING_SKIES_STREET = registerOne(
         "calming_skies_street",
         () -> makeVillagePath(
            SkiesBlocks.coarse_turquoise_dirt,
            SkiesBlocks.turquoise_dirt,
            SkiesBlocks.turquoise_grass_block,
            SkiesBlocks.bluebright_planks,
            SkiesBlocks.midnight_sand,
            SkiesBlocks.midnight_sandstone
         )
      );
      public static final Pointer<StructureProcessorList> BRIGHTLANDS_STREET = registerOne(
         "brightlands_street",
         () -> makeVillagePath(
            SkiesBlocks.coarse_turquoise_dirt,
            SkiesBlocks.turquoise_dirt,
            SkiesBlocks.turquoise_grass_block,
            SkiesBlocks.starlit_planks,
            SkiesBlocks.midnight_sand,
            SkiesBlocks.midnight_sandstone
         )
      );
      public static final Pointer<StructureProcessorList> SLUSHLANDS_STREET = registerOne(
         "slushlands_street", () -> new RandomBlockSwapProcessor(SkiesBlocks.frostbright_planks, 0.2F, SkiesBlocks.stripped_frostbright_wood)
      );
      public static final Pointer<StructureProcessorList> SHADED_WOODLANDS_STREET = registerOne(
         "shaded_woodlands_street",
         () -> makeVillagePath(
            SkiesBlocks.coarse_lunar_dirt,
            SkiesBlocks.lunar_dirt,
            SkiesBlocks.lunar_grass_block,
            SkiesBlocks.lunar_planks,
            SkiesBlocks.crystal_sand,
            SkiesBlocks.crystal_sandstone
         )
      );
      public static final Pointer<StructureProcessorList> SUNSET_MAPLE_FOREST_STREET = registerOne(
         "sunset_maple_forest_street",
         () -> makeVillagePath(
            SkiesBlocks.coarse_lunar_dirt,
            SkiesBlocks.lunar_dirt,
            SkiesBlocks.lunar_grass_block,
            SkiesBlocks.maple_planks,
            SkiesBlocks.crystal_sand,
            SkiesBlocks.crystal_sandstone
         )
      );
      public static final Pointer<StructureProcessorList> CRYSTAL_DUNES = register(
         "crystal_dunes_street",
         () -> new StructureProcessorList(List.of(new RandomBlockSwapProcessor(SkiesBlocks.lunar_stonebrick, SkiesBlocks.turquoise_stonebrick)))
      );

      public static void init() {
      }

      private static Pointer<StructureProcessorList> registerOne(String key, Supplier<StructureProcessor> processor) {
         return register(key, () -> new StructureProcessorList(List.of(processor.get())));
      }

      private static Pointer<StructureProcessorList> register(String key, Supplier<StructureProcessorList> processorList) {
         return HANDLER.createPointer(key, processorList);
      }

      private static Pointer<StructureProcessorList> register(String key, Function<BootstrapContext<?>, StructureProcessorList> processorList) {
         return HANDLER.createPointer(key, processorList);
      }

      private static RuleProcessor makeVillagePath(
         Block path, Block dirt, Block grassBlock, Block bridgeMaterial, Block sand, Block sandstone
      ) {
         return new RuleProcessor(standardPath(path, dirt, grassBlock, bridgeMaterial, sand, sandstone));
      }

      private static List<ProcessorRule> standardPath(
         Block path, Block dirt, Block grassBlock, Block bridgeMaterial, Block sand, Block sandstone
      ) {
         List<ProcessorRule> rules = new ArrayList<>();
         rules.add(new ProcessorRule(new BlockMatchTest(path), new BlockMatchTest(Blocks.WATER), bridgeMaterial.defaultBlockState()));
         if (sand != null && sandstone != null) {
            rules.add(new ProcessorRule(new BlockMatchTest(path), new BlockMatchTest(sand), sandstone.defaultBlockState()));
         }

         rules.add(new ProcessorRule(new RandomBlockMatchTest(path, 0.2F), AlwaysTrueTest.INSTANCE, grassBlock.defaultBlockState()));
         rules.add(new ProcessorRule(new BlockMatchTest(grassBlock), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState()));
         rules.add(new ProcessorRule(new BlockMatchTest(dirt), new BlockMatchTest(Blocks.WATER), bridgeMaterial.defaultBlockState()));
         return rules;
      }
   }
}
