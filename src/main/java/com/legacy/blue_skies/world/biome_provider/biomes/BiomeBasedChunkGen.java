package com.legacy.blue_skies.world.biome_provider.biomes;

import com.legacy.blue_skies.world.SkiesChunkGenerator;
import net.minecraft.world.level.block.state.BlockState;

public record BiomeBasedChunkGen(int minY, int maxY, BiomeBasedChunkGen.GenFunc genFunc) {
   public static final BiomeBasedChunkGen DEFAULT = new BiomeBasedChunkGen(0, 0, (original, chunkGen, height, x, y, z) -> original);
   public static final BiomeBasedChunkGen UNORTHODX_VALLEY = new BiomeBasedChunkGen(165, 225, (original, chunkGen, height, x, y, z) -> {
      if (y > height + 16) {
         double xzScale = 0.006;
         double yScale = 0.01;
         double noiseVal = chunkGen.noiseA.getValue(x * xzScale, y * yScale, z * xzScale);
         xzScale *= 4.0;
         yScale *= 5.0;
         if (noiseVal * 0.75 + chunkGen.noiseA.getValue(x * xzScale, y * yScale, z * xzScale) * 0.25 > 0.75) {
            return chunkGen.defaultBlock();
         }
      }

      return original;
   });

   public interface GenFunc {
      BlockState apply(BlockState var1, SkiesChunkGenerator var2, int var3, int var4, int var5, int var6);
   }
}
