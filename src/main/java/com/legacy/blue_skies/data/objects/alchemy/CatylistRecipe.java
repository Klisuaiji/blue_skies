package com.legacy.blue_skies.data.objects.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.legacy.blue_skies.util.ListSet;
import com.mojang.datafixers.Products.P2;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class CatylistRecipe extends AlchemyRecipe {
   private static final Codec<CatylistRecipe> CODEC = RecordCodecBuilder.create(
      instance -> {
         P2<Mu<CatylistRecipe>, Boolean, ResourceLocation> parent = parentCodec(instance);
         return instance.group(
               parent.t1(),
               parent.t2(),
               BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("input").forGetter(r -> r.input),
               ResourceLocation.CODEC.listOf().optionalFieldOf("inputs", List.of()).forGetter(r -> r.inputs),
               TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("input_tags", List.of()).forGetter(r -> r.inputTags),
               BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("catylist").forGetter(r -> r.catylist),
               ResourceLocation.CODEC.listOf().optionalFieldOf("catylists", List.of()).forGetter(r -> r.catylists),
               TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("catylist_tags", List.of()).forGetter(r -> r.catylistTags),
               ResourceLocation.CODEC.listOf().optionalFieldOf("results", List.of()).forGetter(r -> r.results),
               TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("results_tags", List.of()).forGetter(r -> r.resultsTags)
            )
            .apply(instance, CatylistRecipe::new);
      }
   );
   @Deprecated
   private final Optional<Item> input;
   private final List<ResourceLocation> inputs;
   private final List<TagKey<Item>> inputTags;
   @Deprecated
   private final Optional<Item> catylist;
   private final List<ResourceLocation> catylists;
   private final List<TagKey<Item>> catylistTags;
   private final List<ResourceLocation> results;
   private final List<TagKey<Item>> resultsTags;
   @Nullable
   private ListSet<Item> cachedInput = null;
   @Nullable
   private ListSet<Item> cachedCatylist = null;
   @Nullable
   private ListSet<Item> cachedResults = null;

   private CatylistRecipe(
      boolean shouldOverride,
      ResourceLocation type,
      Optional<Item> input,
      List<ResourceLocation> inputs,
      List<TagKey<Item>> inputTags,
      Optional<Item> catylist,
      List<ResourceLocation> catylists,
      List<TagKey<Item>> catylistTags,
      List<ResourceLocation> results,
      List<TagKey<Item>> resultsTags
   ) {
      super(shouldOverride, type);
      this.input = input;
      this.inputs = new ArrayList<>(inputs);
      this.inputTags = new ArrayList<>(inputTags);
      this.catylist = catylist;
      this.catylists = new ArrayList<>(catylists);
      this.catylistTags = new ArrayList<>(catylistTags);
      this.results = new ArrayList<>(results);
      this.resultsTags = new ArrayList<>(resultsTags);
   }

   private CatylistRecipe(CatylistRecipe.Builder builder) {
      this(
         builder.shouldOverride,
         BlueSkies.locate("catylist"),
         Optional.empty(),
         builder.inputs,
         builder.inputTags,
         Optional.empty(),
         builder.catylists,
         builder.catylistTags,
         builder.results,
         builder.resultsTags
      );
   }

   public static CatylistRecipe.Builder builder(String name) {
      return new CatylistRecipe.Builder(name);
   }

   public List<Item> getInput() {
      if (this.cachedInput == null) {
         this.cachedInput = computeValues(this.inputs, this.inputTags, this.input);
      }

      return this.cachedInput;
   }

   public List<Item> getCatylist() {
      if (this.cachedCatylist == null) {
         this.cachedCatylist = computeValues(this.catylists, this.catylistTags, this.catylist);
      }

      return this.cachedCatylist;
   }

   @Override
   public List<Item> getResults(Item inputA, Item inputB) {
      return this.getResults();
   }

   @Override
   public List<Item> getResults() {
      if (this.cachedResults == null) {
         this.cachedResults = computeValues(this.results, this.resultsTags);
      }

      return this.cachedResults.clone();
   }

   @Override
   public void mergeRecipe(AlchemyRecipe recipe) {
      if (recipe instanceof CatylistRecipe other) {
         this.inputs.addAll(other.inputs);
         this.inputTags.addAll(other.inputTags);
         this.catylists.addAll(other.catylists);
         this.catylistTags.addAll(other.catylistTags);
         this.results.addAll(other.results);
         this.resultsTags.addAll(other.resultsTags);
      }
   }

   @Override
   public boolean matches(Item itemA, Item itemB) {
      return this.getInput().contains(itemA) && this.getCatylist().contains(itemB);
   }

   public CatylistRecipe fromJson(JsonObject json) throws SkiesDataManager.RegistryObjectNotFoundException {
      return (CatylistRecipe)((Pair)CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, JsonParseException::new)).getFirst();
   }

   @Override
   public JsonObject toJson() {
      return ((JsonElement)CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, IllegalStateException::new)).getAsJsonObject();
   }

   public static final class Builder {
      private final String name;
      private boolean shouldOverride = false;
      private final List<ResourceLocation> inputs = new ArrayList<>();
      private final List<TagKey<Item>> inputTags = new ArrayList<>();
      private final List<ResourceLocation> catylists = new ArrayList<>();
      private final List<TagKey<Item>> catylistTags = new ArrayList<>();
      private final List<ResourceLocation> results = new ArrayList<>();
      private final List<TagKey<Item>> resultsTags = new ArrayList<>();

      private Builder(String name) {
         this.name = name;
      }

      public CatylistRecipe.Builder shouldOverride() {
         this.shouldOverride = true;
         return this;
      }

      public CatylistRecipe.Builder input(ItemLike... items) {
         for (ItemLike item : items) {
            this.inputs.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
         }

         return this;
      }

      public CatylistRecipe.Builder input(List<ItemLike> items) {
         return this.input(items.toArray(ItemLike[]::new));
      }

      public CatylistRecipe.Builder input(TagKey<Item> itemTag) {
         this.inputTags.add(itemTag);
         return this;
      }

      public CatylistRecipe.Builder catylist(ItemLike... items) {
         for (ItemLike item : items) {
            this.catylists.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
         }

         return this;
      }

      public CatylistRecipe.Builder catylist(List<ItemLike> items) {
         return this.catylist(items.toArray(ItemLike[]::new));
      }

      public CatylistRecipe.Builder catylist(TagKey<Item> itemTag) {
         this.catylistTags.add(itemTag);
         return this;
      }

      public CatylistRecipe.Builder results(ItemLike... items) {
         for (ItemLike item : items) {
            this.results.add(BuiltInRegistries.ITEM.getKey(item.asItem()));
         }

         return this;
      }

      public CatylistRecipe.Builder results(List<ItemLike> items) {
         return this.results(items.toArray(ItemLike[]::new));
      }

      public CatylistRecipe.Builder results(TagKey<Item> itemTag) {
         this.resultsTags.add(itemTag);
         return this;
      }

      public String getName() {
         return this.name;
      }

      public CatylistRecipe build() {
         if (this.inputs.isEmpty() && this.inputTags.isEmpty()) {
            throw new IllegalStateException("Catylist recipe with name " + this.name + " had no inputs");
         } else if (this.catylists.isEmpty() && this.catylistTags.isEmpty()) {
            throw new IllegalStateException("Catylist recipe with name " + this.name + " had no catylists");
         } else if (this.results.isEmpty() && this.resultsTags.isEmpty()) {
            throw new IllegalStateException("Catylist recipe with name " + this.name + " had no results");
         } else {
            return new CatylistRecipe(this);
         }
      }
   }
}
