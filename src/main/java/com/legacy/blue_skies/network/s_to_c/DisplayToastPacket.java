package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.gui.toast.SkiesToast;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DisplayToastPacket implements PacketHandler.ModPacket<DisplayToastPacket, DisplayToastPacket.Handler> {
   private byte toastId;

   public DisplayToastPacket(byte toastIdIn) {
      this.toastId = toastIdIn;
   }

   public DisplayToastPacket.Handler handler() {
      return new DisplayToastPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<DisplayToastPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("display_toast");
      }

      public void encoder(DisplayToastPacket packet, RegistryFriendlyByteBuf buff) {
         buff.writeByte(packet.toastId);
      }

      public DisplayToastPacket decoder(RegistryFriendlyByteBuf buff) {
         return new DisplayToastPacket(buff.readByte());
      }

      public void handler(DisplayToastPacket packet, IPayloadContext context) {
         context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
               handlePacket(packet);
            }
         });
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(DisplayToastPacket packet) {
         if (packet.toastId <= 0) {
            SkiesPlayer.ifPresent(
               Minecraft.getInstance().player,
               skyPlayer -> builder()
                  .withTitle(StringUtil.getBasicKey("toasts", "unlock_journal_entry"))
                  .withDesc(StringUtil.getBasicKey("toasts", !skyPlayer.hasUsedBlueLore() ? "unlock_journal_entry_no_book.desc" : "unlock_journal_entry.desc"))
                  .withSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT)
                  .build()
                  .beginDisplaying()
            );
         } else if (packet.toastId == 1) {
            builder()
               .withTitle(StringUtil.getBasicKey("toasts", "boss_room_death"))
               .withDesc(StringUtil.getBasicKey("toasts", "boss_room_death.desc"))
               .withDisplayItem(SkiesBlocks.bright_blinding_keystone)
               .build()
               .beginDisplaying();
         } else {
            int start = 15;
            Item key = packet.toastId == start + 1 ? SkiesItems.nature_key : (packet.toastId == start + 2 ? SkiesItems.poison_key : SkiesItems.blinding_key);
            String keyName = BuiltInRegistries.ITEM.getKey(key).getPath();
            builder()
               .withTitle(StringUtil.getBasicKey("toasts", "unlock_key_trade"))
               .withDesc(StringUtil.getBasicKey("toasts", "unlock_" + keyName + "_trade"))
               .withDisplayItem(key)
               .build()
               .beginDisplaying();
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static SkiesToast.Builder builder() {
         return SkiesToast.Builder.get();
      }
   }
}
