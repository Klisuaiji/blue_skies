package com.legacy.blue_skies.assets.managers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class JournalLangManager extends SkiesDataManager<String, Map<String, String>> {
   public JournalLangManager() {
      super("journal_lang");
      this.hasOverrides = true;
   }

   protected void registerData(Map<String, Map<String, String>> activeData, String key, Map<String, String> value) {
      if (activeData.containsKey(key)) {
         value.entrySet().forEach(e -> activeData.get(key).putIfAbsent(e.getKey(), e.getValue()));
      } else {
         activeData.put(key, value);
      }
   }

   @Override
   protected Pair<String, Map<String, String>> parseJson(JsonObject json, ResourceLocation name) throws SkiesDataManager.RegistryObjectNotFoundException {
      String lang = name.getPath();
      Map<String, String> map = new HashMap<>();
      JsonObject obj = json.getAsJsonObject();
      obj.entrySet().forEach(e -> map.put((String)e.getKey(), ((JsonElement)e.getValue()).getAsString()));
      return Pair.of(lang, map);
   }

   @Deprecated
   public Map<String, String> getValue(String langKey) {
      return this.data.getOrDefault(langKey, this.data.getOrDefault("en_us", new HashMap<>()));
   }

   public Component getTranslation(String key) {
      Minecraft mc = Minecraft.getInstance();
      return Component.literal(this.getValue(mc.options.languageCode).getOrDefault(key, key));
   }
}
