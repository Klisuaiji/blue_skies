package com.legacy.blue_skies.blocks.natural;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.neoforged.neoforge.common.util.Lazy;

public class SkyGrassBlock extends GrassBlock {
   private final SkyGrassBlock.GrassProperties grassProp;

   public SkyGrassBlock(SkyGrassBlock.GrassProperties grassProp) {
      super(Properties.of().randomTicks().strength(0.6F).sound(SoundType.GRASS));
      this.grassProp = grassProp;
   }

   public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
      Block block = level.getBlockState(pos.above()).getBlock();
      return (BlockState)state.setValue(SNOWY, block == Blocks.SNOW || block == Blocks.SNOW_BLOCK);
   }

   private static boolean isLightAbove(BlockState state, LevelReader level, BlockPos pos) {
      BlockPos blockpos = pos.above();
      BlockState blockstate = level.getBlockState(blockpos);
      if (blockstate.getBlock() == Blocks.SNOW && (Integer)blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
         return true;
      }

      int i = LightEngine.getLightBlockInto(level, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(level, blockpos));
      return i < level.getMaxLightLevel();
   }

   private static boolean isCovered(BlockState state, LevelReader level, BlockPos pos) {
      return isLightAbove(state, level, pos) && !level.getFluidState(pos.above()).is(FluidTags.WATER);
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (!level.isClientSide) {
         if (!level.isAreaLoaded(pos, 3)) {
            return;
         }

         if (!isLightAbove(state, level, pos)) {
            level.setBlockAndUpdate(pos, this.grassProp.getDirtBlock());
         } else if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
            for (int i = 0; i < 4; i++) {
               BlockPos blockpos = pos.offset(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
               Block dirt = level.getBlockState(blockpos).getBlock();
               BlockState spreadResult = this.grassProp.getSpreadResult(dirt);
               if (spreadResult != null && isCovered(this.defaultBlockState(), level, blockpos)) {
                  level.setBlockAndUpdate(blockpos, (BlockState)spreadResult.setValue(SNOWY, level.getBlockState(blockpos.above()).getBlock() == Blocks.SNOW));
               }
            }
         }
      }
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      BlockPos abovePos = pos.above();
      BlockState grassState = this.grassProp.getTallGrass();

      label44:
      for (int i = 0; i < 128; i++) {
         BlockPos placePos = abovePos;

         for (int j = 0; j < i / 16; j++) {
            placePos = placePos.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
            if (!level.getBlockState(placePos.below()).is(this) || level.getBlockState(placePos).isCollisionShapeFullBlock(level, placePos)) {
               continue label44;
            }
         }

         BlockState blockAtPlace = level.getBlockState(placePos);
         if (blockAtPlace.is(grassState.getBlock()) && rand.nextInt(10) == 0) {
            ((BonemealableBlock)grassState.getBlock()).performBonemeal(level, rand, placePos, blockAtPlace);
         }

         if (blockAtPlace.isAir()) {
            if (rand.nextInt(8) == 0) {
               if (this.grassProp.flowerProvider == null) {
                  List<ConfiguredFeature<?, ?>> flowerFeatures = ((Biome)level.getBiome(placePos).value()).getGenerationSettings().getFlowerFeatures();
                  if (!flowerFeatures.isEmpty()) {
                     PlacedFeature placedFeature = (PlacedFeature)((RandomPatchConfiguration)flowerFeatures.get(rand.nextInt(flowerFeatures.size())).config())
                        .feature()
                        .value();
                     placedFeature.place(level, level.getChunkSource().getGenerator(), rand, placePos);
                  }
               } else {
                  level.setBlockAndUpdate(placePos, this.grassProp.flowerProvider.apply(rand).get().defaultBlockState());
               }
            } else {
               level.setBlockAndUpdate(placePos, grassState);
            }
         }
      }
   }

   public static class GrassProperties {
      private final Lazy<BlockState> tallGrass;
      private final Lazy<BlockState> dirtBlock;
      private final Lazy<Map<Block, BlockState>> spreadMap;
      @Nullable
      public final Function<RandomSource, Supplier<Block>> flowerProvider;

      public GrassProperties(
         Supplier<Block> grass,
         Supplier<Block> dirtBlock,
         Map<Supplier<Block>, Supplier<Block>> spreadMap,
         Function<RandomSource, Supplier<Block>> flowerProvider
      ) {
         this.tallGrass = Lazy.of(() -> grass.get().defaultBlockState());
         this.dirtBlock = Lazy.of(() -> dirtBlock.get().defaultBlockState());
         this.spreadMap = Lazy.of(
            () -> spreadMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().get(), e -> e.getValue().get().defaultBlockState()))
         );
         this.flowerProvider = flowerProvider;
      }

      public GrassProperties(Supplier<Block> grass, Supplier<Block> dirtBlock, Map<Supplier<Block>, Supplier<Block>> spreadMap) {
         this(grass, dirtBlock, spreadMap, null);
      }

      public BlockState getTallGrass() {
         return (BlockState)this.tallGrass.get();
      }

      public BlockState getDirtBlock() {
         return (BlockState)this.dirtBlock.get();
      }

      @Nullable
      public BlockState getSpreadResult(Block dirt) {
         return (BlockState)((Map)this.spreadMap.get()).get(dirt);
      }
   }
}
