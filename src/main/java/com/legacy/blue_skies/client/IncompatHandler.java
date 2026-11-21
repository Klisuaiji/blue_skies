package com.legacy.blue_skies.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.TimerTask;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class IncompatHandler {
   public static void initStartup() {
      IncompatHandler.Optifine.check();
   }

   public static class Optifine {
      public static final boolean OPTIFINE_LOADED = isOptifineLoaded();
      public static final MutableComponent SHADER_MSG = Component.literal("" + ChatFormatting.AQUA + ChatFormatting.BOLD + "Blue Skies: ")
         .append(
            Component.literal(
               ChatFormatting.RESET
                  + "Shaders detected. Shaders cause many problems with the mod, some of which being: \nGlowing/transparent mobs turning black, the sky missing/glitching/displaying incorrectly, commom items turning into pink/black boxes, block glow overlays missing entirely, and custom fog missing. \nWe highly recommend disabling shaders within the dimensions for the best experience. Please do not report grapical issues related to shaders!"
            )
         );
      public static boolean shadersLoaded = false;
      public static boolean shouldWarn = false;

      public static void check() {
         if (OPTIFINE_LOADED) {
            File file = new File("optionsshaders.txt");
            if (file.exists()) {
               try {
                  BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                  String line;
                  while ((line = reader.readLine()) != null) {
                     if (line.startsWith("shaderPack=")) {
                        if (!line.endsWith("=OFF")) {
                           if (shadersLoaded) {
                              reader.close();
                              return;
                           }

                           shadersLoaded = true;
                           break;
                        }

                        shadersLoaded = false;
                     }
                  }

                  reader.close();
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }

            shouldWarn = shadersLoaded;
         }
      }

      private static boolean isOptifineLoaded() {
         try {
            Class.forName("net.optifine.Lang");
            return true;
         } catch (Throwable t) {
            return false;
         }
      }

      public static TimerTask getTask() {
         return new TimerTask() {
            @Override
            public void run() {
               IncompatHandler.Optifine.check();
            }
         };
      }
   }
}
