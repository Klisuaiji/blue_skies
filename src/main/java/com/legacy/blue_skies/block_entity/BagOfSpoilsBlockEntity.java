package com.legacy.blue_skies.block_entity;

import com.legacy.blue_skies.client.gui.menu.BagOfSpoilsMenu;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BagOfSpoilsBlockEntity extends RandomizableContainerBlockEntity {
   private NonNullList<ItemStack> bagContents = NonNullList.withSize(5, ItemStack.EMPTY);
   private int numPlayersUsing;

   public BagOfSpoilsBlockEntity(BlockPos pos, BlockState state) {
      super(SkiesBlockEntityTypes.BAG_OF_SPOILS, pos, state);
   }

   public void load(CompoundTag nbt) {
      super.load(nbt);
      this.loadFromNbt(nbt);
   }

   public void saveAdditional(CompoundTag compound) {
      this.saveToNbt(compound);
   }

   public void loadFromNbt(CompoundTag compound) {
      this.bagContents = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
      if (!this.tryLoadLootTable(compound) && compound.contains("Items", 9)) {
         ContainerHelper.loadAllItems(compound, this.bagContents);
      }
   }

   public CompoundTag saveToNbt(CompoundTag compound) {
      if (!this.trySaveLootTable(compound)) {
         ContainerHelper.saveAllItems(compound, this.bagContents, false);
      }

      return compound;
   }

   public int getContainerSize() {
      return this.bagContents.size();
   }

   protected NonNullList<ItemStack> getItems() {
      return this.bagContents;
   }

   protected void setItems(NonNullList<ItemStack> itemsIn) {
      this.bagContents = itemsIn;
   }

   protected Component getDefaultName() {
      return Component.translatable(SkiesBlocks.bag_of_spoils.getDescriptionId());
   }

   protected AbstractContainerMenu createMenu(int id, Inventory player) {
      return new BagOfSpoilsMenu(id, player, this);
   }

   public void startOpen(Player player) {
      if (!player.isSpectator()) {
         if (this.numPlayersUsing < 0) {
            this.numPlayersUsing = 0;
         }

         this.numPlayersUsing++;
         this.scheduleTick();
         this.level
            .playSound(
               (Player)null,
               this.worldPosition.getX() + 0.5,
               this.worldPosition.getY() + 0.5,
               this.worldPosition.getZ() + 0.5,
               SkiesSounds.ITEM_BAG_OPEN,
               SoundSource.BLOCKS,
               0.5F,
               this.level.random.nextFloat() * 0.1F + 1.0F
            );
      }
   }

   private void scheduleTick() {
      this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 5);
   }

   public void bagTick() {
      this.numPlayersUsing = ChestBlockEntity.getOpenCount(this.level, this.worldPosition);
      if (this.numPlayersUsing > 0) {
         this.scheduleTick();
      } else {
         BlockState blockstate = this.getBlockState();
         if (!blockstate.is(SkiesBlocks.bag_of_spoils)) {
            this.setRemoved();
            return;
         }
      }
   }

   public void stopOpen(Player player) {
      if (!player.isSpectator()) {
         this.numPlayersUsing--;
      }
   }
}
