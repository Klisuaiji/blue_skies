package com.legacy.blue_skies.data.managers;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.FreezingRecipe;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SnowcapOvenFreezingManager extends SkiesSyncedDataManager<Item, FreezingRecipe, SnowcapOvenFreezingManager> {
   public SnowcapOvenFreezingManager() {
      super(FreezingRecipe.getDirectory());
   }

   @Override
   public Pair<Item, FreezingRecipe> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      Item inputItem = parseItem(json, "input");
      Item outputItem = parseItem(json, "output", Items.AIR);
      int count = GsonHelper.getAsInt(json, "count", 1);
      int fuelAmount = GsonHelper.getAsInt(json, "fuel_amount", 1600);
      return Pair.of(inputItem, new FreezingRecipe(inputItem, fuelAmount, outputItem, count));
   }

   public SnowcapOvenFreezingManager newInstance() {
      return new SnowcapOvenFreezingManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(Item key, FreezingRecipe value) {
      CompoundTag keyNbt = new CompoundTag();
      keyNbt.putString("0", BuiltInRegistries.ITEM.getKey(key).toString());
      CompoundTag valueNbt = new CompoundTag();
      this.putItem(valueNbt, "0", value.inputItem);
      valueNbt.putInt("1", value.fuelAmount);
      this.putItem(valueNbt, "2", value.outputItem);
      valueNbt.putInt("3", value.count);
      return Pair.of(keyNbt, valueNbt);
   }

   @Override
   public Pair<Item, FreezingRecipe> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      Item key = this.getItem(keyNbt, "0");
      Item inputItem = this.getItem(valueNbt, "0");
      int fuelAmount = valueNbt.getInt("1");
      Item outputItem = this.getItem(valueNbt, "2");
      int outputCount = valueNbt.getInt("3");
      return Pair.of(key, new FreezingRecipe(inputItem, fuelAmount, outputItem, outputCount));
   }
}
