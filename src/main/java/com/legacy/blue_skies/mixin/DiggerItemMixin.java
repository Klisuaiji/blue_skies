package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.entities.hostile.ArmoredFrostSpiritEntity;
import com.legacy.blue_skies.entities.hostile.StoneletEntity;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DiggerItem.class})
public class DiggerItemMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"hurtEnemy(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z"},
      cancellable = true
   )
   private void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> callback) {
      if (stack.getItem() instanceof AxeItem && (target instanceof StarlitCrusherEntity || target instanceof EntWallEntity)
         || stack.getItem() instanceof PickaxeItem && (target instanceof ArmoredFrostSpiritEntity || target instanceof StoneletEntity)) {
         stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
         callback.setReturnValue(true);
      }
   }
}
