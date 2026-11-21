package com.legacy.blue_skies.blocks.natural;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyOreBlock extends DropExperienceBlock {
   public SkyOreBlock(Properties properties) {
      super(UniformInt.of(0, 1), properties);
   }

   public SkyOreBlock(Properties properties, int minXp, int maxXp) {
      super(UniformInt.of(minXp, maxXp), properties);
   }
}
