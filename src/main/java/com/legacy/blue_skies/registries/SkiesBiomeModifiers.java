package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.biome_modifiers.PurifyBiome;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Static;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;

@RegistrarHolder
public class SkiesBiomeModifiers {
   public static final RegistrarHandler<Codec<? extends BiomeModifier>> SERIALIZER_HANDLER = RegistrarHandler.getOrCreate(
      Keys.BIOME_MODIFIER_SERIALIZERS, "blue_skies"
   );
   public static final Static<Codec<PurifyBiome>> PURIFY_SERIALIZER = SERIALIZER_HANDLER.createStatic("purify", () -> PurifyBiome.CODEC);

   static {
      RegistrarHandler<BiomeModifier> handler = RegistrarHandler.getOrCreate(Keys.BIOME_MODIFIERS, "blue_skies");
      handler.createPointer("purify", () -> PurifyBiome.INSTANCE);
   }
}
