package com.legacy.blue_skies.blocks.util;

import com.legacy.blue_skies.items.buckets.VentiumBucketItem;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.IShearable;

public class DispenserBehaviors {
   public static void init() {
      for (Item item : Arrays.asList(
         SkiesItems.ventium_water_bucket,
         SkiesItems.ventium_lava_bucket,
         SkiesItems.ventium_drifter_bucket,
         SkiesItems.ventium_grittle_flatfish_bucket,
         SkiesItems.ventium_horizofin_tunid_bucket,
         SkiesItems.ventium_municipal_monkfish_bucket,
         SkiesItems.ventium_charscale_moki_bucket
      )) {
         DispenserBlock.registerBehavior(item, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public ItemStack execute(BlockSource blockSource, ItemStack stack) {
               DispensibleContainerItem dispensibleItem = (DispensibleContainerItem)stack.getItem();
               BlockPos pos = blockSource.pos().relative((Direction)blockSource.state().getValue(DispenserBlock.FACING));
               ServerLevel level = blockSource.level();
               if (dispensibleItem.emptyContents(null, level, pos, null)) {
                  dispensibleItem.checkExtraContent(null, level, stack, pos);
                  return new ItemStack(SkiesItems.ventium_bucket);
               } else {
                  return this.defaultDispenseItemBehavior.dispense(blockSource, stack);
               }
            }
         });
      }

      DispenserBlock.registerBehavior(SkiesItems.ventium_bucket, new DefaultDispenseItemBehavior() {
         private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

         public ItemStack execute(BlockSource blockSource, ItemStack stack) {
            LevelAccessor level = blockSource.level();
            BlockPos pos = blockSource.pos().relative((Direction)blockSource.state().getValue(DispenserBlock.FACING));
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof BucketPickup && VentiumBucketItem.isValidFluid(state.getFluidState().getType())) {
               ItemStack fullBucketStack = VentiumBucketItem.convertBucket(((BucketPickup)block).pickupBlock(null, level, pos, state));
               if (fullBucketStack.isEmpty()) {
                  return super.execute(blockSource, stack);
               }

               level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
               Item fullBucket = fullBucketStack.getItem();
               stack.shrink(1);
               if (stack.isEmpty()) {
                  return new ItemStack(fullBucket);
               }

               if (blockSource.blockEntity().addItem(new ItemStack(fullBucket)) < 0) {
                  this.defaultDispenseItemBehavior.dispense(blockSource, new ItemStack(fullBucket));
               }

               return stack;
            } else {
               return super.execute(blockSource, stack);
            }
         }
      });
      DispenserBlock.registerBehavior(
         SkiesItems.ventium_shears,
         new OptionalDispenseItemBehavior() {
            protected ItemStack execute(BlockSource source, ItemStack stack) {
               Level world = source.level();
               if (!world.isClientSide()) {
                  this.setSuccess(false);
                  BlockPos oos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));

                  for (Entity entity : world.getEntities((Entity)null, new AABB(oos), e -> !e.isSpectator() && e instanceof IShearable)) {
                     IShearable target = (IShearable)entity;
                     if (target.isShearable(stack, world, oos)) {
                        List<ItemStack> drops = target.onSheared(
                           null, stack, entity.level(), oos, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FORTUNE, stack)
                        );
                        Random rand = new Random();
                        drops.forEach(
                           d -> {
                              ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                              if (ent != null) {
                                 ent.setDeltaMovement(
                                    ent.getDeltaMovement()
                                       .add(
                                          (rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F
                                       )
                                 );
                              }
                           }
                        );
                        if (stack.hurt(1, world.random, null)) {
                           stack.setCount(0);
                        }

                        this.setSuccess(true);
                        break;
                     }
                  }
               }

               return stack;
            }
         }
      );
   }
}
