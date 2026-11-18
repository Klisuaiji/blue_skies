package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.VillagerHooks;
import com.legacy.blue_skies.entities.villager.SkiesTradeModifiers;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Villager.class})
public class VillagerEntityMixin {
   @Inject(
      at = {@At("TAIL")},
      method = {"registerBrainGoals(Lnet/minecraft/world/entity/ai/Brain;)V"}
   )
   private void registerBrainGoals(Brain<Villager> villagerBrain, CallbackInfo callback) {
      VillagerHooks.registerBrainGoals((Villager)this, villagerBrain);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"hasFarmSeeds()Z"},
      cancellable = true
   )
   private void hasFarmSeeds(CallbackInfoReturnable<Boolean> callback) {
      if (VillagerHooks.hasFarmSeeds((Villager)this)) {
         callback.setReturnValue(true);
      }
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"updateTrades()V"},
      cancellable = true
   )
   private void updateTrades(CallbackInfo callback) {
      SkiesTradeModifiers.replaceOffers((Villager)this);
   }
}
