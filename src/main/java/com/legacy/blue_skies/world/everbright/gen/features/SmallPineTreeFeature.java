package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class SmallPineTreeFeature extends AbstractSkyTreeFeature {
   private final Lazy<BlockState> log;
   private final Lazy<BlockState> leaf;
   private final int baseHeight;

   public SmallPineTreeFeature(Codec<TreeConfiguration> config, int baseHeight, Supplier<BlockState> leaf, Supplier<BlockState> log) {
      super(config, true);
      this.log = Lazy.of(log);
      this.leaf = Lazy.of(leaf);
      this.baseHeight = baseHeight;
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(3) + this.baseHeight;
      if (!this.isAreaOk(world, pos, height, 1, height - 2, 3)) {
         return false;
      }

      for (int y = 0; y < height; y++) {
         this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)this.log.get());
      }

      BlockState leaf = (BlockState)this.leaf.get();
      this.setBlockIfOk(changedLeaves, world, pos.above(height), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(1, height - 1, 0), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(1, height - 2, 0), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(-1, height - 1, 0), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(-1, height - 2, 0), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(0, height - 1, 1), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(0, height - 2, 1), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(0, height - 1, -1), leaf);
      this.setBlockIfOk(changedLeaves, world, pos.offset(0, height - 2, -1), leaf);
      return true;
   }
}
