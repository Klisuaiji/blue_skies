package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class BrumbleVineTopBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
   public static final MapCodec<BrumbleVineTopBlock> CODEC = simpleCodec(BrumbleVineTopBlock::new);

   public BrumbleVineTopBlock(Properties properties) {
      super(properties, Direction.UP, Block.box(2.0, 0.0, 2.0, 15.0, 14.0, 14.0), true, 0.14F);
      this.registerDefaultState(this.defaultBlockState());
   }

   public FluidState getFluidState(BlockState state) {
      return Fluids.WATER.getSource(false);
   }

   public boolean canPlaceLiquid(Player player, BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
      return false;
   }

   public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
      return false;
   }

   protected int getBlocksToGrowWhenBonemealed(RandomSource rand) {
      return 1;
   }

   protected boolean canGrowInto(BlockState state) {
      return state.is(Blocks.WATER);
   }

   protected Block getBodyBlock() {
      return SkiesBlocks.brumble_vine;
   }

   protected boolean canAttachTo(BlockState block) {
      return block.getBlock() != Blocks.MAGMA_BLOCK;
   }

   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
      return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 ? super.getStateForPlacement(context) : null;
   }

   protected MapCodec<? extends GrowingPlantHeadBlock> codec() {
      return CODEC;
   }
}
