package com.legacy.blue_skies.data.providers.journal;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.journal.JournalSection;
import com.legacy.blue_skies.data.providers.SkiesDataProvider;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

public class JournalSectionProv extends SkiesDataProvider<JournalSection> {
   public JournalSectionProv(DataGenerator generator) {
      super(generator, JournalSection.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, JournalSection> gatherData() {
      Map<ResourceLocation, JournalSection> registry = new HashMap<>();
      this.register(registry, "general");
      this.register(registry, "everbright");
      this.register(registry, "everdawn");
      return registry;
   }

   private void register(Map<ResourceLocation, JournalSection> registry, String key) {
      ResourceLocation name = BlueSkies.locate(key);
      registry.put(name, new JournalSection(name, "blue_skies.section." + key, BlueSkies.locate("textures/gui/journal/sections/" + key + ".png")));
   }
}
