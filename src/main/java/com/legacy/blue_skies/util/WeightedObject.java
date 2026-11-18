package com.legacy.blue_skies.util;

import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public class WeightedObject<T> implements WeightedEntry {
   private final T obj;
   private final Weight weight;

   public WeightedObject(T obj, int itemWeight) {
      this.weight = Weight.of(itemWeight);
      this.obj = obj;
   }

   public T get() {
      return this.obj;
   }

   public Weight getWeight() {
      return this.weight;
   }
}
