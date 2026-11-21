package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpawnSupporterPetPacket implements PacketHandler.ModPacket<SpawnSupporterPetPacket, SpawnSupporterPetPacket.Handler> {
   private byte petID;
   private boolean petEnabled;
   private boolean petAudible;
   private String petName;
   private byte styleID;

   public SpawnSupporterPetPacket(byte petIdIn, boolean petEnabledIn, boolean petAudibleIn, String petNameIn, byte styleIdIn) {
      this.petID = petIdIn;
      this.petEnabled = petEnabledIn;
      this.petAudible = petAudibleIn;
      this.petName = petNameIn;
      this.styleID = styleIdIn;
   }

   public SpawnSupporterPetPacket.Handler handler() {
      return new SpawnSupporterPetPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SpawnSupporterPetPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("spawn_supporter_pet");
      }

      public void encoder(SpawnSupporterPetPacket packet, FriendlyByteBuf buff) {
         buff.writeByte(packet.petID);
         buff.writeBoolean(packet.petEnabled);
         buff.writeBoolean(packet.petAudible);
         buff.writeUtf(packet.petName);
         buff.writeByte(packet.styleID);
      }

      public SpawnSupporterPetPacket decoder(FriendlyByteBuf buff) {
         return new SpawnSupporterPetPacket(buff.readByte(), buff.readBoolean(), buff.readBoolean(), buff.readUtf(16), buff.readByte());
      }

      public void handler(SpawnSupporterPetPacket packet, IPayloadContext context) {
         context.enqueueWork(() -> handlePacket(packet, this.serverPlayer(context)));
      }

      private static void handlePacket(SpawnSupporterPetPacket packet, Player player) {
         SkiesPlayer.ifPresent(player, skyPlayer -> {
            skyPlayer.setSupporterPetID(packet.petID);
            skyPlayer.setSupporterPetEnabled(packet.petEnabled);
            skyPlayer.setSupporterPetAudible(packet.petAudible);
            skyPlayer.setSupporterPetName(packet.petName);
            skyPlayer.setSupporterStyleID(packet.styleID);
         });
      }
   }
}
