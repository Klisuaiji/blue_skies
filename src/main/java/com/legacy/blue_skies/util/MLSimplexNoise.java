package com.legacy.blue_skies.util;

import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class MLSimplexNoise extends SimplexNoise {
   private final long seed;

   public MLSimplexNoise(long seed) {
      super(new WorldgenRandom(new LegacyRandomSource(seed)));
      this.seed = seed;
   }

   public long getSeed() {
      return this.seed;
   }
}
