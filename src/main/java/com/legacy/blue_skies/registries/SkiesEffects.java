package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesEffects {
   public static final MobEffect DEADLY_VENOM = new MobEffect(MobEffectCategory.HARMFUL, 9481987) {};

   public static void init(RegisterEvent event) {
      register(event, "deadly_venom", DEADLY_VENOM);
   }

   private static void register(RegisterEvent event, String name, MobEffect type) {
      event.register(Registries.MOB_EFFECT, BlueSkies.locate(name), () -> type);
   }
}
