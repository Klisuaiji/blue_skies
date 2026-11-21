package com.legacy.blue_skies.data.objects.journal;

import com.google.gson.JsonObject;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.IToJson;
import net.minecraft.resources.ResourceLocation;

public class JournalSection implements IToJson<JournalSection> {
   public final ResourceLocation name;
   public final String displayText;
   public final ResourceLocation texture;

   public JournalSection(ResourceLocation name, String displayText, ResourceLocation texture) {
      this.name = name;
      this.displayText = displayText;
      this.texture = texture;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("name", this.name.toString());
      json.addProperty("display_text", this.displayText);
      json.addProperty("texture", this.texture.toString());
      return json;
   }

   public static String getDirectory() {
      return "journal/sections";
   }

   public static class Search extends JournalSection {
      public Search() {
         super(BlueSkies.locate("search"), "blue_skies.section.search", BlueSkies.locate("textures/gui/journal/sections/search.png"));
      }
   }
}
