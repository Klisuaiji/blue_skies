package com.legacy.blue_skies.items.util;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesItems;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBlock;

public class FoodPrepUtils {
   public static final String INGREDIENTS_KEY = "Ingredients";
   public static final String TOTAL_HUNGER_KEY = "Total_Hunger";
   public static final String SATURATION_KEY = "Saturation";
   public static final String USES_KEY = "Uses";
   public static final String MAX_USES_KEY = "MaxUses";
   public static final String EFFECTS_KEY = "Effects";

   public static void serialize(CompoundTag itemNbt, PreparedFood preparedFood) {
      ListTag ingredients = new ListTag();
      preparedFood.getIngredients().forEach(i -> ingredients.add(i.save(new CompoundTag())));
      itemNbt.put("Ingredients", ingredients);
      itemNbt.putInt("Total_Hunger", preparedFood.getTotalHunger());
      itemNbt.putFloat("Saturation", preparedFood.getSaturation());
      itemNbt.putInt("Uses", preparedFood.getUses());
      itemNbt.putInt("MaxUses", preparedFood.getMaxUses());
      ListTag effects = new ListTag();
      preparedFood.getEffects().forEach(e -> effects.add(e.save(new CompoundTag())));
      itemNbt.put("Effects", effects);
   }

   public static PreparedFood deserialize(CompoundTag itemNbt) {
      List<ItemStack> ingredients = new ArrayList<>();
      List<MobEffectInstance> effects = new ArrayList<>();
      int totalHunger = 1;
      int uses = 1;
      int maxUses = 1;
      float saturation = 0.0F;
      if (itemNbt != null) {
         if (itemNbt.contains("Ingredients")) {
            itemNbt.getList("Ingredients", 10).forEach(nbt -> {
               ItemStack ingredient = null;

               try {
                  ingredient = ItemStack.of((CompoundTag)nbt);
               } catch (Exception e) {
                  BlueSkies.LOGGER.warn(e);
               }

               if (ingredient != null) {
                  ingredients.add(ingredient);
               }
            });
         }

         if (itemNbt.contains("Effects")) {
            itemNbt.getList("Effects", 10).forEach(nbt -> {
               MobEffectInstance effect = null;

               try {
                  effect = MobEffectInstance.load((CompoundTag)nbt);
               } catch (Exception e) {
                  BlueSkies.LOGGER.warn(e);
               }

               if (effect != null) {
                  effects.add(effect);
               }
            });
         }

         if (itemNbt.contains("Total_Hunger")) {
            totalHunger = itemNbt.getInt("Total_Hunger");
         }

         if (itemNbt.contains("Uses")) {
            uses = itemNbt.getInt("Uses");
         }

         if (itemNbt.contains("MaxUses")) {
            maxUses = itemNbt.getInt("MaxUses");
         }

         if (itemNbt.contains("Saturation")) {
            saturation = itemNbt.getFloat("Saturation");
         }
      }

      return new PreparedFood(ingredients, effects, totalHunger, uses, maxUses, saturation);
   }

   public static int getUses(ItemStack stack) {
      CompoundTag tag = LegacyNbt.getOrCreate(stack);
      return tag.contains("Uses") ? tag.getInt("Uses") : getMaxUses(stack);
   }

   public static int getMaxUses(ItemStack stack) {
      CompoundTag tag = LegacyNbt.getOrCreate(stack);
      return tag.contains("MaxUses") ? tag.getInt("MaxUses") : 1;
   }

   public static ItemStack createFoodFromItems(List<ItemLike> ingredients) {
      return createFood(ingredients.stream().map(item -> new ItemStack(item)).toList());
   }

   public static ItemStack createFood(Container inventory) {
      List<ItemStack> ingredients = new ArrayList<>();

      for (int i = 0; i < inventory.getContainerSize(); i++) {
         ItemStack slotStack = inventory.getItem(i);
         if (!slotStack.isEmpty()) {
            if (!isValidIngredient(slotStack)) {
               return ItemStack.EMPTY;
            }

            ingredients.add(slotStack);
         }
      }

      return createFood(ingredients);
   }

   public static ItemStack createFood(List<ItemStack> ingredients) {
      if (ingredients.size() < 3) {
         return ItemStack.EMPTY;
      }

      int i = 0;
      int p = 0;

      while (i < ingredients.size()) {
         if (ingredients.get(i).getItem() == Items.POTION) {
            p++;
         }

         if (p > 2) {
            return ItemStack.EMPTY;
         }

         i++;
      }

      i = getTotalHunger(ingredients);
      p = getMaxUses(i);
      ItemStack stack = new ItemStack(SkiesItems.prepared_food);
      serialize(LegacyNbt.getOrCreate(stack), new PreparedFood(ingredients, getEffects(ingredients, p), i, p, p, getSaturation(ingredients)));
      return stack;
   }

   public static boolean isValidIngredient(ItemStack stack) {
      Item item = stack.getItem();
      return item.isEdible() || item == Items.POTION || item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof FlowerBlock;
   }

   public static void causeInstability(PreparedFood preparedFood, LivingEntity entity) {
      int potionCount = 0;

      for (ItemStack item : preparedFood.getIngredients()) {
         if (item.getItem() == Items.POTION) {
            potionCount++;
         }
      }

      if (!entity.level().isClientSide() && potionCount >= 2) {
         List<MobEffectInstance> effects = new ArrayList<>();
         if (entity.getRandom().nextFloat() < 0.333) {
            effects.add(new MobEffectInstance(MobEffects.POISON, 180, entity.getRandom().nextBoolean() ? 1 : 0));
         } else {
            effects.add(
               new MobEffectInstance(
                  entity.getRandom().nextBoolean() ? MobEffects.CONFUSION : MobEffects.MOVEMENT_SLOWDOWN, 150, entity.getRandom().nextBoolean() ? 1 : 0
               )
            );
         }

         if (entity.getRandom().nextBoolean() && potionCount >= 3) {
            if (entity.getRandom().nextBoolean()) {
               effects.add(new MobEffectInstance(MobEffects.HARM, 1, entity.getRandom().nextBoolean() ? 1 : 0));
            } else {
               effects.add(new MobEffectInstance(MobEffects.WITHER, 200, entity.getRandom().nextBoolean() ? 1 : 0));
            }
         }

         effects.forEach(entity::addEffect);
      }
   }

   private static int getTotalHunger(List<ItemStack> ingredients) {
      double hunger = 0.0;
      List<Item> usedIngredients = new ArrayList<>();

      for (ItemStack ingredient : ingredients) {
         if (ingredient.isEdible()) {
            hunger += ingredient.getFoodProperties(null).getNutrition() + (!usedIngredients.contains(ingredient.getItem()) ? 2.5 : 0.0);
            usedIngredients.add(ingredient.getItem());
         }
      }

      return (int)Math.max(1.0, hunger);
   }

   private static int getMaxUses(int totalHunger) {
      return Math.max(1, Math.round(totalHunger / 6.0F));
   }

   private static float getSaturation(List<ItemStack> ingredients) {
      float saturation = 0.0F;
      float count = 0.0F;

      for (ItemStack ingredient : ingredients) {
         if (ingredient.isEdible()) {
            saturation += ingredient.getFoodProperties(null).getSaturationModifier();
            count++;
         }
      }

      return Math.max(0.1F, count == 0.0F ? 0.1F : saturation / count);
   }

   private static List<MobEffectInstance> getEffects(List<ItemStack> ingredients, int maxUses) {
      Map<MobEffect, MobEffectInstance> effectMap = new LinkedHashMap<>();
      boolean brewberryFlag = false;

      for (ItemStack ingredient : ingredients) {
         if (ingredient.getItem() instanceof BlockItem && ((BlockItem)ingredient.getItem()).getBlock() instanceof FlowerBlock) {
            FlowerBlock flower = (FlowerBlock)((BlockItem)ingredient.getItem()).getBlock();
            flower.getSuspiciousEffects().forEach(flowerEffect -> addEffect(effectMap, flowerEffect.createEffectInstance()));
         }

         if (ingredient.isEdible()) {
            for (MobEffectInstance effect : (List)ingredient.getFoodProperties(null).getEffects().stream().map(Pair::getFirst).collect(Collectors.toList())) {
               addEffect(effectMap, effect);
            }
         }

         if (ingredient.getItem() == Items.POTION) {
            PotionUtils.getAllEffects(LegacyNbt.getOrCreate(ingredient)).forEach(e -> addEffect(effectMap, e));
         }

         if (ingredient.getItem() == SkiesItems.brewberry) {
            brewberryFlag = true;
         }
      }

      List<MobEffectInstance> effects = new ArrayList<>();

      for (Entry<MobEffect, MobEffectInstance> entry : effectMap.entrySet()) {
         MobEffectInstance effect = entry.getValue();
         if (!(effect.getEffect() instanceof InstantenousMobEffect) || effect.getEffect() == MobEffects.SATURATION) {
            float time = (float)effect.getDuration() / maxUses;
            time *= brewberryFlag ? 1.4F : 1.1F;
            effects.add(new MobEffectInstance(effect.getEffect(), Math.round(time), effect.getAmplifier()));
         }
      }

      return effects;
   }

   private static void addEffect(Map<MobEffect, MobEffectInstance> map, MobEffectInstance newEffect) {
      if (map.containsKey(newEffect.getEffect())) {
         MobEffectInstance oldEffect = map.get(newEffect.getEffect());
         if (newEffect.getAmplifier() > oldEffect.getAmplifier()) {
            int duration = (int)(newEffect.getDuration() + oldEffect.getDuration() * ((float)(oldEffect.getAmplifier() + 1) / (newEffect.getAmplifier() + 1)));
            map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), duration, newEffect.getAmplifier()));
         } else if (newEffect.getAmplifier() < oldEffect.getAmplifier()) {
            int duration = (int)(oldEffect.getDuration() + newEffect.getDuration() * ((float)(newEffect.getAmplifier() + 1) / (oldEffect.getAmplifier() + 1)));
            map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), duration, oldEffect.getAmplifier()));
         } else {
            int duration = oldEffect.getDuration() + newEffect.getDuration();
            map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), duration, newEffect.getAmplifier()));
         }
      } else {
         map.put(newEffect.getEffect(), new MobEffectInstance(newEffect.getEffect(), newEffect.getDuration(), newEffect.getAmplifier()));
      }
   }

   public enum FoodColor {
      BEEF(8079412, Items.BEEF, Items.COOKED_BEEF, Items.MUTTON, Items.COOKED_MUTTON, Items.ROTTEN_FLESH),
      LETTUCE(6991719),
      CHEESE(15390090, Items.PUFFERFISH),
      APPLE(15013926, Items.APPLE, Items.MELON_SLICE, SkiesItems.comet_berries),
      BREAD(10449954, Items.BREAD, Items.COOKIE),
      PORK(11638883, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.COD, Items.COOKED_COD, SkiesItems.monitor_tail, SkiesItems.cooked_monitor_tail),
      SALMON(11227435, Items.SALMON, Items.COOKED_SALMON),
      KELP(4472114, Items.KELP),
      CHICKEN(12811088, Items.CHICKEN, Items.COOKED_CHICKEN, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, Items.MUSHROOM_STEW, Items.SUSPICIOUS_STEW),
      CARROT(14712350, Items.CARROT, Items.TROPICAL_FISH),
      POTATO(12489280, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.PUMPKIN_PIE, SkiesItems.solnut),
      BEET(11353666, Items.BEETROOT, Items.BEETROOT_SOUP, SkiesItems.fiery_beans),
      SWEET_BERRY(10032151, Items.SWEET_BERRIES, SkiesItems.brewberry),
      WINTER_LEAF(7193708, SkiesItems.winter_leaves, SkiesItems.municipal_monkfish, SkiesItems.cooked_municipal_monkfish),
      CRYO_ROOT(7323079, SkiesItems.cryo_root),
      SCALEFRUIT(13919381, Items.SPIDER_EYE, SkiesItems.scalefruit, SkiesItems.pink_brewberry),
      PINE_FRUIT(4924430, SkiesItems.pine_fruit),
      CRESCENT_FRUIT(13453571, SkiesItems.crescent_fruit),
      CARABEEF(3490920, SkiesItems.carabeef, SkiesItems.cooked_carabeef, SkiesItems.venison, SkiesItems.cooked_venison),
      GRITTLE_FLATFISH(5453392, SkiesItems.grittle_flatfish, SkiesItems.cooked_grittle_flatfish),
      CHARSCALE(14978148, SkiesItems.charscale_moki, SkiesItems.cooked_charscale_moki),
      PEPPER(8657439, SkiesItems.fiery_beans),
      BACKBERRY(3945297, SkiesItems.black_brewberry),
      CHORUS(9792434, Items.CHORUS_FRUIT),
      GOLDEN(
         15577123,
         Items.GOLDEN_APPLE,
         Items.ENCHANTED_GOLDEN_APPLE,
         Items.GOLDEN_CARROT,
         Items.HONEY_BOTTLE,
         Items.GLOW_BERRIES,
         SkiesItems.horizofin_tunid,
         SkiesItems.cooked_horizofin_tunid
      );

      private static final HashMap<Item, FoodPrepUtils.FoodColor> COLORS = new HashMap<>();
      public final int color;
      public final Set<ItemLike> foods;

      FoodColor(int color, ItemLike... foods) {
         this.color = color;
         this.foods = Set.of(foods);
      }

      public static FoodPrepUtils.FoodColor get(Item item) {
         return COLORS.containsKey(item) ? COLORS.get(item) : BEEF;
      }

      public static void setupColors() {
         COLORS.clear();

         label35:
         for (Item item : BuiltInRegistries.ITEM) {
            try {
               if (FoodPrepUtils.isValidIngredient(new ItemStack(item))) {
                  for (FoodPrepUtils.FoodColor color : values()) {
                     if (color.foods.contains(item)) {
                        COLORS.put(item, color);
                        continue label35;
                     }
                  }

                  COLORS.put(item, values()[new Random(BuiltInRegistries.ITEM.getKey(item).hashCode()).nextInt(values().length)]);
               }
            } catch (Exception var6) {
            }
         }
      }
   }
}
