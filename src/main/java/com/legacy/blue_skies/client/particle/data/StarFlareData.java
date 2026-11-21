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

public class StarFlareData implements ParticleOptions {
   // 1.21.1 particle payloads serialize through a MapCodec on ParticleType and a StreamCodec on the network,
   // replacing the removed ParticleOptions.Deserializer.
   public static final MapCodec<StarFlareData> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(starFlareData -> starFlareData.getRed()),
            Codec.FLOAT.fieldOf("g").forGetter(starFlareData -> starFlareData.getGreen()),
            Codec.FLOAT.fieldOf("b").forGetter(starFlareData -> starFlareData.getBlue())
         )
         .apply(instance, StarFlareData::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, StarFlareData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT,
      StarFlareData::getRed,
      ByteBufCodecs.FLOAT,
      StarFlareData::getGreen,
      ByteBufCodecs.FLOAT,
      StarFlareData::getBlue,
      StarFlareData::new
   );
   private final float red;
   private final float green;
   private final float blue;

   public StarFlareData(float r, float g, float b) {
      this.red = r;
      this.green = g;
      this.blue = b;
   }

   public ParticleType<StarFlareData> getType() {
      return SkiesParticles.STAR_FLARE;
   }

   @OnlyIn(Dist.CLIENT)
   public float getRed() {
      return this.red;
   }

   @OnlyIn(Dist.CLIENT)
   public float getGreen() {
      return this.green;
   }

   @OnlyIn(Dist.CLIENT)
   public float getBlue() {
      return this.blue;
   }
}
