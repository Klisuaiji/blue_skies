package com.legacy.blue_skies.client.audio.ambient;

import com.legacy.blue_skies.client.audio.ambient.util.SkiesSubSound;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DungeonAmbientSoundHandler implements AmbientSoundHandler {
   private Minecraft mc = Minecraft.getInstance();
   private LocalPlayer player;
   private final SoundManager soundHandler;
   private int delay = 0;
   public SkiesDungeonType dungeonType = SkiesDungeonType.NONE;

   public DungeonAmbientSoundHandler(LocalPlayer playerIn, SoundManager soundHandlerIn) {
      this.player = playerIn;
      this.soundHandler = soundHandlerIn;
   }

   public void tick() {
      if (this.player == null) {
         this.player = this.mc.player;
      } else if (this.player.isAlive() && this.player != null && this.dungeonType != null && !this.mc.isPaused()) {
         this.delay--;
         if (this.delay <= 0) {
            if (this.dungeonType == SkiesDungeonType.NATURE) {
               float f = this.player.level().random.nextFloat();
               if (f < 0.001F && this.mc.level.getBrightness(LightLayer.SKY, this.mc.player.blockPosition()) < 10) {
                  this.delay = 0;
                  float pitch = (this.player.level().random.nextFloat() - this.player.level().random.nextFloat()) * 0.2F + 1.0F;
                  this.soundHandler.play(new SkiesSubSound(this.player, SkiesSounds.AMBIENT_NATURE_DUNGEON_ADDITIONS, pitch) {
                     @Override
                     public void tick() {
                        super.tick();
                        if (DungeonAmbientSoundHandler.this.dungeonType != SkiesDungeonType.NATURE) {
                           this.stop();
                        }
                     }
                  });
               }
            }

            if (this.dungeonType == SkiesDungeonType.POISON) {
               float f = this.player.level().random.nextFloat();
               if (f < 0.001F) {
                  this.delay = 0;
                  this.soundHandler.play(new SkiesSubSound(this.player, SkiesSounds.AMBIENT_POISON_DUNGEON_ADDITIONS) {
                     @Override
                     public void tick() {
                        super.tick();
                        if (DungeonAmbientSoundHandler.this.dungeonType != SkiesDungeonType.POISON) {
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
