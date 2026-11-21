package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.blocks.natural.GlimmerReedBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GlimmerReedFeature extends Feature<NoneFeatureConfiguration> {
   public GlimmerReedFeature(Codec<NoneFeatureConfiguration> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      int maxHeight = new Random(new BlockPos(pos.getX(), 0, pos.getZ()).asLong()).nextInt(2) + 2;
      int oceanFloor = level.getHeight(Types.OCEAN_FLOOR, pos.getX(), pos.getZ());
      BlockPos blockPos = new BlockPos(pos.getX(), oceanFloor, pos.getZ());
      if (oceanFloor > 59 && oceanFloor < level.getSeaLevel() && !level.getFluidState(blockPos).isEmpty()) {
         int topHeight = level.getSeaLevel() + 1 + maxHeight - oceanFloor;

         for (int i = 0; i < topHeight; i++) {
            BlockState state = (BlockState)SkiesBlocks.glimmer_reed.defaultBlockState().setValue(GlimmerReedBlock.TOP, i == topHeight - 1);
            BlockPos upPos = blockPos.above(i);
            if (state.canSurvive(level, upPos)) {
               level.setBlock(upPos, (BlockState)state.setValue(GlimmerReedBlock.WATERLOGGED, level.getFluidState(upPos).is(FluidTags.WATER)), 2);
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
