package com.legacy.blue_skies.compat;
import net.minecraft.resources.ResourceLocation;
/** ResourceLocation.isValidResourceLocation became private in 1.21.1. */
public final class LegacyRL {
   private LegacyRL() {}
   public static boolean isValid(String s) {
      try { ResourceLocation.parse(s); return true; } catch (Exception e) { return false; }
   }
}
