package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.blocks.construction.BagOfSpoilsBlock;
import com.legacy.blue_skies.registries.SkiesContainers;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class BagOfSpoilsMenu extends AbstractContainerMenu {
   private final Container bag;

   public BagOfSpoilsMenu(int id, Inventory playerInv) {
      this(id, playerInv, new SimpleContainer(5));
   }

   public BagOfSpoilsMenu(int id, Inventory playerInv, Container bagInv) {
      super(SkiesContainers.BAG_OF_SPOILS, id);
      this.bag = bagInv;
      checkContainerSize(bagInv, 5);
      bagInv.startOpen(playerInv.player);

      for (int j = 0; j < 5; j++) {
         this.addSlot(new BagOfSpoilsMenu.BagSlot(bagInv, j, 44 + j * 18, 20));
      }

      for (int l = 0; l < 3; l++) {
         for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
         }
      }

      for (int i1 = 0; i1 < 9; i1++) {
         this.addSlot(new Slot(playerInv, i1, 8 + i1 * 18, 109));
      }
   }

   public boolean stillValid(Player player) {
      return this.bag.stillValid(player);
   }

   public ItemStack quickMoveStack(Player player, int slotId) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(slotId);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         if (slotId < this.bag.getContainerSize()) {
            if (!this.moveItemStackTo(itemstack1, this.bag.getContainerSize(), this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(itemstack1, 0, this.bag.getContainerSize(), false)) {
            return ItemStack.EMPTY;
         }

         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return itemstack;
   }

   public void removed(Player player) {
      super.removed(player);
      this.bag.stopOpen(player);
   }

   class BagSlot extends Slot {
      public BagSlot(Container inv, int slotId, int x, int y) {
         super(inv, slotId, x, y);
      }

      public boolean mayPlace(ItemStack stack) {
         Block block = Block.byItem(stack.getItem());
         return !(block instanceof ShulkerBoxBlock) && !(block instanceof BagOfSpoilsBlock);
      }
   }
}
