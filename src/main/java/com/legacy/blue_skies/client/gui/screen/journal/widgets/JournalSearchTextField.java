package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class JournalSearchTextField extends EditBox {
   public JournalSearchTextField(Font font, int x, int y, int width, int height, Component text) {
      super(font, x, y, width, height, text);
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.isVisible()) {
         graphics.blit(BlueJournalScreen.WIDGETS, this.getX() - 5, this.getY() - 5, 0, 80, 137, 18);
      }

      super.renderWidget(graphics, mouseX, mouseY, partialTicks);
   }
}
