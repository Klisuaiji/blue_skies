package com.legacy.blue_skies.client.gui.screen.journal;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalNarratorButton;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalSectionButton;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.objects.journal.JournalSection;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class BlueJournalStartScreen extends BlueJournalScreen {
   private static final String TAGLINE = "blue_skies.blue_journal.tagline";
   private static final String WELCOME = "blue_skies.blue_journal.welcome";
   private final List<List<JournalSectionButton>> sectionButtons = new ArrayList<>();
   private final List<FormattedText> taglineLines = new ArrayList<>();
   private final List<FormattedText> welcomeLines = new ArrayList<>();
   private boolean sayUsageMessage = true;

   public BlueJournalStartScreen() {
      super(null);
   }

   @Override
   protected void loadData() {
      this.sectionButtons.clear();
      int x = this.width / 2 + 145;
      List<JournalSectionButton> buttons = new ArrayList<>();
      buttons.add(
         new JournalSectionButton(
            x,
            0,
            new JournalSection.Search(),
            button -> this.minecraft.setScreen(new BlueJournalSearchScreen(this, ((JournalSectionButton)button).section, (JournalSectionButton)button))
         )
      );
      BlueSkiesData.JOURNAL_SECTIONS
         .getData()
         .forEach(
            (name, section) -> buttons.add(
               new JournalSectionButton(
                  x, 0, section, button -> Minecraft.getInstance().setScreen(new BlueJournalSectionScreen(this, section, (JournalSectionButton)button))
               )
            )
         );
      buttons.sort(JournalSectionButton::compareTo);
      int buttonsPerPage = 10;
      this.totalPages = buttons.size() / buttonsPerPage + (buttons.size() % buttonsPerPage == 0 ? 0 : 1);

      for (int i = 0; i < this.totalPages; i++) {
         this.sectionButtons.add(buttons.subList(i * buttonsPerPage, Math.min(buttonsPerPage * (i + 1), buttons.size())));
      }

      this.totalPages *= 2;

      for (List<JournalSectionButton> btns : this.sectionButtons) {
         int y = this.height / 2 - 120;

         for (JournalSectionButton b : btns) {
            y += 20;
            b.setY(y);
            this.addRenderableWidget(b);
         }
      }

      this.taglineLines.clear();
      this.welcomeLines.clear();
      int lineWidth = 135;
      this.taglineLines
         .addAll(this.font.getSplitter().splitLines(BlueSkiesAssets.JOURNAL_LANG.getTranslation("blue_skies.blue_journal.tagline"), lineWidth, Style.EMPTY));
      this.welcomeLines
         .addAll(this.font.getSplitter().splitLines(BlueSkiesAssets.JOURNAL_LANG.getTranslation("blue_skies.blue_journal.welcome"), lineWidth, Style.EMPTY));
      this.addRenderableWidget(
         new JournalNarratorButton(
            this.width / 2 - 120, this.height / 2 + 84, button -> JournalNarratorButton.narrate(Arrays.asList(this.taglineLines, this.welcomeLines))
         )
      );
   }

   @Override
   protected boolean shouldSayUsageMessage() {
      if (this.sayUsageMessage) {
         this.sayUsageMessage = false;
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void renderExtra(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      this.renderLogo(graphics);
      this.renderText(graphics);
   }

   protected void renderLogo(GuiGraphics graphics) {
      PoseStack poseStack = graphics.pose();
      int size = 128;
      int x = this.width / 2 - 145;
      int y = this.height / 2 - 120;
      poseStack.pushPose();
      RenderSystem.enableBlend();
      graphics.blit(BlueSkies.locate("textures/gui/journal/logo_backdrop.png"), x, y, 0.0F, 0.0F, size, size, size, size);
      poseStack.popPose();
      graphics.blit(BlueSkies.locate("textures/gui/journal/logo.png"), x, y, 0.0F, 0.0F, size, size, size, size);
   }

   protected void renderText(GuiGraphics graphics) {
      int middleX = this.width / 2;
      int middleY = this.height / 2;
      int offset = 0;

      for (FormattedText t : this.taglineLines) {
         Font var10001 = this.font;
         MutableComponent var10002 = Component.literal(t.getString());
         int var10003 = middleX - 81 - this.font.width(t) / 2;
         int var10004 = middleY - 20;
         offset += 11;
         graphics.drawString(var10001, var10002, var10003, var10004 + offset, 0, false);
      }

      int var7 = 0;

      for (FormattedText t : this.welcomeLines) {
         Font var10 = this.font;
         MutableComponent var11 = Component.literal(t.getString());
         int var12 = middleX + 4;
         int var13 = middleY - 105;
         var7 += 11;
         graphics.drawString(var10, var11, var12, var13 + var7, 0, false);
      }
   }

   @Override
   protected void onPageChange() {
      super.onPageChange();

      for (int i = 0; i < this.sectionButtons.size(); i++) {
         boolean visible = i == this.page / 2;
         this.sectionButtons.get(i).forEach(b -> b.visible = visible);
      }
   }
}
