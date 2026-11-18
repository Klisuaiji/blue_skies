package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.FoodPrepMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FoodPrepScreen extends AbstractContainerScreen<FoodPrepMenu> {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/gui/food_prep_table.png");

   public FoodPrepScreen(FoodPrepMenu container, Inventory playerInventory, Component title) {
      super(container, playerInventory, title);
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(graphics, mouseX, mouseY, partialTicks);
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
      int posX = (this.width - this.imageWidth) / 2;
      int posY = (this.height - this.imageHeight) / 2;
      graphics.blit(TEXTURE, posX, posY, 0, 0, this.imageWidth, this.imageHeight);
      if (!((FoodPrepMenu)this.menu).getSlot(5).hasItem()) {
         graphics.blit(TEXTURE, posX + 82, posY + 36, 176, 0, 13, 15);
      }
   }
}
