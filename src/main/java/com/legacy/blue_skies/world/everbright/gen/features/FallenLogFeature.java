package com.legacy.blue_skies.world.everbright.gen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class FallenLogFeature extends Feature<FallenLogFeature.Config> {
   public FallenLogFeature(Codec<FallenLogFeature.Config> configFactoryIn) {
      super(configFactoryIn);
   }

   public boolean place(FeaturePlaceContext<FallenLogFeature.Config> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      RandomSource rand = level.getRandom();
      Axis axis = rand.nextBoolean() ? Axis.X : Axis.Z;
      int length = rand.nextInt(2) + 3;

      for (int i = 0; i < length; i++) {
         BlockPos blockPos = pos.offset(axis == Axis.X ? i : 0, 0, axis == Axis.Z ? i : 0);
         if (level.getBlockState(blockPos) != Blocks.AIR.defaultBlockState() || !level.getBlockState(blockPos.below()).is(BlockTags.DIRT)) {
            return false;
         }
      }

      for (int i = 0; i < length; i++) {
         BlockPos blockPos = pos.offset(axis == Axis.X ? i : 0, 0, axis == Axis.Z ? i : 0);
         level.setBlock(blockPos, (BlockState)((FallenLogFeature.Config)context.config()).state.setValue(RotatedPillarBlock.AXIS, axis), 3);
      }

      return true;
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<FallenLogFeature.Config> CODEC = RecordCodecBuilder.create(
         inst -> inst.group(BlockState.CODEC.fieldOf("state").forGetter(config -> config.state)).apply(inst, FallenLogFeature.Config::new)
      );
      public final BlockState state;

      public Config(BlockState state) {
         this.state = state;
      }
   }
}
