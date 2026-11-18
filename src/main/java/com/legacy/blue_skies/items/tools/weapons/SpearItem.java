package com.legacy.blue_skies.items.tools.weapons;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import com.legacy.blue_skies.items.util.IFalsiteItem;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.ArrowPiercingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.TridentLoyaltyEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class SpearItem extends Item implements Vanishable, IFalsiteItem {
   private final Supplier<Item> stick;

   public SpearItem(Properties builder, Supplier<Item> stick) {
      super(builder);
      this.stick = stick;
   }

   public Item getStick() {
      return this.stick.get();
   }

   public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
      return !player.isCreative();
   }

   @Override
   public boolean isFalsiteCompatible(ItemStack stack) {
      return true;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.SPEAR;
   }

   public int getUseDuration(ItemStack stack) {
      return 72000;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFoil(ItemStack stack) {
      return super.isFoil(stack);
   }

   public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
      if (entityLiving instanceof Player player) {
         int i = this.getUseDuration(stack) - timeLeft;
         if (i >= 10) {
            if (!world.isClientSide) {
               stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(entityLiving.getUsedItemHand()));
               SpearEntity spearEntity = new SpearEntity(world, player, stack);
               int piercingLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, stack);
               if (piercingLevel > 0) {
                  spearEntity.setPierceLevel((byte)piercingLevel);
               }

               spearEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
               if (player.isCreative()) {
                  spearEntity.pickup = Pickup.CREATIVE_ONLY;
               }

               world.addFreshEntity(spearEntity);
               world.playSound(
                  (Player)null,
                  spearEntity,
                  player.isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value()) ? SkiesSounds.ITEM_SPEAR_THROW_UNDERWATER : SkiesSounds.ITEM_SPEAR_THROW,
                  SoundSource.PLAYERS,
                  1.0F,
                  1.0F
               );
               if (!player.isCreative()) {
                  player.getInventory().removeItem(stack);
               }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
         }
      }
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getDamageValue() >= stack.getMaxDamage()) {
         return InteractionResultHolder.fail(stack);
      } else {
         player.startUsingItem(hand);
         return InteractionResultHolder.consume(stack);
      }
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      return true;
   }

   public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
      if (state.getDestroySpeed(worldIn, pos) != 0.0) {
         stack.hurtAndBreak(2, entityLiving, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      }

      return true;
   }

   public int getMaxDamage(ItemStack stack) {
      return ToolUtils.getMaxDurability(super.getMaxDamage(stack), this.getStick());
   }

   public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
      Multimap<Attribute, AttributeModifier> attributeMap = HashMultimap.create();
      if (equipmentSlot == EquipmentSlot.MAINHAND) {
         Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
         builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 3.0, Operation.ADDITION));
         builder.put(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", ToolUtils.getAttackSpeed(-2.9F, this.getStick()), Operation.ADDITION)
         );
         attributeMap = builder.build();
      }

      return attributeMap;
   }

   public int getEnchantmentValue() {
      return 1;
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> lore, TooltipFlag flag) {
      if (this == SkiesItems.soulbound_spear) {
         lore.add(Component.literal(StringUtil.getAbilityText("gui.blue_skies.item.ability.soulbound")));
      }
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return !(enchantment instanceof TridentLoyaltyEnchantment) && !(enchantment instanceof ArrowPiercingEnchantment)
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
