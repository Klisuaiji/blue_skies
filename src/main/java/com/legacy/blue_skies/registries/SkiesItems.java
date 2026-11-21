package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.events.SkiesMappingChanges;
import com.legacy.blue_skies.items.BlackBrewberryItem;
import com.legacy.blue_skies.items.BlueJournalItem;
import com.legacy.blue_skies.items.BossLootBagItem;
import com.legacy.blue_skies.items.BossSpawnEggItem;
import com.legacy.blue_skies.items.LargeLilyPadItem;
import com.legacy.blue_skies.items.LootBagItem;
import com.legacy.blue_skies.items.PreparedFoodItem;
import com.legacy.blue_skies.items.SkiesMusicDiscItem;
import com.legacy.blue_skies.items.SkyArmorItem;
import com.legacy.blue_skies.items.SkyPlantableBlockItem;
import com.legacy.blue_skies.items.arcs.AquaticArcItem;
import com.legacy.blue_skies.items.arcs.DuskArcItem;
import com.legacy.blue_skies.items.arcs.EtherealArcItem;
import com.legacy.blue_skies.items.arcs.LifeArcItem;
import com.legacy.blue_skies.items.arcs.NatureArcItem;
import com.legacy.blue_skies.items.arcs.PoisonArcItem;
import com.legacy.blue_skies.items.arcs.RunicArcItem;
import com.legacy.blue_skies.items.block.DungeonBlockItem;
import com.legacy.blue_skies.items.block.KeystoneBlockItem;
import com.legacy.blue_skies.items.block.SkyChestBlockItem;
import com.legacy.blue_skies.items.buckets.VentiumBucketItem;
import com.legacy.blue_skies.items.buckets.VentiumMilkBucketItem;
import com.legacy.blue_skies.items.buckets.VentiumMobBucketItem;
import com.legacy.blue_skies.items.tools.AstrolabeItem;
import com.legacy.blue_skies.items.tools.SkyAxeItem;
import com.legacy.blue_skies.items.tools.SkyHoeItem;
import com.legacy.blue_skies.items.tools.SkyPickaxeItem;
import com.legacy.blue_skies.items.tools.SkyShieldItem;
import com.legacy.blue_skies.items.tools.SkyShovelItem;
import com.legacy.blue_skies.items.tools.ZealLighterItem;
import com.legacy.blue_skies.items.tools.weapons.AlchemyScrollItem;
import com.legacy.blue_skies.items.tools.weapons.CrushingHammerItem;
import com.legacy.blue_skies.items.tools.weapons.InfusedArcSwordItem;
import com.legacy.blue_skies.items.tools.weapons.SkySwordItem;
import com.legacy.blue_skies.items.tools.weapons.SpearItem;
import com.legacy.blue_skies.items.tools.weapons.SpikeShieldItem;
import com.legacy.blue_skies.items.tools.weapons.SummoningTomeItem;
import com.legacy.blue_skies.items.tools.weapons.VenomSacItem;
import com.legacy.blue_skies.items.util.ColoredNameItem;
import com.legacy.blue_skies.items.util.SkiesArmorMaterial;
import com.legacy.blue_skies.items.util.SkiesItemTier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesItems {
   public static Item comet_berries;
   public static Item crescent_fruit;
   public static Item brewberry;
   public static Item pink_brewberry;
   public static Item black_brewberry;
   public static Item pine_fruit;
   public static Item cryo_root;
   public static Item winter_leaves;
   public static Item scalefruit;
   public static Item fiery_beans;
   public static Item solnut;
   public static Item winter_leaf_seeds;
   public static Item fiery_bean_seeds;
   public static Item scalefruit_seeds;
   public static Item pine_fruit_seeds;
   public static Item carabeef;
   public static Item cooked_carabeef;
   public static Item monitor_tail;
   public static Item cooked_monitor_tail;
   public static PreparedFoodItem prepared_food;
   public static Item azulfo_horn;
   public static Item fox_pelt;
   public static Item bug_guts;
   public static Item soul_fragment;
   public static Item pearl;
   public static Item venison;
   public static Item cooked_venison;
   public static Item grittle_flatfish;
   public static Item cooked_grittle_flatfish;
   public static Item municipal_monkfish;
   public static Item cooked_municipal_monkfish;
   public static Item charscale_moki;
   public static Item cooked_charscale_moki;
   public static Item horizofin_tunid;
   public static Item cooked_horizofin_tunid;
   public static Item bluebright_stick;
   public static Item starlit_stick;
   public static Item frostbright_stick;
   public static Item lunar_stick;
   public static Item dusk_stick;
   public static Item maple_stick;
   public static Item comet_stick;
   public static Item diopside_gem;
   public static Item pyrope_gem;
   public static Item aquite;
   public static Item charoite;
   public static Item moonstone_shard;
   public static Item falsite_ingot;
   public static Item ventium_ingot;
   public static Item horizonite_ingot;
   public static Item falsite_nugget;
   public static Item ventium_nugget;
   public static Item horizonite_nugget;
   public static Item raw_falsite;
   public static Item raw_ventium;
   public static Item raw_horizonite;
   public static Item raw_aquite;
   public static Item raw_charoite;
   public static Item bluebright_sword;
   public static Item bluebright_pickaxe;
   public static Item bluebright_axe;
   public static Item bluebright_shovel;
   public static Item bluebright_hoe;
   public static Item lunar_sword;
   public static Item lunar_pickaxe;
   public static Item lunar_axe;
   public static Item lunar_shovel;
   public static Item lunar_hoe;
   public static Item starlit_sword;
   public static Item starlit_pickaxe;
   public static Item starlit_axe;
   public static Item starlit_shovel;
   public static Item starlit_hoe;
   public static Item dusk_sword;
   public static Item dusk_pickaxe;
   public static Item dusk_axe;
   public static Item dusk_shovel;
   public static Item dusk_hoe;
   public static Item frostbright_sword;
   public static Item frostbright_pickaxe;
   public static Item frostbright_axe;
   public static Item frostbright_shovel;
   public static Item frostbright_hoe;
   public static Item maple_sword;
   public static Item maple_pickaxe;
   public static Item maple_axe;
   public static Item maple_shovel;
   public static Item maple_hoe;
   public static Item comet_sword;
   public static Item comet_pickaxe;
   public static Item comet_axe;
   public static Item comet_shovel;
   public static Item comet_hoe;
   public static Item turquoise_stone_sword;
   public static Item turquoise_stone_pickaxe;
   public static Item turquoise_stone_axe;
   public static Item turquoise_stone_shovel;
   public static Item turquoise_stone_hoe;
   public static Item lunar_stone_sword;
   public static Item lunar_stone_pickaxe;
   public static Item lunar_stone_axe;
   public static Item lunar_stone_shovel;
   public static Item lunar_stone_hoe;
   public static Item diopside_sword;
   public static Item diopside_pickaxe;
   public static Item diopside_axe;
   public static Item diopside_shovel;
   public static Item diopside_hoe;
   public static Item pyrope_sword;
   public static Item pyrope_pickaxe;
   public static Item pyrope_axe;
   public static Item pyrope_shovel;
   public static Item pyrope_hoe;
   public static Item aquite_sword;
   public static Item aquite_pickaxe;
   public static Item aquite_axe;
   public static Item aquite_shovel;
   public static Item aquite_hoe;
   public static Item charoite_sword;
   public static Item charoite_pickaxe;
   public static Item charoite_axe;
   public static Item charoite_shovel;
   public static Item charoite_hoe;
   public static Item horizonite_sword;
   public static Item horizonite_pickaxe;
   public static Item horizonite_axe;
   public static Item horizonite_shovel;
   public static Item horizonite_hoe;
   public static Item bluebright_spear;
   public static Item starlit_spear;
   public static Item frostbright_spear;
   public static Item lunar_spear;
   public static Item dusk_spear;
   public static Item maple_spear;
   public static Item comet_spear;
   public static Item moonstone_shield;
   public static Item soulbound_spear;
   public static Item spike_shield;
   public static Item different_sword;
   public static Item infused_arc_sword;
   public static Item summoning_tome;
   public static Item alchemy_scroll;
   public static Item crushing_hammer;
   public static Item venom_sac;
   public static Item diopside_helmet;
   public static Item diopside_chestplate;
   public static Item diopside_leggings;
   public static Item diopside_boots;
   public static Item pyrope_helmet;
   public static Item pyrope_chestplate;
   public static Item pyrope_leggings;
   public static Item pyrope_boots;
   public static Item aquite_helmet;
   public static Item aquite_chestplate;
   public static Item aquite_leggings;
   public static Item aquite_boots;
   public static Item shadow_helmet;
   public static Item shadow_chestplate;
   public static Item shadow_leggings;
   public static Item shadow_boots;
   public static Item charoite_helmet;
   public static Item charoite_chestplate;
   public static Item charoite_leggings;
   public static Item charoite_boots;
   public static Item horizonite_helmet;
   public static Item horizonite_chestplate;
   public static Item horizonite_leggings;
   public static Item horizonite_boots;
   public static Item blinding_rage_record;
   public static Item defying_starlight_record;
   public static Item venomous_encounter_record;
   public static Item population_record;
   public static Item zeal_lighter;
   public static Item ventium_bucket;
   public static Item ventium_water_bucket;
   public static Item ventium_lava_bucket;
   public static Item ventium_milk_bucket;
   public static Item ventium_drifter_bucket;
   public static Item ventium_grittle_flatfish_bucket;
   public static Item ventium_municipal_monkfish_bucket;
   public static Item ventium_charscale_moki_bucket;
   public static Item ventium_horizofin_tunid_bucket;
   public static Item ventium_shears;
   public static Item blinding_key;
   public static Item nature_key;
   public static Item poison_key;
   public static Item aquatic_key;
   public static Item life_key;
   public static Item debug_sword;
   public static Item multi_portal_item;
   public static EtherealArcItem ethereal_arc;
   public static DuskArcItem dusk_arc;
   public static PoisonArcItem poison_arc;
   public static NatureArcItem nature_arc;
   public static AquaticArcItem aquatic_arc;
   public static LifeArcItem life_arc;
   public static RunicArcItem runic_arc;
   public static Item lost_page;
   public static Item blue_journal;
   public static Item camel_saddle;
   public static Item astrolabe;
   public static Item gatekeeper_spawn_egg;
   public static LootBagItem loot_bag;
   public static LootBagItem loot_bag_summoner;
   public static LootBagItem loot_bag_alchemist;
   public static LootBagItem loot_bag_starlit_crusher;
   public static LootBagItem loot_bag_arachnarch;
   public static Item azulfo_spawn_egg;
   public static Item stardust_ram_spawn_egg;
   public static Item reindeer_spawn_egg;
   public static Item frost_spirit_spawn_egg;
   public static Item armored_frost_spirit_spawn_egg;
   public static Item crynocerous_spawn_egg;
   public static Item jelly_drifter_spawn_egg;
   public static Item snow_owl_spawn_egg;
   public static Item polargeist_spawn_egg;
   public static Item artificial_golem_spawn_egg;
   public static Item whistleshell_crab_spawn_egg;
   public static Item seclam_spawn_egg;
   public static Item shrumpty_spawn_egg;
   public static Item diophyde_prowler_spawn_egg;
   public static Item grittle_flatfish_spawn_egg;
   public static Item municipal_monkfish_spawn_egg;
   public static Item stonelet_spawn_egg;
   public static Item spewter_spawn_egg;
   public static Item summoner_spawn_egg;
   public static Item starlit_crusher_spawn_egg;
   public static Item nyctofly_spawn_egg;
   public static Item venom_spider_spawn_egg;
   public static Item firefly_spawn_egg;
   public static Item cosmic_fox_spawn_egg;
   public static Item crystal_camel_spawn_egg;
   public static Item emberback_spawn_egg;
   public static Item infested_swarmer_spawn_egg;
   public static Item shade_monitor_spawn_egg;
   public static Item sliv_spawn_egg;
   public static Item crogre_spawn_egg;
   public static Item charscale_moki_spawn_egg;
   public static Item horizofin_tunid_spawn_egg;
   public static Item nested_spider_spawn_egg;
   public static Item alchemist_spawn_egg;
   public static Item arachnarch_spawn_egg;
   public static Item bluebright_door;
   public static Item starlit_door;
   public static Item frostbright_door;
   public static Item lunar_door;
   public static Item dusk_door;
   public static Item maple_door;
   public static Item comet_door;
   public static Item crystallized_door;
   public static Item chilled_lily_pad;
   public static Item moonlit_water_lily;
   public static Item warding_pearl;
   public static Item bluebright_chest;
   public static Item starlit_chest;
   public static Item frostbright_chest;
   public static Item lunar_chest;
   public static Item dusk_chest;
   public static Item maple_chest;
   public static Item comet_chest;
   public static Item bluebright_sign;
   public static Item starlit_sign;
   public static Item frostbright_sign;
   public static Item lunar_sign;
   public static Item dusk_sign;
   public static Item maple_sign;
   public static Item comet_sign;
   public static Item bluebright_hanging_sign;
   public static Item starlit_hanging_sign;
   public static Item frostbright_hanging_sign;
   public static Item lunar_hanging_sign;
   public static Item dusk_hanging_sign;
   public static Item maple_hanging_sign;
   public static Item comet_hanging_sign;
   public static Item bright_blinding_keystone;
   public static Item dawn_blinding_keystone;
   public static Item nature_keystone;
   public static Item poison_keystone;
   public static Item banished_trim;
   public static Item thwarted_trim;
   public static Item regrowth_trim;
   public static Item toxic_trim;
   private static RegisterEvent registry;
   public static List<Item> coloredTools = new ArrayList<>();

   public static void init(RegisterEvent event) {
      registry = event;
      azulfo_spawn_egg = register("azulfo_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.AZULFO, 2832967, 6587281, new Properties()));
      stardust_ram_spawn_egg = register(
         "stardust_ram_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.STARDUST_RAM, 16777215, 8780543, new Properties())
      );
      reindeer_spawn_egg = register("reindeer_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.REINDEER, 6907378, 16777215, new Properties()));
      jelly_drifter_spawn_egg = register(
         "jelly_drifter_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.JELLY_DRIFTER, 9231575, 13107199, new Properties())
      );
      snow_owl_spawn_egg = register("snow_owl_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.SNOW_OWL, 14348799, 5286340, new Properties()));
      frost_spirit_spawn_egg = register(
         "frost_spirit_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.FROST_SPIRIT, 4020336, 13625855, new Properties())
      );
      armored_frost_spirit_spawn_egg = register(
         "armored_frost_spirit_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.ARMORED_FROST_SPIRIT, 9484287, 13163775, new Properties())
      );
      crynocerous_spawn_egg = register(
         "crynocerous_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.CRYNOCEROUS, 4020336, 16777215, new Properties())
      );
      polargeist_spawn_egg = register("polargeist_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.POLARGEIST, 15263991, 10146018, new Properties()));
      whistleshell_crab_spawn_egg = register(
         "whistleshell_crab_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.WHISTLESHELL_CRAB, 9850711, 14732712, new Properties())
      );
      seclam_spawn_egg = register("seclam_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.SECLAM, 13001065, 12354635, new Properties()));
      shrumpty_spawn_egg = register("shrumpty_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.SHRUMPTY, 6928346, 16380645, new Properties()));
      diophyde_prowler_spawn_egg = register(
         "diophyde_prowler_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.DIOPHYDE_PROWLER, 12700099, 4100528, new Properties())
      );
      grittle_flatfish_spawn_egg = register(
         "grittle_flatfish_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.GRITTLE_FLATFISH, 2435633, 10851922, new Properties())
      );
      municipal_monkfish_spawn_egg = register(
         "municipal_monkfish_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.MUNICIPAL_MONKFISH, 2919577, 3782284, new Properties())
      );
      artificial_golem_spawn_egg = register(
         "artificial_golem_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.ARTIFICIAL_GOLEM, 12502988, 6264831, new Properties())
      );
      stonelet_spawn_egg = register("stonelet_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.STONELET, 4365427, 16765440, new Properties()));
      spewter_spawn_egg = register("spewter_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.SPEWTER, 8350460, 16346558, new Properties()));
      firefly_spawn_egg = register("firefly_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.FIREFLY, 3282448, 13754368, new Properties()));
      cosmic_fox_spawn_egg = register("cosmic_fox_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.COSMIC_FOX, 10377197, 13943018, new Properties()));
      crystal_camel_spawn_egg = register(
         "crystal_camel_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.CRYSTAL_CAMEL, 16777215, 14594815, new Properties())
      );
      shade_monitor_spawn_egg = register(
         "shade_monitor_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.SHADE_MONITOR, 6496651, 12620783, new Properties())
      );
      sliv_spawn_egg = register("sliv_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.SLIV, 11474264, 7868833, new Properties()));
      crogre_spawn_egg = register("crogre_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.CROGRE, 11907216, 10731357, new Properties()));
      charscale_moki_spawn_egg = register(
         "charscale_moki_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.CHARSCALE_MOKI, 6827288, 15383154, new Properties())
      );
      horizofin_tunid_spawn_egg = register(
         "horizofin_tunid_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.HORIZOFIN_TUNID, 15707724, 14833766, new Properties())
      );
      nyctofly_spawn_egg = register("nyctofly_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.NYCTOFLY, 3286336, 10344160, new Properties()));
      venom_spider_spawn_egg = register(
         "venom_spider_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.VENOM_SPIDER, 1449231, 3693379, new Properties())
      );
      emberback_spawn_egg = register("emberback_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.EMBERBACK, 1121327, 16746242, new Properties()));
      infested_swarmer_spawn_egg = register(
         "infested_swarmer_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.INFESTED_SWARMER, 1645355, 9222862, new Properties())
      );
      nested_spider_spawn_egg = register(
         "nested_spider_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.NESTED_SPIDER, 2565970, 4696663, new Properties())
      );
      gatekeeper_spawn_egg = register("gatekeeper_spawn_egg", new DeferredSpawnEggItem(() -> SkiesEntityTypes.GATEKEEPER, 16777215, 16777215, new Properties()));
      summoner_spawn_egg = register(
         "summoner_spawn_egg", new BossSpawnEggItem(() -> SkiesEntityTypes.SUMMONER, 16777215, 16777215, new Properties().rarity(Rarity.EPIC))
      );
      alchemist_spawn_egg = register(
         "alchemist_spawn_egg", new BossSpawnEggItem(() -> SkiesEntityTypes.ALCHEMIST, 16777215, 16777215, new Properties().rarity(Rarity.EPIC))
      );
      starlit_crusher_spawn_egg = register(
         "starlit_crusher_spawn_egg", new BossSpawnEggItem(() -> SkiesEntityTypes.STARLIT_CRUSHER, 16777215, 16777215, new Properties().rarity(Rarity.EPIC))
      );
      arachnarch_spawn_egg = register(
         "arachnarch_spawn_egg", new BossSpawnEggItem(() -> SkiesEntityTypes.ARACHNARCH, 16777215, 16777215, new Properties().rarity(Rarity.EPIC))
      );
      comet_berries = register("comet_berries", new Item(new Properties().food(SkiesFoods.COMET_BERRY)));
      winter_leaves = register("winter_leaves", new Item(new Properties().food(SkiesFoods.WINTER_LEAVES)));
      fiery_beans = register("fiery_beans", new Item(new Properties().food(SkiesFoods.FIERY_BEAN)));
      solnut = register("solnut", new SkyPlantableBlockItem(SkiesBlocks.solnuts, new Properties().food(SkiesFoods.SOLNUT)));
      cryo_root = register("cryo_root", new SkyPlantableBlockItem(SkiesBlocks.cryo_roots, new Properties().food(SkiesFoods.CRYO_ROOT)));
      scalefruit = register("scalefruit", new Item(new Properties().food(SkiesFoods.SCALEFRUIT)));
      pine_fruit = register("pine_fruit", new Item(new Properties().food(SkiesFoods.PINE_FRUIT)));
      crescent_fruit = register("crescent_fruit", new Item(new Properties().food(SkiesFoods.CRESCENT_FRUIT)));
      brewberry = register("brewberry", new Item(new Properties().food(SkiesFoods.BREWBERRY)));
      pink_brewberry = register("pink_brewberry", new Item(new Properties().food(SkiesFoods.PINK_BREWBERRY)));
      black_brewberry = register("black_brewberry", new BlackBrewberryItem(new Properties().food(SkiesFoods.BLACK_BREWBERRY)));
      carabeef = register("carabeef", new Item(new Properties().food(SkiesFoods.CARABEEF)));
      cooked_carabeef = register("cooked_carabeef", new Item(new Properties().food(SkiesFoods.COOKED_CARABEEF)));
      venison = register("venison", new Item(new Properties().food(SkiesFoods.VENISON)));
      cooked_venison = register("cooked_venison", new Item(new Properties().food(SkiesFoods.COOKED_VENISON)));
      monitor_tail = register("monitor_tail", new Item(new Properties().food(SkiesFoods.MONTIOR_TAIL)));
      cooked_monitor_tail = register("cooked_monitor_tail", new Item(new Properties().food(SkiesFoods.COOKED_MONTIOR_TAIL)));
      prepared_food = register("prepared_food", new PreparedFoodItem(new Properties().stacksTo(1)));
      grittle_flatfish = register("grittle_flatfish", new Item(new Properties().food(SkiesFoods.GRITTLE_FLATFISH)));
      cooked_grittle_flatfish = register("cooked_grittle_flatfish", new Item(new Properties().food(SkiesFoods.COOKED_GRITTLE_FLATFISH)));
      municipal_monkfish = register("municipal_monkfish", new Item(new Properties().food(SkiesFoods.FISH)));
      cooked_municipal_monkfish = register("cooked_municipal_monkfish", new Item(new Properties().food(SkiesFoods.COOKED_FISH)));
      charscale_moki = register("charscale_moki", new Item(new Properties().food(SkiesFoods.FISH)));
      cooked_charscale_moki = register("cooked_charscale_moki", new Item(new Properties().food(SkiesFoods.COOKED_FISH)));
      horizofin_tunid = register("horizofin_tunid", new Item(new Properties().food(SkiesFoods.FISH)));
      cooked_horizofin_tunid = register("cooked_horizofin_tunid", new Item(new Properties().food(SkiesFoods.COOKED_FISH)));
      zeal_lighter = register("zeal_lighter", new ZealLighterItem(new Properties().stacksTo(1).durability(250)));
      blinding_key = register("blinding_key", new ColoredNameItem(ChatFormatting.GRAY, new Properties().stacksTo(4)));
      nature_key = register("nature_key", new ColoredNameItem(ChatFormatting.GREEN, new Properties().stacksTo(4)));
      poison_key = register("poison_key", new ColoredNameItem(ChatFormatting.LIGHT_PURPLE, new Properties().stacksTo(4)));
      bluebright_stick = register("bluebright_stick", new Item(new Properties()));
      starlit_stick = register("starlit_stick", new Item(new Properties()));
      frostbright_stick = register("frostbright_stick", new Item(new Properties()));
      lunar_stick = register("lunar_stick", new Item(new Properties()));
      dusk_stick = register("dusk_stick", new Item(new Properties()));
      maple_stick = register("maple_stick", new Item(new Properties()));
      comet_stick = register("comet_stick", new Item(new Properties()));
      azulfo_horn = register("azulfo_horn", new Item(new Properties()));
      fox_pelt = register("fox_pelt", new Item(new Properties()));
      bug_guts = register("bug_guts", new Item(new Properties()));
      soul_fragment = register("soul_fragment", new Item(new Properties()));
      pearl = register("pearl", new Item(new Properties()));
      moonstone_shard = register("moonstone_shard", new Item(new Properties()));
      pyrope_gem = register("pyrope_gem", new Item(new Properties()));
      aquite = register("aquite", new Item(new Properties()));
      diopside_gem = register("diopside_gem", new Item(new Properties()));
      charoite = register("charoite", new Item(new Properties()));
      falsite_ingot = register("falsite_ingot", new Item(new Properties()));
      ventium_ingot = register("ventium_ingot", new Item(new Properties()));
      horizonite_ingot = register("horizonite_ingot", new Item(new Properties()));
      raw_aquite = register("raw_aquite", new Item(new Properties()));
      raw_charoite = register("raw_charoite", new Item(new Properties()));
      raw_falsite = register("raw_falsite", new Item(new Properties()));
      raw_ventium = register("raw_ventium", new Item(new Properties()));
      raw_horizonite = register("raw_horizonite", new Item(new Properties()));
      falsite_nugget = register("falsite_nugget", new Item(new Properties()));
      ventium_nugget = register("ventium_nugget", new Item(new Properties()));
      horizonite_nugget = register("horizonite_nugget", new Item(new Properties()));
      ethereal_arc = register("ethereal_arc", new EtherealArcItem(0));
      dusk_arc = register("dusk_arc", new DuskArcItem(1));
      nature_arc = register("nature_arc", new NatureArcItem(2));
      poison_arc = register("poison_arc", new PoisonArcItem(3));
      aquatic_arc = register("aquatic_arc", new AquaticArcItem(4));
      life_arc = register("life_arc", new LifeArcItem(5));
      runic_arc = register("runic_arc", new RunicArcItem(6));
      blue_journal = register("blue_journal", new BlueJournalItem());
      ventium_shears = register("ventium_shears", new ShearsItem(new Properties().durability(238)));
      ventium_bucket = register("ventium_bucket", new VentiumBucketItem(() -> Fluids.EMPTY, new Properties().stacksTo(16)));
      ventium_water_bucket = register(
         "ventium_water_bucket", new VentiumBucketItem(() -> Fluids.WATER, new Properties().stacksTo(1).craftRemainder(ventium_bucket))
      );
      ventium_lava_bucket = register(
         "ventium_lava_bucket", new VentiumBucketItem(() -> Fluids.LAVA, new Properties().stacksTo(1).craftRemainder(ventium_bucket))
      );
      ventium_milk_bucket = register("ventium_milk_bucket", new VentiumMilkBucketItem(new Properties().stacksTo(1).craftRemainder(ventium_bucket)));
      ventium_drifter_bucket = register(
         "ventium_drifter_bucket",
         new VentiumMobBucketItem(
            () -> SkiesEntityTypes.JELLY_DRIFTER,
            () -> SoundEvents.BUCKET_FILL_FISH,
            () -> Fluids.WATER,
            new Properties().stacksTo(1).craftRemainder(ventium_bucket)
         )
      );
      ventium_grittle_flatfish_bucket = register(
         "ventium_grittle_flatfish_bucket",
         new VentiumMobBucketItem(
            () -> SkiesEntityTypes.GRITTLE_FLATFISH,
            () -> SoundEvents.BUCKET_FILL_FISH,
            () -> Fluids.WATER,
            new Properties().stacksTo(1).craftRemainder(ventium_bucket)
         )
      );
      ventium_municipal_monkfish_bucket = register(
         "ventium_municipal_monkfish_bucket",
         new VentiumMobBucketItem(
            () -> SkiesEntityTypes.MUNICIPAL_MONKFISH,
            () -> SoundEvents.BUCKET_FILL_FISH,
            () -> Fluids.WATER,
            new Properties().stacksTo(1).craftRemainder(ventium_bucket)
         )
      );
      ventium_charscale_moki_bucket = register(
         "ventium_charscale_moki_bucket",
         new VentiumMobBucketItem(
            () -> SkiesEntityTypes.CHARSCALE_MOKI,
            () -> SoundEvents.BUCKET_FILL_FISH,
            () -> Fluids.WATER,
            new Properties().stacksTo(1).craftRemainder(ventium_bucket)
         )
      );
      ventium_horizofin_tunid_bucket = register(
         "ventium_horizofin_tunid_bucket",
         new VentiumMobBucketItem(
            () -> SkiesEntityTypes.HORIZOFIN_TUNID,
            () -> SoundEvents.BUCKET_FILL_FISH,
            () -> Fluids.WATER,
            new Properties().stacksTo(1).craftRemainder(ventium_bucket)
         )
      );
      camel_saddle = register("camel_saddle", new Item(new Properties().stacksTo(1)));
      astrolabe = register("astrolabe", new AstrolabeItem(new Properties().stacksTo(1)));
      loot_bag = register("loot_bag", new LootBagItem(new Properties().stacksTo(1)));
      loot_bag_summoner = register("loot_bag_summoner", new BossLootBagItem(new Properties().stacksTo(1), () -> SkiesEntityTypes.SUMMONER));
      loot_bag_alchemist = register("loot_bag_alchemist", new BossLootBagItem(new Properties().stacksTo(1), () -> SkiesEntityTypes.ALCHEMIST));
      loot_bag_starlit_crusher = register("loot_bag_starlit_crusher", new BossLootBagItem(new Properties().stacksTo(1), () -> SkiesEntityTypes.STARLIT_CRUSHER));
      loot_bag_arachnarch = register("loot_bag_arachnarch", new BossLootBagItem(new Properties().stacksTo(1), () -> SkiesEntityTypes.ARACHNARCH));
      soulbound_spear = register("soulbound_spear", new SpearItem(new Properties().durability(450), () -> lunar_stick));
      spike_shield = register("spike_shield", new SpikeShieldItem(new Properties().durability(850)));
      summoning_tome = register("summoning_tome", new SummoningTomeItem(new Properties().durability(250)));
      alchemy_scroll = register("alchemy_scroll", new AlchemyScrollItem(new Properties().durability(250)));
      crushing_hammer = register("crushing_hammer", new CrushingHammerItem(new Properties().durability(250)));
      venom_sac = register("venom_sac", new VenomSacItem(new Properties().durability(250)));
      infused_arc_sword = register("infused_arc_sword", new InfusedArcSwordItem(SkiesItemTier.INFUSED, 4, -2.4F, new Properties().rarity(Rarity.RARE)));
      different_sword = register("different_sword", new SkySwordItem(SkiesItemTier.DIFFERENT, 4, -2.4F, new Properties().rarity(Rarity.UNCOMMON)));
      debug_sword = register("debug_sword", new SkySwordItem(SkiesItemTier.DEVELOPER, 3, 20.0F, new Properties().rarity(Rarity.EPIC)));
      bluebright_sword = registerTool("bluebright_sword", new SkySwordItem(SkiesItemTier.WOOD, 3, -2.4F, new Properties()));
      bluebright_pickaxe = registerTool("bluebright_pickaxe", new SkyPickaxeItem(SkiesItemTier.WOOD, 1, -2.8F, new Properties()));
      bluebright_axe = registerTool("bluebright_axe", new SkyAxeItem(SkiesItemTier.WOOD, 6.0F, -3.2F, new Properties()));
      bluebright_shovel = registerTool("bluebright_shovel", new SkyShovelItem(SkiesItemTier.WOOD, 1.5F, -3.0F, new Properties()));
      bluebright_hoe = registerTool("bluebright_hoe", new SkyHoeItem(SkiesItemTier.WOOD, -3.0F, new Properties()));
      lunar_sword = registerTool("lunar_sword", new SkySwordItem(SkiesItemTier.WOOD, 3, -2.4F, new Properties()));
      lunar_pickaxe = registerTool("lunar_pickaxe", new SkyPickaxeItem(SkiesItemTier.WOOD, 1, -2.8F, new Properties()));
      lunar_axe = registerTool("lunar_axe", new SkyAxeItem(SkiesItemTier.WOOD, 6.0F, -3.2F, new Properties()));
      lunar_shovel = registerTool("lunar_shovel", new SkyShovelItem(SkiesItemTier.WOOD, 1.5F, -3.0F, new Properties()));
      lunar_hoe = registerTool("lunar_hoe", new SkyHoeItem(SkiesItemTier.WOOD, -3.0F, new Properties()));
      starlit_sword = registerTool("starlit_sword", new SkySwordItem(SkiesItemTier.WOOD, 3, -2.4F, new Properties()));
      starlit_pickaxe = registerTool("starlit_pickaxe", new SkyPickaxeItem(SkiesItemTier.WOOD, 1, -2.8F, new Properties()));
      starlit_axe = registerTool("starlit_axe", new SkyAxeItem(SkiesItemTier.WOOD, 6.0F, -3.2F, new Properties()));
      starlit_shovel = registerTool("starlit_shovel", new SkyShovelItem(SkiesItemTier.WOOD, 1.5F, -3.0F, new Properties()));
      starlit_hoe = registerTool("starlit_hoe", new SkyHoeItem(SkiesItemTier.WOOD, -3.0F, new Properties()));
      dusk_sword = registerTool("dusk_sword", new SkySwordItem(SkiesItemTier.WOOD, 3, -2.4F, new Properties()));
      dusk_pickaxe = registerTool("dusk_pickaxe", new SkyPickaxeItem(SkiesItemTier.WOOD, 1, -2.8F, new Properties()));
      dusk_axe = registerTool("dusk_axe", new SkyAxeItem(SkiesItemTier.WOOD, 6.0F, -3.2F, new Properties()));
      dusk_shovel = registerTool("dusk_shovel", new SkyShovelItem(SkiesItemTier.WOOD, 1.5F, -3.0F, new Properties()));
      dusk_hoe = registerTool("dusk_hoe", new SkyHoeItem(SkiesItemTier.WOOD, -3.0F, new Properties()));
      frostbright_sword = registerTool("frostbright_sword", new SkySwordItem(SkiesItemTier.WOOD, 3, -2.4F, new Properties()));
      frostbright_pickaxe = registerTool("frostbright_pickaxe", new SkyPickaxeItem(SkiesItemTier.WOOD, 1, -2.8F, new Properties()));
      frostbright_axe = registerTool("frostbright_axe", new SkyAxeItem(SkiesItemTier.WOOD, 6.0F, -3.2F, new Properties()));
      frostbright_shovel = registerTool("frostbright_shovel", new SkyShovelItem(SkiesItemTier.WOOD, 1.5F, -3.0F, new Properties()));
      frostbright_hoe = registerTool("frostbright_hoe", new SkyHoeItem(SkiesItemTier.WOOD, -3.0F, new Properties()));
      maple_sword = registerTool("maple_sword", new SkySwordItem(SkiesItemTier.WOOD, 3, -2.4F, new Properties()));
      maple_pickaxe = registerTool("maple_pickaxe", new SkyPickaxeItem(SkiesItemTier.WOOD, 1, -2.8F, new Properties()));
      maple_axe = registerTool("maple_axe", new SkyAxeItem(SkiesItemTier.WOOD, 6.0F, -3.2F, new Properties()));
      maple_shovel = registerTool("maple_shovel", new SkyShovelItem(SkiesItemTier.WOOD, 1.5F, -3.0F, new Properties()));
      maple_hoe = registerTool("maple_hoe", new SkyHoeItem(SkiesItemTier.WOOD, -3.0F, new Properties()));
      comet_sword = registerTool("comet_sword", new SkySwordItem(SkiesItemTier.COMET, 3, -2.4F, new Properties()));
      comet_pickaxe = registerTool("comet_pickaxe", new SkyPickaxeItem(SkiesItemTier.COMET, 1, -2.8F, new Properties()));
      comet_axe = registerTool("comet_axe", new SkyAxeItem(SkiesItemTier.COMET, 6.0F, -3.2F, new Properties()));
      comet_shovel = registerTool("comet_shovel", new SkyShovelItem(SkiesItemTier.COMET, 1.5F, -3.0F, new Properties()));
      comet_hoe = registerTool("comet_hoe", new SkyHoeItem(SkiesItemTier.COMET, -3.0F, new Properties()));
      turquoise_stone_sword = registerTool("turquoise_stone_sword", new SkySwordItem(SkiesItemTier.TURQUOISE_STONE, 3, -2.4F, new Properties()));
      turquoise_stone_pickaxe = registerTool("turquoise_stone_pickaxe", new SkyPickaxeItem(SkiesItemTier.TURQUOISE_STONE, 1, -2.8F, new Properties()));
      turquoise_stone_axe = registerTool("turquoise_stone_axe", new SkyAxeItem(SkiesItemTier.TURQUOISE_STONE, 7.0F, -3.2F, new Properties()));
      turquoise_stone_shovel = registerTool("turquoise_stone_shovel", new SkyShovelItem(SkiesItemTier.TURQUOISE_STONE, 1.5F, -3.0F, new Properties()));
      turquoise_stone_hoe = registerTool("turquoise_stone_hoe", new SkyHoeItem(SkiesItemTier.TURQUOISE_STONE, -2.0F, new Properties()));
      lunar_stone_sword = registerTool("lunar_stone_sword", new SkySwordItem(SkiesItemTier.LUNAR_STONE, 3, -2.4F, new Properties()));
      lunar_stone_pickaxe = registerTool("lunar_stone_pickaxe", new SkyPickaxeItem(SkiesItemTier.LUNAR_STONE, 1, -2.8F, new Properties()));
      lunar_stone_axe = registerTool("lunar_stone_axe", new SkyAxeItem(SkiesItemTier.LUNAR_STONE, 7.0F, -3.2F, new Properties()));
      lunar_stone_shovel = registerTool("lunar_stone_shovel", new SkyShovelItem(SkiesItemTier.LUNAR_STONE, 1.5F, -3.0F, new Properties()));
      lunar_stone_hoe = registerTool("lunar_stone_hoe", new SkyHoeItem(SkiesItemTier.LUNAR_STONE, -2.0F, new Properties()));
      pyrope_sword = registerTool("pyrope_sword", new SkySwordItem(SkiesItemTier.PYROPE, 3, -2.0F, new Properties()));
      pyrope_pickaxe = registerTool("pyrope_pickaxe", new SkyPickaxeItem(SkiesItemTier.PYROPE, 1, -2.8F, new Properties()));
      pyrope_axe = registerTool("pyrope_axe", new SkyAxeItem(SkiesItemTier.PYROPE, 6.0F, -3.0F, new Properties()));
      pyrope_shovel = registerTool("pyrope_shovel", new SkyShovelItem(SkiesItemTier.PYROPE, 1.5F, -3.0F, new Properties()));
      pyrope_hoe = registerTool("pyrope_hoe", new SkyHoeItem(SkiesItemTier.PYROPE, -3.0F, new Properties()));
      aquite_sword = registerTool("aquite_sword", new SkySwordItem(SkiesItemTier.AQUITE, 3, -2.4F, new Properties()));
      aquite_pickaxe = registerTool("aquite_pickaxe", new SkyPickaxeItem(SkiesItemTier.AQUITE, 1, -2.8F, new Properties()));
      aquite_axe = registerTool("aquite_axe", new SkyAxeItem(SkiesItemTier.AQUITE, 6.0F, -3.1F, new Properties()));
      aquite_shovel = registerTool("aquite_shovel", new SkyShovelItem(SkiesItemTier.AQUITE, 1.5F, -3.0F, new Properties()));
      aquite_hoe = registerTool("aquite_hoe", new SkyHoeItem(SkiesItemTier.AQUITE, -1.0F, new Properties()));
      diopside_sword = registerTool("diopside_sword", new SkySwordItem(SkiesItemTier.DIOPSIDE, 3, -2.8F, new Properties()));
      diopside_pickaxe = registerTool("diopside_pickaxe", new SkyPickaxeItem(SkiesItemTier.DIOPSIDE, 1, -2.8F, new Properties()));
      diopside_axe = registerTool("diopside_axe", new SkyAxeItem(SkiesItemTier.DIOPSIDE, 6.0F, -3.5F, new Properties()));
      diopside_shovel = registerTool("diopside_shovel", new SkyShovelItem(SkiesItemTier.DIOPSIDE, 1.5F, -3.0F, new Properties()));
      diopside_hoe = registerTool("diopside_hoe", new SkyHoeItem(SkiesItemTier.DIOPSIDE, 0.0F, new Properties()));
      charoite_sword = registerTool("charoite_sword", new SkySwordItem(SkiesItemTier.CHAROITE, 3, -2.4F, new Properties()));
      charoite_pickaxe = registerTool("charoite_pickaxe", new SkyPickaxeItem(SkiesItemTier.CHAROITE, 1, -2.8F, new Properties()));
      charoite_axe = registerTool("charoite_axe", new SkyAxeItem(SkiesItemTier.CHAROITE, 5.0F, -3.0F, new Properties()));
      charoite_shovel = registerTool("charoite_shovel", new SkyShovelItem(SkiesItemTier.CHAROITE, 1.5F, -3.0F, new Properties()));
      charoite_hoe = registerTool("charoite_hoe", new SkyHoeItem(SkiesItemTier.CHAROITE, 0.0F, new Properties()));
      horizonite_sword = registerTool("horizonite_sword", new SkySwordItem(SkiesItemTier.HORIZONITE, 3, -2.4F, new Properties()));
      horizonite_pickaxe = registerTool("horizonite_pickaxe", new SkyPickaxeItem(SkiesItemTier.HORIZONITE, 1, -2.8F, new Properties()));
      horizonite_axe = registerTool("horizonite_axe", new SkyAxeItem(SkiesItemTier.HORIZONITE, 6.0F, -3.0F, new Properties()));
      horizonite_shovel = registerTool("horizonite_shovel", new SkyShovelItem(SkiesItemTier.HORIZONITE, 1.5F, -3.0F, new Properties()));
      horizonite_hoe = registerTool("horizonite_hoe", new SkyHoeItem(SkiesItemTier.HORIZONITE, -1.0F, new Properties()));
      bluebright_spear = register("bluebright_spear", new SpearItem(new Properties().durability(250), () -> bluebright_stick));
      starlit_spear = register("starlit_spear", new SpearItem(new Properties().durability(250), () -> starlit_stick));
      frostbright_spear = register("frostbright_spear", new SpearItem(new Properties().durability(250), () -> frostbright_stick));
      lunar_spear = register("lunar_spear", new SpearItem(new Properties().durability(250), () -> lunar_stick));
      dusk_spear = register("dusk_spear", new SpearItem(new Properties().durability(250), () -> dusk_stick));
      maple_spear = register("maple_spear", new SpearItem(new Properties().durability(250), () -> maple_stick));
      comet_spear = register("comet_spear", new SpearItem(new Properties().durability(250), () -> comet_stick));
      moonstone_shield = register("moonstone_shield", new SkyShieldItem(new Properties().durability(460)));
      Type helmet = Type.HELMET;
      Type chestplate = Type.CHESTPLATE;
      Type leggings = Type.LEGGINGS;
      Type boots = Type.BOOTS;
      pyrope_helmet = register("pyrope_helmet", new SkyArmorItem(SkiesArmorMaterial.PYROPE, helmet, new Properties()));
      pyrope_chestplate = register("pyrope_chestplate", new SkyArmorItem(SkiesArmorMaterial.PYROPE, chestplate, new Properties()));
      pyrope_leggings = register("pyrope_leggings", new SkyArmorItem(SkiesArmorMaterial.PYROPE, leggings, new Properties()));
      pyrope_boots = register("pyrope_boots", new SkyArmorItem(SkiesArmorMaterial.PYROPE, boots, new Properties()));
      aquite_helmet = register("aquite_helmet", new SkyArmorItem(SkiesArmorMaterial.AQUITE, helmet, new Properties()));
      aquite_chestplate = register("aquite_chestplate", new SkyArmorItem(SkiesArmorMaterial.AQUITE, chestplate, new Properties()));
      aquite_leggings = register("aquite_leggings", new SkyArmorItem(SkiesArmorMaterial.AQUITE, leggings, new Properties()));
      aquite_boots = register("aquite_boots", new SkyArmorItem(SkiesArmorMaterial.AQUITE, boots, new Properties()));
      diopside_helmet = register("diopside_helmet", new SkyArmorItem(SkiesArmorMaterial.DIOPSIDE, helmet, new Properties()));
      diopside_chestplate = register("diopside_chestplate", new SkyArmorItem(SkiesArmorMaterial.DIOPSIDE, chestplate, new Properties()));
      diopside_leggings = register("diopside_leggings", new SkyArmorItem(SkiesArmorMaterial.DIOPSIDE, leggings, new Properties()));
      diopside_boots = register("diopside_boots", new SkyArmorItem(SkiesArmorMaterial.DIOPSIDE, boots, new Properties()));
      charoite_helmet = register("charoite_helmet", new SkyArmorItem(SkiesArmorMaterial.CHAROITE, helmet, new Properties()));
      charoite_chestplate = register("charoite_chestplate", new SkyArmorItem(SkiesArmorMaterial.CHAROITE, chestplate, new Properties()));
      charoite_leggings = register("charoite_leggings", new SkyArmorItem(SkiesArmorMaterial.CHAROITE, leggings, new Properties()));
      charoite_boots = register("charoite_boots", new SkyArmorItem(SkiesArmorMaterial.CHAROITE, boots, new Properties()));
      horizonite_helmet = register("horizonite_helmet", new SkyArmorItem(SkiesArmorMaterial.HORIZONITE, helmet, new Properties()));
      horizonite_chestplate = register("horizonite_chestplate", new SkyArmorItem(SkiesArmorMaterial.HORIZONITE, chestplate, new Properties()));
      horizonite_leggings = register("horizonite_leggings", new SkyArmorItem(SkiesArmorMaterial.HORIZONITE, leggings, new Properties()));
      horizonite_boots = register("horizonite_boots", new SkyArmorItem(SkiesArmorMaterial.HORIZONITE, boots, new Properties()));
      shadow_helmet = register("shadow_helmet", new SkyArmorItem(SkiesArmorMaterial.SHADOW, helmet, new Properties().rarity(Rarity.RARE)));
      shadow_chestplate = register("shadow_chestplate", new SkyArmorItem(SkiesArmorMaterial.SHADOW, chestplate, new Properties().rarity(Rarity.RARE)));
      shadow_leggings = register("shadow_leggings", new SkyArmorItem(SkiesArmorMaterial.SHADOW, leggings, new Properties().rarity(Rarity.RARE)));
      shadow_boots = register("shadow_boots", new SkyArmorItem(SkiesArmorMaterial.SHADOW, boots, new Properties().rarity(Rarity.RARE)));
      blinding_rage_record = register(
         "blinding_rage",
         new SkiesMusicDiscItem(SkiesSounds.RECORDS_BLINDING_RAGE, "Jonathing", "Blinding Rage", 221, new Properties().stacksTo(1).rarity(Rarity.RARE))
      );
      defying_starlight_record = register(
         "defying_starlight",
         new SkiesMusicDiscItem(SkiesSounds.RECORDS_DEFYING_STARLIGHT, "Jonathing", "Defying Starlight", 136, new Properties().stacksTo(1).rarity(Rarity.RARE))
      );
      venomous_encounter_record = register(
         "venomous_encounter",
         new SkiesMusicDiscItem(
            SkiesSounds.RECORDS_VENOMOUS_ENCOUNTER, "Jonathing", "Venomous Encounter", 140, new Properties().stacksTo(1).rarity(Rarity.RARE)
         )
      );
      population_record = register(
         "population", new SkiesMusicDiscItem(SkiesSounds.RECORDS_POPULATION, "Lachney", "Population", 214, new Properties().stacksTo(1).rarity(Rarity.RARE))
      );
      winter_leaf_seeds = register("winter_leaf_seeds", new SkyPlantableBlockItem(SkiesBlocks.winter_leaves, new Properties()));
      fiery_bean_seeds = register("fiery_bean_seeds", new SkyPlantableBlockItem(SkiesBlocks.fiery_beans, new Properties()));
      scalefruit_seeds = register("scalefruit_seeds", new SkyPlantableBlockItem(SkiesBlocks.scalefruits, new Properties()));
      pine_fruit_seeds = register("pine_fruit_seeds", new SkyPlantableBlockItem(SkiesBlocks.pine_fruits, new Properties()));
      multi_portal_item = register("multi_portal_item", new Item(new Properties().rarity(Rarity.EPIC)));
      bright_blinding_keystone = register(
         "bright_blinding_keystone", new KeystoneBlockItem(SkiesBlocks.bright_blinding_keystone, new Properties(), SkiesDungeonType.BLINDING)
      );
      dawn_blinding_keystone = register(
         "dawn_blinding_keystone", new KeystoneBlockItem(SkiesBlocks.dawn_blinding_keystone, new Properties(), SkiesDungeonType.BLINDING)
      );
      nature_keystone = register("nature_keystone", new KeystoneBlockItem(SkiesBlocks.nature_keystone, new Properties(), SkiesDungeonType.NATURE));
      poison_keystone = register("poison_keystone", new KeystoneBlockItem(SkiesBlocks.poison_keystone, new Properties(), SkiesDungeonType.POISON));
      registerBlockItems();
      bluebright_door = register("bluebright_door", new DoubleHighBlockItem(SkiesBlocks.bluebright_door, new Properties()));
      starlit_door = register("starlit_door", new DoubleHighBlockItem(SkiesBlocks.starlit_door, new Properties()));
      frostbright_door = register("frostbright_door", new DoubleHighBlockItem(SkiesBlocks.frostbright_door, new Properties()));
      lunar_door = register("lunar_door", new DoubleHighBlockItem(SkiesBlocks.lunar_door, new Properties()));
      dusk_door = register("dusk_door", new DoubleHighBlockItem(SkiesBlocks.dusk_door, new Properties()));
      maple_door = register("maple_door", new DoubleHighBlockItem(SkiesBlocks.maple_door, new Properties()));
      comet_door = register("comet_door", new DoubleHighBlockItem(SkiesBlocks.comet_door, new Properties()));
      crystallized_door = register("crystallized_door", new DoubleHighBlockItem(SkiesBlocks.crystallized_door, new Properties()));
      bluebright_chest = register("bluebright_chest", new SkyChestBlockItem(SkiesBlocks.bluebright_chest, new Properties()));
      starlit_chest = register("starlit_chest", new SkyChestBlockItem(SkiesBlocks.starlit_chest, new Properties()));
      frostbright_chest = register("frostbright_chest", new SkyChestBlockItem(SkiesBlocks.frostbright_chest, new Properties()));
      lunar_chest = register("lunar_chest", new SkyChestBlockItem(SkiesBlocks.lunar_chest, new Properties()));
      dusk_chest = register("dusk_chest", new SkyChestBlockItem(SkiesBlocks.dusk_chest, new Properties()));
      maple_chest = register("maple_chest", new SkyChestBlockItem(SkiesBlocks.maple_chest, new Properties()));
      comet_chest = register("comet_chest", new SkyChestBlockItem(SkiesBlocks.comet_chest, new Properties()));
      bluebright_sign = register("bluebright_sign", new SignItem(new Properties(), SkiesBlocks.bluebright_sign, SkiesBlocks.bluebright_wall_sign));
      starlit_sign = register("starlit_sign", new SignItem(new Properties(), SkiesBlocks.starlit_sign, SkiesBlocks.starlit_wall_sign));
      frostbright_sign = register("frostbright_sign", new SignItem(new Properties(), SkiesBlocks.frostbright_sign, SkiesBlocks.frostbright_wall_sign));
      lunar_sign = register("lunar_sign", new SignItem(new Properties(), SkiesBlocks.lunar_sign, SkiesBlocks.lunar_wall_sign));
      dusk_sign = register("dusk_sign", new SignItem(new Properties(), SkiesBlocks.dusk_sign, SkiesBlocks.dusk_wall_sign));
      maple_sign = register("maple_sign", new SignItem(new Properties(), SkiesBlocks.maple_sign, SkiesBlocks.maple_wall_sign));
      comet_sign = register("comet_sign", new SignItem(new Properties(), SkiesBlocks.comet_sign, SkiesBlocks.comet_wall_sign));
      bluebright_hanging_sign = register(
         "bluebright_hanging_sign", new HangingSignItem(SkiesBlocks.bluebright_hanging_sign, SkiesBlocks.bluebright_wall_hanging_sign, new Properties())
      );
      starlit_hanging_sign = register(
         "starlit_hanging_sign", new HangingSignItem(SkiesBlocks.starlit_hanging_sign, SkiesBlocks.starlit_wall_hanging_sign, new Properties())
      );
      frostbright_hanging_sign = register(
         "frostbright_hanging_sign", new HangingSignItem(SkiesBlocks.frostbright_hanging_sign, SkiesBlocks.frostbright_wall_hanging_sign, new Properties())
      );
      lunar_hanging_sign = register(
         "lunar_hanging_sign", new HangingSignItem(SkiesBlocks.lunar_hanging_sign, SkiesBlocks.lunar_wall_hanging_sign, new Properties())
      );
      dusk_hanging_sign = register(
         "dusk_hanging_sign", new HangingSignItem(SkiesBlocks.dusk_hanging_sign, SkiesBlocks.dusk_wall_hanging_sign, new Properties())
      );
      maple_hanging_sign = register(
         "maple_hanging_sign", new HangingSignItem(SkiesBlocks.maple_hanging_sign, SkiesBlocks.maple_wall_hanging_sign, new Properties())
      );
      comet_hanging_sign = register(
         "comet_hanging_sign", new HangingSignItem(SkiesBlocks.comet_hanging_sign, SkiesBlocks.comet_wall_hanging_sign, new Properties())
      );
      chilled_lily_pad = register("chilled_lily_pad", new PlaceOnWaterBlockItem(SkiesBlocks.chilled_lily_pad, new Properties()));
      moonlit_water_lily = register("moonlit_water_lily", new LargeLilyPadItem(SkiesBlocks.moonlit_water_lily, new Properties()));
      warding_pearl = register(
         "warding_pearl", new StandingAndWallBlockItem(SkiesBlocks.warding_pearl, SkiesBlocks.wall_warding_pearl, new Properties(), Direction.DOWN)
      );
      banished_trim = register("banished_armor_trim_smithing_template", SmithingTemplateItem.createArmorTrimTemplate(SkiesArmorTrims.Pattterns.BANISHED));
      thwarted_trim = register("thwarted_armor_trim_smithing_template", SmithingTemplateItem.createArmorTrimTemplate(SkiesArmorTrims.Pattterns.THWARTED));
      regrowth_trim = register("regrowth_armor_trim_smithing_template", SmithingTemplateItem.createArmorTrimTemplate(SkiesArmorTrims.Pattterns.REGROWTH));
      toxic_trim = register("toxic_armor_trim_smithing_template", SmithingTemplateItem.createArmorTrimTemplate(SkiesArmorTrims.Pattterns.TOXIC));
      SkiesMappingChanges.addItemAliases(event.getRegistry(Registries.ITEM));
   }

   private static void registerBlockItems() {
      for (Block block : SkiesBlocks.blockItemList) {
         registry.register(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block), () -> new BlockItem(block, new Properties()));
      }

      SkiesBlocks.blockItemList.clear();

      for (Entry<Block, Properties> entry : SkiesBlocks.blockItemPropertiesMap.entrySet()) {
         Block block = entry.getKey();
         registry.register(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block), () -> new BlockItem(block, entry.getValue()));
      }

      SkiesBlocks.blockItemPropertiesMap.clear();

      for (Entry<Block, SkiesDungeonType> entry : SkiesBlocks.dungeonBlockItemMap.entrySet()) {
         Block block = entry.getKey();
         registry.register(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block), () -> new DungeonBlockItem(block, new Properties(), entry.getValue()));
      }

      SkiesBlocks.dungeonBlockItemMap.clear();
   }

   private static <T extends Item> T registerTool(String name, T item) {
      coloredTools.add(item);
      return register(name, item);
   }

   private static <T extends Item> T register(String name, T item) {
      registry.register(Registries.ITEM, BlueSkies.locate(name), () -> item);
      return item;
   }
}
