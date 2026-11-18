package com.legacy.blue_skies.data;

import com.legacy.blue_skies.data.providers.AlchemyRecipeProv;
import com.legacy.blue_skies.data.providers.HorizoniteForgeFuelProv;
import com.legacy.blue_skies.data.providers.SkiesAdvancementProv;
import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.data.providers.SkiesRecipeProv;
import com.legacy.blue_skies.data.providers.SkiesTagProv;
import com.legacy.blue_skies.data.providers.SnowcapMushroomFreezingProv;
import com.legacy.blue_skies.data.providers.SnowcapOvenFreezingProv;
import com.legacy.blue_skies.data.providers.ToolHandleTypeProv;
import com.legacy.blue_skies.data.providers.journal.JournalEntryProv;
import com.legacy.blue_skies.data.providers.journal.JournalRequirementProv;
import com.legacy.blue_skies.data.providers.journal.JournalSectionProv;
import com.legacy.blue_skies.registries.SkiesConfiguredFeatures;
import com.legacy.blue_skies.registries.SkiesFeaturePlacements;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class BlueSkiesDataGen {
   @SubscribeEvent
   public static void gatherData(GatherDataEvent event) {
      DataGenerator gen = event.getGenerator();
      ExistingFileHelper helper = event.getExistingFileHelper();
      PackOutput output = gen.getPackOutput();
      boolean server = event.includeServer();
      SkiesConfiguredFeatures.init();
      SkiesFeaturePlacements.init();
      DatapackBuiltinEntriesProvider provider = new DatapackBuiltinEntriesProvider(
         output, event.getLookupProvider(), RegistrarHandler.injectRegistries(new RegistrySetBuilder()), Set.of("blue_skies")
      );
      gen.addProvider(server, provider);
      CompletableFuture<Provider> lookup = provider.getRegistryProvider();
      BlockTagsProvider blockTagProv = new SkiesTagProv.BlockTagProv(gen, helper, lookup);
      gen.addProvider(server, blockTagProv);
      gen.addProvider(server, new SkiesTagProv.ItemTagProv(gen, blockTagProv.contentsGetter(), helper, lookup));
      gen.addProvider(server, new SkiesTagProv.EntityTagProv(gen, helper, lookup));
      gen.addProvider(server, new SkiesTagProv.StructureTagProv(gen, helper, lookup));
      gen.addProvider(server, new SkiesTagProv.BiomeTagProv(gen, helper, lookup));
      gen.addProvider(server, new SkiesTagProv.PoiTagProv(gen, helper, lookup));
      gen.addProvider(server, new SkiesTagProv.DamageTypeProv(output, lookup, helper));
      gen.addProvider(server, new ToolHandleTypeProv(gen));
      gen.addProvider(server, new SkiesRecipeProv(output, lookup));
      gen.addProvider(server, new AlchemyRecipeProv(gen));
      gen.addProvider(server, new SnowcapOvenFreezingProv(gen));
      gen.addProvider(server, new HorizoniteForgeFuelProv(gen));
      gen.addProvider(server, new SnowcapMushroomFreezingProv(gen));
      gen.addProvider(server, new JournalEntryProv(gen));
      gen.addProvider(server, new JournalSectionProv(gen));
      gen.addProvider(server, new JournalRequirementProv(gen));
      gen.addProvider(server, new SkiesAdvancementProv(gen, lookup, helper));
      gen.addProvider(server, new SkiesLootProv(gen));
   }
}
