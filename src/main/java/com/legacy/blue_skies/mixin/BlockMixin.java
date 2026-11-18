package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.BlockHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Block.class})
public class BlockMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"shouldRenderFace"},
      cancellable = true
   )
   private static void shouldRenderFace(
      BlockState state, BlockGetter world, BlockPos pos, Direction dir, BlockPos pos2, CallbackInfoReturnable<Boolean> callback
   ) {
      if (BlockHooks.isTranslucentBlock(state, world, pos, dir)) {
         callback.setReturnValue(false);
      }
   }
}
