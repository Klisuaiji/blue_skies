package com.legacy.blue_skies.capability;

import com.legacy.blue_skies.items.arcs.IArcItem;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArcInventory implements Container {
   public static final int SIZE = 4;
   private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
   private final List<Consumer<ArcInventory>> listeners = new ArrayList<>();

   public void copyFrom(ArcInventory from) {
      for (int i = 0; i < 4; i++) {
         this.items.set(i, from.getItem(i));
      }
   }

   public void addListener(Consumer<ArcInventory> listener) {
      this.listeners.add(listener);
   }

   public void clearContent() {
      this.items.clear();
      this.setChanged();
   }

   public int getContainerSize() {
      return 4;
   }

   public boolean isEmpty() {
      for (int i = 0; i < 4; i++) {
         if (!((ItemStack)this.items.get(i)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public ItemStack getItem(int index) {
      return index >= 0 && index < 4 ? (ItemStack)this.items.get(index) : ItemStack.EMPTY;
   }

   public ItemStack removeItemNoUpdate(int index) {
      return ContainerHelper.takeItem(this.items, index);
   }

   public ItemStack removeItem(int index, int count) {
      ItemStack stack = ContainerHelper.removeItem(this.items, index, count);
      this.setChanged();
      return stack;
   }

   public void setItem(int index, ItemStack stack) {
      this.items.set(index, stack);
   }

   public void setChanged() {
      for (int i = this.listeners.size() - 1; i > -1; i--) {
         this.listeners.get(i).accept(this);
      }
   }

   public boolean stillValid(Player player) {
      return true;
   }

   public int getMaxStackSize() {
      return 1;
   }

   public boolean canPlaceItem(int index, ItemStack stack) {
      return stack.getItem() instanceof IArcItem;
   }

   public NonNullList<ItemStack> getStacks() {
      return this.items;
   }

   public void refreshArcs(Player player) {
      for (int i = 0; i < 4; i++) {
         ItemStack stack = (ItemStack)this.items.get(i);
         if (stack.getItem() instanceof IArcItem arc) {
            arc.refresh(stack, player);
         }
      }
   }

   public boolean hasArc(BiFunction<ItemStack, IArcItem, Boolean> condition) {
      for (int i = 0; i < 4; i++) {
         ItemStack stack = (ItemStack)this.items.get(i);
         if (stack.getItem() instanceof IArcItem arc && condition.apply(stack, arc)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasArc(IArcItem arc) {
      for (int i = 0; i < 4; i++) {
         if (((ItemStack)this.items.get(i)).getItem() == arc) {
            return true;
         }
      }

      return false;
   }

   public boolean hasEmpoweredDusk(Player player) {
      return this.hasArc((stack, arc) -> SkiesItems.dusk_arc == arc && arc.getFunctionalLevel(stack, player) >= 3);
   }

   public ListTag write(net.minecraft.core.HolderLookup.Provider provider) {
      ListTag tagList = new ListTag();
      for (int i = 0; i < 4; i++) {
         CompoundTag nbt = new CompoundTag();
         ((ItemStack)this.items.get(i)).save(provider, nbt);
         tagList.add(nbt);
      }

      return tagList;
   }

   public void read(net.minecraft.core.HolderLookup.Provider provider, ListTag tagList) {
      this.items.clear();

      for (int i = 0; i < 4; i++) {
         CompoundTag nbt = tagList.getCompound(i);
         ItemStack stack = ItemStack.parseOptional(provider, nbt);
         if (!stack.isEmpty()) {
            this.items.set(i, stack);
         }
      }

      this.setChanged();
   }
}
