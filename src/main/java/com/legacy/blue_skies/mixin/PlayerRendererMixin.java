package com.legacy.blue_skies.mixin;

import com.legacy.blue_skies.asm_hooks.PlayerRendererHooks;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerRenderer.class})
public class PlayerRendererMixin {
   @Inject(
      at = {@At("HEAD")},
      method = {"getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;"},
      cancellable = true
   )
   private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<ArmPose> callback) {
      if (PlayerRendererHooks.useHammerPose(player, hand)) {
         callback.setReturnValue(ArmPose.THROW_SPEAR);
      }
   }
}
