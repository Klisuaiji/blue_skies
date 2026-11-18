package com.legacy.blue_skies.network.c_to_s;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.gui.menu.ArcsMenuProvider;
import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.SetMouseStackPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class OpenInventoryPacket {
   public static class Arcs implements PacketHandler.ModPacket<OpenInventoryPacket.Arcs, OpenInventoryPacket.Arcs.Handler> {
      public OpenInventoryPacket.Arcs.Handler handler() {
         return new OpenInventoryPacket.Arcs.Handler();
      }

      public static class Handler implements PacketHandler.PlayHandler<OpenInventoryPacket.Arcs> {
         @Override
         public ResourceLocation id() {
            return BlueSkies.locate("open_inventory_arcs");
         }

         public void encoder(OpenInventoryPacket.Arcs packet, FriendlyByteBuf buffer) {
         }

         public OpenInventoryPacket.Arcs decoder(FriendlyByteBuf buff) {
            return new OpenInventoryPacket.Arcs();
         }

         public void handler(OpenInventoryPacket.Arcs packet, PlayPayloadContext context) {
            context.workHandler().submitAsync(() -> {
               ServerPlayer player = this.serverPlayer(context);
               if (player != null) {
                  SkiesPlayer.ifPresent(player, skyPlayer -> {
                     ItemStack mouseStack = player.containerMenu.getCarried().copy();
                     player.containerMenu.setCarried(ItemStack.EMPTY);
                     player.openMenu(new ArcsMenuProvider());
                     player.containerMenu.setCarried(mouseStack);
                     PacketHandler.sendToClient(new SetMouseStackPacket(mouseStack), player);
                  });
               }
            });
         }
      }
   }

   public static class Mount implements PacketHandler.ModPacket<OpenInventoryPacket.Mount, OpenInventoryPacket.Mount.Handler> {
      public OpenInventoryPacket.Mount.Handler handler() {
         return new OpenInventoryPacket.Mount.Handler();
      }

      public static class Handler implements PacketHandler.PlayHandler<OpenInventoryPacket.Mount> {
         @Override
         public ResourceLocation id() {
            return BlueSkies.locate("open_inventory_mount");
         }

         public void encoder(OpenInventoryPacket.Mount packet, FriendlyByteBuf buffer) {
         }

         public OpenInventoryPacket.Mount decoder(FriendlyByteBuf buff) {
            return new OpenInventoryPacket.Mount();
         }

         public void handler(OpenInventoryPacket.Mount packet, PlayPayloadContext context) {
            context.workHandler().submitAsync(() -> {
               ServerPlayer player = this.serverPlayer(context);
               if (player != null && player.getVehicle() != null && player.getVehicle() instanceof SkiesMountEntity) {
                  SkiesMountEntity.openMountContainer(player, (SkiesMountEntity)player.getVehicle());
               }
            });
         }
      }
   }

   public static class Vanilla implements PacketHandler.ModPacket<OpenInventoryPacket.Vanilla, OpenInventoryPacket.Vanilla.Handler> {
      public OpenInventoryPacket.Vanilla.Handler handler() {
         return new OpenInventoryPacket.Vanilla.Handler();
      }

      public static class Handler implements PacketHandler.PlayHandler<OpenInventoryPacket.Vanilla> {
         @Override
         public ResourceLocation id() {
            return BlueSkies.locate("open_inventory_vanilla");
         }

         public void encoder(OpenInventoryPacket.Vanilla packet, FriendlyByteBuf buffer) {
         }

         public OpenInventoryPacket.Vanilla decoder(FriendlyByteBuf buff) {
            return new OpenInventoryPacket.Vanilla();
         }

         public void handler(OpenInventoryPacket.Vanilla packet, PlayPayloadContext context) {
            context.workHandler().submitAsync(() -> {
               ServerPlayer player = this.serverPlayer(context);
               if (player != null) {
                  if (player.getVehicle() != null && player.getVehicle() instanceof SkiesMountEntity) {
                     SkiesMountEntity.openMountContainer(player, (SkiesMountEntity)player.getVehicle());
                  }

                  SkiesPlayer.ifPresent(player, skyPlayer -> {
                     ItemStack mouseStack = player.containerMenu.getCarried().copy();
                     player.containerMenu.setCarried(ItemStack.EMPTY);
                     player.doCloseContainer();
                     player.containerMenu.setCarried(mouseStack);
                     PacketHandler.sendToClient(new SetMouseStackPacket(mouseStack), player);
                     skyPlayer.syncDataToClient();
                  });
               }
            });
         }
      }
   }
}
