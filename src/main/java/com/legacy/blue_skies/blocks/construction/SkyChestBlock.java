package com.legacy.blue_skies.blocks.construction;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyChestBlock extends ChestBlock {
   private final Supplier<BlockEntityType<? extends ChestBlockEntity>> tileSupplier;

   public SkyChestBlock(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> tileSupplier) {
      super(properties, tileSupplier);
      this.tileSupplier = tileSupplier;
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return this.tileSupplier.get().create(pos, state);
   }
}
