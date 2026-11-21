package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.PatchyBlockFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SeaMossFeature extends PatchyBlockFeature<NoneFeatureConfiguration> {
   public SeaMossFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public void placeBlock(WorldGenLevel world, ChunkGenerator generator, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      BlockPos placementPos = world.getHeightmapPos(Types.OCEAN_FLOOR, pos);
      if (world.getBlockState(placementPos).is(Blocks.WATER)) {
         BlockState placementState = SkiesBlocks.sea_moss.getStateForPlacement(SkiesBlocks.sea_moss.defaultBlockState(), world, placementPos);
         if (placementState.canSurvive(world, placementPos)) {
            world.setBlock(placementPos, placementState, 2);
         }
      }
   }
}
