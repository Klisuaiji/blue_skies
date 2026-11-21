package com.legacy.blue_skies.crafting.toolbox;

import net.minecraft.world.item.ItemStack;

public abstract class ToolboxRecipe {
   public abstract boolean matches(ItemStack var1, ItemStack var2);

   public abstract ItemStack getResult(ItemStack var1, ItemStack var2);
}
