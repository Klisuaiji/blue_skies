package com.legacy.blue_skies.world.everbright.gen.features.cave;

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
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EverbrightCaveSpawnerFeature extends AbstractCaveSpawnerFeature {
   public EverbrightCaveSpawnerFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec, new EverbrightCaveSpawnerFeature.Simple(), new EverbrightCaveSpawnerFeature.Room(), new EverbrightCaveSpawnerFeature.Mineshaft());
   }

   private static class Mineshaft extends AbstractCaveSpawnerFeature.SpawnerTemplateData {
      public Mineshaft() {
         super(() -> SkiesEntityTypes.SHRUMPTY, "everbright_mineshaft");
      }

      @Nullable
      @Override
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         return originalState.getBlock() == SkiesBlocks.turquoise_stone && rand.nextFloat() < 0.25F
            ? SkiesBlocks.turquoise_cobblestone.defaultBlockState()
            : originalState;
      }

      @Override
      protected ResourceLocation getLoot() {
         return SkiesLootProv.EVERBRIGHT_CAVE_SPAWNER;
      }
   }

   private static class Room extends AbstractCaveSpawnerFeature.SpawnerTemplateData {
      public Room() {
         super(() -> SkiesEntityTypes.ARMORED_FROST_SPIRIT, "everbright_room");
      }

      @Nullable
      @Override
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         if (originalState.getBlock() == SkiesBlocks.turquoise_stonebrick) {
            if (rand.nextFloat() < 0.6F) {
               return rand.nextFloat() < 0.85F
                  ? SkiesBlocks.cracked_turquoise_stonebrick.defaultBlockState()
                  : SkiesBlocks.chiseled_turquoise_stonebrick.defaultBlockState();
            } else {
               return originalState;
            }
         } else if (originalState.getBlock() == SkiesBlocks.turquoise_stonebrick_slab && rand.nextFloat() < 0.5F) {
            return (BlockState)((BlockState)SkiesBlocks.cracked_turquoise_stonebrick_slab
                  .defaultBlockState()
                  .setValue(SlabBlock.TYPE, (SlabType)originalState.getValue(SlabBlock.TYPE)))
               .setValue(SlabBlock.WATERLOGGED, (Boolean)originalState.getValue(SlabBlock.WATERLOGGED));
         } else if (originalState.getBlock() == SkiesBlocks.turquoise_stonebrick_stairs && rand.nextFloat() < 0.5F) {
            return (BlockState)((BlockState)((BlockState)((BlockState)SkiesBlocks.cracked_turquoise_stonebrick_stairs
                        .defaultBlockState()
                        .setValue(StairBlock.FACING, (Direction)originalState.getValue(StairBlock.FACING)))
                     .setValue(StairBlock.HALF, (Half)originalState.getValue(StairBlock.HALF)))
                  .setValue(StairBlock.SHAPE, (StairsShape)originalState.getValue(StairBlock.SHAPE)))
               .setValue(StairBlock.WATERLOGGED, (Boolean)originalState.getValue(StairBlock.WATERLOGGED));
         } else {
            return originalState.getBlock() == SkiesBlocks.frose && rand.nextFloat() < 0.75 ? Blocks.AIR.defaultBlockState() : originalState;
         }
      }

      @Override
      protected ResourceLocation getLoot() {
         return SkiesLootProv.EVERBRIGHT_CAVE_SPAWNER;
      }
   }

   private static class Simple extends AbstractCaveSpawnerFeature.SpawnerTemplateData {
      public Simple() {
         super(() -> SkiesEntityTypes.ARMORED_FROST_SPIRIT, "everbright_simple");
      }

      @Nullable
      @Override
      public BlockState modifyState(ServerLevelAccessor world, RandomSource rand, BlockPos pos, BlockState originalState) {
         return originalState.getBlock() == SkiesBlocks.turquoise_stone && rand.nextFloat() < 0.64F
            ? SkiesBlocks.cracked_turquoise_stonebrick.defaultBlockState()
            : originalState;
      }

      @Override
      protected ResourceLocation getLoot() {
         return SkiesLootProv.EVERBRIGHT_CAVE_SPAWNER;
      }
   }
}
