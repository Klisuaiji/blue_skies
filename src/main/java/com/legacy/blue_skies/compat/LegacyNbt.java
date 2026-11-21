package com.legacy.blue_skies.compat;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** 1.20.4 ItemStack NBT API -> 1.21.1 data components.
 *  CustomData.of() retains the tag reference, so writes to the returned tag persist. */
public final class LegacyNbt {
   private LegacyNbt() {}

   public static CompoundTag get(ItemStack stack) {
      CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      return data.copyTag();
   }

   public static CompoundTag getOrCreate(ItemStack stack) {
      CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
      CompoundTag tag = data.copyTag();
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
      return tag;
   }

   public static boolean hasTag(ItemStack stack) {
      return stack.has(DataComponents.CUSTOM_DATA);
   }

   public static void setTag(ItemStack stack, CompoundTag tag) {
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
   }

   public static void removeTagKey(ItemStack stack, String key) {
      CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(key));
   }
}
