package com.legacy.blue_skies.world.util.structure_processors;

import com.legacy.blue_skies.blocks.natural.SkyVineBlock;
import com.legacy.blue_skies.registries.SkiesStructureProcessors;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class VinesProcessor extends StructureProcessor {
   public static final VinesProcessor INSTANCE = new VinesProcessor();
   public static final Codec<VinesProcessor> CODEC = Codec.unit(() -> INSTANCE);

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
      return placed.state().hasProperty(SkyVineBlock.CAN_GROW)
         ? new StructureBlockInfo(placed.pos(), (BlockState)placed.state().setValue(SkyVineBlock.CAN_GROW, false), null)
         : placed;
   }

   protected StructureProcessorType<VinesProcessor> getType() {
      return SkiesStructureProcessors.VINES;
   }
}
