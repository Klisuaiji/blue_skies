package com.legacy.blue_skies.items.util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;

public interface IRarityItem {
   default void setRarityTag(ItemStack stack, IRarityItem.SkiesRarity rarity) {
      CompoundTag nbt = stack.getOrCreateTag();
      nbt.putInt("Rarity", rarity.id);
   }

   default IRarityItem.SkiesRarity getRarityTag(ItemStack stack) {
      CompoundTag nbt = stack.getOrCreateTag();
      return IRarityItem.SkiesRarity.getFromId(nbt.getInt("Rarity"));
   }

   default <T extends Item & IRarityItem> void fillItemCategoryWithRarity(Output output, T item, TabVisibility visibility) {
      for (IRarityItem.SkiesRarity rarity : IRarityItem.SkiesRarity.values()) {
         ItemStack stack = new ItemStack(item);
         item.setRarityTag(stack, rarity);
         output.accept(stack, visibility);
      }
   }

   default void addRarityText(ItemStack stack, List<Component> lore) {
      Component rarityLabel = Component.translatable("gui.blue_skies.item.rarity").withStyle(ChatFormatting.LIGHT_PURPLE);
      Component itemRarity = Component.translatable(String.format("gui.blue_skies.item.rarity.%s", this.getRarityTag(stack).getId()));
      lore.add(Component.translatable("%s: %s", new Object[]{rarityLabel, itemRarity}));
   }

   public static enum SkiesRarity {
      COMMON(0, "common"),
      UNCOMMON(1, "uncommon"),
      RARE(2, "rare");

      private final int id;
      private final String name;

      private SkiesRarity(int id, String name) {
         this.id = id;
         this.name = name;
      }

      public int getId() {
         return this.id;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static IRarityItem.SkiesRarity getFromId(int id) {
         return id >= 0 && id < values().length ? values()[id] : COMMON;
      }
   }

   public static class WeightedSkiesRarity implements WeightedEntry {
      private final IRarityItem.SkiesRarity rarity;
      private final Weight weight;

      public WeightedSkiesRarity(int itemWeight, IRarityItem.SkiesRarity rarity) {
         this.weight = Weight.of(itemWeight);
         this.rarity = rarity;
      }

      public IRarityItem.SkiesRarity getRarity() {
         return this.rarity;
      }

      public Weight getWeight() {
         return this.weight;
      }
   }
}
