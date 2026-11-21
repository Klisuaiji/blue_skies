package com.legacy.blue_skies.world.general_features.structures.villages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

public enum SkiesVillageProfessions {
   LIBRARY("library"),
   TANNERY("tannery"),
   FARM("farm"),
   SHEPHERD("shepherd"),
   FISHER("fisher"),
   BUTCHER("butcher"),
   ARMORER("armorer"),
   CARTOGRAPHER("cartographer"),
   FLETCHER("fletcher"),
   WEAPONSMITH("weaponsmith"),
   TOOLSMITH("toolsmith"),
   MASON("mason"),
   TEMPLE("temple"),
   STARGAZER("stargazer"),
   WRANGLER("wrangler"),
   SHOVELER("shoveler"),
   NIGHTWATCHER("nightwatcher");

   private final String name;

   SkiesVillageProfessions(String name) {
      this.name = name;
   }

   public static List<SkiesVillageProfessions> getWithout(SkiesVillageProfessions... excluded) {
      List<SkiesVillageProfessions> names = new ArrayList<>(Arrays.asList(values()));
      names.removeAll(Arrays.asList(excluded));
      return names;
   }

   @Override
   public String toString() {
      return this.name;
   }

   @Nullable
   public static SkiesVillageProfessions from(String name) {
      for (SkiesVillageProfessions value : values()) {
         if (value.name.equals(name)) {
            return value;
         }
      }

      return null;
   }
}
