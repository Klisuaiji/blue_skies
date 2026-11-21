package com.legacy.blue_skies.entities.util.base;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class SkiesAnimalEntity extends Animal {
   protected SkiesAnimalEntity(EntityType<? extends SkiesAnimalEntity> type, Level worldIn) {
      super(type, worldIn);
   }

   protected void registerGoals() {
      super.registerGoals();
   }

   public boolean isFood(ItemStack stack) {
      return this.getMainBreedingItem() != null && stack.getItem() == this.getMainBreedingItem() || super.isFood(stack);
   }

   @Nullable
   public Item getMainBreedingItem() {
      return null;
   }

   public abstract AgeableMob createChild(AgeableMob var1);

   public AgeableMob getBreedOffspring(ServerLevel worldIn, AgeableMob ageableIn) {
      return this.createChild(ageableIn);
   }
}
