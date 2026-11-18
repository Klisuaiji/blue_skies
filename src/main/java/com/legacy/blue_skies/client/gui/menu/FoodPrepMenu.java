package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.items.util.FoodPrepUtils;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesContainers;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesTriggers;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FoodPrepMenu extends AbstractContainerMenu {
   private final Container inputInventory = new SimpleContainer(5) {
      public void setChanged() {
         super.setChanged();
         FoodPrepMenu.this.slotsChanged(this);
         FoodPrepMenu.this.inventoryUpdateListener.run();
      }
   };
   private final Container outputInventory = new SimpleContainer(1);
   private final ContainerLevelAccess worldPosCallable;
   private final List<Slot> inputSlots = new ArrayList<>();
   private final Slot outputSlot;
   private Runnable inventoryUpdateListener = () -> {};
   private long lastOnTake;

   public FoodPrepMenu(int id, Inventory playerInv) {
      this(id, playerInv, ContainerLevelAccess.NULL);
   }

   public FoodPrepMenu(int id, Inventory playerInv, final ContainerLevelAccess worldPos) {
      super(SkiesContainers.FOOD_PREP_TABLE, id);
      this.worldPosCallable = worldPos;

      for (int i = 0; i < 5; i++) {
         this.inputSlots.add(this.addSlot(new Slot(this.inputInventory, i, 44 + 18 * i, 18)));
      }

      this.outputSlot = this.addSlot(new Slot(this.outputInventory, 0, 80, 53) {
         public boolean mayPlace(ItemStack stack) {
            return false;
         }

         public void onTake(Player player, ItemStack stack) {
            if (player instanceof ServerPlayer) {
               SkiesTriggers.USED_FOOD_PREP.trigger((ServerPlayer)player);
            }

            stack.onCraftedBy(player.level(), player, stack.getCount());

            for (int i = 0; i < FoodPrepMenu.this.inputSlots.size(); i++) {
               this.reduceStack(FoodPrepMenu.this.inputSlots.get(i));
            }

            FoodPrepMenu.this.updateOutputSlot();
            worldPos.execute((world, pos) -> {
               long time = world.getGameTime();
               if (FoodPrepMenu.this.lastOnTake != time) {
                  world.playSound((Player)null, pos, SkiesSounds.BLOCK_FOOD_PREP_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                  FoodPrepMenu.this.lastOnTake = time;
               }
            });
            this.setChanged();
         }

         private ItemStack reduceStack(Slot slot) {
            if (!slot.hasItem()) {
               return ItemStack.EMPTY;
            } else {
               ItemStack stack = slot.getItem();
               ItemStack containerItem = null;
               if (stack.getItem() == Items.POTION) {
                  containerItem = new ItemStack(Items.GLASS_BOTTLE);
               } else if (stack.getItem() instanceof BowlFoodItem || stack.getItem() instanceof SuspiciousStewItem) {
                  containerItem = new ItemStack(Items.BOWL);
               } else if (stack.hasCraftingRemainingItem()) {
                  containerItem = stack.getCraftingRemainingItem();
               }

               if (containerItem != null) {
                  if (stack.getCount() == 1) {
                     slot.set(containerItem);
                     return containerItem;
                  }

                  FoodPrepMenu.this.moveItemStackTo(stack.getCraftingRemainingItem(), 6, 42, false);
               }

               return slot.remove(1);
            }
         }
      });

      for (int y = 0; y < 3; y++) {
         for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
         }
      }

      for (int i = 0; i < 9; i++) {
         this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
      }
   }

   public void removed(Player player) {
      super.removed(player);
      this.worldPosCallable.execute((world, pos) -> this.clearContainer(player, this.inputInventory));
   }

   public boolean stillValid(Player player) {
      return stillValid(this.worldPosCallable, player, SkiesBlocks.food_prep_table);
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack slotStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         int containerSize = 6;
         ItemStack stackInSlot = slot.getItem();
         Item itemInSlot = stackInSlot.getItem();
         slotStack = stackInSlot.copy();
         if (index == containerSize - 1) {
            itemInSlot.onCraftedBy(stackInSlot, player.level(), player);
            if (!this.moveItemStackTo(stackInSlot, containerSize, containerSize + 36, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stackInSlot, slotStack);
         } else if (index >= 0 && index <= containerSize - 2) {
            if (!this.moveItemStackTo(stackInSlot, containerSize, containerSize + 36, false)) {
               return ItemStack.EMPTY;
            }
         } else {
            if (!this.moveItemStackTo(stackInSlot, 0, containerSize - 1, false)) {
               return ItemStack.EMPTY;
            }

            if (index >= containerSize && index < containerSize + 27) {
               if (!this.moveItemStackTo(stackInSlot, containerSize + 27, containerSize + 36, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (index >= containerSize + 27
               && index < containerSize + 36
               && !this.moveItemStackTo(stackInSlot, containerSize, containerSize + 27, false)) {
               return ItemStack.EMPTY;
            }
         }

         if (stackInSlot.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         }

         slot.setChanged();
         if (stackInSlot.getCount() == slotStack.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(player, stackInSlot);
         this.broadcastChanges();
      }

      return slotStack;
   }

   public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
      return slotIn != this.outputSlot && super.canTakeItemForPickAll(stack, slotIn);
   }

   public void slotsChanged(Container inventory) {
      this.updateOutputSlot();
   }

   public boolean clickMenuButton(Player playerIn, int id) {
      this.updateOutputSlot();
      return true;
   }

   private void updateOutputSlot() {
      this.outputSlot.set(FoodPrepUtils.createFood(this.inputInventory));
      this.broadcastChanges();
   }

   @OnlyIn(Dist.CLIENT)
   public void setInventoryUpdateListener(Runnable listener) {
      this.inventoryUpdateListener = listener;
   }
}
