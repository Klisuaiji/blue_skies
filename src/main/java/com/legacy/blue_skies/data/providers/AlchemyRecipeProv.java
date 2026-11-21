package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.alchemy.AlchemyRecipe;
import com.legacy.blue_skies.data.objects.alchemy.CatylistRecipe;
import com.legacy.blue_skies.data.objects.alchemy.TransmuteRecipe;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags.Items;

public class AlchemyRecipeProv extends SkiesDataProvider<AlchemyRecipe> {
   public AlchemyRecipeProv(DataGenerator generator) {
      super(generator, AlchemyRecipe.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, AlchemyRecipe> gatherData() {
      Map<ResourceLocation, AlchemyRecipe> registry = new HashMap<>();
      transmute(registry, TransmuteRecipe.builder("banner").withTag(ItemTags.BANNERS));
      transmute(
         registry, TransmuteRecipe.builder("bed").withTag(ItemTags.BEDS).resultBlacklist("good_nights_sleep", "luxurious_bed", "wretched_bed", "strange_bed")
      );
      transmute(registry, TransmuteRecipe.builder("carpet").withTag(ItemTags.WOOL_CARPETS));
      colored(registry, "concrete");
      colored(registry, "concrete_powder");
      transmute(registry, TransmuteRecipe.builder("dye").withTag(Items.DYES));
      colored(registry, "glazed_terracotta");
      transmute(registry, TransmuteRecipe.builder("stained_glass").withTag(Items.STAINED_GLASS));
      transmute(registry, TransmuteRecipe.builder("stained_glass_pane").withTag(Items.STAINED_GLASS_PANES));
      colored(registry, "terracotta");
      transmute(registry, TransmuteRecipe.builder("wool").withTag(ItemTags.WOOL));
      transmute(registry, TransmuteRecipe.builder("candle").withTag(ItemTags.CANDLES));
      coral(registry, "coral_block", "%s_coral_block");
      coral(registry, "coral", "%s_coral");
      coral(registry, "coral_fan", "%s_coral_fan");
      coral(registry, "dead_coral_block", "dead_%s_coral_block");
      coral(registry, "dead_coral", "dead_%s_coral");
      coral(registry, "dead_coral_fan", "dead_%s_coral_fan");
      transmute(
         registry,
         TransmuteRecipe.builder("log")
            .withTag(ItemTags.LOGS_THAT_BURN)
            .resultBlacklist(
               "botania",
               "livingwood_log",
               "stripped_livingwood_log",
               "glimmering_livingwood_log",
               "glimmering_stripped_livingwood_log",
               "livingwood",
               "stripped_livingwood",
               "glimmering_livingwood",
               "glimmering_stripped_livingwood",
               "dreamwood_log",
               "stripped_dreamwood_log",
               "glimmering_dreamwood_log",
               "glimmering_stripped_dreamwood_log",
               "dreamwood",
               "stripped_dreamwood",
               "glimmering_dreamwood",
               "glimmering_stripped_dreamwood"
            )
      );
      transmute(
         registry,
         TransmuteRecipe.builder("planks")
            .withTag(ItemTags.PLANKS)
            .resultBlacklist("botania", "livingwood_planks", "mossy_livingwood_planks", "dreamwood_planks", "mossy_dreamwood_planks")
      );
      netherWood(registry, "stem", "%s_stem");
      netherWood(registry, "hyphae", "%s_hyphae");
      netherWood(registry, "stripped_stem", "stripped_%s_stem");
      netherWood(registry, "stripped_hyphae", "stripped_%s_hyphae");
      transmute(registry, TransmuteRecipe.builder("iron").items(net.minecraft.world.item.Items.IRON_INGOT, SkiesItems.ventium_ingot));
      transmute(registry, TransmuteRecipe.builder("dust").items(net.minecraft.world.item.Items.REDSTONE, net.minecraft.world.item.Items.GLOWSTONE_DUST));
      transmute(
         registry,
         TransmuteRecipe.builder("stone")
            .items(
               net.minecraft.world.item.Items.STONE,
               net.minecraft.world.item.Items.GRANITE,
               net.minecraft.world.item.Items.ANDESITE,
               net.minecraft.world.item.Items.DIORITE,
               SkiesBlocks.turquoise_stone,
               SkiesBlocks.lunar_stone,
               SkiesBlocks.taratite,
               SkiesBlocks.umber,
               SkiesBlocks.rimestone,
               SkiesBlocks.cinderstone
            )
      );
      transmute(
         registry,
         TransmuteRecipe.builder("cobblestone")
            .items(net.minecraft.world.item.Items.COBBLESTONE, SkiesBlocks.turquoise_cobblestone, SkiesBlocks.lunar_cobblestone)
      );
      transmute(registry, TransmuteRecipe.builder("nether_stone").items(net.minecraft.world.item.Items.NETHERRACK, net.minecraft.world.item.Items.BLACKSTONE));
      transmute(
         registry,
         TransmuteRecipe.builder("dirt")
            .items(
               net.minecraft.world.item.Items.DIRT,
               net.minecraft.world.item.Items.COARSE_DIRT,
               SkiesBlocks.turquoise_dirt,
               SkiesBlocks.coarse_turquoise_dirt,
               SkiesBlocks.lunar_dirt,
               SkiesBlocks.coarse_lunar_dirt
            )
      );
      transmute(
         registry,
         TransmuteRecipe.builder("grass_block")
            .items(
               net.minecraft.world.item.Items.GRASS_BLOCK,
               SkiesBlocks.turquoise_grass_block,
               SkiesBlocks.lunar_grass_block,
               SkiesBlocks.turquoise_comet_grass_block,
               SkiesBlocks.lunar_comet_grass_block
            )
      );
      transmute(
         registry,
         TransmuteRecipe.builder("grass")
            .items(net.minecraft.world.item.Items.SHORT_GRASS, SkiesBlocks.short_turquoise_grass, SkiesBlocks.short_lunar_grass, SkiesBlocks.short_comet_grass)
      );
      transmute(registry, TransmuteRecipe.builder("sand").withTag(Items.SAND).items(SkiesBlocks.midnight_sand, SkiesBlocks.crystal_sand));
      transmute(
         registry,
         TransmuteRecipe.builder("glass")
            .withTag(Items.GLASS_COLORLESS)
            .items(SkiesBlocks.midnight_glass, SkiesBlocks.crystal_glass)
            .resultBlacklist(net.minecraft.world.item.Items.TINTED_GLASS)
      );
      transmute(
         registry,
         TransmuteRecipe.builder("glass_pane").withTag(Items.GLASS_PANES_COLORLESS).items(SkiesBlocks.midnight_glass_pane, SkiesBlocks.crystal_glass_pane)
      );
      transmute(registry, TransmuteRecipe.builder("soul_sand").items(net.minecraft.world.item.Items.SOUL_SAND, net.minecraft.world.item.Items.SOUL_SOIL));
      transmute(registry, TransmuteRecipe.builder("gourd").items(net.minecraft.world.item.Items.MELON, net.minecraft.world.item.Items.PUMPKIN));
      transmute(registry, TransmuteRecipe.builder("coal").withTag(ItemTags.COALS));
      transmute(registry, TransmuteRecipe.builder("cobweb").items(net.minecraft.world.item.Items.COBWEB, SkiesBlocks.spider_webbing));
      transmute(registry, TransmuteRecipe.builder("vine").withTag(SkiesItemTags.VINES).items(net.minecraft.world.item.Items.VINE));
      transmute(
         registry, TransmuteRecipe.builder("nether_vine").items(net.minecraft.world.item.Items.WEEPING_VINES, net.minecraft.world.item.Items.TWISTING_VINES)
      );
      transmute(
         registry,
         TransmuteRecipe.builder("lily_pad").items(net.minecraft.world.item.Items.LILY_PAD, SkiesBlocks.chilled_lily_pad, SkiesBlocks.moonlit_water_lily)
      );
      transmute(registry, TransmuteRecipe.builder("mushroom").items(net.minecraft.world.item.Items.RED_MUSHROOM, net.minecraft.world.item.Items.BROWN_MUSHROOM));
      transmute(registry, TransmuteRecipe.builder("brick").items(net.minecraft.world.item.Items.BRICK, net.minecraft.world.item.Items.NETHER_BRICK));
      transmute(registry, TransmuteRecipe.builder("bricks").items(net.minecraft.world.item.Items.BRICKS, net.minecraft.world.item.Items.NETHER_BRICKS));
      transmute(registry, TransmuteRecipe.builder("dripstone").items(SkiesBlocks.turquoise_dripstone, SkiesBlocks.lunar_dripstone));
      transmute(registry, TransmuteRecipe.builder("crystal").items(SkiesBlocks.moonstone_crystal, SkiesBlocks.sunstone_crystal));
      transmute(
         registry, TransmuteRecipe.builder("nether_fungus").items(net.minecraft.world.item.Items.CRIMSON_FUNGUS, net.minecraft.world.item.Items.WARPED_FUNGUS)
      );
      transmute(
         registry,
         TransmuteRecipe.builder("nether_roots")
            .items(net.minecraft.world.item.Items.CRIMSON_ROOTS, net.minecraft.world.item.Items.WARPED_ROOTS, net.minecraft.world.item.Items.NETHER_SPROUTS)
      );
      transmute(registry, TransmuteRecipe.builder("nylium").items(net.minecraft.world.item.Items.CRIMSON_NYLIUM, net.minecraft.world.item.Items.WARPED_NYLIUM));
      transmute(
         registry,
         TransmuteRecipe.builder("wart_block").items(net.minecraft.world.item.Items.NETHER_WART_BLOCK, net.minecraft.world.item.Items.WARPED_WART_BLOCK)
      );
      transmute(registry, TransmuteRecipe.builder("dungeon_stone").items(SkiesBlocks.blinding_stone, SkiesBlocks.nature_stone, SkiesBlocks.poison_stone));
      catylist(
         registry, CatylistRecipe.builder("sapling").input(ItemTags.SAPLINGS).catylist(net.minecraft.world.item.Items.ECHO_SHARD).results(ItemTags.SAPLINGS)
      );
      catylist(
         registry, "horizonite_from_iron", net.minecraft.world.item.Items.IRON_INGOT, net.minecraft.world.item.Items.BLAZE_ROD, SkiesItems.horizonite_ingot
      );
      catylist(registry, "falsite_from_iron", net.minecraft.world.item.Items.IRON_INGOT, SkiesItems.soul_fragment, SkiesItems.falsite_ingot);
      catylist(
         registry,
         "netherrack_from_stone",
         net.minecraft.world.item.Items.STONE,
         net.minecraft.world.item.Items.NETHER_WART,
         net.minecraft.world.item.Items.NETHERRACK
      );
      catylist(
         registry,
         "end_stone_from_stone",
         net.minecraft.world.item.Items.STONE,
         net.minecraft.world.item.Items.CHORUS_FRUIT,
         net.minecraft.world.item.Items.END_STONE
      );
      catylist(
         registry,
         "grass_block_from_dirt",
         net.minecraft.world.item.Items.DIRT,
         net.minecraft.world.item.Items.BONE_MEAL,
         net.minecraft.world.item.Items.GRASS_BLOCK
      );
      catylist(
         registry, "turquoise_grass_block_from_dirt", SkiesBlocks.turquoise_dirt, net.minecraft.world.item.Items.BONE_MEAL, SkiesBlocks.turquoise_grass_block
      );
      catylist(registry, "lunar_grass_block_from_dirt", SkiesBlocks.lunar_dirt, net.minecraft.world.item.Items.BONE_MEAL, SkiesBlocks.lunar_grass_block);
      catylist(
         registry,
         "crimson_nylium_from_netherrack",
         net.minecraft.world.item.Items.NETHERRACK,
         net.minecraft.world.item.Items.CRIMSON_FUNGUS,
         net.minecraft.world.item.Items.CRIMSON_NYLIUM
      );
      catylist(
         registry,
         "warped_nylium_from_netherrack",
         net.minecraft.world.item.Items.NETHERRACK,
         net.minecraft.world.item.Items.WARPED_FUNGUS,
         net.minecraft.world.item.Items.WARPED_NYLIUM
      );
      catylist(registry, "lunar_mud_from_dirt", SkiesBlocks.lunar_dirt, net.minecraft.world.item.Items.SLIME_BALL, SkiesBlocks.lunar_mud);
      catylist(registry, "soul_sand_from_sand", net.minecraft.world.item.Items.SAND, SkiesItems.soul_fragment, net.minecraft.world.item.Items.SOUL_SAND);
      catylist(registry, "soul_soil_from_dirt", net.minecraft.world.item.Items.DIRT, SkiesItems.soul_fragment, net.minecraft.world.item.Items.SOUL_SOIL);
      catylist(registry, "blinding_stone_from_ethereal_arc", SkiesBlocks.turquoise_stone, SkiesItems.ethereal_arc, SkiesBlocks.blinding_stone);
      catylist(registry, "blinding_stone_from_dusk_arc", SkiesBlocks.lunar_stone, SkiesItems.dusk_arc, SkiesBlocks.blinding_stone);
      catylist(registry, "nature_stone_from_arc", SkiesBlocks.turquoise_stone, SkiesItems.nature_arc, SkiesBlocks.nature_stone);
      catylist(registry, "poison_stone_from_arc", SkiesBlocks.lunar_stone, SkiesItems.poison_arc, SkiesBlocks.poison_stone);
      catylist(
         registry, "gold_lol", net.minecraft.world.item.Items.NETHERITE_INGOT, net.minecraft.world.item.Items.POTION, net.minecraft.world.item.Items.GOLD_INGOT
      );
      return registry;
   }

   private static void transmute(Map<ResourceLocation, AlchemyRecipe> registry, TransmuteRecipe.Builder builder) {
      registry.put(BlueSkies.locate(builder.getName()), builder.build());
   }

   private static void catylist(Map<ResourceLocation, AlchemyRecipe> registry, CatylistRecipe.Builder builder) {
      registry.put(BlueSkies.locate(builder.getName()), builder.build());
   }

   private static void catylist(Map<ResourceLocation, AlchemyRecipe> registry, String name, ItemLike input, ItemLike catylist, ItemLike... outputs) {
      catylist(registry, CatylistRecipe.builder(name).input(input).catylist(catylist).results(outputs));
   }

   private static void colored(Map<ResourceLocation, AlchemyRecipe> registry, String name) {
      List<ItemLike> items = new ArrayList<>();

      for (DyeColor color : DyeColor.values()) {
         ResourceLocation itemName = ResourceLocation.parse(color.getSerializedName() + "_" + name);
         if (BuiltInRegistries.ITEM.containsKey(itemName)) {
            items.add((ItemLike)BuiltInRegistries.ITEM.get(itemName));
         } else {
            LOGGER.warn("No item exists for {}", itemName.toString());
         }
      }

      transmute(registry, TransmuteRecipe.builder(name).items(items));
   }

   private static void coral(Map<ResourceLocation, AlchemyRecipe> registry, String name, String format) {
      List<ItemLike> items = new ArrayList<>();

      for (String coral : Arrays.asList("tube", "brain", "bubble", "fire", "horn")) {
         ResourceLocation itemName = ResourceLocation.parse(String.format(format, coral));
         if (BuiltInRegistries.ITEM.containsKey(itemName)) {
            items.add((ItemLike)BuiltInRegistries.ITEM.get(itemName));
         } else {
            LOGGER.warn("No item exists for {}", itemName.toString());
         }
      }

      transmute(registry, TransmuteRecipe.builder(name).items(items));
   }

   private static void netherWood(Map<ResourceLocation, AlchemyRecipe> registry, String name, String format) {
      List<ItemLike> items = new ArrayList<>();

      for (String coral : Arrays.asList("crimson", "warped")) {
         ResourceLocation itemName = ResourceLocation.parse(String.format(format, coral));
         if (BuiltInRegistries.ITEM.containsKey(itemName)) {
            items.add((ItemLike)BuiltInRegistries.ITEM.get(itemName));
         } else {
            LOGGER.warn("No item exists for {}", itemName.toString());
         }
      }

      transmute(registry, TransmuteRecipe.builder(name).items(items));
   }
}
