package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class BrumbleVineBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
   public static final MapCodec<BrumbleVineBlock> CODEC = simpleCodec(BrumbleVineBlock::new);

   public BrumbleVineBlock(Properties properties) {
      super(properties, Direction.UP, Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0), true);
      this.registerDefaultState(this.defaultBlockState());
   }

   public FluidState getFluidState(BlockState state) {
      return Fluids.WATER.getSource(false);
   }

   protected GrowingPlantHeadBlock getHeadBlock() {
      return SkiesBlocks.brumble_vine_top;
   }

   public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
      return false;
   }

   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
      return false;
   }

   protected MapCodec<? extends GrowingPlantBodyBlock> codec() {
      return CODEC;
   }
}
