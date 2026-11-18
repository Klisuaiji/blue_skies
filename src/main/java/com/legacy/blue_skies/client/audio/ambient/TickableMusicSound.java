package com.legacy.blue_skies.client.audio.ambient;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TickableMusicSound extends AbstractTickableSoundInstance {
   public TickableMusicSound(SoundEvent soundIn, SoundSource categoryIn, boolean repeat) {
      super(soundIn, categoryIn, SoundInstance.createUnseededRandom());
      this.looping = repeat;
      if (repeat) {
         this.delay = 0;
      }

      this.relative = true;
   }

   public void tick() {
   }

   public Attenuation getAttenuation() {
      return Attenuation.NONE;
   }
}
