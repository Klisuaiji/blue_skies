package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.util.LogicUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class MoonstoneSpikeFeature extends Feature<MoonstoneSpikeFeature.Config> {
   protected static final Lazy<BlockState> SPIKE = Lazy.of(() -> SkiesBlocks.vitreous_moonstone.defaultBlockState());

   public MoonstoneSpikeFeature(Codec<MoonstoneSpikeFeature.Config> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<MoonstoneSpikeFeature.Config> context) {
      WorldGenLevel world = context.level();
      BlockPos pos = context.origin();
      RandomSource rand = world.getRandom();
      MoonstoneSpikeFeature.Config config = (MoonstoneSpikeFeature.Config)context.config();
      int maxHeight = rand.nextInt(config.maxHeight - config.minHeight + 1) + config.minHeight;

      for (int height = 0; height < 26; height++) {
         if (world.getBlockState(pos.above(height - 1)).getBlock() == SkiesBlocks.crystal_sand
            && world.getBlockState(pos.above(height)).getBlock() == Blocks.AIR) {
            for (int y = height; y <= maxHeight + height; y++) {
               this.setBlock(world, pos.above(y));
            }

            int radius = rand.nextInt(config.maxRadius - config.minRadius + 1) + config.minRadius;
            double slope = maxHeight / (radius + 1.0);

            for (int i = 1; i <= radius; i++) {
               for (int x = -i; x <= i; x++) {
                  for (int z = -i; z <= i; z++) {
                     if (Math.abs(x) + Math.abs(z) == i) {
                        for (int y = height; y <= Math.max(1, maxHeight + height - LogicUtil.plusOrMinus((int)(slope * i), 2, rand)); y++) {
                           this.setBlock(world, pos.offset(x, y, z));
                        }
                     }
                  }
               }
            }

            for (int x = -radius; x <= radius; x++) {
               for (int zx = -radius; zx <= radius; zx++) {
                  if (Math.abs(x) + Math.abs(zx) <= radius + 1) {
                     for (int y = -2; y <= 0; y++) {
                        this.setBlock(world, pos.offset(x, height + y, zx));
                     }
                  }
               }
            }

            return true;
         }
      }

      return false;
   }

   private void setBlock(LevelAccessor world, BlockPos pos) {
      world.setBlock(pos, (BlockState)SPIKE.get(), 2);
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<MoonstoneSpikeFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               Codec.INT.fieldOf("min_height").forGetter(config -> config.minHeight),
               Codec.INT.fieldOf("max_height").forGetter(config -> config.maxHeight),
               Codec.INT.fieldOf("min_radius").forGetter(config -> config.minRadius),
               Codec.INT.fieldOf("max_radius").forGetter(config -> config.maxRadius)
            )
            .apply(instance, MoonstoneSpikeFeature.Config::new)
      );
      public final int minHeight;
      public final int maxHeight;
      public final int minRadius;
      public final int maxRadius;

      public Config(int minHeight, int maxHeight, int minRadius, int maxRadius) {
         this.minHeight = Math.max(0, minHeight);
         this.maxHeight = Math.max(minHeight, maxHeight);
         this.minRadius = Math.max(0, minRadius);
         this.maxRadius = Math.max(minRadius, maxRadius);
      }
   }
}
