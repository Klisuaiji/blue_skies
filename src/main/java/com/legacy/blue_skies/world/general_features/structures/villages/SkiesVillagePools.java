package com.legacy.blue_skies.world.general_features.structures.villages;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class SkiesVillagePools {
   public static final ResourceKey<StructureTemplatePool> CALMING_SKIES = ResourceKey.create(
      Registries.TEMPLATE_POOL, BlueSkies.locate("village/" + SkiesVillageBiomes.CALMING_SKIES.toString() + "/town_centers")
   );
   public static final ResourceKey<StructureTemplatePool> BRIGHTLANDS = ResourceKey.create(
      Registries.TEMPLATE_POOL, BlueSkies.locate("village/" + SkiesVillageBiomes.BRIGHTLANDS.toString() + "/town_centers")
   );
   public static final ResourceKey<StructureTemplatePool> SLUSHLANDS = ResourceKey.create(
      Registries.TEMPLATE_POOL, BlueSkies.locate("village/" + SkiesVillageBiomes.SLUSHLANDS.toString() + "/town_centers")
   );
   public static final ResourceKey<StructureTemplatePool> SHADED_WOODLANDS = ResourceKey.create(
      Registries.TEMPLATE_POOL, BlueSkies.locate("village/" + SkiesVillageBiomes.SHADED_WOODLANDS.toString() + "/town_centers")
   );
   public static final ResourceKey<StructureTemplatePool> SUNSET_MAPLE_FOREST = ResourceKey.create(
      Registries.TEMPLATE_POOL, BlueSkies.locate("village/" + SkiesVillageBiomes.SUNSET_MAPLE_FOREST.toString() + "/town_centers")
   );
   public static final ResourceKey<StructureTemplatePool> CRYSTAL_DUNES = ResourceKey.create(
      Registries.TEMPLATE_POOL, BlueSkies.locate("village/" + SkiesVillageBiomes.CRYSTAL_DUNES.toString() + "/town_centers")
   );

   public static void bootstrap(BootstapContext<StructureTemplatePool> bootstrap) {
      JigsawPoolBuilder empty = new JigsawRegistryHelper("minecraft", "", bootstrap)
         .poolBuilder()
         .namesR(new ResourceLocation[]{new ResourceLocation("empty")});
      String calmingSkies = SkiesVillageBiomes.CALMING_SKIES.toString();
      createVillagePools(
         bootstrap,
         calmingSkies,
         3,
         1,
         6,
         3,
         6,
         SkiesStructures.ProcessorLists.CALMING_SKIES_STREET.getKey(),
         8,
         2,
         1,
         SkiesVillageProfessions.getWithout(SkiesVillageProfessions.SHOVELER)
      );
      JigsawRegistryHelper registryCalmingSkies = new JigsawRegistryHelper("blue_skies", "village/" + calmingSkies + "/", bootstrap);
      registryCalmingSkies.register(
         "decor",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               registryCalmingSkies.setPrefix(registryCalmingSkies.prefix + "decor/")
                  .poolBuilder()
                  .names(new String[]{"log_pile", "lamp", "flower_bed", "rock", "shrub"}),
               empty.clone()
            }
         )
      );
      String brightlands = SkiesVillageBiomes.BRIGHTLANDS.toString();
      createVillagePools(
         bootstrap,
         brightlands,
         3,
         1,
         6,
         3,
         6,
         SkiesStructures.ProcessorLists.BRIGHTLANDS_STREET.getKey(),
         8,
         2,
         1,
         SkiesVillageProfessions.getWithout(SkiesVillageProfessions.TOOLSMITH)
      );
      JigsawRegistryHelper registryBrightlands = new JigsawRegistryHelper("blue_skies", "village/" + brightlands + "/", bootstrap);
      registryBrightlands.register(
         "decor",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               registryBrightlands.setPrefix(registryBrightlands.prefix + "decor/").poolBuilder().names(new String[]{"campfire", "lamp", "tree", "ice_spike"}),
               empty.clone()
            }
         )
      );
      String slushlands = SkiesVillageBiomes.SLUSHLANDS.toString();
      createVillagePools(
         bootstrap,
         slushlands,
         3,
         1,
         6,
         3,
         6,
         SkiesStructures.ProcessorLists.SLUSHLANDS_STREET.getKey(),
         8,
         2,
         1,
         SkiesVillageProfessions.getWithout(SkiesVillageProfessions.SHOVELER)
      );
      JigsawRegistryHelper registrySlushlands = new JigsawRegistryHelper("blue_skies", "village/" + slushlands + "/", bootstrap);
      registrySlushlands.register(
         "decor",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               registrySlushlands.setPrefix(registrySlushlands.prefix + "decor/")
                  .poolBuilder()
                  .names(ImmutableMap.of("mushroom_basket", 2, "lamp", 3, "small_mushroom", 3, "oven", 1)),
               empty.clone()
            }
         )
      );
      String woodlands = SkiesVillageBiomes.SHADED_WOODLANDS.toString();
      createVillagePools(
         bootstrap,
         woodlands,
         3,
         1,
         6,
         3,
         6,
         SkiesStructures.ProcessorLists.SHADED_WOODLANDS_STREET.getKey(),
         8,
         2,
         1,
         SkiesVillageProfessions.getWithout(SkiesVillageProfessions.SHOVELER, SkiesVillageProfessions.SHEPHERD)
      );
      JigsawRegistryHelper registryWoodlands = new JigsawRegistryHelper("blue_skies", "village/" + woodlands + "/", bootstrap);
      registryWoodlands.register(
         "decor",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               registryWoodlands.setPrefix(registryWoodlands.prefix + "decor/").poolBuilder().names(new String[]{"lamp", "campfire", "chairs", "farm"}),
               empty.clone()
            }
         )
      );
      String maple = SkiesVillageBiomes.SUNSET_MAPLE_FOREST.toString();
      createVillagePools(
         bootstrap,
         maple,
         3,
         1,
         6,
         3,
         6,
         SkiesStructures.ProcessorLists.SUNSET_MAPLE_FOREST_STREET.getKey(),
         8,
         2,
         1,
         SkiesVillageProfessions.getWithout(SkiesVillageProfessions.SHOVELER, SkiesVillageProfessions.SHEPHERD)
      );
      JigsawRegistryHelper registryMaple = new JigsawRegistryHelper("blue_skies", "village/" + maple + "/", bootstrap);
      registryMaple.register(
         "decor",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               registryMaple.setPrefix(registryMaple.prefix + "decor/").poolBuilder().names(ImmutableMap.of("lamp", 6, "campfire", 1)),
               empty.clone().weight(20)
            }
         )
      );
      registryMaple.register("town_centers/well_bottom", registryMaple.poolBuilder().names(new String[]{"town_centers/well_bottom"}).build());
      String dunes = SkiesVillageBiomes.CRYSTAL_DUNES.toString();
      createVillagePools(
         bootstrap,
         dunes,
         3,
         1,
         6,
         3,
         6,
         SkiesStructures.ProcessorLists.CRYSTAL_DUNES.getKey(),
         8,
         2,
         1,
         SkiesVillageProfessions.getWithout(SkiesVillageProfessions.SHOVELER, SkiesVillageProfessions.SHEPHERD, SkiesVillageProfessions.FISHER)
      );
      JigsawRegistryHelper registryDunes = new JigsawRegistryHelper("blue_skies", "village/" + dunes + "/", bootstrap);
      registryDunes.register(
         "decor",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               registryDunes.setPrefix(registryDunes.prefix + "decor/").poolBuilder().names(ImmutableMap.of("lamp", 5, "campfire", 1)),
               empty.clone().weight(15)
            }
         )
      );
      registryDunes.register("town_centers/oasis_bottom", registryDunes.poolBuilder().names(new String[]{"town_centers/oasis_bottom"}).build());
   }

   private static void createVillagePools(
      BootstapContext<StructureTemplatePool> bootstrap,
      String biome,
      int townCenters,
      int terminators,
      int straightStreets,
      int streetCorners,
      int streetIntersections,
      ResourceKey<StructureProcessorList> resourceKey,
      int smallHouses,
      int medHouses,
      int largeHouses,
      List<SkiesVillageProfessions> jobSiteNames
   ) {
      JigsawRegistryHelper registry = new JigsawRegistryHelper("blue_skies", "village/" + biome + "/", bootstrap);
      registry.register("town_centers", makeBuilder(registry, "town_centers/", townCenters).maintainWater(false));
      registry.register(
         "terminators",
         registry.registerBuilder()
            .pools(makeBuilder(registry, "terminators/", terminators).processors(resourceKey).build())
            .projection(Projection.TERRAIN_MATCHING)
      );
      JigsawRegistryHelper streetRegistry = registry.setPrefix(registry.prefix + "streets/");
      JigsawPoolBuilder streets = makeBuilder(streetRegistry, "straight_", straightStreets).weight(10).processors(resourceKey);
      JigsawPoolBuilder corners = makeBuilder(streetRegistry, "corner_", streetCorners).weight(8).processors(resourceKey);
      JigsawPoolBuilder intersections = makeBuilder(streetRegistry, "intersection_", streetIntersections).weight(2).processors(resourceKey);
      registry.register(
         "streets",
         registry.registerBuilder()
            .pools(JigsawPoolBuilder.collect(new JigsawPoolBuilder[]{streets, corners, intersections}))
            .projection(Projection.TERRAIN_MATCHING)
            .fallback("terminators")
      );
      JigsawRegistryHelper houseRegistry = registry.setPrefix(registry.prefix + "houses/");
      JigsawPoolBuilder sHouses = makeBuilder(houseRegistry, "small_", smallHouses);
      JigsawPoolBuilder mHouses = makeBuilder(houseRegistry, "medium_", medHouses);
      JigsawPoolBuilder lHouses = makeBuilder(houseRegistry, "large_", largeHouses);
      JigsawPoolBuilder jobSites = houseRegistry.poolBuilder().names(jobSiteNames.stream().map(SkiesVillageProfessions::toString).collect(Collectors.toList()));
      registry.register(
         "houses",
         registry.registerBuilder().pools(JigsawPoolBuilder.collect(new JigsawPoolBuilder[]{sHouses, mHouses, lHouses, jobSites})).fallback("terminators")
      );
   }

   private static JigsawPoolBuilder makeBuilder(JigsawRegistryHelper registry, String name, int count) {
      String[] names = new String[count];

      for (int i = 0; i < count; i++) {
         names[i] = name + i;
      }

      return registry.poolBuilder().names(names);
   }
}
