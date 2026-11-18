package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NatureArcItem extends ArcItem {
   public NatureArcItem(int arcId) {
      super(arcId, "nature");
   }

   @Override
   public void serverTick(ItemStack stack, ServerPlayer player) {
      SkiesPlayer.ifPresent(player, skiesPlayer -> {
         int regenRate = 200;
         if (player.tickCount % regenRate == 0 && player.getLastDamageSource() == null) {
            float maxHealth = (this.getFunctionalLevel(stack, player) + 1) * 2;
            float currentHealth = skiesPlayer.getNatureHealth();
            if (currentHealth < maxHealth) {
               skiesPlayer.setNatureHealth(Math.min(currentHealth + 1.0F, maxHealth));
            }
         }
      });
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
      SkiesPlayer.ifPresent(player, skiesPlayer -> {
         if (!skiesPlayer.getArcInventory().hasArc(SkiesItems.nature_arc)) {
            skiesPlayer.setNatureHealth(0.0F);
         }
      });
   }

   @Override
   public void onEquip(ItemStack stack, Player player) {
      SkiesPlayer.ifPresent(player, skiesPlayer -> {
         if (skiesPlayer.getNatureHealth() == 0.0F) {
            skiesPlayer.setNatureHealth(1.0F);
         }
      });
   }
}
