package com.legacy.blue_skies.world.everbright.gen.features;

import com.google.common.collect.ImmutableSet;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.PatchyBlockFeature;
import com.mojang.serialization.Codec;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class PatchySnowFeature extends PatchyBlockFeature<NoneFeatureConfiguration> {
   private static final Lazy<Set<Block>> INVALID_BLOCKS = Lazy.of(
      () -> ImmutableSet.of(SkiesBlocks.stripped_frostbright_wood, SkiesBlocks.frostbright_planks, SkiesBlocks.midnight_glass)
   );

   public PatchySnowFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public void placeBlock(WorldGenLevel world, ChunkGenerator generator, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      BlockPos placementPos = world.getHeightmapPos(Types.WORLD_SURFACE, pos);
      if (world.isEmptyBlock(placementPos)) {
         BlockState placementState = Blocks.SNOW.defaultBlockState();
         if (placementState.canSurvive(world, placementPos) && !((Set)INVALID_BLOCKS.get()).contains(world.getBlockState(placementPos.below()).getBlock())) {
            BlockState lowerState = world.getBlockState(placementPos.below());
            if (!(lowerState.getBlock() instanceof HugeMushroomBlock)) {
               world.setBlock(placementPos, placementState, 2);
               if (lowerState.hasProperty(BlockStateProperties.SNOWY)) {
                  world.setBlock(placementPos.below(), (BlockState)lowerState.setValue(BlockStateProperties.SNOWY, true), 2);
               }
            }
         }
      }
   }
}
