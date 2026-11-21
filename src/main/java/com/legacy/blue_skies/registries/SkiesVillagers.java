package com.legacy.blue_skies.registries;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.entities.villager.SkiesVillagerTrades;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import com.legacy.structure_gel.api.util.GelCollectors;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesVillagers {
   public static final Lazy<VillagerProfession> STARGAZER = createProfession(
      "stargazer", SkiesPoiTypes.STAR_EMITTER, ImmutableSet.of(), ImmutableSet.of(), () -> SkiesSounds.ENTITY_VILLAGER_WORK_STARGAZER
   );
   public static final Lazy<VillagerProfession> WRANGLER = createProfession(
      "wrangler", SkiesPoiTypes.TROUGH, ImmutableSet.of(), ImmutableSet.of(), () -> SkiesSounds.ENTITY_VILLAGER_WORK_WRANGLER
   );
   public static final Lazy<VillagerProfession> SHOVELER = createProfession(
      "shoveler",
      SkiesPoiTypes.TOOL_BOX,
      ImmutableSet.of(),
      ImmutableSet.of(SkiesBlocks.coarse_turquoise_dirt, Blocks.SNOW),
      () -> SkiesSounds.ENTITY_VILLAGER_WORK_SHOVELER
   );
   public static final Lazy<VillagerProfession> NIGHTWATCHER = createProfession(
      "nightwatcher", SkiesPoiTypes.BAG_OF_SPOILS, ImmutableSet.of(), ImmutableSet.of(), () -> SkiesSounds.ENTITY_VILLAGER_WORK_NIGHTWATCHER
   );
   public static final Lazy<VillagerProfession> SUMMONER = createProfession(
      "summoner", SkiesPoiTypes.SUMMONING_TABLE, ImmutableSet.of(), ImmutableSet.of(), () -> SkiesSounds.ENTITY_VILLAGER_WORK_SUMMONER
   );
   public static final Lazy<VillagerProfession> ALCHEMIST = createProfession(
      "alchemist", SkiesPoiTypes.ALCHEMY_TABLE, ImmutableSet.of(), ImmutableSet.of(), () -> SkiesSounds.ENTITY_VILLAGER_WORK_ALCHEMIST
   );
   public static final Set<VillagerType> SKIES_TYPES = Sets.newHashSet();
   public static VillagerType CALMING_SKIES;
   public static VillagerType BRIGHTLANDS;
   public static VillagerType SLUSHLANDS;
   public static VillagerType UNORTHODOX_VALLEY;
   public static VillagerType SHADED_WOODLANDS;
   public static VillagerType CRYSTAL_DUNES;
   public static VillagerType SUNSET_MAPLE_FOREST;

   public static void init(RegisterEvent event) {
      registerProf(event, "stargazer", STARGAZER);
      registerProf(event, "wrangler", WRANGLER);
      registerProf(event, "shoveler", SHOVELER);
      registerProf(event, "nightwatcher", NIGHTWATCHER);
      registerProf(event, "summoner", SUMMONER);
      registerProf(event, "alchemist", ALCHEMIST);
      registerTrades(STARGAZER, SkiesVillagerTrades.STARGAZER_OFFERS);
      registerTrades(WRANGLER, SkiesVillagerTrades.WRANGLER_OFFERS);
      registerTrades(SHOVELER, SkiesVillagerTrades.SHOVELER_OFFERS);
      registerTrades(NIGHTWATCHER, SkiesVillagerTrades.NIGHTWATCHER_OFFERS);
      registerTrades(SUMMONER, SkiesVillagerTrades.SUMMONER_OFFERS);
      registerTrades(ALCHEMIST, SkiesVillagerTrades.ALCHEMIST_OFFERS);
      registerGifts(STARGAZER, SkiesLootProv.STARGAZER_GIFTS);
      registerGifts(WRANGLER, SkiesLootProv.WRANGLER_GIFTS);
      registerGifts(SHOVELER, SkiesLootProv.SHOVELER_GIFTS);
      registerGifts(SUMMONER, SkiesLootProv.SUMMONER_GIFTS);
      registerGifts(ALCHEMIST, SkiesLootProv.ALCHEMIST_GIFTS);
      initFoodItem(SkiesItems.solnut, 1);
      initFoodItem(SkiesItems.cryo_root, 1);
      initItemPickup(SkiesItems.winter_leaf_seeds);
      initFoodItem(SkiesItems.winter_leaves, 2);
      initItemPickup(SkiesItems.pine_fruit_seeds);
      initFoodItem(SkiesItems.pine_fruit, 2);
      initItemPickup(SkiesItems.fiery_bean_seeds);
      initFoodItem(SkiesItems.fiery_beans, 1);
      initItemPickup(SkiesItems.scalefruit_seeds);
      initFoodItem(SkiesItems.scalefruit, 2);
   }

   private static void registerProf(RegisterEvent event, String name, Lazy<VillagerProfession> prof) {
      event.register(Registries.VILLAGER_PROFESSION, BlueSkies.locate(name), prof);
   }

   public static void initVillagerTypes() {
      BlueSkies.LOGGER.debug("Registering villager types: " + VillagerTrades.TRADES.size());
      CALMING_SKIES = registerType("calming_skies");
      BRIGHTLANDS = registerType("brightlands");
      SLUSHLANDS = registerType("slushlands");
      UNORTHODOX_VALLEY = registerType("unorthodox_valley");
      SHADED_WOODLANDS = registerType("shaded_woodlands");
      CRYSTAL_DUNES = registerType("crystal_dunes");
      SUNSET_MAPLE_FOREST = registerType("sunset_maple_forest");
      putTypeToBiome(SkiesBiomes.CALMING_SKIES.getKey(), CALMING_SKIES);
      putTypeToBiome(SkiesBiomes.BRIGHTLANDS.getKey(), BRIGHTLANDS);
      putTypeToBiome(SkiesBiomes.SNOW_COVERED_PINES.getKey(), BRIGHTLANDS);
      putTypeToBiome(SkiesBiomes.FROSTBITTEN_FOREST.getKey(), BRIGHTLANDS);
      putTypeToBiome(SkiesBiomes.MIDDAY_SHORE.getKey(), CALMING_SKIES);
      putTypeToBiome(SkiesBiomes.PEEKING_OCEAN.getKey(), CALMING_SKIES);
      putTypeToBiome(SkiesBiomes.SLUSHLANDS.getKey(), SLUSHLANDS);
      putTypeToBiome(SkiesBiomes.POLAR_HIGHLAND.getKey(), CALMING_SKIES);
      putTypeToBiome(SkiesBiomes.BRISK_MEADOW.getKey(), CALMING_SKIES);
      putTypeToBiome(SkiesBiomes.DEEP_PEEKING_OCEAN.getKey(), CALMING_SKIES);
      putTypeToBiome(SkiesBiomes.UNORTHODOX_VALLEY.getKey(), UNORTHODOX_VALLEY);
      putTypeToBiome(SkiesBiomes.SHADED_WOODLANDS.getKey(), SHADED_WOODLANDS);
      putTypeToBiome(SkiesBiomes.CRYSTAL_DUNES.getKey(), CRYSTAL_DUNES);
      putTypeToBiome(SkiesBiomes.SUNSET_MAPLE_FOREST.getKey(), SUNSET_MAPLE_FOREST);
      putTypeToBiome(SkiesBiomes.MOONLIT_RESERVOIR.getKey(), SHADED_WOODLANDS);
      putTypeToBiome(SkiesBiomes.RISING_CREEK.getKey(), UNORTHODOX_VALLEY);
      putTypeToBiome(SkiesBiomes.CRYSTAL_ROUGHS.getKey(), CRYSTAL_DUNES);
      putTypeToBiome(SkiesBiomes.SEARING_GRASSLAND.getKey(), SUNSET_MAPLE_FOREST);
      putTypeToBiome(SkiesBiomes.CRESCENT_ORCHARD.getKey(), UNORTHODOX_VALLEY);
   }

   public static void putTypeToBiome(ResourceKey<Biome> biomeIn, VillagerType type) {
      VillagerType.BY_BIOME.put(biomeIn, type);
   }

   public static void registerTrades(Lazy<VillagerProfession> prof, Int2ObjectMap<ItemListing[]> tradeMap) {
      VillagerTrades.TRADES.put((VillagerProfession)prof.get(), tradeMap);
   }

   public static void registerGifts(Lazy<VillagerProfession> prof, ResourceLocation giftTable) {
      GiveGiftToHero.GIFTS.put((VillagerProfession)prof.get(), giftTable);
   }

   public static VillagerType registerType(String key) {
      VillagerType type = (VillagerType)Registry.register(BuiltInRegistries.VILLAGER_TYPE, BlueSkies.locate(key), new VillagerType(key));
      SKIES_TYPES.add(type);
      return type;
   }

   public static Lazy<VillagerProfession> createProfession(
      String name, Static<PoiType> pointOfInterest, ImmutableSet<Item> specificItems, ImmutableSet<Block> relatedWorldBlocks, Supplier<SoundEvent> sound
   ) {
      return Lazy.of(
         () -> new VillagerProfession(
            name, p -> p.is(pointOfInterest.getKey()), p -> p.is(pointOfInterest.getKey()), specificItems, relatedWorldBlocks, sound.get()
         )
      );
   }

   static void initItemPickup(Item item) {
      Villager.WANTED_ITEMS = GelCollectors.merge(Villager.WANTED_ITEMS, item);
   }

   static void initFoodItem(Item item, int efficiency) {
      Villager.FOOD_POINTS = GelCollectors.merge(Villager.FOOD_POINTS, item, efficiency);
      initItemPickup(item);
   }
}
