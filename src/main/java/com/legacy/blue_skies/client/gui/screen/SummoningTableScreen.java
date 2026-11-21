package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.SummoningTableMenu;
import com.legacy.blue_skies.items.tools.weapons.SummoningTomeItem;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.c_to_s.ChangeTomePacket;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SummoningTableScreen extends AbstractContainerScreen<SummoningTableMenu> {
   private static final ResourceLocation SUMMONING_TABLE_GUI_TEXTURE = BlueSkies.locate("textures/gui/summoning_table.png");
   private Button sphereButton;
   private Button golemButton;

   public SummoningTableScreen(SummoningTableMenu container, Inventory playerInventory, Component textComponent) {
      super(container, playerInventory, textComponent);
   }

   protected void init() {
      super.init();
      this.sphereButton = (Button)this.addRenderableWidget(
         Button.builder(Component.translatable("gui.blue_skies.block.summoning_table.fluctuant_sphere"), button -> this.onTomeButtonPressed(button, false))
            .bounds(this.width / 2 - 25, this.height / 2 - 60, 100, 20)
            .build()
      );
      this.golemButton = (Button)this.addRenderableWidget(
         Button.builder(Component.translatable("gui.blue_skies.block.summoning_table.artificial_golem"), button -> this.onTomeButtonPressed(button, true))
            .bounds(this.width / 2 - 25, this.height / 2 - 35, 100, 20)
            .build()
      );
      this.sphereButton.visible = true;
      this.golemButton.visible = true;
      this.sphereButton.active = false;
      this.golemButton.active = false;
   }

   public void containerTick() {
      super.containerTick();
      ItemStack bookStack = ((SummoningTableMenu)this.menu).getSlot(0).getItem();
      ItemStack soulStack = ((SummoningTableMenu)this.menu).getSlot(1).getItem();
      this.sphereButton.active = !bookStack.isEmpty()
         && !soulStack.isEmpty()
         && bookStack.getItem() instanceof SummoningTomeItem
         && SummoningTomeItem.canSummonGolems(bookStack);
      this.golemButton.active = !bookStack.isEmpty()
         && !soulStack.isEmpty()
         && bookStack.getItem() instanceof SummoningTomeItem
         && !SummoningTomeItem.canSummonGolems(bookStack);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return super.mouseClicked(mouseX, mouseY, button);
   }

   protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
      Lighting.setupForFlatItems();
      int i = (this.width - this.imageWidth) / 2;
      int j = (this.height - this.imageHeight) / 2;
      graphics.blit(SUMMONING_TABLE_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      partialTicks = this.minecraft.getFrameTime();
      this.renderBackground(graphics, mouseX, mouseY, partialTicks);
      super.render(graphics, mouseX, mouseY, partialTicks);
      this.renderTooltip(graphics, mouseX, mouseY);
   }

   public void onTomeButtonPressed(Button button, boolean enablingGolems) {
      PacketHandler.sendToServer(new ChangeTomePacket(((SummoningTableMenu)this.menu).containerId, enablingGolems));
   }
}
