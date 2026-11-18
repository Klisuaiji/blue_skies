package com.legacy.blue_skies.crafting;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ToolRecipeBuilder implements RecipeBuilder {
   final ToolRecipe.Material material;
   final ToolRecipe.RecipeShape shape;
   final ItemStack result;
   @Nullable
   private String group = null;
   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

   ToolRecipeBuilder(ToolRecipe.Material material, ToolRecipe.RecipeShape shape, ItemStack result) {
      this.material = material;
      this.shape = shape;
      this.result = result;
   }

   public static ToolRecipeBuilder of(ToolRecipe.Material material, ToolRecipe.RecipeShape shape, ItemStack result) {
      return new ToolRecipeBuilder(material, shape, result);
   }

   public RecipeBuilder unlockedBy(String key, Criterion<?> criterion) {
      this.criteria.put(key, criterion);
      return this;
   }

   public RecipeBuilder group(String group) {
      this.group = group;
      return this;
   }

   public Item getResult() {
      return this.result.getItem();
   }

   public void save(RecipeOutput output, ResourceLocation recipeId) {
      output.accept(recipeId, new ToolRecipe(this.shape.getPattern(this.material.getIngredient()), this.result), this.makeAdvancement(recipeId));
   }

   private AdvancementHolder makeAdvancement(ResourceLocation recipeId) {
      Builder advancement = Builder.advancement()
         .addCriterion(
            "has_resource",
            TriggerInstance.hasItems(
               new ItemPredicate[]{net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(new ItemLike[]{this.material.getItem()}).build()}
            )
         )
         .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId));

      for (Entry<String, Criterion<?>> entry : this.criteria.entrySet()) {
         advancement.addCriterion(entry.getKey(), entry.getValue());
      }

      ResourceLocation advancementID = recipeId.withPrefix("recipes/blue_skies_tools/");
      return advancement.build(advancementID);
   }
}
