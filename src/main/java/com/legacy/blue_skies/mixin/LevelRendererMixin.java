package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.LevelRendererHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LevelRenderer.class})
public class LevelRendererMixin {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   private ClientLevel level;
   @Shadow
   private int ticks;

   @Inject(
      at = {@At("RETURN")},
      method = {"getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I"},
      cancellable = true
   )
   private static void getLightColor(BlockAndTintGetter lightReaderIn, BlockState blockStateIn, BlockPos blockPosIn, CallbackInfoReturnable<Integer> callback) {
      if (LevelRendererHooks.isEmissive(blockStateIn)) {
         int light = LevelRendererHooks.getLight(lightReaderIn, blockStateIn, blockPosIn);
         if (light > (Integer)callback.getReturnValue()) {
            callback.setReturnValue(light);
         }
      }
   }
}
