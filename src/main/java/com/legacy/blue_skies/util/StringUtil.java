package com.legacy.blue_skies.util;

import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

public class StringUtil {
   public static final MutableComponent UNFINISHED = Component.translatable("gui.blue_skies.item.unused").withStyle(ChatFormatting.GRAY);
   public static final MutableComponent BOSS_EGG = Component.translatable("gui.blue_skies.item.boss_egg").withStyle(ChatFormatting.GRAY);

   public static String getAbilityText(String abilityText) {
      return String.format(
         ChatFormatting.GOLD + I18n.get("gui.blue_skies.item.ability", new Object[0]) + ChatFormatting.WHITE + ": " + I18n.get(abilityText, new Object[0])
      );
   }

   public static String getConText(String conText) {
      return String.format(
         ChatFormatting.AQUA + I18n.get("gui.blue_skies.item.con", new Object[0]) + ChatFormatting.WHITE + ": " + I18n.get(conText, new Object[0])
      );
   }

   public static String toLower(String str) {
      return str.toLowerCase(Locale.ENGLISH);
   }

   public static Component getContainerKey(String blockName) {
      return Component.translatable("container.blue_skies." + blockName);
   }

   public static Component getBasicKey(String prefix, String key) {
      return Component.translatable(prefix + ".blue_skies." + key);
   }

   public static final TextColor parseHex(String hex) {
      return (TextColor)TextColor.parseColor(hex).getOrThrow(s -> new IllegalArgumentException(s + " is now a valid hex color"));
   }
}
