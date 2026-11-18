package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.blocks.natural.HangingFruitLeavesBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class CrescentFruitTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.dusk_log.defaultBlockState());
   private static final Lazy<BlockState> LEAF_CANT_GROW = Lazy.of(() -> SkiesBlocks.crescent_fruit_leaves.defaultBlockState());
   private static final Lazy<BlockState> LEAF_CAN_GROW = Lazy.of(
      () -> (BlockState)SkiesBlocks.crescent_fruit_leaves.defaultBlockState().setValue(HangingFruitLeavesBlock.CAN_GROW, true)
   );

   public CrescentFruitTreeFeature(Codec<TreeConfiguration> config) {
      super(config, true);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      int height = rand.nextInt(3) + (rand.nextFloat() < 0.2 ? 9 : 6);
      if (!this.isAreaOk(world, pos, height, 1, 3, 5)) {
         return false;
      } else {
         this.setLeaves(changedLeaves, world, rand, pos, height);

         for (int yOffset = 0; yOffset < height; yOffset++) {
            this.setBlockIfOk(changedLogs, world, pos.above(yOffset), (BlockState)LOG.get());
         }

         this.setSideLogs(changedLogs, world, rand, pos);
         return true;
      }
   }

   protected void setLeaves(BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, RandomSource rand, BlockPos pos, int height) {
      pos = new BlockPos(pos.getX(), pos.getY() - 1 + height, pos.getZ());

      for (int x = -2; x <= 2; x++) {
         for (int z = -2; z <= 2; z++) {
            for (int y = -3; y <= -1; y++) {
               if (Math.abs(x) != 2 || Math.abs(z) != 2) {
                  if (y == -3 && rand.nextInt(3) == 0) {
                     this.setLeaf(changedLeaves, world, rand, pos.offset(x, y, z));
                  }

                  if (y >= -2) {
                     this.setLeaf(changedLeaves, world, rand, pos.offset(x, y, z));
                  }
               }
            }
         }
      }

      for (int yx = 0; yx <= 1; yx++) {
         if (yx == 0) {
            this.setLeaf(changedLeaves, world, rand, pos.offset(1, yx, 1));
            this.setLeaf(changedLeaves, world, rand, pos.offset(1, yx, -1));
            this.setLeaf(changedLeaves, world, rand, pos.offset(-1, yx, 1));
            this.setLeaf(changedLeaves, world, rand, pos.offset(-1, yx, -1));
         }

         this.setLeaf(changedLeaves, world, rand, pos.offset(1, yx, 0));
         this.setLeaf(changedLeaves, world, rand, pos.offset(0, yx, 1));
         this.setLeaf(changedLeaves, world, rand, pos.offset(0, yx, 0));
         this.setLeaf(changedLeaves, world, rand, pos.offset(0, yx, -1));
         this.setLeaf(changedLeaves, world, rand, pos.offset(-1, yx, 0));
      }

      this.setLeaf(changedLeaves, world, rand, pos.offset(2, 0, 0));
      this.setLeaf(changedLeaves, world, rand, pos.offset(-2, 0, 0));
      this.setLeaf(changedLeaves, world, rand, pos.offset(0, 0, 2));
      this.setLeaf(changedLeaves, world, rand, pos.offset(0, 0, -2));
   }

   protected void setSideLogs(BiConsumer<BlockPos, BlockState> changedLogs, WorldGenLevel world, RandomSource rand, BlockPos pos) {
      pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         BlockState state = world.getBlockState(pos.relative(dir).below());
         if (state.is(BlockTags.REPLACEABLE_BY_TREES) || state.getBlock() instanceof BushBlock) {
            this.setBlockIfOk(changedLogs, world, pos.relative(dir).below(), (BlockState)LOG.get());
         }
      }

      this.setBlockIfOk(changedLogs, world, pos.offset(rand.nextInt(3) - 1, 0, 0), (BlockState)LOG.get());
      this.setBlockIfOk(changedLogs, world, pos.offset(0, 0, rand.nextInt(3) - 1), (BlockState)LOG.get());
   }

   protected void setLeaf(BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, RandomSource rand, BlockPos pos) {
      this.setBlockIfOk(changedLeaves, world, pos, rand.nextFloat() > 0.33 ? (BlockState)LEAF_CANT_GROW.get() : (BlockState)LEAF_CAN_GROW.get());
   }
}
