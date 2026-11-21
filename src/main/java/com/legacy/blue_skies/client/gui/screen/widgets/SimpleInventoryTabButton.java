package com.legacy.blue_skies.client.gui.screen.widgets;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.BlueSkiesConfig;
import com.legacy.blue_skies.capability.SkiesPlayer;
import com.legacy.blue_skies.client.SkiesClientUtil;
import java.util.Optional;
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

public class SimpleInventoryTabButton extends Button {
   private static final ResourceLocation TEXTURE = BlueSkies.locate("textures/gui/inventory_tabs_simple.png");
   private final int textureIndex;
   private final int posIndex;
   private final boolean enabled;
   private final Screen parentGui;
   private final SoundEvent openSound;
   private boolean isRecipeOpen = false;
   private boolean shouldDraw = true;

   public SimpleInventoryTabButton(int textureIndex, Screen parentGui, boolean enabled, int posIndex, OnPress onPress) {
      this(textureIndex, parentGui, enabled, posIndex, onPress, (SoundEvent)SoundEvents.UI_BUTTON_CLICK);
   }

   public SimpleInventoryTabButton(int textureIndex, Screen parentGui, boolean enabled, int posIndex, OnPress onPress, SoundEvent openSound) {
      super(
         parentGui.width / 2 + BlueSkiesConfig.CLIENT.getInventoryTabX() + (BlueSkiesConfig.CLIENT.areInventoryTabsVertical() ? 0 : posIndex) * 25,
         parentGui.height / 2 - BlueSkiesConfig.CLIENT.getInventoryTabY() + (BlueSkiesConfig.CLIENT.areInventoryTabsVertical() ? posIndex : 0) * 22,
         25,
         22,
         Component.empty(),
         onPress,
         Button.DEFAULT_NARRATION
      );
      this.textureIndex = textureIndex;
      this.posIndex = posIndex;
      this.enabled = enabled;
      this.parentGui = parentGui;
      this.openSound = openSound;
   }

   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      Optional<ResourceKey<CreativeModeTab>> selectedTab = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(CreativeModeInventoryScreen.selectedTab);
      boolean wasRecipeOpen = this.isRecipeOpen;
      this.isRecipeOpen = this.parentGui.getClass() == InventoryScreen.class && ((InventoryScreen)this.parentGui).getRecipeBookComponent().isVisible();
      if (wasRecipeOpen != this.isRecipeOpen) {
         this.setX(this.isRecipeOpen ? this.getX() + 77 : this.getX() - 77);
      }

      if (this.parentGui instanceof CreativeModeInventoryScreen c) {
         this.shouldDraw = selectedTab.isPresent() && selectedTab.get().equals(CreativeModeTabs.INVENTORY);
         this.setX(
            this.parentGui.width / 2
               + (this.parentGui.width % 2 == 0 ? BlueSkiesConfig.CLIENT.getCreativeInventoryTabX() - 1 : BlueSkiesConfig.CLIENT.getCreativeInventoryTabX())
         );
         this.setY(this.parentGui.height / 2 - BlueSkiesConfig.CLIENT.getCreativeInventoryTabY() + this.posIndex * 22);
      }

      this.active = this.shouldDraw;
      if (this.shouldDraw) {
         if (this.textureIndex == 3) {
            SkiesPlayer.ifPresent(Minecraft.getInstance().player, skyPlayer -> {
               if (!skyPlayer.hasOpenedBlueLore()) {
                  SkiesClientUtil.renderExclamation(graphics, this.getX() + 18, this.getY() + 3);
               }
            });
         }

         int texStartX = 25 * this.textureIndex;
         graphics.blit(TEXTURE, this.getX(), this.getY(), texStartX, this.isHoveredOrFocused() && this.enabled ? 0 : 22, 25, 22, 128, 128);
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
