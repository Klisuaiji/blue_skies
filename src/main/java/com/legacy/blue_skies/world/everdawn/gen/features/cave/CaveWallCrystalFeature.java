package com.legacy.blue_skies.world.everdawn.gen.features.cave;

import com.legacy.blue_skies.util.EntityUtil;
import com.legacy.blue_skies.world.general_features.cave.AbstractCaveFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CaveWallCrystalFeature extends AbstractCaveFeature<CaveWallCrystalFeature.Config> {
   public CaveWallCrystalFeature(Codec<CaveWallCrystalFeature.Config> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel level, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveWallCrystalFeature.Config config) {
      for (int i = 0; i < config.tries; i++) {
         Direction facing = (Direction)Util.getRandom(Direction.values(), rand);
         BlockState crystal = (BlockState)config.cystal.setValue(BlockStateProperties.FACING, facing);
         BlockPos placePos = pos.offset(
            rand.nextInt(config.radius * 2) - config.radius, rand.nextInt(config.radius * 2) - config.radius, rand.nextInt(config.radius * 2) - config.radius
         );
         if (level.isEmptyBlock(placePos) && crystal.canSurvive(level, placePos) && !EntityUtil.canSeeSkyFromBelowWater(level, null, placePos)) {
            this.setBlock(level, placePos, crystal);
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, CaveWallCrystalFeature.Config config) {
      return world.isEmptyBlock(pos);
   }

   public int getMaxHeight(CaveWallCrystalFeature.Config config) {
      return config.radius;
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<CaveWallCrystalFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               BlockState.CODEC.fieldOf("crystal").forGetter(config -> config.cystal),
               Codec.INT.fieldOf("tries").forGetter(config -> config.tries),
               Codec.INT.fieldOf("radius").forGetter(config -> config.radius)
            )
            .apply(instance, CaveWallCrystalFeature.Config::new)
      );
      public final BlockState cystal;
      public final int tries;
      public final int radius;

      public Config(BlockState cystal, int tries, int radius) {
         this.cystal = cystal;
         this.tries = tries;
         this.radius = radius;
      }
   }
}
