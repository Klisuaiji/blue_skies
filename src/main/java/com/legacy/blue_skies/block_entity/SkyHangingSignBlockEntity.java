package com.legacy.blue_skies.block_entity;

import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkyHangingSignBlockEntity extends HangingSignBlockEntity {
   public SkyHangingSignBlockEntity(BlockPos pos, BlockState state) {
      super(pos, state);
   }

   public BlockEntityType<SkyHangingSignBlockEntity> getType() {
      return SkiesBlockEntityTypes.SKY_HANGING_SIGN;
   }
}
