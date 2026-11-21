package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.HorizoniteForgeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;

public class HorizoniteForgeScreen extends AbstractContainerScreen<HorizoniteForgeMenu> implements RecipeUpdateListener {
   private static final ResourceLocation GUI_TEXTURE = BlueSkies.locate("textures/gui/horizonite_forge.png");
   public final HorizoniteForgeRecipeGui recipeGui = new HorizoniteForgeRecipeGui();
   private boolean widthTooNarrow;

   public HorizoniteForgeScreen(HorizoniteForgeMenu screenContainer, Inventory inv, Component title) {
      super(screenContainer, inv, title);
   }

   public void init() {
      super.init();
      this.widthTooNarrow = this.width < 379;
      this.recipeGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, (RecipeBookMenu)this.menu);
      this.leftPos = this.recipeGui.updateScreenPosition(this.width, this.imageWidth);
      this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 59, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
         this.recipeGui.toggleVisibility();
         this.leftPos = this.recipeGui.updateScreenPosition(this.width, this.imageWidth);
         button.setPosition(this.leftPos + 20, this.height / 2 - 59);
      }));
      this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
   }

   public void containerTick() {
      super.containerTick();
      this.recipeGui.tick();
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      if (this.recipeGui.isVisible() && this.widthTooNarrow) {
         this.renderBackground(graphics, mouseX, mouseY, partialTicks);
         this.recipeGui.render(graphics, mouseX, mouseY, partialTicks);
      } else {
         super.render(graphics, mouseX, mouseY, partialTicks);
         this.recipeGui.render(graphics, mouseX, mouseY, partialTicks);
         this.recipeGui.renderGhostRecipe(graphics, this.getGuiLeft(), this.getGuiTop(), true, partialTicks);
      }

      this.renderTooltip(graphics, mouseX, mouseY);
      this.recipeGui.renderTooltip(graphics, this.getGuiLeft(), this.getGuiTop(), mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
      int left = this.getGuiLeft();
      int top = this.getGuiTop();
      graphics.blit(GUI_TEXTURE, left, top, 0, 0, this.getXSize(), this.getYSize());
      int energy = ((HorizoniteForgeMenu)this.menu).getEnergyLeftScaled();
      int energyStart = (energy - 32) * -1;
      graphics.blit(GUI_TEXTURE, left + 56, top + 38 + energyStart, 176, 31 + energyStart, 16, 31 + energyStart + energy);
      int progression = ((HorizoniteForgeMenu)this.menu).getCookProgressionScaled();
      graphics.blit(GUI_TEXTURE, left + 79, top + 34, 176, 14, progression + 1, 16);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.recipeGui.mouseClicked(mouseX, mouseY, button)) {
         return true;
      } else {
         return this.widthTooNarrow && this.recipeGui.isVisible() ? true : super.mouseClicked(mouseX, mouseY, button);
      }
   }

   protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
      super.slotClicked(slotIn, slotId, mouseButton, type);
      this.recipeGui.slotClicked(slotIn);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return this.recipeGui.keyPressed(keyCode, scanCode, modifiers) ? false : super.keyPressed(keyCode, scanCode, modifiers);
   }

   protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
      boolean flag = mouseX < guiLeftIn || mouseY < guiTopIn || mouseX >= guiLeftIn + this.getXSize() || mouseY >= guiTopIn + this.getYSize();
      return this.recipeGui.hasClickedOutside(mouseX, mouseY, this.getGuiLeft(), this.getGuiTop(), this.getXSize(), this.getYSize(), mouseButton) && flag;
   }

   public boolean charTyped(char codePoint, int modifiers) {
      return this.recipeGui.charTyped(codePoint, modifiers) ? true : super.charTyped(codePoint, modifiers);
   }

   public void recipesUpdated() {
      this.recipeGui.recipesUpdated();
   }

   public RecipeBookComponent getRecipeBookComponent() {
      return this.recipeGui;
   }

   public void removed() {
      super.removed();
   }
}
