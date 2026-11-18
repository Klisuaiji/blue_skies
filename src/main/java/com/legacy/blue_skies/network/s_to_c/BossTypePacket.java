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

public class BossTypePacket implements PacketHandler.ModPacket<BossTypePacket, BossTypePacket.Handler> {
   private byte type;

   public BossTypePacket(byte type) {
      this.type = type;
   }

   public BossTypePacket.Handler handler() {
      return new BossTypePacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<BossTypePacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("boss_type");
      }

      public void encoder(BossTypePacket packet, FriendlyByteBuf buff) {
         buff.writeByte(packet.type);
      }

      public BossTypePacket decoder(FriendlyByteBuf buff) {
         return new BossTypePacket(buff.readByte());
      }

      public void handler(BossTypePacket packet, PlayPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.workHandler().submitAsync(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(BossTypePacket packet) {
         Player player = Minecraft.getInstance().player;
         SkiesPlayer.ifPresent(player, skyPlayer -> skyPlayer.setBossType(SkiesDungeonType.getFromId(packet.type)));
      }
   }
}
