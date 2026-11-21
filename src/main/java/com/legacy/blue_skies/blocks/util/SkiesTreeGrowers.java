package com.legacy.blue_skies.blocks.util;

import com.legacy.blue_skies.registries.SkiesRefs;
import java.util.Optional;
import net.minecraft.world.level.block.grower.TreeGrower;

public class SkiesTreeGrowers {
   public static final TreeGrower BLUEBRIGHT = new TreeGrower("bluebright", Optional.empty(), Optional.of(SkiesRefs.BLUEBRIGHT_TREE), Optional.empty());
   public static final TreeGrower STARLIT = new TreeGrower("starlit", Optional.empty(), Optional.of(SkiesRefs.STARLIT_TREE), Optional.empty());
   public static final TreeGrower FROSTBRIGHT = new TreeGrower(
      "frostbright", Optional.of(SkiesRefs.FROSTBRIGHT_TREE), Optional.of(SkiesRefs.SMALL_FROSTBRIGHT_TREE), Optional.empty()
   );
   public static final TreeGrower LUNAR = new TreeGrower("lunar", Optional.empty(), Optional.of(SkiesRefs.LUNAR_TREE), Optional.empty());
   public static final TreeGrower DUSK = new TreeGrower("dusk", Optional.of(SkiesRefs.LARGE_DUSK_TREE), Optional.of(SkiesRefs.DUSK_TREE), Optional.empty());
   public static final TreeGrower MAPLE = new TreeGrower("maple", Optional.empty(), Optional.of(SkiesRefs.MAPLE_TREE), Optional.empty());
   public static final TreeGrower CRESCENT_FRUIT = new TreeGrower(
      "crescent_fruit", Optional.empty(), Optional.of(SkiesRefs.CRESCENT_FRUIT_TREE), Optional.empty()
   );
   public static final TreeGrower COMET = new TreeGrower("bluebright", Optional.empty(), Optional.of(SkiesRefs.COMET_TREE), Optional.empty());
}
