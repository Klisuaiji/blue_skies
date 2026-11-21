package com.legacy.blue_skies.compat;

import net.minecraft.world.phys.Vec3;

/** PortalInfo was removed in 1.21.1. */
public final class PortalInfo {
   public final Vec3 pos;
   public final Vec3 speed;
   public final float yRot;
   public final float xRot;
   public PortalInfo(Vec3 pos, Vec3 speed, float yRot, float xRot) {
      this.pos = pos; this.speed = speed; this.yRot = yRot; this.xRot = xRot;
   }
}
