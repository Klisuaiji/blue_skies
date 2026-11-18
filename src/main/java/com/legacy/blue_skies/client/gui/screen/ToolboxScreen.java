package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.ToolboxMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ToolboxScreen extends ItemCombinerScreen<ToolboxMenu> {
   private static final ResourceLocation GUI_TEXTURE = BlueSkies.locate("textures/gui/tool_box.png");

   public ToolboxScreen(ToolboxMenu container, Inventory playerInventory, Component title) {
      super(container, playerInventory, title, GUI_TEXTURE);
      this.titleLabelX = 60;
      this.titleLabelY = 18;
   }

   protected void renderLabels(GuiGraphics graphics, int x, int y) {
      RenderSystem.disableBlend();
      super.renderLabels(graphics, x, y);
   }

   protected void renderErrorIcon(GuiGraphics graphics, int x, int y) {
      if ((((ToolboxMenu)this.menu).getSlot(0).hasItem() || ((ToolboxMenu)this.menu).getSlot(1).hasItem())
         && !((ToolboxMenu)this.menu).getSlot(((ToolboxMenu)this.menu).getResultSlot()).hasItem()) {
         graphics.blit(GUI_TEXTURE, x + 99, y + 45, this.imageWidth, 0, 28, 21);
      }
   }
}
