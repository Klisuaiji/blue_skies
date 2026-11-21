package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.block_entity.SkySignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.WoodType;

public class SkyWallSignBlock extends WallSignBlock {
   public SkyWallSignBlock(Properties properties, WoodType type) {
      super(type, properties);
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SkySignBlockEntity(pos, state);
   }
}
