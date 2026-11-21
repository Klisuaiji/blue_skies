package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesContainers;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.registries.SkiesTriggers;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AlchemyTableMenu extends AbstractContainerMenu {
   private List<Item> outputs = new ArrayList<>();
   private final DataSlot selectedItem = DataSlot.standalone();
   private final Container inputInventory = new SimpleContainer(2) {
      public void setChanged() {
         super.setChanged();
         AlchemyTableMenu.this.slotsChanged(this);
         AlchemyTableMenu.this.inventoryUpdateListener.run();
      }
   };
   private final Container outputInventory = new SimpleContainer(1);
   private final ContainerLevelAccess worldPosCallable;
   private final Slot inputSlotA;
   private final Slot inputSlotB;
   private final Slot outputSlot;
   private ItemStack inputSlotAItem = ItemStack.EMPTY;
   private ItemStack inputSlotBItem = ItemStack.EMPTY;
   private Runnable inventoryUpdateListener = () -> {};
   private long lastOnTake;

   public AlchemyTableMenu(int id, Inventory playerInv) {
      this(id, playerInv, ContainerLevelAccess.NULL);
   }

   public AlchemyTableMenu(int id, Inventory playerInv, final ContainerLevelAccess worldPos) {
      super(SkiesContainers.ALCHEMY_TABLE, id);
      this.worldPosCallable = worldPos;
      this.inputSlotA = this.addSlot(new Slot(this.inputInventory, 0, 20, 23));
      this.inputSlotB = this.addSlot(new Slot(this.inputInventory, 1, 20, 42));
      this.outputSlot = this.addSlot(new Slot(this.outputInventory, 0, 143, 33) {
         public boolean mayPlace(ItemStack stack) {
            return false;
         }

         public void onTake(Player player, ItemStack stack) {
            AlchemyTableMenu.this.triggerAdvancement(player, stack);
            stack.onCraftedBy(player.level(), player, stack.getCount());
            ItemStack inputA = this.reduceStack(AlchemyTableMenu.this.inputSlotA);
            ItemStack inputB = this.reduceStack(AlchemyTableMenu.this.inputSlotB);
            if (!inputA.isEmpty() || !inputB.isEmpty()) {
               AlchemyTableMenu.this.updateOutputSlot();
            }

            worldPos.execute((world, pos) -> {
               long time = world.getGameTime();
               if (AlchemyTableMenu.this.lastOnTake != time) {
                  world.playSound((Player)null, pos, SkiesSounds.BLOCK_ALCHEMY_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                  AlchemyTableMenu.this.lastOnTake = time;
               }
            });
            this.setChanged();
         }

         private ItemStack reduceStack(Slot slot) {
            if (slot.hasItem()) {
               ItemStack stack = slot.getItem();
               return stack.hasCraftingRemainingItem() && stack.getCount() == 1 ? stack.getCraftingRemainingItem() : slot.remove(1);
            } else {
               return ItemStack.EMPTY;
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
      return stillValid(this.worldPosCallable, player, SkiesBlocks.alchemy_table);
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack slotStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack stackInSlot = slot.getItem();
         Item itemInSlot = stackInSlot.getItem();
         slotStack = stackInSlot.copy();
         if (index == 2) {
            itemInSlot.onCraftedBy(stackInSlot, player.level(), player);
            this.triggerAdvancement(player, stackInSlot);
            if (!this.moveItemStackTo(stackInSlot, 3, 39, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stackInSlot, slotStack);
         } else if (index != 0 && index != 1) {
            if (!this.moveItemStackTo(stackInSlot, 0, 2, false)) {
               return ItemStack.EMPTY;
            }

            if (index >= 3 && index < 30) {
               if (!this.moveItemStackTo(stackInSlot, 30, 39, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (index >= 30 && index < 39 && !this.moveItemStackTo(stackInSlot, 3, 30, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(stackInSlot, 3, 39, false)) {
            return ItemStack.EMPTY;
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
      ItemStack inputA = this.inputSlotA.getItem();
      ItemStack inputB = this.inputSlotB.getItem();
      if (inputA.getItem() != this.inputSlotAItem.getItem() || inputB.getItem() != this.inputSlotBItem.getItem()) {
         this.inputSlotAItem = inputA.copy();
         this.inputSlotBItem = inputB.copy();
         this.updateRecipes(inventory, inputA, inputB);
      }
   }

   public boolean clickMenuButton(Player playerIn, int id) {
      if (id >= 0 && id < this.outputs.size()) {
         this.selectedItem.set(id);
         this.updateOutputSlot();
      }

      return true;
   }

   private void updateRecipes(Container inventory, ItemStack inputA, ItemStack inputB) {
      this.outputs.clear();
      this.selectedItem.set(-1);
      this.outputSlot.set(ItemStack.EMPTY);
      if (!inputA.isEmpty() || !inputB.isEmpty()) {
         Item itemA = inventory.getItem(0).getItem();
         Item itemB = inventory.getItem(1).getItem();
         List<List<Item>> recipeOutputLists = BlueSkiesData.ALCHEMY_RECIPES
            .getMatching(itemA, itemB)
            .stream()
            .map(recipe -> recipe.getResults(itemA, itemB))
            .collect(Collectors.toList());
         List<Item> allOutputs = new ArrayList<>();
         if (recipeOutputLists.size() == 1) {
            allOutputs = recipeOutputLists.get(0);
         } else {
            for (List<Item> list : new ArrayList<>(recipeOutputLists)) {
               for (Item item : new ArrayList<>(list)) {
                  if (!allOutputs.contains(item)) {
                     allOutputs.add(item);
                  }
               }
            }
         }

         allOutputs.sort((a, b) -> BuiltInRegistries.ITEM.getKey(a).compareTo(BuiltInRegistries.ITEM.getKey(b)));
         this.outputs = allOutputs;
      }
   }

   private void updateOutputSlot() {
      if (!this.outputs.isEmpty() && this.selectedItem.get() >= 0 && this.selectedItem.get() < this.outputs.size()) {
         this.outputSlot.set(new ItemStack((ItemLike)this.outputs.get(this.selectedItem.get())));
      } else {
         this.outputSlot.set(ItemStack.EMPTY);
      }

      this.broadcastChanges();
   }

   private void triggerAdvancement(Player player, ItemStack stack) {
      if (player instanceof ServerPlayer && !stack.isEmpty()) {
         SkiesTriggers.USED_ALCHEMY_TABLE.trigger((ServerPlayer)player, stack);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void setInventoryUpdateListener(Runnable listener) {
      this.inventoryUpdateListener = listener;
   }

   @OnlyIn(Dist.CLIENT)
   public int getSelectedItem() {
      return this.selectedItem.get();
   }

   @OnlyIn(Dist.CLIENT)
   public List<Item> getOutputItems() {
      return this.outputs;
   }

   @OnlyIn(Dist.CLIENT)
   public int getOutputCount() {
      return this.outputs.size();
   }

   @OnlyIn(Dist.CLIENT)
   public boolean hasItemsinInputSlot() {
      return (this.inputSlotA.hasItem() || this.inputSlotB.hasItem()) && !this.outputs.isEmpty();
   }
}
