package com.legacy.blue_skies.world;

import com.legacy.blue_skies.world.biome_provider.provider.AbstractBiomeProvider;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;

public abstract class SkiesBiomeSource extends BiomeSource {
   protected final HolderGetter<Biome> biomeRegistry;
   protected final int minY;
   protected final int height;
   protected final Map<ResourceLocation, Holder<Biome>> allBiomes;
   protected final AbstractBiomeProvider provider;

   public SkiesBiomeSource(HolderGetter<Biome> biomeRegistry, int minY, int height, HolderSet<Biome> biomes, AbstractBiomeProvider biomeProvider) {
      this.biomeRegistry = biomeRegistry;
      this.minY = minY;
      this.height = height;
      this.allBiomes = biomes.stream()
         .collect(
            Collectors.toMap(
               h -> ((ResourceKey)h.unwrapKey().orElseThrow(() -> new IllegalArgumentException("A biome in SkiesBiomeSource did not have a ResourceKey")))
                  .location(),
               Function.identity()
            )
         );
      this.provider = biomeProvider;
   }

   public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler climateSampler) {
      return this.provider.getBiome(this.biomeRegistry, this.allBiomes, x * 4, y * 4, z * 4);
   }

   public int getBiomeID(int x, int y, int z) {
      return this.provider.getWorldRegion(new ChunkPos(x >> 4, z >> 4)).getBiome(x, z);
   }

   public int getHeight(int x, int z) {
      return this.provider.getHeight(x, z);
   }

   public HolderSet<Biome> biomeHolderSet() {
      return HolderSet.direct(List.copyOf(this.possibleBiomes()));
   }

   protected Stream<Holder<Biome>> collectPossibleBiomes() {
      return this.allBiomes.values().stream();
   }

   public void setSeed(long seed) {
      this.provider.setSeed(seed);
   }

   public void updateBiomeProviderCache(int playerCount, int perPlayerCount) {
      this.provider.updateCacheSize(playerCount, perPlayerCount);
   }

   @Nullable
   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(
      int originX,
      int originY,
      int originZ,
      int range,
      int p_207824_,
      Predicate<Holder<Biome>> predicate,
      RandomSource rand,
      boolean p_207827_,
      Sampler climateSampler
   ) {
      return this.possibleBiomes().stream().anyMatch(predicate::test)
         ? super.findBiomeHorizontal(originX, originY, originZ, range, p_207824_, predicate, rand, p_207827_, climateSampler)
         : null;
   }
}
