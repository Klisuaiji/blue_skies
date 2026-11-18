package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.PlayerHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Player.class})
public class PlayerEntityMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"wantsToStopRiding()Z"},
      cancellable = true
   )
   private void wantsToStopRiding(CallbackInfoReturnable<Boolean> callback) {
      if (PlayerHooks.cannotDismount((Player)this)) {
         callback.setReturnValue(false);
      }
   }

   @ModifyVariable(
      at = @At(
         value = "STORE",
         ordinal = 0
      ),
      method = {"getDigSpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F"},
      remap = false
   )
   private float changeBreakSpeed(float f, BlockState state, BlockPos pos) {
      return PlayerHooks.modifyBreakSpeed(f, state, pos, (Player)this);
   }
}
