package com.legacy.blue_skies.data.providers.journal;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.screen.journal.JournalEntryRenderers;
import com.legacy.blue_skies.data.objects.journal.JournalEntry;
import com.legacy.blue_skies.data.providers.SkiesDataProvider;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class JournalEntryProv extends SkiesDataProvider<JournalEntry> {
   public JournalEntryProv(DataGenerator generator) {
      super(generator, JournalEntry.getDirectory());
   }

   @Override
   protected Map<ResourceLocation, JournalEntry> gatherData() {
      Map<ResourceLocation, JournalEntry> registry = new HashMap<>();
      String bright = "everbright";
      String dawn = "everdawn";
      String general = "general";
      String enterEither = "enter_one";
      String enterBright = "enter_everbright";
      String enterDawn = "enter_everdawn";
      String summoner = "defeat_summoner";
      String alchemist = "defeat_alchemist";
      String crusher = "defeat_crusher";
      String arachnarch = "defeat_arachnarch";
      this.register(registry, this.builder("everbright_portal", "everbright", SkiesBlocks.everbright_portal).priority(100));
      this.register(registry, this.builder("brewberry_bush", "everbright", SkiesBlocks.brewberry_bush).requirement("enter_everbright"));
      this.register(registry, this.builder("snowcap_mushroom", "everbright", SkiesBlocks.snowcap_mushroom).requirement("enter_everbright"));
      this.register(registry, this.builder("snowcap_pinhead", "everbright", SkiesBlocks.snowcap_pinhead).requirement("enter_everbright"));
      this.register(registry, this.builder("snowcap_oven", "everbright", SkiesBlocks.snowcap_oven).requirement("enter_everbright"));
      this.register(registry, this.builder("brumble", "everbright", SkiesBlocks.brumble).requirement("enter_everbright"));
      this.register(registry, this.builder("taratite", "everbright", SkiesBlocks.taratite).requirement("enter_everbright"));
      this.register(registry, this.builder("rimestone", "everbright", SkiesBlocks.rimestone).requirement("enter_everbright"));
      this.register(registry, this.builder("ventium", "everbright", SkiesItems.ventium_ingot).requirement("enter_everbright"));
      this.register(registry, this.builder("falsite", "everbright", SkiesItems.falsite_ingot).requirement("enter_everbright"));
      this.register(registry, this.builder("pearl", "everbright", SkiesItems.pearl).requirement("enter_everbright"));
      this.register(registry, this.builder("warding_pearl", "everbright", SkiesBlocks.warding_pearl).requirement("enter_everbright"));
      this.register(registry, this.builder("soul_fragment", "everbright", SkiesItems.soul_fragment).requirement("enter_everbright"));
      this.register(registry, this.builder("azulfo_horn", "everbright", SkiesItems.azulfo_horn).requirement("enter_everbright"));
      this.register(registry, this.builder("summoning_table", "everbright", SkiesBlocks.summoning_table).requirement("defeat_summoner"));
      this.register(registry, this.builder("soulbound_spear", "everbright", SkiesItems.soulbound_spear).requirement("defeat_summoner"));
      this.register(registry, this.builder("summoning_tome", "everbright", SkiesItems.summoning_tome).requirement("defeat_summoner"));
      this.register(registry, this.builder("nature_dungeon", "everbright", SkiesBlocks.nature_keystone).requirement("defeat_summoner").priority(49));
      this.register(registry, this.builder("crushing_hammer", "everbright", SkiesItems.crushing_hammer).requirement("defeat_crusher"));
      this.register(registry, this.builder("everdawn_portal", "everdawn", SkiesBlocks.everdawn_portal).priority(100));
      this.register(registry, this.builder("baneful_mushroom", "everdawn", SkiesBlocks.baneful_mushroom).requirement("enter_everdawn"));
      this.register(registry, this.builder("crystal_flower", "everdawn", SkiesBlocks.crystal_flower).requirement("enter_everdawn"));
      this.register(registry, this.builder("spider_webbing", "everdawn", SkiesBlocks.spider_webbing).requirement("enter_everdawn"));
      this.register(registry, this.builder("crescent_fruit", "everdawn", SkiesItems.crescent_fruit).requirement("enter_everdawn"));
      this.register(registry, this.builder("sunstone_crystal", "everdawn", SkiesBlocks.sunstone_crystal).requirement("enter_everdawn"));
      this.register(registry, this.builder("horizonite_forge", "everdawn", SkiesBlocks.horizonite_forge).requirement("enter_everdawn"));
      this.register(registry, this.builder("cinderstone", "everdawn", SkiesBlocks.cinderstone).requirement("enter_everdawn"));
      this.register(registry, this.builder("umber", "everdawn", SkiesBlocks.umber).requirement("enter_everdawn"));
      this.register(registry, this.builder("crystallized_tree", "everdawn", SkiesBlocks.crystallized_log).requirement("enter_everdawn"));
      this.register(registry, this.builder("horizonite", "everdawn", SkiesItems.horizonite_ingot).requirement("enter_everdawn"));
      this.register(registry, this.builder("moonstone_shield", "everdawn", SkiesItems.moonstone_shield).requirement("enter_everdawn"));
      this.register(registry, this.builder("fox_pelt", "everdawn", SkiesItems.fox_pelt).requirement("enter_everdawn"));
      this.register(registry, this.builder("spike_shield", "everdawn", SkiesItems.spike_shield).requirement("defeat_alchemist"));
      this.register(registry, this.builder("alchemy_table", "everdawn", SkiesBlocks.alchemy_table).requirement("defeat_alchemist"));
      this.register(registry, this.builder("alchemy_scroll", "everdawn", SkiesItems.alchemy_scroll).requirement("defeat_alchemist"));
      this.register(registry, this.builder("poison_dungeon", "everdawn", SkiesBlocks.poison_keystone).requirement("defeat_alchemist").priority(49));
      this.register(registry, this.builder("venom_sac", "everdawn", SkiesItems.venom_sac).requirement("defeat_arachnarch"));
      this.register(registry, this.builder("different_sword", "everdawn", SkiesItems.different_sword).requirement("defeat_arachnarch"));
      this.register(registry, this.builder("blinding_dungeon", "general", SkiesBlocks.bright_blinding_keystone).priority(50).requirement("enter_one"));
      this.register(registry, this.builder("moonstone", "general", SkiesBlocks.moonstone).requirement("enter_one").priority(44));
      this.register(registry, this.builder("pyrope", "general", SkiesItems.pyrope_gem).requirement("enter_one").priority(43));
      this.register(registry, this.builder("aquite", "general", SkiesItems.aquite).requirement("enter_one").priority(42));
      this.register(registry, this.builder("diopside", "general", SkiesItems.diopside_gem).requirement("enter_one").priority(41));
      this.register(registry, this.builder("charoite", "general", SkiesItems.charoite).requirement("enter_one").priority(40));
      this.register(registry, this.builder("star_emitter", "general", SkiesBlocks.star_emitter).requirement("enter_one"));
      this.register(registry, this.builder("trough", "general", SkiesBlocks.trough).requirement("enter_one"));
      this.register(registry, this.builder("tool_box", "general", SkiesBlocks.tool_box).requirement("enter_one"));
      this.register(registry, this.builder("bag_of_spoils", "general", SkiesBlocks.bag_of_spoils).requirement("enter_one"));
      this.register(registry, this.builder("food_prep_table", "general", SkiesBlocks.food_prep_table).requirement("enter_one"));
      this.register(registry, this.builder("spears", "general", SkiesItems.bluebright_spear).requirement("enter_one"));
      this.register(registry, this.builder("astrolabe", "general", SkiesItems.astrolabe).requirement("enter_one"));
      this.register(registry, this.builder("stick_variations", "general", this.icon("stick_variations")).requirement("enter_one"));
      this.register(registry, this.builder("arcs", "general", this.icon("arcs")).requirement("enter_one"));
      this.register(registry, this.builder("enchanting", "general", Blocks.ENCHANTING_TABLE).requirement("enter_one"));
      this.register(
         registry,
         this.builder("azulfo", "everbright", this.icon("azulfo"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.AZULFO, 27, 15))
      );
      this.register(
         registry,
         this.builder("stardust_ram", "everbright", this.icon("stardust_ram"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.STARDUST_RAM, 45, 30))
      );
      this.register(
         registry,
         this.builder("reindeer", "everbright", this.icon("reindeer"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.REINDEER, 40, 35))
      );
      this.register(
         registry,
         this.builder("snow_owl", "everbright", this.icon("snow_owl"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.SNOW_OWL, 100, 35))
      );
      this.register(
         registry,
         this.builder("grittle_flatfish", "everbright", SkiesItems.grittle_flatfish)
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.GRITTLE_FLATFISH, 70, 20))
      );
      this.register(
         registry,
         this.builder("armored_frost_spirit", "everbright", this.icon("armored_frost_spirit"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.ARMORED_FROST_SPIRIT, 70, 70))
      );
      this.register(
         registry,
         this.builder("crynocerous", "everbright", this.icon("crynocerous"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.CRYNOCEROUS, 25, 30))
      );
      this.register(
         registry,
         this.builder("polargeist", "everbright", this.icon("polargeist"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.POLARGEIST, 33, 20))
      );
      this.register(
         registry,
         this.builder("whistleshell_crab", "everbright", this.icon("whistleshell_crab"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.WHISTLESHELL_CRAB, 23, 10))
      );
      this.register(
         registry,
         this.builder("shrumpty", "everbright", this.icon("shrumpty"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.SHRUMPTY, 70, 50))
      );
      this.register(
         registry,
         this.builder("diophyde_prowler", "everbright", this.icon("diophyde_prowler"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.DIOPHYDE_PROWLER, 35, 30))
      );
      this.register(
         registry,
         this.builder("seclam", "everbright", this.icon("seclam"))
            .requirement("enter_everbright")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.SECLAM, 45, 20))
      );
      this.register(
         registry,
         this.builder("stonelet", "everbright", this.icon("stonelet"))
            .requirement("defeat_summoner")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.STONELET, 50, 50))
      );
      this.register(
         registry,
         this.builder("spewter", "everbright", this.icon("spewter"))
            .requirement("defeat_crusher")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.SPEWTER, 47, 40))
      );
      this.register(
         registry,
         this.builder(
               "summoner",
               Arrays.asList(
                  this.lore("pre", "enter_everbright"),
                  this.lore("post", "defeat_summoner", new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.SUMMONER, 47, 50))
               ),
               "everbright",
               this.icon("summoner")
            )
            .requirement("enter_everbright")
      );
      this.register(
         registry,
         this.builder(
               "starlit_crusher",
               Arrays.asList(
                  this.lore("pre", "defeat_summoner"),
                  this.lore("post", "defeat_crusher", new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.STARLIT_CRUSHER, 20, 40))
               ),
               "everbright",
               this.icon("starlit_crusher")
            )
            .requirement("defeat_summoner")
      );
      this.register(
         registry,
         this.builder("cosmic_fox", "everdawn", this.icon("cosmic_fox"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.COSMIC_FOX, 70, 30))
      );
      this.register(
         registry,
         this.builder("crystal_camel", "everdawn", this.icon("crystal_camel"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.CRYSTAL_CAMEL, 36, 30))
      );
      this.register(
         registry,
         this.builder("shade_monitor", "everdawn", this.icon("shade_monitor"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.SHADE_MONITOR, 24, 10))
      );
      this.register(
         registry,
         this.builder("crogre", "everdawn", this.icon("crogre"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.CROGRE, 45, 20))
      );
      this.register(
         registry,
         this.builder("nyctofly", "everdawn", this.icon("nyctofly"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.NYCTOFLY, 34, 10))
      );
      this.register(
         registry,
         this.builder("venom_spider", "everdawn", this.icon("venom_spider"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.VENOM_SPIDER, 50, 30))
      );
      this.register(
         registry,
         this.builder("emberback", "everdawn", this.icon("emberback"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.EMBERBACK, 60, 30))
      );
      this.register(
         registry,
         this.builder("infested_swarmer", "everdawn", this.icon("infested_swarmer"))
            .requirement("enter_everdawn")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.INFESTED_SWARMER, 29, 20))
      );
      this.register(
         registry,
         this.builder("nested_spider", "everdawn", this.icon("nested_spider"))
            .requirement("defeat_arachnarch")
            .display(new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.NESTED_SPIDER, 70, 30))
      );
      this.register(
         registry,
         this.builder(
               "alchemist",
               Arrays.asList(
                  this.lore("pre", "enter_everdawn"),
                  this.lore("post", "defeat_alchemist", new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.ALCHEMIST, 47, 50))
               ),
               "everdawn",
               this.icon("alchemist")
            )
            .requirement("enter_everdawn")
      );
      this.register(
         registry,
         this.builder(
               "arachnarch",
               Arrays.asList(
                  this.lore("pre", "defeat_alchemist"),
                  this.lore("post", "defeat_arachnarch", new JournalEntryRenderers.EntityDisplay(SkiesEntityTypes.ARACHNARCH, 17, 20))
               ),
               "everdawn",
               this.icon("arachnarch")
            )
            .requirement("defeat_alchemist")
      );
      return registry;
   }

   private ResourceLocation icon(String name) {
      return BlueSkies.locate("textures/gui/journal/icons/" + name + ".png");
   }

   private JournalEntry.Lore lore(String text) {
      return this.lore(text, null, null);
   }

   private JournalEntry.Lore lore(String text, String requirement) {
      return this.lore(text, requirement, null);
   }

   private JournalEntry.Lore lore(String text, JournalEntryRenderers.IRenderable display) {
      return this.lore(text, null, display);
   }

   private JournalEntry.Lore lore(String text, String requirement, JournalEntryRenderers.IRenderable display) {
      return new JournalEntry.Lore(text, requirement == null ? null : BlueSkies.locate(requirement), display);
   }

   private JournalEntry.Builder builder(String key, String section, ItemLike icon) {
      return this.builder(key, section, new JournalEntryRenderers.ItemIcon(icon));
   }

   private JournalEntry.Builder builder(String key, String section, ResourceLocation icon) {
      return this.builder(key, section, new JournalEntryRenderers.TextureIcon(icon));
   }

   private JournalEntry.Builder builder(String key, String section, JournalEntryRenderers.IRenderable icon) {
      List<JournalEntry.Lore> lores = Arrays.asList(new JournalEntry.Lore(String.format("%s.%s.%s.lore", "blue_skies", section, key), null, null));
      return new JournalEntry.Builder(key, String.format("%s.%s.%s.title", "blue_skies", section, key), lores, BlueSkies.locate(section), icon);
   }

   private JournalEntry.Builder builder(String key, List<JournalEntry.Lore> lores, String section, ItemLike icon) {
      return this.builder(key, lores, section, new JournalEntryRenderers.ItemIcon(icon));
   }

   private JournalEntry.Builder builder(String key, List<JournalEntry.Lore> lores, String section, ResourceLocation icon) {
      return this.builder(key, lores, section, new JournalEntryRenderers.TextureIcon(icon));
   }

   private JournalEntry.Builder builder(String key, List<JournalEntry.Lore> lores, String section, JournalEntryRenderers.IRenderable icon) {
      List<JournalEntry.Lore> mappedLores = lores.stream()
         .map(lore -> new JournalEntry.Lore(String.format("%s.%s.%s_%s.lore", "blue_skies", section, key, lore.text), lore.requirement, lore.display))
         .collect(Collectors.toList());
      return new JournalEntry.Builder(key, String.format("%s.%s.%s.title", "blue_skies", section, key), mappedLores, BlueSkies.locate(section), icon);
   }

   private void register(Map<ResourceLocation, JournalEntry> registry, JournalEntry.Builder builder) {
      registry.put(BlueSkies.locate(builder.getEntryKey()), builder.build());
   }
}
