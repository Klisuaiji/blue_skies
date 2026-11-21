package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class GetClientMotionPacket implements PacketHandler.ModPacket<GetClientMotionPacket, GetClientMotionPacket.Handler> {
   private double motionX;
   private double motionZ;

   public GetClientMotionPacket(double motionXIn, double motionZIn) {
      this.motionX = motionXIn;
      this.motionZ = motionZIn;
   }

   public GetClientMotionPacket.Handler handler() {
      return new GetClientMotionPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<GetClientMotionPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("get_client_motion");
      }

      public void encoder(GetClientMotionPacket packet, RegistryFriendlyByteBuf buff) {
         buff.writeDouble(packet.motionX);
         buff.writeDouble(packet.motionZ);
      }

      public GetClientMotionPacket decoder(RegistryFriendlyByteBuf buff) {
         return new GetClientMotionPacket(buff.readDouble(), buff.readDouble());
      }

      public void handler(GetClientMotionPacket packet, IPayloadContext context) {
         context.enqueueWork(() -> handlePacket(packet, this.serverPlayer(context)));
      }

      private static void handlePacket(GetClientMotionPacket packet, Player player) {
         SkiesPlayer.ifPresent(player, skyPlayer -> skyPlayer.setSavedClientMotion(packet.motionX, 0.0, packet.motionZ));
      }
   }
}
