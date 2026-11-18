package com.legacy.blue_skies.entities.util;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovingEntitySound extends AbstractTickableSoundInstance {
   protected LivingEntity entity;
   protected float movingVolume = 2.0F;
   private boolean canRepeat = true;
   private boolean forceStopped = false;

   public MovingEntitySound(LivingEntity living, SoundEvent sound, float volume, float pitch, boolean canRepeat) {
      super(sound, SoundSource.HOSTILE, living.getRandom());
      this.entity = living;
      this.movingVolume = volume;
      this.pitch = pitch;
      this.canRepeat = canRepeat;
   }

   public MovingEntitySound(LivingEntity living, SoundEvent sound, float volume, float pitch) {
      this(living, sound, volume, pitch, true);
   }

   public MovingEntitySound(LivingEntity living, SoundEvent sound, float volume) {
      this(living, sound, volume, 1.0F);
   }

   public MovingEntitySound(LivingEntity living, SoundEvent sound) {
      this(living, sound, 2.0F);
   }

   public boolean isStopped() {
      return this.forceStopped ? true : this.entity.getHealth() <= 0.0F || !this.entity.isAlive() || super.isStopped();
   }

   public boolean isLooping() {
      return this.canRepeat;
   }

   public void tick() {
      if (!this.isStopped()) {
         this.x = (float)this.entity.position().x();
         this.y = (float)this.entity.position().y();
         this.z = (float)this.entity.position().z();
         this.volume = this.movingVolume;
      } else {
         this.stop();
      }
   }

   public void forceStop() {
      this.forceStopped = true;
   }
}
