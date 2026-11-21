package com.legacy.blue_skies.data.providers.journal;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.journal.JournalRequirement;
import com.legacy.blue_skies.data.providers.SkiesDataProvider;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

public class JournalRequirementProv extends SkiesDataProvider<JournalRequirement> {
   public JournalRequirementProv(DataGenerator generator) {
      super(generator, JournalRequirement.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, JournalRequirement> gatherData() {
      Map<ResourceLocation, JournalRequirement> registry = new HashMap<>();
      this.register(registry, "enter_one", null, null, BlueSkies.locate("root"));
      this.register(registry, "enter_everbright", null, null, BlueSkies.locate("everbright/enter"));
      this.register(registry, "defeat_summoner", 1, null, null);
      this.register(registry, "defeat_crusher", 2, null, null);
      this.register(registry, "enter_everdawn", null, null, BlueSkies.locate("everdawn/enter"));
      this.register(registry, "defeat_alchemist", null, 1, null);
      this.register(registry, "defeat_arachnarch", null, 2, null);
      return registry;
   }

   private void register(
      Map<ResourceLocation, JournalRequirement> registry,
      String key,
      Integer everbrightProgression,
      Integer everdawnProgression,
      ResourceLocation advancement
   ) {
      ResourceLocation name = BlueSkies.locate(key);
      registry.put(
         name,
         new JournalRequirement(
            name, everbrightProgression == null ? -1 : everbrightProgression, everdawnProgression == null ? -1 : everdawnProgression, advancement
         )
      );
   }
}
