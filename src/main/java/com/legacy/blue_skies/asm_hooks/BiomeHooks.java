package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class BiomeHooks {
   public static boolean preventSnow(Biome biome, LevelReader reader, BlockPos pos) {
      return biome.coldEnoughToSnow(pos)
         && reader instanceof WorldGenLevel levelGen
         && pos.getY() > 145
         && StructureAccessHelper.isInStructure(
            levelGen, (Structure)SkiesStructures.NATURE_DUNGEON.getStructure().get(levelGen.registryAccess().registryOrThrow(Registries.STRUCTURE)), pos
         );
   }
}
