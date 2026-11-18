package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.SetInvisiblePacket;
import com.legacy.blue_skies.network.s_to_c.SpawnParticlePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DuskArcItem extends ArcItem {
   public DuskArcItem(int slotId) {
      super(slotId, "dusk");
   }

   @Override
   public void serverTick(ItemStack stack, ServerPlayer player) {
      if (player.getEffect(MobEffects.INVISIBILITY) == null && !player.isSpectator()) {
         boolean invisCondition = player.isShiftKeyDown()
            && !player.isSwimming()
            && !player.isVisuallySwimming()
            && !player.isFallFlying()
            && !player.isSprinting();
         if (invisCondition != player.isInvisible()) {
            player.setInvisible(invisCondition);
            PacketHandler.sendToClient(new SetInvisiblePacket(invisCondition, (byte)0, player.getUUID()), player);
            SkiesPlayer.ifPresent(player, skiesPlayer -> {
               if (skiesPlayer.getArcInventory().hasEmpoweredDusk(player)) {
                  skiesPlayer.setFullDuskInvis(invisCondition);
                  PacketHandler.sendToClients(new SetInvisiblePacket(invisCondition, (byte)1, player.getUUID()), player.serverLevel());
               }
            });
         }

         if (invisCondition && player.isInvisible()) {
            switch (this.getFunctionalLevel(stack, player)) {
               case 0:
                  if (player.level().getRandom().nextFloat() < 0.3) {
                     PacketHandler.sendToClients(new SpawnParticlePacket((byte)1, player.position()), player.serverLevel());
                  }
                  break;
               case 1:
                  if (player.level().getRandom().nextFloat() < 0.1) {
                     PacketHandler.sendToClients(new SpawnParticlePacket((byte)1, player.position()), player.serverLevel());
                  }
            }
         }
      }
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
      player.setInvisible(false);
      SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.setFullDuskInvis(false));
   }

   public static boolean shouldBeInvisible(Player player) {
      return player.isShiftKeyDown()
         && player.getEffect(MobEffects.INVISIBILITY) == null
         && !player.isSwimming()
         && !player.isVisuallySwimming()
         && !player.isFallFlying()
         && !player.isSprinting()
         && !player.isSpectator();
   }
}
