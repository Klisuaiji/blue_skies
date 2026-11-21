package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.world.everbright.gen.structures.bunker.FrozenBunkerStructure;
import com.legacy.blue_skies.world.general_features.structures.GatekeeperHouseStructure;
import com.legacy.blue_skies.world.general_features.structures.villages.SkiesVillageStructure;
import com.legacy.structure_gel.api.registry.StructureGelRegistries.Keys;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapabilityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesJigsawTypes {
   public static final JigsawCapabilityType<FrozenBunkerStructure.Capability> FROZEN_BUNKER = () -> FrozenBunkerStructure.Capability.CODEC;
   public static final JigsawCapabilityType<GatekeeperHouseStructure.Capability> GATEKEEPER_HOUSE = () -> GatekeeperHouseStructure.Capability.CODEC;
   public static final JigsawCapabilityType<SkiesVillageStructure.Capability> VILLAGE = () -> SkiesVillageStructure.Capability.CODEC;

   @SubscribeEvent
   public static void init(RegisterEvent event) {
      register(event, "frozen_bunker", FROZEN_BUNKER);
      register(event, "gatekeeper_house", GATEKEEPER_HOUSE);
      register(event, "village", VILLAGE);
   }

   private static void register(RegisterEvent event, String key, JigsawCapabilityType<?> type) {
      event.register(Keys.JIGSAW_TYPE, BlueSkies.locate(key), () -> type);
   }
}
