package com.legacy.blue_skies.client.init;

import com.legacy.blue_skies.registries.SkiesWoodTypes;
import net.minecraft.client.renderer.Sheets;

public class SkiesAtlases {
   public static void initWoodTypes() {
      Sheets.addWoodType(SkiesWoodTypes.BLUEBRIGHT);
      Sheets.addWoodType(SkiesWoodTypes.STARLIT);
      Sheets.addWoodType(SkiesWoodTypes.FROSTBRIGHT);
      Sheets.addWoodType(SkiesWoodTypes.LUNAR);
      Sheets.addWoodType(SkiesWoodTypes.DUSK);
      Sheets.addWoodType(SkiesWoodTypes.MAPLE);
      Sheets.addWoodType(SkiesWoodTypes.COMET);
   }
}
