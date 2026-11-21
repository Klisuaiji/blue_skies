package com.legacy.blue_skies.compat;

/** NeoForge PlantType was removed in 1.21; local stand-in. */
public final class PlantType {
   public static final PlantType PLAINS = new PlantType("plains");
   public static final PlantType DESERT = new PlantType("desert");
   public static final PlantType BEACH = new PlantType("beach");
   public static final PlantType CAVE = new PlantType("cave");
   public static final PlantType WATER = new PlantType("water");
   public static final PlantType NETHER = new PlantType("nether");
   public static final PlantType CROP = new PlantType("crop");
   private final String name;
   private PlantType(String name) { this.name = name; }
   public String getName() { return this.name; }
}
