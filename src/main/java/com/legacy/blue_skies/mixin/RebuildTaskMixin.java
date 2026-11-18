package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.LevelRendererHooks;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   targets = {"net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection$RebuildTask"}
)
public class RebuildTaskMixin {
   @ModifyVariable(
      at = @At("STORE"),
      index = 22,
      method = {"compile"}
   )
   private RenderType storeRenderType(RenderType original) {
      LevelRendererHooks.RENDER_TYPE.set(original);
      return original;
   }

   @Inject(
      at = {@At("RETURN")},
      method = {"compile"}
   )
   private void nullRenderType(CallbackInfoReturnable<?> callback) {
      LevelRendererHooks.RENDER_TYPE.set(null);
   }
}
