package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public class JournalBackButton extends JournalButton {
   public JournalBackButton(int x, int y, OnPress pressedAction) {
      super(x, y, 23, 12, BlueSkiesAssets.JOURNAL_LANG.getTranslation("blue_skies.blue_journal.back"), pressedAction);
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         graphics.blit(BlueJournalScreen.WIDGETS, this.getX(), this.getY(), 0, this.isHoveredOrFocused() ? 12 : 0, 23, 12);
      }
   }

   public void playDownSound(SoundManager handler) {
      handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }
}
