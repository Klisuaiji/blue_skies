package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.structure_gel.api.util.Positions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;

public class ClientLevelHooks {
   public static boolean shouldCancelParticle(ParticleOptions particleData, double x, double y, double z, ClientLevel level) {
      return particleData.equals(SkiesParticles.BRISK_PETAL) && !level.canSeeSky(Positions.blockPos(x, y, z))
         ? true
         : particleData.equals(SkiesParticles.SEARING_SMOKE) && y > 55.0;
   }
}
