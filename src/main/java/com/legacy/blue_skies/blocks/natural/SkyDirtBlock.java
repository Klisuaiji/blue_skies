package com.legacy.blue_skies.blocks.natural;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyDirtBlock extends Block {
   public SkyDirtBlock() {
      super(Properties.of().strength(0.5F).sound(SoundType.GRAVEL));
   }
}
