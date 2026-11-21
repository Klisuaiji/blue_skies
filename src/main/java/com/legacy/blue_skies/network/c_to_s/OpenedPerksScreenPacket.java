package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenedPerksScreenPacket implements PacketHandler.ModPacket<OpenedPerksScreenPacket, OpenedPerksScreenPacket.Handler> {
   public OpenedPerksScreenPacket.Handler handler() {
      return new OpenedPerksScreenPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<OpenedPerksScreenPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("opened_perks_screen");
      }

      public void encoder(OpenedPerksScreenPacket packet, FriendlyByteBuf buff) {
      }

      public OpenedPerksScreenPacket decoder(FriendlyByteBuf buff) {
         return new OpenedPerksScreenPacket();
      }

      public void handler(OpenedPerksScreenPacket packet, IPayloadContext context) {
         context.enqueueWork(() -> handlePacket(packet, this.serverPlayer(context)));
      }

      private static void handlePacket(OpenedPerksScreenPacket packet, ServerPlayer player) {
         player.doCloseContainer();
      }
   }
}
