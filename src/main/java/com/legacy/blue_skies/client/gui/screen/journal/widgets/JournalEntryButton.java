package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.SkiesClientUtil;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import com.legacy.blue_skies.data.objects.journal.JournalEntry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class JournalEntryButton extends JournalButton implements Comparable<JournalEntryButton> {
   public final JournalEntry entry;

   public JournalEntryButton(int x, int y, JournalEntry entry, OnPress pressedAction) {
      super(x, y, 130, 18, BlueSkiesAssets.JOURNAL_LANG.getTranslation(entry.title), pressedAction);
      this.entry = entry;
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         PoseStack poseStack = graphics.pose();
         Minecraft mc = Minecraft.getInstance();
         SkiesClientUtil.bind(BlueJournalScreen.WIDGETS);
         int textLength = 105;
         Component display = (Component)(this.isHoveredOrFocused()
            ? this.getMessage()
            : Component.literal(
               mc.font.plainSubstrByWidth(this.getMessage().getString(), textLength) + (mc.font.width(this.getMessage().getString()) > textLength ? "..." : "")
            ));
         if (this.isHoveredOrFocused()) {
            int startX = 56;
            int height = 18;
            graphics.blit(BlueJournalScreen.WIDGETS, this.getX() - 2, this.getY(), 0, startX, 3, height);

            int i;
            for (i = 0; i < mc.font.width(display) + 20; i += 2) {
               graphics.blit(BlueJournalScreen.WIDGETS, this.getX() + i, this.getY(), 4, startX, 2, height);
            }

            graphics.blit(BlueJournalScreen.WIDGETS, this.getX() + i, this.getY(), 7, startX, 3, height);
         }

         this.entry.icon.render(graphics, mc, this.getX() + 1, this.getY() + 1, partialTicks);
         poseStack.pushPose();
         poseStack.translate(0.0F, 0.0F, 300.0F);
         graphics.drawString(mc.font, display, this.getX() + 19, this.getY() + 5, 0, false);
         poseStack.popPose();
      }
   }

   public int compareTo(JournalEntryButton o) {
      return this.entry.priority == o.entry.priority
         ? this.getMessage().getString().compareTo(o.getMessage().getString())
         : Integer.compare(o.entry.priority, this.entry.priority);
   }

   public void playDownSound(SoundManager handler) {
      handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }
}
