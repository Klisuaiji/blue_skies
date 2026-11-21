package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.compat.*;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.neoforge.common.NeoForgeMod;

public class AquaticArcItem extends ArcItem {
   private static final AttributeModifier[] SWIM_SPEED_BOOSTS = new AttributeModifier[]{
      LegacyAttrs.modifier(UUID.fromString("f8edab1e-d58d-45b3-9615-d9f72f21c763"), "aquatic_swim_speed_1", 0.1, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
      LegacyAttrs.modifier(UUID.fromString("4291562c-d877-4c00-b354-92758e421ee0"), "aquatic_swim_speed_2", 0.2, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
   };

   public AquaticArcItem(int slotId) {
      super(slotId, "aquatic", new Properties().stacksTo(1));
   }

   @Override
   public void onEquip(ItemStack stack, Player player) {
      AttributeInstance swimSpeed = player.getAttribute(NeoForgeMod.SWIM_SPEED);
      int level = this.getFunctionalLevel(stack, player);
      if (!swimSpeed.hasModifier(SWIM_SPEED_BOOSTS[Math.min(1, level)])) {
         swimSpeed.addTransientModifier(SWIM_SPEED_BOOSTS[Math.min(1, level)]);
      }
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
      AttributeInstance swimSpeed = player.getAttribute(NeoForgeMod.SWIM_SPEED);

      for (int i = 0; i < SWIM_SPEED_BOOSTS.length; i++) {
         if (swimSpeed.hasModifier(SWIM_SPEED_BOOSTS[i])) {
            swimSpeed.removeModifier(SWIM_SPEED_BOOSTS[i].getId());
         }
      }
   }

   @Override
   public void serverTick(ItemStack stack, ServerPlayer player) {
      int level = this.getFunctionalLevel(stack, player);
      if (level > 1 && player.getRemainingFireTicks() > 0 && player.getRemainingFireTicks() % 15 == 0) {
         int amount = player.getRemainingFireTicks() - (level - 1) * 10;
         player.setRemainingFireTicks(amount);
      }
   }

   @Override
   public void refresh(ItemStack stack, Player player) {
      this.onUnequip(stack, player);
      this.onEquip(stack, player);
   }
}
