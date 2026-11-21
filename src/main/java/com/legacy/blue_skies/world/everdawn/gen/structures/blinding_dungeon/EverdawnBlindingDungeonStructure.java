package com.legacy.blue_skies.world.everdawn.gen.structures.blinding_dungeon;

import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.world.general_features.structures.BlindingDungeonStructure;
import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class EverdawnBlindingDungeonStructure extends BlindingDungeonStructure {
   public static final Codec<EverdawnBlindingDungeonStructure> CODEC = Structure.simpleCodec(EverdawnBlindingDungeonStructure::new);

   public EverdawnBlindingDungeonStructure(StructureSettings settings) {
      super(settings);
   }

   private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      Rotation rotation = Rotation.values()[context.random().nextInt(Rotation.values().length)];
      int y = BlindingDungeonStructure.getHighestY(context.heightAccessor(), context.chunkGenerator(), context.randomState(), chunkPos.x, chunkPos.z);
      if (y < 100 && y >= 60) {
         EverdawnBlindingDungeonPieces.assemble(
            context.structureTemplateManager(),
            new BlockPos(chunkPos.x * 16 + 8, y, chunkPos.z * 16 + 8),
            rotation,
            builder,
            context.random(),
            context.randomState()
         );
      }
   }

   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      return isValidHeight(context) ? onTopOfChunkCenter(context, Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, context)) : Optional.empty();
   }

   public StructureType<?> type() {
      return SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getType();
   }
}
