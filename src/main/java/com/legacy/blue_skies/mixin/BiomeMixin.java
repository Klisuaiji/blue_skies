package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.BiomeHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Biome.class})
public class BiomeMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"},
      cancellable = true
   )
   private void preventSnow(LevelReader worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
      if (BiomeHooks.preventSnow((Biome)this, worldIn, pos)) {
         callback.setReturnValue(false);
      }
   }
}
