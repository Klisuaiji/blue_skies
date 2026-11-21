package com.legacy.blue_skies.world.everdawn.gen.features;

import com.legacy.blue_skies.blocks.natural.LargeLilyPadBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class MoonlitWaterLilyFeature extends Feature<NoneFeatureConfiguration> {
   public MoonlitWaterLilyFeature(Codec<NoneFeatureConfiguration> config) {
      super(config);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      pos = new BlockPos(pos.getX(), level.getSeaLevel() + 1, pos.getZ());
      int oceanDepth = level.getHeight(Types.OCEAN_FLOOR, pos.getX(), pos.getZ());
      if (oceanDepth <= 60 || oceanDepth <= 61 && level.getRandom().nextBoolean()) {
         Direction facing = Direction.values()[level.getRandom().nextInt(4) + 2];

         for (LargeLilyPadBlock.Corner corner : LargeLilyPadBlock.Corner.values()) {
            BlockPos offsetPos = corner.getOffset(pos, facing);
            BlockState lowerState = level.getBlockState(offsetPos.below());
            if (lowerState.getFluidState().getType() != Fluids.WATER && !lowerState.is(BlockTags.ICE) || !level.isEmptyBlock(offsetPos)) {
               return false;
            }
         }

         for (LargeLilyPadBlock.Corner corner : LargeLilyPadBlock.Corner.values()) {
            level.setBlock(
               corner.getOffset(pos, facing),
               (BlockState)((BlockState)SkiesBlocks.moonlit_water_lily.defaultBlockState().setValue(LargeLilyPadBlock.CORNER, corner))
                  .setValue(LargeLilyPadBlock.FACING, facing),
               27
            );
         }

         return true;
      } else {
         return false;
      }
   }
}
