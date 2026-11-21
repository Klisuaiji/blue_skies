package com.legacy.blue_skies.world.general_features.cave;

import com.legacy.blue_skies.registries.SkiesFeatures;
import com.legacy.blue_skies.util.GeometryHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CaveGrassFeature extends AbstractCaveFeature<CaveGrassFeature.Config> {
   public CaveGrassFeature(Codec<CaveGrassFeature.Config> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveGrassFeature.Config config) {
      int width = rand.nextInt(6) + 5;
      int height = rand.nextInt(6) + 5;
      int depth = rand.nextInt(6) + 5;
      float grassChance = rand.nextFloat() * 0.25F;

      for (int x = -width; x <= width; x++) {
         for (int z = -depth; z <= depth; z++) {
            for (int y = -height; y <= height; y++) {
               if (GeometryHelper.isInEllipsoid(width, height, depth, x, y, z)) {
                  BlockPos offsetPos = pos.offset(x, y, z);
                  BlockState state = world.getBlockState(offsetPos);
                  if (SkiesFeatures.Carvers.CARVABLE_BLOCKS.contains(state.getBlock()) && !state.is(BlockTags.ICE) && world.isEmptyBlock(offsetPos.above())) {
                     this.setBlock(world, offsetPos, config.grass_block);
                     if (rand.nextFloat() < grassChance) {
                        this.setBlock(world, offsetPos.above(), config.grass);
                     }

                     if (rand.nextBoolean() && offsetPos.below(1).getY() > 2) {
                        this.setBlock(world, offsetPos.below(1), config.dirt);
                        if (rand.nextBoolean() && offsetPos.below(2).getY() > 2) {
                           this.setBlock(world, offsetPos.below(2), config.dirt);
                        }
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveGrassFeature.Config config) {
      return true;
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<CaveGrassFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               BlockState.CODEC.fieldOf("grass_block").forGetter(config -> config.grass_block),
               BlockState.CODEC.fieldOf("dirt").forGetter(config -> config.dirt),
               BlockState.CODEC.fieldOf("grass").forGetter(config -> config.grass)
            )
            .apply(instance, CaveGrassFeature.Config::new)
      );
      public final BlockState grass_block;
      public final BlockState dirt;
      public final BlockState grass;

      public Config(BlockState grass_block, BlockState dirt, BlockState grass) {
         this.grass_block = grass_block;
         this.dirt = dirt;
         this.grass = grass;
      }
   }
}
