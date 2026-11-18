package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.world.util.structure_processors.RemoveSnowStructureProcessor;
import com.legacy.blue_skies.world.util.structure_processors.VinesProcessor;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class SkiesStructureProcessors {
   @Deprecated
   public static final StructureProcessorType<RemoveGelStructureProcessor> REMOVE_FILLER = register("remove_filler", RemoveGelStructureProcessor.CODEC);
   public static final StructureProcessorType<RemoveSnowStructureProcessor> REMOVE_SNOW = register("remove_snow", RemoveSnowStructureProcessor.CODEC);
   public static final StructureProcessorType<VinesProcessor> VINES = register("vines", VinesProcessor.CODEC);

   public static void init() {
   }

   static <P extends StructureProcessor> StructureProcessorType<P> register(String key, Codec<P> codec) {
      return (StructureProcessorType<P>)Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, BlueSkies.locate(key), (StructureProcessorType)() -> codec);
   }
}
