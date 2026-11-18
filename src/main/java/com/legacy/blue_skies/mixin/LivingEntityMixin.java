package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.LivingEntityHooks;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.registries.SkiesSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public class LivingEntityMixin {
   @Inject(
      at = {@At("RETURN")},
      method = {"getArmorValue()I"},
      cancellable = true
   )
   private void getArmorValue(CallbackInfoReturnable<Integer> callback) {
      callback.setReturnValue(LivingEntityHooks.getArmorValue((Integer)callback.getReturnValue(), (LivingEntity)this));
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"dismountVehicle(Lnet/minecraft/world/entity/Entity;)V"},
      cancellable = true
   )
   private void dismountVehicle(Entity mount, CallbackInfo callback) {
      if (LivingEntityHooks.shouldRepositionDismount((LivingEntity)this, mount)) {
         callback.cancel();
      }
   }

   @Redirect(
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/sounds/SoundEvents;ITEM_BREAK:Lnet/minecraft/sounds/SoundEvent;",
         opcode = 178
      ),
      method = {"breakItem(Lnet/minecraft/world/item/ItemStack;)V"}
   )
   private SoundEvent blue_skies$breakSoundModifier(ItemStack stack) {
      return stack.getItem() instanceof IFalsiteItem && IFalsiteItem.getFalsiteUses(stack) > 0 ? SkiesSounds.ITEM_FALSITE_BREAK : SoundEvents.ITEM_BREAK;
   }
}
