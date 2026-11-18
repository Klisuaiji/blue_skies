package com.legacy.blue_skies.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({RecipeManager.class})
public class RecipeManagerMixin {
   @ModifyReceiver(
      at = {@At(
         value = "INVOKE",
         target = "java/util/stream/Stream.findFirst()Ljava/util/Optional;"
      )},
      method = {"getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"}
   )
   private <C extends Container, T extends Recipe<C>> Stream<RecipeHolder<T>> modifyFoundRecipes(Stream<RecipeHolder<T>> recipeStream) {
      List<RecipeHolder<T>> recipes = recipeStream.toList();
      if (recipes.size() > 1) {
         List<RecipeHolder<T>> blueSkiesRecipes = recipes.stream().filter(r -> r.id().getNamespace().equals("blue_skies")).toList();
         if (!blueSkiesRecipes.isEmpty()) {
            return blueSkiesRecipes.stream();
         }
      }

      return recipes.stream();
   }
}
