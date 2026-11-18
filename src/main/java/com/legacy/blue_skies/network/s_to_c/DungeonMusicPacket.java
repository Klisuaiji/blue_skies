package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class DungeonMusicPacket implements PacketHandler.ModPacket<DungeonMusicPacket, DungeonMusicPacket.Handler> {
   private byte type;

   public DungeonMusicPacket(byte typeIn) {
      this.type = typeIn;
   }

   public DungeonMusicPacket.Handler handler() {
      return new DungeonMusicPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<DungeonMusicPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("dungeon_music");
      }

      public void encoder(DungeonMusicPacket packet, FriendlyByteBuf buff) {
         buff.writeByte(packet.type);
      }

      public DungeonMusicPacket decoder(FriendlyByteBuf buff) {
         return new DungeonMusicPacket(buff.readByte());
      }

      public void handler(DungeonMusicPacket packet, PlayPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.workHandler().submitAsync(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(DungeonMusicPacket packet) {
         Player player = Minecraft.getInstance().player;
         SkiesPlayer.ifPresent(player, skyPlayer -> skyPlayer.setDungeonMusicId(SkiesDungeonType.getFromId(packet.type)));
      }
   }
}
