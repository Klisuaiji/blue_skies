package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.entities.util.base.SkiesIllagerBossEntity;
import net.minecraft.world.entity.monster.SpellcasterIllager;

public class SpellcasterIllagerHooks {
   public static boolean shouldCancelParticles(SpellcasterIllager illager) {
      return illager instanceof SkiesIllagerBossEntity;
   }
}
