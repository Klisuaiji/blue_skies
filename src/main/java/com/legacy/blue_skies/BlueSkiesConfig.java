package com.legacy.blue_skies;

import com.google.common.collect.ImmutableSet;
import com.legacy.structure_gel.api.config.ConfigBuilder;
import com.legacy.structure_gel.api.config.ConfigValueWrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

public class BlueSkiesConfig {
   public static final ModConfigSpec CLIENT_SPEC;
   public static final ModConfigSpec COMMON_SPEC;
   public static final BlueSkiesConfig.ClientConfig CLIENT;
   public static final BlueSkiesConfig.CommonConfig COMMON;

   static {
      Pair<BlueSkiesConfig.ClientConfig, ModConfigSpec> pair = new Builder().configure(BlueSkiesConfig.ClientConfig::new);
      CLIENT = (BlueSkiesConfig.ClientConfig)pair.getLeft();
      CLIENT_SPEC = (ModConfigSpec)pair.getRight();
      pair = new Builder().configure(BlueSkiesConfig.CommonConfig::new);
      COMMON = (BlueSkiesConfig.CommonConfig)pair.getLeft();
      COMMON_SPEC = (ModConfigSpec)pair.getRight();
   }

   public static class ClientConfig {
      private final ConfigValue<Boolean> customPanorama;
      private final ConfigValue<Boolean> limitBrightness;
      private final ConfigValue<Boolean> useSimpleInventoryTabs;
      private final ConfigValue<Boolean> invTabLeftAlign;
      private final ConfigValue<Integer> invTabSimpleX;
      private final ConfigValue<Integer> invTabSimpleY;
      private final ConfigValue<Boolean> invTabIsVertical;
      private final ConfigValue<Integer> invTabCreativeX;
      private final ConfigValue<Integer> invTabCreativeY;

      public ClientConfig(Builder builder) {
         builder.push("Visuals");
         this.customPanorama = ConfigBuilder.makeBoolean(builder, "custom_panorama", "Should blue skies override the title screen panorama.", true);
         this.limitBrightness = ConfigBuilder.makeBoolean(
            builder, "limit_brightness", "Whether or not brightness should be capped when inside the dimensions for aesthetic purposes.", true
         );
         builder.pop();
         builder.push("Inventory Tabs");
         this.useSimpleInventoryTabs = ConfigBuilder.makeBoolean(
            builder, "use_simple_inventory_tabs", "If set to true, inventory tabs will use simple buttons that can be placed anywhere on the screen.", false
         );
         builder.push("Sliding Tabs");
         this.invTabLeftAlign = ConfigBuilder.makeBoolean(
            builder, "left_align", "If set to true, inventory tabs appear on the bottom left corner instead of the bottom right.", false
         );
         builder.pop();
         builder.push("Buttons");
         this.invTabSimpleX = ConfigBuilder.makeConfig(builder, "x", "The x position of the inventory tabs.", -88);
         this.invTabSimpleY = ConfigBuilder.makeConfig(builder, "y", "The y position of the inventory tabs.", -83);
         this.invTabIsVertical = ConfigBuilder.makeBoolean(
            builder, "is_vertical", "If set to true, inventory tabs are sorted vertically. If set to false, they are sorted horizontally.", false
         );
         this.invTabCreativeX = ConfigBuilder.makeConfig(builder, "creative_x", "The x position of the inventory tabs when in the creative inventory.", -122);
         this.invTabCreativeY = ConfigBuilder.makeConfig(builder, "creative_y", "The y position of the inventory tabs when in the creative inventory.", 68);
         builder.pop();
         builder.pop();
      }

      public boolean isPanoramaCustom() {
         return (Boolean)this.customPanorama.get();
      }

      public boolean shouldLimitBrightness() {
         return (Boolean)this.limitBrightness.get();
      }

      public boolean useSimpleInventoryTabs() {
         return (Boolean)this.useSimpleInventoryTabs.get();
      }

      public boolean leftAlignInventoryTabs() {
         return (Boolean)this.invTabLeftAlign.get();
      }

      public int getInventoryTabX() {
         return (Integer)this.invTabSimpleX.get();
      }

      public int getInventoryTabY() {
         return (Integer)this.invTabSimpleY.get();
      }

      public int getCreativeInventoryTabX() {
         return (Integer)this.invTabCreativeX.get();
      }

      public int getCreativeInventoryTabY() {
         return (Integer)this.invTabCreativeY.get();
      }

      public boolean areInventoryTabsVertical() {
         return (Boolean)this.invTabIsVertical.get();
      }
   }

   public static class CommonConfig {
      private final ConfigValue<Boolean> logMissingRegistryObjectForDatapack;
      private final ConfigValue<Boolean> writeWorldRegionsToDisk;
      private final ConfigValue<Boolean> crashOnMissingCapabilityForDungeon;
      private final ConfigValue<Boolean> allowHalloweenContent;
      private final ConfigValue<Boolean> allowChristmasContent;
      private final ConfigValue<Integer> zealLighterCost;
      private final ConfigValueWrapper<List<? extends String>, Set<String>> allowedModsForFeatureGen;
      private final ConfigValueWrapper<List<? extends String>, Set<EntityType<?>>> allowedMobsForSpawning;

      public CommonConfig(Builder builder) {
         IEventBus bus = BlueSkies.MOD_BUS;
         builder.push("Debug");
         this.logMissingRegistryObjectForDatapack = ConfigBuilder.makeBoolean(
            builder,
            "log_missing_registry_data_for_datapacks",
            "When loading files from datapacks, set this to true to log missing registry data for things like items.",
            false
         );
         this.writeWorldRegionsToDisk = ConfigBuilder.makeBoolean(
            builder,
            "write_world_regions_to_disk",
            "When true, generated world regions, 1024 blocks wide, will be saved as a file to make generating chunks faster in already visited areas. If file size is a concern or issues arise, this may be disabled.",
            true
         );
         this.crashOnMissingCapabilityForDungeon = ConfigBuilder.makeBoolean(
            builder, "crash_on_missing_capability_for_dungeon", "When printing \"Blue Skies' player capability wasn't present for...\", crash the game.", false
         );
         builder.pop();
         builder.push("Holiday Content");
         this.allowHalloweenContent = ConfigBuilder.makeBoolean(
            builder, "allow_halloween_content", "Determines if Halloween cosmetic effects should happen.", true
         );
         this.allowChristmasContent = ConfigBuilder.makeBoolean(
            builder, "allow_christmas_content", "Determines if Christmas cosmetic effects should happen.", true
         );
         builder.pop();
         builder.push("Gatekeeper");
         this.zealLighterCost = ConfigBuilder.makeConfig(builder, "zeal_lighter_cost", "The emerald cost for the Zeal Lighter sold by the Gatekeeper.", 8);
         builder.pop();
         builder.push("Mod Compatibility");
         ConfigValue<List<? extends String>> features = ConfigBuilder.makeEmptyList(
            builder,
            "allowed_mods_for_feature_gen",
            "A list of mods that are allowed to generate features in the Everbright and Everdawn.\n This does not make them generate, it just allows them to pass the filter.",
            List.of("minecraft", "farlanders", "botania"),
            new ArrayList(),
            o -> true
         );
         this.allowedModsForFeatureGen = ConfigValueWrapper.create(features, l -> l.stream().collect(Collectors.toSet()), bus, "blue_skies");
         ConfigValue<List<? extends String>> mobs = ConfigBuilder.makeEmptyList(
            builder,
            "allowed_mobs_for_spawning",
            "A list of mobs that are allowed to spawn in the Everbright and Everdawn.\n This does not make them spawn, it just allows them to pass the filter.",
            List.of("minecraft:bee", "moolands:awful_cow", "alexs_mobs:grizzly_bear"),
            new ArrayList(),
            o -> true
         );
         this.allowedMobsForSpawning = ConfigValueWrapper.create(mobs, BlueSkiesConfig.CommonConfig::parseAllowedSpawns, bus, "blue_skies");
         builder.pop();
      }

      public boolean shouldLogMissingRegistryObjectForDatapack() {
         return (Boolean)this.logMissingRegistryObjectForDatapack.get();
      }

      public boolean writeWorldRegionsToDisk() {
         return (Boolean)this.writeWorldRegionsToDisk.get();
      }

      public boolean crashOnMissingCapabilityForDungeon() {
         return (Boolean)this.crashOnMissingCapabilityForDungeon.get();
      }

      public boolean isHalloween() {
         return (Boolean)this.allowHalloweenContent.get() && BlueSkies.isHalloween;
      }

      public boolean isChristmas() {
         return (Boolean)this.allowChristmasContent.get() && BlueSkies.isChristmas;
      }

      public int getZealLighterCost() {
         return (Integer)this.zealLighterCost.get();
      }

      public boolean isModAllowedForFeatureGen(String mod) {
         return "blue_skies".equals(mod) || "minecraft".equals(mod) || ((Set)this.allowedModsForFeatureGen.get()).contains(mod);
      }

      public boolean isEntityAllowedToSpawn(EntityType<?> entityType) {
         ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
         return id != null && ("blue_skies".equals(id.getNamespace()) || ((Set)this.allowedMobsForSpawning.get()).contains(entityType));
      }

      private static Set<EntityType<?>> parseAllowedSpawns(List<? extends String> input) {
         Set<EntityType<?>> entities = input.stream()
            .filter(ResourceLocation::isValidResourceLocation)
            .map(ResourceLocation::new)
            .filter(BuiltInRegistries.ENTITY_TYPE::containsKey)
            .<EntityType<?>>map(BuiltInRegistries.ENTITY_TYPE::get)
            .collect(Collectors.toCollection(HashSet::new));
         entities.addAll(
            ImmutableSet.of(
               EntityType.VILLAGER, EntityType.IRON_GOLEM, EntityType.CAT, EntityType.CAVE_SPIDER, EntityType.COD, EntityType.SALMON, new EntityType[0]
            )
         );
         return entities;
      }
   }
}
