package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.items.util.SkiesArmorMaterial;
import com.legacy.blue_skies.util.StringUtil;
import com.legacy.structure_gel.api.events.RegisterArmorTrimTexturesEvent;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.ItemLike;

@RegistrarHolder
public class SkiesArmorTrims {
   public static final RegistrarHandler<TrimPattern> PATTERN_HANDLER = RegistrarHandler.getOrCreate(Registries.TRIM_PATTERN, "blue_skies")
      .bootstrap(SkiesArmorTrims.Pattterns::trimBootstrap);
   public static final RegistrarHandler<TrimMaterial> MATERIAL_HANDLER = RegistrarHandler.getOrCreate(Registries.TRIM_MATERIAL, "blue_skies")
      .bootstrap(SkiesArmorTrims.Materials::materialBootstrap);

   public static void registerSpriteData(RegisterArmorTrimTexturesEvent event) {
      SkiesArmorTrims.Pattterns.patterns().forEach(pattern -> event.registerPatternSprite(pattern));
      SkiesArmorTrims.Materials.materials()
         .forEach(material -> event.registerMaterialSprite(SkiesArmorTrims.Materials.assetID((ResourceKey<TrimMaterial>)material), material.location(), true));
      event.registerPaletteOverride(
         SkiesArmorTrims.Materials.PYROPE, Map.of(SkiesArmorMaterial.PYROPE, SkiesArmorTrims.Materials.darkerID(SkiesArmorTrims.Materials.PYROPE))
      );
      event.registerPaletteOverride(
         SkiesArmorTrims.Materials.AQUITE, Map.of(SkiesArmorMaterial.AQUITE, SkiesArmorTrims.Materials.darkerID(SkiesArmorTrims.Materials.AQUITE))
      );
      event.registerPaletteOverride(
         SkiesArmorTrims.Materials.DIOPSIDE, Map.of(SkiesArmorMaterial.DIOPSIDE, SkiesArmorTrims.Materials.darkerID(SkiesArmorTrims.Materials.DIOPSIDE))
      );
      event.registerPaletteOverride(
         SkiesArmorTrims.Materials.CHAROITE, Map.of(SkiesArmorMaterial.CHAROITE, SkiesArmorTrims.Materials.darkerID(SkiesArmorTrims.Materials.CHAROITE))
      );
      event.registerPaletteOverride(
         SkiesArmorTrims.Materials.HORIZONITE, Map.of(SkiesArmorMaterial.HORIZONITE, SkiesArmorTrims.Materials.darkerID(SkiesArmorTrims.Materials.HORIZONITE))
      );
      event.registerMaterialBrightness(SkiesArmorTrims.Materials.MOONSTONE, 15);
      event.registerMaterialBrightness(SkiesArmorTrims.Materials.SUNSTONE, 15);
   }

   public interface Materials {
      ResourceKey<TrimMaterial> MOONSTONE = SkiesArmorTrims.MATERIAL_HANDLER.key("moonstone");
      ResourceKey<TrimMaterial> SUNSTONE = SkiesArmorTrims.MATERIAL_HANDLER.key("sunstone");
      ResourceKey<TrimMaterial> HORIZONITE = SkiesArmorTrims.MATERIAL_HANDLER.key("horizonite");
      ResourceKey<TrimMaterial> FALSITE = SkiesArmorTrims.MATERIAL_HANDLER.key("falsite");
      ResourceKey<TrimMaterial> VENTIUM = SkiesArmorTrims.MATERIAL_HANDLER.key("ventium");
      ResourceKey<TrimMaterial> PYROPE = SkiesArmorTrims.MATERIAL_HANDLER.key("pyrope");
      ResourceKey<TrimMaterial> AQUITE = SkiesArmorTrims.MATERIAL_HANDLER.key("aquite");
      ResourceKey<TrimMaterial> DIOPSIDE = SkiesArmorTrims.MATERIAL_HANDLER.key("diopside");
      ResourceKey<TrimMaterial> CHAROITE = SkiesArmorTrims.MATERIAL_HANDLER.key("charoite");

      private static void materialBootstrap(BootstapContext<TrimMaterial> context) {
         register(context, MOONSTONE, SkiesBlocks.moonstone_crystal, "#D9EEF7", 0.108412F);
         register(context, SUNSTONE, SkiesBlocks.sunstone_crystal, "#F6CBB7", 0.508412F);
         register(context, HORIZONITE, SkiesItems.horizonite_ingot, "#FFA43A", 0.518412F);
         register(context, FALSITE, SkiesItems.falsite_ingot, "#8980ED", 0.908412F);
         register(context, VENTIUM, SkiesItems.ventium_ingot, "#E8685C", 0.408412F);
         register(context, PYROPE, SkiesItems.pyrope_gem, "#FF8CB2", 0.418412F);
         register(context, AQUITE, SkiesItems.aquite, "#A3EDF7", 0.808412F);
         register(context, DIOPSIDE, SkiesItems.diopside_gem, "#94F252", 0.708412F);
         register(context, CHAROITE, SkiesItems.charoite, "#B289D9", 1.008412F);
      }

      static List<ResourceKey<TrimMaterial>> materials() {
         return List.of(MOONSTONE, SUNSTONE, HORIZONITE, FALSITE, VENTIUM, PYROPE, AQUITE, DIOPSIDE, CHAROITE);
      }

      private static void register(
         BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, ItemLike ingredient, String hexColor, float itemModelIndex
      ) {
         register(context, key, ingredient, hexColor, itemModelIndex, Map.of());
      }

      private static void register(
         BootstapContext<TrimMaterial> context,
         ResourceKey<TrimMaterial> key,
         ItemLike ingredient,
         String hexColor,
         float itemModelIndex,
         Map<ArmorMaterials, String> overrideArmorMaterials
      ) {
         TrimMaterial trimmaterial = TrimMaterial.create(
            assetID(key),
            ingredient.asItem(),
            itemModelIndex,
            Component.translatable(Util.makeDescriptionId("trim_material", key.location())).withStyle(Style.EMPTY.withColor(StringUtil.parseHex(hexColor))),
            overrideArmorMaterials
         );
         context.register(key, trimmaterial);
      }

      static String assetID(ResourceKey<TrimMaterial> key) {
         return key.location().getNamespace() + "-" + key.location().getPath();
      }

      static String darkerID(ResourceKey<TrimMaterial> key) {
         return assetID(key) + "_darker";
      }
   }

   public interface Pattterns {
      ResourceKey<TrimPattern> BANISHED = SkiesArmorTrims.PATTERN_HANDLER.key("banished");
      ResourceKey<TrimPattern> THWARTED = SkiesArmorTrims.PATTERN_HANDLER.key("thwarted");
      ResourceKey<TrimPattern> REGROWTH = SkiesArmorTrims.PATTERN_HANDLER.key("regrowth");
      ResourceKey<TrimPattern> TOXIC = SkiesArmorTrims.PATTERN_HANDLER.key("toxic");

      private static void trimBootstrap(BootstapContext<TrimPattern> context) {
         register(context, BANISHED, SkiesItems.banished_trim);
         register(context, THWARTED, SkiesItems.thwarted_trim);
         register(context, REGROWTH, SkiesItems.regrowth_trim);
         register(context, TOXIC, SkiesItems.toxic_trim);
      }

      static List<ResourceKey<TrimPattern>> patterns() {
         return List.of(BANISHED, THWARTED, REGROWTH, TOXIC);
      }

      private static void register(BootstapContext<TrimPattern> context, ResourceKey<TrimPattern> key, Item templateItem) {
         TrimPattern trimpattern = new TrimPattern(
            key.location(),
            BuiltInRegistries.ITEM.wrapAsHolder(templateItem),
            Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())),
            false
         );
         context.register(key, trimpattern);
      }
   }
}
