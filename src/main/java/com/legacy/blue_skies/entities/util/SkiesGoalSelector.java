package com.legacy.blue_skies.entities.util;

import java.util.function.Supplier;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;

public class SkiesGoalSelector extends GoalSelector {
   public boolean locked = true;

   public SkiesGoalSelector(Supplier<ProfilerFiller> profiler) {
      super(profiler);
   }

   public void addGoal(int pPriority, Goal pGoal) {
      if (!this.locked) {
         super.addGoal(pPriority, pGoal);
      }
   }

   public void removeGoal(Goal pGoal) {
      if (!this.locked) {
         super.removeGoal(pGoal);
      }
   }

   public void addSkyGoal(int pPriority, Goal pGoal) {
      super.addGoal(pPriority, pGoal);
   }

   public void removeSkyGoal(Goal pGoal) {
      super.removeGoal(pGoal);
   }
}
