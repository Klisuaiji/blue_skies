package com.legacy.blue_skies.blocks.natural;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.common.CommonHooks;

public class HangingFruitLeavesBlock extends SkyLeavesBlock implements BonemealableBlock {
   public static final BooleanProperty CAN_GROW = BooleanProperty.create("mature");
   private final Supplier<FruitBlock> fruit;

   public HangingFruitLeavesBlock(Supplier<FruitBlock> fruit) {
      super(Properties.ofFullCopy(Blocks.OAK_LEAVES));
      this.fruit = fruit;
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(CAN_GROW, false));
   }

   public FruitBlock getFruit() {
      return this.fruit.get();
   }

   public boolean isRandomlyTicking(BlockState state) {
      return !(Boolean)state.getValue(PERSISTENT) || (Boolean)state.getValue(CAN_GROW);
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (this.ableToGrow(state, level, pos)
         && CommonHooks.canCropGrow(level, pos, state, rand.nextFloat() < 0.2F && !this.getFruit().isOvercrowded(level, pos))) {
         this.grow(level, pos);
         CommonHooks.fireCropGrowPost(level, pos, state);
      }

      super.randomTick(state, level, pos, rand);
   }

   public boolean ableToGrow(BlockState state, BlockGetter level, BlockPos pos) {
      return (Boolean)state.getValue(CAN_GROW) && level.getBlockState(pos.below()).isAir();
   }

   public void grow(ServerLevel level, BlockPos pos) {
      level.setBlockAndUpdate(pos.below(), this.getFruit().defaultBlockState());
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{CAN_GROW});
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return this.ableToGrow(state, level, pos);
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return rand.nextFloat() < 0.75F;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      this.grow(level, pos);
   }
}
