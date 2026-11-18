package com.legacy.blue_skies.data.providers;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.FreezingRecipe;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class SnowcapOvenFreezingProv extends SkiesDataProvider<FreezingRecipe> {
   public SnowcapOvenFreezingProv(DataGenerator generator) {
      super(generator, FreezingRecipe.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, FreezingRecipe> gatherData() {
      Map<ResourceLocation, FreezingRecipe> recipes = new HashMap<>();
      int lavaValue = 20000;
      this.register(recipes, Items.LAVA_BUCKET, lavaValue, Items.OBSIDIAN, 1);
      this.register(recipes, SkiesItems.ventium_lava_bucket, lavaValue, Items.OBSIDIAN, 1);
      int waterValue = 30;
      this.register(recipes, Items.WATER_BUCKET, waterValue, Items.ICE, 1);
      this.register(recipes, SkiesItems.ventium_water_bucket, waterValue, Items.ICE, 1);
      this.register(recipes, Items.BLAZE_ROD, 2400);
      this.register(recipes, Items.BLAZE_POWDER, 1200);
      this.register(recipes, Items.MAGMA_CREAM, 1000, Items.SLIME_BALL, 1);
      this.register(recipes, Items.FIRE_CHARGE, 600);
      int torchValue = 300;
      this.register(recipes, Items.TORCH, torchValue);
      this.register(recipes, Items.SOUL_TORCH, torchValue);
      this.register(recipes, Items.CAMPFIRE, torchValue * 3, Items.OAK_LOG, 3);
      this.register(recipes, Items.SOUL_CAMPFIRE, torchValue * 3, Items.OAK_LOG, 3);
      this.register(recipes, Items.LANTERN, torchValue, Items.IRON_NUGGET, 8);
      this.register(recipes, Items.SOUL_LANTERN, torchValue, Items.IRON_NUGGET, 8);
      this.register(recipes, Items.JACK_O_LANTERN, torchValue, Items.CARVED_PUMPKIN, 1);
      this.register(recipes, Items.MAGMA_BLOCK, 3000, Items.NETHERRACK, 1);
      this.register(recipes, SkiesBlocks.blaze_bud, 400);
      int snowcapValue = 1600;
      this.register(recipes, SkiesBlocks.snowcap_mushroom, snowcapValue);
      this.register(recipes, SkiesBlocks.snowcap_pinhead, snowcapValue / 4);
      this.register(recipes, SkiesBlocks.snowcap_mushroom_block, snowcapValue);
      this.register(recipes, SkiesBlocks.snowcap_mushroom_stem, snowcapValue + 400);
      int coalValue = 1600;
      this.register(recipes, SkiesItems.horizonite_ingot, coalValue, Items.IRON_INGOT, 1);
      this.register(recipes, SkiesItems.horizonite_nugget, coalValue / 10, Items.IRON_NUGGET, 1);
      this.register(recipes, SkiesBlocks.horizonite_block, coalValue * 10, Items.IRON_BLOCK, 1);
      this.register(recipes, SkiesItems.raw_horizonite, (int)(coalValue * 0.8), Items.RAW_IRON, 1);
      this.register(recipes, SkiesBlocks.raw_horizonite_block, (int)(coalValue * 0.8 * 10.0), Items.RAW_IRON_BLOCK, 1);
      this.register(recipes, SkiesItems.horizonite_pickaxe, coalValue * 3, Items.IRON_PICKAXE, 1);
      this.register(recipes, SkiesItems.horizonite_axe, coalValue * 3, Items.IRON_AXE, 1);
      this.register(recipes, SkiesItems.horizonite_shovel, coalValue, Items.IRON_SHOVEL, 1);
      this.register(recipes, SkiesItems.horizonite_hoe, coalValue * 2, Items.IRON_HOE, 1);
      this.register(recipes, SkiesItems.horizonite_sword, coalValue * 2, Items.IRON_SWORD, 1);
      this.register(recipes, SkiesItems.horizonite_helmet, coalValue * 5, Items.IRON_HELMET, 1);
      this.register(recipes, SkiesItems.horizonite_chestplate, coalValue * 8, Items.IRON_CHESTPLATE, 1);
      this.register(recipes, SkiesItems.horizonite_leggings, coalValue * 7, Items.IRON_LEGGINGS, 1);
      this.register(recipes, SkiesItems.horizonite_boots, coalValue * 4, Items.IRON_BOOTS, 1);
      int sunstoneValue = 1800;
      this.register(recipes, SkiesBlocks.sunstone_crystal, sunstoneValue);
      this.register(recipes, SkiesBlocks.sunstone_block, sunstoneValue * 10);
      return recipes;
   }

   private void register(Map<ResourceLocation, FreezingRecipe> recipes, String name, ItemLike inputItem, int fuelAmount, ItemLike outputItem, int outputCount) {
      recipes.put(BlueSkies.locate(name), new FreezingRecipe(inputItem.asItem(), fuelAmount, outputItem.asItem(), outputCount));
   }

   private void register(Map<ResourceLocation, FreezingRecipe> recipes, ItemLike inputItem, int fuelAmount, ItemLike outputItem, int outputCount) {
      String name = BuiltInRegistries.ITEM.getKey(inputItem.asItem()).getPath();
      if (outputItem != Items.AIR) {
         name = name + "_to_" + BuiltInRegistries.ITEM.getKey(outputItem.asItem()).getPath();
      }

      this.register(recipes, name, inputItem, fuelAmount, outputItem, outputCount);
   }

   private void register(Map<ResourceLocation, FreezingRecipe> recipes, ItemLike inputItem, int fuelAmount) {
      this.register(recipes, inputItem, fuelAmount, Items.AIR, 0);
   }
}
