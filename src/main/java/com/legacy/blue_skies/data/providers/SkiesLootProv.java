package com.legacy.blue_skies.data.providers;

import com.google.common.collect.ImmutableList;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.blocks.construction.BagOfSpoilsBlock;
import com.legacy.blue_skies.blocks.misc.SkyWebbingBlock;
import com.legacy.blue_skies.blocks.misc.TrophyBlock;
import com.legacy.blue_skies.blocks.natural.BrewberryBushBlock;
import com.legacy.blue_skies.blocks.natural.FruitLeavesBlock;
import com.legacy.blue_skies.blocks.natural.LargeLilyPadBlock;
import com.legacy.blue_skies.blocks.natural.SkyCropBlock;
import com.legacy.blue_skies.blocks.natural.SkyDoubleCropBlock;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.loot_functions.SetArcRarity;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.items.util.IRarityItem;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillageBiomes;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillageProfessions;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds.Ints;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction.NameSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class SkiesLootProv extends LootTableProvider {
   public static final ResourceLocation GATEKEEPER_BOOK = BlueSkies.locate("chests/gatekeeper_house/book");
   public static final ResourceLocation GATEKEEPER_MOUNTAIN = BlueSkies.locate("chests/gatekeeper_house/mountain");
   public static final ResourceLocation GATEKEEPER_PLAINS = BlueSkies.locate("chests/gatekeeper_house/plains");
   public static final ResourceLocation GATEKEEPER_SNOWY = BlueSkies.locate("chests/gatekeeper_house/snowy");
   public static final ResourceLocation[] GATEKEEPER_LOOT = new ResourceLocation[]{GATEKEEPER_BOOK, GATEKEEPER_MOUNTAIN, GATEKEEPER_PLAINS, GATEKEEPER_SNOWY};
   public static final ResourceLocation BUNKER = BlueSkies.locate("chests/bunker/common");
   public static final ResourceLocation BUNKER_RARE = BlueSkies.locate("chests/bunker/rare");
   public static final ResourceLocation BLINDING_STUDY = BlueSkies.locate("chests/blinding_dungeon/study_chest");
   public static final ResourceLocation BLINDING_LIBRARY = BlueSkies.locate("chests/blinding_dungeon/library_chest");
   public static final ResourceLocation BLINDING_LIBRARY_KEY = BlueSkies.locate("chests/blinding_dungeon/library_chest_key");
   public static final ResourceLocation BLINDING_PRISON_EVERBRIGHT = BlueSkies.locate("chests/blinding_dungeon/prison_chest_everbright");
   public static final ResourceLocation BLINDING_PRISON_EVERDAWN = BlueSkies.locate("chests/blinding_dungeon/prison_chest_everdawn");
   public static final Map<String, ResourceLocation> BLINDING_LOOT = (Map<String, ResourceLocation>)Util.make(new HashMap(), map -> {
      map.put("study", BLINDING_STUDY);
      map.put("lib", BLINDING_LIBRARY);
      map.put("lib_key", BLINDING_LIBRARY_KEY);
      map.put("prison_eb", BLINDING_PRISON_EVERBRIGHT);
      map.put("prison_ed", BLINDING_PRISON_EVERDAWN);
   });
   public static final ResourceLocation NATURE_CHEST = BlueSkies.locate("chests/nature_dungeon/chest");
   public static final ResourceLocation NATURE_CHEST_KEY = BlueSkies.locate("chests/nature_dungeon/chest_key");
   public static final ResourceLocation POISON_CHEST = BlueSkies.locate("chests/poison_dungeon/chest");
   public static final ResourceLocation POISON_CHEST_KEY = BlueSkies.locate("chests/poison_dungeon/chest_key");
   public static final EnumMap<SkiesVillageBiomes, ResourceLocation> VILLAGE_BIOME_CHESTS = (EnumMap<SkiesVillageBiomes, ResourceLocation>)Util.make(
      new EnumMap<>(SkiesVillageBiomes.class), map -> {
         for (SkiesVillageBiomes name : SkiesVillageBiomes.values()) {
            map.put(name, BlueSkies.locate("chests/village/" + name.toString()));
         }
      }
   );
   public static final EnumMap<SkiesVillageProfessions, ResourceLocation> VILLAGE_EVERBRIGHT_PROFESSIONS = (EnumMap<SkiesVillageProfessions, ResourceLocation>)Util.make(
      new EnumMap<>(SkiesVillageProfessions.class), map -> {
         for (SkiesVillageProfessions name : SkiesVillageProfessions.values()) {
            map.put(name, BlueSkies.locate("chests/village/profession/everbright/" + name.toString()));
         }
      }
   );
   public static final EnumMap<SkiesVillageProfessions, ResourceLocation> VILLAGE_EVERDAWN_PROFESSIONS = (EnumMap<SkiesVillageProfessions, ResourceLocation>)Util.make(
      new EnumMap<>(SkiesVillageProfessions.class), map -> {
         for (SkiesVillageProfessions name : SkiesVillageProfessions.values()) {
            map.put(name, BlueSkies.locate("chests/village/profession/everdawn/" + name.toString()));
         }
      }
   );
   public static final ResourceLocation EVERBRIGHT_CAVE_SPAWNER = BlueSkies.locate("chests/cave_spawner/chest_everbright");
   public static final ResourceLocation EVERDAWN_CAVE_SPAWNER = BlueSkies.locate("chests/cave_spawner/chest_everdawn");
   public static final ResourceLocation STARGAZER_GIFTS = BlueSkies.locate("gameplay/hero_of_the_village/stargazer_gift");
   public static final ResourceLocation WRANGLER_GIFTS = BlueSkies.locate("gameplay/hero_of_the_village/wrangler_gift");
   public static final ResourceLocation SHOVELER_GIFTS = BlueSkies.locate("gameplay/hero_of_the_village/shoveler_gift");
   public static final ResourceLocation SUMMONER_GIFTS = BlueSkies.locate("gameplay/hero_of_the_village/summoner_gift");
   public static final ResourceLocation ALCHEMIST_GIFTS = BlueSkies.locate("gameplay/hero_of_the_village/alchemist_gift");
   public static final ResourceLocation SUMMONER_BAG_COMMON = BlueSkies.locate("loot_bags/summoner/common");
   public static final ResourceLocation SUMMONER_BAG_UNCOMMON = BlueSkies.locate("loot_bags/summoner/uncommon");
   public static final ResourceLocation SUMMONER_BAG_RARE = BlueSkies.locate("loot_bags/summoner/rare");
   public static final ResourceLocation ALCHEMIST_BAG_COMMON = BlueSkies.locate("loot_bags/alchemist/common");
   public static final ResourceLocation ALCHEMIST_BAG_UNCOMMON = BlueSkies.locate("loot_bags/alchemist/uncommon");
   public static final ResourceLocation ALCHEMIST_BAG_RARE = BlueSkies.locate("loot_bags/alchemist/rare");
   public static final ResourceLocation STARLIT_CRUSHER_BAG_COMMON = BlueSkies.locate("loot_bags/starlit_crusher/common");
   public static final ResourceLocation STARLIT_CRUSHER_BAG_UNCOMMON = BlueSkies.locate("loot_bags/starlit_crusher/uncommon");
   public static final ResourceLocation STARLIT_CRUSHER_BAG_RARE = BlueSkies.locate("loot_bags/starlit_crusher/rare");
   public static final ResourceLocation ARACHNARCH_BAG_COMMON = BlueSkies.locate("loot_bags/arachnarch/common");
   public static final ResourceLocation ARACHNARCH_BAG_UNCOMMON = BlueSkies.locate("loot_bags/arachnarch/uncommon");
   public static final ResourceLocation ARACHNARCH_BAG_RARE = BlueSkies.locate("loot_bags/arachnarch/rare");

   public SkiesLootProv(DataGenerator dataGeneratorIn) {
      super(
         dataGeneratorIn.getPackOutput(),
         Set.of(),
         List.of(
            new SubProviderEntry(SkiesLootProv.SkyBlockLoot::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(SkiesLootProv.SkyEntityLoot::new, LootContextParamSets.ENTITY),
            new SubProviderEntry(SkiesLootProv.SkyChestLoot::new, LootContextParamSets.CHEST),
            new SubProviderEntry(SkiesLootProv.LootBagLoot::new, LootContextParamSets.CHEST),
            new SubProviderEntry(SkiesLootProv.SkyGiftLoot::new, LootContextParamSets.GIFT)
         )
      );
   }

   protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
      map.forEach(
         (name, lootTable) -> lootTable.validate(
            context.setParams(lootTable.getParamSet()).enterElement("{" + name + "}", new LootDataId(LootDataType.TABLE, name))
         )
      );
   }

   public static class LootBagLoot implements SkiesLootProv.LootPoolUtil, LootTableSubProvider {
      public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
         this.summoner(consumer);
         this.alchemist(consumer);
         this.starlitCrusher(consumer);
         this.arachnarch(consumer);
      }

      private void summoner(BiConsumer<ResourceLocation, Builder> consumer) {
         Item arc = SkiesItems.ethereal_arc;
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> common = () -> this.poolOf(
            List.of(
               this.basicEntry(Items.EMERALD, 1, 2),
               this.basicEntry(SkiesItems.soul_fragment, 1, 2),
               this.basicEntry(SkiesItems.moonstone_shard, 2, 4),
               this.basicEntry(SkiesItems.aquite, 1, 2)
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> uncommon = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesBlocks.summoning_table).setQuality(2),
               this.basicEntry(SkiesBlocks.star_flare, 1, 2),
               this.basicEntry(SkiesItems.blinding_rage_record).setQuality(2),
               this.basicEntry(Items.BOOK).apply(this.enchant(15, 20))
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> rare = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesItems.summoning_tome).setQuality(2).setQuality(3),
               this.basicEntry(SkiesItems.falsite_ingot, 1, 4).setQuality(-1),
               this.basicEntry(Items.BOOK).apply(this.enchant(Enchantments.PIERCING, Enchantments.LOYALTY, Enchantments.UNBREAKING)).setQuality(1)
            )
         );
         consumer.accept(
            SkiesLootProv.SUMMONER_BAG_COMMON,
            this.makeBagTable(
               IRarityItem.SkiesRarity.COMMON,
               arc,
               common,
               uncommon,
               rare,
               List.of(this.basicEntry(SkiesItems.soulbound_spear), this.basicEntry(SkiesItems.banished_trim, 0, 1))
            )
         );
         consumer.accept(
            SkiesLootProv.SUMMONER_BAG_UNCOMMON,
            this.makeBagTable(
               IRarityItem.SkiesRarity.UNCOMMON,
               arc,
               common,
               uncommon,
               rare,
               List.of(this.basicEntry(SkiesItems.soulbound_spear), this.basicEntry(SkiesItems.banished_trim, 1, 1))
            )
         );
         consumer.accept(
            SkiesLootProv.SUMMONER_BAG_RARE,
            this.makeBagTable(
               IRarityItem.SkiesRarity.RARE,
               arc,
               common,
               uncommon,
               rare,
               List.of(this.basicEntry(SkiesItems.soulbound_spear), this.basicEntry(SkiesItems.banished_trim, 1, 2))
            )
         );
      }

      private void alchemist(BiConsumer<ResourceLocation, Builder> consumer) {
         Item arc = SkiesItems.dusk_arc;
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> common = () -> this.poolOf(
            List.of(
               this.basicEntry(Items.EMERALD, 1, 2),
               this.basicEntry(SkiesItems.ventium_ingot, 1, 2),
               this.basicEntry(SkiesItems.moonstone_shard, 2, 4),
               this.basicEntry(SkiesItems.aquite, 1, 2)
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> uncommon = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesBlocks.alchemy_table).setQuality(2),
               this.basicEntry(SkiesBlocks.star_flare, 1, 2),
               this.basicEntry(SkiesItems.blinding_rage_record).setQuality(2),
               this.basicEntry(Items.BOOK).apply(this.enchant(15, 20))
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> rare = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesItems.alchemy_scroll).setQuality(3),
               this.basicEntry(SkiesItems.horizonite_ingot, 1, 4).setQuality(-1),
               this.basicEntry(Items.BOOK).apply(this.enchant(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS)).setQuality(1)
            )
         );
         consumer.accept(
            SkiesLootProv.ALCHEMIST_BAG_COMMON,
            this.makeBagTable(
               IRarityItem.SkiesRarity.COMMON,
               arc,
               common,
               uncommon,
               rare,
               List.of(this.basicEntry(SkiesItems.spike_shield), this.basicEntry(SkiesItems.thwarted_trim, 0, 1))
            )
         );
         consumer.accept(
            SkiesLootProv.ALCHEMIST_BAG_UNCOMMON,
            this.makeBagTable(
               IRarityItem.SkiesRarity.UNCOMMON,
               arc,
               common,
               uncommon,
               rare,
               List.of(this.basicEntry(SkiesItems.spike_shield), this.basicEntry(SkiesItems.thwarted_trim, 1, 1))
            )
         );
         consumer.accept(
            SkiesLootProv.ALCHEMIST_BAG_RARE,
            this.makeBagTable(
               IRarityItem.SkiesRarity.RARE,
               arc,
               common,
               uncommon,
               rare,
               List.of(this.basicEntry(SkiesItems.spike_shield), this.basicEntry(SkiesItems.thwarted_trim, 1, 2))
            )
         );
      }

      private void starlitCrusher(BiConsumer<ResourceLocation, Builder> consumer) {
         Item arc = SkiesItems.nature_arc;
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> common = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesBlocks.starlit_log, 5, 10),
               this.basicEntry(SkiesItems.winter_leaves, 2, 5),
               this.basicEntry(SkiesBlocks.comet_sapling, 2, 4),
               this.basicEntry(SkiesItems.aquite, 2, 4)
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> uncommon = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesItems.cryo_root, 1, 3),
               this.basicEntry(SkiesItems.falsite_ingot, 2, 5),
               this.basicEntry(SkiesItems.defying_starlight_record).setQuality(2),
               this.basicEntry(Items.BOOK).apply(this.enchant(20, 28))
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> rare = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesItems.crushing_hammer).setQuality(3),
               this.basicEntry(SkiesItems.diopside_gem, 2, 3).setQuality(-1),
               this.basicEntry(Items.SADDLE).setQuality(1)
            )
         );
         consumer.accept(
            SkiesLootProv.STARLIT_CRUSHER_BAG_COMMON,
            this.makeBagTable(IRarityItem.SkiesRarity.COMMON, arc, common, uncommon, rare, List.of(this.basicEntry(SkiesItems.regrowth_trim, 0, 1)))
         );
         consumer.accept(
            SkiesLootProv.STARLIT_CRUSHER_BAG_UNCOMMON,
            this.makeBagTable(IRarityItem.SkiesRarity.UNCOMMON, arc, common, uncommon, rare, List.of(this.basicEntry(SkiesItems.regrowth_trim, 1, 1)))
         );
         consumer.accept(
            SkiesLootProv.STARLIT_CRUSHER_BAG_RARE,
            this.makeBagTable(IRarityItem.SkiesRarity.RARE, arc, common, uncommon, rare, List.of(this.basicEntry(SkiesItems.regrowth_trim, 1, 2)))
         );
      }

      private void arachnarch(BiConsumer<ResourceLocation, Builder> consumer) {
         Item arc = SkiesItems.poison_arc;
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> common = () -> this.poolOf(
            List.of(
               this.basicEntry(Items.STRING, 5, 10),
               this.basicEntry(SkiesBlocks.spider_webbing, 2, 5),
               this.basicEntry(Items.SPIDER_EYE, 2, 4),
               this.basicEntry(SkiesItems.aquite, 2, 4)
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> uncommon = () -> this.poolOf(
            List.of(
               this.basicEntry(Items.SPLASH_POTION).apply(SetPotionFunction.setPotion(Potions.LONG_POISON)),
               this.basicEntry(SkiesItems.horizonite_ingot, 2, 3),
               this.basicEntry(SkiesItems.venomous_encounter_record).setQuality(2),
               this.basicEntry(Items.BOOK).apply(this.enchant(20, 28))
            )
         );
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> rare = () -> this.poolOf(
            List.of(
               this.basicEntry(SkiesItems.venom_sac).setQuality(2),
               this.basicEntry(SkiesItems.diopside_gem, 2, 3).setQuality(-1),
               this.basicEntry(SkiesItems.different_sword).setQuality(1)
            )
         );
         consumer.accept(
            SkiesLootProv.ARACHNARCH_BAG_COMMON,
            this.makeBagTable(IRarityItem.SkiesRarity.COMMON, arc, common, uncommon, rare, List.of(this.basicEntry(SkiesItems.toxic_trim, 0, 1)))
         );
         consumer.accept(
            SkiesLootProv.ARACHNARCH_BAG_UNCOMMON,
            this.makeBagTable(IRarityItem.SkiesRarity.UNCOMMON, arc, common, uncommon, rare, List.of(this.basicEntry(SkiesItems.toxic_trim, 1, 1)))
         );
         consumer.accept(
            SkiesLootProv.ARACHNARCH_BAG_RARE,
            this.makeBagTable(IRarityItem.SkiesRarity.RARE, arc, common, uncommon, rare, List.of(this.basicEntry(SkiesItems.toxic_trim, 1, 2)))
         );
      }

      private Builder makeBagTable(
         IRarityItem.SkiesRarity rarity,
         Item arc,
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> common,
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> uncommon,
         Supplier<net.minecraft.world.level.storage.loot.LootPool.Builder> rare,
         List<net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder<?>> extraItems
      ) {
         int minCommonRolls;
         int maxCommonRolls;
         int minUncommonRolls;
         int maxUncommonRolls;
         float uncommonChance;
         float rareChance;
         switch (rarity) {
            case COMMON:
               minCommonRolls = 3;
               maxCommonRolls = 6;
               minUncommonRolls = 0;
               maxUncommonRolls = 2;
               uncommonChance = 0.6F;
               rareChance = 0.0F;
               break;
            case UNCOMMON:
               minCommonRolls = 2;
               maxCommonRolls = 5;
               minUncommonRolls = 1;
               maxUncommonRolls = 3;
               uncommonChance = 0.85F;
               rareChance = 0.25F;
               break;
            default:
               minCommonRolls = 1;
               maxCommonRolls = 4;
               minUncommonRolls = 1;
               maxUncommonRolls = 3;
               uncommonChance = 1.0F;
               rareChance = 0.5F;
         }

         List<net.minecraft.world.level.storage.loot.LootPool.Builder> pools = new ArrayList<>();
         pools.add(this.poolOf(ImmutableList.of(this.basicEntry(arc).apply(SetArcRarity.setRarity(rarity)))));

         for (net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder<?> entry : extraItems) {
            pools.add(this.poolOf(List.of(entry)));
         }

         if (maxCommonRolls > 0) {
            pools.add(common.get().setRolls(UniformGenerator.between(minCommonRolls, maxCommonRolls)));
         }

         if (maxUncommonRolls > 0) {
            pools.add(
               uncommon.get()
                  .setRolls(UniformGenerator.between(minUncommonRolls, maxUncommonRolls))
                  .when(LootItemRandomChanceCondition.randomChance(uncommonChance))
            );
         }

         if (rareChance > 0.0F) {
            pools.add(rare.get().when(LootItemRandomChanceCondition.randomChance(rareChance)));
         }

         return this.tableOf(pools);
      }
   }

   public interface LootPoolUtil {
      default Builder tableOf(List<net.minecraft.world.level.storage.loot.LootPool.Builder> pools) {
         Builder table = LootTable.lootTable();
         pools.forEach(pool -> table.withPool(pool));
         return table;
      }

      default Builder tableOf(net.minecraft.world.level.storage.loot.LootPool.Builder pool) {
         return LootTable.lootTable().withPool(pool);
      }

      default net.minecraft.world.level.storage.loot.LootPool.Builder basicPool(ItemLike item, int min, int max) {
         return LootPool.lootPool().add(this.basicEntry(item, min, max));
      }

      default net.minecraft.world.level.storage.loot.LootPool.Builder basicPool(ItemLike item) {
         return LootPool.lootPool().add(this.basicEntry(item));
      }

      default net.minecraft.world.level.storage.loot.LootPool.Builder randItemPool(List<ItemLike> items) {
         return this.poolOf(items.stream().map(i -> this.basicEntry(i)).collect(Collectors.toList()));
      }

      default net.minecraft.world.level.storage.loot.LootPool.Builder poolOf(
         List<net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder<?>> lootEntries
      ) {
         net.minecraft.world.level.storage.loot.LootPool.Builder pool = LootPool.lootPool();
         lootEntries.forEach(entry -> pool.add(entry));
         return pool;
      }

      default net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> basicEntry(ItemLike item, int min, int max) {
         return this.basicEntry(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
      }

      default net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> basicEntry(ItemLike item) {
         return LootItem.lootTableItem(item);
      }

      default net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> setStick(Item stick) {
         return this.setNbt(nbt -> nbt.putString("Stick", BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(stick).getName()));
      }

      default net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> setDamage(int min, int max) {
         return SetItemDamageFunction.setDamage(UniformGenerator.between(min / 100.0F, max / 100.0F));
      }

      default net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> smeltItem(
         net.minecraft.advancements.critereon.EntityPredicate.Builder predicate
      ) {
         return SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(EntityTarget.THIS, predicate));
      }

      default net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> enchant(int minLevel, int maxLevel) {
         return EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(minLevel, maxLevel));
      }

      default net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> enchant(Enchantment... enchantments) {
         net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction.Builder func = new net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction.Builder();

         for (Enchantment enchantment : enchantments) {
            func.withEnchantment(enchantment);
         }

         return func;
      }

      default net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> setNbt(Consumer<CompoundTag> nbt) {
         return SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), nbt));
      }
   }

   private static class SkyBlockLoot extends VanillaBlockLoot implements SkiesLootProv.LootPoolUtil, LootTableSubProvider {
      private final net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(
         net.minecraft.advancements.critereon.ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, Ints.atLeast(1)))
      );
      private final net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder SHEARS = MatchTool.toolMatches(
         net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(new ItemLike[]{Items.SHEARS})
      );
      private final net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder SILK_TOUCH_OR_SHEARS = this.SHEARS.or(this.SILK_TOUCH);
      private final net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder NOT_SILK_TOUCH_OR_SHEARS = this.SILK_TOUCH_OR_SHEARS.invert();
      private float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

      protected void generate() {
         this.blocks()
            .forEach(
               block -> {
                  if (block == SkiesBlocks.short_turquoise_grass) {
                     this.droppingSeedTag(block, SkiesItemTags.TURQUOISE_GRASS_DROPS);
                  } else if (block == SkiesBlocks.tall_turquoise_grass) {
                     this.add(
                        block,
                        b -> createShearsDispatchTable(
                           SkiesBlocks.short_turquoise_grass,
                           ((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)this.applyExplosionCondition(
                                    b, TagEntry.expandTag(SkiesItemTags.TURQUOISE_GRASS_DROPS)
                                 ))
                                 .when(
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
                                       .setProperties(
                                          net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties()
                                             .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                                       )
                                 ))
                              .when(LootItemRandomChanceCondition.randomChance(0.125F))
                        )
                     );
                  } else if (block == SkiesBlocks.short_lunar_grass) {
                     this.droppingSeedTag(block, SkiesItemTags.LUNAR_GRASS_DROPS);
                  } else if (block == SkiesBlocks.tall_lunar_grass) {
                     this.add(
                        block,
                        b -> createShearsDispatchTable(
                           SkiesBlocks.short_lunar_grass,
                           ((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)this.applyExplosionCondition(
                                    b, TagEntry.expandTag(SkiesItemTags.LUNAR_GRASS_DROPS)
                                 ))
                                 .when(
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
                                       .setProperties(
                                          net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties()
                                             .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                                       )
                                 ))
                              .when(LootItemRandomChanceCondition.randomChance(0.125F))
                        )
                     );
                  } else if (block == SkiesBlocks.short_comet_grass) {
                     this.droppingSeedTag(block, SkiesItemTags.COMET_GRASS_DROPS);
                  } else if (block == SkiesBlocks.turquoise_grass_block || block == SkiesBlocks.turquoise_comet_grass_block) {
                     this.silkOrElse(block, SkiesBlocks.turquoise_dirt);
                  } else if (block == SkiesBlocks.lunar_grass_block || block == SkiesBlocks.lunar_comet_grass_block) {
                     this.silkOrElse(block, SkiesBlocks.lunar_dirt);
                  } else if (block == SkiesBlocks.turquoise_farmland.get()) {
                     this.dropOther(block, SkiesBlocks.turquoise_dirt);
                  } else if (block == SkiesBlocks.lunar_farmland.get()) {
                     this.dropOther(block, SkiesBlocks.lunar_dirt);
                  } else if (block == SkiesBlocks.turquoise_stone) {
                     this.silkOrElse(block, SkiesBlocks.turquoise_cobblestone);
                  } else if (block == SkiesBlocks.lunar_stone) {
                     this.silkOrElse(block, SkiesBlocks.lunar_cobblestone);
                  } else if (block instanceof VineBlock
                     || block == SkiesBlocks.muckweed
                     || block == SkiesBlocks.brittlebush
                     || block == SkiesBlocks.chillweed
                     || block == SkiesBlocks.sea_moss
                     || block == SkiesBlocks.crystallized_grass) {
                     this.add(block, VanillaBlockLoot.createShearsOnlyDrop(block));
                  } else if (block == SkiesBlocks.brumble_vine_top || block == SkiesBlocks.brumble_vine) {
                     this.add(block, b -> this.createSingleItemTable(SkiesBlocks.brumble_vine_top));
                  } else if (block == SkiesBlocks.bluebright_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.bluebright_sapling, SkiesItems.bluebright_stick));
                  } else if (block == SkiesBlocks.starlit_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.starlit_sapling, SkiesItems.starlit_stick));
                  } else if (block == SkiesBlocks.frostbright_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.frostbright_sapling, SkiesItems.frostbright_stick));
                  } else if (block == SkiesBlocks.lunar_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.lunar_sapling, SkiesItems.lunar_stick));
                  } else if (block == SkiesBlocks.dusk_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.dusk_sapling, SkiesItems.dusk_stick));
                  } else if (block == SkiesBlocks.maple_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.maple_sapling, SkiesItems.maple_stick));
                  } else if (block == SkiesBlocks.comet_leaves) {
                     this.add(block, b -> this.leavesFruit(b, SkiesBlocks.comet_sapling, SkiesItems.comet_stick, SkiesItems.comet_berries));
                  } else if (block == SkiesBlocks.crescent_fruit_leaves) {
                     this.add(block, b -> this.leaves(b, SkiesBlocks.crescent_fruit_sapling, SkiesItems.lunar_stick));
                  } else if (block == SkiesBlocks.snowcap_mushroom) {
                     this.add(block, b -> this.createSinglePropConditionTable(b, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
                  } else if (block == SkiesBlocks.snowcap_mushroom_block) {
                     this.add(block, b -> this.createSingleItemTableWithSilkTouch(b, SkiesBlocks.snowcap_pinhead, UniformGenerator.between(0.0F, 2.0F)));
                  } else if (block == SkiesBlocks.snowcap_mushroom_stem) {
                     this.add(block, b -> this.createSingleItemTableWithSilkTouch(b, SkiesBlocks.snowcap_pinhead, UniformGenerator.between(0.0F, 1.0F)));
                  } else if (block == SkiesBlocks.brewberry_bush) {
                     this.add(
                        block,
                        b -> (Builder)this.applyExplosionDecay(
                           b,
                           LootTable.lootTable()
                              .withPool(
                                 LootPool.lootPool()
                                    .when(
                                       LootItemBlockStatePropertyCondition.hasBlockStateProperties(SkiesBlocks.brewberry_bush)
                                          .setProperties(
                                             net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BrewberryBushBlock.MATURE, true)
                                          )
                                    )
                                    .add(
                                       LootItem.lootTableItem(SkiesItems.brewberry).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                    )
                              )
                              .withPool(LootPool.lootPool().add(LootItem.lootTableItem(SkiesBlocks.brewberry_bush)))
                        )
                     );
                  } else if (block instanceof SkyWebbingBlock) {
                     this.add(
                        block,
                        b -> createSilkTouchOrShearsDispatchTable(
                           b,
                           (net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder)this.applyExplosionCondition(
                              b, LootItem.lootTableItem(Items.STRING)
                           )
                        )
                     );
                  } else if (block instanceof ChestBlock) {
                     this.add(block, this.createNameableBlockEntityTable(block));
                  } else if (block instanceof SlabBlock) {
                     this.add(block, this.createSlabItemTable(block));
                  } else if (block == SkiesBlocks.crystal_glass
                     || block == SkiesBlocks.midnight_glass
                     || block == SkiesBlocks.crystal_glass_pane
                     || block == SkiesBlocks.midnight_glass_pane
                     || block == SkiesBlocks.crystallized_leaves) {
                     this.dropWhenSilkTouch(block);
                  } else if (block == SkiesBlocks.everbright_moonstone_ore || block == SkiesBlocks.everdawn_moonstone_ore) {
                     this.add(
                        block,
                        b -> createSilkTouchDispatchTable(
                           b,
                           (net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder)this.applyExplosionDecay(
                              b,
                              LootItem.lootTableItem(SkiesItems.moonstone_shard)
                                 .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                 .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.FORTUNE))
                           )
                        )
                     );
                  } else if (block == SkiesBlocks.everbright_pyrope_ore || block == SkiesBlocks.everdawn_pyrope_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.pyrope_gem));
                  } else if (block == SkiesBlocks.everbright_diopside_ore || block == SkiesBlocks.everdawn_diopside_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.diopside_gem));
                  } else if (block == SkiesBlocks.everbright_aquite_ore || block == SkiesBlocks.everdawn_aquite_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.raw_aquite));
                  } else if (block == SkiesBlocks.everbright_charoite_ore || block == SkiesBlocks.everdawn_charoite_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.raw_charoite));
                  } else if (block == SkiesBlocks.ventium_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.raw_ventium));
                  } else if (block == SkiesBlocks.falsite_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.raw_falsite));
                  } else if (block == SkiesBlocks.horizonite_ore) {
                     this.add(block, b -> this.createOreDrop(b, SkiesItems.raw_horizonite));
                  } else if (block == SkiesBlocks.everbright_emerald_ore || block == SkiesBlocks.everdawn_emerald_ore) {
                     this.add(block, b -> this.createOreDrop(b, Items.EMERALD));
                  } else if (block == SkiesBlocks.decaying_spike) {
                     this.dropOther(block, SkiesBlocks.lunar_cobblestone);
                  } else if (block == SkiesBlocks.crescent_fruit) {
                     this.dropOther(block, SkiesItems.crescent_fruit);
                  } else if (block == SkiesBlocks.comet_berry_pie) {
                     this.add(block, noDrop());
                  } else if (block instanceof DoorBlock) {
                     this.add(block, b -> this.createSinglePropConditionTable(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
                  } else if (ImmutableList.of(
                        SkiesBlocks.bluebright_bookshelf,
                        SkiesBlocks.starlit_bookshelf,
                        SkiesBlocks.frostbright_bookshelf,
                        SkiesBlocks.lunar_bookshelf,
                        SkiesBlocks.dusk_bookshelf,
                        SkiesBlocks.maple_bookshelf,
                        SkiesBlocks.comet_bookshelf
                     )
                     .contains(block)) {
                     this.add(block, b -> this.createSingleItemTableWithSilkTouch(b, Items.BOOK, ConstantValue.exactly(3.0F)));
                  } else if (block instanceof FlowerPotBlock) {
                     this.dropPottedContents(block);
                  } else if (block instanceof SkyCropBlock) {
                     net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder growthCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
                           block
                        )
                        .setProperties(
                           net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties()
                              .hasProperty(SkyCropBlock.AGE, ((SkyCropBlock)block).getMaxAge())
                        );
                     if (block == SkiesBlocks.solnuts) {
                        this.add(block, b -> this.crop(growthCondition, b, SkiesItems.solnut));
                     } else if (block == SkiesBlocks.cryo_roots) {
                        this.add(block, b -> this.crop(growthCondition, b, SkiesItems.cryo_root));
                     } else if (block == SkiesBlocks.winter_leaves) {
                        this.add(block, b -> this.crop(growthCondition, b, SkiesItems.winter_leaves, SkiesItems.winter_leaf_seeds));
                     } else if (block == SkiesBlocks.fiery_beans) {
                        this.add(block, b -> this.crop(growthCondition, b, SkiesItems.fiery_beans, SkiesItems.fiery_bean_seeds));
                     } else if (block == SkiesBlocks.scalefruits) {
                        this.add(block, b -> this.doubleCrop(growthCondition, b, SkiesItems.scalefruit, SkiesItems.scalefruit_seeds));
                     } else if (block == SkiesBlocks.pine_fruits) {
                        this.add(block, b -> this.doubleCrop(growthCondition, b, SkiesItems.pine_fruit, SkiesItems.pine_fruit_seeds));
                     }
                  } else if (block == SkiesBlocks.spider_nest) {
                     this.dropOther(block, SkiesBlocks.poison_stone);
                  } else if (block == SkiesBlocks.moonlit_water_lily) {
                     this.add(block, b -> this.createSinglePropConditionTable(b, LargeLilyPadBlock.CORNER, LargeLilyPadBlock.Corner.BOTTOM_L));
                  } else if (block instanceof TrophyBlock) {
                     this.add(block, b -> this.trophy(b));
                  } else if (block instanceof BagOfSpoilsBlock) {
                     this.add(block, b -> this.createBagOfSpoilsDrop(b));
                  } else {
                     this.dropSelf(block);
                  }
               }
            );
      }

      protected Iterable<Block> getKnownBlocks() {
         return this.blocks()::iterator;
      }

      private Stream<Block> blocks() {
         return BuiltInRegistries.BLOCK
            .stream()
            .filter(b -> BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals("blue_skies") && !b.getLootTable().equals(BuiltInLootTables.EMPTY));
      }

      private void droppingSeedTag(Block block, TagKey<Item> tag) {
         this.add(
            block,
            createShearsDispatchTable(
               block,
               (net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer.Builder)this.applyExplosionDecay(
                  block,
                  ((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)TagEntry.expandTag(tag)
                        .when(LootItemRandomChanceCondition.randomChance(0.125F)))
                     .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.FORTUNE, 2))
               )
            )
         );
      }

      private void silkOrElse(Block withSilk, ItemLike without) {
         this.add(withSilk, b -> this.createSingleItemTableWithSilkTouch(b, without));
      }

      private Builder leaves(Block block, ItemLike sapling, ItemLike stick) {
         return createSilkTouchOrShearsDispatchTable(
               block,
               ((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)this.applyExplosionCondition(
                     block, LootItem.lootTableItem(sapling)
                  ))
                  .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.FORTUNE, this.DEFAULT_SAPLING_DROP_RATES))
            )
            .withPool(
               LootPool.lootPool()
                  .setRolls(ConstantValue.exactly(1.0F))
                  .when(this.NOT_SILK_TOUCH_OR_SHEARS)
                  .add(
                     ((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)this.applyExplosionDecay(
                           block, LootItem.lootTableItem(stick).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        ))
                        .when(
                           BonusLevelTableCondition.bonusLevelFlatChance(
                              Enchantments.FORTUNE, new float[]{0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F}
                           )
                        )
                  )
            );
      }

      private Builder leavesFruit(Block block, ItemLike sapling, ItemLike stick, ItemLike fruit) {
         return this.leaves(block, sapling, stick)
            .withPool(
               this.poolOf(List.of(this.basicEntry(SkiesItems.comet_berries)))
                  .when(
                     LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(
                           net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties()
                              .hasProperty(FruitLeavesBlock.GROWTH, FruitLeavesBlock.Growth.GROWN)
                        )
                  )
            );
      }

      private Builder crop(net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder growthCondition, Block block, ItemLike food) {
         return this.crop(growthCondition, block, food, food);
      }

      private Builder crop(
         net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder growthCondition, Block block, ItemLike food, ItemLike seed
      ) {
         net.minecraft.world.level.storage.loot.LootPool.Builder seedPool = LootPool.lootPool()
            .add(
               LootItem.lootTableItem(seed)
                  .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.FORTUNE, 0.5714286F, 3).when(growthCondition))
            );
         net.minecraft.world.level.storage.loot.LootPool.Builder foodPool = LootPool.lootPool()
            .when(growthCondition)
            .add(LootItem.lootTableItem(food).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.FORTUNE, 0.5714286F, 1)));
         return (Builder)this.applyExplosionDecay(block, LootTable.lootTable().withPool(seedPool).withPool(foodPool));
      }

      private Builder doubleCrop(
         net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder growthCondition, Block block, ItemLike food, ItemLike seed
      ) {
         net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder topHalf = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
               block
            )
            .setProperties(
               net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(SkyDoubleCropBlock.HALF, DoubleBlockHalf.UPPER)
            );
         net.minecraft.world.level.storage.loot.predicates.LootItemCondition.Builder lowerHalf = LootItemBlockStatePropertyCondition.hasBlockStateProperties(
               block
            )
            .setProperties(
               net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties().hasProperty(SkyDoubleCropBlock.HALF, DoubleBlockHalf.LOWER)
            );
         net.minecraft.world.level.storage.loot.LootPool.Builder seedPool = LootPool.lootPool()
            .add(
               LootItem.lootTableItem(seed).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.FORTUNE, 0.2F, 2).when(growthCondition))
            );
         net.minecraft.world.level.storage.loot.LootPool.Builder topPool = LootPool.lootPool()
            .when(growthCondition)
            .when(topHalf)
            .add(LootItem.lootTableItem(food).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.FORTUNE, 0.4F, 1)));
         net.minecraft.world.level.storage.loot.LootPool.Builder lowerPool = LootPool.lootPool()
            .when(growthCondition)
            .when(lowerHalf)
            .add(LootItem.lootTableItem(food).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.FORTUNE, 0.2F, 1)));
         return (Builder)this.applyExplosionDecay(block, LootTable.lootTable().withPool(seedPool).withPool(topPool).withPool(lowerPool));
      }

      private Builder trophy(Block block) {
         return LootTable.lootTable()
            .withPool(
               LootPool.lootPool()
                  .add(
                     LootItem.lootTableItem(block)
                        .apply(CopyBlockState.copyState(block).copy(TrophyBlock.TIER))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Label", "display.Name"))
                  )
            );
      }

      private Builder createBagOfSpoilsDrop(Block block) {
         return LootTable.lootTable()
            .withPool(
               (net.minecraft.world.level.storage.loot.LootPool.Builder)this.applyExplosionCondition(
                  block,
                  LootPool.lootPool()
                     .setRolls(ConstantValue.exactly(1.0F))
                     .add(
                        LootItem.lootTableItem(block)
                           .apply(CopyNameFunction.copyName(NameSource.BLOCK_ENTITY))
                           .apply(
                              CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                 .copy("Lock", "BlockEntityTag.Lock")
                                 .copy("LootTable", "BlockEntityTag.LootTable")
                                 .copy("LootTableSeed", "BlockEntityTag.LootTableSeed")
                           )
                           .apply(
                              SetContainerContents.setContents(SkiesBlockEntityTypes.BAG_OF_SPOILS)
                                 .withEntry(DynamicLoot.dynamicEntry(BagOfSpoilsBlock.CONTENTS))
                           )
                     )
               )
            );
      }
   }

   private static class SkyChestLoot implements SkiesLootProv.LootPoolUtil, LootTableSubProvider {
      public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
         this.gatekeeperHouse(consumer);
         this.village(consumer);
         this.frozenBunker(consumer);
         this.caveSpawner(consumer);
         this.blindingDungeon(consumer);
         this.natureDungeon(consumer);
         this.poisonDungeon(consumer);
      }

      private void gatekeeperHouse(BiConsumer<ResourceLocation, Builder> consumer) {
         consumer.accept(
            SkiesLootProv.GATEKEEPER_BOOK,
            this.tableOf(
               ImmutableList.of(
                  this.basicPool(Items.BOOK),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.PAPER, 1, 4).setWeight(4),
                           this.basicEntry(Items.BREAD, 1, 2).setWeight(4),
                           this.basicEntry(Items.MAP).setWeight(2),
                           this.basicEntry(SkiesItems.moonstone_shard, 1, 2).setWeight(2),
                           this.basicEntry(Items.EMERALD, 1, 2).setWeight(1)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.GATEKEEPER_MOUNTAIN,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.WHEAT, 1, 3).setWeight(5),
                           this.basicEntry(Items.STICK, 1, 2).setWeight(3),
                           this.basicEntry(Items.BREAD, 1, 2).setWeight(3),
                           this.basicEntry(Blocks.SPRUCE_LOG, 1, 3).setWeight(2),
                           this.basicEntry(SkiesItems.cooked_monitor_tail, 1, 2).setWeight(2),
                           this.basicEntry(Items.LEATHER_BOOTS).setWeight(1)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.GATEKEEPER_PLAINS,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.BAKED_POTATO, 1, 3).setWeight(5),
                           this.basicEntry(Items.BLACK_WOOL, 1, 2).setWeight(3),
                           this.basicEntry(Items.BREAD, 1, 2).setWeight(3),
                           this.basicEntry(Items.DANDELION, 1, 3).setWeight(2),
                           this.basicEntry(SkiesItems.ventium_ingot).setWeight(1)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.GATEKEEPER_SNOWY,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.WHEAT, 1, 3).setWeight(5),
                           this.basicEntry(Items.CHARCOAL, 1, 2).setWeight(3),
                           this.basicEntry(Items.BREAD, 1, 2).setWeight(3),
                           this.basicEntry(Items.SWEET_BERRIES, 1, 3).setWeight(2),
                           this.basicEntry(SkiesBlocks.snowcap_pinhead, 2, 5).setWeight(2)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
      }

      private void village(BiConsumer<ResourceLocation, Builder> consumer) {
         consumer.accept(
            SkiesLootProv.VILLAGE_BIOME_CHESTS.get(SkiesVillageBiomes.CALMING_SKIES),
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                           this.basicEntry(Items.BOOK).setWeight(1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.brewberry, 1, 7).setWeight(5),
                           this.basicEntry(SkiesBlocks.polar_posy, 1, 2).setWeight(5),
                           this.basicEntry(SkiesBlocks.bluebright_sapling, 1, 2).setWeight(5)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.VILLAGE_BIOME_CHESTS.get(SkiesVillageBiomes.BRIGHTLANDS),
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                           this.basicEntry(Items.BOOK).setWeight(1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.pine_fruit, 1, 7).setWeight(5),
                           this.basicEntry(Items.SNOWBALL, 1, 2).setWeight(3),
                           this.basicEntry(SkiesBlocks.starlit_sapling, 1, 2).setWeight(5)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.VILLAGE_BIOME_CHESTS.get(SkiesVillageBiomes.SLUSHLANDS),
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                           this.basicEntry(Items.MUSHROOM_STEW).setWeight(1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.cooked_carabeef, 1, 6).setWeight(4),
                           this.basicEntry(SkiesBlocks.snowcap_pinhead, 1, 2).setWeight(5),
                           this.basicEntry(SkiesBlocks.snowcap_mushroom_block, 1, 2).setWeight(3)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.VILLAGE_BIOME_CHESTS.get(SkiesVillageBiomes.SHADED_WOODLANDS),
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                           this.basicEntry(Items.BOOK).setWeight(1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.crescent_fruit, 1, 7).setWeight(5),
                           this.basicEntry(SkiesBlocks.baneful_mushroom, 1, 2).setWeight(5),
                           this.basicEntry(SkiesBlocks.dusk_sapling, 1, 2).setWeight(5)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.VILLAGE_BIOME_CHESTS.get(SkiesVillageBiomes.SUNSET_MAPLE_FOREST),
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                           this.basicEntry(Items.BOOK).setWeight(1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.cooked_monitor_tail, 1, 7).setWeight(5),
                           this.basicEntry(SkiesBlocks.blaze_bud, 1, 2).setWeight(5),
                           this.basicEntry(SkiesBlocks.maple_sapling, 1, 2).setWeight(5)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.VILLAGE_BIOME_CHESTS.get(SkiesVillageBiomes.CRYSTAL_DUNES),
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                           this.basicEntry(Items.BOOK).setWeight(1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.scalefruit, 1, 6).setWeight(4),
                           this.basicEntry(SkiesBlocks.crystal_flower, 1, 2).setWeight(5),
                           this.basicEntry(SkiesBlocks.crystallized_planks, 1, 2).setWeight(3)
                        )
                     )
                     .setRolls(UniformGenerator.between(3.0F, 8.0F))
               )
            )
         );

         for (Pair<Boolean, EnumMap<SkiesVillageProfessions, ResourceLocation>> pair : Arrays.asList(
            Pair.of(true, SkiesLootProv.VILLAGE_EVERBRIGHT_PROFESSIONS), Pair.of(false, SkiesLootProv.VILLAGE_EVERDAWN_PROFESSIONS)
         )) {
            boolean everbright = (Boolean)pair.getFirst();
            EnumMap<SkiesVillageProfessions, ResourceLocation> map = (EnumMap<SkiesVillageProfessions, ResourceLocation>)pair.getSecond();
            consumer.accept(
               map.get(SkiesVillageProfessions.LIBRARY),
               this.tableOf(
                  ImmutableList.of(
                     this.poolOf(
                           ImmutableList.of(
                              this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                              this.basicEntry(Items.BOOK).setWeight(4),
                              this.basicEntry(Items.PAPER, 1, 4).setWeight(5),
                              this.basicEntry(Items.FEATHER).setWeight(2),
                              this.basicEntry(Items.BLACK_DYE).setWeight(2)
                           )
                        )
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  )
               )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.TANNERY),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.LEATHER).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE).setWeight(2))
                        .add(LootItem.lootTableItem(Items.LEATHER_BOOTS).setWeight(2))
                        .add(LootItem.lootTableItem(Items.LEATHER_HELMET).setWeight(2))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(5)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS).setWeight(2))
                        .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.FARM),
               this.tableOf(
                  ImmutableList.of(
                     this.poolOf(
                           ImmutableList.of(
                              this.basicEntry(Items.EMERALD, 1, 3).setWeight(1),
                              this.basicEntry(everbright ? SkiesItems.winter_leaf_seeds : SkiesItems.fiery_bean_seeds).setWeight(6),
                              this.basicEntry(Items.BONE_MEAL, 1, 4).setWeight(4),
                              this.basicEntry(everbright ? SkiesItems.bluebright_hoe : SkiesItems.lunar_hoe)
                                 .apply(this.setStick(everbright ? SkiesItems.starlit_stick : SkiesItems.dusk_stick))
                                 .setWeight(1)
                           )
                        )
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  )
               )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.SHEPHERD),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Blocks.WHITE_WOOL).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 8.0F))))
                        .add(LootItem.lootTableItem(Blocks.BLACK_WOOL).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Blocks.GRAY_WOOL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Blocks.BROWN_WOOL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(
                           LootItem.lootTableItem(Blocks.LIGHT_GRAY_WOOL)
                              .setWeight(2)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                        .add(LootItem.lootTableItem(SkiesItems.ventium_shears).setWeight(1))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : Items.WHEAT)
                              .setWeight(6)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F)))
                        )
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.FISHER),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.municipal_monkfish : SkiesItems.horizofin_tunid)
                              .setWeight(2)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.grittle_flatfish : SkiesItems.charscale_moki)
                              .setWeight(1)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.ventium_water_bucket)
                              .setWeight(1)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.BARREL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.winter_leaf_seeds : SkiesItems.fiery_bean_seeds)
                              .setWeight(3)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.BUTCHER),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.carabeef : SkiesItems.monitor_tail)
                              .setWeight(6)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(6)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.brewberry : SkiesItems.crescent_fruit)
                              .setWeight(6)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.venison : Items.SALMON)
                              .setWeight(6)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.ARMORER),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(SkiesItems.aquite).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.brewberry : SkiesItems.crescent_fruit)
                              .setWeight(4)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(LootItem.lootTableItem(SkiesItems.aquite_helmet).setWeight(1))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.CARTOGRAPHER),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.PAPER).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                        .add(LootItem.lootTableItem(Items.COMPASS).setWeight(5))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(15)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.bluebright_stick : SkiesItems.lunar_stick)
                              .setWeight(5)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                        )
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.FLETCHER),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                        .add(LootItem.lootTableItem(Items.ARROW).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.FEATHER).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.FLINT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.bluebright_stick : SkiesItems.lunar_stick)
                              .setWeight(7)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.WEAPONSMITH),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                        .add(
                           LootItem.lootTableItem(SkiesItems.diopside_gem)
                              .setWeight(3)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(SkiesItems.aquite).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.falsite_ingot : SkiesItems.horizonite_ingot)
                              .setWeight(5)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(15)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.brewberry : SkiesItems.crescent_fruit)
                              .setWeight(15)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.aquite_axe)
                              .apply(this.setStick(everbright ? SkiesItems.starlit_stick : SkiesItems.dusk_stick))
                              .setWeight(5)
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.aquite_sword)
                              .apply(this.setStick(everbright ? SkiesItems.starlit_stick : SkiesItems.dusk_stick))
                              .setWeight(5)
                        )
                        .add(LootItem.lootTableItem(SkiesItems.aquite_chestplate).setWeight(5))
                        .add(LootItem.lootTableItem(SkiesItems.aquite_helmet).setWeight(5))
                        .add(LootItem.lootTableItem(SkiesItems.aquite_leggings).setWeight(5))
                        .add(LootItem.lootTableItem(SkiesItems.aquite_boots).setWeight(5))
                        .add(LootItem.lootTableItem(Blocks.OBSIDIAN).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesBlocks.starlit_sapling : SkiesBlocks.dusk_sapling)
                              .setWeight(5)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F)))
                        )
                        .add(LootItem.lootTableItem(everbright ? Items.SADDLE : SkiesItems.camel_saddle).setWeight(3))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.TOOLSMITH),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                        .add(
                           LootItem.lootTableItem(SkiesItems.diopside_gem)
                              .setWeight(1)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(LootItem.lootTableItem(SkiesItems.aquite).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.falsite_ingot : SkiesItems.horizonite_ingot)
                              .setWeight(1)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(15)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.aquite_pickaxe)
                              .apply(this.setStick(everbright ? SkiesItems.frostbright_stick : SkiesItems.maple_stick))
                              .setWeight(5)
                        )
                        .add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.frostbright_stick : SkiesItems.maple_stick)
                              .setWeight(20)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.aquite_shovel)
                              .apply(this.setStick(everbright ? SkiesItems.frostbright_stick : SkiesItems.maple_stick))
                              .setWeight(5)
                        )
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.MASON),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(1.0F, 5.0F))
                        .add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(Items.FLOWER_POT).setWeight(1))
                        .add(LootItem.lootTableItem(everbright ? SkiesBlocks.turquoise_stone : SkiesBlocks.lunar_stone).setWeight(2))
                        .add(LootItem.lootTableItem(everbright ? SkiesBlocks.turquoise_stonebrick : SkiesBlocks.lunar_stonebrick).setWeight(2))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(4)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(LootItem.lootTableItem(everbright ? Items.WHITE_DYE : Items.RED_DYE).setWeight(1))
                        .add(LootItem.lootTableItem(everbright ? SkiesBlocks.taratite : SkiesBlocks.umber).setWeight(1))
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.TEMPLE),
               LootTable.lootTable()
                  .withPool(
                     LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                        .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                        .add(
                           LootItem.lootTableItem(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit)
                              .setWeight(7)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.soul_fragment)
                              .setWeight(7)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.ventium_ingot)
                              .setWeight(1)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(
                           LootItem.lootTableItem(SkiesItems.falsite_ingot)
                              .setWeight(1)
                              .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                        )
                        .add(LootItem.lootTableItem(Items.EMERALD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                  )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.STARGAZER),
               this.tableOf(
                  ImmutableList.of(
                     this.poolOf(
                           ImmutableList.of(
                              this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                              this.basicEntry(SkiesBlocks.star_flare, 1, 2).setWeight(2),
                              this.basicEntry(Items.BOOK).setWeight(4),
                              this.basicEntry(Items.PAPER, 1, 4).setWeight(6),
                              this.basicEntry(Blocks.GLASS, 1, 2).setWeight(4),
                              this.basicEntry(SkiesItems.falsite_ingot).setWeight(1)
                           )
                        )
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  )
               )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.WRANGLER),
               this.tableOf(
                  ImmutableList.of(
                     this.poolOf(
                           ImmutableList.of(
                              this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                              this.basicEntry(Items.LEAD).setWeight(3),
                              this.basicEntry(everbright ? SkiesItems.azulfo_horn : SkiesItems.fox_pelt).setWeight(4),
                              this.basicEntry(everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit, 1, 2).setWeight(6),
                              this.basicEntry(Items.CHARCOAL).setWeight(3)
                           )
                        )
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  )
               )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.SHOVELER),
               this.tableOf(
                  ImmutableList.of(
                     this.poolOf(
                           ImmutableList.of(
                              this.basicEntry(Items.EMERALD, 1, 4).setWeight(1),
                              this.basicEntry(SkiesItems.aquite_shovel)
                                 .apply(this.setStick(everbright ? SkiesItems.frostbright_stick : SkiesItems.maple_stick))
                                 .setWeight(2),
                              this.basicEntry(SkiesItems.falsite_ingot).setWeight(1),
                              this.basicEntry(everbright ? SkiesItems.frostbright_stick : SkiesItems.maple_stick, 1, 2).setWeight(4),
                              this.basicEntry(Items.SNOWBALL, 1, 2).setWeight(6)
                           )
                        )
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  )
               )
            );
            consumer.accept(
               map.get(SkiesVillageProfessions.NIGHTWATCHER),
               this.tableOf(
                  ImmutableList.of(
                     this.poolOf(
                           ImmutableList.of(
                              this.basicEntry(Items.EMERALD, 2, 5).setWeight(1),
                              this.basicEntry(everbright ? SkiesItems.azulfo_horn : SkiesItems.fox_pelt).setWeight(3),
                              this.basicEntry(SkiesItems.fox_pelt).setWeight(2),
                              this.basicEntry(Items.STRING, 1, 3).setWeight(4),
                              this.basicEntry(Items.CHARCOAL, 1, 2).setWeight(3)
                           )
                        )
                        .setRolls(UniformGenerator.between(3.0F, 8.0F))
                  )
               )
            );
         }
      }

      private void frozenBunker(BiConsumer<ResourceLocation, Builder> consumer) {
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> cabbageSeeds = this.basicEntry(SkiesItems.winter_leaf_seeds);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> cryoRoot = this.basicEntry(SkiesItems.cryo_root, 1, 2);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> enchantedBook = this.basicEntry(Items.BOOK)
            .apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(7.0F, 12.0F)));
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> string = this.basicEntry(Items.STRING, 3, 5);
         consumer.accept(
            SkiesLootProv.BUNKER,
            this.tableOf(this.poolOf(ImmutableList.of(cabbageSeeds, cryoRoot, enchantedBook, string)).setRolls(UniformGenerator.between(4.0F, 9.0F)))
         );
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> falsite = this.basicEntry(SkiesItems.falsite_ingot);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> turquoiseGem = this.basicEntry(SkiesItems.aquite, 1, 3);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> pyropeChestplate = this.basicEntry(SkiesItems.pyrope_chestplate);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> packedIce = this.basicEntry(Items.PACKED_ICE, 1, 4);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> enchantedBookRare = this.basicEntry(Items.BOOK)
            .apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10.0F, 15.0F)));
         consumer.accept(
            SkiesLootProv.BUNKER_RARE,
            this.tableOf(
               this.poolOf(ImmutableList.of(falsite, turquoiseGem, pyropeChestplate, packedIce, enchantedBookRare))
                  .setRolls(UniformGenerator.between(4.0F, 9.0F))
            )
         );
      }

      private void caveSpawner(BiConsumer<ResourceLocation, Builder> consumer) {
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> aquite = this.basicEntry(SkiesItems.aquite, 1, 2);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> enchantedBook = this.basicEntry(Items.BOOK)
            .apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(3.0F, 6.0F)));
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> string = this.basicEntry(Items.STRING, 3, 5);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> nameTag = this.basicEntry(Items.NAME_TAG);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> saddle = this.basicEntry(Items.SADDLE, 0, 1);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> ventium = this.basicEntry(SkiesItems.ventium_ingot);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> pyropePickaxeFrostbright = this.basicEntry(
               SkiesItems.pyrope_pickaxe
            )
            .apply(this.setStick(SkiesItems.frostbright_stick))
            .apply(this.setDamage(45, 90));
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> ice = this.basicEntry(Blocks.PACKED_ICE, 2, 3);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> pinhead = this.basicEntry(SkiesBlocks.snowcap_pinhead, 3, 5);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> cryoRoot = this.basicEntry(SkiesItems.cryo_root, 2, 3);
         consumer.accept(
            SkiesLootProv.EVERBRIGHT_CAVE_SPAWNER,
            this.tableOf(
               this.poolOf(ImmutableList.of(ventium, aquite, pyropePickaxeFrostbright, string, ice, pinhead, cryoRoot, enchantedBook, nameTag, saddle))
                  .setRolls(UniformGenerator.between(4.0F, 9.0F))
            )
         );
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> horizonite = this.basicEntry(SkiesItems.horizonite_ingot);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> pyropePickaxeMaple = this.basicEntry(SkiesItems.pyrope_pickaxe)
            .apply(this.setStick(SkiesItems.maple_stick))
            .apply(this.setDamage(45, 90));
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> webbing = this.basicEntry(SkiesBlocks.spider_webbing, 2, 3);
         net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder<?> scalefruitSeeds = this.basicEntry(
            SkiesItems.scalefruit_seeds, 3, 5
         );
         consumer.accept(
            SkiesLootProv.EVERDAWN_CAVE_SPAWNER,
            this.tableOf(
               this.poolOf(ImmutableList.of(horizonite, aquite, pyropePickaxeMaple, string, webbing, scalefruitSeeds, enchantedBook, nameTag, saddle))
                  .setRolls(UniformGenerator.between(4.0F, 9.0F))
            )
         );
      }

      private void blindingDungeon(BiConsumer<ResourceLocation, Builder> consumer) {
         net.minecraft.world.level.storage.loot.LootPool.Builder blindingKey = this.basicPool(SkiesItems.blinding_key);
         net.minecraft.world.level.storage.loot.LootPool.Builder gems = this.poolOf(
               ImmutableList.of(
                  this.basicEntry(Items.BOOK, 2, 4).setWeight(5),
                  this.basicEntry(SkiesItems.comet_berries, 2, 3).setWeight(4),
                  this.basicEntry(SkiesBlocks.blinding_stone, 3, 5).setWeight(3),
                  this.basicEntry(SkiesItems.pyrope_gem, 1, 3).setWeight(2),
                  this.basicEntry(SkiesItems.aquite, 1, 3).setWeight(2),
                  this.basicEntry(SkiesItems.diopside_gem, 1, 2).setWeight(1),
                  this.basicEntry(SkiesItems.population_record).setWeight(1)
               )
            )
            .setRolls(UniformGenerator.between(3.0F, 7.0F));
         consumer.accept(SkiesLootProv.BLINDING_STUDY, this.tableOf(ImmutableList.of(blindingKey, gems)));
         net.minecraft.world.level.storage.loot.LootPool.Builder paper = this.poolOf(
               ImmutableList.of(this.basicEntry(Items.PAPER, 1, 3).setWeight(2), this.basicEntry(Items.BOOK).setWeight(3))
            )
            .setRolls(UniformGenerator.between(5.0F, 11.0F));
         net.minecraft.world.level.storage.loot.LootPool.Builder book = this.basicPool(Items.BOOK)
            .apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10.0F, 16.0F)))
            .setRolls(UniformGenerator.between(0.0F, 2.0F));
         consumer.accept(SkiesLootProv.BLINDING_LIBRARY, this.tableOf(ImmutableList.of(paper, book)));
         net.minecraft.world.level.storage.loot.LootPool.Builder libraryChest = LootPool.lootPool()
            .add(LootTableReference.lootTableReference(SkiesLootProv.BLINDING_LIBRARY));
         consumer.accept(SkiesLootProv.BLINDING_LIBRARY_KEY, this.tableOf(ImmutableList.of(blindingKey, libraryChest)));
         net.minecraft.world.level.storage.loot.LootPool.Builder brightBreadAndAxe = this.poolOf(
               ImmutableList.of(
                  this.basicEntry(Items.BREAD, 1, 2).setWeight(4),
                  this.basicEntry(SkiesItems.pine_fruit, 1, 2).setWeight(3),
                  this.basicEntry(SkiesItems.aquite_axe).apply(this.setStick(SkiesItems.bluebright_stick)).apply(this.setDamage(83, 92))
               )
            )
            .setRolls(UniformGenerator.between(3.0F, 5.0F));
         consumer.accept(SkiesLootProv.BLINDING_PRISON_EVERBRIGHT, this.tableOf(ImmutableList.of(blindingKey, brightBreadAndAxe)));
         net.minecraft.world.level.storage.loot.LootPool.Builder dawnBreadAndAxe = this.poolOf(
               ImmutableList.of(
                  this.basicEntry(Items.BREAD, 3, 6).setWeight(4),
                  this.basicEntry(SkiesItems.scalefruit, 2, 7).setWeight(3),
                  this.basicEntry(SkiesItems.aquite_axe).apply(this.setStick(SkiesItems.lunar_stick)).apply(this.setDamage(84, 91)).setWeight(2)
               )
            )
            .setRolls(UniformGenerator.between(3.0F, 5.0F));
         consumer.accept(SkiesLootProv.BLINDING_PRISON_EVERDAWN, this.tableOf(ImmutableList.of(blindingKey, dawnBreadAndAxe)));
      }

      private void natureDungeon(BiConsumer<ResourceLocation, Builder> consumer) {
         consumer.accept(
            SkiesLootProv.NATURE_CHEST,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesBlocks.starlit_sapling, 2, 4),
                           this.basicEntry(SkiesBlocks.blush_blossom, 1, 2),
                           this.basicEntry(SkiesBlocks.nature_stone, 2, 4),
                           this.basicEntry(SkiesBlocks.glowing_nature_stone, 1, 2),
                           this.basicEntry(SkiesItems.brewberry, 1, 2)
                        )
                     )
                     .setRolls(UniformGenerator.between(5.0F, 7.0F))
                     .when(LootItemRandomChanceCondition.randomChance(1.0F)),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.black_brewberry, 1, 2),
                           this.basicEntry(SkiesItems.pink_brewberry, 0, 1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2),
                           this.basicEntry(SkiesItems.aquite, 1, 2)
                        )
                     )
                     .setRolls(UniformGenerator.between(2.0F, 4.0F))
                     .when(LootItemRandomChanceCondition.randomChance(0.4F)),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.diopside_gem),
                           this.basicEntry(SkiesItems.aquite_axe, 1, 1).apply(this.setStick(SkiesItems.starlit_stick)).apply(this.setDamage(85, 90)),
                           this.basicEntry(SkiesItems.starlit_spear, 1, 1).apply(this.setDamage(80, 85)),
                           this.basicEntry(Items.BOOK, 0, 2).apply(this.enchant(15, 20))
                        )
                     )
                     .setRolls(UniformGenerator.between(1.0F, 2.0F))
                     .when(LootItemRandomChanceCondition.randomChance(0.1F)),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.starlit_spear).apply(this.enchant(15, 20)).apply(this.setDamage(80, 85)),
                           this.basicEntry(Items.SADDLE),
                           this.basicEntry(SkiesItems.charoite)
                        )
                     )
                     .setRolls(ConstantValue.exactly(1.0F))
                     .when(LootItemRandomChanceCondition.randomChance(0.05F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.NATURE_CHEST_KEY,
            this.tableOf(
               ImmutableList.of(
                  this.basicPool(SkiesItems.nature_key),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.black_brewberry, 1, 2),
                           this.basicEntry(SkiesItems.pink_brewberry, 0, 1),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2),
                           this.basicEntry(SkiesItems.aquite, 1, 2)
                        )
                     )
                     .setRolls(UniformGenerator.between(2.0F, 4.0F))
               )
            )
         );
      }

      private void poisonDungeon(BiConsumer<ResourceLocation, Builder> consumer) {
         consumer.accept(
            SkiesLootProv.POISON_CHEST,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.STRING, 2, 3),
                           this.basicEntry(SkiesBlocks.moonstone, 2, 5),
                           this.basicEntry(SkiesBlocks.poison_stone, 2, 4),
                           this.basicEntry(SkiesBlocks.glowing_poison_stone, 1, 2),
                           this.basicEntry(SkiesItems.crescent_fruit, 1, 2)
                        )
                     )
                     .setRolls(UniformGenerator.between(5.0F, 7.0F))
                     .when(LootItemRandomChanceCondition.randomChance(1.0F)),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.SPIDER_EYE, 1, 2),
                           this.basicEntry(SkiesBlocks.spider_webbing, 1, 2),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2),
                           this.basicEntry(SkiesItems.aquite, 1, 2)
                        )
                     )
                     .setRolls(UniformGenerator.between(2.0F, 4.0F))
                     .when(LootItemRandomChanceCondition.randomChance(0.4F)),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.diopside_gem),
                           this.basicEntry(SkiesItems.horizonite_ingot, 1, 1),
                           this.basicEntry(SkiesItems.moonstone_shield, 1, 1).apply(this.setDamage(65, 80)),
                           this.basicEntry(Items.BOOK, 0, 2).apply(this.enchant(15, 20))
                        )
                     )
                     .setRolls(UniformGenerator.between(1.0F, 2.0F))
                     .when(LootItemRandomChanceCondition.randomChance(0.1F)),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.aquite_sword)
                              .apply(this.setStick(SkiesItems.maple_stick))
                              .apply(this.enchant(Enchantments.BANE_OF_ARTHROPODS))
                              .apply(this.setDamage(25, 55)),
                           this.basicEntry(SkiesItems.camel_saddle),
                           this.basicEntry(SkiesItems.charoite)
                        )
                     )
                     .setRolls(ConstantValue.exactly(1.0F))
                     .when(LootItemRandomChanceCondition.randomChance(0.05F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.POISON_CHEST_KEY,
            this.tableOf(
               ImmutableList.of(
                  this.basicPool(SkiesItems.poison_key),
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.SPIDER_EYE, 1, 2),
                           this.basicEntry(SkiesBlocks.spider_webbing, 1, 2),
                           this.basicEntry(SkiesItems.pyrope_gem, 1, 2),
                           this.basicEntry(SkiesItems.aquite, 1, 2)
                        )
                     )
                     .setRolls(UniformGenerator.between(2.0F, 4.0F))
               )
            )
         );
      }
   }

   private static class SkyEntityLoot extends VanillaEntityLoot implements SkiesLootProv.LootPoolUtil {
      public void generate() {
         this.add(SkiesEntityTypes.GATEKEEPER, LootTable.lootTable());
         this.add(SkiesEntityTypes.SUMMONER, LootTable.lootTable());
         this.add(SkiesEntityTypes.ALCHEMIST, LootTable.lootTable());
         this.add(SkiesEntityTypes.ARACHNARCH, LootTable.lootTable());
         this.add(SkiesEntityTypes.STARLIT_CRUSHER, LootTable.lootTable());
         net.minecraft.world.level.storage.loot.LootPool.Builder azulfoBeef = this.lootingPool(SkiesItems.carabeef, 2, 3, 0, 1)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         net.minecraft.world.level.storage.loot.LootPool.Builder auzlfoHorn = this.lootingPool(SkiesItems.azulfo_horn, -1, 2, 0, 1)
            .when(LootItemKilledByPlayerCondition.killedByPlayer());
         this.add(SkiesEntityTypes.AZULFO, this.tableOf(ImmutableList.of(azulfoBeef, auzlfoHorn)));
         net.minecraft.world.level.storage.loot.LootPool.Builder rawVenison = this.lootingPool(SkiesItems.venison, 1, 2, 0, 1)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.REINDEER, this.tableOf(rawVenison));
         net.minecraft.world.level.storage.loot.LootPool.Builder mutton = this.lootingPool(Items.MUTTON, 1, 2, 0, 1).apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.STARDUST_RAM, this.tableOf(mutton));
         Arrays.asList(DyeColor.values())
            .forEach(
               c -> this.add(
                  SkiesEntityTypes.STARDUST_RAM,
                  BlueSkies.locate("entities/" + this.entityName(SkiesEntityTypes.STARDUST_RAM) + "/" + c),
                  this.ramLootTable((ItemLike)BuiltInRegistries.BLOCK.get(ResourceLocation.parse(c + "_wool")))
               )
            );
         net.minecraft.world.level.storage.loot.LootPool.Builder starFlare = this.lootingPool(SkiesBlocks.star_flare, 0, 2, 0, 1)
            .when(LootItemKilledByPlayerCondition.killedByPlayer());
         net.minecraft.world.level.storage.loot.LootPool.Builder soulFragment = this.lootingPool(SkiesItems.soul_fragment, 0, 2, 0, 1)
            .when(LootItemKilledByPlayerCondition.killedByPlayer());
         this.add(SkiesEntityTypes.FROST_SPIRIT, this.tableOf(ImmutableList.of(starFlare, soulFragment)));
         net.minecraft.world.level.storage.loot.LootPool.Builder ice = this.lootingPool(Items.ICE, 0, 2, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder packedIce = this.lootingPool(Items.PACKED_ICE, 0, 2, 0, 1);
         this.add(SkiesEntityTypes.ARMORED_FROST_SPIRIT, this.tableOf(ice));
         this.add(
            SkiesEntityTypes.ARMORED_FROST_SPIRIT,
            BlueSkies.locate("entities/" + this.entityName(SkiesEntityTypes.ARMORED_FROST_SPIRIT) + "_packed"),
            this.tableOf(packedIce)
         );
         this.add(SkiesEntityTypes.CRYNOCEROUS, this.tableOf(ice.setRolls(UniformGenerator.between(1.0F, 2.0F))));
         this.add(
            SkiesEntityTypes.CRYNOCEROUS,
            BlueSkies.locate("entities/" + this.entityName(SkiesEntityTypes.CRYNOCEROUS) + "_packed"),
            this.tableOf(packedIce.setRolls(UniformGenerator.between(1.0F, 2.0F)))
         );
         this.add(SkiesEntityTypes.JELLY_DRIFTER, LootTable.lootTable());
         net.minecraft.world.level.storage.loot.LootPool.Builder feather = this.lootingPool(Items.FEATHER, 1, 2, 0, 1);
         this.add(SkiesEntityTypes.SNOW_OWL, this.tableOf(feather));
         this.add(SkiesEntityTypes.POLARGEIST, this.tableOf(soulFragment));
         net.minecraft.world.level.storage.loot.LootPool.Builder glowstoneDust = this.lootingPool(Items.GLOWSTONE_DUST, 0, 1, 0, 1);
         this.add(SkiesEntityTypes.FIREFLY, this.tableOf(glowstoneDust));
         net.minecraft.world.level.storage.loot.LootPool.Builder bugGuts = this.lootingPool(SkiesItems.bug_guts, 0, 2, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder slimeBall = this.lootingPool(Items.SLIME_BALL, 0, 1, 0, 1);
         this.add(SkiesEntityTypes.NYCTOFLY, this.tableOf(ImmutableList.of(bugGuts, slimeBall)));
         net.minecraft.world.level.storage.loot.LootPool.Builder string = this.lootingPool(Items.STRING, 0, 2, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder spiderEye = this.lootingPool(Items.SPIDER_EYE, -1, 1, 0, 1)
            .when(LootItemKilledByPlayerCondition.killedByPlayer());
         this.add(SkiesEntityTypes.VENOM_SPIDER, this.tableOf(ImmutableList.of(string, spiderEye, bugGuts)));
         net.minecraft.world.level.storage.loot.LootPool.Builder fur = this.lootingPool(SkiesItems.fox_pelt, 1, 2, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder salmon = this.lootingPool(Items.SALMON, -1, 1, 0, 1).apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.COSMIC_FOX, this.tableOf(ImmutableList.of(fur, salmon)));
         net.minecraft.world.level.storage.loot.LootPool.Builder rawMoonstone = this.lootingPool(SkiesBlocks.vitreous_moonstone, 0, 2, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder bone = this.lootingPool(Items.BONE, 0, 2, 0, 1)
            .when(LootItemKilledByPlayerCondition.killedByPlayer());
         this.add(SkiesEntityTypes.CRYSTAL_CAMEL, this.tableOf(ImmutableList.of(rawMoonstone, bone)));
         this.add(SkiesEntityTypes.EMBERBACK, this.tableOf(bugGuts));
         net.minecraft.world.level.storage.loot.LootPool.Builder monitorTail = this.lootingPool(SkiesItems.monitor_tail, 1, 3, 0, 1)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.SHADE_MONITOR, this.tableOf(monitorTail));
         this.add(SkiesEntityTypes.SLIV, LootTable.lootTable());
         this.add(
            SkiesEntityTypes.ARTIFICIAL_GOLEM,
            LootTable.lootTable()
               .withPool(
                  LootPool.lootPool()
                     .add(TagEntry.expandTag(SkiesItemTags.BLINDING_STONES))
                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                     .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
               )
         );
         this.add(SkiesEntityTypes.NESTED_SPIDER, LootTable.lootTable());
         net.minecraft.world.level.storage.loot.LootPool.Builder natureStone = this.lootingPool(SkiesBlocks.nature_stone, 0, 1, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder starlitVines = this.lootingPool(SkiesBlocks.starlit_vine, 0, 1, 0, 1);
         this.add(SkiesEntityTypes.STONELET, this.tableOf(ImmutableList.of(natureStone, starlitVines)));
         this.add(SkiesEntityTypes.SPEWTER, this.tableOf(slimeBall));
         net.minecraft.world.level.storage.loot.LootPool.Builder pinhead = this.lootingPool(SkiesBlocks.snowcap_pinhead, 0, 2, 0, 1);
         this.add(SkiesEntityTypes.SHRUMPTY, this.tableOf(pinhead));
         net.minecraft.world.level.storage.loot.LootPool.Builder leather = this.lootingPool(Items.LEATHER, 0, 1, 0, 1);
         net.minecraft.world.level.storage.loot.LootPool.Builder prowlerFish = this.lootingPool(SkiesItems.municipal_monkfish, 0, 1, 0, 1)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.DIOPHYDE_PROWLER, this.tableOf(ImmutableList.of(leather, prowlerFish)));
         this.add(SkiesEntityTypes.WHISTLESHELL_CRAB, LootTable.lootTable());
         this.add(SkiesEntityTypes.SUPPORTER_PET, LootTable.lootTable());
         net.minecraft.world.level.storage.loot.LootPool.Builder crogreBugGuts = this.lootingPool(SkiesItems.bug_guts, 0, 1, 0, 1);
         this.add(SkiesEntityTypes.CROGRE, this.tableOf(crogreBugGuts));
         net.minecraft.world.level.storage.loot.LootPool.Builder fishBoneMeal = this.basicPool(Items.BONE_MEAL)
            .when(LootItemRandomChanceCondition.randomChance(0.05F));
         net.minecraft.world.level.storage.loot.LootPool.Builder monkfish = this.lootingPool(SkiesItems.municipal_monkfish, 1, 1, 0, 0)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.MUNICIPAL_MONKFISH, this.tableOf(ImmutableList.of(monkfish, fishBoneMeal)));
         net.minecraft.world.level.storage.loot.LootPool.Builder flatfish = this.lootingPool(SkiesItems.grittle_flatfish, 1, 1, 0, 0)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.GRITTLE_FLATFISH, this.tableOf(ImmutableList.of(flatfish, fishBoneMeal)));
         net.minecraft.world.level.storage.loot.LootPool.Builder moki = this.lootingPool(SkiesItems.charscale_moki, 1, 1, 0, 0)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.CHARSCALE_MOKI, this.tableOf(ImmutableList.of(moki, fishBoneMeal)));
         this.add(SkiesEntityTypes.INFESTED_SWARMER, this.tableOf(bugGuts));
         net.minecraft.world.level.storage.loot.LootPool.Builder tunid = this.lootingPool(SkiesItems.horizofin_tunid, 1, 1, 0, 0)
            .apply(this.smeltItem(ENTITY_ON_FIRE));
         this.add(SkiesEntityTypes.HORIZOFIN_TUNID, this.tableOf(ImmutableList.of(tunid, fishBoneMeal)));
         this.add(SkiesEntityTypes.SECLAM, LootTable.lootTable());
      }

      private Builder ramLootTable(ItemLike wool) {
         return LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(wool)))
            .withPool(
               LootPool.lootPool()
                  .setRolls(ConstantValue.exactly(1.0F))
                  .add(LootTableReference.lootTableReference(SkiesEntityTypes.STARDUST_RAM.getDefaultLootTable()))
            );
      }

      private net.minecraft.world.level.storage.loot.LootPool.Builder lootingPool(ItemLike item, int min, int max, int minLooting, int maxLooting) {
         return this.basicPool(item, min, max).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(minLooting, maxLooting)));
      }

      private String entityName(EntityType<?> entity) {
         return BuiltInRegistries.ENTITY_TYPE.getKey(entity).getPath();
      }

      protected Stream<EntityType<?>> getKnownEntityTypes() {
         return BuiltInRegistries.ENTITY_TYPE.stream().filter(e -> BuiltInRegistries.ENTITY_TYPE.getKey(e).getNamespace().contains("blue_skies"));
      }
   }

   public static class SkyGiftLoot implements SkiesLootProv.LootPoolUtil, LootTableSubProvider {
      public void generate(BiConsumer<ResourceLocation, Builder> consumer) {
         consumer.accept(
            SkiesLootProv.STARGAZER_GIFTS,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesBlocks.star_flare, 1, 2).setWeight(3),
                           this.basicEntry(Blocks.GLASS, 1, 2).setWeight(3),
                           this.basicEntry(SkiesItems.falsite_ingot).setWeight(1)
                        )
                     )
                     .setRolls(ConstantValue.exactly(1.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.WRANGLER_GIFTS,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(SkiesItems.brewberry, 3, 4).setWeight(2),
                           this.basicEntry(SkiesItems.crescent_fruit, 2, 3).setWeight(2),
                           this.basicEntry(SkiesItems.azulfo_horn, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.fox_pelt, 1, 2).setWeight(2),
                           this.basicEntry(Items.LEAD, 1, 3).setWeight(1)
                        )
                     )
                     .setRolls(ConstantValue.exactly(1.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.SHOVELER_GIFTS,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.SNOWBALL, 4, 6).setWeight(3),
                           this.basicEntry(SkiesItems.falsite_ingot, 1, 2).setWeight(2),
                           this.basicEntry(SkiesItems.aquite_shovel).apply(this.setStick(SkiesItems.bluebright_stick)).setWeight(1)
                        )
                     )
                     .setRolls(ConstantValue.exactly(1.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.SUMMONER_GIFTS,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(ImmutableList.of(this.basicEntry(SkiesItems.soul_fragment, 2, 3).setWeight(3), this.basicEntry(Items.BOOK).setWeight(2)))
                     .setRolls(ConstantValue.exactly(1.0F))
               )
            )
         );
         consumer.accept(
            SkiesLootProv.ALCHEMIST_GIFTS,
            this.tableOf(
               ImmutableList.of(
                  this.poolOf(
                        ImmutableList.of(
                           this.basicEntry(Items.GOLD_NUGGET, 2, 4).setWeight(6),
                           this.basicEntry(Items.BLAZE_POWDER, 1, 2).setWeight(4),
                           this.basicEntry(Items.GOLD_INGOT).setWeight(1)
                        )
                     )
                     .setRolls(ConstantValue.exactly(1.0F))
               )
            )
         );
      }
   }
}
