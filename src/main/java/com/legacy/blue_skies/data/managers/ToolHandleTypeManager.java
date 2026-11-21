package com.legacy.blue_skies.data.managers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.ToolHandleType;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

public class ToolHandleTypeManager extends SkiesSyncedDataManager<Item, ToolHandleType, ToolHandleTypeManager> {
   public boolean loaded = false;

   public ToolHandleTypeManager() {
      super(ToolHandleType.getDirectory());
   }

   @Override
   protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
      super.apply(jsonMap, resourceManager, profiler);
      this.loaded = true;
   }

   @Override
   protected Pair<Item, ToolHandleType> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      Item stick = parseItem(json, "stick");
      float attackSpeed = GsonHelper.getAsFloat(json, "attack_speed", ToolHandleType.DEFAULT.getAttackSpeedModifier());
      float projectileDamage = GsonHelper.getAsFloat(json, "projectile_damage", ToolHandleType.DEFAULT.getProjectileDamageModifier());
      float durability = GsonHelper.getAsFloat(json, "durability", ToolHandleType.DEFAULT.getDurabilityModifier());
      int color = GsonHelper.getAsInt(json, "color", ToolHandleType.DEFAULT.getColor());
      return Pair.of(stick, new ToolHandleType(durability, attackSpeed, projectileDamage, stick, color));
   }

   public ToolHandleType getHandle(ResourceLocation itemName) {
      return BuiltInRegistries.ITEM.containsKey(itemName) ? this.getHandle((Item)BuiltInRegistries.ITEM.get(itemName)) : ToolHandleType.DEFAULT;
   }

   public ToolHandleType getHandle(Item item) {
      return this.hasHandle(item) ? this.getValue(item) : ToolHandleType.DEFAULT;
   }

   public boolean hasHandle(Item item) {
      return this.getData().containsKey(item);
   }

   public boolean hasHandle(ResourceLocation itemName) {
      return BuiltInRegistries.ITEM.containsKey(itemName) ? this.hasHandle((Item)BuiltInRegistries.ITEM.get(itemName)) : false;
   }

   public ToolHandleTypeManager newInstance() {
      return new ToolHandleTypeManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(Item key, ToolHandleType value) {
      CompoundTag keyNbt = new CompoundTag();
      this.putItem(keyNbt, "0", key);
      CompoundTag valueNbt = new CompoundTag();
      this.putItem(valueNbt, "0", value.getStick());
      valueNbt.putFloat("1", value.getAttackSpeedModifier());
      valueNbt.putFloat("2", value.getDurabilityModifier());
      valueNbt.putFloat("3", value.getProjectileDamageModifier());
      valueNbt.putInt("4", value.getColor());
      return Pair.of(keyNbt, valueNbt);
   }

   @Override
   public Pair<Item, ToolHandleType> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      Item key = this.getItem(keyNbt, "0");
      Item stick = this.getItem(valueNbt, "0");
      float attackSpeed = valueNbt.getFloat("1");
      float durability = valueNbt.getFloat("2");
      float projectileDamage = valueNbt.getFloat("3");
      int color = valueNbt.getInt("4");
      return Pair.of(key, new ToolHandleType(durability, attackSpeed, projectileDamage, stick, color));
   }
}
