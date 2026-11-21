package com.legacy.blue_skies.block_entity;

import net.minecraft.core.HolderLookup;
import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.world.level.block.state.BlockState;

public class TrophyBlockEntity extends SkiesBlockEntity {
   private Component label = Component.empty();

   public TrophyBlockEntity(BlockPos pos, BlockState state) {
      super(SkiesBlockEntityTypes.TROPHY, pos, state);
   }

   public void loadAdditional(CompoundTag compound, HolderLookup.Provider p) {
      super.loadAdditional(compound, p);
      if (compound.contains("Label", 8)) {
         this.label = Serializer.fromJson(compound.getString("Label"));
      }
   }

   public void saveAdditional(CompoundTag compound, HolderLookup.Provider p) {
      super.saveAdditional(compound, p);
      if (this.label != null) {
         compound.putString("Label", Serializer.toJson(this.label));
      }
   }

   public Component getLabel() {
      return this.label;
   }

   public void setLabel(Component label) {
      this.label = label;
      this.setChanged();
   }
}
