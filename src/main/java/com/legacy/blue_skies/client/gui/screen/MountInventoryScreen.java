package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.client.gui.menu.MountInventoryMenu;
import com.legacy.blue_skies.entities.passive.CrystalCamelEntity;
import com.legacy.blue_skies.entities.util.base.SkiesMountEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MountInventoryScreen extends AbstractContainerScreen<MountInventoryMenu> {
   private static final ResourceLocation HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");
   private static final ResourceLocation CHEST_SLOTS_SPRITE = new ResourceLocation("container/horse/chest_slots");
   private static final ResourceLocation SADDLE_SLOT_SPRITE = new ResourceLocation("container/horse/saddle_slot");
   private final SkiesMountEntity mountEntity;

   public MountInventoryScreen(MountInventoryMenu container, Inventory playerInv, Component text) {
      super(container, playerInv, container.mount.getDisplayName());
      this.mountEntity = container.mount;
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      graphics.blit(HORSE_GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);
      if (this.mountEntity.isTamed()) {
         graphics.blitSprite(SADDLE_SLOT_SPRITE, i + 7, j + 35 - 18, 18, 18);
      }

      if (this.mountEntity instanceof CrystalCamelEntity camel && camel.hasChest()) {
         graphics.blitSprite(CHEST_SLOTS_SPRITE, 90, 54, 0, 0, i + 79, j + 17, 90, 54);
      }

      int xOff = i + 26;
      int yOff = j + 18;
      InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, xOff, yOff, xOff + 52, yOff + 52, 17, 0.25F, x, y, this.mountEntity);
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
   }
}
