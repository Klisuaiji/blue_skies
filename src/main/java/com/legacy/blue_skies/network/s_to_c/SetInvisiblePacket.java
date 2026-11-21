package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SetInvisiblePacket implements PacketHandler.ModPacket<SetInvisiblePacket, SetInvisiblePacket.Handler> {
   private boolean value;
   private byte mode;
   private UUID uuid;

   public SetInvisiblePacket(boolean valueIn, byte mode, UUID uuid) {
      this.value = valueIn;
      this.mode = mode;
      this.uuid = uuid;
   }

   public SetInvisiblePacket.Handler handler() {
      return new SetInvisiblePacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SetInvisiblePacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("set_invisible");
      }

      public void encoder(SetInvisiblePacket packet, FriendlyByteBuf buff) {
         buff.writeBoolean(packet.value);
         buff.writeByte(packet.mode);
         buff.writeUUID(packet.uuid);
      }

      public SetInvisiblePacket decoder(FriendlyByteBuf buff) {
         return new SetInvisiblePacket(buff.readBoolean(), buff.readByte(), buff.readUUID());
      }

      public void handler(SetInvisiblePacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(SetInvisiblePacket packet) {
         Player player = Minecraft.getInstance().player.level().getPlayerByUUID(packet.uuid);
         if (packet.mode == 0) {
            if (player != null) {
               player.setInvisible(packet.value);
            }
         } else if (packet.mode == 1) {
            SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.setFullDuskInvis(packet.value));
         }
      }
   }
}
