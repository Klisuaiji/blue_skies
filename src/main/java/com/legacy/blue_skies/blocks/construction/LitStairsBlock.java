package com.legacy.blue_skies.blocks.construction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class LitStairsBlock extends StairBlock {
   public static final MapCodec<LitStairsBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(BlockState.CODEC.fieldOf("base_state").forGetter(rec$ -> rec$.getModelState()), propertiesCodec())
         .apply(instance, LitStairsBlock::new)
   );

   public LitStairsBlock(Supplier<BlockState> state, Properties properties) {
      super(state, properties);
   }

   private LitStairsBlock(BlockState state, Properties properties) {
      this(() -> state, properties);
   }

   public MapCodec<? extends LitStairsBlock> codec() {
      return CODEC;
   }

   public boolean useShapeForLightOcclusion(BlockState state) {
      return false;
   }
}
