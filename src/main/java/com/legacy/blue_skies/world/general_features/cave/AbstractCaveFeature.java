package com.legacy.blue_skies.world.general_features.cave;

import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.blue_skies.world.general_features.IStructureRestricted;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;

public abstract class AbstractCaveFeature<FC extends FeatureConfiguration> extends Feature<FC> implements IStructureRestricted {
   public AbstractCaveFeature(Codec<FC> codec) {
      super(codec);
   }

   public boolean place(FeaturePlaceContext<FC> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      ChunkGenerator chunkGen = context.chunkGenerator();
      FC config = (FC)context.config();
      int surfaceY = level.getChunk(pos).getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ());
      return this.isValidPos(level, chunkGen, level.getRandom(), pos, config)
            && pos.getY() + this.getMaxHeight(config) < surfaceY - 5
            && this.shouldGenerate(level, pos)
         ? this.generate(level, chunkGen, level.getRandom(), pos, config)
         : false;
   }

   public abstract boolean generate(WorldGenLevel var1, ChunkGenerator var2, RandomSource var3, BlockPos var4, FC var5);

   public abstract boolean isValidPos(WorldGenLevel var1, ChunkGenerator var2, RandomSource var3, BlockPos var4, FC var5);

   public int getMaxHeight(FC config) {
      return 2;
   }

   @Override
   public TagKey<Structure> disallowedStructures() {
      return SkiesStructureTags.ON_POISON_MAPS;
   }
}
