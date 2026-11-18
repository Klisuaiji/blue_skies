package com.legacy.blue_skies.blocks.natural;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.common.PlantType;

public class SkyFarmlandBlock extends FarmBlock {
   private final Supplier<Block> dirt;

   public SkyFarmlandBlock(Properties builder, Supplier<Block> dirt) {
      super(builder);
      this.dirt = dirt;
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())
         ? this.dirt.get().defaultBlockState()
         : super.getStateForPlacement(context);
   }

   public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
      PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
      return plantType == PlantType.CROP ? true : super.canSustainPlant(state, world, pos, facing, plantable);
   }

   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
      if (!level.isClientSide && CommonHooks.onFarmlandTrample(level, pos, this.dirt.get().defaultBlockState(), fallDistance, entity)) {
         turnToDirt(level.getBlockState(pos), level, pos, this);
      }

      entity.causeFallDamage(fallDistance, 1.0F, level.damageSources().fall());
   }

   public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      if (!state.canSurvive(world, pos)) {
         turnToDirt(state, world, pos, this);
      }
   }

   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      int i = (Integer)state.getValue(MOISTURE);
      if (!hasWater(world, pos) && !world.isRainingAt(pos.above())) {
         if (i > 0) {
            world.setBlock(pos, (BlockState)state.setValue(MOISTURE, i - 1), 2);
         } else if (!this.hasCrops(world, pos)) {
            turnToDirt(state, world, pos, this);
         }
      } else if (i < 7) {
         world.setBlock(pos, (BlockState)state.setValue(MOISTURE, 7), 2);
      }
   }

   private static boolean hasWater(LevelReader world, BlockPos pos) {
      for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
         if (world.getFluidState(blockpos).is(FluidTags.WATER)) {
            return true;
         }
      }

      return FarmlandWaterManager.hasBlockWaterTicket(world, pos);
   }

   private boolean hasCrops(BlockGetter world, BlockPos pos) {
      BlockState state = world.getBlockState(pos.above());
      return state.getBlock() instanceof IPlantable && this.canSustainPlant(state, world, pos, Direction.UP, (IPlantable)state.getBlock());
   }

   public static void turnToDirt(BlockState state, Level world, BlockPos pos, SkyFarmlandBlock instance) {
      world.setBlockAndUpdate(pos, pushEntitiesUp(state, instance.dirt.get().defaultBlockState(), world, pos));
   }

   public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
      return world.getBlockState(pos).getBlock() == this ? (Integer)state.getValue(FarmBlock.MOISTURE) > 0 : false;
   }
}
