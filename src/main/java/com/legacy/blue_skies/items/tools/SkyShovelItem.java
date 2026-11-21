package com.legacy.blue_skies.items.tools;

import com.legacy.blue_skies.compat.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.items.util.ICustomHandleItem;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkyShovelItem extends ShovelItem implements IFalsiteItem, ICustomHandleItem {
   private final float attackSpeed;

   public SkyShovelItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
      super(tier, attackDamageIn, attackSpeedIn, builder);
      this.attackSpeed = attackSpeedIn;
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      if (this == SkiesItems.horizonite_shovel) {
         lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.smelt")));
      }
   }

   public int getMaxDamage(ItemStack stack) {
      return ToolUtils.getMaxDurability(this.getTier(), stack);
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      if (stack.is(SkiesItemTags.HORIZONITE_TOOLS)) {
         target.setRemainingFireTicks(3);
      }

      return super.hurtEnemy(stack, target, attacker);
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
      if (equipmentSlot == EquipmentSlot.MAINHAND) {
         multimap.put(Attributes.ATTACK_DAMAGE.value(), LegacyAttrs.modifier(LegacyAttrs.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getAttackDamage(), net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
         multimap.put(
            Attributes.ATTACK_SPEED.value(),
            LegacyAttrs.modifier(LegacyAttrs.BASE_ATTACK_SPEED_UUID, "Weapon modifier", ToolUtils.getAttackSpeed(this.attackSpeed, stack), net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE)
         );
      }

      return multimap;
   }

   public int getBarColor(ItemStack stack) {
      return this.falsiteBarColor(stack, super.getBarColor(stack));
   }

   public boolean isBarVisible(ItemStack stack) {
      return this.falsiteBarVisible(stack, super.isBarVisible(stack));
   }

   public int getBarWidth(ItemStack stack) {
      return this.falsiteBarWidth(stack, super.getBarWidth(stack));
   }
}
