package com.legacy.blue_skies.world.everbright.gen.features.cave;

import com.legacy.blue_skies.world.general_features.cave.AbstractCaveFeature;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GiantCaveIcicleFeature extends AbstractCaveFeature<NoneFeatureConfiguration> {
   public GiantCaveIcicleFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      MutableBlockPos newPos = pos.mutable();
      int xSpread = 7;
      int zSpread = 7;

      for (int j = 0; j < 25; j++) {
         newPos.setWithOffset(
            pos,
            rand.nextInt(xSpread + 2) - rand.nextInt(xSpread + 2),
            rand.nextInt(4) - rand.nextInt(4),
            rand.nextInt(zSpread + 2) - rand.nextInt(zSpread + 2)
         );
         if (this.isValidPos(world, chunkGen, rand, newPos, config)) {
            for (int k = 0; k < rand.nextInt(3) + 5 && world.isEmptyBlock(newPos); k++) {
               world.setBlock(newPos, k < 1 + rand.nextInt(2) ? Blocks.PACKED_ICE.defaultBlockState() : Blocks.ICE.defaultBlockState(), 2);
               newPos.move(Direction.DOWN);
            }
         }
      }

      return true;
   }

   public boolean isValidPos(WorldGenLevel world, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, NoneFeatureConfiguration config) {
      return Block.canSupportCenter(world, pos.above(), Direction.DOWN) && world.isEmptyBlock(pos);
   }

   public int getMaxHeight(NoneFeatureConfiguration config) {
      return 10;
   }
}
