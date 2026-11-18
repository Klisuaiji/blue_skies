package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class ShortMoonstoneSpikeFeature extends Feature<NoneFeatureConfiguration> {
   protected static final Lazy<BlockState> SPIKE = Lazy.of(() -> SkiesBlocks.vitreous_moonstone.defaultBlockState());

   public ShortMoonstoneSpikeFeature(Codec<NoneFeatureConfiguration> configFactoryIn) {
      super(configFactoryIn);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      int sides = 0;
      int width = 1;
      if (level.getBlockState(pos.below()).getBlock() == SkiesBlocks.crystal_sand && level.getBlockState(pos).getBlock() == Blocks.AIR) {
         for (int y = -1; y < 4; y++) {
            this.setBlock(level, pos.offset(0, y, 0));
         }

         for (int x = -width; x <= width; x++) {
            for (int z = -width; z <= width; z++) {
               if (Math.abs(x) != width || Math.abs(z) != width) {
                  this.setBlock(level, pos.offset(x, 0, z));
                  if (Math.abs(x) == width ^ Math.abs(z) == width && level.getRandom().nextBoolean() && sides < 3) {
                     this.setBlock(level, pos.offset(x, 1, z));
                     sides++;
                  }
               }
            }
         }
      }

      return true;
   }

   private void setBlock(LevelAccessor worldIn, BlockPos pos) {
      worldIn.setBlock(pos, (BlockState)SPIKE.get(), 2);
   }
}
