package com.legacy.blue_skies.crafting.toolbox;

import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.items.util.ICustomHandleItem;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.items.util.ToolUtils;
import net.minecraft.world.item.ItemStack;

public class ModifyToolHandleRecipe extends ToolboxRecipe {
   @Override
   public boolean matches(ItemStack inputItem, ItemStack modifierItem) {
      return inputItem.getItem() instanceof ICustomHandleItem
         && ((ICustomHandleItem)inputItem.getItem()).isHandleCompatible(inputItem)
         && BlueSkiesData.TOOL_HANDLE_TYPES.hasHandle(modifierItem.getItem());
   }

   @Override
   public ItemStack getResult(ItemStack inputItem, ItemStack modifierItem) {
      ItemStack output = ToolUtils.setStickType(inputItem.copy(), modifierItem.getItem());
      if (output.getDamageValue() >= output.getMaxDamage() - 1) {
         output.setDamageValue(output.getMaxDamage() - 1);
      }

      if (output.getItem() instanceof IFalsiteItem && IFalsiteItem.getFalsiteUses(output) > IFalsiteItem.getMaxFalsiteUses(output)) {
         IFalsiteItem.setFalsiteUses(output, IFalsiteItem.getMaxFalsiteUses(output));
      }

      return output;
   }
}
