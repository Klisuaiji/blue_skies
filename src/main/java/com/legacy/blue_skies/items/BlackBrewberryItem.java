package com.legacy.blue_skies.items;

import com.legacy.blue_skies.registries.SkiesEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public class BlackBrewberryItem extends Item {
   public BlackBrewberryItem(Properties properties) {
      super(properties);
   }

   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      if (!worldIn.isClientSide) {
         entityLiving.removeEffect(MobEffects.POISON);
         entityLiving.removeEffect(MobEffects.WITHER);
         entityLiving.removeEffect(SkiesEffects.DEADLY_VENOM);
      }

      return super.finishUsingItem(stack, worldIn, entityLiving);
   }
}
