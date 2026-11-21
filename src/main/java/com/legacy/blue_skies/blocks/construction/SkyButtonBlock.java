package com.legacy.blue_skies.blocks.construction;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class SkyButtonBlock {
   public static class Stone extends ButtonBlock {
      public Stone(Properties properties) {
         this(properties, BlockSetType.STONE);
      }

      public Stone(Properties properties, BlockSetType type) {
         super(type, 20, properties);
      }
   }

   public static class Wood extends ButtonBlock {
      public Wood(Properties properties) {
         this(properties, BlockSetType.OAK);
      }

      public Wood(Properties properties, BlockSetType type) {
         super(type, 30, properties);
      }
   }
}
