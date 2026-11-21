package com.legacy.blue_skies.client.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.IContainerFactory;

public interface ISkyContainerFactory<C extends AbstractContainerMenu> extends IContainerFactory<C> {
   C make(int var1, Inventory var2, FriendlyByteBuf var3);

   default C create(int windowId, Inventory inv, FriendlyByteBuf data) {
      return null;
   }
}
