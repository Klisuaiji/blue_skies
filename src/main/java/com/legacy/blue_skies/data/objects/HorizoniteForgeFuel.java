package com.legacy.blue_skies.data.objects;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class HorizoniteForgeFuel implements IToJson<HorizoniteForgeFuel> {
   public final Item item;
   public final float fuelAmount;

   public HorizoniteForgeFuel(Item item, float fuelAmount) {
      this.item = item;
      this.fuelAmount = fuelAmount;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item).toString());
      json.addProperty("fuel_amount", this.fuelAmount);
      return json;
   }

   public static String getDirectory() {
      return "horizonite_forge_fuels";
   }
}
