package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateSupportersPacket implements PacketHandler.ModPacket<UpdateSupportersPacket, UpdateSupportersPacket.Handler> {
   public UpdateSupportersPacket.Handler handler() {
      return new UpdateSupportersPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<UpdateSupportersPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("update_supporters");
      }

      public void encoder(UpdateSupportersPacket packet, RegistryFriendlyByteBuf buff) {
      }

      public UpdateSupportersPacket decoder(RegistryFriendlyByteBuf buff) {
         return new UpdateSupportersPacket();
      }

      public void handler(UpdateSupportersPacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(UpdateSupportersPacket packet) {
         BlueSkies.ML_SUPPORTER.refresh();
      }
   }
}
