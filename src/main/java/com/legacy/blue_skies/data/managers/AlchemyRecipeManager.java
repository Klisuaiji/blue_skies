package com.legacy.blue_skies.data.managers;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.alchemy.AlchemyRecipe;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class AlchemyRecipeManager extends SkiesSyncedDataManager<ResourceLocation, AlchemyRecipe, AlchemyRecipeManager> {
   public AlchemyRecipeManager() {
      super(AlchemyRecipe.getDirectory());
      this.hasOverrides = true;
   }

   public List<AlchemyRecipe> getMatching(Item itemA, Item itemB) {
      return this.getData().values().stream().filter(recipe -> recipe.matches(itemA, itemB) || recipe.matches(itemB, itemA)).collect(Collectors.toList());
   }

   protected void registerData(Map<ResourceLocation, AlchemyRecipe> activeData, ResourceLocation key, AlchemyRecipe value) {
      AlchemyRecipe oldValue = activeData.get(key);
      if (oldValue != null && !value.shouldOverride) {
         oldValue.mergeRecipe(value);
      } else {
         activeData.put(key, value);
      }
   }

   public AlchemyRecipeManager newInstance() {
      return new AlchemyRecipeManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(ResourceLocation key, AlchemyRecipe value) {
      CompoundTag keyNbt = new CompoundTag();
      keyNbt.putString("name", key.toString());
      CompoundTag valueNbt = value.toNbt(value);
      return Pair.of(keyNbt, valueNbt);
   }

   @Nullable
   @Override
   public Pair<ResourceLocation, AlchemyRecipe> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      AlchemyRecipe methodHolder = AlchemyRecipe.Types.parse(valueNbt);
      AlchemyRecipe recipe = methodHolder.fromNbt(valueNbt);
      return recipe != null ? Pair.of(ResourceLocation.parse(keyNbt.getString("name")), recipe) : null;
   }

   @Override
   protected Pair<ResourceLocation, AlchemyRecipe> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      AlchemyRecipe methodHolder = AlchemyRecipe.Types.parse(json);
      AlchemyRecipe recipe = methodHolder.fromJson(json);
      return Pair.of(name, recipe);
   }
}
