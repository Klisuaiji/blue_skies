package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.IronBarsBlockHooks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({IronBarsBlock.class})
public class IronBarsBlockMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"attachsTo(Lnet/minecraft/world/level/block/state/BlockState;Z)Z"},
      cancellable = true
   )
   private final void attachsTo(BlockState state, boolean solidSide, CallbackInfoReturnable<Boolean> callback) {
      if (IronBarsBlockHooks.shouldAttach(state)) {
         callback.setReturnValue(true);
      }
   }
}
