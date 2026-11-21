package com.legacy.blue_skies.items.tools.weapons;

import com.legacy.blue_skies.compat.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.legacy.blue_skies.items.tools.SkyShieldItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;

public class SpikeShieldItem extends SkyShieldItem {
   public SpikeShieldItem(Properties builder) {
      super(builder);
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity p_77644_2_, LivingEntity user) {
      stack.hurtAndBreak(1, user, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
      return true;
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
      if (equipmentSlot == EquipmentSlot.MAINHAND) {
         multimap.put(Attributes.ATTACK_DAMAGE.value(), LegacyAttrs.modifier(LegacyAttrs.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
         multimap.put(Attributes.ATTACK_SPEED.value(), LegacyAttrs.modifier(LegacyAttrs.BASE_ATTACK_SPEED_UUID, "Speed modifier", -2.8F, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
      }

      return multimap;
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return !(enchantment instanceof DamageEnchantment) && !(enchantment instanceof KnockbackEnchantment)
         ? super.canApplyAtEnchantingTable(stack, enchantment)
         : true;
   }
}
