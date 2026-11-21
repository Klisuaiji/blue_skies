package com.legacy.blue_skies.client.audio;


import com.legacy.blue_skies.registries.SkiesDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;


public class SkiesMusicHandler {
   private static Minecraft mc = Minecraft.getInstance();
   private static SkiesMusicTicker musicTicker = new SkiesMusicTicker(mc);

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent.Post event) throws Exception {
      if (!mc.isPaused()) {
         musicTicker.tick();
      }
   }

   @SubscribeEvent
   public static void onMusicControl(PlaySoundEvent event) {
      SoundInstance sound = event.getSound();
      if (sound != null) {
         SoundSource category = sound.getSource();
         if (category == SoundSource.MUSIC
            && mc.player != null
            && SkiesDimensions.inSkyDimension(mc.player)
            && !sound.getLocation().toString().contains("blue_skies")
            && (musicTicker.playingMusic() || !musicTicker.playingMusic())) {
            event.setSound(null);
         }
      }
   }
}
