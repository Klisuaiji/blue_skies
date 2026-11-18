package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public class SkiesSounds {
   public static final SoundEvent BLOCK_EVERBRIGHT_PORTAL = create("block.everbright.portal");
   public static final SoundEvent BLOCK_EVERBRIGHT_PORTAL_TRIGGER = create("block.everbright.portal.trigger");
   public static final SoundEvent BLOCK_EVERBRIGHT_PORTAL_TRAVEL = create("block.everbright.portal.travel");
   public static final SoundEvent BLOCK_EVERDAWN_PORTAL = create("block.everdawn.portal");
   public static final SoundEvent BLOCK_EVERDAWN_PORTAL_TRIGGER = create("block.everdawn.portal.trigger");
   public static final SoundEvent BLOCK_EVERDAWN_PORTAL_TRAVEL = create("block.everdawn.portal.travel");
   public static final SoundEvent BLOCK_KEYSTONE_LOCKED = create("block.keystone.locked");
   public static final SoundEvent BLOCK_KEYSTONE_UNLOCK = create("block.keystone.unlock");
   public static final SoundEvent BLOCK_KEYSTONE_TELEPORT = create("block.keystone.teleport");
   public static final SoundEvent BLOCK_MOONSTONE_BREAK = create("block.moonstone.break");
   public static final SoundEvent BLOCK_MOONSTONE_LANTERN_BREAK = create("block.moonstone_lantern.break");
   public static final SoundEvent BLOCK_MOONSTONE_LANTERN_PLACE = create("block.moonstone_lantern.place");
   public static final SoundEvent BLOCK_MOONSTONE_LANTERN_HIT = create("block.moonstone_lantern.hit");
   public static final SoundEvent BLOCK_MUD_BREAK = create("block.mud.break");
   public static final SoundEvent BLOCK_MUD_PLACE = create("block.mud.place");
   public static final SoundEvent BLOCK_MUD_HIT = create("block.mud.hit");
   public static final SoundEvent BLOCK_MUD_STEP = create("block.mud.step");
   public static final SoundEvent BLOCK_RIMESTONE_BREAK = create("block.rimestone.break");
   public static final SoundEvent BLOCK_RIMESTONE_PLACE = create("block.rimestone.place");
   public static final SoundEvent BLOCK_RIMESTONE_HIT = create("block.rimestone.hit");
   public static final SoundEvent BLOCK_CRYSTALLIZED_PLANT_BREAK = create("block.crystallized_plant.break");
   public static final SoundEvent BLOCK_SUMMONING_TABLE_TOME_CHANGE = create("block.summoning_table.tome_change");
   public static final SoundEvent BLOCK_ALCHEMY_TABLE_USE = create("block.alchemy_table.use");
   public static final SoundEvent BLOCK_TOOL_BOX_USE = create("block.tool_box.use");
   public static final SoundEvent BLOCK_TOOL_BOX_USE_FALSITE = create("block.tool_box.use_falsite");
   public static final SoundEvent BLOCK_STAR_EMITTER_USE = create("block.star_emitter.use");
   public static final SoundEvent BLOCK_FOOD_PREP_TABLE_USE = create("block.food_prep_table.use");
   public static final SoundEvent ITEM_SPEAR_THROW = create("item.spear.throw");
   public static final SoundEvent ITEM_SPEAR_THROW_UNDERWATER = create("item.spear.throw_underwater");
   public static final SoundEvent ITEM_SPEAR_HIT = create("item.spear.hit");
   public static final SoundEvent ITEM_SPEAR_HIT_GROUND = create("item.spear.hit_ground");
   public static final SoundEvent ITEM_SPEAR_RETURN = create("item.spear.return");
   public static final SoundEvent ITEM_ETHEREAL_ARC_EQUIP = create("item.arc.ethereal_equip");
   public static final SoundEvent ITEM_DUSK_ARC_EQUIP = create("item.arc.dusk_equip");
   public static final SoundEvent ITEM_NATURE_ARC_EQUIP = create("item.arc.nature_equip");
   public static final SoundEvent ITEM_POISON_ARC_EQUIP = create("item.arc.poison_equip");
   public static final SoundEvent ITEM_AQUATIC_ARC_EQUIP = create("item.arc.aquatic_equip");
   public static final SoundEvent ITEM_LIFE_ARC_EQUIP = create("item.arc.life_equip");
   public static final SoundEvent ITEM_RUNIC_ARC_EQUIP = create("item.arc.runic_equip");
   public static final SoundEvent ITEM_BAG_OPEN = create("item.bag.open");
   public static final SoundEvent ITEM_ASTROLABE_READY = create("item.astrolabe.ready");
   public static final SoundEvent ITEM_FALSITE_BREAK = create("item.falsite.break");
   public static final SoundEvent RECORDS_BLINDING_RAGE = create("records.blinding_rage");
   public static final SoundEvent RECORDS_DEFYING_STARLIGHT = create("records.defying_starlight");
   public static final SoundEvent RECORDS_VENOMOUS_ENCOUNTER = create("records.venomous_encounter");
   public static final SoundEvent RECORDS_POPULATION = create("records.population");
   public static final SoundEvent MUSIC_MARS = create("music.mars");
   public static final SoundEvent MUSIC_MOONLIT_BLOOM = create("music.moonlit_bloom");
   public static final SoundEvent MUSIC_BRISEGEL = create("music.brisegel");
   public static final SoundEvent MUSIC_TURQUOISE = create("music.turquoise");
   public static final SoundEvent MUSIC_SNOWCAP = create("music.snowcap");
   public static final SoundEvent MUSIC_BANEFUL = create("music.baneful");
   public static final SoundEvent MUSIC_BRIGHTLANDS = create("music.brightlands");
   public static final SoundEvent MUSIC_CRYSTAL_DUNES = create("music.crystal_dunes");
   public static final SoundEvent MUSIC_GATEKEEPERS_TALE = create("music.gatekeepers_tale");
   public static final SoundEvent MUSIC_WHISTLESHELL = create("music.whistleshell");
   public static final SoundEvent MUSIC_BRITTLEBUSH = create("music.brittlebush");
   public static final SoundEvent MUSIC_BLINDING_AMBIENCE = create("music.blinding_dungeon_ambience");
   public static final SoundEvent MUSIC_BLINDING_BOSS = create("music.blinding_boss");
   public static final SoundEvent MUSIC_BLINDING_DEFEAT = create("music.blinding_defeat");
   public static final SoundEvent MUSIC_POISON_AMBIENCE = create("music.poison_dungeon_ambience");
   public static final SoundEvent MUSIC_POISON_BOSS = create("music.poison_boss");
   public static final SoundEvent MUSIC_POISON_DEFEAT = create("music.poison_defeat");
   public static final SoundEvent MUSIC_NATURE_AMBIENCE = create("music.nature_dungeon_ambience");
   public static final SoundEvent MUSIC_NATURE_BOSS = create("music.nature_boss");
   public static final SoundEvent MUSIC_NATURE_DEFEAT = create("music.nature_defeat");
   public static final SoundEvent MUSIC_GENERIC_BOSS = create("music.generic_boss");
   public static final SoundEvent MUSIC_GENERIC_DEFEAT = create("music.generic_defeat");
   public static final SoundEvent ENTITY_VILLAGER_WORK_STARGAZER = create("entity.villager.work_stargazer");
   public static final SoundEvent ENTITY_VILLAGER_WORK_WRANGLER = create("entity.villager.work_wrangler");
   public static final SoundEvent ENTITY_VILLAGER_WORK_SHOVELER = create("entity.villager.work_shoveler");
   public static final SoundEvent ENTITY_VILLAGER_WORK_NIGHTWATCHER = create("entity.villager.work_nightwatcher");
   public static final SoundEvent ENTITY_VILLAGER_WORK_SUMMONER = create("entity.villager.work_summoner");
   public static final SoundEvent ENTITY_VILLAGER_WORK_ALCHEMIST = create("entity.villager.work_alchemist");
   public static final SoundEvent ENTITY_AZULFO_IDLE = create("entity.azulfo.idle");
   public static final SoundEvent ENTITY_AZULFO_HURT = create("entity.azulfo.hurt");
   public static final SoundEvent ENTITY_AZULFO_DEATH = create("entity.azulfo.death");
   public static final SoundEvent ENTITY_STARDUST_RAM_IDLE = create("entity.stardust_ram.idle");
   public static final SoundEvent ENTITY_STARDUST_RAM_HURT = create("entity.stardust_ram.hurt");
   public static final SoundEvent ENTITY_STARDUST_RAM_DEATH = create("entity.stardust_ram.death");
   public static final SoundEvent ENTITY_JELLY_DRIFTER_IDLE = create("entity.jelly_drifter.idle");
   public static final SoundEvent ENTITY_JELLY_DRIFTER_HURT = create("entity.jelly_drifter.hurt");
   public static final SoundEvent ENTITY_JELLY_DRIFTER_DEATH = create("entity.jelly_drifter.death");
   public static final SoundEvent ENTITY_JELLY_DRIFTER_POISON = create("entity.jelly_drifter.poison");
   public static final SoundEvent ENTITY_CROGRE_IDLE = create("entity.crogre.idle");
   public static final SoundEvent ENTITY_CROGRE_IDLE_HAPPY = create("entity.crogre.idle_happy");
   public static final SoundEvent ENTITY_CROGRE_HURT = create("entity.crogre.hurt");
   public static final SoundEvent ENTITY_CROGRE_DEATH = create("entity.crogre.death");
   public static final SoundEvent ENTITY_CROGRE_JUMP = create("entity.crogre.jump");
   public static final SoundEvent ENTITY_FIREFLY_DEATH = create("entity.firefly.death");
   public static final SoundEvent ENTITY_REINDEER_IDLE = create("entity.reindeer.idle");
   public static final SoundEvent ENTITY_REINDEER_HURT = create("entity.reindeer.hurt");
   public static final SoundEvent ENTITY_REINDEER_DEATH = create("entity.reindeer.death");
   public static final SoundEvent ENTITY_SNOW_OWL_IDLE = create("entity.snow_owl.idle");
   public static final SoundEvent ENTITY_SNOW_OWL_HURT = create("entity.snow_owl.hurt");
   public static final SoundEvent ENTITY_SNOW_OWL_DEATH = create("entity.snow_owl.death");
   public static final SoundEvent ENTITY_SNOW_OWL_SING = create("entity.snow_owl.sing");
   public static final SoundEvent ENTITY_SNOW_OWL_FLAP = create("entity.snow_owl.flap");
   public static final SoundEvent ENTITY_CRYSTAL_CAMEL_IDLE = create("entity.crystal_camel.idle");
   public static final SoundEvent ENTITY_CRYSTAL_CAMEL_HURT = create("entity.crystal_camel.hurt");
   public static final SoundEvent ENTITY_CRYSTAL_CAMEL_DEATH = create("entity.crystal_camel.death");
   public static final SoundEvent ENTITY_CRYSTAL_CAMEL_STEP = create("entity.crystal_camel.step");
   public static final SoundEvent ENTITY_ARMORED_FROST_SPIRIT_IDLE = create("entity.armored_frost_spirit.idle");
   public static final SoundEvent ENTITY_ARMORED_FROST_SPIRIT_HURT = create("entity.armored_frost_spirit.hurt");
   public static final SoundEvent ENTITY_ARMORED_FROST_SPIRIT_DEATH = create("entity.armored_frost_spirit.death");
   public static final SoundEvent ENTITY_CRYNOCEROUS_IDLE = create("entity.crynocerous.idle");
   public static final SoundEvent ENTITY_CRYNOCEROUS_HURT = create("entity.crynocerous.hurt");
   public static final SoundEvent ENTITY_CRYNOCEROUS_DEATH = create("entity.crynocerous.death");
   public static final SoundEvent ENTITY_FROST_SPIRIT_FREE = create("entity.frost_spirit.free");
   public static final SoundEvent ENTITY_FROST_SPIRIT_HURT = create("entity.frost_spirit.hurt");
   public static final SoundEvent ENTITY_FROST_SPIRIT_DEATH = create("entity.frost_spirit.death");
   public static final SoundEvent ENTITY_NYCTOFLY_BITE = create("entity.nyctofly.bite");
   public static final SoundEvent ENTITY_NYCTOFLY_FLYING = create("entity.nyctofly.flying");
   public static final SoundEvent ENTITY_NYCTOFLY_HURT = create("entity.nyctofly.hurt");
   public static final SoundEvent ENTITY_NYCTOFLY_DEATH = create("entity.nyctofly.death");
   public static final SoundEvent ENTITY_EMBERBACK_SPRAY = create("entity.emberback.spray");
   public static final SoundEvent ENTITY_EMBERBACK_FLYING = create("entity.emberback.flying");
   public static final SoundEvent ENTITY_EMBERBACK_IDLE = create("entity.emberback.idle");
   public static final SoundEvent ENTITY_EMBERBACK_HURT = create("entity.emberback.hurt");
   public static final SoundEvent ENTITY_EMBERBACK_DEATH = create("entity.emberback.death");
   public static final SoundEvent ENTITY_INFESTED_SWARMER_BITE = create("entity.infested_swarmer.bite");
   public static final SoundEvent ENTITY_INFESTED_SWARMER_IDLE = create("entity.infested_swarmer.idle");
   public static final SoundEvent ENTITY_INFESTED_SWARMER_HURT = create("entity.infested_swarmer.hurt");
   public static final SoundEvent ENTITY_INFESTED_SWARMER_DEATH = create("entity.infested_swarmer.death");
   public static final SoundEvent ENTITY_DIOPHYDE_PROWLER_IDLE = create("entity.diophyde_prowler.idle");
   public static final SoundEvent ENTITY_DIOPHYDE_PROWLER_HURT = create("entity.diophyde_prowler.hurt");
   public static final SoundEvent ENTITY_DIOPHYDE_PROWLER_DEATH = create("entity.diophyde_prowler.death");
   public static final SoundEvent ENTITY_DIOPHYDE_PROWLER_LUNGE = create("entity.diophyde_prowler.lunge");
   public static final SoundEvent ENTITY_SHADE_MONITOR_IDLE = create("entity.shade_monitor.idle");
   public static final SoundEvent ENTITY_SHADE_MONITOR_HURT = create("entity.shade_monitor.hurt");
   public static final SoundEvent ENTITY_SHADE_MONITOR_DEATH = create("entity.shade_monitor.death");
   public static final SoundEvent ENTITY_VENOM_SPIDER_IDLE = create("entity.venom_spider.idle");
   public static final SoundEvent ENTITY_VENOM_SPIDER_HURT = create("entity.venom_spider.hurt");
   public static final SoundEvent ENTITY_VENOM_SPIDER_DEATH = create("entity.venom_spider.death");
   public static final SoundEvent ENTITY_VENOM_SPIDER_SPIT = create("entity.venom_spider.spit");
   public static final SoundEvent ENTITY_POLARGEIST_IDLE = create("entity.polargeist.idle");
   public static final SoundEvent ENTITY_POLARGEIST_HURT = create("entity.polargeist.hurt");
   public static final SoundEvent ENTITY_POLARGEIST_DEATH = create("entity.polargeist.death");
   public static final SoundEvent ENTITY_POLARGEIST_ATTACK = create("entity.polargeist.attack");
   public static final SoundEvent ENTITY_SHRUMPTY_IDLE = create("entity.shrumpty.idle");
   public static final SoundEvent ENTITY_SHRUMPTY_HURT = create("entity.shrumpty.hurt");
   public static final SoundEvent ENTITY_SHRUMPTY_DEATH = create("entity.shrumpty.death");
   public static final SoundEvent ENTITY_SHRUMPTY_ATTACK = create("entity.shrumpty.attack");
   public static final SoundEvent ENTITY_STONELET_IDLE = create("entity.stonelet.idle");
   public static final SoundEvent ENTITY_STONELET_HURT = create("entity.stonelet.hurt");
   public static final SoundEvent ENTITY_STONELET_DEATH = create("entity.stonelet.death");
   public static final SoundEvent ENTITY_SLIV_IDLE = create("entity.sliv.idle");
   public static final SoundEvent ENTITY_SLIV_HURT = create("entity.sliv.hurt");
   public static final SoundEvent ENTITY_SLIV_DEATH = create("entity.sliv.death");
   public static final SoundEvent ENTITY_ARTIFICIAL_GOLEM_HURT = create("entity.artificial_golem.hurt");
   public static final SoundEvent ENTITY_ARTIFICIAL_GOLEM_DEATH = create("entity.artificial_golem.death");
   public static final SoundEvent ENTITY_ARTIFICIAL_GOLEM_ATTACK = create("entity.artificial_golem.attack");
   public static final SoundEvent ENTITY_ARTIFICIAL_GOLEM_ACTIVATE = create("entity.artificial_golem.activate");
   public static final SoundEvent ENTITY_ARTIFICIAL_GOLEM_DEACTIVATE = create("entity.artificial_golem.deactivate");
   public static final SoundEvent ENTITY_SPEWTER_SPIT = create("entity.spewter.spit");
   public static final SoundEvent ENTITY_SPEWTER_HURT = create("entity.spewter.hurt");
   public static final SoundEvent ENTITY_SPEWTER_DEATH = create("entity.spewter.death");
   public static final SoundEvent ENTITY_SPEWTER_GROW = create("entity.spewter.grow");
   public static final SoundEvent ENTITY_NESTED_SPIDER_IDLE = create("entity.nested_spider.idle");
   public static final SoundEvent ENTITY_NESTED_SPIDER_HURT = create("entity.nested_spider.hurt");
   public static final SoundEvent ENTITY_NESTED_SPIDER_DEATH = create("entity.nested_spider.death");
   public static final SoundEvent ENTITY_GATEKEEPER_IDLE = create("entity.gatekeeper.idle");
   public static final SoundEvent ENTITY_GATEKEEPER_HURT = create("entity.gatekeeper.hurt");
   public static final SoundEvent ENTITY_GATEKEEPER_DEATH = create("entity.gatekeeper.death");
   public static final SoundEvent ENTITY_GATEKEEPER_CELEBRATE = create("entity.gatekeeper.celebrate");
   public static final SoundEvent ENTITY_GATEKEEPER_CAST_SPELL = create("entity.gatekeeper.cast_spell");
   public static final SoundEvent ENTITY_GATEKEEPER_PREPARE_REGEN = create("entity.gatekeeper.prepare_regen");
   public static final SoundEvent ENTITY_GATEKEEPER_NO = create("entity.gatekeeper.no");
   public static final SoundEvent ENTITY_GATEKEEPER_YES = create("entity.gatekeeper.yes");
   public static final SoundEvent ENTITY_GATEKEEPER_TRADE = create("entity.gatekeeper.trade");
   public static final SoundEvent ENTITY_BOSS_SPAWNED = create("entity.boss.spawn");
   public static final SoundEvent ENTITY_FLUCTUANT_SPHERE_DISAPPEAR = create("entity.fluctuant_sphere.disappear");
   public static final SoundEvent ENTITY_SUMMONER_IDLE = create("entity.summoner.idle");
   public static final SoundEvent ENTITY_SUMMONER_HURT = create("entity.summoner.hurt");
   public static final SoundEvent ENTITY_SUMMONER_DEATH = create("entity.summoner.death");
   public static final SoundEvent ENTITY_SUMMONER_CELEBRATE = create("entity.summoner.celebrate");
   public static final SoundEvent ENTITY_SUMMONER_CAST_SPELL = create("entity.summoner.cast_spell");
   public static final SoundEvent ENTITY_SUMMONER_PREPARE_REGEN = create("entity.summoner.prepare_regen");
   public static final SoundEvent ENTITY_SUMMONER_PREPARE_ATTACK = create("entity.summoner.prepare_attack");
   public static final SoundEvent ENTITY_SUMMONER_PREPARE_SUMMONING = create("entity.summoner.prepare_summon");
   public static final SoundEvent ENTITY_ALCHEMIST_IDLE = create("entity.alchemist.idle");
   public static final SoundEvent ENTITY_ALCHEMIST_HURT = create("entity.alchemist.hurt");
   public static final SoundEvent ENTITY_ALCHEMIST_DEATH = create("entity.alchemist.death");
   public static final SoundEvent ENTITY_ALCHEMIST_CELEBRATE = create("entity.alchemist.celebrate");
   public static final SoundEvent ENTITY_ALCHEMIST_CAST_SPELL = create("entity.alchemist.cast_spell");
   public static final SoundEvent ENTITY_ALCHEMIST_PREPARE_ROCKS = create("entity.alchemist.prepare_rocks");
   public static final SoundEvent ENTITY_ALCHEMIST_PREPARE_REGEN = create("entity.alchemist.prepare_regen");
   public static final SoundEvent ENTITY_ALCHEMIST_PREPARE_BLINDNESS = create("entity.alchemist.prepare_blindness");
   public static final SoundEvent ENTITY_ALCHEMIST_PREPARE_CONVERSION = create("entity.alchemist.prepare_conversion");
   public static final SoundEvent ENTITY_ALCHEMIST_PREPARE_POTIONS = create("entity.alchemist.prepare_potions");
   public static final SoundEvent ENTITY_ALCHEMIST_PREPARE_ATTACK = create("entity.alchemist.prepare_attack");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_IDLE = create("entity.starlit_crusher.idle");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_HURT = create("entity.starlit_crusher.hurt");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_DEATH = create("entity.starlit_crusher.death");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_STEP = create("entity.starlit_crusher.step");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_STUNNED = create("entity.starlit_crusher.stunned");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_ROOT = create("entity.starlit_crusher.root");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_HEAL = create("entity.starlit_crusher.heal");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_PREPARE_SMALL_GROWTH = create("entity.starlit_crusher.prepare_small_growth");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_PREPARE_LARGE_GROWTH = create("entity.starlit_crusher.prepare_large_growth");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_PREPARE_SPIN = create("entity.starlit_crusher.prepare_spin");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_SPIN = create("entity.starlit_crusher.spin");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_PREPARE_OWLS = create("entity.starlit_crusher.prepare_owls");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_PREPARE_HAMMER = create("entity.starlit_crusher.prepare_hammer");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_HAMMER_SMASH = create("entity.starlit_crusher.hammer_smash");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_ATTACK = create("entity.starlit_crusher.attack");
   public static final SoundEvent ENTITY_STARLIT_CRUSHER_FLING_LEAF = create("entity.starlit_crusher.fling_leaf");
   public static final SoundEvent ENTITY_STARLIT_WALL_GROW = create("entity.starlit_wall.grow");
   public static final SoundEvent ENTITY_ARACHNARCH_IDLE = create("entity.arachnarch.idle");
   public static final SoundEvent ENTITY_ARACHNARCH_HURT = create("entity.arachnarch.hurt");
   public static final SoundEvent ENTITY_ARACHNARCH_DEATH = create("entity.arachnarch.death");
   public static final SoundEvent ENTITY_ARACHNARCH_SPIT = create("entity.arachnarch.spit");
   public static final SoundEvent ENTITY_ARACHNARCH_CHARGE = create("entity.arachnarch.charge");
   public static final SoundEvent ENTITY_ARACHNARCH_LUNGE = create("entity.arachnarch.lunge");
   public static final SoundEvent ENTITY_ARACHNARCH_FLIP = create("entity.arachnarch.flip");
   public static final SoundEvent ENTITY_ARACHNARCH_SLING = create("entity.arachnarch.sling");
   public static final SoundEvent ENTITY_ARACHNARCH_STUNNED = create("entity.arachnarch.stunned");
   public static final SoundEvent ENTITY_ARACHNARCH_VENOM_DROP = create("entity.arachnarch.venom_drop");
   public static final SoundEvent ENTITY_ARACHNARCH_SCREECH = create("entity.arachnarch.screech");
   public static final SoundEvent ENTITY_ARACHNARCH_ATTACK = create("entity.arachnarch.attack");
   public static final SoundEvent ENTITY_ARACHNARCH_THROW = create("entity.arachnarch.throw");
   public static final SoundEvent ENTITY_PLAYER_SPIT = create("entity.player.spit");
   public static final SoundEvent ENTITY_PLAYER_CAST_SPELL = create("entity.player.cast_spell");
   public static final SoundEvent ENTITY_PLAYER_PREPARE_ROCKS = create("entity.player.prepare_rocks");
   public static final SoundEvent ENTITY_PLAYER_PREPARE_FLUCTUANT_SPHERE = create("entity.player.prepare_fluctuant_sphere");
   public static final SoundEvent ENTITY_PLAYER_PREPARE_SUMMONING = create("entity.player.prepare_summoning");
   public static final SoundEvent AMBIENT_SNOW_WIND = create("ambient.snow_wind");
   public static final SoundEvent AMBIENT_SNOW_WIND_ADDITIONS = create("ambient.snow_wind.additions");
   public static final SoundEvent AMBIENT_SANDSTORM_WIND = create("ambient.sandstorm_wind");
   public static final SoundEvent AMBIENT_NATURE_DUNGEON_ADDITIONS = create("ambient.nature_dungeon.additions");
   public static final SoundEvent AMBIENT_POISON_DUNGEON_ADDITIONS = create("ambient.poison_dungeon.additions");
   public static final SoundEvent AMBIENT_RAIN_DRIZZLE = create("ambient.rain.drizzle");
   public static final SoundEvent AMBIENT_MOONLIT_RESERVOIR_LOOP = create("ambient.moonlit_reservoir.loop");
   public static final SoundEvent AMBIENT_SHADED_WOODLANDS_LOOP = create("ambient.shaded_woodlands.loop");
   public static final SoundEvent GANGSTER = create("gangster");
   public static final SoundType MOONSTONE = new DeferredSoundType(
      1.0F,
      1.0F,
      () -> BLOCK_MOONSTONE_BREAK,
      () -> SoundEvents.STONE_STEP,
      () -> SoundEvents.GLASS_PLACE,
      () -> SoundEvents.STONE_HIT,
      () -> SoundEvents.STONE_FALL
   );
   public static final SoundType MOONSTONE_LANTERN = new DeferredSoundType(
      1.0F,
      1.0F,
      () -> BLOCK_MOONSTONE_LANTERN_BREAK,
      () -> SoundEvents.LANTERN_STEP,
      () -> BLOCK_MOONSTONE_LANTERN_PLACE,
      () -> BLOCK_MOONSTONE_LANTERN_HIT,
      () -> SoundEvents.STONE_FALL
   );
   public static final SoundType MUD = new DeferredSoundType(
      1.0F, 1.0F, () -> BLOCK_MUD_BREAK, () -> BLOCK_MUD_STEP, () -> BLOCK_MUD_PLACE, () -> BLOCK_MUD_HIT, () -> SoundEvents.GRAVEL_FALL
   );
   public static final SoundType CRYSTALLIZED_PLANT = new DeferredSoundType(
      1.0F,
      1.0F,
      () -> BLOCK_CRYSTALLIZED_PLANT_BREAK,
      () -> SoundEvents.STONE_STEP,
      () -> SoundEvents.GLASS_PLACE,
      () -> SoundEvents.STONE_HIT,
      () -> SoundEvents.STONE_FALL
   );
   public static final SoundType RIMESTONE = new DeferredSoundType(
      1.0F,
      1.25F,
      () -> BLOCK_RIMESTONE_BREAK,
      () -> SoundEvents.STONE_STEP,
      () -> BLOCK_RIMESTONE_PLACE,
      () -> BLOCK_RIMESTONE_HIT,
      () -> SoundEvents.STONE_FALL
   );

   public static void init() {
   }

   private static SoundEvent create(String name) {
      ResourceLocation location = BlueSkies.locate(name);
      SoundEvent sound = SoundEvent.createVariableRangeEvent(location);
      Registry.register(BuiltInRegistries.SOUND_EVENT, location, sound);
      return sound;
   }
}
