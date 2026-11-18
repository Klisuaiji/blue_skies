package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.world.SkiesChunkGenerator;
import com.legacy.blue_skies.world.everbright.EverbrightChunkGenerator;
import com.legacy.blue_skies.world.everbright.biome.provider.EverbrightBiomeSource;
import com.legacy.blue_skies.world.everdawn.EverdawnChunkGenerator;
import com.legacy.blue_skies.world.everdawn.biome.provider.EverdawnBiomeSource;
import com.legacy.structure_gel.api.registry.StructureGelRegistries.Keys;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesRegistry {
   @SubscribeEvent
   public static void onRegistry(RegisterEvent event) {
      if (event.getRegistryKey().equals(Registries.ENTITY_TYPE)) {
         SkiesEntityTypes.init(event);
         SkiesVillagers.initVillagerTypes();
      } else if (event.getRegistryKey().equals(Registries.ITEM)) {
         SkiesItems.init(event);
      } else if (event.getRegistryKey().equals(Registries.BLOCK)) {
         SkiesBlocks.init(event);
      } else if (event.getRegistryKey().equals(Registries.MENU)) {
         SkiesContainers.init(event);
      } else if (event.getRegistryKey().equals(Registries.MOB_EFFECT)) {
         SkiesEffects.init(event);
      } else if (event.getRegistryKey().equals(Registries.SOUND_EVENT)) {
         SkiesSounds.init();
      } else if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
         SkiesRecipes.init(event);
      } else if (event.getRegistryKey().equals(Registries.LOOT_FUNCTION_TYPE)) {
         SkiesLootFunctions.init(event);
      } else if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
         SkiesBlockEntityTypes.init(event);
      } else if (event.getRegistryKey().equals(Registries.SCHEDULE)) {
         SkiesSchedules.init(event);
      } else if (event.getRegistryKey().equals(Registries.VILLAGER_PROFESSION)) {
         SkiesVillagers.init(event);
      } else if (event.getRegistryKey().equals(Registries.POINT_OF_INTEREST_TYPE)) {
         SkiesPoiTypes.init(event);
      } else if (event.getRegistryKey().equals(Registries.FEATURE)) {
         SkiesFeatures.init(event);
      } else if (event.getRegistryKey().equals(Registries.CARVER)) {
         SkiesFeatures.Carvers.init(event);
      } else if (event.getRegistryKey().equals(Registries.PARTICLE_TYPE)) {
         SkiesParticles.init(event);
      } else if (event.getRegistryKey().equals(Registries.PROCESSOR_LIST)) {
         SkiesStructures.ProcessorLists.init();
      } else if (event.getRegistryKey().equals(Registries.STRUCTURE_PROCESSOR)) {
         SkiesStructureProcessors.init();
      } else if (event.getRegistryKey().equals(Registries.CHUNK_GENERATOR)) {
         event.register(Registries.CHUNK_GENERATOR, BlueSkies.locate("skies_chunk_generator"), () -> SkiesChunkGenerator.SKIES_CODEC);
         event.register(Registries.CHUNK_GENERATOR, BlueSkies.locate("everbright"), () -> EverbrightChunkGenerator.EVERBRIGHT_CODEC);
         event.register(Registries.CHUNK_GENERATOR, BlueSkies.locate("everdawn"), () -> EverdawnChunkGenerator.EVERDAWN_CODEC);
      } else if (event.getRegistryKey().equals(Registries.MATERIAL_CONDITION)) {
         event.register(Registries.MATERIAL_CONDITION, BlueSkies.locate("skies_chunk_generator"), () -> SkiesSurfaceRules.RandomConditionSource.CODEC.codec());
      } else if (event.getRegistryKey().equals(Registries.MATERIAL_RULE)) {
         event.register(Registries.MATERIAL_RULE, BlueSkies.locate("crystal_roughness"), () -> SkiesSurfaceRules.CrystalRoughness.CODEC.codec());
         event.register(Registries.MATERIAL_RULE, BlueSkies.locate("reservoir_muddiness"), () -> SkiesSurfaceRules.ReservoirMuddiness.CODEC.codec());
      } else if (event.getRegistryKey().equals(Registries.BIOME_SOURCE)) {
         SkiesBiomes.init();
         event.register(Registries.BIOME_SOURCE, BlueSkies.locate("everbright"), () -> EverbrightBiomeSource.CODEC);
         event.register(Registries.BIOME_SOURCE, BlueSkies.locate("everdawn"), () -> EverdawnBiomeSource.CODEC);
      } else if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
         SkiesCreativeTabs.init(event);
      } else if (event.getRegistryKey().equals(Keys.JIGSAW_TYPE)) {
         SkiesJigsawTypes.init(event);
      } else if (event.getRegistryKey().equals(Registries.TRIGGER_TYPE)) {
         SkiesTriggers.init();
      } else if (event.getRegistryKey().equals(Registries.CUSTOM_STAT)) {
         SkiesStats.init();
      }
   }

   @SubscribeEvent
   public static void registerCapabilities(RegisterCapabilitiesEvent event) {
      for (BlockEntityType type : List.of(SkiesBlockEntityTypes.SNOWCAP_OVEN, SkiesBlockEntityTypes.HORIZONITE_FORGE)) {
         event.registerBlockEntity(
            ItemHandler.BLOCK,
            type,
            (sidedContainer, side) -> (IItemHandler)(side == null
               ? new InvWrapper(sidedContainer)
               : new SidedInvWrapper((WorldlyContainer)sidedContainer, side))
         );
      }
   }
}
