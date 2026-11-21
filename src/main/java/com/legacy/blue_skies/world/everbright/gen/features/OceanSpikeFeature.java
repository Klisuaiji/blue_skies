package com.legacy.blue_skies.world.everbright.gen.features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.world.general_features.IStructureRestricted;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.common.util.Lazy;

public class OceanSpikeFeature extends Feature<NoneFeatureConfiguration> implements IStructureRestricted {
   private static final Lazy<BlockState> SAND = Lazy.of(() -> SkiesBlocks.midnight_sand.defaultBlockState());
   private static final Lazy<BlockState> SANDSTONE = Lazy.of(() -> SkiesBlocks.midnight_sandstone.defaultBlockState());

   public OceanSpikeFeature(Codec<NoneFeatureConfiguration> configFactoryIn) {
      super(configFactoryIn);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      pos = level.getHeightmapPos(Types.OCEAN_FLOOR, pos);
      if (!this.shouldGenerate(level, pos)) {
         return false;
      }

      RandomSource rand = level.getRandom();
      if (pos.getY() > level.getSeaLevel()) {
         return false;
      }

      if (rand.nextBoolean()) {
         int centerHeight = rand.nextInt(2) + 7;
         if (pos.getY() + centerHeight >= level.getSeaLevel() - 1) {
            return false;
         }

         for (int y = 0; y < centerHeight; y++) {
            this.setBlock(level, pos.above(y), y == centerHeight - 1);

            for (int i = -1; i <= 1 && (y < 4 || y < 5 && rand.nextBoolean()); i += 2) {
               this.setBlock(level, pos.offset(i, y, 0), y >= 3);
               this.setBlock(level, pos.offset(0, y, i), y >= 3);
            }

            for (int i = -1; i <= 1 && (y < 2 || y < 3 && rand.nextBoolean()); i += 2) {
               this.setBlock(level, pos.offset(i, y, i), y >= 2);
               this.setBlock(level, pos.offset(-i, y, i), y >= 2);
            }

            for (int i = -2; i <= 2 && y < 1; i += 4) {
               this.setBlock(level, pos.offset(i, y, 0), true);
               this.setBlock(level, pos.offset(0, y, i), true);
            }
         }

         int maxXZ = 2;

         for (int x = -maxXZ; x <= maxXZ; x++) {
            for (int z = -maxXZ; z <= maxXZ; z++) {
               for (int y = -1; y >= -2; y--) {
                  if (Math.abs(x) != maxXZ || Math.abs(x) != Math.abs(z)) {
                     this.setBlock(level, pos.offset(x, y, z), true);
                  }
               }
            }
         }

         return true;
      } else {
         if (!rand.nextBoolean()) {
            return false;
         }

         for (int y = 0; y <= 2; y++) {
            this.setBlock(level, pos.above(y), y >= 2);
         }

         for (int y = 0; y >= -2; y--) {
            for (int xz = -1; xz <= 1; xz += 2) {
               this.setBlock(level, pos.offset(xz, y, 0), false);
               this.setBlock(level, pos.offset(0, y, xz), false);
            }
         }

         boolean x = rand.nextBoolean();
         boolean positive = rand.nextBoolean();
         this.setBlock(level, pos.offset(x ? 1 * (positive ? 1 : -1) : 0, 1, x ? 0 : 1 * (positive ? 1 : -1)), true);
         return true;
      }
   }

   private void setBlock(LevelAccessor world, BlockPos pos, boolean sand) {
      world.setBlock(pos, sand ? (BlockState)SAND.get() : (BlockState)SANDSTONE.get(), 2);
   }
}
