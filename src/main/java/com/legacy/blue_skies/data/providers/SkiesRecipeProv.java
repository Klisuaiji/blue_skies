package com.legacy.blue_skies.data.providers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.crafting.ToolRecipe;
import com.legacy.blue_skies.crafting.ToolRecipeBuilder;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.structure_gel.api.util.GelCollectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.AbstractCookingRecipe.Factory;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import org.apache.commons.lang3.tuple.Pair;

public class SkiesRecipeProv extends RecipeProvider {
   private RecipeOutput con;
   private String hasItem = "has_item";

   public SkiesRecipeProv(PackOutput output, CompletableFuture<Provider> lookupProvider) {
      super(output, lookupProvider);
   }

   protected void buildRecipes(RecipeOutput consumer) {
      this.con = consumer;
      ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SkiesItems.zeal_lighter)
         .requires(SkiesItems.charoite)
         .requires(SkiesItems.moonstone_shard)
         .unlockedBy(this.hasItem, has(SkiesItems.moonstone_shard))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.coarse_turquoise_dirt, 4)
         .define('D', SkiesBlocks.turquoise_dirt)
         .define('G', Items.GRAVEL)
         .pattern("DG")
         .pattern("GD")
         .unlockedBy(this.hasItem, has(Items.GRAVEL))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.coarse_lunar_dirt, 4)
         .define('D', SkiesBlocks.lunar_dirt)
         .define('G', Items.GRAVEL)
         .pattern("DG")
         .pattern("GD")
         .unlockedBy(this.hasItem, has(Items.GRAVEL))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.glowing_blinding_stone)
         .requires(SkiesBlocks.blinding_stone)
         .requires(Items.GLOWSTONE_DUST)
         .unlockedBy(this.hasItem, has(Items.GLOWSTONE_DUST))
         .save(this.con);
      this.slabsStairsWalls(SkiesBlocks.blinding_stone, SkiesBlocks.blinding_slab, SkiesBlocks.blinding_stairs, SkiesBlocks.blinding_wall, true);
      this.slabsStairsWalls(
         SkiesBlocks.glowing_blinding_stone, SkiesBlocks.glowing_blinding_slab, SkiesBlocks.glowing_blinding_stairs, SkiesBlocks.glowing_blinding_wall, true
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.blinding_stone, ImmutableList.of(SkiesBlocks.blinding_stone_pillar, SkiesBlocks.carved_blinding_stone)
      );
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.nature_stone, SkiesBlocks.nature_stonebrick, 4);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.glowing_nature_stone, SkiesBlocks.glowing_nature_stonebrick, 4);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.glowing_nature_stone)
         .requires(SkiesBlocks.nature_stone)
         .requires(Items.GLOWSTONE_DUST)
         .unlockedBy(this.hasItem, has(Items.GLOWSTONE_DUST))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.glowing_nature_stonebrick)
         .requires(SkiesBlocks.nature_stonebrick)
         .requires(Items.GLOWSTONE_DUST)
         .unlockedBy(this.hasItem, has(Items.GLOWSTONE_DUST))
         .save(this.con, BlueSkies.locate("glowing_nature_stonebrick_from_glowstone"));
      this.slabsStairs(SkiesBlocks.nature_stone, SkiesBlocks.nature_stone_slab, SkiesBlocks.nature_stone_stairs, true);
      this.slabsStairs(SkiesBlocks.glowing_nature_stone, SkiesBlocks.glowing_nature_stone_slab, SkiesBlocks.glowing_nature_stone_stairs, true);
      this.slabsStairsWalls(
         SkiesBlocks.nature_stonebrick, SkiesBlocks.nature_stonebrick_slab, SkiesBlocks.nature_stonebrick_stairs, SkiesBlocks.nature_stonebrick_wall, true
      );
      this.slabsStairsWalls(
         SkiesBlocks.glowing_nature_stonebrick,
         SkiesBlocks.glowing_nature_stonebrick_slab,
         SkiesBlocks.glowing_nature_stonebrick_stairs,
         SkiesBlocks.glowing_nature_stonebrick_wall,
         true
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.nature_stone,
         ImmutableList.of(
            SkiesBlocks.nature_stonebrick,
            SkiesBlocks.nature_stonebrick_slab,
            SkiesBlocks.nature_stonebrick_stairs,
            SkiesBlocks.nature_stonebrick_wall,
            SkiesBlocks.carved_nature_stonebrick,
            SkiesBlocks.nature_stone_pillar
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.glowing_nature_stone,
         ImmutableList.of(
            SkiesBlocks.glowing_nature_stonebrick,
            SkiesBlocks.glowing_nature_stonebrick_slab,
            SkiesBlocks.glowing_nature_stonebrick_stairs,
            SkiesBlocks.glowing_nature_stonebrick_wall
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.nature_stonebrick, ImmutableList.of(SkiesBlocks.carved_nature_stonebrick, SkiesBlocks.nature_stone_pillar)
      );
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.poison_stone, SkiesBlocks.poison_stonebrick, 4);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.glowing_poison_stone)
         .requires(SkiesBlocks.poison_stone)
         .requires(Items.GLOWSTONE_DUST)
         .unlockedBy(this.hasItem, has(Items.GLOWSTONE_DUST))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.rough_poison_stonebrick, 4)
         .define('S', SkiesBlocks.poison_stone)
         .define('B', SkiesBlocks.poison_stonebrick)
         .pattern("BS")
         .pattern("SB")
         .unlockedBy(this.hasItem, has(SkiesBlocks.poison_stonebrick))
         .save(this.con);
      this.slabsStairs(SkiesBlocks.poison_stone, SkiesBlocks.poison_stone_slab, SkiesBlocks.poison_stone_stairs, true);
      this.slabsStairs(SkiesBlocks.glowing_poison_stone, SkiesBlocks.glowing_poison_stone_slab, SkiesBlocks.glowing_poison_stone_stairs, true);
      this.slabsStairsWalls(
         SkiesBlocks.poison_stonebrick, SkiesBlocks.poison_stonebrick_slab, SkiesBlocks.poison_stonebrick_stairs, SkiesBlocks.poison_stonebrick_wall, true
      );
      this.slabsStairs(SkiesBlocks.rough_poison_stonebrick, SkiesBlocks.rough_poison_stonebrick_slab, SkiesBlocks.rough_poison_stonebrick_stairs, true);
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.poison_stone,
         ImmutableList.of(
            SkiesBlocks.poison_stonebrick,
            SkiesBlocks.poison_stonebrick_stairs,
            SkiesBlocks.poison_stonebrick_slab,
            SkiesBlocks.poison_stonebrick_wall,
            SkiesBlocks.rough_poison_stonebrick,
            SkiesBlocks.rough_poison_stonebrick_slab,
            SkiesBlocks.rough_poison_stonebrick_stairs,
            SkiesBlocks.carved_poison_stonebrick,
            SkiesBlocks.poison_stone_pillar
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.poison_stonebrick,
         ImmutableList.of(
            SkiesBlocks.rough_poison_stonebrick,
            SkiesBlocks.rough_poison_stonebrick_slab,
            SkiesBlocks.rough_poison_stonebrick_stairs,
            SkiesBlocks.carved_poison_stonebrick,
            SkiesBlocks.poison_stone_pillar
         )
      );
      Ingredient vine = Ingredient.of(new ItemLike[]{SkiesBlocks.lunar_vine, SkiesBlocks.dusk_vine});
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.mossy_lunar_cobblestone)
         .requires(SkiesBlocks.lunar_cobblestone)
         .requires(vine)
         .unlockedBy(this.hasItem, has(SkiesBlocks.lunar_vine))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.mossy_lunar_stonebrick)
         .requires(SkiesBlocks.lunar_stonebrick)
         .requires(vine)
         .unlockedBy(this.hasItem, has(SkiesBlocks.lunar_vine))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.lunar_stone, SkiesBlocks.lunar_stonebrick, 4);
      this.slabsStairsWalls(
         SkiesBlocks.lunar_cobblestone, SkiesBlocks.lunar_cobblestone_slab, SkiesBlocks.lunar_cobblestone_stairs, SkiesBlocks.lunar_cobblestone_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.mossy_lunar_cobblestone,
         SkiesBlocks.mossy_lunar_cobblestone_slab,
         SkiesBlocks.mossy_lunar_cobblestone_stairs,
         SkiesBlocks.mossy_lunar_cobblestone_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.lunar_stonebrick, SkiesBlocks.lunar_stonebrick_slab, SkiesBlocks.lunar_stonebrick_stairs, SkiesBlocks.lunar_stonebrick_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.mossy_lunar_stonebrick,
         SkiesBlocks.mossy_lunar_stonebrick_slab,
         SkiesBlocks.mossy_lunar_stonebrick_stairs,
         SkiesBlocks.mossy_lunar_stonebrick_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.cracked_lunar_stonebrick,
         SkiesBlocks.cracked_lunar_stonebrick_slab,
         SkiesBlocks.cracked_lunar_stonebrick_stairs,
         SkiesBlocks.cracked_lunar_stonebrick_wall
      );
      this.slabsStairs(SkiesBlocks.lunar_stone, SkiesBlocks.lunar_stone_slab, SkiesBlocks.lunar_stone_stairs);
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.lunar_cobblestone,
         ImmutableList.of(SkiesBlocks.lunar_cobblestone_slab, SkiesBlocks.lunar_cobblestone_stairs, SkiesBlocks.lunar_cobblestone_wall)
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.lunar_stone,
         ImmutableList.of(
            SkiesBlocks.lunar_stone_slab,
            SkiesBlocks.lunar_stone_stairs,
            SkiesBlocks.lunar_stonebrick,
            SkiesBlocks.lunar_stonebrick_slab,
            SkiesBlocks.lunar_stonebrick_stairs,
            SkiesBlocks.lunar_stonebrick_wall,
            SkiesBlocks.chiseled_lunar_stonebrick,
            SkiesBlocks.lunar_dripstone
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.lunar_stonebrick,
         ImmutableList.of(
            SkiesBlocks.lunar_stonebrick_slab, SkiesBlocks.lunar_stonebrick_stairs, SkiesBlocks.lunar_stonebrick_wall, SkiesBlocks.chiseled_lunar_stonebrick
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.mossy_lunar_stonebrick,
         ImmutableList.of(SkiesBlocks.mossy_lunar_stonebrick_slab, SkiesBlocks.mossy_lunar_stonebrick_stairs, SkiesBlocks.mossy_lunar_stonebrick_wall)
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.cracked_lunar_stonebrick,
         ImmutableList.of(SkiesBlocks.cracked_lunar_stonebrick_slab, SkiesBlocks.cracked_lunar_stonebrick_stairs, SkiesBlocks.cracked_lunar_stonebrick_wall)
      );
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.lunar_stone_button)
         .requires(SkiesBlocks.lunar_stone)
         .unlockedBy(this.hasItem, has(SkiesBlocks.lunar_stone))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SkiesBlocks.lunar_stone_pressure_plate)
         .define('#', SkiesBlocks.lunar_stone)
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesBlocks.lunar_stone))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SkiesBlocks.lunar_lever)
         .define('S', Ingredient.of(new ItemLike[]{SkiesItems.lunar_stick, SkiesItems.dusk_stick}))
         .define('C', SkiesBlocks.lunar_cobblestone)
         .pattern("S")
         .pattern("C")
         .unlockedBy(this.hasItem, has(SkiesBlocks.lunar_cobblestone))
         .save(this.con);
      vine = Ingredient.of(new ItemLike[]{SkiesBlocks.bluebright_vine, SkiesBlocks.starlit_vine});
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.mossy_turquoise_cobblestone)
         .requires(SkiesBlocks.turquoise_cobblestone)
         .requires(vine)
         .unlockedBy(this.hasItem, has(SkiesBlocks.bluebright_vine))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.mossy_turquoise_stonebrick)
         .requires(SkiesBlocks.turquoise_stonebrick)
         .requires(vine)
         .unlockedBy(this.hasItem, has(SkiesBlocks.bluebright_vine))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.turquoise_stone, SkiesBlocks.turquoise_stonebrick, 4);
      this.slabsStairsWalls(
         SkiesBlocks.turquoise_cobblestone,
         SkiesBlocks.turquoise_cobblestone_slab,
         SkiesBlocks.turquoise_cobblestone_stairs,
         SkiesBlocks.turquoise_cobblestone_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.mossy_turquoise_cobblestone,
         SkiesBlocks.mossy_turquoise_cobblestone_slab,
         SkiesBlocks.mossy_turquoise_cobblestone_stairs,
         SkiesBlocks.mossy_turquoise_cobblestone_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.turquoise_stonebrick,
         SkiesBlocks.turquoise_stonebrick_slab,
         SkiesBlocks.turquoise_stonebrick_stairs,
         SkiesBlocks.turquoise_stonebrick_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.mossy_turquoise_stonebrick,
         SkiesBlocks.mossy_turquoise_stonebrick_slab,
         SkiesBlocks.mossy_turquoise_stonebrick_stairs,
         SkiesBlocks.mossy_turquoise_stonebrick_wall
      );
      this.slabsStairsWalls(
         SkiesBlocks.cracked_turquoise_stonebrick,
         SkiesBlocks.cracked_turquoise_stonebrick_slab,
         SkiesBlocks.cracked_turquoise_stonebrick_stairs,
         SkiesBlocks.cracked_turquoise_stonebrick_wall
      );
      this.slabsStairs(SkiesBlocks.turquoise_stone, SkiesBlocks.turquoise_stone_slab, SkiesBlocks.turquoise_stone_stairs);
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.turquoise_cobblestone,
         ImmutableList.of(SkiesBlocks.turquoise_cobblestone_slab, SkiesBlocks.turquoise_cobblestone_stairs, SkiesBlocks.turquoise_cobblestone_wall)
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.turquoise_stone,
         ImmutableList.of(
            SkiesBlocks.turquoise_stone_slab,
            SkiesBlocks.turquoise_stone_stairs,
            SkiesBlocks.turquoise_stonebrick,
            SkiesBlocks.turquoise_stonebrick_slab,
            SkiesBlocks.turquoise_stonebrick_stairs,
            SkiesBlocks.turquoise_stonebrick_wall,
            SkiesBlocks.chiseled_turquoise_stonebrick,
            SkiesBlocks.turquoise_dripstone
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.turquoise_stonebrick,
         ImmutableList.of(
            SkiesBlocks.turquoise_stonebrick_slab,
            SkiesBlocks.turquoise_stonebrick_stairs,
            SkiesBlocks.turquoise_stonebrick_wall,
            SkiesBlocks.chiseled_turquoise_stonebrick
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.mossy_turquoise_stonebrick,
         ImmutableList.of(
            SkiesBlocks.mossy_turquoise_stonebrick_slab, SkiesBlocks.mossy_turquoise_stonebrick_stairs, SkiesBlocks.mossy_turquoise_stonebrick_wall
         )
      );
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.cracked_turquoise_stonebrick,
         ImmutableList.of(
            SkiesBlocks.cracked_turquoise_stonebrick_slab, SkiesBlocks.cracked_turquoise_stonebrick_stairs, SkiesBlocks.cracked_turquoise_stonebrick_wall
         )
      );
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.turquoise_stone_button)
         .requires(SkiesBlocks.turquoise_stone)
         .unlockedBy(this.hasItem, has(SkiesBlocks.turquoise_stone))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SkiesBlocks.turquoise_stone_pressure_plate)
         .define('#', SkiesBlocks.turquoise_stone)
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesBlocks.turquoise_stone))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SkiesBlocks.turquoise_lever)
         .define('S', Ingredient.of(new ItemLike[]{SkiesItems.bluebright_stick, SkiesItems.starlit_stick}))
         .define('C', SkiesBlocks.turquoise_cobblestone)
         .pattern("S")
         .pattern("C")
         .unlockedBy(this.hasItem, has(SkiesBlocks.turquoise_cobblestone))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.turquoise_dripstone, SkiesBlocks.turquoise_stone);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.lunar_dripstone, SkiesBlocks.lunar_stone);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystal_sand, SkiesBlocks.crystal_sandstone);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.chiseled_crystal_sandstone)
         .define('#', SkiesBlocks.crystal_sandstone_slab)
         .pattern("#")
         .pattern("#")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystal_sandstone_slab))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystal_sandstone, SkiesBlocks.cut_crystal_sandstone, 4);
      this.slabsStairsWalls(
         SkiesBlocks.crystal_sandstone, SkiesBlocks.crystal_sandstone_slab, SkiesBlocks.crystal_sandstone_stairs, SkiesBlocks.crystal_sandstone_wall
      );
      this.slabsStairs(SkiesBlocks.smooth_crystal_sandstone, SkiesBlocks.smooth_crystal_sandstone_slab, SkiesBlocks.smooth_crystal_sandstone_stairs);
      this.slabs(SkiesBlocks.cut_crystal_sandstone, SkiesBlocks.cut_crystal_sandstone_slab).save(this.con);
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.crystal_sandstone,
         ImmutableList.of(
            SkiesBlocks.chiseled_crystal_sandstone,
            SkiesBlocks.crystal_sandstone_slab,
            SkiesBlocks.crystal_sandstone_stairs,
            SkiesBlocks.crystal_sandstone_wall,
            SkiesBlocks.cut_crystal_sandstone,
            SkiesBlocks.cut_crystal_sandstone_slab,
            SkiesBlocks.crystal_sandstone_pillar
         )
      );
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.cut_crystal_sandstone, ImmutableList.of(SkiesBlocks.cut_crystal_sandstone_slab));
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.smooth_crystal_sandstone,
         ImmutableList.of(SkiesBlocks.smooth_crystal_sandstone_slab, SkiesBlocks.smooth_crystal_sandstone_stairs)
      );
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.midnight_sand, SkiesBlocks.midnight_sandstone);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.chiseled_midnight_sandstone)
         .define('#', SkiesBlocks.midnight_sandstone_slab)
         .pattern("#")
         .pattern("#")
         .unlockedBy(this.hasItem, has(SkiesBlocks.midnight_sandstone_slab))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.midnight_sandstone, SkiesBlocks.cut_midnight_sandstone, 4);
      this.slabsStairsWalls(
         SkiesBlocks.midnight_sandstone, SkiesBlocks.midnight_sandstone_slab, SkiesBlocks.midnight_sandstone_stairs, SkiesBlocks.midnight_sandstone_wall
      );
      this.slabsStairs(SkiesBlocks.smooth_midnight_sandstone, SkiesBlocks.smooth_midnight_sandstone_slab, SkiesBlocks.smooth_midnight_sandstone_stairs);
      this.slabs(SkiesBlocks.cut_midnight_sandstone, SkiesBlocks.cut_midnight_sandstone_slab).save(this.con);
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.midnight_sandstone,
         ImmutableList.of(
            SkiesBlocks.chiseled_midnight_sandstone,
            SkiesBlocks.midnight_sandstone_slab,
            SkiesBlocks.midnight_sandstone_stairs,
            SkiesBlocks.midnight_sandstone_wall,
            SkiesBlocks.cut_midnight_sandstone,
            SkiesBlocks.cut_midnight_sandstone_slab,
            SkiesBlocks.midnight_sandstone_pillar
         )
      );
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.cut_midnight_sandstone, ImmutableList.of(SkiesBlocks.cut_midnight_sandstone_slab));
      this.stoneCutting(
         RecipeCategory.BUILDING_BLOCKS,
         SkiesBlocks.smooth_midnight_sandstone,
         ImmutableList.of(SkiesBlocks.smooth_midnight_sandstone_slab, SkiesBlocks.smooth_midnight_sandstone_stairs)
      );
      this.simple3x3(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.brumble_vine_top, SkiesBlocks.brumble_block);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.polished_brumble, SkiesBlocks.polished_brumble_bricks, 4);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.polished_brumble, ImmutableList.of(SkiesBlocks.polished_brumble_bricks));
      this.slabsStairsWalls(
         SkiesBlocks.polished_brumble, SkiesBlocks.polished_brumble_slab, SkiesBlocks.polished_brumble_stairs, SkiesBlocks.polished_brumble_wall, true
      );
      this.slabsStairsWalls(
         SkiesBlocks.polished_brumble_bricks,
         SkiesBlocks.polished_brumble_brick_slab,
         SkiesBlocks.polished_brumble_brick_stairs,
         SkiesBlocks.polished_brumble_brick_wall,
         true
      );
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.brumble_lantern)
         .define('#', SkiesBlocks.brumble_block)
         .define('V', SkiesBlocks.brumble_vine_top)
         .pattern(" # ")
         .pattern("#V#")
         .pattern(" # ")
         .unlockedBy(this.hasItem, has(SkiesBlocks.brumble_block))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.sea_moss_carpet, 3)
         .define('#', SkiesBlocks.sea_moss_block)
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesBlocks.sea_moss_block))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.taratite, SkiesBlocks.polished_taratite, 4);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.taratite, ImmutableList.of(SkiesBlocks.polished_taratite));
      this.slabsStairsWalls(SkiesBlocks.taratite, SkiesBlocks.taratite_slab, SkiesBlocks.taratite_stairs, SkiesBlocks.taratite_wall, true);
      this.slabsStairsWalls(
         SkiesBlocks.polished_taratite, SkiesBlocks.polished_taratite_slab, SkiesBlocks.polished_taratite_stairs, SkiesBlocks.polished_taratite_wall, true
      );
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.rimestone, SkiesBlocks.polished_rimestone, 4);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.rimestone, ImmutableList.of(SkiesBlocks.polished_rimestone));
      this.slabsStairsWalls(SkiesBlocks.rimestone, SkiesBlocks.rimestone_slab, SkiesBlocks.rimestone_stairs, SkiesBlocks.rimestone_wall, true);
      this.slabsStairsWalls(
         SkiesBlocks.polished_rimestone, SkiesBlocks.polished_rimestone_slab, SkiesBlocks.polished_rimestone_stairs, SkiesBlocks.polished_rimestone_wall, true
      );
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.umber, SkiesBlocks.polished_umber, 4);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.umber, ImmutableList.of(SkiesBlocks.polished_umber));
      this.slabsStairsWalls(SkiesBlocks.umber, SkiesBlocks.umber_slab, SkiesBlocks.umber_stairs, SkiesBlocks.umber_wall, true);
      this.slabsStairsWalls(
         SkiesBlocks.polished_umber, SkiesBlocks.polished_umber_slab, SkiesBlocks.polished_umber_stairs, SkiesBlocks.polished_umber_wall, true
      );
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.cinderstone, SkiesBlocks.polished_cinderstone, 4);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.cinderstone, ImmutableList.of(SkiesBlocks.polished_cinderstone));
      this.slabsStairsWalls(SkiesBlocks.cinderstone, SkiesBlocks.cinderstone_slab, SkiesBlocks.cinderstone_stairs, SkiesBlocks.cinderstone_wall, true);
      this.slabsStairsWalls(
         SkiesBlocks.polished_cinderstone,
         SkiesBlocks.polished_cinderstone_slab,
         SkiesBlocks.polished_cinderstone_stairs,
         SkiesBlocks.polished_cinderstone_wall,
         true
      );
      List<SkiesRecipeProv.WoodMap> woodMapping = List.of(
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.BLUEBRIGHT_LOGS,
            SkiesBlocks.bluebright_log,
            SkiesBlocks.bluebright_wood,
            SkiesBlocks.stripped_bluebright_log,
            SkiesBlocks.stripped_bluebright_wood,
            SkiesBlocks.bluebright_planks,
            SkiesBlocks.bluebright_slab,
            SkiesBlocks.bluebright_stairs,
            SkiesBlocks.bluebright_crafting_table,
            SkiesBlocks.bluebright_bookshelf,
            SkiesItems.bluebright_chest,
            SkiesBlocks.bluebright_pressure_plate,
            SkiesBlocks.bluebright_button,
            SkiesItems.bluebright_door,
            SkiesBlocks.bluebright_trapdoor,
            SkiesItems.bluebright_stick,
            SkiesBlocks.bluebright_ladder,
            SkiesBlocks.bluebright_fence,
            SkiesBlocks.bluebright_fence_gate,
            SkiesItems.bluebright_sign,
            SkiesItems.bluebright_hanging_sign
         ),
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.STARLIT_LOGS,
            SkiesBlocks.starlit_log,
            SkiesBlocks.starlit_wood,
            SkiesBlocks.stripped_starlit_log,
            SkiesBlocks.stripped_starlit_wood,
            SkiesBlocks.starlit_planks,
            SkiesBlocks.starlit_slab,
            SkiesBlocks.starlit_stairs,
            SkiesBlocks.starlit_crafting_table,
            SkiesBlocks.starlit_bookshelf,
            SkiesItems.starlit_chest,
            SkiesBlocks.starlit_pressure_plate,
            SkiesBlocks.starlit_button,
            SkiesItems.starlit_door,
            SkiesBlocks.starlit_trapdoor,
            SkiesItems.starlit_stick,
            SkiesBlocks.starlit_ladder,
            SkiesBlocks.starlit_fence,
            SkiesBlocks.starlit_fence_gate,
            SkiesItems.starlit_sign,
            SkiesItems.starlit_hanging_sign
         ),
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.FROSTBRIGHT_LOGS,
            SkiesBlocks.frostbright_log,
            SkiesBlocks.frostbright_wood,
            SkiesBlocks.stripped_frostbright_log,
            SkiesBlocks.stripped_frostbright_wood,
            SkiesBlocks.frostbright_planks,
            SkiesBlocks.frostbright_slab,
            SkiesBlocks.frostbright_stairs,
            SkiesBlocks.frostbright_crafting_table,
            SkiesBlocks.frostbright_bookshelf,
            SkiesItems.frostbright_chest,
            SkiesBlocks.frostbright_pressure_plate,
            SkiesBlocks.frostbright_button,
            SkiesItems.frostbright_door,
            SkiesBlocks.frostbright_trapdoor,
            SkiesItems.frostbright_stick,
            SkiesBlocks.frostbright_ladder,
            SkiesBlocks.frostbright_fence,
            SkiesBlocks.frostbright_fence_gate,
            SkiesItems.frostbright_sign,
            SkiesItems.frostbright_hanging_sign
         ),
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.LUNAR_LOGS,
            SkiesBlocks.lunar_log,
            SkiesBlocks.lunar_wood,
            SkiesBlocks.stripped_lunar_log,
            SkiesBlocks.stripped_lunar_wood,
            SkiesBlocks.lunar_planks,
            SkiesBlocks.lunar_slab,
            SkiesBlocks.lunar_stairs,
            SkiesBlocks.lunar_crafting_table,
            SkiesBlocks.lunar_bookshelf,
            SkiesItems.lunar_chest,
            SkiesBlocks.lunar_pressure_plate,
            SkiesBlocks.lunar_button,
            SkiesItems.lunar_door,
            SkiesBlocks.lunar_trapdoor,
            SkiesItems.lunar_stick,
            SkiesBlocks.lunar_ladder,
            SkiesBlocks.lunar_fence,
            SkiesBlocks.lunar_fence_gate,
            SkiesItems.lunar_sign,
            SkiesItems.lunar_hanging_sign
         ),
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.DUSK_LOGS,
            SkiesBlocks.dusk_log,
            SkiesBlocks.dusk_wood,
            SkiesBlocks.stripped_dusk_log,
            SkiesBlocks.stripped_dusk_wood,
            SkiesBlocks.dusk_planks,
            SkiesBlocks.dusk_slab,
            SkiesBlocks.dusk_stairs,
            SkiesBlocks.dusk_crafting_table,
            SkiesBlocks.dusk_bookshelf,
            SkiesItems.dusk_chest,
            SkiesBlocks.dusk_pressure_plate,
            SkiesBlocks.dusk_button,
            SkiesItems.dusk_door,
            SkiesBlocks.dusk_trapdoor,
            SkiesItems.dusk_stick,
            SkiesBlocks.dusk_ladder,
            SkiesBlocks.dusk_fence,
            SkiesBlocks.dusk_fence_gate,
            SkiesItems.dusk_sign,
            SkiesItems.dusk_hanging_sign
         ),
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.MAPLE_LOGS,
            SkiesBlocks.maple_log,
            SkiesBlocks.maple_wood,
            SkiesBlocks.stripped_maple_log,
            SkiesBlocks.stripped_maple_wood,
            SkiesBlocks.maple_planks,
            SkiesBlocks.maple_slab,
            SkiesBlocks.maple_stairs,
            SkiesBlocks.maple_crafting_table,
            SkiesBlocks.maple_bookshelf,
            SkiesItems.maple_chest,
            SkiesBlocks.maple_pressure_plate,
            SkiesBlocks.maple_button,
            SkiesItems.maple_door,
            SkiesBlocks.maple_trapdoor,
            SkiesItems.maple_stick,
            SkiesBlocks.maple_ladder,
            SkiesBlocks.maple_fence,
            SkiesBlocks.maple_fence_gate,
            SkiesItems.maple_sign,
            SkiesItems.maple_hanging_sign
         ),
         new SkiesRecipeProv.WoodMap(
            SkiesItemTags.COMET_LOGS,
            SkiesBlocks.comet_log,
            SkiesBlocks.comet_wood,
            SkiesBlocks.stripped_comet_log,
            SkiesBlocks.stripped_comet_wood,
            SkiesBlocks.comet_planks,
            SkiesBlocks.comet_slab,
            SkiesBlocks.comet_stairs,
            SkiesBlocks.comet_crafting_table,
            SkiesBlocks.comet_bookshelf,
            SkiesItems.comet_chest,
            SkiesBlocks.comet_pressure_plate,
            SkiesBlocks.comet_button,
            SkiesItems.comet_door,
            SkiesBlocks.comet_trapdoor,
            SkiesItems.comet_stick,
            SkiesBlocks.comet_ladder,
            SkiesBlocks.comet_fence,
            SkiesBlocks.comet_fence_gate,
            SkiesItems.comet_sign,
            SkiesItems.comet_hanging_sign
         )
      );
      woodMapping.forEach(
         wood -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.strippedWood, 3)
               .define('#', wood.strippedLog)
               .pattern("##")
               .pattern("##")
               .group("stripped_bark")
               .unlockedBy(this.hasItem, has(wood.strippedLog))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.wood, 3)
               .define('#', wood.log)
               .pattern("##")
               .pattern("##")
               .group("bark")
               .unlockedBy(this.hasItem, has(wood.wood))
               .save(this.con);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, wood.plank, 4)
               .requires(wood.logTag)
               .group("planks")
               .unlockedBy(this.hasItem, has(wood.logTag))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, wood.stick, 4)
               .define('#', wood.plank)
               .pattern("#")
               .pattern("#")
               .group("sticks")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.craftingTable)
               .define('#', wood.plank)
               .pattern("##")
               .pattern("##")
               .group("crafting_tables")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            this.slabs(wood.plank, wood.slab).group("wooden_slab").save(this.con);
            this.stairs(wood.plank, wood.stair).group("wooden_stairs").save(this.con);
            this.fencesGates(wood.plank, wood.fence, wood.gate);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.bookshelf)
               .define('#', wood.plank)
               .define('B', Items.BOOK)
               .pattern("###")
               .pattern("BBB")
               .pattern("###")
               .group("bookshelves")
               .unlockedBy(this.hasItem, has(Items.BOOK))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.door, 3)
               .define('#', wood.plank)
               .pattern("##")
               .pattern("##")
               .pattern("##")
               .group("wooden_door")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.trapdoor, 2)
               .define('#', wood.plank)
               .pattern("###")
               .pattern("###")
               .group("wooden_trapdoor")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, wood.pressurePlate)
               .define('#', wood.plank)
               .pattern("##")
               .group("wooden_pressure_plate")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, wood.button)
               .requires(wood.plank)
               .group("wooden_button")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.ladder, 3)
               .define('#', wood.stick)
               .pattern("# #")
               .pattern("###")
               .pattern("# #")
               .unlockedBy(this.hasItem, has(wood.stick))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.chest)
               .define('#', wood.plank)
               .pattern("###")
               .pattern("# #")
               .pattern("###")
               .unlockedBy(this.hasItem, has(wood.stick))
               .save(this.con);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood.sign, 3)
               .define('P', wood.plank)
               .define('S', Ingredient.of(net.neoforged.neoforge.common.Tags.Items.RODS_WOODEN))
               .pattern("PPP")
               .pattern("PPP")
               .pattern(" S ")
               .group("wooden_sign")
               .unlockedBy(this.hasItem, has(wood.plank))
               .save(this.con);
            hangingSign(consumer, wood.hangingSign, wood.strippedLog);
         }
      );
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystallized_wood, 3)
         .define('#', SkiesBlocks.crystallized_log)
         .pattern("##")
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystallized_log))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystallized_planks, 4)
         .requires(SkiesItemTags.CRYSTALLIZED_LOGS)
         .unlockedBy(this.hasItem, has(SkiesItemTags.CRYSTALLIZED_LOGS))
         .save(this.con);
      this.walls(SkiesBlocks.crystallized_log, SkiesBlocks.crystallized_wall, "from_log");
      this.walls(SkiesBlocks.crystallized_wood, SkiesBlocks.crystallized_wall, "from_wood");
      this.slabsStairs(SkiesBlocks.crystallized_planks, SkiesBlocks.crystallized_slab, SkiesBlocks.crystallized_stairs);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesItems.crystallized_door, 3)
         .define('#', SkiesBlocks.crystallized_planks)
         .pattern("##")
         .pattern("##")
         .pattern("##")
         .group("wooden_door")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystallized_planks))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystallized_trapdoor, 2)
         .define('#', SkiesBlocks.crystallized_planks)
         .pattern("###")
         .pattern("###")
         .group("wooden_trapdoor")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystallized_planks))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SkiesBlocks.crystallized_pressure_plate)
         .define('#', SkiesBlocks.crystallized_planks)
         .pattern("##")
         .group("wooden_pressure_plate")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystallized_planks))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, SkiesBlocks.crystallized_button)
         .requires(SkiesBlocks.crystallized_planks)
         .group("wooden_button")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystallized_planks))
         .save(this.con);

      for (SkiesRecipeProv.OreMap mat : List.of(
         new SkiesRecipeProv.OreMap(
            SkiesItems.pyrope_gem,
            SkiesBlocks.pyrope_block,
            SkiesItems.pyrope_chestplate,
            SkiesItems.pyrope_leggings,
            SkiesItems.pyrope_boots,
            SkiesItems.pyrope_helmet
         ),
         new SkiesRecipeProv.OreMap(
            SkiesItems.aquite,
            SkiesBlocks.aquite_block,
            SkiesItems.aquite_chestplate,
            SkiesItems.aquite_leggings,
            SkiesItems.aquite_boots,
            SkiesItems.aquite_helmet
         ),
         new SkiesRecipeProv.OreMap(
            SkiesItems.diopside_gem,
            SkiesBlocks.diopside_block,
            SkiesItems.diopside_chestplate,
            SkiesItems.diopside_leggings,
            SkiesItems.diopside_boots,
            SkiesItems.diopside_helmet
         ),
         new SkiesRecipeProv.OreMap(
            SkiesItems.charoite,
            SkiesBlocks.charoite_block,
            SkiesItems.charoite_chestplate,
            SkiesItems.charoite_leggings,
            SkiesItems.charoite_boots,
            SkiesItems.charoite_helmet
         ),
         new SkiesRecipeProv.OreMap(
            SkiesItems.horizonite_ingot,
            SkiesBlocks.horizonite_block,
            SkiesItems.horizonite_chestplate,
            SkiesItems.horizonite_leggings,
            SkiesItems.horizonite_boots,
            SkiesItems.horizonite_helmet
         )
      )) {
         ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, mat.material, 9)
            .requires(mat.block)
            .unlockedBy(this.hasItem, has(mat.block))
            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(mat.material.asItem()).getPath() + "_from_block"));
         this.simple3x3(RecipeCategory.MISC, mat.material, mat.block);
         ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.helmet)
            .define('#', mat.material)
            .pattern("###")
            .pattern("# #")
            .unlockedBy(this.hasItem, has(mat.material))
            .save(this.con);
         ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.chestplate)
            .define('#', mat.material)
            .pattern("# #")
            .pattern("###")
            .pattern("###")
            .unlockedBy(this.hasItem, has(mat.material))
            .save(this.con);
         ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.leggings)
            .define('#', mat.material)
            .pattern("###")
            .pattern("# #")
            .pattern("# #")
            .unlockedBy(this.hasItem, has(mat.material))
            .save(this.con);
         ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, mat.boots)
            .define('#', mat.material)
            .pattern("# #")
            .pattern("# #")
            .unlockedBy(this.hasItem, has(mat.material))
            .save(this.con);
      }

      for (Pair<Item, Item> entry : List.of(
         Pair.of(SkiesItems.ventium_nugget, SkiesItems.ventium_ingot),
         Pair.of(SkiesItems.falsite_nugget, SkiesItems.falsite_ingot),
         Pair.of(SkiesItems.horizonite_nugget, SkiesItems.horizonite_ingot)
      )) {
         ItemLike ingot = (ItemLike)entry.getRight();
         ItemLike nugget = (ItemLike)entry.getLeft();
         ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
            .requires(ingot)
            .unlockedBy(this.hasItem, has(ingot))
            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(nugget.asItem()).getPath() + "_from_ingot"));
         ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot, 1)
            .define('#', nugget)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .unlockedBy(this.hasItem, has(nugget))
            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(ingot.asItem()).getPath() + "_from_nuggets"));
      }

      for (Pair<Item, Block> entry : List.of(
         Pair.of(SkiesItems.raw_aquite, SkiesBlocks.raw_aquite_block),
         Pair.of(SkiesItems.raw_charoite, SkiesBlocks.raw_charoite_block),
         Pair.of(SkiesItems.raw_ventium, SkiesBlocks.raw_ventium_block),
         Pair.of(SkiesItems.raw_falsite, SkiesBlocks.raw_falsite_block),
         Pair.of(SkiesItems.raw_horizonite, SkiesBlocks.raw_horizonite_block)
      )) {
         ItemLike rawBlock = (ItemLike)entry.getRight();
         ItemLike rawOre = (ItemLike)entry.getLeft();
         String rawOreName = BuiltInRegistries.ITEM.getKey(rawOre.asItem()).getPath();
         ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, rawOre, 9)
            .requires(rawBlock)
            .unlockedBy(this.hasItem, has(rawBlock))
            .save(this.con, BlueSkies.find(rawOreName + "_from_block"));
         ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, rawBlock, 1)
            .define('#', rawOre)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .unlockedBy(this.hasItem, has(rawOre))
            .save(this.con, BlueSkies.find(BuiltInRegistries.ITEM.getKey(rawBlock.asItem()).getPath() + "_from_" + rawOreName));
      }

      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SkiesItems.falsite_ingot, 9)
         .requires(SkiesItemTags.FALSITE_BLOCKS)
         .unlockedBy(this.hasItem, has(SkiesItemTags.FALSITE_BLOCKS))
         .save(this.con, BlueSkies.find("falsite_from_block"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.falsite_block)
         .define('#', SkiesItemTags.FALSITE)
         .pattern("###")
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesItemTags.FALSITE))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SkiesItems.ventium_ingot, 9)
         .requires(SkiesItemTags.VENTIUM_BLOCKS)
         .unlockedBy(this.hasItem, has(SkiesItemTags.VENTIUM_BLOCKS))
         .save(this.con, BlueSkies.find("ventium_from_block"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.ventium_block)
         .define('#', SkiesItemTags.VENTIUM)
         .pattern("###")
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesItemTags.VENTIUM))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SkiesBlocks.moonstone, 9)
         .requires(SkiesItemTags.MOONSTONE_BLOCKS)
         .unlockedBy(this.hasItem, has(SkiesItemTags.MOONSTONE_BLOCKS))
         .save(this.con, BlueSkies.find("moonstone_from_block"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.moonstone_block)
         .define('#', SkiesBlocks.moonstone_crystal)
         .pattern("##")
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesBlocks.moonstone_crystal))
         .save(this.con, BlueSkies.find("moonstone_block_from_moonstone_crystal"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.moonstone_block)
         .define('#', SkiesItemTags.MOONSTONE)
         .pattern("###")
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesItemTags.MOONSTONE))
         .save(this.con, BlueSkies.find("moonstone_block_from_moonstone"));
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SkiesBlocks.moonstone, 2)
         .define('#', SkiesItemTags.MOONSTONE_SHARD)
         .pattern("##")
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesItemTags.MOONSTONE_SHARD))
         .save(this.con, BlueSkies.find("moonstone_from_shard"));
      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SkiesBlocks.sunstone_crystal, 4)
         .requires(SkiesBlocks.sunstone_block)
         .unlockedBy(this.hasItem, has(SkiesBlocks.sunstone_block))
         .save(this.con, BlueSkies.find("sunstone_crystal_from_block"));
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SkiesBlocks.sunstone_block)
         .define('#', SkiesBlocks.sunstone_crystal)
         .pattern("##")
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesBlocks.sunstone_crystal))
         .save(this.con);

      for (ToolRecipe.Material material : ToolRecipe.Material.values()) {
         for (ToolRecipe.RecipeShape shape : ToolRecipe.RecipeShape.values()) {
            ResourceLocation item = BlueSkies.locate(material.getSerializedName() + "_" + shape.getSerializedName());
            ToolRecipeBuilder.of(material, shape, ((Item)BuiltInRegistries.ITEM.get(item)).getDefaultInstance()).save(this.con, item);
         }
      }

      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SkiesItems.ventium_shears)
         .define('#', SkiesItemTags.VENTIUM)
         .pattern("# ")
         .pattern(" #")
         .unlockedBy(this.hasItem, has(SkiesItemTags.VENTIUM))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SkiesItems.ventium_bucket)
         .define('#', SkiesItemTags.VENTIUM)
         .pattern("# #")
         .pattern(" # ")
         .unlockedBy(this.hasItem, has(SkiesItemTags.VENTIUM))
         .save(this.con);
      Map<Item, Item> spearMap = new HashMap<>();
      spearMap.put(SkiesItems.bluebright_spear, SkiesItems.bluebright_stick);
      spearMap.put(SkiesItems.starlit_spear, SkiesItems.starlit_stick);
      spearMap.put(SkiesItems.frostbright_spear, SkiesItems.frostbright_stick);
      spearMap.put(SkiesItems.lunar_spear, SkiesItems.lunar_stick);
      spearMap.put(SkiesItems.dusk_spear, SkiesItems.dusk_stick);
      spearMap.put(SkiesItems.maple_spear, SkiesItems.maple_stick);
      spearMap.put(SkiesItems.comet_spear, SkiesItems.comet_stick);
      spearMap.forEach(
         (spear, stick) -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, spear)
            .define('M', SkiesItems.moonstone_shard)
            .define('S', stick)
            .pattern("  M")
            .pattern(" S ")
            .pattern("S  ")
            .unlockedBy(this.hasItem, has(SkiesItems.moonstone_shard))
            .save(this.con)
      );
      ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SkiesItems.moonstone_shield)
         .define('M', SkiesBlocks.vitreous_moonstone)
         .define('S', net.neoforged.neoforge.common.Tags.Items.RODS_WOODEN)
         .pattern("MSM")
         .pattern("MMM")
         .pattern(" M ")
         .unlockedBy(this.hasItem, has(SkiesBlocks.vitreous_moonstone))
         .save(this.con);
      ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 3)
         .requires(SkiesItems.azulfo_horn)
         .unlockedBy(this.hasItem, has(SkiesItems.azulfo_horn))
         .save(this.con, BlueSkies.find("bone_meal_from_azulfo"));
      Map<ItemLike, ItemLike> flowerDyeMap = GelCollectors.mapOf(
         SkiesBlocks.snowbloom,
         Items.WHITE_DYE,
         new Object[]{
            SkiesBlocks.camellia,
            Items.PINK_DYE,
            SkiesBlocks.blaze_bud,
            Items.ORANGE_DYE,
            SkiesBlocks.crystal_flower,
            Items.LIGHT_GRAY_DYE,
            SkiesBlocks.moonlit_bloom,
            Items.BLUE_DYE,
            SkiesBlocks.polar_posy,
            Items.LIME_DYE,
            SkiesBlocks.blush_blossom,
            Items.PINK_DYE,
            SkiesBlocks.chillweed,
            Items.LIGHT_BLUE_DYE,
            SkiesBlocks.briskbloom,
            Items.PURPLE_DYE,
            SkiesBlocks.flare_floret,
            Items.RED_DYE,
            SkiesBlocks.nightcress,
            Items.BLUE_DYE,
            SkiesBlocks.lucentroot,
            Items.YELLOW_DYE,
            SkiesBlocks.frose,
            Items.LIGHT_BLUE_DYE,
            SkiesBlocks.midday_bayhop,
            Items.PINK_DYE
         }
      );
      flowerDyeMap.forEach(
         (flower, dye) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dye)
            .requires(flower)
            .unlockedBy(this.hasItem, has(flower))
            .save(
               this.con,
               BlueSkies.find(BuiltInRegistries.ITEM.getKey(dye.asItem()).getPath() + "_from_" + BuiltInRegistries.ITEM.getKey(flower.asItem()).getPath())
            )
      );
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SkiesItems.camel_saddle)
         .define('L', Items.LEATHER)
         .define('I', SkiesItemTags.VENTIUM)
         .define('F', SkiesItems.fox_pelt)
         .pattern("LLL")
         .pattern("LIL")
         .pattern("F F")
         .unlockedBy(this.hasItem, has(SkiesItems.fox_pelt))
         .group("camel_saddle")
         .save(this.con, BlueSkies.find("camel_saddle_with_ventium"));
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SkiesItems.camel_saddle)
         .define('L', Items.LEATHER)
         .define('I', net.neoforged.neoforge.common.Tags.Items.INGOTS_IRON)
         .define('F', SkiesItems.fox_pelt)
         .pattern("LLL")
         .pattern("LIL")
         .pattern("F F")
         .unlockedBy(this.hasItem, has(SkiesItems.fox_pelt))
         .group("camel_saddle")
         .save(this.con, BlueSkies.find("camel_saddle_with_iron"));
      ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SkiesBlocks.moonstone_pressure_plate)
         .define('#', SkiesItems.moonstone_shard)
         .pattern("##")
         .unlockedBy(this.hasItem, has(SkiesItems.moonstone_shard))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystal_glass_pane, 16)
         .define('#', SkiesBlocks.crystal_glass)
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesBlocks.crystal_glass))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.midnight_glass_pane, 16)
         .define('#', SkiesBlocks.midnight_glass)
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesBlocks.midnight_glass))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, SkiesBlocks.comet_berry_pie)
         .define('C', SkiesItems.comet_berries)
         .define('I', SkiesItemTags.VENTIUM)
         .define('W', Items.WHEAT)
         .pattern("WWW")
         .pattern("CCC")
         .pattern("III")
         .group("comet_berry_pie")
         .unlockedBy(this.hasItem, has(SkiesItems.comet_berries))
         .save(this.con, BlueSkies.find("comet_berry_pie_with_ventium"));
      ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, SkiesBlocks.comet_berry_pie)
         .define('C', SkiesItems.comet_berries)
         .define('I', net.neoforged.neoforge.common.Tags.Items.INGOTS_IRON)
         .define('W', Items.WHEAT)
         .pattern("WWW")
         .pattern("CCC")
         .pattern("III")
         .group("comet_berry_pie")
         .unlockedBy(this.hasItem, has(SkiesItems.comet_berries))
         .save(this.con, BlueSkies.find("comet_berry_pie_with_iron"));
      ImmutableMap<ItemLike, ItemLike> cropSeedMap = ImmutableMap.of(
         SkiesItems.winter_leaf_seeds,
         SkiesItems.winter_leaves,
         SkiesItems.pine_fruit_seeds,
         SkiesItems.pine_fruit,
         SkiesItems.scalefruit_seeds,
         SkiesItems.scalefruit,
         SkiesItems.fiery_bean_seeds,
         SkiesItems.fiery_beans
      );
      cropSeedMap.forEach(
         (seed, crop) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, seed).requires(crop).unlockedBy(this.hasItem, has(crop)).save(this.con)
      );
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.FURNACE)
         .define('#', SkiesItemTags.COBBLESTONES)
         .pattern("###")
         .pattern("# #")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesItemTags.COBBLESTONES))
         .save(this.con, BlueSkies.find("furnace_compat"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.STONECUTTER)
         .define('#', SkiesItemTags.STONES)
         .define('V', SkiesItemTags.VENTIUM)
         .pattern(" V ")
         .pattern("###")
         .unlockedBy(this.hasItem, has(SkiesItemTags.STONES))
         .save(this.con, BlueSkies.find("stonecutter_compat"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.ANVIL)
         .define('B', SkiesItemTags.VENTIUM_BLOCKS)
         .define('I', SkiesItemTags.VENTIUM)
         .pattern("BBB")
         .pattern(" I ")
         .pattern("III")
         .unlockedBy(this.hasItem, has(SkiesItemTags.VENTIUM))
         .save(this.con, BlueSkies.find("anvil_compat"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.snowcap_oven)
         .define('F', Items.FURNACE)
         .define('S', SkiesBlocks.snowcap_mushroom_block)
         .define('C', SkiesBlocks.turquoise_cobblestone)
         .pattern("SSS")
         .pattern("CFC")
         .pattern("CCC")
         .unlockedBy(this.hasItem, has(SkiesBlocks.snowcap_mushroom_block))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.horizonite_forge)
         .define('F', Items.FURNACE)
         .define('H', SkiesItems.horizonite_ingot)
         .define('C', SkiesBlocks.lunar_cobblestone)
         .pattern("HHH")
         .pattern("HFH")
         .pattern("CCC")
         .unlockedBy(this.hasItem, has(SkiesItems.horizonite_ingot))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.JUKEBOX)
         .define('P', ItemTags.PLANKS)
         .define('C', SkiesItems.charoite)
         .pattern("PPP")
         .pattern("PCP")
         .pattern("PPP")
         .unlockedBy(this.hasItem, has(SkiesItems.charoite))
         .save(this.con, BlueSkies.find("jukebox_compat"));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.ENCHANTING_TABLE)
         .define('O', Items.OBSIDIAN)
         .define('B', Items.BOOK)
         .define('C', SkiesItems.charoite)
         .pattern(" B ")
         .pattern("COC")
         .pattern("OOO")
         .unlockedBy(this.hasItem, has(SkiesItems.charoite))
         .save(this.con, BlueSkies.find("enchanting_table_compat"));
      ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Blocks.CAKE)
         .define('A', SkiesItems.ventium_milk_bucket)
         .define('B', Items.SUGAR)
         .define('C', Items.WHEAT)
         .define('E', Items.EGG)
         .pattern("AAA")
         .pattern("BEB")
         .pattern("CCC")
         .unlockedBy(this.hasItem, has(Items.EGG))
         .save(this.con, BlueSkies.find("cake_compat"));
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SkiesItems.warding_pearl)
         .define('F', SkiesItemTags.FALSITE_NUGGETS)
         .define('P', SkiesItems.pearl)
         .define('S', SkiesItems.soul_fragment)
         .pattern("FFF")
         .pattern("SPS")
         .pattern("FFF")
         .unlockedBy(this.hasItem, has(SkiesItems.pearl))
         .save(this.con);
      this.simple2x2(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.snowcap_pinhead, SkiesBlocks.snowcap_mushroom_block);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.food_prep_table)
         .define('L', ItemTags.LOGS)
         .define(
            'S', Ingredient.of(new ItemLike[]{Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, SkiesBlocks.turquoise_stone_slab, SkiesBlocks.lunar_stone_slab})
         )
         .pattern("SSS")
         .pattern("L L")
         .pattern("L L")
         .unlockedBy(this.hasItem, has(Items.FURNACE))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.star_emitter)
         .define('F', SkiesItemTags.FALSITE)
         .define('P', ItemTags.WOODEN_SLABS)
         .define('S', SkiesBlocks.star_flare)
         .pattern(" S ")
         .pattern(" F ")
         .pattern("PPP")
         .unlockedBy(this.hasItem, has(SkiesItemTags.FALSITE))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.trough)
         .define('#', ItemTags.WOODEN_SLABS)
         .pattern("# #")
         .pattern("###")
         .unlockedBy(this.hasItem, has(ItemTags.WOODEN_SLABS))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.tool_box)
         .define('V', SkiesItemTags.VENTIUM)
         .define('P', ItemTags.PLANKS)
         .pattern("VV ")
         .pattern("PP ")
         .pattern("PP ")
         .unlockedBy(this.hasItem, has(SkiesItemTags.VENTIUM))
         .save(this.con);
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.bag_of_spoils)
         .define('F', SkiesItems.fox_pelt)
         .define('S', net.neoforged.neoforge.common.Tags.Items.STRING)
         .pattern(" S ")
         .pattern("F F")
         .pattern("FFF")
         .unlockedBy(this.hasItem, has(SkiesItems.fox_pelt))
         .save(this.con);
      this.smoking(RecipeCategory.FOOD, SkiesItems.venison, SkiesItems.cooked_venison, 0.35F);
      this.smoking(RecipeCategory.FOOD, SkiesItems.carabeef, SkiesItems.cooked_carabeef, 0.35F);
      this.smoking(RecipeCategory.FOOD, SkiesItems.monitor_tail, SkiesItems.cooked_monitor_tail, 0.35F);
      this.smoking(RecipeCategory.FOOD, SkiesItems.grittle_flatfish, SkiesItems.cooked_grittle_flatfish, 0.35F);
      this.smoking(RecipeCategory.FOOD, SkiesItems.municipal_monkfish, SkiesItems.cooked_municipal_monkfish, 0.35F);
      this.smoking(RecipeCategory.FOOD, SkiesItems.charscale_moki, SkiesItems.cooked_charscale_moki, 0.35F);
      this.smoking(RecipeCategory.FOOD, SkiesItems.horizofin_tunid, SkiesItems.cooked_horizofin_tunid, 0.35F);
      this.groupBlasting(
         RecipeCategory.MISC, SkiesBlocks.moonstone, 0.2F, "moonstone", SkiesBlocks.everbright_moonstone_ore, SkiesBlocks.everdawn_moonstone_ore
      );
      this.groupBlasting(RecipeCategory.MISC, SkiesItems.pyrope_gem, 0.7F, "pyrope_gem", SkiesBlocks.everbright_pyrope_ore, SkiesBlocks.everdawn_pyrope_ore);
      this.groupBlasting(
         RecipeCategory.MISC, SkiesItems.diopside_gem, 0.7F, "diopside_gem", SkiesBlocks.everbright_diopside_ore, SkiesBlocks.everdawn_diopside_ore
      );
      this.groupBlasting(
         RecipeCategory.MISC, SkiesItems.aquite, 0.8F, "aquite_gem", SkiesBlocks.everbright_aquite_ore, SkiesBlocks.everdawn_aquite_ore, SkiesItems.raw_aquite
      );
      this.groupBlasting(
         RecipeCategory.MISC,
         SkiesItems.charoite,
         0.9F,
         "charoite",
         SkiesBlocks.everbright_charoite_ore,
         SkiesBlocks.everdawn_charoite_ore,
         SkiesItems.raw_charoite
      );
      this.groupBlasting(RecipeCategory.MISC, SkiesItems.ventium_ingot, 0.9F, "ventium_ingot", SkiesBlocks.ventium_ore, SkiesItems.raw_ventium);
      this.groupBlasting(RecipeCategory.MISC, SkiesItems.falsite_ingot, 0.85F, "falsite_ingot", SkiesBlocks.falsite_ore, SkiesItems.raw_falsite);
      this.groupBlasting(RecipeCategory.MISC, SkiesItems.horizonite_ingot, 1.0F, "horizonite_ingot", SkiesBlocks.horizonite_ore, SkiesItems.raw_horizonite);
      this.groupBlasting(RecipeCategory.MISC, Items.EMERALD, 1.0F, "emerald", SkiesBlocks.everbright_emerald_ore, SkiesBlocks.everdawn_emerald_ore);
      this.groupBlasting(
         RecipeCategory.MISC,
         SkiesItems.horizonite_nugget,
         0.1F,
         "horizonite_nugget_from_item",
         SkiesItems.horizonite_pickaxe,
         SkiesItems.horizonite_axe,
         SkiesItems.horizonite_shovel,
         SkiesItems.horizonite_hoe,
         SkiesItems.horizonite_sword,
         SkiesItems.horizonite_helmet,
         SkiesItems.horizonite_chestplate,
         SkiesItems.horizonite_leggings,
         SkiesItems.horizonite_boots
      );
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.lunar_cobblestone, SkiesBlocks.lunar_stone, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.lunar_stonebrick, SkiesBlocks.cracked_lunar_stonebrick, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.turquoise_cobblestone, SkiesBlocks.turquoise_stone, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.turquoise_stonebrick, SkiesBlocks.cracked_turquoise_stonebrick, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystal_sandstone, SkiesBlocks.smooth_crystal_sandstone, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.crystal_sand, SkiesBlocks.crystal_glass, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.midnight_sandstone, SkiesBlocks.smooth_midnight_sandstone, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.midnight_sand, SkiesBlocks.midnight_glass, 0.1F);
      this.cooking(RecipeCategory.BUILDING_BLOCKS, SkiesBlocks.brumble_block, SkiesBlocks.polished_brumble, 0.1F);
      this.smithingTrim(SkiesItems.banished_trim, null);
      this.smithingTrim(SkiesItems.thwarted_trim, null);
      this.smithingTrim(SkiesItems.regrowth_trim, null);
      this.smithingTrim(SkiesItems.toxic_trim, null);
   }

   private void smithingTrim(ItemLike templateItem, ItemLike copyItem) {
      SmithingTrimRecipeBuilder.smithingTrim(
            Ingredient.of(new ItemLike[]{templateItem}), Ingredient.of(ItemTags.TRIMMABLE_ARMOR), Ingredient.of(ItemTags.TRIM_MATERIALS), RecipeCategory.MISC
         )
         .unlocks("has_smithing_trim_template", has(templateItem))
         .save(this.con, BlueSkies.locate(getItemName(templateItem) + "_smithing_trim"));
      if (copyItem != null) {
         copySmithingTemplate(this.con, templateItem, copyItem);
      }
   }

   private void smithingUpgrade(ItemLike templateItem, Ingredient base, Ingredient upgradeItem, ItemLike result, ItemLike copyItem) {
      SmithingTransformRecipeBuilder.smithing(Ingredient.of(new ItemLike[]{templateItem}), base, upgradeItem, RecipeCategory.MISC, result.asItem())
         .unlocks("has_item", has(templateItem))
         .save(this.con, BlueSkies.locate(getItemName(templateItem) + "_to_" + getItemName(result) + "_smithing"));
      if (copyItem != null) {
         copySmithingTemplate(this.con, templateItem, copyItem);
      }
   }

   private void simple2x2(RecipeCategory category, ItemLike input, ItemLike output, int amount) {
      ShapedRecipeBuilder.shaped(category, output, amount).define('#', input).pattern("##").pattern("##").unlockedBy(this.hasItem, has(input)).save(this.con);
   }

   private void simple2x2(RecipeCategory category, ItemLike input, ItemLike output) {
      this.simple2x2(category, input, output, 1);
   }

   private void simple3x3(RecipeCategory category, ItemLike input, ItemLike output, int amount) {
      ShapedRecipeBuilder.shaped(category, output, amount)
         .define('#', input)
         .pattern("###")
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(input))
         .save(this.con);
   }

   private void simple3x3(RecipeCategory category, ItemLike input, ItemLike output) {
      this.simple3x3(category, input, output, 1);
   }

   private void slabsStairs(ItemLike block, ItemLike slab, ItemLike stair) {
      this.slabs(block, slab).save(this.con);
      this.stairs(block, stair).save(this.con);
   }

   private void slabsStairsWalls(ItemLike block, ItemLike slab, ItemLike stair, ItemLike wall) {
      this.slabsStairs(block, slab, stair);
      this.walls(block, wall);
   }

   private void slabsStairs(ItemLike block, ItemLike slab, ItemLike stair, boolean withStoneCutting) {
      this.slabsStairs(block, slab, stair);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, block, ImmutableList.of(slab, stair));
   }

   private void slabsStairsWalls(ItemLike block, ItemLike slab, ItemLike stair, ItemLike wall, boolean withStoneCutting) {
      this.slabsStairsWalls(block, slab, stair, wall);
      this.stoneCutting(RecipeCategory.BUILDING_BLOCKS, block, ImmutableList.of(slab, stair, wall));
   }

   private ShapedRecipeBuilder slabs(ItemLike ingredient, ItemLike slab) {
      return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
         .define('#', ingredient)
         .pattern("###")
         .unlockedBy(this.hasItem, has(ingredient));
   }

   private ShapedRecipeBuilder stairs(ItemLike ingredient, ItemLike stair) {
      return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stair, 4)
         .define('#', ingredient)
         .pattern("#  ")
         .pattern("## ")
         .pattern("###")
         .unlockedBy(this.hasItem, has(ingredient));
   }

   private void walls(ItemLike ingredient, ItemLike wall) {
      this.walls(ingredient, wall, null);
   }

   private void walls(ItemLike ingredient, ItemLike wall, String suffix) {
      ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(wall.asItem());
      ResourceLocation name = ResourceLocation.fromNamespaceAndPath(itemName.getNamespace(), itemName.getPath() + (suffix != null ? "_" + suffix : ""));
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6)
         .define('#', ingredient)
         .pattern("###")
         .pattern("###")
         .unlockedBy(this.hasItem, has(ingredient))
         .save(this.con, name);
   }

   private void fencesGates(ItemLike plank, ItemLike fence, ItemLike gate) {
      this.fences(plank, fence);
      this.gates(plank, gate);
   }

   private void fences(ItemLike plank, ItemLike fence) {
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fence, 3)
         .define('P', plank)
         .define('S', Ingredient.of(net.neoforged.neoforge.common.Tags.Items.RODS_WOODEN))
         .pattern("PSP")
         .pattern("PSP")
         .group("wooden_fence")
         .unlockedBy(this.hasItem, has(plank))
         .save(this.con);
   }

   private void gates(ItemLike plank, ItemLike gate) {
      ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, gate)
         .define('P', plank)
         .define('S', Ingredient.of(net.neoforged.neoforge.common.Tags.Items.RODS_WOODEN))
         .pattern("SPS")
         .pattern("SPS")
         .group("wooden_fence_gate")
         .unlockedBy(this.hasItem, has(plank))
         .save(this.con);
   }

   private void stoneCutting(RecipeCategory category, ItemLike ingredient, ImmutableList<ItemLike> results) {
      results.forEach(
         result -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[]{ingredient}), category, result, result instanceof SlabBlock ? 2 : 1)
            .unlockedBy(this.hasItem, has(ingredient))
            .save(
               this.con,
               BlueSkies.find(
                  BuiltInRegistries.ITEM.getKey(result.asItem()).getPath() + "_stonecutting_" + BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath()
               )
            )
      );
   }

   private void cooking(RecipeCategory category, ItemLike ingredient, ItemLike result, float exp) {
      this.cooking(category, ingredient, result, exp, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new);
   }

   private <T extends AbstractCookingRecipe> void cooking(
      RecipeCategory category, ItemLike ingredient, ItemLike result, float exp, int time, RecipeSerializer<T> type, Factory<T> factory
   ) {
      SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{ingredient}), category, result, exp, time, type, factory)
         .unlockedBy(this.hasItem, has(ingredient))
         .save(
            this.con,
            BlueSkies.find(
               BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()
                  + "_from_"
                  + BuiltInRegistries.RECIPE_SERIALIZER.getKey(type).getPath()
                  + "_"
                  + BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath()
            )
         );
   }

   private <T extends AbstractCookingRecipe> void cooking(
      RecipeCategory category, ItemLike ingredient, ItemLike result, float exp, int time, RecipeSerializer<T> type, Factory<T> factory, String group
   ) {
      SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{ingredient}), category, result, exp, time, type, factory)
         .unlockedBy(this.hasItem, has(ingredient))
         .group(group)
         .save(
            this.con,
            BlueSkies.find(
               BuiltInRegistries.ITEM.getKey(result.asItem()).getPath()
                  + "_from_"
                  + BuiltInRegistries.RECIPE_SERIALIZER.getKey(type).getPath()
                  + "_"
                  + BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath()
            )
         );
   }

   private void smoking(RecipeCategory category, TagKey<Item> ingredient, ItemLike result, float exp) {
      this.cooking(category, ingredient, result, exp);
      this.cooking(category, ingredient, result, exp, 100, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new);
      this.cooking(category, ingredient, result, exp, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new);
   }

   private void blasting(RecipeCategory category, TagKey<Item> ingredient, ItemLike result, float exp) {
      this.cooking(category, ingredient, result, exp);
      this.cooking(category, ingredient, result, exp, 100, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new);
   }

   private void groupBlasting(RecipeCategory category, ItemLike result, float exp, String group, ItemLike... ingredients) {
      for (ItemLike ingredient : ingredients) {
         this.cooking(category, ingredient, result, exp, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, group);
         this.cooking(category, ingredient, result, exp, 100, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, group);
      }
   }

   private void cooking(RecipeCategory category, TagKey<Item> ingredient, ItemLike result, float exp) {
      this.cooking(category, ingredient, result, exp, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new);
   }

   private <T extends AbstractCookingRecipe> void cooking(
      RecipeCategory category, TagKey<Item> ingredient, ItemLike result, float exp, int time, RecipeSerializer<T> type, Factory<T> factory
   ) {
      SimpleCookingRecipeBuilder.generic(Ingredient.of(ingredient), category, result, exp, time, type, factory)
         .unlockedBy(this.hasItem, has(ingredient))
         .save(
            this.con,
            BlueSkies.find(BuiltInRegistries.ITEM.getKey(result.asItem()).getPath() + "_from_" + BuiltInRegistries.RECIPE_SERIALIZER.getKey(type).getPath())
         );
   }

   private void smoking(RecipeCategory category, ItemLike ingredient, ItemLike result, float exp) {
      this.cooking(category, ingredient, result, exp, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new);
      this.cooking(category, ingredient, result, exp, 100, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new);
      this.cooking(category, ingredient, result, exp, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new);
   }

   private void blasting(RecipeCategory category, ItemLike ingredient, ItemLike result, float exp) {
      this.cooking(category, ingredient, result, exp, 200, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new);
      this.cooking(category, ingredient, result, exp, 100, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new);
   }

   protected record OreMap(ItemLike material, ItemLike block, ItemLike chestplate, ItemLike leggings, ItemLike boots, ItemLike helmet) {
   }

   protected record WoodMap(
      TagKey<Item> logTag,
      ItemLike log,
      ItemLike wood,
      ItemLike strippedLog,
      ItemLike strippedWood,
      ItemLike plank,
      ItemLike slab,
      ItemLike stair,
      ItemLike craftingTable,
      ItemLike bookshelf,
      ItemLike chest,
      ItemLike pressurePlate,
      ItemLike button,
      ItemLike door,
      ItemLike trapdoor,
      ItemLike stick,
      ItemLike ladder,
      ItemLike fence,
      ItemLike gate,
      ItemLike sign,
      ItemLike hangingSign
   ) {
   }
}
