package com.legacy.blue_skies.world.general_features.cave;

import com.legacy.blue_skies.blocks.natural.SkyDripstoneBlock;
import com.legacy.blue_skies.blocks.util.IFluidLoggable;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.common.Tags.Blocks;

public class SkyDripstoneFeature extends AbstractCaveFeature<SkyDripstoneFeature.Config> {
   public SkyDripstoneFeature(Codec<SkyDripstoneFeature.Config> codec) {
      super(codec);
   }

   public boolean generate(WorldGenLevel level, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, SkyDripstoneFeature.Config config) {
      int spread = 15;
      int half = spread / 2;

      for (int i = 0; i < 30; i++) {
         for (SkyDripstoneBlock.Type type : SkyDripstoneBlock.Type.values()) {
            this.createDripstone(
               level, chunkGen, rand, pos.offset(rand.nextInt(spread) - spread, rand.nextInt(spread) - half, rand.nextInt(spread) - half), config, type
            );
         }
      }

      return true;
   }

   private void createDripstone(
      WorldGenLevel level, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, SkyDripstoneFeature.Config config, SkyDripstoneBlock.Type type
   ) {
      BlockState state = (BlockState)config.dripstone.setValue(SkyDripstoneBlock.TYPE, type);
      Direction growthDir = type.growthDir;
      BlockState placeOnto = level.getBlockState(pos.relative(growthDir.getOpposite()));
      if (this.canReplace(level.getBlockState(pos)) && placeOnto.is(Blocks.STONE)) {
         int height = rand.nextInt(config.maxLength);

         for (int y = 0; y <= height; y++) {
            if (!this.canReplace(level.getBlockState(pos.relative(growthDir, y)))) {
               height = y - 1;
               break;
            }
         }

         if (height < 0) {
            return;
         }

         for (int y = 0; y <= height; y++) {
            SkyDripstoneBlock.Section section = SkyDripstoneBlock.Section.MIDDLE;
            if (y == 0 && height > 1) {
               section = SkyDripstoneBlock.Section.BASE;
            } else if (y == height) {
               section = SkyDripstoneBlock.Section.TIP;
            }

            BlockPos placePos = pos.relative(growthDir, y);
            BlockState mergeToState = level.getBlockState(placePos.relative(growthDir));
            if (mergeToState.is(config.dripstone.getBlock())) {
               section = SkyDripstoneBlock.Section.TIP_MERGE;
            }

            this.setBlock(
               level,
               placePos,
               (BlockState)((BlockState)state.setValue(SkyDripstoneBlock.SECTION, section))
                  .setValue(IFluidLoggable.FLUIDLOGGED, IFluidLoggable.FluidLogging.from(level.getFluidState(placePos).getType()))
            );
            if (section == SkyDripstoneBlock.Section.TIP_MERGE) {
               BlockPos mergeToPos = placePos.relative(growthDir);
               this.setBlock(level, mergeToPos, (BlockState)mergeToState.setValue(SkyDripstoneBlock.SECTION, SkyDripstoneBlock.Section.TIP_MERGE));
            }
         }
      }
   }

   private boolean canReplace(BlockState state) {
      return state.isAir()
         || state.getBlock() == net.minecraft.world.level.block.Blocks.WATER
         || state.getBlock() == net.minecraft.world.level.block.Blocks.LAVA;
   }

   public int getMaxHeight(SkyDripstoneFeature.Config config) {
      return 10;
   }

   public boolean isValidPos(WorldGenLevel level, ChunkGenerator chunkGen, RandomSource rand, BlockPos pos, SkyDripstoneFeature.Config config) {
      return true;
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<SkyDripstoneFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               BlockState.CODEC.fieldOf("dripstone").forGetter(config -> config.dripstone),
               Codec.INT.fieldOf("maxLength").forGetter(config -> config.maxLength)
            )
            .apply(instance, SkyDripstoneFeature.Config::new)
      );
      public final BlockState dripstone;
      public final int maxLength;

      public Config(BlockState dripstone, int maxLength) {
         this.dripstone = dripstone.getBlock() instanceof SkyDripstoneBlock ? dripstone : SkiesBlocks.lunar_dripstone.defaultBlockState();
         this.maxLength = maxLength;
      }
   }
}
