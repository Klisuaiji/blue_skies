package com.legacy.blue_skies.world.everbright.gen.structures.nature_dungeon;

import com.legacy.blue_skies.registries.SkiesStructures;
import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class NatureDungeonStructure extends Structure {
   public static final Codec<NatureDungeonStructure> CODEC = Structure.simpleCodec(NatureDungeonStructure::new);

   public NatureDungeonStructure(StructureSettings settings) {
      super(settings);
   }

   public void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      WorldgenRandom random = context.random();
      NatureDungeonPieces.assemble(
         context.structureTemplateManager(),
         new BlockPos(chunkPos.x * 16 + 8, 88, chunkPos.z * 16 + 8),
         Rotation.getRandom(random),
         builder,
         random,
         context.chunkGenerator(),
         context.heightAccessor(),
         context.randomState()
      );
   }

   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      return onTopOfChunkCenter(context, Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, context));
   }

   public StructureType<?> type() {
      return SkiesStructures.NATURE_DUNGEON.getType();
   }
}
