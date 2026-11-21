package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.network.chat.FormattedText;

public class JournalNarratorButton extends JournalButton {
   public JournalNarratorButton(int x, int y, OnPress pressedAction) {
      super(x, y, 16, 16, BlueSkiesAssets.JOURNAL_LANG.getTranslation("blue_skies.blue_journal.read_page"), pressedAction);
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      this.visible = Minecraft.getInstance().options.narrator().get() != NarratorStatus.OFF;
      if (this.visible) {
         int y = 98;
         if (this.isHoveredOrFocused()) {
            y += 16;
         }

         graphics.blit(BlueJournalScreen.WIDGETS, this.getX(), this.getY(), 0, y, 16, 16);
      }
   }

   public static void narrate(List<List<FormattedText>> parts) {
      StringBuilder text = new StringBuilder("");

      for (int i = 0; i < parts.size(); i++) {
         for (FormattedText line : parts.get(i)) {
            text.append(line.getString() + " ");
         }

         if (i < parts.size() - 1) {
            text.append(". . . ");
         }
      }

      while (text.toString().contains("�")) {
         StringBuilder active = new StringBuilder(text.toString());
         int index = active.toString().indexOf("�");
         active.delete(index, index + 2);
         text = active;
      }

      Minecraft.getInstance().getNarrator().sayNow(text.toString());
   }
}
