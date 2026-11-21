package com.legacy.blue_skies.client.audio.ambient;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class FadingMusicSound extends TickableMusicSound {
   public int ticksPlayed;
   public boolean shouldNotFade = true;

   public FadingMusicSound(SoundEvent soundIn, SoundSource categoryIn, boolean repeat) {
      super(soundIn, categoryIn, repeat);
      this.volume = 0.05F;
   }

   @Override
   public void tick() {
      if (this.ticksPlayed >= 0) {
         if (this.shouldNotFade) {
            this.ticksPlayed++;
         } else {
            this.ticksPlayed -= 2;
         }

         this.ticksPlayed = Math.min(this.ticksPlayed, 40);
         this.volume = Math.max(0.0F, Math.min(this.ticksPlayed / 40.0F, 1.0F));
      } else {
         this.stop();
      }
   }

   public boolean isStopped() {
      return super.isStopped();
   }

   public void setVolume(float vol) {
      this.volume = vol;
   }
}
