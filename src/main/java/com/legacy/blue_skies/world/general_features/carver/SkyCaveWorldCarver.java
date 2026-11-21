package com.legacy.blue_skies.world.general_features.carver;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;

public class SkyCaveWorldCarver extends CaveWorldCarver {
   protected final Random setRand = new Random(1337L);
   private final boolean cold;

   public SkyCaveWorldCarver(Codec<CaveCarverConfiguration> configIn, boolean cold) {
      super(configIn);
      this.cold = cold;
   }

   public BlockState getCarveState(CarvingContext context, CaveCarverConfiguration config, BlockPos pos, Aquifer aquifer) {
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
