package com.legacy.blue_skies;

import com.legacy.blue_skies.compat.*;
import com.legacy.blue_skies.assets.BlueSkiesAssets;
import com.legacy.blue_skies.blocks.util.Composting;
import com.legacy.blue_skies.blocks.util.DispenserBehaviors;
import com.legacy.blue_skies.blocks.util.Flammability;
import com.legacy.blue_skies.blocks.util.MapColors;
import com.legacy.blue_skies.blocks.util.ToolCompat;
import com.legacy.blue_skies.client.IncompatHandler;
import com.legacy.blue_skies.client.audio.SkiesMusicHandler;
import com.legacy.blue_skies.client.events.SkiesClientEvents;
import com.legacy.blue_skies.client.gui.screen.AlchemyTableScreen;
import com.legacy.blue_skies.client.gui.screen.ArcsScreen;
import com.legacy.blue_skies.client.gui.screen.BagOfSpoilsScreen;
import com.legacy.blue_skies.client.gui.screen.FoodPrepScreen;
import com.legacy.blue_skies.client.gui.menu.HorizoniteForgeMenu;
import com.legacy.blue_skies.client.gui.screen.HorizoniteForgeScreen;
import com.legacy.blue_skies.client.gui.screen.MountInventoryScreen;
import com.legacy.blue_skies.client.gui.screen.SnowcapOvenScreen;
import com.legacy.blue_skies.client.gui.screen.SummoningTableScreen;
import com.legacy.blue_skies.client.gui.screen.ToolboxScreen;
import com.legacy.blue_skies.client.init.SkiesAtlases;
import com.legacy.blue_skies.client.init.SkiesBlockLayers;
import com.legacy.blue_skies.client.init.SkiesBlockModels;
import com.legacy.blue_skies.client.init.SkiesItemModelPredicates;
import com.legacy.blue_skies.client.init.SkiesResourcePackHandler;
import com.legacy.blue_skies.commands.BlueSkiesCommand;
import com.legacy.blue_skies.data.BlueSkiesData;
import com.legacy.blue_skies.data.BlueSkiesDataGen;
import com.legacy.blue_skies.data.objects.tags.SkiesBlockTags;
import com.legacy.blue_skies.data.objects.tags.SkiesEntityTags;
import com.legacy.blue_skies.data.objects.tags.SkiesItemTags;
import com.legacy.blue_skies.events.SkiesEvents;
import com.legacy.blue_skies.events.SkiesPlayerEvents;
import com.legacy.blue_skies.items.util.FoodPrepUtils;
import com.legacy.blue_skies.registries.SkiesArmorTrims;
import com.legacy.blue_skies.registries.SkiesContainers;
import com.legacy.blue_skies.registries.SkiesEntityTypes;
import com.legacy.blue_skies.registries.SkiesParticles;
import com.legacy.blue_skies.registries.SkiesRegistry;
import com.legacy.blue_skies.registries.SkiesWoodTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("blue_skies")
public class BlueSkies {
   public static final String MODID = "blue_skies";
   public static final Logger LOGGER = LogManager.getLogger("ModdingLegacy/blue_skies");
   public static final MLSupporter ML_SUPPORTER = new MLSupporter("blue_skies");
   private static final BlueSkiesScheduler SCHEDULER = new BlueSkiesScheduler();
   protected static boolean isChristmas = false;
   protected static boolean isHalloween = false;
   private static final boolean INDEV = false;
   public static IEventBus MOD_BUS;
   public static final boolean IDE = isRunningFromIDE();

   public BlueSkies(IEventBus modBus, ModContainer modContainer) {
      MOD_BUS = modBus;
      // 1.21.1 registers configs on the mod container injected into the constructor;
      // ModLoadingContext no longer exposes registerConfig.
      modContainer.registerConfig(Type.COMMON, BlueSkiesConfig.COMMON_SPEC);
      IEventBus forgeBus = NeoForge.EVENT_BUS;
      if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
         SCHEDULER.start(forgeBus);
      }

      if (FMLEnvironment.dist == Dist.CLIENT) {
         SCHEDULER.runChecks();
         modContainer.registerConfig(Type.CLIENT, BlueSkiesConfig.CLIENT_SPEC);
         BlueSkiesAssets.init();
         modBus.addListener(BlueSkies::clientInit);
         modBus.addListener(BlueSkies::registerMenus);
         modBus.addListener(SkiesBlockModels::modelRegistry);
         modBus.addListener(SkiesBlockModels::onModelBake);
         modBus.addListener(SkiesResourcePackHandler::packRegistry);
         modBus.addListener(SkiesClientEvents::registerOverlays);
         modBus.addListener(SkiesClientEvents::initDimensionRenderInfo);
         modBus.addListener(SkiesArmorTrims::registerSpriteData);
         modBus.addListener(SkiesParticles.Register::registerParticleFactories);
         forgeBus.register(SkiesMusicHandler.class);
         forgeBus.register(SkiesClientEvents.class);
      }

      SkiesBlockTags.init();
      SkiesItemTags.init();
      SkiesEntityTags.init();
      modBus.addListener(BlueSkies::commonInit);
      modBus.addListener(SkiesEntityTypes::onAttributesRegistered);
      modBus.addListener(EventPriority.LOWEST, SkiesEntityTypes.SpawnConditions::registerPlacements);
      modBus.addListener(SkiesEntityTypes.SpawnConditions::registerPlacementOverrides);
      modBus.register(SkiesRegistry.class);
      modBus.register(BlueSkiesDataGen.class);
      forgeBus.addListener(BlueSkies::onCommandsRegistered);
      forgeBus.addListener(BlueSkiesData::addListeners);
      forgeBus.register(SkiesEvents.class);
      forgeBus.register(SkiesPlayerEvents.class);
   }

   public static ResourceLocation locate(String key) {
      return ResourceLocation.fromNamespaceAndPath("blue_skies", key);
   }

   public static String find(String key) {
      return "blue_skies:" + key;
   }

   public static void commonInit(FMLCommonSetupEvent event) {
      if (isRunningFromIDE()) {
         LOGGER.warn("Blue Skies is being run from an IDE or via Gradle.");
         LOGGER.warn("If you are planning to make a contribution to Blue Skies, please refer to the LICENSE.md file at the root directory of this repository.");
      } else if (isIndevBuild()) {
         LOGGER.warn("This is an indev build of Blue Skies. Do NOT expect support with it! Things WILL break. Treat it like a Minecraft snapshot version!");
      }

      event.enqueueWork(() -> {
         DispenserBehaviors.init();
         Composting.init();
         MapColors.init();
         Flammability.init();
      });
      ToolCompat.init();
      BlueSkiesData.addSyncedListeners();
   }

   public static void registerMenus(RegisterMenuScreensEvent event) {
      event.register(SkiesContainers.ARC_INVENTORY, ArcsScreen::new);
      event.register(SkiesContainers.MOUNT_INVENTORY, MountInventoryScreen::new);
      event.register(SkiesContainers.HORIZONITE_FORGE, HorizoniteForgeScreen::new);
      event.register(SkiesContainers.SNOWCAP_OVEN, SnowcapOvenScreen::new);
      event.register(SkiesContainers.SUMMONING_TABLE, SummoningTableScreen::new);
      event.register(SkiesContainers.ALCHEMY_TABLE, AlchemyTableScreen::new);
      event.register(SkiesContainers.TOOLBOX, ToolboxScreen::new);
      event.register(SkiesContainers.FOOD_PREP_TABLE, FoodPrepScreen::new);
      event.register(SkiesContainers.BAG_OF_SPOILS, BagOfSpoilsScreen::new);
   }

   public static void clientInit(FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         SkiesWoodTypes.init();
         SkiesAtlases.initWoodTypes();
         SkiesBlockLayers.init();
         SkiesItemModelPredicates.init();
      });
      SkiesClientEvents.Panoramas.registerPanoramas();
      FoodPrepUtils.FoodColor.setupColors();
      IncompatHandler.initStartup();
   }

   public static void onCommandsRegistered(RegisterCommandsEvent event) {
      BlueSkiesCommand.register(event.getDispatcher());
   }

   private static boolean isRunningFromIDE() {
      String p = System.getProperty(String.format("%s.iside", "blue_skies"));
      return Boolean.parseBoolean(p);
   }

   public static Logger makeLogger(Class<?> containerClass) {
      return LogManager.getLogger("ModdingLegacy/blue_skies/" + containerClass.getSimpleName());
   }

   public static boolean isIndevBuild() {
      return IDE;
   }

   public static void printStackTrace() {
      if (IDE) {
         StackTraceElement[] stackArray = Thread.currentThread().getStackTrace();
         System.out.println("---------- Stack Trace ----------");

         for (StackTraceElement stack : stackArray) {
            System.out.println(stack.getClassName() + " " + stack.getMethodName() + " " + stack.getLineNumber());
         }
      }
   }
}
