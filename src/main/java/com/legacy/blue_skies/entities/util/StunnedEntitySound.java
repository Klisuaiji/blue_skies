package com.legacy.blue_skies.entities.util;

import com.legacy.blue_skies.entities.util.interfaces.IStunnableMob;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

public class StunnedEntitySound extends MovingEntitySound {
   private int ticksPlayed = 2;

   public StunnedEntitySound(LivingEntity living, SoundEvent sound, float volume) {
      super(living, sound, volume);
   }

   @Override
   public boolean isStopped() {
      return !(this.entity instanceof IStunnableMob) ? true : this.volume == 0.0F || super.isStopped();
   }

   @Override
   public void tick() {
      if (this.entity instanceof IStunnableMob) {
         if (!this.isStopped()) {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
            this.pitch = 1.0F;
            if (((IStunnableMob)this.entity).isStunned()) {
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
}
