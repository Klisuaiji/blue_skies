package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class HandleScreenClickPacket implements PacketHandler.ModPacket<HandleScreenClickPacket, HandleScreenClickPacket.Handler> {
   private int containerId;
   private int buttonIndex;

   public HandleScreenClickPacket(int containerId, int buttonIndex) {
      this.containerId = containerId;
      this.buttonIndex = buttonIndex;
   }

   public HandleScreenClickPacket.Handler handler() {
      return new HandleScreenClickPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<HandleScreenClickPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("handle_screen_click");
      }

      public void encoder(HandleScreenClickPacket packet, FriendlyByteBuf buff) {
         buff.writeInt(packet.containerId);
         buff.writeInt(packet.buttonIndex);
      }

      public HandleScreenClickPacket decoder(FriendlyByteBuf buff) {
         return new HandleScreenClickPacket(buff.readInt(), buff.readInt());
      }

      public void handler(HandleScreenClickPacket packet, PlayPayloadContext context) {
         context.workHandler().submitAsync(() -> handlePacket(packet, this.serverPlayer(context)));
      }

      private static void handlePacket(HandleScreenClickPacket packet, ServerPlayer player) {
         player.resetLastActionTime();
         if (player.containerMenu.containerId == packet.containerId && !player.isSpectator()) {
            if (!player.containerMenu.stillValid(player)) {
               BlueSkies.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
            } else if (player.containerMenu.clickMenuButton(player, packet.buttonIndex)) {
               player.containerMenu.broadcastChanges();
            }
         }
      }
   }
}
