package com.legacy.blue_skies.asm_hooks;

import com.legacy.blue_skies.entities.villager.SkiesTradeModifiers;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesSchedules;
import com.legacy.blue_skies.registries.SkiesVillagers;
import java.util.Set;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.util.Lazy;

public class VillagerHooks {
   private static final Lazy<Set<Item>> PLANTABLES = Lazy.of(
      () -> Set.of(
         SkiesItems.solnut,
         SkiesItems.cryo_root,
         SkiesItems.fiery_bean_seeds,
         SkiesItems.pine_fruit_seeds,
         SkiesItems.scalefruit_seeds,
         SkiesItems.winter_leaf_seeds
      )
   );

   public static void registerBrainGoals(Villager villager, Brain<Villager> villagerBrain) {
      if (!villager.isBaby() && villager.getVillagerData().getProfession() == SkiesVillagers.NIGHTWATCHER.get()) {
         villagerBrain.setSchedule((Schedule)SkiesSchedules.NIGHT_OWL.get());
      }
   }

   public static boolean hasFarmSeeds(Villager villager) {
      return villager.getInventory().hasAnyOf((Set)PLANTABLES.get());
   }

   public static void dimBasedTrades(Villager villager) {
      SkiesTradeModifiers.replaceOffers(villager);
   }
}
