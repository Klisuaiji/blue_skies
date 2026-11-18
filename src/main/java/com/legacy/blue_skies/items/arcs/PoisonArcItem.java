package com.legacy.blue_skies.items.arcs;

import com.legacy.blue_skies.registries.SkiesEffects;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PoisonArcItem extends ArcItem {
   private static final AttributeModifier[] DAMAGE_BOOSTS = new AttributeModifier[]{
      new AttributeModifier(UUID.fromString("f785f3aa-eea3-42e4-97a0-5271490e0b94"), "poison_damage_1", 1.0, Operation.ADDITION),
      new AttributeModifier(UUID.fromString("5752252b-ed14-4e03-b149-7e05c43152ed"), "poison_damage_2", 2.0, Operation.ADDITION),
      new AttributeModifier(UUID.fromString("f24a19fe-2893-4372-a5b3-11b5d571a7d7"), "poison_damage_3", 3.0, Operation.ADDITION),
      new AttributeModifier(UUID.fromString("948033ce-317c-4d6a-bb14-1ef306ebe88e"), "poison_damage_4", 4.0, Operation.ADDITION)
   };

   public PoisonArcItem(int slotId) {
      super(slotId, "poison");
   }

   @Override
   public void onEquip(ItemStack stack, Player player) {
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
      AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

      for (int i = 0; i < DAMAGE_BOOSTS.length; i++) {
         if (attribute.hasModifier(DAMAGE_BOOSTS[i])) {
            attribute.removeModifier(DAMAGE_BOOSTS[i].getId());
         }
      }
   }

   @Override
   public void serverTick(ItemStack stack, ServerPlayer player) {
      AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
      boolean isPoisoned = player.getEffect(SkiesEffects.DEADLY_VENOM) != null || player.getEffect(MobEffects.POISON) != null;
      int level = this.getFunctionalLevel(stack, player);
      if (isPoisoned) {
         if (!attribute.hasModifier(DAMAGE_BOOSTS[level])) {
            attribute.addTransientModifier(DAMAGE_BOOSTS[level]);
         }
      } else {
         for (int i = 0; i < DAMAGE_BOOSTS.length; i++) {
            if (attribute.hasModifier(DAMAGE_BOOSTS[i])) {
               attribute.removeModifier(DAMAGE_BOOSTS[i].getId());
            }
         }
      }
   }
}
