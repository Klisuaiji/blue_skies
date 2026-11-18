package com.legacy.blue_skies.items.util;

import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.common.extensions.IItemExtension;

public interface IFalsiteItem extends IItemExtension {
   String FALSITE_KEY = "Falsite";

   default <T extends LivingEntity> int damageItemFalsite(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
      int falsiteUses = getFalsiteUses(stack);
      if (falsiteUses > 0) {
         falsiteUses -= amount;
         setFalsiteUses(stack, falsiteUses);
         if (falsiteUses <= 0) {
            onBroken.accept(entity);
            return Math.abs(falsiteUses);
         } else {
            return 0;
         }
      } else {
         return amount;
      }
   }

   static ItemStack applyFalsite(ItemStack stack) {
      stack.getOrCreateTag().putInt("Falsite", getMaxFalsiteUses(stack));
      return stack;
   }

   static int getMaxFalsiteUses(ItemStack stack) {
      return Math.max(Math.floorDiv(stack.getMaxDamage(), 5), 30);
   }

   static void setFalsiteUses(ItemStack stack, int value) {
      stack.getOrCreateTag().putInt("Falsite", Math.max(value, 0));
   }

   static int getFalsiteUses(ItemStack stack) {
      CompoundTag tag = stack.getTag();
      return tag != null ? tag.getInt("Falsite") : 0;
   }

   static boolean hasUses(ItemStack stack) {
      return getFalsiteUses(stack) > 0;
   }

   default boolean isFalsiteCompatible(ItemStack stack) {
      if (stack.getItem() instanceof TieredItem) {
         Tier tier = ((TieredItem)stack.getItem()).getTier();
         return tier instanceof SkiesItemTier ? ((SkiesItemTier)tier).isCompatWithFalsite() : false;
      } else if (stack.getItem() instanceof ArmorItem) {
         ArmorMaterial material = ((ArmorItem)stack.getItem()).getMaterial();
         return material instanceof SkiesArmorMaterial ? ((SkiesArmorMaterial)material).isCompatWithFalsite() : false;
      } else {
         return false;
      }
   }

   default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
      return this.isFalsiteCompatible(stack) ? this.damageItemFalsite(stack, amount, entity, onBroken) : amount;
   }

   default int falsiteBarColor(ItemStack stack, int vanillaColor) {
      return hasUses(stack) ? Mth.hsvToRgb(0.5F, 1.0F, 1.0F) : vanillaColor;
   }

   default boolean falsiteBarVisible(ItemStack stack, boolean vanillaCondition) {
      return hasUses(stack) || vanillaCondition;
   }

   default int falsiteBarWidth(ItemStack stack, int vanillaWidth) {
      if (hasUses(stack)) {
         float falsiteWidth = 1.0F - (getMaxFalsiteUses(stack) - getFalsiteUses(stack) * 13.0F) / getMaxFalsiteUses(stack);
         return Math.round(falsiteWidth);
      } else {
         return vanillaWidth;
      }
   }
}
