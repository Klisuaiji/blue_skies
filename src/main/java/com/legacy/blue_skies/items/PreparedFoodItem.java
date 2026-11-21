package com.legacy.blue_skies.items;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.items.util.FoodPrepUtils;
import com.legacy.blue_skies.items.util.PreparedFood;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PreparedFoodItem extends Item {
   public PreparedFoodItem(Properties properties) {
      super(properties);
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.EAT;
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack heldItem = player.getItemInHand(hand);
      if (player.canEat(FoodPrepUtils.deserialize(LegacyNbt.get(heldItem)).getHungerPerBite() <= 2)) {
         player.startUsingItem(hand);
         return InteractionResultHolder.consume(heldItem);
      } else {
         return InteractionResultHolder.fail(heldItem);
      }
   }

   public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
      PreparedFood preparedFood = FoodPrepUtils.deserialize(LegacyNbt.get(stack));
      if (entity instanceof Player player) {
         player.getFoodData().eat(preparedFood.getHungerPerBite(), preparedFood.getSaturation());
         player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
         world.playSound(
            (Player)null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.PLAYER_BURP,
            SoundSource.PLAYERS,
            0.5F,
            world.random.nextFloat() * 0.1F + 0.9F
         );
         if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
         }
      }

      world.playSound(
         (Player)null,
         entity.getX(),
         entity.getY(),
         entity.getZ(),
         entity.getEatingSound(stack),
         SoundSource.NEUTRAL,
         1.0F,
         1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F
      );
      preparedFood.getEffects().forEach(entity::addEffect);
      CompoundTag tag = LegacyNbt.getOrCreate(stack);
      if (!(entity instanceof Player) || !((Player)entity).getAbilities().instabuild) {
         preparedFood.use();
         if (preparedFood.getUses() <= 0) {
            stack.shrink(1);
         } else {
            tag.putInt("Uses", preparedFood.getUses());
         }
      }

      FoodPrepUtils.causeInstability(preparedFood, entity);
      return stack;
   }

   public boolean isEdible() {
      return false;
   }

   public boolean isBarVisible(ItemStack stack) {
      return FoodPrepUtils.getUses(stack) < FoodPrepUtils.getMaxUses(stack);
   }

   public int getBarWidth(ItemStack stack) {
      double durability = 1.0 - ((double)FoodPrepUtils.getMaxUses(stack) - FoodPrepUtils.getUses(stack)) / FoodPrepUtils.getMaxUses(stack);
      return (int)(durability * 13.0);
   }

   public int getBarColor(ItemStack stack) {
      float p = Math.max(0.0F, (float)FoodPrepUtils.getUses(stack) / FoodPrepUtils.getMaxUses(stack));
      return Mth.hsvToRgb(p / 3.0F, 1.0F, 1.0F);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
      PreparedFood preparedFood = FoodPrepUtils.deserialize(LegacyNbt.get(stack));
      preparedFood.getIngredients().forEach(i -> tooltip.add(Component.literal("- ").append(i.getHoverName()).withStyle(ChatFormatting.GRAY)));
      if (BlueSkies.IDE && Screen.hasShiftDown()) {
         tooltip.add(Component.empty());
         tooltip.add(Component.literal("Developer Info").withStyle(ChatFormatting.UNDERLINE));
         tooltip.add(Component.literal("Uses: " + preparedFood.getUses() + "/" + preparedFood.getMaxUses()).withStyle(ChatFormatting.GRAY));
         tooltip.add(Component.literal("Hunger: " + preparedFood.getHungerPerBite()).withStyle(ChatFormatting.GRAY));
         tooltip.add(Component.literal("Total Hunger: " + preparedFood.getTotalHunger()).withStyle(ChatFormatting.GRAY));
         tooltip.add(Component.literal("Saturation: " + preparedFood.getSaturation()).withStyle(ChatFormatting.GRAY));
         if (preparedFood.getEffects().size() > 0) {
            tooltip.add(Component.literal("Effects:").withStyle(ChatFormatting.GRAY));
            preparedFood.getEffects()
               .forEach(
                  i -> tooltip.add(
                     Component.literal("- ")
                        .append(
                           String.format(
                              "%s x%d %d:%d",
                              i.getEffect().value().getDisplayName().getString(),
                              i.getAmplifier() + 1,
                              (int)(i.getDuration() / 20.0F) / 60,
                              (int)(i.getDuration() / 20.0F) % 60
                           )
                        )
                        .withStyle(ChatFormatting.GRAY)
                  )
               );
         }
      }
   }
}
