package com.legacy.blue_skies.entities.util;

import com.legacy.blue_skies.entities.util.interfaces.IStunnableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class StunnableLookController extends LookControl {
   public StunnableLookController(Mob mob) {
      super(mob);
   }

   public void setLookAt(double x, double y, double z, float deltaYaw, float deltaPitch) {
      IStunnableMob boss = (IStunnableMob)this.mob;
      if (!boss.isStunned() && this.mob.getPassengers().isEmpty()) {
         super.setLookAt(x, y, z, deltaYaw, deltaPitch);
      }
   }
}
