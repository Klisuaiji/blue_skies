package com.legacy.blue_skies.world.everbright;

import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.world.SkiesChunkGenerator;
import com.legacy.blue_skies.world.everbright.biome.provider.EverbrightBiomeSource;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class EverbrightChunkGenerator extends SkiesChunkGenerator {
   public static final Codec<EverbrightChunkGenerator> EVERBRIGHT_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(RegistryOps.retrieveGetter(Registries.BIOME), RegistryOps.retrieveGetter(Registries.NOISE_SETTINGS))
         .apply(instance, EverbrightChunkGenerator::new)
   );

   public EverbrightChunkGenerator(HolderGetter<Biome> biomeRegistry, HolderGetter<NoiseGeneratorSettings> noiseSettingsRegistry) {
      super(new EverbrightBiomeSource(biomeRegistry, 0, 320), noiseSettingsRegistry.getOrThrow(SkiesDimensions.EVERBRIGHT.getNoiseSettings().getKey()));
   }

   @Override
   protected int getSeedModifier() {
      return 80085;
   }

   @Override
   protected Codec<? extends ChunkGenerator> codec() {
      return EVERBRIGHT_CODEC;
   }
}
