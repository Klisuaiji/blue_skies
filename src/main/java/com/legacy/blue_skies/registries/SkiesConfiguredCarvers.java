package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.biome.BiomeGenerationSettings.Builder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration.CanyonShapeConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;

@RegistrarHolder
public class SkiesConfiguredCarvers {
   public static final RegistrarHandler<ConfiguredWorldCarver<?>> HANDLER = RegistrarHandler.getOrCreate(Registries.CONFIGURED_CARVER, "blue_skies")
      .bootstrap(c -> bootstrap(c));
   public static final ResourceKey<ConfiguredWorldCarver<?>> SKY_NOISE_CARVER = createKey("sky_noise_carver");
   public static final ResourceKey<ConfiguredWorldCarver<?>> SKY_CAVE_CARVER = createKey("sky_cave_carver");
   public static final ResourceKey<ConfiguredWorldCarver<?>> SKY_CANYON_CARVER = createKey("sky_canyon_carver");
   public static final ResourceKey<ConfiguredWorldCarver<?>> EVERBRIGHT_NOISE_CARVER = createKey("everbright_noise_carver");
   public static final ResourceKey<ConfiguredWorldCarver<?>> EVERBRIGHT_CAVE_CARVER = createKey("everbright_cave_carver");
   public static final ResourceKey<ConfiguredWorldCarver<?>> EVERBRIGHT_CANYON_CARVER = createKey("everbright_canyon_carver");

   private static ResourceKey<ConfiguredWorldCarver<?>> createKey(String key) {
      return ResourceKey.create(Registries.CONFIGURED_CARVER, BlueSkies.locate(key));
   }

   public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> bootstrap) {
      HolderGetter<Block> blockGetter = bootstrap.lookup(Registries.BLOCK);
      CarverConfiguration dummyConfig = new CarverConfiguration(
         0.0F,
         ConstantHeight.of(VerticalAnchor.absolute(0)),
         ConstantFloat.ZERO,
         VerticalAnchor.absolute(0),
         CarverDebugSettings.DEFAULT,
         HolderSet.direct(new Holder[0])
      );
      CaveCarverConfiguration caveConfig = new CaveCarverConfiguration(
         0.14285715F,
         BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127), 8),
         ConstantFloat.of(0.5F),
         VerticalAnchor.aboveBottom(10),
         blockGetter.getOrThrow(SkiesBlockTags.CARVER_REPLACEABLES),
         ConstantFloat.of(1.0F),
         ConstantFloat.of(1.0F),
         ConstantFloat.of(-0.7F)
      );
      CanyonCarverConfiguration canyonConfig = new CanyonCarverConfiguration(
         0.02F,
         BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(67), 8),
         ConstantFloat.of(3.0F),
         VerticalAnchor.aboveBottom(10),
         CarverDebugSettings.of(false, SkiesBlocks.lunar_button.defaultBlockState()),
         blockGetter.getOrThrow(SkiesBlockTags.CARVER_REPLACEABLES),
         UniformFloat.of(-0.125F, 0.125F),
         new CanyonShapeConfiguration(UniformFloat.of(0.75F, 1.0F), TrapezoidFloat.of(0.0F, 6.0F, 2.0F), 3, UniformFloat.of(0.75F, 1.0F), 1.0F, 0.0F)
      );
      bootstrap.register(SKY_NOISE_CARVER, SkiesFeatures.Carvers.SKY_NOISE_CARVER.configured(dummyConfig));
      bootstrap.register(SKY_CAVE_CARVER, SkiesFeatures.Carvers.SKY_CAVE_CARVER.configured(caveConfig));
      bootstrap.register(SKY_CANYON_CARVER, SkiesFeatures.Carvers.SKY_CANYON_CARVER.configured(canyonConfig));
      bootstrap.register(EVERBRIGHT_NOISE_CARVER, SkiesFeatures.Carvers.EVERBRIGHT_NOISE_CARVER.configured(dummyConfig));
      bootstrap.register(EVERBRIGHT_CAVE_CARVER, SkiesFeatures.Carvers.EVERBRIGHT_CAVE_CARVER.configured(caveConfig));
      bootstrap.register(EVERBRIGHT_CANYON_CARVER, SkiesFeatures.Carvers.EVERBRIGHT_CANYON_CARVER.configured(canyonConfig));
   }

   public static void addDawnCarvers(Builder builder) {
      builder.addCarver(Carving.AIR, SKY_CAVE_CARVER);
      builder.addCarver(Carving.AIR, SKY_CANYON_CARVER);
      builder.addCarver(Carving.AIR, SKY_NOISE_CARVER);
   }

   public static void addOceanCarvers(Builder builder) {
   }

   public static void addBrightCarvers(Builder builder) {
      builder.addCarver(Carving.AIR, EVERBRIGHT_CAVE_CARVER);
      builder.addCarver(Carving.AIR, EVERBRIGHT_CANYON_CARVER);
      builder.addCarver(Carving.AIR, EVERBRIGHT_NOISE_CARVER);
   }
}
