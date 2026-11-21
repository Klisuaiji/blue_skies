package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.client.gui.menu.BagOfSpoilsMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BagOfSpoilsScreen extends AbstractContainerScreen<BagOfSpoilsMenu> {
   private static final ResourceLocation HOPPER_LOCATION = ResourceLocation.parse("textures/gui/container/hopper.png");

   public BagOfSpoilsScreen(BagOfSpoilsMenu container, Inventory playerInv, Component containerName) {
      super(container, playerInv, containerName);
      this.imageHeight = 133;
      this.inventoryLabelY = this.imageHeight - 94;
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      graphics.blit(HOPPER_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
   }
}
