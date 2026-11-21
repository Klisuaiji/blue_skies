package com.legacy.blue_skies.world.everdawn.gen.structures.poison_dungeon;

import com.legacy.blue_skies.registries.SkiesStructures;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class PoisonDungeonStructure extends Structure {
   public static final Codec<PoisonDungeonStructure> CODEC = Structure.simpleCodec(PoisonDungeonStructure::new);

   public PoisonDungeonStructure(StructureSettings settings) {
      super(settings);
   }

   private void generatePieces(StructurePiecesBuilder builder, GenerationContext context) {
      ChunkGenerator chunkGen = context.chunkGenerator();
      ChunkPos chunkPos = context.chunkPos();
      Rotation rotation = Rotation.getRandom(context.random());
      int i = getYValue(context.heightAccessor(), chunkGen, chunkPos, context.randomState());
      if (i >= 60 || chunkGen instanceof FlatLevelSource) {
         BlockPos blockpos = new BlockPos(chunkPos.x * 16 + 8, i, chunkPos.z * 16 + 8);
         PoisonDungeonPieces.assemble(context.structureTemplateManager(), blockpos, rotation, builder, context.random(), context.randomState());
      }
   }

   public static int getYValue(LevelHeightAccessor level, ChunkGenerator chunkGen, ChunkPos chunkPos, RandomState randomState) {
      Random random = new Random(chunkPos.x + chunkPos.z * 10387313);
      Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
      int i = 5;
      int j = 5;
      if (rotation == Rotation.CLOCKWISE_90) {
         i = -5;
      } else if (rotation == Rotation.CLOCKWISE_180) {
         i = -5;
         j = -5;
      } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
         j = -5;
      }

      int k = (chunkPos.x << 4) + 7;
      int l = (chunkPos.z << 4) + 7;
      int i1 = chunkGen.getFirstOccupiedHeight(k, l, Types.WORLD_SURFACE_WG, level, randomState);
      int j1 = chunkGen.getFirstOccupiedHeight(k, l + j, Types.WORLD_SURFACE_WG, level, randomState);
      int k1 = chunkGen.getFirstOccupiedHeight(k + i, l, Types.WORLD_SURFACE_WG, level, randomState);
      int l1 = chunkGen.getFirstOccupiedHeight(k + i, l + j, Types.WORLD_SURFACE_WG, level, randomState);
      return Math.min(Math.min(i1, j1), Math.min(k1, l1));
   }

   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      return onTopOfChunkCenter(context, Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, context));
   }

   public StructureType<?> type() {
      return SkiesStructures.POISON_DUNGEON.getType();
   }
}
