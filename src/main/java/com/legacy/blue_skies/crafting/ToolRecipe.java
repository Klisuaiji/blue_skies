package com.legacy.blue_skies.crafting;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.crafting.ingredient.ToolHandleIngredient;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.crafting.IShapedRecipe;

public class ToolRecipe implements CraftingRecipe, IShapedRecipe<CraftingContainer> {
   final ShapedRecipePattern pattern;
   final ItemStack result;

   public ToolRecipe(ShapedRecipePattern pattern, ItemStack result) {
      this.pattern = pattern;
      this.result = result;
   }

   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)SkiesRecipes.TOOL_RECIPE.get();
   }

   public ItemStack getResultItem(RegistryAccess registry) {
      return this.result;
   }

   public NonNullList<Ingredient> getIngredients() {
      return this.pattern.ingredients();
   }

   public boolean canCraftInDimensions(int pWidth, int pHeight) {
      return pWidth >= this.pattern.width() && pHeight >= this.pattern.height();
   }

   public int getRecipeWidth() {
      return this.pattern.width();
   }

   public int getRecipeHeight() {
      return this.pattern.height();
   }

   public CraftingBookCategory category() {
      return CraftingBookCategory.EQUIPMENT;
   }

   public boolean isIncomplete() {
      NonNullList<Ingredient> ingredients = this.getIngredients();
      return ingredients.isEmpty() || ingredients.stream().filter(ing -> !ing.isEmpty()).anyMatch(CommonHooks::hasNoElements);
   }

   public boolean matches(CraftingContainer inv, Level world) {
      return this.pattern.matches(inv) && this.getStick(inv).isPresent();
   }

   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      Optional<Item> stick = this.getStick(inv);
      return stick.isPresent() ? ToolUtils.setStickType(this.getResultItem(registryAccess).copy(), stick.get()) : ItemStack.EMPTY;
   }

   private Optional<Item> getStick(CraftingContainer inv) {
      Item stick = null;

      for (ItemStack stack : inv.getItems()) {
         if (BlueSkiesData.TOOL_HANDLE_TYPES.hasHandle(stack.getItem())) {
            if (stick == null) {
               stick = stack.getItem();
            } else if (!stack.is(stick)) {
               return Optional.empty();
            }
         }
      }

      return Optional.ofNullable(stick);
   }

   public enum Material implements StringRepresentable {
      BLUEBRIGHT("bluebright", SkiesBlocks.bluebright_planks),
      STARLIT("starlit", SkiesBlocks.starlit_planks),
      FROSTBRIGHT("frostbright", SkiesBlocks.frostbright_planks),
      LUNAR("lunar", SkiesBlocks.lunar_planks),
      DUSK("dusk", SkiesBlocks.dusk_planks),
      MAPLE("maple", SkiesBlocks.maple_planks),
      COMET("comet", SkiesBlocks.comet_planks),
      TURQUOISE_STONE("turquoise_stone", SkiesBlocks.turquoise_cobblestone),
      LUNAR_STONE("lunar_stone", SkiesBlocks.lunar_cobblestone),
      PYROPE("pyrope", SkiesItems.pyrope_gem),
      AQUITE("aquite", SkiesItems.aquite),
      DIOPSIDE("diopside", SkiesItems.diopside_gem),
      CHAROITE("charoite", SkiesItems.charoite),
      HORIZONITE("horizonite", SkiesItems.horizonite_ingot);

      private final String name;
      private final Ingredient ingredient;

      Material(String name, ItemLike item) {
         this.name = name;
         this.ingredient = Ingredient.of(new ItemLike[]{item});
      }

      public Ingredient getIngredient() {
         return this.ingredient;
      }

      public ItemLike getItem() {
         return this.ingredient.getItems()[0].getItem();
      }

      public String getSerializedName() {
         return this.name;
      }
   }

   public static class OverwrittenCraftingContainer {
   }

   public enum RecipeShape implements StringRepresentable {
      AXE("axe", 2, 3, "mm", "ms", " s"),
      PICKAXE("pickaxe", 3, 3, "mmm", " s ", " s "),
      SHOVEL("shovel", 1, 3, "m", "s", "s"),
      HOE("hoe", 2, 3, "mm", " s", " s"),
      SWORD("sword", 1, 3, "m", "m", "s");

      private final String name;
      private final int width;
      private final int height;
      private final String[] rows;

      RecipeShape(String name, int width, int height, String... rows) {
         this.name = name;
         this.width = width;
         this.height = height;
         this.rows = rows;
      }

      public NonNullList<Ingredient> create(Ingredient material) {
         Ingredient[] ingredients = new Ingredient[this.width * this.height];
         Set<Item> sticks = (Set<Item>)(!BlueSkiesData.TOOL_HANDLE_TYPES.getData().isEmpty()
            ? BlueSkiesData.TOOL_HANDLE_TYPES.getData().keySet()
            : ImmutableSet.of(Items.STICK));
         Ingredient stickIngredient = Ingredient.of((ItemLike[])sticks.toArray(Item[]::new));

         for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
               char c = this.rows[row].charAt(col);

               Ingredient ingredient = switch (c) {
                  case 'm' -> material;
                  case 's' -> stickIngredient;
                  default -> Ingredient.EMPTY;
               };
               ingredients[row * this.width + col] = ingredient;
            }
         }

         return NonNullList.of(Ingredient.EMPTY, ingredients);
      }

      public ShapedRecipePattern getPattern(Ingredient material) {
         Map<Character, Ingredient> ingredients = Map.of('m', material, 's', ToolHandleIngredient.INSTANCE);
         List<String> pattern = List.of(this.rows);
         return ShapedRecipePattern.of(ingredients, pattern);
      }

      public String getSerializedName() {
         return this.name;
      }
   }

   public static class Serializer implements RecipeSerializer<ToolRecipe> {
      public static final Codec<ToolRecipe> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               ShapedRecipePattern.MAP_CODEC.fieldOf("pattern").forGetter(r -> r.pattern), ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
            )
            .apply(instance, ToolRecipe::new)
      );

      public Codec<ToolRecipe> codec() {
         return CODEC;
      }

      public ToolRecipe fromNetwork(FriendlyByteBuf buffer) {
         ShapedRecipePattern pattern = ShapedRecipePattern.fromNetwork(buffer);
         ItemStack result = buffer.readItem();
         return new ToolRecipe(pattern, result);
      }

      public void toNetwork(FriendlyByteBuf buffer, ToolRecipe recipe) {
         recipe.pattern.toNetwork(buffer);
         buffer.writeItem(recipe.result);
      }
   }
}
