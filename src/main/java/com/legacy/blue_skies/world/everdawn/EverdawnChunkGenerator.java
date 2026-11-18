package com.legacy.blue_skies.world.everdawn;

import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.world.SkiesChunkGenerator;
import com.legacy.blue_skies.world.everdawn.biome.provider.EverdawnBiomeSource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class EverdawnChunkGenerator extends SkiesChunkGenerator {
   public static final Codec<EverdawnChunkGenerator> EVERDAWN_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(RegistryOps.retrieveGetter(Registries.BIOME), RegistryOps.retrieveGetter(Registries.NOISE_SETTINGS))
         .apply(instance, EverdawnChunkGenerator::new)
   );

   public EverdawnChunkGenerator(HolderGetter<Biome> biomeRegistry, HolderGetter<NoiseGeneratorSettings> noiseSettingsRegistry) {
      super(new EverdawnBiomeSource(biomeRegistry, 0, 320), noiseSettingsRegistry.getOrThrow(SkiesDimensions.EVERDAWN.getNoiseSettings().getKey()));
   }

   @Override
   protected int getSeedModifier() {
      return 1337;
   }

   @Override
   protected Codec<? extends ChunkGenerator> codec() {
      return EVERDAWN_CODEC;
   }
}
