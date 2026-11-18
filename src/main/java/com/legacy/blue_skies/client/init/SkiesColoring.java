package com.legacy.blue_skies.client.init;

import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.items.util.FoodPrepUtils;
import com.legacy.blue_skies.items.util.PreparedFood;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.structure_gel.api.registry.RegistryHelper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Item;

@EventBusSubscriber(
   modid = "blue_skies",
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class SkiesColoring {
   @SubscribeEvent
   public static void registerBlockColors(Block event) {
      Minecraft mc = Minecraft.getInstance();
      event.register(
         (state, world, pos, index) -> {
            Biome biome;
            if (mc.level != null
               && pos != null
               && (
                  mc.level.dimension().equals(SkiesDimensions.everbrightKey())
                     || (biome = (Biome)mc.level.getBiome(pos).value()) != null
                        && RegistryHelper.isInTag(mc.level.registryAccess().registryOrThrow(Registries.BIOME), SkiesBiomeTags.EVERBRIGHT, biome)
               )) {
               return world != null ? BiomeColors.getAverageGrassColor(world, pos) : 16777215;
            } else {
               return 16777215;
            }
         },
         new net.minecraft.world.level.block.Block[]{
            SkiesBlocks.turquoise_grass_block,
            SkiesBlocks.short_turquoise_grass,
            SkiesBlocks.tall_turquoise_grass,
            SkiesBlocks.brittlebush,
            SkiesBlocks.potted_brittlebush,
            SkiesBlocks.chillweed,
            SkiesBlocks.potted_chillweed
         }
      );
      event.register(
         (state, world, pos, index) -> {
            Biome biome;
            if (mc.level != null
               && pos != null
               && (
                  mc.level.dimension().equals(SkiesDimensions.everdawnKey())
                     || (biome = (Biome)mc.level.getBiome(pos).value()) != null
                        && RegistryHelper.isInTag(mc.level.registryAccess().registryOrThrow(Registries.BIOME), SkiesBiomeTags.EVERDAWN, biome)
               )) {
               return world != null ? BiomeColors.getAverageGrassColor(world, pos) : 14843647;
            } else {
               return 14843647;
            }
         },
         new net.minecraft.world.level.block.Block[]{
            SkiesBlocks.lunar_grass_block,
            SkiesBlocks.short_lunar_grass,
            SkiesBlocks.tall_lunar_grass,
            SkiesBlocks.muckweed,
            SkiesBlocks.potted_muckweed,
            SkiesBlocks.lucentroot,
            SkiesBlocks.potted_lucentroot
         }
      );
      event.register((state, world, pos, index) -> 9269656, new net.minecraft.world.level.block.Block[]{SkiesBlocks.moonlit_water_lily});
   }

   @SubscribeEvent
   public static void registerItemColors(Item event) {
      event.register(
         (itemStack, index) -> index == 0 ? ToolUtils.getHandleColor(itemStack) : -1, SkiesItems.coloredTools.toArray(new net.minecraft.world.item.Item[0])
      );
      event.register(
         (itemStack, index) -> index == 0 ? 16777215 : -1,
         new ItemLike[]{
            SkiesBlocks.turquoise_grass_block,
            SkiesBlocks.short_turquoise_grass,
            SkiesBlocks.tall_turquoise_grass,
            SkiesBlocks.brittlebush,
            SkiesBlocks.chillweed
         }
      );
      event.register(
         (itemStack, index) -> index == 0 ? 14843647 : -1,
         new ItemLike[]{
            SkiesBlocks.lunar_grass_block, SkiesBlocks.short_lunar_grass, SkiesBlocks.tall_lunar_grass, SkiesBlocks.muckweed, SkiesBlocks.lucentroot
         }
      );
      event.register((itemStack, index) -> index == 0 ? 9269656 : -1, new ItemLike[]{SkiesItems.moonlit_water_lily});
      event.register((itemStack, index) -> {
         PreparedFood preparedFood = FoodPrepUtils.deserialize(itemStack.getTag());
         List<ItemStack> ingredients = preparedFood.getIngredients();
         if (index == 1) {
            return ingredients.size() > 0 ? FoodPrepUtils.FoodColor.get(ingredients.get(0).getItem()).color : FoodPrepUtils.FoodColor.BEEF.color;
         } else if (index == 2) {
            return ingredients.size() > 1 ? FoodPrepUtils.FoodColor.get(ingredients.get(1).getItem()).color : FoodPrepUtils.FoodColor.LETTUCE.color;
         } else if (index == 3) {
            List<MobEffectInstance> effects = preparedFood.getEffects();
            if (effects.size() > 0) {
               return effects.get(0).getEffect().getColor();
            } else {
               return ingredients.size() > 2 ? FoodPrepUtils.FoodColor.get(ingredients.get(2).getItem()).color : FoodPrepUtils.FoodColor.CHEESE.color;
            }
         } else {
            return -1;
         }
      }, new ItemLike[]{SkiesItems.prepared_food});
   }
}
