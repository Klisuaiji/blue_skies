package com.legacy.blue_skies.world.general_features;

import com.google.common.collect.Sets;
import com.legacy.structure_gel.core.util.Internal;
import com.mojang.serialization.Codec;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public abstract class BaseLargeMushroomFeature extends AbstractSkyTreeFeature {
   public BaseLargeMushroomFeature(Codec<TreeConfiguration> config) {
      super(config, true);
   }

   public abstract boolean place(WorldGenLevel var1, RandomSource var2, BlockPos var3, BiConsumer<BlockPos, BlockState> var4);

   @Internal
   @Override
   public final boolean place(
      WorldGenLevel world,
      RandomSource rand,
      BlockPos pos,
      BiConsumer<BlockPos, BlockState> changedMushrooms,
      @Deprecated BiConsumer<BlockPos, BlockState> dontUseThis
   ) {
      Set<BlockPos> shrooms = Sets.newHashSet();
      if (!this.place(world, rand, pos, (blockPosx, blockState) -> {
         changedMushrooms.accept(blockPosx, blockState);
         shrooms.add(blockPosx);
      })) {
         return false;
      }

      for (BlockPos blockPos : shrooms) {
         for (Direction dir : Direction.values()) {
            TreeFeature.setBlockKnownShape(
               world,
               blockPos,
               world.getBlockState(blockPos).updateShape(dir, world.getBlockState(blockPos.relative(dir)), world, blockPos, blockPos.relative(dir))
            );
         }
      }

      return true;
   }

   @Override
   protected boolean setBlockIfOk(BiConsumer<BlockPos, BlockState> changedBlocks, WorldGenLevel world, BlockPos pos, BlockState state) {
      Block block = state.getBlock();
      if (block instanceof HugeMushroomBlock) {
         if (this.isReplaceableByMushroom(world, pos)) {
            this.setBlock(changedBlocks, world, pos, state);
            return true;
         } else {
            return false;
         }
      } else {
         return super.setBlockIfOk(changedBlocks, world, pos, state);
      }
   }

   @Override
   public boolean isReplaceableByLeaves(WorldGenLevel world, BlockPos pos) {
      return this.isReplaceableByMushroom(world, pos);
   }

   @Override
   public boolean isReplaceableByLogs(WorldGenLevel world, BlockPos pos) {
      return this.isReplaceableByMushroom(world, pos);
   }

   public boolean isReplaceableByMushroom(WorldGenLevel world, BlockPos pos) {
      BlockState state = world.getBlockState(pos);
      return this.isMushroomBlock(state) || this.isMushroomStem(state) || super.isReplaceableByLogs(world, pos) || super.isReplaceableByLeaves(world, pos);
   }

   @Override
   public boolean isValidGround(WorldGenLevel world, BlockState state, BlockPos pos) {
      return super.isValidGround(world, state, pos) || state.is(BlockTags.MUSHROOM_GROW_BLOCK);
   }

   public boolean isMushroomStem(BlockState state) {
      return state.getBlock() instanceof HugeMushroomBlock;
   }

   public boolean isMushroomBlock(BlockState state) {
      return state.getBlock() instanceof HugeMushroomBlock;
   }
}
