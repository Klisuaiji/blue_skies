package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SkyIceAndSnowFeature extends Feature<NoneFeatureConfiguration> {
   public SkyIceAndSnowFeature(Codec<NoneFeatureConfiguration> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      MutableBlockPos blockpos$mutable = new MutableBlockPos();
      MutableBlockPos blockpos$mutable1 = new MutableBlockPos();

      for (int i = 0; i < 16; i++) {
         for (int j = 0; j < 16; j++) {
            int k = pos.getX() + i;
            int l = pos.getZ() + j;
            int i1 = level.getHeight(Types.MOTION_BLOCKING, k, l);
            blockpos$mutable.set(k, i1, l);
            blockpos$mutable1.set(blockpos$mutable).move(Direction.DOWN, 1);
            Biome biome = (Biome)level.getBiome(blockpos$mutable).value();
            if (biome.shouldFreeze(level, blockpos$mutable1, false)) {
               level.setBlock(blockpos$mutable1, Blocks.ICE.defaultBlockState(), 2);
            }

            if (biome.shouldSnow(level, blockpos$mutable) && !level.getBlockState(blockpos$mutable1).is(SkiesBlocks.coarse_turquoise_dirt)) {
               level.setBlock(blockpos$mutable, Blocks.SNOW.defaultBlockState(), 2);
               BlockState blockstate = level.getBlockState(blockpos$mutable1);
               if (blockstate.hasProperty(SnowyDirtBlock.SNOWY)) {
                  level.setBlock(blockpos$mutable1, (BlockState)blockstate.setValue(SnowyDirtBlock.SNOWY, true), 2);
               }
            }
         }
      }

      return true;
   }
}
