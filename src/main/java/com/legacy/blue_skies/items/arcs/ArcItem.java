package com.legacy.blue_skies.items.arcs;

import com.google.common.collect.Sets;
import com.legacy.blue_skies.capability.ArcInventory;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.items.util.ColoredNameItem;
import com.legacy.blue_skies.items.util.ISpecialTabItem;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ArcItem extends ColoredNameItem implements IArcItem, ISpecialTabItem {
   private final int arcId;
   private final String ability;

   public ArcItem(int slotId) {
      this(slotId, "missing");
   }

   public ArcItem(int arcId, String ability) {
      this(arcId, ability, new Properties().stacksTo(1));
   }

   public ArcItem(int arcId, String ability, Properties props) {
      super(getColorFromType(arcId), props);
      this.ability = ability;
      this.arcId = arcId;
   }

   public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      return SkiesPlayer.getIfPresent(player, skiesPlayer -> {
         if (stack.getItem() instanceof IArcItem && !skiesPlayer.getArcInventory().hasArc((IArcItem)stack.getItem())) {
            for (int slot = 0; slot < skiesPlayer.getArcInventory().getContainerSize(); slot++) {
               if (skiesPlayer.getArcInventory().getItem(slot).isEmpty()) {
                  skiesPlayer.getArcInventory().setItem(slot, stack.copy());
                  ((IArcItem)stack.getItem()).onEquip(stack, player);
                  stack.setCount(0);
                  skiesPlayer.syncDataToClient();
                  player.playSound(this.getEquipSound(this.arcId), 1.0F, 1.0F);
                  return InteractionResultHolder.success(stack);
               }
            }
         }

         return InteractionResultHolder.fail(stack);
      }, () -> InteractionResultHolder.fail(stack));
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> lore, TooltipFlag flag) {
      boolean unfinished = stack.getItem() == SkiesItems.aquatic_arc || stack.getItem() == SkiesItems.life_arc || stack.getItem() == SkiesItems.runic_arc;
      if (unfinished) {
         lore.add(StringUtil.UNFINISHED);
      }

      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      int level = this.getFunctionalLevel(stack, player);
      Component abilityLabel = Component.translatable("gui.blue_skies.item.ability").withStyle(ChatFormatting.GREEN);
      Component itemAbility;
      if (((ArcItem)stack.getItem()).ability.equals("missing")) {
         itemAbility = Component.translatable(String.format("gui.blue_skies.item.ability.missing"));
      } else {
         itemAbility = Component.translatable(String.format("gui.blue_skies.item.ability.%s.%s", ((ArcItem)stack.getItem()).ability, level));
      }

      lore.add(Component.translatable("%s: %s", new Object[]{abilityLabel, itemAbility}));
      if (this.getMaxLevel() > 0) {
         Component rarityLabel = Component.translatable("gui.blue_skies.item.rarity").withStyle(ChatFormatting.LIGHT_PURPLE);
         Component itemRarity = Component.translatable(String.format("gui.blue_skies.item.rarity.%s", level));
         Boolean hasRunic = SkiesPlayer.getIfPresent(player, skyPlayer -> {
            ArcInventory inv = skyPlayer.getArcInventory();
            return inv.hasAnyOf(Sets.newHashSet(new Item[]{SkiesItems.runic_arc})) && inv.getStacks().contains(stack);
         }, () -> false);
         if (hasRunic && this.getLevel(stack) < this.getMaxLevel()) {
            lore.add(
               Component.translatable(
                  "%s: %s (%s)", new Object[]{rarityLabel, itemRarity, SkiesItems.runic_arc.getName(new ItemStack(SkiesItems.runic_arc)).getString()}
               )
            );
         } else {
            lore.add(Component.translatable("%s: %s", new Object[]{rarityLabel, itemRarity}));
         }
      }

      lore.add(Component.translatable("gui.blue_skies.item.ability.summon_boss").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFoil(ItemStack stack) {
      Minecraft mc = Minecraft.getInstance();
      return this.getFunctionalLevel(stack, mc.player) == this.getMaxLevel() && this.getMaxLevel() > 0 || super.isFoil(stack);
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }

   @Override
   public int getFunctionalLevel(ItemStack stack, Player player) {
      int level = this.getLevel(stack);
      Boolean hasRunic = SkiesPlayer.getIfPresent(player, skyPlayer -> {
         ArcInventory inv = skyPlayer.getArcInventory();
         return inv.hasAnyOf(Sets.newHashSet(new Item[]{SkiesItems.runic_arc})) && inv.getStacks().contains(stack);
      }, () -> false);
      if (level < this.getMaxLevel() && hasRunic) {
         level++;
      }

      return level;
   }

   public boolean hasCraftingRemainingItem() {
      return true;
   }

   public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
      return itemStack.copy();
   }

   @Override
   public void fillTabs(Output output) {
      if (this.getMaxLevel() >= 0) {
         output.accept(new ItemStack(this), TabVisibility.PARENT_TAB_ONLY);

         for (int level = 0; level < this.getMaxLevel(); level++) {
            ItemStack stack = new ItemStack(this);
            this.setLevel(stack, level);
            output.accept(stack, TabVisibility.SEARCH_TAB_ONLY);
         }
      } else {
         output.accept(new ItemStack(this), TabVisibility.PARENT_AND_SEARCH_TABS);
      }
   }

   private static ChatFormatting getColorFromType(int dungeonTypeIn) {
      switch (dungeonTypeIn) {
         case 0:
            return ChatFormatting.DARK_AQUA;
         case 1:
            return ChatFormatting.GRAY;
         case 2:
            return ChatFormatting.GREEN;
         case 3:
            return ChatFormatting.LIGHT_PURPLE;
         case 4:
            return ChatFormatting.BLUE;
         case 5:
            return ChatFormatting.RED;
         case 6:
            return ChatFormatting.GOLD;
         default:
            return ChatFormatting.WHITE;
      }
   }

   private SoundEvent getEquipSound(int id) {
      switch (id) {
         case 0:
            return SkiesSounds.ITEM_ETHEREAL_ARC_EQUIP;
         case 1:
            return SkiesSounds.ITEM_DUSK_ARC_EQUIP;
         case 2:
            return SkiesSounds.ITEM_NATURE_ARC_EQUIP;
         case 3:
            return SkiesSounds.ITEM_POISON_ARC_EQUIP;
         case 4:
            return SkiesSounds.ITEM_AQUATIC_ARC_EQUIP;
         case 5:
            return SkiesSounds.ITEM_LIFE_ARC_EQUIP;
         case 6:
            return SkiesSounds.ITEM_RUNIC_ARC_EQUIP;
         default:
            return SoundEvents.ARMOR_EQUIP_GENERIC;
      }
   }

   @Override
   public void serverTick(ItemStack stack, ServerPlayer player) {
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void clientTick(ItemStack stack, LocalPlayer player) {
   }

   @Override
   public void onEquip(ItemStack stack, Player player) {
   }

   @Override
   public void onUnequip(ItemStack stack, Player player) {
   }

   @Override
   public void refresh(ItemStack stack, Player player) {
   }

   @Override
   public void onHit(ItemStack stack, Player player, LivingDamageEvent event) {
   }
}
