package com.legacy.blue_skies.data.objects.journal;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.objects.IToJson;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class JournalRequirement implements IToJson<JournalRequirement> {
   public final ResourceLocation name;
   public final int everbrightProgression;
   public final int everdawnProgression;
   @Nullable
   public final ResourceLocation advancement;

   public JournalRequirement(ResourceLocation name, int everbrightProgression, int everdawnProgression, ResourceLocation advancement) {
      this.name = name;
      this.everbrightProgression = everbrightProgression;
      this.everdawnProgression = everdawnProgression;
      this.advancement = advancement;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("name", this.name.toString());
      if (this.everbrightProgression > 0) {
         json.addProperty("everbright_progression", this.everbrightProgression);
      }

      if (this.everdawnProgression > 0) {
         json.addProperty("everdawn_progression", this.everdawnProgression);
      }

      if (this.advancement != null) {
         json.addProperty("advancement", this.advancement.toString());
      }

      return json;
   }

   public static String getDirectory() {
      return "journal/requirements";
   }
}
