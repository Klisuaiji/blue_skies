package com.legacy.blue_skies.world.everbright.gen.structures.bunker;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawRegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class FrozenBunkerPools {
   public static final ResourceKey<StructureTemplatePool> ROOT = ResourceKey.create(Registries.TEMPLATE_POOL, BlueSkies.locate("bunker/entrance"));

   public static void bootstrap(BootstrapContext<StructureTemplatePool> bootstrap) {
      JigsawRegistryHelper registry = new JigsawRegistryHelper("blue_skies", "bunker/", bootstrap);
      JigsawPoolBuilder builder = registry.poolBuilder().maintainWater(false).processors(SkiesStructures.ProcessorLists.DECAY_TURQUOISE_STONEBRICK.getKey());
      registry.registerBuilder().pools(builder.clone().names(new String[]{"entrance"})).register("entrance");
      registry.register("pipe", builder.clone().names(new String[]{"pipe"}));
      registry.register("main_room", builder.clone().names(new String[]{"main_room"}));
      registry.register("rooms", builder.clone().names(new String[]{"rooms/room_1", "rooms/room_2", "rooms/room_3", "rooms/room_4"}));
   }
}
