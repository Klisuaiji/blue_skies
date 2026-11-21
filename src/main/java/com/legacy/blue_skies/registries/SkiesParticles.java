package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.particle.BriskPetalParticle;
import com.legacy.blue_skies.client.particle.DuskParticle;
import com.legacy.blue_skies.client.particle.DustParticle;
import com.legacy.blue_skies.client.particle.EverbrightPortalParticle;
import com.legacy.blue_skies.client.particle.EverdawnPortalParticle;
import com.legacy.blue_skies.client.particle.FallingLeafParticle;
import com.legacy.blue_skies.client.particle.FluctuantSphereBeamParticle;
import com.legacy.blue_skies.client.particle.FluctuantSphereParticle;
import com.legacy.blue_skies.client.particle.FroseSnowParticle;
import com.legacy.blue_skies.client.particle.PoisonParticle;
import com.legacy.blue_skies.client.particle.SearingSmokeParticle;
import com.legacy.blue_skies.client.particle.SkyFlameParticle;
import com.legacy.blue_skies.client.particle.StarFlareParticle;
import com.legacy.blue_skies.client.particle.StarGlimmerParticle;
import com.legacy.blue_skies.client.particle.data.PoisonData;
import com.legacy.blue_skies.client.particle.data.StarFlareData;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesParticles {
   public static final SimpleParticleType BLUE_FLAME = new SimpleParticleType(false) {};
   public static final SimpleParticleType BLACK_FLAME = new SimpleParticleType(false) {};
   public static final SimpleParticleType EVERBRIGHT_PORTAL = new SimpleParticleType(false) {};
   public static final SimpleParticleType EVERDAWN_PORTAL = new SimpleParticleType(false) {};
   public static final ParticleType<PoisonData> POISON = new ParticleType<PoisonData>(false, PoisonData.DESERIALIZER) {
      public Codec<PoisonData> codec() {
         return PoisonData.CODEC;
      }
   };
   public static final ParticleType<StarFlareData> STAR_FLARE = new ParticleType<StarFlareData>(true, StarFlareData.DESERIALIZER) {
      public Codec<StarFlareData> codec() {
         return StarFlareData.CODEC;
      }
   };
   public static final SimpleParticleType DUSK_SMOKE = new SimpleParticleType(false) {};
   public static final SimpleParticleType FLUCTUANT_SPHERE = new SimpleParticleType(false) {};
   public static final SimpleParticleType FLUCTUANT_SPHERE_BEAM = new SimpleParticleType(false) {};
   public static final SimpleParticleType FALLING_LEAF = new SimpleParticleType(false) {};
   public static final SimpleParticleType DUST = new SimpleParticleType(false) {};
   public static final SimpleParticleType CRUSHER_DUST = new SimpleParticleType(false) {};
   public static final SimpleParticleType BRISK_PETAL = new SimpleParticleType(false) {};
   public static final SimpleParticleType FROSE_SNOW = new SimpleParticleType(false) {};
   public static final SimpleParticleType SEARING_SMOKE = new SimpleParticleType(false) {};
   public static final SimpleParticleType STAR_GLIMMER = new SimpleParticleType(false) {};

   public static void init(RegisterEvent event) {
      register(event, "blue_flame", BLUE_FLAME);
      register(event, "black_flame", BLACK_FLAME);
      register(event, "everbright_portal", EVERBRIGHT_PORTAL);
      register(event, "everdawn_portal", EVERDAWN_PORTAL);
      register(event, "star_flare", STAR_FLARE);
      register(event, "poison", POISON);
      register(event, "dusk_smoke", DUSK_SMOKE);
      register(event, "fluctuant_sphere", FLUCTUANT_SPHERE);
      register(event, "fluctuant_sphere_beam", FLUCTUANT_SPHERE_BEAM);
      register(event, "falling_leaf", FALLING_LEAF);
      register(event, "dust", DUST);
      register(event, "crusher_dust", CRUSHER_DUST);
      register(event, "brisk_petal", BRISK_PETAL);
      register(event, "frose_snow", FROSE_SNOW);
      register(event, "searing_smoke", SEARING_SMOKE);
      register(event, "star_glimmer", STAR_GLIMMER);
   }

   private static void register(RegisterEvent event, String key, ParticleType<?> particle) {
      event.register(Registries.PARTICLE_TYPE, BlueSkies.locate(key), () -> particle);
   }

   public static void registerFactories(RegisterParticleProvidersEvent event) {
      event.registerSpriteSet(BLUE_FLAME, SkyFlameParticle.Factory::new);
      event.registerSpriteSet(BLACK_FLAME, SkyFlameParticle.Factory::new);
      event.registerSpriteSet(EVERBRIGHT_PORTAL, EverbrightPortalParticle.Factory::new);
      event.registerSpriteSet(EVERDAWN_PORTAL, EverdawnPortalParticle.Factory::new);
      event.registerSpriteSet(POISON, PoisonParticle.Factory::new);
      event.registerSpriteSet(STAR_FLARE, StarFlareParticle.Factory::new);
      event.registerSpriteSet(DUSK_SMOKE, DuskParticle.Factory::new);
      event.registerSpriteSet(FLUCTUANT_SPHERE, FluctuantSphereParticle.Factory::new);
      event.registerSpriteSet(FLUCTUANT_SPHERE_BEAM, FluctuantSphereBeamParticle.Factory::new);
      event.registerSpriteSet(FALLING_LEAF, FallingLeafParticle.Factory::new);
      event.registerSpriteSet(DUST, DustParticle.Factory::new);
      event.registerSpriteSet(CRUSHER_DUST, DustParticle.Factory::new);
      event.registerSpriteSet(BRISK_PETAL, BriskPetalParticle.Factory::new);
      event.registerSpriteSet(FROSE_SNOW, FroseSnowParticle.Factory::new);
      event.registerSpriteSet(SEARING_SMOKE, SearingSmokeParticle.Factory::new);
      event.registerSpriteSet(STAR_GLIMMER, StarGlimmerParticle.Factory::new);
   }

   public static class Register {
      @SubscribeEvent
      public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
         SkiesParticles.registerFactories(event);
      }
   }
}
