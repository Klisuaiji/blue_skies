package com.legacy.blue_skies.blocks.natural;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkyCropBlock extends CropBlock {
   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
   private final Supplier<? extends Item> seed;
   private final VoxelShape[] shapeByAge;

   public SkyCropBlock(Supplier<? extends Item> seed, double height) {
      super(Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.0F).sound(SoundType.CROP));
      this.seed = seed;
      this.shapeByAge = this.generateShapes(height);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(AGE, 0));
   }

   public VoxelShape[] generateShapes(double height) {
      int ages = this.getAgeProperty().getPossibleValues().size();
      VoxelShape[] shapes = new VoxelShape[ages];

      for (int i = 0; i < ages; i++) {
         shapes[i] = Block.box(0.0, 0.0, 0.0, 16.0, (double)(i + 1) / ages * height, 16.0);
      }

      return shapes;
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return this.shapeByAge[state.getValue(this.getAgeProperty())];
   }

   public IntegerProperty getAgeProperty() {
      return AGE;
   }

   public boolean isRandomlyTicking(BlockState state) {
      return !this.isMaxAge(state);
   }

   public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
      if (random.nextInt(3) != 0) {
         super.randomTick(state, worldIn, pos, random);
      }
   }

   public int getMaxAge() {
      return this.getAgeProperty().getPossibleValues().size() - 1;
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
      return state.getBlock() instanceof FarmBlock;
   }

   protected ItemLike getBaseSeedId() {
      return (ItemLike)this.seed.get();
   }

   public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader world, BlockPos pos, Player player) {
      return new ItemStack(this.getBaseSeedId());
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{AGE});
   }
}
