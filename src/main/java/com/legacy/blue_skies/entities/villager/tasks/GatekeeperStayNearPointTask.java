package com.legacy.blue_skies.entities.villager.tasks;

import com.google.common.collect.ImmutableMap;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.legacy.structure_gel.api.util.Positions;
import java.util.Optional;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class GatekeeperStayNearPointTask extends Behavior<GatekeeperEntity> {
   private final MemoryModuleType<GlobalPos> memoryType;
   private final float speedModifier;
   private final int closeEnoughDist;
   private final int tooFarDistance;
   private final int tooLongUnreachableDuration;

   public GatekeeperStayNearPointTask(MemoryModuleType<GlobalPos> p_i51501_1_, float p_i51501_2_, int p_i51501_3_, int p_i51501_4_, int p_i51501_5_) {
      super(
         ImmutableMap.of(
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryStatus.REGISTERED,
            MemoryModuleType.WALK_TARGET,
            MemoryStatus.VALUE_ABSENT,
            p_i51501_1_,
            MemoryStatus.VALUE_PRESENT
         )
      );
      this.memoryType = p_i51501_1_;
      this.speedModifier = p_i51501_2_;
      this.closeEnoughDist = p_i51501_3_;
      this.tooFarDistance = p_i51501_4_;
      this.tooLongUnreachableDuration = p_i51501_5_;
   }

   private void dropPOI(GatekeeperEntity p_225457_1_, long p_225457_2_) {
      Brain<?> brain = p_225457_1_.getBrain();
      p_225457_1_.releasePoi(this.memoryType);
      brain.eraseMemory(this.memoryType);
      brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, p_225457_2_);
   }

   protected void start(ServerLevel worldIn, GatekeeperEntity entity, long gameTimeIn) {
      Brain<?> brain = entity.getBrain();
      brain.getMemory(this.memoryType)
         .ifPresent(
            memoryPos -> {
               if (this.tiredOfTryingToFindTarget(worldIn, entity)) {
                  this.dropPOI(entity, gameTimeIn);
               } else if (this.wrongDimension(worldIn, entity, memoryPos)) {
                  Vec3 vec3d = null;

                  int i;
                  for (i = 0;
                     i < 1000 && (vec3d == null || this.wrongDimension(worldIn, entity, GlobalPos.of(entity.level().dimension(), Positions.blockPos(vec3d))));
                     i++
                  ) {
                     vec3d = DefaultRandomPos.getPosTowards(entity, 15, 7, Vec3.atBottomCenterOf(memoryPos.pos()), (float) (Math.PI / 2));
                  }

                  if (i == 1000) {
                     this.dropPOI(entity, gameTimeIn);
                     return;
                  }

                  brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3d, this.speedModifier, this.closeEnoughDist));
               } else if (!this.closeEnough(worldIn, entity, memoryPos)) {
                  brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(memoryPos.pos(), this.speedModifier, this.closeEnoughDist));
               }
            }
         );
   }

   private boolean tiredOfTryingToFindTarget(ServerLevel level, GatekeeperEntity entity) {
      Optional<Long> optional = entity.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
      return optional.isPresent() ? level.getGameTime() - optional.get() > this.tooLongUnreachableDuration : false;
   }

   private boolean wrongDimension(ServerLevel level, GatekeeperEntity entity, GlobalPos memoryPos) {
      return memoryPos.dimension() != level.dimension() || memoryPos.pos().distManhattan(entity.blockPosition()) > this.tooFarDistance;
   }

   private boolean closeEnough(ServerLevel level, GatekeeperEntity entity, GlobalPos memoryPos) {
      return memoryPos.dimension() == level.dimension() && memoryPos.pos().distManhattan(entity.blockPosition()) <= this.closeEnoughDist;
   }
}
