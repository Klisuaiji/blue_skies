package com.legacy.blue_skies.entities.util.damage_source;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SummonedDamageSource extends DamageSource {
   protected final Entity summonerEntity;

   public SummonedDamageSource(Holder<DamageType> damageType, Entity directSource, Entity summoner) {
      super(damageType, directSource);
      this.summonerEntity = summoner;
   }

   public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
      ItemStack itemstack = this.getEntity() instanceof LivingEntity ? ((LivingEntity)this.getEntity()).getMainHandItem() : ItemStack.EMPTY;
      String s = "death.attack." + this.getMsgId();
      return !itemstack.isEmpty() && itemstack.hasCustomHoverName()
         ? Component.translatable(
            s + ".item",
            new Object[]{
               entityLivingBaseIn.getDisplayName(), this.summonerEntity.getDisplayName(), this.getEntity().getDisplayName(), itemstack.getDisplayName()
            }
         )
         : Component.translatable(s, new Object[]{entityLivingBaseIn.getDisplayName(), this.summonerEntity.getDisplayName(), this.getEntity().getDisplayName()});
   }
}
