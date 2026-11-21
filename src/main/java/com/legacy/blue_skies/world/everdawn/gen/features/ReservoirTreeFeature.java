package com.legacy.blue_skies.world.everdawn.gen.features;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class ReservoirTreeFeature extends DuskTreeFeature {
   public ReservoirTreeFeature(Codec<TreeConfiguration> configIn, int minTreeHeight) {
      super(configIn, minTreeHeight);
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      if (super.place(world, rand, pos, changedLogs, changedLeaves)) {
         placeVines(
            world, pos, rand, 11, this.minTreeHeight + 3, 3, 7, 0.08F, SkiesBlocks.dusk_vine.defaultBlockState(), ImmutableSet.of(SkiesBlocks.dusk_leaves)
         );
         return true;
      } else {
         return false;
      }
   }
}
