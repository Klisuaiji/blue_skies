package com.legacy.blue_skies.events;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.structure_gel.core.registry.SGRegistry.Blocks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SkiesMappingChanges {
   public static void addBlockAliases(Registry<Block> registry) {
      add(registry, "poison_slab", SkiesBlocks.poison_stone_slab);
      add(registry, "poison_stairs", SkiesBlocks.poison_stone_stairs);
      add(registry, "poison_wall", SkiesBlocks.poison_stonebrick_wall);
      add(registry, "glowing_poison_slab", SkiesBlocks.glowing_poison_stone_slab);
      add(registry, "glowing_poison_stairs", SkiesBlocks.glowing_poison_stone_stairs);
      add(registry, "glowing_poison_wall", SkiesBlocks.poison_stonebrick_wall);
      add(registry, "nature_slab", SkiesBlocks.nature_stone_slab);
      add(registry, "nature_stairs", SkiesBlocks.nature_stone_stairs);
      add(registry, "nature_wall", SkiesBlocks.nature_stonebrick_wall);
      add(registry, "glowing_nature_slab", SkiesBlocks.glowing_nature_stone_slab);
      add(registry, "glowing_nature_stairs", SkiesBlocks.glowing_nature_stone_stairs);
      add(registry, "glowing_nature_wall", SkiesBlocks.glowing_nature_stonebrick_wall);
      add(registry, "structure_filler", Blocks.RED_GEL.getName());
      add(registry, "corn", SkiesBlocks.pine_fruits);
      add(registry, "cabbage", SkiesBlocks.winter_leaves);
      add(registry, "raspberry_bush", SkiesBlocks.brewberry_bush);
      add(registry, "potted_raspberry_bush", SkiesBlocks.potted_brewberry_bush);
      add(registry, "tomatoes", SkiesBlocks.scalefruits);
      add(registry, "green_beans", SkiesBlocks.fiery_beans);
      add(registry, "peanuts", SkiesBlocks.solnuts);
      add(registry, "nectarine_fruit", SkiesBlocks.crescent_fruit);
      add(registry, "nectarine_leaves", SkiesBlocks.crescent_fruit_leaves);
      add(registry, "nectarine_sapling", SkiesBlocks.crescent_fruit_sapling);
      add(registry, "potted_nectarine_sapling", SkiesBlocks.potted_crescent_fruit_sapling);
      add(registry, "turquoise_block", SkiesBlocks.aquite_block);
      add(registry, "everbright_turquoise_ore", SkiesBlocks.everbright_aquite_ore);
      add(registry, "everdawn_turquoise_ore", SkiesBlocks.everdawn_aquite_ore);
      add(registry, "raw_moonstone", SkiesBlocks.vitreous_moonstone);
      add(registry, "cherry_pie", SkiesBlocks.comet_berry_pie);
      add(registry, "cherry_log", SkiesBlocks.comet_log);
      add(registry, "cherry_wood", SkiesBlocks.comet_wood);
      add(registry, "stripped_cherry_log", SkiesBlocks.stripped_comet_log);
      add(registry, "stripped_cherry_wood", SkiesBlocks.stripped_comet_wood);
      add(registry, "cherry_planks", SkiesBlocks.comet_planks);
      add(registry, "cherry_stairs", SkiesBlocks.comet_stairs);
      add(registry, "cherry_slab", SkiesBlocks.comet_slab);
      add(registry, "cherry_fence", SkiesBlocks.comet_fence);
      add(registry, "cherry_fence_gate", SkiesBlocks.comet_fence_gate);
      add(registry, "cherry_door", SkiesBlocks.comet_door);
      add(registry, "cherry_trapdoor", SkiesBlocks.comet_trapdoor);
      add(registry, "cherry_pressure_plate", SkiesBlocks.comet_pressure_plate);
      add(registry, "cherry_button", SkiesBlocks.comet_button);
      add(registry, "cherry_ladder", SkiesBlocks.comet_ladder);
      add(registry, "cherry_bookshelf", SkiesBlocks.comet_bookshelf);
      add(registry, "cherry_chest", SkiesBlocks.comet_chest);
      add(registry, "cherry_sign", SkiesBlocks.comet_sign);
      add(registry, "cherry_wall_sign", SkiesBlocks.comet_wall_sign);
      add(registry, "cherry_crafting_table", SkiesBlocks.comet_crafting_table);
      add(registry, "cherry_leaves", SkiesBlocks.comet_leaves);
      add(registry, "cherry_grass", SkiesBlocks.short_comet_grass);
      add(registry, "turquoise_cherry_grass_block", SkiesBlocks.turquoise_comet_grass_block);
      add(registry, "lunar_cherry_grass_block", SkiesBlocks.lunar_comet_grass_block);
      add(registry, "cherry_sapling", SkiesBlocks.comet_sapling);
      add(registry, "potted_cherry_sapling", SkiesBlocks.potted_comet_sapling);
      add(registry, "turquoise_grass", SkiesBlocks.short_turquoise_grass);
      add(registry, "lunar_grass", SkiesBlocks.short_lunar_grass);
      add(registry, "comet_grass", SkiesBlocks.short_comet_grass);
   }

   public static void addItemAliases(Registry<Item> registry) {
      add(registry, "poison_slab", (ItemLike)SkiesBlocks.poison_stone_slab);
      add(registry, "poison_stairs", (ItemLike)SkiesBlocks.poison_stone_stairs);
      add(registry, "poison_wall", (ItemLike)SkiesBlocks.poison_stonebrick_wall);
      add(registry, "glowing_poison_slab", (ItemLike)SkiesBlocks.glowing_poison_stone_slab);
      add(registry, "glowing_poison_stairs", (ItemLike)SkiesBlocks.glowing_poison_stone_stairs);
      add(registry, "glowing_poison_wall", (ItemLike)SkiesBlocks.poison_stonebrick_wall);
      add(registry, "nature_slab", (ItemLike)SkiesBlocks.nature_stone_slab);
      add(registry, "nature_stairs", (ItemLike)SkiesBlocks.nature_stone_stairs);
      add(registry, "nature_wall", (ItemLike)SkiesBlocks.nature_stonebrick_wall);
      add(registry, "glowing_nature_slab", (ItemLike)SkiesBlocks.glowing_nature_stone_slab);
      add(registry, "glowing_nature_stairs", (ItemLike)SkiesBlocks.glowing_nature_stone_stairs);
      add(registry, "glowing_nature_wall", (ItemLike)SkiesBlocks.glowing_nature_stonebrick_wall);
      add(registry, "structure_filler", Blocks.RED_GEL.getName());
      add(registry, "corn", SkiesItems.pine_fruit);
      add(registry, "cabbage", SkiesItems.winter_leaves);
      add(registry, "raspberry_bush", (ItemLike)SkiesBlocks.brewberry_bush);
      add(registry, "raspberry", SkiesItems.brewberry);
      add(registry, "pink_raspberry", SkiesItems.pink_brewberry);
      add(registry, "black_raspberry", SkiesItems.black_brewberry);
      add(registry, "tomato", SkiesItems.scalefruit);
      add(registry, "green_bean", SkiesItems.fiery_beans);
      add(registry, "peanut", SkiesItems.solnut);
      add(registry, "nectarine", SkiesItems.crescent_fruit);
      add(registry, "nectarine_leaves", SkiesBlocks.crescent_fruit_leaves);
      add(registry, "nectarine_sapling", (ItemLike)SkiesBlocks.crescent_fruit_sapling);
      add(registry, "corn_seeds", SkiesItems.pine_fruit_seeds);
      add(registry, "cabbage_seeds", SkiesItems.winter_leaf_seeds);
      add(registry, "tomato_seeds", SkiesItems.scalefruit_seeds);
      add(registry, "green_bean_seeds", SkiesItems.fiery_bean_seeds);
      add(registry, "hellfire_arc", SkiesItems.life_arc);
      add(registry, "blue_lore", SkiesItems.blue_journal);
      add(registry, "turquoise_gem", SkiesItems.aquite);
      add(registry, "turquoise_sword", SkiesItems.aquite_sword);
      add(registry, "turquoise_pickaxe", SkiesItems.aquite_pickaxe);
      add(registry, "turquoise_axe", SkiesItems.aquite_axe);
      add(registry, "turquoise_shovel", SkiesItems.aquite_shovel);
      add(registry, "turquoise_hoe", SkiesItems.aquite_hoe);
      add(registry, "turquoise_helmet", SkiesItems.aquite_helmet);
      add(registry, "turquoise_chestplate", SkiesItems.aquite_chestplate);
      add(registry, "turquoise_leggings", SkiesItems.aquite_leggings);
      add(registry, "turquoise_boots", SkiesItems.aquite_boots);
      add(registry, "mudkipz_sword", SkiesItems.debug_sword);
      add(registry, "turquoise_block", (ItemLike)SkiesBlocks.aquite_block);
      add(registry, "everbright_turquoise_ore", SkiesBlocks.everbright_aquite_ore);
      add(registry, "everdawn_turquoise_ore", SkiesBlocks.everdawn_aquite_ore);
      add(registry, "raw_azulfo_beef", SkiesItems.carabeef);
      add(registry, "azulfo_steak", SkiesItems.cooked_carabeef);
      add(registry, "raw_venison", SkiesItems.venison);
      add(registry, "venison_steak", SkiesItems.cooked_venison);
      add(registry, "aquite_gem", SkiesItems.aquite);
      add(registry, "cherry_pie", (ItemLike)SkiesBlocks.comet_berry_pie);
      add(registry, "cherry", SkiesItems.comet_berries);
      add(registry, "cherry_log", (ItemLike)SkiesBlocks.comet_log);
      add(registry, "cherry_wood", (ItemLike)SkiesBlocks.comet_wood);
      add(registry, "stripped_cherry_log", (ItemLike)SkiesBlocks.stripped_comet_log);
      add(registry, "stripped_cherry_wood", (ItemLike)SkiesBlocks.stripped_comet_wood);
      add(registry, "cherry_planks", (ItemLike)SkiesBlocks.comet_planks);
      add(registry, "cherry_stairs", (ItemLike)SkiesBlocks.comet_stairs);
      add(registry, "cherry_slab", (ItemLike)SkiesBlocks.comet_slab);
      add(registry, "cherry_fence", (ItemLike)SkiesBlocks.comet_fence);
      add(registry, "cherry_fence_gate", (ItemLike)SkiesBlocks.comet_fence_gate);
      add(registry, "cherry_door", SkiesItems.comet_door);
      add(registry, "cherry_trapdoor", (ItemLike)SkiesBlocks.comet_trapdoor);
      add(registry, "cherry_pressure_plate", (ItemLike)SkiesBlocks.comet_pressure_plate);
      add(registry, "cherry_button", (ItemLike)SkiesBlocks.comet_button);
      add(registry, "cherry_ladder", (ItemLike)SkiesBlocks.comet_ladder);
      add(registry, "cherry_bookshelf", (ItemLike)SkiesBlocks.comet_bookshelf);
      add(registry, "cherry_chest", (ItemLike)SkiesBlocks.comet_chest);
      add(registry, "cherry_sign", (ItemLike)SkiesBlocks.comet_sign);
      add(registry, "cherry_crafting_table", (ItemLike)SkiesBlocks.comet_crafting_table);
      add(registry, "cherry_stick", SkiesItems.comet_stick);
      add(registry, "cherry_axe", SkiesItems.comet_axe);
      add(registry, "cherry_pickaxe", SkiesItems.comet_pickaxe);
      add(registry, "cherry_shovel", SkiesItems.comet_shovel);
      add(registry, "cherry_hoe", SkiesItems.comet_hoe);
      add(registry, "cherry_sword", SkiesItems.comet_sword);
      add(registry, "cherry_spear", SkiesItems.comet_spear);
      add(registry, "cherry_leaves", SkiesBlocks.comet_leaves);
      add(registry, "cherry_grass", (ItemLike)SkiesBlocks.short_comet_grass);
      add(registry, "turquoise_cherry_grass_block", (ItemLike)SkiesBlocks.turquoise_comet_grass_block);
      add(registry, "lunar_cherry_grass_block", (ItemLike)SkiesBlocks.lunar_comet_grass_block);
      add(registry, "cherry_sapling", (ItemLike)SkiesBlocks.comet_sapling);
      add(registry, "turquoise_grass", (ItemLike)SkiesBlocks.short_turquoise_grass);
      add(registry, "lunar_grass", (ItemLike)SkiesBlocks.short_lunar_grass);
      add(registry, "comet_grass", (ItemLike)SkiesBlocks.short_comet_grass);
   }

   public static void addBlockEntityAliases(Registry<BlockEntityType<?>> registry) {
      add(registry, "cherry_chest", SkiesBlockEntityTypes.COMET_CHEST);
   }

   private static void add(Registry<?> registry, ResourceLocation oldName, ResourceLocation newName) {
      registry.addAlias(oldName, newName);
   }

   private static void add(Registry<?> registry, String oldName, ResourceLocation newName) {
      add(registry, BlueSkies.locate(oldName), newName);
   }

   private static void add(Registry<Block> registry, String oldName, Block newValue) {
      add(registry, oldName, registry.getKey(newValue));
   }

   private static void add(Registry<Item> registry, String oldName, ItemLike newValue) {
      add(registry, oldName, registry.getKey(newValue.asItem()));
   }

   private static void add(Registry<BlockEntityType<?>> registry, String oldName, BlockEntityType<?> newValue) {
      add(registry, oldName, registry.getKey(newValue));
   }
}
