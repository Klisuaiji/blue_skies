package com.legacy.blue_skies.items.buckets;

import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class VentiumMilkBucketItem extends MilkBucketItem {
   public VentiumMilkBucketItem(Properties builder) {
      super(builder);
   }

   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!worldIn.isClientSide) {
         entityLiving.removeEffectsCuredBy(EffectCures.MILK);
      }

      if (entityLiving instanceof ServerPlayer serverplayerentity) {
         CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
         serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
      }

      if (entityLiving instanceof Player && !((Player)entityLiving).getAbilities().instabuild) {
         stack.shrink(1);
      }

      if (!worldIn.isClientSide) {
         entityLiving.removeAllEffects();
      }

      return stack.isEmpty() ? new ItemStack(SkiesItems.ventium_bucket) : stack;
   }
}
