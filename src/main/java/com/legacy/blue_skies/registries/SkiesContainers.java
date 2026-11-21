package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.client.gui.menu.AlchemyTableMenu;
import com.legacy.blue_skies.client.gui.menu.ArcsMenu;
import com.legacy.blue_skies.client.gui.menu.BagOfSpoilsMenu;
import com.legacy.blue_skies.client.gui.menu.FoodPrepMenu;
import com.legacy.blue_skies.client.gui.menu.HorizoniteForgeMenu;
import com.legacy.blue_skies.client.gui.menu.MountInventoryMenu;
import com.legacy.blue_skies.client.gui.menu.SnowcapOvenMenu;
import com.legacy.blue_skies.client.gui.menu.SummoningTableMenu;
import com.legacy.blue_skies.client.gui.menu.ToolboxMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesContainers {
   public static final MenuType<ArcsMenu> ARC_INVENTORY = make((id, inv) -> new ArcsMenu(id, inv, inv.player));
   public static final MenuType<MountInventoryMenu> MOUNT_INVENTORY = makePacket((id, inv, buffer) -> new MountInventoryMenu(id, inv, buffer));
   public static final MenuType<HorizoniteForgeMenu> HORIZONITE_FORGE = make(HorizoniteForgeMenu::new);
   public static final MenuType<SnowcapOvenMenu> SNOWCAP_OVEN = make(SnowcapOvenMenu::new);
   public static final MenuType<SummoningTableMenu> SUMMONING_TABLE = make(SummoningTableMenu::new);
   public static final MenuType<AlchemyTableMenu> ALCHEMY_TABLE = make(AlchemyTableMenu::new);
   public static final MenuType<ToolboxMenu> TOOLBOX = make(ToolboxMenu::new);
   public static final MenuType<FoodPrepMenu> FOOD_PREP_TABLE = make(FoodPrepMenu::new);
   public static final MenuType<BagOfSpoilsMenu> BAG_OF_SPOILS = make(BagOfSpoilsMenu::new);
   private static RegisterEvent registerEvent;

   private static <T extends AbstractContainerMenu> MenuType<T> makePacket(IContainerFactory<T> factory) {
      return new MenuType(factory, FeatureFlags.DEFAULT_FLAGS);
   }

   private static <T extends AbstractContainerMenu> MenuType<T> make(MenuSupplier<T> factory) {
      return new MenuType(factory, FeatureFlags.DEFAULT_FLAGS);
   }

   public static void init(RegisterEvent event) {
      registerEvent = event;
      register("arc_inventory", ARC_INVENTORY);
      register("mount_inventory", MOUNT_INVENTORY);
      register("horizonite_forge", HORIZONITE_FORGE);
      register("snowcap_oven", SNOWCAP_OVEN);
      register("summoning_table", SUMMONING_TABLE);
      register("alchemy_table", ALCHEMY_TABLE);
      register("toolbox", TOOLBOX);
      register("food_prep_table", FOOD_PREP_TABLE);
      register("bag_of_spoils", BAG_OF_SPOILS);
   }

   private static void register(String name, MenuType<?> type) {
      registerEvent.register(Registries.MENU, BlueSkies.locate(name), () -> type);
   }
}
