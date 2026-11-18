package com.legacy.blue_skies.world.general_features.carver;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CanyonWorldCarver;
import net.minecraft.world.level.levelgen.carver.CarvingContext;

public class SkyCanyonWorldCarver extends CanyonWorldCarver {
   protected final Random setRand = new Random(1337L);
   private final boolean cold;

   public SkyCanyonWorldCarver(Codec<CanyonCarverConfiguration> config, boolean cold) {
      super(config);
      this.cold = cold;
   }

   public BlockState getCarveState(CarvingContext context, CanyonCarverConfiguration config, BlockPos pos, Aquifer aquifer) {
      int lavaLevel = config.lavaLevel.resolveY(context);
      if (pos.getY() > lavaLevel) {
         return AIR;
      } else {
         return this.cold
            ? (pos.getY() == lavaLevel && this.setRand.nextFloat() < 0.4F ? Blocks.PACKED_ICE.defaultBlockState() : Blocks.ICE.defaultBlockState())
            : LAVA.createLegacyBlock();
      }
   }
}
