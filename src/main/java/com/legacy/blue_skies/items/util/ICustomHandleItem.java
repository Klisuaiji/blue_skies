package com.legacy.blue_skies.items.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

public interface ICustomHandleItem {
   default boolean isHandleCompatible(ItemStack stack) {
      if (stack.getItem() instanceof TieredItem) {
         Tier tier = ((TieredItem)stack.getItem()).getTier();
         return tier instanceof SkiesItemTier ? ((SkiesItemTier)tier).isCompatWithHandle() : false;
      } else {
         return false;
      }
   }
}
