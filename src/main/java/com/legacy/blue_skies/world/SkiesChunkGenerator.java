package com.legacy.blue_skies.world;

import com.google.common.collect.Sets;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeBasedChunkGen;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeData;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.DensityFunction.SinglePointContext;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.WorldgenRandom.Algorithm;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class SkiesChunkGenerator extends NoiseBasedChunkGenerator {
   public static final Codec<SkiesChunkGenerator> SKIES_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(chunkGen -> chunkGen.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(chunkGen -> chunkGen.generatorSettings())
         )
         .apply(instance, instance.stable(SkiesChunkGenerator::new))
   );
   public static final BlockState AIR = Blocks.AIR.defaultBlockState();
   public static final BlockState WATER = Blocks.WATER.defaultBlockState();
   public SimplexNoise noiseA = null;
   public SimplexNoise noiseB = null;
   public Long seed = null;
   private static final boolean DEBUG = false;
   private static long totalTime = 0L;
   private static long iterations = 0L;

   public SkiesChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
      super(biomeSource, settings);
   }

   private void setSeed(long seed) {
      seed += this.getSeedModifier();
      this.noiseA = new SimplexNoise(new WorldgenRandom(Algorithm.LEGACY.newInstance(seed)));
      this.noiseB = new SimplexNoise(new WorldgenRandom(Algorithm.LEGACY.newInstance(seed + 41L)));
      this.seed = seed;
      if (this.biomeSource instanceof SkiesBiomeSource) {
         ((SkiesBiomeSource)this.biomeSource).setSeed(seed);
      }
   }

   protected int getSeedModifier() {
      return 0;
   }

   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureRegistry, RandomState rand, long serverSeed) {
      this.setSeed(serverSeed);
      return super.createState(structureRegistry, rand, serverSeed);
   }

   protected Codec<? extends ChunkGenerator> codec() {
      return SKIES_CODEC;
   }

   public BlockState defaultBlock() {
      return ((NoiseGeneratorSettings)this.generatorSettings().value()).defaultBlock();
   }

   public int getBaseHeight(int x, int z, Types heightMap, LevelHeightAccessor level, RandomState randomState) {
      return level.getMinBuildHeight() + this.iterateColumn(x, z, new ArrayList<>(80), heightMap.isOpaque(), level);
   }

   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
      List<BlockState> states = new ArrayList<>(80);
      this.iterateColumn(x, z, states, null, level);
      return new NoiseColumn(level.getMinBuildHeight(), states.toArray(BlockState[]::new));
   }

   private int iterateColumn(int worldX, int worldZ, List<BlockState> states, Predicate<BlockState> stateTest, LevelHeightAccessor level) {
      int terrainHeight = this.getTerrainHeight(worldX, worldZ);
      int maxY = level.getMaxBuildHeight();

      for (int worldY = level.getMinBuildHeight(); worldY < maxY; worldY++) {
         BiomeData biomeData = BiomeIds.getData(this.getNoisyBiomeId(worldX, worldY, worldZ));
         BlockState state = this.getStateAt(worldX, worldY, worldZ, terrainHeight, biomeData);
         if (stateTest != null && !stateTest.test(state)) {
            break;
         }

         states.add(state);
      }

      return states.size();
   }

   public CompletableFuture<ChunkAccess> fillFromNoise(
      Executor exc, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess
   ) {
      NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.generatorSettings().value()).noiseSettings();
      LevelHeightAccessor level = chunkAccess.getHeightAccessorForGeneration();
      int minY = level.getMinBuildHeight();
      int height = level.getMaxBuildHeight();
      int upperCellCount = Math.floorDiv(height - minY, noiseSettings.getCellHeight());
      if (upperCellCount <= 0) {
         return CompletableFuture.completedFuture(chunkAccess);
      }

      int maxSectionIndex = chunkAccess.getSectionIndex(upperCellCount * noiseSettings.getCellHeight() - 1 + minY);
      int minSectionIndex = chunkAccess.getSectionIndex(minY);
      Set<LevelChunkSection> set = Sets.newHashSet();

      for (int i = maxSectionIndex; i >= minSectionIndex; i--) {
         LevelChunkSection section = chunkAccess.getSection(i);
         section.acquire();
         set.add(section);
      }

      return CompletableFuture.<ChunkAccess>supplyAsync(
            Util.wrapThreadWithTaskName("wgen_fill_noise", () -> this.doFill(blender, structureManager, chunkAccess)), Util.backgroundExecutor()
         )
         .whenCompleteAsync((ca, t) -> {
            for (LevelChunkSection secion : set) {
               secion.release();
            }
         }, exc);
   }

   private ChunkAccess doFill(Blender blender, StructureManager structureManager, ChunkAccess chunkAccess) {
      int secHeight = 16;
      int secWidth = 16;
      Beardifier beardifier = Beardifier.forStructuresInChunk(structureManager, chunkAccess.getPos());
      Heightmap oceanHM = chunkAccess.getOrCreateHeightmapUnprimed(Types.OCEAN_FLOOR_WG);
      Heightmap surfaceHM = chunkAccess.getOrCreateHeightmapUnprimed(Types.WORLD_SURFACE_WG);
      ChunkPos chunkPos = chunkAccess.getPos();

      for (int x = 0; x < 16; x++) {
         int worldX = chunkPos.getBlockX(x);

         for (int z = 0; z < 16; z++) {
            int worldZ = chunkPos.getBlockZ(z);
            int terrainHeight = this.getTerrainHeight(worldX, worldZ);
            int maxGenHeight = Math.max(this.getSeaLevel(), terrainHeight) + 32;

            for (int sectionY = chunkAccess.getSections().length - 1; sectionY > -1; sectionY--) {
               int worldSectionY = sectionY * 16;
               LevelChunkSection section = chunkAccess.getSection(sectionY);

               for (int y = 15; y > -1; y--) {
                  int worldY = worldSectionY + y;
                  BiomeData biomeData = BiomeIds.getData(this.getNoisyBiomeId(worldX, worldY, worldZ));
                  BiomeBasedChunkGen biomeBasedGen = biomeData.biomeBasedChunkGen();
                  if (worldY > maxGenHeight && (worldY < biomeBasedGen.minY() - 16 || worldY > biomeBasedGen.maxY() + 16)) {
                     break;
                  }

                  BlockState state = this.getStateAt(worldX, worldY, worldZ, terrainHeight, biomeData);
                  double beard = beardifier.compute(new SinglePointContext(worldX, worldY, worldZ));
                  if (beard > 0.1) {
                     state = this.defaultBlock();
                  }

                  if (state != AIR) {
                     section.setBlockState(x, y, z, state, false);
                     oceanHM.update(x, worldY, z, state);
                     surfaceHM.update(x, worldY, z, state);
                     if (!state.getFluidState().isEmpty()) {
                        chunkAccess.markPosForPostprocessing(new BlockPos(worldX, worldY, worldZ));
                     }
                  }
               }
            }
         }
      }

      return chunkAccess;
   }

   private BlockState getStateAt(int worldX, int worldY, int worldZ, int terrainHeight, BiomeData biomeData) {
      BlockState state;
      if (worldY <= terrainHeight) {
         state = this.defaultBlock();
      } else if (worldY <= this.getSeaLevel()) {
         state = WATER;
      } else {
         state = AIR;
      }

      int variance = biomeData.variance();
      double extraNoiseRange = Math.min(12.0, variance * 0.4);
      if (worldY <= terrainHeight + extraNoiseRange && worldY >= terrainHeight - extraNoiseRange) {
         double nAScale = 0.004;
         double nA = this.noiseA.getValue(worldX * 0.004, worldY * 0.004 * 0.1, worldZ * 0.004);
         double nRScale = 0.03;
         double nR = this.noiseA.getValue(worldX * 0.03, worldY * 0.03, worldZ * 0.03);
         if (nA * 0.9 + nR * 0.1 > 0.68) {
            state = this.defaultBlock();
         }

         if (variance > 8) {
            double nB = this.noiseB.getValue(worldX * 0.004, worldY * 0.004 * 3.0, worldZ * 0.004);
            if (nB * 0.8 + nR * 0.2 > 0.7) {
               state = AIR;
            }
         }
      }

      BiomeBasedChunkGen bbChunkGen = biomeData.biomeBasedChunkGen();
      if (worldY >= bbChunkGen.minY() && worldY <= bbChunkGen.maxY()) {
         state = bbChunkGen.genFunc().apply(state, this, terrainHeight, worldX, worldY, worldZ);
      }

      return state;
   }

   private int getTerrainHeight(int worldX, int worldZ) {
      return this.biomeSource instanceof SkiesBiomeSource ? ((SkiesBiomeSource)this.biomeSource).getHeight(worldX, worldZ) : 70;
   }

   private int getBiomeId(int worldX, int worldY, int worldZ) {
      return this.biomeSource instanceof SkiesBiomeSource ? ((SkiesBiomeSource)this.biomeSource).getBiomeID(worldX, worldY, worldZ) : BiomeIds.PLAINS;
   }

   private int getNoisyBiomeId(int worldX, int worldY, int worldZ) {
      double scale = 0.04;
      double x = worldX * scale;
      double y = worldY * scale;
      double z = worldZ * scale;
      int xOffset = (int)Math.round(this.noiseA.getValue(x, y, z) * 3.0);
      int zOffset = (int)Math.round(this.noiseB.getValue(x, y, z) * 3.0);
      return this.getBiomeId(worldX + xOffset, worldY, worldZ + zOffset);
   }
}
