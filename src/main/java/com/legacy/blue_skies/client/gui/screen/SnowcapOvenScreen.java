package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.SnowcapOvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class SnowcapOvenScreen extends AbstractFurnaceScreen<SnowcapOvenMenu> {
   private static final ResourceLocation GUI_TEXTURE = BlueSkies.locate("textures/gui/snowcap_oven.png");

   public SnowcapOvenScreen(SnowcapOvenMenu container, Inventory playerInv, Component title) {
      super(container, new SnowcapOvenRecipeGui(), playerInv, title, GUI_TEXTURE, GUI_TEXTURE, GUI_TEXTURE);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
      int left = this.leftPos;
      int top = this.topPos;
      graphics.blit(GUI_TEXTURE, left, top, 0, 0, this.imageWidth, this.imageHeight);
      if (((SnowcapOvenMenu)this.menu).isLit()) {
         int burnLeft = Mth.ceil(((SnowcapOvenMenu)this.menu).getLitProgress() * 13.0F) + 1;
         graphics.blit(GUI_TEXTURE, left + 56, top + 36 + 12 - burnLeft, 176, 12 - burnLeft, 14, burnLeft + 1);
      }

      int progress = Mth.ceil(((SnowcapOvenMenu)this.menu).getBurnProgress() * 24.0F);
      graphics.blit(GUI_TEXTURE, left + 79, top + 20, 176, 14, progress + 1, 16);
   }
}
