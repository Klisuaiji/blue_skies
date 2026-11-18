package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.registries.SkiesVillagers;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class SetWalkTargetFromBlockMemoryHooks {
   public static boolean canContinueToWander(Villager entity, MemoryModuleType<GlobalPos> memoryType) {
      if (entity.getVillagerData().getProfession() == SkiesVillagers.SHOVELER.get()
         || entity.getVillagerData().getProfession() == SkiesVillagers.NIGHTWATCHER.get()) {
         double jobPosDistance = entity.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent()
            ? EntityUtil.getDistanceToPos(entity.blockPosition(), ((GlobalPos)entity.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()).pos())
            : 0.0;
         if (memoryType == MemoryModuleType.JOB_SITE && jobPosDistance < 40.0) {
            return true;
         }
      }

      return false;
   }
}
