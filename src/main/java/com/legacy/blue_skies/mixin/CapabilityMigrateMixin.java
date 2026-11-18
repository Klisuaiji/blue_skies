package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesAttachmentTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class CapabilityMigrateMixin {
   @Mixin({Entity.class})
   public static class EntityMixin {
      private static final Map<String, String> TO_MIGRATE = (Map<String, String>)Util.make(() -> {
         Map<ResourceLocation, ResourceLocation> map = new HashMap<>();
         map.put(BlueSkies.locate("player_capability"), SkiesAttachmentTypes.PLAYER_ATTACHMENT.getName());
         return map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
      });

      @Inject(
         at = {@At("HEAD")},
         method = {"load(Lnet/minecraft/nbt/CompoundTag;)V"}
      )
      private void migrateForgeCaps(CompoundTag tag, CallbackInfo callback) {
         if (tag != null) {
            try {
               String forgeCapsKey = "ForgeCaps";
               if (tag.contains(forgeCapsKey, 10)) {
                  CompoundTag forgeCaps = tag.getCompound(forgeCapsKey);
                  Map<String, Tag> newData = new HashMap<>();

                  for (String oldKey : TO_MIGRATE.keySet()) {
                     if (forgeCaps.contains(oldKey)) {
                        newData.put(oldKey, forgeCaps.get(oldKey));
                     }
                  }

                  if (!newData.isEmpty()) {
                     CompoundTag neoforgeAttachments = tag.contains("neoforge:attachments")
                        ? tag.getCompound("neoforge:attachments")
                        : (CompoundTag)Util.make(new CompoundTag(), c -> tag.put("neoforge:attachments", c));

                     for (Entry<String, Tag> entry : newData.entrySet()) {
                        String oldKeyx = entry.getKey();
                        String newKey = TO_MIGRATE.get(oldKeyx);
                        if (!neoforgeAttachments.contains(newKey)) {
                           BlueSkies.LOGGER.debug("Migrating Forge Capability {} to NeoForge Attachment {} for {}", oldKeyx, newKey, this);
                           neoforgeAttachments.put(newKey, entry.getValue());
                        }
                     }
                  }
               }
            } catch (Throwable var11) {
               CrashReport crashreport = CrashReport.forThrowable(var11, "Loading entity NBT");
               CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being loaded - blue_skies: Migrating Forge Capabilities");
               ((Entity)this).fillCrashReportCategory(crashreportcategory);
               throw new ReportedException(crashreport);
            }
         }
      }
   }
}
