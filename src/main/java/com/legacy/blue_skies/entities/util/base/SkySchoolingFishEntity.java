package com.legacy.blue_skies.entities.util.base;

import com.legacy.blue_skies.entities.util.interfaces.IVentiumCatchable;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public abstract class SkySchoolingFishEntity extends AbstractSchoolingFish implements IVentiumCatchable {
   public SkySchoolingFishEntity(EntityType<? extends SkySchoolingFishEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   public ItemStack getBucketItemStack() {
      return ItemStack.EMPTY;
   }

   @Override
   public void setFromVentiumBucket(boolean fromBucket) {
      this.setFromBucket(fromBucket);
   }

   @Override
   public abstract ItemStack getVentiumBucket();

   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      if (itemstack.getItem() == Items.WATER_BUCKET) {
         return InteractionResult.FAIL;
      }

      if (itemstack.getItem() == SkiesItems.ventium_water_bucket && this.isAlive()) {
         this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0F, 1.0F);
         itemstack.shrink(1);
         ItemStack bucketItem = this.getVentiumBucket();
         this.saveToBucketTag(bucketItem);
         if (!this.level().isClientSide) {
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucketItem);
         }

         if (itemstack.isEmpty()) {
            player.setItemInHand(hand, bucketItem);
         } else if (!player.getInventory().add(bucketItem)) {
            player.drop(bucketItem, false);
         }

         this.discard();
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      } else {
         return super.mobInteract(player, hand);
      }
   }
}
