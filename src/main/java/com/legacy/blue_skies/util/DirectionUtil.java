package com.legacy.blue_skies.util;

import java.util.Arrays;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;

public class DirectionUtil {
   public static final Direction[] HORIZONTAL = Arrays.stream(Direction.values()).filter(d -> d.getAxis() != Axis.Y).toArray(Direction[]::new);

   public static Direction randHorizontal(RandomSource rand) {
      return (Direction)Util.getRandom(HORIZONTAL, rand);
   }
}
