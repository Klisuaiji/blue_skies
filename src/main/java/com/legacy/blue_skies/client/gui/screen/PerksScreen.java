package com.legacy.blue_skies.client.gui.screen;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.SupporterPetEntity;
import com.legacy.blue_skies.network.PacketHandler;
import com.legacy.blue_skies.network.c_to_s.SpawnSupporterPetPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class PerksScreen extends Screen {
   private static final Minecraft mc = Minecraft.getInstance();
   private static final ResourceLocation BACKGROUND = BlueSkies.locate("textures/gui/empty.png");
   private static final Component SCREEN_NAME = Component.literal("Perks");
   private byte petID = 0;
   private boolean petAudible = false;
   private Button idButton;
   private Button soundButton;
   private Button enableButton;
   private Button disableButton;
   private Button styleButton;
   private EditBox nameEdit;
   private SupporterPetEntity petEntity;
   private ChatFormatting style = ChatFormatting.RESET;
   private int styleIndex = 15;

   public PerksScreen() {
      super(SCREEN_NAME);
   }

   public void tick() {
   }

   protected void init() {
      MutableComponent soundText = Component.translatable("gui.blue_skies.supporter_inventory.audible").append(": " + this.petAudible);
      MutableComponent typeText = Component.translatable("gui.blue_skies.supporter_inventory.type")
         .append(": ")
         .append(Component.translatable("gui.blue_skies.supporter_inventory.type.%s".replace("%s", this.petID + "")));
      this.idButton = (Button)this.addRenderableWidget(
         Button.builder(
               typeText,
               button -> {
                  if (this.petID < 2) {
                     this.petID++;
                  } else {
                     this.petID = 0;
                  }

                  this.idButton
                     .setMessage(
                        Component.translatable("gui.blue_skies.supporter_inventory.type")
                           .append(": ")
                           .append(Component.translatable("gui.blue_skies.supporter_inventory.type.%s".replace("%s", this.petID + "")))
                     );
                  this.petEntity.setTypeID(this.petID);
               }
            )
            .bounds(this.width / 2 - 70, this.height / 2 - 35, 140, 20)
            .build()
      );
      this.soundButton = (Button)this.addRenderableWidget(Button.builder(soundText, button -> {
         this.petAudible = !this.petAudible;
         this.soundButton.setMessage(Component.translatable("gui.blue_skies.supporter_inventory.audible").append(": " + this.petAudible));
      }).bounds(this.width / 2 - 50, this.height / 2 - 13, 100, 20).build());
      this.enableButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("gui.blue_skies.supporter_inventory.enable"), button -> {
         PacketHandler.sendToServer(new SpawnSupporterPetPacket(this.petID, true, this.petAudible, this.nameEdit.getValue(), (byte)this.styleIndex));
         this.enableButton.active = false;
         this.disableButton.active = true;
      }).bounds(this.width / 2 - 80, this.height / 2 + 50, 70, 20).build());
      this.disableButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("gui.blue_skies.supporter_inventory.disable"), button -> {
         PacketHandler.sendToServer(new SpawnSupporterPetPacket(this.petID, false, this.petAudible, "", (byte)0));
         this.enableButton.active = true;
         this.disableButton.active = false;
      }).bounds(this.width / 2 + 10, this.height / 2 + 50, 70, 20).build());
      this.styleButton = (Button)this.addRenderableWidget(Button.builder(Component.literal("I").withStyle(this.style), button -> {
         if (this.styleIndex < 15) {
            this.styleIndex++;
         } else {
            this.styleIndex = 0;
         }

         this.style = ChatFormatting.values()[this.styleIndex];
         this.styleButton.setMessage(Component.literal("I").withStyle(this.style));
      }).bounds(this.width / 2 - 80, this.height / 2 + 20, 20, 20).build());
      this.nameEdit = new EditBox(
         this.font, this.width / 2 - 50, this.height / 2 + 20, 100, 20, Component.translatable("gui.blue_skies.supporter_inventory.name")
      );
      this.nameEdit.setMaxLength(16);
      this.setInitialFocus(this.nameEdit);
      this.idButton.visible = true;
      this.soundButton.visible = true;
      this.enableButton.visible = true;
      this.disableButton.visible = true;
      this.nameEdit.visible = true;
      boolean ranked = BlueSkies.ML_SUPPORTER.getRank(mc.player).hasPerks();
      this.idButton.active = ranked;
      this.soundButton.active = ranked;
      this.enableButton.active = ranked;
      this.disableButton.active = ranked;
      this.petEntity = new SupporterPetEntity(mc.level, this.petID);
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(graphics, mouseX, mouseY, partialTicks);
      boolean large = this.height > 350;
      int xSize = 176;
      int ySize = 166;
      int i = (this.width - xSize) / 2;
      int j = (this.height - ySize) / 2;
      graphics.blit(BACKGROUND, i, j, 0, 0, xSize, ySize);
      MutableComponent title = !BlueSkies.ML_SUPPORTER.getRank(mc.player).hasPerks()
         ? Component.literal("Thanks for sup- wait hold on... ")
         : Component.translatable("gui.blue_skies.supporter_inventory");
      graphics.drawString(
         this.font,
         title.withStyle(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC}),
         this.width / 2 - this.font.width(title.getString()) / 2,
         this.height / 2 - (large ? 70 : 78),
         -1,
         false
      );

      for (Renderable renderable : this.renderables) {
         renderable.render(graphics, mouseX, mouseY, partialTicks);
      }

      if (this.petEntity != null) {
         int xOff = i + 90;
         int yOff = large ? j - 55 : j + 30;
         int size = large ? 56 : 18;
         InventoryScreen.renderEntityInInventoryFollowsMouse(
            graphics, xOff - size, yOff - size, xOff + size, yOff + size, large ? 100 : 30, !large ? 0.1F : 0.0F, mouseX, mouseY, this.petEntity
         );
      }

      graphics.drawString(
         this.font,
         Component.translatable("gui.blue_skies.supporter_inventory.name")
            .withStyle(ChatFormatting.DARK_GRAY)
            .append(Component.literal(" ").append(Component.translatable("gui.blue_skies.supporter_inventory.name_subtext")).withStyle(ChatFormatting.ITALIC)),
         this.width / 2 - 45,
         this.height / 2 + 10,
         -1,
         false
      );
      this.nameEdit.render(graphics, mouseX, mouseY, partialTicks);
   }

   public void resize(Minecraft minecraft, int width, int height) {
      String s = this.nameEdit.getValue();
      this.init(minecraft, width, height);
      this.nameEdit.setValue(s);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256 && this.shouldCloseOnEsc()) {
         this.onClose();
         return true;
      } else {
         return this.nameEdit.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean charTyped(char codePoint, int modifiers) {
      return this.nameEdit.charTyped(codePoint, modifiers);
   }

   public boolean isPauseScreen() {
      return false;
   }
}
