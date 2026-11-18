package com.legacy.blue_skies.entities.villager.tasks;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class GatekeeperClearHurtTask extends Behavior<GatekeeperEntity> {
   public GatekeeperClearHurtTask() {
      super(ImmutableMap.of());
   }

   protected void start(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      boolean flag = GatekeeperPanicTask.hasBeenHurt(entityIn) || GatekeeperPanicTask.hostileNearby(entityIn) || isCloseToEntityThatHurtMe(entityIn);
      if (!flag) {
         entityIn.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
         entityIn.getBrain().eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
         entityIn.getBrain().updateActivityFromSchedule(worldIn.getDayTime(), worldIn.getGameTime());
      }
   }

   private static boolean isCloseToEntityThatHurtMe(GatekeeperEntity p_220394_0_) {
      return p_220394_0_.getBrain()
         .getMemory(MemoryModuleType.HURT_BY_ENTITY)
         .filter(p_223523_1_ -> p_223523_1_.distanceToSqr(p_220394_0_) <= 36.0)
         .isPresent();
   }
}
