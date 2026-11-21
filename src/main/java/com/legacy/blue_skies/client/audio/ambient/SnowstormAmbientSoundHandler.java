package com.legacy.blue_skies.client.audio.ambient;

import com.legacy.blue_skies.client.audio.ambient.util.SkiesSubSound;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnowstormAmbientSoundHandler implements AmbientSoundHandler {
   private LocalPlayer player;
   private final SoundManager soundHandler;
   private int delay = 0;

   public SnowstormAmbientSoundHandler(LocalPlayer playerIn, SoundManager soundHandlerIn) {
      this.player = playerIn;
      this.soundHandler = soundHandlerIn;
   }

   public void tick() {
      if (this.player != null) {
         if (this.player.level().getBrightness(LightLayer.SKY, this.player.blockPosition()) > 0) {
            this.delay--;
            if (this.delay <= 0 && this.player.level().isRaining()) {
               float f = this.player.level().random.nextFloat();
               if (f < 0.001F) {
                  this.delay = 0;
                  this.soundHandler.play(new SkiesSubSound(this.player, SkiesSounds.AMBIENT_SNOW_WIND_ADDITIONS) {
                     @Override
                     public void tick() {
                        if (!SnowstormAmbientSoundHandler.this.player.level().isRaining()) {
                           this.stop();
                        }
                     }
                  });
               }
            }
         }
      }
   }
}
