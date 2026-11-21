package com.legacy.blue_skies.data.managers;

import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.s_to_c.SyncDataRegistryPacket;
import com.legacy.structure_gel.core.util.Internal;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class SkiesSyncedDataManager<K, V, T extends SkiesSyncedDataManager<K, V, T>> extends SkiesDataManager<K, V> {
   public SkiesSyncedDataManager(String name) {
      super(name);
   }

   public void syncToClient(ServerPlayer serverPlayer) {
      PacketHandler.sendToClient(this.getPacket(), serverPlayer);
   }

   @Internal
   public void setData(SkiesSyncedDataManager<?, ?, ?> newInstance) {
      this.data = (Map<K, V>)newInstance.getData();
   }

   public SyncDataRegistryPacket getPacket() {
      return new SyncDataRegistryPacket(this.getId(), this);
   }

   public int getId() {
      for (int id = 0; id < BlueSkiesData.SYNCED_MANAGERS.size(); id++) {
         if (BlueSkiesData.SYNCED_MANAGERS.get(id).getClass() == this.getClass()) {
            return id;
         }
      }

      return 0;
   }

   public abstract T newInstance();

   public CompoundTag toNBT() {
      CompoundTag nbt = new CompoundTag();
      ListTag list = new ListTag();
      this.data.forEach((key, value) -> {
         CompoundTag entry = new CompoundTag();
         Pair<CompoundTag, CompoundTag> entryNbt = this.entryToNbt((K)key, (V)value);
         entry.put("key", (Tag)entryNbt.getFirst());
         entry.put("value", (Tag)entryNbt.getSecond());
         list.add(entry);
      });
      nbt.put("data", list);
      return nbt;
   }

   public abstract Pair<CompoundTag, CompoundTag> entryToNbt(K var1, V var2);

   public T fromNBT(CompoundTag nbt) {
      T manager = this.newInstance();
      ((ListTag)nbt.get("data")).forEach(entryInbt -> {
         if (entryInbt instanceof CompoundTag entry) {
            CompoundTag keyNbt = entry.getCompound("key");
            CompoundTag valueNbt = entry.getCompound("value");
            Pair<K, V> dataEntry = this.entryFromNbt(keyNbt, valueNbt);
            if (dataEntry != null) {
               manager.registerData(manager.getData(), (K)dataEntry.getFirst(), (V)dataEntry.getSecond());
            }
         }
      });
      return manager;
   }

   @Nullable
   public abstract Pair<K, V> entryFromNbt(CompoundTag var1, CompoundTag var2);

   public void putItem(CompoundTag nbt, String key, Item item) {
      nbt.putString(key, BuiltInRegistries.ITEM.getKey(item).toString());
   }

   public Item getItem(CompoundTag nbt, String key) {
      return (Item)BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString(key)));
   }

   public void putBlock(CompoundTag nbt, String key, Block block) {
      nbt.putString(key, BuiltInRegistries.BLOCK.getKey(block).toString());
   }

   public Block getBlock(CompoundTag nbt, String key) {
      return (Block)BuiltInRegistries.BLOCK.get(ResourceLocation.parse(nbt.getString(key)));
   }
}
