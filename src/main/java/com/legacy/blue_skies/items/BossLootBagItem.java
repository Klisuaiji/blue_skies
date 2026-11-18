package com.legacy.blue_skies.items;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.items.util.IRarityItem;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item.Properties;

public class BossLootBagItem extends LootBagItem {
   private final Supplier<EntityType<?>> boss;

   public BossLootBagItem(Properties properties, Supplier<EntityType<?>> boss) {
      super(properties);
      this.boss = boss;
   }

   @Override
   public <T extends Item & IRarityItem> void fillItemCategoryWithRarity(Output output, T item, TabVisibility visibility) {
      String bossName = BuiltInRegistries.ENTITY_TYPE.getKey(this.boss.get()).getPath();

      for (IRarityItem.SkiesRarity rarity : IRarityItem.SkiesRarity.values()) {
         ItemStack stack = new ItemStack(item);
         item.setRarityTag(stack, rarity);
         LootBagItem.setLootTable(stack, BlueSkies.locate("loot_bags/" + bossName + "/" + rarity));
         output.accept(stack, visibility);
      }
   }

   @Override
   public void fillTabs(Output output) {
      this.fillItemCategoryWithRarity(output, this, TabVisibility.SEARCH_TAB_ONLY);
      ItemStack stack = new ItemStack(this);
      String bossName = BuiltInRegistries.ENTITY_TYPE.getKey(this.boss.get()).getPath();
      this.setRarityTag(stack, IRarityItem.SkiesRarity.COMMON);
      LootBagItem.setLootTable(stack, BlueSkies.locate("loot_bags/" + bossName + "/" + IRarityItem.SkiesRarity.COMMON));
      output.accept(stack, TabVisibility.PARENT_TAB_ONLY);
   }
}
