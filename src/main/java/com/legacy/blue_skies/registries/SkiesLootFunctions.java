package com.legacy.blue_skies.registries;

import com.legacy.blue_skies.BlueSkies;
import com.legacy.blue_skies.data.loot_functions.SetArcRarity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SkiesLootFunctions {
   public static final Lazy<LootItemFunctionType> SET_ARC_RARITY = Lazy.of(() -> new LootItemFunctionType(SetArcRarity.CODEC));

   public static void init(RegisterEvent event) {
      event.register(Registries.LOOT_FUNCTION_TYPE, BlueSkies.locate("set_arc_rarity"), SET_ARC_RARITY);
   }
}
