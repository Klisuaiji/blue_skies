package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.blocks.natural.LargeFlowerBlock;
import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.data.objects.tags.SkiesPoiTags;
import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.blue_skies.registries.SkiesBiomes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDamageTypes;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesPoiTypes;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillageBiomes;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Pointer;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.data.tags.TagsProvider.TagLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SkiesTagProv {
   public static class BiomeTagProv extends BiomeTagsProvider {
      public BiomeTagProv(DataGenerator generatorIn, ExistingFileHelper existingFileHelper, CompletableFuture<Provider> lookup) {
         super(generatorIn.getPackOutput(), lookup, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider prov) {
         this.blueSkies();
         this.vanilla();
         this.forge();
      }

      public CompletableFuture<?> run(CachedOutput cache) {
         SkiesEntityTags.init();
         return super.run(cache);
      }

      private void blueSkies() {
         this.add(SkiesBiomeTags.EVERBRIGHT, SkiesBiomes.EVERBRIGHT_BIOMES);
         this.add(SkiesBiomeTags.EVERDAWN, SkiesBiomes.EVERDAWN_BIOMES);
         this.add(SkiesBiomeTags.HAS_SANDSTORMS, SkiesBiomes.CRYSTAL_DUNES, SkiesBiomes.CRYSTAL_DUNES_SPIKES);
         this.tag(SkiesBiomeTags.DRIPSTONES_DRIP_LAVA)
            .add(new ResourceKey[]{SkiesBiomes.SEARING_GRASSLAND.getKey(), SkiesBiomes.SUNSET_MAPLE_FOREST.getKey()})
            .addTag(BiomeTags.IS_NETHER);
         this.add(SkiesBiomeTags.SHADE_MONITOR_PURPLE, SkiesBiomes.UNORTHODOX_VALLEY);
         this.add(SkiesBiomeTags.SHADE_MONITOR_WHITE, SkiesBiomes.CRYSTAL_DUNES, SkiesBiomes.CRYSTAL_DUNES_SPIKES);
         this.add(SkiesBiomeTags.SHADE_MONITOR_WHITE_AND_PURPLE, SkiesBiomes.CRYSTAL_ROUGHS);
         this.add(SkiesBiomeTags.SHADE_MONITOR_RED, SkiesBiomes.SUNSET_MAPLE_FOREST, SkiesBiomes.SEARING_GRASSLAND);
         this.add(SkiesBiomeTags.SHADE_MONITOR_PINK, SkiesBiomes.SHADED_WOODLANDS);
         this.add(SkiesBiomeTags.SHADE_MONITOR_MUDDY_PURPLE, SkiesBiomes.MOONLIT_RESERVOIR);
         this.tag(SkiesBiomeTags.SHADE_MONITOR_SNOWY_BLUE).addTag(SkiesBiomeTags.EVERBRIGHT);
      }

      private void vanilla() {
         this.add(BiomeTags.INCREASED_FIRE_BURNOUT, SkiesBiomes.RISING_CREEK, SkiesBiomes.MOONLIT_RESERVOIR, SkiesBiomes.BRIGHTLANDS);
         this.tag(BiomeTags.SNOW_GOLEM_MELTS).addTag(SkiesBiomeTags.EVERDAWN);
         this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).addTag(SkiesBiomeTags.EVERDAWN);
         this.tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).addTag(SkiesBiomeTags.EVERBRIGHT);
         this.tag(BiomeTags.SPAWNS_SNOW_FOXES).addTag(SkiesBiomeTags.EVERBRIGHT);
         this.tag(BiomeTags.SPAWNS_WHITE_RABBITS).addTag(SkiesBiomeTags.EVERBRIGHT);
         this.add(
            BiomeTags.WATER_ON_MAP_OUTLINES,
            SkiesBiomes.PEEKING_OCEAN,
            SkiesBiomes.DEEP_PEEKING_OCEAN,
            SkiesBiomes.BRUMBLE_FOREST,
            SkiesBiomes.RISING_CREEK,
            SkiesBiomes.MOONLIT_RESERVOIR
         );
      }

      private void forge() {
      }

      private void add(TagKey<Biome> tag, Collection<Pointer<Biome>> list) {
         this.tag(tag).add(list.stream().map(r -> r.getKey()).toArray(ResourceKey[]::new));
      }

      private void add(TagKey<Biome> tag, Pointer<Biome>... registrars) {
         this.tag(tag).add(Arrays.stream(registrars).map(r -> r.getKey()).toArray(ResourceKey[]::new));
      }

      public String getName() {
         return "Blue Skies Biome Tags";
      }
   }

   public static class BlockTagProv extends BlockTagsProvider {
      public BlockTagProv(DataGenerator generatorIn, ExistingFileHelper existingFileHelper, CompletableFuture<Provider> lookup) {
         super(generatorIn.getPackOutput(), lookup, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider prov) {
         this.blueSkies(prov);
         this.vanilla(prov);
         this.forge(prov);
      }

      public CompletableFuture<?> run(CachedOutput cache) {
         SkiesBlockTags.init();
         return super.run(cache);
      }

      private void blueSkies(Provider prov) {
         this.tag(SkiesBlockTags.BOOKSHELVES)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_bookshelf,
                  SkiesBlocks.starlit_bookshelf,
                  SkiesBlocks.frostbright_bookshelf,
                  SkiesBlocks.lunar_bookshelf,
                  SkiesBlocks.dusk_bookshelf,
                  SkiesBlocks.maple_bookshelf,
                  SkiesBlocks.comet_bookshelf
               }
            );
         this.tag(SkiesBlockTags.CRAFTING_TABLES)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_crafting_table,
                  SkiesBlocks.starlit_crafting_table,
                  SkiesBlocks.frostbright_crafting_table,
                  SkiesBlocks.lunar_crafting_table,
                  SkiesBlocks.dusk_crafting_table,
                  SkiesBlocks.maple_crafting_table,
                  SkiesBlocks.comet_crafting_table
               }
            );
         this.tag(SkiesBlockTags.LADDERS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_ladder,
                  SkiesBlocks.starlit_ladder,
                  SkiesBlocks.frostbright_ladder,
                  SkiesBlocks.lunar_ladder,
                  SkiesBlocks.dusk_ladder,
                  SkiesBlocks.maple_ladder,
                  SkiesBlocks.comet_ladder
               }
            );
         this.tag(SkiesBlockTags.VINES)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_vine,
                  SkiesBlocks.starlit_vine,
                  SkiesBlocks.frostbright_vine,
                  SkiesBlocks.lunar_vine,
                  SkiesBlocks.dusk_vine,
                  SkiesBlocks.maple_vine
               }
            );
         this.tag(SkiesBlockTags.BLUEBRIGHT_LOGS)
            .add(
               new Block[]{SkiesBlocks.bluebright_log, SkiesBlocks.bluebright_wood, SkiesBlocks.stripped_bluebright_log, SkiesBlocks.stripped_bluebright_wood}
            );
         this.tag(SkiesBlockTags.STARLIT_LOGS)
            .add(new Block[]{SkiesBlocks.starlit_log, SkiesBlocks.starlit_wood, SkiesBlocks.stripped_starlit_log, SkiesBlocks.stripped_starlit_wood});
         this.tag(SkiesBlockTags.FROSTBRIGHT_LOGS)
            .add(
               new Block[]{
                  SkiesBlocks.frostbright_log, SkiesBlocks.frostbright_wood, SkiesBlocks.stripped_frostbright_log, SkiesBlocks.stripped_frostbright_wood
               }
            );
         this.tag(SkiesBlockTags.LUNAR_LOGS)
            .add(new Block[]{SkiesBlocks.lunar_log, SkiesBlocks.lunar_wood, SkiesBlocks.stripped_lunar_log, SkiesBlocks.stripped_lunar_wood});
         this.tag(SkiesBlockTags.DUSK_LOGS)
            .add(new Block[]{SkiesBlocks.dusk_log, SkiesBlocks.dusk_wood, SkiesBlocks.stripped_dusk_log, SkiesBlocks.stripped_dusk_wood});
         this.tag(SkiesBlockTags.MAPLE_LOGS)
            .add(new Block[]{SkiesBlocks.maple_log, SkiesBlocks.maple_wood, SkiesBlocks.stripped_maple_log, SkiesBlocks.stripped_maple_wood});
         this.tag(SkiesBlockTags.COMET_LOGS)
            .add(new Block[]{SkiesBlocks.comet_log, SkiesBlocks.comet_wood, SkiesBlocks.stripped_comet_log, SkiesBlocks.stripped_comet_wood});
         this.tag(SkiesBlockTags.CRYSTALLIZED_LOGS).add(new Block[]{SkiesBlocks.crystallized_log, SkiesBlocks.crystallized_wood});
         this.tag(SkiesBlockTags.COBBLESTONES).add(new Block[]{SkiesBlocks.turquoise_cobblestone, SkiesBlocks.lunar_cobblestone});
         this.tag(SkiesBlockTags.STONES)
            .add(
               new Block[]{
                  SkiesBlocks.turquoise_stone, SkiesBlocks.lunar_stone, SkiesBlocks.rimestone, SkiesBlocks.taratite, SkiesBlocks.umber, SkiesBlocks.cinderstone
               }
            );
         this.tag(SkiesBlockTags.BLINDING_STONES).add(new Block[]{SkiesBlocks.blinding_stone, SkiesBlocks.glowing_blinding_stone});
         this.tag(SkiesBlockTags.POISON_STONES).add(new Block[]{SkiesBlocks.poison_stone, SkiesBlocks.glowing_poison_stone, SkiesBlocks.poison_stonebrick});
         this.tag(SkiesBlockTags.NATURE_STONES).add(new Block[]{SkiesBlocks.nature_stone, SkiesBlocks.glowing_nature_stone});
         this.tag(SkiesBlockTags.AQUATIC_STONES).add(new Block[]{SkiesBlocks.aquatic_stone, SkiesBlocks.glowing_aquatic_stone});
         this.tag(SkiesBlockTags.LIFE_STONES).add(new Block[]{SkiesBlocks.life_stone, SkiesBlocks.glowing_life_stone});
         this.tag(SkiesBlockTags.ILLAGER_TELEPORTABLE)
            .add(
               new Block[]{
                  SkiesBlocks.turquoise_stonebrick,
                  SkiesBlocks.mossy_turquoise_stonebrick,
                  SkiesBlocks.cracked_turquoise_stonebrick,
                  SkiesBlocks.lunar_stonebrick,
                  SkiesBlocks.mossy_lunar_stonebrick,
                  SkiesBlocks.cracked_lunar_stonebrick
               }
            );
         this.tag(SkiesBlockTags.MOONSTONE_ORES).add(new Block[]{SkiesBlocks.everbright_moonstone_ore, SkiesBlocks.everdawn_moonstone_ore});
         this.tag(SkiesBlockTags.PYROPE_ORES).add(new Block[]{SkiesBlocks.everbright_pyrope_ore, SkiesBlocks.everdawn_pyrope_ore});
         this.tag(SkiesBlockTags.DIOPSIDE_ORES).add(new Block[]{SkiesBlocks.everbright_diopside_ore, SkiesBlocks.everdawn_diopside_ore});
         this.tag(SkiesBlockTags.AQUITE_ORES).add(new Block[]{SkiesBlocks.everbright_aquite_ore, SkiesBlocks.everdawn_aquite_ore});
         this.tag(SkiesBlockTags.CHAROITE_ORES).add(new Block[]{SkiesBlocks.everbright_charoite_ore, SkiesBlocks.everdawn_charoite_ore});
         this.tag(SkiesBlockTags.HORIZONITE_ORES).add(SkiesBlocks.horizonite_ore);
         this.tag(SkiesBlockTags.FALSITE_ORES).add(SkiesBlocks.falsite_ore);
         this.tag(SkiesBlockTags.VENTIUM_ORES).add(new Block[]{SkiesBlocks.ventium_ore, SkiesBlocks.ventium_ore});
         this.tag(SkiesBlockTags.MOONSTONE_BLOCKS).add(SkiesBlocks.moonstone_block);
         this.tag(SkiesBlockTags.PYROPE_BLOCKS).add(SkiesBlocks.pyrope_block);
         this.tag(SkiesBlockTags.DIOPSIDE_BLOCKS).add(SkiesBlocks.diopside_block);
         this.tag(SkiesBlockTags.AQUITE_BLOCKS).add(SkiesBlocks.aquite_block);
         this.tag(SkiesBlockTags.CHAROITE_BLOCKS).add(SkiesBlocks.charoite_block);
         this.tag(SkiesBlockTags.HORIZONITE_BLOCKS).add(SkiesBlocks.horizonite_block);
         this.tag(SkiesBlockTags.FALSITE_BLOCKS).add(SkiesBlocks.falsite_block);
         this.tag(SkiesBlockTags.VENTIUM_BLOCKS).add(SkiesBlocks.ventium_block);
         this.tag(SkiesBlockTags.RAW_AQUITE_BLOCKS).add(SkiesBlocks.raw_aquite_block);
         this.tag(SkiesBlockTags.RAW_CHAROITE_BLOCKS).add(SkiesBlocks.raw_charoite_block);
         this.tag(SkiesBlockTags.RAW_HORIZONITE_BLOCKS).add(SkiesBlocks.raw_horizonite_block);
         this.tag(SkiesBlockTags.RAW_FALSITE_BLOCKS).add(SkiesBlocks.raw_falsite_block);
         this.tag(SkiesBlockTags.RAW_VENTIUM_BLOCKS).add(SkiesBlocks.raw_ventium_block);
         this.tag(SkiesBlockTags.CARVER_REPLACEABLES)
            .add(new Block[]{SkiesBlocks.lunar_mud, Blocks.GRAVEL})
            .addTags(
               new TagKey[]{
                  SkiesBlockTags.STONES,
                  BlockTags.DIRT,
                  net.neoforged.neoforge.common.Tags.Blocks.SANDSTONE,
                  BlockTags.SNOW,
                  BlockTags.ICE,
                  BlockTags.DIRT,
                  BlockTags.SAND
               }
            );
         this.tag(SkiesBlockTags.BLUE_FIRE_BASE_BLOCKS).add(SkiesBlocks.chiseled_turquoise_stonebrick);
         this.tag(SkiesBlockTags.BLACK_FIRE_BASE_BLOCKS).add(SkiesBlocks.chiseled_lunar_stonebrick);
         this.tag(SkiesBlockTags.WEBBING_ATTACHES_TO).add(new Block[]{SkiesBlocks.spider_webbing, Blocks.COBWEB, SkiesBlocks.poison_keystone});
      }

      private void vanilla(Provider prov) {
         this.addMatching(BlockTags.FLOWER_POTS, b -> b instanceof FlowerPotBlock);
         this.tag(BlockTags.IMPERMEABLE).add(new Block[]{SkiesBlocks.crystal_glass, SkiesBlocks.midnight_glass});
         this.addMatching(BlockTags.LEAVES, b -> b instanceof LeavesBlock);
         this.tag(BlockTags.LOGS).add(new Block[]{SkiesBlocks.crystallized_log, SkiesBlocks.crystallized_wood});
         this.tag(BlockTags.LOGS_THAT_BURN)
            .addTags(
               new TagKey[]{
                  SkiesBlockTags.BLUEBRIGHT_LOGS,
                  SkiesBlockTags.STARLIT_LOGS,
                  SkiesBlockTags.FROSTBRIGHT_LOGS,
                  SkiesBlockTags.LUNAR_LOGS,
                  SkiesBlockTags.DUSK_LOGS,
                  SkiesBlockTags.MAPLE_LOGS,
                  SkiesBlockTags.COMET_LOGS
               }
            );
         this.addMatching(BlockTags.SAPLINGS, b -> b instanceof SaplingBlock);
         this.addMatching(BlockTags.VALID_SPAWN, b -> b instanceof GrassBlock);
         this.addMatching(BlockTags.WALLS, b -> b instanceof WallBlock);
         this.addMatching(BlockTags.SLABS, b -> b instanceof SlabBlock);
         this.tag(BlockTags.WOODEN_SLABS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_slab,
                  SkiesBlocks.starlit_slab,
                  SkiesBlocks.frostbright_slab,
                  SkiesBlocks.lunar_slab,
                  SkiesBlocks.dusk_slab,
                  SkiesBlocks.maple_slab,
                  SkiesBlocks.comet_slab
               }
            );
         this.addMatching(
            BlockTags.SMALL_FLOWERS,
            b -> (b instanceof FlowerBlock || b instanceof LargeFlowerBlock) && b != SkiesBlocks.muckweed && b != SkiesBlocks.brittlebush
         );
         this.addMatching(BlockTags.STAIRS, b -> b instanceof StairBlock);
         this.tag(BlockTags.WOODEN_STAIRS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_stairs,
                  SkiesBlocks.starlit_stairs,
                  SkiesBlocks.frostbright_stairs,
                  SkiesBlocks.lunar_stairs,
                  SkiesBlocks.dusk_stairs,
                  SkiesBlocks.maple_stairs,
                  SkiesBlocks.comet_stairs
               }
            );
         this.tag(BlockTags.BUTTONS).add(new Block[]{SkiesBlocks.turquoise_stone_button, SkiesBlocks.lunar_stone_button});
         this.tag(BlockTags.WOODEN_BUTTONS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_button,
                  SkiesBlocks.starlit_button,
                  SkiesBlocks.frostbright_button,
                  SkiesBlocks.lunar_button,
                  SkiesBlocks.dusk_button,
                  SkiesBlocks.maple_button,
                  SkiesBlocks.comet_button
               }
            );
         this.tag(BlockTags.DOORS).add(SkiesBlocks.crystallized_door);
         this.tag(BlockTags.WOODEN_DOORS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_door,
                  SkiesBlocks.starlit_door,
                  SkiesBlocks.frostbright_door,
                  SkiesBlocks.lunar_door,
                  SkiesBlocks.dusk_door,
                  SkiesBlocks.maple_door,
                  SkiesBlocks.comet_door
               }
            );
         this.tag(BlockTags.WOODEN_DOORS).add(SkiesBlocks.crystallized_door);
         this.tag(BlockTags.WOODEN_FENCES)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_fence,
                  SkiesBlocks.starlit_fence,
                  SkiesBlocks.frostbright_fence,
                  SkiesBlocks.lunar_fence,
                  SkiesBlocks.dusk_fence,
                  SkiesBlocks.maple_fence,
                  SkiesBlocks.comet_fence
               }
            );
         this.tag(BlockTags.FENCE_GATES)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_fence_gate,
                  SkiesBlocks.starlit_fence_gate,
                  SkiesBlocks.frostbright_fence_gate,
                  SkiesBlocks.lunar_fence_gate,
                  SkiesBlocks.dusk_fence_gate,
                  SkiesBlocks.maple_fence_gate,
                  SkiesBlocks.comet_fence_gate
               }
            );
         this.tag(BlockTags.PRESSURE_PLATES).add(new Block[]{SkiesBlocks.turquoise_stone_pressure_plate, SkiesBlocks.lunar_stone_pressure_plate});
         this.tag(BlockTags.WOODEN_PRESSURE_PLATES)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_pressure_plate,
                  SkiesBlocks.starlit_pressure_plate,
                  SkiesBlocks.frostbright_pressure_plate,
                  SkiesBlocks.lunar_pressure_plate,
                  SkiesBlocks.dusk_pressure_plate,
                  SkiesBlocks.maple_pressure_plate,
                  SkiesBlocks.comet_pressure_plate
               }
            );
         this.tag(BlockTags.TRAPDOORS).add(SkiesBlocks.crystallized_trapdoor);
         this.tag(BlockTags.WOODEN_TRAPDOORS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_trapdoor,
                  SkiesBlocks.starlit_trapdoor,
                  SkiesBlocks.frostbright_trapdoor,
                  SkiesBlocks.lunar_trapdoor,
                  SkiesBlocks.dusk_trapdoor,
                  SkiesBlocks.maple_trapdoor,
                  SkiesBlocks.comet_trapdoor
               }
            );
         this.addMatching(BlockTags.STANDING_SIGNS, b -> b instanceof StandingSignBlock);
         this.addMatching(BlockTags.WALL_SIGNS, b -> b instanceof WallSignBlock);
         this.addMatching(BlockTags.CEILING_HANGING_SIGNS, b -> b instanceof CeilingHangingSignBlock);
         this.addMatching(BlockTags.WALL_HANGING_SIGNS, b -> b instanceof WallHangingSignBlock);
         this.tag(BlockTags.PIGLIN_REPELLENTS).add(new Block[]{SkiesBlocks.warding_pearl, SkiesBlocks.wall_warding_pearl});
         this.tag(BlockTags.PLANKS)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_planks,
                  SkiesBlocks.starlit_planks,
                  SkiesBlocks.frostbright_planks,
                  SkiesBlocks.lunar_planks,
                  SkiesBlocks.dusk_planks,
                  SkiesBlocks.maple_planks,
                  SkiesBlocks.comet_planks
               }
            );
         this.tag(BlockTags.CLIMBABLE).addTags(new TagKey[]{SkiesBlockTags.LADDERS, SkiesBlockTags.VINES});
         this.tag(BlockTags.BEACON_BASE_BLOCKS)
            .addTags(
               new TagKey[]{
                  SkiesBlockTags.PYROPE_BLOCKS,
                  SkiesBlockTags.DIOPSIDE_BLOCKS,
                  SkiesBlockTags.AQUITE_BLOCKS,
                  SkiesBlockTags.CHAROITE_BLOCKS,
                  SkiesBlockTags.HORIZONITE_BLOCKS,
                  SkiesBlockTags.FALSITE_BLOCKS,
                  SkiesBlockTags.VENTIUM_BLOCKS
               }
            );
         this.tag(BlockTags.DIRT)
            .add(
               new Block[]{
                  SkiesBlocks.turquoise_grass_block,
                  SkiesBlocks.lunar_grass_block,
                  SkiesBlocks.turquoise_dirt,
                  SkiesBlocks.lunar_dirt,
                  SkiesBlocks.coarse_turquoise_dirt,
                  SkiesBlocks.coarse_lunar_dirt,
                  SkiesBlocks.turquoise_comet_grass_block,
                  SkiesBlocks.lunar_comet_grass_block
               }
            );
         this.tag(BlockTags.WALL_POST_OVERRIDE).add(SkiesBlocks.moonstone);
         this.tag(BlockTags.SAND).add(new Block[]{SkiesBlocks.crystal_sand, SkiesBlocks.midnight_sand});
         this.tag(BlockTags.EMERALD_ORES).add(new Block[]{SkiesBlocks.everbright_emerald_ore, SkiesBlocks.everdawn_emerald_ore});
         this.tag(BlockTags.MOSS_REPLACEABLE).addTag(SkiesBlockTags.STONES);
         this.tag(BlockTags.SCULK_REPLACEABLE).addTag(SkiesBlockTags.STONES);
         this.tag(BlockTags.CROPS)
            .add(
               new Block[]{
                  SkiesBlocks.winter_leaves,
                  SkiesBlocks.pine_fruits,
                  SkiesBlocks.cryo_roots,
                  SkiesBlocks.solnuts,
                  SkiesBlocks.scalefruits,
                  SkiesBlocks.fiery_beans
               }
            );
         this.tag(BlockTags.FROG_PREFER_JUMP_TO).add(new Block[]{SkiesBlocks.chilled_lily_pad, SkiesBlocks.moonlit_water_lily});
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{
                  SkiesBlocks.turquoise_stone,
                  SkiesBlocks.turquoise_cobblestone,
                  SkiesBlocks.mossy_turquoise_cobblestone,
                  SkiesBlocks.turquoise_stonebrick,
                  SkiesBlocks.mossy_turquoise_stonebrick,
                  SkiesBlocks.cracked_turquoise_stonebrick,
                  SkiesBlocks.chiseled_turquoise_stonebrick,
                  SkiesBlocks.taratite,
                  SkiesBlocks.polished_taratite,
                  SkiesBlocks.rimestone,
                  SkiesBlocks.polished_rimestone,
                  SkiesBlocks.polished_brumble,
                  SkiesBlocks.polished_brumble_bricks,
                  SkiesBlocks.chiseled_polished_brumble,
                  SkiesBlocks.brumble_lantern,
                  SkiesBlocks.midnight_sandstone,
                  SkiesBlocks.chiseled_midnight_sandstone,
                  SkiesBlocks.cut_midnight_sandstone,
                  SkiesBlocks.smooth_midnight_sandstone,
                  SkiesBlocks.midnight_sandstone_pillar,
                  SkiesBlocks.turquoise_dripstone,
                  SkiesBlocks.everbright_diopside_ore,
                  SkiesBlocks.everbright_pyrope_ore,
                  SkiesBlocks.everbright_aquite_ore,
                  SkiesBlocks.everbright_charoite_ore,
                  SkiesBlocks.everbright_moonstone_ore,
                  SkiesBlocks.everbright_emerald_ore,
                  SkiesBlocks.falsite_ore,
                  SkiesBlocks.ventium_ore,
                  SkiesBlocks.lunar_stone,
                  SkiesBlocks.lunar_cobblestone,
                  SkiesBlocks.mossy_lunar_cobblestone,
                  SkiesBlocks.lunar_stonebrick,
                  SkiesBlocks.mossy_lunar_stonebrick,
                  SkiesBlocks.cracked_lunar_stonebrick,
                  SkiesBlocks.chiseled_lunar_stonebrick,
                  SkiesBlocks.umber,
                  SkiesBlocks.polished_umber,
                  SkiesBlocks.cinderstone,
                  SkiesBlocks.polished_cinderstone,
                  SkiesBlocks.crystallized_grass,
                  SkiesBlocks.crystallized_leaves,
                  SkiesBlocks.crystallized_log,
                  SkiesBlocks.crystallized_wood,
                  SkiesBlocks.crystallized_planks,
                  SkiesBlocks.vitreous_moonstone,
                  SkiesBlocks.crystal_sandstone,
                  SkiesBlocks.chiseled_crystal_sandstone,
                  SkiesBlocks.cut_crystal_sandstone,
                  SkiesBlocks.smooth_crystal_sandstone,
                  SkiesBlocks.crystal_sandstone_pillar,
                  SkiesBlocks.lunar_dripstone,
                  SkiesBlocks.decaying_spike,
                  SkiesBlocks.everdawn_diopside_ore,
                  SkiesBlocks.everdawn_pyrope_ore,
                  SkiesBlocks.everdawn_aquite_ore,
                  SkiesBlocks.everdawn_charoite_ore,
                  SkiesBlocks.everdawn_moonstone_ore,
                  SkiesBlocks.everdawn_emerald_ore,
                  SkiesBlocks.horizonite_ore,
                  SkiesBlocks.comet_berry_pie,
                  SkiesBlocks.raw_falsite_block,
                  SkiesBlocks.raw_ventium_block,
                  SkiesBlocks.raw_horizonite_block,
                  SkiesBlocks.raw_aquite_block,
                  SkiesBlocks.raw_charoite_block,
                  SkiesBlocks.moonstone_block,
                  SkiesBlocks.sunstone_block,
                  SkiesBlocks.falsite_block,
                  SkiesBlocks.ventium_block,
                  SkiesBlocks.horizonite_block,
                  SkiesBlocks.pyrope_block,
                  SkiesBlocks.aquite_block,
                  SkiesBlocks.diopside_block,
                  SkiesBlocks.charoite_block,
                  SkiesBlocks.moonstone,
                  SkiesBlocks.warding_pearl,
                  SkiesBlocks.wall_warding_pearl,
                  SkiesBlocks.moonstone_crystal,
                  SkiesBlocks.sunstone_crystal,
                  SkiesBlocks.snowcap_oven,
                  SkiesBlocks.horizonite_forge,
                  SkiesBlocks.crystallized_door,
                  SkiesBlocks.crystallized_trapdoor,
                  SkiesBlocks.moonstone_pressure_plate,
                  SkiesBlocks.turquoise_stone_pressure_plate,
                  SkiesBlocks.lunar_stone_pressure_plate,
                  SkiesBlocks.crystallized_pressure_plate,
                  SkiesBlocks.turquoise_stone_button,
                  SkiesBlocks.lunar_stone_button,
                  SkiesBlocks.crystallized_button,
                  SkiesBlocks.summoner_trophy,
                  SkiesBlocks.alchemist_trophy,
                  SkiesBlocks.arachnarch_trophy,
                  SkiesBlocks.starlit_crusher_trophy,
                  SkiesBlocks.crystallized_slab,
                  SkiesBlocks.turquoise_stone_slab,
                  SkiesBlocks.turquoise_cobblestone_slab,
                  SkiesBlocks.mossy_turquoise_cobblestone_slab,
                  SkiesBlocks.turquoise_stonebrick_slab,
                  SkiesBlocks.mossy_turquoise_stonebrick_slab,
                  SkiesBlocks.cracked_turquoise_stonebrick_slab,
                  SkiesBlocks.midnight_sandstone_slab,
                  SkiesBlocks.smooth_midnight_sandstone_slab,
                  SkiesBlocks.polished_brumble_slab,
                  SkiesBlocks.polished_brumble_brick_slab,
                  SkiesBlocks.taratite_slab,
                  SkiesBlocks.polished_taratite_slab,
                  SkiesBlocks.rimestone_slab,
                  SkiesBlocks.polished_rimestone_slab,
                  SkiesBlocks.lunar_stone_slab,
                  SkiesBlocks.lunar_cobblestone_slab,
                  SkiesBlocks.mossy_lunar_cobblestone_slab,
                  SkiesBlocks.lunar_stonebrick_slab,
                  SkiesBlocks.mossy_lunar_stonebrick_slab,
                  SkiesBlocks.cracked_lunar_stonebrick_slab,
                  SkiesBlocks.crystal_sandstone_slab,
                  SkiesBlocks.smooth_crystal_sandstone_slab,
                  SkiesBlocks.cut_midnight_sandstone_slab,
                  SkiesBlocks.cut_crystal_sandstone_slab,
                  SkiesBlocks.umber_slab,
                  SkiesBlocks.polished_umber_slab,
                  SkiesBlocks.cinderstone_slab,
                  SkiesBlocks.polished_cinderstone_slab,
                  SkiesBlocks.crystallized_stairs,
                  SkiesBlocks.turquoise_stone_stairs,
                  SkiesBlocks.turquoise_stonebrick_stairs,
                  SkiesBlocks.mossy_turquoise_stonebrick_stairs,
                  SkiesBlocks.cracked_turquoise_stonebrick_stairs,
                  SkiesBlocks.turquoise_cobblestone_stairs,
                  SkiesBlocks.mossy_turquoise_cobblestone_stairs,
                  SkiesBlocks.midnight_sandstone_stairs,
                  SkiesBlocks.smooth_midnight_sandstone_stairs,
                  SkiesBlocks.polished_brumble_stairs,
                  SkiesBlocks.polished_brumble_brick_stairs,
                  SkiesBlocks.taratite_stairs,
                  SkiesBlocks.polished_taratite_stairs,
                  SkiesBlocks.rimestone_stairs,
                  SkiesBlocks.polished_rimestone_stairs,
                  SkiesBlocks.lunar_stone_stairs,
                  SkiesBlocks.lunar_stonebrick_stairs,
                  SkiesBlocks.mossy_lunar_stonebrick_stairs,
                  SkiesBlocks.cracked_lunar_stonebrick_stairs,
                  SkiesBlocks.lunar_cobblestone_stairs,
                  SkiesBlocks.mossy_lunar_cobblestone_stairs,
                  SkiesBlocks.crystal_sandstone_stairs,
                  SkiesBlocks.smooth_crystal_sandstone_stairs,
                  SkiesBlocks.umber_stairs,
                  SkiesBlocks.polished_umber_stairs,
                  SkiesBlocks.cinderstone_stairs,
                  SkiesBlocks.polished_cinderstone_stairs,
                  SkiesBlocks.star_emitter
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(new Block[]{SkiesBlocks.brumble_block, SkiesBlocks.brumble_slab, SkiesBlocks.brumble_stairs, SkiesBlocks.brumble_wall});
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{
                  SkiesBlocks.blinding_stone,
                  SkiesBlocks.glowing_blinding_stone,
                  SkiesBlocks.carved_blinding_stone,
                  SkiesBlocks.blinding_stone_pillar,
                  SkiesBlocks.blinding_stairs,
                  SkiesBlocks.glowing_blinding_stairs,
                  SkiesBlocks.blinding_slab,
                  SkiesBlocks.glowing_blinding_slab,
                  SkiesBlocks.blinding_wall,
                  SkiesBlocks.glowing_blinding_wall
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{
                  SkiesBlocks.nature_stone,
                  SkiesBlocks.glowing_nature_stone,
                  SkiesBlocks.nature_stonebrick,
                  SkiesBlocks.glowing_nature_stonebrick,
                  SkiesBlocks.carved_nature_stonebrick,
                  SkiesBlocks.nature_stone_pillar,
                  SkiesBlocks.nature_stone_stairs,
                  SkiesBlocks.glowing_nature_stone_stairs,
                  SkiesBlocks.nature_stonebrick_stairs,
                  SkiesBlocks.glowing_nature_stonebrick_stairs,
                  SkiesBlocks.nature_stone_slab,
                  SkiesBlocks.glowing_nature_stone_slab,
                  SkiesBlocks.nature_stonebrick_slab,
                  SkiesBlocks.glowing_nature_stonebrick_slab,
                  SkiesBlocks.nature_stonebrick_wall,
                  SkiesBlocks.glowing_nature_stonebrick_wall
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{
                  SkiesBlocks.poison_stone,
                  SkiesBlocks.glowing_poison_stone,
                  SkiesBlocks.poison_stonebrick,
                  SkiesBlocks.rough_poison_stonebrick,
                  SkiesBlocks.carved_poison_stonebrick,
                  SkiesBlocks.spider_nest,
                  SkiesBlocks.poison_stone_pillar,
                  SkiesBlocks.poison_stone_stairs,
                  SkiesBlocks.glowing_poison_stone_stairs,
                  SkiesBlocks.poison_stonebrick_stairs,
                  SkiesBlocks.rough_poison_stonebrick_stairs,
                  SkiesBlocks.poison_stone_slab,
                  SkiesBlocks.glowing_poison_stone_slab,
                  SkiesBlocks.poison_stonebrick_slab,
                  SkiesBlocks.rough_poison_stonebrick_slab,
                  SkiesBlocks.poison_stonebrick_wall
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{
                  SkiesBlocks.aquatic_stone,
                  SkiesBlocks.glowing_aquatic_stone,
                  SkiesBlocks.aquatic_stairs,
                  SkiesBlocks.glowing_aquatic_stairs,
                  SkiesBlocks.aquatic_slab,
                  SkiesBlocks.glowing_aquatic_slab,
                  SkiesBlocks.moonstone_lantern
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{
                  SkiesBlocks.life_stone,
                  SkiesBlocks.glowing_life_stone,
                  SkiesBlocks.life_stairs,
                  SkiesBlocks.glowing_life_stairs,
                  SkiesBlocks.life_slab,
                  SkiesBlocks.glowing_life_slab,
                  SkiesBlocks.life_wall,
                  SkiesBlocks.glowing_life_wall
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
               new Block[]{SkiesBlocks.bright_blinding_keystone, SkiesBlocks.dawn_blinding_keystone, SkiesBlocks.nature_keystone, SkiesBlocks.poison_keystone}
            );
         this.tag(BlockTags.MINEABLE_WITH_AXE)
            .addTags(new TagKey[]{SkiesBlockTags.VINES, SkiesBlockTags.CRAFTING_TABLES, SkiesBlockTags.BOOKSHELVES, SkiesBlockTags.LADDERS});
         this.tag(BlockTags.MINEABLE_WITH_AXE)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_chest,
                  SkiesBlocks.starlit_chest,
                  SkiesBlocks.frostbright_chest,
                  SkiesBlocks.lunar_chest,
                  SkiesBlocks.dusk_chest,
                  SkiesBlocks.maple_chest,
                  SkiesBlocks.comet_chest
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_AXE)
            .add(
               new Block[]{
                  SkiesBlocks.brumble_vine,
                  SkiesBlocks.brumble_vine_top,
                  SkiesBlocks.snowcap_pinhead,
                  SkiesBlocks.snowcap_mushroom,
                  SkiesBlocks.snowcap_mushroom_block,
                  SkiesBlocks.snowcap_mushroom_stem,
                  SkiesBlocks.baneful_mushroom,
                  SkiesBlocks.brewberry_bush,
                  SkiesBlocks.chilled_lily_pad,
                  SkiesBlocks.moonlit_water_lily,
                  SkiesBlocks.crescent_fruit,
                  SkiesBlocks.short_lunar_grass,
                  SkiesBlocks.tall_lunar_grass,
                  SkiesBlocks.short_turquoise_grass,
                  SkiesBlocks.tall_turquoise_grass,
                  SkiesBlocks.short_comet_grass,
                  SkiesBlocks.muckweed,
                  SkiesBlocks.chillweed,
                  SkiesBlocks.brittlebush,
                  SkiesBlocks.glimmer_reed,
                  SkiesBlocks.trough,
                  SkiesBlocks.tool_box,
                  SkiesBlocks.summoning_table,
                  SkiesBlocks.alchemy_table,
                  SkiesBlocks.food_prep_table
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(
               new Block[]{
                  SkiesBlocks.turquoise_dirt,
                  SkiesBlocks.lunar_dirt,
                  SkiesBlocks.turquoise_grass_block,
                  SkiesBlocks.lunar_grass_block,
                  SkiesBlocks.turquoise_comet_grass_block,
                  SkiesBlocks.lunar_comet_grass_block,
                  SkiesBlocks.coarse_turquoise_dirt,
                  SkiesBlocks.coarse_lunar_dirt,
                  (Block)SkiesBlocks.turquoise_farmland.get(),
                  (Block)SkiesBlocks.lunar_farmland.get(),
                  SkiesBlocks.lunar_mud,
                  SkiesBlocks.midnight_sand,
                  SkiesBlocks.crystal_sand
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_HOE)
            .add(
               new Block[]{
                  SkiesBlocks.brumble_block,
                  SkiesBlocks.brumble_slab,
                  SkiesBlocks.brumble_stairs,
                  SkiesBlocks.brumble_wall,
                  SkiesBlocks.brumble_lantern,
                  SkiesBlocks.sea_moss_block,
                  SkiesBlocks.sea_moss,
                  SkiesBlocks.sea_moss_carpet,
                  SkiesBlocks.bag_of_spoils
               }
            );
         this.tag(BlockTags.MINEABLE_WITH_HOE)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_leaves,
                  SkiesBlocks.starlit_leaves,
                  SkiesBlocks.frostbright_leaves,
                  SkiesBlocks.lunar_leaves,
                  SkiesBlocks.dusk_leaves,
                  SkiesBlocks.maple_leaves,
                  SkiesBlocks.comet_leaves
               }
            );
         this.tag(BlockTags.SWORD_EFFICIENT)
            .add(
               new Block[]{
                  SkiesBlocks.chilled_lily_pad,
                  SkiesBlocks.moonlit_water_lily,
                  SkiesBlocks.brumble,
                  SkiesBlocks.brumble_vine,
                  SkiesBlocks.brumble_vine_top,
                  SkiesBlocks.sea_moss,
                  SkiesBlocks.sea_moss_carpet
               }
            );
         this.tag(BlockTags.NEEDS_STONE_TOOL)
            .add(new Block[]{SkiesBlocks.raw_ventium_block, SkiesBlocks.raw_falsite_block, SkiesBlocks.raw_horizonite_block, SkiesBlocks.raw_aquite_block})
            .addTags(
               new TagKey[]{
                  SkiesBlockTags.PYROPE_ORES,
                  SkiesBlockTags.PYROPE_BLOCKS,
                  SkiesBlockTags.AQUITE_ORES,
                  SkiesBlockTags.AQUITE_BLOCKS,
                  SkiesBlockTags.VENTIUM_ORES,
                  SkiesBlockTags.VENTIUM_BLOCKS,
                  SkiesBlockTags.FALSITE_ORES,
                  SkiesBlockTags.FALSITE_BLOCKS,
                  SkiesBlockTags.HORIZONITE_BLOCKS,
                  SkiesBlockTags.HORIZONITE_ORES
               }
            );
         this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(new Block[]{SkiesBlocks.everbright_emerald_ore, SkiesBlocks.everdawn_emerald_ore, SkiesBlocks.raw_charoite_block})
            .addTags(new TagKey[]{SkiesBlockTags.DIOPSIDE_BLOCKS, SkiesBlockTags.DIOPSIDE_ORES, SkiesBlockTags.CHAROITE_BLOCKS, SkiesBlockTags.CHAROITE_ORES});
         this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
            .add(new Block[]{SkiesBlocks.chilled_lily_pad, SkiesBlocks.moonlit_water_lily, SkiesBlocks.sea_moss_carpet});
         this.tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(SkiesBlocks.sea_moss_carpet);
         this.tag(BlockTags.SNIFFER_EGG_HATCH_BOOST).add(SkiesBlocks.sea_moss_block);
         this.tag(BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .add(
               new Block[]{
                  SkiesBlocks.turquoise_dirt,
                  SkiesBlocks.lunar_dirt,
                  SkiesBlocks.turquoise_grass_block,
                  SkiesBlocks.lunar_grass_block,
                  SkiesBlocks.turquoise_comet_grass_block,
                  SkiesBlocks.lunar_comet_grass_block,
                  SkiesBlocks.coarse_turquoise_dirt,
                  SkiesBlocks.coarse_lunar_dirt,
                  SkiesBlocks.lunar_mud,
                  SkiesBlocks.sea_moss_block
               }
            );
         this.tag(BlockTags.FALL_DAMAGE_RESETTING).add(new Block[]{SkiesBlocks.brewberry_bush, SkiesBlocks.spider_webbing});
         this.tag(BlockTags.REPLACEABLE_BY_TREES)
            .add(
               new Block[]{
                  SkiesBlocks.short_lunar_grass,
                  SkiesBlocks.tall_lunar_grass,
                  SkiesBlocks.short_turquoise_grass,
                  SkiesBlocks.tall_turquoise_grass,
                  SkiesBlocks.short_comet_grass,
                  SkiesBlocks.chillweed,
                  SkiesBlocks.brittlebush,
                  SkiesBlocks.muckweed,
                  SkiesBlocks.snowcap_pinhead,
                  SkiesBlocks.snowcap_mushroom,
                  SkiesBlocks.baneful_mushroom
               }
            );
         IntrinsicTagAppender<Block> appender = this.tag(BlockTags.REPLACEABLE);
         prov.lookupOrThrow(Registries.BLOCK)
            .filterElements(b -> b.defaultBlockState().canBeReplaced())
            .listElementIds()
            .filter(r -> r.location().getNamespace().equals("blue_skies"))
            .forEach(appender::add);
         this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).addTag(SkiesBlockTags.BOOKSHELVES);
         this.tag(BlockTags.MAINTAINS_FARMLAND)
            .add(
               new Block[]{
                  SkiesBlocks.winter_leaves,
                  SkiesBlocks.pine_fruits,
                  SkiesBlocks.cryo_roots,
                  SkiesBlocks.solnuts,
                  SkiesBlocks.scalefruits,
                  SkiesBlocks.fiery_beans
               }
            );
      }

      private void forge(Provider prov) {
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.CHESTS_WOODEN)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_chest,
                  SkiesBlocks.starlit_chest,
                  SkiesBlocks.frostbright_chest,
                  SkiesBlocks.lunar_chest,
                  SkiesBlocks.dusk_chest,
                  SkiesBlocks.maple_chest,
                  SkiesBlocks.comet_chest
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.FENCE_GATES_WOODEN)
            .add(
               new Block[]{
                  SkiesBlocks.bluebright_fence_gate,
                  SkiesBlocks.starlit_fence_gate,
                  SkiesBlocks.frostbright_fence_gate,
                  SkiesBlocks.lunar_fence_gate,
                  SkiesBlocks.dusk_fence_gate,
                  SkiesBlocks.maple_fence_gate,
                  SkiesBlocks.comet_fence_gate
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.COBBLESTONE).addTags(new TagKey[]{SkiesBlockTags.COBBLESTONES});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.STONE).addTags(new TagKey[]{SkiesBlockTags.STONES});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.STAINED_GLASS).add(new Block[]{SkiesBlocks.crystal_glass, SkiesBlocks.midnight_glass});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.GLASS_SILICA).add(new Block[]{SkiesBlocks.crystal_glass, SkiesBlocks.midnight_glass});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.STAINED_GLASS_PANES)
            .add(new Block[]{SkiesBlocks.crystal_glass_pane, SkiesBlocks.midnight_glass_pane});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.ORE_RATES_SPARSE)
            .add(new Block[]{SkiesBlocks.everdawn_moonstone_ore, SkiesBlocks.everbright_moonstone_ore});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.ORE_RATES_SINGULAR)
            .add(
               new Block[]{
                  SkiesBlocks.everdawn_emerald_ore,
                  SkiesBlocks.everbright_emerald_ore,
                  SkiesBlocks.everdawn_pyrope_ore,
                  SkiesBlocks.everbright_pyrope_ore,
                  SkiesBlocks.everdawn_diopside_ore,
                  SkiesBlocks.everbright_diopside_ore,
                  SkiesBlocks.everdawn_aquite_ore,
                  SkiesBlocks.everbright_aquite_ore,
                  SkiesBlocks.everdawn_charoite_ore,
                  SkiesBlocks.everbright_charoite_ore,
                  SkiesBlocks.horizonite_ore,
                  SkiesBlocks.falsite_ore,
                  SkiesBlocks.ventium_ore
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES)
            .addTags(
               new TagKey[]{
                  SkiesBlockTags.MOONSTONE_ORES,
                  SkiesBlockTags.PYROPE_ORES,
                  SkiesBlockTags.DIOPSIDE_ORES,
                  SkiesBlockTags.AQUITE_ORES,
                  SkiesBlockTags.CHAROITE_ORES,
                  SkiesBlockTags.HORIZONITE_ORES,
                  SkiesBlockTags.FALSITE_ORES,
                  SkiesBlockTags.VENTIUM_ORES
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES_EMERALD)
            .add(new Block[]{SkiesBlocks.everbright_emerald_ore, SkiesBlocks.everdawn_emerald_ore});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.SANDSTONE)
            .add(
               new Block[]{
                  SkiesBlocks.crystal_sandstone,
                  SkiesBlocks.chiseled_crystal_sandstone,
                  SkiesBlocks.cut_crystal_sandstone,
                  SkiesBlocks.smooth_crystal_sandstone,
                  SkiesBlocks.midnight_sandstone,
                  SkiesBlocks.chiseled_midnight_sandstone,
                  SkiesBlocks.cut_midnight_sandstone
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.STORAGE_BLOCKS)
            .addTags(
               new TagKey[]{
                  SkiesBlockTags.MOONSTONE_BLOCKS,
                  SkiesBlockTags.PYROPE_BLOCKS,
                  SkiesBlockTags.DIOPSIDE_BLOCKS,
                  SkiesBlockTags.AQUITE_BLOCKS,
                  SkiesBlockTags.RAW_AQUITE_BLOCKS,
                  SkiesBlockTags.CHAROITE_BLOCKS,
                  SkiesBlockTags.RAW_CHAROITE_BLOCKS,
                  SkiesBlockTags.HORIZONITE_BLOCKS,
                  SkiesBlockTags.RAW_HORIZONITE_BLOCKS,
                  SkiesBlockTags.FALSITE_BLOCKS,
                  SkiesBlockTags.RAW_FALSITE_BLOCKS,
                  SkiesBlockTags.VENTIUM_BLOCKS,
                  SkiesBlockTags.RAW_VENTIUM_BLOCKS
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.SAND).add(new Block[]{SkiesBlocks.crystal_sand, SkiesBlocks.midnight_sand});
         this.tag(net.neoforged.neoforge.common.Tags.Blocks.BOOKSHELVES).addTag(SkiesBlockTags.BOOKSHELVES);
      }

      public String getName() {
         return "Blue Skies Block Tags";
      }

      private Stream<Block> getMatching(Function<Block, Boolean> condition) {
         return BuiltInRegistries.BLOCK
            .stream()
            .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("blue_skies") && condition.apply(block));
      }

      private void addMatching(TagKey<Block> blockTag, Function<Block, Boolean> condition) {
         this.getMatching(condition).forEach(this.tag(blockTag)::add);
      }
   }

   public static class DamageTypeProv extends TagsProvider<DamageType> {
      public DamageTypeProv(PackOutput packOutput, CompletableFuture<Provider> lookup, ExistingFileHelper existingFileHelper) {
         super(packOutput, Registries.DAMAGE_TYPE, lookup, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider lookup) {
         this.tag(DamageTypeTags.IS_PROJECTILE)
            .add(
               new ResourceKey[]{
                  SkiesDamageTypes.FALLING_SPIKE.getKey(), SkiesDamageTypes.FALLING_SPIKE_INDIRECT.getKey(), SkiesDamageTypes.VENOM_SPIT_INDIRECT.getKey()
               }
            );
         this.tag(DamageTypeTags.BYPASSES_ARMOR)
            .add(
               new ResourceKey[]{
                  SkiesDamageTypes.STRANGE_LIGHTNING.getKey(),
                  SkiesDamageTypes.VENOM_SPIT.getKey(),
                  SkiesDamageTypes.FALLING_SPIKE.getKey(),
                  SkiesDamageTypes.SLAM.getKey()
               }
            );
         this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(SkiesDamageTypes.VENOM_SPIT.getKey());
         this.tag(DamageTypeTags.DAMAGES_HELMET)
            .add(new ResourceKey[]{SkiesDamageTypes.FALLING_SPIKE.getKey(), SkiesDamageTypes.FALLING_SPIKE_INDIRECT.getKey()});
         this.tag(DamageTypeTags.IS_LIGHTNING).add(SkiesDamageTypes.STRANGE_LIGHTNING.getKey());
         this.tag(DamageTypeTags.IS_FIRE).add(SkiesDamageTypes.EMBER.getKey());
         this.tag(DamageTypeTags.BYPASSES_SHIELD)
            .add(
               new ResourceKey[]{
                  SkiesDamageTypes.FLUCTUANT_SPHERE.getKey(),
                  SkiesDamageTypes.SLAM.getKey(),
                  SkiesDamageTypes.CRUSHER_ROOT.getKey(),
                  SkiesDamageTypes.FALLING_SPIKE.getKey(),
                  SkiesDamageTypes.FALLING_SPIKE_INDIRECT.getKey(),
                  SkiesDamageTypes.STRANGE_LIGHTNING.getKey()
               }
            );
      }
   }

   public static class EntityTagProv extends EntityTypeTagsProvider {
      public EntityTagProv(DataGenerator generatorIn, ExistingFileHelper existingFileHelper, CompletableFuture<Provider> lookup) {
         super(generatorIn.getPackOutput(), lookup, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider prov) {
         this.blueSkies();
         this.vanilla();
         this.forge();
      }

      public CompletableFuture<?> run(CachedOutput cache) {
         SkiesEntityTags.init();
         return super.run(cache);
      }

      private void blueSkies() {
         this.tag(SkiesEntityTags.FROST_SPIRITS)
            .add(new EntityType[]{SkiesEntityTypes.FROST_SPIRIT, SkiesEntityTypes.ARMORED_FROST_SPIRIT, SkiesEntityTypes.CRYNOCEROUS});
         this.tag(SkiesEntityTags.BOSSES)
            .add(new EntityType[]{SkiesEntityTypes.SUMMONER, SkiesEntityTypes.ALCHEMIST, SkiesEntityTypes.STARLIT_CRUSHER, SkiesEntityTypes.ARACHNARCH});
         this.tag(SkiesEntityTags.NATURE_ALLIES)
            .add(
               new EntityType[]{
                  SkiesEntityTypes.STARLIT_CRUSHER,
                  SkiesEntityTypes.SPEWTER,
                  SkiesEntityTypes.SNOW_OWL,
                  SkiesEntityTypes.ENT_WALL,
                  SkiesEntityTypes.ENT_ROOT,
                  SkiesEntityTypes.ENT_LEAF,
                  SkiesEntityTypes.STONELET
               }
            );
         this.tag(SkiesEntityTags.POISON_ALLIES)
            .add(
               new EntityType[]{
                  SkiesEntityTypes.ARACHNARCH,
                  SkiesEntityTypes.NESTED_SPIDER,
                  SkiesEntityTypes.VENOM_SPIT,
                  SkiesEntityTypes.VENOM_SPIDER,
                  EntityType.SPIDER,
                  EntityType.CAVE_SPIDER
               }
            );
      }

      private void vanilla() {
         this.tag(EntityTypeTags.RAIDERS).add(new EntityType[]{SkiesEntityTypes.SUMMONER, SkiesEntityTypes.ALCHEMIST});
         this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(new EntityType[]{SkiesEntityTypes.SPEAR, SkiesEntityTypes.VENOM_SPIT, SkiesEntityTypes.ENT_LEAF});
         this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
            .add(new EntityType[]{SkiesEntityTypes.POLARGEIST, SkiesEntityTypes.SNOW_OWL})
            .addTags(new TagKey[]{SkiesEntityTags.BOSSES, SkiesEntityTags.FROST_SPIRITS});
         this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(new EntityType[]{SkiesEntityTypes.EMBERBACK, SkiesEntityTypes.SPEWTER});
         this.tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
            .add(
               new EntityType[]{
                  SkiesEntityTypes.MUNICIPAL_MONKFISH, SkiesEntityTypes.GRITTLE_FLATFISH, SkiesEntityTypes.HORIZOFIN_TUNID, SkiesEntityTypes.CHARSCALE_MOKI
               }
            );
         this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
            .add(new EntityType[]{SkiesEntityTypes.SNOW_OWL, SkiesEntityTypes.SLIV, SkiesEntityTypes.COSMIC_FOX});
      }

      private void forge() {
      }

      public String getName() {
         return "Blue Skies EntityType Tags";
      }
   }

   public static class ItemTagProv extends ItemTagsProvider {
      public ItemTagProv(
         DataGenerator gen, CompletableFuture<TagLookup<Block>> blockTagProv, ExistingFileHelper existingFileHelper, CompletableFuture<Provider> lookup
      ) {
         super(gen.getPackOutput(), lookup, blockTagProv, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider prov) {
         this.blueSkies();
         this.vanilla();
         this.forge();
      }

      public CompletableFuture<?> run(CachedOutput cache) {
         SkiesItemTags.init();
         return super.run(cache);
      }

      private void blueSkies() {
         this.copy(SkiesBlockTags.BOOKSHELVES, SkiesItemTags.BOOKSHELVES);
         this.copy(SkiesBlockTags.CRAFTING_TABLES, SkiesItemTags.CRAFTING_TABLES);
         this.copy(SkiesBlockTags.LADDERS, SkiesItemTags.LADDERS);
         this.copy(SkiesBlockTags.VINES, SkiesItemTags.VINES);
         this.copy(SkiesBlockTags.BLUEBRIGHT_LOGS, SkiesItemTags.BLUEBRIGHT_LOGS);
         this.copy(SkiesBlockTags.STARLIT_LOGS, SkiesItemTags.STARLIT_LOGS);
         this.copy(SkiesBlockTags.FROSTBRIGHT_LOGS, SkiesItemTags.FROSTBRIGHT_LOGS);
         this.copy(SkiesBlockTags.LUNAR_LOGS, SkiesItemTags.LUNAR_LOGS);
         this.copy(SkiesBlockTags.DUSK_LOGS, SkiesItemTags.DUSK_LOGS);
         this.copy(SkiesBlockTags.MAPLE_LOGS, SkiesItemTags.MAPLE_LOGS);
         this.copy(SkiesBlockTags.COMET_LOGS, SkiesItemTags.COMET_LOGS);
         this.copy(SkiesBlockTags.CRYSTALLIZED_LOGS, SkiesItemTags.CRYSTALLIZED_LOGS);
         this.copy(SkiesBlockTags.COBBLESTONES, SkiesItemTags.COBBLESTONES);
         this.copy(SkiesBlockTags.STONES, SkiesItemTags.STONES);
         this.copy(SkiesBlockTags.BLINDING_STONES, SkiesItemTags.BLINDING_STONES);
         this.copy(SkiesBlockTags.POISON_STONES, SkiesItemTags.POISON_STONES);
         this.copy(SkiesBlockTags.NATURE_STONES, SkiesItemTags.NATURE_STONES);
         this.copy(SkiesBlockTags.AQUATIC_STONES, SkiesItemTags.AQUATIC_STONES);
         this.copy(SkiesBlockTags.LIFE_STONES, SkiesItemTags.LIFE_STONES);
         this.copy(SkiesBlockTags.MOONSTONE_ORES, SkiesItemTags.MOONSTONE_ORES);
         this.copy(SkiesBlockTags.PYROPE_ORES, SkiesItemTags.PYROPE_ORES);
         this.copy(SkiesBlockTags.DIOPSIDE_ORES, SkiesItemTags.DIOPSIDE_ORES);
         this.copy(SkiesBlockTags.AQUITE_ORES, SkiesItemTags.AQUITE_ORES);
         this.copy(SkiesBlockTags.CHAROITE_ORES, SkiesItemTags.CHAROITE_ORES);
         this.copy(SkiesBlockTags.HORIZONITE_ORES, SkiesItemTags.HORIZONITE_ORES);
         this.copy(SkiesBlockTags.FALSITE_ORES, SkiesItemTags.FALSITE_ORES);
         this.copy(SkiesBlockTags.VENTIUM_ORES, SkiesItemTags.VENTIUM_ORES);
         this.copy(SkiesBlockTags.MOONSTONE_BLOCKS, SkiesItemTags.MOONSTONE_BLOCKS);
         this.copy(SkiesBlockTags.PYROPE_BLOCKS, SkiesItemTags.PYROPE_BLOCKS);
         this.copy(SkiesBlockTags.DIOPSIDE_BLOCKS, SkiesItemTags.DIOPSIDE_BLOCKS);
         this.copy(SkiesBlockTags.AQUITE_BLOCKS, SkiesItemTags.AQUITE_BLOCKS);
         this.copy(SkiesBlockTags.CHAROITE_BLOCKS, SkiesItemTags.CHAROITE_BLOCKS);
         this.copy(SkiesBlockTags.HORIZONITE_BLOCKS, SkiesItemTags.HORIZONITE_BLOCKS);
         this.copy(SkiesBlockTags.FALSITE_BLOCKS, SkiesItemTags.FALSITE_BLOCKS);
         this.copy(SkiesBlockTags.VENTIUM_BLOCKS, SkiesItemTags.VENTIUM_BLOCKS);
         this.copy(SkiesBlockTags.RAW_AQUITE_BLOCKS, SkiesItemTags.RAW_AQUITE_BLOCKS);
         this.copy(SkiesBlockTags.RAW_CHAROITE_BLOCKS, SkiesItemTags.RAW_CHAROITE_BLOCKS);
         this.copy(SkiesBlockTags.RAW_HORIZONITE_BLOCKS, SkiesItemTags.RAW_HORIZONITE_BLOCKS);
         this.copy(SkiesBlockTags.RAW_FALSITE_BLOCKS, SkiesItemTags.RAW_FALSITE_BLOCKS);
         this.copy(SkiesBlockTags.RAW_VENTIUM_BLOCKS, SkiesItemTags.RAW_VENTIUM_BLOCKS);
         this.copy(SkiesBlockTags.ILLAGER_TELEPORTABLE, SkiesItemTags.ILLAGER_TELEPORTABLE);
         this.tag(SkiesItemTags.WOODEN_PICKAXES)
            .add(
               new Item[]{
                  SkiesItems.bluebright_pickaxe,
                  SkiesItems.starlit_pickaxe,
                  SkiesItems.frostbright_pickaxe,
                  SkiesItems.lunar_pickaxe,
                  SkiesItems.dusk_pickaxe,
                  SkiesItems.maple_pickaxe,
                  SkiesItems.comet_pickaxe
               }
            );
         this.tag(SkiesItemTags.WOODEN_SHOVELS)
            .add(
               new Item[]{
                  SkiesItems.bluebright_shovel,
                  SkiesItems.starlit_shovel,
                  SkiesItems.frostbright_shovel,
                  SkiesItems.lunar_shovel,
                  SkiesItems.dusk_shovel,
                  SkiesItems.maple_shovel,
                  SkiesItems.comet_shovel
               }
            );
         this.tag(SkiesItemTags.WOODEN_AXES)
            .add(
               new Item[]{
                  SkiesItems.bluebright_axe,
                  SkiesItems.starlit_axe,
                  SkiesItems.frostbright_axe,
                  SkiesItems.lunar_axe,
                  SkiesItems.dusk_axe,
                  SkiesItems.maple_axe,
                  SkiesItems.comet_axe
               }
            );
         this.tag(SkiesItemTags.WOODEN_HOES)
            .add(
               new Item[]{
                  SkiesItems.bluebright_hoe,
                  SkiesItems.starlit_hoe,
                  SkiesItems.frostbright_hoe,
                  SkiesItems.lunar_hoe,
                  SkiesItems.dusk_hoe,
                  SkiesItems.maple_hoe,
                  SkiesItems.comet_hoe
               }
            );
         this.tag(SkiesItemTags.WOODEN_SWORDS)
            .add(
               new Item[]{
                  SkiesItems.bluebright_sword,
                  SkiesItems.starlit_sword,
                  SkiesItems.frostbright_sword,
                  SkiesItems.lunar_sword,
                  SkiesItems.dusk_sword,
                  SkiesItems.maple_sword,
                  SkiesItems.comet_sword
               }
            );
         this.tag(SkiesItemTags.WOODEN_TOOLS)
            .addTags(
               new TagKey[]{
                  SkiesItemTags.WOODEN_PICKAXES,
                  SkiesItemTags.WOODEN_SHOVELS,
                  SkiesItemTags.WOODEN_AXES,
                  SkiesItemTags.WOODEN_HOES,
                  SkiesItemTags.WOODEN_SWORDS
               }
            );
         this.tag(SkiesItemTags.HORIZONITE_TOOLS)
            .add(
               new Item[]{
                  SkiesItems.horizonite_pickaxe,
                  SkiesItems.horizonite_axe,
                  SkiesItems.horizonite_shovel,
                  SkiesItems.horizonite_hoe,
                  SkiesItems.horizonite_sword
               }
            );
         this.tag(SkiesItemTags.PICKAXES)
            .add(
               new Item[]{
                  SkiesItems.pyrope_pickaxe, SkiesItems.diopside_pickaxe, SkiesItems.aquite_pickaxe, SkiesItems.charoite_pickaxe, SkiesItems.horizonite_pickaxe
               }
            )
            .addTags(new TagKey[]{SkiesItemTags.WOODEN_PICKAXES});
         this.tag(SkiesItemTags.AXES)
            .add(new Item[]{SkiesItems.pyrope_axe, SkiesItems.diopside_axe, SkiesItems.aquite_axe, SkiesItems.charoite_axe, SkiesItems.horizonite_axe})
            .addTags(new TagKey[]{SkiesItemTags.WOODEN_AXES});
         this.tag(SkiesItemTags.SHOVELS)
            .add(
               new Item[]{
                  SkiesItems.pyrope_shovel, SkiesItems.diopside_shovel, SkiesItems.aquite_shovel, SkiesItems.charoite_shovel, SkiesItems.horizonite_shovel
               }
            )
            .addTags(new TagKey[]{SkiesItemTags.WOODEN_SHOVELS});
         this.tag(SkiesItemTags.HOES)
            .add(new Item[]{SkiesItems.pyrope_hoe, SkiesItems.diopside_hoe, SkiesItems.aquite_hoe, SkiesItems.charoite_hoe, SkiesItems.horizonite_hoe})
            .addTags(new TagKey[]{SkiesItemTags.WOODEN_HOES});
         this.tag(SkiesItemTags.SWORDS)
            .add(
               new Item[]{
                  SkiesItems.turquoise_stone_sword,
                  SkiesItems.lunar_stone_sword,
                  SkiesItems.pyrope_sword,
                  SkiesItems.diopside_sword,
                  SkiesItems.aquite_sword,
                  SkiesItems.charoite_sword,
                  SkiesItems.horizonite_sword,
                  SkiesItems.infused_arc_sword,
                  SkiesItems.different_sword
               }
            )
            .addTags(new TagKey[]{SkiesItemTags.WOODEN_SWORDS});
         this.tag(SkiesItemTags.TOOLS)
            .addTags(new TagKey[]{SkiesItemTags.PICKAXES, SkiesItemTags.AXES, SkiesItemTags.SHOVELS, SkiesItemTags.HOES, SkiesItemTags.SWORDS});
         this.tag(SkiesItemTags.MOONSTONE).add(SkiesBlocks.moonstone.asItem());
         this.tag(SkiesItemTags.MOONSTONE_SHARD).add(SkiesItems.moonstone_shard);
         this.tag(SkiesItemTags.PYROPE).add(SkiesItems.pyrope_gem);
         this.tag(SkiesItemTags.AQUITE).add(SkiesItems.aquite);
         this.tag(SkiesItemTags.DIOPSIDE).add(SkiesItems.diopside_gem);
         this.tag(SkiesItemTags.CHAROITE).add(SkiesItems.charoite);
         this.tag(SkiesItemTags.HORIZONITE).add(SkiesItems.horizonite_ingot);
         this.tag(SkiesItemTags.FALSITE).add(SkiesItems.falsite_ingot);
         this.tag(SkiesItemTags.VENTIUM).add(SkiesItems.ventium_ingot);
         this.tag(SkiesItemTags.HORIZONITE_NUGGETS).add(SkiesItems.horizonite_nugget);
         this.tag(SkiesItemTags.FALSITE_NUGGETS).add(SkiesItems.falsite_nugget);
         this.tag(SkiesItemTags.VENTIUM_NUGGETS).add(SkiesItems.ventium_nugget);
         this.tag(SkiesItemTags.RAW_MATERIALS_AQUITE).add(SkiesItems.raw_aquite);
         this.tag(SkiesItemTags.RAW_MATERIALS_CHAROITE).add(SkiesItems.raw_charoite);
         this.tag(SkiesItemTags.RAW_MATERIALS_HORIZONITE).add(SkiesItems.raw_horizonite);
         this.tag(SkiesItemTags.RAW_MATERIALS_FALSITE).add(SkiesItems.raw_falsite);
         this.tag(SkiesItemTags.RAW_MATERIALS_VENTIUM).add(SkiesItems.raw_ventium);
         this.tag(SkiesItemTags.DUNGEON_KEYS).add(new Item[]{SkiesItems.blinding_key, SkiesItems.nature_key, SkiesItems.poison_key});
         this.tag(SkiesItemTags.ARCS)
            .add(
               new Item[]{
                  SkiesItems.ethereal_arc,
                  SkiesItems.dusk_arc,
                  SkiesItems.nature_arc,
                  SkiesItems.poison_arc,
                  SkiesItems.aquatic_arc,
                  SkiesItems.life_arc,
                  SkiesItems.runic_arc
               }
            );
         this.tag(SkiesItemTags.COMET_GRASS_DROPS);
         this.tag(SkiesItemTags.TURQUOISE_GRASS_DROPS).add(new Item[]{SkiesItems.pine_fruit_seeds, SkiesItems.winter_leaf_seeds});
         this.tag(SkiesItemTags.LUNAR_GRASS_DROPS).add(new Item[]{SkiesItems.scalefruit_seeds, SkiesItems.fiery_bean_seeds});
         this.tag(SkiesItemTags.TROUGH_FOODS).add(new Item[]{Items.WHEAT, Items.CARROT, SkiesItems.pine_fruit, SkiesItems.scalefruit});
      }

      private void vanilla() {
         this.copy(BlockTags.LEAVES, ItemTags.LEAVES);
         this.copy(BlockTags.LOGS, ItemTags.LOGS);
         this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
         this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
         this.copy(BlockTags.SLABS, ItemTags.SLABS);
         this.copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
         this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
         this.copy(BlockTags.WALLS, ItemTags.WALLS);
         this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
         this.copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
         this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
         this.copy(BlockTags.DOORS, ItemTags.DOORS);
         this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
         this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
         this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
         this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
         this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
         this.copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
         this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
         this.copy(BlockTags.SAND, ItemTags.SAND);
         this.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
         this.copy(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS);
         this.copy(BlockTags.EMERALD_ORES, ItemTags.EMERALD_ORES);
         this.tag(ItemTags.PIGLIN_REPELLENTS).add(new Item[]{SkiesItems.warding_pearl, SkiesItems.soul_fragment});
         this.tag(ItemTags.FISHES)
            .add(
               new Item[]{
                  SkiesItems.grittle_flatfish,
                  SkiesItems.cooked_grittle_flatfish,
                  SkiesItems.municipal_monkfish,
                  SkiesItems.cooked_municipal_monkfish,
                  SkiesItems.charscale_moki,
                  SkiesItems.cooked_charscale_moki,
                  SkiesItems.horizofin_tunid,
                  SkiesItems.cooked_horizofin_tunid
               }
            );
         this.tag(ItemTags.BEACON_PAYMENT_ITEMS)
            .add(
               new Item[]{
                  SkiesItems.pyrope_gem,
                  SkiesItems.aquite,
                  SkiesItems.diopside_gem,
                  SkiesItems.charoite,
                  SkiesItems.falsite_ingot,
                  SkiesItems.ventium_ingot,
                  SkiesItems.horizonite_ingot
               }
            );
         this.addMatching(ItemTags.MUSIC_DISCS, i -> i instanceof RecordItem);
         this.tag(ItemTags.SWORDS).addTag(SkiesItemTags.SWORDS);
         this.tag(ItemTags.PICKAXES).addTag(SkiesItemTags.PICKAXES);
         this.tag(ItemTags.AXES).addTag(SkiesItemTags.AXES);
         this.tag(ItemTags.SHOVELS).addTag(SkiesItemTags.SHOVELS);
         this.tag(ItemTags.HOES).addTag(SkiesItemTags.HOES);
         this.tag(ItemTags.VILLAGER_PLANTABLE_SEEDS)
            .add(
               new Item[]{
                  SkiesItems.pine_fruit_seeds,
                  SkiesItems.winter_leaf_seeds,
                  SkiesItems.scalefruit_seeds,
                  SkiesItems.fiery_bean_seeds,
                  SkiesItems.cryo_root,
                  SkiesItems.solnut
               }
            );
         this.tag(ItemTags.TRIMMABLE_ARMOR)
            .add(
               new Item[]{
                  SkiesItems.pyrope_helmet,
                  SkiesItems.aquite_helmet,
                  SkiesItems.horizonite_helmet,
                  SkiesItems.charoite_helmet,
                  SkiesItems.diopside_helmet,
                  SkiesItems.shadow_helmet
               }
            );
         this.tag(ItemTags.TRIMMABLE_ARMOR)
            .add(
               new Item[]{
                  SkiesItems.pyrope_chestplate,
                  SkiesItems.aquite_chestplate,
                  SkiesItems.horizonite_chestplate,
                  SkiesItems.charoite_chestplate,
                  SkiesItems.diopside_chestplate,
                  SkiesItems.shadow_chestplate
               }
            );
         this.tag(ItemTags.TRIMMABLE_ARMOR)
            .add(
               new Item[]{
                  SkiesItems.pyrope_leggings,
                  SkiesItems.aquite_leggings,
                  SkiesItems.horizonite_leggings,
                  SkiesItems.charoite_leggings,
                  SkiesItems.diopside_leggings,
                  SkiesItems.shadow_leggings
               }
            );
         this.tag(ItemTags.TRIMMABLE_ARMOR)
            .add(
               new Item[]{
                  SkiesItems.pyrope_boots,
                  SkiesItems.aquite_boots,
                  SkiesItems.horizonite_boots,
                  SkiesItems.charoite_boots,
                  SkiesItems.diopside_boots,
                  SkiesItems.shadow_boots
               }
            );
         this.tag(ItemTags.TRIM_MATERIALS)
            .add(
               new Item[]{
                  SkiesBlocks.moonstone_crystal.asItem(),
                  SkiesBlocks.sunstone_crystal.asItem(),
                  SkiesItems.falsite_ingot,
                  SkiesItems.ventium_ingot,
                  SkiesItems.horizonite_ingot,
                  SkiesItems.pyrope_gem,
                  SkiesItems.aquite,
                  SkiesItems.diopside_gem,
                  SkiesItems.charoite
               }
            );
         this.tag(ItemTags.TRIM_TEMPLATES).add(new Item[]{SkiesItems.banished_trim, SkiesItems.thwarted_trim, SkiesItems.regrowth_trim, SkiesItems.toxic_trim});
         this.tag(ItemTags.BOOKSHELF_BOOKS).add(SkiesItems.blue_journal);
      }

      private void forge() {
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.CHESTS_WOODEN, net.neoforged.neoforge.common.Tags.Items.CHESTS_WOODEN);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.COBBLESTONE, net.neoforged.neoforge.common.Tags.Items.COBBLESTONE);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.STONE, net.neoforged.neoforge.common.Tags.Items.STONE);
         this.copy(SkiesBlockTags.BOOKSHELVES, net.neoforged.neoforge.common.Tags.Items.BOOKSHELVES);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.SAND, net.neoforged.neoforge.common.Tags.Items.SAND);
         this.tag(net.neoforged.neoforge.common.Tags.Items.RODS_WOODEN)
            .add(
               new Item[]{
                  SkiesItems.bluebright_stick,
                  SkiesItems.starlit_stick,
                  SkiesItems.frostbright_stick,
                  SkiesItems.lunar_stick,
                  SkiesItems.dusk_stick,
                  SkiesItems.maple_stick,
                  SkiesItems.comet_stick
               }
            );
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.STAINED_GLASS, net.neoforged.neoforge.common.Tags.Items.STAINED_GLASS);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.GLASS_SILICA, net.neoforged.neoforge.common.Tags.Items.GLASS_SILICA);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.STAINED_GLASS_PANES, net.neoforged.neoforge.common.Tags.Items.STAINED_GLASS_PANES);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.ORES, net.neoforged.neoforge.common.Tags.Items.ORES);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.ORES_EMERALD, net.neoforged.neoforge.common.Tags.Items.ORES_EMERALD);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.SANDSTONE, net.neoforged.neoforge.common.Tags.Items.SANDSTONE);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.STORAGE_BLOCKS, net.neoforged.neoforge.common.Tags.Items.STORAGE_BLOCKS);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.ORE_RATES_SPARSE, net.neoforged.neoforge.common.Tags.Items.ORE_RATES_SPARSE);
         this.copy(net.neoforged.neoforge.common.Tags.Blocks.ORE_RATES_SINGULAR, net.neoforged.neoforge.common.Tags.Items.ORE_RATES_SINGULAR);
         this.tag(net.neoforged.neoforge.common.Tags.Items.MUSHROOMS)
            .add(new Item[]{SkiesBlocks.snowcap_pinhead.asItem(), SkiesBlocks.snowcap_mushroom.asItem(), SkiesBlocks.baneful_mushroom.asItem()});
         this.tag(net.neoforged.neoforge.common.Tags.Items.CROPS)
            .add(
               new Item[]{
                  SkiesItems.pine_fruit, SkiesItems.winter_leaves, SkiesItems.cryo_root, SkiesItems.scalefruit, SkiesItems.fiery_beans, SkiesItems.solnut
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Items.SEEDS)
            .add(new Item[]{SkiesItems.pine_fruit_seeds, SkiesItems.winter_leaf_seeds, SkiesItems.scalefruit_seeds, SkiesItems.fiery_bean_seeds});
         this.tag(net.neoforged.neoforge.common.Tags.Items.GEMS)
            .addTags(new TagKey[]{SkiesItemTags.PYROPE, SkiesItemTags.DIOPSIDE, SkiesItemTags.AQUITE, SkiesItemTags.CHAROITE});
         this.tag(net.neoforged.neoforge.common.Tags.Items.INGOTS)
            .addTags(new TagKey[]{SkiesItemTags.HORIZONITE, SkiesItemTags.FALSITE, SkiesItemTags.VENTIUM});
         this.tag(net.neoforged.neoforge.common.Tags.Items.NUGGETS)
            .addTags(new TagKey[]{SkiesItemTags.HORIZONITE_NUGGETS, SkiesItemTags.FALSITE_NUGGETS, SkiesItemTags.VENTIUM_NUGGETS});
         this.tag(net.neoforged.neoforge.common.Tags.Items.RAW_MATERIALS)
            .addTags(
               new TagKey[]{
                  SkiesItemTags.RAW_MATERIALS_AQUITE,
                  SkiesItemTags.RAW_MATERIALS_CHAROITE,
                  SkiesItemTags.RAW_MATERIALS_FALSITE,
                  SkiesItemTags.RAW_MATERIALS_HORIZONITE,
                  SkiesItemTags.RAW_MATERIALS_VENTIUM
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Items.ENCHANTING_FUELS).addTag(SkiesItemTags.FALSITE_NUGGETS);
         this.tag(net.neoforged.neoforge.common.Tags.Items.ARMORS_HELMETS)
            .add(
               new Item[]{
                  SkiesItems.pyrope_helmet,
                  SkiesItems.aquite_helmet,
                  SkiesItems.horizonite_helmet,
                  SkiesItems.charoite_helmet,
                  SkiesItems.diopside_helmet,
                  SkiesItems.shadow_helmet
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Items.ARMORS_CHESTPLATES)
            .add(
               new Item[]{
                  SkiesItems.pyrope_chestplate,
                  SkiesItems.aquite_chestplate,
                  SkiesItems.horizonite_chestplate,
                  SkiesItems.charoite_chestplate,
                  SkiesItems.diopside_chestplate,
                  SkiesItems.shadow_chestplate
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Items.ARMORS_LEGGINGS)
            .add(
               new Item[]{
                  SkiesItems.pyrope_leggings,
                  SkiesItems.aquite_leggings,
                  SkiesItems.horizonite_leggings,
                  SkiesItems.charoite_leggings,
                  SkiesItems.diopside_leggings,
                  SkiesItems.shadow_leggings
               }
            );
         this.tag(net.neoforged.neoforge.common.Tags.Items.ARMORS_BOOTS)
            .add(
               new Item[]{
                  SkiesItems.pyrope_boots,
                  SkiesItems.aquite_boots,
                  SkiesItems.horizonite_boots,
                  SkiesItems.charoite_boots,
                  SkiesItems.diopside_boots,
                  SkiesItems.shadow_boots
               }
            );
      }

      public String getName() {
         return "Blue Skies Item Tags";
      }

      private Stream<Item> getMatching(Function<Item, Boolean> condition) {
         return BuiltInRegistries.ITEM
            .stream()
            .filter(block -> BuiltInRegistries.ITEM.getKey(block).getNamespace().equals("blue_skies") && condition.apply(block));
      }

      private void addMatching(TagKey<Item> itemTag, Function<Item, Boolean> condition) {
         this.getMatching(condition).forEach(this.tag(itemTag)::add);
      }
   }

   public static class PoiTagProv extends PoiTypeTagsProvider {
      public PoiTagProv(DataGenerator generatorIn, ExistingFileHelper existingFileHelper, CompletableFuture<Provider> lookup) {
         super(generatorIn.getPackOutput(), lookup, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider prov) {
         this.blueSkies();
         this.vanilla();
         this.forge();
      }

      public CompletableFuture<?> run(CachedOutput cache) {
         SkiesPoiTags.init();
         return super.run(cache);
      }

      private void blueSkies() {
      }

      private void vanilla() {
         this.add(
            PoiTypeTags.ACQUIRABLE_JOB_SITE,
            SkiesPoiTypes.STAR_EMITTER,
            SkiesPoiTypes.TROUGH,
            SkiesPoiTypes.TOOL_BOX,
            SkiesPoiTypes.BAG_OF_SPOILS,
            SkiesPoiTypes.SUMMONING_TABLE,
            SkiesPoiTypes.ALCHEMY_TABLE
         );
      }

      private void add(TagKey<PoiType> tag, Static<PoiType>... pois) {
         this.tag(tag).add(List.of(pois).stream().map(r -> r.getKey()).toArray(ResourceKey[]::new));
      }

      private void forge() {
      }

      public String getName() {
         return "Blue Skies PoiType Tags";
      }
   }

   public static class StructureTagProv extends StructureTagsProvider {
      public StructureTagProv(DataGenerator generatorIn, ExistingFileHelper existingFileHelper, CompletableFuture<Provider> lookup) {
         super(generatorIn.getPackOutput(), lookup, "blue_skies", existingFileHelper);
      }

      protected void addTags(Provider prov) {
         this.blueSkies();
         this.vanilla();
         this.forge();
      }

      public CompletableFuture<?> run(CachedOutput cache) {
         SkiesEntityTags.init();
         return super.run(cache);
      }

      private void blueSkies() {
         this.allConfigured(SkiesStructureTags.ON_BLINDING_MAPS, SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON, SkiesStructures.EVERDAWN_BLINDING_DUNGEON);
         this.allConfigured(SkiesStructureTags.ON_NATURE_MAPS, SkiesStructures.NATURE_DUNGEON);
         this.allConfigured(SkiesStructureTags.ON_POISON_MAPS, SkiesStructures.POISON_DUNGEON);
         this.allConfigured(SkiesStructureTags.ON_BUNKER_MAPS, SkiesStructures.FROZEN_BUNKER);
         this.allConfigured(SkiesStructureTags.GATEKEEPER_HOUSES, SkiesStructures.GATEKEEPER_HOUSE);
         this.allConfigured(
            SkiesStructureTags.DUNGEONS,
            SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON,
            SkiesStructures.EVERDAWN_BLINDING_DUNGEON,
            SkiesStructures.NATURE_DUNGEON,
            SkiesStructures.POISON_DUNGEON
         );
         this.tag(SkiesStructureTags.TREE_DISALLOWED)
            .addTags(new TagKey[]{SkiesStructureTags.ON_NATURE_MAPS})
            .add(
               SkiesStructures.BLUE_SKIES_VILLAGE
                  .getStructures()
                  .entrySet()
                  .stream()
                  .filter(e -> e.getKey() != SkiesVillageBiomes.SHADED_WOODLANDS.toString())
                  .map(e -> ((Pointer)e.getValue()).getKey())
                  .toArray(ResourceKey[]::new)
            );
      }

      private void vanilla() {
         this.allConfigured(StructureTags.VILLAGE, SkiesStructures.BLUE_SKIES_VILLAGE);
      }

      private void forge() {
      }

      private void allConfigured(TagKey<Structure> tagKey, StructureRegistrar<?>... registrars) {
         TagAppender<Structure> appender = this.tag(tagKey);

         for (StructureRegistrar<?> entry : registrars) {
            entry.getStructures().values().forEach(r -> appender.add(r.getKey()));
         }
      }

      public String getName() {
         return "Blue Skies Structure Tags";
      }
   }
}
