package com.legacy.blue_skies.compat;

import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/** 1.20.4 UUID/name based AttributeModifier -> 1.21.1 id based record. */
public final class LegacyAttrs {
   private LegacyAttrs() {}

   public static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
   public static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

   /** 1.20.4's new AttributeModifier(name, amount, op) auto-generated a random UUID. */
   public static AttributeModifier modifier(String name, double amount, AttributeModifier.Operation operation) {
      return modifier(UUID.randomUUID(), name, amount, operation);
   }

   public static AttributeModifier modifier(UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
      String slug = name.toLowerCase().replaceAll("[^a-z0-9]+", "_").replaceAll("(^_+|_+$)", "");
      return new AttributeModifier(ResourceLocation.fromNamespaceAndPath("blue_skies", slug + "_" + uuid), amount, operation);
   }
}
