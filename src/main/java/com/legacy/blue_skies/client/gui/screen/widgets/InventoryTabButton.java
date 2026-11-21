package com.legacy.blue_skies.client.gui.screen.widgets;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.SkiesClientUtil;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public class InventoryTabButton extends Button {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/gui/inventory_tabs.png");
   private final int textureIndex;
   private final boolean enabled;
   private final Screen parentGui;
   private final SoundEvent openSound;
   private long hoverStart = 0L;
   private int offsetY = 0;
   private boolean wasHovered = false;
   private boolean isRecipeOpen = false;
   private boolean shouldDraw = true;

   public InventoryTabButton(int textureIndex, Screen parentGui, boolean enabled, int heightIndex, OnPress onPress) {
      this(textureIndex, parentGui, enabled, heightIndex, onPress, (SoundEvent)SoundEvents.UI_BUTTON_CLICK);
   }

   public InventoryTabButton(int textureIndex, Screen parentGui, boolean enabled, int posIndex, OnPress onPress, SoundEvent openSound) {
      super(parentGui.width / 2 + xPosFromIndex(posIndex), parentGui.height / 2 + 83, 22, 22, Component.empty(), onPress, Button.DEFAULT_NARRATION);
      this.textureIndex = textureIndex;
      this.enabled = enabled;
      this.parentGui = parentGui;
      this.openSound = openSound;
   }

   private static int xPosFromIndex(int heightIndex) {
      return BlueSkiesConfig.CLIENT.leftAlignInventoryTabs() ? -63 + (heightIndex - 1) * 22 : 86 + (-heightIndex - 1) * 22;
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      boolean wasRecipeOpen = this.isRecipeOpen;
      this.isRecipeOpen = this.parentGui.getClass() == InventoryScreen.class && ((InventoryScreen)this.parentGui).getRecipeBookComponent().isVisible();
      if (wasRecipeOpen != this.isRecipeOpen) {
         this.setX(this.isRecipeOpen ? this.getX() + 77 : this.getX() - 77);
      }

      if (this.parentGui instanceof CreativeModeInventoryScreen) {
         Optional<ResourceKey<CreativeModeTab>> key = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(CreativeModeInventoryScreen.selectedTab);
         this.shouldDraw = key.isPresent() && key.get().equals(CreativeModeTabs.INVENTORY);
      }

      long time = Util.getMillis();
      int animationTime = 180;
      int texStartX = 22 * this.textureIndex;
      int height = 18;
      if (this.enabled) {
         if (this.isHoveredOrFocused()) {
            if (!this.wasHovered) {
               this.hoverStart = Util.getMillis();
            }

            if (this.offsetY < height) {
               this.offsetY = (int)((time - this.hoverStart) * height / animationTime);
            }

            if (this.offsetY > height) {
               this.offsetY = height;
            }
         } else {
            if (this.wasHovered) {
               this.hoverStart = Util.getMillis();
            }

            if (this.offsetY > 0) {
               this.offsetY = height - (int)((time - this.hoverStart) * height / animationTime);
            }

            if (this.offsetY < 0) {
               this.offsetY = 0;
            }
         }
      } else {
         this.hoverStart = 0L;
         this.offsetY = height;
      }

      this.wasHovered = this.isHoveredOrFocused();
      this.active = this.shouldDraw;
      if (this.shouldDraw) {
         graphics.blit(
            TEXTURE, this.getX(), this.getY(), texStartX, height - this.offsetY + (this.enabled ? height + 4 : 0), height + 4, 4 + this.offsetY, 128, 128
         );
         if (this.textureIndex == 3) {
            SkiesPlayer.ifPresent(Minecraft.getInstance().player, skyPlayer -> {
               if (!skyPlayer.hasOpenedBlueLore()) {
                  SkiesClientUtil.renderExclamation(graphics, this.getX() + 7, this.getY() + 9 + this.offsetY);
               }
            });
         }
      }
   }

   public void onPress() {
      if (this.enabled) {
         super.onPress();
      }
   }

   public void playDownSound(SoundManager soundHandler) {
      soundHandler.play(SimpleSoundInstance.forUI(this.openSound, 1.0F));
   }
}
