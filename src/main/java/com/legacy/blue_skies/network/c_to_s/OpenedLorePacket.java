package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenedLorePacket implements PacketHandler.ModPacket<OpenedLorePacket, OpenedLorePacket.Handler> {
   public OpenedLorePacket.Handler handler() {
      return new OpenedLorePacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<OpenedLorePacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("opened_lore_packet");
      }

      public void encoder(OpenedLorePacket packet, RegistryFriendlyByteBuf buff) {
      }

      public OpenedLorePacket decoder(RegistryFriendlyByteBuf buff) {
         return new OpenedLorePacket();
      }

      public void handler(OpenedLorePacket packet, IPayloadContext context) {
         context.enqueueWork(() -> handlePacket(packet, this.serverPlayer(context)));
      }

      private static void handlePacket(OpenedLorePacket packet, ServerPlayer player) {
         player.doCloseContainer();
         SkiesPlayer.ifPresent(player, skyPlayer -> skyPlayer.setOpenedBlueLore(true));
      }
   }
}
