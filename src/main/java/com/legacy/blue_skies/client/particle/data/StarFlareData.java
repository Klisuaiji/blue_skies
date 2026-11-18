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

public class StarFlareData implements ParticleOptions {
   public static final Deserializer<StarFlareData> DESERIALIZER = new Deserializer<StarFlareData>() {
      public StarFlareData fromCommand(ParticleType<StarFlareData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
         reader.expect(' ');
         float r = (float)reader.readDouble();
         reader.expect(' ');
         float g = (float)reader.readDouble();
         reader.expect(' ');
         float b = (float)reader.readDouble();
         return new StarFlareData(r, g, b);
      }

      public StarFlareData fromNetwork(ParticleType<StarFlareData> particleTypeIn, FriendlyByteBuf buffer) {
         return new StarFlareData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
      }
   };
   public static final Codec<StarFlareData> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(starFlareData -> starFlareData.getRed()),
            Codec.FLOAT.fieldOf("g").forGetter(starFlareData -> starFlareData.getGreen()),
            Codec.FLOAT.fieldOf("b").forGetter(starFlareData -> starFlareData.getBlue())
         )
         .apply(instance, StarFlareData::new)
   );
   private final float red;
   private final float green;
   private final float blue;

   public StarFlareData(float r, float g, float b) {
      this.red = r;
      this.green = g;
      this.blue = b;
   }

   public void writeToNetwork(FriendlyByteBuf buffer) {
      buffer.writeFloat(this.red);
      buffer.writeFloat(this.green);
      buffer.writeFloat(this.blue);
   }

   public String writeToString() {
      return String.format(Locale.ROOT, "%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.red, this.green, this.blue);
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
