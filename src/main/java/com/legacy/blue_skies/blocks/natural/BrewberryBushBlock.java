package com.legacy.blue_skies.blocks.natural;

import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesItems;
import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public class BrewberryBushBlock extends BushBlock implements BonemealableBlock {
   public static final MapCodec<BrewberryBushBlock> CODEC = MapCodec.unit(BrewberryBushBlock::new);
   private static final VoxelShape BUSH_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
   public static final BooleanProperty MATURE = BooleanProperty.create("mature");
   public static final EnumProperty<BrewberryBushBlock.Type> TYPE = EnumProperty.create("type", BrewberryBushBlock.Type.class);

   public BrewberryBushBlock() {
      super(Properties.of().replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).randomTicks().strength(0.2F).noCollission().sound(SoundType.GRASS));
      this.registerDefaultState((BlockState)((BlockState)super.defaultBlockState().setValue(MATURE, false)).setValue(TYPE, BrewberryBushBlock.Type.RED));
   }

   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return BUSH_SHAPE;
   }

   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
      if (entityIn instanceof LivingEntity && entityIn.getType() != EntityType.FOX) {
         entityIn.makeStuckInBlock(state, new Vec3(0.9, 0.85, 0.9));
      }
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{MATURE}).add(new Property[]{TYPE});
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      if (!level.isClientSide && !(Boolean)state.getValue(MATURE) && CommonHooks.canCropGrow(level, pos, state, rand.nextInt(60) == 0)) {
         level.setBlockAndUpdate(pos, this.getGrownState(level, pos, rand));
         CommonHooks.fireCropGrowPost(level, pos, state);
      }
   }

   private ItemStack getBerry(Level level, BlockState state, BlockPos pos) {
      Block soil = level.getBlockState(pos.below()).getBlock();
      int count = level.random.nextInt(2) + 1;
      if (soil instanceof FarmBlock || soil == Blocks.PODZOL) {
         count += level.random.nextInt(3);
      }

      return new ItemStack(((BrewberryBushBlock.Type)state.getValue(TYPE)).getBerry(), count);
   }

   private BlockState getGrownState(Level level, BlockPos pos, RandomSource rand) {
      BrewberryBushBlock.Type type = BrewberryBushBlock.Type.RED;
      if (rand.nextBoolean()) {
         Block soil = level.getBlockState(pos.below()).getBlock();
         if (soil == Blocks.MYCELIUM) {
            type = BrewberryBushBlock.Type.BLACK;
         } else if (soil == SkiesBlocks.turquoise_comet_grass_block || soil == SkiesBlocks.lunar_comet_grass_block) {
            type = BrewberryBushBlock.Type.PINK;
         }
      }

      return (BlockState)((BlockState)this.defaultBlockState().setValue(MATURE, true)).setValue(TYPE, type);
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
      if ((Boolean)state.getValue(MATURE)) {
         level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, this.getBerry(level, state, pos)));
         level.playSound(null, pos, SoundEvents.GRASS_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
         level.setBlockAndUpdate(pos, (BlockState)((BlockState)state.setValue(MATURE, false)).setValue(TYPE, BrewberryBushBlock.Type.RED));
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.PASS;
      }
   }

   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      Block block = state.getBlock();
      return super.mayPlaceOn(state, level, pos) || block instanceof FarmBlock || block == Blocks.MYCELIUM;
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return !(Boolean)state.getValue(MATURE);
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return !(Boolean)state.getValue(MATURE);
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      if (rand.nextBoolean()) {
         level.setBlock(pos, this.getGrownState(level, pos, rand), 3);
      }
   }

   protected MapCodec<? extends BushBlock> codec() {
      return CODEC;
   }

   public enum Type implements StringRepresentable {
      RED("red", () -> SkiesItems.brewberry),
      PINK("pink", () -> SkiesItems.pink_brewberry),
      BLACK("black", () -> SkiesItems.black_brewberry);

      private final String name;
      private final Supplier<Item> berry;

      Type(String name, Supplier<Item> berry) {
         this.name = name;
         this.berry = berry;
      }

      public Item getBerry() {
         return this.berry.get();
      }

      public String getSerializedName() {
         return this.name;
      }
   }
}
