package com.legacy.blue_skies.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;

public class CrusherDustParticle extends DustParticle {
   protected CrusherDustParticle(ClientLevel world, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
      super(world, x, y, z, dx, dy, dz, spriteSet);
      this.hasPhysics = false;
      if (this.yd < 0.0) {
         this.yd = 0.0;
      }
   }

   @Override
   public float getBaseVelocityMultiplier() {
      return 15.0F;
   }

   @Override
   public float getTickVelocityMultiplier() {
      return 0.75F;
   }
}
