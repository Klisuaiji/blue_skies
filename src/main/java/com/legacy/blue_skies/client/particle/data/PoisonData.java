package com.legacy.blue_skies.client.particle.data;

import com.legacy.blue_skies.registries.SkiesParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PoisonData implements ParticleOptions {
   // 1.21.1 particle payloads serialize through a MapCodec on ParticleType and a StreamCodec on the network,
   // replacing the removed ParticleOptions.Deserializer.
   public static final MapCodec<PoisonData> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(Codec.BOOL.fieldOf("small").forGetter(poisonData -> poisonData.isSmall())).apply(instance, PoisonData::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, PoisonData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.BOOL, PoisonData::isSmall, PoisonData::new
   );
   private final boolean small;

   public PoisonData(boolean small) {
      this.small = small;
   }

   public ParticleType<PoisonData> getType() {
      return SkiesParticles.POISON;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isSmall() {
      return this.small;
   }
}
