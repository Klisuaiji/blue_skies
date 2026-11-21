package com.legacy.blue_skies.items.util;

import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.registries.SkiesBlocks;
import java.util.function.Supplier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.util.Lazy;

public enum SkiesItemTier implements Tier {
   WOOD(0, 59, 2.0F, 0.0F, 15, () -> Ingredient.of(ItemTags.PLANKS), false, true),
   COMET(1, 100, 3.0F, 0.5F, 15, () -> Ingredient.of(new ItemLike[]{SkiesBlocks.comet_planks}), false, true),
   LUNAR_STONE(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(new ItemLike[]{SkiesBlocks.lunar_cobblestone}), false, true),
   TURQUOISE_STONE(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(new ItemLike[]{SkiesBlocks.turquoise_cobblestone}), false, true),
   PYROPE(1, 200, 11.0F, 1.5F, 10, () -> Ingredient.of(SkiesItemTags.PYROPE), true, true),
   AQUITE(2, 270, 6.0F, 2.0F, 32, () -> Ingredient.of(SkiesItemTags.AQUITE), true, true),
   DIOPSIDE(3, 1661, 6.0F, 5.0F, 14, () -> Ingredient.of(SkiesItemTags.DIOPSIDE), true, true),
   CHAROITE(3, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(SkiesItemTags.CHAROITE), true, true),
   HORIZONITE(3, 250, 8.0F, 2.0F, 10, () -> Ingredient.of(SkiesItemTags.HORIZONITE), true, true),
   INFUSED(3, 900, 8.0F, 4.0F, 0, () -> Ingredient.EMPTY, true, false),
   DIFFERENT(3, 1000, 8.0F, 6.0F, 0, () -> Ingredient.EMPTY, true, false),
   DEVELOPER(10, 1, 10.0F, 100.0F, 999, () -> Ingredient.EMPTY, false, false);

   private final int harvestLevel;
   private final int maxUses;
   private final float efficiency;
   private final float attackDamage;
   private final int enchantability;
   private final boolean compatWithFalsite;
   private final boolean compatWithHandle;
   private final Lazy<Ingredient> repairMaterial;

   SkiesItemTier(
      int harvestLevelIn,
      int maxUsesIn,
      float efficiencyIn,
      float attackDamageIn,
      int enchantabilityIn,
      Supplier<Ingredient> repairMaterialIn,
      boolean compatWithFalsite,
      boolean compatWithHandle
   ) {
      this.harvestLevel = harvestLevelIn;
      this.maxUses = maxUsesIn;
      this.efficiency = efficiencyIn;
      this.attackDamage = attackDamageIn;
      this.enchantability = enchantabilityIn;
      this.repairMaterial = Lazy.of(repairMaterialIn);
      this.compatWithFalsite = compatWithFalsite;
      this.compatWithHandle = compatWithHandle;
   }

   public int getUses() {
      return this.maxUses;
   }

   public float getSpeed() {
      return this.efficiency;
   }

   public float getAttackDamageBonus() {
      return this.attackDamage;
   }

   public int getLevel() {
      return this.harvestLevel;
   }

   public int getEnchantmentValue() {
      return this.enchantability;
   }

   public Ingredient getRepairIngredient() {
      return (Ingredient)this.repairMaterial.get();
   }

   public boolean isCompatWithFalsite() {
      return this.compatWithFalsite;
   }

   public boolean isCompatWithHandle() {
      return this.compatWithHandle;
   }
}
