package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.registries.SkiesItems;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class PlayerRendererHooks {
   public static boolean useHammerPose(AbstractClientPlayer player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      return !itemstack.isEmpty()
         && player.getUsedItemHand() == hand
         && player.getUseItemRemainingTicks() > 0
         && itemstack.getItem() == SkiesItems.crushing_hammer;
   }

   public static ArmPose getHammerPose() {
      return ArmPose.THROW_SPEAR;
   }
}
