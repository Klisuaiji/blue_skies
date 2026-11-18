package com.legacy.blue_skies.client.gui.toast;

import com.legacy.blue_skies.registries.SkiesItems;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.components.toasts.Toast.Visibility;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class SkiesToast implements Toast {
   private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");
   private final ResourceLocation backgroundTexture;
   private final Component desc;
   private final Component name;
   private final Item displayItem;
   private final ChatFormatting defaultColor;
   private boolean hasPlayedSound;
   @Nullable
   private final SoundEvent sound;

   public SkiesToast(
      Component nameIn, Component description, @Nullable SoundEvent soundIn, ResourceLocation backgroundTextureIn, Item itemIn, ChatFormatting defaultColorIn
   ) {
      this.desc = description;
      this.name = nameIn;
      this.sound = soundIn;
      this.backgroundTexture = backgroundTextureIn;
      this.displayItem = itemIn;
      this.defaultColor = defaultColorIn;
   }

   public void beginDisplaying() {
      Minecraft.getInstance().getToasts().addToast(this);
   }

   public Visibility render(GuiGraphics graphics, ToastComponent toast, long toastTime) {
      graphics.blitSprite(this.backgroundTexture, 0, 0, this.width(), this.height());
      List<FormattedCharSequence> list = toast.getMinecraft().font.split(this.desc, 125);
      int color = this.desc.getStyle() != null && this.desc.getStyle().getColor() != null
         ? this.desc.getStyle().getColor().getValue()
         : this.defaultColor.getColor();
      if (list.size() == 1) {
         graphics.drawString(toast.getMinecraft().font, this.name, 30, 7, color | 0xFF000000);
         graphics.drawString(toast.getMinecraft().font, list.get(0), 30, 18, -1);
      } else if (toastTime < 1500L) {
         int k = Mth.floor(Mth.clamp((float)(1500L - toastTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
         graphics.drawString(toast.getMinecraft().font, this.name, 30, 11, color | k);
      } else {
         int i1 = Mth.floor(Mth.clamp((float)(toastTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
         int l = this.height() / 2 - list.size() * 9 / 2;

         for (FormattedCharSequence ireorderingprocessor : list) {
            graphics.drawString(toast.getMinecraft().font, ireorderingprocessor, 30, l, 16777215 | i1);
            l += 9;
         }
      }

      if (!this.hasPlayedSound && toastTime > 0L) {
         this.hasPlayedSound = true;
         if (this.sound != null) {
            toast.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(this.sound, 1.0F, 1.0F));
         }
      }

      graphics.renderItem(new ItemStack(this.displayItem), 8, 8);
      return toastTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
   }

   public static class Builder {
      private ResourceLocation backgroundTexture = SkiesToast.BACKGROUND_SPRITE;
      private Component desc = Component.empty();
      private Component name = Component.empty();
      private Item displayItem = SkiesItems.blue_journal;
      private SoundEvent sound = null;
      private ChatFormatting defaultColor = ChatFormatting.AQUA;

      public static SkiesToast.Builder get() {
         return new SkiesToast.Builder();
      }

      public SkiesToast.Builder withTexture(ResourceLocation backgroundTexture) {
         this.backgroundTexture = backgroundTexture;
         return this;
      }

      public SkiesToast.Builder withDesc(Component desc) {
         this.desc = desc;
         return this;
      }

      public SkiesToast.Builder withTitle(Component name) {
         this.name = name;
         return this;
      }

      public SkiesToast.Builder withDisplayItem(ItemLike displayItem) {
         this.displayItem = displayItem.asItem();
         return this;
      }

      public SkiesToast.Builder withSound(SoundEvent sound) {
         this.sound = sound;
         return this;
      }

      public SkiesToast.Builder withTitleColor(ChatFormatting color) {
         this.defaultColor = color;
         return this;
      }

      public SkiesToast build() {
         return new SkiesToast(this.name, this.desc, this.sound, this.backgroundTexture, this.displayItem, this.defaultColor);
      }
   }
}
