package com.legacy.blue_skies.entities.util;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;

/**
 * 1.21.1 migration note: {@code Mob.goalSelector}/{@code Mob.targetSelector} are final fields now,
 * so the 1.20.4 trick of swapping the whole selector for a "locked" subclass no longer works.
 * This class is a thin wrapper around the mob's real {@link GoalSelector}: it keeps the original
 * locked/addSkyGoal API used by the boss entities and simply delegates to the vanilla selector.
 */
public class SkiesGoalSelector {
   private final GoalSelector delegate;
   public boolean locked = true;

   public SkiesGoalSelector(GoalSelector delegate) {
      this.delegate = delegate;
   }

   public void addGoal(int priority, Goal goal) {
      if (!this.locked) {
         this.delegate.addGoal(priority, goal);
      }
   }

   public void removeGoal(Goal goal) {
      if (!this.locked) {
         this.delegate.removeGoal(goal);
      }
   }

   public void addSkyGoal(int priority, Goal goal) {
      this.delegate.addGoal(priority, goal);
   }

   public void removeSkyGoal(Goal goal) {
      this.delegate.removeGoal(goal);
   }

   /** Used by boss base classes to reproduce 1.20.4's "fresh selector" behaviour. */
   public void removeAllGoals() {
      this.delegate.removeAllGoals(goal -> true);
   }
}
