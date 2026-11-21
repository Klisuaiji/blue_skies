package com.legacy.blue_skies.data.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesStructures;
import com.legacy.blue_skies.triggers.SpecialArachnarchTrigger;
import com.legacy.blue_skies.triggers.SpecialCrusherTrigger;
import com.legacy.blue_skies.triggers.UsedAlchemyTableTrigger;
import com.legacy.blue_skies.triggers.UsedToolboxTrigger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.critereon.EntityPredicate.Builder;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger.TriggerInstance;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import org.apache.logging.log4j.Logger;

public class SkiesAdvancementProv extends AdvancementProvider {
   private static final Logger LOGGER = BlueSkies.makeLogger(SkiesAdvancementProv.class);
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

   public SkiesAdvancementProv(DataGenerator gen, CompletableFuture<Provider> lookup, ExistingFileHelper existingFileHelper) {
      super(gen.getPackOutput(), lookup, existingFileHelper, List.of(new SkiesAdvancementProv.BlueSkiesAdvancements()));
   }

   private static class BlueSkiesAdvancements implements AdvancementGenerator {
      private String section = "";

      public void generate(Provider lookup, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
         AdvancementHolder root = this.builder(
               SkiesItems.multi_portal_item, "root", BlueSkies.locate("textures/block/dungeon/blinding_stone.png"), AdvancementType.TASK, false, false, false
            )
            .requirements(Strategy.OR)
            .addCriterion("enter_everbright", this.changeDim(genEverbrightKey()))
            .addCriterion("enter_everdawn", this.changeDim(genEverdawnKey()))
            .save(saver, BlueSkies.find("root"));
         AdvancementHolder thinkingPortals = this.builder(SkiesItems.zeal_lighter, "thinking_with_portals", AdvancementType.TASK, true, true, false)
            .parent(root)
            .addCriterion("enter_everbright", this.changeDim(genEverbrightKey()))
            .addCriterion("enter_everdawn", this.changeDim(genEverdawnKey()))
            .save(saver, BlueSkies.find("thinking_with_portals"));
         AdvancementHolder moonstoneShard = this.builder(SkiesItems.moonstone_shard, "get_moonstone_shard", AdvancementType.TASK, true, true, false)
            .parent(thinkingPortals)
            .addCriterion(
               "obtain_moonstone_shard",
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.moonstone_shard})
            )
            .save(saver, BlueSkies.find("get_moonstone_shard"));
         AdvancementHolder pyrope = this.builder(SkiesItems.pyrope_gem, "get_pyrope", AdvancementType.TASK, true, true, false)
            .parent(moonstoneShard)
            .addCriterion(
               "obtain_material", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.pyrope_gem})
            )
            .save(saver, BlueSkies.find("get_pyrope"));
         AdvancementHolder aquite = this.builder(SkiesItems.aquite, "get_aquite", AdvancementType.TASK, true, true, false)
            .parent(pyrope)
            .addCriterion(
               "obtain_material", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.aquite})
            )
            .save(saver, BlueSkies.find("get_aquite"));
         AdvancementHolder diopside = this.builder(SkiesItems.diopside_gem, "get_diopside", AdvancementType.TASK, true, true, false)
            .parent(aquite)
            .addCriterion(
               "obtain_material", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.diopside_gem})
            )
            .save(saver, BlueSkies.find("get_diopside"));
         AdvancementHolder charoite = this.builder(SkiesItems.charoite, "get_charoite", AdvancementType.TASK, true, true, false)
            .parent(aquite)
            .addCriterion(
               "obtain_material", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.charoite})
            )
            .save(saver, BlueSkies.find("get_charoite"));
         AdvancementHolder foodPrepTable = this.builder(SkiesBlocks.food_prep_table, "craft_food_prep_table", AdvancementType.TASK, true, true, false)
            .parent(thinkingPortals)
            .addCriterion(
               "obtain_prep_table",
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesBlocks.food_prep_table})
            )
            .save(saver, BlueSkies.find("craft_food_prep_table"));
         AdvancementHolder enterBlindingDungeon = this.enterAnyStructure(
               this.builder(SkiesBlocks.bright_blinding_keystone, "enter_blinding_dungeon", AdvancementType.GOAL, true, true, false)
                  .parent(thinkingPortals)
                  .requirements(Strategy.OR),
               List.of(SkiesStructures.EVERBRIGHT_BLINDING_DUNGEON.getStructure().getKey(), SkiesStructures.EVERDAWN_BLINDING_DUNGEON.getStructure().getKey())
            )
            .save(saver, BlueSkies.find("enter_blinding_dungeon"));
         AdvancementHolder keys = this.builder(SkiesItems.blinding_key, "keys", AdvancementType.TASK, true, true, false)
            .parent(enterBlindingDungeon)
            .addCriterion(
               "obtain_key", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.blinding_key})
            )
            .save(saver, BlueSkies.find("keys"));
         AdvancementHolder regrowth = this.builder(SkiesBlocks.comet_sapling, "regrowth", AdvancementType.TASK, true, true, false)
            .parent(keys)
            .addCriterion(
               "have_sapling", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesBlocks.comet_sapling})
            )
            .save(saver, BlueSkies.find("regrowth"));
         AdvancementHolder cometPie = this.builder(SkiesBlocks.comet_berry_pie, "blue_sky_pie", AdvancementType.GOAL, true, true, false)
            .parent(regrowth)
            .addCriterion(
               "have_pie", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesBlocks.comet_berry_pie})
            )
            .save(saver, BlueSkies.find("blue_sky_pie"));
         this.setSection("everbright");
         AdvancementHolder enterBright = this.builder(SkiesBlocks.everbright_portal, "enter", AdvancementType.TASK, true, true, false)
            .parent(root)
            .addCriterion("enter_everbright", this.changeDim(genEverbrightKey()))
            .save(saver, BlueSkies.find("everbright/enter"));
         AdvancementHolder getWoodBright = this.haveAnyItem(
               this.builder(SkiesBlocks.bluebright_log, "get_wood", AdvancementType.TASK, true, true, false).parent(enterBright).requirements(Strategy.OR),
               List.of(
                  SkiesBlocks.bluebright_log,
                  SkiesBlocks.bluebright_wood,
                  SkiesBlocks.starlit_log,
                  SkiesBlocks.starlit_wood,
                  SkiesBlocks.frostbright_log,
                  SkiesBlocks.frostbright_wood
               )
            )
            .save(saver, BlueSkies.find("everbright/get_wood"));
         AdvancementHolder snowcapOven = this.builder(SkiesBlocks.snowcap_oven, "craft_snowcap_oven", AdvancementType.TASK, true, true, false)
            .parent(getWoodBright)
            .addCriterion(
               "obtain_oven", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesBlocks.snowcap_oven})
            )
            .save(saver, BlueSkies.find("everbright/craft_snowcap_oven"));
         AdvancementHolder falsite = this.builder(SkiesItems.falsite_ingot, "get_falsite", AdvancementType.TASK, true, true, false)
            .parent(getWoodBright)
            .addCriterion(
               "obtain_material",
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.falsite_ingot})
            )
            .save(saver, BlueSkies.find("everbright/get_falsite"));
         AdvancementHolder ventium = this.builder(SkiesItems.ventium_ingot, "get_ventium", AdvancementType.TASK, true, true, false)
            .parent(getWoodBright)
            .addCriterion(
               "obtain_material",
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.ventium_ingot})
            )
            .save(saver, BlueSkies.find("everbright/get_ventium"));
         AdvancementHolder useToolBox = this.builder(SkiesBlocks.tool_box, "use_tool_box", AdvancementType.TASK, true, true, false)
            .parent(ventium)
            .addCriterion("use_tool_box", UsedToolboxTrigger.Instance.any())
            .save(saver, BlueSkies.find("everbright/use_tool_box"));
         AdvancementHolder useFalsiteInToolBox = this.builder(SkiesItems.falsite_ingot, "use_falsite_in_tool_box", AdvancementType.TASK, true, true, false)
            .parent(useToolBox)
            .addCriterion("use_tool_box", UsedToolboxTrigger.Instance.withModifier(SkiesItemTags.FALSITE))
            .save(saver, BlueSkies.find("everbright/use_falsite_in_tool_box"));
         AdvancementHolder killSummoner = this.builder(SkiesItems.ethereal_arc, "kill_summoner", AdvancementType.CHALLENGE, true, true, false)
            .parent(getWoodBright)
            .addCriterion(
               "kill_summoner",
               net.minecraft.advancements.critereon.KilledTrigger.TriggerInstance.playerKilledEntity(Builder.entity().of(SkiesEntityTypes.SUMMONER))
            )
            .save(saver, BlueSkies.find("everbright/kill_summoner"));
         AdvancementHolder summonGolem = this.builder(SkiesItems.summoning_tome, "summon_golem", AdvancementType.TASK, true, true, false)
            .parent(killSummoner)
            .addCriterion(
               "summon_golem",
               net.minecraft.advancements.critereon.SummonedEntityTrigger.TriggerInstance.summonedEntity(Builder.entity().of(SkiesEntityTypes.ARTIFICIAL_GOLEM))
            )
            .save(saver, BlueSkies.find("everbright/summon_artificial_golem"));
         AdvancementHolder regenBrewberry = this.builder(SkiesItems.pink_brewberry, "regen_brewberry", AdvancementType.GOAL, true, true, false)
            .parent(regrowth)
            .addCriterion("have_pink_brewberry", net.minecraft.advancements.critereon.ConsumeItemTrigger.TriggerInstance.usedItem(SkiesItems.pink_brewberry))
            .save(saver, BlueSkies.find("everbright/regen_brewberry"));
         AdvancementHolder enterNatureDungeon = this.builder(SkiesBlocks.nature_keystone, "enter_nature_dungeon", AdvancementType.GOAL, true, true, false)
            .parent(killSummoner)
            .addCriterion("entered_dungeon", this.enterStructure(SkiesStructures.NATURE_DUNGEON.getStructure().getKey()))
            .save(saver, BlueSkies.find("everbright/enter_nature_dungeon"));
         AdvancementHolder killCrusher = this.builder(SkiesItems.nature_arc, "kill_starlit_crusher", AdvancementType.CHALLENGE, true, true, false)
            .parent(enterNatureDungeon)
            .addCriterion(
               "kill_starlit_crusher",
               net.minecraft.advancements.critereon.KilledTrigger.TriggerInstance.playerKilledEntity(Builder.entity().of(SkiesEntityTypes.STARLIT_CRUSHER))
            )
            .save(saver, BlueSkies.find("everbright/kill_starlit_crusher"));
         AdvancementHolder killCrusherSpecial = this.builder(
               SkiesItems.nature_arc, "kill_starlit_crusher_special", AdvancementType.CHALLENGE, true, true, false
            )
            .parent(killCrusher)
            .addCriterion("punch_crusher", SpecialCrusherTrigger.Instance.any())
            .save(saver, BlueSkies.find("everbright/kill_starlit_crusher_special"));
         this.setSection("everdawn");
         AdvancementHolder enterDawn = this.builder(SkiesBlocks.everdawn_portal, "enter", AdvancementType.TASK, true, true, false)
            .parent(root)
            .addCriterion("enter_everdawn", this.changeDim(genEverdawnKey()))
            .save(saver, BlueSkies.find("everdawn/enter"));
         AdvancementHolder getWoodDawn = this.haveAnyItem(
               this.builder(SkiesBlocks.lunar_log, "get_wood", AdvancementType.TASK, true, true, false).parent(enterDawn).requirements(Strategy.OR),
               List.of(
                  SkiesBlocks.lunar_log, SkiesBlocks.lunar_wood, SkiesBlocks.dusk_log, SkiesBlocks.dusk_wood, SkiesBlocks.maple_log, SkiesBlocks.maple_wood
               )
            )
            .save(saver, BlueSkies.find("everdawn/get_wood"));
         AdvancementHolder horizonite = this.builder(SkiesItems.horizonite_ingot, "get_horizonite", AdvancementType.TASK, true, true, false)
            .parent(getWoodDawn)
            .addCriterion(
               "obtain_material",
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.horizonite_ingot})
            )
            .save(saver, BlueSkies.find("everdawn/get_horizonite"));
         AdvancementHolder horizoniteForge = this.builder(SkiesBlocks.horizonite_forge, "craft_horizonite_forge", AdvancementType.TASK, true, true, false)
            .parent(horizonite)
            .addCriterion(
               "obtain_forge",
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesBlocks.horizonite_forge})
            )
            .save(saver, BlueSkies.find("everdawn/craft_horizonite_forge"));
         AdvancementHolder poisonShroom = this.builder(SkiesBlocks.baneful_mushroom, "poison_shroom", AdvancementType.TASK, true, true, false)
            .parent(enterDawn)
            .addCriterion("touch_shroom", net.minecraft.advancements.critereon.EnterBlockTrigger.TriggerInstance.entersBlock(SkiesBlocks.baneful_mushroom))
            .save(saver, BlueSkies.find("everdawn/poison_shroom"));
         AdvancementHolder killAlchemist = this.builder(SkiesItems.dusk_arc, "kill_alchemist", AdvancementType.CHALLENGE, true, true, false)
            .parent(getWoodDawn)
            .addCriterion(
               "kill_alchemist",
               net.minecraft.advancements.critereon.KilledTrigger.TriggerInstance.playerKilledEntity(Builder.entity().of(SkiesEntityTypes.ALCHEMIST))
            )
            .save(saver, BlueSkies.find("everdawn/kill_alchemist"));
         AdvancementHolder useAlchemyTable = this.builder(SkiesBlocks.alchemy_table, "use_alchemy_table", AdvancementType.TASK, true, true, false)
            .parent(killAlchemist)
            .addCriterion("use_alchemy_table", UsedAlchemyTableTrigger.Instance.any())
            .save(saver, BlueSkies.find("everdawn/use_alchemy_table"));
         AdvancementHolder enterPoisonDungeon = this.builder(SkiesBlocks.poison_keystone, "enter_poison_dungeon", AdvancementType.GOAL, true, true, false)
            .parent(killAlchemist)
            .addCriterion("entered_dungeon", this.enterStructure(SkiesStructures.POISON_DUNGEON.getStructure().getKey()))
            .save(saver, BlueSkies.find("everdawn/enter_poison_dungeon"));
         AdvancementHolder killArachnarch = this.builder(SkiesItems.poison_arc, "kill_arachnarch", AdvancementType.CHALLENGE, true, true, false)
            .parent(enterPoisonDungeon)
            .addCriterion(
               "kill_arachnarch",
               net.minecraft.advancements.critereon.KilledTrigger.TriggerInstance.playerKilledEntity(Builder.entity().of(SkiesEntityTypes.ARACHNARCH))
            )
            .save(saver, BlueSkies.find("everdawn/kill_arachnarch"));
         AdvancementHolder killArachnarchSpecial = this.builder(SkiesItems.poison_arc, "kill_arachnarch_special", AdvancementType.CHALLENGE, true, true, false)
            .parent(killArachnarch)
            .addCriterion("drop_arachnarch", SpecialArachnarchTrigger.Instance.any())
            .save(saver, BlueSkies.find("everdawn/kill_starlit_arachnarch_special"));
         AdvancementHolder different = this.builder(SkiesItems.different_sword, "think_different", AdvancementType.GOAL, true, true, false)
            .parent(killArachnarch)
            .addCriterion(
               "have_sword", net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{SkiesItems.different_sword})
            )
            .save(saver, BlueSkies.find("everdawn/think_different"));
         AdvancementHolder killBugs = this.killAllOf(
               this.builder(SkiesItems.bug_guts, "kill_bug", AdvancementType.GOAL, true, true, false).parent(poisonShroom),
               List.of(
                  SkiesEntityTypes.VENOM_SPIDER,
                  SkiesEntityTypes.NYCTOFLY,
                  SkiesEntityTypes.EMBERBACK,
                  SkiesEntityTypes.INFESTED_SWARMER,
                  SkiesEntityTypes.ARACHNARCH,
                  SkiesEntityTypes.NESTED_SPIDER
               )
            )
            .save(saver, BlueSkies.find("everdawn/kill_bug"));
         AdvancementHolder alchemyGold = this.builder(Items.GOLD_INGOT, "alchemy_gold", AdvancementType.CHALLENGE, true, true, true)
            .parent(useAlchemyTable)
            .addCriterion("craft_gold", UsedAlchemyTableTrigger.Instance.withItem(Items.GOLD_INGOT))
            .save(saver, BlueSkies.find("everdawn/alchemy_gold"));
         this.setSection("progression");
         AdvancementHolder impossibleRoot = net.minecraft.advancements.Advancement.Builder.advancement()
            .addCriterion(
               "impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new net.minecraft.advancements.critereon.ImpossibleTrigger.TriggerInstance())
            )
            .save(saver, BlueSkies.find("progression/root"));
         this.unlockKeyTrade("unlock_everbright_blinding_key_trade", SkiesItems.blinding_key, SkiesBlocks.bright_blinding_keystone, impossibleRoot, saver);
         this.unlockKeyTrade("unlock_everdawn_blinding_key_trade", SkiesItems.blinding_key, SkiesBlocks.dawn_blinding_keystone, impossibleRoot, saver);
         this.unlockKeyTrade("unlock_nature_key_trade", SkiesItems.nature_key, SkiesBlocks.nature_keystone, impossibleRoot, saver);
         this.unlockKeyTrade("unlock_poison_key_trade", SkiesItems.poison_key, SkiesBlocks.poison_keystone, impossibleRoot, saver);
      }

      private net.minecraft.advancements.Advancement.Builder killAllOf(net.minecraft.advancements.Advancement.Builder builder, List<EntityType<?>> entities) {
         entities.forEach(
            e -> builder.addCriterion(
               "kill_" + BuiltInRegistries.ENTITY_TYPE.getKey(e).getPath(),
               net.minecraft.advancements.critereon.KilledTrigger.TriggerInstance.playerKilledEntity(Builder.entity().of(e))
            )
         );
         return builder;
      }

      private net.minecraft.advancements.Advancement.Builder haveAnyItem(net.minecraft.advancements.Advancement.Builder builder, List<ItemLike> items) {
         items.forEach(
            item -> builder.addCriterion(
               "has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(),
               net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{item})
            )
         );
         return builder;
      }

      private net.minecraft.advancements.Advancement.Builder enterAnyStructure(
         net.minecraft.advancements.Advancement.Builder builder, List<ResourceKey<Structure>> structures
      ) {
         structures.forEach(
            structure -> builder.addCriterion("entered_" + structure.location().getPath(), this.enterStructure((ResourceKey<Structure>)structure))
         );
         return builder;
      }

      private Criterion<net.minecraft.advancements.critereon.ChangeDimensionTrigger.TriggerInstance> changeDim(ResourceKey<Level> type) {
         return net.minecraft.advancements.critereon.ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(type);
      }

      private Criterion<net.minecraft.advancements.critereon.PlayerTrigger.TriggerInstance> enterStructure(ResourceKey<Structure> type) {
         return net.minecraft.advancements.critereon.PlayerTrigger.TriggerInstance.located(
            net.minecraft.advancements.critereon.LocationPredicate.Builder.inStructure(type)
         );
      }

      private void unlockKeyTrade(String name, Item key, Block keystone, AdvancementHolder parent, Consumer<AdvancementHolder> consumer) {
         Criterion<TriggerInstance> clickKeystone = TriggerInstance.itemUsedOnBlock(
            net.minecraft.advancements.critereon.LocationPredicate.Builder.location()
               .setBlock(net.minecraft.advancements.critereon.BlockPredicate.Builder.block().of(new Block[]{keystone})),
            net.minecraft.advancements.critereon.ItemPredicate.Builder.item()
         );
         net.minecraft.advancements.Advancement.Builder.advancement()
            .parent(parent)
            .display(key, Component.empty(), Component.empty(), null, AdvancementType.TASK, false, false, true)
            .addCriterion("click_keystone", clickKeystone)
            .save(consumer, BlueSkies.find("progression/" + name));
      }

      private Component translate(String key) {
         return Component.translatable("advancements.blue_skies" + (this.section.equals("") ? "" : "." + this.section) + "." + key);
      }

      private void setSection(String name) {
         this.section = name;
      }

      private net.minecraft.advancements.Advancement.Builder builder(
         ItemLike displayItem,
         String name,
         ResourceLocation background,
         AdvancementType advancementType,
         boolean showToast,
         boolean announceToChat,
         boolean hidden
      ) {
         return net.minecraft.advancements.Advancement.Builder.advancement()
            .display(displayItem, this.translate(name), this.translate(name + ".desc"), background, advancementType, showToast, announceToChat, hidden);
      }

      private net.minecraft.advancements.Advancement.Builder builder(
         ItemLike displayItem, String name, AdvancementType advancementType, boolean showToast, boolean announceToChat, boolean hidden
      ) {
         return this.builder(displayItem, name, (ResourceLocation)null, advancementType, showToast, announceToChat, hidden);
      }

      public static ResourceKey<Level> genEverbrightKey() {
         return ResourceKey.create(Registries.DIMENSION, SkiesDimensions.EVERBRIGHT_ID);
      }

      public static ResourceKey<Level> genEverdawnKey() {
         return ResourceKey.create(Registries.DIMENSION, SkiesDimensions.EVERDAWN_ID);
      }
   }
}
