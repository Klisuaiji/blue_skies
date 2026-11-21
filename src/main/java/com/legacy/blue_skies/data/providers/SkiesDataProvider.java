package com.legacy.blue_skies.data.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.IToJson;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public abstract class SkiesDataProvider<D extends IToJson<D>> implements DataProvider {
   protected static final Logger LOGGER = BlueSkies.makeLogger(SkiesDataProvider.class);
   protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   protected Map<ResourceLocation, D> data;
   protected final DataGenerator generator;
   protected final String name;

   public SkiesDataProvider(DataGenerator generator, String name) {
      this.generator = generator;
      this.name = "blue_skies/" + name;
   }

   public CompletableFuture<?> run(CachedOutput cache) {
      this.data = this.gatherData();
      Path outputFolder = this.generator.getPackOutput().getOutputFolder();
      List<CompletableFuture<?>> list = new ArrayList<>();

      for (Entry<ResourceLocation, D> dataEntry : this.data.entrySet()) {
         Path outputFile = this.createPath(outputFolder, dataEntry.getKey());

         try {
            JsonObject json = dataEntry.getValue().toJson();
            list.add(DataProvider.saveStable(cache, json, outputFile));
         } catch (Exception e) {
            LOGGER.error("Couldn't save {} {}", this.name, outputFile, e);
         }
      }

      return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
   }

   protected abstract Map<ResourceLocation, D> gatherData();

   protected Path createPath(Path outputFolder, ResourceLocation dataEntryName) {
      return outputFolder.resolve("data/" + dataEntryName.getNamespace() + "/" + this.name + "/" + dataEntryName.getPath() + ".json");
   }

   public String getName() {
      return this.name;
   }
}
