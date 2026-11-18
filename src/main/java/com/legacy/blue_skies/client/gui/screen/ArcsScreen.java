package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.ArcsMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArcsScreen extends EffectRenderingInventoryScreen<ArcsMenu> {
   private static final ResourceLocation ARC_INVENTORY_BACKGROUND = BlueSkies.locate("textures/gui/arc_inventory.png");
   private float oldMouseX;
   private float oldMouseY;

   public ArcsScreen(ArcsMenu container, Inventory playerInv, Component text) {
      super(container, playerInv, text);
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      this.renderBg(graphics, partialTicks, mouseX, mouseY);
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
      this.oldMouseX = mouseX;
      this.oldMouseY = mouseY;
   }

   protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
      int fontWidth = this.font.width(this.title.getString());
      graphics.drawString(this.font, this.title, 117 - fontWidth / 2, 6, 4210752, false);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
      int i = this.getGuiLeft();
      int j = this.getGuiTop();
      graphics.blit(ARC_INVENTORY_BACKGROUND, i, j, 0, 0, this.getXSize(), this.getYSize());
      int xOff = i + 8;
      int yOff = j + 8;
      InventoryScreen.renderEntityInInventoryFollowsMouse(
         graphics, xOff, yOff, xOff + 52, yOff + 70, 30, 0.0625F, this.oldMouseX, this.oldMouseY, this.minecraft.player
      );
   }
}
