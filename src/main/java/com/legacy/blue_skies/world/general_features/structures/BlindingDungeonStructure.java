package com.legacy.blue_skies.world.general_features.structures;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;

public abstract class BlindingDungeonStructure extends Structure {
   public BlindingDungeonStructure(StructureSettings settings) {
      super(settings);
   }

   protected static boolean isValidHeight(GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      return getHighestY(context.heightAccessor(), context.chunkGenerator(), context.randomState(), chunkPos.x, chunkPos.z) >= 60;
   }

   protected static int getHighestY(LevelHeightAccessor level, ChunkGenerator chunkGen, RandomState randomState, int chunkX, int chunkZ) {
      int width = 18;
      int x = chunkX * 16 + 8;
      int z = chunkZ * 16 + 8;
      int y = 0;

      for (int dx = 0; dx < 2; dx++) {
         for (int dz = 0; dz < 2; dz++) {
            int h = chunkGen.getBaseHeight(dx * width + x, dz * width + z, Types.WORLD_SURFACE_WG, level, randomState);
            if (y > 0 && (h > y + 9 || h < y - 9)) {
               return 0;
            }

            y = Math.max(y, h);
         }
      }

      return y;
   }
}
