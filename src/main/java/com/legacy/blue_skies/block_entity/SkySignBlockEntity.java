package com.legacy.blue_skies.block_entity;

import com.legacy.blue_skies.registries.SkiesBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkySignBlockEntity extends SignBlockEntity {
   public SkySignBlockEntity(BlockPos pos, BlockState state) {
      super(pos, state);
   }

   public BlockEntityType<SkySignBlockEntity> getType() {
      return SkiesBlockEntityTypes.SKY_SIGN;
   }
}
