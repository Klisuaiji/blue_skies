package com.legacy.blue_skies.blocks.construction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkyGlassBlock extends TransparentBlock {
   private final float[] rgb = new float[3];

   public SkyGlassBlock(Properties properties, float red, float green, float blue) {
      super(properties.noOcclusion());
      this.rgb[0] = red / 255.0F;
      this.rgb[1] = green / 255.0F;
      this.rgb[2] = blue / 255.0F;
   }

   public SkyGlassBlock(Properties properties) {
      super(properties);
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return true;
   }

   public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
      return this.rgb;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
      return adjacentBlockState.getBlock() == this;
   }
}
