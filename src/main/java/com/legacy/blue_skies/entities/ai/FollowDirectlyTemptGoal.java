package com.legacy.blue_skies.entities.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class FollowDirectlyTemptGoal extends TemptGoal {
   private final double speed;

   public FollowDirectlyTemptGoal(PathfinderMob creatureIn, double speedIn, Ingredient temptItemsIn) {
      super(creatureIn, speedIn, temptItemsIn, false);
      this.speed = speedIn;
   }

   public void tick() {
      this.mob.getLookControl().setLookAt(this.player, this.mob.getMaxHeadYRot() + 20, this.mob.getMaxHeadXRot());
      if (this.mob.distanceToSqr(this.player) < 6.25) {
         this.mob.getNavigation().stop();
      } else if (this.mob.getSensing().hasLineOfSight(this.player) && !this.mob.horizontalCollision) {
         this.mob.getMoveControl().setWantedPosition(this.player.getX(), this.player.getY(), this.player.getZ(), this.speed);
      } else {
         this.mob.getNavigation().moveTo(this.player, this.speed);
      }
   }
}
