package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.blocks.util.ITranslucentBlock;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TranslucentStairsBlock extends StairBlock implements ITranslucentBlock {
   private final Supplier<Set<Block>> cullingBlocks;

   public TranslucentStairsBlock(Supplier<BlockState> state, Properties properties, Supplier<Set<Block>> cullingBlocks) {
      super(state, properties);
      this.cullingBlocks = cullingBlocks;
   }

   @Override
   public Set<Block> getCullingBlocks() {
      return this.cullingBlocks.get();
   }
}
