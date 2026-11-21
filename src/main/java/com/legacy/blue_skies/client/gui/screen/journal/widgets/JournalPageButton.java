package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public class JournalPageButton extends JournalButton {
   private final boolean isForward;

   public JournalPageButton(int x, int y, boolean isForward, OnPress pressedAction) {
      super(x, y, 32, 16, BlueSkiesAssets.JOURNAL_LANG.getTranslation("blue_skies.blue_journal.turn_page"), pressedAction);
      this.isForward = isForward;
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         int x = 0;
         int y = 24;
         if (this.isHoveredOrFocused()) {
            y += 16;
         }

         if (!this.isForward) {
            x += 32;
         }

         graphics.blit(BlueJournalScreen.WIDGETS, this.getX(), this.getY(), x, y, 32, 16);
      }
   }

   public void playDownSound(SoundManager handler) {
      handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }
}
