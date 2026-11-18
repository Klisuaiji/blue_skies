package com.legacy.blue_skies.blocks.construction;

import com.google.common.base.Supplier;
import com.legacy.blue_skies.registries.SkiesItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.CommonHooks;

public class SkyBrewberryBushPotBlock extends SkyFlowerPotBlock implements BonemealableBlock {
   public static final MapCodec<FlowerPotBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(flowerCodec(), propertiesCodec()).apply(instance, (flower, prop) -> new SkyBrewberryBushPotBlock(() -> flower, prop))
   );
   public static final BooleanProperty MATURE = BooleanProperty.create("mature");

   public SkyBrewberryBushPotBlock(Supplier<? extends Block> flower, Properties properties) {
      super(flower, properties);
      this.registerDefaultState((BlockState)this.defaultBlockState().setValue(MATURE, false));
   }

   protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{MATURE});
   }

   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      if (!level.isClientSide && !(Boolean)state.getValue(MATURE) && CommonHooks.onCropsGrowPre(level, pos, state, random.nextInt(60) == 0)) {
         level.setBlockAndUpdate(pos, (BlockState)state.setValue(MATURE, true));
         CommonHooks.onCropsGrowPost(level, pos, state);
      }
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (player.getItemInHand(hand).getItem() == Items.BONE_MEAL) {
         return InteractionResult.PASS;
      } else if ((Boolean)state.getValue(MATURE)) {
         level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(SkiesItems.brewberry)));
         level.playSound((Player)null, pos, SoundEvents.GRASS_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
         level.setBlockAndUpdate(pos, (BlockState)state.setValue(MATURE, false));
         return InteractionResult.SUCCESS;
      } else {
         super.use(state, level, pos, player, hand, hit);
         return InteractionResult.PASS;
      }
   }

   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
      return !(Boolean)state.getValue(MATURE);
   }

   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return !(Boolean)state.getValue(MATURE);
   }

   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      if (rand.nextFloat() > 0.5F) {
         level.setBlock(pos, (BlockState)state.setValue(MATURE, true), 3);
      }
   }

   @Override
   public MapCodec<FlowerPotBlock> codec() {
      return CODEC;
   }
}
