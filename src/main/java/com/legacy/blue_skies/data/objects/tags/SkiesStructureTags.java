package com.legacy.blue_skies.data.objects.tags;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SkiesStructureTags {
   public static final TagKey<Structure> DUNGEONS = tag("dungeons");
   public static final TagKey<Structure> ON_BLINDING_MAPS = tag("on_blinding_maps");
   public static final TagKey<Structure> ON_NATURE_MAPS = tag("on_nature_maps");
   public static final TagKey<Structure> ON_POISON_MAPS = tag("on_poison_maps");
   public static final TagKey<Structure> ON_BUNKER_MAPS = tag("on_bunker_maps");
   public static final TagKey<Structure> GATEKEEPER_HOUSES = tag("gatekeeper_houses");
   public static final TagKey<Structure> TREE_DISALLOWED = tag("tree_disallowed");

   public static void init() {
   }

   private static TagKey<Structure> tag(String key) {
      return TagKey.create(Registries.STRUCTURE, BlueSkies.locate(key));
   }
}
