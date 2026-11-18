package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.entities.passive.CrystalCamelEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import com.legacy.blue_skies.registries.SkiesContainers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class MountInventoryMenu extends AbstractContainerMenu {
   private final Container mountInventory;
   private final Container playerInventory;
   public final SkiesMountEntity mount;
   private boolean hasChest = false;

   public MountInventoryMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
      this(id, playerInventory, (SkiesMountEntity)playerInventory.player.level().getEntity(buffer.readInt()));
   }

   public MountInventoryMenu(int id, Inventory playerInventory, final SkiesMountEntity mount) {
      super(SkiesContainers.MOUNT_INVENTORY, id);
      this.mount = mount;
      this.mountInventory = mount.getMountInventory();
      this.playerInventory = playerInventory;
      this.playerInventory.startOpen(playerInventory.player);
      this.mountInventory.startOpen(playerInventory.player);

      for (int i = 0; i < 9; i++) {
         this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
      }

      for (int y = 0; y < 3; y++) {
         for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));
         }
      }

      this.addSlot(new Slot(this.mountInventory, 0, 8, 18) {
         public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == mount.getSaddleItem() && !this.hasItem();
         }

         @OnlyIn(Dist.CLIENT)
         public boolean isActive() {
            return mount.isTamed() && mount.getSaddleItem() != null;
         }
      });
      if (mount instanceof CrystalCamelEntity && ((CrystalCamelEntity)mount).hasChest()) {
         this.hasChest = true;

         for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 5; x++) {
               this.addSlot(new Slot(this.mountInventory, 1 + x + y * 5, 80 + x * 18, 18 + y * 18));
            }
         }
      }
   }

   public boolean stillValid(Player playerIn) {
      return this.mountInventory.stillValid(playerIn) && this.mount.isAlive() && this.mount.distanceTo(playerIn) < 8.0F;
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack clickedStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      ItemStack slotStack = slot.getItem();
      clickedStack = slotStack.copy();
      if (slot != null && slot.hasItem()) {
         if (index >= 0 && index <= 8) {
            if (!this.moveItemStackTo(slotStack, 36, 37, false)) {
               if (this.hasChest) {
                  if (!this.moveItemStackTo(slotStack, 37, 52, false) && !this.moveItemStackTo(slotStack, 9, 36, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (!this.moveItemStackTo(slotStack, 9, 36, false)) {
                  return ItemStack.EMPTY;
               }
            }
         } else if (index >= 9 && index <= 35) {
            if (!this.moveItemStackTo(slotStack, 36, 37, false)) {
               if (this.hasChest) {
                  if (!this.moveItemStackTo(slotStack, 37, 52, false) && !this.moveItemStackTo(slotStack, 0, 9, false)) {
                     return ItemStack.EMPTY;
                  }
               } else if (!this.moveItemStackTo(slotStack, 0, 9, false)) {
                  return ItemStack.EMPTY;
               }
            }
         } else if (index == 36) {
            if (!this.moveItemStackTo(slotStack, 9, 36, false) && !this.moveItemStackTo(slotStack, 0, 9, false)) {
               return ItemStack.EMPTY;
            }
         } else if (this.hasChest
            && index >= 37
            && index <= 52
            && !this.moveItemStackTo(slotStack, 0, 9, true)
            && !this.moveItemStackTo(slotStack, 9, 36, true)) {
            return ItemStack.EMPTY;
         }

         if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return clickedStack;
   }

   public void removed(Player playerIn) {
      super.removed(playerIn);
      this.mountInventory.stopOpen(playerIn);
      this.playerInventory.stopOpen(playerIn);
   }
}
