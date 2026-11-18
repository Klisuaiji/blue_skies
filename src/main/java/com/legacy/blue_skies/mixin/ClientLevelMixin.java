package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.ClientLevelHooks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientLevel.class})
public class ClientLevelMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"},
      cancellable = true
   )
   private void addParticle(ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo callback) {
      if (ClientLevelHooks.shouldCancelParticle(particleData, x, y, z, (ClientLevel)this)) {
         callback.cancel();
      }
   }
}
