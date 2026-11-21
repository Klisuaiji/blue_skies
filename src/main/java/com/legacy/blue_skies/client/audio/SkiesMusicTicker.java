package com.legacy.blue_skies.client.audio;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.audio.ambient.FadingMusicSound;
import com.legacy.blue_skies.client.audio.ambient.TickableMusicSound;
import com.legacy.blue_skies.entities.util.SkiesDungeonType;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

@OnlyIn(Dist.CLIENT)
public class SkiesMusicTicker implements Tickable {
   private final Minecraft mc;
   public SoundInstance ambientMusic;
   public SoundInstance bossMusic;
   public SoundInstance dungeonMusic;
   public SoundInstance playingRecord;
   private int timeUntilNextMusic = 100;

   public SkiesMusicTicker(Minecraft mcIn) {
      this.mc = mcIn;
   }

   public void tick() {
      if (this.mc.player != null && !this.mc.getSoundManager().isActive(this.playingRecord)) {
         ResourceKey<Level> playerDim = this.mc.player.level().dimension();
         boolean properDimension = playerDim == SkiesDimensions.everbrightKey() || playerDim == SkiesDimensions.everdawnKey();
         boolean inWater = this.mc.player.isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value());
         SkiesPlayer.ifPresent(
            this.mc.player,
            skyPlayer -> {
               boolean bossActive = skyPlayer.getBossType() != null && this.mc.gui.getBossOverlay().shouldPlayMusic();
               boolean insideDungeon = skyPlayer.getDungeonMusicId() != null && skyPlayer.getDungeonMusicId() != SkiesDungeonType.NONE;
               if (this.dungeonMusic != null && (this.dungeonMusic.getSound() == null || this.dungeonMusic.getVolume() <= 0.0F)) {
                  this.dungeonMusic = null;
                  if (this.ambientMusic != null) {
                     this.timeUntilNextMusic = 1200;
                  }
               }

               if (this.ambientMusic != null && (this.ambientMusic.getSound() == null || this.ambientMusic.getVolume() <= 0.0F)) {
                  this.ambientMusic = null;
               }

               if (this.bossMusic != null && (this.bossMusic.getSound() == null || this.bossMusic.getVolume() <= 0.0F)) {
                  this.bossMusic = null;
               }

               if (bossActive && properDimension) {
                  SkiesMusicTicker.TrackType bosstrack = this.getBossTrack(skyPlayer.getBossType());
                  if (this.ambientMusic != null || this.dungeonMusic != null) {
                     this.stopMusic();
                     this.stopDungeonMusic();
                  }

                  if (this.bossMusic != null && !this.mc.getSoundManager().isActive(this.bossMusic)) {
                     this.bossMusic = null;
                     this.timeUntilNextMusic = Math.min(
                        Mth.nextInt(this.mc.player.getRandom(), bosstrack.getMinDelay(), bosstrack.getMaxDelay()), this.timeUntilNextMusic
                     );
                  }

                  this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, bosstrack.getMaxDelay());
                  if (this.bossMusic == null && this.timeUntilNextMusic-- <= 0) {
                     this.playBossMusic(bosstrack);
                  }
               } else if (!bossActive && properDimension && this.mc.getSoundManager().isActive(this.bossMusic)) {
                  this.playBossFanfare(skyPlayer.getBossType());
               } else if (insideDungeon && properDimension) {
                  SkiesMusicTicker.TrackType tracktype = this.getDungeonTrack(skyPlayer.getDungeonMusicId());
                  if (this.ambientMusic != null) {
                     this.stopMusic();
                  }

                  if (this.dungeonMusic != null && !this.mc.getSoundManager().isActive(this.dungeonMusic)) {
                     this.dungeonMusic = null;
                     this.timeUntilNextMusic = Math.min(Mth.nextInt(this.mc.player.getRandom(), tracktype.getMinDelay(), 200), this.timeUntilNextMusic);
                  }

                  if (this.timeUntilNextMusic <= 0 || this.timeUntilNextMusic > 200 && this.ambientMusic == null) {
                     this.timeUntilNextMusic = 0;
                  }

                  if (this.dungeonMusic == null && this.timeUntilNextMusic-- <= 0) {
                     this.playDungeonMusic(tracktype);
                  }
               } else if (properDimension) {
                  SkiesMusicTicker.TrackType tracktype = this.getRandomAmbientTrack(playerDim, inWater);
                  if (this.bossMusic != null || this.dungeonMusic != null) {
                     this.stopBossMusic();
                     this.stopDungeonMusic();
                  }

                  if (this.ambientMusic != null && !this.mc.getSoundManager().isActive(this.ambientMusic)) {
                     this.ambientMusic = null;
                     this.timeUntilNextMusic = Math.min(
                        Mth.nextInt(this.mc.player.getRandom(), tracktype.getMinDelay(), tracktype.getMaxDelay()), this.timeUntilNextMusic
                     );
                  }

                  this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, tracktype.getMaxDelay());
                  if (this.ambientMusic != null && (this.ambientMusic.getSound() == null || this.ambientMusic.getVolume() < 1.0F)
                     || this.ambientMusic == null && this.timeUntilNextMusic-- <= 0) {
                     this.playMusic(tracktype);
                  }
               }
            }
         );
      } else {
         this.stopMusic();
         this.stopDungeonMusic();
         this.stopBossMusic();
      }
   }

   public boolean playingMusic() {
      return this.ambientMusic != null;
   }

   public boolean playingRecord() {
      return this.playingRecord != null;
   }

   public SkiesMusicTicker.TrackType getRandomAmbientTrack(ResourceKey<Level> dimType, boolean inWater) {
      if (dimType == SkiesDimensions.everbrightKey()) {
         if (inWater) {
            return SkiesMusicTicker.TrackType.WHISTLESHELL;
         }

         int num = this.mc.player.getRandom().nextInt(5);
         switch (num) {
            case 0:
               return SkiesMusicTicker.TrackType.BRISEGEL;
            case 1:
               return SkiesMusicTicker.TrackType.TURQUOISE;
            case 2:
               return SkiesMusicTicker.TrackType.BRIGHTLANDS;
            case 3:
               return SkiesMusicTicker.TrackType.BRITTLEBUSH;
            default:
               return SkiesMusicTicker.TrackType.SNOWCAP;
         }
      } else {
         int num = this.mc.player.getRandom().nextInt(5);
         switch (num) {
            case 0:
               return SkiesMusicTicker.TrackType.MOONLIT_BLOOM;
            case 1:
               return SkiesMusicTicker.TrackType.BANEFUL;
            case 2:
               return SkiesMusicTicker.TrackType.CRYSTAL_DUNES;
            case 3:
               return SkiesMusicTicker.TrackType.GATEKEEPERS_TALE;
            default:
               return SkiesMusicTicker.TrackType.MARS;
         }
      }
   }

   public SkiesMusicTicker.TrackType getBossTrack(SkiesDungeonType bossType) {
      if (bossType == null) {
         return SkiesMusicTicker.TrackType.GENERIC_BOSS;
      }

      switch (bossType) {
         case BLINDING:
            return SkiesMusicTicker.TrackType.BLINDING_BOSS;
         case NATURE:
            return SkiesMusicTicker.TrackType.NATURE_BOSS;
         case POISON:
            return SkiesMusicTicker.TrackType.POISON_BOSS;
         default:
            return SkiesMusicTicker.TrackType.GENERIC_BOSS;
      }
   }

   public SkiesMusicTicker.TrackType getDungeonTrack(SkiesDungeonType dungeonType) {
      switch (dungeonType) {
         case BLINDING:
            return SkiesMusicTicker.TrackType.BLINDING_AMBIENCE;
         case NATURE:
            return SkiesMusicTicker.TrackType.NATURE_AMBIENCE;
         case POISON:
            return SkiesMusicTicker.TrackType.POISON_AMBIENCE;
         default:
            return SkiesMusicTicker.TrackType.BLINDING_AMBIENCE;
      }
   }

   public void playMusic(SkiesMusicTicker.TrackType requestedMusicType) {
      this.ambientMusic = new FadingMusicSound(requestedMusicType.getMusicLocation(), SoundSource.MUSIC, false);
      ((FadingMusicSound)this.ambientMusic).setVolume(1.0F);
      ((FadingMusicSound)this.ambientMusic).ticksPlayed = 40;
      this.mc.getSoundManager().play(this.ambientMusic);
      this.timeUntilNextMusic = Integer.MAX_VALUE;
   }

   public void playDungeonMusic(SkiesMusicTicker.TrackType requestedMusicType) {
      this.dungeonMusic = new FadingMusicSound(requestedMusicType.getMusicLocation(), SoundSource.MUSIC, true);
      this.mc.getSoundManager().play(this.dungeonMusic);
      this.timeUntilNextMusic = Integer.MAX_VALUE;
   }

   public void playBossMusic(SkiesMusicTicker.TrackType requestedMusicType) {
      this.bossMusic = new TickableMusicSound(requestedMusicType.getMusicLocation(), SoundSource.MUSIC, true);
      this.mc.getSoundManager().play(this.bossMusic);
      this.timeUntilNextMusic = Integer.MAX_VALUE;
   }

   public void playBossFanfare(SkiesDungeonType bossType) {
      SoundEvent sound = (SoundEvent)SoundEvents.AMBIENT_CAVE;
      if (bossType != null) {
         switch (bossType) {
            case BLINDING:
               sound = SkiesSounds.MUSIC_BLINDING_DEFEAT;
               break;
            case NATURE:
               sound = SkiesSounds.MUSIC_NATURE_DEFEAT;
               break;
            case POISON:
               sound = SkiesSounds.MUSIC_POISON_DEFEAT;
               break;
            default:
               sound = SkiesSounds.MUSIC_GENERIC_DEFEAT;
         }
      }

      this.stopBossMusic();
      this.timeUntilNextMusic = 200;
      this.mc.getSoundManager().play(SimpleSoundInstance.forMusic(sound));
   }

   public void stopBossMusic() {
      if (this.bossMusic != null) {
         this.mc.getSoundManager().stop(this.bossMusic);
         this.bossMusic = null;
         this.timeUntilNextMusic = 0;
      }
   }

   public void stopMusic() {
      if (this.ambientMusic != null) {
         ((FadingMusicSound)this.ambientMusic).shouldNotFade = false;
         this.timeUntilNextMusic = 0;
      }
   }

   public void stopDungeonMusic() {
      if (this.dungeonMusic != null) {
         ((FadingMusicSound)this.dungeonMusic).shouldNotFade = false;
         this.timeUntilNextMusic = 0;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public enum TrackType {
      MARS(SkiesSounds.MUSIC_MARS, 1200, 1500),
      MOONLIT_BLOOM(SkiesSounds.MUSIC_MOONLIT_BLOOM, 1200, 1500),
      BRISEGEL(SkiesSounds.MUSIC_BRISEGEL, 1200, 1500),
      TURQUOISE(SkiesSounds.MUSIC_TURQUOISE, 1200, 1500),
      SNOWCAP(SkiesSounds.MUSIC_SNOWCAP, 1200, 1500),
      BANEFUL(SkiesSounds.MUSIC_BANEFUL, 1200, 1500),
      BRIGHTLANDS(SkiesSounds.MUSIC_BRIGHTLANDS, 1200, 1500),
      CRYSTAL_DUNES(SkiesSounds.MUSIC_CRYSTAL_DUNES, 1200, 1500),
      GATEKEEPERS_TALE(SkiesSounds.MUSIC_GATEKEEPERS_TALE, 1200, 1500),
      WHISTLESHELL(SkiesSounds.MUSIC_WHISTLESHELL, 1200, 1500),
      BRITTLEBUSH(SkiesSounds.MUSIC_BRITTLEBUSH, 1200, 1500),
      BLINDING_AMBIENCE(SkiesSounds.MUSIC_BLINDING_AMBIENCE, 0, 200),
      NATURE_AMBIENCE(SkiesSounds.MUSIC_NATURE_AMBIENCE, 0, 200),
      POISON_AMBIENCE(SkiesSounds.MUSIC_POISON_AMBIENCE, 0, 200),
      BLINDING_BOSS(SkiesSounds.MUSIC_BLINDING_BOSS, 0, 0),
      NATURE_BOSS(SkiesSounds.MUSIC_NATURE_BOSS, 0, 0),
      POISON_BOSS(SkiesSounds.MUSIC_POISON_BOSS, 0, 0),
      GENERIC_BOSS(SkiesSounds.MUSIC_GENERIC_BOSS, 0, 0),
      LOOP_TEST(SkiesSounds.MUSIC_GENERIC_BOSS, 1200, 1500);

      private final SoundEvent musicLocation;
      private final int minDelay;
      private final int maxDelay;

      TrackType(SoundEvent musicLocationIn, int minDelayIn, int maxDelayIn) {
         this.musicLocation = musicLocationIn;
         this.minDelay = minDelayIn;
         this.maxDelay = maxDelayIn;
      }

      public SoundEvent getMusicLocation() {
         return this.musicLocation;
      }

      public int getMinDelay() {
         return this.minDelay;
      }

      public int getMaxDelay() {
         return this.maxDelay;
      }
   }
}
