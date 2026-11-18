package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.entities.util.damage_source.SummonedDamageSource;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SkiesDamageSources {
   private static Map<ResourceKey<Level>, SkiesDamageSources> instances = new HashMap<>();
   private final DamageSource summon;
   private final Registry<DamageType> damageTypes;

   public SkiesDamageSources(RegistryAccess registryAccess) {
      this.damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
      this.summon = this.source(SkiesDamageTypes.SUMMON.getKey());
   }

   public static SkiesDamageSources get(Level level) {
      return instances.computeIfAbsent(level.dimension(), k -> new SkiesDamageSources(level.registryAccess()));
   }

   public static SkiesDamageSources get(Entity entityToHurt) {
      return get(entityToHurt.level());
   }

   public static void create(Level level) {
      instances.put(level.dimension(), new SkiesDamageSources(level.registryAccess()));
   }

   private DamageSource source(ResourceKey<DamageType> type) {
      return new DamageSource(this.damageTypes.getHolderOrThrow(type));
   }

   private DamageSource source(ResourceKey<DamageType> type, @Nullable Entity directEntity) {
      return new DamageSource(this.damageTypes.getHolderOrThrow(type), directEntity);
   }

   private DamageSource source(ResourceKey<DamageType> type, @Nullable Entity directEntity, @Nullable Entity causer) {
      return new DamageSource(this.damageTypes.getHolderOrThrow(type), directEntity, causer);
   }

   public DamageSource crusherRoot(@Nullable Entity root, @Nullable Entity caster) {
      return this.source(SkiesDamageTypes.CRUSHER_ROOT.getKey(), root, caster);
   }

   public DamageSource fallingSpike(@Nullable Entity spike, @Nullable Entity dropper) {
      return this.source(dropper != null ? SkiesDamageTypes.FALLING_SPIKE_INDIRECT.getKey() : SkiesDamageTypes.FALLING_SPIKE.getKey(), spike, dropper);
   }

   public DamageSource summon(@Nonnull Entity directEntity, @Nonnull LivingEntity summoner) {
      return new SummonedDamageSource(this.summon.typeHolder(), directEntity, summoner);
   }

   public DamageSource ember(@Nullable Entity directEntity) {
      return this.source(SkiesDamageTypes.EMBER.getKey(), directEntity);
   }

   public DamageSource bite(@Nullable Entity directEntity) {
      return this.source(SkiesDamageTypes.BITE.getKey(), directEntity);
   }

   public DamageSource strangeLightning(@Nullable Entity bolt, @Nullable Entity caster) {
      return this.source(SkiesDamageTypes.STRANGE_LIGHTNING.getKey(), bolt, caster);
   }

   public DamageSource fluctuantSphere(@Nullable Entity sphere, @Nullable Entity caster) {
      return this.source(SkiesDamageTypes.FLUCTUANT_SPHERE.getKey(), sphere, caster);
   }

   public DamageSource venomSpit() {
      return this.venomSpit(null, null);
   }

   public DamageSource venomSpit(@Nullable Entity spit, @Nullable Entity shooter) {
      return this.source(shooter != null ? SkiesDamageTypes.VENOM_SPIT_INDIRECT.getKey() : SkiesDamageTypes.VENOM_SPIT.getKey(), spit, shooter);
   }

   public DamageSource slam(@Nullable Entity directEntity) {
      return this.source(SkiesDamageTypes.SLAM.getKey(), null, directEntity);
   }
}
