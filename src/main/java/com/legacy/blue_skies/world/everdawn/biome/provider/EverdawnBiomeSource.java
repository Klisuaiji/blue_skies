package com.legacy.blue_skies.world.everdawn.biome.provider;

import com.legacy.blue_skies.registries.SkiesBiomes;
import com.legacy.blue_skies.world.SkiesBiomeSource;
import com.legacy.blue_skies.world.biome_provider.provider.EverdawnBiomeProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public class EverdawnBiomeSource extends SkiesBiomeSource {
   public static final Codec<EverdawnBiomeSource> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            RegistryOps.retrieveGetter(Registries.BIOME),
            Codec.INT.fieldOf("min_y").stable().forGetter(biomeSource -> biomeSource.minY),
            Codec.INT.fieldOf("height").stable().forGetter(biomeSource -> biomeSource.height),
            RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").stable().forGetter(biomeSource -> biomeSource.biomeHolderSet())
         )
         .apply(instance, instance.stable(EverdawnBiomeSource::new))
   );

   public EverdawnBiomeSource(HolderGetter<Biome> biomeRegistry, int minY, int height) {
      this(biomeRegistry, minY, height, HolderSet.direct(r -> biomeRegistry.getOrThrow(r.getKey()), SkiesBiomes.EVERDAWN_BIOMES));
   }

   private EverdawnBiomeSource(HolderGetter<Biome> biomeRegistry, int minY, int height, HolderSet<Biome> biomes) {
      super(biomeRegistry, minY, height, biomes, new EverdawnBiomeProvider(minY, height));
   }

   protected Codec<? extends BiomeSource> codec() {
      return CODEC;
   }
}
