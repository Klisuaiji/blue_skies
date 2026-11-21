package com.legacy.blue_skies.items.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Client-only lookup provider used when encoding/decoding item stack data components
 *  (e.g. prepared food ingredients) outside of a server context, such as item tooltips.
 *  Kept in its own class so dedicated servers never load client classes. */
@OnlyIn(Dist.CLIENT)
public final class ClientRegistryProvider {
   private ClientRegistryProvider() {
   }

   public static HolderLookup.Provider get() {
      Minecraft minecraft = Minecraft.getInstance();
      Level level = minecraft.level;
      return level != null ? level.registryAccess() : RegistryAccess.EMPTY;
   }
}
