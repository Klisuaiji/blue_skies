package com.legacy.blue_skies.client.renders.world;

import com.legacy.blue_skies.client.audio.ambient.SkiesWeatherAmbientSounds;
import com.legacy.blue_skies.client.audio.ambient.SnowstormAmbientSoundHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class BrightWeatherTick {
   public static final BrightWeatherTick INSTANCE = new BrightWeatherTick();
   private SoundInstance snowWind;
   private AmbientSoundHandler ambientSoundHandler;

   public void tick(int ticks, ClientLevel level, Minecraft mc, Camera camera) {
      this.tickLoops(mc.getSoundManager(), level, ticks, camera);
   }

   private void tickLoops(SoundManager soundManager, ClientLevel level, int ticks, Camera camera) {
      BlockPos blockpos = camera.getBlockPosition();
      if (level.isRaining()
         && ((Biome)level.getBiome(blockpos).value()).getPrecipitationAt(blockpos) == Precipitation.SNOW
         && camera.getEntity() instanceof LocalPlayer p) {
         if (this.ambientSoundHandler == null) {
            this.ambientSoundHandler = new SnowstormAmbientSoundHandler(p, soundManager);
         }

         this.ambientSoundHandler.tick();
         boolean shouldStopPlaying = level.getBrightness(LightLayer.SKY, blockpos) <= 0;
         boolean shouldPlay = level.isRaining()
            && ((Biome)level.getBiome(blockpos).value()).getPrecipitationAt(blockpos) != Precipitation.NONE
            && !shouldStopPlaying;
         if (shouldPlay && (this.snowWind != null && !soundManager.isActive(this.snowWind) || this.snowWind == null && !soundManager.isActive(this.snowWind))) {
            soundManager.play(this.snowWind = new SkiesWeatherAmbientSounds.SnowWindSound(p));
         }
      }
   }
}
