package com.legacy.blue_skies.data.managers;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.HorizoniteForgeFuel;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;

public class HorizoniteForgeFuelManager extends SkiesSyncedDataManager<Item, HorizoniteForgeFuel, HorizoniteForgeFuelManager> {
   public HorizoniteForgeFuelManager() {
      super(HorizoniteForgeFuel.getDirectory());
   }

   @Override
   protected Pair<Item, HorizoniteForgeFuel> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      Item item = parseItem(json, "item");
      float fuelAmount = GsonHelper.getAsFloat(json, "fuel_amount");
      return Pair.of(item, new HorizoniteForgeFuel(item, fuelAmount));
   }

   public HorizoniteForgeFuelManager newInstance() {
      return new HorizoniteForgeFuelManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(Item key, HorizoniteForgeFuel value) {
      CompoundTag keyNbt = new CompoundTag();
      keyNbt.putString("0", BuiltInRegistries.ITEM.getKey(key).toString());
      CompoundTag valueNbt = new CompoundTag();
      this.putItem(valueNbt, "0", value.item);
      valueNbt.putFloat("1", value.fuelAmount);
      return Pair.of(keyNbt, valueNbt);
   }

   @Override
   public Pair<Item, HorizoniteForgeFuel> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      Item key = this.getItem(keyNbt, "0");
      Item item = this.getItem(valueNbt, "0");
      float fuelAmount = valueNbt.getFloat("1");
      return Pair.of(key, new HorizoniteForgeFuel(item, fuelAmount));
   }
}
