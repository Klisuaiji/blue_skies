package com.legacy.blue_skies.items.tools.weapons;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class InfusedArcSwordItem extends SkySwordItem {
   protected AttributeModifier INFUSED_DAMAGE = new AttributeModifier(
      BASE_ATTACK_DAMAGE_UUID, "Infused damage modifier", this.getDamage() * 0.7F, Operation.ADDITION
   );
   private byte maxInfusedHits = 30;

   public InfusedArcSwordItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
      super(tier, attackDamageIn, attackSpeedIn, builder);
   }

   public static byte getInfusedHits(ItemStack stack) {
      CompoundTag compoundnbt = stack.getTag();
      return compoundnbt.getByte("InfusedHits");
   }

   public static void setInfusedHits(ItemStack stack, byte hitsIn) {
      CompoundTag compoundnbt = stack.getOrCreateTag();
      compoundnbt.putByte("InfusedHits", hitsIn);
   }

   public static int getInfusedCooldown(ItemStack stack) {
      CompoundTag compoundnbt = stack.getTag();
      return compoundnbt.getInt("InfusedCooldown");
   }

   public static void setInfusedCooldown(ItemStack stack, int cooldownIn) {
      CompoundTag compoundnbt = stack.getOrCreateTag();
      compoundnbt.putInt("InfusedCooldown", cooldownIn);
   }

   public void inventoryTick(ItemStack stack, Level worldIn, Entity entity, int itemSlot, boolean isSelected) {
      if (getInfusedCooldown(stack) > 0 && entity.tickCount % 20 == 0) {
         setInfusedCooldown(stack, getInfusedCooldown(stack) - 1);
         if (getInfusedCooldown(stack) == 0) {
            entity.level().playSound((Player)null, entity, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
         }
      }
   }

   @Override
   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      if (attacker instanceof Player) {
         if (getInfusedCooldown(stack) > 0) {
            return false;
         }

         if (getInfusedHits(stack) < this.maxInfusedHits) {
            setInfusedHits(stack, (byte)(getInfusedHits(stack) + 1));
         } else {
            attacker.level().playSound(null, attacker, SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
            setInfusedCooldown(stack, this.maxInfusedHits * 2);
            setInfusedHits(stack, (byte)0);
         }
      }

      return super.hurtEnemy(stack, target, attacker);
   }

   @Override
   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
      if (equipmentSlot == EquipmentSlot.MAINHAND) {
         if (getInfusedCooldown(stack) > 0) {
            multimap.put(Attributes.ATTACK_DAMAGE, this.INFUSED_DAMAGE);
         } else {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), Operation.ADDITION));
         }

         multimap.put(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ToolUtils.getAttackSpeed(this.attackSpeed, stack), Operation.ADDITION)
         );
      }

      return multimap;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      if (getInfusedCooldown(stack) <= 0) {
         lore.add(Component.literal(ChatFormatting.GRAY + "Charged Hits Remaining: " + (this.maxInfusedHits + 1 - getInfusedHits(stack))));
      } else {
         lore.add(Component.literal(ChatFormatting.AQUA + "Time Remaining: " + getInfusedCooldown(stack)));
      }

      lore.add(StringUtil.UNFINISHED);
   }
}
