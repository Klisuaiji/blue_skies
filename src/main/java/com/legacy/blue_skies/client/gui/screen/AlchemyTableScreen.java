package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.AlchemyTableMenu;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.c_to_s.HandleScreenClickPacket;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/gui/alchemy_table.png");
   private float sliderProgress;
   private boolean clickedOnSroll;
   private int recipeIndexOffset;
   private boolean hasItemsInInputSlot;

   public AlchemyTableScreen(AlchemyTableMenu screenContainer, Inventory inv, Component title) {
      super(screenContainer, inv, title);
      screenContainer.setInventoryUpdateListener(this::onInventoryUpdate);
      this.titleLabelY--;
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
      int left = this.leftPos;
      int top = this.topPos;
      graphics.blit(TEXTURE, left, top, 0, 0, this.imageWidth, this.imageHeight);
      int sliderProgress = (int)(41.0F * this.sliderProgress);
      graphics.blit(TEXTURE, left + 119, top + 15 + sliderProgress, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
      int leftOffset = this.leftPos + 52;
      int topOffset = this.topPos + 14;
      int recipeIndexOffset = this.recipeIndexOffset + 12;
      this.renderRecipeBackground(graphics, x, y, leftOffset, topOffset, recipeIndexOffset);
      this.drawRecipesItems(graphics, leftOffset, topOffset, recipeIndexOffset);
   }

   protected void renderTooltip(GuiGraphics graphics, int hoverX, int hoverY) {
      super.renderTooltip(graphics, hoverX, hoverY);
      if (this.hasItemsInInputSlot) {
         int guiLeft = this.leftPos + 52;
         int guiTop = this.topPos + 14;
         int recipeIndexOffset = this.recipeIndexOffset + 12;
         List<Item> outputs = ((AlchemyTableMenu)this.menu).getOutputItems();

         for (int i = this.recipeIndexOffset; i < recipeIndexOffset && i < ((AlchemyTableMenu)this.menu).getOutputCount(); i++) {
            int index = i - this.recipeIndexOffset;
            int x = guiLeft + index % 4 * 16;
            int y = guiTop + index / 4 * 18 + 2;
            if (hoverX >= x && hoverX < x + 16 && hoverY >= y && hoverY < y + 18) {
               graphics.renderTooltip(this.font, new ItemStack((ItemLike)outputs.get(i)), hoverX, hoverY);
            }
         }
      }
   }

   private void renderRecipeBackground(GuiGraphics graphics, int x, int y, int leftOffset, int topOffset, int recipeIndexOffset) {
      for (int i = this.recipeIndexOffset; i < recipeIndexOffset && i < ((AlchemyTableMenu)this.menu).getOutputCount(); i++) {
         int recipeIndex = i - this.recipeIndexOffset;
         int xPos = leftOffset + recipeIndex % 4 * 16;
         int dividedIndex = recipeIndex / 4;
         int yPos = topOffset + dividedIndex * 18 + 2;
         int vOffset = this.imageHeight;
         if (i == ((AlchemyTableMenu)this.menu).getSelectedItem()) {
            vOffset += 18;
         } else if (x >= xPos && y >= yPos && x < xPos + 16 && y < yPos + 18) {
            vOffset += 36;
         }

         graphics.blit(TEXTURE, xPos, yPos - 1, 0, vOffset, 16, 18);
      }
   }

   private void drawRecipesItems(GuiGraphics graphics, int left, int top, int recipeIndexOffsetMax) {
      List<Item> outputs = ((AlchemyTableMenu)this.menu).getOutputItems();

      for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < ((AlchemyTableMenu)this.menu).getOutputCount(); i++) {
         int recipeIndex = i - this.recipeIndexOffset;
         int xPos = left + recipeIndex % 4 * 16;
         int recipe = recipeIndex / 4;
         int yPos = top + recipe * 18 + 2;
         graphics.renderItem(new ItemStack((ItemLike)outputs.get(i)), xPos, yPos);
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.clickedOnSroll = false;
      if (this.hasItemsInInputSlot) {
         int leftOffset = this.leftPos + 52;
         int topOffset = this.topPos + 14;
         int recipeIndexOffset = this.recipeIndexOffset + 12;

         for (int clickedIndex = this.recipeIndexOffset; clickedIndex < recipeIndexOffset; clickedIndex++) {
            int offsetIndex = clickedIndex - this.recipeIndexOffset;
            double xPos = mouseX - (leftOffset + offsetIndex % 4 * 16);
            double yPos = mouseY - (topOffset + offsetIndex / 4 * 18);
            if (xPos >= 0.0 && yPos >= 0.0 && xPos < 16.0 && yPos < 18.0 && ((AlchemyTableMenu)this.menu).clickMenuButton(this.minecraft.player, clickedIndex)) {
               Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
               PacketHandler.sendToServer(new HandleScreenClickPacket(((AlchemyTableMenu)this.menu).containerId, clickedIndex));
               return true;
            }
         }

         leftOffset = this.leftPos + 119;
         topOffset = this.topPos + 9;
         if (mouseX >= leftOffset && mouseX < leftOffset + 12 && mouseY >= topOffset && mouseY < topOffset + 54) {
            this.clickedOnSroll = true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
      if (this.clickedOnSroll && this.canScroll()) {
         int topOffset = this.topPos + 14;
         int lowerBounds = topOffset + 54;
         this.sliderProgress = ((float)mouseY - topOffset - 7.5F) / (lowerBounds - topOffset - 15.0F);
         this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
         this.recipeIndexOffset = (int)(this.sliderProgress * this.getHiddenRows() + 0.5) * 4;
         return true;
      } else {
         return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
      if (this.canScroll()) {
         int hiddenRows = this.getHiddenRows();
         this.sliderProgress = (float)(this.sliderProgress - deltaY / hiddenRows);
         this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
         this.recipeIndexOffset = (int)(this.sliderProgress * hiddenRows + 0.5) * 4;
      }

      return true;
   }

   private boolean canScroll() {
      return this.hasItemsInInputSlot && ((AlchemyTableMenu)this.menu).getOutputCount() > 12;
   }

   protected int getHiddenRows() {
      return (((AlchemyTableMenu)this.menu).getOutputCount() + 4 - 1) / 4 - 3;
   }

   private void onInventoryUpdate() {
      this.hasItemsInInputSlot = ((AlchemyTableMenu)this.menu).hasItemsinInputSlot();
      if (!this.hasItemsInInputSlot) {
         this.sliderProgress = 0.0F;
         this.recipeIndexOffset = 0;
      }
   }
}
