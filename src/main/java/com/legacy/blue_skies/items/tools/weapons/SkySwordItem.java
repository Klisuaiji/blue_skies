package com.legacy.blue_skies.items.tools.weapons;

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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class SkySwordItem extends SwordItem implements IFalsiteItem, ICustomHandleItem {
   protected final float attackSpeed;

   public SkySwordItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
      super(tier, attackDamageIn, attackSpeedIn, builder);
      this.attackSpeed = attackSpeedIn;
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      if (stack.is(SkiesItemTags.HORIZONITE_TOOLS)) {
         target.setSecondsOnFire(3);
      }

      if (this == SkiesItems.different_sword) {
         target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 3));
      }

      return super.hurtEnemy(stack, target, attacker);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      if (this == SkiesItems.different_sword) {
         lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.powerful")));
         lore.add(Component.literal(StringUtil.getConText("gui.blue_skies.item.con.regen")));
      } else if (this == SkiesItems.horizonite_sword) {
         lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.fire")));
      } else if (this == SkiesItems.debug_sword) {
         lore.add(Component.literal(ChatFormatting.GRAY + "This is a testing item, you should not have this in normal gameplay."));
      }
   }

   public int getMaxDamage(ItemStack stack) {
      return ToolUtils.getMaxDurability(this.getTier(), stack);
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
      if (equipmentSlot == EquipmentSlot.MAINHAND) {
         multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), Operation.ADDITION));
         multimap.put(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ToolUtils.getAttackSpeed(this.attackSpeed, stack), Operation.ADDITION)
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
