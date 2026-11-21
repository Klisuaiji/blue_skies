package com.legacy.blue_skies.client.gui.screen;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.data.BlueSkiesData;
import java.util.Set;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SnowcapOvenRecipeGui extends SmeltingRecipeBookComponent {
   protected Set<Item> getFuelItems() {
      return (Set<Item>)(!BlueSkiesData.SNOWCAP_OVEN_FREEZING.getData().isEmpty()
         ? BlueSkiesData.SNOWCAP_OVEN_FREEZING.getData().keySet()
         : ImmutableSet.of(Items.AIR));
   }
}
