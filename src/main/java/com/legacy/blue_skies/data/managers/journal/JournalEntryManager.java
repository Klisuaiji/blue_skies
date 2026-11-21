package com.legacy.blue_skies.data.managers.journal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.client.gui.screen.journal.JournalEntryRenderers;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.legacy.blue_skies.data.managers.SkiesSyncedDataManager;
import com.legacy.blue_skies.data.objects.journal.JournalEntry;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class JournalEntryManager extends SkiesSyncedDataManager<ResourceLocation, JournalEntry, JournalEntryManager> {
   public JournalEntryManager() {
      super(JournalEntry.getDirectory());
   }

   public JournalEntryManager newInstance() {
      return new JournalEntryManager();
   }

   public Pair<CompoundTag, CompoundTag> entryToNbt(ResourceLocation key, JournalEntry value) {
      CompoundTag keyNbt = new CompoundTag();
      keyNbt.putString("0", key.toString());
      CompoundTag valueNbt = (CompoundTag)JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, value.toJson());
      return Pair.of(keyNbt, valueNbt);
   }

   @Override
   public Pair<ResourceLocation, JournalEntry> entryFromNbt(CompoundTag keyNbt, CompoundTag valueNbt) {
      ResourceLocation key = ResourceLocation.parse(keyNbt.getString("0"));
      JsonObject json = ((JsonElement)NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, valueNbt)).getAsJsonObject();

      try {
         return Pair.of(key, (JournalEntry)this.parseJson(json, key).getSecond());
      } catch (SkiesDataManager.RegistryObjectNotFoundException e) {
         throw new IllegalArgumentException("Failed to get registry object");
      }
   }

   @Override
   protected Pair<ResourceLocation, JournalEntry> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      String title = GsonHelper.getAsString(json, "title");
      List<JournalEntry.Lore> lores = new ArrayList<>();

      for (JsonElement e : GsonHelper.getAsJsonArray(json, "lores")) {
         JsonObject loreJson = e.getAsJsonObject();
         String text = GsonHelper.getAsString(loreJson, "text");
         ResourceLocation requirement = loreJson.has("requirement") ? ResourceLocation.parse(GsonHelper.getAsString(loreJson, "requirement")) : null;
         JournalEntryRenderers.IRenderable display = null;
         if (loreJson.has("display")) {
            JsonObject displayJson = GsonHelper.getAsJsonObject(loreJson, "display");
            String displayType = GsonHelper.getAsString(displayJson, "type");
            JsonObject displayData = GsonHelper.getAsJsonObject(displayJson, "display");
            if (displayType.equals("entity")) {
               int scale = GsonHelper.getAsInt(displayData, "scale");
               int yOffset = GsonHelper.getAsInt(displayData, "y_offset");
               display = new JournalEntryRenderers.EntityDisplay(parseEntity(displayData, "entity"), scale, yOffset);
            } else {
               if (!displayType.equals("image")) {
                  throw new IllegalArgumentException("display type invalid");
               }

               display = new JournalEntryRenderers.ImageDisplay(ResourceLocation.parse(GsonHelper.getAsString(displayData, "image")));
            }
         }

         lores.add(new JournalEntry.Lore(text, requirement, display));
      }

      JournalEntryRenderers.IRenderable icon = null;
      JsonObject iconJson = GsonHelper.getAsJsonObject(json, "icon");
      String iconType = GsonHelper.getAsString(iconJson, "type");
      JsonObject iconData = GsonHelper.getAsJsonObject(iconJson, "icon");
      if (iconType.equals("item")) {
         icon = new JournalEntryRenderers.ItemIcon(parseItem(iconData, "item"));
      } else {
         if (!iconType.equals("texture")) {
            throw new IllegalArgumentException("icon type invalid");
         }

         icon = new JournalEntryRenderers.TextureIcon(ResourceLocation.parse(GsonHelper.getAsString(iconData, "texture")));
      }

      ResourceLocation section = ResourceLocation.parse(GsonHelper.getAsString(json, "section"));
      ResourceLocation requirement = json.has("requirement") ? ResourceLocation.parse(GsonHelper.getAsString(json, "requirement")) : null;
      int priority = GsonHelper.getAsInt(json, "priority", 0);
      return Pair.of(name, new JournalEntry(title, lores, icon, section, requirement, priority));
   }
}
