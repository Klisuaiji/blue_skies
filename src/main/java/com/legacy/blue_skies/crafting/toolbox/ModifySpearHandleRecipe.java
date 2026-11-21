package com.legacy.blue_skies.crafting.toolbox;

import com.legacy.blue_skies.compat.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ModifySpearHandleRecipe extends ToolboxRecipe {
   private static BiMap<Item, Item> spearStickRegistry = (BiMap<Item, Item>)Util.make(HashBiMap.create(), map -> {
      map.put(SkiesItems.bluebright_spear, SkiesItems.bluebright_stick);
      map.put(SkiesItems.starlit_spear, SkiesItems.starlit_stick);
      map.put(SkiesItems.frostbright_spear, SkiesItems.frostbright_stick);
      map.put(SkiesItems.lunar_spear, SkiesItems.lunar_stick);
      map.put(SkiesItems.dusk_spear, SkiesItems.dusk_stick);
      map.put(SkiesItems.maple_spear, SkiesItems.maple_stick);
      map.put(SkiesItems.comet_spear, SkiesItems.comet_stick);
   });

   @Override
   public boolean matches(ItemStack inputItem, ItemStack modifierItem) {
      return spearStickRegistry.containsKey(inputItem.getItem()) && spearStickRegistry.containsValue(modifierItem.getItem());
   }

   @Override
   public ItemStack getResult(ItemStack inputItem, ItemStack modifierItem) {
      ItemStack output = new ItemStack((ItemLike)spearStickRegistry.inverse().get(modifierItem.getItem()));
      output.setTag(LegacyNbt.get(inputItem));
      if (output.getDamageValue() >= output.getMaxDamage() - 1) {
         output.setDamageValue(output.getMaxDamage() - 1);
      }

      if (output.getItem() instanceof IFalsiteItem && IFalsiteItem.getFalsiteUses(output) > IFalsiteItem.getMaxFalsiteUses(output)) {
         IFalsiteItem.setFalsiteUses(output, IFalsiteItem.getMaxFalsiteUses(output));
      }

      return output;
   }
}
