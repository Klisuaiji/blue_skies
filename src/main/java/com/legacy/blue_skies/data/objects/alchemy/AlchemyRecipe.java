package com.legacy.blue_skies.data.objects.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.legacy.blue_skies.data.objects.IToJson;
import com.legacy.blue_skies.util.ListSet;
import com.mojang.datafixers.Products.P2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public abstract class AlchemyRecipe implements IToJson<AlchemyRecipe> {
   public final boolean shouldOverride;
   public final ResourceLocation type;

   public AlchemyRecipe(boolean shouldOverride, ResourceLocation type) {
      this.shouldOverride = shouldOverride;
      this.type = type;
   }

   protected static <R extends AlchemyRecipe> P2<Mu<R>, Boolean, ResourceLocation> parentCodec(Instance<R> instance) {
      return instance.group(
         Codec.BOOL.optionalFieldOf("override", false).forGetter(r -> r.shouldOverride), ResourceLocation.CODEC.fieldOf("type").forGetter(r -> r.type)
      );
   }

   public static ListSet<Item> computeValues(List<ResourceLocation> names, List<TagKey<Item>> tags) {
      return Stream.concat(
            names.stream().map(BuiltInRegistries.ITEM::get).filter(Objects::nonNull),
            tags.stream()
               .<Optional>map(BuiltInRegistries.ITEM::getTag)
               .filter(Optional::isPresent)
               .map(Optional::get)
               .flatMap(HolderSet::stream)
               .map(Holder::value)
         )
         .distinct()
         .collect(ListSet.toListSet());
   }

   public static ListSet<Item> computeValues(List<ResourceLocation> names, List<TagKey<Item>> tags, Optional<Item> extra) {
      ListSet<Item> computed = computeValues(names, tags);
      return extra.isPresent()
         ? Stream.concat(Stream.of(extra).filter(Optional::isPresent).map(Optional::get), computed.stream()).distinct().collect(ListSet.toListSet())
         : computed;
   }

   public abstract void mergeRecipe(AlchemyRecipe var1);

   public abstract List<Item> getResults(Item var1, Item var2);

   public abstract List<Item> getResults();

   public abstract boolean matches(Item var1, Item var2);

   public abstract AlchemyRecipe fromJson(JsonObject var1) throws SkiesDataManager.RegistryObjectNotFoundException;

   protected List<Item> toItemList(List<ResourceLocation> resourceLocationSet) {
      return resourceLocationSet.stream().filter(BuiltInRegistries.ITEM::containsKey).<Item>map(BuiltInRegistries.ITEM::get).collect(Collectors.toList());
   }

   @Nullable
   public AlchemyRecipe fromNbt(CompoundTag nbt) {
      try {
         return this.fromJson(((JsonElement)NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, nbt)).getAsJsonObject());
      } catch (Exception var3) {
         SkiesDataManager.LOGGER.error("Failed to read an alchemy recipe from nbt {}");
         return null;
      }
   }

   public CompoundTag toNbt(AlchemyRecipe recipe) {
      if (this.getClass().isInstance(recipe)) {
         return (CompoundTag)JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, this.toJson());
      } else {
         throw new IllegalArgumentException("The recipe passed was not an instanceof " + this.getClass().toString());
      }
   }

   public static String getDirectory() {
      return "alchemy_recipes";
   }

   public static class Types {
      private static final Map<ResourceLocation, AlchemyRecipe> REGISTRY = new HashMap<>();
      private static final String TYPE_KEY = "type";

      public static void register(AlchemyRecipe recipeDefault) {
         REGISTRY.put(recipeDefault.type, recipeDefault);
      }

      public static AlchemyRecipe parse(JsonObject json) {
         ResourceLocation name = new ResourceLocation(GsonHelper.getAsString(json, "type"));
         return get(name);
      }

      public static AlchemyRecipe parse(CompoundTag nbt) {
         ResourceLocation name = new ResourceLocation(nbt.getString("type"));
         return get(name);
      }

      public static AlchemyRecipe get(ResourceLocation name) {
         if (REGISTRY.containsKey(name)) {
            return REGISTRY.get(name);
         } else {
            IllegalArgumentException e = new IllegalArgumentException(name.toString() + " is not a registered recipe type!");
            BlueSkies.LOGGER.fatal(e);
            throw e;
         }
      }

      static {
         register(TransmuteRecipe.builder("default").items(Items.AIR).build());
         register(CatylistRecipe.builder("default").input(Items.AIR).catylist(Items.AIR).results(Items.AIR).build());
      }
   }
}
