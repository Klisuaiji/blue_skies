package com.legacy.blue_skies.world.general_features.cave;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.world.general_features.AbstractSkyTreeFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CaveVineFeature extends AbstractCaveFeature<CaveVineFeature.Config> {
   public CaveVineFeature(Codec<CaveVineFeature.Config> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveVineFeature.Config config) {
      AbstractSkyTreeFeature.placeVines(world, pos, rand, 11, 3, 2, 8, 0.15F, config.vine, ImmutableSet.of());
      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveVineFeature.Config config) {
      return world.isEmptyBlock(pos);
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<CaveVineFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(BlockState.CODEC.fieldOf("vine").forGetter(config -> config.vine)).apply(instance, CaveVineFeature.Config::new)
      );
      public final BlockState vine;

      public Config(Block vine) {
         this.vine = vine instanceof VineBlock ? vine.defaultBlockState() : Blocks.VINE.defaultBlockState();
      }

      public Config(BlockState vine) {
         this(vine.getBlock());
      }
   }
}
