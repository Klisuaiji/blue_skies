package com.legacy.blue_skies.data.objects;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ToolHandleType implements IToJson<ToolHandleType> {
   public static ToolHandleType DEFAULT = new ToolHandleType(1.0F, 1.0F, 1.0F, Items.STICK, Integer.parseInt("896727", 16));
   private final float durability;
   private final float attackSpeed;
   private final float projectileDamage;
   private final Item stick;
   private final int color;

   public ToolHandleType(float durability, float attackSpeed, float projectileDamage, Item stick, int color) {
      this.durability = durability;
      this.attackSpeed = attackSpeed;
      this.projectileDamage = projectileDamage;
      this.stick = stick;
      this.color = color;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("stick", BuiltInRegistries.ITEM.getKey(this.stick).toString());
      json.addProperty("attack_speed", this.attackSpeed);
      json.addProperty("projectile_damage", this.projectileDamage);
      json.addProperty("durability", this.durability);
      json.addProperty("color", this.color);
      return json;
   }

   public static String getDirectory() {
      return "tool_handle_types";
   }

   public float getDurabilityModifier() {
      return this.durability;
   }

   public float getAttackSpeedModifier() {
      return this.attackSpeed;
   }

   public float getProjectileDamageModifier() {
      return this.projectileDamage;
   }

   public Item getStick() {
      return this.stick;
   }

   public int getColor() {
      return this.color;
   }

   public String getName() {
      return BuiltInRegistries.ITEM.getKey(this.getStick()).toString();
   }

   @Override
   public String toString() {
      return String.format(
         "ToolHandleType: { stick: %s, durability: %s, attack_spd: %s, projectile_dmg: %s, color: %s }",
         BuiltInRegistries.ITEM.getKey(this.stick),
         this.durability,
         this.attackSpeed,
         this.projectileDamage,
         this.color
      );
   }
}
