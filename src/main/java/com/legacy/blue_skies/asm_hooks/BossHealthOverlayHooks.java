package com.legacy.blue_skies.asm_hooks;

import com.google.common.collect.Sets;
import com.legacy.blue_skies.BlueSkies;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent.BossBarColor;

public class BossHealthOverlayHooks {
   private static final Set<BossBarColor> COLORED_INFO_FRAMES = Sets.newHashSet(
      new BossBarColor[]{BossBarColor.PURPLE, BossBarColor.RED, BossBarColor.GREEN, BossBarColor.PINK}
   );
   private static final ResourceLocation BOSSBAR_FRAMES = BlueSkies.locate("textures/gui/bossbar_frames.png");

   public static void render(Minecraft minecraft, Map<UUID, LerpingBossEvent> events, GuiGraphics graphics) {
      if (!events.isEmpty()) {
         int i = minecraft.getWindow().getGuiScaledWidth();
         int j = 12;

         for (LerpingBossEvent clientbossinfo : events.values()) {
            int k = i / 2 - 91;
            if (shouldDisplayFrame(clientbossinfo)) {
               graphics.blit(BOSSBAR_FRAMES, k - 7, j - 2, -4.0F, offsetForEntity(clientbossinfo) * 9, 192, 9, 256, 256);
            }

            j += 10 + 9;
            if (j >= minecraft.getWindow().getGuiScaledHeight() / 3) {
               break;
            }
         }
      }
   }

   private static boolean shouldDisplayFrame(LerpingBossEvent info) {
      return info.getName().getString().contains("⭐") && COLORED_INFO_FRAMES.contains(info.getColor());
   }

   private static int offsetForEntity(LerpingBossEvent info) {
      if (info.getColor() == BossBarColor.RED) {
         return 1;
      } else if (info.getColor() == BossBarColor.GREEN) {
         return 2;
      } else {
         return info.getColor() == BossBarColor.PINK ? 3 : 0;
      }
   }
}
