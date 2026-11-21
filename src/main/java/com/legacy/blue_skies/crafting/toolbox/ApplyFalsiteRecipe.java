package com.legacy.blue_skies.crafting.toolbox;

import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import net.minecraft.world.item.ItemStack;

public class ApplyFalsiteRecipe extends ToolboxRecipe {
   @Override
   public boolean matches(ItemStack inputItem, ItemStack modifierItem) {
      return inputItem.getItem() instanceof IFalsiteItem
         && ((IFalsiteItem)inputItem.getItem()).isFalsiteCompatible(inputItem)
         && modifierItem.is(SkiesItemTags.FALSITE)
         && IFalsiteItem.getFalsiteUses(inputItem) < IFalsiteItem.getMaxFalsiteUses(inputItem);
   }

   @Override
   public ItemStack getResult(ItemStack inputItem, ItemStack modifierItem) {
      return IFalsiteItem.applyFalsite(inputItem.copy());
   }
}
