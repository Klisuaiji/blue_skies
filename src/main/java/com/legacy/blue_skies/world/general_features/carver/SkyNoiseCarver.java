package com.legacy.blue_skies.world.general_features.carver;

import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import com.legacy.blue_skies.util.MLSimplexNoise;
import com.legacy.structure_gel.core.util.Internal;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

public class SkyNoiseCarver extends WorldCarver<CarverConfiguration> {
   private final float xScale = 50.0F;
   private final float yScale = 25.0F;
   private final float zScale = 50.0F;
   private final int maxGenHeight;

   public SkyNoiseCarver(Codec<CarverConfiguration> codec, int genHeight) {
      super(codec);
      this.maxGenHeight = genHeight;
   }

   @Deprecated
   @Internal
   public boolean carve(
      CarvingContext context,
      CarverConfiguration config,
      ChunkAccess chunk,
      Function<BlockPos, Holder<Biome>> biomePos,
      RandomSource rand,
      Aquifer aquifer,
      ChunkPos offsetChunkPos,
      CarvingMask carvingMask
   ) {
      return offsetChunkPos.x - chunk.getPos().x == 0 && offsetChunkPos.z - chunk.getPos().z == 0
         ? this.carve(chunk, rand, offsetChunkPos.x, offsetChunkPos.z, new MLSimplexNoise(1337L))
         : false;
   }

   public boolean carve(ChunkAccess chunk, RandomSource rand, int chunkX, int chunkZ, MLSimplexNoise noise) {
      int chunkWidth = 16;
      int cX = chunkX * chunkWidth;
      int cZ = chunkZ * chunkWidth;

      for (int x = 0; x < chunkWidth; x++) {
         int posX = cX + x;

         for (int z = 0; z < chunkWidth; z++) {
            int posZ = cZ + z;

            for (int posY = Math.min(chunk.getHeight(Types.OCEAN_FLOOR, x, z) - 8, this.maxGenHeight); posY > 0; posY--) {
               if (noise.getValue(posX / 50.0F, posY / 25.0F, posZ / 50.0F) < -0.4446F) {
                  BlockPos pos = new BlockPos(posX, posY, posZ);
                  BlockState state = chunk.getBlockState(pos);
                  if (state.is(SkiesBlockTags.CARVER_REPLACEABLES) && state.getFluidState().isEmpty()) {
                     chunk.setBlockState(pos, this.getStateForPlace(rand, posY), false);
                  }
               }
            }
         }
      }

      return true;
   }

   public boolean isStartChunk(CarverConfiguration config, RandomSource rand) {
      return true;
   }

   protected BlockState getStateForPlace(RandomSource rand, int y) {
      return y <= 10 ? Blocks.LAVA.defaultBlockState() : CAVE_AIR;
   }
}
