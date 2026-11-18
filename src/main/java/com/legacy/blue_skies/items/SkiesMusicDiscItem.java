package com.legacy.blue_skies.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkiesMusicDiscItem extends RecordItem {
   public final String artistName;
   public final String songName;

   public SkiesMusicDiscItem(SoundEvent soundIn, String artist, String song, int timeInSeconds, Properties builder) {
      super(7, () -> soundIn, builder, (timeInSeconds + 15) * 20);
      this.artistName = artist;
      this.songName = song;
   }

   public String getDescriptionId(ItemStack stack) {
      return "item.blue_skies.sky_music_disc";
   }

   @OnlyIn(Dist.CLIENT)
   public MutableComponent getDisplayName() {
      return Component.literal(this.artistName + " - " + this.songName);
   }
}
