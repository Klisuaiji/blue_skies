package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class SkiesWoodTypes {
   public static final WoodType BLUEBRIGHT = register("bluebright", SkiesBlockSets.BLUEBRIGHT);
   public static final WoodType STARLIT = register("starlit", SkiesBlockSets.STARLIT);
   public static final WoodType FROSTBRIGHT = register("frostbright", SkiesBlockSets.FROSTBRIGHT);
   public static final WoodType LUNAR = register("lunar", SkiesBlockSets.LUNAR);
   public static final WoodType DUSK = register("dusk", SkiesBlockSets.DUSK);
   public static final WoodType MAPLE = register("maple", SkiesBlockSets.MAPLE);
   public static final WoodType COMET = register("comet", SkiesBlockSets.COMET);

   public static WoodType register(String key, BlockSetType blockSet) {
      return WoodType.register(new WoodType(BlueSkies.find(key), blockSet));
   }

   public static void init() {
   }
}
