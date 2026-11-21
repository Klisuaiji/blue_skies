package com.legacy.blue_skies.client.gui.menu;

import com.legacy.blue_skies.block_entity.HorizoniteForgeBlockEntity;
import com.legacy.blue_skies.block_entity.IRecipeUnlocker;
import com.legacy.blue_skies.registries.SkiesContainers;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.EventHooks;

public class HorizoniteForgeMenu extends RecipeBookMenu<SingleRecipeInput, SmeltingRecipe> {
   private final Container tile;
   private final ContainerData furnaceData;
   protected final Level world;

   public HorizoniteForgeMenu(int id, Inventory playerInv) {
      this(id, playerInv, new SimpleContainer(3), new SimpleContainerData(4));
   }

   public HorizoniteForgeMenu(int id, Inventory playerInv, Container tile, ContainerData furnaceData) {
      super(SkiesContainers.HORIZONITE_FORGE, id);
      this.tile = tile;
      this.furnaceData = furnaceData;
      this.world = playerInv.player.level();
      this.addSlot(new Slot(tile, 0, 56, 17));
      this.addSlot(new HorizoniteForgeMenu.FuelSlot(this, tile, 1, 22, 54));
      this.addSlot(new HorizoniteForgeMenu.ResultSlot(playerInv.player, tile, 2, 116, 35));

      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for (int k = 0; k < 9; k++) {
         this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
      }

      this.addDataSlots(furnaceData);
   }

   public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
      if (this.tile instanceof StackedContentsCompatible) {
         ((StackedContentsCompatible)this.tile).fillStackedContents(itemHelperIn);
      }
   }

   public boolean shouldMoveToInventory(int slotIndex) {
      return slotIndex != 1;
   }

   public void clearCraftingContent() {
      this.getSlot(0).set(ItemStack.EMPTY);
      this.getSlot(2).set(ItemStack.EMPTY);
   }

   public boolean recipeMatches(RecipeHolder<SmeltingRecipe> recipeIn) {
      return recipeIn.value().matches(new SingleRecipeInput(this.tile.getItem(0)), this.world);
   }

   public int getResultSlotIndex() {
      return 2;
   }

   public int getGridWidth() {
      return 1;
   }

   public int getGridHeight() {
      return 1;
   }

   public int getSize() {
      return 3;
   }

   public boolean stillValid(Player player) {
      return this.tile.stillValid(player);
   }

   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack existingClone = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack existingStack = slot.getItem();
         existingClone = existingStack.copy();
         if (index == 2) {
            if (!this.moveItemStackTo(existingStack, 3, 39, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(existingStack, existingClone);
         } else if (index != 1 && index != 0) {
            if (this.hasRecipe(existingStack)) {
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

         if (existingStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (existingStack.getCount() == existingClone.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(playerIn, existingStack);
      }

      return existingClone;
   }

   protected boolean hasRecipe(ItemStack stack) {
      return this.world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), this.world).isPresent();
   }

   protected boolean isFuel(ItemStack stack) {
      return HorizoniteForgeBlockEntity.isFuel(stack);
   }

   @OnlyIn(Dist.CLIENT)
   public int getCookProgressionScaled() {
      double cookTime = this.furnaceData.get(2);
      double maxCookTime = this.furnaceData.get(3);
      return (int)(cookTime / maxCookTime * 24.0);
   }

   @OnlyIn(Dist.CLIENT)
   public int getEnergyLeftScaled() {
      double energy = this.furnaceData.get(0);
      double maxEnergy = this.furnaceData.get(1);
      double scaled = energy / maxEnergy * 32.0;
      return scaled > 0.0 && scaled < 1.0 ? 1 : (int)scaled;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isCooking() {
      return this.furnaceData.get(2) > 0;
   }

   @OnlyIn(Dist.CLIENT)
   public RecipeBookType getRecipeBookType() {
      return RecipeBookType.FURNACE;
   }

   public static class FuelSlot extends Slot {
      private final HorizoniteForgeMenu container;

      public FuelSlot(HorizoniteForgeMenu container, Container furnaceInv, int id, int x, int y) {
         super(furnaceInv, id, x, y);
         this.container = container;
      }

      public boolean mayPlace(ItemStack stack) {
         return this.container.isFuel(stack);
      }
   }

   public static class ResultSlot extends FurnaceResultSlot {
      private final Player player;
      private int removeCount;

      public ResultSlot(Player player, Container furnaceInv, int id, int x, int y) {
         super(player, furnaceInv, id, x, y);
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

      public void onTake(Player player, ItemStack stack) {
         this.checkTakeAchievements(stack);
         super.onTake(player, stack);
      }

      protected void onQuickCraft(ItemStack stack, int amount) {
         this.removeCount += amount;
         this.checkTakeAchievements(stack);
      }

      protected void checkTakeAchievements(ItemStack stack) {
         stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
         if (!this.player.level().isClientSide() && this.container instanceof IRecipeUnlocker) {
            ((IRecipeUnlocker)this.container).unlockRecipes(this.player);
         }

         this.removeCount = 0;
         EventHooks.firePlayerSmeltedEvent(this.player, stack);
      }
   }
}
