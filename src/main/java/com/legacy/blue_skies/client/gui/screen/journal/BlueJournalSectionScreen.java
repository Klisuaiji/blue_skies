package com.legacy.blue_skies.client.gui.screen.journal;

import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalEntryButton;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalSectionButton;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.journal.JournalEntry;
import com.legacy.blue_skies.data.objects.journal.JournalRequirement;
import com.legacy.blue_skies.data.objects.journal.JournalSection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;

public class BlueJournalSectionScreen extends BlueJournalScreen {
   protected final List<List<JournalEntryButton>> entryButtons = new ArrayList<>();
   public final JournalSection section;
   public final JournalSectionButton sectionButton;

   public BlueJournalSectionScreen(@Nullable BlueJournalScreen previousScreen, JournalSection section, JournalSectionButton sectionButton) {
      super(previousScreen);
      this.section = section;
      this.sectionButton = sectionButton;
   }

   @Override
   protected void loadData() {
      this.injectEntryButtons(this.getEntryButtons());
      JournalSectionButton currentSectionButton = new JournalSectionButton(
         this.width / 2 + 145, this.height / 2 - 100, this.sectionButton.section, button -> this.goBack()
      );
      currentSectionButton.forced = true;
      this.addRenderableWidget(currentSectionButton);
   }

   protected boolean isCorrectSection(JournalEntry entry) {
      return entry.section.equals(this.section.name);
   }

   protected boolean testEntry(JournalEntry entry) {
      if (entry.requirement != null) {
         LocalPlayer player = this.minecraft.player;
         JournalRequirement requirement = BlueSkiesData.JOURNAL_REQUIREMENTS.getValue(entry.requirement);
         if (requirement != null) {
            boolean advancementDone = false;
            if (requirement.advancement != null) {
               ClientAdvancements advancements = player.connection.getAdvancements();
               AdvancementHolder advancement = advancements.get(requirement.advancement);
               if (advancement != null) {
                  advancementDone = ((AdvancementProgress)advancements.progress.get(advancement)).isDone();
               }
            }

            int everbrightProg = SkiesPlayer.getIfPresent(player, s -> Integer.valueOf(s.getBrightProgression()), () -> -1);
            int everdawnProg = SkiesPlayer.getIfPresent(player, s -> Integer.valueOf(s.getDawnProgression()), () -> -1);
            return (advancementDone || requirement.advancement == null)
               && (everbrightProg >= requirement.everbrightProgression || requirement.everbrightProgression < 0)
               && (everdawnProg >= requirement.everdawnProgression || requirement.everdawnProgression < 0);
         }
      }

      return true;
   }

   protected List<JournalEntryButton> getEntryButtons() {
      List<JournalEntryButton> buttons = new ArrayList<>();
      BlueSkiesData.JOURNAL_ENTRIES.getData().forEach((name, entry) -> {
         if (this.isCorrectSection(entry) && this.testEntry(entry)) {
            buttons.add(new JournalEntryButton(0, 0, entry, button -> this.minecraft.setScreen(new BlueJournalEntryScreen(this, entry, this.sectionButton))));
         }
      });
      buttons.sort(JournalEntryButton::compareTo);
      return buttons;
   }

   protected void injectEntryButtons(List<JournalEntryButton> buttons) {
      this.entryButtons.clear();
      this.renderables.removeIf(bx -> bx instanceof JournalEntryButton);
      this.children().removeIf(bx -> bx instanceof JournalEntryButton);
      int buttonsPerPage = 9;
      this.totalPages = buttons.size() / buttonsPerPage + (buttons.size() % buttonsPerPage == 0 ? 0 : 1);

      for (int i = 0; i < this.totalPages; i++) {
         this.entryButtons.add(buttons.subList(i * buttonsPerPage, Math.min(buttonsPerPage * (i + 1), buttons.size())));
      }

      for (int i = 0; i < this.entryButtons.size(); i++) {
         int x = this.width / 2 + (i % 2 == 0 ? -145 : 4);
         int y = this.height / 2 - 115;

         for (JournalEntryButton b : this.entryButtons.get(i)) {
            y += 20;
            if (b != null) {
               b.setX(x);
               b.setY(y);
               this.addRenderableWidget(b);
            }
         }
      }
   }

   @Override
   protected void onPageChange() {
      super.onPageChange();

      for (int i = 0; i < this.entryButtons.size(); i++) {
         boolean visible = i == this.page || i == this.page + 1;
         this.entryButtons.get(i).forEach(b -> {
            if (b != null) {
               b.visible = visible;
            }
         });
      }
   }
}
