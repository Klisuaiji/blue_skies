package com.legacy.blue_skies.client.audio.ambient;

import com.legacy.blue_skies.data.objects.tags.SkiesBiomeTags;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class SkiesWeatherAmbientSounds {
   public static class SandstormSound extends AbstractTickableSoundInstance {
      private final LocalPlayer player;
      private int ticksInWeather;

      public SandstormSound(LocalPlayer playerIn) {
         super(SkiesSounds.AMBIENT_SANDSTORM_WIND, SoundSource.AMBIENT, playerIn.getRandom());
         this.player = playerIn;
         this.looping = true;
         this.delay = 0;
         this.volume = 0.1F;
         this.relative = true;
      }

      public void tick() {
         Minecraft mc = Minecraft.getInstance();
         BlockPos blockpos = mc.getCameraEntity().blockPosition();
         boolean shouldStopPlaying = mc.level.getBrightness(LightLayer.SKY, blockpos) <= 0;
         boolean shouldPlay = mc.level.isRaining()
            && mc.level.registryAccess().registryOrThrow(Registries.BIOME).getOrCreateTag(SkiesBiomeTags.HAS_SANDSTORMS).contains(mc.level.getBiome(blockpos))
            && !shouldStopPlaying;
         boolean shouldPitchLower = mc.level.getBrightness(LightLayer.SKY, blockpos) <= 5
               && blockpos.getY() >= mc.level.getSeaLevel() + 1
               && mc.gui.getBossOverlay().shouldPlayMusic()
            || mc.level.getBrightness(LightLayer.SKY, blockpos) <= 12;
         if (this.player.isAlive() && this.ticksInWeather >= 0) {
            if (shouldPlay) {
               this.ticksInWeather++;
            } else {
               this.ticksInWeather -= 2;
            }

            this.ticksInWeather = Math.min(this.ticksInWeather, 40);
            this.volume = Math.max(0.0F, Math.min(this.ticksInWeather / 40.0F, shouldPitchLower ? 0.8F : 1.0F));
            this.pitch = shouldPitchLower ? 0.7F : 1.0F;
         } else {
            this.stop();
         }
      }
   }

   public static class SnowWindSound extends AbstractTickableSoundInstance {
      private final LocalPlayer player;
      private int ticksInWeather;
      private float requestedVolume = 0.8F;
      private float requestedPitch = 1.0F;

      public SnowWindSound(LocalPlayer playerIn) {
         super(SkiesSounds.AMBIENT_SNOW_WIND, SoundSource.AMBIENT, playerIn.getRandom());
         this.player = playerIn;
         this.looping = true;
         this.delay = 0;
         this.volume = 0.1F;
         this.relative = true;
      }

      public void tick() {
         Minecraft mc = Minecraft.getInstance();
         Level world = this.player.level();
         BlockPos blockpos = mc.getCameraEntity().blockPosition();
         boolean shouldStopPlaying = world.getBrightness(LightLayer.SKY, blockpos) <= 0;
         boolean shouldPlay = world.isRaining()
            && ((Biome)world.getBiome(blockpos).value()).getPrecipitationAt(blockpos) == Precipitation.SNOW
            && !shouldStopPlaying;
         boolean shouldPitchLower = world.getBrightness(LightLayer.SKY, blockpos) <= 5
               && blockpos.getY() >= world.getSeaLevel() + 1
               && mc.gui.getBossOverlay().shouldPlayMusic()
            || world.getBrightness(LightLayer.SKY, blockpos) <= 12;
         if (this.player.isAlive() && this.ticksInWeather >= 0) {
            if (shouldPlay) {
               this.ticksInWeather++;
            } else {
               this.ticksInWeather -= 2;
            }

            this.ticksInWeather = Math.min(this.ticksInWeather, 40);
            this.volume = Math.max(0.0F, Math.min(this.volume + (this.requestedVolume > this.volume ? 0.05F : -0.05F), 0.8F));
            this.pitch = Math.max(0.0F, Math.min(this.pitch + (this.requestedPitch > this.pitch ? 0.01F : -0.01F), 1.0F));
            this.requestedPitch = shouldPitchLower ? 0.7F : 1.0F;
            this.requestedVolume = shouldPlay ? 0.8F : 0.0F;
         } else {
            this.stop();
         }
      }
   }
}
