package com.legacy.blue_skies.client.gui.screen.journal;

import com.google.common.collect.Lists;
import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalNarratorButton;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalSectionButton;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.journal.JournalEntry;
import com.legacy.blue_skies.data.objects.journal.JournalRequirement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class BlueJournalEntryScreen extends BlueJournalScreen {
   private final JournalEntry entry;
   public final JournalSectionButton sectionButton;
   private List<List<FormattedText>> pagesOfLines = new ArrayList<>();
   private JournalEntry.Lore lore;

   public BlueJournalEntryScreen(@Nullable BlueJournalScreen previousScreen, JournalEntry entry, JournalSectionButton sectionButton) {
      super(previousScreen);
      this.entry = entry;
      this.sectionButton = sectionButton;
   }

   @Override
   protected void loadData() {
      this.pagesOfLines.clear();
      int lineWidth = 135;
      this.lore = this.getLore();
      List<FormattedText> lines = this.font
         .getSplitter()
         .splitLines(Component.literal("\n\n").append(BlueSkiesAssets.JOURNAL_LANG.getTranslation(this.lore.text)), lineWidth, Style.EMPTY);
      int linesPerPage = 16;
      this.totalPages = lines.size() / linesPerPage + (lines.size() % linesPerPage == 0 ? 0 : 1);

      for (int i = 0; i < this.totalPages; i++) {
         this.pagesOfLines.add(lines.subList(i * linesPerPage, Math.min(linesPerPage * (i + 1), lines.size())));
      }

      if (this.lore.display != null) {
         this.totalPages++;
         if (this.pagesOfLines.size() >= 1) {
            this.pagesOfLines.add(1, new ArrayList<>());
         }

         this.lore.display.init(this.minecraft);
      }

      JournalSectionButton currentSectionButton = new JournalSectionButton(
         this.width / 2 + 145, this.height / 2 - 100, this.sectionButton.section, button -> this.previousScreen.goBack()
      );
      currentSectionButton.forced = true;
      this.addRenderableWidget(currentSectionButton);
      this.addRenderableWidget(new JournalNarratorButton(this.width / 2 - 120, this.height / 2 + 84, button -> {
         List<FormattedText> text = new ArrayList<>();

         for (List<FormattedText> page : this.pagesOfLines) {
            for (FormattedText line : page) {
               text.add(line);
            }
         }

         JournalNarratorButton.narrate(Arrays.asList(text));
      }));
   }

   protected JournalEntry.Lore getLore() {
      if (this.entry.lores.size() == 1) {
         return this.entry.lores.get(0);
      } else {
         this.entry.lores.sort((p1, p2) -> {
            JournalRequirement r1 = BlueSkiesData.JOURNAL_REQUIREMENTS.getValue(p1.requirement);
            JournalRequirement r2 = BlueSkiesData.JOURNAL_REQUIREMENTS.getValue(p2.requirement);
            if (r1 != null && r2 != null) {
               int a1 = r1.advancement == null ? 0 : 1;
               int a2 = r2.advancement == null ? 0 : 1;
               int b1 = r1.everbrightProgression < 0 ? 0 : r1.everbrightProgression;
               int b2 = r2.everbrightProgression < 0 ? 0 : r2.everbrightProgression;
               int d1 = r1.everdawnProgression < 0 ? 0 : r1.everdawnProgression;
               int d2 = r2.everdawnProgression < 0 ? 0 : r2.everdawnProgression;
               int total1 = a1 + b1 + d1;
               int total2 = a2 + b2 + d2;
               return Integer.compare(total1, total2);
            } else {
               return 0;
            }
         });

         for (JournalEntry.Lore lorePair : Lists.reverse(this.entry.lores)) {
            if (lorePair.requirement == null) {
               return lorePair;
            }

            LocalPlayer player = this.minecraft.player;
            JournalRequirement requirement = BlueSkiesData.JOURNAL_REQUIREMENTS.getValue(lorePair.requirement);
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
               if ((advancementDone || requirement.advancement == null)
                  && (everbrightProg >= requirement.everbrightProgression || requirement.everbrightProgression < 0)
                  && (everdawnProg >= requirement.everdawnProgression || requirement.everdawnProgression < 0)) {
                  return lorePair;
               }
            }
         }

         return this.entry.lores.get(this.entry.lores.size() - 1);
      }
   }

   @Override
   protected void renderExtra(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.page == 0) {
         this.renderTitle(graphics);
         if (this.lore.display != null) {
            this.renderDisplay(graphics, partialTicks);
         }
      }

      this.renderText(graphics, this.page);
   }

   protected void renderTitle(GuiGraphics graphics) {
      Component title = BlueSkiesAssets.JOURNAL_LANG.getTranslation(this.entry.title);
      int centerX = this.width / 2;
      int centerY = this.height / 2;
      graphics.drawString(this.font, title, centerX - this.font.width(title) / 2 - 80, centerY - 90, 0, false);
      graphics.blit(BlueJournalScreen.WIDGETS, centerX - 140, centerY - 80, 0, 74, 122, 6);
   }

   protected void renderText(GuiGraphics graphics, int page) {
      int middleX = this.width / 2;
      int middleY = this.height / 2;

      for (int i = 0; i < 2 && page + i < this.pagesOfLines.size(); i++) {
         int offset = 0;

         for (FormattedText t : this.pagesOfLines.get(page + i)) {
            Font var10001 = this.font;
            MutableComponent var10002 = Component.literal(t.getString());
            int var10003 = middleX + (i == 0 ? -145 : 4);
            int var10004 = middleY - 105;
            offset += 11;
            graphics.drawString(var10001, var10002, var10003, var10004 + offset, 0, false);
         }
      }
   }

   protected void renderDisplay(GuiGraphics graphics, float partialTicks) {
      int bookHeight = 220;
      graphics.blit(BlueJournalScreen.DISPLAY_FRAME, this.width / 2, (this.height - bookHeight) / 2, 0, 0, 153, bookHeight);
      this.lore.display.render(graphics, this.minecraft, this.width, this.height, partialTicks);
   }
}
