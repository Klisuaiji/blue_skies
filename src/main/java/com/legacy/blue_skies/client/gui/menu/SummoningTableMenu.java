package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.items.tools.weapons.SummoningTomeItem;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesContainers;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SummoningTableMenu extends AbstractContainerMenu {
   private final Container tableInventory = new SimpleContainer(2);
   private final ContainerLevelAccess worldPosCallable;

   public SummoningTableMenu(int id, Inventory playerInventory) {
      this(id, playerInventory, ContainerLevelAccess.NULL);
   }

   public SummoningTableMenu(int id, Inventory playerInventory, ContainerLevelAccess worldPosCallable) {
      super(SkiesContainers.SUMMONING_TABLE, id);
      this.worldPosCallable = worldPosCallable;
      this.addSlot(new Slot(this.tableInventory, 0, 15, 47) {
         public boolean mayPlace(ItemStack stack) {
            return true;
         }

         public int getMaxStackSize() {
            return 1;
         }
      });
      this.addSlot(new Slot(this.tableInventory, 1, 35, 47) {
         public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == SkiesItems.soul_fragment;
         }
      });

      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for (int k = 0; k < 9; k++) {
         this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
      }
   }

   public void onTomeButtonPressed(boolean enablingGolems) {
      ItemStack bookStack = this.getSlot(0).getItem();
      ItemStack soulStack = this.getSlot(1).getItem().copy();
      soulStack.shrink(1);
      this.getSlot(0).set(SummoningTomeItem.setSummonsGolems(bookStack, enablingGolems));
      this.getSlot(1).set(soulStack);
   }

   public void removed(Player playerIn) {
      super.removed(playerIn);
      this.worldPosCallable.execute((world, pos) -> this.clearContainer(playerIn, this.tableInventory));
   }

   public boolean stillValid(Player playerIn) {
      return stillValid(this.worldPosCallable, playerIn, SkiesBlocks.summoning_table);
   }

   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         if (index == 0) {
            if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
               return ItemStack.EMPTY;
            }
         } else if (index == 1) {
            if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
               return ItemStack.EMPTY;
            }
         } else if (itemstack1.getItem() == SkiesItems.soul_fragment) {
            if (!this.moveItemStackTo(itemstack1, 1, 2, true)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (((Slot)this.slots.get(0)).hasItem() || !((Slot)this.slots.get(0)).mayPlace(itemstack1)) {
               return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = itemstack1.copy();
            itemstack2.setCount(1);
            itemstack1.shrink(1);
            ((Slot)this.slots.get(0)).set(itemstack2);
         }

         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(playerIn, itemstack1);
      }

      return itemstack;
   }
}
