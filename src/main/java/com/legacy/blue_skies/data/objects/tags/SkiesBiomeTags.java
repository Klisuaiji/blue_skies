package com.legacy.blue_skies.data.objects.tags;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface SkiesBiomeTags {
   TagKey<Biome> EVERBRIGHT = tag("everbright");
   TagKey<Biome> EVERDAWN = tag("everdawn");
   TagKey<Biome> HAS_SANDSTORMS = tag("has_sandstorms");
   TagKey<Biome> DRIPSTONES_DRIP_LAVA = tag("dripstones_drip_lava");
   TagKey<Biome> SHADE_MONITOR_PURPLE = tag("shade_monitor/purple");
   TagKey<Biome> SHADE_MONITOR_WHITE = tag("shade_monitor/white");
   TagKey<Biome> SHADE_MONITOR_WHITE_AND_PURPLE = tag("shade_monitor/white_and_purple");
   TagKey<Biome> SHADE_MONITOR_RED = tag("shade_monitor/red");
   TagKey<Biome> SHADE_MONITOR_PINK = tag("shade_monitor/pink");
   TagKey<Biome> SHADE_MONITOR_MUDDY_PURPLE = tag("shade_monitor/muddy_purple");
   TagKey<Biome> SHADE_MONITOR_SNOWY_BLUE = tag("shade_monitor/snowy_blue");

   static void init() {
   }

   private static TagKey<Biome> tag(String key) {
      return TagKey.create(Registries.BIOME, BlueSkies.locate(key));
   }
}
