package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.util.base.SkiesIllagerBossEntity;
import com.legacy.blue_skies.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class IllagerTeleportPacket implements PacketHandler.ModPacket<IllagerTeleportPacket, IllagerTeleportPacket.Handler> {
   private int entityId;

   public IllagerTeleportPacket(int entityIdIn) {
      this.entityId = entityIdIn;
   }

   public IllagerTeleportPacket.Handler handler() {
      return new IllagerTeleportPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<IllagerTeleportPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("illager_teleport");
      }

      public void encoder(IllagerTeleportPacket packet, FriendlyByteBuf buff) {
         buff.writeInt(packet.entityId);
      }

      public IllagerTeleportPacket decoder(FriendlyByteBuf buff) {
         return new IllagerTeleportPacket(buff.readInt());
      }

      public void handler(IllagerTeleportPacket packet, IPayloadContext context) {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> handlePacket(packet));
         }
      }

      @OnlyIn(Dist.CLIENT)
      private static void handlePacket(IllagerTeleportPacket packet) {
         if (Minecraft.getInstance().level.getEntity(packet.entityId) instanceof SkiesIllagerBossEntity illager) {
            illager.noLerp = true;
         }
      }
   }
}
