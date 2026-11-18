package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.SpellcasterIllagerHooks;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SpellcasterIllager.class})
public class SpellcastingIllagerMixin {
   @Inject(
      at = {@At(
         value = "INVOKE_ASSIGN",
         target = "net/minecraft/world/entity/monster/SpellcasterIllager.getCurrentSpell()Lnet/minecraft/world/entity/monster/SpellcasterIllager$IllagerSpell;"
      )},
      method = {"tick()V"},
      cancellable = true
   )
   private void tick(CallbackInfo callback) {
      if (SpellcasterIllagerHooks.shouldCancelParticles((SpellcasterIllager)this)) {
         callback.cancel();
      }
   }
}
