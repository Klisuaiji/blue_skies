package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.blocks.natural.BrumbleVineTopBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BrumbleVineFeature extends Feature<NoneFeatureConfiguration> {
   public BrumbleVineFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      RandomSource rand = level.getRandom();
      this.placeVine(level, pos, rand, 10);
      int extraVines = rand.nextInt(2) + 2;

      for (int i = 0; i < extraVines; i++) {
         this.placeVine(level, pos.offset(rand.nextInt(11) - 5, 0, rand.nextInt(11) - 5), rand, rand.nextInt(3) + 2);
      }

      return true;
   }

   private void placeVine(WorldGenLevel world, BlockPos pos, RandomSource rand, int minHeight) {
      pos = world.getHeightmapPos(Types.OCEAN_FLOOR, pos);
      int maxHeight = rand.nextInt(5) + minHeight;
      int height = 0;

      while (height < world.getSeaLevel() && height < maxHeight) {
         height++;
      }

      for (int y = 0; y < height; y++) {
         BlockPos offsetPos = pos.above(y);
         BlockState top = (BlockState)SkiesBlocks.brumble_vine_top.defaultBlockState().setValue(BrumbleVineTopBlock.AGE, 25);
         BlockState vineState = y == height - 1 ? top : SkiesBlocks.brumble_vine.defaultBlockState();
         if (world.getBlockState(offsetPos).getBlock() != Blocks.WATER || !vineState.canSurvive(world, offsetPos)) {
            if (world.getBlockState(offsetPos.below()).getBlock() == SkiesBlocks.brumble_vine) {
               world.setBlock(offsetPos.below(), top, 2);
            }
            break;
         }

         world.setBlock(offsetPos, vineState, 2);
      }
   }
}
