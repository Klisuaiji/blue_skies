package com.legacy.blue_skies.client.gui.menu;

import com.google.common.collect.Sets;
import com.legacy.blue_skies.capability.ArcInventory;
import com.legacy.blue_skies.items.arcs.IArcItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ArcSlot extends Slot {
   private final Player player;

   public ArcSlot(ArcInventory inventory, Player player, int index, Pair<Integer, Integer> xy) {
      super(inventory, index, (Integer)xy.getFirst(), (Integer)xy.getSecond());
      this.player = player;
   }

   public boolean mayPlace(ItemStack stack) {
      return this.container.hasAnyOf(Sets.newHashSet(new Item[]{stack.getItem()})) ? false : stack.getItem() instanceof IArcItem;
   }

   public void set(ItemStack stack) {
      super.set(stack);
      if (stack.getItem() instanceof IArcItem) {
         ((IArcItem)stack.getItem()).onEquip(stack, this.player);
      }
   }

   public void onTake(Player player, ItemStack stack) {
      this.setChanged();
      if (stack.getItem() instanceof IArcItem) {
         ((IArcItem)stack.getItem()).onUnequip(stack, player);
      }
   }
}
