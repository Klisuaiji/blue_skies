package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.entities.BossItemEntity;
import com.legacy.blue_skies.entities.SupporterPetEntity;
import com.legacy.blue_skies.entities.hostile.ArmoredFrostSpiritEntity;
import com.legacy.blue_skies.entities.hostile.CrynocerousEntity;
import com.legacy.blue_skies.entities.hostile.DiophydeProwlerEntity;
import com.legacy.blue_skies.entities.hostile.EmberbackEntity;
import com.legacy.blue_skies.entities.hostile.FrostSpiritEntity;
import com.legacy.blue_skies.entities.hostile.InfestedSwarmerEntity;
import com.legacy.blue_skies.entities.hostile.NyctoflyEntity;
import com.legacy.blue_skies.entities.hostile.PolargeistEntity;
import com.legacy.blue_skies.entities.hostile.SeclamEntity;
import com.legacy.blue_skies.entities.hostile.ShrumptyEntity;
import com.legacy.blue_skies.entities.hostile.SpewterEntity;
import com.legacy.blue_skies.entities.hostile.StoneletEntity;
import com.legacy.blue_skies.entities.hostile.VenomSpiderEntity;
import com.legacy.blue_skies.entities.hostile.WhistleshellCrabEntity;
import com.legacy.blue_skies.entities.hostile.boss.AlchemistEntity;
import com.legacy.blue_skies.entities.hostile.boss.ArachnarchEntity;
import com.legacy.blue_skies.entities.hostile.boss.StarlitCrusherEntity;
import com.legacy.blue_skies.entities.hostile.boss.SummonerEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ArtificialGolemEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.DecayingSpikeEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.NestedSpiderEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.StrangeLightningEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntRootEntity;
import com.legacy.blue_skies.entities.hostile.boss.summons.ent.EntWallEntity;
import com.legacy.blue_skies.entities.passive.AzulfoEntity;
import com.legacy.blue_skies.entities.passive.CosmicFoxEntity;
import com.legacy.blue_skies.entities.passive.CrogreEntity;
import com.legacy.blue_skies.entities.passive.CrystalCamelEntity;
import com.legacy.blue_skies.entities.passive.FireflyEntity;
import com.legacy.blue_skies.entities.passive.ReindeerEntity;
import com.legacy.blue_skies.entities.passive.ShadeMonitorEntity;
import com.legacy.blue_skies.entities.passive.SlivEntity;
import com.legacy.blue_skies.entities.passive.SnowOwlEntity;
import com.legacy.blue_skies.entities.passive.StardustRamEntity;
import com.legacy.blue_skies.entities.passive.fish.CharscaleMokiEntity;
import com.legacy.blue_skies.entities.passive.fish.GrittleFlatfishEntity;
import com.legacy.blue_skies.entities.passive.fish.HorizofinTunidEntity;
import com.legacy.blue_skies.entities.passive.fish.JellyDrifterEntity;
import com.legacy.blue_skies.entities.passive.fish.MunicipalMonkfishEntity;
import com.legacy.blue_skies.entities.projectile.EntLeafEntity;
import com.legacy.blue_skies.entities.projectile.FluctuantSphereEntity;
import com.legacy.blue_skies.entities.projectile.SeedBombEntity;
import com.legacy.blue_skies.entities.projectile.SpearEntity;
import com.legacy.blue_skies.entities.projectile.VenomBombEntity;
import com.legacy.blue_skies.entities.projectile.VenomSpitEntity;
import com.legacy.blue_skies.entities.villager.GatekeeperEntity;
import com.legacy.blue_skies.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.SpawnPlacements.SpawnPredicate;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesEntityTypes {
   public static final EntityType<AzulfoEntity> AZULFO = buildEntity("azulfo", Builder.of(AzulfoEntity::new, MobCategory.CREATURE).sized(2.0F, 2.3F));
   public static final EntityType<StardustRamEntity> STARDUST_RAM = buildEntity(
      "stardust_ram", Builder.of(StardustRamEntity::new, MobCategory.CREATURE).sized(1.4F, 1.7F)
   );
   public static final EntityType<ReindeerEntity> REINDEER = buildEntity("reindeer", Builder.of(ReindeerEntity::new, MobCategory.CREATURE).sized(1.3F, 1.5F));
   public static final EntityType<JellyDrifterEntity> JELLY_DRIFTER = buildEntity(
      "jelly_drifter", Builder.of(JellyDrifterEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 1.2F)
   );
   public static final EntityType<SnowOwlEntity> SNOW_OWL = buildEntity("snow_owl", Builder.of(SnowOwlEntity::new, MobCategory.CREATURE).sized(0.5F, 0.9F));
   public static final EntityType<FrostSpiritEntity> FROST_SPIRIT = buildEntity(
      "frost_spirit", Builder.of(FrostSpiritEntity::new, MobCategory.AMBIENT).sized(0.6F, 0.6F)
   );
   public static final EntityType<GrittleFlatfishEntity> GRITTLE_FLATFISH = buildEntity(
      "grittle_flatfish", Builder.of(GrittleFlatfishEntity::new, MobCategory.WATER_AMBIENT).sized(1.0F, 0.7F)
   );
   public static final EntityType<MunicipalMonkfishEntity> MUNICIPAL_MONKFISH = buildEntity(
      "municipal_monkfish", Builder.of(MunicipalMonkfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.3F)
   );
   public static final EntityType<ArmoredFrostSpiritEntity> ARMORED_FROST_SPIRIT = buildEntity(
      "armored_frost_spirit", Builder.of(ArmoredFrostSpiritEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F)
   );
   public static final EntityType<CrynocerousEntity> CRYNOCEROUS = buildEntity(
      "crynocerous", Builder.of(CrynocerousEntity::new, MobCategory.MONSTER).sized(2.0F, 2.3F)
   );
   public static final EntityType<PolargeistEntity> POLARGEIST = buildEntity(
      "polargeist", Builder.of(PolargeistEntity::new, MobCategory.MONSTER).sized(1.6F, 1.6F)
   );
   public static final EntityType<StoneletEntity> STONELET = buildEntity("stonelet", Builder.of(StoneletEntity::new, MobCategory.MONSTER).sized(1.3F, 2.0F));
   public static final EntityType<WhistleshellCrabEntity> WHISTLESHELL_CRAB = buildEntity(
      "whistleshell_crab", Builder.of(WhistleshellCrabEntity::new, MobCategory.MONSTER).sized(1.8F, 0.8F)
   );
   public static final EntityType<ShrumptyEntity> SHRUMPTY = buildEntity("shrumpty", Builder.of(ShrumptyEntity::new, MobCategory.MONSTER).sized(0.6F, 1.3F));
   public static final EntityType<DiophydeProwlerEntity> DIOPHYDE_PROWLER = buildEntity(
      "diophyde_prowler", Builder.of(DiophydeProwlerEntity::new, MobCategory.MONSTER).sized(1.6F, 1.6F)
   );
   public static final EntityType<SeclamEntity> SECLAM = buildEntity("seclam", Builder.of(SeclamEntity::new, MobCategory.MONSTER).sized(1.6F, 0.8F));
   public static final EntityType<FireflyEntity> FIREFLY = buildEntity("firefly", Builder.of(FireflyEntity::new, MobCategory.AMBIENT).sized(0.7F, 0.6F));
   public static final EntityType<CosmicFoxEntity> COSMIC_FOX = buildEntity(
      "cosmic_fox", Builder.of(CosmicFoxEntity::new, MobCategory.CREATURE).sized(0.7F, 0.75F)
   );
   public static final EntityType<CrystalCamelEntity> CRYSTAL_CAMEL = buildEntity(
      "crystal_camel", Builder.of(CrystalCamelEntity::new, MobCategory.CREATURE).sized(1.5F, 2.2F)
   );
   public static final EntityType<ShadeMonitorEntity> SHADE_MONITOR = buildEntity(
      "shade_monitor", Builder.of(ShadeMonitorEntity::new, MobCategory.CREATURE).sized(1.8F, 1.0F)
   );
   public static final EntityType<SlivEntity> SLIV = buildEntity("sliv", Builder.of(SlivEntity::new, MobCategory.CREATURE).sized(1.0F, 0.2F));
   public static final EntityType<CrogreEntity> CROGRE = buildEntity("crogre", Builder.of(CrogreEntity::new, MobCategory.CREATURE).sized(1.3F, 0.8F));
   public static final EntityType<CharscaleMokiEntity> CHARSCALE_MOKI = buildEntity(
      "charscale_moki", Builder.of(CharscaleMokiEntity::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.3F)
   );
   public static final EntityType<HorizofinTunidEntity> HORIZOFIN_TUNID = buildEntity(
      "horizofin_tunid", Builder.of(HorizofinTunidEntity::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.3F)
   );
   public static final EntityType<NyctoflyEntity> NYCTOFLY = buildEntity("nyctofly", Builder.of(NyctoflyEntity::new, MobCategory.MONSTER).sized(1.2F, 0.8F));
   public static final EntityType<VenomSpiderEntity> VENOM_SPIDER = buildEntity(
      "venom_spider", Builder.of(VenomSpiderEntity::new, MobCategory.MONSTER).sized(1.4F, 0.9F)
   );
   public static final EntityType<VenomSpitEntity> VENOM_SPIT = buildEntity(
      "venom_spit", Builder.of(VenomSpitEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F)
   );
   public static final EntityType<EmberbackEntity> EMBERBACK = buildEntity(
      "emberback", Builder.of(EmberbackEntity::new, MobCategory.MONSTER).sized(1.4F, 0.9F).fireImmune()
   );
   public static final EntityType<InfestedSwarmerEntity> INFESTED_SWARMER = buildEntity(
      "infested_swarmer", Builder.of(InfestedSwarmerEntity::new, MobCategory.MONSTER).sized(1.7F, 0.9F)
   );
   public static final EntityType<SummonerEntity> SUMMONER = buildEntity(
      "summoner", Builder.of(SummonerEntity::new, MobCategory.MONSTER).sized(0.6F, 2.0F).fireImmune()
   );
   public static final EntityType<ArtificialGolemEntity> ARTIFICIAL_GOLEM = buildEntity(
      "artificial_golem", Builder.of(ArtificialGolemEntity::new, MobCategory.MONSTER).sized(1.0F, 2.1F).fireImmune()
   );
   public static final EntityType<StrangeLightningEntity> STRANGE_LIGHTNING = buildEntity(
      "strange_lightning", Builder.of(StrangeLightningEntity::new, MobCategory.MISC).sized(0.0F, 0.0F)
   );
   public static final EntityType<FluctuantSphereEntity> FLUCTUANT_SPHERE = buildEntity(
      "fluctuant_sphere", Builder.of(FluctuantSphereEntity::new, MobCategory.MISC).fireImmune().setShouldReceiveVelocityUpdates(true).sized(0.7F, 0.7F)
   );
   public static final EntityType<AlchemistEntity> ALCHEMIST = buildEntity(
      "alchemist", Builder.of(AlchemistEntity::new, MobCategory.MONSTER).sized(0.6F, 2.0F).fireImmune()
   );
   public static final EntityType<DecayingSpikeEntity> DECAYING_SPIKE = buildEntity(
      "decaying_spike", Builder.of(DecayingSpikeEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).fireImmune().sized(1.0F, 1.0F)
   );
   public static final EntityType<StarlitCrusherEntity> STARLIT_CRUSHER = buildEntity(
      "starlit_crusher", Builder.of(StarlitCrusherEntity::new, MobCategory.MONSTER).sized(1.6F, 3.2F)
   );
   public static final EntityType<EntRootEntity> ENT_ROOT = buildEntity("ent_root", Builder.of(EntRootEntity::new, MobCategory.MISC).sized(0.5F, 0.8F));
   public static final EntityType<EntWallEntity> ENT_WALL = buildEntity("ent_wall", Builder.of(EntWallEntity::new, MobCategory.MISC).sized(1.0F, 3.0F));
   public static final EntityType<EntLeafEntity> ENT_LEAF = buildEntity("ent_leaf", Builder.of(EntLeafEntity::new, MobCategory.MISC).sized(0.3F, 0.1F));
   public static final EntityType<SpewterEntity> SPEWTER = buildEntity("spewter", Builder.of(SpewterEntity::new, MobCategory.MONSTER).sized(1.0F, 1.2F));
   public static final EntityType<SeedBombEntity> SEED_BOMB = buildEntity(
      "seed_bomb", Builder.of(SeedBombEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F)
   );
   public static final EntityType<ArachnarchEntity> ARACHNARCH = buildEntity(
      "arachnarch", Builder.of(ArachnarchEntity::new, MobCategory.MONSTER).sized(3.0F, 1.7F).fireImmune()
   );
   public static final EntityType<VenomBombEntity> VENOM_BOMB = buildEntity(
      "venom_bomb", Builder.of(VenomBombEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F)
   );
   public static final EntityType<NestedSpiderEntity> NESTED_SPIDER = buildEntity(
      "nested_spider", Builder.of(NestedSpiderEntity::new, MobCategory.MONSTER).sized(0.7F, 0.5F)
   );
   public static final EntityType<GatekeeperEntity> GATEKEEPER = buildEntity(
      "gatekeeper", Builder.of(GatekeeperEntity::new, MobCategory.CREATURE).sized(0.6F, 1.95F)
   );
   public static final EntityType<SpearEntity> SPEAR = buildEntity(
      "spear", Builder.of(SpearEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).sized(0.5F, 0.5F)
   );
   public static final EntityType<SupporterPetEntity> SUPPORTER_PET = buildEntity(
      "supporter_pet", Builder.of(SupporterPetEntity::new, MobCategory.MONSTER).fireImmune().sized(0.8F, 0.8F)
   );
   public static final EntityType<BossItemEntity> BOSS_ITEM = buildEntity(
      "boss_item",
      Builder.of(BossItemEntity::new, MobCategory.MISC).clientTrackingRange(6).updateInterval(20).setShouldReceiveVelocityUpdates(true).sized(0.25F, 0.25F)
   );
   private static RegisterEvent registerEvent;

   public static void init(RegisterEvent event) {
      registerEvent = event;
      register("azulfo", AZULFO);
      register("stardust_ram", STARDUST_RAM);
      register("reindeer", REINDEER);
      register("frost_spirit", FROST_SPIRIT);
      register("grittle_flatfish", GRITTLE_FLATFISH);
      register("municipal_monkfish", MUNICIPAL_MONKFISH);
      register("armored_frost_spirit", ARMORED_FROST_SPIRIT);
      register("crynocerous", CRYNOCEROUS);
      register("jelly_drifter", JELLY_DRIFTER);
      register("snow_owl", SNOW_OWL);
      register("polargeist", POLARGEIST);
      register("stonelet", STONELET);
      register("whistleshell_crab", WHISTLESHELL_CRAB);
      register("shrumpty", SHRUMPTY);
      register("diophyde_prowler", DIOPHYDE_PROWLER);
      register("seclam", SECLAM);
      register("firefly", FIREFLY);
      register("cosmic_fox", COSMIC_FOX);
      register("crystal_camel", CRYSTAL_CAMEL);
      register("shade_monitor", SHADE_MONITOR);
      register("sliv", SLIV);
      register("crogre", CROGRE);
      register("charscale_moki", CHARSCALE_MOKI);
      register("horizofin_tunid", HORIZOFIN_TUNID);
      register("nyctofly", NYCTOFLY);
      register("venom_spider", VENOM_SPIDER);
      register("venom_spit", VENOM_SPIT);
      register("emberback", EMBERBACK);
      register("infested_swarmer", INFESTED_SWARMER);
      register("gatekeeper", GATEKEEPER);
      register("summoner", SUMMONER);
      register("artificial_golem", ARTIFICIAL_GOLEM);
      register("strange_lightning", STRANGE_LIGHTNING);
      register("fluctuant_sphere", FLUCTUANT_SPHERE);
      register("alchemist", ALCHEMIST);
      register("decaying_spike", DECAYING_SPIKE);
      register("starlit_crusher", STARLIT_CRUSHER);
      register("ent_root", ENT_ROOT);
      register("ent_wall", ENT_WALL);
      register("ent_leaf", ENT_LEAF);
      register("spewter", SPEWTER);
      register("seed_bomb", SEED_BOMB);
      register("arachnarch", ARACHNARCH);
      register("venom_bomb", VENOM_BOMB);
      register("nested_spider", NESTED_SPIDER);
      register("spear", SPEAR);
      register("supporter_pet", SUPPORTER_PET);
      register("boss_item", BOSS_ITEM);
   }

   public static void onAttributesRegistered(EntityAttributeCreationEvent event) {
      event.put(AZULFO, AzulfoEntity.registerAttributes().build());
      event.put(STARDUST_RAM, StardustRamEntity.registerAttributes().build());
      event.put(REINDEER, ReindeerEntity.registerAttributes().build());
      event.put(JELLY_DRIFTER, JellyDrifterEntity.registerAttributes().build());
      event.put(SNOW_OWL, SnowOwlEntity.registerAttributes().build());
      event.put(GRITTLE_FLATFISH, GrittleFlatfishEntity.registerAttributes().build());
      event.put(MUNICIPAL_MONKFISH, AbstractFish.createAttributes().build());
      event.put(CRYSTAL_CAMEL, CrystalCamelEntity.registerAttributes().build());
      event.put(COSMIC_FOX, CosmicFoxEntity.registerAttributes().build());
      event.put(FIREFLY, FireflyEntity.registerAttributes().build());
      event.put(SHADE_MONITOR, ShadeMonitorEntity.registerAttributes().build());
      event.put(SLIV, SlivEntity.registerAttributes().build());
      event.put(CROGRE, CrogreEntity.registerAttributes().build());
      event.put(CHARSCALE_MOKI, AbstractFish.createAttributes().build());
      event.put(HORIZOFIN_TUNID, AbstractFish.createAttributes().build());
      event.put(ARMORED_FROST_SPIRIT, ArmoredFrostSpiritEntity.registerAttributes().build());
      event.put(CRYNOCEROUS, CrynocerousEntity.registerAttributes().build());
      event.put(POLARGEIST, PolargeistEntity.registerAttributes().build());
      event.put(FROST_SPIRIT, FrostSpiritEntity.registerAttributes().build());
      event.put(WHISTLESHELL_CRAB, WhistleshellCrabEntity.registerAttributes().build());
      event.put(SHRUMPTY, ShrumptyEntity.registerAttributes().build());
      event.put(DIOPHYDE_PROWLER, DiophydeProwlerEntity.registerAttributes().build());
      event.put(SECLAM, SeclamEntity.registerAttributes().build());
      event.put(NYCTOFLY, NyctoflyEntity.registerAttributes().build());
      event.put(VENOM_SPIDER, VenomSpiderEntity.registerAttributes().build());
      event.put(EMBERBACK, EmberbackEntity.registerAttributes().build());
      event.put(INFESTED_SWARMER, InfestedSwarmerEntity.registerAttributes().build());
      event.put(GATEKEEPER, GatekeeperEntity.registerAttributes().build());
      event.put(SUMMONER, SummonerEntity.registerAttributes().build());
      event.put(ARTIFICIAL_GOLEM, ArtificialGolemEntity.registerAttributes().build());
      event.put(STARLIT_CRUSHER, StarlitCrusherEntity.registerAttributes().build());
      event.put(STONELET, StoneletEntity.registerAttributes().build());
      event.put(ENT_WALL, EntWallEntity.registerAttributes().build());
      event.put(SPEWTER, SpewterEntity.registerAttributes().build());
      event.put(ALCHEMIST, AlchemistEntity.registerAttributes().build());
      event.put(ARACHNARCH, ArachnarchEntity.registerAttributes().build());
      event.put(NESTED_SPIDER, NestedSpiderEntity.registerAttributes().build());
      event.put(SUPPORTER_PET, SupporterPetEntity.registerAttributes().build());
   }

   private static <T extends Entity> EntityType<T> buildEntity(String key, Builder<T> builder) {
      return builder.build(BlueSkies.find(key));
   }

   private static void register(String name, EntityType<?> type) {
      if (registerEvent != null) {
         registerEvent.register(Registries.ENTITY_TYPE, BlueSkies.locate(name), () -> type);
      }
   }

   public static class SpawnConditions {
      public static void registerPlacements(SpawnPlacementRegisterEvent event) {
         basicPlacement(event, SkiesEntityTypes.AZULFO, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.STARDUST_RAM, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.REINDEER, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         event.register(SkiesEntityTypes.JELLY_DRIFTER, Type.IN_WATER, Types.MOTION_BLOCKING_NO_LEAVES, JellyDrifterEntity::spawnConditions, Operation.REPLACE);
         event.register(SkiesEntityTypes.SNOW_OWL, Type.ON_GROUND, Types.MOTION_BLOCKING, SnowOwlEntity::spawnConditions, Operation.REPLACE);
         event.register(
            SkiesEntityTypes.GRITTLE_FLATFISH,
            Type.IN_WATER,
            Types.MOTION_BLOCKING_NO_LEAVES,
            WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
            Operation.REPLACE
         );
         event.register(
            SkiesEntityTypes.MUNICIPAL_MONKFISH,
            Type.IN_WATER,
            Types.MOTION_BLOCKING_NO_LEAVES,
            WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
            Operation.REPLACE
         );
         basicPlacement(event, SkiesEntityTypes.CRYSTAL_CAMEL, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.COSMIC_FOX, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.FIREFLY, FireflyEntity::spawnConditions);
         basicPlacement(event, SkiesEntityTypes.SHADE_MONITOR, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.SLIV, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.CROGRE, SkiesEntityTypes.SpawnConditions::animalSpawnConditions);
         event.register(
            SkiesEntityTypes.CHARSCALE_MOKI, Type.IN_WATER, Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, Operation.REPLACE
         );
         event.register(
            SkiesEntityTypes.HORIZOFIN_TUNID, Type.IN_WATER, Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules, Operation.REPLACE
         );
         basicPlacement(event, SkiesEntityTypes.ARMORED_FROST_SPIRIT, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.CRYNOCEROUS, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.POLARGEIST, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         event.register(
            SkiesEntityTypes.WHISTLESHELL_CRAB,
            SkiesEntityTypes.SpawnPlacementTypes.ON_GROUND_OR_WATER,
            Types.MOTION_BLOCKING_NO_LEAVES,
            SkiesEntityTypes.SpawnConditions::waterBeachMonsterSpawnConditions,
            Operation.REPLACE
         );
         basicPlacement(event, SkiesEntityTypes.SHRUMPTY, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.DIOPHYDE_PROWLER, SkiesEntityTypes.SpawnConditions::surfaceBrightMonsterSpawnConditions);
         event.register(
            SkiesEntityTypes.SECLAM,
            SkiesEntityTypes.SpawnPlacementTypes.ON_GROUND_OR_WATER,
            Types.OCEAN_FLOOR,
            SkiesEntityTypes.SpawnConditions::oceanFloorMonsterSpawnConditions,
            Operation.REPLACE
         );
         basicPlacement(event, SkiesEntityTypes.NYCTOFLY, SkiesEntityTypes.SpawnConditions::dawnMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.VENOM_SPIDER, VenomSpiderEntity::spawnConditions);
         basicPlacement(event, SkiesEntityTypes.EMBERBACK, SkiesEntityTypes.SpawnConditions::dawnMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.INFESTED_SWARMER, InfestedSwarmerEntity::swarmerSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.GATEKEEPER, SkiesEntityTypes.SpawnConditions::noSpawnCon);
         basicPlacement(event, SkiesEntityTypes.SUMMONER, SkiesEntityTypes.SpawnConditions::noSpawnCon);
         basicPlacement(event, SkiesEntityTypes.ARTIFICIAL_GOLEM, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.STARLIT_CRUSHER, SkiesEntityTypes.SpawnConditions::noSpawnCon);
         basicPlacement(event, SkiesEntityTypes.STONELET, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.SPEWTER, SkiesEntityTypes.SpawnConditions::brightMonsterSpawnConditions);
         basicPlacement(event, SkiesEntityTypes.ALCHEMIST, SkiesEntityTypes.SpawnConditions::noSpawnCon);
         basicPlacement(event, SkiesEntityTypes.ARACHNARCH, SkiesEntityTypes.SpawnConditions::noSpawnCon);
         basicPlacement(event, SkiesEntityTypes.NESTED_SPIDER, SkiesEntityTypes.SpawnConditions::dawnMonsterSpawnConditions);
      }

      public static void registerPlacementOverrides(SpawnPlacementRegisterEvent event) {
         event.register(
            EntityType.CAVE_SPIDER,
            null,
            null,
            (type, level, reason, pos, rand) -> reason != MobSpawnType.NATURAL
               || level.getLevel() == null
               || !EntityUtil.isInDungeon(level.getLevel(), pos)
               || pos.getY() >= 15 && pos.getY() <= 50 && rand.nextInt(7) == 0,
            Operation.AND
         );
      }

      public static <T extends Mob> void basicPlacement(SpawnPlacementRegisterEvent event, EntityType<T> type, SpawnPredicate<T> predicate) {
         event.register(type, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, predicate, Operation.REPLACE);
      }

      public static boolean animalSpawnConditions(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
         Block block = world.getBlockState(pos.below()).getBlock();
         return (block == SkiesBlocks.turquoise_grass_block || block == SkiesBlocks.lunar_grass_block || block == SkiesBlocks.crystal_sand)
            && world.getRawBrightness(pos, 0) > 8;
      }

      public static boolean mobSpawnConditions(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
         Block block = world.getBlockState(pos.below()).getBlock();
         return (block == SkiesBlocks.turquoise_grass_block || block == SkiesBlocks.lunar_grass_block || block == SkiesBlocks.crystal_sand)
            && world.getRawBrightness(pos, 0) > 8
            && pos.getY() >= 60;
      }

      public static boolean brightMonsterSpawnConditions(
         EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand
      ) {
         int addedChance = reason == MobSpawnType.SPAWNER ? 50 : (world.getBrightness(LightLayer.SKY, pos) <= 0 && pos.getY() < world.getSeaLevel() ? 50 : 150);
         return Mob.checkMobSpawnRules(type, world, reason, pos, rand) && world.getDifficulty() != Difficulty.PEACEFUL && rand.nextInt(addedChance) == 0;
      }

      public static boolean dawnMonsterSpawnConditions(
         EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand
      ) {
         int addedChance = reason == MobSpawnType.SPAWNER ? 50 : (world.getBrightness(LightLayer.SKY, pos) <= 0 && pos.getY() < world.getSeaLevel() ? 50 : 150);
         return Mob.checkMobSpawnRules(type, world, reason, pos, rand) && world.getDifficulty() != Difficulty.PEACEFUL && rand.nextInt(addedChance) == 0;
      }

      public static boolean surfaceBrightMonsterSpawnConditions(
         EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand
      ) {
         return brightMonsterSpawnConditions(type, world, reason, pos, rand) && (pos.getY() >= world.getSeaLevel() || world.canSeeSky(pos));
      }

      public static boolean undergroundDawnMonsterSpawnConditions(
         EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand
      ) {
         return dawnMonsterSpawnConditions(type, world, reason, pos, rand) && pos.getY() < world.getSeaLevel() && !world.canSeeSky(pos);
      }

      public static boolean waterBeachMonsterSpawnConditions(
         EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand
      ) {
         return brightMonsterSpawnConditions(type, world, reason, pos, rand) && world.canSeeSkyFromBelowWater(pos)
            || oceanFloorMonsterSpawnConditions(type, world, reason, pos, rand);
      }

      public static boolean oceanFloorMonsterSpawnConditions(
         EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand
      ) {
         int addedChance = reason == MobSpawnType.SPAWNER ? 50 : 120;
         return rand.nextInt(addedChance) == 0 && (world.isWaterAt(pos) || world.isWaterAt(pos.below()));
      }

      public static boolean noSpawnCon(EntityType<?> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
         return false;
      }
   }

   public static class SpawnPlacementTypes {
      private static TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> onGroundInWaterPredicate = (worldIn, pos, entityTypeIn) -> {
         BlockState blockstate = worldIn.getBlockState(pos);
         FluidState fluidstate = worldIn.getFluidState(pos);
         BlockPos blockpos = pos.above();
         BlockPos blockpos1 = pos.below();
         boolean inWater = fluidstate.is(FluidTags.WATER)
            && worldIn.getFluidState(blockpos1).is(FluidTags.WATER)
            && !worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos);
         boolean onLand = NaturalSpawner.isValidEmptySpawnBlock(worldIn, pos, blockstate, fluidstate, entityTypeIn)
            && NaturalSpawner.isValidEmptySpawnBlock(worldIn, blockpos, worldIn.getBlockState(blockpos), worldIn.getFluidState(blockpos), entityTypeIn);
         return inWater || onLand;
      };
      private static final Type ON_GROUND_OR_WATER = Type.create(BlueSkies.find("on_ground_or_water"), onGroundInWaterPredicate);
   }
}
