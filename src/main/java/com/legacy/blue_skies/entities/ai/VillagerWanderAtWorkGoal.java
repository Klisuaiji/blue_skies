package com.legacy.blue_skies.entities.ai;

import com.legacy.blue_skies.registries.SkiesVillagers;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class VillagerWanderAtWorkGoal extends WaterAvoidingRandomStrollGoal {
   private final Villager villager;

   public VillagerWanderAtWorkGoal(Villager creature, double speedIn) {
      super(creature, speedIn, 0.1F);
      this.villager = creature;
   }

   public boolean canUse() {
      if (this.villager.getVillagerData().getProfession() != SkiesVillagers.NIGHTWATCHER.get()
         && this.villager.getVillagerData().getProfession() != SkiesVillagers.SHOVELER.get()) {
         return false;
      } else {
         double jobPosDistance = this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent()
            ? EntityUtil.getDistanceToPos(this.villager.blockPosition(), ((GlobalPos)this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()).pos())
            : 0.0;
         return jobPosDistance > 30.0 ? false : this.isWorkTime() && !this.villager.getMoveControl().hasWanted() && super.canUse();
      }
   }

   public boolean canContinueToUse() {
      if (this.villager.getVillagerData().getProfession() != SkiesVillagers.NIGHTWATCHER.get()
         && this.villager.getVillagerData().getProfession() != SkiesVillagers.SHOVELER.get()) {
         return false;
      } else {
         double jobPosDistance = this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent()
            ? EntityUtil.getDistanceToPos(this.villager.blockPosition(), ((GlobalPos)this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()).pos())
            : 0.0;
         return jobPosDistance > 30.0 ? false : this.isWorkTime() && super.canContinueToUse();
      }
   }

   private boolean isWorkTime() {
      return EntityUtil.isVillagerWorkTime(this.villager);
   }
}
