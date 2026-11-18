package com.legacy.blue_skies.crafting.ingredient;

import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.registries.SkiesIngredientTypes;
import com.mojang.serialization.Codec;
import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Ingredient.Value;

public class ToolHandleIngredient extends Ingredient {
   public static final ToolHandleIngredient INSTANCE = new ToolHandleIngredient();
   public static final Codec<ToolHandleIngredient> CODEC = Codec.unit(() -> INSTANCE);

   private ToolHandleIngredient() {
      super(Stream.of(ToolHandleIngredient.ToolHandleValue.INSTANCE), SkiesIngredientTypes.TOOL_HANDLES);
   }

   private static class ToolHandleValue implements Value {
      private static final ToolHandleIngredient.ToolHandleValue INSTANCE = new ToolHandleIngredient.ToolHandleValue();

      public Collection<ItemStack> getItems() {
         return BlueSkiesData.TOOL_HANDLE_TYPES.getData().keySet().stream().<ItemStack>map(ItemStack::new).toList();
      }
   }
}
