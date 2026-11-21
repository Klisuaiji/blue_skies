package com.legacy.blue_skies.blocks.construction;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class LitSlabBlock extends SlabBlock {
   public static final MapCodec<LitSlabBlock> CODEC = simpleCodec(LitSlabBlock::new);

   public LitSlabBlock(Properties properties) {
      super(properties);
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return false;
   }

   public MapCodec<? extends LitSlabBlock> codec() {
      return CODEC;
   }
}
