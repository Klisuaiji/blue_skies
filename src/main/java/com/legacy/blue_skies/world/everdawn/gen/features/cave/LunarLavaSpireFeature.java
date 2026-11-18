package com.legacy.blue_skies.world.everdawn.gen.features.cave;

import com.legacy.blue_skies.blocks.natural.SkyDripstoneBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.cave.AbstractCaveFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LunarLavaSpireFeature extends AbstractCaveFeature<NoneFeatureConfiguration> {
   public LunarLavaSpireFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      MutableBlockPos newPos = pos.mutable();
      int xSpread = 7;
      int zSpread = 7;

      for (int j = 0; j < 10; j++) {
         newPos.setWithOffset(pos, rand.nextInt(xSpread + 2) - rand.nextInt(xSpread + 2), 0, rand.nextInt(zSpread + 2) - rand.nextInt(zSpread + 2));

         while (world.getFluidState(newPos).is(FluidTags.LAVA) && newPos.getY() < world.getSeaLevel()) {
            world.setBlock(newPos, SkiesBlocks.lunar_stone.defaultBlockState(), 2);
            newPos.move(Direction.UP);
         }

         if (world.isEmptyBlock(newPos)) {
            int height = j == 0 ? 6 : 2 + rand.nextInt(3);

            for (int i = 0; i < height; i++) {
               BlockState state = SkiesBlocks.lunar_stone.defaultBlockState();
               if (i == height - 1) {
                  state = (BlockState)SkiesBlocks.lunar_dripstone.defaultBlockState().setValue(SkyDripstoneBlock.SECTION, SkyDripstoneBlock.Section.TIP);
               }

               if (i == height - 2) {
                  state = (BlockState)SkiesBlocks.lunar_dripstone.defaultBlockState().setValue(SkyDripstoneBlock.SECTION, SkyDripstoneBlock.Section.MIDDLE);
               }

               if (i == height - 3) {
                  state = (BlockState)SkiesBlocks.lunar_dripstone.defaultBlockState().setValue(SkyDripstoneBlock.SECTION, SkyDripstoneBlock.Section.BASE);
               }

               world.setBlock(newPos.above(i), state, 2);
            }
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      return world.getFluidState(pos).is(FluidTags.LAVA) && !world.getFluidState(pos.below()).is(FluidTags.LAVA);
   }

   public int getMaxHeight(NoneFeatureConfiguration config) {
      return 10;
   }
}
