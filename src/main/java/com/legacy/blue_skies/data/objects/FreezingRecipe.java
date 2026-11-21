package com.legacy.blue_skies.data.objects;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FreezingRecipe implements IToJson<FreezingRecipe> {
   public final Item inputItem;
   public final int fuelAmount;
   public final Item outputItem;
   public final int count;

   public FreezingRecipe(Item inputItem, int fuelAmount, Item outputItem, int outputCount) {
      this.inputItem = inputItem;
      this.fuelAmount = fuelAmount;
      this.outputItem = outputItem;
      this.count = outputCount;
   }

   public ItemStack getOutput() {
      return this.outputItem != Items.AIR ? new ItemStack(this.outputItem, this.count) : ItemStack.EMPTY;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("input", BuiltInRegistries.ITEM.getKey(this.inputItem).toString());
      json.addProperty("fuel_amount", this.fuelAmount);
      if (this.outputItem != Items.AIR) {
         json.addProperty("output", BuiltInRegistries.ITEM.getKey(this.outputItem).toString());
         json.addProperty("count", this.count);
      }

      return json;
   }

   public static String getDirectory() {
      return "snowcap_oven_freezing_recipes";
   }
}
