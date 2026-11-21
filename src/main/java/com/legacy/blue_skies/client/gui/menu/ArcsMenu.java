package com.legacy.blue_skies.client.gui.menu;

import com.google.common.collect.ImmutableList;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.items.arcs.IArcItem;
import com.legacy.blue_skies.registries.SkiesContainers;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArcsMenu extends AbstractContainerMenu {
   public ArcsMenu(int id, Inventory playerInv, Player player) {
      super(SkiesContainers.ARC_INVENTORY, id);
      if (player != null && playerInv != null) {
         for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
         }

         for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
               this.addSlot(new Slot(playerInv, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));
            }
         }

         SkiesPlayer.ifPresent(player, skiesPlayer -> {
            List<Pair<Integer, Integer>> slotPoses = ImmutableList.of(Pair.of(109, 18), Pair.of(109, 48), Pair.of(88, 33), Pair.of(130, 33));

            for (int i = 0; i < 4; i++) {
               this.addSlot(new ArcSlot(skiesPlayer.getArcInventory(), player, i, slotPoses.get(i)));
            }
         });
      }
   }

   public void removed(Player player) {
      super.removed(player);
   }

   public boolean stillValid(Player player) {
      return true;
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack clickedStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      ItemStack slotStack = slot.getItem();
      clickedStack = slotStack.copy();
      if (slot != null && slot.hasItem()) {
         if (index >= 0 && index < 9) {
            if (!this.moveItemStackTo(slotStack, 36, 40, false)) {
               if (!this.moveItemStackTo(slotStack, 9, 36, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (clickedStack.getItem() instanceof IArcItem) {
               ((IArcItem)clickedStack.getItem()).onEquip(clickedStack, player);
            }
         } else if (index >= 9 && index < 36) {
            if (!this.moveItemStackTo(slotStack, 36, 40, false)) {
               if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (clickedStack.getItem() instanceof IArcItem) {
               ((IArcItem)clickedStack.getItem()).onEquip(clickedStack, player);
            }
         } else if (index >= 36 && index < 43) {
            if (!this.moveItemStackTo(slotStack, 9, 36, false)) {
               if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
                  return ItemStack.EMPTY;
               }

               if (clickedStack.getItem() instanceof IArcItem) {
                  ((IArcItem)clickedStack.getItem()).onUnequip(clickedStack, player);
               }
            } else if (clickedStack.getItem() instanceof IArcItem) {
               ((IArcItem)clickedStack.getItem()).onUnequip(clickedStack, player);
            }
         }

         if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.getArcInventory().refreshArcs(player));
      return clickedStack;
   }
}
