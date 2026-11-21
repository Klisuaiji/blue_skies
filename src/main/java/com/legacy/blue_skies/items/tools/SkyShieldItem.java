package com.legacy.blue_skies.items.tools;

import com.legacy.blue_skies.items.util.IFalsiteItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ItemAbilities;

public class SkyShieldItem extends ShieldItem implements IFalsiteItem {
   public SkyShieldItem(Properties builder) {
      super(builder);
   }

   public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
      return ItemAbilities.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
   }

   @Override
   public boolean isFalsiteCompatible(ItemStack stack) {
      return true;
   }

   public int getBarColor(ItemStack stack) {
      return this.falsiteBarColor(stack, super.getBarColor(stack));
   }

   public boolean isBarVisible(ItemStack stack) {
      return this.falsiteBarVisible(stack, super.isBarVisible(stack));
   }

   public int getBarWidth(ItemStack stack) {
      return this.falsiteBarWidth(stack, super.getBarWidth(stack));
   }
}
