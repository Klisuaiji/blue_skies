package com.legacy.blue_skies.data;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.managers.AlchemyRecipeManager;
import com.legacy.blue_skies.data.managers.HorizoniteForgeFuelManager;
import com.legacy.blue_skies.data.managers.SkiesDataManager;
import com.legacy.blue_skies.data.managers.SkiesSyncedDataManager;
import com.legacy.blue_skies.data.managers.SnowcapMushroomFreezingManager;
import com.legacy.blue_skies.data.managers.SnowcapOvenFreezingManager;
import com.legacy.blue_skies.data.managers.ToolHandleTypeManager;
import com.legacy.blue_skies.data.managers.journal.JournalEntryManager;
import com.legacy.blue_skies.data.managers.journal.JournalRequirementManager;
import com.legacy.blue_skies.data.managers.journal.JournalSectionManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class BlueSkiesData {
   public static final ToolHandleTypeManager TOOL_HANDLE_TYPES = new ToolHandleTypeManager();
   public static final SnowcapOvenFreezingManager SNOWCAP_OVEN_FREEZING = new SnowcapOvenFreezingManager();
   public static final HorizoniteForgeFuelManager HORIZONITE_FORGE_FUEL = new HorizoniteForgeFuelManager();
   public static final SnowcapMushroomFreezingManager SNOWCAP_MUSHROOM_FREEZING = new SnowcapMushroomFreezingManager();
   public static final AlchemyRecipeManager ALCHEMY_RECIPES = new AlchemyRecipeManager();
   public static final JournalEntryManager JOURNAL_ENTRIES = new JournalEntryManager();
   public static final JournalSectionManager JOURNAL_SECTIONS = new JournalSectionManager();
   public static final JournalRequirementManager JOURNAL_REQUIREMENTS = new JournalRequirementManager();
   public static final List<SkiesSyncedDataManager<?, ?, ?>> SYNCED_MANAGERS = new ArrayList<>();

   public static void addListeners(AddReloadListenerEvent event) {
      register(event, TOOL_HANDLE_TYPES);
      register(event, SNOWCAP_OVEN_FREEZING);
      register(event, HORIZONITE_FORGE_FUEL);
      register(event, SNOWCAP_MUSHROOM_FREEZING);
      register(event, ALCHEMY_RECIPES);
      register(event, JOURNAL_ENTRIES);
      register(event, JOURNAL_SECTIONS);
      register(event, JOURNAL_REQUIREMENTS);
   }

   public static void addSyncedListeners() {
      registerSynced(TOOL_HANDLE_TYPES, SNOWCAP_OVEN_FREEZING, HORIZONITE_FORGE_FUEL, ALCHEMY_RECIPES);
      registerSynced(JOURNAL_ENTRIES, JOURNAL_SECTIONS, JOURNAL_REQUIREMENTS);
   }

   private static void register(AddReloadListenerEvent event, SkiesDataManager<?, ?> dataManager) {
      event.addListener(dataManager);
   }

   private static void registerSynced(SkiesSyncedDataManager<?, ?, ?>... managers) {
      for (SkiesSyncedDataManager<?, ?, ?> manager : managers) {
         SYNCED_MANAGERS.add(manager);
      }
   }

   public static void syncManagersToClient(ServerPlayer player) {
      SYNCED_MANAGERS.forEach(manager -> {
         BlueSkies.LOGGER.debug("Sending {} to {}", manager.getName(), player.getName().getString());
         manager.syncToClient(player);
      });
   }

   public static SkiesSyncedDataManager<?, ?, ?> getSyncedManager(int id) {
      if (id >= 0 && id < SYNCED_MANAGERS.size()) {
         return SYNCED_MANAGERS.get(id);
      } else {
         BlueSkies.LOGGER.warn("Attempted to get a client-synced manager with an invalid id: {}", id);
         return TOOL_HANDLE_TYPES;
      }
   }
}
