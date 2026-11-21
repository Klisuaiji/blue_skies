package com.legacy.blue_skies.entities.util;

public enum SkiesDungeonType {
   NONE(0),
   BLINDING(1),
   NATURE(2),
   POISON(3),
   AQUATIC(4),
   LIFE(5),
   RUNIC(6);

   private final int id;

   SkiesDungeonType(int idIn) {
      this.id = idIn;
   }

   public static SkiesDungeonType getFromId(int idIn) {
      for (SkiesDungeonType typeIn : values()) {
         if (idIn == typeIn.id) {
            return typeIn;
         }
      }

      return null;
   }

   public int getId() {
      return this.id;
   }
}
