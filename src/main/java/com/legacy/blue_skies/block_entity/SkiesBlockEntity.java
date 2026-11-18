package com.legacy.blue_skies.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SkiesBlockEntity extends BlockEntity {
   public SkiesBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
   }

   public CompoundTag getUpdateTag() {
      CompoundTag tag = new CompoundTag();
      this.saveAdditional(tag);
      return tag;
   }

   public void handleUpdateTag(CompoundTag tag) {
      this.load(tag);
   }

   public final ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this, type -> type.serializeNBT());
   }

   public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
      this.load(packet.getTag());
   }
}
