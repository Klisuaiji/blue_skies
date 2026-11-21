package com.legacy.blue_skies.items.util;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.ToolHandleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class ToolUtils {
   public static final String STICK_KEY = "Stick";

   public static float getAttackSpeed(float attackSpeed, ItemStack stack) {
      return attackSpeed * getStickType(stack).getAttackSpeedModifier();
   }

   public static float getAttackSpeed(float attackSpeed, Item stick) {
      return attackSpeed * BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(stick).getAttackSpeedModifier();
   }

   public static int getMaxDurability(Tier tier, ItemStack stack) {
      return Math.round(tier.getUses() * getStickType(stack).getDurabilityModifier());
   }

   public static int getMaxDurability(int baseDurability, Item stick) {
      return Math.round(baseDurability * BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(stick).getDurabilityModifier());
   }

   public static int getHandleColor(ItemStack stack) {
      return getStickType(stack).getColor();
   }

   public static float getProjectileDamage(float damage, Item stick) {
      return damage * BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(stick).getProjectileDamageModifier();
   }

   public static ToolHandleType getStickType(ItemStack stack) {
      CompoundTag tag = LegacyNbt.get(stack);
      if (tag != null && tag.contains("Stick")) {
         String str = tag.getString("Stick");
         if (LegacyRL.isValid(str)) {
            return BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(ResourceLocation.parse(str));
         }
      }

      return ToolHandleType.DEFAULT;
   }

   public static ItemStack setStickType(ItemStack stack, Item stick) {
      LegacyNbt.getOrCreate(stack).putString("Stick", BlueSkiesData.TOOL_HANDLE_TYPES.getHandle(stick).getName());
      return stack;
   }
}
