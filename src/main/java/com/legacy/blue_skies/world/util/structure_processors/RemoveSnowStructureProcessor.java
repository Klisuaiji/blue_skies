package com.legacy.blue_skies.world.util.structure_processors;

import com.legacy.blue_skies.registries.SkiesStructureProcessors;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class RemoveSnowStructureProcessor extends StructureProcessor {
   public static final RemoveSnowStructureProcessor INSTANCE = new RemoveSnowStructureProcessor();
   public static final Codec<RemoveSnowStructureProcessor> CODEC = Codec.unit(() -> INSTANCE);

   @Nullable
   public StructureBlockInfo process(
      LevelReader world,
      BlockPos pos,
      BlockPos pos2,
      StructureBlockInfo existing,
      StructureBlockInfo placed,
      StructurePlaceSettings settings,
      StructureTemplate template
   ) {
      if (((Biome)world.getBiome(pos).value()).getPrecipitationAt(pos) != Precipitation.SNOW) {
         if (placed.state() == Blocks.GRASS_BLOCK.defaultBlockState().setValue(GrassBlock.SNOWY, true) || placed.state().getBlock() == Blocks.DIRT) {
            return new StructureBlockInfo(placed.pos(), Blocks.GRASS_BLOCK.defaultBlockState(), null);
         }

         if (placed.state().getBlock() == Blocks.SNOW) {
            return new StructureBlockInfo(placed.pos(), Blocks.AIR.defaultBlockState(), null);
         }
      }

      return placed;
   }

   protected StructureProcessorType<RemoveSnowStructureProcessor> getType() {
      return SkiesStructureProcessors.REMOVE_SNOW;
   }
}
