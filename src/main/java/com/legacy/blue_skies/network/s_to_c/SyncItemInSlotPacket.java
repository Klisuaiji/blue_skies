package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SyncItemInSlotPacket(ItemStack mainItem, ItemStack offItem, int selectedSlot)
   implements PacketHandler.ModPacket<SyncItemInSlotPacket, SyncItemInSlotPacket.Handler> {
   public SyncItemInSlotPacket.Handler handler() {
      return new SyncItemInSlotPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SyncItemInSlotPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("sync_item_in_slot");
      }

      public void encoder(SyncItemInSlotPacket packet, FriendlyByteBuf buff) {
         buff.writeItem(packet.mainItem);
         buff.writeItem(packet.offItem);
         buff.writeInt(packet.selectedSlot);
      }

      public SyncItemInSlotPacket decoder(FriendlyByteBuf buff) {
         return new SyncItemInSlotPacket(buff.readItem(), buff.readItem(), buff.readInt());
      }

      public void handler(SyncItemInSlotPacket packet, PlayPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.workHandler().submitAsync(() -> {
               LocalPlayer player = Minecraft.getInstance().player;
               if (player.getInventory().selected == packet.selectedSlot) {
                  player.setItemInHand(InteractionHand.MAIN_HAND, packet.mainItem);
               }

               player.setItemInHand(InteractionHand.OFF_HAND, packet.offItem);
            });
         }
      }
   }
}
