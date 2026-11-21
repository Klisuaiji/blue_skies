package com.legacy.blue_skies.data.objects.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.util.ListSet;
import com.mojang.datafixers.Products.P2;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class TransmuteRecipe extends AlchemyRecipe {
   private static final Codec<TransmuteRecipe> CODEC = RecordCodecBuilder.create(
      instance -> {
         P2<Mu<TransmuteRecipe>, Boolean, ResourceLocation> parent = parentCodec(instance);
         return instance.group(
               parent.t1(),
               parent.t2(),
               ResourceLocation.CODEC.listOf().optionalFieldOf("items", Collections.emptyList()).forGetter(r -> r.items),
               TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("tags", Collections.emptyList()).forGetter(r -> r.itemTags),
               ResourceLocation.CODEC.listOf().optionalFieldOf("blacklist", Collections.emptyList()).forGetter(r -> r.resultBlacklist)
            )
            .apply(instance, TransmuteRecipe::new);
      }
   );
   private final List<ResourceLocation> items;
   private final List<TagKey<Item>> itemTags;
   private final List<ResourceLocation> resultBlacklist;
   @Nullable
   protected ListSet<Item> cachedResults = null;

   private TransmuteRecipe(
      boolean shouldOverride, ResourceLocation type, List<ResourceLocation> items, List<TagKey<Item>> itemTags, List<ResourceLocation> resultBlacklist
   ) {
      super(shouldOverride, type);
      this.items = items;
      this.itemTags = itemTags;
      this.resultBlacklist = resultBlacklist;
   }

   private TransmuteRecipe(TransmuteRecipe.Builder builder) {
      this(builder.shouldOverride, BlueSkies.locate("transmute"), builder.items, builder.itemTags, builder.resultBlacklist);
   }

   public static TransmuteRecipe.Builder builder(String name) {
      return new TransmuteRecipe.Builder(name);
   }

   @Override
   public void mergeRecipe(AlchemyRecipe recipe) {
      if (recipe instanceof TransmuteRecipe transmute) {
         this.items.addAll(transmute.items);
         this.itemTags.addAll(transmute.itemTags);
         this.resultBlacklist.addAll(transmute.resultBlacklist);
      }
   }

   @Override
   public List<Item> getResults(Item inputA, Item inputB) {
      List<Item> results = this.getResults();
      results.remove(inputA);
      results.remove(inputB);
      return results;
   }

   @Override
   public List<Item> getResults() {
      if (this.cachedResults == null) {
         this.cachedResults = computeValues(this.items, this.itemTags);
         this.cachedResults.removeAll(computeValues(this.resultBlacklist, List.of()));
      }

      return this.cachedResults.clone();
   }

   @Override
   public boolean matches(Item itemA, Item itemB) {
      return (itemB == Items.AIR || itemB == null) && this.getResults().contains(itemA);
   }

   public TransmuteRecipe fromJson(JsonObject json) {
      return (TransmuteRecipe)((Pair)CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(JsonParseException::new)).getFirst();
   }

   @Override
   public JsonObject toJson() {
      return ((JsonElement)CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(IllegalStateException::new)).getAsJsonObject();
   }

   public static class Builder {
      private final String name;
      private boolean shouldOverride = false;
      private List<ResourceLocation> items = new ArrayList<>();
      private List<TagKey<Item>> itemTags = new ArrayList<>();
      private List<ResourceLocation> resultBlacklist = new ArrayList<>();

      private Builder(String name) {
         this.name = name;
      }

      public TransmuteRecipe.Builder shouldOverride() {
         this.shouldOverride = true;
         return this;
      }

      public TransmuteRecipe.Builder items(ItemLike... items) {
         for (ItemLike item : items) {
            this.items.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
         }

         return this;
      }

      public TransmuteRecipe.Builder items(List<ItemLike> items) {
         return this.items(items.toArray(ItemLike[]::new));
      }

      public TransmuteRecipe.Builder withTag(TagKey<Item> itemTag) {
         this.itemTags.add(itemTag);
         return this;
      }

      public TransmuteRecipe.Builder resultBlacklist(ItemLike... items) {
         for (ItemLike item : items) {
            this.resultBlacklist.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
         }

         return this;
      }

      public TransmuteRecipe.Builder resultBlacklist(String modID, String... itemNames) {
         for (String itemName : itemNames) {
            this.resultBlacklist.add(ResourceLocation.fromNamespaceAndPath(modID, itemName));
         }

         return this;
      }

      public String getName() {
         return this.name;
      }

      public TransmuteRecipe build() {
         if (this.items.isEmpty() && this.itemTags.isEmpty()) {
            throw new IllegalStateException("Transmute recipe with name " + this.name + " had no items");
         } else {
            return new TransmuteRecipe(this);
         }
      }
   }
}
