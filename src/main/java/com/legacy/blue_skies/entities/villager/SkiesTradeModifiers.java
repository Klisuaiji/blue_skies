package com.legacy.blue_skies.entities.villager;

import com.legacy.blue_skies.items.util.FoodPrepUtils;
import com.legacy.blue_skies.items.util.ToolUtils;
import com.legacy.blue_skies.registries.SkiesBlocks;
import com.legacy.blue_skies.registries.SkiesDimensions;
import com.legacy.blue_skies.registries.SkiesItems;
import com.legacy.blue_skies.registries.SkiesVillagers;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class SkiesTradeModifiers {
   public static void replaceOffers(Villager villager) {
      boolean everbright = villager.level().dimension() == SkiesDimensions.everbrightKey();
      boolean everdawn = villager.level().dimension() == SkiesDimensions.everdawnKey();
      if ((everbright || everdawn) && SkiesVillagers.SKIES_TYPES.contains(villager.getVillagerData().getType())) {
         Item stick = everbright ? SkiesItems.bluebright_stick : SkiesItems.lunar_stick;
         Item strongStick = everbright ? SkiesItems.frostbright_stick : SkiesItems.maple_stick;
         VillagerProfession prof = villager.getVillagerData().getProfession();
         villager.getOffers()
            .forEach(
               offer -> {
                  if (prof == VillagerProfession.FARMER) {
                     replace(offer, Items.WHEAT, everbright ? SkiesItems.pine_fruit : SkiesItems.scalefruit);
                     replace(offer, Items.POTATO, SkiesItems.winter_leaves);
                     replace(offer, Items.CARROT, SkiesItems.fiery_beans);
                     replace(offer, Items.BEETROOT, SkiesItems.solnut);
                     replace(offer, Items.BREAD, SkiesItems.cryo_root);
                     replace(offer, Items.MELON, everbright ? SkiesItems.brewberry : SkiesItems.crescent_fruit);
                     replace(offer, Items.CAKE, SkiesBlocks.comet_berry_pie);
                  } else if (prof == VillagerProfession.FISHERMAN) {
                     if (everbright) {
                        replace(offer, Items.SALMON, SkiesItems.municipal_monkfish);
                        replace(offer, Items.COOKED_SALMON, SkiesItems.cooked_municipal_monkfish);
                        replace(offer, Items.TROPICAL_FISH, SkiesItems.grittle_flatfish);
                        replace(offer, Items.COD_BUCKET, SkiesItems.ventium_municipal_monkfish_bucket);
                     } else {
                        replace(offer, Items.COD, SkiesItems.charscale_moki);
                        replace(offer, Items.COOKED_COD, SkiesItems.cooked_charscale_moki);
                        replace(offer, Items.COD_BUCKET, SkiesItems.ventium_charscale_moki_bucket);
                        replace(offer, Items.SALMON, SkiesItems.horizofin_tunid);
                        replace(offer, Items.COOKED_SALMON, SkiesItems.cooked_horizofin_tunid);
                     }
                  } else if (prof == VillagerProfession.CARTOGRAPHER) {
                     replace(offer, Blocks.GLASS_PANE, everbright ? SkiesBlocks.midnight_glass_pane : SkiesBlocks.crystal_glass_pane);
                  } else if (prof == VillagerProfession.LIBRARIAN) {
                     replace(offer, Blocks.BOOKSHELF, everbright ? SkiesBlocks.bluebright_bookshelf : SkiesBlocks.lunar_bookshelf);
                  } else if (prof == VillagerProfession.FLETCHER) {
                     replace(offer, Items.STICK, everbright ? SkiesItems.bluebright_stick : SkiesItems.lunar_stick);
                  } else if (prof == VillagerProfession.MASON) {
                     replace(offer, Items.BRICK, everbright ? SkiesBlocks.lunar_stonebrick : SkiesBlocks.turquoise_stonebrick);
                     replace(offer, Items.GRANITE, everbright ? SkiesBlocks.taratite : SkiesBlocks.umber);
                     replace(offer, Items.POLISHED_GRANITE, everbright ? SkiesBlocks.polished_taratite : SkiesBlocks.polished_umber);
                     replace(offer, Items.DIORITE, everbright ? SkiesBlocks.rimestone : SkiesBlocks.cinderstone);
                     replace(offer, Items.POLISHED_DIORITE, everbright ? SkiesBlocks.polished_taratite : SkiesBlocks.polished_cinderstone);
                     replace(offer, Items.ANDESITE, SkiesBlocks.brumble);
                     replace(offer, Items.POLISHED_ANDESITE, SkiesBlocks.polished_brumble);
                     replace(offer, Items.STONE, everbright ? SkiesBlocks.turquoise_stone : SkiesBlocks.lunar_stone);
                     replace(offer, Items.CHISELED_STONE_BRICKS, everbright ? SkiesBlocks.chiseled_turquoise_stonebrick : SkiesBlocks.chiseled_lunar_stonebrick);
                  } else if (prof == VillagerProfession.BUTCHER) {
                     replace(offer, Items.CHICKEN, SkiesItems.monitor_tail);
                     replace(offer, Items.PORKCHOP, SkiesItems.venison);
                     replace(offer, Items.RABBIT, SkiesItems.grittle_flatfish);
                     List<List<ItemLike>> recipes = List.of(
                        List.of(
                           SkiesItems.brewberry,
                           SkiesBlocks.midday_bayhop,
                           SkiesBlocks.midday_bayhop,
                           SkiesItems.cooked_municipal_monkfish,
                           SkiesItems.cooked_grittle_flatfish
                        ),
                        List.of(SkiesItems.brewberry, SkiesBlocks.camellia, SkiesBlocks.camellia, SkiesItems.winter_leaves, SkiesItems.comet_berries),
                        List.of(SkiesItems.brewberry, SkiesBlocks.blaze_bud, SkiesBlocks.blaze_bud, SkiesBlocks.blaze_bud, SkiesItems.fiery_beans),
                        List.of(
                           SkiesItems.cooked_monitor_tail,
                           SkiesItems.cooked_carabeef,
                           SkiesItems.cooked_venison,
                           SkiesItems.cooked_municipal_monkfish,
                           Items.COOKED_MUTTON
                        )
                     );
                     replace(offer, Items.RABBIT_STEW, FoodPrepUtils.createFoodFromItems((List<ItemLike>)Util.getRandom(recipes, villager.getRandom())), 2);
                     replace(offer, Items.COOKED_CHICKEN, SkiesItems.cooked_monitor_tail);
                     replace(offer, Items.PORKCHOP, SkiesItems.cooked_venison);
                     replace(offer, Items.BEEF, SkiesItems.carabeef);
                     replace(offer, Items.SWEET_BERRIES, SkiesItems.brewberry);
                  } else if (prof == VillagerProfession.SHEPHERD) {
                     replace(offer, Items.SHEARS, SkiesItems.ventium_shears);
                  } else if (prof == VillagerProfession.ARMORER) {
                     replace(offer, Items.COAL, (ItemLike)(everbright ? SkiesBlocks.snowcap_mushroom : SkiesBlocks.sunstone_crystal));
                     replace(offer, Items.IRON_INGOT, everbright ? SkiesItems.ventium_ingot : SkiesItems.horizonite_ingot);
                     replace(offer, Items.DIAMOND, SkiesItems.diopside_gem);
                     replace(offer, Items.LAVA_BUCKET, SkiesItems.ventium_lava_bucket);
                     replace(offer, Items.IRON_HELMET, SkiesItems.aquite_helmet);
                     replace(offer, Items.IRON_CHESTPLATE, SkiesItems.aquite_chestplate);
                     replace(offer, Items.IRON_LEGGINGS, SkiesItems.aquite_leggings);
                     replace(offer, Items.IRON_BOOTS, SkiesItems.aquite_boots);
                     replace(offer, Items.SHIELD, SkiesItems.moonstone_shield);
                     replace(offer, Items.DIAMOND_HELMET, SkiesItems.diopside_helmet);
                     replace(offer, Items.DIAMOND_CHESTPLATE, SkiesItems.diopside_chestplate);
                     replace(offer, Items.DIAMOND_LEGGINGS, SkiesItems.diopside_leggings);
                     replace(offer, Items.DIAMOND_BOOTS, SkiesItems.diopside_boots);
                  } else if (prof == VillagerProfession.WEAPONSMITH) {
                     replace(offer, Items.COAL, (ItemLike)(everbright ? SkiesBlocks.snowcap_mushroom : SkiesBlocks.sunstone_crystal));
                     replace(offer, Items.IRON_INGOT, SkiesItems.aquite);
                     replace(offer, Items.DIAMOND, SkiesItems.charoite);
                     replace(offer, Items.IRON_SWORD, ToolUtils.setStickType(new ItemStack(SkiesItems.aquite_sword), stick));
                     replace(offer, Items.IRON_AXE, ToolUtils.setStickType(new ItemStack(SkiesItems.aquite_axe), stick));
                     replace(offer, Items.DIAMOND_SWORD, ToolUtils.setStickType(new ItemStack(SkiesItems.charoite_sword), stick));
                     replace(offer, Items.DIAMOND_AXE, ToolUtils.setStickType(new ItemStack(SkiesItems.charoite_axe), stick));
                  } else if (prof == VillagerProfession.TOOLSMITH) {
                     replace(offer, Items.COAL, (ItemLike)(everbright ? SkiesBlocks.snowcap_mushroom : SkiesBlocks.sunstone_crystal));
                     replace(offer, Items.IRON_INGOT, SkiesItems.aquite);
                     replace(offer, Items.DIAMOND, SkiesItems.diopside_gem);
                     replace(
                        offer,
                        Items.STONE_SWORD,
                        ToolUtils.setStickType(new ItemStack(everbright ? SkiesItems.turquoise_stone_sword : SkiesItems.lunar_stone_sword), strongStick)
                     );
                     replace(
                        offer,
                        Items.STONE_PICKAXE,
                        ToolUtils.setStickType(new ItemStack(everbright ? SkiesItems.turquoise_stone_pickaxe : SkiesItems.lunar_stone_pickaxe), strongStick)
                     );
                     replace(
                        offer,
                        Items.STONE_AXE,
                        ToolUtils.setStickType(new ItemStack(everbright ? SkiesItems.turquoise_stone_axe : SkiesItems.lunar_stone_axe), strongStick)
                     );
                     replace(
                        offer,
                        Items.STONE_SHOVEL,
                        ToolUtils.setStickType(new ItemStack(everbright ? SkiesItems.turquoise_stone_shovel : SkiesItems.lunar_stone_shovel), strongStick)
                     );
                     replace(
                        offer,
                        Items.STONE_HOE,
                        ToolUtils.setStickType(new ItemStack(everbright ? SkiesItems.turquoise_stone_hoe : SkiesItems.lunar_stone_hoe), strongStick)
                     );
                     replace(offer, Items.IRON_AXE, ToolUtils.setStickType(new ItemStack(SkiesItems.aquite_axe), strongStick));
                     replace(offer, Items.IRON_SHOVEL, ToolUtils.setStickType(new ItemStack(SkiesItems.aquite_shovel), strongStick));
                     replace(offer, Items.IRON_PICKAXE, ToolUtils.setStickType(new ItemStack(SkiesItems.aquite_pickaxe), strongStick));
                     replace(offer, Items.DIAMOND_HOE, ToolUtils.setStickType(new ItemStack(SkiesItems.diopside_hoe), strongStick));
                     replace(offer, Items.DIAMOND_AXE, ToolUtils.setStickType(new ItemStack(SkiesItems.diopside_axe), strongStick));
                     replace(offer, Items.DIAMOND_SHOVEL, ToolUtils.setStickType(new ItemStack(SkiesItems.diopside_shovel), strongStick));
                     replace(offer, Items.DIAMOND_PICKAXE, ToolUtils.setStickType(new ItemStack(SkiesItems.diopside_pickaxe), strongStick));
                  }
               }
            );
      }
   }

   private static void replace(MerchantOffer offer, ItemLike oldItem, ItemLike newItem) {
      replace(offer, oldItem.asItem(), new ItemStack(newItem), 0);
   }

   private static void replace(MerchantOffer offer, ItemLike oldItem, ItemLike newItem, int aCostMod) {
      replace(offer, oldItem.asItem(), new ItemStack(newItem), aCostMod);
   }

   private static void replace(MerchantOffer offer, ItemLike oldItem, ItemStack newItem) {
      replace(offer, oldItem.asItem(), newItem, 0);
   }

   private static void replace(MerchantOffer offer, ItemLike oldItem, ItemStack newItem, int aCostMod) {
      ItemStack costA = offer.getCostA();
      if (!costA.isEmpty()) {
         if (costA.getItem() == oldItem.asItem()) {
            newItem.setCount(costA.getCount() + aCostMod);
            offer.baseCostA = newItem;
         } else if (aCostMod > 0) {
            offer.baseCostA.setCount(costA.getCount() + aCostMod);
         }
      }

      ItemStack costB = offer.getCostB();
      if (!costB.isEmpty() && costB.getItem() == oldItem.asItem()) {
         newItem.setCount(costB.getCount());
         offer.costB = newItem;
      }

      ItemStack result = offer.getResult();
      if (!result.isEmpty() && result.getItem() == oldItem.asItem()) {
         newItem.setCount(result.getCount());
         EnchantmentHelper.getEnchantments(result).forEach(newItem::enchant);
         offer.result = newItem;
      }
   }
}
