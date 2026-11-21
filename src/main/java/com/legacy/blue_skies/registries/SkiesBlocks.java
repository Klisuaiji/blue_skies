package com.legacy.blue_skies.registries;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.blocks.construction.AlchemyTableBlock;
import com.legacy.blue_skies.blocks.construction.BagOfSpoilsBlock;
import com.legacy.blue_skies.blocks.construction.FoodPrepTableBlock;
import com.legacy.blue_skies.blocks.construction.HorizoniteForgeBlock;
import com.legacy.blue_skies.blocks.construction.LitSlabBlock;
import com.legacy.blue_skies.blocks.construction.LitStairsBlock;
import com.legacy.blue_skies.blocks.construction.MoonstonePressurePlateBlock;
import com.legacy.blue_skies.blocks.construction.SkyBanefulPotBlock;
import com.legacy.blue_skies.blocks.construction.SkyBrewberryBushPotBlock;
import com.legacy.blue_skies.blocks.construction.SkyButtonBlock;
import com.legacy.blue_skies.blocks.construction.SkyCeilingHangingSignBlock;
import com.legacy.blue_skies.blocks.construction.SkyChestBlock;
import com.legacy.blue_skies.blocks.construction.SkyFlowerPotBlock;
import com.legacy.blue_skies.blocks.construction.SkyGlassBlock;
import com.legacy.blue_skies.blocks.construction.SkyGlassPaneBlock;
import com.legacy.blue_skies.blocks.construction.SkyLampBlock;
import com.legacy.blue_skies.blocks.construction.SkySnowcapPotBlock;
import com.legacy.blue_skies.blocks.construction.SkyStandingSignBlock;
import com.legacy.blue_skies.blocks.construction.SkyWallHangingSignBlock;
import com.legacy.blue_skies.blocks.construction.SkyWallSignBlock;
import com.legacy.blue_skies.blocks.construction.SkyWorkbenchBlock;
import com.legacy.blue_skies.blocks.construction.SnowcapOvenBlock;
import com.legacy.blue_skies.blocks.construction.StarEmitterBlock;
import com.legacy.blue_skies.blocks.construction.SummoningTableBlock;
import com.legacy.blue_skies.blocks.construction.ToolBoxBlock;
import com.legacy.blue_skies.blocks.construction.TranslucentBlock;
import com.legacy.blue_skies.blocks.construction.TranslucentPillarBlock;
import com.legacy.blue_skies.blocks.construction.TranslucentSlabBlock;
import com.legacy.blue_skies.blocks.construction.TranslucentStairsBlock;
import com.legacy.blue_skies.blocks.construction.TranslucentWallBlock;
import com.legacy.blue_skies.blocks.construction.TroughBlock;
import com.legacy.blue_skies.blocks.construction.WallMoonstoneBlock;
import com.legacy.blue_skies.blocks.construction.WallWardingPearlBlock;
import com.legacy.blue_skies.blocks.construction.WardingPearlBlock;
import com.legacy.blue_skies.blocks.dungeon.BlindingKeystoneBlock;
import com.legacy.blue_skies.blocks.dungeon.NatureKeystoneBlock;
import com.legacy.blue_skies.blocks.dungeon.PoisonKeystoneBlock;
import com.legacy.blue_skies.blocks.dungeon.SkyDecayingSpikeBlock;
import com.legacy.blue_skies.blocks.dungeon.SpiderNestBlock;
import com.legacy.blue_skies.blocks.misc.CrystalBlock;
import com.legacy.blue_skies.blocks.misc.SkyFireBlock;
import com.legacy.blue_skies.blocks.misc.SkyPieBlock;
import com.legacy.blue_skies.blocks.misc.SkyUpsidedownFireBlock;
import com.legacy.blue_skies.blocks.misc.SkyWebbingBlock;
import com.legacy.blue_skies.blocks.misc.StarFlareBlock;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.blocks.natural.BanefulMushroomBlock;
import com.legacy.blue_skies.blocks.natural.BrewberryBushBlock;
import com.legacy.blue_skies.blocks.natural.BrumbleBlock;
import com.legacy.blue_skies.blocks.natural.BrumbleVineBlock;
import com.legacy.blue_skies.blocks.natural.BrumbleVineTopBlock;
import com.legacy.blue_skies.blocks.natural.ChilledLilyPadBlock;
import com.legacy.blue_skies.blocks.natural.CrystalFlowerBlock;
import com.legacy.blue_skies.blocks.natural.CrystallizedGrassBlock;
import com.legacy.blue_skies.blocks.natural.FroseFlowerBlock;
import com.legacy.blue_skies.blocks.natural.FruitBlock;
import com.legacy.blue_skies.blocks.natural.FruitLeavesBlock;
import com.legacy.blue_skies.blocks.natural.GlimmerReedBlock;
import com.legacy.blue_skies.blocks.natural.HangingFruitLeavesBlock;
import com.legacy.blue_skies.blocks.natural.LargeFlowerBlock;
import com.legacy.blue_skies.blocks.natural.LargeLilyPadBlock;
import com.legacy.blue_skies.blocks.natural.MiddayBayhopBlock;
import com.legacy.blue_skies.blocks.natural.SeaMossBlock;
import com.legacy.blue_skies.blocks.natural.SeaMossCarpetBlock;
import com.legacy.blue_skies.blocks.natural.SeaMossPlantBlock;
import com.legacy.blue_skies.blocks.natural.SkyCropBlock;
import com.legacy.blue_skies.blocks.natural.SkyDirtBlock;
import com.legacy.blue_skies.blocks.natural.SkyDoubleCropBlock;
import com.legacy.blue_skies.blocks.natural.SkyDripstoneBlock;
import com.legacy.blue_skies.blocks.natural.SkyFarmlandBlock;
import com.legacy.blue_skies.blocks.natural.SkyGrassBlock;
import com.legacy.blue_skies.blocks.natural.SkyLeavesBlock;
import com.legacy.blue_skies.blocks.natural.SkyMudBlock;
import com.legacy.blue_skies.blocks.natural.SkyOreBlock;
import com.legacy.blue_skies.blocks.natural.SkySandBlock;
import com.legacy.blue_skies.blocks.natural.SkyTallGrassBlock;
import com.legacy.blue_skies.blocks.natural.SkyVineBlock;
import com.legacy.blue_skies.blocks.natural.SnowcapMushroomBlock;
import com.legacy.blue_skies.blocks.natural.SnowcapPinheadBlock;
import com.legacy.blue_skies.blocks.natural.WallCrystalBlock;
import com.legacy.blue_skies.blocks.portal.EverbrightPortalBlock;
import com.legacy.blue_skies.blocks.portal.EverdawnPortalBlock;
import com.legacy.blue_skies.blocks.portal.SkyPortalBlock;
import com.legacy.blue_skies.blocks.util.SkiesTreeGrowers;
import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.events.SkiesMappingChanges;
import com.legacy.structure_gel.api.util.GelCollectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.util.NonNullLazy;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesBlocks {
   public static Block turquoise_grass_block;
   public static Block short_turquoise_grass;
   public static Block tall_turquoise_grass;
   public static Block turquoise_dirt;
   public static Block coarse_turquoise_dirt;
   public static Block turquoise_stone;
   public static Block turquoise_cobblestone;
   public static Block turquoise_stonebrick;
   public static Block mossy_turquoise_cobblestone;
   public static Block mossy_turquoise_stonebrick;
   public static Block cracked_turquoise_stonebrick;
   public static Block chiseled_turquoise_stonebrick;
   public static Block taratite;
   public static Block polished_taratite;
   public static Block rimestone;
   public static Block polished_rimestone;
   public static Block lunar_grass_block;
   public static Block short_lunar_grass;
   public static Block tall_lunar_grass;
   public static Block lunar_dirt;
   public static Block coarse_lunar_dirt;
   public static Block lunar_mud;
   public static Block lunar_stone;
   public static Block lunar_cobblestone;
   public static Block lunar_stonebrick;
   public static Block mossy_lunar_cobblestone;
   public static Block mossy_lunar_stonebrick;
   public static Block cracked_lunar_stonebrick;
   public static Block chiseled_lunar_stonebrick;
   public static Block umber;
   public static Block polished_umber;
   public static Block cinderstone;
   public static Block polished_cinderstone;
   public static SkyDripstoneBlock lunar_dripstone;
   public static SkyDripstoneBlock turquoise_dripstone;
   public static NonNullLazy<SkyFarmlandBlock> turquoise_farmland = NonNullLazy.of(
      () -> new SkyFarmlandBlock(Properties.ofFullCopy(Blocks.FARMLAND), () -> turquoise_dirt)
   );
   public static NonNullLazy<SkyFarmlandBlock> lunar_farmland = NonNullLazy.of(
      () -> new SkyFarmlandBlock(Properties.ofFullCopy(Blocks.FARMLAND), () -> lunar_dirt)
   );
   public static Block bluebright_log;
   public static Block starlit_log;
   public static Block frostbright_log;
   public static Block lunar_log;
   public static Block dusk_log;
   public static Block maple_log;
   public static Block comet_log;
   public static Block crystallized_log;
   public static Block stripped_bluebright_log;
   public static Block stripped_starlit_log;
   public static Block stripped_frostbright_log;
   public static Block stripped_lunar_log;
   public static Block stripped_dusk_log;
   public static Block stripped_maple_log;
   public static Block stripped_comet_log;
   public static Block bluebright_wood;
   public static Block starlit_wood;
   public static Block frostbright_wood;
   public static Block lunar_wood;
   public static Block dusk_wood;
   public static Block maple_wood;
   public static Block comet_wood;
   public static Block crystallized_wood;
   public static Block stripped_bluebright_wood;
   public static Block stripped_starlit_wood;
   public static Block stripped_frostbright_wood;
   public static Block stripped_lunar_wood;
   public static Block stripped_dusk_wood;
   public static Block stripped_maple_wood;
   public static Block stripped_comet_wood;
   public static Block bluebright_planks;
   public static Block starlit_planks;
   public static Block frostbright_planks;
   public static Block lunar_planks;
   public static Block dusk_planks;
   public static Block maple_planks;
   public static Block comet_planks;
   public static Block crystallized_planks;
   public static SkyLeavesBlock bluebright_leaves;
   public static SkyLeavesBlock starlit_leaves;
   public static SkyLeavesBlock frostbright_leaves;
   public static SkyLeavesBlock lunar_leaves;
   public static SkyLeavesBlock dusk_leaves;
   public static SkyLeavesBlock maple_leaves;
   public static SkyLeavesBlock comet_leaves;
   public static SkyLeavesBlock crystallized_leaves;
   public static HangingFruitLeavesBlock crescent_fruit_leaves;
   public static FruitBlock crescent_fruit;
   public static Block bluebright_sapling;
   public static Block starlit_sapling;
   public static Block frostbright_sapling;
   public static Block lunar_sapling;
   public static Block dusk_sapling;
   public static Block maple_sapling;
   public static Block comet_sapling;
   public static Block crescent_fruit_sapling;
   public static Block camellia;
   public static Block snowbloom;
   public static Block blush_blossom;
   public static Block polar_posy;
   public static Block chillweed;
   public static Block brittlebush;
   public static Block briskbloom;
   public static Block frose;
   public static Block midday_bayhop;
   public static Block moonlit_bloom;
   public static Block crystal_flower;
   public static Block blaze_bud;
   public static Block flare_floret;
   public static Block nightcress;
   public static Block muckweed;
   public static Block lucentroot;
   public static SkyVineBlock bluebright_vine;
   public static SkyVineBlock starlit_vine;
   public static SkyVineBlock frostbright_vine;
   public static SkyVineBlock lunar_vine;
   public static SkyVineBlock dusk_vine;
   public static SkyVineBlock maple_vine;
   public static Block brewberry_bush;
   public static WaterlilyBlock chilled_lily_pad;
   public static SeaMossPlantBlock sea_moss;
   public static Block sea_moss_block;
   public static Block sea_moss_carpet;
   public static BrumbleBlock brumble;
   public static BrumbleVineBlock brumble_vine;
   public static BrumbleVineTopBlock brumble_vine_top;
   public static Block brumble_block;
   public static Block polished_brumble;
   public static Block polished_brumble_bricks;
   public static Block chiseled_polished_brumble;
   public static Block brumble_lantern;
   public static Block snowcap_pinhead;
   public static Block snowcap_mushroom;
   public static Block snowcap_mushroom_block;
   public static Block snowcap_mushroom_stem;
   public static LargeLilyPadBlock moonlit_water_lily;
   public static Block baneful_mushroom;
   public static Block glimmer_reed;
   public static Block crystallized_grass;
   public static SkyOreBlock everbright_charoite_ore;
   public static SkyOreBlock everbright_moonstone_ore;
   public static SkyOreBlock everbright_diopside_ore;
   public static SkyOreBlock everbright_pyrope_ore;
   public static SkyOreBlock everbright_aquite_ore;
   public static SkyOreBlock everbright_emerald_ore;
   public static SkyOreBlock falsite_ore;
   public static SkyOreBlock ventium_ore;
   public static SkyOreBlock everdawn_charoite_ore;
   public static SkyOreBlock everdawn_moonstone_ore;
   public static SkyOreBlock everdawn_diopside_ore;
   public static SkyOreBlock everdawn_pyrope_ore;
   public static SkyOreBlock everdawn_aquite_ore;
   public static SkyOreBlock everdawn_emerald_ore;
   public static SkyOreBlock horizonite_ore;
   public static CrystalBlock moonstone_block;
   public static CrystalBlock sunstone_block;
   public static Block charoite_block;
   public static Block diopside_block;
   public static Block pyrope_block;
   public static Block aquite_block;
   public static Block horizonite_block;
   public static Block ventium_block;
   public static Block falsite_block;
   public static Block raw_falsite_block;
   public static Block raw_ventium_block;
   public static Block raw_horizonite_block;
   public static Block raw_aquite_block;
   public static Block raw_charoite_block;
   public static Block bluebright_crafting_table;
   public static Block starlit_crafting_table;
   public static Block frostbright_crafting_table;
   public static Block lunar_crafting_table;
   public static Block dusk_crafting_table;
   public static Block maple_crafting_table;
   public static Block comet_crafting_table;
   public static Block bluebright_bookshelf;
   public static Block starlit_bookshelf;
   public static Block frostbright_bookshelf;
   public static Block lunar_bookshelf;
   public static Block dusk_bookshelf;
   public static Block maple_bookshelf;
   public static Block comet_bookshelf;
   public static Block bluebright_chest;
   public static Block starlit_chest;
   public static Block frostbright_chest;
   public static Block lunar_chest;
   public static Block dusk_chest;
   public static Block maple_chest;
   public static Block comet_chest;
   public static Block turquoise_comet_grass_block;
   public static Block lunar_comet_grass_block;
   public static Block short_comet_grass;
   public static Block crystal_sand;
   public static Block midnight_sand;
   public static Block vitreous_moonstone;
   public static Block crystal_glass;
   public static Block crystal_glass_pane;
   public static Block midnight_glass;
   public static Block midnight_glass_pane;
   public static Block comet_berry_pie;
   public static Block star_flare;
   public static Block moonstone;
   public static Block moonstone_lantern;
   public static SkyFireBlock blue_fire;
   public static SkyFireBlock black_fire;
   public static Block upsidedown_blue_fire;
   public static Block upsidedown_black_fire;
   public static Block warding_pearl;
   public static Block wall_warding_pearl;
   public static WallCrystalBlock moonstone_crystal;
   public static WallCrystalBlock sunstone_crystal;
   public static Block decaying_spike;
   public static Block spider_webbing;
   public static Block spider_nest;
   public static Block bluebright_door;
   public static Block starlit_door;
   public static Block frostbright_door;
   public static Block lunar_door;
   public static Block dusk_door;
   public static Block maple_door;
   public static Block comet_door;
   public static Block crystallized_door;
   public static Block bluebright_slab;
   public static Block starlit_slab;
   public static Block frostbright_slab;
   public static Block lunar_slab;
   public static Block dusk_slab;
   public static Block maple_slab;
   public static Block comet_slab;
   public static Block crystallized_slab;
   public static Block turquoise_stone_slab;
   public static Block turquoise_cobblestone_slab;
   public static Block lunar_stone_slab;
   public static Block lunar_cobblestone_slab;
   public static Block mossy_turquoise_cobblestone_slab;
   public static Block mossy_lunar_cobblestone_slab;
   public static Block turquoise_stonebrick_slab;
   public static Block lunar_stonebrick_slab;
   public static Block mossy_turquoise_stonebrick_slab;
   public static Block mossy_lunar_stonebrick_slab;
   public static Block cracked_turquoise_stonebrick_slab;
   public static Block cracked_lunar_stonebrick_slab;
   public static Block brumble_slab;
   public static Block polished_brumble_slab;
   public static Block polished_brumble_brick_slab;
   public static Block taratite_slab;
   public static Block polished_taratite_slab;
   public static Block rimestone_slab;
   public static Block polished_rimestone_slab;
   public static Block umber_slab;
   public static Block polished_umber_slab;
   public static Block cinderstone_slab;
   public static Block polished_cinderstone_slab;
   public static Block turquoise_cobblestone_wall;
   public static Block lunar_cobblestone_wall;
   public static Block mossy_turquoise_cobblestone_wall;
   public static Block mossy_lunar_cobblestone_wall;
   public static Block turquoise_stonebrick_wall;
   public static Block lunar_stonebrick_wall;
   public static Block mossy_turquoise_stonebrick_wall;
   public static Block mossy_lunar_stonebrick_wall;
   public static Block cracked_turquoise_stonebrick_wall;
   public static Block cracked_lunar_stonebrick_wall;
   public static Block crystallized_wall;
   public static Block brumble_wall;
   public static Block polished_brumble_wall;
   public static Block polished_brumble_brick_wall;
   public static Block taratite_wall;
   public static Block polished_taratite_wall;
   public static Block rimestone_wall;
   public static Block polished_rimestone_wall;
   public static Block umber_wall;
   public static Block polished_umber_wall;
   public static Block cinderstone_wall;
   public static Block polished_cinderstone_wall;
   public static Block bluebright_stairs;
   public static Block lunar_stairs;
   public static Block comet_stairs;
   public static Block starlit_stairs;
   public static Block dusk_stairs;
   public static Block frostbright_stairs;
   public static Block maple_stairs;
   public static Block crystallized_stairs;
   public static Block turquoise_stone_stairs;
   public static Block turquoise_cobblestone_stairs;
   public static Block lunar_stone_stairs;
   public static Block lunar_cobblestone_stairs;
   public static Block turquoise_stonebrick_stairs;
   public static Block lunar_stonebrick_stairs;
   public static Block mossy_turquoise_stonebrick_stairs;
   public static Block mossy_lunar_stonebrick_stairs;
   public static Block cracked_turquoise_stonebrick_stairs;
   public static Block cracked_lunar_stonebrick_stairs;
   public static Block crystal_sandstone;
   public static Block chiseled_crystal_sandstone;
   public static Block cut_crystal_sandstone;
   public static Block smooth_crystal_sandstone;
   public static Block crystal_sandstone_pillar;
   public static Block crystal_sandstone_slab;
   public static Block crystal_sandstone_stairs;
   public static Block smooth_crystal_sandstone_slab;
   public static Block smooth_crystal_sandstone_stairs;
   public static Block cut_crystal_sandstone_slab;
   public static Block crystal_sandstone_wall;
   public static Block midnight_sandstone;
   public static Block chiseled_midnight_sandstone;
   public static Block cut_midnight_sandstone;
   public static Block smooth_midnight_sandstone;
   public static Block midnight_sandstone_pillar;
   public static Block midnight_sandstone_slab;
   public static Block midnight_sandstone_stairs;
   public static Block smooth_midnight_sandstone_slab;
   public static Block smooth_midnight_sandstone_stairs;
   public static Block cut_midnight_sandstone_slab;
   public static Block midnight_sandstone_wall;
   public static Block mossy_turquoise_cobblestone_stairs;
   public static Block mossy_lunar_cobblestone_stairs;
   public static Block brumble_stairs;
   public static Block polished_brumble_stairs;
   public static Block polished_brumble_brick_stairs;
   public static Block taratite_stairs;
   public static Block polished_taratite_stairs;
   public static Block rimestone_stairs;
   public static Block polished_rimestone_stairs;
   public static Block umber_stairs;
   public static Block polished_umber_stairs;
   public static Block cinderstone_stairs;
   public static Block polished_cinderstone_stairs;
   public static Block bluebright_ladder;
   public static Block starlit_ladder;
   public static Block frostbright_ladder;
   public static Block lunar_ladder;
   public static Block dusk_ladder;
   public static Block maple_ladder;
   public static Block comet_ladder;
   public static Block bluebright_fence;
   public static Block starlit_fence;
   public static Block frostbright_fence;
   public static Block lunar_fence;
   public static Block dusk_fence;
   public static Block maple_fence;
   public static Block comet_fence;
   public static Block bluebright_fence_gate;
   public static Block starlit_fence_gate;
   public static Block frostbright_fence_gate;
   public static Block lunar_fence_gate;
   public static Block dusk_fence_gate;
   public static Block maple_fence_gate;
   public static Block comet_fence_gate;
   public static Block bluebright_trapdoor;
   public static Block starlit_trapdoor;
   public static Block frostbright_trapdoor;
   public static Block lunar_trapdoor;
   public static Block dusk_trapdoor;
   public static Block maple_trapdoor;
   public static Block comet_trapdoor;
   public static Block crystallized_trapdoor;
   public static Block bluebright_pressure_plate;
   public static Block starlit_pressure_plate;
   public static Block frostbright_pressure_plate;
   public static Block lunar_pressure_plate;
   public static Block dusk_pressure_plate;
   public static Block maple_pressure_plate;
   public static Block comet_pressure_plate;
   public static Block crystallized_pressure_plate;
   public static Block turquoise_stone_pressure_plate;
   public static Block lunar_stone_pressure_plate;
   public static Block moonstone_pressure_plate;
   public static Block bluebright_button;
   public static Block starlit_button;
   public static Block frostbright_button;
   public static Block lunar_button;
   public static Block dusk_button;
   public static Block maple_button;
   public static Block comet_button;
   public static Block crystallized_button;
   public static Block turquoise_stone_button;
   public static Block lunar_stone_button;
   public static Block turquoise_lever;
   public static Block lunar_lever;
   public static Block bluebright_sign;
   public static Block starlit_sign;
   public static Block frostbright_sign;
   public static Block lunar_sign;
   public static Block dusk_sign;
   public static Block maple_sign;
   public static Block comet_sign;
   public static Block bluebright_wall_sign;
   public static Block starlit_wall_sign;
   public static Block frostbright_wall_sign;
   public static Block lunar_wall_sign;
   public static Block dusk_wall_sign;
   public static Block maple_wall_sign;
   public static Block comet_wall_sign;
   public static CeilingHangingSignBlock bluebright_hanging_sign;
   public static CeilingHangingSignBlock starlit_hanging_sign;
   public static CeilingHangingSignBlock frostbright_hanging_sign;
   public static CeilingHangingSignBlock lunar_hanging_sign;
   public static CeilingHangingSignBlock dusk_hanging_sign;
   public static CeilingHangingSignBlock maple_hanging_sign;
   public static CeilingHangingSignBlock comet_hanging_sign;
   public static WallHangingSignBlock bluebright_wall_hanging_sign;
   public static WallHangingSignBlock starlit_wall_hanging_sign;
   public static WallHangingSignBlock frostbright_wall_hanging_sign;
   public static WallHangingSignBlock lunar_wall_hanging_sign;
   public static WallHangingSignBlock dusk_wall_hanging_sign;
   public static WallHangingSignBlock maple_wall_hanging_sign;
   public static WallHangingSignBlock comet_wall_hanging_sign;
   public static Block blinding_stone;
   public static Block glowing_blinding_stone;
   public static Block carved_blinding_stone;
   public static Block blinding_stone_pillar;
   public static Block blinding_slab;
   public static Block blinding_stairs;
   public static Block glowing_blinding_slab;
   public static Block glowing_blinding_stairs;
   public static Block blinding_wall;
   public static Block glowing_blinding_wall;
   public static Block nature_stone;
   public static Block glowing_nature_stone;
   public static Block nature_stonebrick;
   public static Block glowing_nature_stonebrick;
   public static Block carved_nature_stonebrick;
   public static Block nature_stone_pillar;
   public static Block nature_stone_slab;
   public static Block nature_stone_stairs;
   public static Block glowing_nature_stone_slab;
   public static Block glowing_nature_stone_stairs;
   public static Block nature_stonebrick_slab;
   public static Block nature_stonebrick_stairs;
   public static Block glowing_nature_stonebrick_slab;
   public static Block glowing_nature_stonebrick_stairs;
   public static Block nature_stonebrick_wall;
   public static Block glowing_nature_stonebrick_wall;
   public static Block poison_stone;
   public static Block glowing_poison_stone;
   public static Block poison_stonebrick;
   public static Block rough_poison_stonebrick;
   public static Block carved_poison_stonebrick;
   public static Block poison_stone_pillar;
   public static Block poison_stone_slab;
   public static Block poison_stone_stairs;
   public static Block glowing_poison_stone_slab;
   public static Block glowing_poison_stone_stairs;
   public static Block poison_stonebrick_slab;
   public static Block poison_stonebrick_stairs;
   public static Block rough_poison_stonebrick_slab;
   public static Block rough_poison_stonebrick_stairs;
   public static Block poison_stonebrick_wall;
   public static Block aquatic_stone;
   public static Block glowing_aquatic_stone;
   public static Block aquatic_slab;
   public static Block aquatic_stairs;
   public static Block glowing_aquatic_slab;
   public static Block glowing_aquatic_stairs;
   public static Block aquatic_wall;
   public static Block glowing_aquatic_wall;
   public static Block life_stone;
   public static Block glowing_life_stone;
   public static Block life_slab;
   public static Block life_stairs;
   public static Block glowing_life_slab;
   public static Block glowing_life_stairs;
   public static Block life_wall;
   public static Block glowing_life_wall;
   public static Block bright_blinding_keystone;
   public static Block dawn_blinding_keystone;
   public static Block nature_keystone;
   public static Block poison_keystone;
   public static AbstractFurnaceBlock snowcap_oven;
   public static AbstractFurnaceBlock horizonite_forge;
   public static SkyPortalBlock everdawn_portal;
   public static SkyPortalBlock everbright_portal;
   public static Block carrots;
   public static Block potatoes;
   public static Block wheat;
   public static Block beetroots;
   public static Block pumpkin_stem;
   public static Block melon_stem;
   public static Block winter_leaves;
   public static Block fiery_beans;
   public static Block solnuts;
   public static Block cryo_roots;
   public static Block scalefruits;
   public static Block pine_fruits;
   public static Block potted_camellia;
   public static Block potted_snowbloom;
   public static Block potted_briskbloom;
   public static Block potted_blush_blossom;
   public static Block potted_polar_posy;
   public static Block potted_chillweed;
   public static Block potted_brittlebush;
   public static Block potted_frose;
   public static Block potted_midday_bayhop;
   public static Block potted_blaze_bud;
   public static Block potted_moonlit_bloom;
   public static Block potted_crystal_flower;
   public static Block potted_flare_floret;
   public static Block potted_nightcress;
   public static Block potted_muckweed;
   public static Block potted_lucentroot;
   public static Block potted_snowcap_mushroom;
   public static Block potted_baneful_mushroom;
   public static Block potted_bluebright_sapling;
   public static Block potted_lunar_sapling;
   public static Block potted_starlit_sapling;
   public static Block potted_dusk_sapling;
   public static Block potted_frostbright_sapling;
   public static Block potted_maple_sapling;
   public static Block potted_crescent_fruit_sapling;
   public static Block potted_comet_sapling;
   public static Block potted_brewberry_bush;
   public static Block star_emitter;
   public static Block trough;
   public static Block tool_box;
   public static Block bag_of_spoils;
   public static Block summoning_table;
   public static Block alchemy_table;
   public static Block food_prep_table;
   public static TrophyBlock summoner_trophy;
   public static TrophyBlock alchemist_trophy;
   public static TrophyBlock starlit_crusher_trophy;
   public static TrophyBlock arachnarch_trophy;
   public static List<Block> blockItemList = new ArrayList<>();
   public static Map<Block, net.minecraft.world.item.Item.Properties> blockItemPropertiesMap = new LinkedHashMap<>();
   public static Map<Block, SkiesDungeonType> dungeonBlockItemMap = new LinkedHashMap<>();
   private static RegisterEvent registry;

   public static void init(RegisterEvent event) {
      registry = event;
      Properties plankProp = Properties.ofFullCopy(Blocks.OAK_PLANKS);
      Properties brickProp = Properties.ofFullCopy(Blocks.STONE_BRICKS);
      Properties leafProp = Properties.ofFullCopy(Blocks.OAK_LEAVES)
         .isValidSpawn(SkyLeavesBlock::allowsSpawnOnLeaves)
         .isSuffocating(SkyLeavesBlock::isntSolid)
         .isViewBlocking(SkyLeavesBlock::isntSolid);
      Properties logProp = Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD);
      Properties vineProp = Properties.ofFullCopy(Blocks.VINE);
      Properties flowerProp = Properties.ofFullCopy(Blocks.POPPY);
      Properties replacableFlowerProp = Properties.ofFullCopy(Blocks.SHORT_GRASS);
      Properties chestProp = Properties.ofFullCopy(Blocks.CHEST);
      short_turquoise_grass = register("short_turquoise_grass", new SkyTallGrassBlock(() -> (DoublePlantBlock)tall_turquoise_grass));
      tall_turquoise_grass = register("tall_turquoise_grass", new DoublePlantBlock(Properties.ofFullCopy(Blocks.TALL_GRASS)));
      turquoise_grass_block = register(
         "turquoise_grass_block",
         new SkyGrassBlock(
            new SkyGrassBlock.GrassProperties(
               () -> short_turquoise_grass,
               () -> turquoise_dirt,
               GelCollectors.mapOf((Supplier<Block>)() -> turquoise_dirt, (Supplier<Block>)() -> turquoise_grass_block, new Object[0])
            )
         )
      );
      turquoise_dirt = register("turquoise_dirt", new SkyDirtBlock());
      coarse_turquoise_dirt = register("coarse_turquoise_dirt", new SkyDirtBlock());
      register("turquoise_farmland", (SkyFarmlandBlock)turquoise_farmland.get());
      turquoise_stone = register("turquoise_stone", new Block(Properties.ofFullCopy(Blocks.STONE)));
      turquoise_cobblestone = register("turquoise_cobblestone", new Block(Properties.ofFullCopy(Blocks.COBBLESTONE)));
      mossy_turquoise_cobblestone = register("mossy_turquoise_cobblestone", new Block(Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE)));
      turquoise_stonebrick = register("turquoise_stonebrick", new Block(brickProp));
      mossy_turquoise_stonebrick = register("mossy_turquoise_stonebrick", new Block(brickProp));
      cracked_turquoise_stonebrick = register("cracked_turquoise_stonebrick", new Block(brickProp));
      chiseled_turquoise_stonebrick = register("chiseled_turquoise_stonebrick", new Block(brickProp));
      taratite = register("taratite", new Block(Properties.ofFullCopy(Blocks.GRANITE)));
      polished_taratite = register("polished_taratite", new Block(Properties.ofFullCopy(Blocks.POLISHED_GRANITE)));
      rimestone = register("rimestone", new Block(Properties.ofFullCopy(Blocks.GRANITE).sound(SkiesSounds.RIMESTONE)));
      polished_rimestone = register("polished_rimestone", new Block(Properties.ofFullCopy(Blocks.POLISHED_GRANITE).sound(SkiesSounds.RIMESTONE)));
      camellia = register("camellia", new FlowerBlock(MobEffects.MOVEMENT_SPEED, 6.0F, flowerProp));
      snowbloom = register("snowbloom", new FlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 6.0F, flowerProp));
      blush_blossom = register("blush_blossom", new FlowerBlock(MobEffects.REGENERATION, 6.0F, flowerProp));
      polar_posy = register("polar_posy", new FlowerBlock(MobEffects.LUCK, 6.0F, flowerProp));
      chillweed = register("chillweed", new LargeFlowerBlock(MobEffects.SLOW_FALLING, 6.0F, replacableFlowerProp));
      brittlebush = register("brittlebush", new LargeFlowerBlock(MobEffects.WEAKNESS, 6.0F, replacableFlowerProp));
      briskbloom = register("briskbloom", new LargeFlowerBlock(MobEffects.HUNGER, 6.0F, Properties.ofFullCopy(Blocks.POPPY).offsetType(OffsetType.XZ)));
      frose = register("frose", new FroseFlowerBlock(MobEffects.BLINDNESS, 6.0F, flowerProp));
      midday_bayhop = register("midday_bayhop", new MiddayBayhopBlock(MobEffects.CONDUIT_POWER, 6, flowerProp));
      sea_moss = register("sea_moss", new SeaMossPlantBlock(Properties.ofFullCopy(Blocks.SEAGRASS)));
      sea_moss_block = register(
         "sea_moss_block",
         new SeaMossBlock(
            Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).instrument(NoteBlockInstrument.BASEDRUM).strength(0.2F).sound(SoundType.WET_GRASS)
         )
      );
      sea_moss_carpet = register(
         "sea_moss_carpet",
         new SeaMossCarpetBlock(
            Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).instrument(NoteBlockInstrument.BASEDRUM).strength(0.1F).sound(SoundType.WET_GRASS)
         )
      );
      brumble = register("brumble", new BrumbleBlock(Properties.ofFullCopy(Blocks.SEAGRASS).offsetType(OffsetType.XYZ)));
      brumble_vine = registerBlock("brumble_vine", new BrumbleVineBlock(Properties.ofFullCopy(Blocks.KELP_PLANT)));
      brumble_vine_top = register("brumble_vine_top", new BrumbleVineTopBlock(Properties.ofFullCopy(Blocks.KELP).lightLevel(state -> 15)));
      brumble_block = register("brumble_block", new Block(Properties.ofFullCopy(Blocks.FIRE_CORAL_BLOCK)));
      polished_brumble = register("polished_brumble", new Block(Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE)));
      polished_brumble_bricks = register("polished_brumble_bricks", new Block(Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE_BRICKS)));
      chiseled_polished_brumble = register("chiseled_polished_brumble", new Block(Properties.ofFullCopy(Blocks.CHISELED_POLISHED_BLACKSTONE)));
      brumble_lantern = register("brumble_lantern", new Block(Properties.ofFullCopy(brumble_block).lightLevel(state -> 15)));
      bluebright_sapling = register("bluebright_sapling", new SaplingBlock(SkiesTreeGrowers.BLUEBRIGHT, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      starlit_sapling = register("starlit_sapling", new SaplingBlock(SkiesTreeGrowers.STARLIT, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      frostbright_sapling = register("frostbright_sapling", new SaplingBlock(SkiesTreeGrowers.FROSTBRIGHT, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      brewberry_bush = register("brewberry_bush", new BrewberryBushBlock());
      chilled_lily_pad = registerBlock("chilled_lily_pad", new ChilledLilyPadBlock());
      snowcap_pinhead = register(
         "snowcap_pinhead", new SnowcapPinheadBlock(Properties.of().pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS))
      );
      snowcap_mushroom = register(
         "snowcap_mushroom",
         new SnowcapMushroomBlock(
            Properties.of()
               .pushReaction(PushReaction.DESTROY)
               .strength(0.2F, 0.0F)
               .randomTicks()
               .sound(SoundType.GRASS)
               .dynamicShape()
               .offsetType(OffsetType.XYZ)
         )
      );
      snowcap_mushroom_block = register("snowcap_mushroom_block", new HugeMushroomBlock(Properties.ofFullCopy(Blocks.RED_MUSHROOM_BLOCK)));
      snowcap_mushroom_stem = register("snowcap_mushroom_stem", new HugeMushroomBlock(Properties.ofFullCopy(Blocks.MUSHROOM_STEM)));
      bluebright_vine = register("bluebright_vine", new SkyVineBlock(vineProp));
      bluebright_leaves = register("bluebright_leaves", new SkyLeavesBlock(leafProp));
      bluebright_log = register("bluebright_log", new RotatedPillarBlock(logProp));
      bluebright_wood = register("bluebright_wood", new RotatedPillarBlock(logProp));
      stripped_bluebright_log = register("stripped_bluebright_log", new RotatedPillarBlock(logProp));
      stripped_bluebright_wood = register("stripped_bluebright_wood", new RotatedPillarBlock(logProp));
      bluebright_planks = register("bluebright_planks", new Block(plankProp));
      starlit_vine = register("starlit_vine", new SkyVineBlock(vineProp));
      starlit_leaves = register("starlit_leaves", new SkyLeavesBlock(leafProp));
      starlit_log = register("starlit_log", new RotatedPillarBlock(logProp));
      starlit_wood = register("starlit_wood", new RotatedPillarBlock(logProp));
      stripped_starlit_log = register("stripped_starlit_log", new RotatedPillarBlock(logProp));
      stripped_starlit_wood = register("stripped_starlit_wood", new RotatedPillarBlock(logProp));
      starlit_planks = register("starlit_planks", new Block(plankProp));
      frostbright_vine = register("frostbright_vine", new SkyVineBlock(vineProp));
      frostbright_leaves = register("frostbright_leaves", new SkyLeavesBlock(leafProp));
      frostbright_log = register("frostbright_log", new RotatedPillarBlock(logProp));
      frostbright_wood = register("frostbright_wood", new RotatedPillarBlock(logProp));
      stripped_frostbright_log = register("stripped_frostbright_log", new RotatedPillarBlock(logProp));
      stripped_frostbright_wood = register("stripped_frostbright_wood", new RotatedPillarBlock(logProp));
      frostbright_planks = register("frostbright_planks", new Block(plankProp));
      bluebright_crafting_table = register("bluebright_crafting_table", new SkyWorkbenchBlock());
      starlit_crafting_table = register("starlit_crafting_table", new SkyWorkbenchBlock());
      frostbright_crafting_table = register("frostbright_crafting_table", new SkyWorkbenchBlock());
      bluebright_chest = registerBlock("bluebright_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.BLUEBRIGHT_CHEST));
      starlit_chest = registerBlock("starlit_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.STARLIT_CHEST));
      frostbright_chest = registerBlock("frostbright_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.FROSTBRIGHT_CHEST));
      midnight_sand = register("midnight_sand", new SkySandBlock(3553084, Properties.ofFullCopy(Blocks.SAND), rand -> midday_bayhop));
      midnight_sandstone = register("midnight_sandstone", new Block(Properties.ofFullCopy(Blocks.SANDSTONE)));
      chiseled_midnight_sandstone = register("chiseled_midnight_sandstone", new Block(Properties.ofFullCopy(midnight_sandstone)));
      cut_midnight_sandstone = register("cut_midnight_sandstone", new Block(Properties.ofFullCopy(midnight_sandstone)));
      smooth_midnight_sandstone = register("smooth_midnight_sandstone", new Block(Properties.ofFullCopy(midnight_sandstone)));
      midnight_sandstone_pillar = register("midnight_sandstone_pillar", new RotatedPillarBlock(Properties.ofFullCopy(midnight_sandstone)));
      turquoise_dripstone = register(
         "turquoise_dripstone", new SkyDripstoneBlock(Properties.ofFullCopy(turquoise_stone).sound(SoundType.POINTED_DRIPSTONE).dynamicShape())
      );
      everbright_diopside_ore = register("everbright_diopside_ore", new SkyOreBlock(getOreProperties(), 3, 7));
      everbright_pyrope_ore = register("everbright_pyrope_ore", new SkyOreBlock(getOreProperties(), 1, 2));
      everbright_aquite_ore = register("everbright_aquite_ore", new SkyOreBlock(getOreProperties()));
      everbright_charoite_ore = register("everbright_charoite_ore", new SkyOreBlock(getOreProperties()));
      everbright_moonstone_ore = register("everbright_moonstone_ore", new SkyOreBlock(getOreProperties().lightLevel(state -> 7), 0, 2));
      everbright_emerald_ore = register("everbright_emerald_ore", new SkyOreBlock(Properties.ofFullCopy(Blocks.EMERALD_ORE), 3, 7));
      falsite_ore = register("falsite_ore", new SkyOreBlock(getOreProperties()));
      ventium_ore = register("ventium_ore", new SkyOreBlock(getOreProperties()));
      short_lunar_grass = register("short_lunar_grass", new SkyTallGrassBlock(() -> (DoublePlantBlock)tall_lunar_grass));
      tall_lunar_grass = register("tall_lunar_grass", new DoublePlantBlock(Properties.ofFullCopy(Blocks.TALL_GRASS)));
      lunar_grass_block = register(
         "lunar_grass_block",
         new SkyGrassBlock(
            new SkyGrassBlock.GrassProperties(
               () -> short_lunar_grass,
               () -> lunar_dirt,
               GelCollectors.mapOf((Supplier<Block>)() -> lunar_dirt, (Supplier<Block>)() -> lunar_grass_block, new Object[0])
            )
         )
      );
      lunar_dirt = register("lunar_dirt", new SkyDirtBlock());
      coarse_lunar_dirt = register("coarse_lunar_dirt", new SkyDirtBlock());
      lunar_mud = register("lunar_mud", new SkyMudBlock(Properties.ofFullCopy(Blocks.DIRT).sound(SkiesSounds.MUD)));
      register("lunar_farmland", (SkyFarmlandBlock)lunar_farmland.get());
      lunar_stone = register("lunar_stone", new Block(Properties.ofFullCopy(Blocks.STONE)));
      lunar_cobblestone = register("lunar_cobblestone", new Block(Properties.ofFullCopy(Blocks.COBBLESTONE)));
      mossy_lunar_cobblestone = register("mossy_lunar_cobblestone", new Block(Properties.ofFullCopy(Blocks.MOSSY_COBBLESTONE)));
      lunar_stonebrick = register("lunar_stonebrick", new Block(brickProp));
      mossy_lunar_stonebrick = register("mossy_lunar_stonebrick", new Block(brickProp));
      cracked_lunar_stonebrick = register("cracked_lunar_stonebrick", new Block(brickProp));
      chiseled_lunar_stonebrick = register("chiseled_lunar_stonebrick", new Block(brickProp));
      umber = register("umber", new Block(Properties.ofFullCopy(Blocks.GRANITE)));
      polished_umber = register("polished_umber", new Block(Properties.ofFullCopy(Blocks.POLISHED_GRANITE)));
      cinderstone = register("cinderstone", new Block(Properties.ofFullCopy(Blocks.GRANITE).lightLevel(state -> 4)));
      polished_cinderstone = register("polished_cinderstone", new Block(Properties.ofFullCopy(Blocks.POLISHED_GRANITE).lightLevel(state -> 4)));
      moonlit_bloom = register("moonlit_bloom", new FlowerBlock(MobEffects.NIGHT_VISION, 6.0F, flowerProp));
      crystal_flower = register("crystal_flower", new CrystalFlowerBlock(MobEffects.INVISIBILITY, 6.0F, flowerProp));
      blaze_bud = register("blaze_bud", new FlowerBlock(MobEffects.FIRE_RESISTANCE, 6.0F, flowerProp));
      flare_floret = register("flare_floret", new FlowerBlock(MobEffects.HUNGER, 6.0F, flowerProp));
      nightcress = register("nightcress", new FlowerBlock(MobEffects.BAD_OMEN, 6.0F, flowerProp));
      muckweed = register("muckweed", new LargeFlowerBlock(MobEffects.DIG_SLOWDOWN, 6.0F, replacableFlowerProp));
      lucentroot = register("lucentroot", new FlowerBlock(MobEffects.UNLUCK, 6.0F, flowerProp));
      lunar_sapling = register("lunar_sapling", new SaplingBlock(SkiesTreeGrowers.LUNAR, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      dusk_sapling = register("dusk_sapling", new SaplingBlock(SkiesTreeGrowers.DUSK, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      maple_sapling = register("maple_sapling", new SaplingBlock(SkiesTreeGrowers.MAPLE, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      moonlit_water_lily = registerBlock(
         "moonlit_water_lily",
         new LargeLilyPadBlock(Properties.of().pushReaction(PushReaction.DESTROY).strength(0.15F).sound(SoundType.LILY_PAD).noOcclusion())
      );
      baneful_mushroom = register(
         "baneful_mushroom",
         new BanefulMushroomBlock(
            Properties.of().pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS).offsetType(OffsetType.XZ)
         )
      );
      glimmer_reed = register("glimmer_reed", new GlimmerReedBlock(Properties.ofFullCopy(Blocks.ROSE_BUSH).randomTicks()));
      crystallized_grass = register(
         "crystallized_grass",
         new CrystallizedGrassBlock(
            Properties.of()
               .replaceable()
               .noCollission()
               .instabreak()
               .offsetType(OffsetType.XYZ)
               .pushReaction(PushReaction.DESTROY)
               .sound(SkiesSounds.CRYSTALLIZED_PLANT)
         )
      );
      lunar_vine = register("lunar_vine", new SkyVineBlock(vineProp));
      lunar_leaves = register("lunar_leaves", new SkyLeavesBlock(leafProp));
      lunar_log = register("lunar_log", new RotatedPillarBlock(logProp));
      lunar_wood = register("lunar_wood", new RotatedPillarBlock(logProp));
      stripped_lunar_log = register("stripped_lunar_log", new RotatedPillarBlock(logProp));
      stripped_lunar_wood = register("stripped_lunar_wood", new RotatedPillarBlock(logProp));
      lunar_planks = register("lunar_planks", new Block(plankProp));
      dusk_vine = register("dusk_vine", new SkyVineBlock(vineProp));
      dusk_leaves = register("dusk_leaves", new SkyLeavesBlock(leafProp));
      dusk_log = register("dusk_log", new RotatedPillarBlock(logProp));
      dusk_wood = register("dusk_wood", new RotatedPillarBlock(logProp));
      stripped_dusk_log = register("stripped_dusk_log", new RotatedPillarBlock(logProp));
      stripped_dusk_wood = register("stripped_dusk_wood", new RotatedPillarBlock(logProp));
      dusk_planks = register("dusk_planks", new Block(plankProp));
      maple_vine = register("maple_vine", new SkyVineBlock(vineProp));
      maple_leaves = register("maple_leaves", new SkyLeavesBlock(leafProp));
      maple_log = register("maple_log", new RotatedPillarBlock(logProp));
      maple_wood = register("maple_wood", new RotatedPillarBlock(logProp));
      stripped_maple_log = register("stripped_maple_log", new RotatedPillarBlock(logProp));
      stripped_maple_wood = register("stripped_maple_wood", new RotatedPillarBlock(logProp));
      maple_planks = register("maple_planks", new Block(plankProp));
      lunar_crafting_table = register("lunar_crafting_table", new SkyWorkbenchBlock());
      dusk_crafting_table = register("dusk_crafting_table", new SkyWorkbenchBlock());
      maple_crafting_table = register("maple_crafting_table", new SkyWorkbenchBlock());
      Properties crystallizedProp = Properties.ofFullCopy(Blocks.COBBLESTONE)
         .sound(SoundType.GLASS)
         .noOcclusion()
         .isViewBlocking((state, reader, pos) -> false);
      Supplier<Set<Block>> crystallizedLogs = () -> ImmutableSet.of(crystallized_log, crystallized_wood, crystallized_wall);
      Supplier<Set<Block>> crystallizedPlanks = () -> ImmutableSet.of(crystallized_planks, crystallized_slab, crystallized_stairs);
      crystallized_leaves = register(
         "crystallized_leaves",
         new SkyLeavesBlock(
            Properties.ofFullCopy(Blocks.COBBLESTONE)
               .sound(SoundType.GLASS)
               .noOcclusion()
               .isViewBlocking((state, reader, pos) -> false)
               .sound(SkiesSounds.CRYSTALLIZED_PLANT)
         )
      );
      crystallized_log = register("crystallized_log", new TranslucentPillarBlock(crystallizedProp, crystallizedLogs));
      crystallized_wood = register("crystallized_wood", new TranslucentPillarBlock(crystallizedProp, crystallizedLogs));
      crystallized_planks = register("crystallized_planks", new TranslucentBlock(crystallizedProp, crystallizedPlanks));
      lunar_chest = registerBlock("lunar_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.LUNAR_CHEST));
      dusk_chest = registerBlock("dusk_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.DUSK_CHEST));
      maple_chest = registerBlock("maple_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.MAPLE_CHEST));
      vitreous_moonstone = register(
         "vitreous_moonstone",
         new SkyGlassBlock(
            Properties.of()
               .instrument(NoteBlockInstrument.BASEDRUM)
               .requiresCorrectToolForDrops()
               .noOcclusion()
               .strength(2.0F, 5.0F)
               .sound(SoundType.GLASS)
               .lightLevel(value -> 4),
            121.0F,
            151.0F,
            168.0F
         )
      );
      crystal_sand = register(
         "crystal_sand", new SkySandBlock(16777215, Properties.ofFullCopy(Blocks.SAND), rand -> rand.nextFloat() < 0.3 ? crystal_flower : crystallized_grass)
      );
      crystal_sandstone = register("crystal_sandstone", new Block(Properties.ofFullCopy(Blocks.SANDSTONE)));
      chiseled_crystal_sandstone = register("chiseled_crystal_sandstone", new Block(Properties.ofFullCopy(crystal_sandstone)));
      cut_crystal_sandstone = register("cut_crystal_sandstone", new Block(Properties.ofFullCopy(crystal_sandstone)));
      smooth_crystal_sandstone = register("smooth_crystal_sandstone", new Block(Properties.ofFullCopy(crystal_sandstone)));
      crystal_sandstone_pillar = register("crystal_sandstone_pillar", new RotatedPillarBlock(Properties.ofFullCopy(crystal_sandstone)));
      lunar_dripstone = register("lunar_dripstone", new SkyDripstoneBlock(Properties.ofFullCopy(lunar_stone).sound(SoundType.POINTED_DRIPSTONE).dynamicShape()));
      decaying_spike = register("decaying_spike", new SkyDecayingSpikeBlock());
      spider_webbing = register("spider_webbing", new SkyWebbingBlock(Properties.ofFullCopy(Blocks.COBWEB)));
      spider_nest = register("spider_nest", new SpiderNestBlock(getDungeonStoneProperties().strength(6.5F)));
      everdawn_diopside_ore = register("everdawn_diopside_ore", new SkyOreBlock(getOreProperties(), 3, 7));
      everdawn_pyrope_ore = register("everdawn_pyrope_ore", new SkyOreBlock(getOreProperties(), 1, 2));
      everdawn_aquite_ore = register("everdawn_aquite_ore", new SkyOreBlock(getOreProperties()));
      everdawn_charoite_ore = register("everdawn_charoite_ore", new SkyOreBlock(getOreProperties()));
      everdawn_moonstone_ore = register("everdawn_moonstone_ore", new SkyOreBlock(Properties.ofFullCopy(everbright_moonstone_ore), 0, 2));
      everdawn_emerald_ore = register("everdawn_emerald_ore", new SkyOreBlock(Properties.ofFullCopy(Blocks.EMERALD_ORE), 3, 7));
      horizonite_ore = register("horizonite_ore", new SkyOreBlock(getOreProperties()));
      short_comet_grass = register("short_comet_grass", new SkyTallGrassBlock(() -> null));
      Map<Supplier<Block>, Supplier<Block>> cometGrassSpread = new HashMap<>();
      cometGrassSpread.put(() -> turquoise_dirt, () -> turquoise_comet_grass_block);
      cometGrassSpread.put(() -> lunar_dirt, () -> lunar_comet_grass_block);
      Function<RandomSource, Supplier<Block>> cometGrassFlowerFunc = rand -> () -> blush_blossom;
      turquoise_comet_grass_block = register(
         "turquoise_comet_grass_block",
         new SkyGrassBlock(new SkyGrassBlock.GrassProperties(() -> short_comet_grass, () -> turquoise_dirt, cometGrassSpread, cometGrassFlowerFunc))
      );
      lunar_comet_grass_block = register(
         "lunar_comet_grass_block",
         new SkyGrassBlock(new SkyGrassBlock.GrassProperties(() -> short_comet_grass, () -> lunar_dirt, cometGrassSpread, cometGrassFlowerFunc))
      );
      comet_leaves = register("comet_leaves", new FruitLeavesBlock(leafProp, () -> SkiesItems.comet_berries));
      comet_log = register("comet_log", new RotatedPillarBlock(logProp));
      comet_wood = register("comet_wood", new RotatedPillarBlock(logProp));
      stripped_comet_log = register("stripped_comet_log", new RotatedPillarBlock(logProp));
      stripped_comet_wood = register("stripped_comet_wood", new RotatedPillarBlock(logProp));
      comet_planks = register("comet_planks", new Block(plankProp));
      comet_sapling = register("comet_sapling", new SaplingBlock(SkiesTreeGrowers.COMET, Properties.ofFullCopy(Blocks.CHERRY_SAPLING)));
      comet_crafting_table = register("comet_crafting_table", new SkyWorkbenchBlock());
      comet_chest = registerBlock("comet_chest", new SkyChestBlock(chestProp, () -> SkiesBlockEntityTypes.COMET_CHEST));
      comet_berry_pie = register("comet_berry_pie", new SkyPieBlock(Properties.ofFullCopy(Blocks.CAKE).sound(SoundType.STONE)));
      crescent_fruit_sapling = register("crescent_fruit_sapling", new SaplingBlock(SkiesTreeGrowers.CRESCENT_FRUIT, Properties.ofFullCopy(Blocks.OAK_SAPLING)));
      crescent_fruit_leaves = register("crescent_fruit_leaves", new HangingFruitLeavesBlock(() -> crescent_fruit));
      crescent_fruit = registerBlock("crescent_fruit", new FruitBlock(() -> crescent_fruit_leaves));
      Properties rawProps = Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK);
      raw_falsite_block = register("raw_falsite_block", new Block(rawProps));
      raw_ventium_block = register("raw_ventium_block", new Block(rawProps));
      raw_horizonite_block = register("raw_horizonite_block", new Block(rawProps));
      raw_aquite_block = register("raw_aquite_block", new Block(rawProps));
      raw_charoite_block = register("raw_charoite_block", new Block(rawProps));
      moonstone_block = register(
         "moonstone_block", new CrystalBlock(getOreBlockProperties().sound(SoundType.METAL).randomTicks().lightLevel(state -> 15), () -> moonstone_crystal)
      );
      sunstone_block = register(
         "sunstone_block", new CrystalBlock(getOreBlockProperties().sound(SoundType.METAL).randomTicks().lightLevel(state -> 15), () -> sunstone_crystal)
      );
      falsite_block = register("falsite_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      ventium_block = register("ventium_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      horizonite_block = register("horizonite_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      pyrope_block = register("pyrope_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      aquite_block = register("aquite_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      diopside_block = register("diopside_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      charoite_block = register("charoite_block", new Block(getOreBlockProperties().sound(SoundType.METAL)));
      moonstone = register(
         "moonstone",
         new WallMoonstoneBlock(
            Properties.of().pushReaction(PushReaction.DESTROY).strength(0.0F).noCollission().sound(SkiesSounds.MOONSTONE).lightLevel(value -> 15)
         )
      );
      moonstone_lantern = register("moonstone_lantern", new SkyLampBlock());
      star_flare = register("star_flare", new StarFlareBlock());
      blue_fire = registerBlock(
         "blue_fire",
         new SkyFireBlock(
            Properties.ofFullCopy(Blocks.FIRE),
            () -> new MobEffectInstance(MobEffects.LEVITATION, 20),
            () -> everbright_portal,
            SkiesBlockTags.BLUE_FIRE_BASE_BLOCKS
         )
      );
      black_fire = registerBlock(
         "black_fire",
         new SkyFireBlock(
            Properties.ofFullCopy(Blocks.FIRE).lightLevel(state -> 7).emissiveRendering((state, level, pos) -> true),
            () -> new MobEffectInstance(MobEffects.BLINDNESS, 60),
            () -> everdawn_portal,
            SkiesBlockTags.BLACK_FIRE_BASE_BLOCKS
         )
      );
      upsidedown_blue_fire = registerBlock(
         "upsidedown_blue_fire", new SkyUpsidedownFireBlock(Properties.ofFullCopy(blue_fire), () -> new MobEffectInstance(MobEffects.SLOW_FALLING, 20))
      );
      upsidedown_black_fire = registerBlock(
         "upsidedown_black_fire", new SkyUpsidedownFireBlock(Properties.ofFullCopy(black_fire), () -> new MobEffectInstance(MobEffects.BLINDNESS, 60))
      );
      warding_pearl = registerBlock(
         "warding_pearl", new WardingPearlBlock(Properties.of().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(state -> 3))
      );
      wall_warding_pearl = registerBlock("wall_warding_pearl", new WallWardingPearlBlock(Properties.ofFullCopy(warding_pearl)));
      moonstone_crystal = register("moonstone_crystal", new WallCrystalBlock(Properties.ofFullCopy(moonstone).lightLevel(state -> 7).strength(1.5F)));
      sunstone_crystal = register("sunstone_crystal", new WallCrystalBlock(Properties.ofFullCopy(moonstone).lightLevel(state -> 8).strength(1.5F)));
      crystal_glass = register("crystal_glass", new SkyGlassBlock(Properties.ofFullCopy(Blocks.GLASS), 218.0F, 217.0F, 255.0F));
      crystal_glass_pane = register("crystal_glass_pane", new SkyGlassPaneBlock(Properties.ofFullCopy(Blocks.GLASS_PANE), 218.0F, 217.0F, 255.0F));
      midnight_glass = register("midnight_glass", new SkyGlassBlock(Properties.ofFullCopy(Blocks.GLASS), 3.0F, 3.0F, 35.0F));
      midnight_glass_pane = register("midnight_glass_pane", new SkyGlassPaneBlock(Properties.ofFullCopy(Blocks.GLASS_PANE), 3.0F, 3.0F, 35.0F));
      everbright_portal = register("everbright_portal", new EverbrightPortalBlock(SkyPortalBlock.makeProps()));
      everdawn_portal = register("everdawn_portal", new EverdawnPortalBlock(SkyPortalBlock.makeProps()));
      snowcap_oven = register("snowcap_oven", new SnowcapOvenBlock(Properties.ofFullCopy(Blocks.FURNACE)));
      horizonite_forge = register("horizonite_forge", new HorizoniteForgeBlock(Properties.ofFullCopy(Blocks.FURNACE).sound(SoundType.METAL)));
      Properties doorProp = Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).noOcclusion().strength(3.0F).sound(SoundType.WOOD);
      bluebright_door = registerBlock("bluebright_door", door(doorProp, SkiesBlockSets.BLUEBRIGHT));
      starlit_door = registerBlock("starlit_door", door(doorProp, SkiesBlockSets.STARLIT));
      frostbright_door = registerBlock("frostbright_door", door(doorProp, SkiesBlockSets.FROSTBRIGHT));
      lunar_door = registerBlock("lunar_door", door(doorProp, SkiesBlockSets.LUNAR));
      dusk_door = registerBlock("dusk_door", door(doorProp, SkiesBlockSets.DUSK));
      maple_door = registerBlock("maple_door", door(doorProp, SkiesBlockSets.MAPLE));
      comet_door = registerBlock("comet_door", door(doorProp, SkiesBlockSets.COMET));
      crystallized_door = registerBlock("crystallized_door", door(crystallizedProp, SkiesBlockSets.CRYSTALLIZED));
      bluebright_bookshelf = register("bluebright_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      starlit_bookshelf = register("starlit_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      frostbright_bookshelf = register("frostbright_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      lunar_bookshelf = register("lunar_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      dusk_bookshelf = register("dusk_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      maple_bookshelf = register("maple_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      comet_bookshelf = register("comet_bookshelf", new Block(Properties.ofFullCopy(Blocks.BOOKSHELF)));
      Properties ladderProp = Properties.ofFullCopy(Blocks.LADDER);
      bluebright_ladder = register("bluebright_ladder", new LadderBlock(ladderProp) {});
      starlit_ladder = register("starlit_ladder", new LadderBlock(ladderProp) {});
      frostbright_ladder = register("frostbright_ladder", new LadderBlock(ladderProp) {});
      lunar_ladder = register("lunar_ladder", new LadderBlock(ladderProp) {});
      dusk_ladder = register("dusk_ladder", new LadderBlock(ladderProp) {});
      maple_ladder = register("maple_ladder", new LadderBlock(ladderProp) {});
      comet_ladder = register("comet_ladder", new LadderBlock(ladderProp) {});
      Properties fenceProp = Properties.of()
         .ignitedByLava()
         .instrument(NoteBlockInstrument.BASS)
         .mapColor(MapColor.TERRACOTTA_BLUE)
         .strength(2.0F, 3.0F)
         .sound(SoundType.WOOD);
      bluebright_fence = register("bluebright_fence", new FenceBlock(fenceProp));
      starlit_fence = register("starlit_fence", new FenceBlock(fenceProp));
      frostbright_fence = register("frostbright_fence", new FenceBlock(fenceProp));
      lunar_fence = register("lunar_fence", new FenceBlock(fenceProp));
      dusk_fence = register("dusk_fence", new FenceBlock(fenceProp));
      maple_fence = register("maple_fence", new FenceBlock(fenceProp));
      comet_fence = register("comet_fence", new FenceBlock(fenceProp));
      Properties fenceGateProp = Properties.of()
         .ignitedByLava()
         .instrument(NoteBlockInstrument.BASS)
         .mapColor(MapColor.TERRACOTTA_BLUE)
         .strength(2.0F, 3.0F)
         .sound(SoundType.WOOD);
      bluebright_fence_gate = register("bluebright_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.BLUEBRIGHT));
      starlit_fence_gate = register("starlit_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.STARLIT));
      frostbright_fence_gate = register("frostbright_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.FROSTBRIGHT));
      lunar_fence_gate = register("lunar_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.LUNAR));
      dusk_fence_gate = register("dusk_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.DUSK));
      maple_fence_gate = register("maple_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.MAPLE));
      comet_fence_gate = register("comet_fence_gate", fenceGate(fenceGateProp, SkiesWoodTypes.COMET));
      Properties trapDoorProp = Properties.of()
         .ignitedByLava()
         .instrument(NoteBlockInstrument.BASS)
         .mapColor(MapColor.WOOD)
         .noOcclusion()
         .strength(3.0F)
         .sound(SoundType.WOOD);
      bluebright_trapdoor = register("bluebright_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.BLUEBRIGHT));
      starlit_trapdoor = register("starlit_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.STARLIT));
      frostbright_trapdoor = register("frostbright_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.FROSTBRIGHT));
      lunar_trapdoor = register("lunar_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.LUNAR));
      dusk_trapdoor = register("dusk_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.DUSK));
      maple_trapdoor = register("maple_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.MAPLE));
      comet_trapdoor = register("comet_trapdoor", trapDoor(trapDoorProp, SkiesBlockSets.COMET));
      crystallized_trapdoor = register("crystallized_trapdoor", trapDoor(crystallizedProp, SkiesBlockSets.CRYSTALLIZED));
      Properties woodPlateProp = Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE);
      Properties stonePlateProp = Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE);
      moonstone_pressure_plate = register("moonstone_pressure_plate", new MoonstonePressurePlateBlock(SkiesBlockSets.MOONSTONE, stonePlateProp));
      turquoise_stone_pressure_plate = register("turquoise_stone_pressure_plate", new PressurePlateBlock(SkiesBlockSets.TURQUOISE_STONE, stonePlateProp) {});
      lunar_stone_pressure_plate = register("lunar_stone_pressure_plate", new PressurePlateBlock(SkiesBlockSets.LUNAR_STONE, stonePlateProp) {});
      bluebright_pressure_plate = register("bluebright_pressure_plate", new PressurePlateBlock(SkiesBlockSets.BLUEBRIGHT, woodPlateProp) {});
      starlit_pressure_plate = register("starlit_pressure_plate", new PressurePlateBlock(SkiesBlockSets.STARLIT, woodPlateProp) {});
      frostbright_pressure_plate = register("frostbright_pressure_plate", new PressurePlateBlock(SkiesBlockSets.FROSTBRIGHT, woodPlateProp) {});
      lunar_pressure_plate = register("lunar_pressure_plate", new PressurePlateBlock(SkiesBlockSets.LUNAR, woodPlateProp) {});
      dusk_pressure_plate = register("dusk_pressure_plate", new PressurePlateBlock(SkiesBlockSets.DUSK, woodPlateProp) {});
      maple_pressure_plate = register("maple_pressure_plate", new PressurePlateBlock(SkiesBlockSets.MAPLE, woodPlateProp) {});
      comet_pressure_plate = register("comet_pressure_plate", new PressurePlateBlock(SkiesBlockSets.COMET, woodPlateProp) {});
      crystallized_pressure_plate = register("crystallized_pressure_plate", new PressurePlateBlock(SkiesBlockSets.CRYSTALLIZED, stonePlateProp) {});
      Properties woodButtonProp = Properties.ofFullCopy(Blocks.OAK_BUTTON);
      Properties stoneButtonProp = Properties.ofFullCopy(Blocks.STONE_BUTTON);
      turquoise_stone_button = register("turquoise_stone_button", new SkyButtonBlock.Stone(stoneButtonProp));
      lunar_stone_button = register("lunar_stone_button", new SkyButtonBlock.Stone(stoneButtonProp));
      bluebright_button = register("bluebright_button", new SkyButtonBlock.Wood(woodButtonProp));
      starlit_button = register("starlit_button", new SkyButtonBlock.Wood(woodButtonProp));
      frostbright_button = register("frostbright_button", new SkyButtonBlock.Wood(woodButtonProp));
      lunar_button = register("lunar_button", new SkyButtonBlock.Wood(woodButtonProp));
      dusk_button = register("dusk_button", new SkyButtonBlock.Wood(woodButtonProp));
      maple_button = register("maple_button", new SkyButtonBlock.Wood(woodButtonProp));
      comet_button = register("comet_button", new SkyButtonBlock.Wood(woodButtonProp));
      crystallized_button = register("crystallized_button", new SkyButtonBlock.Stone(stoneButtonProp));
      Properties leverProp = Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.WOOD);
      turquoise_lever = register("turquoise_lever", new LeverBlock(leverProp) {});
      lunar_lever = register("lunar_lever", new LeverBlock(leverProp) {});
      Properties signProp = Properties.ofFullCopy(Blocks.OAK_SIGN);
      bluebright_sign = registerBlock("bluebright_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.BLUEBRIGHT));
      starlit_sign = registerBlock("starlit_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.STARLIT));
      frostbright_sign = registerBlock("frostbright_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.FROSTBRIGHT));
      lunar_sign = registerBlock("lunar_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.LUNAR));
      dusk_sign = registerBlock("dusk_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.DUSK));
      maple_sign = registerBlock("maple_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.MAPLE));
      comet_sign = registerBlock("comet_sign", new SkyStandingSignBlock(signProp, SkiesWoodTypes.COMET));
      bluebright_wall_sign = registerBlock("bluebright_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.BLUEBRIGHT));
      starlit_wall_sign = registerBlock("starlit_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.STARLIT));
      frostbright_wall_sign = registerBlock("frostbright_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.FROSTBRIGHT));
      lunar_wall_sign = registerBlock("lunar_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.LUNAR));
      dusk_wall_sign = registerBlock("dusk_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.DUSK));
      maple_wall_sign = registerBlock("maple_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.MAPLE));
      comet_wall_sign = registerBlock("comet_wall_sign", new SkyWallSignBlock(signProp, SkiesWoodTypes.COMET));
      Properties hangingSignProp = Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN);
      bluebright_hanging_sign = registerBlock("bluebright_hanging_sign", new SkyCeilingHangingSignBlock(hangingSignProp, SkiesWoodTypes.BLUEBRIGHT));
      starlit_hanging_sign = registerBlock("starlit_hanging_sign", new SkyCeilingHangingSignBlock(hangingSignProp, SkiesWoodTypes.STARLIT));
      frostbright_hanging_sign = registerBlock("frostbright_hanging_sign", new SkyCeilingHangingSignBlock(hangingSignProp, SkiesWoodTypes.FROSTBRIGHT));
      lunar_hanging_sign = registerBlock("lunar_hanging_sign", new SkyCeilingHangingSignBlock(hangingSignProp, SkiesWoodTypes.LUNAR));
      dusk_hanging_sign = registerBlock("dusk_hanging_sign", new SkyCeilingHangingSignBlock(hangingSignProp, SkiesWoodTypes.DUSK));
      maple_hanging_sign = registerBlock("maple_hanging_sign", new SkyCeilingHangingSignBlock(hangingSignProp, SkiesWoodTypes.MAPLE));
      comet_hanging_sign = registerBlock(
         "comet_hanging_sign",
         new SkyCeilingHangingSignBlock(Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).sound(SoundType.CHERRY_WOOD), SkiesWoodTypes.COMET)
      );
      Properties wallHangingSignProp = Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN);
      bluebright_wall_hanging_sign = registerBlock("bluebright_wall_hanging_sign", new SkyWallHangingSignBlock(wallHangingSignProp, SkiesWoodTypes.BLUEBRIGHT));
      starlit_wall_hanging_sign = registerBlock("starlit_wall_hanging_sign", new SkyWallHangingSignBlock(wallHangingSignProp, SkiesWoodTypes.STARLIT));
      frostbright_wall_hanging_sign = registerBlock(
         "frostbright_wall_hanging_sign", new SkyWallHangingSignBlock(wallHangingSignProp, SkiesWoodTypes.FROSTBRIGHT)
      );
      lunar_wall_hanging_sign = registerBlock("lunar_wall_hanging_sign", new SkyWallHangingSignBlock(wallHangingSignProp, SkiesWoodTypes.LUNAR));
      dusk_wall_hanging_sign = registerBlock("dusk_wall_hanging_sign", new SkyWallHangingSignBlock(wallHangingSignProp, SkiesWoodTypes.DUSK));
      maple_wall_hanging_sign = registerBlock("maple_wall_hanging_sign", new SkyWallHangingSignBlock(wallHangingSignProp, SkiesWoodTypes.MAPLE));
      comet_wall_hanging_sign = registerBlock(
         "comet_wall_hanging_sign",
         new SkyWallHangingSignBlock(Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).sound(SoundType.CHERRY_WOOD), SkiesWoodTypes.COMET)
      );
      blinding_stone = registerDungeon("blinding_stone", new Block(getDungeonStoneProperties()), SkiesDungeonType.BLINDING);
      glowing_blinding_stone = registerDungeon(
         "glowing_blinding_stone", new Block(Properties.ofFullCopy(blinding_stone).lightLevel(value -> 15)), SkiesDungeonType.BLINDING
      );
      carved_blinding_stone = registerDungeon("carved_blinding_stone", new Block(Properties.ofFullCopy(blinding_stone)), SkiesDungeonType.BLINDING);
      blinding_stone_pillar = registerDungeon("blinding_stone_pillar", new RotatedPillarBlock(Properties.ofFullCopy(blinding_stone)), SkiesDungeonType.BLINDING);
      blinding_stairs = registerDungeon(
         "blinding_stairs", new StairBlock(blinding_stone.defaultBlockState(), Properties.ofFullCopy(blinding_stone)) {}, SkiesDungeonType.BLINDING
      );
      glowing_blinding_stairs = registerDungeon(
         "glowing_blinding_stairs",
         new LitStairsBlock(() -> glowing_blinding_stone.defaultBlockState(), Properties.ofFullCopy(glowing_blinding_stone)),
         SkiesDungeonType.BLINDING
      );
      blinding_slab = registerDungeon("blinding_slab", new SlabBlock(Properties.ofFullCopy(blinding_stone)) {}, SkiesDungeonType.BLINDING);
      glowing_blinding_slab = registerDungeon(
         "glowing_blinding_slab", new LitSlabBlock(Properties.ofFullCopy(glowing_blinding_stone)), SkiesDungeonType.BLINDING
      );
      blinding_wall = registerDungeon("blinding_wall", new WallBlock(Properties.ofFullCopy(blinding_stone)), SkiesDungeonType.BLINDING);
      glowing_blinding_wall = registerDungeon("glowing_blinding_wall", new WallBlock(Properties.ofFullCopy(glowing_blinding_stone)), SkiesDungeonType.BLINDING);
      nature_stone = registerDungeon("nature_stone", new Block(getDungeonStoneProperties()), SkiesDungeonType.NATURE);
      glowing_nature_stone = registerDungeon(
         "glowing_nature_stone", new Block(Properties.ofFullCopy(nature_stone).lightLevel(value -> 15)), SkiesDungeonType.NATURE
      );
      nature_stonebrick = registerDungeon("nature_stonebrick", new Block(Properties.ofFullCopy(nature_stone)), SkiesDungeonType.NATURE);
      glowing_nature_stonebrick = registerDungeon("glowing_nature_stonebrick", new Block(Properties.ofFullCopy(glowing_nature_stone)), SkiesDungeonType.NATURE);
      carved_nature_stonebrick = registerDungeon("carved_nature_stonebrick", new Block(Properties.ofFullCopy(nature_stonebrick)), SkiesDungeonType.NATURE);
      nature_stone_pillar = registerDungeon("nature_stone_pillar", new RotatedPillarBlock(Properties.ofFullCopy(nature_stonebrick)), SkiesDungeonType.NATURE);
      nature_stone_stairs = registerDungeon(
         "nature_stone_stairs", new StairBlock(nature_stone.defaultBlockState(), Properties.ofFullCopy(nature_stone)) {}, SkiesDungeonType.NATURE
      );
      glowing_nature_stone_stairs = registerDungeon(
         "glowing_nature_stone_stairs",
         new LitStairsBlock(() -> glowing_nature_stone.defaultBlockState(), Properties.ofFullCopy(glowing_nature_stone)),
         SkiesDungeonType.NATURE
      );
      nature_stonebrick_stairs = registerDungeon(
         "nature_stonebrick_stairs",
         new StairBlock(nature_stonebrick.defaultBlockState(), Properties.ofFullCopy(nature_stonebrick)) {},
         SkiesDungeonType.NATURE
      );
      glowing_nature_stonebrick_stairs = registerDungeon(
         "glowing_nature_stonebrick_stairs",
         new StairBlock(glowing_nature_stonebrick.defaultBlockState(), Properties.ofFullCopy(glowing_nature_stonebrick)) {},
         SkiesDungeonType.NATURE
      );
      nature_stone_slab = registerDungeon("nature_stone_slab", new SlabBlock(Properties.ofFullCopy(nature_stone)) {}, SkiesDungeonType.NATURE);
      glowing_nature_stone_slab = registerDungeon(
         "glowing_nature_stone_slab", new LitSlabBlock(Properties.ofFullCopy(glowing_nature_stone)), SkiesDungeonType.NATURE
      );
      nature_stonebrick_slab = registerDungeon("nature_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(nature_stonebrick)) {}, SkiesDungeonType.NATURE);
      glowing_nature_stonebrick_slab = registerDungeon(
         "glowing_nature_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(glowing_nature_stonebrick)) {}, SkiesDungeonType.NATURE
      );
      nature_stonebrick_wall = registerDungeon("nature_stonebrick_wall", new WallBlock(Properties.ofFullCopy(nature_stonebrick)), SkiesDungeonType.NATURE);
      glowing_nature_stonebrick_wall = registerDungeon(
         "glowing_nature_stonebrick_wall", new WallBlock(Properties.ofFullCopy(glowing_nature_stonebrick)), SkiesDungeonType.NATURE
      );
      poison_stone = registerDungeon("poison_stone", new Block(getDungeonStoneProperties()), SkiesDungeonType.POISON);
      glowing_poison_stone = registerDungeon(
         "glowing_poison_stone", new Block(Properties.ofFullCopy(poison_stone).lightLevel(value -> 8)), SkiesDungeonType.POISON
      );
      poison_stonebrick = registerDungeon("poison_stonebrick", new Block(Properties.ofFullCopy(poison_stone)), SkiesDungeonType.POISON);
      rough_poison_stonebrick = registerDungeon("rough_poison_stonebrick", new Block(Properties.ofFullCopy(poison_stonebrick)), SkiesDungeonType.POISON);
      carved_poison_stonebrick = registerDungeon("carved_poison_stonebrick", new Block(Properties.ofFullCopy(poison_stonebrick)), SkiesDungeonType.POISON);
      poison_stone_pillar = registerDungeon("poison_stone_pillar", new RotatedPillarBlock(Properties.ofFullCopy(poison_stonebrick)), SkiesDungeonType.POISON);
      poison_stone_stairs = registerDungeon(
         "poison_stone_stairs", new StairBlock(poison_stone.defaultBlockState(), Properties.ofFullCopy(poison_stone)) {}, SkiesDungeonType.POISON
      );
      glowing_poison_stone_stairs = registerDungeon(
         "glowing_poison_stone_stairs",
         new LitStairsBlock(() -> glowing_poison_stone.defaultBlockState(), Properties.ofFullCopy(glowing_poison_stone)),
         SkiesDungeonType.POISON
      );
      poison_stonebrick_stairs = registerDungeon(
         "poison_stonebrick_stairs",
         new StairBlock(poison_stonebrick.defaultBlockState(), Properties.ofFullCopy(poison_stonebrick)) {},
         SkiesDungeonType.POISON
      );
      rough_poison_stonebrick_stairs = registerDungeon(
         "rough_poison_stonebrick_stairs",
         new StairBlock(rough_poison_stonebrick.defaultBlockState(), Properties.ofFullCopy(rough_poison_stonebrick)) {},
         SkiesDungeonType.POISON
      );
      poison_stone_slab = registerDungeon("poison_stone_slab", new SlabBlock(Properties.ofFullCopy(poison_stone)) {}, SkiesDungeonType.POISON);
      glowing_poison_stone_slab = registerDungeon(
         "glowing_poison_stone_slab", new LitSlabBlock(Properties.ofFullCopy(glowing_poison_stone)), SkiesDungeonType.POISON
      );
      poison_stonebrick_slab = registerDungeon("poison_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(poison_stonebrick)) {}, SkiesDungeonType.POISON);
      rough_poison_stonebrick_slab = registerDungeon(
         "rough_poison_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(rough_poison_stonebrick)) {}, SkiesDungeonType.POISON
      );
      poison_stonebrick_wall = registerDungeon("poison_stonebrick_wall", new WallBlock(Properties.ofFullCopy(poison_stonebrick)), SkiesDungeonType.POISON);
      aquatic_stone = registerDungeon("aquatic_stone", new Block(getDungeonStoneProperties()), SkiesDungeonType.AQUATIC);
      glowing_aquatic_stone = registerDungeon(
         "glowing_aquatic_stone", new Block(Properties.ofFullCopy(aquatic_stone).lightLevel(value -> 15)), SkiesDungeonType.AQUATIC
      );
      aquatic_stairs = registerDungeon(
         "aquatic_stairs", new StairBlock(aquatic_stone.defaultBlockState(), Properties.ofFullCopy(aquatic_stone)) {}, SkiesDungeonType.AQUATIC
      );
      glowing_aquatic_stairs = registerDungeon(
         "glowing_aquatic_stairs",
         new LitStairsBlock(() -> glowing_aquatic_stone.defaultBlockState(), Properties.ofFullCopy(glowing_aquatic_stone)),
         SkiesDungeonType.AQUATIC
      );
      aquatic_slab = registerDungeon("aquatic_slab", new SlabBlock(Properties.ofFullCopy(aquatic_stone)) {}, SkiesDungeonType.AQUATIC);
      glowing_aquatic_slab = registerDungeon("glowing_aquatic_slab", new LitSlabBlock(Properties.ofFullCopy(glowing_aquatic_stone)), SkiesDungeonType.AQUATIC);
      aquatic_wall = registerDungeon("aquatic_wall", new WallBlock(Properties.ofFullCopy(aquatic_stone)), SkiesDungeonType.AQUATIC);
      glowing_aquatic_wall = registerDungeon("glowing_aquatic_wall", new WallBlock(Properties.ofFullCopy(glowing_aquatic_stone)), SkiesDungeonType.AQUATIC);
      life_stone = registerDungeon("life_stone", new Block(getDungeonStoneProperties()), SkiesDungeonType.LIFE);
      glowing_life_stone = registerDungeon("glowing_life_stone", new Block(Properties.ofFullCopy(life_stone).lightLevel(value -> 15)), SkiesDungeonType.LIFE);
      life_stairs = registerDungeon(
         "life_stairs", new StairBlock(life_stone.defaultBlockState(), Properties.ofFullCopy(life_stone)) {}, SkiesDungeonType.LIFE
      );
      glowing_life_stairs = registerDungeon(
         "glowing_life_stairs",
         new LitStairsBlock(() -> glowing_life_stone.defaultBlockState(), Properties.ofFullCopy(glowing_life_stone)),
         SkiesDungeonType.LIFE
      );
      life_slab = registerDungeon("life_slab", new SlabBlock(Properties.ofFullCopy(life_stone)) {}, SkiesDungeonType.LIFE);
      glowing_life_slab = registerDungeon("glowing_life_slab", new LitSlabBlock(Properties.ofFullCopy(glowing_life_stone)), SkiesDungeonType.LIFE);
      life_wall = registerDungeon("life_wall", new WallBlock(Properties.ofFullCopy(life_stone)), SkiesDungeonType.LIFE);
      glowing_life_wall = registerDungeon("glowing_life_wall", new WallBlock(Properties.ofFullCopy(glowing_life_stone)), SkiesDungeonType.LIFE);
      bright_blinding_keystone = registerBlock("bright_blinding_keystone", new BlindingKeystoneBlock());
      dawn_blinding_keystone = registerBlock("dawn_blinding_keystone", new BlindingKeystoneBlock());
      nature_keystone = registerBlock("nature_keystone", new NatureKeystoneBlock());
      poison_keystone = registerBlock("poison_keystone", new PoisonKeystoneBlock());
      Properties trophyProp = Properties.ofFullCopy(Blocks.GOLD_BLOCK)
         .noOcclusion()
         .isValidSpawn((state, world, pos, entity) -> false)
         .isRedstoneConductor((state, world, pos) -> false)
         .isSuffocating((state, world, pos) -> false)
         .isViewBlocking((state, world, pos) -> false);
      net.minecraft.world.item.Item.Properties trophyItemProp = new net.minecraft.world.item.Item.Properties().stacksTo(1);
      summoner_trophy = registerItemProperties("summoner_trophy", new TrophyBlock(TrophyBlock.Type.SUMMONER, trophyProp), trophyItemProp);
      alchemist_trophy = registerItemProperties("alchemist_trophy", new TrophyBlock(TrophyBlock.Type.ALCHEMIST, trophyProp), trophyItemProp);
      starlit_crusher_trophy = registerItemProperties("starlit_crusher_trophy", new TrophyBlock(TrophyBlock.Type.STARLIT_CRUSHER, trophyProp), trophyItemProp);
      arachnarch_trophy = registerItemProperties("arachnarch_trophy", new TrophyBlock(TrophyBlock.Type.ARACHNARCH, trophyProp), trophyItemProp);
      bluebright_slab = register("bluebright_slab", new SlabBlock(Properties.ofFullCopy(bluebright_planks)) {});
      starlit_slab = register("starlit_slab", new SlabBlock(Properties.ofFullCopy(starlit_planks)) {});
      frostbright_slab = register("frostbright_slab", new SlabBlock(Properties.ofFullCopy(frostbright_planks)) {});
      lunar_slab = register("lunar_slab", new SlabBlock(Properties.ofFullCopy(lunar_planks)) {});
      dusk_slab = register("dusk_slab", new SlabBlock(Properties.ofFullCopy(dusk_planks)) {});
      maple_slab = register("maple_slab", new SlabBlock(Properties.ofFullCopy(maple_planks)) {});
      comet_slab = register("comet_slab", new SlabBlock(Properties.ofFullCopy(comet_planks)) {});
      crystallized_slab = register("crystallized_slab", new TranslucentSlabBlock(Properties.ofFullCopy(crystallized_planks), crystallizedPlanks));
      turquoise_stone_slab = register("turquoise_stone_slab", new SlabBlock(Properties.ofFullCopy(turquoise_stone)) {});
      turquoise_cobblestone_slab = register("turquoise_cobblestone_slab", new SlabBlock(Properties.ofFullCopy(turquoise_cobblestone)) {});
      mossy_turquoise_cobblestone_slab = register("mossy_turquoise_cobblestone_slab", new SlabBlock(Properties.ofFullCopy(mossy_turquoise_cobblestone)) {});
      turquoise_stonebrick_slab = register("turquoise_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(turquoise_stonebrick)) {});
      mossy_turquoise_stonebrick_slab = register("mossy_turquoise_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(mossy_turquoise_stonebrick)) {});
      cracked_turquoise_stonebrick_slab = register("cracked_turquoise_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(cracked_turquoise_stonebrick)) {});
      midnight_sandstone_slab = register("midnight_sandstone_slab", new SlabBlock(Properties.ofFullCopy(midnight_sandstone)) {});
      smooth_midnight_sandstone_slab = register("smooth_midnight_sandstone_slab", new SlabBlock(Properties.ofFullCopy(smooth_midnight_sandstone)) {});
      brumble_slab = register("brumble_slab", new SlabBlock(Properties.ofFullCopy(brumble_block)) {});
      polished_brumble_slab = register("polished_brumble_slab", new SlabBlock(Properties.ofFullCopy(polished_brumble)) {});
      polished_brumble_brick_slab = register("polished_brumble_brick_slab", new SlabBlock(Properties.ofFullCopy(polished_brumble_bricks)) {});
      taratite_slab = register("taratite_slab", new SlabBlock(Properties.ofFullCopy(taratite)) {});
      polished_taratite_slab = register("polished_taratite_slab", new SlabBlock(Properties.ofFullCopy(polished_taratite)) {});
      rimestone_slab = register("rimestone_slab", new SlabBlock(Properties.ofFullCopy(rimestone)) {});
      polished_rimestone_slab = register("polished_rimestone_slab", new SlabBlock(Properties.ofFullCopy(polished_rimestone)) {});
      lunar_stone_slab = register("lunar_stone_slab", new SlabBlock(Properties.ofFullCopy(lunar_stone)) {});
      lunar_cobblestone_slab = register("lunar_cobblestone_slab", new SlabBlock(Properties.ofFullCopy(lunar_cobblestone)) {});
      mossy_lunar_cobblestone_slab = register("mossy_lunar_cobblestone_slab", new SlabBlock(Properties.ofFullCopy(mossy_lunar_cobblestone)) {});
      lunar_stonebrick_slab = register("lunar_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(lunar_stonebrick)) {});
      mossy_lunar_stonebrick_slab = register("mossy_lunar_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(mossy_lunar_stonebrick)) {});
      cracked_lunar_stonebrick_slab = register("cracked_lunar_stonebrick_slab", new SlabBlock(Properties.ofFullCopy(cracked_lunar_stonebrick)) {});
      crystal_sandstone_slab = register("crystal_sandstone_slab", new SlabBlock(Properties.ofFullCopy(midnight_sandstone)) {});
      smooth_crystal_sandstone_slab = register("smooth_crystal_sandstone_slab", new SlabBlock(Properties.ofFullCopy(smooth_crystal_sandstone)) {});
      cut_midnight_sandstone_slab = register("cut_midnight_sandstone_slab", new SlabBlock(Properties.ofFullCopy(cut_midnight_sandstone)) {});
      cut_crystal_sandstone_slab = register("cut_crystal_sandstone_slab", new SlabBlock(Properties.ofFullCopy(cut_crystal_sandstone)) {});
      umber_slab = register("umber_slab", new SlabBlock(Properties.ofFullCopy(umber)) {});
      polished_umber_slab = register("polished_umber_slab", new SlabBlock(Properties.ofFullCopy(polished_umber)) {});
      cinderstone_slab = register("cinderstone_slab", new LitSlabBlock(Properties.ofFullCopy(cinderstone)));
      polished_cinderstone_slab = register("polished_cinderstone_slab", new LitSlabBlock(Properties.ofFullCopy(polished_cinderstone)));
      turquoise_cobblestone_wall = register("turquoise_cobblestone_wall", new WallBlock(Properties.ofFullCopy(turquoise_cobblestone)));
      mossy_turquoise_cobblestone_wall = register("mossy_turquoise_cobblestone_wall", new WallBlock(Properties.ofFullCopy(mossy_turquoise_cobblestone)));
      turquoise_stonebrick_wall = register("turquoise_stonebrick_wall", new WallBlock(Properties.ofFullCopy(turquoise_stonebrick)));
      mossy_turquoise_stonebrick_wall = register("mossy_turquoise_stonebrick_wall", new WallBlock(Properties.ofFullCopy(mossy_turquoise_stonebrick)));
      cracked_turquoise_stonebrick_wall = register("cracked_turquoise_stonebrick_wall", new WallBlock(Properties.ofFullCopy(cracked_turquoise_stonebrick)));
      brumble_wall = register("brumble_wall", new WallBlock(Properties.ofFullCopy(brumble_block)));
      polished_brumble_wall = register("polished_brumble_wall", new WallBlock(Properties.ofFullCopy(polished_brumble)));
      polished_brumble_brick_wall = register("polished_brumble_brick_wall", new WallBlock(Properties.ofFullCopy(polished_brumble_bricks)));
      taratite_wall = register("taratite_wall", new WallBlock(Properties.ofFullCopy(taratite)));
      polished_taratite_wall = register("polished_taratite_wall", new WallBlock(Properties.ofFullCopy(polished_taratite)));
      rimestone_wall = register("rimestone_wall", new WallBlock(Properties.ofFullCopy(rimestone)));
      polished_rimestone_wall = register("polished_rimestone_wall", new WallBlock(Properties.ofFullCopy(polished_rimestone)));
      lunar_cobblestone_wall = register("lunar_cobblestone_wall", new WallBlock(Properties.ofFullCopy(lunar_cobblestone)));
      mossy_lunar_cobblestone_wall = register("mossy_lunar_cobblestone_wall", new WallBlock(Properties.ofFullCopy(mossy_lunar_cobblestone)));
      lunar_stonebrick_wall = register("lunar_stonebrick_wall", new WallBlock(Properties.ofFullCopy(lunar_stonebrick)));
      mossy_lunar_stonebrick_wall = register("mossy_lunar_stonebrick_wall", new WallBlock(Properties.ofFullCopy(mossy_lunar_stonebrick)));
      cracked_lunar_stonebrick_wall = register("cracked_lunar_stonebrick_wall", new WallBlock(Properties.ofFullCopy(cracked_lunar_stonebrick)));
      midnight_sandstone_wall = register("midnight_sandstone_wall", new WallBlock(Properties.ofFullCopy(midnight_sandstone)));
      crystal_sandstone_wall = register("crystal_sandstone_wall", new WallBlock(Properties.ofFullCopy(crystal_sandstone)));
      crystallized_wall = register("crystallized_wall", new TranslucentWallBlock(Properties.ofFullCopy(crystallized_planks), crystallizedLogs));
      umber_wall = register("umber_wall", new WallBlock(Properties.ofFullCopy(umber)));
      polished_umber_wall = register("polished_umber_wall", new WallBlock(Properties.ofFullCopy(polished_umber)));
      cinderstone_wall = register("cinderstone_wall", new WallBlock(Properties.ofFullCopy(cinderstone)));
      polished_cinderstone_wall = register("polished_cinderstone_wall", new WallBlock(Properties.ofFullCopy(polished_cinderstone)));
      bluebright_stairs = register("bluebright_stairs", new StairBlock(bluebright_planks.defaultBlockState(), Properties.ofFullCopy(bluebright_planks)) {});
      starlit_stairs = register("starlit_stairs", new StairBlock(starlit_planks.defaultBlockState(), Properties.ofFullCopy(starlit_planks)) {});
      frostbright_stairs = register(
         "frostbright_stairs", new StairBlock(frostbright_planks.defaultBlockState(), Properties.ofFullCopy(frostbright_planks)) {}
      );
      lunar_stairs = register("lunar_stairs", new StairBlock(lunar_planks.defaultBlockState(), Properties.ofFullCopy(lunar_planks)) {});
      dusk_stairs = register("dusk_stairs", new StairBlock(dusk_planks.defaultBlockState(), Properties.ofFullCopy(dusk_planks)) {});
      maple_stairs = register("maple_stairs", new StairBlock(maple_planks.defaultBlockState(), Properties.ofFullCopy(maple_planks)) {});
      comet_stairs = register("comet_stairs", new StairBlock(comet_planks.defaultBlockState(), Properties.ofFullCopy(comet_planks)) {});
      crystallized_stairs = register(
         "crystallized_stairs",
         new TranslucentStairsBlock(() -> crystallized_planks.defaultBlockState(), Properties.ofFullCopy(crystallized_planks), crystallizedPlanks)
      );
      turquoise_stone_stairs = register(
         "turquoise_stone_stairs", new StairBlock(turquoise_stone.defaultBlockState(), Properties.ofFullCopy(turquoise_stone)) {}
      );
      turquoise_stonebrick_stairs = register(
         "turquoise_stonebrick_stairs", new StairBlock(turquoise_stonebrick.defaultBlockState(), Properties.ofFullCopy(turquoise_stonebrick)) {}
      );
      mossy_turquoise_stonebrick_stairs = register(
         "mossy_turquoise_stonebrick_stairs",
         new StairBlock(mossy_turquoise_stonebrick.defaultBlockState(), Properties.ofFullCopy(mossy_turquoise_stonebrick)) {}
      );
      cracked_turquoise_stonebrick_stairs = register(
         "cracked_turquoise_stonebrick_stairs",
         new StairBlock(cracked_turquoise_stonebrick.defaultBlockState(), Properties.ofFullCopy(cracked_turquoise_stonebrick)) {}
      );
      turquoise_cobblestone_stairs = register(
         "turquoise_cobblestone_stairs", new StairBlock(turquoise_cobblestone.defaultBlockState(), Properties.ofFullCopy(turquoise_cobblestone)) {}
      );
      mossy_turquoise_cobblestone_stairs = register(
         "mossy_turquoise_cobblestone_stairs",
         new StairBlock(mossy_turquoise_cobblestone.defaultBlockState(), Properties.ofFullCopy(mossy_turquoise_cobblestone)) {}
      );
      midnight_sandstone_stairs = register(
         "midnight_sandstone_stairs", new StairBlock(midnight_sandstone.defaultBlockState(), Properties.ofFullCopy(midnight_sandstone)) {}
      );
      smooth_midnight_sandstone_stairs = register(
         "smooth_midnight_sandstone_stairs",
         new StairBlock(smooth_midnight_sandstone.defaultBlockState(), Properties.ofFullCopy(smooth_midnight_sandstone)) {}
      );
      brumble_stairs = register("brumble_stairs", new StairBlock(brumble_block.defaultBlockState(), Properties.ofFullCopy(brumble_block)) {});
      polished_brumble_stairs = register(
         "polished_brumble_stairs", new StairBlock(polished_brumble.defaultBlockState(), Properties.ofFullCopy(polished_brumble)) {}
      );
      polished_brumble_brick_stairs = register(
         "polished_brumble_brick_stairs", new StairBlock(polished_brumble_bricks.defaultBlockState(), Properties.ofFullCopy(polished_brumble_bricks)) {}
      );
      taratite_stairs = register("taratite_stairs", new StairBlock(taratite.defaultBlockState(), Properties.ofFullCopy(taratite)) {});
      polished_taratite_stairs = register(
         "polished_taratite_stairs", new StairBlock(polished_taratite.defaultBlockState(), Properties.ofFullCopy(polished_taratite)) {}
      );
      rimestone_stairs = register("rimestone_stairs", new StairBlock(rimestone.defaultBlockState(), Properties.ofFullCopy(rimestone)) {});
      polished_rimestone_stairs = register(
         "polished_rimestone_stairs", new StairBlock(polished_rimestone.defaultBlockState(), Properties.ofFullCopy(polished_rimestone)) {}
      );
      lunar_stone_stairs = register("lunar_stone_stairs", new StairBlock(lunar_stone.defaultBlockState(), Properties.ofFullCopy(lunar_stone)) {});
      lunar_stonebrick_stairs = register(
         "lunar_stonebrick_stairs", new StairBlock(lunar_stonebrick.defaultBlockState(), Properties.ofFullCopy(lunar_stonebrick)) {}
      );
      mossy_lunar_stonebrick_stairs = register(
         "mossy_lunar_stonebrick_stairs", new StairBlock(mossy_lunar_stonebrick.defaultBlockState(), Properties.ofFullCopy(mossy_lunar_stonebrick)) {}
      );
      cracked_lunar_stonebrick_stairs = register(
         "cracked_lunar_stonebrick_stairs", new StairBlock(cracked_lunar_stonebrick.defaultBlockState(), Properties.ofFullCopy(cracked_lunar_stonebrick)) {}
      );
      lunar_cobblestone_stairs = register(
         "lunar_cobblestone_stairs", new StairBlock(lunar_cobblestone.defaultBlockState(), Properties.ofFullCopy(lunar_cobblestone)) {}
      );
      mossy_lunar_cobblestone_stairs = register(
         "mossy_lunar_cobblestone_stairs", new StairBlock(mossy_lunar_cobblestone.defaultBlockState(), Properties.ofFullCopy(mossy_lunar_cobblestone)) {}
      );
      crystal_sandstone_stairs = register(
         "crystal_sandstone_stairs", new StairBlock(crystal_sandstone.defaultBlockState(), Properties.ofFullCopy(crystal_sandstone)) {}
      );
      smooth_crystal_sandstone_stairs = register(
         "smooth_crystal_sandstone_stairs", new StairBlock(smooth_crystal_sandstone.defaultBlockState(), Properties.ofFullCopy(smooth_crystal_sandstone)) {}
      );
      umber_stairs = register("umber_stairs", new StairBlock(umber.defaultBlockState(), Properties.ofFullCopy(umber)) {});
      polished_umber_stairs = register("polished_umber_stairs", new StairBlock(polished_umber.defaultBlockState(), Properties.ofFullCopy(polished_umber)) {});
      cinderstone_stairs = register("cinderstone_stairs", new LitStairsBlock(() -> cinderstone.defaultBlockState(), Properties.ofFullCopy(cinderstone)));
      polished_cinderstone_stairs = register(
         "polished_cinderstone_stairs", new LitStairsBlock(() -> polished_cinderstone.defaultBlockState(), Properties.ofFullCopy(polished_cinderstone))
      );
      potted_baneful_mushroom = registerBlock(
         "potted_baneful_mushroom", new SkyBanefulPotBlock(() -> baneful_mushroom, Properties.ofFullCopy(Blocks.POTTED_POPPY))
      );
      potted_snowcap_mushroom = registerBlock(
         "potted_snowcap_mushroom", new SkySnowcapPotBlock(() -> snowcap_mushroom, Properties.ofFullCopy(Blocks.POTTED_POPPY).randomTicks())
      );
      potted_brewberry_bush = registerBlock(
         "potted_brewberry_bush", new SkyBrewberryBushPotBlock(() -> brewberry_bush, Properties.ofFullCopy(Blocks.POTTED_POPPY).randomTicks())
      );
      potted_camellia = registerBlock("potted_camellia", new SkyFlowerPotBlock(() -> camellia));
      potted_snowbloom = registerBlock("potted_snowbloom", new SkyFlowerPotBlock(() -> snowbloom));
      potted_briskbloom = registerBlock("potted_briskbloom", new SkyFlowerPotBlock(() -> briskbloom));
      potted_blush_blossom = registerBlock("potted_blush_blossom", new SkyFlowerPotBlock(() -> blush_blossom));
      potted_polar_posy = registerBlock("potted_polar_posy", new SkyFlowerPotBlock(() -> polar_posy));
      potted_chillweed = registerBlock("potted_chillweed", new SkyFlowerPotBlock(() -> chillweed));
      potted_brittlebush = registerBlock("potted_brittlebush", new SkyFlowerPotBlock(() -> brittlebush));
      potted_frose = registerBlock("potted_frose", new SkyFlowerPotBlock(() -> frose));
      potted_midday_bayhop = registerBlock("potted_midday_bayhop", new SkyFlowerPotBlock(() -> midday_bayhop));
      potted_moonlit_bloom = registerBlock("potted_moonlit_bloom", new SkyFlowerPotBlock(() -> moonlit_bloom));
      potted_crystal_flower = registerBlock("potted_crystal_flower", new SkyFlowerPotBlock(() -> crystal_flower));
      potted_blaze_bud = registerBlock("potted_blaze_bud", new SkyFlowerPotBlock(() -> blaze_bud));
      potted_flare_floret = registerBlock("potted_flare_floret", new SkyFlowerPotBlock(() -> flare_floret));
      potted_nightcress = registerBlock("potted_nightcress", new SkyFlowerPotBlock(() -> nightcress));
      potted_muckweed = registerBlock("potted_muckweed", new SkyFlowerPotBlock(() -> muckweed));
      potted_lucentroot = registerBlock("potted_lucentroot", new SkyFlowerPotBlock(() -> lucentroot));
      potted_bluebright_sapling = registerBlock("potted_bluebright_sapling", new SkyFlowerPotBlock(() -> bluebright_sapling));
      potted_starlit_sapling = registerBlock("potted_starlit_sapling", new SkyFlowerPotBlock(() -> starlit_sapling));
      potted_frostbright_sapling = registerBlock("potted_frostbright_sapling", new SkyFlowerPotBlock(() -> frostbright_sapling));
      potted_lunar_sapling = registerBlock("potted_lunar_sapling", new SkyFlowerPotBlock(() -> lunar_sapling));
      potted_dusk_sapling = registerBlock("potted_dusk_sapling", new SkyFlowerPotBlock(() -> dusk_sapling));
      potted_maple_sapling = registerBlock("potted_maple_sapling", new SkyFlowerPotBlock(() -> maple_sapling));
      potted_comet_sapling = registerBlock("potted_comet_sapling", new SkyFlowerPotBlock(() -> comet_sapling));
      potted_crescent_fruit_sapling = registerBlock("potted_crescent_fruit_sapling", new SkyFlowerPotBlock(() -> crescent_fruit_sapling));
      star_emitter = register(
         "star_emitter",
         new StarEmitterBlock(Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F).sound(SoundType.METAL))
      );
      trough = register("trough", new TroughBlock(Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
      tool_box = register("tool_box", new ToolBoxBlock(Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
      bag_of_spoils = registerItemProperties(
         "bag_of_spoils",
         new BagOfSpoilsBlock(Properties.of().strength(0.4F).sound(SoundType.WOOL)),
         new net.minecraft.world.item.Item.Properties().stacksTo(1)
      );
      summoning_table = register(
         "summoning_table", new SummoningTableBlock(Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD))
      );
      alchemy_table = register(
         "alchemy_table", new AlchemyTableBlock(Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD))
      );
      food_prep_table = register(
         "food_prep_table", new FoodPrepTableBlock(Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD))
      );
      winter_leaves = registerBlock("winter_leaves", new SkyCropBlock(() -> SkiesItems.winter_leaf_seeds, 10.0));
      fiery_beans = registerBlock("fiery_beans", new SkyCropBlock(() -> SkiesItems.fiery_bean_seeds, 14.0));
      solnuts = registerBlock("solnuts", new SkyCropBlock(() -> SkiesItems.solnut, 13.0));
      cryo_roots = registerBlock("cryo_roots", new SkyCropBlock(() -> SkiesItems.cryo_root, 8.0));
      scalefruits = registerBlock("scalefruits", new SkyDoubleCropBlock(() -> SkiesItems.scalefruit_seeds, 16.0));
      pine_fruits = registerBlock("pine_fruits", new SkyDoubleCropBlock(() -> SkiesItems.pine_fruit_seeds, 16.0));
      if (FMLEnvironment.dist == Dist.CLIENT) {
         SkiesEmissives.initBlocks();
      }

      SkiesMappingChanges.addBlockAliases(event.getRegistry(Registries.BLOCK));
   }

   public static <B extends Block> B register(String key, B block) {
      blockItemList.add(block);
      return registerBlock(key, block);
   }

   public static <B extends Block> B registerItemProperties(String key, B block, net.minecraft.world.item.Item.Properties itemProperties) {
      blockItemPropertiesMap.put(block, itemProperties);
      return registerBlock(key, block);
   }

   public static <B extends Block> B registerBlock(String key, B block) {
      registry.register(Registries.BLOCK, BlueSkies.locate(key), () -> block);
      return block;
   }

   public static <B extends Block> B registerDungeon(String key, B block, SkiesDungeonType dungeonType) {
      dungeonBlockItemMap.put(block, dungeonType);
      return registerBlock(key, block);
   }

   public static Properties getOreProperties() {
      return Properties.of().instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops();
   }

   public static Properties getDungeonStoneProperties() {
      return Properties.of().instrument(NoteBlockInstrument.BASEDRUM).strength(5.0F).requiresCorrectToolForDrops();
   }

   public static Properties getOreBlockProperties() {
      return Properties.of().instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 6.0F).requiresCorrectToolForDrops();
   }

   private static DoorBlock door(Properties props, BlockSetType blockSet) {
      return new DoorBlock(blockSet, props) {};
   }

   private static TrapDoorBlock trapDoor(Properties props, BlockSetType blockSet) {
      return new TrapDoorBlock(blockSet, props) {};
   }

   private static FenceGateBlock fenceGate(Properties props, WoodType woodType) {
      return new FenceGateBlock(woodType, props) {};
   }
}
