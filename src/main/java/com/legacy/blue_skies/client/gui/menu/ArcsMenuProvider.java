package com.legacy.blue_skies.client.gui.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ArcsMenuProvider implements MenuProvider {
   public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
      return new ArcsMenu(id, playerInv, player);
   }

   public Component getDisplayName() {
      return Component.translatable("gui.blue_skies.arc_inventory");
   }
}
