package com.legacy.blue_skies.items;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import com.legacy.blue_skies.client.gui.toast.SkiesToast;
import com.legacy.blue_skies.util.StringUtil;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlueJournalItem extends Item {
   public BlueJournalItem() {
      super(new Properties().stacksTo(1));
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
      ItemStack stack = player.getItemInHand(handIn);
      return SkiesPlayer.getIfPresent(player, skyPlayer -> {
         if (!skyPlayer.hasUsedBlueLore()) {
            skyPlayer.setUsedBlueLore(true);
            player.displayClientMessage(Component.translatable("gui.blue_skies.blue_journal.unlock_status").withStyle(ChatFormatting.GOLD), true);
            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
            if (!player.isCreative()) {
               player.getItemInHand(handIn).shrink(1);
            }

            if (worldIn.isClientSide) {
               this.showToast();
            }

            return InteractionResultHolder.success(stack);
         } else {
            if (worldIn.isClientSide) {
               this.openJournal();
            }

            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
            return InteractionResultHolder.success(stack);
         }
      }, () -> InteractionResultHolder.fail(stack));
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      tooltip.add(Component.translatable("item.blue_skies.blue_journal.info").withStyle(ChatFormatting.YELLOW));
      tooltip.add(Component.translatable("item.blue_skies.blue_journal.notice").withStyle(ChatFormatting.GRAY));
   }

   @OnlyIn(Dist.CLIENT)
   public void showToast() {
      SkiesToast.Builder.get()
         .withTitle(StringUtil.getBasicKey("toasts", "unlock_journal_entry"))
         .withDesc(StringUtil.getBasicKey("toasts", "unlock_journal_entry.desc"))
         .withSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT)
         .build()
         .beginDisplaying();
   }

   @OnlyIn(Dist.CLIENT)
   public void openJournal() {
      BlueJournalScreen.open(false);
   }
}
