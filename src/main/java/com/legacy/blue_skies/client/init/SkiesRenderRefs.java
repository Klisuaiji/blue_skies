package com.legacy.blue_skies.client.init;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.registries.SkiesWoodTypes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;

public class SkiesRenderRefs {
   public static final Material BLUEBRIGHT_CHEST_LOCATION = chest("bluebright");
   public static final Material BLUEBRIGHT_CHEST_LOCATION_LEFT = chest("bluebright_left");
   public static final Material BLUEBRIGHT_CHEST_LOCATION_RIGHT = chest("bluebright_right");
   public static final Material STARLIT_CHEST_LOCATION = chest("starlit");
   public static final Material STARLIT_CHEST_LOCATION_LEFT = chest("starlit_left");
   public static final Material STARLIT_CHEST_LOCATION_RIGHT = chest("starlit_right");
   public static final Material FROSTBRIGHT_CHEST_LOCATION = chest("frostbright");
   public static final Material FROSTBRIGHT_CHEST_LOCATION_LEFT = chest("frostbright_left");
   public static final Material FROSTBRIGHT_CHEST_LOCATION_RIGHT = chest("frostbright_right");
   public static final Material LUNAR_CHEST_LOCATION = chest("lunar");
   public static final Material LUNAR_CHEST_LOCATION_LEFT = chest("lunar_left");
   public static final Material LUNAR_CHEST_LOCATION_RIGHT = chest("lunar_right");
   public static final Material DUSK_CHEST_LOCATION = chest("dusk");
   public static final Material DUSK_CHEST_LOCATION_LEFT = chest("dusk_left");
   public static final Material DUSK_CHEST_LOCATION_RIGHT = chest("dusk_right");
   public static final Material MAPLE_CHEST_LOCATION = chest("maple");
   public static final Material MAPLE_CHEST_LOCATION_LEFT = chest("maple_left");
   public static final Material MAPLE_CHEST_LOCATION_RIGHT = chest("maple_right");
   public static final Material COMET_CHEST_LOCATION = chest("comet");
   public static final Material COMET_CHEST_LOCATION_LEFT = chest("comet_left");
   public static final Material COMET_CHEST_LOCATION_RIGHT = chest("comet_right");
   public static final Material PUMPKIN_CHEST_LOCATION = chest("pumpkin");
   public static final ModelLayerLocation BLUEBRIGHT_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.BLUEBRIGHT);
   public static final ModelLayerLocation STARLIT_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.STARLIT);
   public static final ModelLayerLocation FROSTBRIGHT_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.FROSTBRIGHT);
   public static final ModelLayerLocation LUNAR_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.LUNAR);
   public static final ModelLayerLocation DUSK_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.DUSK);
   public static final ModelLayerLocation MAPLE_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.MAPLE);
   public static final ModelLayerLocation COMET_SIGN = ModelLayers.createSignModelName(SkiesWoodTypes.COMET);
   public static final ModelLayerLocation AZULFO = layer("azulfo");
   public static final ModelLayerLocation STARDUST_RAM = layer("stardust_ram");
   public static final ModelLayerLocation STARDUST_RAM_FUR = layer("stardust_ram", "fur");
   public static final ModelLayerLocation REINDEER = layer("reindeer");
   public static final ModelLayerLocation REINDEER_SADDLE = layer("reindeer", "saddle");
   public static final ModelLayerLocation REINDEER_XMAS_GLOW = layer("reindeer", "glow");
   public static final ModelLayerLocation JELLY_DRIFTER = layer("jelly_drifter");
   public static final ModelLayerLocation SNOW_OWL = layer("snow_owl");
   public static final ModelLayerLocation FROST_SPIRIT = layer("frost_spirit");
   public static final ModelLayerLocation GRITTLE_FLATFISH = layer("grittle_flatfish");
   public static final ModelLayerLocation MUNICIPAL_MONKFISH = layer("municipal_monkfish");
   public static final ModelLayerLocation ARMORED_FROST_SPIRIT = layer("armored_frost_spirit");
   public static final ModelLayerLocation CRYNOCEROUS = layer("crynocerous");
   public static final ModelLayerLocation POLARGEIST = layer("polargeist");
   public static final ModelLayerLocation STONELET = layer("stonelet");
   public static final ModelLayerLocation WHISTLESHELL_CRAB = layer("whistleshell_crab");
   public static final ModelLayerLocation SHRUMPTY = layer("shrumpty");
   public static final ModelLayerLocation DIOPHYDE_PROWLER = layer("diophyde_prowler");
   public static final ModelLayerLocation SECLAM = layer("seclam");
   public static final ModelLayerLocation FIREFLY = layer("firefly");
   public static final ModelLayerLocation COSMIC_FOX = layer("cosmic_fox");
   public static final ModelLayerLocation CRYSTAL_CAMEL = layer("crystal_camel");
   public static final ModelLayerLocation SHADE_MONITOR = layer("shade_monitor");
   public static final ModelLayerLocation SLIV = layer("sliv");
   public static final ModelLayerLocation CROGRE = layer("crogre");
   public static final ModelLayerLocation CROGRE_SADDLE = layer("crogre", "saddle");
   public static final ModelLayerLocation CHARSCALE_MOKI = layer("charscale_moki");
   public static final ModelLayerLocation HORIZOFIN_TUNID = layer("horizofin_tunid");
   public static final ModelLayerLocation NYCTOFLY = layer("nyctofly");
   public static final ModelLayerLocation VENOM_SPIDER = layer("venom_spider");
   public static final ModelLayerLocation VENOM_SPIT = layer("venom_spit");
   public static final ModelLayerLocation EMBERBACK = layer("emberback");
   public static final ModelLayerLocation INFESTED_SWARMER = layer("infested_swarmer");
   public static final ModelLayerLocation INFESTED_SWARMER_GLOW = layer("infested_swarmer", "glow");
   public static final ModelLayerLocation SUMMONER = layer("summoner");
   public static final ModelLayerLocation ARTIFICIAL_GOLEM = layer("artificial_golem");
   public static final ModelLayerLocation STRANGE_LIGHTNING = layer("strange_lightning");
   public static final ModelLayerLocation FLUCTUANT_SPHERE = layer("fluctuant_sphere");
   public static final ModelLayerLocation ALCHEMIST = layer("alchemist");
   public static final ModelLayerLocation ALCHEMY_SCROLL = layer("alchemy_scroll");
   public static final ModelLayerLocation STARLIT_CRUSHER = layer("starlit_crusher");
   public static final ModelLayerLocation ENT_ROOT = layer("ent_root");
   public static final ModelLayerLocation ENT_WALL = layer("ent_wall");
   public static final ModelLayerLocation ENT_LEAF = layer("ent_leaf");
   public static final ModelLayerLocation SPEWTER = layer("spewter");
   public static final ModelLayerLocation SEED_BOMB = layer("need_bomb");
   public static final ModelLayerLocation ARACHNARCH = layer("arachnarch");
   public static final ModelLayerLocation VENOM_BOMB = layer("venom_spider");
   public static final ModelLayerLocation NESTED_SPIDER = layer("nested_spider");
   public static final ModelLayerLocation GATEKEEPER = layer("gatekeeper");
   public static final ModelLayerLocation BABY_NYCTOFLY_SUPPORTER_PET = layer("baby_nyctofly_supporter_pet");
   public static final ModelLayerLocation PEBBLET_SUPPORTER_PET = layer("pebblet_supporter_pet");
   public static final ModelLayerLocation BABY_ARTIFICIAL_GOLEM_SUPPORTER_PET = layer("baby_artificial_golem_supporter_pet");
   public static final ModelLayerLocation SPEAR = layer("spear");
   public static final ModelLayerLocation SOULBOUND_SPEAR = layer("spear", "soulbound");
   public static final ModelLayerLocation BOSS_ITEM = layer("boss_item");

   private static ModelLayerLocation layer(String name) {
      return layer(name, "main");
   }

   private static ModelLayerLocation layer(String name, String layer) {
      return new ModelLayerLocation(BlueSkies.locate(name), layer);
   }

   private static Material chest(String name) {
      return new Material(Sheets.CHEST_SHEET, BlueSkies.locate("entity/chest/" + name));
   }
}
