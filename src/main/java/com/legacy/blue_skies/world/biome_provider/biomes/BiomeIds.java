package com.legacy.blue_skies.world.biome_provider.biomes;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesBiomes;
import com.legacy.blue_skies.world.biome_provider.pixel_functions.HeightFuncs;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Pointer;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class BiomeIds {
   private static final List<ResourceLocation> BIOME_NAMES = new ArrayList<>();
   private static final List<BiomeData> BIOME_DATA = new ArrayList<>();
   private static final Object2IntOpenHashMap<ResourceLocation> BIOME_NAMES_TO_ID = new Object2IntOpenHashMap();
   private static int nextId = 0;
   public static final int PLAINS = register("minecraft:plains", BiomeData.builder(70, 5));
   public static final int OCEAN = register("minecraft:ocean", BiomeData.builder(40, 5));
   public static final int DEEP_OCEAN = register("minecraft:deep_ocean", BiomeData.builder(20, 5));
   public static final int CALMING_SKIES = register(SkiesBiomes.CALMING_SKIES, BiomeData.builder(83, 7).temperatures(BiomeIds.Temperature.NEUTRAL).roughness(4));
   public static final int BRIGHTLANDS = register(
      SkiesBiomes.BRIGHTLANDS, BiomeData.builder(150, 70).temperatures(BiomeIds.Temperature.FROZEN).pointiness(25).roughness(9).steps(16, 7, 0.0)
   );
   public static final int SLUSHLANDS = register(
      SkiesBiomes.SLUSHLANDS, BiomeData.builder(66, 7).temperatures(BiomeIds.Temperature.HOT).valleyDepth(6).roughness(7).noBeaches().cantBecomeRiver()
   );
   public static final int FROSTBITTEN_FOREST = register(SkiesBiomes.FROSTBITTEN_FOREST, BiomeData.builder(75, 6).temperatures(BiomeIds.Temperature.FROZEN));
   public static final int SNOW_COVERED_PINES = register(
      SkiesBiomes.SNOW_COVERED_PINES, BiomeData.builder(90, 8).temperatures(BiomeIds.Temperature.FROZEN, BiomeIds.Temperature.NEUTRAL)
   );
   public static final int BRISK_MEADOW = register(SkiesBiomes.BRISK_MEADOW, BiomeData.builder(78, 5).temperatures(BiomeIds.Temperature.HOT));
   public static final int POLAR_HIGHLAND = register(
      SkiesBiomes.POLAR_HIGHLAND, BiomeData.builder(87, 20).temperatures(BiomeIds.Temperature.NEUTRAL, BiomeIds.Temperature.HOT).pointiness(8).valleyDepth(12)
   );
   public static final int PEEKING_OCEAN = register(
      SkiesBiomes.PEEKING_OCEAN, BiomeData.builder(40, 5).temperatures(BiomeIds.Temperature.NEUTRAL).isShallowOcean(BiomeIds::deepPeekingOcean)
   );
   public static final int DEEP_PEEKING_OCEAN = register(
      SkiesBiomes.DEEP_PEEKING_OCEAN, BiomeData.builder(25, 5).temperatures(BiomeIds.Temperature.NEUTRAL).isDeepOcean(BiomeIds::peekingOcean)
   );
   public static final int SHALLOW_BRIGHTLANDS = register(
      BlueSkies.locate("shallow_brightlands"), BiomeData.builder(110, 20).pointiness(12).roughness(6).steps(11, 8, 0.4).alias(BRIGHTLANDS)
   );
   public static final int FROSTBITTEN_FOREST_CLEARING = register(SkiesBiomes.FROSTBITTEN_FOREST_CLEARING, BiomeData.builder(72, 5));
   public static final int MIDDAY_SHORE = register(SkiesBiomes.MIDDAY_SHORE, BiomeData.builder(65, 2).valleyDepth(0));
   public static final int BRUMBLE_FOREST = register(
      SkiesBiomes.BRUMBLE_FOREST, BiomeData.builder(25, 10).pointiness(4).valleyDepth(4).roughness(6).isDeepOcean(BiomeIds::peekingOcean)
   );
   public static final int UNORTHODOX_VALLEY = register(
      SkiesBiomes.UNORTHODOX_VALLEY,
      BiomeData.builder(85, 120)
         .biomeBasedChunkGen(BiomeBasedChunkGen.UNORTHODX_VALLEY)
         .mainHeightFunc(HeightFuncs::unorthodoxHeightFunc)
         .temperatures(BiomeIds.Temperature.FROZEN)
         .valleyDepth(40)
         .roughness(15)
         .pointiness(5)
         .steps(5, 2, 0.3)
   );
   public static final int SHADED_WOODLANDS = register(SkiesBiomes.SHADED_WOODLANDS, BiomeData.builder(68, 5).temperatures(BiomeIds.Temperature.FROZEN));
   public static final int CRYSTAL_DUNES = register(SkiesBiomes.CRYSTAL_DUNES, BiomeData.builder(70, 5).temperatures(BiomeIds.Temperature.HOT).pointiness(6));
   public static final int SUNSET_MAPLE_FOREST = register(SkiesBiomes.SUNSET_MAPLE_FOREST, BiomeData.builder(70, 6).temperatures(BiomeIds.Temperature.NEUTRAL));
   public static final int MOONLIT_RESERVOIR = register(
      SkiesBiomes.MOONLIT_RESERVOIR, BiomeData.builder(64, 8).temperatures(BiomeIds.Temperature.NEUTRAL).noBeaches().cantBecomeRiver()
   );
   public static final int CRYSTAL_ROUGHS = register(
      SkiesBiomes.CRYSTAL_ROUGHS, BiomeData.builder(78, 10).temperatures(BiomeIds.Temperature.HOT).pointiness(8).steps(4, 2, 0.0)
   );
   public static final int SEARING_GRASSLAND = register(SkiesBiomes.SEARING_GRASSLAND, BiomeData.builder(70, 4).temperatures(BiomeIds.Temperature.NEUTRAL));
   public static final int CRESCENT_ORCHARD = register(SkiesBiomes.CRESCENT_ORCHARD, BiomeData.builder(68, 5).temperatures(BiomeIds.Temperature.FROZEN));
   public static final int CRYSTAL_DUNES_SPIKES = register(SkiesBiomes.CRYSTAL_DUNES_SPIKES, BiomeData.builder(75, 9).pointiness(3));
   public static final int CRESCENT_ORCHARD_LAKE = register(SkiesBiomes.CRESCENT_ORCHARD_LAKE, BiomeData.builder(55, 2));
   public static final int RISING_CREEK = register(SkiesBiomes.RISING_CREEK, BiomeData.builder(55, 6));
   public static final int RISING_CREEK_EDGE = register(BlueSkies.locate("rising_creek_edge"), BiomeData.builder(64, 3).valleyDepth(2).alias(RISING_CREEK));
   public static final int FROZ = register("blue_skies_debug:frozen", BiomeData.builder(1, 1).temperatures(BiomeIds.Temperature.FROZEN));
   public static final int COLD = register("blue_skies_debug:cold", BiomeData.builder(1, 1).temperatures(BiomeIds.Temperature.FROZEN));
   public static final int NEUT = register("blue_skies_debug:neutral", BiomeData.builder(1, 1).temperatures(BiomeIds.Temperature.NEUTRAL));
   public static final int WARM = register("blue_skies_debug:warm", BiomeData.builder(1, 1).temperatures(BiomeIds.Temperature.FROZEN));
   public static final int HOTT = register("blue_skies_debug:hot", BiomeData.builder(1, 1).temperatures(BiomeIds.Temperature.HOT));

   public static int register(Pointer<Biome> shadedWoodlands, BiomeData.Builder data) {
      return register(shadedWoodlands.getName(), data);
   }

   public static int register(String name, BiomeData.Builder data) {
      return register(ResourceLocation.parse(name), data);
   }

   public static int register(ResourceLocation name, BiomeData.Builder data) {
      int id = nextId++;
      BIOME_NAMES.add(name);
      BIOME_DATA.add(data.build(id, name));
      BIOME_NAMES_TO_ID.put(name, id);
      return id;
   }

   public static ResourceLocation getName(int id) {
      return BIOME_NAMES.get(id);
   }

   public static BiomeData getData(int id) {
      return BIOME_DATA.get(id);
   }

   public static int getId(ResourceLocation name) {
      return BIOME_NAMES_TO_ID.getInt(name);
   }

   private static int peekingOcean() {
      return PEEKING_OCEAN;
   }

   private static int deepPeekingOcean() {
      return DEEP_PEEKING_OCEAN;
   }

   static {
      BIOME_NAMES_TO_ID.defaultReturnValue(PLAINS);
   }

   public enum Temperature {
      FROZEN,
      COLD,
      NEUTRAL,
      WARM,
      HOT;
   }
}
