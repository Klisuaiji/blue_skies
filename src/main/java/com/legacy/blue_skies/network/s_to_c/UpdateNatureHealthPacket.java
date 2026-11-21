package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateNatureHealthPacket implements PacketHandler.ModPacket<UpdateNatureHealthPacket, UpdateNatureHealthPacket.Handler> {
   private float health;

   public UpdateNatureHealthPacket(float health) {
      this.health = health;
   }

   public UpdateNatureHealthPacket.Handler handler() {
      return new UpdateNatureHealthPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<UpdateNatureHealthPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("update_nature_health");
      }

      public void encoder(UpdateNatureHealthPacket packet, FriendlyByteBuf buff) {
         buff.writeFloat(packet.health);
      }

      public UpdateNatureHealthPacket decoder(FriendlyByteBuf buff) {
         return new UpdateNatureHealthPacket(buff.readFloat());
      }

      public void handler(UpdateNatureHealthPacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(UpdateNatureHealthPacket packet) {
         Player player = Minecraft.getInstance().player;
         SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.setNatureHealth(packet.health));
      }
   }
}
