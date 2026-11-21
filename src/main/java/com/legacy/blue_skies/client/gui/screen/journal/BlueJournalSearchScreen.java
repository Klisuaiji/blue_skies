package com.legacy.blue_skies.client.gui.screen.journal;

import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalEntryButton;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalSearchTextField;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalSectionButton;
import com.legacy.blue_skies.data.objects.journal.JournalEntry;
import com.legacy.blue_skies.data.objects.journal.JournalSection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;

public class BlueJournalSearchScreen extends BlueJournalSectionScreen {
   private EditBox searchBox;

   public BlueJournalSearchScreen(BlueJournalScreen previousScreen, JournalSection section, JournalSectionButton sectionButton) {
      super(previousScreen, section, sectionButton);
   }

   @Override
   public void tick() {
      super.tick();
   }

   @Override
   protected void loadData() {
      String oldText = this.searchBox == null ? "" : this.searchBox.getValue();
      this.searchBox = new JournalSearchTextField(
         this.font, this.width / 2 - 142, this.height / 2 - 91, 125, 16, BlueSkiesAssets.JOURNAL_LANG.getTranslation("blue_skies.section.search")
      );
      this.addRenderableWidget(this.searchBox);
      this.searchBox.setResponder(s -> {
         this.injectEntryButtons(this.getEntryButtons());
         this.setPage(0);
      });
      this.searchBox.setTextColor(-1);
      this.searchBox.setBordered(false);
      this.searchBox.setMaxLength(1000);
      super.loadData();
      if (!oldText.isEmpty()) {
         this.searchBox.setValue(oldText);
      }
   }

   @Override
   protected void renderExtra(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      super.renderExtra(graphics, mouseX, mouseY, partialTicks);
      this.searchBox.render(graphics, mouseX, mouseY, partialTicks);
   }

   @Override
   public boolean keyPressed(int key, int scancode, int modifier) {
      if (this.searchBox.isFocused() && key != 258) {
         if (key != 256) {
            return !this.searchBox.keyPressed(key, scancode, modifier) && !this.searchBox.canConsumeInput() ? super.keyPressed(key, scancode, modifier) : true;
         }

         this.minecraft.player.closeContainer();
      }

      return super.keyPressed(key, scancode, modifier);
   }

   @Override
   protected boolean isCorrectSection(JournalEntry entry) {
      return true;
   }

   @Override
   protected List<JournalEntryButton> getEntryButtons() {
      List<JournalEntryButton> buttons = super.getEntryButtons();

      for (int i = 0; i < buttons.size(); i += 18) {
         buttons.add(i, null);
      }

      return buttons;
   }

   @Override
   protected void injectEntryButtons(List<JournalEntryButton> buttons) {
      super.injectEntryButtons(buttons);
   }

   @Override
   protected boolean testEntry(JournalEntry entry) {
      return super.testEntry(entry)
         && BlueSkiesAssets.JOURNAL_LANG.getTranslation(entry.title).getString().toLowerCase().contains(this.searchBox.getValue().toLowerCase());
   }
}
