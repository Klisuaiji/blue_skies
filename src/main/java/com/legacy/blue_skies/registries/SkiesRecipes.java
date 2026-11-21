package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.crafting.ToolRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesRecipes {
   public static final Lazy<RecipeSerializer<?>> TOOL_RECIPE = Lazy.of(() -> new ToolRecipe.Serializer());

   public static void init(RegisterEvent event) {
      event.register(Registries.RECIPE_SERIALIZER, BlueSkies.locate("tool_recipe"), TOOL_RECIPE);
   }
}
