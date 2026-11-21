package com.legacy.blue_skies.world.biome_provider.provider;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.world.SkiesBiomeSource;
import com.legacy.blue_skies.world.biome_provider.biomes.BiomeIds;
import com.legacy.blue_skies.world.biome_provider.region.BakedWorldRegion;
import com.legacy.blue_skies.world.biome_provider.region.BiomeRegion;
import com.legacy.blue_skies.world.biome_provider.region.HeightRegion;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.WorldgenRandom.Algorithm;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public abstract class AbstractBiomeProvider {
   private final EnumMap<BiomeIds.Temperature, int[]> landBiomes = new EnumMap<>(BiomeIds.Temperature.class);
   private final EnumMap<BiomeIds.Temperature, int[]> oceanBiomes = new EnumMap<>(BiomeIds.Temperature.class);
   private static final List<ResourceKey<Level>> TRACKED_LEVELS = new ArrayList<>(2);
   public final int minY;
   public final int maxY;
   private final Long2ObjectLinkedOpenHashMap<BakedWorldRegion> cache;
   private int cacheSize = 8;
   private final LevelResource worldRegionDir;
   public Long seed = null;
   public PerlinSimplexNoise noiseA = null;
   public PerlinSimplexNoise noiseB = null;
   public PerlinSimplexNoise noiseC = null;

   public AbstractBiomeProvider(ResourceKey<Level> level, int minY, int height) {
      if (!TRACKED_LEVELS.contains(level)) {
         TRACKED_LEVELS.add(level);
      }

      this.minY = minY;
      this.maxY = height + minY;
      this.cache = new Long2ObjectLinkedOpenHashMap();
      ResourceLocation dim = level.location();
      this.worldRegionDir = new LevelResource("dimensions/" + dim.getNamespace() + "/" + dim.getPath() + "/baked_world_regions");
      this.bakeBiomeTemps(this.getLandBiomes(), this.landBiomes);
      this.bakeBiomeTemps(this.getOceanBiomes(), this.oceanBiomes);
   }

   public abstract int[] getLandBiomes();

   public abstract int[] getOceanBiomes();

   public void bakeBiomeTemps(int[] biomes, EnumMap<BiomeIds.Temperature, int[]> finalMap) {
      if (biomes.length == 0) {
         throw new IllegalStateException("The biomes array was empty. At least one ocean and land biome must be defined, even if they are not normally used.");
      }

      EnumMap<BiomeIds.Temperature, List<Integer>> biomeMap = new EnumMap<>(BiomeIds.Temperature.class);

      for (BiomeIds.Temperature temp : BiomeIds.Temperature.values()) {
         biomeMap.put(temp, new ArrayList<>());
      }

      for (int biome : biomes) {
         for (BiomeIds.Temperature biomeTemp : BiomeIds.getData(biome).temperatures()) {
            biomeMap.get(biomeTemp).add(biome);
         }
      }

      for (BiomeIds.Temperature temp : BiomeIds.Temperature.values()) {
         List<Integer> biomesForTemp = biomeMap.get(temp);
         finalMap.put(temp, biomesForTemp.isEmpty() ? biomes : biomesForTemp.stream().mapToInt(i -> i).toArray());
      }
   }

   public BakedWorldRegion getWorldRegion(ChunkPos chunkPos) {
      int regionX = (int)Math.floor(chunkPos.x / 64.0);
      int regionZ = (int)Math.floor(chunkPos.z / 64.0);
      long pos = ChunkPos.asLong(regionX, regionZ);
      synchronized (this.cache) {
         BakedWorldRegion worldRegion = (BakedWorldRegion)this.cache.getAndMoveToLast(pos);
         if (worldRegion != null) {
            return worldRegion;
         }

         MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
         File file = server != null && BlueSkiesConfig.COMMON.writeWorldRegionsToDisk()
            ? new File(server.getWorldPath(this.worldRegionDir).toString() + "/" + pos + ".dat")
            : null;
         if (file != null && file.exists()) {
            worldRegion = this.timeBench("Reading File", () -> {
               try {
                  return new BakedWorldRegion(NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap()));
               } catch (IOException e) {
                  e.printStackTrace();
                  return null;
               }
            });
         }

         if (worldRegion == null) {
            worldRegion = this.timeBench("Calculating", () -> {
               BiomeRegion biomeRegion = this.timeBench("  Biomes", () -> {
                  BiomeRegion bRegion = new BiomeRegion(this, regionX, regionZ, 4);
                  this.transformBiomes(bRegion);
                  return bRegion;
               });
               HeightRegion heightRegion = this.timeBench("  Heights", () -> {
                  HeightRegion hRegion = new HeightRegion(this, regionX, regionZ, biomeRegion.getScale(), biomeRegion);
                  this.transformHeights(hRegion);
                  return hRegion;
               });
               return new BakedWorldRegion(biomeRegion, heightRegion);
            });
            if (file != null) {
               try {
                  file.getParentFile().mkdirs();
                  NbtIo.writeCompressed(worldRegion.save(), file.toPath());
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }

         this.cache.put(pos, worldRegion);

         while (this.cache.size() > this.cacheSize) {
            this.cache.removeFirst();
         }

         return worldRegion;
      }
   }

   private <T> T timeBench(String label, Supplier<T> runner) {
      return runner.get();
   }

   public abstract void transformBiomes(BiomeRegion var1);

   public abstract void transformHeights(HeightRegion var1);

   public int getBiomeForTemp(BiomeIds.Temperature temp, boolean isOcean, Random rand) {
      int[] biomes = (isOcean ? this.oceanBiomes : this.landBiomes).get(temp);
      return biomes[rand.nextInt(biomes.length)];
   }

   public Holder<Biome> getBiome(HolderGetter<Biome> registry, Map<ResourceLocation, Holder<Biome>> allBiomes, int x, int y, int z) {
      ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
      Holder<Biome> biome = allBiomes.get(BiomeIds.getName(this.getWorldRegion(chunkPos).getBiome(x, z)));
      if (biome == null) {
         BlueSkies.LOGGER.warn("Blue Skies found an invalid biome at chunk ({}, {}) using {}", chunkPos.x, chunkPos.z, this.getClass().getCanonicalName());
         return registry.getOrThrow(Biomes.PLAINS);
      } else {
         return biome;
      }
   }

   public int getHeight(int x, int z) {
      return this.getWorldRegion(new ChunkPos(new BlockPos(x, 0, z))).getHeight(x, z, this);
   }

   public void setSeed(long seed) {
      if (this.seed == null) {
         this.seed = seed;
         this.noiseA = new PerlinSimplexNoise(Algorithm.LEGACY.newInstance(seed), List.of(0));
         this.noiseB = new PerlinSimplexNoise(Algorithm.LEGACY.newInstance(seed + 10L), List.of(0));
         this.noiseC = new PerlinSimplexNoise(Algorithm.LEGACY.newInstance(seed + 20L), List.of(0));
      }
   }

   public static void updateCacheSize(MinecraftServer server) {
      int viewDist = server.getPlayerList().getViewDistance();
      int viewRadius = (viewDist + 1) * 16;
      int regionLength = (int)Math.ceil(viewRadius / 1024.0) * 2;
      int regionArea = regionLength * regionLength;
      int perPlayerCount = regionArea + regionLength * 2;

      for (ResourceKey<Level> levelKey : TRACKED_LEVELS) {
         ServerLevel level = server.getLevel(levelKey);
         BiomeSource biomeSource = level.getChunkSource().getGenerator().getBiomeSource();
         if (biomeSource instanceof SkiesBiomeSource) {
            ((SkiesBiomeSource)biomeSource).updateBiomeProviderCache(level.players().size(), perPlayerCount);
         }
      }
   }

   public void updateCacheSize(int playerCount, int perPlayerCount) {
      this.cacheSize = Math.max(playerCount, 1) * perPlayerCount;
   }
}
