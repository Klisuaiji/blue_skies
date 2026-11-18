package com.legacy.blue_skies.data.objects.tags;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SkiesEntityTags {
   public static final TagKey<EntityType<?>> FROST_SPIRITS = tag("frost_spirits");
   public static final TagKey<EntityType<?>> BOSSES = tag("bosses");
   public static final TagKey<EntityType<?>> NATURE_ALLIES = tag("nature_allies");
   public static final TagKey<EntityType<?>> POISON_ALLIES = tag("poison_allies");

   public static void init() {
   }

   private static TagKey<EntityType<?>> tag(String key) {
      return TagKey.create(Registries.ENTITY_TYPE, BlueSkies.locate(key));
   }
}
