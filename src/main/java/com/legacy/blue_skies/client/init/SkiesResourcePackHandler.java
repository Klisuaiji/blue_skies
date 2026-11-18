package com.legacy.blue_skies.client.init;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.Pack.Position;
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
      PathPackResources packResources = new PathPackResources(BlueSkies.find(folder), file.findResource(new String[]{"assets/blue_skies/" + folder}), true);
      return Pack.readMetaAndCreate(
         BlueSkies.find(folder), name, false, BuiltInPackSource.fixedResources(packResources), PackType.CLIENT_RESOURCES, Position.TOP, PackSource.BUILT_IN
      );
   }
}
