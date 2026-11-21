package com.legacy.blue_skies.items.tools.weapons;

import com.legacy.blue_skies.compat.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.registries.SkiesDamageSources;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CrushingHammerItem extends Item implements IFalsiteItem {
   public static final int MAX_USE_TIME = 20;

   public CrushingHammerItem(Properties builder) {
      super(builder);
   }

   public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
      return !player.isCreative();
   }

   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.BOW;
   }

   public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
      Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
      if (equipmentSlot == EquipmentSlot.MAINHAND) {
         multimap.put(Attributes.ATTACK_DAMAGE.value(), LegacyAttrs.modifier(LegacyAttrs.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 11.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
         multimap.put(Attributes.ATTACK_SPEED.value(), LegacyAttrs.modifier(LegacyAttrs.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.3F, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE));
      }

      return multimap;
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack useStack = playerIn.getItemInHand(handIn);
      playerIn.startUsingItem(handIn);
      if (useStack.getItem() == this && playerIn.getUsedItemHand() == handIn) {
         playerIn.level().playSound(null, playerIn, SkiesSounds.ENTITY_STARLIT_CRUSHER_PREPARE_HAMMER, SoundSource.PLAYERS, 1.0F, 1.0F);
      }

      return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
   }

   public void releaseUsing(ItemStack stack, Level world, LivingEntity entityIn, int timeLeft) {
      int i = this.getUseDuration(stack) - timeLeft;
      if (entityIn instanceof Player && i >= 20 && entityIn.onGround()) {
         Player player = (Player)entityIn;
         player.getCooldowns().addCooldown(this, 80);
         ItemStack itemstack = player.getItemInHand(player.getUsedItemHand());
         itemstack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
         AABB hitbox = player.getBoundingBox().inflate(7.0, 1.0, 7.0);
         player.playSound(SkiesSounds.ENTITY_STARLIT_CRUSHER_HAMMER_SMASH, 1.0F, 1.0F);
         player.swing(player.getUsedItemHand(), true);
         player.setDeltaMovement(player.getDeltaMovement().x(), 0.2, player.getDeltaMovement().z());
         player.hurtMarked = true;
         if (world instanceof ServerLevel) {
            for (int j = 0; j < 20; j++) {
               ((ServerLevel)world).sendParticles(SkiesParticles.DUST, player.getX(), player.getY(), player.getZ(), 10, 0.0, 0.0, 0.0, 0.05);
            }
         }

         for (Entity entity : world.getEntities(player, hitbox)) {
            if (entity instanceof LivingEntity living
               && living.onGround()
               && living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue() < 1.0
               && living.hurt(SkiesDamageSources.get(living.level()).slam(player), 6.0F)) {
               living.setDeltaMovement(living.getDeltaMovement().x(), 0.7, living.getDeltaMovement().z());
               if (living instanceof Player) {
                  ((Player)living).hurtMarked = true;
               }
            }
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level worldIn, List<Component> lore, TooltipFlag flagIn) {
      lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.crushing_hammer")));
   }

   @Override
   public boolean isFalsiteCompatible(ItemStack stack) {
      return true;
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return !(enchantment instanceof DamageEnchantment) && !(enchantment instanceof KnockbackEnchantment)
         ? super.canApplyAtEnchantingTable(stack, enchantment)
         : true;
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
