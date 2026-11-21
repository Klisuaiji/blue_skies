package com.legacy.blue_skies.client.init;

import com.legacy.blue_skies.BlueSkies;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

public class SkiesResourcePackHandler {
   public static final Pack LEGACY_PACK = createPack(Component.literal("Blue Skies Legacy Art"), "legacy_pack").hidden();

   public static void packRegistry(AddPackFindersEvent event) {
      if (event.getPackType() == PackType.CLIENT_RESOURCES) {
      }
   }

   private static void register(AddPackFindersEvent event, MutableComponent name, String folder) {
      event.addRepositorySource(consumer -> consumer.accept(createPack(name, folder)));
   }

   public static Pack createPack(MutableComponent name, String folder) {
      IModFile file = ModList.get().getModFileById("blue_skies").getFile();
      // 1.21.1 pack API: PathPackResources requires a PackLocationInfo, and Pack.readMetaAndCreate
      // was reshaped to (location, resourcesSupplier, packType, PackSelectionConfig).
      PackLocationInfo location = new PackLocationInfo(BlueSkies.find(folder), name, PackSource.BUILT_IN, Optional.empty());
      PathPackResources packResources = new PathPackResources(location, file.findResource("assets/blue_skies/" + folder));
      return Pack.readMetaAndCreate(
         location,
         BuiltInPackSource.fixedResources(packResources),
         PackType.CLIENT_RESOURCES,
         new PackSelectionConfig(false, Pack.Position.TOP, false)
      );
   }
}
