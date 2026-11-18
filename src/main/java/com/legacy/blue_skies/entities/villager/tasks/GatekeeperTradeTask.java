package com.legacy.blue_skies.entities.villager.tasks;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;

public class GatekeeperTradeTask extends Behavior<GatekeeperEntity> {
   private final float speedModifier;

   public GatekeeperTradeTask(float p_i50359_1_) {
      super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Integer.MAX_VALUE);
      this.speedModifier = p_i50359_1_;
   }

   protected boolean checkExtraStartConditions(ServerLevel worldIn, GatekeeperEntity owner) {
      Player playerentity = owner.getTradingPlayer();
      return owner.isAlive()
         && playerentity != null
         && !owner.isInWater()
         && !owner.hurtMarked
         && owner.distanceToSqr(playerentity) <= 16.0
         && playerentity.containerMenu != null;
   }

   protected boolean canStillUse(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      return this.checkExtraStartConditions(worldIn, entityIn);
   }

   protected void start(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      this.followPlayer(entityIn);
   }

   protected void stop(ServerLevel worldIn, GatekeeperEntity entityIn, long gameTimeIn) {
      Brain<?> brain = entityIn.getBrain();
      brain.eraseMemory(MemoryModuleType.WALK_TARGET);
      brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
   }

   protected void tick(ServerLevel worldIn, GatekeeperEntity owner, long gameTime) {
      this.followPlayer(owner);
   }

   protected boolean timedOut(long gameTime) {
      return false;
   }

   private void followPlayer(GatekeeperEntity p_220475_1_) {
      EntityTracker entityposwrapper = new EntityTracker(p_220475_1_.getTradingPlayer(), true);
      Brain<?> brain = p_220475_1_.getBrain();
      brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(entityposwrapper, this.speedModifier, 2));
      brain.setMemory(MemoryModuleType.LOOK_TARGET, entityposwrapper);
   }
}
