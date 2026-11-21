package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.compat.*;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EtherealArcItem extends ArcItem {
   private static final AttributeModifier[] SPEED_BOOSTS = new AttributeModifier[]{
      LegacyAttrs.modifier(UUID.fromString("9b997a54-73f4-4cea-9527-d24220cc98f6"), "ethereal_speed_1", 0.1, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
      LegacyAttrs.modifier(UUID.fromString("33b4f594-371b-11e9-b210-d663bd873d94"), "ethereal_speed_2", 0.15, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
      LegacyAttrs.modifier(UUID.fromString("945684da-5657-4294-9342-803c3528dbc6"), "ethereal_speed_3", 0.2, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
      LegacyAttrs.modifier(UUID.fromString("9f6ef2b9-a342-4a6f-adf9-13b30fb71ea7"), "ethereal_speed_4", 0.25, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
   };

   public EtherealArcItem(int slotId) {
      super(slotId, "ethereal");
   }

   @Override
   public void onEquip(ItemStack stack, Player player) {
      AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
      int level = this.getFunctionalLevel(stack, player);
      if (!attribute.hasModifier(SPEED_BOOSTS[level].id())) {
         attribute.addTransientModifier(SPEED_BOOSTS[level]);
      }
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
      AttributeInstance attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

      for (int i = 0; i < SPEED_BOOSTS.length; i++) {
         if (attribute.hasModifier(SPEED_BOOSTS[i].id())) {
            attribute.removeModifier(SPEED_BOOSTS[i].id());
         }
      }
   }

   @Override
   public void refresh(ItemStack stack, Player player) {
      this.onUnequip(stack, player);
      this.onEquip(stack, player);
   }
}
