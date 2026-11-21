package com.legacy.blue_skies.world.general_features.cave;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CavePlantFeature extends AbstractCaveFeature<CavePlantFeature.Config> {
   public CavePlantFeature(Codec<CavePlantFeature.Config> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CavePlantFeature.Config config) {
      for (int i = 0; i < config.tries; i++) {
         BlockState plant = config.plant;
         BlockPos placePos = pos.offset(
            rand.nextInt(config.radius * 2) - config.radius, rand.nextInt(config.radius * 2) - config.radius, rand.nextInt(config.radius * 2) - config.radius
         );
         if (world.isEmptyBlock(placePos) && plant.canSurvive(world, placePos) && !world.canSeeSkyFromBelowWater(placePos)) {
            this.setBlock(world, placePos, plant);
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CavePlantFeature.Config config) {
      return world.isEmptyBlock(pos);
   }

   public int getMaxHeight(CavePlantFeature.Config config) {
      return config.radius;
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<CavePlantFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               BlockState.CODEC.fieldOf("plant").forGetter(config -> config.plant),
               Codec.INT.fieldOf("tries").forGetter(config -> config.tries),
               Codec.INT.fieldOf("radius").forGetter(config -> config.radius)
            )
            .apply(instance, CavePlantFeature.Config::new)
      );
      public final BlockState plant;
      public final int tries;
      public final int radius;

      public Config(BlockState plant, int tries, int radius) {
         this.plant = plant;
         this.tries = tries;
         this.radius = radius;
      }
   }
}
