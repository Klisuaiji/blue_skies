package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.capability.SkiesPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;

public class RunicArcItem extends ArcItem {
   public RunicArcItem(int slotId) {
      super(slotId, "runic", new Properties().stacksTo(1));
   }

   @Override
   public int getMaxLevel() {
      return 0;
   }

   @Override
   public void onEquip(ItemStack stack, Player player) {
      this.refreshOthers(stack, player);
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
      this.refreshOthers(stack, player);
   }

   private void refreshOthers(ItemStack stack, Player player) {
      SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.getArcInventory().refreshArcs(player));
   }
}
