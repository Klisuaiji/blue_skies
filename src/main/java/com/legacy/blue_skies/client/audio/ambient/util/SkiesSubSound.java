package com.legacy.blue_skies.client.audio.ambient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkiesSubSound extends AbstractTickableSoundInstance {
   private final LocalPlayer player;

   public SkiesSubSound(LocalPlayer playerIn, SoundEvent soundIn) {
      this(playerIn, soundIn, 1.0F);
   }

   public SkiesSubSound(LocalPlayer playerIn, SoundEvent soundIn, float pitchIn) {
      super(soundIn, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
      this.pitch = pitchIn;
      this.player = playerIn;
      this.looping = false;
      this.delay = 0;
      this.volume = 0.7F;
      this.relative = true;
   }

   public void tick() {
      if (!this.player.isAlive() || Minecraft.getInstance().isPaused()) {
         this.stop();
      }
   }
}
