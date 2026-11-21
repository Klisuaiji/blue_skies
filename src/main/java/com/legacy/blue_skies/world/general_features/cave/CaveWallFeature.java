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

public class CaveWallFeature extends AbstractCaveFeature<CaveWallFeature.Config> {
   public CaveWallFeature(Codec<CaveWallFeature.Config> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveWallFeature.Config config) {
      int width = rand.nextInt(6) + 5;
      int height = rand.nextInt(6) + 5;
      int depth = rand.nextInt(6) + 5;

      for (int x = -width; x <= width; x++) {
         for (int z = -depth; z <= depth; z++) {
            for (int y = -height; y <= height; y++) {
               if (GeometryHelper.isInEllipsoid(width, height, depth, x, y, z)) {
                  BlockPos offsetPos = pos.offset(x, y, z);
                  boolean airInOneDir = world.isEmptyBlock(offsetPos.north())
                     || world.isEmptyBlock(offsetPos.south())
                     || world.isEmptyBlock(offsetPos.east())
                     || world.isEmptyBlock(offsetPos.west())
                     || world.isEmptyBlock(offsetPos.below());
                  BlockState state = world.getBlockState(offsetPos);
                  if (SkiesFeatures.Carvers.CARVABLE_BLOCKS.contains(state.getBlock())
                     && !state.is(BlockTags.ICE)
                     && !world.isEmptyBlock(offsetPos.above())
                     && airInOneDir) {
                     if (rand.nextFloat() < 0.3F) {
                        this.setBlock(world, offsetPos, config.accentBlock);
                     } else {
                        this.setBlock(world, offsetPos, config.mainBlock);
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveWallFeature.Config config) {
      return world.isEmptyBlock(pos.above());
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<CaveWallFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               BlockState.CODEC.fieldOf("main_block").forGetter(config -> config.mainBlock),
               BlockState.CODEC.fieldOf("accent_block").forGetter(config -> config.accentBlock)
            )
            .apply(instance, CaveWallFeature.Config::new)
      );
      public final BlockState mainBlock;
      public final BlockState accentBlock;

      public Config(BlockState mainBlock, BlockState accentBlock) {
         this.mainBlock = mainBlock;
         this.accentBlock = accentBlock;
      }

      public Config(BlockState mainBlock) {
         this(mainBlock, mainBlock);
      }
   }
}
