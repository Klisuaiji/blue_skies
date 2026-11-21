package com.legacy.blue_skies.util;

import net.minecraft.util.RandomSource;

public class LogicUtil {
   public static double plusOrMinus(double original, double range, RandomSource rand) {
      double value = range * rand.nextDouble();
      return Math.random() < 0.5 ? original + value : original - value;
   }

   public static double plusOrMinus(double degree, RandomSource rand) {
      return plusOrMinus(0.0, degree, rand);
   }

   public static float plusOrMinus(float original, float range, RandomSource rand) {
      float value = range * rand.nextFloat();
      return Math.random() < 0.5 ? original + value : original - value;
   }

   public static float plusOrMinus(float degree, RandomSource rand) {
      return plusOrMinus(0.0F, degree, rand);
   }

   public static int plusOrMinus(int original, int range, RandomSource rand) {
      int value = rand.nextInt(range + 1);
      return Math.random() < 0.5 ? original + value : original - value;
   }

   public static int plusOrMinus(int degree, RandomSource rand) {
      return plusOrMinus(0, degree, rand);
   }

   public static int bind(int min, int value, int max) {
      return Math.max(min, Math.min(value, max));
   }

   public static float bind(float min, float value, float max) {
      return Math.max(min, Math.min(value, max));
   }

   public static double bind(double min, double value, double max) {
      return Math.max(min, Math.min(value, max));
   }

   public static double convertToNewRange(double oldValue, double oldMin, double oldMax, double newMin, double newMax) {
      return (oldValue - oldMin) / (oldMax - oldMin) * (newMax - newMin) + newMin;
   }
}
