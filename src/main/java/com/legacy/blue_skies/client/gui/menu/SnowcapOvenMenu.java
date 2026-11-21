package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.block_entity.SnowcapOvenBlockEntity;
import com.legacy.blue_skies.registries.SkiesContainers;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class SnowcapOvenMenu extends AbstractFurnaceMenu {
   public SnowcapOvenMenu(int id, Inventory playerInv) {
      this(id, playerInv, new SimpleContainer(4), new SimpleContainerData(4));
   }

   public SnowcapOvenMenu(int id, Inventory playerInv, Container furnaceInv, ContainerData furnaceData) {
      super(SkiesContainers.SNOWCAP_OVEN, RecipeType.SMELTING, RecipeBookType.FURNACE, id, playerInv, furnaceInv, furnaceData);
      this.setSlot(2, new FurnaceResultSlot(playerInv.player, furnaceInv, 2, 116, 21));
      this.addSlot(new SnowcapOvenMenu.FreezeResultSlot(playerInv.player, furnaceInv, 3, 116, 49));
   }

   public void setSlot(int index, Slot slot) {
      slot.index = index;
      this.slots.set(index, slot);
   }

   public int getFrozenOutputSlot() {
      return 39;
   }

   public int getSize() {
      return 3;
   }

   public boolean shouldMoveToInventory(int slotIndex) {
      return slotIndex != 1;
   }

   public void clearCraftingContent() {
      this.getSlot(0).set(ItemStack.EMPTY);
      this.getSlot(2).set(ItemStack.EMPTY);
   }

   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack copiedStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack existingStack = slot.getItem();
         copiedStack = existingStack.copy();
         if (index != 2 && index != this.getFrozenOutputSlot()) {
            if (index != 1 && index != 0) {
               if (this.canSmelt(existingStack)) {
                  if (!this.moveItemStackTo(existingStack, 0, 1, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (this.isFuel(existingStack)) {
                  if (!this.moveItemStackTo(existingStack, 1, 2, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (index >= 3 && index < 30) {
                  if (!this.moveItemStackTo(existingStack, 30, 39, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (index >= 30 && index < 39 && !this.moveItemStackTo(existingStack, 3, 30, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (!this.moveItemStackTo(existingStack, 3, 39, false)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (!this.moveItemStackTo(existingStack, 3, 39, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(existingStack, copiedStack);
         }

         if (existingStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (existingStack.getCount() == copiedStack.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(playerIn, existingStack);
      }

      return copiedStack;
   }

   protected boolean isFuel(ItemStack stack) {
      return SnowcapOvenBlockEntity.isFuel(stack);
   }

   public static class FreezeResultSlot extends Slot {
      private final Player player;
      private int removeCount;

      public FreezeResultSlot(Player player, Container inv, int index, int x, int y) {
         super(inv, index, x, y);
         this.player = player;
      }

      public boolean mayPlace(ItemStack stack) {
         return false;
      }

      public ItemStack remove(int amount) {
         if (this.hasItem()) {
            this.removeCount = this.removeCount + Math.min(amount, this.getItem().getCount());
         }

         return super.remove(amount);
      }

      public void onTake(Player thePlayer, ItemStack stack) {
         this.checkTakeAchievements(stack);
         super.onTake(thePlayer, stack);
      }

      protected void onQuickCraft(ItemStack stack, int amount) {
         this.removeCount += amount;
         this.checkTakeAchievements(stack);
      }

      protected void checkTakeAchievements(ItemStack stack) {
         stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
         this.removeCount = 0;
      }
   }
}
