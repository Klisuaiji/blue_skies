package com.legacy.blue_skies.client.gui.screen;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.data.BlueSkiesData;
import java.util.Set;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class HorizoniteForgeRecipeGui extends SmeltingRecipeBookComponent {
   protected Set<Item> getFuelItems() {
      return (Set<Item>)(!BlueSkiesData.HORIZONITE_FORGE_FUEL.getData().isEmpty()
         ? BlueSkiesData.HORIZONITE_FORGE_FUEL.getData().keySet()
         : ImmutableSet.of(Items.AIR));
   }
}
