package com.legacy.blue_skies.world.general_features;

import com.legacy.blue_skies.blocks.natural.FruitLeavesBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.util.DirectionUtil;
import com.legacy.structure_gel.api.structure.StructureAccessHelper;
import com.mojang.serialization.Codec;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.util.Lazy;

public class CometTreeFeature extends AbstractSkyTreeFeature {
   private static final Lazy<BlockState> LOG = Lazy.of(() -> SkiesBlocks.comet_log.defaultBlockState());
   private static final Lazy<BlockState> LOG_X = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.X));
   private static final Lazy<BlockState> LOG_Z = Lazy.of(() -> (BlockState)((BlockState)LOG.get()).setValue(RotatedPillarBlock.AXIS, Axis.Z));
   private static final Lazy<BlockState> LEAF_CANT_GROW = Lazy.of(() -> SkiesBlocks.comet_leaves.defaultBlockState());
   private static final Lazy<BlockState> LEAF_CAN_GROW = Lazy.of(
      () -> (BlockState)SkiesBlocks.comet_leaves.defaultBlockState().setValue(FruitLeavesBlock.GROWTH, FruitLeavesBlock.Growth.CAN_GROW)
   );
   private static final Lazy<BlockState> LEAF_GROWN = Lazy.of(
      () -> (BlockState)SkiesBlocks.comet_leaves.defaultBlockState().setValue(FruitLeavesBlock.GROWTH, FruitLeavesBlock.Growth.GROWN)
   );
   private final boolean fromDungeon;

   public CometTreeFeature(Codec<TreeConfiguration> config, boolean fromDungeonIn) {
      super(config, true);
      this.fromDungeon = fromDungeonIn;
   }

   @Override
   public boolean place(
      WorldGenLevel world, RandomSource rand, BlockPos pos, BiConsumer<BlockPos, BlockState> changedLogs, BiConsumer<BlockPos, BlockState> changedLeaves
   ) {
      if (!this.fromDungeon
         || StructureAccessHelper.isInStructurePiece(world, (Structure)SkiesStructures.NATURE_DUNGEON.getStructure().get(world), pos)
            && world.getBlockState(pos.below()).getBlock() == SkiesBlocks.turquoise_comet_grass_block) {
         int height = rand.nextInt(3) + 10;
         if (!this.isAreaOk(world, pos, height, 1, height - 4, 7)) {
            return false;
         } else {
            for (int y = 0; y < height; y++) {
               this.setBlockIfOk(changedLogs, world, pos.above(y), (BlockState)LOG.get());
            }

            this.setLeafBall(changedLeaves, world, pos.offset(0, height - 2, 0), rand);
            this.setBranches(changedLogs, changedLeaves, world, rand, pos, height);
            this.setSideLogs(changedLogs, world, pos);
            return true;
         }
      } else {
         return false;
      }
   }

   protected void setBranches(
      BiConsumer<BlockPos, BlockState> changedLogs,
      BiConsumer<BlockPos, BlockState> changedLeaves,
      WorldGenLevel world,
      RandomSource rand,
      BlockPos pos,
      int height
   ) {
      int iPosX = height - 4 - rand.nextInt(3);
      int iNegX = height - 4 - rand.nextInt(3);
      int iPosZ = height - 4 - rand.nextInt(3);
      int iNegZ = height - 4 - rand.nextInt(3);

      for (int i = 1; i <= 3; i++) {
         if (i == 3) {
            this.setLeafBall(changedLeaves, world, pos.offset(i, iPosX + 2, rand.nextInt(3) - 1), rand);
            this.setLeafBall(changedLeaves, world, pos.offset(rand.nextInt(3) - 1, iPosZ + 2, i), rand);
         }

         this.setBlockIfOk(changedLogs, world, pos.offset(i, iPosX + i, 0), (BlockState)LOG_X.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, iPosZ + i, i), (BlockState)LOG_Z.get());
      }

      for (int i = -1; i >= -3; i--) {
         if (i == -3) {
            this.setLeafBall(changedLeaves, world, pos.offset(i, iNegX + 2, rand.nextInt(3) - 1), rand);
            this.setLeafBall(changedLeaves, world, pos.offset(rand.nextInt(3) - 1, iNegZ + 2, i), rand);
         }

         this.setBlockIfOk(changedLogs, world, pos.offset(i, iNegX - i, 0), (BlockState)LOG_X.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, iNegZ - i, i), (BlockState)LOG_Z.get());
      }
   }

   protected void setLeafBall(BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, BlockPos pos, RandomSource rand) {
      for (int x = -2; x <= 2; x++) {
         for (int z = -2; z <= 2; z++) {
            for (int y = 0; y <= 2; y++) {
               if (Math.abs(x) != 2 || Math.abs(z) != 2) {
                  this.setBlockIfOk(changedLeaves, world, pos.offset(x, y, z), this.getLeaf(rand));
               }
            }
         }
      }

      this.setLeafCap(changedLeaves, world, pos.below(), rand);
      this.setLeafCap(changedLeaves, world, pos.above(3), rand);
   }

   protected void setLeafCap(BiConsumer<BlockPos, BlockState> changedLeaves, WorldGenLevel world, BlockPos pos, RandomSource rand) {
      this.setBlockIfOk(changedLeaves, world, pos, this.getLeaf(rand));

      for (Direction dir : DirectionUtil.HORIZONTAL) {
         this.setBlockIfOk(changedLeaves, world, pos.relative(dir), this.getLeaf(rand));
      }
   }

   protected void setSideLogs(BiConsumer<BlockPos, BlockState> changedLogs, WorldGenLevel world, BlockPos pos) {
      for (int i = 0; i >= -1; i--) {
         this.setBlockIfOk(changedLogs, world, pos.offset(1, i, 0), (BlockState)LOG.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(-1, i, 0), (BlockState)LOG.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, i, 1), (BlockState)LOG.get());
         this.setBlockIfOk(changedLogs, world, pos.offset(0, i, -1), (BlockState)LOG.get());
      }
   }

   protected BlockState getLeaf(RandomSource rand) {
      if (rand.nextFloat() < 0.3) {
         return rand.nextBoolean() ? (BlockState)LEAF_CAN_GROW.get() : (BlockState)LEAF_GROWN.get();
      } else {
         return (BlockState)LEAF_CANT_GROW.get();
      }
   }

   @Override
   public TagKey<Structure> disallowedStructures() {
      return this.fromDungeon ? null : super.disallowedStructures();
   }
}
