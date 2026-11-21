package com.legacy.blue_skies.compat;

import net.minecraft.resources.ResourceLocation;

/** VanillaGuiOverlay was relocated in 1.21.1; name-only stand-in. */
public final class LegacyGuiOverlay {
   private LegacyGuiOverlay() {}
   public static final Named PLAYER_HEALTH = new Named("player_health");
   public static final Named ITEM_NAME = new Named("item_name");
   public static final class Named {
      private final String name;
      Named(String name) { this.name = name; }
      public ResourceLocation id() { return ResourceLocation.fromNamespaceAndPath("minecraft", this.name); }
   }
}
