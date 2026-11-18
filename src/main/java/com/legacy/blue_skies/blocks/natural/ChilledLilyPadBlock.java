package com.legacy.blue_skies.blocks.natural;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.common.PlantType;

public class ChilledLilyPadBlock extends WaterlilyBlock {
   public ChilledLilyPadBlock() {
      super(Properties.ofFullCopy(Blocks.LILY_PAD));
   }

   public PlantType getPlantType(BlockGetter level, BlockPos pos) {
      return PlantType.WATER;
   }
}
