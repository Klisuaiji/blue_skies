package com.legacy.blue_skies.data.objects.journal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.screen.journal.JournalEntryRenderers;
import com.legacy.blue_skies.data.objects.IToJson;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class JournalEntry implements IToJson<JournalEntry> {
   public final String title;
   public final List<JournalEntry.Lore> lores;
   public final JournalEntryRenderers.IRenderable icon;
   public final ResourceLocation section;
   @Nullable
   public final ResourceLocation requirement;
   public final int priority;

   public JournalEntry(
      String title,
      List<JournalEntry.Lore> lores,
      JournalEntryRenderers.IRenderable icon,
      ResourceLocation section,
      ResourceLocation requirement,
      int priority
   ) {
      this.title = title;
      this.lores = new ArrayList<>(lores);
      this.icon = icon;
      this.section = section;
      this.requirement = requirement;
      this.priority = priority;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("title", this.title);
      JsonArray loreArray = new JsonArray();

      for (JournalEntry.Lore lore : this.lores) {
         loreArray.add(lore.toJson());
      }

      json.add("lores", loreArray);
      JsonObject iconJson = new JsonObject();
      iconJson.addProperty("type", this.icon.getType());
      iconJson.add("icon", this.icon.toJson());
      json.add("icon", iconJson);
      json.addProperty("section", this.section.toString());
      if (this.requirement != null) {
         json.addProperty("requirement", this.requirement.toString());
      }

      if (this.priority != 0) {
         json.addProperty("priority", this.priority);
      }

      return json;
   }

   public static String getDirectory() {
      return "journal/entries";
   }

   public static class Builder {
      private final String entryKey;
      private JournalEntryRenderers.IRenderable globalDisplay;
      private final String title;
      private final List<JournalEntry.Lore> lores;
      private final JournalEntryRenderers.IRenderable icon;
      private final ResourceLocation section;
      private ResourceLocation requirement;
      private int priority = 0;

      public Builder(String entryKey, String title, List<JournalEntry.Lore> lores, ResourceLocation section, JournalEntryRenderers.IRenderable icon) {
         this.entryKey = entryKey;
         this.title = title;
         this.lores = lores;
         this.section = section;
         this.icon = icon;
      }

      public JournalEntry.Builder requirement(ResourceLocation requirement) {
         this.requirement = requirement;
         return this;
      }

      public JournalEntry.Builder requirement(String requirement) {
         return this.requirement(BlueSkies.locate(requirement));
      }

      public JournalEntry.Builder priority(int priority) {
         this.priority = priority;
         return this;
      }

      public JournalEntry.Builder display(JournalEntryRenderers.IRenderable display) {
         this.globalDisplay = display;
         return this;
      }

      public JournalEntry build() {
         List<JournalEntry.Lore> lores;
         if (this.globalDisplay != null) {
            List<JournalEntry.Lore> newLores = new ArrayList<>();

            for (JournalEntry.Lore lore : this.lores) {
               newLores.add(new JournalEntry.Lore(lore.text, lore.requirement, this.globalDisplay));
            }

            lores = newLores;
         } else {
            lores = this.lores;
         }

         return new JournalEntry(this.title, lores, this.icon, this.section, this.requirement, this.priority);
      }

      public String getEntryKey() {
         return this.entryKey;
      }
   }

   public static class Lore implements IToJson<JournalEntry.Lore> {
      public final String text;
      @Nullable
      public final ResourceLocation requirement;
      @Nullable
      public final JournalEntryRenderers.IRenderable display;

      public Lore(String text, ResourceLocation requirement, JournalEntryRenderers.IRenderable display) {
         this.text = text;
         this.requirement = requirement;
         this.display = display;
      }

      @Override
      public JsonObject toJson() {
         JsonObject json = new JsonObject();
         json.addProperty("text", this.text);
         if (this.requirement != null) {
            json.addProperty("requirement", this.requirement.toString());
         }

         if (this.display != null) {
            JsonObject displayJson = new JsonObject();
            displayJson.addProperty("type", this.display.getType());
            displayJson.add("display", this.display.toJson());
            json.add("display", displayJson);
         }

         return json;
      }
   }
}
