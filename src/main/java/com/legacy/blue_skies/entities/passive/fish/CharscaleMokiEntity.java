package com.legacy.blue_skies.entities.passive.fish;

import com.legacy.blue_skies.entities.util.base.SkySchoolingFishEntity;
import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CharscaleMokiEntity extends SkySchoolingFishEntity {
   public CharscaleMokiEntity(EntityType<? extends CharscaleMokiEntity> typeIn, Level worldIn) {
      super(typeIn, worldIn);
   }

   @Override
   public ItemStack getVentiumBucket() {
      return new ItemStack(SkiesItems.ventium_charscale_moki_bucket);
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.COD_AMBIENT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.COD_DEATH;
   }

   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.COD_HURT;
   }

   protected SoundEvent getFlopSound() {
      return SoundEvents.COD_FLOP;
   }
}
