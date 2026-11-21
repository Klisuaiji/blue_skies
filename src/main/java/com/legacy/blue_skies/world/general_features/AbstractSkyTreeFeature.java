package com.legacy.blue_skies.world.general_features;

import com.legacy.blue_skies.blocks.misc.SkyWebbingBlock;
import com.legacy.blue_skies.data.objects.tags.SkiesStructureTags;
import com.legacy.structure_gel.core.util.Internal;
import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageSetter;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluids;

public abstract class AbstractSkyTreeFeature extends TreeFeature implements IStructureRestricted {
   public static final TreeConfiguration DUMMY_CONFIG = new TreeConfigurationBuilder(
         BlockStateProvider.simple(Blocks.OAK_LOG.defaultBlockState()),
         new StraightTrunkPlacer(4, 2, 0),
         BlockStateProvider.simple(Blocks.OAK_LEAVES.defaultBlockState()),
         new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
         new TwoLayersFeatureSize(1, 0, 1)
      )
      .ignoreVines()
      .build();
   public static final TreeConfiguration DUMMY_HUGE_CONFIG = DUMMY_CONFIG;
   private static final Map<Direction, BooleanProperty> VINE_PROPERTIES = VineBlock.PROPERTY_BY_DIRECTION
      .entrySet()
      .stream()
      .filter(e -> e.getKey() != Direction.UP)
      .collect(Util.toMap());
   protected final boolean natural;

   public AbstractSkyTreeFeature(Codec<TreeConfiguration> config, boolean natural) {
      super(config);
      this.natural = natural;
   }

   @Internal
   public final boolean doPlace(
      WorldGenLevel level,
      RandomSource rand,
      BlockPos pos,
      BiConsumer<BlockPos, BlockState> changedLogs,
      BiConsumer<BlockPos, BlockState> changedLeaves,
      FoliageSetter whatIsThis,
      TreeConfiguration config
   ) {
      BlockPos groundPos = pos.below();
      return this.isValidGround(level, level.getBlockState(groundPos), groundPos) && this.shouldGenerate(level, pos)
         ? this.place(level, rand, pos, changedLogs, changedLeaves)
         : false;
   }

   public abstract boolean place(
      WorldGenLevel var1, RandomSource var2, BlockPos var3, BiConsumer<BlockPos, BlockState> var4, BiConsumer<BlockPos, BlockState> var5
   );

   @Override
   public boolean pieceSpecific() {
      return true;
   }

   @Override
   public TagKey<Structure> disallowedStructures() {
      return SkiesStructureTags.TREE_DISALLOWED;
   }

   public boolean isValidGround(WorldGenLevel level, BlockState state, BlockPos pos) {
      return this.natural ? isDirt(state) : isDirt(state) || state.getBlock() instanceof FarmBlock;
   }

   protected boolean setBlockIfOk(BiConsumer<BlockPos, BlockState> changedBlocks, WorldGenLevel level, BlockPos pos, BlockState state) {
      Block block = state.getBlock();
      if (block instanceof RotatedPillarBlock) {
         if (this.isReplaceableByLogs(level, pos)) {
            this.setBlock(changedBlocks, level, pos, state);
            return true;
         } else {
            return false;
         }
      } else if (block instanceof LeavesBlock) {
         if (this.isReplaceableByLeaves(level, pos)) {
            this.setBlock(changedBlocks, level, pos, state);
            return true;
         } else {
            return false;
         }
      } else if (block instanceof SkyWebbingBlock) {
         if (level.isEmptyBlock(pos)) {
            this.setBlock(changedBlocks, level, pos, state);
            return true;
         } else {
            return false;
         }
      } else if (level.isEmptyBlock(pos) && state.canSurvive(level, pos)) {
         this.setBlock(changedBlocks, level, pos, state);
         if (block instanceof SnowLayerBlock) {
            BlockState lower = level.getBlockState(pos.below());
            if (lower.hasProperty(BlockStateProperties.SNOWY)) {
               level.setBlock(pos.below(), (BlockState)lower.setValue(BlockStateProperties.SNOWY, true), 2);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected void setBlock(BiConsumer<BlockPos, BlockState> changedBlocks, WorldGenLevel level, BlockPos pos, BlockState state) {
      if (state.hasProperty(BlockStateProperties.WATERLOGGED) && level.getFluidState(pos).is(Fluids.WATER)) {
         state = (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, true);
      }

      TreeFeature.setBlockKnownShape(level, pos, state);
      if (changedBlocks != null) {
         changedBlocks.accept(pos, state);
      }
   }

   public boolean isReplaceableByLeaves(WorldGenLevel level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      return state.canBeReplaced() || state.is(BlockTags.REPLACEABLE_BY_TREES);
   }

   public boolean isReplaceableByLogs(WorldGenLevel level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      return state.canBeReplaced() || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(BlockTags.LOGS);
   }

   public static void placeVines(
      WorldGenLevel level,
      BlockPos trunkStart,
      RandomSource rand,
      int areaWidth,
      int areaHeight,
      int minVineLength,
      int maxVineLength,
      float placementChance,
      BlockState vine,
      Set<Block> allowedAttachmentBlocks
   ) {
      int halfWidth = Math.max((areaWidth - 1) / 2, 1);

      for (int x = -halfWidth; x <= halfWidth; x++) {
         for (int z = -halfWidth; z <= halfWidth; z++) {
            for (int y = 2; y <= areaHeight; y++) {
               BlockPos offsetPos = trunkStart.offset(x, y, z);
               if (level.isEmptyBlock(offsetPos) && rand.nextFloat() < placementChance) {
                  for (Entry<Direction, BooleanProperty> prop : VINE_PROPERTIES.entrySet()) {
                     BlockState vineBlock = (BlockState)vine.setValue((Property)prop.getValue(), true);
                     if (vineBlock.canSurvive(level, offsetPos)
                        && (
                           allowedAttachmentBlocks.isEmpty()
                              || allowedAttachmentBlocks.contains(level.getBlockState(offsetPos.relative(prop.getKey())).getBlock())
                        )) {
                        int maxLength = rand.nextInt(maxVineLength - minVineLength) + minVineLength;

                        for (int vineLength = 0;
                           vineLength <= maxLength && level.isEmptyBlock(offsetPos.below(vineLength)) && vineBlock.canSurvive(level, offsetPos);
                           vineLength++
                        ) {
                           level.setBlock(offsetPos.below(vineLength), vineBlock, 6);
                        }
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public boolean isAreaOk(WorldGenLevel level, BlockPos pos, int trunkHeight, int trunkWidth, int leafStartHeight, int leafWidth) {
      if (pos.getY() >= 1 && pos.getY() + trunkHeight + 1 < level.getHeight()) {
         if (trunkWidth == 1) {
            for (int y = 0; y <= 1 + trunkHeight && pos.getY() + y >= 1 && pos.getY() + y < level.getHeight(); y++) {
               if (!this.isReplaceableByLogs(level, pos.above(y))) {
                  return false;
               }
            }
         } else {
            for (int y = 0; y <= 1 + trunkHeight && pos.getY() + y >= 1 && pos.getY() + y < level.getHeight(); y++) {
               for (int x = 0; x < trunkWidth; x++) {
                  for (int z = 0; z < trunkWidth; z++) {
                     if (!this.isReplaceableByLogs(level, pos.offset(x, y, z))) {
                        return false;
                     }
                  }
               }
            }
         }

         int width = Math.max((leafWidth - 1) / 2, 1);

         for (int y = leafStartHeight; y <= trunkHeight + 2 && pos.getY() + y >= 0 && pos.getY() + y < level.getHeight(); y++) {
            for (int x = -width; x <= width; x++) {
               for (int z = -width; z <= width; z++) {
                  if (!this.isReplaceableByLeaves(level, pos.offset(x, y, z))) {
                     return false;
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
