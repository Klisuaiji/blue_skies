package com.legacy.blue_skies.items.buckets;

import com.legacy.blue_skies.blocks.util.IFluidLoggable;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.function.Supplier;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.event.EventHooks;

public class VentiumBucketItem extends BucketItem {
   public VentiumBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
      super(supplier, builder);
   }

   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack handStack = player.getItemInHand(hand);
      BlockHitResult hitResult = getPlayerPOVHitResult(
         level,
         player,
         this.content == Fluids.EMPTY ? net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY : net.minecraft.world.level.ClipContext.Fluid.NONE
      );
      InteractionResultHolder<ItemStack> ret = EventHooks.onBucketUse(player, level, handStack, hitResult);
      if (ret != null) {
         return ret;
      }

      if (hitResult.getType() == Type.MISS) {
         return InteractionResultHolder.pass(handStack);
      }

      if (hitResult.getType() != Type.BLOCK) {
         return InteractionResultHolder.pass(handStack);
      }

      BlockPos hitPos = hitResult.getBlockPos();
      Direction offset = hitResult.getDirection();
      BlockPos offsetPos = hitPos.relative(offset);
      if (level.mayInteract(player, hitPos) && player.mayUseItemAt(offsetPos, offset, handStack)) {
         BlockState hitState = level.getBlockState(hitPos);
         if (this.content == Fluids.EMPTY) {
            if (hitState.getBlock() instanceof BucketPickup bucketPickup && isValidFluid(hitState.getFluidState().getType())) {
               ItemStack fullBucket = convertBucket(bucketPickup.pickupBlock(player, level, hitPos, hitState));
               if (!fullBucket.isEmpty()) {
                  player.awardStat(Stats.ITEM_USED.get(this));
                  Fluid fluid = hitState.getFluidState().getType();
                  (fluid == Fluids.WATER ? bucketPickup.getPickupSound() : fluid.getPickupSound()).ifPresent(sound -> player.playSound(sound, 1.0F, 1.0F));
                  level.gameEvent(player, GameEvent.FLUID_PICKUP, hitPos);
                  ItemStack filledResult = ItemUtils.createFilledResult(handStack, player, fullBucket);
                  if (!level.isClientSide) {
                     CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, fullBucket);
                  }

                  return InteractionResultHolder.sidedSuccess(filledResult, level.isClientSide());
               }
            }

            return InteractionResultHolder.fail(handStack);
         } else {
            BlockPos placePos = this.canBlockContainFluid(player, level, hitPos, hitState) ? hitPos : offsetPos;
            if (this.emptyContents(player, level, placePos, hitResult)) {
               this.checkExtraContent(player, level, handStack, placePos);
               if (player instanceof ServerPlayer) {
                  CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, placePos, handStack);
               }

               player.awardStat(Stats.ITEM_USED.get(this));
               return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(handStack, player), level.isClientSide());
            } else {
               return InteractionResultHolder.fail(handStack);
            }
         }
      } else {
         return InteractionResultHolder.fail(handStack);
      }
   }

   public static ItemStack getEmptySuccessItem(ItemStack stack, Player player) {
      return !player.getAbilities().instabuild ? new ItemStack(SkiesItems.ventium_bucket) : stack;
   }

   protected boolean canBlockContainFluid(Player player, Level level, BlockPos pos, BlockState state) {
      return state.getBlock() instanceof LiquidBlockContainer
         && ((LiquidBlockContainer)state.getBlock()).canPlaceLiquid(player, level, pos, state, this.content);
   }

   public static ItemStack convertBucket(ItemStack stack) {
      Item vanillaBucket = stack.getItem();
      if (vanillaBucket == Items.WATER_BUCKET) {
         return new ItemStack(SkiesItems.ventium_water_bucket);
      } else {
         return vanillaBucket == Items.LAVA_BUCKET ? new ItemStack(SkiesItems.ventium_lava_bucket) : stack;
      }
   }

   public static boolean isValidFluid(Fluid fluid) {
      return IFluidLoggable.FluidLogging.isValid(fluid);
   }
}
