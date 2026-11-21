package com.legacy.blue_skies.blocks.natural;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;

public class FruitLeavesBlock extends SkyLeavesBlock implements BonemealableBlock {
   public static final EnumProperty<FruitLeavesBlock.Growth> GROWTH = EnumProperty.create("growth", FruitLeavesBlock.Growth.class);
   private final Supplier<ItemLike> berry;

   public FruitLeavesBlock(Properties props, Supplier<ItemLike> berry) {
      super(props);
      this.berry = berry;
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(GROWTH, FruitLeavesBlock.Growth.CANT_GROW));
   }

   public boolean isRandomlyTicking(BlockState state) {
      return !(Boolean)state.getValue(PERSISTENT) || state.getValue(GROWTH) == FruitLeavesBlock.Growth.CAN_GROW;
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (this.ableToGrow(state) && CommonHooks.onCropsGrowPre(level, pos, state, rand.nextFloat() < 0.1F)) {
         this.grow(state, level, pos);
         CommonHooks.onCropsGrowPost(level, pos, state);
      }

      super.randomTick(state, level, pos, rand);
   }

   public boolean ableToGrow(BlockState state) {
      return state.getValue(GROWTH) == FruitLeavesBlock.Growth.CAN_GROW;
   }

   public void grow(BlockState state, ServerLevel level, BlockPos pos) {
      level.setBlockAndUpdate(pos, (BlockState)state.setValue(GROWTH, FruitLeavesBlock.Growth.GROWN));
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (state.getValue(GROWTH) == FruitLeavesBlock.Growth.GROWN) {
         level.setBlockAndUpdate(pos, (BlockState)state.setValue(GROWTH, FruitLeavesBlock.Growth.CAN_GROW));
         level.playSound(null, pos, SoundEvents.GRASS_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
         popResourceFromFace(level, pos, hit.getDirection(), new ItemStack(this.berry.get()));
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   @Override
   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(new Property[]{GROWTH});
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return this.ableToGrow(state);
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return rand.nextFloat() < 0.75F;
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      this.grow(state, level, pos);
   }

   public enum Growth implements StringRepresentable {
      CANT_GROW("cant_grow"),
      CAN_GROW("can_grow"),
      GROWN("grown");

      private final String name;

      Growth(String name) {
         this.name = name;
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}
