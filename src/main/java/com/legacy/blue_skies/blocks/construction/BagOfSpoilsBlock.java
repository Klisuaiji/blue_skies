package com.legacy.blue_skies.blocks.construction;

import com.legacy.blue_skies.block_entity.BagOfSpoilsBlockEntity;
import com.legacy.blue_skies.registries.SkiesStats;
import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BagOfSpoilsBlock extends BaseEntityBlock {
   public static final MapCodec<BagOfSpoilsBlock> CODEC = simpleCodec(BagOfSpoilsBlock::new);
   private static final VoxelShape SHAPE = Shapes.or(
      Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0), new VoxelShape[]{Block.box(4.0, 10.0, 4.0, 12.0, 12.0, 12.0), Block.box(5.0, 8.0, 5.0, 11.0, 10.0, 11.0)}
   );
   public static final ResourceLocation CONTENTS = ResourceLocation.parse("contents");

   public BagOfSpoilsBlock(Properties builder) {
      super(builder);
   }

   public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
      return SHAPE;
   }

   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new BagOfSpoilsBlockEntity(pos, state);
   }

   public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
      if (worldIn.isClientSide) {
         return InteractionResult.SUCCESS;
      } else if (player.isSpectator()) {
         return InteractionResult.CONSUME;
      } else if (worldIn.getBlockEntity(pos) instanceof BagOfSpoilsBlockEntity bagTile) {
         player.openMenu(bagTile);
         player.awardStat(SkiesStats.OPEN_BAG_OF_SPOILS);
         PiglinAi.angerNearbyPiglins(player, true);
         return InteractionResult.CONSUME;
      } else {
         return InteractionResult.PASS;
      }
   }

   public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
      if (worldIn.getBlockEntity(pos) instanceof BagOfSpoilsBlockEntity bagTile) {
         if (!worldIn.isClientSide && player.isCreative() && !bagTile.isEmpty()) {
            ItemStack itemstack = new ItemStack(this);
            CompoundTag compoundnbt = bagTile.saveToNbt(new CompoundTag());
            if (!compoundnbt.isEmpty()) {
               itemstack.addTagElement("BlockEntityTag", compoundnbt);
            }

            if (bagTile.hasCustomName()) {
               itemstack.setHoverName(bagTile.getCustomName());
            }

            ItemEntity itementity = new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemstack);
            itementity.setDefaultPickUpDelay();
            worldIn.addFreshEntity(itementity);
         } else {
            bagTile.unpackLootTable(player);
         }
      }

      return super.playerWillDestroy(worldIn, pos, state, player);
   }

   public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
      BlockEntity tileentity = worldIn.getBlockEntity(pos);
      if (tileentity instanceof BagOfSpoilsBlockEntity) {
         ((BagOfSpoilsBlockEntity)tileentity).bagTick();
      }
   }

   public List<ItemStack> getDrops(BlockState state, Builder params) {
      BlockEntity blockentity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
      if (blockentity instanceof BagOfSpoilsBlockEntity bag) {
         params = params.withDynamicDrop(CONTENTS, action -> {
            for (int i = 0; i < bag.getContainerSize(); i++) {
               action.accept(bag.getItem(i));
            }
         });
      }

      return super.getDrops(state, params);
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.DESTROY;
   }

   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   public ItemStack getCloneItemStack(LevelReader worldIn, BlockPos pos, BlockState state) {
      ItemStack itemstack = new ItemStack(this);
      BagOfSpoilsBlockEntity bagTile = (BagOfSpoilsBlockEntity)worldIn.getBlockEntity(pos);
      CompoundTag compoundnbt = bagTile.saveToNbt(new CompoundTag());
      if (!compoundnbt.isEmpty()) {
         itemstack.addTagElement("BlockEntityTag", compoundnbt);
      }

      return itemstack;
   }

   protected MapCodec<? extends BaseEntityBlock> codec() {
      return CODEC;
   }
}
