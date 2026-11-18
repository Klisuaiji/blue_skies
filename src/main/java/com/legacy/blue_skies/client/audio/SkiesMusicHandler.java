package com.legacy.blue_skies.client.audio;

import com.legacy.blue_skies.registries.SkiesDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.TickEvent.ClientTickEvent;
import net.neoforged.neoforge.event.TickEvent.Phase;
import net.neoforged.neoforge.event.TickEvent.Type;

public class SkiesMusicHandler {
   private static Minecraft mc = Minecraft.getInstance();
   private static SkiesMusicTicker musicTicker = new SkiesMusicTicker(mc);

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) throws Exception {
      Phase phase = event.phase;
      Type type = event.type;
      if (phase == Phase.END && type.equals(Type.CLIENT) && !mc.isPaused()) {
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
