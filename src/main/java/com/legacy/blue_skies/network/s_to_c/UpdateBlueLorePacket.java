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

public class UpdateBlueLorePacket implements PacketHandler.ModPacket<UpdateBlueLorePacket, UpdateBlueLorePacket.Handler> {
   private boolean usedLore;

   public UpdateBlueLorePacket(boolean usedLore) {
      this.usedLore = usedLore;
   }

   public UpdateBlueLorePacket.Handler handler() {
      return new UpdateBlueLorePacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<UpdateBlueLorePacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("update_blue_lore");
      }

      public void encoder(UpdateBlueLorePacket packet, FriendlyByteBuf buff) {
         buff.writeBoolean(packet.usedLore);
      }

      public UpdateBlueLorePacket decoder(FriendlyByteBuf buff) {
         return new UpdateBlueLorePacket(buff.readBoolean());
      }

      public void handler(UpdateBlueLorePacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(UpdateBlueLorePacket packet) {
         Player player = Minecraft.getInstance().player;
         SkiesPlayer.ifPresent(player, skiesPlayer -> skiesPlayer.setUsedBlueLore(packet.usedLore));
      }
   }
}
