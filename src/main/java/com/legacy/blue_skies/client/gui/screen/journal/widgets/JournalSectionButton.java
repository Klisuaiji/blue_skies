package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.gui.screen.journal.BlueJournalScreen;
import com.legacy.blue_skies.data.objects.journal.JournalSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public class JournalSectionButton extends JournalButton implements Comparable<JournalSectionButton> {
   public final JournalSection section;
   public boolean forced = false;

   public JournalSectionButton(int x, int y, JournalSection section, OnPress pressAction) {
      super(x, y, 48, 16, BlueSkiesAssets.JOURNAL_LANG.getTranslation(section.displayText), pressAction);
      this.section = section;
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         Minecraft mc = Minecraft.getInstance();
         int x = 0;
         int y = 0;
         if (this.isHoveredOrFocused() || this.forced) {
            y += 16;
         }

         graphics.blit(this.section.texture, this.getX(), this.getY(), x, y, 48, 16, 64, 64);
         if (this.isHovered) {
            int startX = 56;
            int height = 18;
            int x1 = mouseX - 10;
            int y1 = mouseY - 14;
            graphics.blit(BlueJournalScreen.WIDGETS, x1 - 2, y1, 0, startX, 3, height);

            int i;
            for (i = 0; i < mc.font.width(this.getMessage()) + 8; i += 2) {
               graphics.blit(BlueJournalScreen.WIDGETS, x1 + i, y1, 4, startX, 2, height);
            }

            graphics.blit(BlueJournalScreen.WIDGETS, x1 + i, y1, 7, startX, 3, height);
            graphics.drawString(mc.font, this.getMessage(), mouseX - 5, mouseY - 9, 0, false);
         }
      }
   }

   public int compareTo(JournalSectionButton o) {
      return o.section instanceof JournalSection.Search ? 1 : this.section.displayText.compareTo(o.section.displayText);
   }

   public void playDownSound(SoundManager handler) {
      handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }
}
