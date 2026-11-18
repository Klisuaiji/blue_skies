package com.legacy.blue_skies.world.everdawn.gen.features.cave;

import com.legacy.blue_skies.registries.SkiesFeatures;
import com.legacy.blue_skies.util.GeometryHelper;
import com.legacy.blue_skies.world.general_features.cave.AbstractCaveFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CaveMagmaFeature extends AbstractCaveFeature<NoneFeatureConfiguration> {
   public CaveMagmaFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      pos = new BlockPos(pos.getX(), 10, pos.getZ());
      int width = rand.nextInt(6) + 5;
      int height = rand.nextInt(7) + 6;
      int depth = rand.nextInt(6) + 5;

      for (int x = -width; x <= width; x++) {
         for (int z = -depth; z <= depth; z++) {
            for (int y = -height; y <= height; y++) {
               if (GeometryHelper.isInEllipsoid(width, height, depth, x, y, z)) {
                  BlockPos offsetPos = pos.offset(x, y, z);
                  if (SkiesFeatures.Carvers.CARVABLE_BLOCKS.contains(world.getBlockState(offsetPos).getBlock())) {
                     float chance;
                     if (y <= 1) {
                        chance = 1.0F;
                     } else if (y <= height / 2.0F) {
                        chance = 0.25F;
                     } else {
                        chance = 0.05F;
                     }

                     if (rand.nextFloat() < chance) {
                        this.setBlock(world, offsetPos, Blocks.MAGMA_BLOCK.defaultBlockState());
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      return world.getFluidState(new BlockPos(pos.getX(), 10, pos.getZ())).is(FluidTags.LAVA);
   }
}
