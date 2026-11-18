package com.legacy.blue_skies.items.util;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public class PreparedFood {
   private final List<ItemStack> ingredients;
   private final List<MobEffectInstance> effects;
   private final int totalHunger;
   private int uses = 0;
   private final int maxUses;
   private final float saturation;

   public PreparedFood(List<ItemStack> ingredients, List<MobEffectInstance> effects, int totalHunger, int uses, int maxUses, float saturation) {
      this.ingredients = ingredients;
      this.effects = effects;
      this.totalHunger = totalHunger;
      this.uses = uses;
      this.maxUses = maxUses;
      this.saturation = saturation;
   }

   public List<ItemStack> getIngredients() {
      return this.ingredients;
   }

   public List<MobEffectInstance> getEffects() {
      return this.effects;
   }

   public int getUses() {
      return this.uses;
   }

   public void use() {
      this.uses--;
   }

   public int getMaxUses() {
      return this.maxUses;
   }

   public int getTotalHunger() {
      return this.totalHunger;
   }

   public int getHungerPerBite() {
      return Math.max(1, (int)Math.ceil((float)this.getTotalHunger() / this.getMaxUses()));
   }

   public float getSaturation() {
      return this.saturation;
   }
}
