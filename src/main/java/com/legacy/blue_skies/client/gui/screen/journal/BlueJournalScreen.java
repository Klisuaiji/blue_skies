package com.legacy.blue_skies.client.gui.screen.journal;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalBackButton;
import com.legacy.blue_skies.client.gui.screen.journal.widgets.JournalPageButton;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.c_to_s.OpenInventoryPacket;
import com.legacy.blue_skies.registries.SkiesSounds;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public abstract class BlueJournalScreen extends Screen {
   public static final ResourceLocation PAGE_LEFT = BlueSkies.locate("textures/gui/journal/page_left.png");
   public static final ResourceLocation PAGE_RIGHT = BlueSkies.locate("textures/gui/journal/page_right.png");
   public static final ResourceLocation WIDGETS = BlueSkies.locate("textures/gui/journal/widgets.png");
   public static final ResourceLocation DISPLAY_FRAME = BlueSkies.locate("textures/gui/journal/display_frame.png");
   private static final Component SCREEN_NAME = Component.empty();
   private static final int[] CODE = new int[]{265, 265, 264, 264, 263, 262, 263, 262, 66, 65};
   private int codeIndex = 0;
   private boolean showCode = false;
   private static int codeX = 0;
   private static int codeY = 0;
   public static int translateX = 0;
   public static int translateY = 0;
   private static int codeMotionX = 1;
   private static int codeMotionY = 1;
   @Nullable
   public static BlueJournalScreen savedScreen = null;
   private static boolean openInventoryOnBackButton = false;
   protected final BlueJournalScreen previousScreen;
   protected int totalPages = 0;
   protected int page = 0;
   protected JournalBackButton backButton;
   protected JournalPageButton prevPageButton;
   protected JournalPageButton nextPageButton;

   public BlueJournalScreen(BlueJournalScreen previousScreen) {
      super(SCREEN_NAME);
      this.previousScreen = previousScreen;
      this.codeIndex = previousScreen != null ? previousScreen.codeIndex : 0;
      this.showCode = previousScreen != null ? previousScreen.showCode : false;
   }

   protected void init() {
      super.init();
      int middleX = this.width / 2;
      int middleY = this.height / 2;
      this.loadData();
      this.backButton = (JournalBackButton)this.addRenderableWidget(new JournalBackButton(middleX - 30, middleY + 86, button -> this.goBack()));
      this.prevPageButton = (JournalPageButton)this.addRenderableWidget(new JournalPageButton(middleX - 155, middleY + 86, false, button -> {
         if (this.page - 2 >= 0) {
            this.setPage(this.page - 2);
         }
      }));
      this.nextPageButton = (JournalPageButton)this.addRenderableWidget(new JournalPageButton(middleX + 113, middleY + 86, true, button -> {
         if (this.page + 2 < this.totalPages) {
            this.setPage(this.page + 2);
         }
      }));
      this.onPageChange();
      this.minecraft.getNarrator().clear();
   }

   protected void loadData() {
   }

   protected void updateNarrationState(NarrationElementOutput narration) {
      if (this.shouldSayUsageMessage()) {
         super.updateNarrationState(narration);
      } else {
         this.updateNarratedWidget(narration);
      }
   }

   protected boolean shouldSayUsageMessage() {
      return false;
   }

   public boolean isMouseOver(double mouseX, double mouseY) {
      if (this.showCode) {
         mouseX = this.offsetMouseX(mouseX);
         mouseY = this.offsetMouseY(mouseY);
      }

      return super.isMouseOver(-mouseX, -mouseY);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
      if (this.showCode) {
         mouseX = this.offsetMouseX(mouseX);
         mouseY = this.offsetMouseY(mouseY);
      }

      return super.mouseClicked(mouseX, mouseY, clickType);
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      PoseStack poseStack = graphics.pose();
      this.renderBackground(graphics, mouseX, mouseY, partialTicks);
      if (this.showCode) {
         poseStack.pushPose();
         translateX = codeX - this.width / 2 + 163;
         translateY = codeY - this.height / 2 + 110;
         poseStack.translate(translateX, translateY, 0.0F);
         mouseX = (int)this.offsetMouseX(mouseX);
         mouseY = (int)this.offsetMouseY(mouseY);
      } else {
         translateX = 0;
         translateY = 0;
      }

      int bookHeight = 220;
      int yOffset = (this.height - bookHeight) / 2;
      graphics.blit(PAGE_LEFT, this.width / 2 - 163, yOffset, 0, 0, 163, bookHeight);
      graphics.blit(PAGE_RIGHT, this.width / 2, yOffset, 0, 0, 153, bookHeight);
      this.renderExtra(graphics, mouseX, mouseY, partialTicks);

      for (Renderable renderable : this.renderables) {
         if (renderable instanceof AbstractWidget widget && !widget.isHoveredOrFocused()) {
            widget.render(graphics, mouseX, mouseY, partialTicks);
         }
      }

      for (Renderable renderable : this.renderables) {
         if (renderable instanceof AbstractWidget widget && widget.isHoveredOrFocused()) {
            widget.render(graphics, mouseX, mouseY, partialTicks);
         }
      }

      if (this.showCode) {
         poseStack.popPose();
      }
   }

   protected void renderExtra(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
   }

   protected void setPage(int page) {
      this.page = page;
      this.onPageChange();
   }

   protected void onPageChange() {
      this.nextPageButton.visible = this.page < this.totalPages - 2;
      this.prevPageButton.visible = this.page > 0;
   }

   public static void open(boolean openInventoryOnBackButton) {
      Minecraft.getInstance().setScreen(savedScreen == null ? new BlueJournalStartScreen() : savedScreen);
      BlueJournalScreen.openInventoryOnBackButton = openInventoryOnBackButton;
   }

   public void onClose() {
      savedScreen = this;
      this.showCode = false;
      this.minecraft.getNarrator().clear();
      super.onClose();
   }

   protected void goBack() {
      Player player = this.minecraft.player;
      if (this.previousScreen == null) {
         savedScreen = null;
         if (openInventoryOnBackButton) {
            this.minecraft.setScreen(new InventoryScreen(player));
            PacketHandler.sendToServer(new OpenInventoryPacket.Vanilla());
            if (this.minecraft.gameMode.isServerControlledInventory()) {
               this.minecraft.player.sendOpenInventory();
            } else {
               this.minecraft.setScreen(new InventoryScreen(player));
            }
         } else {
            this.minecraft.setScreen(null);
         }
      } else {
         this.minecraft.setScreen(this.previousScreen);
         this.previousScreen.showCode = this.showCode;
      }
   }

   public boolean keyPressed(int key, int scancode, int modifier) {
      if (key == CODE[this.codeIndex]) {
         this.codeIndex++;
         if (this.codeIndex >= CODE.length) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SkiesSounds.ENTITY_FLUCTUANT_SPHERE_DISAPPEAR, 0.7F));
            this.showCode = !this.showCode;
            this.setCodeData();
            this.codeIndex = 0;
         }
      } else {
         this.codeIndex = 0;
      }

      switch (key) {
         case 259:
            this.backButton.mouseClicked(this.backButton.getX(), this.backButton.getY(), 0);
            return true;
         case 260:
         case 261:
         default:
            Key input = InputConstants.getKey(key, scancode);
            if (this.minecraft.options.keyInventory.isActiveAndMatches(input)) {
               this.onClose();
               return true;
            }

            return super.keyPressed(key, scancode, modifier);
         case 262:
            this.nextPageButton.mouseClicked(this.nextPageButton.getX(), this.nextPageButton.getY(), 0);
            return true;
         case 263:
            this.prevPageButton.mouseClicked(this.prevPageButton.getX(), this.prevPageButton.getY(), 0);
            return true;
      }
   }

   public void tick() {
      if (this.showCode) {
         if (codeX <= 0 || codeX + 316 >= this.width) {
            codeMotionX *= -1;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BASEDRUM, 1.0F));
         }

         if (codeY <= 0 || codeY + 220 >= this.height) {
            codeMotionY *= -1;
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BASEDRUM, 1.0F));
         }

         if ((codeX <= 0 || codeX + 316 >= this.width) && (codeY <= 0 || codeY + 220 >= this.height)) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
         }

         codeX = codeX + codeMotionX;
         codeY = codeY + codeMotionY;
      }
   }

   public void resize(Minecraft minecraft, int newWidth, int newHeight) {
      super.resize(minecraft, newWidth, newHeight);
      this.setCodeData();
   }

   public boolean isPauseScreen() {
      return false;
   }

   private final void setCodeData() {
      codeMotionX = Math.random() < 0.5 ? 1 : -1;
      codeMotionY = Math.random() < 0.5 ? 1 : -1;
      codeX = this.width / 2 - 163;
      codeY = this.height / 2 - 110;
   }

   private final double offsetMouseX(double mouseX) {
      return mouseX - (codeX - this.width / 2 + 163);
   }

   private final double offsetMouseY(double mouseY) {
      return mouseY - (codeY - this.height / 2 + 110);
   }
}
