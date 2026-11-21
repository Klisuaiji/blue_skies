package com.legacy.blue_skies.blocks.construction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SkyGlassPaneBlock extends IronBarsBlock {
   private final float[] rgb = new float[3];

   public SkyGlassPaneBlock(Properties properties, float red, float green, float blue) {
      super(properties);
      this.rgb[0] = red / 255.0F;
      this.rgb[1] = green / 255.0F;
      this.rgb[2] = blue / 255.0F;
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return true;
   }

   public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
      return this.rgb;
   }
}
