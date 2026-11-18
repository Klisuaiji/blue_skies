package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.crafting.ingredient.ToolHandleIngredient;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

@RegistrarHolder
public class SkiesIngredientTypes {
   public static final RegistrarHandler<IngredientType<?>> HANDLER = RegistrarHandler.getOrCreate(Keys.INGREDIENT_TYPES, "blue_skies");
   public static final Static<IngredientType<ToolHandleIngredient>> TOOL_HANDLES = HANDLER.createStatic(
      "tool_handles", () -> new IngredientType(ToolHandleIngredient.CODEC)
   );
}
