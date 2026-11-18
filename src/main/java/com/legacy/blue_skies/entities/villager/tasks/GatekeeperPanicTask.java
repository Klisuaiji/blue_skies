package com.legacy.blue_skies.entities.villager.tasks;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class GatekeeperPanicTask extends Behavior<GatekeeperEntity> {
   public GatekeeperPanicTask() {
      super(ImmutableMap.of());
   }

   protected boolean canStillUse(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      return hasBeenHurt(entityIn) || hostileNearby(entityIn);
   }

   protected void start(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      if (hasBeenHurt(entityIn) || hostileNearby(entityIn)) {
         Brain<?> brain = entityIn.getBrain();
         if (!brain.isActive(Activity.PANIC)) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            brain.eraseMemory(MemoryModuleType.BREED_TARGET);
            brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
         }

         brain.setActiveActivityIfPossible(Activity.PANIC);
      }
   }

   public static boolean hostileNearby(LivingEntity p_220513_0_) {
      return p_220513_0_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
   }

   public static boolean hasBeenHurt(LivingEntity p_220512_0_) {
      return p_220512_0_.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
   }
}
