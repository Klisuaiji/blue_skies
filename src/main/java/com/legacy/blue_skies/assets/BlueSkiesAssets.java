package com.legacy.blue_skies.assets;

import com.legacy.blue_skies.assets.managers.JournalLangManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlueSkiesAssets {
   public static final JournalLangManager JOURNAL_LANG = new JournalLangManager();

   public static void init() {
      if (Minecraft.getInstance() != null) {
         ReloadableResourceManager manager = (ReloadableResourceManager)Minecraft.getInstance().getResourceManager();
         manager.registerReloadListener(JOURNAL_LANG);
      }
   }
}
