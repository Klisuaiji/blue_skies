package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SetMouseStackPacket implements PacketHandler.ModPacket<SetMouseStackPacket, SetMouseStackPacket.Handler> {
   private final ItemStack stack;

   public SetMouseStackPacket(ItemStack stack) {
      this.stack = stack;
   }

   public SetMouseStackPacket.Handler handler() {
      return new SetMouseStackPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SetMouseStackPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("set_mouse_stack");
      }

      public void encoder(SetMouseStackPacket packet, FriendlyByteBuf buff) {
         buff.writeItem(packet.stack);
      }

      public SetMouseStackPacket decoder(FriendlyByteBuf buff) {
         return new SetMouseStackPacket(buff.readItem());
      }

      public void handler(SetMouseStackPacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> {
               LocalPlayer player = Minecraft.getInstance().player;
               if (player != null) {
                  player.containerMenu.setCarried(packet.stack.copy());
               }
            });
         }
      }
   }
}
