package com.legacy.blue_skies.data.managers;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.mojang.datafixers.util.Pair;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.Logger;

public abstract class SkiesDataManager<K, V> extends SimpleJsonResourceReloadListener {
   protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   public static final Logger LOGGER = BlueSkies.makeLogger(SkiesDataManager.class);
   protected final String name;
   protected Map<K, V> data = Maps.newHashMap();
   protected boolean hasOverrides = false;

   public SkiesDataManager(String name) {
      super(GSON, "blue_skies/" + name);
      this.name = name;
   }

   protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
      Map<K, V> newDataMap = Maps.newHashMap();
      new HashSet();
      Set dataSet;
      if (this.hasOverrides) {
         Set<Entry<ResourceLocation, JsonElement>> newData = new HashSet<>();
         String folder = "blue_skies/" + this.name;
         resourceManager.listResources(folder, fileName -> fileName.toString().endsWith(".json"))
            .forEach(
               (fileName, resource) -> {
                  for (Resource iResource : resourceManager.getResourceStack(fileName)) {
                     ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
                        fileName.getNamespace(), fileName.getPath().substring(folder.length() + 1, fileName.getPath().length() - ".json".length())
                     );

                     try {
                        InputStream inputStream = iResource.open();
                        Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        JsonObject jsonObj = (JsonObject)GsonHelper.fromJson(GSON, reader, JsonObject.class);
                        if (jsonObj != null) {
                           newData.add(new SimpleEntry<>(location, jsonObj));
                        }
                     } catch (Exception e) {
                        LOGGER.error("Failed to load {}", location.toString());
                     }
                  }
               }
            );
         dataSet = newData;
      } else {
         dataSet = jsonMap.entrySet();
      }

      for (Entry<ResourceLocation, JsonElement> entry : dataSet) {
         ResourceLocation name = entry.getKey();

         try {
            Pair<K, V> parsedData = this.parseJson(entry.getValue().getAsJsonObject(), name);
            this.registerData(newDataMap, (K)parsedData.getFirst(), (V)parsedData.getSecond());
         } catch (SkiesDataManager.RegistryObjectNotFoundException e) {
            if (BlueSkiesConfig.COMMON.shouldLogMissingRegistryObjectForDatapack()) {
               LOGGER.error(e);
            }
         } catch (Exception e) {
            LOGGER.error("Parsing error loading {}: {}", this.name, name, e);
         }
      }

      this.data = newDataMap;
      LOGGER.info("Loaded {} {}", newDataMap.size(), this.name);
   }

   protected abstract Pair<K, V> parseJson(JsonObject var1, ResourceLocation var2) throws SkiesDataManager.RegistryObjectNotFoundException;

   protected void registerData(Map<K, V> activeData, K key, V value) {
      activeData.put(key, value);
   }

   public Map<K, V> getData() {
      return this.data;
   }

   @Nullable
   public V getValue(K key) {
      return this.data.get(key);
   }

   @Nullable
   public static <R> R parseRegistry(JsonObject json, String key, Registry<R> registry, R defaultVal) throws SkiesDataManager.RegistryObjectNotFoundException {
      String val = GsonHelper.getAsString(json, key, null);
      if (val == null) {
         return defaultVal;
      } else if (registry.containsKey(ResourceLocation.parse(val))) {
         return (R)registry.get(ResourceLocation.parse(val));
      } else {
         throw new SkiesDataManager.RegistryObjectNotFoundException(String.format("The %s does not exist in the %s registry", val, registry.key()));
      }
   }

   public static <R> R parseRegistry(JsonObject json, String key, Registry<R> registry) throws SkiesDataManager.RegistryObjectNotFoundException {
      if (json.has(key)) {
         return parseRegistry(json, key, registry, null);
      } else {
         throw new SkiesDataManager.RegistryObjectNotFoundException(String.format("No value was stored under %s in the %s registry", key, registry.key()));
      }
   }

   public static Item parseItem(JsonObject json, String key) throws SkiesDataManager.RegistryObjectNotFoundException {
      return parseRegistry(json, key, BuiltInRegistries.ITEM);
   }

   public static Item parseItem(JsonObject json, String key, Item defaultVal) throws SkiesDataManager.RegistryObjectNotFoundException {
      return parseRegistry(json, key, BuiltInRegistries.ITEM, defaultVal);
   }

   public static Block parseBlock(JsonObject json, String key) throws SkiesDataManager.RegistryObjectNotFoundException {
      return parseRegistry(json, key, BuiltInRegistries.BLOCK);
   }

   public static Block parseBlock(JsonObject json, String key, Block defaultVal) throws SkiesDataManager.RegistryObjectNotFoundException {
      return parseRegistry(json, key, BuiltInRegistries.BLOCK, defaultVal);
   }

   public static EntityType<?> parseEntity(JsonObject json, String key) throws SkiesDataManager.RegistryObjectNotFoundException {
      return parseRegistry(json, key, BuiltInRegistries.ENTITY_TYPE);
   }

   public static EntityType<?> parseEntity(JsonObject json, String key, EntityType<?> defaultVal) throws SkiesDataManager.RegistryObjectNotFoundException {
      return parseRegistry(json, key, BuiltInRegistries.ENTITY_TYPE, defaultVal);
   }

   public static class RegistryObjectNotFoundException extends Exception {
      private static final long serialVersionUID = -3662308733522131159L;
      private final String msg;

      public RegistryObjectNotFoundException(String msg) {
         this.msg = msg;
      }

      @Override
      public String getMessage() {
         return this.msg;
      }
   }
}
