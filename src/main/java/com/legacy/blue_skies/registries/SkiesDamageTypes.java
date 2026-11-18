package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.structure_gel.api.registry.RegistrarHolder;
import com.legacy.structure_gel.api.registry.registrar.RegistrarHandler;
import com.legacy.structure_gel.api.registry.registrar.Registrar.Pointer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;

@RegistrarHolder
public class SkiesDamageTypes {
   public static final RegistrarHandler<DamageType> HANDLER = RegistrarHandler.getOrCreate(Registries.DAMAGE_TYPE, "blue_skies");
   public static final Pointer<DamageType> CRUSHER_ROOT = create("root");
   public static final Pointer<DamageType> FALLING_SPIKE = create("rock_stab");
   public static final Pointer<DamageType> FALLING_SPIKE_INDIRECT = create("rock_stab.mob");
   public static final Pointer<DamageType> SUMMON = create("summon");
   public static final Pointer<DamageType> EMBER = create("ember");
   public static final Pointer<DamageType> BITE = create("bite");
   public static final Pointer<DamageType> STRANGE_LIGHTNING = create("summoned_lightning");
   public static final Pointer<DamageType> FLUCTUANT_SPHERE = create("fluctuant_sphere");
   public static final Pointer<DamageType> VENOM_SPIT = create("venom_spit");
   public static final Pointer<DamageType> VENOM_SPIT_INDIRECT = create("venom_spit.mob");
   public static final Pointer<DamageType> SLAM = create("slam");

   private static Pointer<DamageType> create(String name) {
      return create(name, 0.1F);
   }

   private static Pointer<DamageType> create(String name, float exhaustion) {
      return HANDLER.createPointer(name, () -> new DamageType(BlueSkies.find(name), exhaustion));
   }
}
