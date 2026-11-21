package com.legacy.blue_skies.world.general_features.structures;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class GatekeeperHousePools {
   public static final ResourceKey<StructureTemplatePool> PLAINS = ResourceKey.create(Registries.TEMPLATE_POOL, BlueSkies.locate("gatekeeper/plains/main"));
   public static final ResourceKey<StructureTemplatePool> SNOWY = ResourceKey.create(Registries.TEMPLATE_POOL, BlueSkies.locate("gatekeeper/snowy/main"));
   public static final ResourceKey<StructureTemplatePool> MOUNTAIN = ResourceKey.create(Registries.TEMPLATE_POOL, BlueSkies.locate("gatekeeper/mountain/main"));

   public static void bootstrap(BootstrapContext<StructureTemplatePool> bootstrap) {
      ResourceKey<StructureProcessorList> removeSnow = SkiesStructures.ProcessorLists.REMOVE_SNOW.getKey();
      ResourceKey<StructureProcessorList> modifyGrass = SkiesStructures.ProcessorLists.MODIFY_GRASS.getKey();
      JigsawRegistryHelper plainsReg = new JigsawRegistryHelper("blue_skies", "gatekeeper/plains/", bootstrap);
      plainsReg.register("main", plainsReg.poolBuilder().names(new String[]{"main_1", "main_2"}));
      plainsReg.register(
         "path",
         plainsReg.registerBuilder()
            .pools(plainsReg.poolBuilder().names(new String[]{"path_1", "path_2"}).processors(SkiesStructures.ProcessorLists.MODIFY_PATH.getKey()))
            .projection(Projection.TERRAIN_MATCHING)
      );
      plainsReg.register(
         "shack",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               plainsReg.poolBuilder().names(new String[]{"shack_1", "shack_2"}),
               plainsReg.poolBuilder().names(new String[]{"shack_1", "shack_2"}).processors(SkiesStructures.ProcessorLists.SWAP_PORTAL.getKey())
            }
         )
      );
      JigsawRegistryHelper snowyReg = new JigsawRegistryHelper("blue_skies", "gatekeeper/snowy/", bootstrap);
      snowyReg.register(
         "main",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               snowyReg.poolBuilder().names(new String[]{"main"}).processors(SkiesStructures.ProcessorLists.SWAP_PORTAL_REMOVE_SNOW.getKey()),
               snowyReg.poolBuilder().names(new String[]{"main"}).processors(removeSnow)
            }
         )
      );
      snowyReg.register("bedroom", snowyReg.poolBuilder().names(new String[]{"bedroom_1", "bedroom_2"}).processors(removeSnow));
      snowyReg.register("storage", snowyReg.poolBuilder().names(new String[]{"storage_1", "storage_2"}).processors(removeSnow));
      JigsawRegistryHelper mountainReg = new JigsawRegistryHelper("blue_skies", "gatekeeper/mountain/", bootstrap);
      mountainReg.register(
         "main",
         JigsawPoolBuilder.collect(
            new JigsawPoolBuilder[]{
               mountainReg.poolBuilder().names(new String[]{"main"}).processors(SkiesStructures.ProcessorLists.SWAP_PORTAL_MODIFY_GRASS.getKey()),
               mountainReg.poolBuilder().names(new String[]{"main"}).processors(modifyGrass)
            }
         )
      );
      mountainReg.register("side", mountainReg.poolBuilder().names(new String[]{"side_1", "side_2"}).processors(modifyGrass));
      mountainReg.register("back", mountainReg.poolBuilder().names(new String[]{"back_1", "back_2"}).processors(modifyGrass));
   }
}
