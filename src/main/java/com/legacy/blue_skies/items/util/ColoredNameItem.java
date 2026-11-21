package com.legacy.blue_skies.items.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;

public class ColoredNameItem extends Item {
   private final ChatFormatting color;

   public ColoredNameItem(ChatFormatting color, Properties properties) {
      super(properties);
      this.color = color;
   }

   public Component getName(ItemStack stack) {
      return Component.empty().append(super.getName(stack)).withStyle(this.color);
   }
}
