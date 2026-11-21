package com.legacy.blue_skies.world.everdawn.gen.features.cave;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.blue_skies.world.general_features.cave.AbstractCaveFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CaveCrystalFeature extends AbstractCaveFeature<NoneFeatureConfiguration> {
   public CaveCrystalFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      for (int i = 0; i < 15 && world.isEmptyBlock(pos.above(i)); i++) {
         this.setBlock(world, pos.above(i), SkiesBlocks.vitreous_moonstone.defaultBlockState());

         for (Direction dir : DirectionUtil.HORIZONTAL) {
            if (rand.nextFloat() < 0.7F && i < 13) {
               this.setBlock(world, pos.above(i).relative(dir), SkiesBlocks.vitreous_moonstone.defaultBlockState());
            }
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      return world.getBlockState(pos.below()).canOcclude() && world.isEmptyBlock(pos);
   }
}
