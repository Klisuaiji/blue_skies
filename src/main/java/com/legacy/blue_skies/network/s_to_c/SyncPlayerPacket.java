package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncPlayerPacket implements PacketHandler.ModPacket<SyncPlayerPacket, SyncPlayerPacket.Handler> {
   protected final SkiesPlayer attachment;

   public SyncPlayerPacket(SkiesPlayer attachment) {
      this.attachment = attachment;
   }

   public SyncPlayerPacket.Handler handler() {
      return new SyncPlayerPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SyncPlayerPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("sync_player");
      }

      public void encoder(SyncPlayerPacket packet, FriendlyByteBuf buff) {
         CompoundTag tag = SkiesPlayer.Serializer.INSTANCE.write(packet.attachment);
         buff.writeNbt(tag);
      }

      public SyncPlayerPacket decoder(FriendlyByteBuf buff) {
         SkiesPlayer attachment = SkiesPlayer.Serializer.INSTANCE.read(null, buff.readNbt());
         return new SyncPlayerPacket(attachment);
      }

      public void handler(SyncPlayerPacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(SyncPlayerPacket packet) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.player != null) {
            SkiesPlayer.get(mc.player).copyFrom(packet.attachment);
         }
      }
   }
}
