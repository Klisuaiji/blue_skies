package com.legacy.blue_skies.data.managers;

import com.legacy.blue_skies.crafting.toolbox.ApplyFalsiteRecipe;
import com.legacy.blue_skies.crafting.toolbox.ModifySpearHandleRecipe;
import com.legacy.blue_skies.crafting.toolbox.ModifyToolHandleRecipe;
import com.legacy.blue_skies.crafting.toolbox.ToolboxRecipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;

public class ToolboxRecipeManager {
   public static final ToolboxRecipeManager INSTANCE = new ToolboxRecipeManager();
   private final List<ToolboxRecipe> recipes = new ArrayList<>();

   private ToolboxRecipeManager() {
      this.recipes.add(new ApplyFalsiteRecipe());
      this.recipes.add(new ModifyToolHandleRecipe());
      this.recipes.add(new ModifySpearHandleRecipe());
   }

   public Optional<ToolboxRecipe> getMatching(ItemStack inputItem, ItemStack modifierItem) {
      return this.recipes.stream().filter(r -> r.matches(inputItem, modifierItem)).findFirst();
   }

   public void addRecipe(ToolboxRecipe recipe) {
      this.recipes.add(recipe);
   }

   public List<ToolboxRecipe> getRecipes() {
      return this.recipes;
   }
}
