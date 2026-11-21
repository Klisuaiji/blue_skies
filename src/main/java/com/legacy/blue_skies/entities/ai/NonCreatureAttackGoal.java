package com.legacy.blue_skies.entities.ai;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class NonCreatureAttackGoal extends Goal {
   protected final Mob mob;
   protected int attackTick;
   private final double speedTowardsTarget;
   private final boolean longMemory;
   private Path path;
   private int delayCounter;
   private double targetX;
   private double targetY;
   private double targetZ;
   protected final int attackInterval = 20;
   private long lastCanUseCheck;
   private int failedPathFindingPenalty = 0;
   private boolean canPenalize = false;

   public NonCreatureAttackGoal(Mob creature, double speedIn, boolean useLongMemory) {
      this.mob = creature;
      this.speedTowardsTarget = speedIn;
      this.longMemory = useLongMemory;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
   }

   public boolean canUse() {
      long i = this.mob.level().getGameTime();
      if (i - this.lastCanUseCheck < 20L) {
         return false;
      }

      this.lastCanUseCheck = i;
      LivingEntity livingentity = this.mob.getTarget();
      if (livingentity == null) {
         return false;
      }

      if (!livingentity.isAlive()) {
         return false;
      }

      if (this.canPenalize) {
         if (--this.delayCounter <= 0) {
            this.path = this.mob.getNavigation().createPath(livingentity, 0);
            this.delayCounter = 4 + this.mob.getRandom().nextInt(7);
            return this.path != null;
         } else {
            return true;
         }
      } else {
         this.path = this.mob.getNavigation().createPath(livingentity, 0);
         return this.path != null
            ? true
            : this.getAttackReachSqr(livingentity) >= this.mob.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ());
      }
   }

   public boolean canContinueToUse() {
      LivingEntity livingentity = this.mob.getTarget();
      if (livingentity == null) {
         return false;
      } else if (!livingentity.isAlive()) {
         return false;
      } else if (!this.longMemory) {
         return !this.mob.getNavigation().isDone();
      } else {
         return !this.mob.isWithinRestriction(livingentity.blockPosition())
            ? false
            : !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
      }
   }

   public void start() {
      this.mob.getNavigation().moveTo(this.path, this.speedTowardsTarget);
      this.mob.setAggressive(true);
      this.delayCounter = 0;
   }

   public void stop() {
      LivingEntity livingentity = this.mob.getTarget();
      if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
         this.mob.setTarget((LivingEntity)null);
      }

      this.mob.setAggressive(false);
      this.mob.getNavigation().stop();
   }

   public void tick() {
      LivingEntity livingentity = this.mob.getTarget();
      this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
      double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getBoundingBox().minY, livingentity.getZ());
      this.delayCounter--;
      if ((this.longMemory || this.mob.getSensing().hasLineOfSight(livingentity))
         && this.delayCounter <= 0
         && (
            this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0
               || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0
               || this.mob.getRandom().nextFloat() < 0.05F
         )) {
         this.targetX = livingentity.getX();
         this.targetY = livingentity.getBoundingBox().minY;
         this.targetZ = livingentity.getZ();
         this.delayCounter = 4 + this.mob.getRandom().nextInt(7);
         if (this.canPenalize) {
            this.delayCounter = this.delayCounter + this.failedPathFindingPenalty;
            if (this.mob.getNavigation().getPath() != null) {
               Node finalPathPoint = this.mob.getNavigation().getPath().getEndNode();
               if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1.0) {
                  this.failedPathFindingPenalty = 0;
               } else {
                  this.failedPathFindingPenalty += 10;
               }
            } else {
               this.failedPathFindingPenalty += 10;
            }
         }

         if (d0 > 1024.0) {
            this.delayCounter += 10;
         } else if (d0 > 256.0) {
            this.delayCounter += 5;
         }

         if (!this.mob.getNavigation().moveTo(livingentity, this.speedTowardsTarget)) {
            this.delayCounter += 15;
         }
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      this.checkAndPerformAttack(livingentity, d0);
   }

   protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
      double d0 = this.getAttackReachSqr(enemy);
      if (distToEnemySqr <= d0 && this.attackTick <= 0) {
         this.attackTick = 20;
         this.mob.swing(InteractionHand.MAIN_HAND);
         this.mob.doHurtTarget(enemy);
      }
   }

   protected double getAttackReachSqr(LivingEntity attackTarget) {
      return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + attackTarget.getBbWidth();
   }
}
