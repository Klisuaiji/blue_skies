package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.data.providers.SkiesBiomeProv;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Pointer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;

@RegistrarHolder
public class SkiesBiomes {
   public static final RegistrarHandler<Biome> HANDLER = RegistrarHandler.getOrCreate(Registries.BIOME, "blue_skies");
   public static final Pointer<Biome> CALMING_SKIES = create("calming_skies", SkiesBiomeProv.Makers::calmingSkies);
   public static final Pointer<Biome> BRIGHTLANDS = create("brightlands", SkiesBiomeProv.Makers::brightlands);
   public static final Pointer<Biome> SLUSHLANDS = create("slushlands", SkiesBiomeProv.Makers::slushlands);
   public static final Pointer<Biome> FROSTBITTEN_FOREST = create("frostbitten_forest", c -> SkiesBiomeProv.Makers.frostbittenForest(c, false));
   public static final Pointer<Biome> FROSTBITTEN_FOREST_CLEARING = create("frostbitten_forest_clearing", c -> SkiesBiomeProv.Makers.frostbittenForest(c, true));
   public static final Pointer<Biome> PEEKING_OCEAN = create("peeking_ocean", c -> SkiesBiomeProv.Makers.peekingOcean(c, false, false));
   public static final Pointer<Biome> DEEP_PEEKING_OCEAN = create("deep_peeking_ocean", c -> SkiesBiomeProv.Makers.peekingOcean(c, true, false));
   public static final Pointer<Biome> BRUMBLE_FOREST = create("brumble_forest", c -> SkiesBiomeProv.Makers.peekingOcean(c, true, true));
   public static final Pointer<Biome> MIDDAY_SHORE = create("midday_shore", SkiesBiomeProv.Makers::middayShore);
   public static final Pointer<Biome> SNOW_COVERED_PINES = create("snow_covered_pines", SkiesBiomeProv.Makers::snowCoveredPines);
   public static final Pointer<Biome> BRISK_MEADOW = create("brisk_meadow", SkiesBiomeProv.Makers::briskMeadow);
   public static final Pointer<Biome> POLAR_HIGHLAND = create("polar_highland", SkiesBiomeProv.Makers::polarHighland);
   public static final Pointer<Biome> UNORTHODOX_VALLEY = create("unorthodox_valley", SkiesBiomeProv.Makers::unorthodoxValley);
   public static final Pointer<Biome> SHADED_WOODLANDS = create("shaded_woodlands", SkiesBiomeProv.Makers::shadedWoodlands);
   public static final Pointer<Biome> CRYSTAL_DUNES = create("crystal_dunes", c -> SkiesBiomeProv.Makers.crystalDunes(c, false));
   public static final Pointer<Biome> CRYSTAL_DUNES_SPIKES = create("crystal_dunes_spikes", c -> SkiesBiomeProv.Makers.crystalDunes(c, true));
   public static final Pointer<Biome> SUNSET_MAPLE_FOREST = create("sunset_maple_forest", SkiesBiomeProv.Makers::sunsetMapleForest);
   public static final Pointer<Biome> RISING_CREEK = create("rising_creek", SkiesBiomeProv.Makers::risingCreek);
   public static final Pointer<Biome> MOONLIT_RESERVOIR = create("moonlit_reservoir", SkiesBiomeProv.Makers::moonlitReservoir);
   public static final Pointer<Biome> CRYSTAL_ROUGHS = create("crystal_roughs", SkiesBiomeProv.Makers::crystalRoughs);
   public static final Pointer<Biome> SEARING_GRASSLAND = create("searing_grassland", SkiesBiomeProv.Makers::searingGrassland);
   public static final Pointer<Biome> CRESCENT_ORCHARD = create("crescent_orchard", c -> SkiesBiomeProv.Makers.crescentOrchard(c, false));
   public static final Pointer<Biome> CRESCENT_ORCHARD_LAKE = create("crescent_orchard_lake", c -> SkiesBiomeProv.Makers.crescentOrchard(c, true));
   public static final List<Pointer<Biome>> EVERBRIGHT_BIOMES = new ArrayList<>();
   public static final List<Pointer<Biome>> EVERDAWN_BIOMES = new ArrayList<>();

   public static void init() {
      register(Arrays.asList(CALMING_SKIES, BRIGHTLANDS, SLUSHLANDS, FROSTBITTEN_FOREST, PEEKING_OCEAN, MIDDAY_SHORE), true);
      register(Arrays.asList(UNORTHODOX_VALLEY, SHADED_WOODLANDS, CRYSTAL_DUNES, SUNSET_MAPLE_FOREST, RISING_CREEK), false);
      register(Arrays.asList(SNOW_COVERED_PINES, DEEP_PEEKING_OCEAN, BRISK_MEADOW, POLAR_HIGHLAND, FROSTBITTEN_FOREST_CLEARING, BRUMBLE_FOREST), true);
      register(Arrays.asList(MOONLIT_RESERVOIR, CRYSTAL_ROUGHS, SEARING_GRASSLAND, CRESCENT_ORCHARD, CRESCENT_ORCHARD_LAKE, CRYSTAL_DUNES_SPIKES), false);
   }

   public static void register(List<Pointer<Biome>> biome, boolean inEverbright) {
      biome.forEach(b -> {
         b.getKey();
         if (inEverbright) {
            EVERBRIGHT_BIOMES.add((Pointer<Biome>)b);
         } else {
            EVERDAWN_BIOMES.add((Pointer<Biome>)b);
         }
      });
   }

   private static Pointer<Biome> create(String name, Function<BootstapContext<?>, Biome> biome) {
      return HANDLER.createPointer(name, biome);
   }
}
