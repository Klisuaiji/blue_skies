package com.legacy.blue_skies.entities.util;

import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import net.minecraft.sounds.SoundEvent;

public class StarlitCrusherSpinSound extends MovingEntitySound {
   private final StarlitCrusherEntity crusher;
   private int ticksPlayed;

   public StarlitCrusherSpinSound(StarlitCrusherEntity living, SoundEvent sound, float volume) {
      super(living, sound, volume);
      this.crusher = living;
      this.ticksPlayed = 2;
   }

   @Override
   public boolean isStopped() {
      return this.volume == 0.0F || super.isStopped();
   }

   @Override
   public void tick() {
      if (!this.isStopped()) {
         this.x = this.entity.getX();
         this.y = this.entity.getY();
         this.z = this.entity.getZ();
         this.pitch = 1.0F;
         if (this.crusher.isSpinning()) {
            this.ticksPlayed++;
         } else {
            this.ticksPlayed--;
         }

         this.ticksPlayed = Math.min(this.ticksPlayed, 10);
         if (this.volume < this.movingVolume) {
            this.volume = Math.max(0.0F, Math.min(this.ticksPlayed / 10.0F, this.movingVolume));
         }
      } else {
         this.stop();
      }
   }
}
