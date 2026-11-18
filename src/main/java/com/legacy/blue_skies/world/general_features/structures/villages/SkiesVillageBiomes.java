package com.legacy.blue_skies.world.general_features.structures.villages;

import javax.annotation.Nullable;

public enum SkiesVillageBiomes {
   CALMING_SKIES("calming_skies"),
   BRIGHTLANDS("brightlands"),
   SLUSHLANDS("slushlands"),
   SHADED_WOODLANDS("shaded_woodlands"),
   SUNSET_MAPLE_FOREST("sunset_maple_forest"),
   CRYSTAL_DUNES("crystal_dunes");

   private final String name;

   private SkiesVillageBiomes(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return this.name;
   }

   @Nullable
   public static SkiesVillageBiomes from(String name) {
      for (SkiesVillageBiomes value : values()) {
         if (value.name.equals(name)) {
            return value;
         }
      }

      return null;
   }
}
