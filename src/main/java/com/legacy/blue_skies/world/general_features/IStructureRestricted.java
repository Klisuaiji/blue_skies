package com.legacy.blue_skies.world.general_features;

import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.structure_gel.api.registry.RegistryHelper;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.Structure;

public interface IStructureRestricted {
   default TagKey<Structure> disallowedStructures() {
      return SkiesStructureTags.DUNGEONS;
   }

   default boolean pieceSpecific() {
      return false;
   }

   default boolean shouldGenerate(WorldGenLevel level, BlockPos pos) {
      return this.disallowedStructures() == null
         || level.getLevel().structureManager().startsForStructure(new ChunkPos(pos), structure -> this.test(level, structure, pos)).isEmpty();
   }

   default boolean test(WorldGenLevel level, Structure structure, BlockPos pos) {
      return RegistryHelper.isInTag(level.registryAccess().registryOrThrow(Registries.STRUCTURE), this.disallowedStructures(), structure)
         && (!this.pieceSpecific() || this.pieceSpecific() && StructureAccessHelper.isInStructurePiece(level, structure, pos));
   }
}
