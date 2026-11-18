package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.registries.SkiesDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({LightTexture.class})
public class LightTextureMixin {
   @ModifyVariable(
      at = @At("STORE"),
      method = {"updateLightTexture(F)V"},
      index = 20
   )
   private float modify$f14(float f14) {
      if (SkiesDimensions.inSkyDimension(Minecraft.getInstance().player)) {
         return BlueSkiesConfig.CLIENT.shouldLimitBrightness() ? Math.min(f14, 0.5F) : 0.0F;
      } else {
         return f14;
      }
   }
}
