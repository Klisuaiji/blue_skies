package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class SkiesWoodTypes {
   // 1.21.1: WoodType is a final record and WoodType.register is private. Custom wood
   // types are constructed directly; they are referenced by the mod's sign blocks only.
   public static final WoodType BLUEBRIGHT = new WoodType(BlueSkies.find("bluebright"), SkiesBlockSets.BLUEBRIGHT);
   public static final WoodType STARLIT = new WoodType(BlueSkies.find("starlit"), SkiesBlockSets.STARLIT);
   public static final WoodType FROSTBRIGHT = new WoodType(BlueSkies.find("frostbright"), SkiesBlockSets.FROSTBRIGHT);
   public static final WoodType LUNAR = new WoodType(BlueSkies.find("lunar"), SkiesBlockSets.LUNAR);
   public static final WoodType DUSK = new WoodType(BlueSkies.find("dusk"), SkiesBlockSets.DUSK);
   public static final WoodType MAPLE = new WoodType(BlueSkies.find("maple"), SkiesBlockSets.MAPLE);
   public static final WoodType COMET = new WoodType(BlueSkies.find("comet"), SkiesBlockSets.COMET);

   public static void init() {
   }
}
