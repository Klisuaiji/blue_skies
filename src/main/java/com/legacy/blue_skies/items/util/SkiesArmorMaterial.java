package com.legacy.blue_skies.items.util;

import com.google.common.collect.Streams;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import java.util.EnumMap;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Ingredient.TagValue;
import net.neoforged.neoforge.common.Tags.Items;
import net.neoforged.neoforge.common.util.Lazy;

public enum SkiesArmorMaterial implements ArmorMaterial {
   PYROPE("pyrope", 15, armorValues(1, 4, 5, 2), 12, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, () -> Ingredient.of(SkiesItemTags.PYROPE), true),
   AQUITE("aquite", 15, armorValues(2, 5, 6, 2), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, () -> Ingredient.of(SkiesItemTags.AQUITE), true),
   HORIZONITE("horizonite", 15, armorValues(1, 4, 5, 2), 12, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, () -> Ingredient.of(SkiesItemTags.HORIZONITE), true),
   DIOPSIDE("diopside", 36, armorValues(2, 5, 7, 2), 9, SoundEvents.ARMOR_EQUIP_DIAMOND, 4.5F, () -> Ingredient.of(SkiesItemTags.DIOPSIDE), true),
   CHAROITE("charoite", 33, armorValues(3, 6, 8, 3), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, () -> Ingredient.of(SkiesItemTags.CHAROITE), true),
   SHADOW(
      "shadow",
      35,
      armorValues(3, 6, 8, 3),
      10,
      SoundEvents.ARMOR_EQUIP_DIAMOND,
      3.0F,
      () -> Ingredient.fromValues(Streams.concat(new Stream[]{Stream.of(new TagValue(SkiesItemTags.CHAROITE)), Stream.of(new TagValue(Items.GEMS_DIAMOND))})),
      true
   );

   private static final EnumMap<Type, Integer> HEALTH_FUNCTION_FOR_TYPE = armorValues(13, 15, 16, 11);
   private final String name;
   private final int maxDamageFactor;
   private final EnumMap<Type, Integer> damageReductionAmountArray;
   private final int enchantability;
   private final SoundEvent soundEvent;
   private final float toughness;
   private final boolean compatWithFalsite;
   private final Lazy<Ingredient> repairMaterial;

   private SkiesArmorMaterial(
      String nameIn,
      int maxDamage,
      EnumMap<Type, Integer> damageReductionArray,
      int enchantability,
      SoundEvent equipSound,
      float toughness,
      Supplier<Ingredient> repairMaterial,
      boolean compatWithFalsite
   ) {
      this.name = BlueSkies.find(nameIn);
      this.maxDamageFactor = maxDamage;
      this.damageReductionAmountArray = damageReductionArray;
      this.enchantability = enchantability;
      this.soundEvent = equipSound;
      this.toughness = toughness;
      this.repairMaterial = Lazy.of(repairMaterial);
      this.compatWithFalsite = compatWithFalsite;
   }

   public int getDurabilityForType(Type type) {
      return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.maxDamageFactor;
   }

   public int getDefenseForType(Type type) {
      return this.damageReductionAmountArray.get(type);
   }

   public int getEnchantmentValue() {
      return this.enchantability;
   }

   public SoundEvent getEquipSound() {
      return this.soundEvent;
   }

   public Ingredient getRepairIngredient() {
      return (Ingredient)this.repairMaterial.get();
   }

   public String getName() {
      return this.name;
   }

   public float getToughness() {
      return this.toughness;
   }

   public boolean isCompatWithFalsite() {
      return this.compatWithFalsite;
   }

   public float getKnockbackResistance() {
      return 0.0F;
   }

   public static EnumMap<Type, Integer> armorValues(int boots, int legs, int chest, int helmet) {
      return (EnumMap<Type, Integer>)Util.make(new EnumMap(Type.class), material -> {
         material.put(Type.BOOTS, boots);
         material.put(Type.LEGGINGS, legs);
         material.put(Type.CHESTPLATE, chest);
         material.put(Type.HELMET, helmet);
      });
   }
}
