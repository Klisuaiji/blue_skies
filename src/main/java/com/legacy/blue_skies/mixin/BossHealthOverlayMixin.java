package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.BossHealthOverlayHooks;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BossHealthOverlay.class})
public class BossHealthOverlayMixin {
   @Final
   @Shadow
   private Minecraft minecraft;
   @Final
   @Shadow
   private Map<UUID, LerpingBossEvent> events;

   @Inject(
      at = {@At("TAIL")},
      method = {"render"},
      cancellable = true
   )
   private void displayBossbar(GuiGraphics graphics, CallbackInfo callback) {
      BossHealthOverlayHooks.render(this.minecraft, this.events, graphics);
   }
}
