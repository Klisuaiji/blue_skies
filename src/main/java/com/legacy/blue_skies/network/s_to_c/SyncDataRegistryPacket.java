package com.legacy.blue_skies.network.s_to_c;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.managers.SkiesSyncedDataManager;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.structure_gel.api.util.NetworkUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncDataRegistryPacket implements PacketHandler.ModPacket<SyncDataRegistryPacket, SyncDataRegistryPacket.Handler> {
   private final byte id;
   private final SkiesSyncedDataManager<?, ?, ?> manager;

   public SyncDataRegistryPacket(int id, SkiesSyncedDataManager<?, ?, ?> manager) {
      this.id = (byte)id;
      this.manager = manager;
   }

   public SyncDataRegistryPacket.Handler handler() {
      return new SyncDataRegistryPacket.Handler();
   }

   public static class Handler implements PacketHandler.PlayHandler<SyncDataRegistryPacket> {
      @Override
      public ResourceLocation id() {
         return BlueSkies.locate("sync_data_registry");
      }

      public void encoder(SyncDataRegistryPacket packet, RegistryFriendlyByteBuf buff) {
         buff.writeByte(packet.id);
         NetworkUtil.writeCompressedNbt(buff, packet.manager.toNBT());
      }

      public SyncDataRegistryPacket decoder(RegistryFriendlyByteBuf buff) {
         byte id = buff.readByte();
         CompoundTag tag = NetworkUtil.readCompressedNbt(buff);
         return new SyncDataRegistryPacket(id, BlueSkiesData.getSyncedManager(id).fromNBT(tag));
      }

      public void handler(SyncDataRegistryPacket packet, IPayloadContext context) {
         context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
               BlueJournalScreen.savedScreen = null;
               BlueSkiesData.getSyncedManager(packet.id).setData(packet.manager);
               BlueSkies.LOGGER.debug("Recieved {} from the server", packet.manager.getName());
            }
         });
      }
   }
}
