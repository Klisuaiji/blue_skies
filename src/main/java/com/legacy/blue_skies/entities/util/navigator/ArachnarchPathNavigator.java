package com.legacy.blue_skies.entities.util.navigator;

import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.structure_gel.api.util.Positions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

public class ArachnarchPathNavigator extends GroundPathNavigation {
   private BlockPos targetPosition;
   private ArachnarchEntity spiderBoss;

   public ArachnarchPathNavigator(ArachnarchEntity entityLivingIn, Level worldIn) {
      super(entityLivingIn, worldIn);
      this.spiderBoss = entityLivingIn;
   }

   public Path createPath(BlockPos pos, int p_179680_2_) {
      this.targetPosition = pos;
      return super.createPath(pos, p_179680_2_);
   }

   public Path getPathToEntityLiving(Entity entityIn, int p_75494_2_) {
      this.targetPosition = entityIn.blockPosition();
      return super.createPath(entityIn, p_75494_2_);
   }

   public boolean moveTo(Entity entityIn, double speedIn) {
      Path path = this.getPathToEntityLiving(entityIn, 0);
      if (path != null) {
         return this.moveTo(path, speedIn);
      }

      this.targetPosition = entityIn.blockPosition();
      this.speedModifier = speedIn;
      return true;
   }

   public void tick() {
      if (!this.spiderBoss.isCeilingLocked() && !this.spiderBoss.isCharging()) {
         if (!this.isDone()) {
            super.tick();
         } else if (this.targetPosition != null && !this.spiderBoss.isCeilingLocked()) {
            if (this.targetPosition.closerToCenterThan(this.mob.position(), this.mob.getBbWidth())
               || this.mob.getY() > this.targetPosition.getY()
                  && Positions.blockPos(this.targetPosition.getX(), this.mob.getY(), this.targetPosition.getZ())
                     .closerToCenterThan(this.mob.position(), this.mob.getBbWidth())) {
               this.targetPosition = null;
            } else {
               this.mob
                  .getMoveControl()
                  .setWantedPosition(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speedModifier);
            }
         }
      }
   }
}
