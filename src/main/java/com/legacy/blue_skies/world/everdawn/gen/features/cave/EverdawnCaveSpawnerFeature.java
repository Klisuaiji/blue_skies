package com.legacy.blue_skies.world.everdawn.gen.features.cave;

import com.legacy.blue_skies.data.providers.SkiesLootProv;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.world.general_features.cave.AbstractCaveSpawnerFeature;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EverdawnCaveSpawnerFeature extends AbstractCaveSpawnerFeature {
   public EverdawnCaveSpawnerFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec, new EverdawnCaveSpawnerFeature.Simple(), new EverdawnCaveSpawnerFeature.SpiderNest(), new EverdawnCaveSpawnerFeature.Moonstone());
   }

   private static class Moonstone extends AbstractCaveSpawnerFeature.SpawnerTemplateData {
      public Moonstone() {
         super(() -> SkiesEntityTypes.INFESTED_SWARMER, "everdawn_moonstone");
      }

      @Nullable
      @Override
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         return originalState.getBlock() == SkiesBlocks.lunar_stone && rand.nextFloat() < 0.1F
            ? SkiesBlocks.lunar_cobblestone.defaultBlockState()
            : originalState;
      }

      @Override
      protected ResourceLocation getLoot() {
         return SkiesLootProv.EVERDAWN_CAVE_SPAWNER;
      }
   }

   private static class Simple extends AbstractCaveSpawnerFeature.SpawnerTemplateData {
      public Simple() {
         super(() -> SkiesEntityTypes.INFESTED_SWARMER, "everdawn_simple");
      }

      @Nullable
      @Override
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         if (originalState.getBlock() == SkiesBlocks.lunar_stonebrick && rand.nextFloat() < 0.64F) {
            return SkiesBlocks.cracked_lunar_stonebrick.defaultBlockState();
         } else {
            return originalState.getBlock() == SkiesBlocks.lunar_stonebrick_stairs && rand.nextFloat() < 0.5F
               ? (BlockState)((BlockState)((BlockState)((BlockState)SkiesBlocks.cracked_lunar_stonebrick_stairs
                           .defaultBlockState()
                           .setValue(StairBlock.FACING, (Direction)originalState.getValue(StairBlock.FACING)))
                        .setValue(StairBlock.HALF, (Half)originalState.getValue(StairBlock.HALF)))
                     .setValue(StairBlock.SHAPE, (StairsShape)originalState.getValue(StairBlock.SHAPE)))
                  .setValue(StairBlock.WATERLOGGED, (Boolean)originalState.getValue(StairBlock.WATERLOGGED))
               : originalState;
         }
      }

      @Override
      protected ResourceLocation getLoot() {
         return SkiesLootProv.EVERDAWN_CAVE_SPAWNER;
      }
   }

   private static class SpiderNest extends AbstractCaveSpawnerFeature.SpawnerTemplateData {
      public SpiderNest() {
         super(() -> SkiesEntityTypes.VENOM_SPIDER, "everdawn_spider_nest");
      }

      @Nullable
      @Override
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         if (originalState.getBlock() == SkiesBlocks.lunar_mud && rand.nextFloat() < 0.35F) {
            return SkiesBlocks.coarse_lunar_dirt.defaultBlockState();
         } else {
            return originalState.getBlock() == SkiesBlocks.spider_webbing && rand.nextFloat() < 0.4F ? Blocks.AIR.defaultBlockState() : originalState;
         }
      }

      @Override
      protected ResourceLocation getLoot() {
         return SkiesLootProv.EVERDAWN_CAVE_SPAWNER;
      }
   }
}
