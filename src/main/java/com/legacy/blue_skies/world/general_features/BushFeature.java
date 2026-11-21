package com.legacy.blue_skies.world.general_features;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;

public class BushFeature extends Feature<BushFeature.Config> {
   public BushFeature(Codec<BushFeature.Config> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<BushFeature.Config> context) {
      WorldGenLevel worldIn = context.level();
      BlockPos pos = context.origin();
      BushFeature.Config config = (BushFeature.Config)context.config();
      boolean insideDungeon = StructureAccessHelper.isInStructurePiece(worldIn, (Structure)SkiesStructures.NATURE_DUNGEON.getStructure().get(worldIn), pos);
      if ((!config.fromDungeon || insideDungeon) && (config.fromDungeon || !insideDungeon)) {
         if (config.fromDungeon && worldIn.isStateAtPosition(pos.below(), state -> state.getBlock() != SkiesBlocks.turquoise_grass_block)) {
            return false;
         }

         if (worldIn.isStateAtPosition(pos.below(), state -> state.is(BlockTags.DIRT))) {
            for (int x = -1; x < 2; x++) {
               for (int z = -1; z < 2; z++) {
                  for (int y = 0; y < 2; y++) {
                     if (y != 1 || Math.abs(x) != 1 || Math.abs(z) != 1) {
                        worldIn.setBlock(pos.offset(x, y, z), (BlockState)config.leaf.get().setValue(LeavesBlock.DISTANCE, 1), 3);
                     }
                  }
               }
            }

            worldIn.setBlock(pos, config.log.get(), 3);
         }

         return false;
      } else {
         return false;
      }
   }

   protected void setBlock(LevelSimulatedRW worldIn, BlockPos pos, BlockState state) {
      if (this.canReplace(worldIn, pos)) {
         worldIn.setBlock(pos, state, 2);
      }
   }

   private boolean canReplace(LevelSimulatedRW worldIn, BlockPos pos) {
      return worldIn.isStateAtPosition(
         pos, blockState -> blockState.is(BlockTags.LEAVES) || blockState.is(BlockTags.LOGS) || blockState.getBlock() == Blocks.SNOW
      );
   }

   public static class Config implements FeatureConfiguration {
      public static final Codec<BushFeature.Config> CODEC = RecordCodecBuilder.create(
         instance -> instance.group(
               BlockState.CODEC.fieldOf("log").forGetter(config -> config.log.get()),
               BlockState.CODEC.fieldOf("leaf").forGetter(config -> config.leaf.get()),
               Codec.BOOL.fieldOf("fromDungeon").forGetter(config -> config.fromDungeon)
            )
            .apply(instance, BushFeature.Config::new)
      );
      public final Supplier<BlockState> log;
      public final Supplier<BlockState> leaf;
      public final boolean fromDungeon;

      public Config(Supplier<BlockState> log, Supplier<BlockState> leaf, boolean fromDungeon) {
         this.log = log;
         this.leaf = leaf;
         this.fromDungeon = fromDungeon;
      }

      private Config(BlockState log, BlockState leaf, boolean fromDungeon) {
         this.log = () -> log;
         this.leaf = () -> leaf;
         this.fromDungeon = fromDungeon;
      }
   }
}
