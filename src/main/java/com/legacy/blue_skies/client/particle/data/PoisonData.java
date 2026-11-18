package com.legacy.blue_skies.client.particle.data;

import com.legacy.blue_skies.registries.SkiesParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PoisonData implements ParticleOptions {
   public static final Deserializer<PoisonData> DESERIALIZER = new Deserializer<PoisonData>() {
      public PoisonData fromCommand(ParticleType<PoisonData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
         reader.expect(' ');
         boolean small = reader.readBoolean();
         return new PoisonData(small);
      }

      public PoisonData fromNetwork(ParticleType<PoisonData> particleTypeIn, FriendlyByteBuf buffer) {
         return new PoisonData(buffer.readBoolean());
      }
   };
   public static final Codec<PoisonData> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(Codec.BOOL.fieldOf("small").forGetter(poisonData -> poisonData.isSmall())).apply(instance, PoisonData::new)
   );
   private final boolean small;

   public PoisonData(boolean small) {
      this.small = small;
   }

   public ParticleType<PoisonData> getType() {
      return SkiesParticles.POISON;
   }

   public void writeToNetwork(FriendlyByteBuf buffer) {
      buffer.writeBoolean(this.small);
   }

   public String writeToString() {
      return String.format(Locale.ROOT, "%s small=%s", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.small ? "true" : "false");
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isSmall() {
      return this.small;
   }
}
