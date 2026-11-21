package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class StarlitTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.starlit_log.defaultBlockState());
   private static final Lazy<BlockState> LEAF = Lazy.of(() -> SkiesBlocks.starlit_leaves.defaultBlockState());

   public StarlitTreeFeature(Codec<TreeConfiguration> config) {
      super(config, true);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(3) + 7;
      if (!this.isAreaOk(world, pos, height, 1, 2, 0)) {
         return false;
      }

      for (int y = 0; y < height; y++) {
         this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)LOG.get());
      }

      this.setBlockIfOk(changedLeaves, world, pos.above(height), (BlockState)LEAF.get());
      this.setBlockIfOk(changedLeaves, world, pos.above(height + 1), (BlockState)LEAF.get());
      this.setBlockIfOk(changedLeaves, world, pos.above(height + 2), (BlockState)LEAF.get());

      for (int y = height - 5; y <= height; y++) {
         for (Direction dir : DirectionUtil.HORIZONTAL) {
            this.setBlockIfOk(changedLeaves, world, pos.relative(dir).above(y), (BlockState)LEAF.get());
         }

         if (y == height - 4 || y == height - 2) {
            int x = -2;

            for (int z = 0; x <= 0; z++) {
               this.setBlockIfOk(changedLeaves, world, pos.offset(x, y, z), (BlockState)LEAF.get());
               this.setBlockIfOk(changedLeaves, world, pos.offset(x + 1, y, z - 1), (BlockState)LEAF.get());
               this.setBlockIfOk(changedLeaves, world, pos.offset(x + 2, y, z - 2), (BlockState)LEAF.get());
               x++;
            }
         }

         if (y == height - 3 || y == height - 1) {
            int x = 1;
            int z = 1;

            for (int j = 0; j <= 1; j++) {
               for (int k = 0; k <= 1; k++) {
                  if (rand.nextBoolean()) {
                     this.setBlockIfOk(changedLeaves, world, pos.offset(x, y, z), (BlockState)LEAF.get());
                  }

                  z = Math.negateExact(z);
               }

               x = Math.negateExact(x);
            }
         }
      }

      return true;
   }
}
